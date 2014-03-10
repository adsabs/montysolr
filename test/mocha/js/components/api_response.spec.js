define(['js/components/json_response', 'backbone', 'jquery'], function(Response, Backbone, $) {
  describe("Main Response Object (API)", function () {
      
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

    it('has simple api to retrieve values', function() {
    });

    it("is immutable by default and ignores modifications", function() {
      var rsp =  new Response(this.jsonData);
      expect(rsp.get('response.docs[0]')).to.eql
    });
    
    it("can accept any type of keys/values, of unlimited structure");
    
    it("is forgiving (accessing unknown keys is not throwing error), but strict mode is possible");
    
    it("can be serialized and de-serialized (saved as string and reloaded)");
    
    it("contains the original query (but not the request itself)");
    
    it("provides a key under which this object can be cached/retrieved");
    
    
  });
});
