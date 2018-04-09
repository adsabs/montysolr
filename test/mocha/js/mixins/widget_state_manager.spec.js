'use strict';
define([
  'underscore',
  'js/bugutils/minimal_pubsub',
  'js/mixins/widget_state_manager'
], function (_, MinimalPubsub, stateManager) {
  var MockWidget = function () {
    this.activate = function () {
      this.activateWidget();
    };
  };
  _.extend(MockWidget.prototype, stateManager);

  var init = function () {
    this.sb = sinon.sandbox.create();
    var minsub = new MinimalPubsub({verbose: false});
    minsub.getCurrentPubSubKey = _.constant(minsub.key);
    this.subSpy = this.sb.spy(minsub, 'subscribe');
    this.getPubSubKeySpy = this.sb.spy(minsub, 'getCurrentPubSubKey');
    this.widget = new MockWidget();
    this.widget.getPubSub = _.constant(minsub);
    this.widget.activate(minsub.beehive.getHardenedInstance());
  };

  var teardown = function () {
    this.sb.restore();
    this.subSpy = null;
    this.getPubSubKeySpy = null;
    this.widget = null;
  }

  describe('Widget State Manager (widget_state_manager.spec.js)', function () {
    describe('Feedback Management', function () {

      describe('activation', function () {
        beforeEach(init);
        afterEach(teardown);

        it('attaches handlerManager property', function () {
          expect(this.widget.hasOwnProperty('__widgetHandlerManager')).to.eql(true);
        });

        it('initialize properly', function () {

          // added a subscription
          expect(this.subSpy.callCount).to.eql(1);
          expect(this.subSpy.args[0][0]).to.match(/feedback/i);
        });
      });

      describe('attachHandler', function () {
        beforeEach(init);
        afterEach(teardown);

        it('attaches handler to code', function () {
          var handler = this.sb.spy();
          this.widget.attachHandler(-10, handler);
          var arr = this.widget.__widgetHandlerManager.handlers[-10];
          expect(arr.length).to.eql(1);
          expect(arr[0]).to.eql(handler);
        });

        it('attaches multiple handlers to code', function () {
          var self = this;
          var handlers = _.map(_.range(0, 10), function () {
            var handler = self.sb.spy();
            self.widget.attachHandler(-10, handler);
            return handler;
          });

          var arr = this.widget.__widgetHandlerManager.handlers[-10];
          expect(arr.length).to.eql(10);
          expect(_.isEqual(arr, handlers)).to.eql(true);
        });
      });

      describe('detachHandler', function () {
        beforeEach(init);
        afterEach(teardown);

        it('detaches handler from code', function () {
          var handler = this.sb.spy();
          this.widget.attachHandler(-10, handler);
          var arr = this.widget.__widgetHandlerManager.handlers[-10];
          expect(arr.length).to.eql(1);
          expect(arr[0]).to.eql(handler);
          this.widget.detachHandler(handler);
          expect(arr.length).to.eql(0);
        });

        it('detaches multiple handlers from code', function () {
          var self = this;
          var handlers = _.map(_.range(0, 10), function () {
            var handler = self.sb.spy();
            self.widget.attachHandler(-10, handler);
            return handler;
          });

          var arr = this.widget.__widgetHandlerManager.handlers[-10];
          expect(arr.length).to.eql(10);
          expect(_.isEqual(arr, handlers)).to.eql(true);

          _.forEach(handlers, function (h) {
            var len = arr.length;
            self.widget.detachHandler(h);
            expect(arr.length).to.eql(len - 1);
          });
          expect(arr.length).to.eql(0);
        });
      });

      describe('fireHandlers', function () {
        beforeEach(init);
        afterEach(teardown);

        it('fires handler attached to a code', function () {
          var handler = this.sb.spy();
          this.widget.attachHandler(-10, handler);
          var pubsub = this.widget.getPubSub();
          pubsub.publish(pubsub.FEEDBACK, { code: -10 });
          expect(this.getPubSubKeySpy.callCount).to.eql(1);
          expect(handler.callCount).to.eql(1);
          expect(handler.args[0][0]).to.eql({ code: -10 });
        });

        it('fires multiple handlers attached to a code', function () {
          var self = this;
          var handlers = _.map(_.range(0, 10), function () {
            var handler = self.sb.spy();
            self.widget.attachHandler(-10, handler);
            return handler;
          });
          var pubsub = this.widget.getPubSub();
          pubsub.publish(pubsub.FEEDBACK, { code: -10 });
          _.forEach(handlers, function (h) {
            expect(h.callCount).to.eql(1);
            expect(h.args[0][0]).to.eql({ code: -10 });
          });
        });
      });
    });

    describe('State Management', function () {

      describe('activation', function () {
        beforeEach(init);
        afterEach(teardown);

        it('attaches state property', function () {
          expect(this.widget.hasOwnProperty('__state')).to.eql(true);
          expect(this.widget.__state).to.eql('ready');
        });
      });

      describe('getState', function () {
        beforeEach(init);
        afterEach(teardown);

        it('calling getState returns current state', function () {
          expect(this.widget.getState()).to.eql(this.widget.__state);
        });

      });

      describe('updateState', function () {
        beforeEach(init);
        afterEach(teardown);

        it('updates state properly', function () {
          var w = this.widget;
          w.__fireStateHandler = this.sb.spy();
          w.onStateChange = this.sb.spy();
          expect(w.__state).to.eql('ready');
          w.updateState('loading');
          expect(w.__state).to.eql('loading');
          w.updateState('errored');
          expect(w.__state).to.eql('errored');
          w.updateState('foo');
          expect(w.__state).to.eql('foo');
          expect(w.__fireStateHandler.callCount).to.eql(3);
          expect(w.onStateChange.callCount).to.eql(3);
        });

        it('does nothing if state is same', function () {
          var w = this.widget;
          w.__fireStateHandler = this.sb.spy();
          w.onStateChange = this.sb.spy();
          w.updateState('foo');
          expect(w.__fireStateHandler.callCount).to.eql(1);
          expect(w.onStateChange.callCount).to.eql(1);
          w.updateState('foo');
          expect(w.__fireStateHandler.callCount).to.eql(1);
          expect(w.onStateChange.callCount).to.eql(1);
        });

        it('does nothing if state is not a string', function () {
          var w = this.widget;
          w.__fireStateHandler = this.sb.spy();
          w.onStateChange = this.sb.spy();
          w.updateState(999);
          w.updateState({});
          w.updateState(true);
          expect(w.__fireStateHandler.callCount).to.eql(0);
          expect(w.onStateChange.callCount).to.eql(0);
        });
      });

      describe('onStateChange', function () {
        beforeEach(init);
        afterEach(teardown);

        it('gets new and previous state', function () {
          var w = this.widget;
          w.onStateChange = this.sb.spy();
          w.updateState('loading');
          expect(w.onStateChange.args[0]).to.eql(['loading', 'ready']);
          w.updateState('idle');
          expect(w.onStateChange.args[1]).to.eql(['idle', 'loading']);
        });
      });

      describe('properly fires state handlers', function () {
        beforeEach(init);
        afterEach(teardown);

        it('fires off handler for current state', function () {
          var w = this.widget;
          w.onReady = this.sb.spy();
          w.onLoading = this.sb.spy();
          w.onIdle = this.sb.spy();
          w.onErrored = this.sb.spy();
          w.onStateChange = this.sb.spy();

          w.updateState('loading');
          expect(w.onLoading.callCount).to.eql(1);
          expect(w.onStateChange.callCount).to.eql(1);

          w.updateState('ready');
          expect(w.onReady.callCount).to.eql(1);
          expect(w.onStateChange.callCount).to.eql(2);

          w.updateState('idle');
          expect(w.onIdle.callCount).to.eql(1);
          expect(w.onStateChange.callCount).to.eql(3);

          w.updateState('errored');
          expect(w.onErrored.callCount).to.eql(1);
          expect(w.onStateChange.callCount).to.eql(4);
        });
      });
    });
  });
});
