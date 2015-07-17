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

  var passwordRegex = /(?=.*\d)(?=.*[a-zA-Z]).{5,}/;

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
        pattern : passwordRegex,
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

    validation: {
      old_password: {
        required: true,
        pattern : passwordRegex,
        msg: "(A valid password is required)"
      },
      new_password1: {
        required: true,
        pattern : passwordRegex,
        msg: "(A valid password is required)"

      },
      new_password2: {
        required: true,
        equalTo: 'new_password1',
        pattern : passwordRegex,
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

  ChangeTokenModel = Backbone.Model.extend({

    defaults : function(){
      return {
        access_token : undefined
      }
    }

  });

  ChangeTokenView = Marionette.ItemView.extend({

    template : TokenTemplate,
    className : "change-token",
    triggerSubmit: FormFunctions.triggerSubmit,

    events : {
      "click button[type=submit]" : "triggerSubmit"
    },

    // a promise could put the token in the model
    // after render has already been called by "show"
    modelEvents : {
      "change:access_token" : "render"
    }

  });

  var DeleteAccountView, DeleteAccountModel;

  DeleteAccountModel = FormModel.extend({

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
      var config = Marionette.getOption(this, "config"),
          viewModel = new config[subView].model(),
          viewToShow = new config[subView].view({model : viewModel}),
          that = this;

      //cache the subView
      this.model.set("subView", subView);

      if (subView == "token") {

        if (this.model.get("access_token")){
          viewModel.set("access_token", this.model.get("access_token"));
        }
        else {
            this.getToken().done(function(data){
             //keep for next time
            that.model.set("access_token", data.access_token);
             //set in current viewmodel
            viewModel.set("access_token", data.access_token);
          });
        }
      }

      else if (subView == "email") {
        viewModel.set("user", this.model.get("user"));
      }

      this.listenToOnce(viewToShow, "submit-form", this.forwardSubmit);
      this.content.show(viewToShow);

  },

    //special success views
    showPasswordSuccessView : function(){
      this.content.show(new SuccessView({title : "Password Changed" , message : "Next time you log in, please use your new password"}));
    },

    forwardSubmit : function(model){
      this.trigger("submit-form", model);
    }

  });

UserSettingsModel = Backbone.Model.extend({

  defaults : function(){
    return {
      user: undefined,
      token: undefined,
      subView : undefined
    }
  }

})

UserSettings = BaseWidget.extend({

  config : {
    "email" : {view: ChangeEmailView, model : ChangeEmailModel },
    "password" : {view: ChangePasswordView, model : ChangePasswordModel},
    "token" : {view: ChangeTokenView, model : ChangeTokenModel },
    "delete" : {view: DeleteAccountView, model: DeleteAccountModel }
  },

  initialize : function(options){
      options = options || {};

      this.model = new UserSettingsModel();
      this.view = new UserSettingsView({model : this.model,
        config : this.config
      });
    BaseWidget.prototype.initialize.apply(this, arguments);

    },

   setSubView : function(subView){
     if (_.isArray(subView)){
       //XXX:figure out why array
       subView = subView[0];
     }
     //call this regardless of whether subView changed
     this.view.setSubView(subView);
   },

   activate : function(beehive) {
     var that = this;
     this.beehive = beehive;
     this.pubsub = beehive.Services.get('PubSub');
     _.bindAll(this, ["handleUserAnnouncement"]);
     this.pubsub.subscribe(this.pubsub.USER_ANNOUNCEMENT, this.handleUserAnnouncement);
     this.view.getToken = function() {
       return that.beehive.getObject("User").getToken();
     }
   },

    viewEvents : {
      "submit-form" : "submitForm"
    },

  handleUserAnnouncement: function(announcement, arg1){

    if (announcement == "user_signed_in"){
      this.model.set("user", arg1)
    }
    else if (announcement == "user_signed_out"){
      this.model.clear();
    }

  },

   submitForm : function(model){

     var UserObject = this.beehive.getObject("User"), that = this;

     if  (model instanceof this.config.token.model) {
       UserObject.generateToken().done(function (data) {
         that.model.set("access_token", data.access_token);
         //show new token view with new token
         that.setSubView("token");
       })
     }

     else if (model instanceof this.config.delete.model) {
       UserObject.deleteAccount();
     }

     else if (model instanceof this.config.email.model) {
       UserObject.changeEmail(model.toJSON());
     }

     else if (model instanceof this.config.password.model) {
       UserObject.changePassword(model.toJSON())
         .done(function(){
           that.view.showPasswordSuccessView();
         });
     }
   }

  });

  return UserSettings

});
