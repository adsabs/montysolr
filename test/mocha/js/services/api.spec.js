/**
 * Created by rchyla on 3/28/14.
 */

define([
  'jquery',
  'underscore',
  'js/services/api',
  'js/components/api_request',
  'js/components/api_query',
  'js/components/api_response'
], function(
  $,
  _,
  Api,
  ApiRequest,
  ApiQuery,
  ApiResponse
  ) {

  describe("Api Service (api.spec.js)", function() {
    beforeEach(function(done) {
      this.server = sinon.fakeServer.create();
      this.server.autoRespond = false;
      this.server.respondWith(/\/api\/1\/search.*/,
        [200, { "Content-Type": "application/json" }, validResponse]);
      this.server.respondWith(/\/api\/1\/parseerror.*/,
        [200, { "Content-Type": "application/json" }, validResponse.substring(2)]);
      this.server.respondWith(/\/api\/1\/error.*/,
        [500, { "Content-Type": "application/json" }, validResponse.substring(2)]);
      this.server.respondWith(/http:\/\/foo.*/,
        [200, { "Content-Type": "application/json" }, validResponse]);
      //sinon.stub($, 'ajax').yieldsTo('done', apiResponseOK);
      done();
    });

    afterEach(function(done) {
      this.server.restore();
      //$.ajax.restore()
      done();
    });

    it('should return API object', function(done) {
      expect(new Api()).to.be.instanceof(Api);
      done();
    });

    it('should accept ApiRequest objects only', function(done) {
      var api = new Api();
      sinon.stub(api, 'trigger');

      expect(function() {api.request({query: 'q=foo'});}).to.throw(Error);
      expect(function() {api.request(new ApiRequest({query: 'q=foo'}))}).to.throw(Error);
      expect(function() {
        api.url = '';
        api.request(new ApiRequest({query: new ApiQuery()}))}
      ).to.throw(Error);
      this.server.respond();
      done();
    });

    it("should look at the ApiRequest to check whether to send a get (with url data) or post request (with json data)", function(done){

      var api = new Api({url: '/api/1'}); // url is there, but i want to be explicit

      var q = new ApiQuery({q: 'foo'});
      api.request(new ApiRequest({target: 'search', query: q}));
      this.server.respond();

      expect(this.server.requests[0].method).to.eql("GET");
      expect(this.server.requests[0].requestHeaders["Content-Type"]).to.eql("application/x-www-form-urlencoded");

      var q = new ApiQuery({q: 'foo'});
      api.request(new ApiRequest({target: 'search', query: q, options : {method : "POST", contentType: 'application/json' }}));
      this.server.respond();

      expect(this.server.requests[1].method).to.eql("POST");
      expect(this.server.requests[1].url).to.eql("/api/1/search");
      expect(this.server.requests[1].requestHeaders["Content-Type"]).to.eql('application/json;charset=utf-8');
      expect(this.server.requests[1].requestBody).to.eql("{\"q\":[\"foo\"]}");
      done();

    });

    it("should allow to override anything via options", function() {
      var api = new Api({url: '/nonexisting/1'});
      var q = new ApiQuery({q: 'foo'});
      var spy = sinon.spy();
      api.request(new ApiRequest({target: 'search', query: q}), {url: 'http://foo.dot.com'})
        .done(spy);
      this.server.respond();
      expect(spy.callCount).to.eql(1);

      api.request(new ApiRequest({target: 'search', query: q, options:{url: 'http://foo.dot.com'}}))
        .done(spy);
      this.server.respond();
      expect(spy.callCount).to.eql(2);

    });

    it("should call appropriate callback upon arrival of data", function(done) {
      var api = new Api({url: '/api/1'}); // url is there, but i want to be explicit
      sinon.stub(api, 'trigger');
      var q = new ApiQuery({q: 'foo'});

      expect(api.request(new ApiRequest({target: 'search', query: q, sender: 'woo'}))).to.be.OK;
      this.server.respond();

      expect(api.trigger.calledOnce).to.be.true;
      var a = api.trigger.firstCall.args;
      expect(a[0]).to.be.equal('api-response');
      expect(a[1]).to.be.instanceof(ApiResponse);
      expect(a[1].getApiQuery()).to.be.instanceof(ApiQuery);
      expect(a[1].getApiQuery()).to.eql(q);
      done();
    });

    it("should call error handlers on failed request", function(done) {
      var api = new Api({url: '/api/1'}); // url is there, but i want to be explicit
      sinon.stub(api, 'trigger');
      var q = new ApiQuery({q: 'foo'});

      expect(api.request(new ApiRequest({target: 'error', query: q, sender: 'woo'}))).to.be.OK;
      this.server.respond();
      expect(api.trigger.calledOnce).to.be.true;
      var a = api.trigger.firstCall.args;
      expect(a[0]).to.be.equal('api-error');
      expect(a[1].api).to.be.eql(api);
      expect(a[1].request).to.be.instanceof(ApiRequest);
      expect(a[2].status).to.be.eql(500); //jqXHR
      expect(a[3]).to.be.OK; //textStatus
      expect(a[4]).to.be.equal('Internal Server Error'); //errorThrown
      done();
    });

    it("should call error handlers when response is not valid", function() {
      var api = new Api({url: '/api/1'}); // url is there, but i want to be explicit
      sinon.stub(api, 'trigger');
      var q = new ApiQuery({q: 'foo'});

      expect(api.request(new ApiRequest({target: 'parseerror', query: q, sender: 'woo'}))).to.be.OK;
      this.server.respond();
      expect(api.trigger.calledOnce).to.be.true;
      var a = api.trigger.firstCall.args;
      expect(a[0]).to.be.equal('api-error');
      expect(a[1].api).to.be.eql(api);
      expect(a[1].request).to.be.instanceof(ApiRequest);
      expect(a[2].status).to.be.eql(200); //jqXHR
      expect(a[3]).to.be.OK; //textStatus
      expect(a[4]).to.be.instanceof(Error); //errorThrown
    });

    it("should call 'always' handler (even if redefined)", function() {
      var api = new Api({url: '/api/1'}); // url is there, but i want to be explicit
      var spy = sinon.spy();
      sinon.spy(api, 'always', api.always);
      var q = new ApiQuery({q: 'foo'});

      expect(api.getNumOutstandingRequests()).to.equal(0);
      expect(api.request(new ApiRequest({target: 'parseerror', query: q}),
        {always: spy})).to.be.OK;
      expect(api.getNumOutstandingRequests()).to.equal(1);
      this.server.respond();
      expect(api.getNumOutstandingRequests()).to.equal(0);
      expect(spy.callCount).to.be.equal(1);
      expect(api.always.callCount).to.be.equal(1);
    });

    it("should call 'done' handler (even if redefined)", function() {
      var api = new Api({url: '/api/1'}); // url is there, but i want to be explicit
      var spy = sinon.spy();
      sinon.spy(api, 'done', api.done);
      var q = new ApiQuery({q: 'foo'});

      expect(api.request(new ApiRequest({target: 'search', query: q}),
        {done: spy})).to.be.OK;
      this.server.respond();
      expect(spy.callCount).to.be.equal(1);
      expect(spy.args[0][0]).to.have.ownProperty('responseHeader'); // JSON data
      expect(spy.args[0][2]).to.have.ownProperty('status'); // jqXHR object
      expect(spy.thisValues[0]).to.have.ownProperty('api', 'request');
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


  describe("API Service - using live server (api.spec.js)", function() {

    this.pending = !window.bbbTest.serverReady;

    it("should retrieve data from server using GET and POST (default)", function(done) {
      var api = new Api({url: '/api/1'}); // url is there, but i want to be explicit
      var q = new ApiQuery({q: 'foo'});
      var spy = sinon.spy();

      var f = function() {
        console.log('received', arguments);
        expect(arguments[0].response.numFound).to.be.defined;
      };

      var defers = [];

      defers.push(api.request(new ApiRequest({target: 'search', query: q, sender: 'woo'}), {done: f, type: "GET"}));
      defers.push(api.request(new ApiRequest({target: 'search', query: q, sender: 'woo'}), {done: f, type: "POST"}));
      defers.push(api.request(new ApiRequest({target: 'search', query: q, sender: 'woo'}), {done: f}))
      defers.push(api.request(new ApiRequest({target: 'search', query: q, sender: 'woo'})).done(f));

      $.when(defers).then(function() {
        done();
      });

    });
  });

});
