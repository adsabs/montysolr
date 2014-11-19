define(['marionette',
    'backbone',
    'underscore',
    'js/components/api_request',
    'js/components/api_query',
    'js/widgets/base/base_widget',
    'hbs!./query_info_template',
    'js/mixins/formatter'
  ],

  function(Marionette,
           Backbone,
           _,
           ApiRequest,
           ApiQuery,
           BaseWidget,
           queryInfoTemplate,
           FormatMixin
    ) {

    var queryModel = Backbone.Model.extend({

      defaults: {
        currentQuery: undefined,
        numFound: undefined,
        currentSort: undefined,
        citations: undefined
      }
    })

    var queryDisplayView = Backbone.View.extend({

   	  className : "query-info-widget",

      initialize: function(options) {
        this.model = new queryModel();
      },

      events : {
        "click #show-more-query-data" : "toggleAdditionalInfo"
      },

      toggleAdditionalInfo : function(ev){
        var $add = $("#additional-query-data");
        var $button = $("#show-more-query-data");
        $add.toggleClass("hide");

        if ($add.hasClass("hide")){
          $button.html(("<i class=\"fa fa-plus fa-lg\"></i>"))
        }
        else {
          $button.html("<i class=\"fa fa-minus fa-lg\"></i>")
        }

      },

      render: function() {
        var json = this.model.toJSON();
        json.numFound = json.numFound ? this.formatNum(json.numFound) : 0;

        if (json.citations){

          _.each(json.citations, function(v,k){
            json.citations[k] = parseInt(v)
          })

          json.citations.sum = json.citations.sum ? this.formatNum(json.citations.sum) : 0;
        }

        this.$el.html(this.template(json))
        return this
      },

      template: queryInfoTemplate
    })

    _.extend(queryDisplayView.prototype, FormatMixin)

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
