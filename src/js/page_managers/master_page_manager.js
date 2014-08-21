/*
* keeps track of what is in the DOM
* calls the showPage method of the proper page manager
* TODO: publish events notifying interested widgets of scroll events (?)
* TODO: handle page transitions (?)
*
*
* */

define(['js/widgets/base/base_widget'], function(BaseWidget){

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

      this.currentPage = undefined;

    },

    activate : function(beehive){

    this.pubsub = beehive.Services.get('PubSub');

      _.bindAll(this, "showSearchPage");

    this.pubsub.subscribe(this.pubsub.START_SEARCH, this.showSearchPage);

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
      console.log("showing search page")
      //first, navigate to proper URL
      this.pubsub.publish(this.pubsub.NAVIGATE_WITHOUT_TRIGGER,apiQuery.url())

      //then show results page
      this.showPage("results")
    }

  })

  return MasterManager;

})