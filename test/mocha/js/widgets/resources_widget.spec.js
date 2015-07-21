
define(['jquery', 'js/widgets/resources/widget', 'js/widgets/base/base_widget', 'js/bugutils/minimal_pubsub'],
  function($, ResourcesWidget, BaseWidget, MinPubSub){

  describe("Resources Widget", function(){

    var widget, minsub, $w, sentRequest;



    it("extends from BaseWidget and can communicate with pubsub and its page controller through loadBibcodeData function", function(){

      var restore = ResourcesWidget.prototype.processResponse;

      ResourcesWidget.prototype.processResponse = sinon.spy();

      widget = new ResourcesWidget();


      minsub = new (MinPubSub.extend({
        request: function(apiRequest) {
          sentRequest = apiRequest;

          return {
            "responseHeader": {
              "status": 0,
              "QTime": 2,
              "params": {
                "wt": "json",
                "q": "bibcode:1984NASCP2349..191B",
                "fl": "links_data,[citations],property,bibcode"
              }
            },
            "response": {
              "numFound": 1,
              "start": 0,
              "docs": [
                {
                  "bibcode": "1984NASCP2349..191B",

                  "links_data": [
                    "{\"title\":\"\", \"type\":\"simbad\", \"instances\":\"2\", \"access\":\"\"}",
                    "{\"title\":\"\", \"type\":\"ADSlink\", \"instances\":\"\", \"access\":\"open\"}"
                  ],
                  "property": [
                    "OPENACCESS",
                    "NOT REFEREED",
                    "ADS_SCAN",
                    "TOC",
                    "INPROCEEDINGS"
                  ],
                  "[citations]": {
                    "num_citations": 1,
                    "num_references": 0
                  }
                }
              ]
            }
          }
        }

      }))({verbose: false});

      widget.activate(minsub.beehive.getHardenedInstance());
      $w = widget.render().$el;

     expect(widget).to.be.instanceof(BaseWidget);
     expect(widget.loadBibcodeData).to.be.instanceof(Function);
     widget.loadBibcodeData("fakeBibcode");

     //these fields are required for the link generator mixin
     expect(sentRequest.get("query").get("fl")[0]).to.eql("links_data,[citations],property,bibcode,first_author,year,page,pub,pubdate,title,volume,doi,issue,issn");
     expect(sentRequest.get('query').get('q')[0]).to.eql("bibcode:fakeBibcode");

      //why was regular sinon restore call not working?
      ResourcesWidget.prototype.processResponse = restore;

    })


    it("processes received data and renders full text sources as well as data products, if they exist", function(){

      widget = new ResourcesWidget();

      minsub = new (MinPubSub.extend({
        request: function(apiRequest) {
          sentRequest = apiRequest;

          return {
            "responseHeader": {
              "status": 0,
              "QTime": 2,
              "params": {
                "wt": "json",
                "q": "bibcode:1984NASCP2349..191B",
                "fl": "links_data,[citations],property,bibcode"
              }
            },
            "response": {
              "numFound": 1,
              "start": 0,
              "docs": [
                {
                  "bibcode": "1984NASCP2349..191B",

                  "links_data": [
                    "{\"title\":\"\", \"type\":\"simbad\", \"instances\":\"2\", \"access\":\"\"}",
                    "{\"title\":\"\", \"type\":\"ADSlink\", \"instances\":\"\", \"access\":\"open\"}"
                  ],
                  "property": [
                    "OPENACCESS",
                    "NOT REFEREED",
                    "ADS_SCAN",
                    "TOC",
                    "INPROCEEDINGS"
                  ],
                  "[citations]": {
                    "num_citations": 1,
                    "num_references": 0
                  }
                }
              ]
            }
          }
        }

      }))({verbose: false});

      widget.activate(minsub.beehive.getHardenedInstance());

      var getUserDataSpy = sinon.spy(function(){return {link_server : "fake"}});
      widget.__beehive.getObject = function(){return {getUserData : getUserDataSpy }};

      $w = widget.render().$el;

      expect(widget.parseResourcesData).to.be.instanceof(Function);
      expect(widget.adsUrlRedirect).to.be.instanceof(Function);

      widget.loadBibcodeData("fakeBibcode");

      //gets link server info
      expect(getUserDataSpy.callCount).to.eql(1);

      expect($w.find("ul:first").find("a").attr("href")).to.eql(
        'http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=1984NASCP2349..191B&link_type=SIMBAD'
      );

      expect($w.find("ul:last").find("a").attr("href")).to.eql(
        'http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=1984NASCP2349..191B&link_type=SIMBAD'
      );

    });


    it("knows about the user's link_server if user is signed in, and adds that to the list of full text links if applicable", function(){

      widget = new ResourcesWidget();

      $w = widget.render().$el;

      var data =  {
          fullTextSources : [{openAccess: false, title: "Publisher Article", link: "fakelink", openUrl: true}]

        };

    widget.model.set(data);

    $("#test").append($w);

      expect($("#test").find(".resources-widget a").html().trim()).to.eql('Find it at your institution\n            <i class="fa fa-university" data-toggle="tooltip" data-placement="top" title="" data-original-title="This resource is available through your institution."></i>' );

      $("#test").empty();

    });

  })




})