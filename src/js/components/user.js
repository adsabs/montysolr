/*
 * A generic class that lazy-loads User info
 *
 *
 * there are only three USER_ANNOUNCEMENT events that widgets can subscribe to:
 *
 * user.USER_SIGNED_IN: (third argument is username)
 * user.USER_SIGNED_OUT: (third argument is undefined)
 * user.USER_INFO_CHANGE: (third argument is dict of changed values)
 *
 * the user has two models:
 *
 * userModel represents the logged-in user and
 * stores values that should be synced with the user account,
 *
 * localStorageModel represents all values stored in local storage,
 * right now this is limited to orcidModeOn
 *
 */
define([
    'backbone',
    'js/components/api_request',
    'js/components/api_targets',
    'js/components/generic_module',
    'js/mixins/dependon',
    'js/mixins/hardened',
    'js/components/api_feedback',
    'js/mixins/api_access'

  ],
  function(
    Backbone,
    ApiRequest,
    ApiTargets,
    GenericModule,
    Dependon,
    Hardened,
    ApiFeedback,
    ApiAccess
    ) {

    var LocalStorageModel, UserModel, User;

//any variable that doesn't track with user accounts
    LocalStorageModel = Backbone.Model.extend({
      defaults : function(){
        return {
          isOrcidModeOn : false,
          //eventually propagate this to user account
          perPage : undefined,
        }
      }
    });

   //this data reflects user accounts
    UserModel = Backbone.Model.extend({
      defaults : function(){
        return {
          user : undefined,
          link_server : undefined
        }
      }
    });


    User = GenericModule.extend({

      initialize : function(options){
       //this model is for settings that come from PersistentStorage service
        this.localStorageModel = new LocalStorageModel();
        this.listenTo(this.localStorageModel, "change", this.broadcastUserDataChange);

        //this model is for settings that come from user accounts
        this.userModel = new UserModel();
        this.listenTo(this.userModel, "change:user", this.broadcastUserChange);

        this.listenTo(this.userModel, "change", function(key){
          if (key !== "user") this.broadcastUserDataChange.apply(this, arguments);
        });

        _.bindAll(this, "completeLogIn", "completeLogOut");

        //set base url, currently only necessary for change_email endpoint
        this.base_url = this.test ? "location.origin" : location.origin;

        this.buildAdditionalParameters();
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);

        // Check if the Orcid access was revoked by the user
        // It will only be considered 'revoked' if a 401 is received by the API handler
        var checkOrcidToken = function() {
          var orcidApi = beehive.getService('OrcidApi');
          var user = beehive.getObject('User');

          // Only run if there is actually authData stored in the user object
          if (orcidApi.authData !== null) {
            orcidApi.checkAccessOrcidApiAccess().fail(function() {
              var status_code = arguments[0][0].status;
              if (status_code == 401) {
                orcidApi.signOut();
                user.setOrcidMode(0);
              }
            });
          }
        };
        this.getPubSub().subscribe(this.getPubSub().APP_STARTING, checkOrcidToken);

        var storage = beehive.getService('PersistentStorage');
        if (storage) {
          var prefs = storage.get('UserPreferences');
          if (prefs) {
            this.localStorageModel.set(prefs);
          }
        }
      },


      //general persistent storage
      _persistModel: function() {

        var storage  = this.getBeeHive().getService('PersistentStorage');
        if (storage) {
          storage.set('UserPreferences', this.localStorageModel.toJSON());
        }
      },

      /*generic set localStorage function*/
      setLocalStorage : function(obj){
        this.localStorageModel.set(obj);
        this._persistModel();
      },

      /*generic get localStorage function, gives you everything*/
      getLocalStorage : function(obj){
        return this.localStorageModel.toJSON();
      },

      /* orcid functions */
      setOrcidMode : function(val){
        this.localStorageModel.set("isOrcidModeOn", val);
        this._persistModel();
      },

      isOrcidModeOn : function(){
        return this.localStorageModel.get("isOrcidModeOn");
      },

      /* general functions */

      //tell widgets that data changed, and send them that data
      broadcastUserDataChange : function(changedModel){
        this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, this.USER_INFO_CHANGE, changedModel.changed);
      },

      broadcastUserChange : function(){
        //user has signed in or out
        var message = this.userModel.get("user") ? this.USER_SIGNED_IN : this.USER_SIGNED_OUT;
        this.getPubSub().publish(this.getPubSub().USER_ANNOUNCEMENT, message, this.userModel.get("user"));
        this.redirectIfNecessary();
      },

      buildAdditionalParameters : function() {
        //any extra info that needs to be sent in post or get requests
        //but not known about by the widget models goes here
        var additional = {};
        additional.CHANGE_EMAIL = { verify_url : this.base_url + "/#user/account/verify/change-email"};
        this.additionalParameters = additional;
      },

      handleFailedPOST : function(jqXHR, status, errorThrown, target){
        var pubsub = this.getPubSub(), error;

        if (jqXHR.responseJSON && jqXHR.responseJSON.error){
          error = jqXHR.responseJSON.error;
        } else if (jqXHR.responseText){
          error = jqXHR.responseText;
        } else {
          error = "error unknown";
        }

        var message = 'Unable to update information for endpoint ' + target + ' (' + error + ')' ;
        pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
      },

      handleFailedGET :  function(jqXHR, status, errorThrown, target){
        var pubsub = this.getPubSub(), error;

        if (jqXHR.responseJSON && jqXHR.responseJSON.error){
          error = jqXHR.responseJSON.error;
        } else if (jqXHR.responseText){
          error = jqXHR.responseText;
        } else {
          error = "error unknown";
        }

        var message = 'Unable to retrieve information for endpoint ' + target + ' (' + error + ')' ;
        pubsub.publish(pubsub.ALERT, new ApiFeedback({code: 0, msg: message, type : "danger"}));
      },

      fetchData : function(target){
        return this.composeRequest({target: target, method : "GET"});
      },

      /*
       * POST data to endpoint: accessible through facade
       *
       * */
      postData: function (target, data) {

        if (this.additionalParameters[target]){
          _.extend(data, this.additionalParameters[target]);
        }
        return this.composeRequest({target : target, method : "POST", data : data });
      },

      /*PUT data to pre-existing endpoint: accessible through facade */
      putData: function (target, data) {

        if (this.additionalParameters[target]){
          _.extend(data, this.additionalParameters[target]);
        }
        return this.composeRequest({target : target, method : "PUT", data : data });
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

        var target = config.target,
          method = config.method,
          data = config.data || {},
          csrf = data.csrf,
        //don't want to send this to the endpoint
          data = _.omit(data, "csrf");

        var endpoint = ApiTargets[target],
          that = this,
          deferred = $.Deferred(),
          requestData;

        function done() {
          deferred.resolve.apply(undefined, arguments);
        }

        //will have a default fail message for get requests or put/post requests
        function fail() {
          var toCall = method == "GET" ? that.handleFailedGET : that.handleFailedPOST;
          var argsWithTarget = [].slice.apply(arguments);
          argsWithTarget.push(target);
          toCall.apply(that, argsWithTarget);
          deferred.fail.apply(undefined, arguments);
        }

        requestData = {
          target : endpoint,
          options : {
            context : this,
            type: method,
            data: !_.isEmpty(data) ? JSON.stringify(data) : undefined,
            contentType : "application/json",
            done: done,
            fail : fail
          }
        };

        //it came from a form, needs to have a csrf token
        if (csrf){

          this.sendRequestWithNewCSRF(function(csrfToken){
            requestData.options.headers =  {'X-CSRFToken' :  csrfToken };
            this.getBeeHive().getService("Api").request(new ApiRequest(requestData));
          });
        }

        else {
          this.getBeeHive().getService("Api").request(new ApiRequest(requestData));
        }

        return deferred;
      },

      redirectIfNecessary : function(){
        var pubsub = this.getPubSub();

        //redirect user to wherever they were before authentication page
        if (this.getBeeHive().getObject("MasterPageManager").currentChild === "AuthenticationPage" && this.isLoggedIn()){
          //so that navigator can redirect to the proper page
          var previousNav = this.getBeeHive().getService("HistoryManager").getPreviousNav();
          pubsub.publish.apply(pubsub, [pubsub.NAVIGATE].concat(previousNav));
        }
        else  if (this.getBeeHive().getObject("MasterPageManager").currentChild === "SettingsPage" && !this.isLoggedIn()){
          pubsub.publish(pubsub.NAVIGATE, "authentication-page");
        }
      },

      /*set user */
      setUser : function(username){
        this.userModel.set("user", username);
        if (this.isLoggedIn()){
          this.completeLogIn();
        }
      },

      //this function is called immediately after the login is confirmed
      //get the user's data from myads
      completeLogIn : function(){
        var that = this;
        this.fetchData("USER_DATA").done(function(data){
          that.userModel.set(data);
        });
      },

      //this function is called immediately after the logout is confirmed
      completeLogOut : function(){
        this.userModel.clear();
      },

      // publicly accessible

      deleteAccount : function(){
        var that = this;
        return this.postData("DELETE", {csrf : true}).done(function(){
          that.getApiAccess({reconnect: true}).done(function(){
            that.completeLogOut();
          });
        });
      },

      changeEmail : function(data){

        var new_email = data.email;

        function onDone(){
          //publish alert
          function alertSuccess (){
            var message = "<p>Please check <b>" + new_email+ "</b> for further instructions</p><p>(If you don't see the email, please <b>check your spam folder</b>)</p>";
            this.getPubSub().publish(this.getPubSub().ALERT,  new ApiFeedback({code: 0, msg: message, type : "success", title: "Success", modal: true}));
          };
          //need to do it this way so the alert doesnt get lost after page is changed
          this.getPubSub().subscribeOnce(this.getPubSub().NAVIGATE, _.bind(alertSuccess, this));
          this.getBeeHive().getObject("Session").logout();
        };

        data = _.extend(data, {csrf : true});

        return this.postData("CHANGE_EMAIL", data).done(_.bind(onDone, this));
      },

      changePassword : function(data){

        data = _.extend(data, {csrf : true});
        return this.postData("CHANGE_PASSWORD", data);

      },

      //returns a promise
      getToken : function(){
        return this.fetchData("TOKEN");
      },

      generateToken : function(){
        return this.putData("TOKEN", {csrf : true});
      },

      getUserData : function(){
        return this.userModel.toJSON();
      },

      /*
       * POST an update to the myads user_data endpoint
       * (success will automatically update the user object's model of myads data)
       * */

      setUserData : function(data){
        var that = this;
        return this.postData("USER_DATA", data).done(function(data){
          that.userModel.set(data);
        });
      },

      getUserName : function(){
        return this.userModel.get("user");
      },

      isLoggedIn : function(){
        return !!this.userModel.get("user");
      },


      /*
       * POST an update to the myads user_data endpoint
       * (success will automatically update the user object's model of myads data)
       * */

      setMyADSData : function(data) {
        return this.postData("USER_DATA", data);
      },


      /*
       * this function queries the myads open url configuration endpoint
       * and returns a promise that it resolves with the data
       * (it is only needed by the preferences widget)
       * */


      getOpenURLConfig : function(){
        return this.getSiteConfig('link_servers');
      },

      getSiteConfig: function(key) {
        var deferred = $.Deferred();

        function done (data){
          deferred.resolve(data);
        };

        function fail(data){
          deferred.reject(data);
        };

        var request = new ApiRequest({
          target : key ? ApiTargets["SITE_CONFIGURATION"] + '/' + key : ApiTargets["SITE_CONFIGURATION"],
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
        setLocalStorage : "sets an object in to user's local storage",
        getLocalStorage : "gives you a json object for user's local storage",
        isOrcidModeOn : "figure out if user has Orcid mode activated",
        setOrcidMode : "set orcid ui on or off",
        getOpenURLConfig : "get list of openurl endpoints",
        getUserData : "myads data",
        setUserData : "POST user data to myads endpoint",
        generateToken : "PUT to token endpoint to make a new token",
        getToken : "GET from token endpoint",
        deleteAccount : "POST to delete account endpoint",
        changePassword : "POST to change password endpoint",
        changeEmail : "POST to change email endpoint",
        setMyADSData : "",
        USER_SIGNED_IN: 'constant',
        USER_SIGNED_OUT: 'constant',
        USER_INFO_CHANGE: 'constant'
      }

    });

    _.extend(User.prototype, Hardened, Dependon.BeeHive, Dependon.App, ApiAccess, {
      USER_SIGNED_IN: 'user_signed_in',
      USER_SIGNED_OUT: 'user_signed_out',
      USER_INFO_CHANGE: 'user_info_change'
    });

    return User;

  });