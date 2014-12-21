define([
    'backbone',
    'underscore'
  ],
  function (Backbone,
            _) {

    var OrcidWorkModel = Backbone.Model.extend({
      defaults: function () {
        var workExternalItentifier = {
          id: undefined,
          type: undefined
        };

        var item = {
          publicationData: undefined,
          workExternalIdentifiers: [],
          workTitle: undefined,
          workType: undefined,
          workSourceUri: undefined,
          workSourceHost: undefined
        };

        var result = {

          item: undefined,
          isLoaded : false,
          isWaitingForProfileInfo : false
          //isLoaded: false,
          //isLoading: false
        };

        return result;
      }
    });

  });