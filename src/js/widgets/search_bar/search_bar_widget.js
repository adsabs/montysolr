define([
    'marionette',
    'js/components/api_query',
    'js/widgets/base/base_widget',
    'hbs!./templates/search_bar_template',
    'hbs!./templates/search_form_template',
    'js/components/query_builder/plugin',
    'js/components/api_feedback',
    'bootstrap' // if bootstrap is missing, jQuery events get propagated
  ],
  function (
    Marionette,
    ApiQuery,
    BaseWidget,
    SearchBarTemplate,
    SearchFormTemplate,
    QueryBuilderPlugin,
    ApiFeedback,
    bootstrap
    ) {


    // XXX:rca - to be removed
    $.fn.selectRange = function (start, end) {
      if (!end) end = start;
      return this.each(function () {
        if (this.setSelectionRange) {
          this.focus();
          this.setSelectionRange(start, end);
        } else if (this.createTextRange) {
          var range = this.createTextRange();
          range.collapse(true);
          range.moveEnd('character', end);
          range.moveStart('character', start);
          range.select();
        }
      });
    };

    var SearchBarView = Marionette.ItemView.extend({

      template: SearchBarTemplate,

      render : function(){
        Marionette.ItemView.prototype.render.apply(this, arguments);
        this.render = function(){ return this}
      },

      className : "s-search-bar-widget",

      initialize: function (options) {
        _.bindAll(this, "tempFieldInsert", "tempFieldClear");
        this.queryBuilder = new QueryBuilderPlugin();
      },

      activate: function(beehive) {
        this.queryBuilder.setQTreeGetter(QueryBuilderPlugin.buildQTreeGetter(beehive));
        var that = this;
        this.queryBuilder.attachHeartBeat(function() {
          that.onBuilderChange();
        });
        this.beehive = beehive;
      },

      onBuilderChange: function() {
        if (this.queryBuilder.isDirty()) {
          var newQuery = this.queryBuilder.getQuery();
          this.setFormVal(newQuery);
        }
      },

      onRender: function () {
        this.$("#search-form-container").append(SearchFormTemplate);

        //        I think this is not acting perfectly if people hover and then enter text into
        // the input field without clicking.
        //        this.$("#field-options div").hoverIntent(this.tempFieldInsert, this.tempFieldClear);
        this.$("#search-gui").append(this.queryBuilder.$el);
      },

      events: {
        "click #field-options button" : "tempFieldInsert",
        "keypress .q": function(e){
          this.highlightFields(e);
          this.setAddField();
        },
        "blur .q": "unHighlightFields",
        "click #search-form-container": function (e) {
          e.stopPropagation();
        },
        "click #search-form-container .title": "toggleFormSection",
        "click .show-form": "onShowForm",
        "submit form[name=main-query]": "submitQuery"
      },

      getFormVal: function() {
        return this.$(".q").val();
      },

      setFormVal: function(v) {
        return this.$(".q").val(v);
      },

      onShowForm: function() {
        var formVal = this.getFormVal();
        if (formVal.trim().length > 0) {
          this.queryBuilder.updateQueryBuilder(formVal);
        }
        else { // display a default form
          this.queryBuilder.setRules({
            "condition": "AND",
            "rules": [
              {
                "id": "author",
                "field": "author",
                "type": "string",
                "input": "text",
                "operator": "is",
                "value": ""
              }
            ]
          });
        }

        // show the form
        this.specifyFormWidth();
      },

      specifyFormWidth: function () {
        this.$("#search-form-container").width(this.$(".input-group").width());
      },

      toggleFormSection: function (e) {
        var $p = $(e.target).parent();
        $p.next().toggleClass("hide");
        $p.toggleClass("search-form-header-active");
      },

      highlightFields: function () {
        this.$(".show-form").addClass("draw-attention")
      },

      unHighlightFields: function () {
        this.$(".show-form").removeClass("draw-attention")
      },

      tempFieldInsert: function (e) {
        e.preventDefault();
        var currentVal, newVal, df;

        //        this.unsetAddField();

        currentVal = this.getFormVal();
        this.priorVal = currentVal;

        df = $(e.target).attr("data-field");

        if (df.split("-")[0] === "operator") {
          //cache it for mouseleave events
          if (currentVal !== "") {
            newVal = df.split("-")[1] + "(" + currentVal + ")";
          } else {
            newVal = df.split("-")[1] + "( )";
          }
          this.setFormVal(newVal);

        } else {

          //checking if first author
          if (df === "first-author") {
            newVal = currentVal + " author:\"^\"";
          } else {
            newVal = currentVal + " " + df + ":\"\"";
          }
          this.setFormVal(newVal);
        }

        this.$(".q").focus();
        this.$(".q").selectRange(this.$(".q").val().length - 1);
      },

      tempFieldClear: function (e) {
        if (this.addField === true) {
          this.$(".q").focus();
          this.$(".q").selectRange(this.$(".q").val().length - 1);
          this.addField = false;
        }
        else {
          //assumption that final entry in query bar needs to be cleared
          this.$(".q").val(this.priorVal);
          this.priorVal = undefined;
        }
      },

      setAddField: function (e) {
        this.addField = true;
      },

      unsetAddField : function (e) {
        this.addField = false;
      },

      submitQuery: function(e) {
        e.preventDefault();
        e.stopPropagation();

        var query = this.getFormVal();
        this.trigger("start_search", query);
      }
    });

    var SearchBarWidget = BaseWidget.extend({

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        // search widget doesn't need to execute queries (but it needs to listen to them)
        this.pubsub.subscribe(this.pubsub.FEEDBACK, _.bind(this.handleFeedback, this));
        this.view.activate(beehive);
      },

      defaultQueryArguments: {
        //sort: 'date desc',
        fl: 'id'
      },

      handleFeedback: function(feedback) {
        switch (feedback.code) {
          case ApiFeedback.CODES.SEARCH_CYCLE_STARTED:
            this.setCurrentQuery(feedback.query);
            this.view.setFormVal(feedback.query.get('q').join(' '));
            break;
        }
      },

      initialize: function (options) {

        this.currentQuery = undefined;
        this.view = new SearchBarView();
        this.listenTo(this.view, "start_search", function (query) {
          var newQuery = new ApiQuery({
            q: query
          });

          this.changeDefaultSort(newQuery);
          this.navigate(newQuery);
        });

        this.listenTo(this.view, "render", function () {
          var query = this.getCurrentQuery().get("q");
          if (query) {
            this.view.setFormVal(query)
          }
        });

        BaseWidget.prototype.initialize.call(this, options)
      },

      changeDefaultSort : function(query) {

        //make sure not to override an explicit sort if there is one
        if (!query.has("sort")){
          var queryVal, toMatch, operator;
          queryVal = query.get("q")[0];

          //citations operator should be sorted by pubdate too
          toMatch = ["trending(", "instructive(", "useful(", "references("];
          operator = _.find(toMatch, function(e) {
            if (queryVal.indexOf(e) !== -1) {
              return e
            }
          });

          if (operator && operator === "references(" ){
            query.set("sort", "first_author asc")
          }
          if (!operator) {
            query.set("sort", "date desc");
          }
        }
      },

      navigate: function (newQuery) {
        this.pubsub.publish(this.pubsub.START_SEARCH, newQuery);
      },

      openQueryAssistant: function(queryString) {
        if (queryString) {
          this.view.setFormVal(queryString);
        }
        this.view.$el.find('.show-form').click();
      }
    });
    return SearchBarWidget;
  });