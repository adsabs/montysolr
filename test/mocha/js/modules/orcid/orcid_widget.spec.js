define([
    'marionette',
    'backbone',
    'jquery',
    'js/bugutils/minimal_pubsub',
    'js/modules/orcid/widget/widget',
    'js/widgets/list_of_things/widget',
    'js/components/json_response',
    'js/modules/orcid/module',
    'js/components/api_query'
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
    ApiQuery
    ) {

    describe("Orcid Widget (orcid_widget.spec.js)", function () {
      var minsub, beehive, notifier;
      // this is useful if you want to test widget getting data from Orcid
      // it simulates pubsub response
      beforeEach(function (done) {
        minsub = new (MinimalPubsub.extend({
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

        beehive = minsub.beehive;
        done();
      });

      afterEach(function (done) {
        minsub.destroy();
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

      var _getWidget = function () {
        var widget = new OrcidWidget();

        widget.activate(minsub.beehive.getHardenedInstance());
        return widget;
      };

      var getOrcidApi = function () {
        beehive.addObject('DynamicConfig', {
          orcidClientId: 'APP-P5ANJTQRRTMA6GXZ',
          orcidApiEndpoint: 'https://api.orcid.org',
          orcidRedirectUrlBase: 'http://localhost:8000',
          orcidLoginEndpoint: 'https://api.orcid.org/oauth/authorize'
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

      var defaultResponse = function () {
        return {
          "message-version": "1.2",
          "orcid-profile": {
            "orcid": null,
            "orcid-identifier": {
              "value": null,
              "uri": "http://sandbox.orcid.org/0000-0001-8178-9506",
              "path": "0000-0001-8178-9506",
              "host": "sandbox.orcid.org"
            },

        "orcid-bio":{"personal-details": {"family-name": {"value" : "Chyla"}, "given-names": {"value": "Roman"}}},

        "orcid-preferences": {
              "locale": "EN"
            },
            "orcid-history": {
              "creation-method": "DIRECT",
              "submission-date": {
                "value": 1422645321288
              },
              "last-modified-date": {
                "value": 1423688425823
              },
              "claimed": {
                "value": true
              },
              "source": null,
              "verified-email": {
                "value": false
              },
              "verified-primary-email": {
                "value": false
              },
              "visibility": null
            },
            "orcid-activities": {
              "affiliations": null,
              "orcid-works": {
                "orcid-work": [
                  {
                    "language-code": "es",
                    "put-code": "469257",
                    "source": {
                      "source-client-id": {
                        "path": "APP-P5ANJTQRRTMA6GXZ",
                        "host": "sandbox.orcid.org",
                        "uri": "http://sandbox.orcid.org/client/APP-P5ANJTQRRTMA6GXZ",
                        "value": null
                      },
                      "source-name": {
                        "value": "nasa ads"
                      },
                      "source-date": {
                        "value": 1424194783005
                      }
                    },
                    "work-title": {
                      "subtitle": null,
                      "title": {
                        "value": "Tecnologias XXX"
                      }
                    },
                    "last-modified-date": {
                      "value": 1424194783005
                    },
                    "created-date": {
                      "value": 1424194783005
                    },
                    "visibility": "PUBLIC",
                    "work-type": "JOURNAL_ARTICLE",
                    "publication-date": {
                      "month": {
                        "value": "11"
                      },
                      "day": null,
                      "media-type": null,
                      "year": {
                        "value": "2014"
                      }
                    },
                    "journal-title": {
                      "value": "El Profesional de la Informacion"
                    },
                    "work-external-identifiers": {
                      "scope": null,
                      "work-external-identifier": [
                        {
                          "work-external-identifier-id": {
                            "value": "test-bibcode"
                          },
                          "work-external-identifier-type": "BIBCODE"
                        }
                      ]
                    },
                    "url": null,
                    "work-contributors": {
                      "contributor": null
                    }
                  },
                  {
                    "put-code": "466190",
                    "work-title": {
                      "title": {
                        "value": "ADS 2.0"
                      },
                      "subtitle": null
                    },
                    "journal-title": {
                      "value": "foo"
                    },
                    "work-external-identifiers": {
                      "scope": null,
                      "work-external-identifier": [
                        {
                          "work-external-identifier-id": {
                            "value": "10.1126/science.276.5309.88"
                          },
                          "work-external-identifier-type": "doi"
                        }
                      ]
                    },
                    "work-type": "JOURNAL_ARTICLE",
                    "publication-date": {
                      "year": {
                        "value": "2015"
                      },
                      "month": {
                        "value": "01"
                      },
                      "day": {
                        "value": "01"
                      },
                      "media-type": null
                    },
                    "url": null,
                    "source": {
                      "source-orcid": {
                        "value": null,
                        "uri": "http://sandbox.orcid.org/0000-0001-8178-9506",
                        "path": "0000-0001-8178-9506",
                        "host": "sandbox.orcid.org"
                      },
                      "source-name": {
                        "value": "Roman Chyla"
                      },
                      "source-date": {
                        "value": 1422645668284
                      }
                    },
                    "created-date": {
                      "value": 1422645668284
                    },
                    "last-modified-date": {
                      "value": 1422645668284
                    },
                    "visibility": "PUBLIC"
                  },
                  {
                    "put-code": "466191",
                    "work-title": {
                      "title": {
                        "value": "External article"
                      },
                      "subtitle": null
                    },
                    "journal-title": {
                      "value": "foo"
                    },
                    "work-external-identifiers": {
                      "scope": null,
                      "work-external-identifier": [
                        {
                          "work-external-identifier-id": {
                            "value": "external"
                          },
                          "work-external-identifier-type": "other"
                        }
                      ]
                    },
                    "work-type": "JOURNAL_ARTICLE",
                    "publication-date": {
                      "year": {
                        "value": "2015"
                      },
                      "month": {
                        "value": "01"
                      },
                      "day": {
                        "value": "01"
                      },
                      "media-type": null
                    },
                    "url": null,
                    "source": {
                      "source-orcid": {
                        "value": null,
                        "uri": "http://sandbox.orcid.org/0000-0001-8178-9506",
                        "path": "0000-0001-8178-9506",
                        "host": "sandbox.orcid.org"
                      },
                      "source-name": {
                        "value": "Roman Chyla"
                      },
                      "source-date": {
                        "value": 1422645668284
                      }
                    },
                    "created-date": {
                      "value": 1422645668284
                    },
                    "last-modified-date": {
                      "value": 1422645668284
                    },
                    "visibility": "PUBLIC"
                  }
                ],
                "scope": null
              }
            },
            "type": "USER",
            "group-type": null,
            "client-type": null
          }
        };
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
        minsub.beehive.removeObject("User");
        minsub.beehive.addObject("User", fakeUser);

        var orcidApi = getOrcidApi();
        sinon.stub(orcidApi, 'hasAccess', function() {return true});
        var widget = _getWidget();


        var response = new JsonResponse(orcidApi.transformOrcidProfile(defaultResponse()['orcid-profile']));
        response.setApiQuery(new ApiQuery(response.get('responseHeader.params')));

        widget.processResponse(response);

        var $w = widget.render().$el;
        $('#test').append($w);

        expect(widget.collection.models.length).to.eql(3);
        expect(widget.view.children.findByIndex(2).$el.find('div.identifier').text().trim()).to.eql('test-bibcode');
        expect(widget.view.children.findByIndex(0).$el.find('div.identifier').text().trim()).to.eql('1997sci...276...88v'); // found through doi, alternate_bibcode:bibcode-foo

        orcidMode = true;

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
        expect(widget.collection.models[0].get('title')).to.eql('ADS 2.0');
        expect(widget.collection.models[1].get('title')).to.eql('Tecnologias XXX');
        expect(widget.collection.models[2].get('title')).to.eql('External article');

        // sort and filter
        widget.update({filterBy: ['ads', 'others'], sortBy: 'title'});
        expect(widget.collection.models[0].get('title')).to.eql('ADS 2.0');
        expect(widget.collection.models[1].get('title')).to.eql('Tecnologias XXX');

        done();
      });

      it("should show a loading view before orcid profile is loaded", function(){

        var orcidApi = getOrcidApi();
        orcidApi.saveAccessData({access: true});
        orcidApi.getUserProfile = function() {
          expect($("#test .s-results-control-row-container").text().trim()).to.eql('My ORCID Papers \n        \n        \n        \n             Loading ORCID data...');
          var d = $.Deferred();
          d.resolve(defaultResponse()['orcid-profile']);
          return d;
        };

        orcidMode = true;

        var fakeUser = {
          getHardenedInstance : function(){return this},
          getLocalStorage : function(){return { perPage : 50 }},
          isOrcidModeOn: function() {
            return orcidMode;
          },
        };
        minsub.beehive.removeObject("User");
        minsub.beehive.addObject("User", fakeUser);


        var widget = _getWidget();

        var $w = widget.render().$el;
        $('#test').append($w);


        widget.onShow();


        expect($("#test .s-results-control-row-container").text().replace(/\s{2,}/g, "")).to.eql('My ORCID Papers(3)ORCID Username: Roman ChylaORCID ID: 0000-0001-8178-9506To share this list of your ORCID papers:You can share this link: https://ui.adsabs.harvard.edu/#search/q=orcid%3A0000-0001-8178-9506&sort=date+descOr searchorcid:0000-0001-8178-9506in the ADS interface at any time.To claim papers in ORCID and add to this listclick here to search your name in ADSLearn more about using ORCID with ADS')


      });

      it("should allow the user to search in ADS when search button is clicked", function(done){

        var orcidApi = getOrcidApi();
        orcidApi.saveAccessData({access: true});
        orcidApi.getUserProfile = function() {
          var d = $.Deferred();
          d.resolve(defaultResponse()['orcid-profile']);
          return d;
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
        minsub.beehive.removeObject("User");
        minsub.beehive.addObject("User", fakeUser);

        var widget = _getWidget();
        widget.activate(minsub.beehive.getHardenedInstance());

        var publishStub = sinon.stub(widget.getPubSub(), "publish");

        widget.onShow();
        setTimeout(function() {

          var $w = widget.render().$el;
          $('#test').append($w);

         $("button.search-author-name").click();

          expect(publishStub.args[0][0]).to.eql("[PubSub]-New-Query");
          expect(publishStub.args[0][1].toJSON()).to.eql({
            "q": [
              "author:(\"Name, Variation 1\" OR \"Name, Variation 2\" OR \"Chyla, Roman\")"
            ]
          });

          done();
        }, 200);

      });

      it("should load ORCID when onShow is called", function(done) {
        var orcidApi = getOrcidApi();
        orcidApi.saveAccessData({access: true});
        orcidApi.getUserProfile = function() {
          var d = $.Deferred();
          d.resolve(defaultResponse()['orcid-profile']);
          return d;
        };


        var fakeUser = {
          getHardenedInstance : function(){return this},
          getLocalStorage : function(){return { perPage : 50 }},
          isOrcidModeOn: function() {
            return orcidMode;
          },
        };
        minsub.beehive.removeObject("User");
        minsub.beehive.addObject("User", fakeUser);

        var widget = _getWidget();

        widget.onShow();
        setTimeout(function() {

          var $w = widget.render().$el;
          $('#test').append($w);

          expect(widget.collection.models.length).to.eql(3);
          expect(widget.view.children.findByIndex(2).$el.find('div.identifier').text().trim()).to.eql('test-bibcode');
          expect(widget.view.children.findByIndex(1).$el.find('div.identifier').text().trim()).to.eql(''); // since we don't make api resolution, bibcode cannot be found

          done();
        }, 200);
      });

      it("should know how to de-duplicate records", function() {
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
        minsub.beehive.addObject("User", fakeUser);

        var orcidApi = getOrcidApi();
        sinon.stub(orcidApi, 'hasAccess', function() {return true});
        var widget = _getWidget();

        var response = new JsonResponse(data);
        response.setApiQuery(new ApiQuery(response.get('responseHeader.params')));

        widget.processResponse(response);

        var $w = widget.render().$el;
        $('#test').append($w);

        expect(widget.collection.models.length).to.eql(1);
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