define(['js/components/application', 'module'], function(Application, module) {
  describe("Application Scaffolding (application.spec.js)", function () {

    var config = null;
    beforeEach(function(done) {
      config = {
        core: {
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
          Test: 'js/components/multi_params'
        }
      };
      done();
    });


    it("should create application object", function(done) {
      expect(new Application()).to.be.instanceof(Application);
      done();
    });

    it("loads components", function(done) {
      var app = new Application();
      var defer = app.loadModules(config);
      expect(defer.state()).to.be.equal("pending");

      defer.done(function() {
        expect(app.getBeeHive()).to.be.defined;
        var beehive = app.getBeeHive();

        expect(beehive.getService('Api').request).to.be.defined;
        expect(beehive.getService('PubSub').publish).to.be.defined;
        expect(beehive.getObject('User')).to.be.defined;
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
        expect(app.getAllWidgets().length).to.be.equal(2);
        expect(app.getAllPlugins().length).to.be.equal(1);

        expect(app.isActivated()).to.be.equal(false);
        app.activate();
        expect(app.isActivated()).to.be.equal(true);

        var w1 = app.getWidget('ApiResponse');
        var w2 = app.getWidget('ApiResponse2');

        expect(app.getPluginOrWidgetByPubSubKey(w1.pubsub.getCurrentPubSubKey().getId())).to.be.eql(w1);
        expect(app.getPluginOrWidgetByPubSubKey(w2.pubsub.getCurrentPubSubKey().getId())).to.be.eql(w2);

        expect(app.getPluginOrWidgetByPubSubKey('foo')).to.be.undefined;
        delete app.__barbarianRegistry[w1.pubsub.getPubSubKey()];
        expect(function() {app.getPluginOrWidgetByPubSubKey('foo')}).to.throw.Error;

        done();
      });
    });

    it("has triggerMethod", function(done) {
      var app = new Application();
      var defer = app.loadModules(config);

      defer.done(function() {
        var counter = 0;
        var args = [];
        _.each(app.getAllWidgets(), function(w) {
          w[1].foox = function(options) {
            counter += 1;
            args.push(options);
          }
        });

        expect(counter).to.be.equal(0);
        app.triggerMethodOnAll('foox', 'foo');
        expect(counter).to.be.equal(2);
        expect(args).to.be.eql(['foo', 'foo']);

        done();
      });
    });

  });
});