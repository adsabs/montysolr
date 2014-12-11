
define(['jquery', 'js/widgets/resources/widget', 'js/widgets/base/base_widget', 'js/bugutils/minimal_pubsub'],
  function($, ResourcesWidget, BaseWidget, MinPubSub){

  describe("Resources Widget", function(){

    var widget, minsub, $w, sentRequest;

      beforeEach(function(){
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
                  "fl": "links_data,ids_data,[citations],property,bibcode"
                }
              },
              "response": {
                "numFound": 1,
                "start": 0,
                "docs": [
                  {
                    "bibcode": "1984NASCP2349..191B",
                    "ids_data": [
                      "{\"identifier\":\"1984NASCP2349..191B\", \"alternate_bibcode\":\"\", \"deleted_bibcode\":\"\", \"description\":\"ADS bibcode\"}",
                      "{\"identifier\":\"\", \"alternate_bibcode\":\"1984fiue.rept..191B\", \"deleted_bibcode\":\"\", \"description\":\"alternate\"}",
                      "{\"identifier\":\"\", \"alternate_bibcode\":\"1984IUE84......191B\", \"deleted_bibcode\":\"\", \"description\":\"alternate\"}"
                    ],
                    "links_data": [
                      "{\"title\":\"\", \"type\":\"simbad\", \"instances\":\"2\"}",
                      "{\"title\":\"\", \"type\":\"ADSlink\", \"instances\":\"\"}"
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

    })


    it("extends from BaseWidget and can communicate with pubsub and its page controller through loadBibcodeData function", function(){

      expect(widget).to.be.instanceof(BaseWidget);

     expect(widget.loadBibcodeData).to.be.instanceof(Function);

     widget.loadBibcodeData("fakeBibcode");

     expect(sentRequest.get('query').get('q')[0]).to.eql("bibcode:fakeBibcode")


    })

    it("has the LinkGeneratorMixin to parse the links data", function(){

      expect(widget.parseResourcesData).to.be.instanceof(Function);
      expect(widget.adsUrlRedirect).to.be.instanceof(Function);

    })

    it("processes received data and renders full text sources as well as data products, if they exist", function(){

      widget.loadBibcodeData("fakeBibcode");

      expect($w.find("p:first").find("a").attr("href")).to.eql(
        'http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=1984NASCP2349..191B&link_type=GIF'
      )

      expect($w.find("p:last").find("a").attr("href")).to.eql(
        "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=1984NASCP2349..191B&link_type=SIMBAD"
      )



    })


  })




})