define(['api_query', 'backbone'], function(ApiQuery, Backbone) {
  describe("API query object", function () {
      
    // Runs once before all tests start.
    before(function () {
    });
  
    // Runs once when all tests finish.
    after(function () {
    });
  
    it("should return API query object", function() {
      expect(new ApiQuery()).to.be.an.instanceof(Backbone.Model);

    });
    
    it("should store all values as arrays", function() {
      var t = new ApiQuery();

      t.set('string', 'bar');
      expect(t.get('string')).to.eql(['bar']);

      t.set('array', ['bar']);
      expect(t.get('array')).to.eql(['bar']);

      t.set('array with empty element', ['bar', '']);
      expect(t.get('array with empty element')).to.eql(['bar']);

      t.set({'foo': 'bar'});
      expect(t.get('foo')).to.eql(['bar']);

      t.set('number', 8);
      expect(t.get('number')).to.eql([8]);


      // test only strings/numbers are allowed
      assert.throw(function() {t.set('foo', {key: 'value'})}, Error);
      assert.throw(function() {t.set('foo', [['foo']])}, Error);
      assert.throw(function() {t.set('foo', null)}, Error);


      // the validation can be surpressed
      var Q2 = ApiQuery.extend({
        _validate: function(attrs, options) {
          return true;
        }
      });
      t = new Q2();


      assert.doesNotThrow(function() {t.set('foo', {key: 'value'})}, Error);
      assert.doesNotThrow(function() {t.set('foo', [['foo']])}, Error);
      assert.doesNotThrow(function() {t.set('foo', null)}, Error);

    });

    it("can be overwritten, but provides old values", function() {
      var t = new ApiQuery({'foo': ['boo', 'woo']});
      expect(t.get('foo')).to.eql(['boo', 'woo']);
      expect(t.previous('foo')).to.eql(undefined);

      t.set('foo', 'bar');
      expect(t.get('foo')).to.eql(['bar']);
      expect(t.previous('foo')).to.eql(['boo', 'woo']);

      t.set('foo', 'baz');
      expect(t.get('foo')).to.eql(['baz']);
      expect(t.previous('foo')).to.eql(['bar']);

    });
    

    it("can be serialized and de-serialized (saved as string and reloaded)", function() {
      var t = new ApiQuery({'foo': ['bar', 'baz'], 'boo': ['woo', 1]});
      expect(t.url()).to.equal('boo=1&boo=woo&foo=bar&foo=baz');

      t = new ApiQuery({'foo': ['bar', 'baz'], 'boo': ['woo', '1']});
      expect(t.url()).to.equal('boo=1&boo=woo&foo=bar&foo=baz');

      expect(t.parse('foo=bar&foo=baz&boo=woo&boo=1')).to.eql({'foo': ['bar', 'baz'], 'boo': ['woo', '1']});
    });
    
    it("can be cloned", function() {
      var t = new ApiQuery({'foo': ['boo', 'woo']});
      var t2 = t.clone();

      expect(t).to.be.not.equal(t2);
      expect(t.attributes).to.eql({'foo': ['boo', 'woo']});
      expect(t.attributes).to.eql(t2.attributes);

      // and callbacks still work
      assert.throw(function() {t2.set('foo', {key: 'value'})}, Error);
    });

    it("cannot be sync'ed to the server (it is state-less)", function() {
      expect(function() {var t = new ApiQuery({'foo': 'bar'});t.sync()}).to.throw("ApiQuery cannot be saved to server");
      expect(function() {var t = new ApiQuery({'foo': 'bar'});t.save()}).to.throw("ApiQuery cannot be saved to server");
      expect(function() {var t = new ApiQuery({'foo': 'bar'});t.fetch()}).to.throw("ApiQuery cannot be saved to server");
      expect(function() {var t = new ApiQuery({'foo': 'bar'});t.destroy()}).to.not.throw("ApiQuery cannot be saved to server");
    })

  });
});
