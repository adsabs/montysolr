define([
  'js/page_managers/toc_controller',
  'js/page_managers/three_column_view',
  'hbs!./user-settings-layout',
  'hbs!./user_nav'
], function (
  PageManagerController,
  PageManagerView,
  PageManagerTemplate,
  TOCTemplate
  ) {

  var PageManager = PageManagerController.extend({

    TOCTemplate : TOCTemplate,

    createView: function(options) {
      options = options || {};
      options.template = options.template || PageManagerTemplate;
      return new PageManagerView({template: PageManagerTemplate, className :  "s-user-settings-page-layout s-100-height",  id : "user-page-layout"  })
    },

    activate: function (beehive) {
      this.beehive = beehive;
      this.pubsub = beehive.getHardenedInstance().Services.get('PubSub');
      this.debug = beehive.getDebug(); // XXX:rca - think of st better
      this.view = this.createView({debug : this.debug, widgets: this.widgets});
    }

  });
  return PageManager;
});