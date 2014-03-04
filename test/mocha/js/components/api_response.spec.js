define(['js/components/api_response', 'backbone'], function(ApiResponse, Backbone) {
  describe("API response object", function () {
      
    // Runs once before all tests start.
    before(function () {
    });
  
    // Runs once when all tests finish.
    after(function () {
    });
  
    it("should return API response object", function() {
      expect(new ApiResponse()).to.be.an.instanceof(Object);
      expect(ApiResponse.extend).to.be.OK;


      var test = ApiResponse.extend({
        foo: function() {
          //pass;
        }
      });
      expect(new test()).to.be.instanceof(ApiResponse);
    });
    
    it("is immutable by default and throws errors on attempted modification");
    
    it("can accept any type of keys/values, of unlimited structure");
    
    it("is forgiving (accessing unknown keys is not throwing error), but strict mode is possible");
    
    it("can be serialized and de-serialized (saved as string and reloaded)");
    
    it("contains the original query (but not the request itself)");
    
    it("provides a key under which this object can be cached/retrieved");
    
    
  });
});
