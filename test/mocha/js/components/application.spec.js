define([
  'js/components/application',
  'module'
], function(
  Application,
  module) {
  describe("Application Scaffolding (application.spec.js)", function () {

    var config = null;
    beforeEach(function(done) {
      config = {
        core: {
          controllers: {
            FeedbackMediator: 'js/wraps/discovery_mediator'
          },
          services: {
            'Api': 'js/services/api',
            'PubSub': 'js/services/pubsub'
          },
          objects: {
            User: 'js/components/user'
          },
          modules: {
            QM: 'js/components/query_mediator'
          }
        },
        widgets: {
          ApiResponse: 'js/widgets/api_response/widget',
          ApiResponse2: 'js/widgets/api_response/widget'
        },
        plugins: {
          Test: 'js/widgets/api_response/widget'
        }
      };
      done();
    });


    it("should create application object", function(done) {
      expect(new Application()).to.be.instanceof(Application);
      var app = new Application();
      expect(app.getBeeHive()).to.be.defined;
      var beehive = app.getBeeHive();
      sinon.spy(beehive, 'destroy');
      app.destroy();
      expect(beehive.destroy.called).to.be.true;
      done();
    });

    it("loads components", function(done) {
      var app = new Application();
      var defer = app.loadModules(config);
      expect(defer.state()).to.be.equal("pending");

      defer.done(function() {
        expect(app.getBeeHive()).to.be.defined;
        var beehive = app.getBeeHive();

        expect(app.hasService('Api')).to.be.true;
        expect(app.hasObject('User')).to.be.true;
        expect(app.hasController('FeedbackMediator')).to.be.true;

        expect(app.getService('Api').request).to.be.defined;
        expect(app.getService('PubSub').publish).to.be.defined;
        expect(app.getObject('User')).to.be.defined;
        expect(app.getController('FeedbackMediator')).to.be.defined;

        expect(app.getModule('QM')).to.be.defined;

        expect(app.getWidget('ApiResponse')).to.be.defined;
        expect(app.getWidget('ApiResponse')).to.not.be.equal(app.getWidget('ApiResponse2'));
        expect(app.getPlugin('Test')).to.be.defined;

        done();
      });

    });

    it.skip("handles errors of loading components", function(done) {
      var app = new Application();
      config.core.services.Api = 'js/components/nonexisting';

      var promise = app.loadModules(config);
      expect(promise.state()).to.be.equal("pending");

      promise.fail(function() {
        expect(app.getBeeHive()).to.be.defined;
        var beehive = app.getBeeHive();

        expect(beehive.getService('Api')).to.be.undefined;
        expect(beehive.getService('PubSub')).to.be.undefined;
        expect(beehive.getObject('User')).to.be.undefined;

        expect(app.getWidget('ApiResponse')).to.be.undefined;
        expect(app.getPlugin('Test')).to.be.defined;
        done();
      });
    });

    it("provides methods to retrieve widgets/plugins", function(done) {
      var app = new Application();
      var defer = app.loadModules(config);

      defer.done(function() {
        app._getWidget('ApiResponse');
        app._getWidget('ApiResponse2');
        app._getPlugin('Test');

        app.getAllWidgets().done(function(w) {
          expect(w.length).to.be.eql(2);
        });
        app.getAllPlugins().done(function(w) {
          expect(w.length).to.be.eql(1);
        });

        expect(app.isActivated()).to.be.equal(false);
        app.activate();
        expect(app.isActivated()).to.be.equal(true);

        app.getWidget('ApiResponse', 'ApiResponse2').done(
          function(w) {
            var w1 = w.ApiResponse;
            var w2 = w.ApiResponse2;

            expect(app.getPluginOrWidgetByPubSubKey(w1.getPubSub().getCurrentPubSubKey().getId())).to.be.eql(w1);
            expect(app.getPluginOrWidgetByPubSubKey(w2.getPubSub().getCurrentPubSubKey().getId())).to.be.eql(w2);

            expect(app.getPluginOrWidgetByPubSubKey('foo')).to.be.undefined;
            delete app.__barbarianRegistry[w1.getPubSub().getCurrentPubSubKey()];
            expect(function() {app.getPluginOrWidgetByPubSubKey('foo')}).to.throw.Error;

            done();
        });
      });
    });

    it("has triggerMethod", function(done) {
      var app = new Application();
      var defer = app.loadModules(config);

      defer.done(function() {
        var counter = 0;
        var args = [];
        _.each(app.getAllControllers(), function(w) {
          w[1].foox = function(options) {
            counter += 1;
            args.push(options);
          }
        });

        app._getWidget('ApiResponse').foox = function(options) {
          counter += 1;
          args.push(options);
        };

        expect(counter).to.be.equal(0);
        app.triggerMethodOnAll('foox', 'foo');
        expect(counter).to.be.equal(2);
        expect(args).to.be.eql(['foo', 'foo']);

        done();
      });
    });

    it("has getApiAccess", function(done) {
      var app = new Application();
      var spy = sinon.spy();
      var api = {request: function(apiRequest, options) {
        expect(apiRequest.url()).to.contain('/accounts/bootstrap');
        options.done(
          {
            "username": "user@gmail.com",
            "scopes": ["user"],
            "access_token": "ap0MkGjroS1zzijLlk9fV2UKXdRDo5nzUueTNaog",
            "token_type": "Bearer",
            "csrf": "1428969367##8460e442cb2984810486bf959048a05d7e7d9e78",
            "expire_in": "2500-01-01T00:00:00",
            "refresh_token": "KKGJp56UlpKgfHUuNNNJvj3XgepWlkTfKKtqmpkM"
          });
      }};
      app.getBeeHive().addService('Api', api);
      var fakeUser = {setUser : sinon.spy()};
      app.getBeeHive().addObject("User", fakeUser);
      var spy = sinon.spy(app, 'onBootstrap');
      app.getApiAccess({reconnect: true})
        .done(function() {
          expect(spy.called).to.eql(true);
          expect(api.access_token).to.eql('Bearer:ap0MkGjroS1zzijLlk9fV2UKXdRDo5nzUueTNaog');
          //every time onbootstrap is called, update the user object with username/undefined to show that user is anonymous
          expect(fakeUser.setUser.args[0]).to.eql(["user@gmail.com"]);
          done();
        })
    });

    it("uses reference counters to get rid of objects", function() {
      var app = new Application();
      var defer = app.loadModules(config);

      defer.done(function() {
        var counter = 0;
        app.getWidget('ApiResponse')
          .done(function(widget) {
            expect(widget.getBeeHive).to.be.defined;
            expect(widget.getPubSub).to.be.defined;
            expect(app.__barbarianInstances['widget:' + 'ApiResponse'].counter).to.eql(1);
          });


        setTimeout(function() {
          expect(app.__barbarianInstances['widget:' + 'ApiResponse']).to.be.undefined;
          done();
        }, 10)

      });
    });

  });
});