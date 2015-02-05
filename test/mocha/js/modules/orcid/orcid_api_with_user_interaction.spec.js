define([
    'js/components/generic_module',
    'js/mixins/dependon',
    'jquery',
    'js/modules/orcid/orcid_api',
    'underscore',
    'xml2json',
    'js/components/application',
    'js/modules/orcid/orcid_api_constants',
    'js/components/pubsub_events'
  ],

  function (
    GenericModule,
    Mixins,
    $,
    OrcidApi,
    _,
    xml2json,
    Application,
    OrcidApiConstants,
    PubSubEvents
    ) {


    describe("Orcid API service with user interaction", function () {
      this.timeout(5000);

      var app, beeHive;
      before(function(done) {
        app = new Application();
        var config = {
          core: {
            services: {
              PubSub: 'js/services/pubsub',
              Api: 'js/services/api',
              LocalStorage: 'js/services/localStorage',
              OrcidApi: 'js/modules/orcid/orcid_api',
              Json2Xml: 'js/modules/orcid/json2xml'
            },
            objects: {
              QueryMediator: 'js/components/query_mediator'
            }
          },
          widgets: {
          }
        };

        app.loadModules(config)
          .done(function() {
            app.activate();
            beeHive = app.getBeeHive();
            done();
          });

      });


      it('input to spec is not undefined', function(done){

        expect(OrcidApi != undefined).to.be.true
        expect(OrcidApiConstants != undefined).to.be.true

        done();

      });
      it('should be GenericModule', function (done) {
        expect(new OrcidApi()).to.be.an.instanceof(GenericModule);
        expect(new OrcidApi()).to.be.an.instanceof(OrcidApi);
        done();
      });

      it('should have empty url', function (done) {
        var orcidApi = new OrcidApi();
        expect(orcidApi.orcidProxyUri == '').to.be.true;
        done();
      });

      it('should trigger success over pubsub events', function(done){
        var orcidApi = new OrcidApi();

        var pubSub = beeHive.getService('PubSub');
        var pubSubKey = pubSub.getPubSubKey();

        pubSub.subscribeOnce(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.LoginSuccess) {
            expect(msg.dummy == 'dummy').to.be.true;
            done();
          }
        });

        pubSub.publish(pubSubKey, pubSub.ORCID_ANNOUNCEMENT, {msgType: OrcidApiConstants.Events.LoginSuccess, dummy:'dummy'});
      });

      it('function should be called on event trigger', function(done){
        var orcidApi = new OrcidApi();
        orcidApi.activate(beeHive);

        var pubSub = beeHive.getService('PubSub');
        var pubSubKey = pubSub.getPubSubKey();

        orcidApi.signOut = function(){};
        var spy = sinon.spy(orcidApi, "signOut");
        pubSub.publish(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, { msgType: OrcidApiConstants.Events.SignOut });
        expect(spy.called).to.be.ok;

        orcidApi.showLoginDialog = function(){};
        var spy = sinon.spy(orcidApi, "showLoginDialog");
        pubSub.publish(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, { msgType: OrcidApiConstants.Events.LoginRequested });
        expect(spy.called).to.be.ok;

        orcidApi.processOrcidAction = function(){};
        var spy = sinon.spy(orcidApi, "processOrcidAction");
        pubSub.publish(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, { msgType: OrcidApiConstants.Events.OrcidAction, dummy:'dummy'});
        expect(spy.called).to.be.ok;

        done();
      });

      it('should delete userSession data on SignOut', function(done){
        var orcidApi = new OrcidApi();// beeHive.getService("OrcidApi");
        orcidApi.activate(beeHive);

        var LocalStorage = beeHive.getService("LocalStorage");

        LocalStorage.setObject('userSession', {dummy: 'data'});

        var userSession = LocalStorage.getObject("userSession");

        expect(userSession).to.be.an('object');

        orcidApi.signOut();

        userSession = LocalStorage.getObject("userSession");

        expect(userSession.isEmpty).to.be.true;

        done();
      });

      it('process full page oauth redirect', function(done) {
        var orcidApi = new OrcidApi();
        orcidApi.activate(beeHive);
        var pubSub = beeHive.getService('PubSub');
        var pubSubKey = pubSub.getPubSubKey();

        orcidApi.oauthAuthCodeReceived = function (code, redirectUri, _that) {
          done();
        };

        pubSub.publish(pubSubKey, PubSubEvents.APP_LOADED);
      });

      it('show login dialog', function (done) {
        var orcidApi = new OrcidApi();
        orcidApi.activate(beeHive);
        orcidApi.showLoginDialog();

        var pubSub = beeHive.getService('PubSub');
        var pubSubKey = pubSub.getPubSubKey();

        pubSub.subscribe(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.LoginSuccess) {

            var LocalStorage = beeHive.getService("LocalStorage");
            var userSession = LocalStorage.getObject("userSession");

            expect(userSession.orcidProfile).to.be.an('object');
            expect(userSession.authData).to.be.an('object');

            done();
          }
        });
      });

      it('should cancel login', function (done) {
        var orcidApi = new OrcidApi();
        orcidApi.activate(beeHive);
        orcidApi.showLoginDialog();

        var pubSub = beeHive.getService('PubSub');
        var pubSubKey = pubSub.getPubSubKey();

        pubSub.subscribeOnce(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.LoginCancelled) {
            done();
          }
        });
      });

      it('add orcid works', function (done) {
        var orcidApi = new OrcidApi();
        orcidApi.activate(beeHive);

        orcidApi.addWorks({
            "orcid-message": {
              "$": {
                "xmlns": "http://www.orcid.org/ns/orcid"
              },
              "message-version": "1.1",
              "orcid-profile": {
                "orcid-activities": {
                  "$": {},
                  "orcid-works": {
                    "orcid-work": [
                      {
                        "work-title": {
                          "$": {},
                          "title": "Testing publication 1"
                        },
                        "work-type": "test"
                      },
                      {
                        "work-title": {
                          "$": {},
                          "title": "Testing publication 2"
                        },
                        "work-type": "test"
                      }
                    ]
                  }
                }
              }
            }
          });

        var pubSub = beeHive.getService('PubSub');
        var pubSubKey = pubSub.getPubSubKey();

        pubSub.subscribeOnce(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
            done();
          }
        });
      });

      it('replace all orcid works', function (done) {
        var orcidApi = new OrcidApi();
        orcidApi.activate(beeHive);

        orcidApi.replaceAllWorks({
            "orcid-message": {
              "$": {
                "xmlns": "http://www.orcid.org/ns/orcid"
              },
              "message-version": "1.1",
              "orcid-profile": {
                "orcid-activities": {
                  "$": {},
                  "orcid-works": {
                    "orcid-work": [
                      {
                        "work-title": {
                          "$": {},
                          "title": "Testing publication 2"
                        },
                        "work-type": "test"
                      },
                      {
                        "work-title": {
                          "$": {},
                          "title": "Testing publication 13"
                        },
                        "work-type": "test"
                      },
                      {
                        "work-title": {
                          "$": {},
                          "title": "Testing publication 14"
                        },
                        "work-type": "test",
                        "work-external-identifiers": [
                          {
                            "work-external-identifier": {
                              "work-external-identifier-type": 'other-id',
                              "work-external-identifier-id": 'ads:6789'
                            }
                          }
                        ]
                      },
                      {
                        "work-title": {
                          "$": {},
                          "title": "Testing publication 15"
                        },
                        "work-type": "test",
                        "work-external-identifiers": [
                          {
                            "work-external-identifier": {
                              "work-external-identifier-type": 'other-id',
                              "work-external-identifier-id": 'ads:12345'
                            }
                          }
                        ]
                      }
                    ]
                  }
                }
              }
            }
          });

        var pubSub = beeHive.getService('PubSub');
        var pubSubKey = pubSub.getPubSubKey();

        pubSub.subscribeOnce(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
            done();
          }
        });
      });

      it('delete orcid works', function (done) {
        var orcidApi = new OrcidApi();
        orcidApi.activate(beeHive);


        orcidApi.getUserProfile()
          .done(function(data) {

            var orcidWorks = $.xml2json(data)['#document']['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"]["orcid-work"];

            var orcidWorkToDelete = orcidWorks.filter(function(item) {
              return item["work-title"].title == "Testing publication 15";
            })[0];

            orcidApi.deleteWorks([orcidWorkToDelete["$"]["put-code"]]);

            var pubSub = beeHive.getService('PubSub');
            var pubSubKey = pubSub.getPubSubKey();

            pubSub.subscribeOnce(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, function(msg) {
              if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {

                var orcidWorks = msg.data["orcid-activities"]["orcid-works"]["orcid-work"];

                orcidWorks = Array.isArray(orcidWorks) ? orcidWorks : [orcidWorks];

                orcidWorks = orcidWorks.filter(function(item) {
                  return orcidApi.isWorkFromAds(item);
                });

                var adsId = orcidWorks[0]["work-external-identifiers"]["work-external-identifier"]["work-external-identifier-id"];

                expect(adsId == "ads:6789").to.be.true;

                done();
              }
            });
          });
      });

      it('update orcid works', function (done) {
        var orcidApi = new OrcidApi();
        orcidApi.activate(beeHive);


        orcidApi.getUserProfile()
          .done(function(data) {

            var orcidWorks = $.xml2json(data)['#document']['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"]["orcid-work"];

            var orcidWorkToUpdate = orcidWorks.filter(function(item) {
              return item["work-title"].title == "Testing publication 14";
            })[0];

            orcidWorkToUpdate["work-title"].title = "Testing publication 14X";

            orcidApi.updateWorks([orcidWorkToUpdate]);

            var pubSub = beeHive.getService('PubSub');
            var pubSubKey = pubSub.getPubSubKey();

            pubSub.subscribeOnce(pubSubKey, PubSubEvents.ORCID_ANNOUNCEMENT, function(msg) {
              if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {

                var orcidWorks = msg.data["orcid-activities"]["orcid-works"]["orcid-work"];

                var updatedOrcidWorks = orcidWorks.filter(function(item) {
                  return item["work-title"].title == "Testing publication 14X";
                });

                expect(updatedOrcidWorks.length == 1).to.be.true;

                var adsId = updatedOrcidWorks[0]["work-external-identifiers"]["work-external-identifier"]["work-external-identifier-id"];

                expect(adsId == "ads:6789").to.be.true;

                done();
              }
            });
          });
      });
    });
  }
);