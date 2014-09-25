/**
 * This widget controls what gets displayed on the 'abstract' view
 *
 * It knows about the central region manager (passed it on instantiation)
 * and can manipulate it (also it can add sub regions)
 *
 * Listens to any events that request the abstract page or a sub part
 * of it and displays the necessary views
 *
 * Provides an api that can be used by the router
 */

define(["marionette",
    "hbs!./templates/abstract-page-layout",
    'js/widgets/base/base_widget',
    'js/components/api_query',
    'hbs!./templates/abstract-nav',
    'js/mixins/add_stable_index_to_collection',
    'js/page_managers/abstract_title_view_mixin.js'
  ],
  function(Marionette,
    threeColumnTemplate,
    BaseWidget,
    ApiQuery,
    abstractNavTemplate,
    WidgetPaginationMixin,
    AbstractTitleViewMixin){

    var currentBibcode;

    var AbstractNavView = Backbone.View.extend({

      template : abstractNavTemplate,

      render : function(subView){
        this.$el.html(this.template({bibcode: currentBibcode}));

        if (subView){
          this.$("#"+subView)
            .addClass("s-abstract-nav-active")
            .removeClass("s-abstract-nav-inactive");
        }
        return this
      },

      events : {
        "click a" : function(e){
          $t  = $(e.currentTarget);
          if ($t.find("div").hasClass("s-abstract-nav-inactive")){
            return false
          }
          if ($t.find("div").attr("id") !== $(".s-abstract-nav-active").attr("id")){
            this.emitNavigateEvent($t);
            this.changeHighlight($t);
          }
          return false
        }
      },

      changeHighlight : function($t){
        //first, remove all highlights
        this.$(".abstract-nav").removeClass("s-abstract-nav-active");

        //then add it in for the right one
        $t.find(".abstract-nav").addClass("s-abstract-nav-active");

      },

      emitNavigateEvent : function($t){
        var route = $t.attr("href");
        //taking only final path
        this.trigger("navigate", route);
      }

    })



    var API = {

      insertTemplate: function () {

        var $b = $("#body-template-container");


        if (this.cachedTemplate){

          $b.children().detach()

          $b.append(this.cachedTemplate);

          return

        }

        else {

          $b.children().detach();

          $b.append(threeColumnTemplate());

          this.fillTemplateWithWidgets();

        }

      },

      fillTemplateWithWidgets : function(){
        this.displayRightColumn();
        this.showTitle();

        this.cachedTemplate = $("#abstract-page-layout");

      },


      loadWidgetData: function () {
        var that = this;

        this.widgetDict.resources.loadBibcodeData(this._bibcode);

        _.each(this.abstractSubViews, function (v, k) {

          if (k === "abstract") {

            v.widget.loadBibcodeData(this._bibcode);
            this.activateNavButton(k)

          }
          else {

            var promise = v.widget.loadBibcodeData(this._bibcode);

            promise.done(function (numFound) {

              if (numFound) {
                that.activateNavButton(k, v.showNumFound, numFound)
              }
              else {
                that.deactivateNavButton(k)
              }

            })
          }

        }, this);


      },

      activateNavButton: function (k, showNumFound, colLength) {

        var $navButton = $("#" + k);

        $navButton.removeClass("s-abstract-nav-inactive");
        $navButton.parent().off(this, this.deactivateLink);

        if (showNumFound) {
          $navButton.find(".num-items").text("(" + colLength + ")")
        }

      },

      deactivateLink: function () {
        return false
      },

      deactivateNavButton: function (k) {

        var $navButton = $("#" + k);

        $navButton.addClass("s-abstract-nav-inactive")

        $navButton.parent().on("click", this.deactivateLink)

      },

      displayAbstractNav: function (subPage) {

        var $leftCol = $(".s-left-col-container")

        $leftCol.append(this.navView.render(subPage).el);

      },

      showTitle: function () {
        var $titleRow = $("#abstract-title-container");
        $titleRow.append(this.titleView.el);

      },

      showSubView: function (viewName) {

        var dataForRouter, $middleCol, widget;

        if (!viewName) {
          console.warn("viewname undefined")
          return
        }

        //tell router to display the correct subview only if it's not already there
        if (!this.collection.subPage || this.collection.subPage.viewKey !== viewName || $("s-middle-col-container").children().length === 0){

          dataForRouter = "abs/" + this._bibcode + "/" + viewName;

          this.pubsub.publish(this.pubsub.NAVIGATE_WITHOUT_TRIGGER, dataForRouter);

          $middleCol = $("#current-subview");

          $middleCol.children().detach();

          widget = this.abstractSubViews[viewName]["widget"];

          $middleCol.append(widget.render().el);

          /*The two lines below notify the title descriptor view to change*/
          this.collection.subPage = this.abstractSubViews[viewName];
          this.collection.subPage.viewKey = viewName

          this.collection.trigger("subPageChanged");

        }

      },

      displayRightColumn: function () {
        var $rightCol = $("#right-col-container");
        $rightCol.append(this.widgetDict.resources.render().el)

      },

      displayTopRow: function () {
        var $searchBar = $("#search-bar-row");
        $searchBar.append(this.widgetDict.searchBar.render().el);

      }

    };

    var AbstractControllerModel = Backbone.Model.extend({
      defaults: function () {
        return {
          bibcode: undefined,
          title: undefined,
          resultsIndex : undefined
          //id will be the result Index
        }
      },

      idAttribute : "resultsIndex",

      parse : function(d){
        d.title = d.title[0];
        return d
      }

    });

    var AbstractControllerCollection = Backbone.Collection.extend({
      model: AbstractControllerModel,

      comparator: "resultsIndex"
    });


    var AbstractController = BaseWidget.extend({


      initialize : function(options){

        options = options || {};

        this.collection = new AbstractControllerCollection();

        //from the title view mixin
        this.titleView = this.returnNewTitleView();

        this.navView = new AbstractNavView();

        this.listenTo(this.navView, "navigate", this.subPageNavigate)

        //represents the current bibcode (always only 1)
        this._bibcode = undefined;


        if (!options.widgetDict){
          throw new error("page managers need a dictionary of widgets to render")
        }

        this.widgetDict = options.widgetDict

        _.extend(this, API);

        this.history = options.history;

        //      to be explicit, transferring only those widgets considered "sub views" to this dict
        //      allowing abstractSubViews to be set by options for testing purposes
        this.abstractSubViews = options.abstractSubViews ||  {
          //the keys are also the sub-routes
          "abstract": {widget: this.widgetDict.abstract, title:"Abstract", descriptor: "Abstract for:"},
          "references": {widget: this.widgetDict.references, title:"References", descriptor: "References in:", showNumFound : true},
          "citations": {widget: this.widgetDict.citations, title: "Citations", descriptor: "Papers that cite:", showNumFound : true},
          "coreads" : {widget: this.widgetDict.coreads, title: "Co-Reads", descriptor: "Other papers read by those who read:"},
         "tableofcontents" : {widget: this.widgetDict.tableOfContents, title: "Table of Contents", descriptor: "Table of Contents for:", showNumFound : false},
//          "similar": {widget : this.widgetDict.similar, title : "Similar Papers", descriptor : "Papers with similar characteristics to:"}
        };

        this.listenTo(this.titleView, "all", this.onAllInternalEvents)

        options.rows = 40;

        BaseWidget.prototype.initialize.call(this, options);

      },


      onAllInternalEvents : function(ev, arg1, arg2){

        if (ev.indexOf("loadMore")!== -1){
          //from abstract title view mixin
          this.checkLoadMore();
        }

      },

      subPageNavigate : function(route){

        //now, just render the relevant page
        var sub  = route.match(/\/(\w+)$/)[1];
          this.showSubView(sub);
      },


      getMasterQuery : function(){
        return this._masterQuery;
      },


      setCurrentBibcode :function(bib){
        this._bibcode = bib;
        //so view can access it through closure
        currentBibcode = bib;
      },

      //   called by the router

      showPage : function(options){

        var bib = options.bibcode;
        var subPage = options.subPage;
        var inDom = options.inDom;

        this.setCurrentBibcode(bib);


        if (!inDom) {

          this.insertTemplate();
        }

        /*
        * these functions must be called each time
        * this page is rendered, regardless of whether
        * it's been rendered before
        * */
        this.displayTopRow();
        this.displayAbstractNav(subPage);
        this.renderNewBibcode();
        this.loadWidgetData(bib);
        this.showSubView(subPage);

      }

    })

    _.extend(AbstractController.prototype, WidgetPaginationMixin);
    _.extend(AbstractController.prototype, AbstractTitleViewMixin);

    return AbstractController


  })