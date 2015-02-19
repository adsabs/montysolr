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

        }))({verbose: false});

        beehive = minsub.beehive;
        done();
      });

      afterEach(function (done) {
        minsub.close();
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
        return beehive.getService('OrcidApi');
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
                            "value": "bibcode-foo"
                          },
                          "work-external-identifier-type": "bibcode"
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

      it("Should display records coming from ORCID", function (done) {
        var widget = _getWidget();
        var orcidApi = getOrcidApi();


        var response = new JsonResponse(orcidApi.transformOrcidProfile(defaultResponse()['orcid-profile']));
        response.setApiQuery(new ApiQuery(response.get('responseHeader.params')));

        widget.processResponse(response);

        var $w = widget.render().$el
        $('#test').append($w);

        expect(widget.collection.models.length).to.eql(2);
        expect(widget.view.children.findByIndex(0).$el.find('div.identifier').text().trim()).to.eql('test-bibcode');
        expect(widget.view.children.findByIndex(1).$el.find('div.identifier').text().trim()).to.eql('bibcode-foo');
        done();
      });
    });

  });