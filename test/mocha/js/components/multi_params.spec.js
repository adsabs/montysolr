define(['js/components/multi_params', 'backbone'], function(MultiParams, Backbone) {
  describe("Multi Params (API)", function () {
      
    // Runs once before all tests start.
    before(function () {
    });
  
    // Runs once when all tests finish.
    after(function () {
    });
  
    it("should return API params model", function() {
      expect(new MultiParams()).to.be.an.instanceof(Backbone.Model);

    });
    
    it("should store all values as arrays", function() {
      var t = new MultiParams();

      t.set('string', 'bar');
      expect(t.get('string')).to.eql(['bar']);

      t.set('array', [[['bar'], 'baz']]);
      expect(t.get('array')).to.eql(['bar', 'baz']);

      t.set('array', ['bar']);
      expect(t.get('array')).to.eql(['bar']);

      t.set('array with empty element', ['bar', '']);
      expect(t.get('array with empty element')).to.eql(['bar']);

      t.set({'foo': 'bar'});
      expect(t.get('foo')).to.eql(['bar']);

      t.set('number', 0);
      expect(t.get('number')).to.eql([0]);

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
      var Q2 = MultiParams.extend({
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
      var t = new MultiParams({'foo': ['boo', 'woo']});
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
      var t = new MultiParams({'foo': ['bar', 'baz'], 'boo': ['woo', 1]});
      expect(t.url()).to.equal('boo=woo&boo=1&foo=bar&foo=baz');

      t = new MultiParams({'foo': '{!type=aqp fq=title v=$vv}', 'vv': 'title:foo'});
      expect(t.url()).to.equal('foo=%7B!type%3Daqp+fq%3Dtitle+v%3D%24vv%7D&vv=title%3Afoo');

      t = new MultiParams().load('foo=%7B!type%3Daqp+fq%3Dtitle+v%3D%24vv%7D&vv=title%3Afoo');
      expect(t.get('foo')).to.be.eql(['{!type=aqp fq=title v=$vv}']);
      expect(t.get('vv')).to.be.eql(['title:foo']);

      t = new MultiParams({'foo': ['baz', 'bar'], 'boo': ['woo', '1']});
      expect(t.url()).to.equal('boo=woo&boo=1&foo=baz&foo=bar');

      // the order needs to be preserved
      expect(t.parse('foo=bar&foo=baz&boo=woo&boo=1')).to.eql({'foo': ['bar', 'baz'], 'boo': ['woo', '1']});
      expect(t.parse('foo=baz&foo=bar&boo=1&boo=woo')).to.eql({'foo': ['baz', 'bar'], 'boo': ['1', 'woo']});

      t = t.load('facet=true&facet.field=author_facet_hier&facet.mincount=1&facet.prefix=0%2F&fl=id&fq=%7B!type%3Daqp+v%3D%24fq_author_facet_hier%7D&fq_author_facet_hier=(0%5C%2FWang%2C%5C+J+AND+0%5C%2FLee%2C%5C+J)&q=star');

      expect(t.get('fq_author_facet_hier')).to.eql(['(0\\/Wang,\\ J AND 0\\/Lee,\\ J)']);
      expect(t.get('fq')).to.be.eql(["{!type=aqp v=$fq_author_facet_hier}"]);

      var t2 = t.clone();
      expect(t2.get('fq_author_facet_hier')).to.eql(['(0\\/Wang,\\ J AND 0\\/Lee,\\ J)']);
      expect(t2.get('fq')).to.be.eql(["{!type=aqp v=$fq_author_facet_hier}"]);

    });
    
    it("can be cloned", function() {
      var t = new MultiParams({'foo': ['boo', 'woo']});
      var t2 = t.clone();

      expect(t).to.be.not.equal(t2);
      expect(t.attributes).to.eql({'foo': ['boo', 'woo']});
      expect(t.attributes).to.eql(t2.attributes);

      // and the old values are unchanged
      t2.add('foo', 'zoo');
      expect(t.attributes).to.eql({'foo': ['boo', 'woo']});
      expect(t2.attributes).to.eql({'foo': ['boo', 'woo', 'zoo']});

      // and callbacks still work
      assert.throw(function() {t2.set('foo', {key: 'value'})}, Error);
    });

    it("cannot be sync'ed to the server (it is state-less)", function() {
      expect(function() {var t = new MultiParams({'foo': 'bar'});t.sync()}).to.throw("MultiParams cannot be saved to server");
      expect(function() {var t = new MultiParams({'foo': 'bar'});t.save()}).to.throw("MultiParams cannot be saved to server");
      expect(function() {var t = new MultiParams({'foo': 'bar'});t.fetch()}).to.throw("MultiParams cannot be saved to server");
      expect(function() {var t = new MultiParams({'foo': 'bar'});t.destroy()}).to.not.throw("MultiParams cannot be saved to server");
    })

    it("Can contain local parameters", function() {
      var t = new MultiParams().load('__author_facet_hier_fq_author=AND&__author_facet_hier_fq_author=author:"Lee,+J"&facet.field=author_facet_hier&facet.mincount=1&fl=id&fq={!type=aqp+cache=false+cost=150+v=$fq_author}&fq_author=(author:"Lee,+J")&q=star');
      expect(t.get('fq')).to.be.eql(['{!type=aqp cache=false cost=150 v=$fq_author}']);
    })

  });
});
