/*
 * A generic class that lazy-loads User info
 */
define([
  'backbone',
  'js/components/api_request',
  'js/components/api_targets',
  'js/mixins/hardened',
  'js/components/generic_module',
  'js/mixins/dependon',
  'js/components/api_query',
  'js/components/user',
  'js/components/api_feedback',
  'js/mixins/discovery_bootstrap'

], function
  (Backbone,
   ApiRequest,
   ApiTargets,
   Hardened,
   GenericModule,
   Dependon,
   ApiQuery,
   User,
   ApiFeedback,
   DiscoveryBootstrap
    ) {


  var Session = GenericModule.extend({

    initialize: function (options) {
      var options = options || {};

      _.bindAll(this, [
        "loginSuccess",
        "loginFail",
        "registerSuccess",
        "registerFail",
        "resetPassword1Success",
        "resetPassword1Fail",
        "resetPassword2Success",
        "resetPassword2Fail"
      ]);
    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.setPubSub(beehive);
    },

    /* methods that will be available to widgets */

    //not sure if I need this
    isLoggedIn: function () {
      return this.getBeeHive().getObject("User").isLoggedIn();
    },

    login: function (data) {
      var request = new ApiRequest({
        target : ApiTargets.USER,
        query: new ApiQuery({}),
        options : {
          type : "POST",
          data: JSON.stringify(data),
          contentType : "application/json",
          done : this.loginSuccess,
          fail : this.loginFail,
          beforeSend: function(jqXHR, settings) {
            jqXHR.session = this;
          }
        }
      });

     this.getBeeHive().getService("Api").request(request);
    },

    logout: function () {
      var request = new ApiRequest({
        target : ApiTargets.LOGOUT,
        query : new ApiQuery({}),
        options : {
          context : this,
          type : "GET",
          contentType : "application/json",
          done : this.logoutSuccess
        }
      });
      this.getBeeHive().getService("Api").request(request);
    },

    register: function (data) {
      //add base_url to data so email redirects to right url
      _.extend(data, {verify_url : location.origin + "/user/account/verify/" + ApiTargets.REGISTER });

      var request = new ApiRequest({
        target : ApiTargets.REGISTER,
        query : new ApiQuery({}),
        options : {
          type : "POST",
          data : JSON.stringify(data),
          contentType : "application/json",
          done : this.registerSuccess,
          fail : this.registerFail
        }
      });
      this.getBeeHive().getService("Api").request(request);
    },

    deleteAccount: function () {
      var request = new ApiRequest({
        target : ApiTargets.DELETE,
        query : new ApiQuery({}),
        options : {
          type : "POST",
          data : undefined,
          contentType : "application/json",
          done : this.deleteSuccess,
          fail : this.deleteFail
        }
      });
      this.getBeeHive().getService("Api").request(request);
    },

    resetPassword1: function(data){
      //add base_url to data so email redirects to right url
      _.extend(data, {verify_url : location.origin + "/user/account/verify/" + ApiTargets.RESET_PASSWORD });

     var email = data.email;
     var data = _.pick(data, "g-recaptcha-response");

      var request = new ApiRequest({
        target : ApiTargets.RESET_PASSWORD + "/" + email,
        query : new ApiQuery({}),
        options : {
          type : "POST",
          data : JSON.stringify(data),
          contentType : "application/json",
          done : this.resetPassword1Success,
          fail : this.resetPassword1Fail
        }
    });
      this.getBeeHive().getService("Api").request(request);
    },

    resetPassword2: function(data){
      var email = data.email;
      var data = _.pick(data, "g-recaptcha-response");

      var request = new ApiRequest({
        target : ApiTargets.RESET_PASSWORD + "/" + email,
        query : new ApiQuery({}),
        options : {
          type : "PUT",
          data : JSON.stringify(data),
          contentType : "application/json",
          done : this.resetPassword2Success,
          fail : this.resetPassword2Fail
        }
      });
      this.getBeeHive().getService("Api").request(request);
    },

    /* private methods */

    loginSuccess: function (response, status, jqXHR) {
      var user, promise, pubsub;
      user = this.getBeeHive().getObject("User");

      //reset auth token by contacting Bootstrap
      promise = this.getApiAccess();
      promise.done(user.completeLogIn());

      //notify interested widgets
      pubsub = this.getPubSub();
      promise.done(function(){
        pubsub.publish(pubsub.USER_ANNOUNCEMENT, "login_success");
      });
      //finally, redirect to landing page
      promise.done(function(){
        pubsub.publish(pubsub.NAVIGATE, 'index-page');
      });
    },

    loginFail : function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var message = 'Log in was unsuccessful (' + errorThrown + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "login_fail");
    },

    logoutSuccess : function (response, status, jqXHR) {
      //set session state to logged out
      var user = this.getBeeHive().getObject("User");
      user.completeLogOut();

      var pubsub = this.getPubSub();
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "logout_success");
    },

    registerSuccess : function (response, status, jqXHR) {
      var pubsub = this.getPubSub();
      //authentication widget will show a "success" view in response to this user announcement
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "register_success");
    },

    registerFail : function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var message = 'Registration was unsuccessful (' + errorThrown + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "register_fail");
    },

    deleteSuccess : function (response, status, jqXHR) {
      //this doesn't exist yet
      var pubsub = this.getPubSub();
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "user_delete_success");
    },

    deleteFail : function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var message = 'Account deletion was unsuccessful (' + errorThrown + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "user_delete_fail");
    },

    resetPassword1Success :function(response, status, jqXHR){
      var pubsub = this.getPubSub();
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "reset_password_success");
    },

    resetPassword1Fail: function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var message = 'Request for password reset was unsucessful (' + errorThrown + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "reset_password_fail");
    },

    resetPassword2Success :function(response, status, jqXHR){
      var pubsub = this.getPubSub();
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "reset_password_success");
    },

    resetPassword2Fail: function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var message = 'Request for password reset was unsucessful (' + errorThrown + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "reset_password_fail");
    },


    hardenedInterface: {
      isLoggedIn: "is the user authenticated",
      login: "log the user in",
      logout: "log the user out",
      register: "registers a new user",
      resetPassword1 : "sends an email to account",
      resetPassword2 : "updates the password",
      deleteAccount: "deletes the current account"
    }

  });

  _.extend(Session.prototype, Dependon.BeeHive, Dependon.PubSub);
  _.extend(Session.prototype, Hardened);
  _.extend(Session.prototype, DiscoveryBootstrap);

  return Session;

});