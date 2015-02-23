define([
  'marionette',
  'js/widgets/base/base_widget',
  'js/mixins/form_view_functions',
  'js/components/api_targets',
  'js/widgets/success/view',
  'hbs!./templates/api_key',
  'hbs!./templates/change_email',
  'hbs!./templates/change_password',
  'hbs!./templates/preferences',
  'hbs!./templates/user_settings',
  'hbs!./templates/delete_account',
  'hbs!./templates/nav_template',
  'hbs!./templates/header_template',
  'backbone-validation',
  'backbone.stickit'

], function(
  Marionette,
  BaseWidget,
  FormFunctions,
  ApiTargets,
  SuccessView,
  TokenTemplate,
  EmailTemplate,
  PasswordTemplate,
  PreferencesTemplate,
  UserSettingsTemplate,
  DeleteAccountTemplate,
  NavTemplate,
  HeadingTemplate
  ){

  //this allows for instant validation of form fields using the backbone-validation plugin
  _.extend(Backbone.Validation.callbacks, {
    valid: function (view, attr, selector) {
      var $el = view.$('input[name=' + attr + ']'),
        $group = $el.closest('.form-group');

      $group.removeClass('has-error').addClass("has-success");
      $group.find(".icon-success").removeClass("hidden");
      $group.find('.help-block').html('').addClass('no-show');

    },
    invalid: function (view, attr, error, selector) {
      var $el = view.$('[name=' + attr + ']');
      $group = $el.closest('.form-group');

      $group.removeClass("has-success");
      $group.find(".icon-success").addClass("hidden");

      if (view.submit === true){
        //only show error states if there has been a submit event
        $group.addClass('has-error');
        $group.find('.help-block').html(error).removeClass('no-show');
      }
    }
  });

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
    isValidSafe : FormFunctions.isValidSafe
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

    //override default to get rid of user
    triggerSubmit : function(e){
      this.model.unset("user");
      FormView.prototype.triggerSubmit.apply(this, arguments);
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

    onRender : function(){
      this.activateValidation();
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
      "click .nav-container a" : "changeActive"
    },

    collectionEvents : {
      "change:active" : "showView"
    },

    modelEvents : {
      "change:user" : "renderHeading"
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
      var subPage = $current.data("subpage");
      this.trigger("change:subview", subPage);
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
        var name = model.get("name");
        var viewName = name + "View";
        var View = this.views[viewName];
        var viewToShow = new View({model : Marionette.getOption(this, "subViewModels")[name[0].toLowerCase() + name.slice(1) + "Model" ]});

        this.listenToOnce(viewToShow, "submit-form", this.forwardSubmit);
        this.content.show(viewToShow);
      }
    },

    //special success views
    showPasswordSuccessView : function(){
      this.successView = true;
      this.content.show(new SuccessView({title : "Password Changed" , message : "Next time you log in, please use your new password"}));
    },

    showEmailSuccessView : function(){
      this.successView = true;
      this.content.show(new SuccessView({title : "Email Changed"}));
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
     "DeleteAccountView" : DeleteAccountView
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
      _.each(this.modelConfig, function(v,k) {
        var instanceName = k[0].toLowerCase() + k.slice(1);
        this.subViewModels[instanceName] = new v();
      },this);

      this.model = new UserSettingsModel();

      this.navCollection = new UserSettingsCollection(this.layoutConfig);
      this.view = new UserSettingsView({model : this.model,
        collection : this.navCollection,
        subViewModels : this.subViewModels,
        viewConfig : this.viewConfig
      });

    BaseWidget.prototype.initialize.apply(this, arguments);
    },

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

   handleUserAnnouncement : function(msg, endpoint){
     //the alert should actually provide the fail message
     if (msg === "data_post_successful" ) {
       switch(endpoint){
         case "CHANGE_PASSWORD":
           this.view.showPasswordSuccessView();
           break;
         case "CHANGE_EMAIL":
           this.view.showEmailSuccessView();
           break;
         case "TOKEN":
          this.getUserData();
          break;
//        case "CHANGE_PREFERENCE":
//           this.changePreferenceSuccess();
//           this.getUserData();
       }
     }
     else if (msg === "data_post_unsuccessful"){
     }
     else if (msg === "user_info_change"){
       //refresh all models in the widget
       this.getUserData();
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