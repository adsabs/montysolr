/**
 * Created by rchyla on 3/28/14.
 */

define(['jquery', 'underscore',
  'js/services/api', 'js/components/api_request', 'js/components/api_query'], function($, _, Api, ApiRequest, ApiQuery) {
  describe("Api Service (API)", function() {


    beforeEach(function() {
      this.server = sinon.fakeServer.create();
      //sinon.stub($, 'ajax').yieldsTo('done', apiResponseOK);
    });

    afterEach(function() {
      this.server.restore();
      //$.ajax.restore();
    });

    it('should return API object', function(done) {
      expect(new Api()).to.be.instanceof(Api);
      done();
    });

    it('should accept ApiRequest objects only', function(done) {
      var api = new Api();
      sinon.stub(api, 'trigger');

      expect(api.request(new ApiRequest({target: 'foo'}))).to.be.OK;
      this.server.respond();
      expect(api.trigger.calledOnce).to.be.true;

      expect(function() {api.request({url: './'});}).to.throw(Error);
      done();
    });

    it("should call appropriate callback upon arrival of data", function() {
      var api = new Api({url: '/api/1'}); // url is there, but i want to be explicit
      sinon.stub(api, 'done');
      sinon.stub(api, 'fail');
      sinon.stub(api, 'trigger');

      var r = new ApiRequest({target: 'search', query: new ApiQuery({q: 'foo'}), sender: 'woo'});


      this.server.respondWith("/api/1/search",
        [200, { "Content-Type": "application/json" }, validResponse]);

      api.request(r);

      this.server.respond();

      expect(api.done.calledOnce).to.be.true;
      expect(api.fail.calledOnce).to.be.false;
      console.log(api.trigger.args);

    });


    var validResponse = '{\
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
          "title":["<em>Star</em> Streams"]}}}';

  });
});
