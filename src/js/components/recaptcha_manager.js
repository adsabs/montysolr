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

    onRecaptchaLoad = function(){
      grecaptchaDeferred.resolve();
    }

    var RecaptchaManager = GenericModule.extend({

      initialize : function(){
        this.grecaptchaDeferred = grecaptchaDeferred;
        this.siteKeyDeferred = $.Deferred();
        //this has to be global
        onRecaptchaLoad = function(){
          this.grecaptchaDeferred.resolve();
        }
        this.when = $.when(this.siteKeyDeferred, this.grecaptchaDeferred);
      },

      activate: function (beehive) {
        this.beehive = beehive;
        this.pubsub = beehive.Services.get('PubSub');
        this.key = this.pubsub.getPubSubKey();
        _.bindAll(this, [ "getRecaptchaKey"]);
        this.pubsub.subscribe(this.key, this.pubsub.APP_STARTED, this.getRecaptchaKey);
      },

      getRecaptchaKey : function(){
        siteKey = this.beehive.getObject("AppStorage").getConfigCopy().recaptchaKey;
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
              view.model.set("g-recaptcha-response", response);
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