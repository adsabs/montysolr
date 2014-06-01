define(['js/components/solr_params', 'backbone'], function(SolrParams, Backbone) {
  describe("SOLR Params (API)", function () {
      

    it ("should treat certain fields specially", function() {
      var t = new SolrParams({'q': ['full:foo', 'title:bar'], 'fq': ['database:astronomy', 'aff:xxx']});
      // 'fq=aff:xxx&fq=database:astronomy&q=full:foo AND title:bar'
      expect(t.url()).to.equal('fq=database%3Aastronomy&fq=aff%3Axxx&q=full%3Afoo+AND+title%3Abar');

      // but the original values should remain untouched
      expect(t.get('q')).to.eql(['full:foo', 'title:bar']);


    });


    // ------
    // everything below is just a copy from the multi_params.spec.js
    // ------

    it("should return API params model", function() {
      expect(new SolrParams()).to.be.an.instanceof(Backbone.Model);

    });
    
    it("should store all values as arrays", function() {
      var t = new SolrParams();

      t.set('string', 'bar');
      expect(t.get('string')).to.eql(['bar']);

      t.set('array', ['bar']);
      expect(t.get('array')).to.eql(['bar']);

      t.set('array', [['bar']]);
      expect(t.get('array')).to.eql(['bar']);

      t.set('array with empty element', ['bar', '']);
      expect(t.get('array with empty element')).to.eql(['bar']);

      t.set({'foo': 'bar'});
      expect(t.get('foo')).to.eql(['bar']);

      t.set('number', 8);
      expect(t.get('number')).to.eql([8]);

      t.add('number', '9');
      expect(t.get('number')).to.eql([8, '9']);

      t.add({'string': 'baz', 'array': ['baz']});
      expect(t.get('string')).to.eql(['bar', 'baz']);
      expect(t.get('array')).to.eql(['bar', 'baz']);

      // test only strings/numbers are allowed
      assert.throw(function() {t.set('foo', {key: 'value'})}, Error);
      assert.throw(function() {t.set('foo', null)}, Error);


      // the validation can be surpressed
      var Q2 = SolrParams.extend({
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
      var t = new SolrParams({'foo': ['boo', 'woo']});
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
      var t = new SolrParams({'foo': ['bar', 'baz'], 'boo': ['woo', 1]});
      expect(t.url()).to.equal('boo=woo&boo=1&foo=bar&foo=baz');

      t = new SolrParams({'foo': ['bar', 'baz'], 'boo': ['woo', '1']});
      expect(t.url()).to.equal('boo=woo&boo=1&foo=bar&foo=baz');

      expect(t.parse('foo=bar&foo=baz&boo=woo&boo=1')).to.eql({'foo': ['bar', 'baz'], 'boo': ['woo', '1']});
    });
    
    it("can be cloned", function() {
      var t = new SolrParams({'foo': ['boo', 'woo']});
      var t2 = t.clone();

      expect(t).to.be.not.equal(t2);
      expect(t.attributes).to.eql({'foo': ['boo', 'woo']});
      expect(t.attributes).to.eql(t2.attributes);

      // and callbacks still work
      assert.throw(function() {t2.set('foo', {key: 'value'})}, Error);
    });

    it("cannot be sync'ed to the server (it is state-less)", function() {
      expect(function() {var t = new SolrParams({'foo': 'bar'});t.sync()}).to.throw("MultiParams cannot be saved to server");
      expect(function() {var t = new SolrParams({'foo': 'bar'});t.save()}).to.throw("MultiParams cannot be saved to server");
      expect(function() {var t = new SolrParams({'foo': 'bar'});t.fetch()}).to.throw("MultiParams cannot be saved to server");
      expect(function() {var t = new SolrParams({'foo': 'bar'});t.destroy()}).to.not.throw("MultiParams cannot be saved to server");
    })

  });
});
