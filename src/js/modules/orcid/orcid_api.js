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
 *  - wrap write operations into throttling mode? (it is
 *    more efficient to do updates in bulk)
 *
 */

'use strict';
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
    'js/components/api_targets',
    'js/components/api_query_updater',
    'js/components/api_feedback',
    'js/modules/orcid/work',
    'js/modules/orcid/profile'
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
    ApiTargets,
    ApiQueryUpdater,
    ApiFeedback,
    Work,
    Profile
    ) {

    var ADD_WAIT = 3000;
    var PROFILE_WAIT = 500;
    var ORCID_ADD_MAX = 100;

    var OrcidApi = GenericModule.extend({

      initialize: function() {
        this.orcidProxyUri = '';
        this.userData = {};
        this.addCache = [];
        this.getUserProfileCache = [];
        this.authData = null;
        this.db = {};
        this.profile = {};
        this.checkInterval = 3600 * 1000;
        this.clearDBWait = 30 * 1000;
        this.dbUpdatePromise = null;
        this.maxQuerySize = 100;
        this.queryUpdater = new ApiQueryUpdater('orcid_api');
        this.orcidApiTimeout = 30000; //30seconds
        this.dirty = true; // initialize as dirty, so it updates
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);

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
      checkAccessOrcidApiAccess: function() {
        return this.getUserProfile();
      },

      hasAccess: function() {
        var expires = this.authData && this.authData.expires;
        if (expires) {
          return expires > new Date().getTime();
        }
        return !!(this.authData);
      },

      /**
       * Redirects to ORCID where the user logs in and ORCID will forward
       * user back to us
       */
      signIn: function (targetRoute) {
        this.getPubSub().publish(this.getPubSub().APP_EXIT, {
          type: 'orcid',
          url: this.config.loginUrl
            + "&redirect_uri=" + encodeURIComponent(this.config.redirectUrlBase +
            (targetRoute || '/#/user/orcid'))
        });
        //make sure to redirect to the proper page after sign in
        this.getPubSub().publish(this.getPubSub().ORCID_ANNOUNCEMENT, "login");
      },

      /*
      * set ADS data on endpoint /preferences[orcid id]
      * */
      setADSUserData : function(data){
        var url = this.getBeeHive().getService("Api").url +
          ApiTargets.ORCID_PREFERENCES + "/" + this.authData.orcid;
        var request = this.createRequest(url, {}, data);
        request.fail(function () {
          var msg = 'ADS ORCiD preferences could not be set';
          console.error.apply(console, [msg].concat(arguments));
        });
        return request;
      },

      /*
       * get ADS data from endpoint /preferences[orcid id]
       * */
      getADSUserData : function() {
        var url = this.getBeeHive().getService("Api").url +
          ApiTargets.ORCID_PREFERENCES + "/" + this.authData.orcid;
        var request = this.createRequest(url);
        request.fail(function () {
          var msg = 'ADS ORCiD preferences could not be retrieved';
          console.error.apply(console, [msg].concat(arguments));
        });
        return request;
      },

      /**
       * Forgets the OAuth access_token
       */
      signOut: function () {
        this.saveAccessData(null);
        this.getPubSub().publish(this.getPubSub().ORCID_ANNOUNCEMENT, "logout");
      },

      hasExchangeCode: function (searchString) {
        return !!this.getExchangeCode(searchString);
      },

      getExchangeCode: function (searchString) {
        return this.getUrlParameter('code', searchString || window.location.search);
      },

      /**
       * Extract values from the URL (used to get code from redirects)
       *
       * @param {String} sParam - parameter to find
       * @param {String} searchString - string to search
       * @returns {String|Undefined} - value of param, if found
       */
      getUrlParameter: function (sParam, searchString) {
        var sPageURL = searchString.substring(1);
        var sURLVariables = sPageURL.split('&');
        for (var i = 0; i < sURLVariables.length; i++) {
          var sParameterName = sURLVariables[i].split('=');
          if (sParameterName[0] === sParam) {
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
            done: function () {
              promise.resolve.apply(promise, arguments);
            },
            headers: {
              Accept: 'application/json',
              Authorization: api.access_token
            }
          }
        );
        r.fail(function (jqXHR, textStatus, errorThrown) {
          promise.reject.apply(promise, arguments);
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

        var pubsub = this.getPubSub();
        if (!this.hasAccess()){
          pubsub.publish(pubsub.ALERT, new ApiFeedback({
            code: ApiFeedback.CODES.ALERT,
            msg: " No access_token found",
            type: "danger",
            title: "Unable to contact ORCID API",
            modal : true
          }));
        }
        if (!this.config.apiEndpoint){
          pubsub.publish(pubsub.ALERT, new ApiFeedback({
            code: ApiFeedback.CODES.ALERT,
            msg: "The config variable apiEndpoint is missing",
            type: "danger",
            title: "Unable to contact ORCID API",
            modal : true
          }));
        }
      },

      /**
       * ===============================================================================
       * API ORCID access points
       * ===============================================================================
       */

      /**
       * Convenience method for converting target short name into the full url
       * @param {String} name - short name for endpoint
       * @param {Array|Number} [putCodes] putCodes - putcodes to append to end of url
       * @returns {string} - the url
       */
      getUrl: function (name, putCodes) {
        var targets = {
          'profile': '/orcid-profile',
          'works': '/orcid-works',
          'work': '/orcid-work'
        };
        var url = this.config.apiEndpoint + '/' +
          this.authData.orcid + targets[name];

        var end = (_.isArray(putCodes)) ? putCodes.join(',') : putCodes;

        if (end) {
          url += '/' + end;
        }
        return url;
      },

      /**
       * Create a new request and filter success/fail always to appropriate methods
       * @param {String} url - url to send request to
       * @param {Object} [options={}] options - options for request
       * @param {Object} [data] data - payload of message
       * @returns {jQuery.Promise} promise object for request
       */
      createRequest: function (url, options, data) {
        if (_.isUndefined(url)) {
          throw new Error('Url must be defined');
        }
        var $dd = $.Deferred();
        var prom = this.sendData(url, data, options || {});
        prom.done(function () {
          $dd.resolve.apply($dd, arguments);
        });
        prom.fail(function () {
          $dd.reject.apply($dd, arguments);
        });
        return $dd.promise();
      },

      _getUserProfile: _.debounce(function () {
        var self = this;
        var request = this.createRequest(this.getUrl('profile'));

        request.done(function (profile) {
          _.forEach(self.getUserProfileCache, function (promise) {
            promise.resolve(new Profile(profile));
          });
        });

        request.fail(function () {
          _.forEach(self.getUserProfileCache, function (promise) {
            promise.resolve(new Profile(profile));
          });
        });

        request.always(function () {
          self.getUserProfileCache = [];
        });
      }, PROFILE_WAIT),

      /**
       * Retrieves user profile
       * Must have scope: /orcid-profile/read-limited
       *
       * @returns {jQuery.Promise<Profile>} - Promise that resolves with profile
       */
      getUserProfile: function () {
        var $dd = $.Deferred();

        this.getUserProfileCache.push($dd);
        this._getUserProfile.call(this);
        return $dd.promise();
      },

      /**
       * Retrieve an entire work entry from ORCiD
       * This is different than the summary, it includes all information they
       * have in their system for the entry.
       *
       * This is necessary to get the author information
       *
       * @param {Number} putCode - putcode to be retrieved
       * @returns {jQuery.Promise<Work>} - promise that resolves with the work
       */
      getWork: function (putCode) {
        var $dd = $.Deferred();
        this.createRequest(this.getUrl('works', putCode)).done(function (work) {
          $dd.resolve(new Work(work));
        }).fail(function () {
          $dd.reject.apply($dd, arguments);
        });
        return $dd.promise();
      },

      /**
       * Retrieve the full works as an array,
       * this can take any number of putcodes, it will chunk the requests and
       * return a deferred that will resolve with an array of works
       *
       * @param {Array} putCodes - putcodes to be retrieved
       * @returns {jQuery.Promise<Work[]>}
       */
      getWorks: function (putCodes) {
        if (!_.isArray(putCodes)) {
          throw new TypeError('putcodes must be an Array');
        }
        var $dd = $.Deferred();
        var chunkSize = 50;
        var proms = [];
        for (var i = 0, j = putCodes.length; i < j; i += chunkSize) {
          var chunk = putCodes.slice(i, i + chunkSize);
          var url = this.getUrl('works') + '/' + chunk.join(',');
          proms.push(_.partial(this.createRequest, url));
        }

        var reqs = $.when.apply($, proms);
        reqs.done(function () {
          $dd.resolve(arguments);
        });
        reqs.fail(function () {

          // we are passed an array for EACH argument, so passing the whole thing
          $dd.reject(arguments);
        });
        return $dd.promise();
      },

      /**
       * Update an existing ORCiD work.  This method requires that the putcode
       * for the work be present in the update object.
       *
       * @param {Object} work - object containing updated orcid information
       * @param {Number} work["put-code"] - putcode to update
       * @returns {jQuery.Promise} - promise for the request
       */
      updateWork: function (work) {
        if (!_.isPlainObject(work)) {
          throw new TypeError('Work should be a simple object');
        }

        var putcode = work['put-code'];
        if (!putcode) {
          return $.Deferred().reject().promise();
        }

        var url = this.getUrl('works', putcode);
        return this.createRequest(url, { method: 'PUT' }, work);
      },

      /**
       * Delete a single work from ORCiD
       *
       * @param {Number} putCode - putcode of work to be deleted
       * @returns {jQuery.Promise} - promise for the request
       */
      deleteWork: function (putCode) {
        var url = this.getUrl('works', putCode);
        var prom = this.createRequest(url, { method: 'DELETE' });
        prom.done(_.bind(this.setDirty, this));
        return prom;
      },

      _addWork: _.debounce(function () {
        var self = this;
        var cachedWorks = _.map(self.addCache, 'work');
        var cachedIds = _.map(self.addCache, 'id');
        var prom = self._addWorks(cachedWorks, cachedIds);

        // On success, create a new work and remove the entry from the cache
        prom.done(function (workResponse) {

          // workResponse will be in ID:WORK format
          _.forEach(workResponse, function (work, id) {
            var cacheEntry = _.find(self.addCache, function (e) {
              return e.id = id;
            });

            if (!cacheEntry) {
              return console.error('No cache entry found', self);
            }

            var promise = cacheEntry.promise;
            var oldWork = cacheEntry.work;

            // check to see if the work is an error message
            if (work.error) {

              // check to see if it's just a conflict
              if (work.error['response-code'] === 409) {
                promise.resolve(oldWork);
              } else {
                promise.reject();
              }
            } else {

              // no errors, resole with the new work
              promise.resolve(new Work(work));
            }
            _.remove(self.addCache, cacheEntry);
          });

          // on fail, reject the promises
          prom.fail(function (res, status, xhr) {
            var responseIds = xhr.orcidAddWorkIds;
            if (!responseIds || responseIds.length !== works.length) {
              return console.error('Response ids do not match payload length');
            }
            var args = arguments;
            var indexedIds = _.indexBy(self.addCache, 'id');
            _.forEach(responseIds, function (id) {
              var entry = indexedIds[id];
              if (entry) {
                _.remove(self.addCache, entry);
                entry.promise.reject.apply(entry.promise, args);
              } else {
                console.error('no cache entry', self);
              }
            });
          });
        });
      }, ADD_WAIT),

      /**
       * Add new ORCiD work
       *
       * @param {Object} orcidWork
       */
      addWork: function (orcidWork) {
        var $dd = $.Deferred();
        this.addCache.push({
          id: _.uniqueId(),
          work: orcidWork,
          promise: $dd
        });
        this._addWork.call(this);
        return $dd.promise();
      },

      /**
       * Add multiple works to ORCiD
       *
       * @param {Object[]} orcidWorks
       * @param {Number[]} ids
       */
      _addWorks: function (orcidWorks, ids) {
        var self = this;
        if (!_.isArray(orcidWorks)) {
          throw new TypeError('works must be an Array');
        }

        var $dd = $.Deferred();
        var promises = [];
        var chunk;
        var chunkIds;
        for (var i = 0; i < orcidWorks.length; i += ORCID_ADD_MAX) {
          chunk = orcidWorks.slice(i, i + ORCID_ADD_MAX);
          chunkIds = ids.slice(i, i + ORCID_ADD_MAX);

          // create bulk object
          var bulkWorks = { bulk: [] };
          _.each(chunk, function (w) {
            bulkWorks.bulk.push({ work: w });
          });

          var url = this.getUrl('works');
          promises.push(this.createRequest(url, {
            beforeSend: function (xhr) {
              xhr.cacheIds = chunkIds;
            },
            method: 'POST'
          }, bulkWorks));
        }

        // when all the promises finish, aggregate the result and index by id
        $.when.apply($, promises).then(function () {

          // make sure arguments is an 2d array
          var doneArgs = _.isArray(arguments[0]) ? arguments : [arguments];

          var obj = _.reduce(doneArgs, function (res, args) {
            var works = args && args[0] && args[0].bulk;
            var ids = args && args[2] && args[2].cacheIds;

            // build response object, indexed by ids
            _.forEach(ids, function (id, idx) {
              res[id] = works[idx].work;
            });

            return res;
          }, {});

          $dd.resolve(obj);
        }, function () {
          self.setDirty();
          $dd.reject.apply($dd, arguments);
        });

        return $dd.promise();
      },

      // #############################################################################

      /**
       * Use Api service to make a request to the ORCID api
       *
       * @param {string} url - request url
       * @param {Object} [data={}] data - request payload
       * @param {Object} [opts={}] opts - request options
       * @returns {*}
       */
      sendData: function (url, data, opts) {

        var result = $.Deferred();
        opts = opts || {};

        var options = {
          type: 'GET',
          url: url,
          contentType: 'application/json',
          cache: !!this.dbUpdatePromise, // true = do not generate _ parameters (let browser cache responses)
          timeout: this.orcidApiTimeout,
          done: function () {
            result.resolve.apply(result, arguments);
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

        api.request(new ApiRequest({target: url, query: new ApiQuery(), options: options}))
          .fail(function() {
            result.reject.apply(result, arguments);
          });
        return result.promise();
      },

      /**
       * Takes in a query containing all identifiers to check against what is in
       * ADS.  Query will return all known (to ADS) records, but in the following
       * format:
       *
       * "IDENTIFIER_TYPE:IDENTIFIER_VALUE": "BIBCODE"
       *
       * @example
       * q = ['bibcode:2018CNSNS..56..270Q OR alternate_bibcode:2018CNSNS..56..270Q']
       * // returns:
       * {
       *  "bibcode:2018CNSNS..56..270Q": "2018CNSNS..56..270Q"
       *  "doi:10.1016/j.cnsns.2017.08.014": "2018CNSNS..56..270Q"
       * }
       *
       * @param {ApiQuery} apiQuery - query to check
       * @returns {jQuery.Promise<Object>} - request promise
       * @private
       */
      _checkIdsInADS: function (apiQuery) {
        var api = this.getBeeHive().getService('Api');
        var dd = $.Deferred();

        apiQuery.set('fl', 'bibcode, doi, alternate_bibcode');
        apiQuery.set('rows', '5000');

        var onDone = function (data) {

          if (!data || !data.response || !data.response.docs) {
            return dd.resolve({});
          }

          var ret = _.reduce(data.response.docs, function (res, doc) {
            var bibcode = doc.bibcode.toLowerCase();
            var key = 'bibcode:' + bibcode;
            res[key] = bibcode;
            _.each(doc.doi, function (doi) {
              var key = 'doi:' + doi.toLowerCase().replace('doi:', '');
              res[key] = bibcode;
            });
            _.each(doc.alternate_bibcode, function (ab) {
              var key = 'bibcode:' + ab.toLowerCase();
              res[key] = bibcode;
            });
            return res;
          }, {});

          dd.resolve(ret);
        };

        var onFail = function () {
          dd.resolve({});
        };

        api.request(new ApiRequest({
          target: ApiTargets.SEARCH,
          query: apiQuery,
          options: {
            done: onDone,
            fail: onFail
          }
        })).fail(onFail);

        return dd.promise();
      },

      /**
       * Check if the work was sourced by ADS
       * @param {Work} work
       */
      isSourcedByADS: function isSourcedByADS(work) {
        return this.config.clientId === work.getSourceClientIdPath();
      },

      /**
       * Generate a query string by doing custom joins on the array.  Each entry
       * in the passed in query gets checked and added to the generated string
       * by "OR"-ing them together.
       *
       * @example
       * _buildQuery({
       *  bibcode: ['2018CNSNS..56..270Q'],
       *  alternate_bibcode: ['2018CNSNS..56..270Q']
       * });
       * // returns:
       * { "q": [
       *  "bibcode:(2018CNSNS..56..270Q) OR alternate_bibcode:(2018CNSNS..56..270Q)"
       * ]}
       *
       * @param {Object} query - query object used to build new ApiQuery
       * @param {string[]} [query.bibcode] query.bibcode
       * @param {string[]} [query.alternate_bibcode] query.alternate_bibcode
       * @param {string[]} [query.doi] query.doi
       * @returns {ApiQuery} - a new api query to use in a request
       * @private
       */
      _buildQuery: function (query) {
        var formatString = function (values, field) {
          if (values.length === 0) {
            return '';
          }

          if (field === 'doi') {
            var str = '(';
            str += _.map(values, function (p) {
              return 'doi:' + p;
            }).join(' OR ');
            str += ')';
            return str;
          } else {
            return field + ':(' + values.join(' OR ') + ')';
          }
        };

        var q = _(query)
          .map(formatString)
          .filter('length')
          .value()
          .join(' OR ');
        return new ApiQuery({ q: q });
      },

      /**
       * Updates our current knowledge of ORCID Data
       * This will typically be only summaries of works
       *
       * @param {Profile} [profile] profile
       */
      updateDatabase: function updateDatabase(profile) {
        var self = this;
        if (this.dbUpdatePromise && this.dbUpdatePromise.state() === 'pending') {
          return this.dbUpdatePromise.promise();
        }
        this.dbUpdatePromise = $.Deferred();

        // set the database object and resolve the promise
        var finishUpdate = function (db) {
          var dbPromise = self.dbUpdatePromise;
          self.setClean();
          self.db = db;
          self.profile = profile;
          if (dbPromise) {
            dbPromise.resolve();
          }
          setTimeout(function () {
            if (dbPromise && dbPromise.state() !== 'pending') {
              dbPromise = null;
              self.setDirty();
            }
          }, self.clearDBWait);
        };

        // apply the update to the database
        var update = function update(profile) {
          // get the works and all external IDs for them
          var works = profile.getWorks();
          var query = [];
          var db = {};
          _.forEach(works, function addIdsToDatabase(w, i) {
            var key;
            var ids = w.getExternalIds();
            if (ids.bibcode) {
              key = 'bibcode:' + ids.bibcode;
              query.push('alternate_bibcode:' + ids.bibcode);
            } else if (ids.doi) {
              key = 'doi:' + ids.doi;
            }

            if (key) {
              query.push(key);
              db[key.toLowerCase()] = {
                sourcedByADS: self.isSourcedByADS(w),
                putcode: w.getPutCode(),
                idx: i
              };
            }
          });

          if (query.length && self.maxQuerySize > 0) {
            var whereClauses = [];
            query = query.sort();
            var steps = _.range(0, query.length, self.maxQuerySize);
            for (var i = 0; i < steps.length; i++) {
              var q = {};
              for (var j = steps[i]; j < steps[i] + self.maxQuerySize; j++) {
                if (j >= query.length) break;
                var ps = query[j].split(':');
                if (!q[ps[0]])
                  q[ps[0]] = [];
                q[ps[0]].push(self.queryUpdater.quoteIfNecessary(ps[1]));
              }
              var newQuery = self._buildQuery(q);
              whereClauses.push(self._checkIdsInADS(newQuery));
            }
          } else {
            finishUpdate(db);
          }

          var querySuccess = function () {
            _.each(arguments, function (bibcodes) {

              // Update each orcid record with identifier info gained from ADS
              _.each(db, function (v, key) {

                var bibcode = bibcodes[key];

                // ADS did not find a record for this identifier
                if (!bibcode) {
                  db[key].idx = -1;
                } else {
                  db[key].bibcode = bibcode;
                }
              });
            });
            finishUpdate(db);
          };

          var queryFailure = function () {
            console.error.apply(console, [
              'Error processing response from ADS'].concat(arguments));
            finishUpdate(db);
          };

          $.when.apply($, whereClauses).then(querySuccess, queryFailure);
        };

        if (profile) {
          update(profile);
        } else {

          // if we aren't passed a profile, get the current one
          this.getUserProfile().done(update).fail(function () {
            self.dbUpdatePromise.reject.apply(self.dbUpdatePromise, arguments);
          });
        }

        return self.dbUpdatePromise.promise();
      },

      /**
       * Creates a metadata object based on the work that is passed in that
       * helps with understanding the record's relationship with ADS.  Figures
       * out if the record is sourced by ADS, whether it is known, and whether
       * ADS has the rights to update/delete it.
       *
       * @param {Work} work
       */
      getRecordInfo: function (work) {
        var self = this;
        var dd = $.Deferred();

        /**
         * Creates a metadata object based on the record data passed in.
         *
         * @param {Object} data - record to gather metadata on
         * @returns {{
         *  isCreatedByADS: boolean,
         *  isCreatedByOthers: boolean,
         *  isKnownToADS: boolean,
         *  provenance: null
         * }}
         */
        var getInfo = function getInfo (data) {
          var out = {
            isCreatedByADS: false,
            isCreatedByOthers: false,
            isKnownToADS: false,
            provenance: null
          };

          var updateRecord = function (k, v, out) {
            var key = (k + ':' + v).toLowerCase();
            var rec = self.db[key];

            if (rec) {
              if (rec.sourcedByADS) {
                out.isCreatedByADS = true;
              } else {
                out.isCreatedByOthers = true;
              }

              if (rec.idx > -1) {
                out.isKnownToADS = true;
              }
              out.putcode = rec.putcode;
              out.bibcode = rec.bibcode;
            }
          };

          var ids = _.pick(data, 'bibcode', 'doi', 'alternate_bibcode');

          _.each(ids, function (value, key) {
            if (_.isArray(value)) {
              _.each(value, updateRecord);
            } else {
              updateRecord(key, value, out);
            }
          });

          return out;
        };

        var fail = function () {
          dd.reject.apply(dd, arguments);
        };

        if (this.needsUpdate()) {
          this.updateDatabase()
            .done(function () {
              dd.resolve(getInfo(work));
            })
            .fail(fail);
        } else {
          dd.resolve(getInfo(work));
        }

        return dd.promise();
      },

      /**
       * Determines if the database needs to be updated.  It may, if there have
       * been updates/deletes or if the internal timeout fired.
       *
       * @returns {boolean}
       */
      needsUpdate: function () {
        return this.dirty;
      },

      setDirty: function () {
        this.dirty = true;
      },

      setClean: function () {
        this.dirty = false;
      },

      hardenedInterface: {
        hasAccess: 'boolean indicating access to ORCID Api',
        getUserProfile : 'get user profile',
        signIn: 'login',
        signOut: 'logout',
        getADSUserData : '',
        setADSUserData : '',
        getRecordInfo: 'provides info about a document',
        addWork: 'add a new orcid work',
        deleteWork: 'remove an entry from orcid',
        updateWork: 'update an orcid work',
        getWork: 'get an orcid work',
        getWorks: 'get an array of orcid works'
      }
    });

    _.extend(OrcidApi.prototype, Mixins.BeeHive);
    _.extend(OrcidApi.prototype, HardenedMixin);

    return OrcidApi;
  });
