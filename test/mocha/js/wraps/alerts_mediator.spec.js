define([
  'jquery',
  'js/wraps/alerts_mediator',
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

  describe("Alerts Mediator (wraps/alerts_mediator.spec.js)", function () {

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
      sinon.spy(m, 'checkAndDisplaySiteMessage');

      var app = {
        getWidget: function(name) {
          if (name == 'AlertsWidget')
            return widget;
        },
        getController: function(name) {
          if (name == 'AlertsController')
            return m;
        },

        _getWidget: function(){
          return widget;
        }
      };
      m.activate(minsub.beehive, app);
      return {m: m, app:app, widget:widget};
    };

    it("registers itself", function (done) {
      var x = _getM();
      var m = x.m;


      expect(m.onAlert.called).to.be.false;
      minsub.publish(minsub.APP_STARTED);
      setTimeout(function() {
        expect(m.timerId).to.be.defined;
        m.onDestroy();
        done();
      }, 1001);

    });

    it("gets and displays site-wide message", function (done) {
      var x = _getM();
      var m = x.m;

      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.false;


      m.getBeeHive().getObject = function() {
        return {
          getSiteConfig: function() {
            var d = $.Deferred();
            d.reject({});
            return d.promise();
          }
        }
      };
      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.false;


      m.getBeeHive().getObject = function() {
        return {
          getSiteConfig: function() {
            var d = $.Deferred();
            d.resolve('foo bar');
            return d.promise();
          },
          isLoggedIn: function() {
            return false;
          }
        }
      };
      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.true;
      expect(m.alert.lastCall.args[0].msg).to.eql('foo bar');


      minsub.publish(minsub.ALERT, new ApiFeedback({code: 0, msg: 'foo'}));
      m.alert.reset();
      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.false;


      m._dirty = false;
      m.getBeeHive().getObject = function() {
        return {
          getSiteConfig: function() {
            var d = $.Deferred();
            d.resolve('foo bar');
            return d.promise();
          },
          isLoggedIn: function() {
            return true;
          },
          getUserData: function() {
            return {'last_seen_message': 'foo bar'};
          }
        }
      };
      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.false;

      m.getBeeHive().getObject = function() {
        return {
          getSiteConfig: function() {
            var d = $.Deferred();
            d.resolve('foo bar');
            return d.promise();
          },
          isLoggedIn: function() {
            return true;
          },
          getUserData: function() {
            return {'last_seen_message': 'foo x'};
          }
        }
      };
      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.true;
      m.alert.reset();


      var pVal = 'foo bar';
      minsub.beehive.addService('PersistentStorage', {
        get: function() {
          return pVal;
        },
        set: sinon.spy()
      });
      m.getBeeHive().getObject = function() {
        return {
          getSiteConfig: function() {
            var d = $.Deferred();
            d.resolve('foo bar');
            return d.promise();
          },
          isLoggedIn: function() {
            return false;
          }
        }
      };
      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.false;

      pVal = 'foo x';
      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.true;


      m.alert.reset();
      var $w = x.widget.render().$el;
      $('#test').append($w);
      var user = {
        getSiteConfig: function() {
          var d = $.Deferred();
          d.resolve('foo bar');
          return d.promise();
        },
        isLoggedIn: function() {
          return true;
        },
        getUserData: function() {
          return {'last_seen_message': 'foo x'};
        },
        setMyADSData: sinon.spy()
      };

      m.getBeeHive().getObject = function() {
        return user;
      };
      m.checkAndDisplaySiteMessage();
      expect(m.alert.called).to.be.true;

      $w.find('button.close').click();
      expect(m.getBeeHive().getService('PersistentStorage').set.lastCall.args).to.eql(['last_seen_message', 'foo bar'])
      expect(m.getBeeHive().getObject().setMyADSData.lastCall.args[0]).to.eql({ last_seen_message: 'foo bar' });

      done();

    });

  })

});