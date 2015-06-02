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

  describe("Alerts Widget (alerts_widget.spec.js)", function () {

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

    var _getWidget = function() {
      var widget = new AlertsWidget();
      sinon.spy(widget, 'alert');
      widget.activate(minsub.beehive.getHardenedInstance());
      return widget;
    };

    it("extends BaseWidget", function () {
      expect(new AlertsWidget()).to.be.instanceof(BaseWidget);
    });

    it("displays messages", function(done) {
      var widget = _getWidget();

      var $w = widget.render().$el;
      $('#test').append($w);

      widget.model.set('msg', 'this is simple message');
      expect($w.find('#alertBox').text().indexOf('this is simple message') > -1).to.be.true;

      widget.model.set('msg', '');
      expect($w.find('#alertBox').length).to.be.eql(0);

      widget.model.set('msg', 'this is <a href="foo">html</a> message');
      expect($w.find('#alertBox a').attr('href')).to.be.eql('foo');

      // we can pass pass events
      var promise = widget.alert({
        msg: 'this is <a href="foo">html</a> message',
        events: {
          'click #alertBox a': {
            action: Alerts.ACTION.CALL_PUBSUB,
            signal: minsub.BIG_FIRE,
            arguments: ['foo', 'bar']
          }
        }
      });
      expect(promise.state()).to.be.eql('pending');
      var spy;
      promise.done((spy = sinon.spy()));
      $w.find('#alertBox a').click();
      expect(promise.state()).to.be.eql('resolved');
      expect(spy.lastCall.args[0]).to.be.eql({
        action: Alerts.ACTION.CALL_PUBSUB,
        signal: minsub.BIG_FIRE,
        arguments: ['foo', 'bar']
      });


      // now test that the event gets properly removed
      // after it was called
      sinon.spy(widget.model, 'get');
      widget.alert({
        msg: 'this is <a href="foo">html</a> message',
        events: {
          'click #alertBox a': 'foo-bar'
        }
      });
      $w.find('#alertBox a').click();

      widget.alert({
        msg: 'this is <a href="foo">html</a> message',
        events: {
          'click #alertBox a': 'foo-bar'
        }
      });
      $w.find('#alertBox a').click();

      // if the previous handler was removed, we'll get only 2 calls
      expect(_.filter(_.flatten(widget.model.get.args), function(x) {return x == 'promise'}).length).to.be.eql(2);



      //now check the widget appears in modal mode
      promise = widget.alert({
        msg: 'this is <a href="foo">html</a> message',
        modal: true,
        events: {
          'click #alertBox a': {
            action: Alerts.ACTION.TRIGGER_FEEDBACK,
            arguments: {code: 0}
          }
        }
      });

      $w.find('#alertBox a').click();
      expect(promise.state()).to.be.eql('resolved');

      setTimeout(function() {
        expect($w.find('#alertBox a').is(':visible')).to.be.true;
        $w.find('button.close').click();
        setTimeout(function() {
          expect($w.find('#alertBox a').is(':visible')).to.be.false;
          done();
        }, 500)

      }, 500);

    });



  })

});