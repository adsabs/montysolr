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

          console.log(apiRequest.toJSON(), apiRequest.get('query').get('q'));

          return sendRequest(url, options, data);
        });
      }

      return server;
    };

    describe("Orcid API service (orcid_api.spec.js)", function () {

      describe("OAuth", function() {
        var minsub, beehive;
        beforeEach(function (done) {
          minsub = new (MinimalPubsub.extend({
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
          beehive = minsub.beehive;
          done();
        });

        afterEach(function(done) {
          //this.server.restore();
          done();
        });

        var getOrcidApi = function() {
          beehive.addObject('DynamicConfig', {
            orcidClientId: 'APP-P5ANJTQRRTMA6GXZ',
            orcidApiEndpoint: 'https://api.orcid.org',
            orcidRedirectUrlBase: 'http://localhost:8000',
            orcidLoginEndpoint: 'https://api.orcid.org/oauth/authorize'
          });


          var oModule = new OrcidModule();
          oModule.activate(beehive);
          return beehive.getService('OrcidApi');
        };
        
        it("has methods to extract access code", function() {
          var oApi = getOrcidApi();
          // it receives window.location.search
          expect(oApi.getUrlParameter('code', '?foo=bar&code=H1trXI')).to.eql('H1trXI');
          expect(oApi.hasExchangeCode('?foo=bar&code=H1trXI')).to.eql(true);
          expect(oApi.getExchangeCode('?foo=bar&code=H1trXI')).to.eql('H1trXI');
        });

        it("can exchange code for access_token (auth data)", function(done) {
          var oApi = getOrcidApi();
          var r = oApi.getAccessData('secret');
          expect(r.done).to.be.defined;
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

          var oApi = getOrcidApi();

          beehive.getService("PubSub").publish = sinon.spy();

          oApi.signIn();

          expect(JSON.stringify(beehive.getService("PubSub").publish.args[0])).to.eql('[{},"[App]-Exit",{"type":"orcid","url":"https://api.orcid.org/oauth/authorize?scope=/orcid-profile/read-limited%20/orcid-works/create%20/orcid-works/update&response_type=code&access_type=offline&show_login=true&client_id=APP-P5ANJTQRRTMA6GXZ&redirect_uri=http%3A%2F%2Flocalhost%3A8000%2F%23%2Fuser%2Forcid"}]');
          expect(JSON.stringify(beehive.getService("PubSub").publish.args[1])).to.eql('[{},"[PubSub]-Orcid-Announcement","login"]')
        });

        it("signOut forgets authentication details", function() {
          var oApi = getOrcidApi();
          expect(oApi.hasAccess()).to.be.eql(false);
          oApi.authData = {foo: 'bar'};
          expect(oApi.hasAccess()).to.be.eql(true);
          oApi.signOut();
          expect(oApi.hasAccess()).to.be.eql(false);

          oApi.authData = {expires: new Date().getTime() + 100};
          expect(oApi.hasAccess()).to.be.eql(true);
          oApi.authData = {expires: new Date().getTime() - 100};
          expect(oApi.hasAccess()).to.be.eql(false);
        });

      });

      describe("Orcid Actions", function() {

        var minsub, beehive, server;

        var getOrcidApi = function() {
          minsub = new MinimalPubsub({ verbose: false });
          beehive = minsub.beehive;
          beehive.addObject('DynamicConfig', {
            orcidClientId: 'APP-P5ANJTQRRTMA6GXZ',
            orcidApiEndpoint: 'https://api.orcid.org',
            orcidRedirectUrlBase: 'http://localhost:8000'
          });
          var oModule = new OrcidModule();
          oModule.activate(beehive);
          var oApi = beehive.getService('OrcidApi');
          oApi.saveAccessData({
            "access_token":"4274a0f1-36a1-4152-9a6b-4246f166bafe",
            "orcid":"0000-0001-8178-9506"
          });
          return oApi;
        };

        afterEach(function () {
          server && server.restore && server.restore();
        });

        it('should be GenericModule', function (done) {
          expect(new OrcidApi() instanceof GenericModule).to.equal(true, 'is GenericModule');
          expect(new OrcidApi() instanceof OrcidApi).to.equal(true, 'is OrcidApi');
          done();
        });

        it("exports hardened interface", function() {
          var oApi = getOrcidApi();
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
           'addWorks',
           'deleteWork',
           'updateWork',
           'getWork',
           'getWorks',
          ]);
        });

        it('getUserProfile', function (done) {
          var oApi = getOrcidApi();
          server = createOrcidServer(oApi);
          oApi.getUserProfile()
            .done(function (profile) {
              expect(profile instanceof Profile).to.equal(true, 'returns instance of Profile');
              done();
            });
        });

        it('getWork', function (done) {
          var oApi = getOrcidApi();
          server = createOrcidServer(oApi);
          oApi.getWork(99999)
            .done(function (work) {
              expect(work instanceof Work).to.equal(true, 'returns instance of Work');
              done();
            });
        });

        it('addWorks', function (done) {
          var oApi = getOrcidApi();
          server = createOrcidServer(oApi);
          var mockNewWork = {
            "title": {
              "title": {
                "value": "TEST TITLE"
              }
            },
            "publication-date": {
              "year": {
                "value": "2017"
              },
              "month": {
                "value": "03"
              }
            },
            "journal-title": {
              "value": "TEST JOURNAL"
            }
          };
          oApi.addWorks([mockNewWork])
            .done(function (bulkWork, status, xhr) {
              expect(xhr.status).to.equal(201, 'got 201 response code');
              expect(JSON.stringify(bulkWork)).to.equal(
                JSON.stringify({ bulk: [{ work: mockNewWork }]}),
                'get back a bulk work on success'
              );
              done();
            });
        });

        it('deleteWork', function (done) {
          var oApi = getOrcidApi();
          server = createOrcidServer(oApi);
          oApi.deleteWork(99999)
            .done(function (data, status, xhr) {
              expect(xhr.status).to.equal(204, 'get 204 status code');
              expect(data).to.equal(undefined, 'got no content');
              done();
            });
        });

        it('updateWork', function (done) {
          var oApi = getOrcidApi();
          server = createOrcidServer(oApi);
          var orcidWork = new Work(helpers.getMock('work')).getAsOrcid();
          oApi.updateWork(orcidWork)
            .done(function (work) {
              expect(JSON.stringify(work)).to.equal(JSON.stringify(orcidWork),
                'get our updated work back');
              done();
            });
        });

        it('getRecordInfo', function () {
          var oApi = getOrcidApi();
          server = createOrcidServer(oApi, minsub);
          sinon.stub(oApi, 'needsUpdate', _.constant(false));

          var db = {
            "bibcode:foo": {
              "sourcedByADS": true,
              "putcode": 889362,
              "idx": 0
            },
            "bibcode:bar": {
              "sourcedByADS": false,
              "putcode": 878658,
              "idx": 1
            },
            "doi:boo": {
              "sourcedByADS": true,
              "putcode": 880867,
              "idx": 1
            },
            "doi:baz": {
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


        // it('getExternalIds and isWorkFromADS', function(done){
        //   var oApi = getOrcidApi();
        //   oApi.getUserProfile().done(function (profile) {
        //     var works = profile.getWorks();
        //
        //   });
        //   oApi.getWork()
        //     .done(function(res) {
        //
        //       var works = _(res).map('work-summary').map('0').value();
        //
        //       expect(oApi.isWorkCreatedByADS(works[0])).to.be.eql(true);
        //       expect(oApi.isWorkCreatedByADS(works[1])).to.be.eql(true);
        //       expect(oApi.isWorkCreatedByADS(works[2])).to.be.eql(true);
        //       expect(oApi.isWorkCreatedByADS(works[3])).to.be.eql(false);
        //       expect(oApi.isWorkCreatedByADS(works[4])).to.be.eql(false);
        //       expect(oApi.isWorkCreatedByADS({})).to.be.eql(false);
        //
        //       done();
        //     });
        // });
   
        // it("has methods to query status of a record", function(done) {
        //   var oApi = getOrcidApi();
        //   server = createOrcidServer(oApi, minsub);
        //   sinon.spy(oApi, 'updateDatabase');
        //   sinon.stub(oApi, '_checkIdsInADS', function (query) {
        //     var $dd = $.Deferred().resolve({
        //       "bibcode:2018cnsns..56..296s": "2018cnsns..56..296s",
        //       "doi:10.1016/j.cnsns.2017.08.013": "2018cnsns..56..296s",
        //       "bibcode:2018cnsns..56..270q": "2018cnsns..56..270q",
        //       "doi:10.1016/j.cnsns.2017.08.014": "2018cnsns..56..270q"
        //     }).promise();
        //   });
        //
        //   // for testing purpose, force split into many queries
        //   oApi.maxQuerySize = 2;
        //
        //
        //
        //   oApi.getRecordInfo({bibcode: '2018cnsns..56..296s'})
        //     .done(function (recInfo) {
        //
        //       expect(oApi._checkIdsInADS.called).to.eql(true);
        //       expect(oApi._checkIdsInADS.calledTwice).to.eql(true);
        //       expect(oApi._checkIdsInADS.args[0][0].get('q')).eql(["alternate_bibcode:(\"bibcode-foo\" OR \"test-bibcode\")"]);
        //       expect(oApi._checkIdsInADS.args[1][0].get('q')).eql(["bibcode:(\"bibcode-foo\" OR \"test-bibcode\")"]);
        //
        //
        //       expect(recInfo.isCreatedByADS).to.eql(true);
        //       expect(recInfo.isCreatedByOthers).to.eql(false);
        //
        //       // this one should return immediately
        //       oApi.getRecordInfo({bibcode: 'bibcode-foo'})
        //         .done(function(recInfo) {
        //           expect(recInfo.isCreatedByADS).to.eql(false);
        //           expect(recInfo.isCreatedByOthers).to.eql(true);
        //           expect(recInfo.isKnownToAds).to.eql(true);
        //         });
        //
        //       oApi.getRecordInfo({doi: '10.1126/science.276.5309.88'}) // doi of bibcode-foo
        //         .done(function(recInfo) {
        //           expect(recInfo.isCreatedByADS).to.eql(false);
        //           expect(recInfo.isCreatedByOthers).to.eql(true);
        //           expect(recInfo.isKnownToAds).to.eql(true);
        //         });
        //
        //       oApi.getRecordInfo({doi: '10.1103/physrevlett.84.3823'}) // test-bibcode
        //         .done(function(recInfo) {
        //           expect(recInfo.isCreatedByADS).to.eql(true);
        //           expect(recInfo.isCreatedByOthers).to.eql(false);
        //           expect(recInfo.isKnownToAds).to.eql(true);
        //         });
        //
        //       oApi.getRecordInfo({bibcode: '1997Sci...276...88V'}) // alternate bibcode of bibcode-foo
        //         .done(function(recInfo) {
        //           expect(recInfo.isCreatedByADS).to.eql(false);
        //           expect(recInfo.isCreatedByOthers).to.eql(true);
        //           expect(recInfo.isKnownToAds).to.eql(true);
        //         });
        //
        //       // found by one of the queries, but could not be mapped to bibcode
        //       // this should not normally be happening, but i've added the logic
        //       // to accomodate it - just in case...
        //       oApi.getRecordInfo({bibcode: '2015CeMDA.tmp....1D'})
        //         .done(function(recInfo) {
        //           expect(recInfo.isCreatedByADS).to.eql(false);
        //           expect(recInfo.isCreatedByOthers).to.eql(true);
        //           expect(recInfo.isKnownToAds).to.eql(false);
        //         });
        //
        //       // non-ADS record
        //       oApi.getRecordInfo({bibcode: 'sfasdfsdfsdfsdfsdf'})
        //         .done(function(recInfo) {
        //           expect(recInfo.isCreatedByADS).to.eql(false);
        //           expect(recInfo.isCreatedByOthers).to.eql(false);
        //           expect(recInfo.isKnownToAds).to.eql(false);
        //         });
        //
        //       oApi._checkIdsInADS.restore();
        //       oApi.updateDatabase.restore();
        //
        //       done();
        //     });
        //
        // });

        // it("updateOrcid(add)", function(done) {
        //   var oApi = getOrcidApi();
        //   sinon.spy(oApi, 'addWorks');
        //   sinon.spy(oApi, 'updateWorks');
        //   sinon.spy(oApi, 'deleteWorks');
        //   sinon.spy(oApi, 'updateDatabase');
        //
        //   oApi.updateOrcid('add', {title: 'foo', bibcode: 'bar'})
        //     .done(function(recInfo) {
        //       expect(recInfo.isCreatedByADS).to.eql(true);
        //       expect(oApi.addWorks.called).to.eql(true);
        //       expect(oApi.updateWorks.called).to.eql(false);
        //       expect(oApi.deleteWorks.called).to.eql(false);
        //       expect(oApi.updateDatabase.called).to.eql(true);
        //       done();
        //     });
        // });
        //
        // it("updateOrcid(update)", function(done) {
        //   var oApi = getOrcidApi();
        //   sinon.spy(oApi, 'addWorks');
        //   sinon.spy(oApi, 'updateWorks');
        //   sinon.spy(oApi, 'deleteWorks');
        //   sinon.spy(oApi, 'updateDatabase');
        //
        //   oApi.updateDatabase()
        //     .done(function() {
        //       oApi.updateDatabase.reset();
        //
        //       oApi.updateOrcid('update', {title: 'foo', bibcode: 'test-bibcode'})
        //         .done(function(recInfo) {
        //           expect(recInfo.isCreatedByADS).to.eql(true);
        //           expect(oApi.addWorks.called).to.eql(false);
        //           expect(oApi.updateWorks.called).to.eql(true);
        //           expect(oApi.deleteWorks.called).to.eql(true);
        //           expect(oApi.updateDatabase.called).to.eql(true);
        //           done();
        //         });
        //     })
        //
        // });
      });
    });
  });
