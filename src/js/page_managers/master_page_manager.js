/*
* keeps track of what is in the DOM
* calls the showPage method of the proper page manager
* TODO: publish events notifying interested widgets of scroll events (?)
* TODO: handle page transitions (?)
*
*
* */

define([
  'js/widgets/base/base_widget',
  'js/components/api_query_updater',
  'js/components/api_query'
], function(
  BaseWidget,
  ApiQueryUpdater,
  ApiQuery
  ){

  /*
  * Expects in its options a dictionary in the following form:
  *
  * {pageControllers : {'search': SearchPageManager, 'index': LandingPageManager}}
  *
  *
  * */

  var MasterManager = BaseWidget.extend({

    initialize : function(options){

      options = options || {};

      this.pageControllers = options.pageControllers;

      if (!this.pageControllers){
        throw new Error("Page manager wasn't given any page controllers to work with")
      }

      _.each(this.pageControllers, function(c){

        if (c.controllerView && c.controllerView.render){
          c.controllerView.render();
        }
      });

      this.currentPage = undefined;

    },

    activate : function(beehive){

    this.pubsub = beehive.Services.get('PubSub');

      _.bindAll(this, "showSearchPage", "insertAriaAnnouncement");

    this.pubsub.subscribe(this.pubsub.START_SEARCH, this.showSearchPage);

    this.pubsub.subscribe(this.pubsub.ARIA_ANNOUNCEMENT, this.insertAriaAnnouncement);


    },

    insertAriaAnnouncement : function(text){

      var $ac;

      $ac = this.ariaAnnouncementContainer || $("#aria-announcement-container");

      $ac.html(text);

    },

    showPage : function(pageName, options){

      options = options || {};

      options.inDom = (this.currentPage === pageName) ? true : false;

      if (!options.inDom){
        //scroll up automatically
        window.scrollTo(0,0);
      }
        this.currentPage = pageName;

        this.pageControllers[pageName].showPage(options);
  },

    showSearchPage:  function(apiQuery){

      /*
      * takes care of navigating to the proper url
      * */

      var urlToShow = ApiQueryUpdater.prototype.clean(apiQuery);

      var path = "search/" + urlToShow.url();

      var args = {}

      args.path = path;

      //first, navigate to proper URL
      this.pubsub.publish(this.pubsub.NAVIGATE_WITHOUT_TRIGGER,  args);

      //then show results page
      this.showPage("results")
    }

  })

  return MasterManager;

})