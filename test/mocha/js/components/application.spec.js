define(['js/components/application', 'module'], function(Application, module) {
  describe("Application (Scaffolding)", function () {

    var config = {
      core: {
        services: {
          'Api': 'js/services/api',
          'PubSub': 'js/services/pubsub'
        },
        objects: {
          User: 'js/components/user'
        }
      },
      regions: {
        header: {},
        middle: {},
        footer: {}
      }
    }

    it("should create application object", function() {
      expect(new Application(config)).to.be.instanceof(Application);
    });

    it("loads crucial components first", function() {
      var app = new Application(config);
      expect(app.getBeeHive()).to.be.OK;

      var beehive = app.getBeeHive();
      console.log(beehive.getService('Api'));

      expect(beehive.getService('Api').request).to.be.OK;
      expect(beehive.getService('PubSub').publish).to.be.OK;

      expect(beehive.getObject('User')).to.be.OK;

    });
  });
});