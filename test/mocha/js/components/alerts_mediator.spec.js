define([
  'jquery',
  'js/components/alerts_mediator',
  'js/components/generic_module',
  'js/bugutils/minimal_pubsub',
  'js/components/alerts',
  'js/widgets/alerts/widget',
  'js/components/api_feedback'
], function (
  $,
  AlertsMediator,
  GenericModule,
  MinimalPubSub,
  Alerts,
  AlertsWidget,
  ApiFeedback
  ) {

  describe("Alerts Mediator (alerts_mediator.spec.js)", function () {

    var minsub;
    beforeEach(function (done) {
      minsub = new MinimalPubSub({verbose: false});
      done();
    });

    afterEach(function (done) {
      minsub.close();
      var ta = $('#test');
      if (ta) {
        ta.empty();
      }
      done();
    });

    var _getM = function() {
      var m = new AlertsMediator();
      var widget = new AlertsWidget();
      widget.activate(minsub.beehive.getHardenedInstance());
      sinon.spy(m, 'alert');
      sinon.spy(m, 'onAlert');
      var app = {
        getWidget: function(name) {
          if (name == 'AlertsWidget')
            return widget;
        },
        getController: function(name) {
          if (name == 'AlertsController')
            return m;
        }
      };
      m.activate(minsub.beehive, app);
      return {m: m, app:app, widget:widget};
    };

    it("extends GenericModule", function () {
      expect(new AlertsMediator()).to.be.instanceof(GenericModule);
    });

    it("works with pubsub and alone", function(done) {
      var x = _getM();
      var m = x.m;

      expect(m.getWidget()).to.equal(x.widget);

      minsub.publish(minsub.ALERT, new ApiFeedback({code: 0, msg: 'foo'}));
      expect(m.onAlert.called).to.be.true;
      expect(m.alert.called).to.be.true;

      done();
    });



  })

});