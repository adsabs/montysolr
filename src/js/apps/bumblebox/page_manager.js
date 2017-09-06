define([
  'js/page_managers/controller',
  'js/page_managers/one_column_view',
  'hbs!js/apps/bumblebox/templates/embedded-page'
], function (
  PageManagerController,
  PageManagerView,
  PageManagerTemplate) {

  var PageManager = PageManagerController.extend({

    createView: function(options) {
      options = options || {};
      options.template = PageManagerTemplate;
      return new PageManagerView({template: PageManagerTemplate, className: "s-embedded-layout"})
    }
  });

  return PageManager;

});
