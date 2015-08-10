/*
 widgets can attach callbacks to a deferred that waits until
 * grecaptcha is loaded from google, and sitekey info is loaded from discovery.vars.js
 *
 * */
define([
    'backbone',
    'js/components/generic_module',
    'js/mixins/hardened'
  ],
  function(
    Backbone,
    GenericModule,
    Hardened) {

    var grecaptchaDeferred = $.Deferred();

    // this has to be global
    onRecaptchaLoad = function(){
      grecaptchaDeferred.resolve();
    }

    var RecaptchaManager = GenericModule.extend({

      initialize : function(){
        this.grecaptchaDeferred = grecaptchaDeferred;
        this.siteKeyDeferred = $.Deferred();
        this.when = $.when(this.siteKeyDeferred, this.grecaptchaDeferred);
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        var pubsub = this.getPubSub();
        _.bindAll(this, [ "getRecaptchaKey"]);
        pubsub.subscribe(pubsub.APP_STARTED, this.getRecaptchaKey);
      },

      getRecaptchaKey : function(){
        var siteKey = this.getBeeHive().getObject("AppStorage").getConfigCopy().recaptchaKey;
        this.siteKeyDeferred.resolve(siteKey);
      },

      /**
       * widgets use this to attach a callback to the recaptcha promise
       * the callback  will automatically put the completed recaptcha into the view's model
       * view template needs an element with the class of "g-recaptcha" for this to work
       *
       * @param view to render recaptcha on
       */
      activateRecaptcha : function(view){
        this.when.done(_.partial(this.renderRecaptcha, view));
      },

      renderRecaptcha :  function(view, siteKey, undefined){
        grecaptcha.render(view.$(".g-recaptcha")[0],
          {
            sitekey: siteKey, callback: function (response) {
            //this might need to be inserted into the model.
            //or in the case of feedback form, it just needs
            //to be in the serialized form
            if (view.model){
              view.model.set("g-recaptcha-response", response);
            }
          }
          });
      },

      hardenedInterface: {
        activateRecaptcha : "activateRecaptcha"
      }

    });

    _.extend(RecaptchaManager.prototype, Hardened);

    return RecaptchaManager;

  });