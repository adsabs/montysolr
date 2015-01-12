define([
    'js/components/generic_module',
    'js/mixins/dependon',
    'jquery',
    'js/modules/orcid/orcid_api',
    'js/services/localStorage',
    'js/modules/orcid/json2xml',
    'underscore',
    'xml2json',
    'js/bugutils/minimal_pubsub',
    'js/components/application',
    'js/modules/orcid/orcid_api_constants',
    'js/modules/orcid/orcid_model_notifier/module',
    'js/components/pubsub_events',
    '../../widgets/test_orcid_data/orcid_profile_data'
  ],

  function (
    GenericModule,
    Mixins,
    $,
    OrcidApi,
    LocalStorage,
    Json2XML,
    _,
    xml2json,
    MinimalPubsub,
    Application,
    OrcidApiConstants,
    OrcidNotifierModule,
    PubSubEvents,
    TestOrcidProfileData
  ) {


    describe("Orcid API service", function () {

      var app, minsub, beehive, notifier, localStorage, json2XML;
      beforeEach(function (done) {

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

        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            if (this.requestCounter % 2 === 0) {
              //return Test2();
            } else {
              //return Test1();
            }
          }
        }))({verbose: false});

        beehive = minsub.beehive;

        notifier = new OrcidNotifierModule();
        notifier.activate(beehive);
        notifier.initialize();

        localStorage = new LocalStorage();
        localStorage.activate(beehive);
        localStorage.initialize();

        json2XML = new Json2XML();
        json2XML.activate(beehive);
        json2XML.initialize();

        beehive.addService('OrcidModelNotifier', notifier);
        beehive.addService('LocalStorage', localStorage);
        beehive.addService('Json2Xml', json2XML);

        done();
      });

      var getUserProfileJson = function () {
        return $.xml2json(TestOrcidProfileData)['orcid-message']['orcid-profile'];
      };

      var getUserProfileXML = function(){
        return TestOrcidProfileData;
      };

      var getAuthJSON = function(){
        return {"access_token":"4378477a-34ea-4474-a8d7-bb0a52831b72","token_type":"bearer","expires_in":3599,"scope":"/orcid-profile/read-limited /orcid-works/create /orcid-works/update","orcid":"0000-0002-4800-0523","name":"zdenek heller"};
      };

      var setUserSession = function(){
        var LocalStorage = beehive.getService("LocalStorage");

        LocalStorage.setObject("userSession", {
          authData: getAuthJSON()
        });
      };

      var getOrcidApi = function(){
        var orcidApi = new OrcidApi();

        orcidApi.sendData = function(opts){

          var deferred = $.Deferred();

          var data = {};

          if (opts.url.indexOf('orcid-profile') > -1){
            data = getUserProfileXML();
          } else if (opts.url.indexOf('exchangeAuthCode') > -1){
            data = getAuthJSON();
          } else if (opts.url.indexOf('orcid-works') > -1){
            data = getUserProfileXML();
          }
          else{
            var some = '';
          }

          deferred.resolve(data);

          return deferred.promise();
        };

        orcidApi.activate(beehive);

        return orcidApi;
      }

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

      it('function should be called on event trigger', function(done){
        var orcidApi = getOrcidApi();

        var pubSub = beehive.getService('PubSub');
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

      it('should properly call getUserProfile', function(done){
        var orcidApi = getOrcidApi();

        setUserSession();

        orcidApi.getUserProfile().done(function(data){
          var orcidWorks = $.xml2json(data)['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"]["orcid-work"];

          expect(orcidWorks != undefined).to.be.true;

          done();
        });
      });

      it('should call refresh user profile after oauthCodeReceived', function(done){
        var orcidApi = getOrcidApi();

        var loginSuccessCalled = false;

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg){
          if (msg.msgType == OrcidApiConstants.Events.LoginSuccess){

            var orcidWorks = msg.data["orcid-activities"]["orcid-works"]["orcid-work"];

            expect(orcidWorks != undefined).to.be.true;

            loginSuccessCalled = true;

          }
        });

        orcidApi.oauthAuthCodeReceived('code', 'some ').done(function(){

          var LocalStorage = beehive.getService("LocalStorage");

          expect(LocalStorage.getObject("userSession") != undefined).to.true;

          expect(loginSuccessCalled).to.be.true;

          done();

        });
      });

      it('should empty LocalStorage.userSession when signOut is called', function(done){
        var orcidApi = getOrcidApi();

        orcidApi.signOut();

        var emptyUserSession = localStorage.getObject('userSession');

        expect(emptyUserSession.isEmpty === true).to.be.true;

        done();
      });

      it('should pass the getAdsIds', function(done){
        var orcidApi = getOrcidApi();
        var orcidWorks = getUserProfileJson()['orcid-activities']['orcid-works']['orcid-work'];

        var orcidWork_nonADS = orcidWorks[0];
        var orcidWork_AdsOne = orcidWorks[1];

        var adsIds = orcidApi.getAdsIds(orcidWork_nonADS);

        expect(adsIds != undefined).to.be.true;
        expect(adsIds.length).to.be.eq(0);

        adsIds = orcidApi.getAdsIds(orcidWork_AdsOne);

        expect(adsIds != undefined).to.be.true;
        expect(adsIds.length).to.be.eq(1);

        done();

      });

      it('should pass the isWorkFromADS', function(done){
        var orcidApi = getOrcidApi();
        var orcidWorks =  getUserProfileJson()['orcid-activities']['orcid-works']['orcid-work'];

        var orcidWork_nonADS = orcidWorks[0];
        var orcidWork_AdsOne = orcidWorks[1];

        expect(orcidApi.isWorkFromAds(orcidWork_nonADS)).to.false;
        expect(orcidApi.isWorkFromAds(orcidWork_AdsOne)).to.true;

        done();
      });

      it('should pass worcidWorks', function(done){
        setUserSession();

        var orcidApi = getOrcidApi();

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
            done();
          }
        });

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
      });

      it('replace all orcid works', function (done) {
        setUserSession();

        var orcidApi = getOrcidApi();

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
            done();
          }
        });

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
      });

      it('delete orcid works', function (done) {

        setUserSession();

        var orcidApi = getOrcidApi();


        orcidApi.getUserProfile()
          .done(function(data) {

            var orcidWorks = $.xml2json(data)['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"]["orcid-work"];

            var orcidWorkToDelete = orcidWorks[1];

            minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg) {
              if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
                done();
              }
            });

            orcidApi.deleteWorks([orcidWorkToDelete["$"]["put-code"]]);
          });
      });

      it('update orcid works', function (done) {
        setUserSession();

        var orcidApi = getOrcidApi();


        orcidApi.getUserProfile()
          .done(function(data) {

            var orcidWorks = $.xml2json(data)['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"]["orcid-work"];

            var orcidWorkToUpdate = orcidWorks[1];

            orcidWorkToUpdate["work-title"].title = "Testing publication 14X";

            minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg) {
              if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
                done();
              }
            });

            orcidApi.updateWorks([orcidWorkToUpdate]);
          });
      });

      it('should pass processOrcidAction - delete orcidData', function(done){
        var orcidApi = getOrcidApi();

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
            done();
          }
        });

        orcidApi.processOrcidAction({"actionType":"delete","model":{"putCode":"457068","publicationData":"","workExternalIdentifiers":[{"id":"2015GeoJI.200..917T","type":"bibcode"},{"id":"ads:10686818","type":"other-id"}],"workTitle":"Palaeosecular variation recorded by 9 ka to 2.5-Ma-old lavas from Martinique Island: new evidence for the La Palma aborted reversal ̃617 ka ago","workType":"book","workSourceUri":"","workSourceHost":"","shownContributors":["Tanty, Cyrielle","Carlut, Julie","Valet, Jean-Pierre"],"extraContributors":1,"isFromAds":true,"bibcode":"2015GeoJI.200..917T"},"modelType":"orcidData"});
      });
      it('should pass processOrcidAction - delete adsData', function(done){
        notifier.model.set('adsIdsWithPutCodeList', [{adsId : 'ads:9116735'}]);

        var orcidApi = getOrcidApi();

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
            done();
          }
        });

        orcidApi.processOrcidAction({"actionType":"delete","model":{"pubdate":"2015-07-00","links_data":["{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"],"pub":"Experimental Heat Transfer","volume":"28","id":"9116735","bibcode":"2015ExHT...28..344A","author":["Ardekani, M. A.","Farhani, F.","Mazidi, M."],"aff":["-","-","-"],"title":["Effects of Cross Wind Conditions on Efficiency of Heller Dry Cooling Tower"],"property":["REFEREED","ARTICLE"],"email":["-","-","-"],"[citations]":{"num_citations":0,"num_references":3},"identifier":"2015ExHT...28..344A","resultsIndex":0,"details":{"highlights":["Effects of Cross Wind Conditions on Efficiency of <em>Heller</em> Dry Cooling Tower"],"pub":"Experimental Heat Transfer"},"authorFormatted":["Ardekani, M. A.;","Farhani, F.;","Mazidi, M."],"num_citations":0,"formattedDate":"2015/07","links":{"list":[{"letter":"R","title":"References (3)","link":"/#abs/2015ExHT...28..344A/references"}],"data":[],"text":[{"openAccess":false,"title":"Publisher Article","link":"http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=2015ExHT...28..344A&link_type=EJOURNAL"}]},"emptyPlaceholder":false,"visible":true,"actionsVisible":true},"modelType":"adsData"});

      });

      it('should pass processOrcidAction - insert', function(done){
        var orcidApi = getOrcidApi();

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
            done();
          }
        });

        orcidApi.processOrcidAction({"actionType":"insert","model":{"pubdate":"2015-02-00","abstract":"Although 25-50 per cent of white dwarfs (WDs) display evidence for remnant planetary systems, their orbital architectures and overall sizes remain unknown. Vibrant close-in (≃1 R<SUB>☉</SUB>) circumstellar activity is detected at WDs spanning many Gyr in age, suggestive of planets further away. Here we demonstrate how systems with 4 and 10 closely packed planets that remain stable and ordered on the main sequence can become unpacked when the star evolves into a WD and experience pervasive inward planetary incursions throughout WD cooling. Our full-lifetime simulations run for the age of the Universe and adopt main-sequence stellar masses of 1.5, 2.0 and 2.5 M<SUB>☉</SUB>, which correspond to the mass range occupied by the progenitors of typical present-day WDs. These results provide (i) a natural way to generate an ever-changing dynamical architecture in post-main-sequence planetary systems, (ii) an avenue for planets to achieve temporary close-in orbits that are potentially detectable by transit photometry and (iii) a dynamical explanation for how residual asteroids might pollute particularly old WDs.","links_data":["{\"title\":\"\", \"type\":\"pdf\", \"instances\":\"\", \"access\":\"\"}","{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"],"pub":"Monthly Notices of the Royal Astronomical Society","volume":"447","keyword":["methods: numerical","celestial mechanics","minor planets","asteroids: general","planets and satellites: dynamical evolution and stability","protoplanetary discs","white dwarfs"],"property":["REFEREED","ARTICLE"],"id":"10601162","bibcode":"2015MNRAS.447.1053V","author":["Veras, Dimitri","Gänsicke, Boris T."],"aff":["; Department of Physics, University of Warwick, Coventry CV4 7AL, UK","Department of Physics, University of Warwick, Coventry CV4 7AL, UK"],"title":["Detectable close-in planets around white dwarfs through late unpacking"],"email":["d.veras@warwick.ac.uk","-"],"[citations]":{"num_citations":0,"num_references":149},"identifier":"2015MNRAS.447.1053V","resultsIndex":3,"details":{"highlights":[" Barnes <em>Heller</em> 2013). Fossati et al. (2012) has demonstrated that photosynthetic processes associated"],"pub":"Monthly Notices of the Royal Astronomical Society","shortAbstract":"Although 25-50 per cent of white dwarfs (WDs) display evidence for remnant planetary systems, their orbital architectures and overall sizes remain unknown. Vibrant close-in (≃1 R<SUB>☉</SUB>) circumstellar activity is detected at WDs spanning many Gyr in age, suggestive of planets further away. Here we demonstrate how systems with 4 and 10 closely packed planets that remain stable and ordered on the main sequence can become unpacked when the star evolves into a WD and experience pervasive inward planetary incursions throughout WD cooling. Our full-lifetime simulations run for the age of the Universe and adopt main-sequence stellar masses of 1.5, 2.0 and 2.5 M<SUB>☉</SUB>, which correspond to the mass range occupied by the progenitors of typical present-day WDs. These results provide (i) a natural way to generate an ever-changing dynamical architecture in post-main-sequence planetary systems, (ii) an avenue for planets to achieve temporary close-in orbits that are potentially detectable by transit photometry and (iii) a dynamical explanation for how residual asteroids might pollute particularly old WDs."},"authorFormatted":["Veras, Dimitri;","Gänsicke, Boris T."],"num_citations":0,"formattedDate":"2015/02","links":{"list":[{"letter":"R","title":"References (149)","link":"/#abs/2015MNRAS.447.1053V/references"}],"data":[],"text":[{"openAccess":false,"title":"Publisher PDF","link":"http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=2015MNRAS.447.1053V&link_type=ARTICLE"},{"openAccess":false,"title":"Publisher Article","link":"http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=2015MNRAS.447.1053V&link_type=EJOURNAL"}]},"emptyPlaceholder":false,"visible":true,"actionsVisible":true},"modelType":"adsData"});
      });

      it('should pass processOrcidAction - update', function(done){
        var orcidApi = getOrcidApi();

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg) {
          if (msg.msgType == OrcidApiConstants.Events.UserProfileRefreshed) {
            done();
          }
        });

        orcidApi.processOrcidAction({"actionType":"update","model":{"pubdate":"2015-07-00","links_data":["{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"],"pub":"Experimental Heat Transfer","volume":"28","id":"9116132","bibcode":"2015ExHT...28..344A","author":["Ardekani, M. A.","Farhani, F.","Mazidi, M."],"aff":["-","-","-"],"title":["Effects of Cross Wind Conditions on Efficiency of Heller Dry Cooling Tower"],"property":["REFEREED","ARTICLE"],"email":["-","-","-"],"[citations]":{"num_citations":0,"num_references":3},"identifier":"2015ExHT...28..344A","resultsIndex":0,"details":{"highlights":["Effects of Cross Wind Conditions on Efficiency of <em>Heller</em> Dry Cooling Tower"],"pub":"Experimental Heat Transfer"},"authorFormatted":["Ardekani, M. A.;","Farhani, F.;","Mazidi, M."],"num_citations":0,"formattedDate":"2015/07","links":{"list":[{"letter":"R","title":"References (3)","link":"/#abs/2015ExHT...28..344A/references"}],"data":[],"text":[{"openAccess":false,"title":"Publisher Article","link":"http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=2015ExHT...28..344A&link_type=EJOURNAL"}]},"emptyPlaceholder":false,"visible":true,"actionsVisible":true},"modelType":"adsData"});
      });



      it ('should display login dialog', function(done){
        var orcidApi = getOrcidApi();
        orcidApi.showLoginDialog();
        expect(orcidApi.loginWindow != undefined).to.be.true;

        minsub.subscribe(minsub.ORCID_ANNOUNCEMENT, function(msg){
          if (msg.msgType ==  OrcidApiConstants.Events.LoginCancelled ){
            done();
          }
        });

        orcidApi.loginWindow.close();
      })

    });
  });