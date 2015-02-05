/**
 * Simple widget which just displays 'Login to Orcid' button and when the action
 * is complete, changes the spinning wheel back to normal. It communicates
 * through PubSub using ORCID_ANNOUNCEMENT signals
 *
 * XXX:rca This is a toy example that can be removed
 */
define([
    'underscore',
    'jquery',
    'backbone',
    'marionette',
    'js/widgets/base/base_widget',
    'js/modules/orcid/orcid_api_constants',
    'hbs!./templates/orcid_login_template',
    './model'
  ],
  function (
    _,
    $,
    Backbone,
    Marionette,
    BaseWidget,
    OrcidApiConstants,
    OrcidLoginTemplate,
    Model
    ) {

    var OrcidLoginView = Marionette.ItemView.extend({
      template: OrcidLoginTemplate,

      events: {
        "click .login-orcid-button": "loginClick",
        "click .signout-orcid-button": "signoutClick"
      },

      modelEvents: {
      },

      constructor: function(options) {
        this.model = new Model();
        this.listenTo(this, "loginwidget:stateChanged", this.stateChanged);
        return Marionette.ItemView.prototype.constructor.apply(this, arguments);
      },

      loginClick: function(e) {
        e.preventDefault();
        this.trigger('loginwidget:loginRequested');
      },

      signoutClick: function(e){
        e.preventDefault();
        this.trigger('loginwidget:signoutRequested');
      },

      //TODO:rca - move to the controller and let view synchronize via model changes
      stateChanged: function(state) {
        this.model.set('isSignedIn', false);
        this.model.set('isWaitingForProfileInfo', false);

        switch (state){
          case 'signedIn':
            this.model.set('isSignedIn', true);
            break;
          case 'signedOut':
            // isSignedIn is already false
            break;
          case 'waitingForProfileInfo':
            this.model.set('isWaitingForProfileInfo', true);
            break;
        }
        this.render();
      }
    });

    var OrcidLogin = BaseWidget.extend({

      activate: function (beehive) {
        this.pubSub = beehive.Services.get('PubSub');
        this.pubSub.subscribe(this.pubSub.ORCID_ANNOUNCEMENT, _.bind(this.routeOrcidPubSub, this));
      },

      initialize: function (options) {
        this.view = new OrcidLoginView();
        this.listenTo(this.view, "all", this.onAllInternalEvents);
        BaseWidget.prototype.initialize.call(this, options);
        return this;
      },

      routeOrcidPubSub : function(msg){
        switch (msg.msgType){
          case OrcidApiConstants.Events.LoginSuccess:
            this.switchToProfileView(msg.data);
            break;
          case OrcidApiConstants.Events.SignOut:
          case OrcidApiConstants.Events.LoginCancelled:
            this.switchToLoginView(msg.data);
            break;
        }
      },

      render: function () {
        this.view.render();
        return this.view;
      },

      switchToProfileView: function(orcidProfile){
        var personalDetails = orcidProfile['orcid-bio']['personal-details'];
        this.view.model.set('familyName', personalDetails['family-name']);
        this.view.model.set('givenName', personalDetails['given-names']);
        this.view.trigger('loginwidget:stateChanged', 'signedIn');
      },

      switchToLoginView : function(){
        this.view.model.set('familyName', undefined);
        this.view.model.set('givenName', undefined);

        this.view.trigger('loginwidget:stateChanged', 'signedOut');
      },

      onAllInternalEvents: function(ev, arg1, arg2) {

        if (ev === 'loginwidget:loginRequested') {
          this.view.trigger('loginwidget:stateChanged', 'waitingForProfileInfo');
          this.pubSub.publish(this.pubSub.ORCID_ANNOUNCEMENT, {msgType: OrcidApiConstants.Events.LoginRequested});
        }
        else if (ev === 'loginwidget:signoutRequested') {
          this.switchToLoginView();
          this.pubSub.publish(this.pubSub.ORCID_ANNOUNCEMENT, {msgType: OrcidApiConstants.Events.SignOut});
        }
      }
    });

    return OrcidLogin;
  });
