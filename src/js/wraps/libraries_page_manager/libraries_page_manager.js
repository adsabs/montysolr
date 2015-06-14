define([
  'js/page_managers/toc_controller',
  'js/page_managers/toc_widget',
  'js/page_managers/three_column_view',
  'hbs!./libraries-page-layout',
  'hbs!./libraries-nav'
], function (
  TOCController,
  TOCView,
  PageManagerView,
  PageManagerTemplate,
  TOCTemplate
  ) {

  var PageManager = TOCController.extend({

    TOCTemplate : TOCTemplate,

    TOCEvents :  _.extend(TOCView.prototype.events, {
      "click a[href='/#/user/libraries']" : function(){
        this.trigger("page-manager-event", "navigate", {navCommand : "AllLibrariesWidget" , sub : "libraries"});
      },

      "click button.create-library" : function(){
        function createLib(){
          var that = this;
          this.beehive.getObject("LibraryController")
            .createLibrary()
            .done(function(data){
              that.pubsub.publish(that.pubsub.NAVIGATE, "IndividualLibraryWidget", {sub: "library", id : data.id});
            });
        }
        this.trigger("page-manager-event", "apply-function", {func : createLib});
      }
    }),

    createView: function(options) {
      options = options || {};
      options.template = options.template || PageManagerTemplate;

      return new PageManagerView({
        template: PageManagerTemplate,
        className :  "s-libraries-layout s-100-height",
        id : "libraries-layout"
      });


    },

    activate: function (beehive) {
      this.beehive = beehive;
      this.pubsub = beehive.getHardenedInstance().Services.get('PubSub');
      this.view = this.createView({debug : this.debug, widgets: this.widgets});
      this.debug = beehive.getDebug(); // XXX:rca - think of st better
    }

  });
  return PageManager;
});