define(['underscore'
    ],
  function(
    _
    ) {


    var Transition = function (endpoint, options) {
      if (!_.isString(endpoint)) {
        throw new Exception('Endpoint name must be a string');
      }
      this.endpoint = endpoint;
      _.extend(this, options);
    };
    _.extend(Transition.prototype, {
      route: false,
      trigger: false,
      replace: false,
      execute: function () {
        throw new Exception("You must override this method");
      }
    });

    return Transition;
  }
);
