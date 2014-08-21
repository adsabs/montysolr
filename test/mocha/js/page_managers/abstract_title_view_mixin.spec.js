define([
    'underscore',
    'backbone',
    'js/page_managers/abstract_title_view_mixin',
    'js/widgets/base/base_widget',
    'js/components/api_response',
    'js/components/api_query',
    '../widgets/test_json/test1'],

  function(_,
    Backbone,
    PaginationMixin,
    BaseWidget,
    ApiResponse,
    ApiQuery,
    TestResponse){

    describe("Abstract Title Widget (UI Widget)", function(){

      var w, dispatchRequestSpy, pubsubStub;

      beforeEach(function(){

        var W = BaseWidget.extend({

          initialize : function(){

//            this.titleView = this.returnNewTitleView();

            BaseWidget.prototype.initialize.apply(this)

          }

        })

         _.extend(W.prototype, PaginationMixin);

         w = new W();

        w.collection = new Backbone.Collection();

        w.collection.add( new Backbone.Model({resultsIndex : 0, bibcode : 'test0'}))
        w.collection.add( new Backbone.Model({resultsIndex : 6,  bibcode : 'test6'}))
        w.collection.add( new Backbone.Model({resultsIndex : 11, bibcode : 'test11'}))


        w.setCurrentQuery(new ApiQuery({q : "test"}))

        w.pubsub = {publish : function(){}};

        pubsubStub = sinon.stub(w.pubsub, "publish");


      })


      it("should monitor page changes from the main results page and reset with that selection plus a buffer of 10 objects before and 19 after", function(){

        w.autoPaginate({event: "pagination",data : {start : 10} })

        expect(pubsubStub.firstCall.args[1].get("query").url()).to.eql("fl=title%2Cbibcode&q=test&rows=30&start=0")

        w.autoPaginate({event: "pagination",data : {start : 0} })

//      shouldn't request less than start = 0

        expect(pubsubStub.firstCall.args[1].get("query").url()).to.eql("fl=title%2Cbibcode&q=test&rows=30&start=0")


      })

      it("should respond to paging events on the abstract page by checking to see if there is still a large enough buffer (5) on either side, and if not, requesting more", function(){

        dispatchRequestSpy = sinon.spy(w, "dispatchRequest");

        w._bibcode = 'test6';
        w.numFound = 20;
        w.checkLoadMore();

        expect(dispatchRequestSpy.firstCall.args[0].url()).to.eql("q=test&rows=10&start=12")

        w._bibcode = 'test6';
        w.numFound = 11;
        w.checkLoadMore();

        //already have all records

        expect(dispatchRequestSpy.secondCall).to.eql(null)


      })

      it("should set the AbstractTitleModel with the correct data depending on whether the bibcode is a one-off or a member of the result set")





    })



  })