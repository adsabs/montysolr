define(['js/components/json_response', 'backbone', 'jquery'], function(Response, Backbone, $) {
  describe("JSON Response Object (API)", function () {

    // Runs once before all tests start.
    // test: http://adswhy:9000/solr/collection1/select?q=title%3Astar&fq=database%3Aastronomy&start=10&rows=5&fl=title%2Cbibcode%2Cauthor&wt=json&indent=true&hl=true&hl.fl=title&hl.simple.pre=%3Cem%3E&hl.simple.post=%3C%2Fem%3E&facet=true&facet.query=title%3Astar&facet.field=author
    before(function () {
      this.jsonData = $.parseJSON(
        '{\
        "responseHeader":{\
          "status":0,\
            "QTime":88,\
            "params":{\
            "facet":"true",\
              "indent":"true",\
              "hl.simple.pre":"<em>",\
              "hl.fl":"title",\
              "wt":"json",\
              "hl":"true",\
              "rows":"5",\
              "fl":"title,bibcode,author",\
              "start":"10",\
              "facet.query":"title:star",\
              "q":"title:star",\
              "hl.simple.post":"</em>",\
              "facet.field":"author",\
              "fq":"database:astronomy"}},\
        "response":{"numFound":172978,"start":10,"docs":[\
          {\
            "author":["Morrison, J."],\
            "bibcode":"1903PA.....11...88M",\
            "title":["The Star of Bethlehem"]},\
          {\
            "author":["Morrison, J."],\
            "bibcode":"1903PA.....11..122M",\
            "title":["The Star of Bethlehem (cont.)"]},\
          {\
            "author":["Wing, Daniel E."],\
            "bibcode":"1903PA.....11..481W",\
            "title":["Star Dust"]},\
          {\
            "bibcode":"1906PA.....14R.507.",\
            "title":["Star Charts"]},\
          {\
            "author":["Holmes, Edwin"],\
            "bibcode":"1894JBAA....5...26H",\
            "title":["Star Streams"]}]\
        },\
        "facet_counts":{\
          "facet_queries":{\
            "title:star":172978},\
          "facet_fields":{\
            "author":[\
              "heber, u",301,\
              "linsky, j l",299,\
              "mayor, m",273,\
              "kurtz, d w",270,\
              "pickering, edward c",262,\
              "maeder, a",260,\
              "schmitt, j h m m",259,\
              "campbell, leon",256,\
              "mundt, r",138]},\
          "facet_dates":{},\
          "facet_ranges":{}},\
        "highlighting":{\
          "166243":{\
            "title":["The <em>Star</em> of Bethlehem"]},\
          "166285":{\
            "title":["The <em>Star</em> of Bethlehem (cont.)"]},\
          "166785":{\
            "title":["<em>Star</em> Dust"]},\
          "192734":{\
            "title":["<em>Star</em> Charts"]},\
          "139757":{\
            "title":["<em>Star</em> Streams"]}}}'
      );

    });

    // Runs once when all tests finish.
    after(function () {
      delete this.jsonData;
    });

    it("should return API response object", function() {
      expect(new Response()).to.be.an.instanceof(Object);
      expect(Response.extend).to.be.OK;


      var test = Response.extend({
        foo: function() {
          //pass;
        }
      });
      expect(new test()).to.be.instanceof(Response);


    });

    it('has simple api to retrieve values (keys/values, of unlimited structure)', function() {
      var rsp =  new Response(this.jsonData);

      expect(rsp.get()).to.have.keys(['response', 'facet_counts', 'responseHeader', 'highlighting']);
      expect(rsp.get(null)).to.have.keys(['response', 'facet_counts', 'responseHeader', 'highlighting']);

      expect(rsp.get('response')).to.have.keys(['numFound', 'start', 'docs']);
      expect(rsp.get('response.numFound')).to.be.equal(172978);

      expect(rsp.get('responseHeader.status')).to.be.equal(0);
      expect(rsp.get('responseHeader.params.facet')).to.be.equal('true');
      expect(rsp.get('responseHeader.params["facet.query"]')).to.be.equal('title:star');
      expect(rsp.get('responseHeader.params[\'facet.query\']')).to.be.equal('title:star');
      expect(rsp.get('responseHeader.params.fl')).to.be.equal('title,bibcode,author');


      expect(rsp.get('response.docs[0]')).to.have.keys(['author', 'bibcode', 'title']);
      expect(rsp.get('response["docs"][0]')).to.have.keys(['author', 'bibcode', 'title']);
      expect(rsp.get('response["docs"]["0"]')).to.have.keys(['author', 'bibcode', 'title']);


      expect(function() {rsp.get('response["docs"]["0]')}).to.throw(Error);
      expect(function() {rsp.get('response["docs"][10]')}).to.throw(Error);
      expect(function() {rsp.get('response["docs"][-1]')}).to.throw(Error);

      expect(rsp.get('facet_counts.facet_fields')).to.have.keys(['author']);
      expect(rsp.get('facet_counts.facet_fields.author[0]')).to.be.equal('heber, u');
      expect(rsp.get('facet_counts.facet_fields.author[1]')).to.be.equal(301);
    });

    it("is immutable by default and ignores modifications", function() {
      var rsp =  new Response(this.jsonData);
      expect(rsp.get()).to.have.keys(['response', 'facet_counts', 'responseHeader', 'highlighting']);

      var data = rsp.get();
      data.foo = ['foo'];

      expect(data).to.have.keys(['response', 'facet_counts', 'responseHeader', 'highlighting', 'foo']);
      expect(data.foo).to.be.eql(['foo']);
      expect(rsp.get()).to.not.have.keys(['foo']);
      expect(function() {rsp.set('response', ['foo'])}).to.throw(Error);

      // however can be created mutable
      rsp = new Response(this.jsonData, {readOnly: false});
      var data = rsp.get();
      data.foo = ['foo'];
      expect(rsp.get()).to.have.keys(['response', 'facet_counts', 'responseHeader', 'highlighting', 'foo']);

      rsp.set('boo', 'bar');
      expect(rsp.get('boo')).to.be.equal('bar');


    });


    it("can be serialized and de-serialized (saved as string and reloaded)", function() {
      var rsp =  new Response(this.jsonData);
      var rsp2 = rsp.clone();
      expect(rsp.get()).to.be.eql(rsp2.get());

      var rsp3 = new Response(JSON.parse(JSON.stringify(rsp.toJSON())));
      expect(rsp.get()).to.be.eql(rsp3.get());
    });


    it("provides a key under which this object can be cached/retrieved", function() {
      var rsp =  new Response(this.jsonData);
      expect(rsp.url()).to.not.be.undefined;

      rsp =  new Response(this.jsonData, {'url': 'foo=bar&boo=bar'});
      expect(rsp.url()).to.be.equal('foo=bar&boo=bar');
    });


  });
});
