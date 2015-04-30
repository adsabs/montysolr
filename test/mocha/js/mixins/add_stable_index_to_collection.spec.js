define([
    'underscore',
    'js/mixins/add_stable_index_to_collection',
    'js/components/api_response',
    '../widgets/test_json/test1'
  ],

  function(_,
    PaginationMixin,
    ApiResponse,
    TestData
    ){

  describe("Widget Paginator (Widget Mixin)", function(){

    beforeEach(function(){

      fakeResponse = new ApiResponse(TestData());


    });

    it ("should add a property called resultsIndex to each doc prior to pushing them to the collection", function(){

      var docs  = PaginationMixin.addPaginationToDocs(fakeResponse.get("response.docs"), fakeResponse.get("response.start"));
      var resultsIndexes = _.pluck(docs, "resultsIndex");
      expect(resultsIndexes).to.eql([0,1,2,3,4,5,6,7,8,9])

    });


    it("should also add a property called indexToShow which is the resultsIndex value +1", function(){

      var docs  = PaginationMixin.addPaginationToDocs(fakeResponse.get("response.docs"), fakeResponse.get("response.start"));
      var indexesToShow = _.pluck(docs, "indexToShow");
      expect(indexesToShow).to.eql([1,2,3,4,5,6,7,8,9,10]);

    })


    it("should be aware of the apiResponse's start value and change the results index to reflect that value", function(){

      var d = TestData();
      d.response.start = 100;

      var fakeResponse = new ApiResponse(d);
      var docs = PaginationMixin.addPaginationToDocs(fakeResponse.get("response.docs"), fakeResponse.get("response.start"));
      var resultsIndexes = _.pluck(docs, "resultsIndex");
      expect(resultsIndexes).to.eql([100, 101, 102, 103, 104, 105, 106, 107, 108, 109])
    });


    it("should have a getPageStart method that returns a starting index given a page number and number of records per page", function(){
        expect(PaginationMixin.getPageStart(3, 10)).to.eql(30);
        expect(PaginationMixin.getPageStart(1, 5)).to.eql(5);
        expect(PaginationMixin.getPageStart(5, 25)).to.eql(125);
        expect(PaginationMixin.getPageStart(5, 25, 100)).to.eql(100);
    });

    it("should have a getPageEnd method that returns an ending index given a page number, number of records per page, and total number of records found", function(){


      //ignoring numFound
      expect(PaginationMixin.getPageEnd(3, 10, 1000)).to.eql(40);
      expect(PaginationMixin.getPageEnd(0, 5, 1000)).to.eql(5);
      expect(PaginationMixin.getPageEnd(5, 25, 1000)).to.eql(150);

      //testing numFound limitation
      expect(PaginationMixin.getPageEnd(2, 10, 25)).to.eql(25);
      expect(PaginationMixin.getPageEnd(5, 25, 120)).to.eql(120);

    });

    it("should have a getPageVal method that returns the page number given a starting index and number of records per Page", function(){

      expect(PaginationMixin.getPageVal(10, 20)).to.eql(0);
      expect(PaginationMixin.getPageVal(10, 5)).to.eql(1);
      expect(PaginationMixin.getPageVal(11, 5)).to.eql(2);
      expect(PaginationMixin.getPageVal(11, 2)).to.eql(5);
    });
  })
});