define(['js/components/api_request', 'js/components/multi_params', 'js/components/api_query',
  'backbone'], function(ApiRequest, MultiParams, ApiQuery, Backbone) {
  describe.skip("ApiRequest (API)", function () {
      
    // Runs once before all tests start.
    before(function () {
    });
  
    // Runs once when all tests finish.
    after(function () {
    });
  
    it("should return bare API params object", function() {
      expect(new ApiRequest({target: 'foo'})).to.be.instanceof(ApiRequest);
      expect(new ApiRequest({target: 'foo'})).not.to.be.instanceof(MultiParams);
      expect(new ApiRequest({target: 'foo'})).not.to.be.instanceof(Backbone.Model);

      expect(new ApiRequest({target: 'foo'}).clone()).to.be.instanceof(ApiRequest);
      expect(new ApiRequest({target: 'foo'}).clone()).not.to.be.instanceof(MultiParams);
      expect(new ApiRequest({target: 'foo'}).clone()).not.to.be.instanceof(Backbone.Model);
    });
    
    it("accepts only certain keys, throws errors on others", function() {
      var r = new ApiRequest({target: 'foo'});
      expect(r.get('target')).to.eql(null);
      expect(r.set('target', 'bar')).to.be.OK;
      expect(r.get('target')).to.eql(['bar']);

      // it accepts only api query objects
      var q = new ApiQuery();
      expect(r.get('query')).to.eql(null);
      expect(r.set('query', q)).to.be.OK;
      expect(r.get('query')).to.equal(q);
      expect(function() {r.set('query', 'baz')}).to.throw(Error);

      expect(r.get('sender')).to.eql(null);
      expect(r.set('sender', 'usama')).to.be.OK;
      expect(r.get('sender')).to.eql(['usama']);

      // trying ot set other values results in error
      expect(function() {q.set('foo', 'baz')}).to.throw(Error);
    });
    
    it("can be de/serialized", function() {
      var r = new ApiRequest({'target': 'foo', 'query': new ApiQuery({q:'foo', b: 'boo'})});
      expect(r.url()).to.equal('/target?q=foo&b=boo');

      expect(new ApiRequest({target:'foo', sender: 'boo'}).url()).to.be.equal('/foo#sender=boo');
      expect(new ApiRequest({target:'foo', sender: 'boo', query: new ApiQuery({q: 'one'})}).url()).to.be.equal('/foo?q=one#sender=boo');

      r = new ApiRequest().load('/foo?q=one#sender=boo');
      expect(r.get('target')).to.eql(['foo']);
      expect(r.get('sender')).to.eql(['boo']);

      r = new ApiRequest().load('/foo?q=one#sender=boo&sendor=woo');
      expect(r.get('target')).to.eql(['foo']);
      expect(r.get('q')).to.eql(['one']);
      expect(r.get('sender')).to.eql(['boo', 'woo']);
    });
    

    it("can be made immutable and throws error on modification", function() {
      var q = new ApiRequest({'url': 'bar'});
      q.lock();
      expect(function() {q.set('url', 'baz')}).to.throw("Request locked");
      expect(q.isLocked()).to.be.true;
      q.unlock();
      expect(q.isLocked()).to.be.false;
      expect(q.set('url', 'baz')).to.be.ok;
    });
    

  });
});
