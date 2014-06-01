/**
 * Created by alex on 5/8/14.
 */
define(['backbone', 'marionette', 'js/components/api_query', 'js/components/api_request',
    'js/widgets/base/multi_callback_widget', 'js/components/paginator'],
  function (Backbone, Marionette, ApiQuery, ApiRequest, MultiCallbackWidget, Paginator) {

    var PaginatedMultiCallbackWidget = MultiCallbackWidget.extend({

      initialize: function (options) {
        this.paginator = options.paginator || new Paginator({"start": options.start, "rows": options.rows,
          "startName": options.startName, "rowsName": options.rowsName});
        this._paginators = {};
        MultiCallbackWidget.prototype.initialize.call(this, options);
      },

      dispatchRequest: function(apiQuery) {
        var pag = this.findPaginator(apiQuery);
        pag.paginator.reset();
        this._dispatchRequest(apiQuery);
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
        var pag = this.findPaginator(q);
        var newQ = pag.paginator.run(q);
        this.reAssignPaginator(pag.key, newQ.url());
        return newQ;
      },

      findPaginator: function(apiQuery) {
        var key = apiQuery.url();
        if (!this._paginators[key]) {
          //console.log('creating new paginator', key)
          this._paginators[key] = new Paginator(this.paginator);
        }
        return {key: key, paginator: this._paginators[key]};
      },

      reAssignPaginator: function(oldKey, newKey) {
        if (this._paginators[oldKey]) {
          this._paginators[newKey] = this._paginators[oldKey];
          delete this._paginators[oldKey];
        }
        else {
          throw new Error("Trying to remove paginator that doesn't exist: " + oldKey);
        }
      }
    });

    return PaginatedMultiCallbackWidget;
  });