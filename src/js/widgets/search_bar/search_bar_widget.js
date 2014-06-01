define(['marionette', 'js/components/api_query', 'js/widgets/base/base_widget',
    'hbs!./templates/search_bar_template', 'bootstrap', 'hoverIntent'
  ],
  function(Marionette, ApiQuery, BaseWidget, SearchBarTemplate) {

    $.fn.selectRange = function(start, end) {
      if (!end) end = start;
      return this.each(function() {
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

      initialize: function(options) {
        _.bindAll(this, "tempFieldInsert", "tempFieldClear")
      },

      onRender: function() {
        this.$(".field-options div").hoverIntent(this.tempFieldInsert, this.tempFieldClear);

      },

      events: {
        "click .search-submit": "submitQuery",
        "click .field-options div": "addField",
        "keypress .q": function(e){
          this.checkSubmit(e);
          this.highlightFields(e);
        },
        "blur .q": "unHighlightFields"
      },

      highlightFields : function(){
        this.$(".show-fields").addClass("draw-attention")
      },

      unHighlightFields : function(){
        this.$(".show-fields").removeClass("draw-attention")
      },

      tempFieldInsert: function(e) {
        e.preventDefault();

        var currentVal, newVal, df;

        currentVal = this.$(".q").val();
        this.priorVal = currentVal;

        df = $(e.target).attr("data-field");

        if (df.split("-")[0] === "operator") {
          //cache it for mouseleave events
          if (currentVal !== "") {
            newVal = df.split("-")[1] + "(" + currentVal + ")";
          } else {
            newVal = df.split("-")[1] + "( )";
          }
          this.$(".q").val(newVal);

        } else {

          //checking if first author
          if (df === "first-author") {
            newVal = currentVal + " author:\"^\"";
          } else {
            newVal = currentVal + " " + df + ":\"\"";
          };
          this.$(".q").val(newVal);
        }
      },

      tempFieldClear: function(e) {
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

     addField: function(e, mouseover) {
        this.addField = true;
      },

      checkSubmit: function(e) {

        if (e.keyCode === 13) {
          e.preventDefault();
          this.submitQuery();
        }
      },

      submitQuery: function() {
        var query = (this.$(".q").val());
        this.trigger("new_query", query)
      }

    })

    var SearchBarWidget = BaseWidget.extend({

      initialize: function(options) {
        this.view = new SearchBarView();
        this.listenTo(this.view, "new_query", this.submitNewQuery);

        BaseWidget.prototype.initialize.call(this, options)
      },

      processResponse: function(apiResponse) {
        this.setCurrentQuery(apiResponse.getApiQuery());
      },

      submitNewQuery: function(query) {
        var newQuery = new ApiQuery({
          q: query
        })

        this.pubsub.publish(this.pubsub.NEW_QUERY, newQuery);
      }
    })


    return SearchBarWidget;


  });
