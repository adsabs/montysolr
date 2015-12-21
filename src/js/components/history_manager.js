
define([
      'js/components/generic_module',
      'js/mixins/dependon',
      'js/mixins/hardened',
      'js/components/pubsub_key'
    ],
    function(
        GenericModule,
        Dependon,
        Hardened,
        PubSubKey
    ) {

      var History = GenericModule.extend({

        initialize: function () {
          this._history = [];
        },

        activate: function (beehive) {

          this.setBeeHive(beehive);
          var pubsub = this.getPubSub();
          pubsub.subscribe(pubsub.NAVIGATE, _.bind(this.recordNav, this));

        },

        recordNav: function () {
          this._history.push([].slice.apply(arguments));
        },

        getCurrentNav : function(){
          return this._history[this._history.length-1];
        },

        getPreviousNav: function(){
          return this._history[this._history.length-2];
        },

        hardenedInterface: {
         getPreviousNav : "",
         getCurrentNav : "",
        }

      });

      _.extend(History.prototype, Dependon.BeeHive, Hardened);

      return History;

});
