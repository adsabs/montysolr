define([
  "underscore"
], function(
  _

  ){



  //some functions to be used by form views which auto-validate
  var formFunctions = {

    //for the view
    checkValidationState: function () {
      //hide help
      if (this.model.isValidSafe()) {

        this.$("button[type=submit]")
          .prev(".help-block")
          .html("")
          .addClass("no-show");

      }

    },

    //for the view
    //when someone clicks on submit button
    //parent views/controllers need to listen for "submit-form" event
    triggerSubmit: function (e) {
      e.preventDefault();
      // (only show error messages if submit == true), so once user has unsuccessfully
      // submitted 1 time
      //don't need to reset because view will be disposed of after successful submit event
      this.submit = true;
      if (this.model.isValid(true)) {
        var working = '<i class="fa fa-lg fa-spinner fa-pulse"></i> Working...'
        this.trigger("submit-form", this.model);
        this.$("button[type=submit]")
          .prev(".help-block")
          .addClass("no-show");
        this.$("button[type=submit]")
          .removeClass("btn-success")
          .addClass("disabled")
          .html(working);
      }
      else {
        this.$("button[type=submit]")
          .prev(".help-block")
          .removeClass("no-show")
          .html("Fields missing or incomplete")
      }
    },

    //for the view, to be called onRender
    activateValidation: function () {
      Backbone.Validation.bind(this, {
        forceUpdate: true
      });
      this.stickit();
    },

    //for the model
    //a way to validate all fields without causing invalid states for empty fields
    isValidSafe: function () {
      //check everything that has required=true before you do this.isValid(), and make sure it isn't empty
      //otherwise the way it is set up, the form will show invalid markers for unentered fields
      var allRequired = true;
      _.each(this.validation, function (v, k) {
        if (v.required && !this.get(k)) {
          allRequired = false;
        }
      }, this);

      if (allRequired && this.isValid(true)) {
        return true
      }
    },

    //for the model, if it has a validation hash from backbone-validation
    //right now, useful only for user setting models that combine user-entered info and info from the server
    reset: function () {
      var valKeys = _.keys(this.validation);
      _.each(this.attributes, function (v,k) {
        if (_.contains(valKeys, k)) {
          this.unset(k, {silent: true});
        }
      }, this);
    }
  }


  return formFunctions

})
