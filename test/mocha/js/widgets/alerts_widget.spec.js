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

    it("displays messages", function(done) {
      var widget = _getWidget();
      sinon.spy(widget.pubsub, 'publish');

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
            signal: minsub.BIG_FIRE,
            arguments: ['foo', 'bar']
          }
        }
      });
      $w.find('#alertBox a').click();

      expect(widget.pubsub.publish.lastCall.args).to.be.eql([minsub.BIG_FIRE, ['foo', 'bar']]);

      widget.pubsub.publish.reset();

      widget.model.set({
        msg: 'this is <a href="foo">html</a> message',
        events: {
          'click #alertBox a': {
            action: Alerts.ACTION.TRIGGER_FEEDBACK,
            arguments: {code: 0}
          }
        }
      });
      $w.find('#alertBox a').click();

      // if the previous handler was removed, we'll get only one call
      expect(widget.pubsub.publish.callCount).to.be.eql(1);
      expect(widget.pubsub.publish.lastCall.args[0]).to.be.eql(minsub.FEEDBACK);
      expect(widget.pubsub.publish.lastCall.args[1].toJSON()).to.be.eql({code: 0, msg: undefined});

      //now it should appear in modal mode
      widget.model.set({
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
      setTimeout(function() {
        expect($w.find('#alertBox a').is(':visible')).to.be.true;
        //widget.view.closeModal();
        widget.model.set({modal: false});
        setTimeout(function() {
          expect($w.find('#alertBox.modal').length).to.be.eql(0);
          done();
        }, 500)

      }, 500);

    });



  })

});