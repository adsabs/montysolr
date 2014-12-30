
define([
  'jquery',
  'underscore',
  'js/services/api',
  'js/components/api_request',
  'js/components/api_query',
  'js/components/api_response',
  'js/wraps/discovery_mediator',
  'js/components/feedback_mediator',
  '../../widgets/test_json/test1',
  'js/bugutils/minimal_pubsub',
  'js/mixins/discovery_bootstrap'
], function(
  $,
  _,
  Api,
  ApiRequest,
  ApiQuery,
  ApiResponse,
  DiscoveryMediator,
  FeedbackMediator,
  Test1,
  MinimalPubSub,
  DiscoveryBootstrapMixin
  ) {

  describe("Discovery mediator (discovery_mediator.spec.js)", function() {

    var minsub;
    beforeEach(function() {
      var api = new Api();
      minsub = new MinimalPubSub({verbose: true, Api: api});

      this.server = sinon.fakeServer.create();
      this.server.autoRespond = false;
      this.server.respondWith(/\/api\/1\/search.*/,
        [200, { "Content-Type": "application/json" }, JSON.stringify(Test1())]);
      this.server.respondWith(/\/api\/1\/parseerror.*/,
        [200, { "Content-Type": "application/json" }, JSON.stringify(Test1()).substring(2)]);
      this.server.respondWith(/\/api\/1\/error.*/,
        [500, { "Content-Type": "application/json" }, JSON.stringify(Test1()).substring(2)]);
      this.server.respondWith(/.*unauthorized.*/,
        [401, { "Content-Type": "application/json" }, JSON.stringify({})]);

    });

    afterEach(function() {
      minsub.close();
      this.server.restore();
    });


    it('should return API object', function (done) {
      expect(new DiscoveryMediator()).to.be.instanceof(FeedbackMediator);
      done();
    });

    var _getM = function() {
      var mediator = new DiscoveryMediator();
      var app = {
        getWidget: sinon.stub().returns({})
      };
      mediator.activate(minsub.beehive, app);

      var qParseErr =  minsub.createQuery({'q': 'parseerror'});
      var qError = minsub.createQuery({'q': 'eerror'});
      var q = minsub.createQuery({'q': 'search'});
      var qUnAuthorized = minsub.createQuery({'q': 'unauthorized'});

      return {
        app: app,
        dm: mediator,
        reqParseErr: minsub.createRequest({target: 'parseerror', query: qParseErr, sender: 'woo'}),
        reqErr: minsub.createRequest({target: 'eerror', query: qError, sender: 'woo'}),
        req: minsub.createRequest({target: 'search', query:q, sender: 'woo'}),
        reqUnAuthorized: minsub.createRequest({target: 'search', query:qUnAuthorized, sender: 'woo'}),
        qParseErr: qParseErr,
        qError: qError,
        q: q
      };
    };

    it("catches errors (wrong token)", function(done) {
      var x = _getM();
      minsub.subscribeOnce(minsub.INVITING_REQUEST, function() {
        minsub.publish(minsub.DELIVERING_REQUEST, x.reqUnAuthorized);
      });
      minsub.publish(minsub.START_SEARCH, x.qError);


      minsub.subscribeOnce(minsub.START_SEARCH, function() {
        done()}); // if this fires, query was resurrected

      x.app = _.extend(x.app, {
        getApiAccess: function() {
          var defer = $.Deferred();
          defer.resolve({});
          return defer;
        },
        getController: function(name) {
          if (name == 'QueryMediator')
            return {
              resetFailures: function() {}
            }
        },
        getService: function(name) {
          if (name == 'Api') {
            return {
              request: function (apiReq, options) {
                var d = $.Deferred();
                d.done(options.done);
                d.fail(options.fail);
                d.resolve();
                return d;
              }
            }
          }
        }
        });
      this.server.respond();

    });

  })
});