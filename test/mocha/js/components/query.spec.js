define(['js/components/query', 'js/components/multi_params', 'backbone'], function(ApiQuery, MultiParams, Backbone) {
  describe("API params object", function () {
      
    // Runs once before all tests start.
    before(function () {
    });
  
    // Runs once when all tests finish.
    after(function () {
    });
  
    it("should return bare API params object", function() {
      expect(new ApiQuery()).to.be.instanceof(Object);
      expect(new ApiQuery()).not.to.be.instanceof(MultiParams);
      expect(new ApiQuery()).not.to.be.instanceof(Backbone.Model);

      expect(new ApiQuery().clone()).to.be.instanceof(Object);
      expect(new ApiQuery().clone()).not.to.be.instanceof(MultiParams);
      expect(new ApiQuery().clone()).not.to.be.instanceof(Backbone.Model);
    });
    
    it("has methods for manipulating keys/values", function() {
      var q = new ApiQuery({'foo': 'bar'});
      expect(q.get('foo')).to.eql(['bar']);

      q.set('foo', ['bar', 'baz']);
      expect(q.get('foo')).to.eql(['bar', 'baz']);

      expect(q.toJSON()).to.eql({'foo': ['bar', 'baz']});

      expect(q.url()).to.eql('foo=bar&foo=baz');
      expect(new ApiQuery().load('foo=bar&foo=baz').toJSON()).to.eql({'foo': ['bar', 'baz']});

      expect(q.clone()).to.not.equal(q);
      expect(q.clone().toJSON()).to.eql(q.toJSON());

      q.clear();
      expect(q.toJSON()).to.eql({});

      q.set('foo', 'boo');
      q.set('hey', 'boo');
      expect(q.has('foo')).to.be.true;
      expect(q.has('hey')).to.be.true;
      expect(q.has('boo')).to.be.false;

      q.unset('hey');
      expect(q.has('foo')).to.be.true;
      expect(q.has('hey')).to.be.false;
      expect(q.has('boo')).to.be.false;

      q.set('bumble', 'bee');
      expect(q.pairs()).to.eql([['foo', ['boo']], ['bumble', ['bee']]]);
      expect(q.keys()).to.eql(['foo', 'bumble']);
      expect(q.values()).to.eql([['boo'], ['bee']]);

      q.set('bumble', 'mee');
      expect(q.hasChanged()).to.be.true;
      expect(q.previous('bumble')).to.eql(['bee']);
      expect(q.previousAttributes()).to.eql({'foo': ['boo'], bumble: ['bee']});

      q.add('bumble', 'eee');
      expect(q.get('bumble')).to.eql(['mee', 'eee']);

    });
    
    it("hides parameters, you cannot directly change them", function() {
      expect(new ApiQuery().attributes).to.be.undefined;
      expect(new ApiQuery().save).to.be.undefined;
      expect(new ApiQuery().sync).to.be.undefined;
    });
    

    it("can be made immutable and throws error on modification", function() {
      var q = new ApiQuery({'foo': 'bar'});
      q.lock();
      expect(function() {q.set('foo', 'baz')}).to.throw("Query locked");
      expect(q.isLocked()).to.be.true;
      q.unlock();
      expect(q.isLocked()).to.be.false;
      expect(q.set('foo', 'baz')).to.be.ok;
    });
    

  });
});
