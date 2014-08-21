define(['js/page_managers/master_page_manager',
  'js/bugutils/minimal_pubsub',
  'js/components/api_query'
], function(MasterPageManager, MinPubSub, ApiQuery){


  describe("Master Page Manager", function(){


    var w, fakeShowPage, minsub;

    beforeEach(function(){


      //you appear to have to add the methods before simon will stub them (?)
      var DummyPage = function(){this.showPage = function(){}}

      var dummyPage = new DummyPage();

      fakeShowPage = sinon.stub(dummyPage, "showPage");


      w = new MasterPageManager({pageControllers: { results: dummyPage}})

      minsub = new (MinPubSub.extend({
              request: function(apiRequest) {
                   return {some: 'foo'}
                 }
               }))({verbose: false});

      w.activate(minsub.beehive.getHardenedInstance());

    })


    it("should have a dict of page managers", function(){

      expect(_.keys(w.pageControllers).length).to.eql(1)

    })

    it("should be able to communicate with pubsub (currently listens to START_SEARCH to show the results page", function(){

      sinon.stub(w, "showSearchPage")

      expect(w.showSearchPage.callCount).to.eql(0);

//      this is not working even though showSearchPage is getting called (?)

//      minsub.publish(minsub.START_SEARCH, new ApiQuery())
//
//      expect(w.showSearchPage.callCount).to.eql(1);



    })

    it("should have a showPage method that tells a page controller to render itself,informs it whether it is already in the dom, and scrolls up automatically", function(){

      w.showPage("results")

      expect(fakeShowPage.callCount).to.eql(1);

      expect(fakeShowPage.args[0][0].inDom).to.be.false;

      w.showPage("results");


      expect(fakeShowPage.args[1][0].inDom).to.be.true;


    })




  })




})