define([
    'underscore',
    'js/mixins/add_stable_index_to_collection',
    'js/components/api_response',
    '../widgets/test_json/test1'
  ],

  function(_,
    PaginationMixin,
    ApiResponse,
    TestResponse
    ){

  describe("Widget Paginator (Widget Mixin)", function(){

    beforeEach(function(){

      fakeResponse = new ApiResponse(TestResponse);


    });

    it ("should add a property called resultsIndex to each doc prior to pushing them to the collection", function(){

      var docs  = PaginationMixin.addPaginationToDocs(fakeResponse.get("response.docs"), fakeResponse.get("response.start"));

      var resultsIndexes = _.pluck(docs, "resultsIndex");

      expect(resultsIndexes).to.eql([0,1,2,3,4,5,6,7,8,9])


    });


    it("should be aware of the apiResponse's start value and change the results index to reflect that value", function(){

      TestResponse.response.start = 100;

      fakeResponse = new ApiResponse(TestResponse);

      var docs = PaginationMixin.addPaginationToDocs(fakeResponse.get("response.docs"), fakeResponse.get("response.start"));

      var resultsIndexes = _.pluck(docs, "resultsIndex");

      expect(resultsIndexes).to.eql([100, 101, 102, 103, 104, 105, 106, 107, 108, 109])


    })


    it("should have a getPageStart method that returns a starting index given a page number and number of records per page", function(){

        expect(PaginationMixin.getPageStart).to.be.instanceof(Function);

        expect(PaginationMixin.getPageStart(3, 10)).to.eql(20);

        expect(PaginationMixin.getPageStart(1, 5)).to.eql(0);

        expect(PaginationMixin.getPageStart(1, 13)).to.eql(0);

        expect(PaginationMixin.getPageStart(5, 25)).to.eql(100);


    });

    it("should have a getPageEnd method that returns an ending index given a page number, number of records per page, and total number of records found", function(){

      expect(PaginationMixin.getPageEnd).to.be.instanceof(Function);

      //ignoring numFound

      expect(PaginationMixin.getPageEnd(3, 10, 1000)).to.eql(29);

      expect(PaginationMixin.getPageEnd(1, 5, 1000)).to.eql(4);

      expect(PaginationMixin.getPageEnd(1, 13, 1000)).to.eql(12);

      expect(PaginationMixin.getPageEnd(5, 25, 1000)).to.eql(124);

      //testing numFound limitation

      expect(PaginationMixin.getPageEnd(3, 10, 25)).to.eql(24);

      expect(PaginationMixin.getPageEnd(1, 5, 3)).to.eql(2);

      expect(PaginationMixin.getPageEnd(1, 13, 12)).to.eql(11);

      expect(PaginationMixin.getPageEnd(5, 25, 120)).to.eql(119);

    });

    it("should have a getPageVal method that returns the page number given a starting index and number of records per Page", function(){

      expect(PaginationMixin.getPageVal).to.be.instanceof(Function);

      expect(PaginationMixin.getPageVal(10, 5)).to.eql(3);

//      expect(PaginationMixin.getPageVal(11, 5)).to.throw(Error);
//
      expect(PaginationMixin.getPageVal(100, 25)).to.eql(5);
//
//      expect(PaginationMixin.getPageVal(101, 25)).to.throw(Error);
//
      expect(PaginationMixin.getPageVal(10, 2)).to.eql(6);
//
//      expect(PaginationMixin.getPageVal(9, 2)).to.throw(Error);






    });



  })



})