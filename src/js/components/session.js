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
    },

    /* methods that will be available to widgets */


    login: function (data) {

    var d = $.Deferred(),
        that = this;

    this.sendRequestWithNewCSRF(function(csrfToken){
      var request = new ApiRequest({
        target : ApiTargets.USER,
        query: new ApiQuery({}),
        options : {
          type : "POST",
          data: JSON.stringify(data),
          contentType : "application/json",
          headers : {'X-CSRFToken' :  csrfToken },
          done : function(){
            //allow widgets to listen for success or failure
            d.resolve.apply(d, arguments);
            //session response to success
            that.loginSuccess.apply(that, arguments);
          },
          fail : function(){
            d.reject.apply(d, arguments);
            that.loginFail.apply(that, arguments);
          },
          beforeSend: function(jqXHR, settings) {
            jqXHR.session = this;
          }
        }
      });
      return this.getBeeHive().getService("Api").request(request);
    });
      return d.promise();
    },

    /*
    * every time a csrf token is required, csrf manager will request a new token,
    * and it allows you to attach callbacks to the promise it returns
    * */
    sendRequestWithNewCSRF : function(callback){
      callback = _.bind(callback, this);
      this.getBeeHive().getObject("CSRFManager").getCSRF().done(callback);
    },

    logout: function () {

      this.sendRequestWithNewCSRF(function(csrfToken){

        var request = new ApiRequest({
          target : ApiTargets.LOGOUT,
          query : new ApiQuery({}),
          options : {
            context : this,
            type : "POST",
            headers : {'X-CSRFToken' :  csrfToken },
            contentType : "application/json",
            done : this.logoutSuccess
          }
        });
        return this.getBeeHive().getService("Api").request(request);

      });
    },

    register: function (data) {

      var current_loc = this.test ? "location.origin" : location.origin;
      //add base_url to data so email redirects to right url
       _.extend(data, {verify_url : current_loc + "/#user/account/verify/register" });

      this.sendRequestWithNewCSRF(function(csrfToken) {

        var request = new ApiRequest({
          target: ApiTargets.REGISTER,
          query: new ApiQuery({}),
          options: {
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            headers: {'X-CSRFToken': csrfToken },
            done: this.registerSuccess,
            fail: this.registerFail
          }
        });
        return this.getBeeHive().getService("Api").request(request);

      });
    },

    resetPassword1: function(data){

      var current_loc = this.test ? "location.origin" : location.origin;
      //add base_url to data so email redirects to right url
      _.extend(data, {reset_url : current_loc + "/#user/account/verify/reset-password"});

     var email = data.email;
     var data = _.omit(data, "email");

     this.sendRequestWithNewCSRF(function(csrfToken){
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
     });

    },

    resetPassword2: function(data){

      this.sendRequestWithNewCSRF(function(csrfToken){
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
      });

    },

    setChangeToken : function(token){
      this.model.set("resetPasswordToken", token);
    },

    /* private methods */

    loginSuccess: function (response, status, jqXHR) {
      //reset auth token by contacting Bootstrap, which will log user in
      var that = this;
       this.getApiAccess({reconnect: true}).done(function(){
       });
    },

    loginFail : function(xhr, status, errorThrown){
      var pubsub = this.getPubSub(), error;
      if (xhr.responseJSON && xhr.responseJSON.error ){
        error = xhr.responseJSON.error
      }
      else if (xhr.responseJSON && xhr.responseJSON.message){
        error = xhr.responseJSON.message;
      }
      else {
        error = "error unknown";
      }
      var message = 'Log in was unsuccessful (' + error + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade : true}));
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "login_fail");
    },

    logoutSuccess : function (response, status, jqXHR) {
      var that = this;
      this.getApiAccess({reconnect: true}).done(function(){
        //set session state to logged out
        that.getBeeHive().getObject("User").completeLogOut();
      });
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
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "reset_password_1_success");
    },

    resetPassword1Fail: function(xhr, status, errorThrown){
      var pubsub = this.getPubSub();
      var error = (xhr.responseJSON && xhr.responseJSON.error)  ? xhr.responseJSON.error : "error unknown";
      var message = 'password reset step 1 was unsucessful (' + error + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger", fade: true}));
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "reset_password_1_fail");
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
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "reset_password_2_fail");
    },


    hardenedInterface: {
      login: "log the user in",
      logout: "log the user out",
      register: "registers a new user",
      resetPassword1 : "sends an email to account",
      resetPassword2 : "updates the password",
      setChangeToken : "the router stores the token to reset password here"
    }

  });

  _.extend(Session.prototype, Dependon.BeeHive, Hardened, ApiAccess);

  return Session;

});