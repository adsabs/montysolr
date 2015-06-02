define([
  'marionette',
  'js/widgets/base/base_widget',
  'js/mixins/form_view_functions',
  'js/widgets/success/view',
  'js/components/api_feedback',
  'hbs!./templates/api_key',
  'hbs!./templates/change_email',
  'hbs!./templates/change_password',
  'hbs!./templates/delete_account',
  'backbone-validation',
  'backbone.stickit',
  'bootstrap'

], function(
  Marionette,
  BaseWidget,
  FormFunctions,
  SuccessView,
  ApiFeedback,
  TokenTemplate,
  EmailTemplate,
  PasswordTemplate,
  DeleteAccountTemplate,
  Bootstrap
  ){

  var FormView, FormModel;

  FormView = Marionette.ItemView.extend({

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


  var ChangeEmailView, ChangeEmailModel;

  ChangeEmailModel = FormModel.extend({

    target : "CHANGE_EMAIL",

    validation: {
      email: {
        required: true,
        pattern: "email",
        msg: "(A valid email is required)"
      },
      "confirm-email" : {
        required: true,
        pattern: "email",
        equalTo: 'email',
        msg: "(This email must be valid and match the one above)"
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
      "input[name=confirm-email]": {observe: "confirm-email",
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
        pattern : /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/,
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
    target : "TOKEN",
    /*otherwise submit will call the user's "postData" method*/
    PUT : true
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

  var UserSettingsView, UserSettings;


  UserSettingsView = Marionette.LayoutView.extend({

    initialize : function(options){
      options = options ||{};
      this.views = {};

      _.each(Marionette.getOption(this, "viewConfig"), function(v,k){
          this.views[k] = v;
        },this);
    },

    template : function(){return "<div class=\"content-container\"></div>"},

    className : "s-user-settings s-form-widget",

    regions : {
      content : ".content-container"
    },

    events : {
      "click .user-pill-nav a:not(.dropdown-toggle)" : "stopPropagation"
    },

    stopPropagation : function(){
      return false
    },

    setSubView : function(subView) {
      var View = this.getCurrentViewConstructor(subView);
      var model = this.getCurrentModel(subView);
      var viewToShow = new View({model : model});

      this.listenToOnce(viewToShow, "submit-form", this.forwardSubmit);
      this.content.show(viewToShow);
  },

    getCurrentModel : function(name){
      var model = Marionette.getOption(this, "subViewModels")[name];
      return model;
    },

    getCurrentViewConstructor : function(name) {
      var View = this.views[name];
      return View
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

  /* use the pathname to identify (found in user nav template */

   viewConfig : {
     //each time it needs to be shown, a new view is created
     "email" : ChangeEmailView,
     "password" : ChangePasswordView,
     "token" : ChangeTokenView,
     "delete" : DeleteAccountView
   },

   //only one of each of these models exists for the life of the app
   modelConfig : {
    "email" : ChangeEmailModel,
    "password" : ChangePasswordModel,
    "token" : ChangeTokenModel,
    "delete" : DeleteAccountModel
  },

  initialize : function(options){
      options = options || {};

      this.subViewModels = {};
    //create and store the models
      _.each(this.modelConfig, function(v,k) {
        var instanceName = k[0].toLowerCase() + k.slice(1);
        this.subViewModels[instanceName] = new v();
      },this);

    //parent layout iew
      this.view = new UserSettingsView({model : this.model,
        subViewModels : this.subViewModels,
        viewConfig : this.viewConfig
      });

    BaseWidget.prototype.initialize.apply(this, arguments);
    },

   //only called by navigator
   setSubView : function(subView){
     if (_.isArray(subView)){
       //XXX:figure out why array
       subView = subView[0];
     }
     this.view.setSubView(subView)
   },

   activate : function(beehive) {
     this.beehive = beehive;
     this.pubsub = beehive.Services.get('PubSub');

     _.bindAll(this, ["handleUserAnnouncement", "handleOutsideNavigate"]);
     this.pubsub.subscribe(this.pubsub.USER_ANNOUNCEMENT, this.handleUserAnnouncement);
     this.pubsub.subscribe(this.pubsub.NAVIGATE, this.handleOutsideNavigate);
   },

    viewEvents : {
      "submit-form" : "submitForm"
    },

  handleOutsideNavigate : function(pageName){

    if (this.modelsHaveData()){
      this.resetModels();
    }
  },

  modelsHaveData: function(){
    var hasData = false;
    var models = _.values(this.subViewModels);
    _.each(models, function(m){
      if (!_.isEmpty(m.toJSON())){
        //might be a default val not inputed by user
        if (_.keys(m.toJSON()).toString()== "user" ||_.keys(m.toJSON()).toString() == "token" ){
          return
        }
        else {
          hasData = true;
        }
      }
    })
    return hasData;
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
           var new_email = this.subViewModels.email.get("email");
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
     if (model.PUT){
       user.putData(target, model.toJSON(), {csrf : true});
     }
     else {
       user.postData(target, model.toJSON(), {csrf : true});
     }
   },

   // this function puts the data from user object into the correct models
   getUserData :function(){
     var data = this.beehive.getObject("User").getUserData();

     //set the data into the correct models
     this.subViewModels.token.set(data["TOKEN"]);
     this.subViewModels.email.set(data["USER"]);

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
