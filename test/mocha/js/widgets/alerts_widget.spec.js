define([
  'jquery',
  'js/widgets/alerts/widget',
  'js/widgets/base/base_widget',
  'js/bugutils/minimal_pubsub',
  'js/components/alerts'
], function (
  $,
  AlertsWidget,
  BaseWidget,
  MinimalPubSub,
  Alerts
  ) {

  describe("Export Widget (alerts_widget.spec.js)", function () {

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

    var _getWidget = function() {
      var widget = new AlertsWidget();
      sinon.spy(widget, 'alert');
      widget.activate(minsub.beehive.getHardenedInstance());
      return widget;
    };

    it("extends BaseWidget", function () {
      expect(new AlertsWidget()).to.be.instanceof(BaseWidget);
    });

    it("displays messages", function() {
      var widget = _getWidget();
      var $w = widget.render().$el;
      $('#test').append($w);

      widget.model.set('msg', 'this is simple message');
      expect($w.find('#alertBox').text().indexOf('this is simple message') > -1).to.be.true;

      widget.model.set('msg', '');
      expect($w.find('#alertBox').length).to.be.eql(0);

      widget.model.set('msg', 'this is <a href="foo">html</a> message');
      expect($w.find('#alertBox a').attr('href')).to.be.eql('foo');

      // we can pass 'events' and they will be delegated
      widget.model.set({
        msg: 'this is <a href="foo">html</a> message',
        events: {
          'click #alertBox a': {
            action: Alerts.ACTION.CALL_PUBSUB,
            arguments: ['foo', 'bar']
          }
        }
      });
      $w.find('#alertBox a').click();

    });



  })

});