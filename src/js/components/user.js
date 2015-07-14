/*
 * A generic class that lazy-loads User info
 */
define([
  'backbone',
  'js/components/api_request',
  'js/components/api_targets',
  'js/components/generic_module',
  'js/mixins/dependon',
  'js/mixins/hardened',
  'js/components/api_feedback'
  ],
 function(
   Backbone,
   ApiRequest,
   ApiTargets,
   GenericModule,
   Dependon,
   Hardened,
   ApiFeedback) {

 var PersistentUserModel, UserModel, UserDataModel, User;

//stored in Persistent Storage for now
 PersistentUserModel = Backbone.Model.extend({
    defaults : function(){
      return {
        isOrcidModeOn : false
      }
    }
  });

   UserModel = Backbone.Model.extend({
     defaults : function(){
       return {
         user : undefined
       }
     }
   })

  UserDataModel = Backbone.Model.extend({

    defaults : function(){
      return {
        link_server : undefined
      }
    }

  });

  User = GenericModule.extend({

    initialize : function(options){
      //model is for settings that don't have an accounts target
      this.persistentModel = new PersistentUserModel();
      this.model = new UserModel();
      this.listenTo(this.model, "change:user", this.broadcastUserChange);
      //each entry in the collection corresponds to an target
      this.userDataModel = new UserDataModel();
      this.listenTo(this.userDataModel, "change", this.broadcastUserDataChange);

      _.bindAll(this, "completeLogIn", "completeLogOut");

      //set base url, currently only necessary for change_email endpoint
      this.base_url = this.test ? "location.origin" : location.origin;

      this.buildAdditionalParameters();
    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.setPubSub(beehive);
      this.key = this.getPubSub().getPubSubKey();

      var storage = beehive.getService('PersistentStorage');
      if (storage) {
        var prefs = storage.get('UserPreferences');
        if (prefs) {
          this.model.set(prefs);
        }
      }
    },

    /* orcid functions */
    setOrcidMode : function(val){
      if (!this.hasBeeHive())
        return;

      this.model.set("isOrcidModeOn", val);
      if (_.has(this.persistentModel.changedAttributes(), "isOrcidModeOn")){
        this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, "orcidUIChange", this.persistentModel.get("isOrcidModeOn"));
      }
      this._persistModel();
    },

    isOrcidModeOn : function(){
      return this.model.get("isOrcidModeOn");
    },

    //XXX a quick hack
    _persistModel: function() {
      var beehive = this.getBeeHive();
      var storage = beehive.getService('PersistentStorage');
      if (storage) {
        storage.set('UserPreferences', this.persistentModel.attributes);
      }
    },

    /* general functions */

    // finally, check if logged in, might have to redirect to auth page/settings page
    broadcastUserDataChange : function(model){
      this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, "user_info_change", this.userDataModel.toJSON());
    },

    broadcastUserChange : function(){
      //user has signed in or out
      var message = this.model.get("user") ? "user_signed_in" : "user_signed_out";
      this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, message, this.model.get("user"));
      this.redirectIfNecessary();
    },

    buildAdditionalParameters : function() {
      //any extra info that needs to be sent in post or get requests
      //but not known about by the widget models goes here
      var additional = {};
          additional.CHANGE_EMAIL = { verify_url : this.base_url + "/#user/account/verify/change-email"};
      this.additionalParameters = additional;
    },

    handleFailedPOST : function(jqXHR, status, errorThrown){
      var pubsub = this.getPubSub();
      var error = (jqXHR.responseJSON && jqXHR.responseJSON.error) ? jqXHR.responseJSON.error : "error unknown";
      var message = 'User update was unsuccessful (' + error + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
    },

    handleFailedGET :  function(jqXHR, status, errorThrown){
      var pubsub = this.getPubSub();
      var error = (jqXHR.responseJSON && jqXHR.responseJSON.error) ? jqXHR.responseJSON.error : "error unknown";
      var message = 'Unable to retrieve information (' + error + ')';
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
    },

   fetchData : function(target){
      return this.composeRequest({target: target, method : "GET"});
    },

    /*POST data to endpoint: accessible through facade*/
    postData: function (target, data, options) {

      if (this.additionalParameters[target]){
        _.extend(data, this.additionalParameters[target]);
      }
      return this.composeRequest({target : target, method : "POST", data : data, options : options});
    },

    /*PUT data to pre-existing endpoint: accessible through facade */
    putData: function (target, data, options) {

      if (this.additionalParameters[target]){
        _.extend(data, this.additionalParameters[target]);
      }
      return this.composeRequest({target : target, method : "PUT", data : data, options : options });
    },

    /*
     * every time a csrf token is required, app storage will request a new token,
     * so it allows you to attach callbacks to the promise it returns
     * */
    sendRequestWithNewCSRF : function(callback){
      callback = _.bind(callback, this);
      this.getBeeHive().getObject("CSRFManager").getCSRF().done(callback);
    },

    /*
     * returns a promise
     * */

    composeRequest : function (config) {

      var target = config.target, method = config.method, data = config.data, options = config.options;
      var endpoint = ApiTargets[target],
        that = this,
        deferred = $.Deferred(),
        request;

      //get data from the relevant model based on the endpoint
      data = data || undefined;
      options = options || {};

      function done(){
        deferred.resolve.apply(undefined, arguments);
      }

      //will have a default fail message for get requests or put/post requests
      function fail(){
        var toCall = method == "GET" ? that.handleFailedGET : that.handleFailedPOST;
        toCall.apply(that, arguments);
        deferred.resolve.apply(undefined, arguments);
      }

      //it came from a form, needs to have a csrf token
      if (options.csrf){

        this.sendRequestWithNewCSRF(function(csrfToken){

          request = new ApiRequest({
            target : endpoint,
            options : {
              context : this,
              type: method,
              data: JSON.stringify(data),
              contentType : "application/json",
              headers : {'X-CSRFToken' :  csrfToken },
              done: done,
              fail : fail
            }
          });

          this.getBeeHive().getService("Api").request(request);
        });
      }

      else {
        request = new ApiRequest({
          target : endpoint,
          options : {
            context : this,
            type: method,
            data: JSON.stringify(data),
            contentType : "application/json",
            done: done,
            fail : fail
          }
        });
        this.getBeeHive().getService("Api").request(request);
      }
      return deferred;
    },

    redirectIfNecessary : function(){
      var pubsub = this.getPubSub();
      if (this.getBeeHive().getObject("MasterPageManager").currentChild === "AuthenticationPage" && this.isLoggedIn()){
        pubsub.publish(pubsub.NAVIGATE, "index-page");
      }
      else  if (this.getBeeHive().getObject("MasterPageManager").currentChild === "SettingsPage" && !this.isLoggedIn()){
        pubsub.publish(pubsub.NAVIGATE, "authentication-page");
      }
    },

    /*set user */
    setUser : function(username){
      this.model.set("user", username);
      if (this.isLoggedIn()){
        this.completeLogIn();
      }
    },

    //this function is called immediately after the login is confirmed
    //get the user's data from myads
    completeLogIn : function(){
      var that = this;
      this.userDataModel.clear();
      this.fetchData("USER_DATA").done(function(data){
        that.userDataModel.set(data);
      });
    },

    //this function is called immediately after the logout is confirmed
    completeLogOut : function(){
      this.model.clear();
      this.userDataModel.clear();
      this.pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, "logout_success");
    },


    // publicly accessible

    deleteAccount : function(){
      var that = this;
      return this.postData("DELETE", {csrf : true}).done(function(){
        that.completeLogOut();
      });
    },

    changeEmail : function(data){

      if (JSON.stringify(_.keys(data)) !== '["email","confirm_email","password"]'){
        throw new Error("changeEmail function wasn't provided with proper information from the form")
      }

      var new_email = data.email;

      function onDone(){
        //publish alert
        function alertSuccess (){
          var message = "Please check <b>" + new_email+ "</b> for further instructions";
          this.pubsub.publish(this.pubsub.ALERT,  new ApiFeedback({code: 0, msg: message, type : "success", title: "Success", modal: true}));
        };
        //need to do it this way so the alert doesnt get lost after page is changed
        this.pubsub.subscribeOnce(this.pubsub.NAVIGATE, _.bind(alertSuccess, this));
        this.getBeeHive().getObject("Session").logout();
      };

      return this.postData("CHANGE_EMAIL", data, {csrf : true}).done(_.bind(onDone, this));
    },

    changePassword : function(data){

      if (JSON.stringify(_.keys(data)) !== '["old_password","new_password1","new_password_2"]'){
        throw new Error("changePassword function wasn't provided with proper information from the form");
      }

      return this.postData("CHANGE_PASSWORD", data, {csrf : true});
    },

    //returns a promise
    getToken : function(){
      return this.fetchData("TOKEN");
    },

    generateToken : function(){
      return this.putData("TOKEN", {}, {csrf : true});
    },

    getUserData : function(){
        return this.userDataModel.toJSON();
    },

    /*
     * POST an update to the myads user_data endpoint
     * (success will automatically update the user object's model of myads data)
     * */

    setUserData : function(data){
      var that = this;
      return this.postData("USER_DATA", data).done(function(data){
        that.userDataModel.set(data);
      });
    },

    getUserName : function(){
        return this.model.get("user");
    },

    isLoggedIn : function(){
        return !!this.model.get("user");
    },

    /*
    * this function queries the myads open url configuration endpoint
    * and returns a promise that it resolves with the data
    * (it is only needed by the preferences widget)
    * */

    getOpenURLConfig : function(){
      var deferred = $.Deferred();

      function done (data){
        deferred.resolve(data);
      }
      function fail(data){
        deferred.reject(data);
      }
       var request = new ApiRequest({
          target : ApiTargets["OPENURL_CONFIGURATION"],
          options : {
            type: "GET",
            done: done,
            fail: fail
          }
        });

     this.getBeeHive().getService("Api").request(request);
     return deferred.promise();
    },

    hardenedInterface: {
      setUser : "set username into user",
      isLoggedIn: "whether the user is logged in",
      getUserName: "get the user's email before the @",
      isOrcidModeOn : "figure out if user has Orcid mode activated",
      setOrcidMode : "set orcid ui on or off",
      getOpenURLConfig : "get list of openurl endpoints",
      getUserData : "myads data",
      setUserData : "POST user data to myads endpoint",
      generateToken : "PUT to token endpoint to make a new token",
      getToken : "GET from token endpoint",
      deleteAccount : "POST to delete account endpoint",
      changePassword : "POST to change password endpoint",
      changeEmail : "POST to change email endpoint"
    }

  });

  _.extend(User.prototype, Hardened, Dependon.BeeHive, Dependon.App, Dependon.PubSub);

  return User;

});