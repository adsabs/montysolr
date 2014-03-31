/**
 * Created by rchyla on 3/31/14.
 */

define(['underscore', 'jquery', 'js/components/query_mediator', 'js/components/beehive',
        'js/services/pubsub', 'js/services/api', 'js/components/generic_module',
        'js/components/pubsub_key', 'js/components/api_query', 'js/components/api_request'
        ],
  function(_, $, QueryMediator, BeeHive, PubSub, Api, GenericModule, PubSubKey, ApiQuery, ApiRequest) {

    var beehive;
    beforeEach(function(done) {
      this.server = sinon.fakeServer.create();
      this.server.autoRespond = true;
      this.server.respondWith("/api/1/search",
        [200, { "Content-Type": "application/json" }, validResponse]);

      beehive = new BeeHive();
      beehive.addService('Api', new Api());
      beehive.addService('PubSub', new PubSub());
      done();
    });

    afterEach(function(done) {
      beehive.close();
      beehive = null;
      this.server.restore();
      done();
    });

    describe('Query Mediator (Scaffolding)', function() {

      it("returns API object", function(done) {
        expect(new QueryMediator()).to.be.instanceof(QueryMediator);
        expect(new QueryMediator()).to.be.instanceof(GenericModule);
        done();
      });

      it("has 'activate' function - which does the appropriate setup", function(done) {
        var qm = new QueryMediator();
        var pubsub = beehive.Services.get('PubSub');

        sinon.stub(pubsub, 'subscribe');
        qm.activate(beehive);

        expect(qm.hasBeeHive()).to.be.true;
        expect(qm.getBeeHive()).to.be.equal(beehive);
        expect(qm.pubSubKey).to.be.instanceof(PubSubKey);

        expect(pubsub.subscribe.callCount).to.be.eql(2);
        expect(pubsub.subscribe.args[0].slice(0,2)).to.be.eql([qm.pubSubKey, pubsub.NEW_QUERY]);
        expect(pubsub.subscribe.args[1].slice(0,2)).to.be.eql([qm.pubSubKey, pubsub.NEW_REQUEST]);

        done();
      });

      it("should mediate between modules; passing data back and forth", function(done) {
        var qm = new QueryMediator();
        qm.activate(beehive);

        // install spies into pubsub
        var pubsub = beehive.Services.get('PubSub');
        var pubsubSpy = sinon.spy();
        pubsub.on('all', pubsubSpy);
        var api = beehive.Services.get('Api');
        var apiSpy = sinon.spy();
        api.on('all', apiSpy);


        // fake UI widget that sends signals
        var M = GenericModule.extend({
          activate: function(beehive) {
            this.bee = beehive;
            pubsub = beehive.Services.get('PubSub');
            pubsub.subscribe(pubsub.WANTING_QUERY, _.bind(this._wanting_query, this));
            pubsub.subscribe(pubsub.WANTING_REQUEST, _.bind(this._wanting_request, this));
            pubsub.subscribe(pubsub.NEW_RESPONSE, _.bind(this._getting_response, this));
          },
          userAction: function(q) {
            var pubsub = this.bee.Services.get('PubSub');
            pubsub.publish(pubsub.NEW_QUERY, q);
          },
          _wanting_query: function(q) {
            var pubsub = this.bee.Services.get('PubSub');
            q.add('q', this._mid);
            pubsub.publish(pubsub.NEW_QUERY, q);
            this._q = q;
          },
          _wanting_request: function(q) {
            var pubsub = this.bee.Services.get('PubSub');
            pubsub.publish(pubsub.NEW_REQUEST, new ApiRequest({target: 'search', query:q}));
          },
          _getting_response: function(r) {
            // do something, display data etc
            expect(r.get('responseHeader.status.QTime')).to.equal(88);
            expect(r.getApiQuery()).to.equal(this._q);
          }
        });

        var m1 = new M();
        var m2 = new M();

        //sinon.stub(m1, '_new_request', m1._new_request);
        //sinon.stub(m1, '_getting_response', m1._getting_response);
        //sinon.stub(m2, '_new_request', m2._new_request);
        //sinon.stub(m2, '_getting_response', m2._getting_response);

        // each component will be activated by the app
        var hardenedBee = beehive.getHardenedInstance();
        m1.activate(beehive.getHardenedInstance());
        m2.activate(beehive.getHardenedInstance());

        // create a fake query
        var q = new ApiQuery({'q': '*:*'});

        // pretend user clicked and a new query is fired
        m1.userAction(q);

        /*
         whole chain of events should happen:
         - mediator gets: NEW_QUERY
         - mediator issues: WANTING_REQUEST
           - m1 and m2 respond: NEW_REQUEST
         - mediator gets: NEW_RESPONSE
           - mediator calls api: api.request(apiRequest)
           - api gets data and calls mediator's 'done' callback
             - mediator gets data and issues: SENDING_RESPONSE
               - m1 & m2 receive: apiResponse
        */




        done();
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
