define([
    "marionette",
    "hbs!./templates/results-page-layout",
    'hbs!./templates/results-control-row',
    'js/widgets/base/base_widget'
  ],
  function (Marionette,
            pageTemplate,
            controlRowTemplate
    ) {


    /*
     * keeps track of the open/closed state of the three columns
     * */
    var ResultsStateModel = Backbone.Model.extend({
      defaults : function(){
        return {
          left : "open",
          right : "open",
          user_left: null,
          user_right: null,
          largerThanTablet : true
        }
      }
    });


    var ThreeColumnView = Marionette.ItemView.extend({

      initialize : function(options){
        var options = options || {};
        this.widgets = options.widgets;
        this.model = new ResultsStateModel();

      },

      onDetach : function(){
        $(window).off("resize", this.setScreenSize)
      },

      template : pageTemplate,


      modelEvents: {
        "change:left": "_updateColumnView",
        "change:right": "_updateColumnView",
        "change:largerThanTablet": "resizeColumns"
      },

      events: {
        "click .btn-expand": "onClickToggleColumns"
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

        //let view know whether it should display a 2 or 3 column layout
        this.setScreenSize();

        //listen for resizing events
        $(window).resize(_.bind(this.setScreenSize, this));
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

      setScreenSize: _.debounce(function() {
        if (this.$(".right-expand").css("display") == "none") {
          this.model.set("largerThanTablet", false)
        }
        else {
          this.model.set("largerThanTablet", true)

        }
        // higher debounce times had a noticable lag
      }, 200),

      /**
       * when model.largerThanTablet changes; this function will
       * decide how to lay out the individual columns
       */
      resizeColumns: function () {
        var leftHidden = (this.model.get("left") === "closed");

        if (this.model.get("largerThanTablet")) {
          // it's a three column layout
          this.$("#results-right-column").append(this.$(".right-col-container"));
          if (leftHidden) {
            this.$(".right-col-container").show();
          }
        }
        else {
          // two column layout
          this.$("#results-left-column").append(this.$(".right-col-container"));
          if (leftHidden) {
            this.$(".right-col-container").show();
          }
        }
      },

      /**
       * Show/hide - in a slide fashion - the columns when user clicks on the
       * controls
       *
       * @param e
       */
      onClickToggleColumns: function (e) {
        var name, $button, state;
        $button = $(e.currentTarget);
        $button.toggleClass("btn-reversed");
        name = $button.hasClass("left-expand") ? "left" : "right";
        state = this.model.get(name) === "open" ? "closed" : "open";
        this.model.set('user_' + name, state);
        this.model.set(name, state);
      },

      _returnBootstrapClasses: function () {
        var classes = this.classList;
        var toRemove = [];
        _.each(classes, function (c) {
          if (c.indexOf("col-") !== -1) {
            toRemove.push(c)
          }
        });
        return toRemove.join(" ")
      },

      /**
       * Method to display/hide columns, accepts object with keys:
       *  left: true/false
       *  right: true|false
       *  force: true if you want to override user action (i.e. open
       *         column, even if they changed it manually)
       * @param options
       */
      showCols: function(options) {
        options = options || {left: open, right: open, force: false};
        var keys = ['left', 'right'];
        for (var i=0; i<keys.length; i++) {
          var k = keys[i];
          if (k in options) {
            var ul = this.model.get('user_' + k);
            if (ul === null || options.force)
              this.model.set(k, options[k] ? 'open' : 'closed');
          }
        }
      },



      _updateColumnView: function () {

        var leftState, rightState, $leftCol, $rightCol, $middleCol;

        leftState = this.model.get("left");
        rightState = this.model.get("right");


        $leftCol = this.$("#results-left-column");
        $rightCol = this.$("#results-right-column");
        $middleCol = this.$("#results-middle-column");


        _.each([['left', leftState, $leftCol], ['right', rightState, $rightCol]], function(x) {
          if (x[1] == 'open') {
            x[2].removeClass("hidden-col");
            var $col = x[2];
            setTimeout(function(){
              $col.children().show(0);
            }, 500)
          }
          else {
            x[2].addClass("hidden-col");
          }
        });

        if (leftState === "open" && rightState === "open") {
          $middleCol.removeClass(this._returnBootstrapClasses)
            .addClass("col-md-7 col-sm-8")
        }
        else if (leftState === "closed" && rightState === "open") {
          $middleCol.removeClass(this._returnBootstrapClasses)
            .addClass("col-md-9 col-sm-12")
        }
        else if (leftState === "open" && rightState === "closed") {
          $middleCol.removeClass(this._returnBootstrapClasses)
            .addClass("col-md-10 col-sm-8")
        }
        else if (leftState === "closed" && rightState === "closed") {
          $middleCol.removeClass(this._returnBootstrapClasses)
            .addClass("col-md-12 col-sm-12")
        }
      }

    });
    return ThreeColumnView;

  });