'use strict';
define([
    'js/components/generic_module',
    'js/mixins/dependon',
    'jquery',
    'js/modules/orcid/orcid_api',
    'js/components/persistent_storage',
    'underscore',
    'js/bugutils/minimal_pubsub',
    'js/components/pubsub_events',
    'js/modules/orcid/module',
    './helpers',
    'js/modules/orcid/work',
    'js/modules/orcid/profile'
  ],

  function (
    GenericModule,
    Mixins,
    $,
    OrcidApi,
    LocalStorage,
    _,
    MinimalPubsub,
    PubSubEvents,
    OrcidModule,
    helpers,
    Work,
    Profile
  ) {
  sinon.test(function () {
    var createOrcidServer = function (orcidApi, minsub) {
      var server = sinon.fakeServer.create();

      server.respondWith('GET', /\/orcid-works\/(.*)/, function (xhr, putcodes) {
        xhr.respond(200, {
          'Content-Type': 'application/json'
        }, JSON.stringify(helpers.getMock('work')));
      });

      server.respondWith('POST', /\/orcid-works/, function (xhr) {
        xhr.respond(201, {
          'Content-Type': 'application/json'
        }, xhr.requestBody);
      });

      server.respondWith('PUT', /\/orcid-works\/(.*)/, function (xhr, putcodes) {
        xhr.respond(200, {
          'Content-Type': 'application/json'
        }, xhr.requestBody);
      });

      server.respondWith('DELETE', /\/orcid-works\/.*/, function (xhr) {
        xhr.respond(204);
      });

      server.respondWith('GET', /\/orcid-profile/, function (xhr) {
        xhr.respond(200, {
          'Content-Type': 'application/json'
        }, JSON.stringify(helpers.getMock('profile')));
      });

      var sendRequest = function (url, options, data) {
        var $dd = $.Deferred();

        $.ajax(url, _.extend(options, {
          data: JSON.stringify(data),
          dataType: 'json',
          headers: {
            'Content-Type': 'application/json'
          }
        }))
        .done(function () {
          options && options.done && options.done.apply(this, arguments);
          $dd.resolve.apply($dd, arguments);
        })
        .fail(function () {
          options && options.fail && options.fail.apply(this, arguments);
          $dd.reject.apply($dd, arguments);
        });

        server.respond();
        return $dd.promise();
      };

      sinon.stub(orcidApi, 'createRequest', function (url, options, data) {
        var parts = url.split('/');
        if (parts.length === 5) {
          url = '/' + _.last(parts);
        } else {
          url = '/' + parts.slice(-2).join('/');
        }
        return sendRequest(url, options, data);
      });

      if (minsub) {
        server.respondWith('GET', /\/exchangeOAuthCode/, [200, {
          'Content-Type': 'application/json'
        }, JSON.stringify(helpers.getMock('oAuth'))]);

        server.respondWith('GET', /search\/query/, function (xhr) {
          xhr.respond(200, {
            'Content-Type': 'application/json'
          }, JSON.stringify(helpers.getMock('adsResponse')));
        });

        sinon.stub(minsub, 'request', function (apiRequest) {
          var url = apiRequest.get('target');
          var options = apiRequest.get('options');
          var data = options.data;
          return sendRequest(url, options, data);
        });
      }

      return server;
    };

    var getMinSub = function () {
      var minsub = new MinimalPubsub({ verbose: false });
      minsub.beehive.addObject('DynamicConfig', {
        orcidClientId: 'APP-P5ANJTQRRTMA6GXZ',
        orcidApiEndpoint: 'https://api.orcid.org',
        orcidRedirectUrlBase: 'http://localhost:8000'
      });
      return minsub;
    };

    var getOrcidApi = function (beehive) {
      var oModule = new OrcidModule();
      oModule.activate(beehive);
      var oApi = beehive.getService('OrcidApi');
      oApi.saveAccessData({
        "access_token":"4274a0f1-36a1-4152-9a6b-4246f166bafe",
        "orcid":"0000-0001-8178-9506"
      });
      return oApi;
    };

    describe("Orcid API service (orcid_api.spec.js)", function () {
      describe("OAuth", function() {
        beforeEach(function (done) {
          var minsub = new (MinimalPubsub.extend({
            request: function (apiRequest) {
              if (apiRequest.get('target').indexOf('/exchangeOAuthCode') > -1) {
                expect(apiRequest.get('query').get('code')).to.eql(['secret']);
                return {
                  "access_token":"4274a0f1-36a1-4152-9a6b-4246f166bafe",
                  "token_type":"bearer",
                  "expires_in":3599,
                  "scope":"/orcid-works/create /orcid-profile/read-limited /orcid-works/update",
                  "orcid":"0000-0001-8178-9506",
                  "name":"Roman Chyla"};
              }
              else if (apiRequest.get('target').indexOf('test-query') > -1) {
                var opts = apiRequest.get('options');
                expect(opts.headers["Orcid-Authorization"]).to.eql('Bearer 4274a0f1-36a1-4152-9a6b-4246f166bafe');
                expect(opts.data).to.eql('{"data":{"foo":"bar"}}');
                return {success: true};
              }
            }
          }))({verbose: false});
          minsub.beehive.addObject('DynamicConfig', {
            orcidClientId: 'APP-P5ANJTQRRTMA6GXZ',
            orcidApiEndpoint: 'https://api.orcid.org',
            orcidRedirectUrlBase: 'http://localhost:8000'
          });
          this.minsub = minsub;
          this.beehive = minsub.beehive;
          done();
        });

        it("has methods to extract access code", function() {
          var oApi = getOrcidApi(this.minsub.beehive);
          // it receives window.location.search
          expect(oApi.getUrlParameter('code', '?foo=bar&code=H1trXI')).to.eql('H1trXI');
          expect(oApi.hasExchangeCode('?foo=bar&code=H1trXI')).to.eql(true);
          expect(oApi.getExchangeCode('?foo=bar&code=H1trXI')).to.eql('H1trXI');
        });

        it("can exchange code for access_token (auth data)", function(done) {
          var oApi = getOrcidApi(this.minsub.beehive);
          var r = oApi.getAccessData('secret');
          expect(_.isUndefined(r.done)).to.eql(false);
          //this.server.respond();
          r.done(function(res) {
            expect(res).to.eql({
              "access_token":"4274a0f1-36a1-4152-9a6b-4246f166bafe",
              "token_type":"bearer",
              "expires_in":3599,
              "scope":"/orcid-works/create /orcid-profile/read-limited /orcid-works/update",
              "orcid":"0000-0001-8178-9506",
              "name":"Roman Chyla"});

            oApi.saveAccessData(res);

            expect(oApi.authData).to.eql(res);

            // the expires was added
            expect(oApi.authData.expires).to.be.gt(new Date().getTime());

            // now request uses access_token
            var req = oApi.sendData('test-query', {data: {foo: 'bar'}});
            req.done(function(res) {
              expect(res).to.eql({success: true});
              done();
            });

          })
        });

        it("should handle ORCID sign in", function(){

          var oApi = getOrcidApi(this.minsub.beehive);

          this.beehive.getService("PubSub").publish = sinon.spy();

          oApi.signIn();

          expect(JSON.stringify(this.beehive.getService("PubSub").publish.args[0])).to.eql('[{},"[App]-Exit",{"type":"orcid","url":"undefined?scope=/orcid-profile/read-limited%20/orcid-works/create%20/orcid-works/update&response_type=code&access_type=offline&show_login=true&client_id=APP-P5ANJTQRRTMA6GXZ&redirect_uri=http%3A%2F%2Flocalhost%3A8000%2F%23%2Fuser%2Forcid"}]');
          expect(JSON.stringify(this.beehive.getService("PubSub").publish.args[1])).to.eql('[{},"[PubSub]-Orcid-Announcement","login"]')
        });

        it("signOut forgets authentication details", function() {
          var oApi = getOrcidApi(this.minsub.beehive);
          expect(oApi.hasAccess()).to.be.eql(false, 'needs to have authData defined');
          oApi.authData = {foo: 'bar'};
          expect(oApi.hasAccess()).to.be.eql(false, 'should need to have expires');
          oApi.signOut();
          expect(oApi.hasAccess()).to.be.eql(false, 'sign out cleared authdata');

          oApi.authData = {expires: new Date().getTime() + 100};
          expect(oApi.hasAccess()).to.be.eql(true, 'authData was not expired');
          oApi.authData = {expires: new Date().getTime() - 100};
          expect(oApi.hasAccess()).to.be.eql(false, 'authData was expired');
        });

      });

      var isPromise = function (val) {
        var con = val.constructor === $.Deferred().constructor;
        var props = val.done && !val.resolve;
        return con && props;
      };

      describe('addWork', function () {
        var sb, server, oApi;
        beforeEach(function () {
          sb = sinon.sandbox.create();
          var minsub = getMinSub();
          oApi = getOrcidApi(minsub.beehive);
          server = createOrcidServer(oApi, minsub);
        });
        afterEach(function () {
          sb.restore();
          server && server.restore && server.restore();
        });

        it('handles bad input', function (done) {
          expect(oApi.addWork.bind(oApi)).to.throw(TypeError);
          expect(oApi.addWork.bind(oApi, null)).to.throw(TypeError);
          expect(oApi.addWork.bind(oApi, [])).to.throw(TypeError);
          done();
        });

        it('updates cache', function (done) {
          oApi.addWork({ test: 'test' });
          expect(oApi.addCache.length).to.eql(1);
          expect(oApi.addCache[0].work).to.eql({ test: 'test' });
          done();
        });

        it('calls _addWork', function (done) {
          sb.spy(oApi, '_addWork');
          oApi.addWork({ test: 'test' });
          expect(oApi._addWork.callCount).to.eql(1);
          done();
        });

        it('returns a promise', function (done) {
          var ret = oApi.addWork({ test: 'test' });
          expect(isPromise(ret)).to.eql(true);
          done();
        });

        it('returned promise equals one in cache', function (done) {
          var ret = oApi.addWork({ test: 'test' });
          expect(isPromise(ret)).to.eql(true);
          expect(oApi.addCache[0].promise.promise()).to.eql(ret);
          done();
        });
      });

      describe('_addWork', function () {
        var sb, server, oApi;
        beforeEach(function () {
          sb = sinon.sandbox.create();
          var minsub = getMinSub();
          oApi = getOrcidApi(minsub.beehive);
          server = createOrcidServer(oApi, minsub);
          oApi._addWork = _.debounce(OrcidApi.prototype._addWork, 10);
        });
        afterEach(function () {
          sb.restore();
          server && server.restore && server.restore();
        });

        it('calls _addWorks', function (done) {
          sb.spy(oApi, '_addWorks');
          oApi._addWork();
          _.delay(function () {
            expect(oApi._addWorks.callCount).to.eql(1);
            done();
          }, 15);
        });

        it('is debounced, firing only after idle period', function (done) {
          sb.spy(oApi, '_addWorks');
          oApi._addWork();
          oApi._addWork();
          oApi._addWork();
          oApi._addWork();
          _.delay(function () {
            expect(oApi._addWorks.callCount).to.eql(1);
            done();
          }, 15);
        });

        it('recovers from orcid conflict', function (done) {

          // returns an orcid conflict error msg
          sb.stub(oApi, '_addWorks', function () {
            var val = { work: { error: { 'response-code': 409 }}};
            return $.Deferred().resolve(val).promise();
          });

          var $dd = $.Deferred();
          oApi.addCache.push({ id: 0, work: { test: 'old' }, promise: $dd });
          oApi._addWork();
          $dd.done(function (work) {

            // should get an object back with *old* work
            expect(_.isPlainObject(work)).to.eql(true);
            expect(work).to.eql({ test: 'old' });
            done();
          });
        });

        it('rejects promise on orcid error (non-conflict)', function (done) {

          // returns an orcid generic error msg
          sb.stub(oApi, '_addWorks', function () {
            var val = { work: { error: {}}};
            return $.Deferred().resolve(val).promise();
          });

          var $dd = $.Deferred();
          oApi.addCache.push({ id: 0, work: { test: 'old' }, promise: $dd });
          oApi._addWork();
          $dd.done(function () { expect(false); });
          $dd.fail(function () {
            expect(true);
            done();
          });
        });

        it('returns a Work record', function (done) {

          // returns a *new* orcid object
          sb.stub(oApi, '_addWorks', function () {
            var val = { 0: { work: { test: 'new' }}};
            return $.Deferred().resolve(val).promise();
          });

          var $dd = $.Deferred();
          oApi.addCache.push({ id: 0, work: { test: 'old' }, promise: $dd });
          oApi._addWork();
          $dd.done(function (orcidWork) {

            // should get a Work object back with *new* work
            expect(orcidWork instanceof Work).to.eql(true);
            expect(orcidWork._root).to.eql({ test: 'new' });
            done();
          });
        });

        it('removes cache items as they are used', function (done) {
          oApi.addCache = [
            { id: 0, work: {}, promise: $.Deferred() },
            { id: 1, work: {}, promise: $.Deferred() },
            { id: 2, work: {}, promise: $.Deferred() }
          ];
          oApi._addWork();
          _.delay(function () {
            expect(oApi.addCache.length).to.eql(0);
            done();
          }, 15);
        });

        it('rejects cached promises upon request failure', function (done) {

          // the request fails...
          sb.stub(oApi, '_addWorks', function () {
            var val = [0];
            return $.Deferred().reject(val).promise();
          });

          var $dd = $.Deferred();
          oApi.addCache.push({ id: 0, work: { test: 'old' }, promise: $dd });
          oApi._addWork();
          $dd.fail(function (ids) {
            expect(ids).to.eql([0]);
            expect(oApi.addCache.length).to.eql(0);
            done();
          });
        });
      });

      describe('_addWorks', function () {
        var sb, server, oApi;
        beforeEach(function () {
          sb = sinon.sandbox.create();
          var minsub = getMinSub();
          oApi = getOrcidApi(minsub.beehive);
          server = createOrcidServer(oApi, minsub);
          oApi._addWork = _.debounce(OrcidApi.prototype._addWork, 10);
        });
        afterEach(function () {
          sb.restore();
        });

        it('handles bad input', function (done) {
          expect(oApi._addWorks.bind(oApi)).to.throw(TypeError);
          expect(oApi._addWorks.bind(oApi, null, [])).to.throw(TypeError);
          expect(oApi._addWorks.bind(oApi, [], null)).to.throw(TypeError);
          expect(oApi._addWorks.bind(oApi, null, null)).to.throw(TypeError);
          expect(oApi._addWorks.bind(oApi, {}, {})).to.throw(TypeError);
          expect(oApi._addWorks.bind(oApi, {}, null)).to.throw(TypeError);
          expect(oApi._addWorks.bind(oApi, null, {})).to.throw(TypeError);
          done();
        });

        it('correctly chunks input', function (done) {
          oApi.maxAddChunkSize = 10;
          var works = _.map(_.range(0, 100), function (i) {
            return { title: 'test', i: i };
          });
          var ids = _.range(100, 200);
          var prom = oApi._addWorks(works, ids);
          prom.done(function () {
            expect(oApi.createRequest.callCount).to.eql(10, '10 requests');
            done();
          });
        });

        it('request on success, resolves w/ id-indexed works', function (done) {
          var works = _.map(_.range(0, 200), function (i) {
            return { title: 'test', i: i };
          });
          var ids = _.range(100, 300);
          var prom = oApi._addWorks(works, ids);
          prom.done(function (res) {

            // check the response is right
            _.forEach(ids, function (n, i) {
              expect(res[n].work).to.eql(works[i], 'work matches');
            });
            done();
          });
        });

        it('request on fail, resolves promise w/ array of ids', function (done) {

          server.respondWith('POST', /\/orcid-works/, function (xhr) {
            xhr.respond(404, {
              'Content-Type': 'application/json'
            }, xhr.requestBody);
          });

          var works = _.map(_.range(0, 200), function (i) {
            return { title: 'test', i: i };
          });
          var ids = _.range(100, 300);
          var prom = oApi._addWorks(works, ids);
          prom.fail(function (res) {
            expect(_.isArray(res)).to.eql(true);

            // jquery fails on first failure
            expect(res).to.eql(ids.splice(0, 100));
            done();
          });
        });

        it('returns a promise', function (done) {
          var prom = oApi._addWorks([], []);
          expect(isPromise(prom)).to.eql(true);
          done();
        });
      });

      describe("Orcid Actions", function() {

        beforeEach(function () {
          this.minsub = getMinSub();
        });

        it('should be GenericModule', function (done) {
          expect(new OrcidApi() instanceof GenericModule).to.equal(true, 'is GenericModule');
          expect(new OrcidApi() instanceof OrcidApi).to.equal(true, 'is OrcidApi');
          done();
        });

        it("exports hardened interface", function() {
          var oApi = getOrcidApi(this.minsub.beehive);
          var hardened = oApi.getHardenedInstance();
          var check = function (props) {
            _.forEach(props, function (p) {
              expect(_.has(hardened, p)).to.equal(true, 'Hardened Interface has property ' + p);
            });
          };

          check([
           'hasAccess',
           'getUserProfile',
           'signIn',
           'signOut',
           'getADSUserData',
           'setADSUserData',
           'getRecordInfo',
           'addWork',
           'deleteWork',
           'updateWork',
           'getWork',
           'getWorks'
          ]);
        });

        //TODO: throwing sinon error, extend this
        it('getUserProfile', function (done) {
          var oApi = getOrcidApi(this.minsub.beehive);
          createOrcidServer(oApi, this.minsub);
          oApi.getUserProfile()
            .done(function (profile) {
              expect(profile instanceof Profile).to.equal(true, 'returns instance of Profile');
              done();
            });
        });

        it('getWork', function (done) {
          var oApi = getOrcidApi(this.minsub.beehive);
          createOrcidServer(oApi, this.minsub);
          oApi.getWork(99999)
            .done(function (work) {
              expect(work instanceof Work).to.equal(true, 'returns instance of Work');
              done();
            });
        });

        it('deleteWork', function (done) {
          var oApi = getOrcidApi(this.minsub.beehive);
          createOrcidServer(oApi, this.minsub);
          oApi.deleteWork(99999)
            .done(function (data, status, xhr) {
              expect(xhr.status).to.equal(204, 'get 204 status code');
              expect(data).to.equal(undefined, 'got no content');
              done();
            });
        });

        it('updateWork', function (done) {
          var oApi = getOrcidApi(this.minsub.beehive);
          createOrcidServer(oApi, this.minsub);
          var orcidWork = new Work(helpers.getMock('work')).getAsOrcid();
          oApi.updateWork(orcidWork)
            .done(function (work) {
              expect(JSON.stringify(work)).to.equal(JSON.stringify(orcidWork),
                'get our updated work back');
              done();
            });
        });

        it('getRecordInfo', function () {
          var oApi = getOrcidApi(this.minsub.beehive);
          sinon.stub(oApi, 'needsUpdate', _.constant(false));

          var db = {
            "identifier:foo": {
              "sourcedByADS": true,
              "putcode": 889362,
              "idx": 0
            },
            "identifier:bar": {
              "sourcedByADS": false,
              "putcode": 878658,
              "idx": 1
            },
            "identifier:boo": {
              "sourcedByADS": true,
              "putcode": 880867,
              "idx": 1
            },
            "identifier:baz": {
              "sourcedByADS": false,
              "putcode": 880867,
              "idx": -1
            }
          };

          var check = function (rInfo, val, exp) {
            expect(rInfo[val]).to.equal(exp, val + ' : ' + exp);
          };

          oApi.db = db;
          oApi.getRecordInfo({ bibcode: 'foo' }).done(function (rInfo) {
            check(rInfo, 'isCreatedByADS', true);
            check(rInfo, 'isCreatedByOthers', false);
            check(rInfo, 'isKnownToADS', true);
          });

          oApi.getRecordInfo({ bibcode: 'bar' }).done(function (rInfo) {
            check(rInfo, 'isCreatedByADS', false);
            check(rInfo, 'isCreatedByOthers', true);
            check(rInfo, 'isKnownToADS', true);
          });

          oApi.getRecordInfo({ doi: 'boo' }).done(function (rInfo) {
            check(rInfo, 'isCreatedByADS', true);
            check(rInfo, 'isCreatedByOthers', false);
            check(rInfo, 'isKnownToADS', true);
          });

          oApi.getRecordInfo({ doi: 'baz' }).done(function (rInfo) {
            check(rInfo, 'isCreatedByADS', false);
            check(rInfo, 'isCreatedByOthers', true);
            check(rInfo, 'isKnownToADS', false);
          });
        });

        it.skip("has methods to query status of a record", function(done) {
          var oApi = getOrcidApi(this.minsub.beehive);
          createOrcidServer(oApi, this.minsub);
          sinon.spy(oApi, 'updateDatabase');
          sinon.stub(oApi, '_checkIdsInADS', function (query) {
            return $.Deferred().resolve({
              "bibcode:2018cnsns..56..296s": "2018cnsns..56..296s",
              "doi:10.1016/j.cnsns.2017.08.013": "2018cnsns..56..296s",
              "bibcode:2018cnsns..56..270q": "2018cnsns..56..270q",
              "doi:10.1016/j.cnsns.2017.08.014": "2018cnsns..56..270q"
            }).promise();
          });

          // for testing purpose, force split into many queries
          oApi.maxQuerySize = 2;

          oApi.getRecordInfo({bibcode: '2018cnsns..56..296s'})
            .done(function (recInfo) {

              expect(oApi._checkIdsInADS.called).to.eql(true);
              expect(oApi._checkIdsInADS.calledTwice).to.eql(true);
              expect(oApi._checkIdsInADS.args[0][0].get('q')).eql(["alternate_bibcode:(\"bibcode-foo\" OR \"test-bibcode\")"]);
              expect(oApi._checkIdsInADS.args[1][0].get('q')).eql(["bibcode:(\"bibcode-foo\" OR \"test-bibcode\")"]);


              expect(recInfo.isCreatedByADS).to.eql(true);
              expect(recInfo.isCreatedByOthers).to.eql(false);

              // this one should return immediately
              oApi.getRecordInfo({bibcode: 'bibcode-foo'})
                .done(function(recInfo) {
                  expect(recInfo.isCreatedByADS).to.eql(false);
                  expect(recInfo.isCreatedByOthers).to.eql(true);
                  expect(recInfo.isKnownToAds).to.eql(true);
                });

              oApi.getRecordInfo({doi: '10.1126/science.276.5309.88'}) // doi of bibcode-foo
                .done(function(recInfo) {
                  expect(recInfo.isCreatedByADS).to.eql(false);
                  expect(recInfo.isCreatedByOthers).to.eql(true);
                  expect(recInfo.isKnownToAds).to.eql(true);
                });

              oApi.getRecordInfo({doi: '10.1103/physrevlett.84.3823'}) // test-bibcode
                .done(function(recInfo) {
                  expect(recInfo.isCreatedByADS).to.eql(true);
                  expect(recInfo.isCreatedByOthers).to.eql(false);
                  expect(recInfo.isKnownToAds).to.eql(true);
                });

              oApi.getRecordInfo({bibcode: '1997Sci...276...88V'}) // alternate bibcode of bibcode-foo
                .done(function(recInfo) {
                  expect(recInfo.isCreatedByADS).to.eql(false);
                  expect(recInfo.isCreatedByOthers).to.eql(true);
                  expect(recInfo.isKnownToAds).to.eql(true);
                });

              // found by one of the queries, but could not be mapped to bibcode
              // this should not normally be happening, but i've added the logic
              // to accomodate it - just in case...
              oApi.getRecordInfo({bibcode: '2015CeMDA.tmp....1D'})
                .done(function(recInfo) {
                  expect(recInfo.isCreatedByADS).to.eql(false);
                  expect(recInfo.isCreatedByOthers).to.eql(true);
                  expect(recInfo.isKnownToAds).to.eql(false);
                });

              // non-ADS record
              oApi.getRecordInfo({bibcode: 'sfasdfsdfsdfsdfsdf'})
                .done(function(recInfo) {
                  expect(recInfo.isCreatedByADS).to.eql(false);
                  expect(recInfo.isCreatedByOthers).to.eql(false);
                  expect(recInfo.isKnownToAds).to.eql(false);
                });

              oApi._checkIdsInADS.restore();
              oApi.updateDatabase.restore();

              done();
            });

        });
      });
    });
  });
});
