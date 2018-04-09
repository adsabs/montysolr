define(['backbone', 'marionette',
  'js/components/api_query', 'js/components/api_request', 'js/widgets/base/base_widget'
], function(
  Backbone, Marionette, ApiQuery, ApiRequest, BaseWidget) {

    var Factory = {
      createSimpleWidget: function(view, coll) {
        var t = new BaseWidget();
        t.view = view;
        t.collection = coll;
        return t;
      },

      createSW: function(view, coll) {
        // check arguments
        var T = BaseWidget.extend({
          initialize: function(options) {
            this.view = options.view;
          }
        });
        return new T(view, coll)
      }
    };

    return Factory;
  }
);
