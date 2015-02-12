define([
    'underscore',
    'bootstrap',
    'jquery',
    'xml2json',
    'backbone',
    'js/components/generic_module',
    'js/mixins/dependon',
    'js/mixins/string_utils',
    'js/modules/orcid/orcid_api_constants',
    'js/components/pubsub_events',
    'js/mixins/link_generator_mixin',
    'js/modules/orcid/json2xml',
    'js/components/api_query',
    'js/components/api_request'
  ],
  function (
    _,
    Bootstrap,
    $,
    Xml2json,
    Backbone,
    GenericModule,
    Mixins,
    StringUtils,
    OrcidApiConstants,
    PubSubEvents,
    LinkGeneratorMixin,
    Json2Xml,
    ApiQuery,
    ApiRequest
    ) {



    var OrcidApi = GenericModule.extend({

      initialize: function() {
        this.orcidProxyUri = '';
        this.userData = {};
        this.authData = null;
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
            //TODO:rca - check the access_token works (by loading profile)
            this.saveAccessData(orcid.authData);
          }
        }
      },


      hasAccess: function() {
        if (this.authData)
          return true;
        return false;
      },

      /**
       * Redirects to ORCID where the user logs in and ORCID will forward
       * user back to us
       */
      signIn: function () {
        this.pubsub.publish(this.pubsub.APP_EXIT, {url: this.config.loginUrl});
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
        api.request(
          new ApiRequest({target: this.config.exchangeTokenUrl, query: new ApiQuery({code: oAuthCode})}),
          {
            url: this.config.exchangeTokenUrl,
            done: function (data, textStatus, jqXHR) {
              promise.resolve(data);
            },
            fail: function (jqXHR, textStatus, errorThrown) {
              promise.reject(jqXHR);
            },
            headers: {
              Accept: 'application/json'
            }
          }
        );
        return promise.promise();
      },

      saveAccessData: function(authData) {
        var beehive = this.getBeeHive();
        //var LocalStorage = beeHive.getService("LocalStorage");
        //LocalStorage.setObject("userSession", {
        //  authData: authData
        //});
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
            ret.resolve(res['orcid-profile']['orcid-activities']['orcid-works']);
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

        var that = this;
        var formatWorks = function (adsWorks) {
          var result = [];
          _.each(adsWorks,
            function (adsWork) {
              result.push(that.formatOrcidWork(adsWork));
            }
          );
          return {'orcid-work': result};
        };

        var orcidWorksMessage = {
          "message-version": "1.2",
          "orcid-profile": {
            "orcid-activities": {
              "orcid-works": formatWorks(adsData)
            }
          }
        };
        return orcidWorksMessage;
      },


      /**
       * Posts (appends) new papers to Orcid;
       *
       * @param adsRecords
       * @returns {*}
       */
      addWorks: function (adsRecs) {
        return this.sendData(this.config.apiEndpoint + '/' + this.authData.orcid + '/orcid-works',
          this.formatOrcidWorks(adsRecs),
          {
            type: "POST"
          });
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
      deleteWorks: function (identifiers) {
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

            var report = {
              deleted: toRemove.length,
              added: 0,
              msg: 'Attempting removal',
              adsTotal: newWorks.length,
              totalRecs: orcidWorks["orcid-work"].length
            };

            self.setWorks(newWorks)
              .done(function (res) {
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
      setWorks: function (adsRecs) {
        return this.sendData(this.config.apiEndpoint + '/' + this.authData.orcid + '/orcid-works',
          this.formatOrcidWorks(adsRecs),
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

        _.extend(options, opts);

        if (!options.headers)
          options.headers = {};
        if (!options.headers["Orcid-Authorization"] && this.authData)
          options.headers["Orcid-Authorization"] = "Bearer " + this.authData.access_token;
        if (!options.headers["Content-Type"])
          options.headers["Content-Type"] = "application/json";
        if (!options.headers["Accept"])
          options.headers["Accept"] = "application/json";

        var api = this.getBeeHive().getService('Api');
        api.request(new ApiRequest({target: url, query: new ApiQuery(), options: options}));
        return result.promise();
      },


      hardenedInstance: {
        hasAccess: 'boolean indicating access to ORCID Api',
        signIn: 'login',
        signOut: 'logout'
      }
    });

    _.extend(OrcidApi.prototype, Mixins.BeeHive);

    return OrcidApi;
  });