define(['underscore'
    ],
  function(
    _
    ) {


    var Transition = function (endpoint, options) {
      if (!_.isString(endpoint)) {
        throw new Error('Endpoint name must be a string');
      }
      this.endpoint = endpoint;
      _.extend(this, options);
    };
    _.extend(Transition.prototype, {
      route: false,
      trigger: false,
      replace: false,
      execute: function () {
        throw new Error("You must override this method");
      }
    });

    return Transition;
  }
);
