define(['jquery',
  'js/widgets/table_of_contents/widget',
  'js/bugutils/minimal_pubsub',
  'js/components/api_query',
  './test_json/test1',
  './test_json/test2',
], function($, TableOfContentsWidget, MinPubSub, ApiQuery, Test1, Test2){

  describe("Table Of Contents Widget (UI Widget)", function(){

    var widget, minsub, sentRequest;

    beforeEach(function(){
      widget = new TableOfContentsWidget();

      minsub = new (MinPubSub.extend({
        request: function(apiRequest) {
          sentRequest = apiRequest;
          numRequests++;

          if (sentRequest.toJSON().query.get("q")[0] === "bibcode:2015NewA...34") {
            return Test1;
          } else if(sentRequest.toJSON().query.get("q")[0] === "bibcode:2014IJMPD..23") {
            return Test2;
          }
        }
      }))({verbose: false});

      widget.activate(minsub.beehive.getHardenedInstance());

    });



    it("has a loadBibcodeInfo function that takes a bibcode, requests bibcode:data[:13]/bibcode:data[:14], and returns a promise", function(){

      var p = widget.loadBibcodeData("2015NewA...34..108Y")

      //how do you get the apiQuery from the apiRequest in an easier way?
      expect(sentRequest.toJSON().query.get("q")[0]).to.equal("bibcode:2015NewA...34")
      //test if it returns a promise
      expect(p.then).to.be.a("function")

    })

    it("fetches citation information for the bibcode only if it doesn't already have it and loads it into a collection", function(){

      var spy = sinon.spy(widget, 'dispatchRequest');

      widget.loadBibcodeData("2015NewA...34..108Y");
      expect(spy.callCount).to.equal(1);

      expect(widget.collection.toJSON()[0].bibcode).to.equal("2013arXiv1305.3460H");

      widget.loadBibcodeData("2015NewA...34..108Y");
      expect(spy.callCount).to.equal(1);

      widget.loadBibcodeData("2014IJMPD..2330002Z");
      expect(spy.callCount).to.equal(2);

      expect(widget.collection.toJSON()[0].bibcode).to.equal('2006IEDL...27..896K');

    })

    it("resolves the promise from loadBibcodeData with numFound", function(){

      var numFound;

      p = widget.loadBibcodeData("2015NewA...34")

      p.done(function(n){numFound= n});

      expect(numFound).to.equal(841359)

      console.log(numFound)
    })

  })


})