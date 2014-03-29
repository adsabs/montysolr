/**
 * Created by rchyla on 3/28/14.
 */

define(['underscore', 'backbone', 'js/components/api_query'], function(_, Backbone, ApiQuery) {

  var allowedAttrs = {
    url: _.isString,
    query: function(v) {return v instanceof ApiQuery},
    target: _.isString,
    sender: _.isString
  };

  var ApiRequest = Backbone.Model.extend({
    _validate: function(attributes, options) {
      for (attr in attributes) {

        var tempVal = attributes[attr];

        if (!(attr in allowedAttrs)) {
          throw new Error('Invalid attr: '+ attr);
        }


        if (!allowedAttrs[attr].call(allowedAttrs, tempVal)) {
          throw new Error('Invalid value:key ' + attr  + tempVal);
        }
      }
      return true;
    }
  });

  return ApiRequest;
});
