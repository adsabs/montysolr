define([
    "marionette",
    "hbs!./templates/results-page-layout",
    'hbs!./templates/results-control-row',
    'js/widgets/base/base_widget'
  ],
  function (Marionette,
            pageTemplate,
            controlRowTemplate,
            BaseWidget
            ) {

    var ThreeColumnView = Marionette.ItemView.extend({

      initialize : function(options){
        var options = options || {};
        this.widgets = options.widgets;
      },

      template : pageTemplate,

      events : {
        "click .btn-expand" : "toggleColumns"
      },

      onRender : function(){
        var self = this;

        this.$("#results-control-row")
          .append(controlRowTemplate());

        this.displaySearchBar(this.options.displaySearchBar);
        this.displayControlRow(this.options.displayControlRow);
        this.displayLeftColumn(this.options.displayLeftColumn);
        this.displayRightColumn(this.options.displayRightColumn);
        this.displayMiddleColumn(this.options.displayMiddleColumn);
      },

      onShow : function(){
        //these functions must be called every time the template is inserted
        this.displaySearchBar(true);

      },

      displaySearchBar: function (show) {
        $("#search-bar-row").toggle(show === null ? true : show);
      },

      displayLeftColumn: function(show) {
        this.$(".s-left-col-container").toggle(show === null ? true : show);
      },

      displayControlRow: function (show) {
        this.$("#results-control-row").toggle(show === null ? true : show);
      },

      displayRightColumn: function (show) {
        this.$(".s-left-col-container").toggle(show === null ? true : show);
      },

      displayMiddleColumn: function (show) {
        this.$(".s-left-col-container").toggle(show === null ? true : show);
      },


      /**
       * Show/hide - in a slide fashion - the columns when user clicks on the
       * controls
       *
       * @param e
       */
      toggleColumns :function(e){

        var $t = $(e.currentTarget);
        var $leftCol =  this.$(".s-results-left-column");
        var $rightCol =  this.$(".s-results-right-column");

        if ($t.hasClass("btn-upside-down")){

          $t.removeClass("btn-upside-down");

          if ($t.hasClass("left-expand")){

            $leftCol.removeClass("hidden-col");
            $leftCol.find(".left-col-container").width('').fadeIn(500).children().show();

          }
          else {
            $rightCol.removeClass("hidden-col");

            $rightCol.find(".right-col-container").width('').fadeIn(500) ;

          }

          if (!$rightCol.hasClass("hidden-col") && !$leftCol.hasClass("hidden-col")){
            this.$("#results-middle-column")
              .css({"width": ""})

          }
          else if ($leftCol.hasClass("hidden-col")){
            this.$("#results-middle-column")
              .css({"width": "75%"})
          }
          else {
            this.$("#results-middle-column")
              .css({"width":  "83.33333333%"})

          }

        }
        else {
          $t.addClass("btn-upside-down");

          if ($t.hasClass("left-expand")){

            $leftCol.find(".left-col-container").width(0).fadeOut(500).children().hide();

            $leftCol.addClass("hidden-col")

          }
          else {
            //expand to the right

            $rightCol.find(".right-col-container").width(0).hide(500);

            $rightCol.addClass("hidden-col");


          }

          if ($rightCol.hasClass("hidden-col") && $leftCol.hasClass("hidden-col")){
            this.$("#results-middle-column")
              .addC //XXX: what is this???

          }
          else if ($rightCol.hasClass("hidden-col")){
            this.$("#results-middle-column")
              // 58.33333 + 25
              .css("width", "83.33333333%")
          }
          else {
            //58.33333 + 16.666666
            this.$("#results-middle-column")
              .css("width", "75%")

          }

        }
      }

    });
    return ThreeColumnView;

  });