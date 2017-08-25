
define([
  'jquery',
  'underscore',
  'js/services/api',
  'js/components/api_request',
  'js/components/api_query',
  'js/components/api_response',
  'js/wraps/discovery_mediator',
  'js/components/feedback_mediator',
  '../widgets/test_json/test1',
  'js/bugutils/minimal_pubsub',
  'js/mixins/discovery_bootstrap',
  'js/modules/orcid/module',
  'js/components/user',
  'js/components/api_feedback'
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
  DiscoveryBootstrapMixin,
  OrcidModule,
  User,
  ApiFeedback
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
      this.server.respondWith(/.*orcid.*/,
          [401, { "Content-Type": "application/json" }, JSON.stringify({})]);
    });

    afterEach(function() {
      minsub.destroy();
      this.server.restore();
    });


    it('should return API object', function (done) {
      expect(new DiscoveryMediator()).to.be.instanceof(FeedbackMediator);
      done();
    });

    var _getM = function() {
      var mediator = new DiscoveryMediator();
      var app = {
        getWidget: sinon.stub().returns({}),
        getPskOfPluginOrWidget: sinon.stub().returns(null)
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

    it("on SEARCH_CYCLE_STARTED", function(done) {
      var x = _getM();
      var m = x.m;
      x.app.getObject = sinon.stub().returns({setCurrentQuery: sinon.spy()});
      x.app.getService = sinon.stub().returns({navigate: sinon.spy()});
      x.dm.getAlerter = sinon.stub().returns({alert: sinon.spy()});

      // it resets itself on new search
      minsub.publish(minsub.FEEDBACK, minsub.createFeedback({
        code: minsub.T.FEEDBACK.CODES.SEARCH_CYCLE_STARTED,
        cycle: {}
      }));
      expect(x.dm.getAlerter.called).to.be.true;

      done();
    });


    it("catches errors (wrong token)", function(done) {
      var x = _getM();
      minsub.subscribeOnce(minsub.INVITING_REQUEST, function() {
        minsub.publish(minsub.DELIVERING_REQUEST, x.reqUnAuthorized);
      });
      minsub.publish(minsub.START_SEARCH, x.qError);


      minsub.subscribeOnce(minsub.START_SEARCH, function() {
        done()}); // if this fires, query was resurrected

      x.app.getPluginOrWidgetName = sinon.stub().returns(null);
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

    it("resets ORCID settings when the ORCID API returns 401, which is passed to the feedback service", function () {

      // Stub the behaviour of the application so that we do not have to modify the discovery_mediator.js code
      var app = {
        getWidget: sinon.stub().returns({}),
        getPluginOrWidgetName: sinon.stub().returns(true),
        getService: function(service) {return minsub.beehive.getService(service)},
        getObject: function(_object) {return minsub.beehive.getObject(_object)}
      };

      // Intialise the mediator
      var dm = new DiscoveryMediator();
      dm.activate(minsub.beehive, app);
      dm.getAlerter = sinon.stub().returns({alert: sinon.spy()});

      // Initialise the settings for OrcidAPI
      minsub.beehive.addObject('DynamicConfig', {
        orcidClientId: 'APP-P5ANJTQRRTMA6GXZ',
        orcidApiEndpoint: 'https://api.orcid.org',
        orcidRedirectUrlBase: 'http://localhost:8000',
        orcidLoginEndpoint: 'https://api.orcid.org/oauth/authorize'
      });
      var oModule = new OrcidModule();
      oModule.activate(minsub.beehive);
      var orcidApi = minsub.beehive.getService('OrcidApi');

      // Lets watch the signOut method and see if it is called
      sinon.spy(orcidApi, 'signOut');

      // Initialise the user and to the beehive, and activate
      var user = new User();
      minsub.beehive.addObject("User", user);
      user.activate(minsub.beehive);

      // Set some defaults that mimicks that the user is logged in to Orcid
      user.setOrcidMode(1);
      orcidApi.authData = {};

      // Lets watch the setOrcidMode method and see if it is called
      sinon.spy(user, 'setOrcidMode');

      // Pretend that an API request causes there to be a feedback injected into the PubSub queue
      // First we stub the request....
      var request = minsub.createRequest({
        'target': 'www.adsapi.com/v1/orcid/undefined/orcid-works',
        'query': minsub.createQuery({'q': 'foo:bar'})
      });
      // .... then the feedback containing the error messages ....
      var feedback = new ApiFeedback({
        request: request,
        code: ApiFeedback.CODES.API_REQUEST_ERROR,
        msg: 'foo',
        errorThrown: '',
        error: {status: 401},
        psk: minsub.beehive.getService('PubSub').getPubSubKey()
      });

      //.... then finally push the message to the PubSub queue
      minsub.publish(minsub.FEEDBACK, feedback);

      // The behaviour should be that the User is logged out of Orcid and things are reset
      expect(orcidApi.signOut.called).to.eql(true);
      expect(user.setOrcidMode.called).to.eql(true);
      expect(orcidApi.authData).to.eql(null);
      expect(user.isOrcidModeOn()).to.eql(0);
    });

  })
});