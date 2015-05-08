define(['js/components/api_request', 'js/components/multi_params', 'js/components/api_query',
  'backbone'], function(ApiRequest, MultiParams, ApiQuery, Backbone) {
  describe("ApiRequest (API)", function () {
      

    it("should return bare API params object", function() {
      expect(new ApiRequest({target: 'foo'})).to.be.instanceof(ApiRequest);
      expect(new ApiRequest({target: 'foo'})).not.to.be.instanceof(MultiParams);
      expect(new ApiRequest({target: 'foo'})).not.to.be.instanceof(Backbone.Model);

      expect(new ApiRequest({target: 'foo'}).clone()).to.be.instanceof(ApiRequest);
      expect(new ApiRequest({target: 'foo'}).clone()).not.to.be.instanceof(MultiParams);
      expect(new ApiRequest({target: 'foo'}).clone()).not.to.be.instanceof(Backbone.Model);

      expect(new ApiRequest({target: 'foo'}).load('foo')).to.be.instanceof(ApiRequest);
      expect(new ApiRequest({target: 'foo'}).load('foo')).not.to.be.instanceof(MultiParams);
      expect(new ApiRequest({target: 'foo'}).load('foo')).not.to.be.instanceof(Backbone.Model);
    });


    it("accepts only certain keys, throws errors on others", function() {
      var r = new ApiRequest({target: 'foo'});
      expect(r.get('target')).to.eql('foo');
      expect(r.set('target', 'bar')).to.be.OK;
      expect(r.get('target')).to.eql('bar');

      // it accepts only api query objects if the query parameter is provided
      var q = new ApiQuery();
      expect(r.get('query')).to.eql(undefined);
      expect(r.set('query', q)).to.be.OK;
      expect(r.set('query', q)).to.be.OK;
      expect(r.get('query')).to.equal(q);
      expect(function() {r.set('query', 'baz')}).to.throw(Error);

      expect(r.get('sender')).to.eql(undefined);
      expect(r.set('sender', 'usama')).to.be.OK;
      expect(r.get('sender')).to.eql('usama');

      // trying ot set other values results in error
      expect(function() {r.set('foo', 'baz')}).to.throw(Error);
    });

    it("has methods for manipulating keys/values", function() {
      var q = new ApiRequest({target: 'bar'});
      expect(q.get('target')).to.eql('bar');

      q.set('target', ['bar', 'baz']);
      expect(q.get('target')).to.eql(['bar', 'baz']);

      expect(q.toJSON()).to.eql({'target': ['bar', 'baz']});

      expect(q.url()).to.eql('bar/baz');
      expect(new ApiRequest().load('bar/baz').toJSON()).to.eql({'target': 'bar/baz'});

      expect(q.clone()).to.not.eql(q);
      expect(q.clone().toJSON()).to.eql(q.toJSON());

      q.clear();
      expect(q.toJSON()).to.eql({});

      q.set('target', 'boo');
      expect(q.has('target')).to.be.true;

      q.unset('target');
      expect(q.has('target')).to.be.false;

      q.set({'target': 'boo', 'sender': 'bee'});
      expect(q.pairs()).to.eql([['target', 'boo'], ['sender', 'bee']]);
      expect(q.keys()).to.eql(['target', 'sender']);
      expect(q.values()).to.eql(['boo', 'bee']);

      q.set('sender', 'mee');
      expect(q.hasChanged()).to.be.true;
      expect(q.previous('sender')).to.eql('bee');
      expect(q.previousAttributes()).to.eql({'target': 'boo', 'sender': 'bee'});

    });

    it("can be de/serialized", function() {
      var r = new ApiRequest({'target': 'foo', 'query': new ApiQuery({q:'foo', b: 'boo'})});
      expect(r.url()).to.equal('foo?b=boo&q=foo');

      expect(new ApiRequest({target:'foo', sender: 'boo'}).url()).to.be.equal('foo#sender=boo');
      expect(new ApiRequest({target:'foo', sender: 'boo', query: new ApiQuery({q: 'one'})}).url()).to.be.equal('foo?q=one#sender=boo');

      r = new ApiRequest().load('foo?q=one#sender=boo');
      expect(r.get('target')).to.eql('foo');
      expect(r.get('sender')).to.eql('boo');

      r = new ApiRequest().load('foo?q=one#sender=boo&sender=woo');
      expect(r.get('target')).to.eql('foo');
      expect(r.get('query').url()).to.eql('q=one');
      expect(r.get('sender')).to.eql(['boo', 'woo']);
    });
    

    it("can be made immutable and throws error on modification", function() {
      var q = new ApiRequest({'target': 'bar'});
      q.lock();
      expect(function() {q.set('url', 'baz')}).to.throw("Object locked");
      expect(q.isLocked()).to.be.true;
      q.unlock();
      expect(q.isLocked()).to.be.false;
      expect(q.set('target', 'baz')).to.be.undefined;
    });
    

  });
});
