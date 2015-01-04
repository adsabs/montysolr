/*
 * A generic class that holds the application storage
 */

define([
  'backbone',
  'underscore',
  'js/components/api_query'
], function(
  Backbone,
  _,
  ApiQuery
  ) {


  var AppStorage = Backbone.Model.extend({
      setCurrentQuery: function(apiQuery) {
        if (!apiQuery)
          apiQuery = new ApiQuery();

        if (!(apiQuery instanceof ApiQuery)) {
          throw new Error('You must save ApiQuery instance');
        }
        this.set('currentQuery', apiQuery);
      },

      getCurrentQuery: function() {
        return this.get('currentQuery');
      },

      hasCurrentQuery: function() {
        return this.has('currentQuery');
      },

      hasSelectedPapers: function() {
        return this.has('selectedPapers');
      },
      getSelectedPapers: function() {
        return _.keys(this.get('selectedPapers') || {});
      },
      addSelectedPapers: function(identifiers) {
        var data = this.get('selectedPapers') || {};
        if (_.isArray(identifiers)) {
          _.each(identifiers, function(i) {
            data[i] = true;
          })
        }
        else {
          data[identifiers] = true;
        }
        this.set('selectedPapers', data);
      },
      isSelectedPaper: function(identifier) {
        var data = this.get('selectedPapers') || {};
        return data[identifier];
      },
      removeSelectedPapers: function(identifiers) {
        var data = this.get('selectedPapers') || {};
        if (_.isArray(identifiers)) {
          _.each(identifiers, function(i) {
            delete data[i];
          })
        }
        else {
          delete data[identifiers];
        }
        this.set('selectedPapers', data);
      }


    }
  );

  return AppStorage;

});