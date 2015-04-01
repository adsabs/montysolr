/**
 * This is the core of the ORCID implementation
 * Written by (rca) - totally re-implemented the
 * initial implementation.
 *
 * The important details are:
 *
 *  - all communication with ORCID happens in JSON
 *  - there are multiple access points
 *    addWorks()
 *    setWorks()
 *    deleteWorks()
 *
 *    But in reality, the ORCID API allows three
 *    operations:
 *
 *      reading (GET)
 *      adding (POST)
 *      re-writing (PUT)
 *
 * This module will be contacting a web-service, such
 * a service needs to allows CORS requests (since we
 * run inside a browser)
 *
 * Our implementation of the orcid-proxy can be found
 * at:  http://github.com/adsabs/orcid-service
 *
 * The important configuration details are configured
 * in the ./module.js (the module will actually create
 * OrcidApi and insert it into the beehive)
 *
 *
 * TODO:
 *  - error handling (discover more error situations and
 *    take care of them; such as duplicated put-codes)
 *  - keep track of write operations and cache ORCID
 *    profile (to avoid refetching it every time)
 *  - provide a more frindly api to query a status of
 *    a document
 *  - wrap write operations into throttling mode? (it is
 *    more efficient to do updates in bulk)
 *
 */

define([
    'underscore',
    'bootstrap',
    'jquery',
    'backbone',
    'js/components/generic_module',
    'js/mixins/dependon',
    'js/components/pubsub_events',
    'js/mixins/link_generator_mixin',
    'js/components/api_query',
    'js/components/api_request',
    'js/mixins/hardened',
    'js/components/api_targets'
  ],
  function (
    _,
    Bootstrap,
    $,
    Backbone,
    GenericModule,
    Mixins,
    PubSubEvents,
    LinkGeneratorMixin,
    ApiQuery,
    ApiRequest,
    HardenedMixin,
    ApiTargets
    ) {



    var OrcidApi = GenericModule.extend({

      initialize: function() {
        this.orcidProxyUri = '';
        this.userData = {};
        this.authData = null;
        this.db = {};
        this.profile = {};
        this.checkInterval = 3600 * 1000;
        this.virgin = true;
        this.maxQuerySize = 512;
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.pubsub = this.getBeeHive().getService('PubSub').getHardenedInstance();

        var config = beehive.getObject('DynamicConfig');
        this.config = {};
        _.extend(this.config, config['Orcid']);

        var storage = beehive.getService('PersistentStorage');
        if (storage) {
          var orcid = storage.get('Orcid');

          if (orcid && orcid.authData) {
            this.saveAccessData(orcid.authData);
          }
        }
      },


      /**
       * Checks access to ORCID api by making request for a user profile
       * returns a promise; done() means success, fail() no access
       * @param authData
       */
      checkAccessOrcidApiAccess: function(authData) {
        authData = authData || this.authData;
        //check the access_token works (by loading profile)
        var ret = $.Deferred();
        this.sendData(this.config.apiEndpoint + '/' + authData.orcid + '/orcid-profile',
          null,
          {
            fail: function() {
              ret.reject();
            },
            done: function(res) {
              ret.resolve(res);
            },
            headers: {
              "Orcid-Authorization": "Bearer " + authData.access_token
            }
          });
        return ret.promise();
      },

      hasAccess: function() {
        if (this.authData) {
          if (this.authData.expires && this.authData.expires <= new Date().getTime()) {
            return false;
          }
          return true;
        }
        return false;
      },

      /**
       * Redirects to ORCID where the user logs in and ORCID will forward
       * user back to us
       */
      signIn: function (targetRoute) {
        this.pubsub.publish(this.pubsub.APP_EXIT, {
          type: 'orcid',
          url: this.config.loginUrl
            + "&redirect_uri=" + encodeURIComponent(this.config.redirectUrlBase +
            (targetRoute || '/#/user/orcid'))
        });
      },

      /**
       * Forgets the OAuth access_token
       */
      signOut: function () {
        this.saveAccessData(null);
      },

      hasExchangeCode: function (searchString) {
        var code = this.getUrlParameter('code', searchString || window.location.search);
        if (code)
          return true;
      },

      getExchangeCode: function (searchString) {
        return this.getUrlParameter('code', searchString || window.location.search);
      },

      /**
       * Extract values from the URL (used to get code from redirects)
       *
       * @param sParam
       * @returns {string}
       */
      getUrlParameter: function (sParam, searchString) {
        var sPageURL = searchString.substring(1);
        var sURLVariables = sPageURL.split('&');
        for (var i = 0; i < sURLVariables.length; i++) {
          var sParameterName = sURLVariables[i].split('=');
          if (sParameterName[0] == sParam) {
            return decodeURIComponent(sParameterName[1]);
          }
        }
      },

      /**
       * Using access code retrieve the access_token from ORCID
       *
       * Example response:
       *  {"access_token":"4274a0f1-36a1-4152-9a6b-4246f166bafe","token_type":"bearer","ex
       *  pires_in":3599,"scope":"/orcid-works/create /orcid-profile/read-limited /orcid-w
       *  orks/update","orcid":"0000-0001-8178-9506","name":"Roman Chyla"}
       *
       * @param oAuthCode
       * @returns {*}
       */
      getAccessData: function (oAuthCode) {
        var api = this.getBeeHive().getService('Api');
        var promise = $.Deferred();
        var r = api.request(
          new ApiRequest({target: this.config.exchangeTokenUrl, query: new ApiQuery({code: oAuthCode})}),
          {
            url: this.config.exchangeTokenUrl,
            done: function (data, textStatus, jqXHR) {
              promise.resolve(data);
            },
            headers: {
              Accept: 'application/json',
              Authorization: api.access_token
            }
          }
        );
        r.fail(function (jqXHR, textStatus, errorThrown) {
          promise.reject(jqXHR);
        });
        return promise.promise();
      },

      saveAccessData: function(authData) {
        var beehive = this.getBeeHive();

        if (authData && !authData.expires && authData.expires_in) {
          authData.expires = new Date().getTime() + ((authData.expires_in * 1000) - 1000);
        }
        this.authData = authData;
        var storage = beehive.getService('PersistentStorage');
        if (storage) {
          var orcid = storage.get('Orcid') || {};
          orcid.authData = authData;
          storage.set('Orcid', orcid);
        }
      },

      checkAccess: function() {
        if (!this.hasAccess())
          throw new Error('You must first obtain access_token before calling this API');
        if (!this.config.apiEndpoint)
          throw new Error('OrcidApi was not properly configured; apiEndpoint is missing');
      },


      /**
       * ===============================================================================
       * API ORCID access points
       * ===============================================================================
       */

      /**
       * Retrieve user profile
       *
       *  curl -H 'Authorization: Bearer bd25a79d-eb7d-4341-aab9-c17e17f5fd21' 'http://api.sandbox.orcid.org/v1.2/0000-0001-8178-9506/orcid-profile' -L -i -H 'Accept: application/json'
       *
       * Must have scope: /orcid-profile/read-limited
       *
       * @returns {*}
       */
      getUserProfile: function () {
        this.checkAccess();
        var ret = $.Deferred();
        this.sendData(this.config.apiEndpoint + '/' + this.authData.orcid + '/orcid-profile')
          .done(function(res) {
            ret.resolve(res['orcid-profile']);
          });
        return ret.promise();
      },


      /**
       * Retrieve User's documents
       *
       *  curl -H 'Authorization: Bearer bd25a79d-eb7d-4341-aab9-c17e17f5fd21' 'http://api.sandbox.orcid.org/v1.2/0000-0001-8178-9506/orcid-works' -L -i -H 'Accept: application/json'
       *
       * Must have scope: /orcid-works/....
       */
      getWorks: function() {
        this.checkAccess();
        var ret = $.Deferred();
        this.sendData(this.config.apiEndpoint + '/' + this.authData.orcid + '/orcid-works')
          .done(function(res) {
            ret.resolve(res['orcid-profile']['orcid-activities'] ? res['orcid-profile']['orcid-activities']['orcid-works'] : {});
          });
        return ret.promise();
      },


      /**
       * From the ORCID response extract all external IDs
       *
       * @param orcidWorks
       * @returns {*}
       */
      getExternalIds: function (orcidWorks) {
        var ret = {};
        if (!orcidWorks) return ret;

        if (orcidWorks && orcidWorks['orcid-work']) {
          orcidWorks = orcidWorks['orcid-work']
        }
        else if(!_.isArray(orcidWorks)) {
          orcidWorks = [orcidWorks];
        }
        _.each(orcidWorks, function(w, idx) {
          if (w['work-external-identifiers'] && w['work-external-identifiers']['work-external-identifier']) {
            _.each(w['work-external-identifiers']['work-external-identifier'], function(el) {
              ret[el['work-external-identifier-id']['value']] = {idx: idx, type: el['work-external-identifier-type'], "put-code": w['put-code']};
            });
          }
        });
        return ret;
      },

      /**
       * Orcid stores application id inside the source field when the OAuth app
       * created the record. We can identify resources by matching the ID.
       *
       * "source": {
       *       "source-client-id": {
       *         "path": "APP-P5ANJTQRRTMA6GXZ",
       *         "host": "sandbox.orcid.org",
       *         "uri": "http://sandbox.orcid.org/client/APP-P5ANJTQRRTMA6GXZ",
       *         "value": null
       *       },
       *       "source-name": {
       *         "value": "nasa ads"
       *       },
       *       "source-date": {
       *         "value": 1424194783005
       *       }
       *     },
       * @param orcidWork
       * @returns {boolean}
       */
      isWorkCreatedByUs: function (orcidWork) {
        if (orcidWork['source']
          && orcidWork['source']['source-client-id']
          && orcidWork['source']['source-client-id']['path']
          && orcidWork['source']['source-client-id']['path'] == this.config.clientId) {
          return true;
        }
        return false;
      },

      /**
       *  Formats the datastructure of one paper - to be sent to the Orcid API
       *
       * @param adsWork
       * @param putCode
       * @returns {{work-title: {$: {}, title: *}, short-description: (.response.abstract|*|ItemModel.defaults.abstract|AbstractModel.defaults.abstract|AbstractModel.parse.abstract|responseWithHighlights.highlighting.abstract), work-external-identifiers: *[], work-type: string, work-contributors, url: *}}
       */
      formatOrcidWork: function (adsWork, putCode) {
        var self = this;
        var formatContributors = function (adsAuthors) {
          var result = [];
          _.each(adsAuthors, function (author) {
            result.push({
              "credit-name": author,
              "contributor-attributes": {
                "contributor-role": "AUTHOR"
              }
            });
          });
          return {
            contributor: result
          };
        };

        var out = {
          "work-type": self._getOrcidWorkType(adsWork),
          "url": adsWork.doi
            ? LinkGeneratorMixin.adsUrlRedirect("doi", adsWork.doi)
            : LinkGeneratorMixin.adsUrlRedirect("article", adsWork.bibcode) // TODO : in item_view model DOI is missing
        };
        var ids = ['bibcode', 'id'];
        _.each(ids, function(fldName) {
          if (adsWork[fldName]) {
            if (!out["work-external-identifiers"]) out["work-external-identifiers"] = {"work-external-identifier": []};

            if (_.isArray(adsWork[fldName])) {
              _.each(adsWork[fldName], function(value) {
                out["work-external-identifiers"]['work-external-identifier'].push({
                    "work-external-identifier-type": self._getOrcidIdentifierType(fldName),
                    "work-external-identifier-id": {
                      value: value
                    }
                  }
                );
              })
            }
            else {
              out["work-external-identifiers"]['work-external-identifier'].push({
                  "work-external-identifier-type": self._getOrcidIdentifierType(fldName),
                  "work-external-identifier-id": {
                    value: adsWork[fldName]
                  }
                }
              );
            }
          }
        });

        if (adsWork.title) {
          out["work-title"] = {
            "title": _.isArray(adsWork.title) ? adsWork.title.join(' ') : adsWork.title
          };
        }
        if (adsWork.abstract) {
          out["short-description"] = adsWork.abstract;
        }
        if (adsWork.author) {
          out["work-contributors"] = formatContributors(adsWork.author);
        }

        if (adsWork.pubdate && adsWork.pubdate.length > 0 && adsWork.pubdate.indexOf(' ') > 0) {
          out['publication-date'] = {
            "year": adsWork.pubdate.split(' ')[1]
          }
        }

        if (putCode) {
          out["put-code"] = putCode;
        }
        return out;
      },

      /**
       * Get the appropriate type, accepted values:
       * http://support.orcid.org/knowledgebase/articles/118795-supported-work-types
       */
      _getOrcidWorkType: function(adsWork) {
        //TODO: rca enhance this
        return 'JOURNAL_ARTICLE'
      },

      _getOrcidIdentifierType: function(fldName) {
        var f = fldName.toLowerCase();
        switch (f) {
          case 'doi':
            return 'DOI';
          case 'bibcode':
            return 'BIBCODE';
          default:
            return 'OTHER_ID';
        }
      },

      /**
       * Original method as developed by blocshop
       *
       * It formats a list of papers into orcid-message
       *
       * @param adsData
       * @returns {{orcid-message: {$: {xmlns: string}, message-version: string, orcid-profile: {orcid-activities: {$: {}, orcid-works}}}}}
       */
      formatOrcidWorks: function (adsData) {

        if (!_.isArray(adsData))
          throw new Exception('Input to formatOrcidWorks must be an array of objects');

        var self = this;
        var result = [];
        _.each(adsData, function (adsWork) {
            result.push(self.formatOrcidWork(adsWork));
          }
        );
        return this._createOrcidMessage(result);
      },

      _createOrcidMessage: function(orcidWorks) {
        return {
          "message-version": "1.2",
          "orcid-profile": {
            "orcid-activities": {
              "orcid-works": {
                'orcid-work': orcidWorks
              }
            }
          }
        };
      },

      /**
       * Posts (appends) new papers to Orcid;
       *
       * @param adsRecords
       * @returns {*}
       */
      addWorks: function (adsRecs) {
        this.dirty = true; // will need to synchronize
        return this.sendData(this.config.apiEndpoint + '/' + this.authData.orcid + '/orcid-works',
          this.formatOrcidWorks(adsRecs),
          {
            type: "POST"
          });
      },

      /**
       * Updates the profile - by removing works from the profile, and re-adding
       * them using new call to formatOrcidWork
       *
       * @param identifiers
       * @returns {*}
       */
      updateWorks: function(adsRecs) {
        return this.deleteWorks(this._extractIdentifiers(adsRecs), adsRecs);
      },

      _extractIdentifiers: function(adsRecs) {
        var ids = [];
        _.each(adsRecs, function(r) {
          if (r.bibcode) ids.push(r.bibcode);
          if (r.doi) ids.push(r.doi);
          if (r.alternate_bibcode) {
            for (var bb in r.alternate_bibcode) {
              ids.push(bb);
            }
          }
        });
        return ids;
      },

      /**
       * Deletes certain records from Orcid database. This operation is three-step
       *
       *  1. retrieve works from the user profile
       *  2. filter them and remove the un-wanted put-codes
       *  3. update the profile (sending the new datastructure)
       *
       *
       * @param identifiers
       * @returns {*}
       */
      deleteWorks: function (identifiers, recsToAdd) {
        var self = this;
        var deferred = $.Deferred();

        this.getWorks()
          .done(function (orcidWorks) {

            // Exclude works not coming from ADS (since we can only modify
            // records created by us)
            var adsWorks = orcidWorks["orcid-work"].filter(function (orcidWork) {
              return self.isWorkCreatedByUs(orcidWork);
            });

            if (adsWorks.length == 0) {
              deferred.resolve({
                deleted: 0,
                added: 0,
                msg: 'No ADS records found',
                totalRecs: orcidWorks["orcid-work"].length,
                adsTotal: 0
              });
              return;
            }

            var toRemove = [];
            var extIds = self.getExternalIds(adsWorks);
            _.each(identifiers, function(id) {
              if (extIds[id]) {
                toRemove.push(extIds[id].idx);
              }
            });

            if (toRemove.length == 0) {
              deferred.resolve({
                deleted: 0,
                added: 0,
                msg: 'No ADS rec found that could be deleted',
                totalRecs: orcidWorks["orcid-work"].length,
                adsTotal: adsWorks.length
              });
              return;
            }

            var newWorks = [];
            _.each(adsWorks, function (work, idx, l) {
              if (toRemove.indexOf(idx) == -1)
                newWorks.push(work);
            });

            if (recsToAdd) {
              _.each(recsToAdd, function(rec) {
                newWorks.push(self.formatOrcidWork(rec));
              })
            }

            var report = {
              deleted: toRemove.length,
              added: 0,
              msg: 'Attempting removal',
              adsTotal: newWorks.length,
              totalRecs: orcidWorks["orcid-work"].length
            };

            self.sendData(self.config.apiEndpoint + '/' + self.authData.orcid + '/orcid-works',
              self._createOrcidMessage(newWorks),
              {type: "PUT"}
              )
              .done(function (res) {
                self.dirty = true; // will need to synchronize
                report['response'] = res;
                deferred.resolve(report);
              });
          })
          .fail(function (err) {
            deferred.reject({msg: 'Error getting list of Orcid records (we cant delete anything)'});
          });

        return deferred.promise();

      },


      /**
       * Replaces orcid-works with a new set of works (PUT operation)
       *
       * @param adsRecs
       * @returns {*}
       */
      setWorks: function (adsRecs, skipFormatting) {
        return this.sendData(this.config.apiEndpoint + '/' + this.authData.orcid + '/orcid-works',
          skipFormatting ? adsRecs : this.formatOrcidWorks(adsRecs),
          {type: "PUT"}
        );
      },

      /**
       * Use Api service to make a request to the ORCID api
       *
       * @param url - remote endpoint
       * @param data - JSON object
       * @param opts
       * @returns {*}
       */
      sendData: function (url, data, opts) {

        var result = $.Deferred();
        opts = opts || {};

        var options = {
          type: 'GET',
          url: url,
          cache: false,
          done: function(data) {
            result.resolve(data);
          }
        };

        if (data) {
          options.dataType = 'json';
          options.data = JSON.stringify(data);

          // because ORCID sends empty response for POST requests
          // we must be able to handle it properly (but it is not
          // nice of them)
          options.converters = {
            "* text": window.String,
            "text html": true,
            "text json": function(input) {input = input || '{}'; return $.parseJSON(input)},
            "text xml": $.parseXML
          }
        }
        else {
          options.data = null; // to prevent api.request() from adding {} to the url params
        }

        _.extend(options, opts);

        var api = this.getBeeHive().getService('Api');

        if (!options.headers)
          options.headers = {};

        options.headers.Authorization = api.access_token;
        if (!options.headers["Orcid-Authorization"] && this.authData)
          options.headers["Orcid-Authorization"] = "Bearer " + this.authData.access_token;
        if (!options.headers["Content-Type"])
          options.headers["Content-Type"] = "application/json";
        if (!options.headers["Accept"])
          options.headers["Accept"] = "application/json";


        api.request(new ApiRequest({target: url, query: new ApiQuery(), options: options}));
        return result.promise();
      },

      /**
       * Queries ADS Api using orcid identifiers; returns a map that maps
       * known records, ie
       *    {
       *      'doi:1234.5566': '2015aas...34.4',
       *      'bibcode:2015aas...34.4': '2015aas...34.4'
       *    }
       * @param apiQuery
       * @returns {*}
       * @private
       */
      _checkOrcidIdsInAds: function(apiQuery) {
        var api = this.getBeeHive().getService('Api');
        var defer = new $.Deferred();

        if (!api) {
          var p = defer.promise();
          defer.resolve({});
          return p;
        }

        apiQuery.set('fl', 'bibcode,doi,alternate_bibcode');
        apiQuery.set('rows', '5000'); // unrealistic, but that's fine

        api.request(new ApiRequest({target: ApiTargets.SEARCH, query: apiQuery, options: {
          done: function(data, textStatus, jqXHR) {
            var ret = {}, key, value, bibcode;
            if (!data || !data.response || !data.response.docs)
              return defer.resolve(ret);

            _.each(data.response.docs, function(doc) {
              bibcode = doc.bibcode.toLowerCase();
              key = 'bibcode:' + bibcode;
              ret[key] = bibcode;
              _.each(doc.doi, function(doi) {
                ret['doi:' + doi.toLowerCase().replace('doi:', '')] = bibcode;
              });
              _.each(doc.alternate_bibcode, function(ab) {
                ret['bibcode:' + ab.toLowerCase()] = bibcode;
              });
            });
            defer.resolve(ret);
          },
          fail: function() {
            defer.resolve({});
          }
        }}));

        return defer.promise();
      },


      /**
       * Returns a promise with information about an ORCID document, it accepts an object with
       *  - bibcode
       *  - doi
       *  - id
       *  - alternate_bibcode
       *
       * It will automatically update orcid-database if necessary
       *
       * @param data
       * @returns Promise
       *  {{isCreatedByUs: boolean, isCreatedByOthers: boolean, provenance: null}}
       */
      getRecordInfo: function(data) {

        if (!data) {
          throw new Error("Empty input");
        }
        var result = $.Deferred();
        var self = this;

        if (this.needsUpdate() || this.pending) {
          this.updateDatabase()
            .done(function() {
              result.resolve(self._getRecInfo(data));
            })
        }
        else {
          result.resolve(self._getRecInfo(data));
        }

        return result.promise();
      },

      _getRecInfo: function(data) {
        var out = {
          isCreatedByUs: false,
          isCreatedByOthers: false,
          provenance: null
        };
        var self = this;
        _.each(data, function(value, key, obj) {
          if (_.isArray(value)) {
            for (var v in value) {
              self._updateRec(key + ':' + v, out)
            }
          }
          else {
            self._updateRec(key + ':' + value, out);
          }
        });
        return out;
      },

      _updateRec: function(k, out) {
        k = k.toLowerCase();
        if (this.db[k]) {
          if (this.db[k].isAds) {
            out.isCreatedByUs = true;
          }
          else {
            out.isCreatedByOthers = true;
          }
          out.putcode = this.db[k].putcode;
        }
      },

      /**
       * Updates our knowledge about ORCID by
       *  - fetching fresh profile (unless one is supplied)
       *  - extracting info into fast-lookup format
       */
      updateDatabase: function(profile) {

        if (profile && profile['orcid-profile'])
          profile = profile['orcid-profile'];
        
        var self = this;

        var whenDone = $.Deferred();
        if (self.pending) { // update is already running
          var tme = setInterval(function() {
            if (self.pending == false) {
              whenDone.resolve();
              clearInterval(tme);
            }
          }, 200);
          setTimeout(function() {
            clearInterval(tme);
          }, 3000);
          return whenDone.promise();
        }

        self.pending = true;
        self.virgin = false;

        var defer;
        if (profile && profile.orcid) {
          defer = $.Deferred();
          defer.resolve(profile);
        }
        else {
          defer = this.getUserProfile();
        }

        defer.done(function(profile) {

          self.dirty = false;

          var works = profile['orcid-activities'] ? profile['orcid-activities']['orcid-works'] : [];
          var ids = self.getExternalIds(works);
          var db = {}, key, isOurs, query = [], field, fValue;
          _.each(ids, function(value, key, obj) {
            field = value.type.toLowerCase().trim();
            fValue = key.toLowerCase().trim();
            key =  field + ':' + fValue;

            query.push(key);
            if (field == 'bibcode') {
              query.push('alternate_bibcode:' + fValue);
            }

            isOurs = self.isWorkCreatedByUs(works['orcid-work'][value.idx]);
            //TODO:rca - what if there are mutliple ids, one created by us, one by others?
            db[key] = {isAds: isOurs, idx: value.idx, putcode: value['put-code']};
          });

          // query our API to find out which records ADS has
          if (query.length && self.maxQuerySize > 0) {
            var whereClauses = [];
            query = query.sort();
            var steps = _.range(0, query.length, self.maxQuerySize);
            var start, end;
            for(var i=0; i<steps.length; i++) {
              var q = {};
              for (var j=steps[i]; j<steps[i]+self.maxQuerySize; j++) {
                if (j >= query.length) break;
                var ps = query[j].split(':');
                if (!q[ps[0]])
                  q[ps[0]] = [];
                q[ps[0]].push(ps[1]);
              }
              whereClauses.push(self._checkOrcidIdsInAds(self._buildQuery(q)));
            }

            $.when.apply($, whereClauses)
              .then(function() {
                _.each(arguments, function (adsResponse, idx) {
                  _.each(adsResponse, function(bibcode, key) {
                    var orcidRec = db['bibcode:' + bibcode];
                    if (!orcidRec) {
                      orcidRec = {idx: -1, putcode: -1}; // create an empty rec
                    }
                    if (orcidRec.idx == -1 && db[key] && db[key].idx > -1) {
                      _.extend(orcidRec, db[key]); // update empty rec with real data
                    }
                    db[key] = orcidRec;
                  });
                });

                self.db = db;
                self.profile = profile;
                self.pending = false;
                whenDone.resolve();
              }, function() {
                console.error('Error processing response from ADS', arguments);
                self.db = db;
                self.profile = profile;
                self.pending = false;
                whenDone.resolve();
              });
          }
          else {
            self.db = db;
            self.profile = profile;
            self.pending = false;
            whenDone.resolve();
          }
          })
          .fail(function() {
            whenDone.reject(arguments);
          });
        return whenDone;
      },

      _buildQuery: function(query) {
        var parts = _.map(query, function(values, field) {
          if (values.length == 0)
            return '';
          return field + ':(' + values.join(' OR ') + ')';
        }).filter(function(x) {return x.length});
        return new ApiQuery({'q': parts.join(' OR ')});
      },

      /**
       * Returns true when the OrcidApi needs to update its own database (synchronize
       * with the remote API); this happens either because we have updated something
       * or when a predefined time-interval passed
       */
      needsUpdate: function() {

        if (this.virgin)
          return true;

        if (!this.dirtyThrottle) {
          this.dirtyThrottle = _.throttle(function(orcidApi) {
            orcidApi.checkAccessOrcidApiAccess()
              .done(function(profile) {
                orcidApi.updateDatabase(profile);
              })
              .fail(function() {
                console.log('We have lost access to ORCID Api (maybe the user revoked the token?)');
                orcidApi.authData = {};
              })

          }, this.checkInterval || (3600 * 1000), this); // check every hour (or sooner?)
        }
        this.dirtyThrottle(this);

        return this.dirty;
      },

      /**
       * Updates ORCID - this method is made available to widgets
       */
      updateOrcid: function(action, adsDoc) {
        if (!_.isObject(adsDoc)) throw new Error('You are supposed to send simple object');

        var result = $.Deferred();
        var self = this;

        if (action == 'update') {
          this.updateWorks([adsDoc])
            .done(function(resp) {
              self.getRecordInfo(adsDoc)
                .done(function(recInfo) {
                  result.resolve(recInfo);
                });
            })
        }
        else if (action == 'delete') {
          this.deleteWorks(this._extractIdentifiers([adsDoc]))
            .done(function(resp) {
              self.getRecordInfo(adsDoc)
                .done(function(recInfo) {
                  result.resolve(recInfo);
                });
            })
        }
        else if (action == 'add') {
          var recInfo = this.getRecordInfo(adsDoc);
          if (!recInfo.isCreatedByUs && !recInfo.isCreatedByOthers) {
            this.addWorks([adsDoc])
              .done(function() {
                recInfo.isCreatedByUs = true;
                result.resolve(recInfo);
              })
          }
          else {
            return this.updateOrcid('update', adsDoc); // not safe to just add
          }
        }
        else {
          throw new Error('Unknown action: ' + action);
        }

        return result.promise();
      },

      /**
       * Transfroms ORCID profile into ADS format (ApiResponse) which is easy to
       * ingest for the widgets
       *
       * @param orcidProfile
       */
      transformOrcidProfile: function(orcidProfile) {
        var docs = [];

        if (!orcidProfile['orcid-activities'] || !orcidProfile['orcid-activities']['orcid-works'] || !orcidProfile['orcid-activities']['orcid-works']['orcid-work'])
          return docs;

        var works = orcidProfile['orcid-activities']['orcid-works']['orcid-work'];
        var orcidId = orcidProfile['orcid-identifier']['path'];

        function extr(el) {
          if (!el) return null;
          if (el.value)
            return el.value;
          return el;
        }

        function formatDate(el) {
          if (!el) return null;
          var yr = extr(el['year']) || '????';
          var mn = extr(el['month']) || '??';
          return yr + '/' + mn;
        }

        function pickIdentifier(d, ids) {
          if (d.bibcode) return d.bibcode;
          if (d.doi) return d.doi;
          if (ids.length) return ids[0];
          return 'unknown';
        }

        function extractAuthors(el) {
          if (!el) return [];
          var res = [];
          if (el.contributor) {
            _.each(el.contributor, function(x) {
              if (x['credit-name'])
                res.push(extr(x['credit-name']));
            });
          }
          return res;
        }

        var self = this;
        _.each(works, function(w) {
          var d = {};
          var ids = self.getExternalIds(w);
          _.each(ids, function(value, key, obj) {
            d[value.type.toLowerCase()] = key;
          });
          d['putcode'] = extr(w['put-code']);
          d['title'] = extr(w['work-title'] ? w['work-title']['title'] : null);
          d['visibility'] = extr(w['visibility']);
          d['formattedDate'] = formatDate(w['publication-date']);
          d['pub'] = extr(w['journal-title']);
          d['abstract'] = extr(w['short-description']);
          d.author = extractAuthors(w["work-contributors"]);
          d.identifier = pickIdentifier(d, ids);
          //d.orcid = self.getRecordInfo(d);
          docs.push(d);
        });

        return {
          responseHeader: {
            params: {
              orcid: orcidId
            }
          },
          response:{
            numFound:docs.length,
            start: 0,
            docs: docs
          }
        }

      },

      getOrcidProfileInAdsFormat: function() {
        var d = $.Deferred();
        var self = this;
        this.getUserProfile()
          .done(function(profile) {
            self.updateDatabase(profile)
              .done(function() {
                d.resolve(self.transformOrcidProfile(profile));
              });
          });
        return d.promise();
      },

      hardenedInterface: {
        hasAccess: 'boolean indicating access to ORCID Api',
        getUserProfile : 'get user profile',
        signIn: 'login',
        signOut: 'logout',
        getRecordInfo: 'provides info about a document',
        updateOrcid: 'the main access point for widgets',
        getOrcidProfileInAdsFormat: 'retrieves the Orcid profile in ADS format'
      }
    });

    _.extend(OrcidApi.prototype, Mixins.BeeHive);
    _.extend(OrcidApi.prototype, HardenedMixin);

    return OrcidApi;
  });
