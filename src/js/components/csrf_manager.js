/*
 widgets can attach callbacks to a deferred that waits until
 * a new csrf token has been requested
 *
 * */
define([
    'backbone',
    'js/components/generic_module',
    'js/mixins/hardened',
    "js/components/api_request",
    "js/components/api_targets"
  ],
  function(
    Backbone,
    GenericModule,
    Hardened,
    ApiRequest,
    ApiTargets
    ) {


    var CSRFManager = GenericModule.extend({

      activate: function (beehive) {
        this.beehive = beehive;
        this.pubsub = beehive.Services.get('PubSub');
        this.key = this.pubsub.getPubSubKey();
        _.bindAll(this, ["resolvePromiseWithNewKey"]);
        this.pubsub.subscribe(this.key, this.pubsub.DELIVERING_RESPONSE, this.resolvePromiseWithNewKey);
      },

     getCSRF : function(){
       this.deferred = $.Deferred();

       var request = new ApiRequest({
         target : ApiTargets.CSRF
       });

       this.pubsub.publish(this.key, this.pubsub.EXECUTE_REQUEST, request);
       return this.deferred.promise();
     },

      resolvePromiseWithNewKey : function(response){
        //get csrf here
        var csrf = response.toJSON().csrf;
        this.deferred.resolve(csrf);
      },

      hardenedInterface: {
        getCSRF : "getCSRF"
      }

    });

    _.extend(CSRFManager.prototype, Hardened);

    return CSRFManager;

  });