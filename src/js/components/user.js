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

 var SettingsModel, UserModel, Config, UserCollection, User;

// user.model; stores any variables disconnected from accounts endpoints
//stored in Persistent Storage for now
 SettingsModel = Backbone.Model.extend({
    defaults : function(){
      return {
        isOrcidModeOn : false
      }
    }
  });

//for the User Collection
 UserModel = Backbone.Model.extend({

    idAttribute : "target"
    });

  //all the targets that the user object needs to know about for storing data in its collection
  //the endpoint is the key in api_targets
  //this doesn't have the targets necessary to post data to (e.g. "change_password")
  Config = [
    {target : "TOKEN"},
    {target : "USER"}
    ];

  UserCollection = Backbone.Collection.extend({

    model : UserModel,

    initialize : function(Config, options){
      if (!Config){
        throw new Error("no user endpoints provided");
      }
    }
  });

  User = GenericModule.extend({

    initialize : function(options){
      //model is for settings that don't have an accounts target
      this.model = new SettingsModel();
      //each entry in the collection corresponds to an target
      this.collection = new UserCollection(Config);

      _.bindAll(this, "completeLogIn", "completeLogOut");

      this.listenTo(this.collection, "change", this.broadcastChange);
     this.listenTo(this.collection, "reset", this.broadcastReset);

      //set base url, currently only necessary for change_email endpoint
      if (!this.test){
        this.base_url = location.origin;
      }
      else {
        this.base_url = "location.origin"
      }

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
      var pubsub = this.getPubSub();

      if (_.has(this.model.changedAttributes(), "isOrcidModeOn")){
        pubsub.publish(this.key, pubsub.USER_ANNOUNCEMENT, "orcidUIChange", this.model.get("isOrcidModeOn"));
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
        storage.set('UserPreferences', this.model.attributes);
      }
      this.setPubSub(beehive);
    },

    /* general functions */

    //every time something in the collection changes
    // tell subscribing widgets that something has changed, and tell them which
    // endpoint the change belonged to
    //finally, check if logged in, might have to redirect to auth page/settings page
    broadcastChange : function(model){
      this.getPubSub().publish(this.pubsub.USER_ANNOUNCEMENT, "user_info_change", model.get("target"));
      this.redirectIfNecessary();
    },
    broadcastReset : function(){
      _.each(this.collection.pluck("target"), function(t){
        this.getPubSub().publish(this.pubsub.USER_ANNOUNCEMENT, "user_info_change", t);
      },this);
      this.redirectIfNecessary();
    },

    handleSuccessfulGET : function(response, status, jqXHR){
      var target = jqXHR.target;
      this.collection.get(target).set(response);
      //the change event on the collection should notify widgets
    },

    handleFailedGET : function(jqXHR, status, errorThrown){
      // sanity check : if user target is unauthorized, the user isn't actually logged in, so clear the model
      // not sure if this gets all cases
      if (jqXHR.target === "USER" && jqXHR.status === 401){
        this.completeLogOut();
      }
      this.getPubSub().publish(this.pubsub.USER_ANNOUNCEMENT, "data_get_unsuccessful", jqXHR.target);
    },

    // so success post handler can call the right callback depending on the target
    callbacks : {
      "TOKEN" : function changeTokenSuccess(response, status, jqXHR){
        this.collection.get("TOKEN").set(response);
        this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, "data_post_successful", "TOKEN");
      },
      "CHANGE_EMAIL" : function ChangeEmailSuccess(response, status, jqXHR){
        this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, "data_post_successful", "CHANGE_EMAIL");
      },
      "CHANGE_PASSWORD" : function changePasswordSuccess(response, status, jqXHR){
        this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, "data_post_successful", "CHANGE_PASSWORD");
      },
      "DELETE" : function deleteAccountSuccess(response, status, jqXHR){
        this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, "delete_account_successful", "DELETE");
        this.completeLogOut();
      }
    },

    buildAdditionalParameters : function() {
      //any extra info that needs to be sent in post or get requests
      //but not known about by the widget models goes here
      //this will be called by user.initialize
      var additional = {};
          additional.CHANGE_EMAIL = { verify_url : this.base_url + "/#user/account/verify/change-email"};

      this.additionalParameters = additional;
    },

    handleSuccessfulPOST : function(response, status, jqXHR) {
      var target = jqXHR.target;
      var bound = _.bind(this.callbacks[target], this, response, status, jqXHR);
      bound();
    },

    handleFailedPOST : function(jqXHR, status, errorThrown){
      var target = jqXHR.target;
      var pubsub = this.getPubSub();
      var error = (jqXHR.responseJSON && jqXHR.responseJSON.error) ? jqXHR.responseJSON.error : "error unknown";

      var message = 'User update was unsuccessful (' + error + ')';
      pubsub.publish(pubsub.USER_ANNOUNCEMENT, "data_post_unsuccessful", target);
      pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
    },

   fetchData : function(target){
      this.composeRequest(target, "GET");
    },

    /*post data to endpoint: accessible through facade (1 of 3)*/
    postData: function (target, data) {
      //make sure it has a callback to access later
      if (!this.callbacks[target]){
        throw new Error("a post request was made that doesn't have a success callback");
      }
      if (this.additionalParameters[target]){
        _.extend(data, this.additionalParameters[target]);
      }
      this.composeRequest(target, "POST", data);
    },

    /*return read-only copy of user model(s) for widgets: accessible through facade(2 of 3)*/
    getUserData : function(target){
      var data = {}, collection;
      if (target){
        data = _.omit(this.collection.get(target).toJSON(), "target");
        return JSON.parse(JSON.stringify(data));
      }
      else {
        /*return a data structure with the keys as the target title
         (e.g. "Target") and the values all values for the target, with the target itself removed
         */
        collection = this.collection.toJSON();
        _.each(collection, function(c){
          data[c.target] = _.omit(c, "target");
        });
        return JSON.parse(JSON.stringify(data));
      }
    },

    getUserName : function(){
        var full = this.collection.get("USER").get("user");
        return full;
    },

    isLoggedIn : function(){
        return !!this.collection.get("USER").get("user");
    },

    composeRequest : function (target, method, data, done, fail) {
      var request, endpoint;
      //using "endpoint" to mean the actual url string
      endpoint = ApiTargets[target];
      //get data from the relevant model based on the endpoint
      data = data || undefined;
      //allow caller to provide a done method if desired, otherwise go with the standard ones
      done = done || (method == "GET" ? this.handleSuccessfulGET : this.handleSuccessfulPOST);
      fail = fail || (method == "GET" ? this.handleFailedGET : this.handleFailedPOST);

      request = new ApiRequest({
        target : endpoint,
        options : {
          context : this,
          type: method,
          data: JSON.stringify(data),
          contentType : "application/json",
          done: done,
          fail : fail,
          //record the endpoint & data
          beforeSend: function(jqXHR, settings) {
            jqXHR.target = target;
            jqXHR.data = data;
          }
        }
      });
      this.getBeeHive().getService("Api").request(request);
    },

    //check if  logged in/logged out state has changed
    redirectIfNecessary : function(){
      var pubsub = this.getPubSub();
      if (this.getBeeHive().getObject("MasterPageManager").currentChild === "AuthenticationPage" && this.isLoggedIn()){
        pubsub.publish(pubsub.NAVIGATE, "index-page");
      }
      else  if (this.getBeeHive().getObject("MasterPageManager").currentChild === "SettingsPage" && !this.isLoggedIn()){
        pubsub.publish(pubsub.NAVIGATE, "authentication-page");
      }
    },

    //this function is called immediately after the login is confirmed
    completeLogIn : function(){
        //fetch all user data
        var targets = this.collection.pluck("target");
        _.each(targets, function(e){
          this.fetchData(e);
        }, this);
    },

    setUser : function(username){
      this.collection.get("USER").set("user", username);
      //fetch rest of data
      this.completeLogIn();
    },

   //this function is called immediately after the logout is confirmed
    completeLogOut : function(){
      // should the setting model be cleared?
      this.model.clear();
      //this clears the username at target = "USER", which is the cue that we are no longer signed in
      this.collection.reset(Config);
    },

    hardenedInterface: {
      completeLogIn : "sync user object with database",
      completeLogOut: "clear user object",
      isLoggedIn: "whether the user is logged in",
      postData: "POST new values to user endpoint (params: endpoint, data)",
      getUserData: "get a copy of user data currently in the model for an endpoint, or all user data (params: optional endpoint)",
      getUserName: "get the user's email before the @",
      isOrcidModeOn : "figure out if user has Orcid mode activated",
      setOrcidMode : "set orcid ui on or off",
      setUser : "set the username to log the user in and fetch his/her info"
    }

  });

  _.extend(User.prototype, Hardened, Dependon.BeeHive, Dependon.App, Dependon.PubSub);

  return User;

});