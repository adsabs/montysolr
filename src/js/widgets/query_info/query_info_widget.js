define(['marionette', 'backbone', 'underscore', 'js/components/api_request', 'js/components/api_query',
    'js/widgets/base/base_widget', 'hbs!./query_info_template'
  ],

  function(Marionette, Backbone, _, ApiRequest, ApiQuery, BaseWidget, queryInfoTemplate) {

    var queryModel = Backbone.Model.extend({

      defaults: {
        currentQuery: undefined,
        numFound: undefined,
        currentSort: undefined
      }
    })

    var queryDisplayView = Backbone.View.extend({

   	  className : "well well-small",

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

        this.view.render();

        this.view.$("#info-changeable").fadeIn()


      }

    })

    return Widget


  })
