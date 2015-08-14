define([
  'js/page_managers/controller',
  'js/page_managers/one_column_view',
  'hbs!./templates/orcid-page-layout'
], function (
  PageManagerController,
  PageManagerView,
  PageManagerTemplate) {

  var PageManager = PageManagerController.extend({
    createView: function(options) {
      options = options || {};
      options.template = options.template || PageManagerTemplate;
      return new PageManagerView({template: PageManagerTemplate})
    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.debug = beehive.getDebug(); // XXX:rca - think of st better
      this.view = this.createView({debug : this.debug, widgets: this.widgets});
    }

  });
  return PageManager;
});