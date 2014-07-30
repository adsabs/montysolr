/**
 * Created by alex on 5/8/14.
 *
 * TODO:rca - it seems overkill to have a separate widget just to add a pagination
 * feature to each query; I'll have to rethink this
 */

define(['backbone', 'marionette', 'js/widgets/base/base_widget',
    'js/components/api_query', 'js/components/api_request', 'js/components/paginator'],
  function (Backbone, Marionette, BaseWidget, ApiQuery, ApiRequest, Paginator) {

    var PaginatedBaseWidget = BaseWidget.extend({

      initialize: function (options) {
        this.paginator = options.paginator || new Paginator({"start": options.start, "rows": options.rows,
          "startName": options.startName, "rowsName": options.rowsName});

        BaseWidget.prototype.initialize.call(this, options)
      },

      /**
       * Default action to modify ApiQuery (called from inside dispatchRequest)
       *
       * @param apiQuery
       */
      customizeQuery: function (apiQuery) {
        var q = apiQuery.clone();
        q.unlock();
        if (this.defaultQueryArguments) {
          q = this.composeQuery(this.defaultQueryArguments, q);
        }
        return this.paginator.run(q);
      }
    });

    return PaginatedBaseWidget

  });

