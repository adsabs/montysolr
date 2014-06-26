define(['marionette', 'backbone', 'underscore', 'js/components/api_request', 'js/components/api_query',
    'js/widgets/base/base_widget', 'hbs!./query_info_template'
  ],

  function(Marionette, Backbone, _, ApiRequest, ApiQuery, BaseWidget, queryInfoTemplate) {

    var queryModel = Backbone.Model.extend({

      defaults: {
        currentQuery: undefined,
        numFound: undefined,
        currentSort: undefined,
        citations: undefined
      }
    })

    var queryDisplayView = Backbone.View.extend({

   	  className : "",

      initialize: function(options) {
        this.model = new queryModel();
      },

      render: function() {
        this.$el.html(this.template(this.model.attributes))
        return this
      },

      template: queryInfoTemplate,
    });

    var Widget = BaseWidget.extend({

      defaultQueryArguments: {
        stats: 'true',
        'stats.field': 'citation_count'
      },

      initialize: function(options) {
        this.view = new queryDisplayView();
        BaseWidget.prototype.initialize.call(this, options)
      },

      processResponse: function(apiResponse) {
        var q = apiResponse.getApiQuery();
        var numFound = apiResponse.get("response.numFound");

        this.view.model.set("currentQuery", q.get("q"));

        var filters = [];
        _.each(q.keys(), function(k) {
          if (k.substring(0,2) == 'fq') {
            _.each(q.get(k), function(v) {
              if (v.indexOf('{!') == -1) {
                filters.push(v);
              }
            });
          }
        });
        this.view.model.set("facets", filters);
        this.view.model.set("sort", q.get("sort"));
        this.view.model.set("numFound", numFound);
        this.view.model.set("citations", apiResponse.get('stats.stats_fields.citation_count'));
        this.view.render();

        this.view.$("#info-changeable").fadeIn()


      }

    })

    return Widget


  })
