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
  'js/mixins/api_access'

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
   ApiAccess
    ) {


  var SessionModel = Backbone.Model.extend({

    defaults : function(){
      return {
        resetPasswordToken: undefined
      }
    }

  });


  var Session = GenericModule.extend({

    initialize: function (options) {
      var options = options || {};
      //right now, this will only be used if someone forgot their password
      this.model = new SessionModel();

      this.test = options.test ? true : undefined;

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

      var csrfToken = this.getBeeHive().getObject("AppStorage").get("csrf");

      var request = new ApiRequest({
        target : ApiTargets.USER,
        query: new ApiQuery({}),
        options : {
          type : "POST",
          data: JSON.stringify(data),
          contentType : "application/json",
          headers : {'X-CSRFToken' :  csrfToken },
          done : this.loginSuccess,
          fail : this.loginFail,
          beforeSend: function(jqXHR, settings) {
            jqXHR.session = this;
          }
        }
      });
     return this.getBeeHive().getService("Api").request(request);
    },

    logout: function () {

      var csrfToken = this.getBeeHive().getObject("AppStorage").get("csrf");

      var request = new ApiRequest({
        target : ApiTargets.LOGOUT,
        query : new ApiQuery({}),
        options : {
          context : this,
          type : "GET",
          headers : {'X-CSRFToken' :  csrfToken },
          contentType : "application/json",
          done : this.logoutSuccess
        }
      });
      return this.getBeeHive().getService("Api").request(request);
    },

    register: function (data) {
      var base_url;
      //add base_url to data so email redirects to right url
      if (!this.test){
        base_url = location.origin
        _.extend(data, {verify_url : location.origin + "/#user/account/verify/register" });
      }
      else {
        base_url = "location.origin"
      }
      _.extend(data, {verify_url : base_url + "/#user/account/verify/register" });

      var csrfToken = this.getBeeHive().getObject("AppStorage").get("csrf");

      var request = new ApiRequest({
        target : ApiTargets.REGISTER,
        query : new ApiQuery({}),
        options : {
          type : "POST",
          data : JSON.stringify(data),
          contentType : "application/json",
          headers : {'X-CSRFToken' :  csrfToken },
          done : this.registerSuccess,
          fail : this.registerFail
        }
      });
      return this.getBeeHive().getService("Api").request(request);

    },

    resetPassword1: function(data){
      var current_loc
      //add base_url to data so email redirects to right url
      if (!this.test){
        current_loc = location.origin;
      }
      else {
        current_loc = "location.origin";
      }
      _.extend(data, {reset_url : current_loc + "/#user/account/verify/reset-password"});

     var email = data.email;
     var data = _.omit(data, "email");
     var csrfToken = this.getBeeHive().getObject("AppStorage").get("csrf");

      var request = new ApiRequest({
        target : ApiTargets.RESET_PASSWORD + "/" + email,
        query : new ApiQuery({}),
        options : {
          type : "POST",
          data : JSON.stringify(data),
          headers : {'X-CSRFToken' :  csrfToken },
          contentType : "application/json",
          done : this.resetPassword1Success,
          fail : this.resetPassword1Fail
        }
    });
      return this.getBeeHive().getService("Api").request(request);
    },

    resetPassword2: function(data){
      var csrfToken = this.getBeeHive().getObject("AppStorage").get("csrf");

      var request = new ApiRequest({
        target : ApiTargets.RESET_PASSWORD + "/" + this.model.get("resetPasswordToken"),
        query : new ApiQuery({}),
        options : {
          type : "PUT",
          data : JSON.stringify(data),
          contentType : "application/json",
          headers : {'X-CSRFToken' :  csrfToken },
          done : this.resetPassword2Success,
          fail : this.resetPassword2Fail
        }
      });
      return this.getBeeHive().getService("Api").request(request);
    },

    setChangeToken : function(token){
      this.model.set("resetPasswordToken", token);
    },

    /* private methods */

    loginSuccess: function (response, status, jqXHR) {
      var promise, pubsub;
      //reset auth token by contacting Bootstrap
      //this will log the user in
      promise = this.getApiAccess({reconnect: true});
      //notify interested widgets
      pubsub = this.getPubSub();
      promise.done(function(){
        pubsub.publish(pubsub.USER_ANNOUNCEMENT, "login_success");
      });
      promise.fail(function(){
        pubsub.publish(pubsub.USER_ANNOUNCEMENT, "login_fail");
      })
    },

    loginFail : function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var error =  xhr.responseJSON && xhr.responseJSON.error ?  xhr.responseJSON.error : "error unknown";

      var message = 'Log in was unsuccessful (' + error + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade : true}));
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
      var error = (xhr.responseJSON && xhr.responseJSON.error)  ? xhr.responseJSON.error : "error unknown";
      var message = 'Registration was unsuccessful (' + error + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "register_fail");
    },

    resetPassword1Success :function(response, status, jqXHR){
      var pubsub = this.getPubSub();
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "reset_password_1_success");
    },

    resetPassword1Fail: function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var error = (xhr.responseJSON && xhr.responseJSON.error)  ? xhr.responseJSON.error : "error unknown";
      var message = 'password reset step 1 was unsucessful (' + error + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "reset_password_1_fail");
    },

    resetPassword2Success :function(response, status, jqXHR){

      var promise, pubsub;
      //reset auth token by contacting Bootstrap
      //this will log the user in
      promise = this.getApiAccess({reconnect : true});
      //notify interested widgets
      pubsub = this.getPubSub();
      promise.done(function(){
        pubsub.publish(pubsub.USER_ANNOUNCEMENT, "reset_password_2_success");
        var message = "Your password has been successfully reset";
        pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "success", modal: true}));
      });

      promise.fail(function(){
        pubsub.publish(pubsub.USER_ANNOUNCEMENT, "reset_password_2_fail");
        var message = "Your password was not successfully reset. Please try to follow the link from the email you received again.";
        pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", modal: true}));
      });

    },

    resetPassword2Fail: function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var error = (xhr.responseJSON && xhr.responseJSON.error)  ? xhr.responseJSON.error : "error unknown";
      var message = 'password reset step 2 was unsucessful (' + error + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "reset_password_2_fail");
    },


    hardenedInterface: {
      isLoggedIn: "is the user authenticated",
      login: "log the user in",
      logout: "log the user out",
      register: "registers a new user",
      resetPassword1 : "sends an email to account",
      resetPassword2 : "updates the password",
      setChangeToken : "the router stores the token to reset password here"
    }

  });

  _.extend(Session.prototype, Dependon.BeeHive, Dependon.PubSub);
  _.extend(Session.prototype, Hardened);
  _.extend(Session.prototype, ApiAccess);

  return Session;

});