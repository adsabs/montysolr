/**
 * This is the core of the ORCID implementation
 * Written by (rca) - totally re-implemented the
 * initial implementation.
 *
 * The important details are:
 *
 *  - all communication with ORCID happens in JSON
 *  - there are multiple access points
 *    addWork()
 *    updateWork()
 *    deleteWork()
 *
 *    But in reality, the ORCID API allows the following
 *    operations:
 *
 *      reading (GET)
 *      adding (POST)
 *      updating (PUT)
 *      deleting (DELETE)
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

    var OrcidApi = GenericModule.extend({

      /**
       * Initialize the service
       */
      initialize: function() {
        this.userData = {};
        this.addCache = [];
        this.getUserProfileCache = [];
        this.authData = null;
        this.addWait = 3000;
        this.profileWait = 500;
        this.maxAddChunkSize = 100;
        this.db = {};
        this.clearDBWait = 30000;
        this.dbUpdatePromise = null;
        this.maxQuerySize = 50;
        this.queryUpdater = new ApiQueryUpdater('orcid_api');
        this.orcidApiTimeout = 30000; // 30 seconds
        this.adsQueryTimeout = 10; // 10 seconds
        this.dirty = true; // initialize as dirty, so it updates
      },

      /**
       * Activate the service.  Setup the configuration and
       * save the current ORCID preferences.
       *
       * @param {BeeHive} beehive
       */
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
        this._addWork = _.debounce(this._addWork, this.addWait);
        this._getUserProfile = _.debounce(this._getUserProfile, this.profileWait);
      },

      /**
       * Checks access to ORCID api by making request for a user profile
       * returns a promise; done() means success, fail() no access
       *
       * @returns {jQuery.Promise}
       */
      checkAccessOrcidApiAccess: function () {
        if (this.hasAccess()) {
          return this.getUserProfile();
        }
        return $.Deferred().reject().promise();
      },

      /**
       * Checks to see if the api has been given authentication information
       * from ORCiD, and if that information has expired or not
       * @returns {boolean}
       */
      hasAccess: function () {
        if (this.authData && this.authData.expires) {
          return this.authData.expires > new Date().getTime();
        }
        return false;
      },

      /**
       * Redirects to ORCID where the user logs in and ORCID will forward
       * user back to us
       *
       * @param {String} [targetRoute='/#/user/orcid'] targetRoute
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

      /**
       * Set the preferences for the user
       *
       * @param {Object} userData - user data to update
       * @returns {*|jQuery.Promise}
       */
      setADSUserData : function (userData) {
        var url = this.getBeeHive().getService("Api").url +
          ApiTargets.ORCID_PREFERENCES + "/" + this.authData.orcid;
        var request = this.createRequest(url, {}, userData);
        request.fail(function () {
          var msg = 'ADS ORCiD preferences could not be set';
          console.error.apply(console, [msg].concat(arguments));
        });
        return request;
      },

      /**
       * Uses the ADS ORCID preferences endpoint to grab the preferences
       * for this user
       *
       * @returns {*|jQuery.Promise}
       */
      getADSUserData : function () {
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

      /**
       * Checks if the exchange code is present on the string
       *
       * @param {String} searchString
       * @returns {boolean}
       */
      hasExchangeCode: function (searchString) {
        return !!this.getExchangeCode(searchString);
      },

      /**
       * Get the exchange token from the location string or one specified
       * @param {String} [searchString=window.location.search] searchString
       * @returns {*|String|Undefined}
       */
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
       * @param {String} oAuthCode
       * @returns {jQuery.Promise}
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

      /**
       * Save the passed in authentication data from orcid
       * into user persistent storage for safe keeping.
       *
       * @param {object} authData - the authentication data
       */
      saveAccessData: function (authData) {
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

      /**
       * Debounced method for keeping lots of request for the profile at bay.
       * This method will resolve all awaiting promises when there has been an
       * idle period following the initial request.
       *
       * Different from getWorks, because with profile we are only concerned
       * with the most up-to-date response.  So a single response to resolve them
       * all is good enough.
       */
      _getUserProfile: function () {
        var self = this;
        var request = this.createRequest(this.getUrl('profile'));

        // get everything so far in the cache
        var cache = self.getUserProfileCache.slice(0);

        request.done(function (profile) {
          _.forEach(cache, function (promise) {
            promise.resolve(new Profile(profile));
          });
        });

        request.fail(function () {
          var args = arguments;
          _.forEach(cache, function (promise) {
            promise.reject.apply(promise, args);
          });
        });
      },

      /**
       * Retrieves user profile
       * Must have scope: /orcid-profile/read-limited
       *
       * Adds to the internal profile cache, which
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

      /**
       * Add new ORCiD work
       * This will add an entry to an internal cache which will be used when
       * the requests finally run.  Here we provide the old work, id and promise
       * to the cache.
       *
       * @param {Object} orcidWork
       */
      addWork: function (orcidWork) {
        if (!_.isPlainObject(orcidWork)) {
          throw new TypeError('Should be plain object');
        }
        var $dd = $.Deferred();
        this.addCache.push({

          // create unique request id to ride along with request
          id: _.uniqueId(),
          work: orcidWork,
          promise: $dd
        });
        this._addWork.call(this);
        return $dd.promise();
      },

      /**
       * Debounced method for adding works
       * This method will run iif it has been called once and then an
       * idle period has passed without another call.  At that point it will
       * get the current cache and make a request.
       *
       * Cached entries are checked against ids that ride along the request on
       * the xhr object.
       *
       * @private
       * @returns {*}
       */
      _addWork: function () {
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

            // check to see if the work is an error message, { error: {...} }
            if (!work) {

              // something weird going on with work, just reject
              promise.reject();
            } else if (work.error) {

              // check to see if it's just a conflict
              if (work.error['response-code'] === 409) {
                promise.resolve(oldWork);
              } else {
                promise.reject();
              }
            } else {

              // no errors, resolve with the new work, { work: {...} }
              promise.resolve(new Work(work.work));
            }
            _.remove(self.addCache, cacheEntry);
          });
        });

        // on fail, reject the promises
        prom.fail(function (responseIds) {
          if (!_.isArray(responseIds) || _.isEmpty(responseIds)) {
            console.error('no cache values found on failure');
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
      },

      /**
       * Add multiple works to ORCiD
       * This method will chunk the incoming works by a maximum chunk size
       * and send a separate request for each.  When all requests complete, it
       * will aggregate and index them using unique request ids
       *
       * @private
       * @param {Object[]} orcidWorks
       * @param {Number[]} ids
       */
      _addWorks: function (orcidWorks, ids) {
        var self = this;
        if (!_.isArray(orcidWorks) || !_.isArray(ids)) {
          throw new TypeError('works and ids must be arrays');
        }

        var $dd = $.Deferred();
        var promises = [];
        var chunk;
        var chunkIds;
        for (var i = 0; i < orcidWorks.length; i += this.maxAddChunkSize) {
          chunk = orcidWorks.slice(i, i + this.maxAddChunkSize);
          chunkIds = ids.slice(i, i + this.maxAddChunkSize);

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

          // we have to create an identifier string for each
          var ret = _.reduce(data.response.docs, function (res, doc) {
            var bibcode = doc.bibcode.toLowerCase();
            var key = 'identifier:' + bibcode;
            res[key] = bibcode;
            _.each(doc.doi, function (doi) {
              var key = 'identifier:' + doi.toLowerCase().replace('doi:', '');
              res[key] = bibcode;
            });
            _.each(doc.alternate_bibcode, function (ab) {
              var key = 'identifier:' + ab.toLowerCase();
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

        // reject after timeout, if necessary
        (function check (count) {
          if (dd.state() === 'pending' && count <= 0) {
            return dd.reject('Request Timeout');
          } else if (dd.state() === 'resolved') {
            return;
          }
          _.delay(check, 1000, --count);
        })(this.adsQueryTimeout);

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
       *  identifier: ['2018CNSNS..56..270Q', '2017CNSNS..56..270Q']
       * });
       * // returns:
       * { "q": [
       *  "identifier:2018CNSNS..56..270Q OR identifier:2017CNSNS..56..270Q"
       * ]}
       *
       * @param {Object} query - query object used to build new ApiQuery
       * @param {string[]} [query.identifier] query.identifier
       * @returns {ApiQuery} - a new api query to use in a request
       * @private
       */
      _buildQuery: function (query) {
        if (_.isEmpty(query) || !query.identifier) {
          return null;
        }

        // reformat array as 'identifier:xxx OR identifier:xxx'
        var q = _(query.identifier)
          .map(function (v) {
            return 'identifier:' + v;
          })
          .filter('length')
          .value()
          .join(' OR ');

        // don't let an empty query string through
        if (_.isEmpty(q)) {
          return null;
        }

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
            var key = 'identifier:';
            var ids = w.getExternalIds();
            if (ids.bibcode) {
              key += ids.bibcode;
            } else if (ids.doi) {
              key += ids.doi;
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
              if (newQuery) {
                whereClauses.push(self._checkIdsInADS(newQuery));
              }
            }
          } else {
            finishUpdate(db);
          }

          /**
           * This will receive a set of of identifier strings that are in the
           * following format:
           *
           * @example
           * [
           *  identifier:2017geoji.tmp...42f:"2017geoji.209..597f",
           *  identifier:2017gml...tmp...20d:"2017gml...tmp...20d"
           * ]
           *
           * It will then update the database, by setting a bibcode property on
           * each record.  Also, if the record is not found here, it will be
           * unset (-1) so that it won't be counted as an orcid record.
           *
           */
          var querySuccess = function () {
            var ids = _.flatten(arguments);

            // Update each orcid record with identifier info gained from ADS
            _.each(db, function (v, key) {
              var bibcode = ids[key];

              // ADS did not find a record for this identifier
              if (!bibcode) {
                db[key].idx = -1;
              } else {
                db[key].bibcode = bibcode;
              }
            });

            finishUpdate(db);
          };

          // on fail, alert the console and finish the update
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
       * @param {object} adsWork
       */
      getRecordInfo: function (adsWork) {
        var self = this;
        var dd = $.Deferred();

        /**
         * Creates a metadata object based on the record data passed in.
         *
         * @returns {{
         *  isCreatedByADS: boolean,
         *  isCreatedByOthers: boolean,
         *  isKnownToADS: boolean,
         *  provenance: null
         * }}
         *
         * @param {object} adsWork
         */
        var getRecordMetaData = function getInfo (adsWork) {
          var out = {
            isCreatedByADS: false,
            isCreatedByOthers: false,
            isKnownToADS: false,
            provenance: null
          };

          /*
          Looking to match the work record passed in to the entry in the db
          Then we can add some metadata like whether it was an ADS sourced
          record or not
           */
          var updateRecord = function (k, v, out) {

            // db is always 'identifier:xxx'
            var key = ('identifier:' + v).toLowerCase();
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

          var ids = _.pick(adsWork, 'identifier', 'bibcode', 'doi', 'alternate_bibcode');

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
              dd.resolve(getRecordMetaData(adsWork));
            })
            .fail(fail);
        } else {
          dd.resolve(getRecordMetaData(adsWork));
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
