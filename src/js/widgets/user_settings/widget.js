define([
  'marionette',
  'js/widgets/base/base_widget',
  'js/mixins/form_view_functions',
  'js/components/api_targets',
  'js/widgets/success/view',
  'js/components/api_feedback',
  'hbs!./templates/api_key',
  'hbs!./templates/change_email',
  'hbs!./templates/change_password',
  'hbs!./templates/preferences',
  'hbs!./templates/user_settings',
  'hbs!./templates/delete_account',
  'hbs!./templates/nav_template',
  'hbs!./templates/header_template',
  'backbone-validation',
  'backbone.stickit',
  'bootstrap'

], function(
  Marionette,
  BaseWidget,
  FormFunctions,
  ApiTargets,
  SuccessView,
  ApiFeedback,
  TokenTemplate,
  EmailTemplate,
  PasswordTemplate,
  PreferencesTemplate,
  UserSettingsTemplate,
  DeleteAccountTemplate,
  NavTemplate,
  HeadingTemplate,
  Bootstrap
  ){

  var FormView, FormModel;

  FormView = Marionette.ItemView.extend({

    activateRecaptcha : FormFunctions.activateRecaptcha,
    activateValidation: FormFunctions.activateValidation,
    checkValidationState : FormFunctions.checkValidationState,
    triggerSubmit : FormFunctions.triggerSubmit,

    modelEvents: {
      "change": "checkValidationState"
    },
    events : {
      "click button[type=submit]" : "triggerSubmit"
    }
  });

  FormModel = Backbone.Model.extend({
    isValidSafe : FormFunctions.isValidSafe,
    reset : FormFunctions.reset
  });

  var ChangePreferencesView, ChangePreferencesModel;

  ChangePreferencesModel = FormModel.extend({

    target : ""

  });

  ChangePreferencesView = FormView.extend({

    template : PreferencesTemplate,

    className : "change-preferences",

    onRender : function(){
      this.activateValidation();
    }
  });

  var ChangeEmailView, ChangeEmailModel;

  ChangeEmailModel = FormModel.extend({

    target : "CHANGE_EMAIL",

    validation: {
      email: {
        required: true,
        pattern: "email",
        msg: "(A valid email is required)"
      },
      password: {
        required: true,
        pattern : /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/,
        msg: "(A valid password is required)"
      }
    }

  });

  ChangeEmailView = FormView.extend({

    template : EmailTemplate,

    className : "change-email",

    triggerSubmit : function(){
      this.model.unset("user");

      //manually close the modal, for some reason just the close markup
      //only works some of the time
      this.$(".modal").modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
      FormFunctions.triggerSubmit.apply(this, arguments);

    },

    //for the view
    //checks whether to show a green submit button on model change
    checkValidationState : function(){
      //hide possible submit button message
      if (this.model.isValidSafe()){
      //don't go for actual submit button, that is on the modal
        this.$("button.initial-submit")
          .addClass("btn-success")
          .prev(".help-block")
          .html("")
          .addClass("no-show");

      }
      else {
        this.$("button.initial-submit")
          .removeClass("btn-success");
      }
    },

    bindings: {
      "input[name=email]": {observe: "email",
        setOptions: {
          validate: true
        }
      },
      "input[name=password]": {observe: "password",
        setOptions: {
          validate: true
        }
      }
    },

    onRender : function(){
      this.activateValidation();
    }

  });

  var ChangePasswordView, ChangePasswordModel;

  ChangePasswordModel = FormModel.extend({

    target : "CHANGE_PASSWORD",

    validation: {
      old_password: {
        required: true,
        pattern : /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/,
        msg: "(A valid password is required)"
      },
      new_password1: {
        required: true,
        pattern : /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/,
        msg: "(A valid password is required)"

      },
      new_password2: {
        required: true,
        equalTo: 'new_password1',
        msg: "(The passwords do not match)"
      }
    }

  });

  ChangePasswordView = FormView.extend({

    template : PasswordTemplate,

    className : "change-password",

    onRender : function(){
      this.activateValidation();
    },

    bindings: {
      "input[name=old_password]": {observe: "old_password",
        setOptions: {
          validate: true
        }
      },
      "input[name=new_password1]": {observe: "new_password1",
        setOptions: {
          validate: true
        }
      },
      "input[name=new_password2]": {observe: "new_password2",
        setOptions: {
          validate: true
        }
      }
    }

  });

  var ChangeTokenView, ChangeTokenModel;

  ChangeTokenModel = FormModel.extend({
    target : "TOKEN"
  });

  ChangeTokenView = FormView.extend({

    template : TokenTemplate,
    className : "change-token"

  });

  var DeleteAccountView, DeleteAccountModel;

  DeleteAccountModel = FormModel.extend({

    target : "DELETE"

  });

  DeleteAccountView = FormView.extend({

    template : DeleteAccountTemplate,
    className : "delete-account",

    triggerSubmit : function(){
      //manually close the modal, for some reason just the close markup
      //only works some of the time
      this.$(".modal").modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
      FormFunctions.triggerSubmit.apply(this, arguments);
    }
  });

  var UserSettingsModel, UserSettingsView, UserSettings;

  UserSettingsModel = Backbone.Model.extend({

    defaults  : function(){
      return {
        user : undefined
      }
    }

  });

  UserSettingsCollection = Backbone.Collection.extend({
    model : Backbone.Model
});

  UserSettingsView = Marionette.Layout.extend({

    initialize : function(options){
      options = options ||{};
      this.views = {};

      _.each(Marionette.getOption(this, "viewConfig"), function(v,k){
          this.views[k] = v;
        },this);
    },

    template : UserSettingsTemplate,
    navTemplate : NavTemplate,
    headingTemplate : HeadingTemplate,
    className : "s-user-settings s-form-widget",

    regions : {
      content : ".content-container"
    },

    serializeData : function(){
      var data = this.model.toJSON();
      data.subViews = this.collection.toJSON();
      return data;
    },

    events : {
      "click .nav-container a" : "changeActive",
      "click .user-pill-nav a:not(.dropdown-toggle)" : "stopPropagation",
      "click .leave-form" : "completeChangeActive"
    },

    collectionEvents : {
      "change:active" : "showView"
    },

    modelEvents : {
      "change:user" : "renderHeading"
    },

    stopPropagation : function(){
      return false
    },

    renderNav : function(){
      this.$(".nav-container").html(this.navTemplate(this.collection.toJSON()));
    },

    renderHeading : function(){
      this.$(".heading-container").html(this.headingTemplate(this.model.toJSON()))
    },

    onRender : function(){
      this.renderHeading();
      this.showView();
    },

    changeActive : function(e){
      e.preventDefault();

      var $current = $(e.currentTarget);
      this.potentialSubPage = $current.data("subpage");

      //do check for uncompleted current form info here
      model = this.collection.findWhere({active : true});
      //uses the active nav model to find the current view's model
      currentModel = this.getCurrentModel(model);

     //check the vals that we're validating, not just everything that might be in the model
    var changedVals = _.values(_.pick(currentModel.attributes, _.keys(currentModel.validation)));

     if (changedVals.join("")){
       //model isn't empty, user has entered something, so show a warning
       this.showConfirmModal();
     }
      else {
       this.completeChangeActive();
     }
    },

    completeChangeActive : function(){
      //reset the current model
      model = this.collection.findWhere({active : true});
      //uses the active nav model to find the current view's model
      currentModel = this.getCurrentModel(model);
      //clears all values that require validation
      currentModel.reset();
      this.trigger("change:subview", this.potentialSubPage);
    },

    getCurrentModel : function(navModel){
      var name = navModel.get("name");
      var model = Marionette.getOption(this, "subViewModels")[name[0].toLowerCase() + name.slice(1) + "Model"];
      return model;
    },

    getCurrentViewConstructor : function(navModel){
      var name = navModel.get("name");
      var viewName = name + "View";
      var View = this.views[viewName];
      return View
    },

    showConfirmModal: function(){
      this.$(".confirm-modal").modal();
    },


    showView : function(model){
      if (!model){
        model = this.collection.findWhere({active : true});
      }
      //only show if the change:active is true, not false
      if (model && model.get("active")) {
        //it's no longer showing a successView
        this.successView = false;
        this.renderNav();
        var View = this.getCurrentViewConstructor(model);
        var model = this.getCurrentModel(model);
        var viewToShow = new View({model : model});

        this.listenToOnce(viewToShow, "submit-form", this.forwardSubmit);
        this.content.show(viewToShow);
      }
    },

    //special success views
    showPasswordSuccessView : function(){
      this.successView = true;
      this.content.show(new SuccessView({title : "Password Changed" , message : "Next time you log in, please use your new password"}));
    },

    forwardSubmit : function(model){
      this.trigger("submit-form", model);
    }

  });

 UserSettings = BaseWidget.extend({

   viewConfig : {
     //each time it needs to be shown, a new view is created
     "ChangePreferencesView" : ChangePreferencesView,
     "ChangeEmailView" : ChangeEmailView,
     "ChangePasswordView" : ChangePasswordView,
     "ChangeTokenView" : ChangeTokenView,
     "DeleteAccountView" : DeleteAccountView,
   },

   //only one of each of these models exists for the life of the app
   modelConfig : {
     "ChangePreferencesModel" : ChangePreferencesModel,
     "ChangeEmailModel" : ChangeEmailModel,
    "ChangePasswordModel" : ChangePasswordModel,
    "ChangeTokenModel" : ChangeTokenModel,
     "DeleteAccountModel" : DeleteAccountModel
  },

    layoutConfig : [
    //where name corresponds to the name of the model/view
    {title : "User Preferences", href : "preferences", name : "ChangePreferences"},
    {title : "Change email", href : "email", name : "ChangeEmail"},
    {title : "Change password", href : "password", name : "ChangePassword"},
    {title : "Api Token", href : "token", name : "ChangeToken"},
    {title : "Delete Account", href : "delete", name : "DeleteAccount"}
    ],

  initialize : function(options){
      options = options || {};

      this.subViewModels = {};
    //create and store the models
      _.each(this.modelConfig, function(v,k) {
        var instanceName = k[0].toLowerCase() + k.slice(1);
        this.subViewModels[instanceName] = new v();
      },this);

    //the main model
      this.model = new UserSettingsModel();

    //navigation collection
      this.navCollection = new UserSettingsCollection(this.layoutConfig);
    //parent layout iew
      this.view = new UserSettingsView({model : this.model,
        collection : this.navCollection,
        subViewModels : this.subViewModels,
        viewConfig : this.viewConfig
      });

    BaseWidget.prototype.initialize.apply(this, arguments);
    },

   //only called by navigator
   setSubView : function(subView){
     this.navCollection.each(function(m){
       m.set("active", false, {silent : true});
     });
     var toShow = this.navCollection.findWhere({href : subView});
     toShow.set("active", true);
   },

   activate : function(beehive) {
     this.beehive = beehive;
     this.pubsub = beehive.Services.get('PubSub');

     _.bindAll(this, ["handleUserAnnouncement"]);
     this.pubsub.subscribe(this.pubsub.USER_ANNOUNCEMENT, this.handleUserAnnouncement);
   },

    viewEvents : {
      "change:subview" : "navigateToSubView",
      "submit-form" : "submitForm"
    },

   navigateToSubView : function(subPage){
     this.pubsub.publish(this.pubsub.NAVIGATE, "settings-page", {subView: subPage});
   },

   /*
   * how to respond after form was submitted and
   * server has replied with either success or fail message
   * */
   handleUserAnnouncement : function(msg, endpoint){
     //the alert should actually provide the fail message
     if (msg === "data_post_successful" ) {
       switch(endpoint){
         case "CHANGE_PASSWORD":
           this.view.showPasswordSuccessView();
           break;
         case "CHANGE_EMAIL":
          //get current email, this will be discarded
          //by the call to "resetModels" below
           var new_email = this.subViewModels.changeEmailModel.get("email");
           //publish alert
           function alertSuccess (){
           var message = "Please check <b>" + new_email+ "</b> for further instructions";
           this.pubsub.publish(this.pubsub.ALERT,  new ApiFeedback({code: 0, msg: message, type : "success", title: "Success", modal: true}));
          };
           //need to do it this way so the alert doesnt get lost after page is changed
           this.pubsub.subscribeOnce(this.pubsub.NAVIGATE, _.bind(alertSuccess, this));
           this.beehive.getObject("Session").logout();
           break;
         case "TOKEN":
          break;
//        case "CHANGE_PREFERENCE":
//           this.changePreferenceSuccess();
//           this.getUserData();
       }
     }
     else if (msg === "data_post_unsuccessful"){
       //should show alert
     }
     else if (msg === "user_info_change"){
       //refresh all models in the widget
     }
     //clear models, then refill with data from user object
     this.resetModels();
   },

   resetModels : function(){
     //reset to clean models with user object info
     _.each(this.subViewModels, function(m){
       m.clear();
     });
     this.getUserData();
   },

   submitForm : function(model){
     var user = this.beehive.getObject("User");
     var target = model.target;
     user.postData(target, model.toJSON());
   },

   // this function puts the data from user object into the correct models
   getUserData :function(){
     var data = this.beehive.getObject("User").getUserData();

     //set the data into the correct models
     this.subViewModels.changeTokenModel.set(data["TOKEN"]);
     this.subViewModels.changeEmailModel.set(data["USER"]);

     //the main view also needs some info
     this.model.set("user", this.beehive.getObject("User").getUserName());

     //re-render whatever the current view is as long as it's not a success view
     if (!this.view.successView){
       this.view.showView();
     }
   },

   /*
   * update user data on widget show event
   * */
   onShow : function(){
    this.getUserData();
   }

  });

  return UserSettings

})