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

        expect(pubsub.subscribe.callCount).to.be.eql(3);
        expect(pubsub.subscribe.args[0].slice(0,2)).to.be.eql([qm.pubSubKey, pubsub.NEW_QUERY]);
        expect(pubsub.subscribe.args[1].slice(0,2)).to.be.eql([qm.pubSubKey, pubsub.UPDATED_QUERY]);
        expect(pubsub.subscribe.args[2].slice(0,2)).to.be.eql([qm.pubSubKey, pubsub.NEW_REQUEST]);

        done();
      });

      it("should mediate between modules; passing data back and forth", function(done) {
        var qm = new QueryMediator();
        qm.activate(beehive);

        // install spies into pubsub
        var pubsub = beehive.Services.get('PubSub');
        var pubsubSpy = sinon.spy(function(){console.log('pubsub event:', arguments[0])});
        pubsub.on('all', pubsubSpy);
        var api = beehive.Services.get('Api');
        var apiSpy = sinon.spy(function(){console.log('api event:', arguments)});
        api.on('all', apiSpy);

        var globalCounter = 0;

        // fake UI widget that sends signals
        var M = GenericModule.extend({
          activate: function(beehive) {
            this.bee = beehive;
            pubsub = beehive.Services.get('PubSub');
            pubsub.subscribe(pubsub.WANTING_QUERY, _.bind(this.return_modified_query, this));
            pubsub.subscribe(pubsub.WANTING_REQUEST, _.bind(this.return_request, this));
            pubsub.subscribe(pubsub.NEW_RESPONSE, _.bind(this.receive_response, this));
          },
          userAction: function(q) {
            console.log('User Action Worker:', this.mid, q.url());
            var pubsub = this.bee.Services.get('PubSub');
            pubsub.publish(pubsub.NEW_QUERY, q);
          },
          return_modified_query: function(q) {
            var pubsub = this.bee.Services.get('PubSub');
            q.add('q', 'field:' + this.mid);
            console.log('Returning query Worker:', this.mid, q.url());
            pubsub.publish(pubsub.UPDATED_QUERY, q);
            this._q = q;
          },
          return_request: function(q) {
            var pubsub = this.bee.Services.get('PubSub');
            var r = new ApiRequest({target: 'search', query:q});
            console.log('Returning Request Worker:', this.mid, r.url());
            pubsub.publish(pubsub.NEW_REQUEST, r);
          },
          receive_response: function(r) {
            console.log('Receiving Response Worker:', this.mid, r.toJSON());
            // do something, display data etc
            expect(r.get('responseHeader.QTime')).to.equal(88);
            // TODO: check the query
            globalCounter += 1;
            console.log('globalCounter', globalCounter);
            if (globalCounter > 1) {
              console.log('closing');
              done();
            }
          }
        });

        var m1 = new M();
        var m2 = new M();

/*
        sinon.spy(m1, '_wanting_query', m1._new_request);
        sinon.spy(m1, '_wanting_request', m1._wanting_request);
        sinon.spy(m1, '_getting_response', m1._getting_response);

        sinon.spy(m2, '_wanting_query', m2._new_request);
        sinon.spy(m2, '_wanting_request', m2._wanting_request);
        sinon.spy(m2, '_getting_response', m2._getting_response);
*/
        // each component will be activated by the app
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



        console.log('done');
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
