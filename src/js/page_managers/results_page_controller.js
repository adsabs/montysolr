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
  'hbs!./templates/results-control-row',
  'js/components/api_query'],
  function (
    Marionette,
    threeColumnTemplate,
    BaseWidget,
    LoadingWidget,
    resultsControlRowTemplate,
    ApiQuery) {

    var widgetDict, history, API;

    //  router can make use of these functions

    API = {

      insertTemplate: function () {

        $("#body-template-container").children().detach();

        $("#body-template-container").append(threeColumnTemplate());

        $("#results-control-row").append(resultsControlRowTemplate())

      },


      insertLoadingView: function () {
        $("#body-template-container").append(this.loadingWidget.render().el);

        this.loadingWidget.trigger("showLoading")

      },

      displayFacets: function () {

        var $leftCol = $("#s-left-col-container");

        $leftCol.append(widgetDict.authorFacets.render().el).append(widgetDict.database.render().el).append(widgetDict.refereed.render().el).append(widgetDict.keywords.render().el).append(widgetDict.pub.render().el).append(widgetDict.bibgroup.render().el).append(widgetDict.data.render().el).append(widgetDict.vizier.render().el).append(widgetDict.grants.render().el);

      },

      displayControlRow: function () {
        $("#query-info-container").append(widgetDict.queryInfo.render().el)
      },

      displayRightColumn: function () {
        var $rightCol = $("#s-right-col-container");

        $rightCol.append(widgetDict.graphTabs.render().el).append(widgetDict.queryDebugInfo.render().el);

      },

      displaySearchBar: function () {
        $("#search-bar-row").append(widgetDict.searchBar.render().el);

      },

      displayResultsList: function () {

        var $middleCol = $("#s-middle-col-container");

        $middleCol.append(widgetDict.results.render().el);

        $(".list-of-things").removeClass("hide")

      },

      enableRightColToggle: function () {

        $("#right-col-toggle").on("click", function (e) {

          var $this = $(this);
          var $i = $this.find("i");

          if ($i.hasClass("right-col-open")) {

            $i.removeClass("right-col-open").addClass("right-col-close");
            $this.find("span").text("show 3rd col");

            $("#right-column").addClass("no-display");
            $("#middle-column").removeClass("col-md-7").addClass("col-md-9");
            $("#left-column").removeClass("col-md-2").addClass("col-md-3");

          }
          else {
            $this.find("span").text("hide 3rd col");

            $i.removeClass("right-col-close").addClass("right-col-open");

            $("#right-column").removeClass("no-display");
            $("#middle-column").removeClass("col-md-9").addClass("col-md-7");
            $("#left-column").removeClass("col-md-3").addClass("col-md-2");

          }
        })
      }
    };

    var ResultsController = BaseWidget.extend({

      initialize: function (options) {

        options = options || {};

        _.bindAll(this, 'showPage');

        _.extend(this, API);

        if (!options.widgetDict) {
          throw new error("page managers need a dictionary of widgets to render");
        }

        widgetDict = options.widgetDict;

        history = options.history;

        this.loadingWidget = new LoadingWidget();

      },

      activate: function (beehive) {

        this.pubsub = beehive.Services.get('PubSub');

        this.pubsub.subscribe(this.pubsub.START_SEARCH, this.showPage);

      },

      showPage: function (apiQuery) {

        //it's false when the router uses this function to display the results page
        if (apiQuery !== false) {

          var tempQuery = new ApiQuery();
          if (apiQuery.get("q")){
            tempQuery.set("q", apiQuery.get("q"));
          }
          if (apiQuery.get("fq")){
            tempQuery.set("fq", apiQuery.get("fq"));
          }

          var urlData = {page: "resultsPage", subPage: undefined, data: apiQuery.toJSON(), path: "search/" + tempQuery.url()};

          this.pubsub.publish(this.pubsub.NAVIGATE_WITHOUT_TRIGGER, urlData);

        }

        this.insertTemplate();
        this.displaySearchBar();
        this.displayControlRow();
        this.displayFacets();
        this.displayRightColumn();
        this.displayResultsList();
        this.enableRightColToggle();
        //this.insertLoadingView()

      }

    });

    return ResultsController

  });