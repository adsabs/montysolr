define([
    'backbone',
    'underscore'
  ],
  function(
    Backbone,
    _
  ) {

    var OrcidProfileModel = Backbone.Model.extend({
      defaults: function () {
        var result = {
          familyName: undefined,
          givenName: undefined,

          isWaitingForProfileInfo : false,
          isSignedIn : false
        };

        return result;
      }
    });

    return OrcidProfileModel;

  });