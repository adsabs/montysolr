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
      minsub.destroy();
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
      var m = new AlertsMediator();
      expect(function() {m.activate(minsub.beehive, {getWidget: function() {}})}).to.throw.Error;
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

    it("fails when message cannot be displayed", function() {
      var x = _getM();
      x.app.getWidget = function() {};
      var promise = x.m.alert(new ApiFeedback({msg: 'foo'}));
      expect(promise.state()).to.be.eql('rejected');
    });

    it("accepts different payload for events", function() {
      var x = _getM();
      var $w = x.widget.render().$el;
      $('#test').append($w);

      var promise;
      promise = x.m.onAlert(new ApiFeedback({
        msg: 'this is <a href="foo">html</a> message',
        events: {
          'click #alertBox a': 'foo-bar'
        }
      }))
      .done(function(x) {
        expect(x).to.be.eql('foo-bar');
      });
      $w.find('#alertBox a').click();
      expect(promise.state()).to.be.eql('resolved');


      // function
      var spy = sinon.spy();
      promise = x.m.onAlert(new ApiFeedback({
        msg: 'this is <a href="foo">html</a> message',
        events: {
          'click #alertBox a': spy
        }
      }));
      $w.find('#alertBox a').click();
      expect(promise.state()).to.eql('resolved');
      expect(spy.called).to.be.true;

      // actions
      sinon.spy(x.m.pubsub, 'publish');
      promise = x.m.onAlert(new ApiFeedback({
        msg: 'this is <a href="foo">html</a> message',
        events: {
          'click #alertBox a': {
            action: Alerts.ACTION.TRIGGER_FEEDBACK,
            arguments: {code: 0}
          }
        }
      }));
      $w.find('#alertBox a').click();
      expect(x.m.pubsub.publish.called).to.be.true;

    });

  })

});