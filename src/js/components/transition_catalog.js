define(['underscore',
    'js/components/transition'
    ],
  function(
    _,
    Transition
    ) {


    var TransitionCatalog = function (options) {
      this._catalog = {};
    };
    _.extend(TransitionCatalog.prototype, {
      add: function(transition) {
        if (!(transition instanceof Transition)) {
          throw new Error("You can add only Transition objects");
        }
        this._catalog[transition.endpoint] = transition;
      },
      get: function(name) {
        return this._catalog[name];
      },
      remove: function(name) {
        delete this._catalog[name];
      }
    });

    return TransitionCatalog;
  }
);
