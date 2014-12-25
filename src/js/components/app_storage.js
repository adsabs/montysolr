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
      }


    }
  );

  return AppStorage;

});