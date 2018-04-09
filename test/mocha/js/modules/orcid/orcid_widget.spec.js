define([
    'marionette',
    'backbone',
    'jquery',
    'js/bugutils/minimal_pubsub',
    'js/modules/orcid/widget/widget',
    'js/widgets/list_of_things/widget',
    'js/components/json_response',
    'js/modules/orcid/module',
    'js/components/api_query',
    'js/modules/orcid/profile',
    './helpers'
  ],
  function (
    Marionette,
    Backbone,
    $,
    MinimalPubsub,
    OrcidWidget,
    ListOfThingsWidget,
    JsonResponse,
    OrcidModule,
    ApiQuery,
    Profile,
    helpers
    ) {

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

    describe("Orcid Widget (orcid_widget.spec.js)", function () {
      // this is useful if you want to test widget getting data from Orcid
      // it simulates pubsub response
      var orcidMode;
      beforeEach(function (done) {
        var minsub = new (MinimalPubsub.extend({
          request: function(apiRequest) {

            var target = apiRequest.get('target');
            var opts = apiRequest.get('options');

            if (target.indexOf('/orcid-profile') > -1) {
              return defaultResponse(); // default orcid
            }
            else {
              return {
                "responseHeader": {
                  "status": 0,
                  "QTime": 2,
                  "params": {
                    "fl": "id,bibcode,alternate_bibcode,doi",
                    "indent": "true",
                    "q": "doi:\"*\" and alternate_bibcode:2015*",
                    "_": "1427847655704",
                    "wt": "json",
                    "rows": "5"
                  }
                },
                "response": {
                  "numFound": 2441,
                  "start": 0,
                  "docs": [
                    {
                      "alternate_bibcode": [
                        "2015arXiv150105026H"
                      ],
                      "doi": [
                        "10.1103/PhysRevLett.84.3823"
                      ],
                      "id": "5796418",
                      "bibcode": "test-bibcode"
                    },
                    {
                      "alternate_bibcode": [
                        "bibcode-foo"
                      ],
                      "doi": [
                        "10.1126/science.276.5309.88"
                      ],
                      "id": "1135646",
                      "bibcode": "1997Sci...276...88V"
                    },
                    {
                      "alternate_bibcode": [
                        "2015CeMDA.tmp....1D"
                      ],
                      "doi": [
                        "10.1007/s10569-014-9601-4"
                      ],
                      "id": "10734037",
                      "bibcode": "2015CeMDA.121..301D"
                    },
                    {
                      "doi": [
                          "10.1126/duplica.276.5309.88"
                      ],
                      "bibcode": "dup-bibcode"
                    }
                  ]
                }
              };
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

      afterEach(function (done) {
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });


      it("returns OrcidWidget object", function (done) {
        expect(new OrcidWidget()).to.be.instanceof(OrcidWidget);
        expect(new OrcidWidget()).to.be.instanceof(ListOfThingsWidget);
        done();
      });

      var _getWidget = function (beehive) {
        var widget = new OrcidWidget();

        widget.activate(beehive.getHardenedInstance());
        return widget;
      };

      var defaultResponse = function () {
        return helpers.getMock('profile');
      };


      it("Should display records coming from ORCID and has methods to filter/sort them", function (done) {

        var orcidMode = true;

        var fakeUser = {
          getHardenedInstance : function(){return this},
          getLocalStorage : function(){return { perPage : 50 }},
          isOrcidModeOn: function() {
            return orcidMode;
          },
        };
        this.minsub.beehive.removeObject("User");
        this.minsub.beehive.addObject("User", fakeUser);

        var orcidApi = getOrcidApi(this.minsub.beehive);
        sinon.stub(orcidApi, 'hasAccess', function() {return true});
        var widget = _getWidget(this.minsub.beehive);

        var profile = new Profile(helpers.getMock('profile'));
        var response = new JsonResponse(profile.toADSFormat());
        response.setApiQuery(new ApiQuery(response.get('responseHeader.params')));

        widget.processResponse(response);

        var $w = widget.render().$el;
        $('#test').append($w);

        expect(widget.collection.models.length).to.eql(3);
        expect(widget.view.children.findByIndex(0).$el.find('div.identifier').text().trim()).to.eql('2018CNSNS..56..270Q');
        expect(widget.view.children.findByIndex(1).$el.find('div.identifier').text().trim()).to.eql('2018CNSNS..56..296S'); // found through doi, alternate_bibcode:bibcode-foo

        orcidMode = true;

        widget.collection.models[0].set({ title: 'Tecnologias XXX', orcid: { provenance: 'ads' }});
        widget.collection.models[1].set({ title: 'ADS 2.0', orcid: { provenance: 'others' }});
        widget.collection.models[2].set({ title: 'External article', orcid: { provenance: null }});

        // filter by provenance
        widget.update({filterBy: 'ads'});
        expect(widget.collection.models.length).to.eql(1);
        expect(widget.collection.models[0].get('title')).to.eql('Tecnologias XXX');

        widget.update({filterBy: 'others'});
        expect(widget.collection.models.length).to.eql(1);
        expect(widget.collection.models[0].get('title')).to.eql('ADS 2.0');

        widget.update({filterBy: null});
        expect(widget.collection.models.length).to.eql(1);
        expect(widget.collection.models[0].get('title')).to.eql('External article');

        widget.update(); // show all
        expect(widget.collection.models.length).to.eql(3);

        // sort
        widget.update({sortBy: 'identifier'});
        expect(widget.collection.models.length).to.eql(3);
        expect(widget.collection.models[0].get('title')).to.eql('Tecnologias XXX');
        expect(widget.collection.models[1].get('title')).to.eql('ADS 2.0');
        expect(widget.collection.models[2].get('title')).to.eql('External article');

        // sort and filter
        widget.update({filterBy: ['ads', 'others'], sortBy: 'title'});
        expect(widget.collection.models[0].get('title')).to.eql('ADS 2.0');
        expect(widget.collection.models[1].get('title')).to.eql('Tecnologias XXX');

        done();
      });

      it("should show a loading view before orcid profile is loaded", function(){

        var orcidApi = getOrcidApi(this.minsub.beehive);
        orcidApi.saveAccessData({access: true});
        orcidApi.getUserProfile = function() {
          return $.Deferred()
            .resolve(new Profile(helpers.getMock('profile'))).promise();
        };

        orcidMode = true;

        var fakeUser = {
          getHardenedInstance : function(){return this},
          getLocalStorage : function(){return { perPage : 50 }},
          isOrcidModeOn: function() {
            return orcidMode;
          }
        };
        this.minsub.beehive.removeObject("User");
        this.minsub.beehive.addObject("User", fakeUser);

        var widget = _getWidget(this.minsub.beehive);
        widget.getBeeHive().getService('OrcidApi').hasAccess = _.constant(true);

        var $w = widget.render().$el;
        $('#test').append($w);

        widget.onShow();

        expect($("#test .s-results-control-row-container").text().replace(/\s{2,}/g, "")).to.eql("My ORCID Papers(3)ORCID Username: Tim HostetlerORCID ID: 0000-0001-9790-1275To share this list of your ORCID papers:Search for your ORCID ID in ADS (orcid:0000-0001-9790-1275)You can then share the url of your results:https://ui.adsabs.harvard.edu/#search/q=orcid%3A0000-0001-9790-1275&sort=date+descPlease note that claims take up to 24 hours to be indexed in ADS.To claim papers in ORCID and add to this listclick here to search your name in ADSLearn more about using ORCID with ADS")
      });

      it("should allow the user to search in ADS when search button is clicked", function(done){

        var orcidApi = getOrcidApi(this.minsub.beehive);
        orcidApi.saveAccessData({access: true});
        orcidApi.getUserProfile = function() {
          return $.Deferred()
            .resolve(new Profile(helpers.getMock('profile'))).promise();
        };

        orcidApi.getADSUserData = sinon.spy(function(){
          var d = $.Deferred();

          d.resolve({ nameVariations : ["Name, Variation 1", "Name, Variation 2"]});
          return d.promise();

        });

        var fakeUser = {
          getHardenedInstance : function(){return this},
          getLocalStorage : function(){return { perPage : 50 }},
          isOrcidModeOn: function() {
            return orcidMode;
          },
        };
        this.minsub.beehive.removeObject("User");
        this.minsub.beehive.addObject("User", fakeUser);

        var widget = _getWidget(this.minsub.beehive);
        widget.getBeeHive().getService('OrcidApi').hasAccess = _.constant(true);

        var publishStub = sinon.stub(widget.getPubSub(), "publish");

        widget.onShow();
        setTimeout(function() {

          var $w = widget.render().$el;
          $('#test').append($w);

         $("button.search-author-name").click();

          expect(publishStub.args[0][0]).to.eql("[PubSub]-New-Query");
          expect(publishStub.args[0][1].toJSON()).to.eql({
            "q": [
              "author:(\"Name, Variation 1\" OR \"Name, Variation 2\" OR \"Hostetler, Tim\")"
            ],
            "sort": ["date desc"]
          });

          done();
        }, 200);

      });

      it.skip("should load ORCID when onShow is called", function(done) {
        var orcidMode = true;
        var orcidApi = getOrcidApi();
        orcidApi.saveAccessData({access: true});
        orcidApi.getUserProfile = function() {
          return $.Deferred()
            .resolve(new Profile(helpers.getMock('profile'))).promise();
        };
        orcidApi.sendData = function () {
          return $.Deferred().resolve({}).promise();
        };


        var fakeUser = {
          getHardenedInstance : function(){return this},
          getLocalStorage : function(){return { perPage : 50 }},
          isOrcidModeOn: function() {
            return orcidMode;
          },
        };
        this.minsub.beehive.removeObject("User");
        this.minsub.beehive.addObject("User", fakeUser);

        var wid = _getWidget(this.minsub.beehive);
        wid.activate(this.minsub.beehive.getHardenedInstance());
        wid.onShow();
        setTimeout(function() {
          var $w = wid.render().$el;
          $('#test').append($w);

          expect(wid.collection.models.length).to.eql(3);
          expect(wid.view.children.findByIndex(0).$el.find('div.identifier').text().trim()).to.eql('2018CNSNS..56..270Q');
          expect(wid.view.children.findByIndex(2).$el.find('div.identifier').text().trim()).to.eql(''); // since we don't make api resolution, bibcode cannot be found

          done();
        }, 200);
      });

      it.skip("should know how to de-duplicate records", function() {
        var data = {
          "responseHeader": {
            "params": {
              "orcid": "0000-0001-8178-9506"
            }
          },
          "response": {
            "numFound": 2,
            "start": 0,
            "docs": [
              {
                "bibcode": "dup-bibcode",
                "putcode": "469257",
                "title": "Tecnologias XXX",
                "visibility": "PUBLIC",
                "formattedDate": "2014/11",
                "pub": "El Profesional de la Informacion",
                "abstract": null,
                "author": [],
                "source_name": "nasa ads"
              },
              {
                "doi": "10.1126/duplica.276.5309.88",
                "putcode": "469256",
                "title": "Tecnologias XXX",
                "visibility": "PUBLIC",
                "formattedDate": "2014/11",
                "pub": "El Profesional de la Informacion",
                "abstract": null,
                "author": [],
                "source_name": "external source"
              }
            ]
          }
        };

        var fakeUser = {
          getHardenedInstance : function(){return this},
          getLocalStorage : function(){return { perPage : 50 }},
          isOrcidModeOn: function() {
            return orcidMode;
          },
        };
        this.minsub.beehive.addObject("User", fakeUser);

        var orcidApi = getOrcidApi();
        sinon.stub(orcidApi, 'hasAccess', function() {return true});
        var widget = _getWidget(this.minsub.beehive);

        var response = new JsonResponse(data);
        response.setApiQuery(new ApiQuery(response.get('responseHeader.params')));

        widget.processResponse(response);

        var $w = widget.render().$el;
        $('#test').append($w);

        expect(widget.collection.models.length).to.eql(2);
        expect(widget.view.children.findByIndex(0).$el.find('div.identifier').text().trim()).to.eql('dup-bibcode');
        expect(widget.view.children.findByIndex(0).$el.find('span.s-orcid-source').text().trim()).to.eql('(nasa ads; external source)');

        // pretend we are being called after the update
        widget.hiddenCollection.add({
          "doi": "10.1126/duplica.276.5309.88",
          "putcode": "469256",
          "title": "Tecnologias XXX",
          "visibility": "PUBLIC",
          "formattedDate": "2014/11",
          "pub": "El Profesional de la Informacion",
          "abstract": null,
          "author": [],
          "source_name": "external source"
        });
        widget.update();
        expect(widget.hiddenCollection.models.length).to.eql(1);
        expect(widget.hiddenCollection.models[0].attributes.source_name).to.eql("nasa ads; external source");
      })
    });

  });
