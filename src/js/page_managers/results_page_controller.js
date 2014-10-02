/**
 * This widget knows about the central region manager (passed it on instantiation)
 * and can manipulate it (also it can add sub regions)
 *
 * It listens to any events that request the abstract page or a sub part of it and
 * displays the necessary views
 *
 * Exposes an api that can be used by the router (to change what is currently
 * displayed)
 *
 */


define([
    "marionette",
    "hbs!./templates/results-page-layout",
    'js/widgets/base/base_widget',
    'js/widgets/loading/widget',
    'hbs!./templates/results-control-row'],
  function (
    Marionette,
    threeColumnTemplate,
    BaseWidget,
    LoadingWidget,
    resultsControlRowTemplate) {



    ResultsControllerView = Marionette.ItemView.extend({

      initialize : function(options){

        var options = options || {};
        this.widgetDict = options.widgetDict;

      },

      template : threeColumnTemplate,

      resultsControlRowTemplate  : resultsControlRowTemplate,

      onRender : function(){

        this.displayControlRow();
        this.displayFacets();
        this.displayRightColumn();
        this.displayResultsList();
      },


      displayFacets: function() {

        this.$(".s-left-col-container")
          .append(this.widgetDict.authorFacets.render().el)
          .append(this.widgetDict.database.render().el)
          .append(this.widgetDict.refereed.render().el)
          .append(this.widgetDict.keywords.render().el)
          .append(this.widgetDict.pub.render().el)
          .append(this.widgetDict.bibgroup.render().el)
          .append(this.widgetDict.data.render().el)
          .append(this.widgetDict.vizier.render().el)
          .append(this.widgetDict.grants.render().el);

      },

      displayControlRow: function () {

        this.$("#results-control-row")
          .append(this.resultsControlRowTemplate());

        this.$("#query-info-container")
          .append(this.widgetDict.queryInfo.render().el)
      },

      displayRightColumn: function () {
        this.$(".right-col-container")
          .append(this.widgetDict.graphTabs.render().el);

          if (Marionette.getOption(this, "debug")) {
            this.$(".right-col-container")
              .append(this.widgetDict.queryDebugInfo.render().el);

          }
      },

      displayResultsList: function () {

        this.widgetDict.results.view.sortView = this.widgetDict.sort.view;

        this.$(".main-content-container")
          .append(this.widgetDict.results.render().el);



      },

      onShow : function(){

        //these functions must be called every time the template is inserted
        this.displaySearchBar();

      },

      displaySearchBar: function () {
        $("#search-bar-row")
          .append(this.widgetDict.searchBar.render().el)

      },

      events : {
        "click .btn-expand" : "toggleColumns"
      },

      toggleColumns :function(e){

        var $t = $(e.currentTarget);
        var $leftCol =  this.$(".s-results-left-column");
        var $rightCol =  this.$(".s-results-right-column");

        if ($t.hasClass("btn-upside-down")){

          $t.removeClass("btn-upside-down");

          if ($t.hasClass("left-expand")){

            $leftCol.removeClass("hidden-col")
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
              .css({"width": "100%"})

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


    var ResultsController = BaseWidget.extend({

      initialize: function (options) {

        options = options || {};

        _.bindAll(this, 'showPage');


        if (!options.widgetDict){
          throw new Error("page managers need a dictionary of widgets to render")
        }

        this.widgetDict = options.widgetDict;

      },

      //don't subscribe to events

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        //this has to go here to get information from beehive
        //better place to put it?

        this.controllerView = new ResultsControllerView({widgetDict : this.widgetDict, debug : beehive.getDebug()});


      },

      insertResultsControllerView : function(){

          var $b = $("#body-template-container");

          $b.children().detach();

          //don't call render each time or else we
          //would have to re-delegate widget events

          $b.append(this.controllerView.el);

          this.controllerView.triggerMethod("show");

      },

      showPage: function (options) {

        var inDom = options.inDom;

        if (!inDom){
          this.insertResultsControllerView();
        }

      }

    });

    return ResultsController

  });

