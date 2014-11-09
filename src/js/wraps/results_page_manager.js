define([
  'js/page_managers/controller',
  'js/page_managers/three_column_view',
  'hbs!js/page_managers/templates/results-page-layout'
  ], function (
  PageManagerController,
  PageManagerView,
  PageManagerTemplate) {

  var PageManager = PageManagerController.extend({
    createView: function(options) {
      options = options || {};
      options.template = options.template || PageManagerTemplate;
      return new PageManagerView({template: PageManagerTemplate})
    }
  });
  return PageManager;
});