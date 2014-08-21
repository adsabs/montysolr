define([
    'underscore',
    'js/mixins/add_stable_index_to_collection',
    'js/components/api_response',
    '../widgets/test_json/test1'],

  function(_,
    PaginationMixin,
    ApiResponse,
    TestResponse){

  describe("Widget Paginator (Widget Mixin)", function(){

    var w, addSpy, resetSpy, fakeResponse, triggerSpy;

    beforeEach(function(){

      w = {trigger : function(){}};
      w.collection = {reset: function(){}, add : function(){}};

      triggerSpy = sinon.spy(w, "trigger");
      
      addSpy = sinon.spy(w.collection,"add");
      resetSpy = sinon.spy(w.collection,"reset");

      _.extend(w, PaginationMixin);

      fakeResponse = new ApiResponse(TestResponse);


    })

    it ("should add a property called resultsIndex to each doc prior to pushing them to the collection", function(){

      w.insertPaginatedDocsIntoCollection(fakeResponse.get("response.docs"), fakeResponse)

      var resultsIndexes = _.pluck(resetSpy.firstCall.args[0], "resultsIndex")

      expect(resultsIndexes).to.eql([0,1,2,3,4,5,6,7,8,9])


    })


    it("should be aware of the apiResponse's start value and change the results index to reflect that value", function(){

      TestResponse.response.start = 100;

      fakeResponse = new ApiResponse(TestResponse);

      w.insertPaginatedDocsIntoCollection(fakeResponse.get("response.docs"), fakeResponse)

      var resultsIndexes = _.pluck(resetSpy.firstCall.args[0], "resultsIndex")

      expect(resultsIndexes).to.eql([100, 101, 102, 103, 104, 105, 106, 107, 108, 109])


    })


    it("should add docs to the collection when the widget has a numFound value", function(){

      w.numFound = 10
      w.insertPaginatedDocsIntoCollection(fakeResponse.get("response.docs"), fakeResponse)

      expect(addSpy.callCount).to.eql(1);
      expect(resetSpy.callCount).to.eql(0);

    })

    it("should reset the widget's collection when the widget does not have a numFound value", function(){


      w.numFound = undefined;
      w.insertPaginatedDocsIntoCollection(fakeResponse.get("response.docs"), fakeResponse)

      expect(addSpy.callCount).to.eql(0);
      expect(resetSpy.callCount).to.eql(1)


    })



  })



})