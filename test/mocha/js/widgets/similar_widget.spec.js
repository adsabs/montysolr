define(['jquery',
  'js/widgets/similar/widget',
  'js/bugutils/minimal_pubsub',

], function($, CitationWidget, MinPubSub){

  describe("Similar Widget (UI Widget)", function(){

    var widget, minsub, sentRequest;

    beforeEach(function(){
      widget = new CitationWidget();

      minsub = new (MinPubSub.extend({
        request: function(apiRequest) {
          sentRequest = apiRequest;

          var query = sentRequest.toJSON().query;

          if (query.get("q")[0] === "bibcode:sampleBib1" && (query.get("mlt")[0] === "true") && (query.get("mlt.fl")[0] === "title,body")) {
            return {"responseHeader":{"params" : {}},"moreLikeThis":{"randomNum":{"numFound": 2745325, docs : [{bibcode: "2013arXiv1305.3460H"}]}}}

        } else if(query.get("q")[0] === "bibcode:sampleBib2" && (query.get("mlt")[0] === "true") && (query.get("mlt.fl")[0] == "title,body")) {
            return {"responseHeader":{"params":{}},"moreLikeThis":{"randomNum":{"numFound": 2745325, docs : [{bibcode: "2006IEDL...27..896K"}]}}};
          }
        }

      }))({verbose: false});

      widget.activate(minsub.beehive.getHardenedInstance());
      var $w = widget.render().$el;

    });

    it("has a loadBibcodeInfo function that takes a bibcode; requests bibcode:data, mlt: true, mlt.fl = title, body; and returns a promise", function(){

      var p = widget.loadBibcodeData("sampleBib1")

      //how do you get the apiQuery from the apiRequest in an easier way?
      expect(sentRequest.toJSON().query.get("q")[0]).to.equal("bibcode:sampleBib1")
      //test if it returns a promise
      expect(p.then).to.be.a("function")

    })

    it("fetches citation information for the bibcode only if it doesn't already have it and loads it into a collection", function(){


      var spy = sinon.spy(widget, 'dispatchRequest');

      widget.loadBibcodeData("sampleBib1");
      expect(spy.callCount).to.equal(1);

      expect(widget.collection.toJSON()[0].bibcode).to.equal("2013arXiv1305.3460H");

      widget.loadBibcodeData("sampleBib1");
      expect(spy.callCount).to.equal(1);

      widget.loadBibcodeData("sampleBib2");
      expect(spy.callCount).to.equal(2)

      expect(widget.collection.toJSON()[0].bibcode).to.equal('2006IEDL...27..896K');

    })

    it("resolves the promise from loadBibcodeInfo with the collection", function(){

      var promisedCollection;

      p = widget.loadBibcodeData("sampleBib1")

      p.done(function(collection){console.log("similarok", collection.toJSON()[0]); promisedCollection =  collection.toJSON()});

     expect(promisedCollection[0].identifier).to.equal("2013arXiv1305.3460H")
    })

  })

})