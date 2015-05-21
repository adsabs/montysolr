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

    var QueryModel = Backbone.Model.extend({

      defaults: {
        numFound: 0,
        selected: 0,
        fq: undefined
      }
    })

    var QueryDisplayView = Marionette.ItemView.extend({

   	  className : "query-info-widget s-query-info-widget",
      template: queryInfoTemplate,
      serializeData : function(){
        var data = this.model.toJSON();
        data.numFound = this.formatNum(data.numFound);
        data.selected = this.formatNum(data.selected);
        return data;
      },

      modelEvents : {
        "change" : "render"
      },

      events : {
        "click .show-filter" : function(){
          this.model.set("showFilter", true);
        },
        "click .hide-filter" : function(){
          this.model.set("showFilter", false);
        },
        "click .clear-selected" : function(){
          this.trigger("clear-selected");
        },
        "click .page-bulk-add" : function(){
          this.trigger("page-bulk-add");
        }
      }
    })

    _.extend(QueryDisplayView.prototype, FormatMixin)

    var Widget = BaseWidget.extend({

      initialize: function(options) {
        this.model = new QueryModel();
        this.view = new QueryDisplayView({model : this.model});
        this.view.on("clear-selected", this.clearSelected, this);
        this.view.on("page-bulk-add", this.triggerBulkAdd, this);
        BaseWidget.prototype.initialize.call(this, options)
      },

      activate: function(beehive) {
        this.beehive = beehive;
        _.bindAll(this);
        this.pubsub = beehive.getService('PubSub');
        var pubsub = this.pubsub;

        pubsub.subscribe(pubsub.STORAGE_PAPER_UPDATE, this.onStoragePaperChange);
        pubsub.subscribe(pubsub.INVITING_REQUEST, this.dispatchRequest);
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      onStoragePaperChange : function(numSelected){
       this.model.set("selected", numSelected);
      },

      clearSelected : function(){
        this.beehive.getObject("AppStorage").clearSelectedPapers();
      },

      triggerBulkAdd : function(){
        this.pubsub.publish(this.pubsub.CUSTOM_EVENT, "add-all-on-page");
      },

      processResponse: function(apiResponse) {
        var q = apiResponse.getApiQuery();
        var numFound = apiResponse.get("response.numFound");
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
        this.view.model.set("fq", filters);
        this.view.model.set("numFound", numFound);
      }

    })

    return Widget


  })
