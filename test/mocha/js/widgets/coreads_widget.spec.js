define(['jquery',
  'js/widgets/coreads/widget',
  'js/bugutils/minimal_pubsub',
  'js/components/api_query',
  './test_json/test1',
  './test_json/test2',
], function($, CitationWidget, MinPubSub, ApiQuery, Test1, Test2){

  describe("CoReads Widget (UI Widget)", function(){

    var widget, minsub, sentRequest, numRequests;

    beforeEach(function(){
      widget = new CitationWidget();

      minsub = new (MinPubSub.extend({
        request: function(apiRequest) {
          sentRequest = apiRequest;

          if (sentRequest.toJSON().query.get("q")[0] === "trending(bibcode:sampleBib1)") {
            return Test1;
          } else if(sentRequest.toJSON().query.get("q")[0] === "trending(bibcode:sampleBib1)") {
            return Test2;
          }
        }
      }))({verbose: false});

      widget.activate(minsub.beehive.getHardenedInstance());
      var $w = widget.render().$el;

      //prevent infinite requests for data
      widget.collection.requestData = function(){};

    });



    it("has a loadBibcodeInfo function that takes a bibcode, requests trending(data), and returns a promise", function(){

      var p = widget.loadBibcodeData("sampleBib1")

      //how do you get the apiQuery from the apiRequest in an easier way?
      expect(sentRequest.toJSON().query.get("q")[0]).to.equal("trending(bibcode:sampleBib1)")
      //test if it returns a promise
      expect(p.then).to.be.a("function")

    })



    it("resolves the promise from loadBibcodeData with numFound", function(){

      var numFound;

      p = widget.loadBibcodeData("sampleBib1")

      p.done(function(n){numFound= n});

      expect(numFound).to.equal(841359);
    })

  })


})