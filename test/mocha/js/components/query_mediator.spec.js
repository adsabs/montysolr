/**
 * Created by rchyla on 3/31/14.
 */

define([
    'underscore',
    'jquery',
    'js/components/query_mediator',
    'js/components/beehive',
    'js/services/pubsub',
    'js/services/api',
    'js/components/generic_module',
    'js/components/pubsub_key',
    'js/components/api_query',
    'js/components/api_request',
    'js/components/pubsub_events',
    'js/components/api_feedback',
    'js/components/json_response'
     ],
  function(
    _,
    $,
    QueryMediator,
    BeeHive,
    PubSub,
    Api,
    GenericModule,
    PubSubKey,
    ApiQuery,
    ApiRequest,
    PubSubEvents,
    ApiFeedback,
    JsonResponse
    ) {

    var debug = false, pubSpy;
    describe('Query Mediator - Controller (query_mediator.spec.js)', function() {


      beforeEach(function(done) {
        this.server = sinon.fakeServer.create();
        this.server.autoRespond = false;  // when true, all sorts of evil things happen

        this.server.respondWith(function(req) {
          req.respond(getNextCode(req.url), { "Content-Type": "application/json" }, validResponse);
        });

        var urlCodes = {};
        var getNextCode = function(url) {
          //url = url.substring(0, url.indexOf('&_')); // remove the caching tmp key
          urlCodes['lastUrl'] = url;
          if (urlCodes[url]) return urlCodes[url];

          if (url.indexOf('/api/1/error') > -1) {
            return 500;
          }
          if (url.indexOf('/api/1/503') > -1) {
            return 503;
          }
          if (url.indexOf('/api/1/401') > -1) {
            return 401;
          }

          return 200;
        };

        this.urlCodes = urlCodes;
        beehive = new BeeHive();
        this.beehive = beehive;
        beehive.addObject("AppStorage", {clearSelectedPapers : sinon.spy()});
        var api = new Api();
        api.expire_in = Date.now() + 100000000;
        sinon.spy(api, 'request');
        beehive.addService('Api', api);
        var ps = new PubSub();
        ps.debug = true;
        pubSpy = sinon.spy();
        ps.on('all', pubSpy);
        beehive.addService('PubSub', ps);
        done();
      });

      afterEach(function(done) {
        this.beehive.destroy();
        this.beehive = null;
        this.server.restore();
        done();
      });

      it("returns QueryMediator object", function(done) {
        expect(new QueryMediator()).to.be.instanceof(QueryMediator);
        expect(new QueryMediator()).to.be.instanceof(GenericModule);
        done();
      });

      it("has 'activate' and 'activateCache' function - which does the appropriate setup", function(done) {
        var qm = new QueryMediator();
        var pubsub = this.beehive.Services.get('PubSub');

        sinon.stub(pubsub, 'subscribe');
        qm.activate(this.beehive, {getPskOfPluginOrWidget: function() {}});

        expect(qm.hasBeeHive()).to.be.true;
        expect(qm.getBeeHive()).to.be.equal(this.beehive);

        expect(pubsub.subscribe.callCount).to.be.eql(4);
        expect(pubsub.subscribe.args[0].slice(1,2)).to.be.eql([pubsub.START_SEARCH]);
        expect(pubsub.subscribe.args[1].slice(1,2)).to.be.eql([pubsub.DELIVERING_REQUEST]);

        expect(qm._cache).to.be.null;
        qm.activateCache();
        expect(qm._cache).to.be.defined;

        done();
      });

      it("should be able to get query ids for bigqueries", function(done){

        var qm = new QueryMediator();
        qm.activate(this.beehive, {getPskOfPluginOrWidget: function() {}});
        var api = this.beehive.Services.get('Api');
        api.request.restore();
        sinon.stub(api, "request", function(request){
         var done = request.toJSON().options.done;
             done({"qid": "fakeQueryID"});
        });

        var q = new ApiQuery({
          q : "*:*",
          __bigquery : ["bib1", "bib2", "bib3"]
        });

        var startSearch = sinon.stub(qm, "startSearchCycle");

        qm.getQueryAndStartSearchCycle(q, "fakeKey");

        setTimeout(function(){
          expect(startSearch.args[0][0].toJSON()).to.eql(
            {
              "q": [
                "*:*"
              ],
              "__qid": [
                "fakeQueryID"
              ]
            }
          );

          done();
        }, 1);

        qm.startSearchCycle.restore();

      });

      it("should be able to get SIMBAD identifiers for queries with 'object:' field", function(done){

        var qm =  createTestQM(this.beehive).qm;

        var publishSpy = sinon.spy(qm.getPubSub(), "publish");

        var q = new ApiQuery({
          q : "bibstem:ApJ object:Foo year:2001"
        });

        var startSearch = sinon.stub(qm, "startSearchCycle");

        qm.getQueryAndStartSearchCycle(q, "fakeKey");

        var r = new ApiRequest({
          target: "objects/query",
          query: new ApiQuery({"query" : "bibstem:ApJ object:Foo year:2001"}),
          "options": {"type": "POST","contentType": "application/json"}
        });

        expect(publishSpy.args[0][0]).to.eql("[PubSub]-Execute-Request");
        expect(publishSpy.args[0][1].get('query').url()).to.eql(r.get('query').url());

        // Check that query mediator object has "original_query" and "original_url" attributes
        expect(qm.original_query).to.eql(["bibstem:ApJ object:Foo year:2001"]);
        expect(qm.original_url).to.eql("q=bibstem%3AApJ%20object%3AFoo%20year%3A2001");

        // Check that response from API gets properly processed
        qm.getPubSub().publish(qm.getPubSub().DELIVERING_RESPONSE, new JsonResponse({"query":"bibstem:ApJ simbid:123456 year:2001"}));


        expect(JSON.stringify(startSearch.args[0])).to.eql(JSON.stringify([
          {
            "q": [
              "bibstem:ApJ simbid:123456 year:2001"
            ]
          },
          "fakeKey"
        ]));

        qm.startSearchCycle.restore();
        done();
      });

      it("should simply add q parameters to a bigquery if the bigquery is not overridden", function(done){

        var qm =  createTestQM(this.beehive).qm;

        var publishSpy = sinon.spy(qm.getPubSub(), "publish");

        //as if previous query was a bigquery
        qm.mostRecentQuery= new ApiQuery({
            "q": [
              "*:*"
            ],
            "__qid": [
              "fakeQueryID"
            ]
          });

        qm.startSearchCycle( new ApiQuery({
          q : 'star'
        }), {getId : function(){ return 1 }});

        expect(publishSpy.args[0][0]).to.eql("[PubSub]-Inviting-Request");

        //it has just augmented the query with a new q parameter
        expect(publishSpy.args[0][1].toJSON()).to.eql(
          {
            "q": [
              "star"
            ],
            "__qid": [
              "fakeQueryID"
            ]
          }
        );
        done();
      });

      it("should mediate between modules; passing data back and forth", function(done) {
        var qm = new QueryMediator({'debug': debug});
        qm.activate(this.beehive, {getPskOfPluginOrWidget: function() {}});

        this.server.autoRespond = true;
        // install spies into pubsub and api
        var pubsub = this.beehive.Services.get('PubSub');
        var pubsubSpy = sinon.spy(function(){if (debug) {console.log('[pubsub:all]', arguments)}});
        pubsub.on('all', pubsubSpy);
        var api = this.beehive.Services.get('Api');
        var apiSpy = sinon.spy(function(){if (debug) {console.log('[api:all]', arguments)}});
        api.on('all', apiSpy);

        // test counter for number of responses received
        var globalCounter = 0;
        var beehive = this.beehive;

        // create fake UI widgets that simulate interaction with mediator
        // they send signals and receive responses
        var M = GenericModule.extend({
          activate: function(beehive) {
            this.bee = beehive;
            pubsub = beehive.Services.get('PubSub');
            pubsub.subscribe(pubsub.INVITING_REQUEST, _.bind(this.sendRequest, this));
            pubsub.subscribe(pubsub.DELIVERING_RESPONSE, _.bind(this.receiveResponse, this));
          },
          userAction: function(q) {
            if (debug)
              console.log('[' + this.mid + '] User Action:', q.url());
            var pubsub = this.bee.Services.get('PubSub');
            pubsub.publish(pubsub.START_SEARCH, q);
          },
          sendRequest: function(q) {
            q = q.clone();
            q.unlock();
            q.add('q', 'field:' + this.mid);
            var pubsub = this.bee.Services.get('PubSub');
            var r = new ApiRequest({target: 'search', query:q});

            if (debug)
              console.log('[' + this.mid + '] Returning Request:', r.url());
            pubsub.publish(pubsub.DELIVERING_REQUEST, r);
            this._q = q;
          },
          receiveResponse: function(r) {
            if (debug)
              console.log('[' + this.mid + '] Receiving Response:', JSON.stringify(r.toJSON()));

            // do something with response...
            expect(r.get('responseHeader.QTime')).to.equal(88);
            // check the query was the same
            expect(r.getApiQuery().url()).to.equal(this._q.url());
            // actually, even the same instance
            expect(r.getApiQuery()).to.equal(this._q);

            globalCounter += 1;
            if (globalCounter > 1) {
              if (debug)
                console.log('test: closing');
              checkIt();
              done();
            }
          }
        });

        // create components
        var m1 = new M();
        var m2 = new M();

        // install spies into components
        sinon.spy(m1, 'sendRequest', m1.sendRequest);
        sinon.spy(m1, 'receiveResponse', m1.receiveResponse);
        sinon.spy(m2, 'sendRequest', m2.sendRequest);
        sinon.spy(m2, 'receiveResponse', m2.receiveResponse);

        // because of the spies, we must activate only now...
        m1.activate(this.beehive.getHardenedInstance());
        m2.activate(this.beehive.getHardenedInstance());

        // pretend user clicked and a new query is fired
        var q = new ApiQuery({'q': '*:*'});
        m1.userAction(q);

        /*
         whole chain of events should happen:
         - user clicked: START_SEARCH
         - mediator issues: INVITING_REQUEST
           - m1 and m2 respond with: DELIVERING_REQUEST
         - mediator collects requests
           - mediator tests the first query
           - mediator calls api: api.request(apiRequest)
             - api gets data and sends them back to mediator
             - mediator wraps it into ApiResponse and issues: DELIVERING_RESPONSE
                - m1 & m2 receive the apiResponse (each for their own request only)
        */

        // must be called after we have collected everything
        var checkIt = function() {
          // each was called once
          expect(m1.sendRequest.callCount).to.be.equal(1);
          expect(m2.sendRequest.callCount).to.be.equal(1);

          // they received a clone of the query (but not the original)
          expect(m1.sendRequest.args[0][0].url()).to.equal(q.url());
          expect(m2.sendRequest.args[0][0].url()).to.equal(q.url());
          expect(m1.sendRequest.args[0][0]).to.not.equal(q);
          expect(m2.sendRequest.args[0][0]).to.not.equal(q);
          // and it was 'locked'
          expect(m1.sendRequest.args[0][0].isLocked()).to.be.true;
          expect(m2.sendRequest.args[0][0].isLocked()).to.be.true;

          // each UI must receive only its own data
          expect(m1.receiveResponse.callCount).to.be.equal(1);
          expect(m2.receiveResponse.callCount).to.be.equal(1);
        };

        if (debug)
          console.log('test: reached end');
      });

      it("has _getCacheKey function", function(done) {
        var qm = new QueryMediator();
        var req = new ApiRequest({target: 'search', query:new ApiQuery({'q': 'pluto'})});

        req.get('query').set('__x', 'foo');
        expect(req.url()).to.be.equal('search?__x=foo&q=pluto');
        var key = qm._getCacheKey(req);
        expect(req.url()).to.be.equal('search?__x=foo&q=pluto');
        expect(key).to.be.equal('search?q=pluto');
        done();
      });

      it("has START_SEARCH signal", function(done) {
        var x = createTestQM(this.beehive);
        var qm = x.qm;
        qm.activateCache();

        sinon.spy(qm, 'startExecutingQueries');

        var pubsub = x.pubsub;
        var key = pubsub.getPubSubKey();

        // it needs requests to start working
        pubsub.subscribe(key, pubsub.INVITING_REQUEST, function() {
          pubsub.publish(x.key1, pubsub.DELIVERING_REQUEST, x.req1);
        });

        qm._cache.put('foo', 'bar');

        expect(this.beehive.getObject("AppStorage").clearSelectedPapers.callCount).to.eql(0);

        qm.startSearchCycle(new ApiQuery({'q': 'foo'}), key);

        expect(qm._cache.size).to.be.eql(0);
        expect(qm.reset.callCount).to.eql(1);
        expect(qm.__searchCycle.initiator).to.be.eql(key.getId());
        expect(qm.__searchCycle.waiting[key.getId()]).to.be.defined;
        expect(pubSpy.lastCall.args[0]).to.be.eql(PubSubEvents.INVITING_REQUEST);

        var test = this;
        expect(qm.monitorExecution.called).to.be.false;
        setTimeout(function() {
          expect(qm.startExecutingQueries.called).to.be.true;
          expect(test.beehive.getObject("AppStorage").clearSelectedPapers.callCount).to.eql(2);
          expect(qm.monitorExecution.called).to.be.true;
          done();
        }, 50);


        //if the queries match, the mediator checks to see if the query came from the search widget
        qm.setApp({
          getPskOfPluginOrWidget: function(){},
          getPluginOrWidgetName : function(){return false},
          hasService: function() {return true;},
          getService : sinon.spy(function(){
            return {
              navigate: sinon.spy()
            }})});
        qm.startSearchCycle(new ApiQuery({'q': 'foo'}), key);

        //same query will get executed
        expect(qm.reset.callCount).to.eql(2);

      });



      it("responds to EXECUTE_REQUEST signal", function(done) {
        var x = createTestQM(this.beehive);
        var qm = x.qm, key1 = x.key1, key2 = x.key2, req1 = x.req1, req2 = x.req2;
        qm.activateCache();

        qm._makeWidgetSpin = sinon.spy();

        var pubsub = x.pubsub;
        var key = pubsub.getPubSubKey();

        qm._cache.put('foo', 'bar');
        qm.__searchCycle.waiting['foo'] = 'foo';

        expect(function() {pubsub.publish(key, PubSubEvents.EXECUTE_REQUEST, x.q1)}).to.throw.Error;
        pubsub.publish(key, PubSubEvents.EXECUTE_REQUEST, x.req1);

        this.server.respond();

        setTimeout(function() {

          expect(qm.reset.called).to.be.false;
          expect(qm.executeRequest.called).to.be.true;
          expect(qm._executeRequest.called).to.be.true;
          expect(qm.onApiResponse.called).to.be.true;
          expect(qm._cache.size).to.be.eql(2);
          expect(qm.__searchCycle.waiting['foo']).to.be.eql('foo');
          expect(pubSpy.lastCall.args[0].indexOf(PubSubEvents.DELIVERING_RESPONSE)>-1).to.be.true;

          done();
        }, 5);

      });

      it("responds to GET_QTREE signal", function(done) {
        var x = createTestQM(this.beehive);
        var qm = x.qm, key1 = x.key1, key2 = x.key2, req1 = x.req1, req2 = x.req2;
        qm.activateCache();

        var pubsub = x.pubsub;
        var key = pubsub.getPubSubKey();

        pubsub.publish(key, PubSubEvents.GET_QTREE, x.q1);
        this.server.respond();

        setTimeout(function() {

          expect(qm.reset.called).to.be.false;
          expect(qm.executeRequest.called).to.be.false;
          expect(qm._executeRequest.called).to.be.true;
          expect(qm.onApiResponse.called).to.be.true;
          expect(qm._cache.size).to.be.eql(1);
          expect(pubSpy.lastCall.args[0].indexOf(PubSubEvents.DELIVERING_RESPONSE)>-1).to.be.true;

          done();
        }, 5);

      });

      var createTestQM = function(beehive) {
        var qm = new QueryMediator({
          shortDelayInMs: 2,
          recoveryDelayInMs: 3,
          longDelayInMs: 0
        });

        var pubsub = beehive.Services.get('PubSub');
        var api = beehive.Services.get('Api');
        var key1 = pubsub.getPubSubKey();
        var key2 = pubsub.getPubSubKey();
        var q1 = new ApiQuery({'q': 'foo'});
        var q2 = new ApiQuery({'q': 'bar'});
        var req1 = new ApiRequest({target: 'search', query: q1});
        var req2 = new ApiRequest({target: 'search', query: q2});

        sinon.spy(qm, 'reset');
        sinon.spy(qm, 'executeRequest');
        sinon.spy(qm, '_executeRequest');
        sinon.spy(qm, 'monitorExecution');
        sinon.spy(qm, 'onApiResponse');
        sinon.spy(qm, 'onApiRequestFailure');
        sinon.spy(qm, 'tryToRecover');
        sinon.spy(qm, 'getQTree');

        qm.activate(beehive, {getPskOfPluginOrWidget: function() {}, hasService: function(){}});
        return {qm: qm, key1: key1, key2:key2, req1: req1, req2:req2, q1:q1, q2:q2,
          pubsub:pubsub, api:api}
      };

      it("executes queries", function(done) {

        var x = createTestQM(this.beehive);
        var qm = x.qm, key1 = x.key1, key2 = x.key2, req1 = x.req1, req2 = x.req2;

        qm.__searchCycle.running = true;
        qm.startExecutingQueries();
        expect(qm._executeRequest.called).to.be.false;

        qm.__searchCycle.running = false;
        qm.startExecutingQueries();
        expect(qm._executeRequest.called).to.be.false;


        qm.__searchCycle.waiting[key1.getId()] = {key: key1, request: req1};
        qm.__searchCycle.waiting[key2.getId()] = {key: key2, request: req2};
        qm.startExecutingQueries();
        expect(qm.__searchCycle.running).to.be.true;
        expect(qm.__searchCycle.inprogress[key1.getId()]).to.be.defined;
        expect(qm._executeRequest.callCount).to.be.eql(1);
        expect(qm.onApiResponse.callCount).to.be.eql(0);

        this.server.respond();
        this.server.respond();
        setTimeout(function() {
          expect(pubSpy.firstCall.args[0]).to.be.eql(PubSubEvents.DELIVERING_RESPONSE + key1.getId());
          expect(pubSpy.secondCall.args[0]).to.be.eql(PubSubEvents.FEEDBACK);
          expect(pubSpy.secondCall.args[1].code).to.be.eql(ApiFeedback.CODES.SEARCH_CYCLE_STARTED);

          expect(qm._executeRequest.callCount).to.be.eql(2);
          expect(qm.onApiResponse.callCount).to.be.eql(2);

          done();
        }, 50);

      });

      it("executes queries (first, the one we want)", function(done) {

        var x = createTestQM(this.beehive);
        var qm = x.qm, key1 = x.key1, key2 = x.key2, req1 = x.req1, req2 = x.req2;

        beehive.addObject('DynamicConfig', {pskToExecuteFirst: key2.getId()});
        qm.__searchCycle.waiting[key1.getId()] = {key: key1, request: req1};
        qm.__searchCycle.waiting[key2.getId()] = {key: key2, request: req2};
        qm.startExecutingQueries();
        expect(qm.__searchCycle.running).to.be.true;
        expect(qm.__searchCycle.inprogress[key2.getId()]).to.be.defined;

        this.server.respond();
        this.server.respond();
        done();
      });

      it("when error happens on the first query, it stops execution and triggers Feedback", function(done) {

        var x = createTestQM(this.beehive);
        var qm = x.qm, key1 = x.key1, key2 = x.key2, req1 = x.req1, req2 = x.req2;


        req1.set('target', '401');
        beehive.addObject('DynamicConfig', {pskToExecuteFirst: key1.getId()});
        qm.__searchCycle.waiting[key1.getId()] = {key: key1, request: req1};
        qm.__searchCycle.waiting[key2.getId()] = {key: key2, request: req2};

        qm.startExecutingQueries();
        expect(qm._executeRequest.callCount).to.be.eql(1);
        expect(qm.onApiResponse.callCount).to.be.eql(0);

        this.server.respond();
        this.server.respond();

        setTimeout(function() {
          expect(pubSpy.firstCall.args[1].error.status).to.be.eql(401);
          expect(pubSpy.lastCall.args[1].code).to.be.eql(ApiFeedback.CODES.SEARCH_CYCLE_FAILED_TO_START);

          expect(qm._executeRequest.callCount).to.be.eql(1);
          expect(qm.onApiRequestFailure.callCount).to.be.eql(1);

          done();
        }, 50);

      });

      it("uses cache to serve identical requests", function(done) {

        var x = createTestQM(this.beehive);
        var qm = x.qm, key1 = x.key1, key2 = x.key2, req1 = x.req1, req2 = x.req2;
        qm.activateCache();

        var rk = qm._getCacheKey(req1);

        qm._executeRequest(req1, key1);
        expect(qm._cache.size).to.be.eql(1);
        expect(qm._cache.getSync(rk).state()).to.be.eql('pending');

        this.server.respond();
        expect(qm.onApiResponse.callCount).to.be.eql(1);

        qm._executeRequest(req1, key1);
        expect(qm._cache.size).to.be.eql(1);
        expect(x.api.request.callCount).to.be.eql(1);
        expect(qm.onApiResponse.callCount).to.be.eql(2);

        // when requests arrive too early, they will be queued
        qm.onApiResponse.reset();
        x.api.request.reset();

        qm._cache.invalidateAll();
        qm._executeRequest(x.req1, x.key1);
        qm._executeRequest(x.req1, x.key2);

        expect(qm._cache.size).to.be.eql(1);
        expect(qm._cache.getSync(rk).state()).to.be.eql('pending');

        this.server.respond();

        expect(qm.onApiResponse.callCount).to.be.eql(2);
        expect(x.api.request.callCount).to.be.eql(1);

        // errors are cached (after certain amount of retries)
        qm._cache.invalidateAll();
        req1.set('target', 'error');
        qm.maxRetries = 2;
        rk = qm._getCacheKey(req1);
        qm._executeRequest(req1, key1);
        this.server.respond();
        expect(qm.failedRequestsCache.getSync(rk)).to.be.eql(1);

        qm._executeRequest(req1, key1);
        expect(qm._cache.size).to.be.eql(1);
        this.server.respond();
        expect(qm.failedRequestsCache.getSync(rk)).to.be.eql(2);
        expect(qm._cache.size).to.be.eql(0);

        // further requests are ignored
        qm._executeRequest(req1, key1);
        expect(qm._cache.size).to.be.eql(0);

        done();
      });

      it("sends CYCLE signals when job starts and is done", function(done) {

        var x = createTestQM(this.beehive);
        var qm = x.qm, key1 = x.key1, key2 = x.key2, req1 = x.req1, req2 = x.req2;

        this.beehive.addObject('DynamicConfig', {pskToExecuteFirst: key2.getId()});
        qm.__searchCycle.waiting[key1.getId()] = {key: key1, request: req1};
        qm.__searchCycle.waiting[key2.getId()] = {key: key2, request: req2};
        qm.startExecutingQueries();
        expect(qm.__searchCycle.running).to.be.true;
        expect(qm.__searchCycle.inprogress[key2.getId()]).to.be.defined;

        this.server.respond();
        expect(qm.__searchCycle.done[key2.getId()]).to.be.defined;
        expect(qm.__searchCycle.inprogress[key1.getId()]).to.be.defined;
        expect(_.keys(qm.__searchCycle.waiting).length).to.be.eql(0);

        expect(pubSpy.lastCall.args[1]).to.be.instanceOf(ApiFeedback);
        expect(pubSpy.lastCall.args[1].code).to.be.eql(ApiFeedback.CODES.SEARCH_CYCLE_STARTED);
        var self = this;
        setTimeout(function() {
          self.server.respond();
          expect(pubSpy.lastCall.args[1]).to.be.instanceOf(ApiFeedback);
          expect(pubSpy.lastCall.args[1].code).to.be.eql(ApiFeedback.CODES.SEARCH_CYCLE_FINISHED);
          done();
        }, 50);

      });

      it("knows to recover from certain errors", function(done) {
        done();
        return;

        var x = createTestQM(this.beehive);
        var qm = x.qm, key1 = x.key1, key2 = x.key2, req1 = x.req1, req2 = x.req2;
        var rk = qm._getCacheKey(req1);

        req1.set('target', '503');
        qm._executeRequest(req1, key1);

        this.server.respond();
        expect(qm.onApiRequestFailure.callCount).to.be.eql(1);
        expect(qm.tryToRecover.callCount).to.be.eql(1);

        // now pretend that server 'recovered'
        this.urlCodes[this.urlCodes.lastUrl] = 200;

        var self = this;
        setTimeout(function() {
          self.server.respond();
          expect(qm.onApiResponse.callCount).to.be.eql(1);
          done();
        }, 5);


      });

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
