define([
  'js/page_managers/controller',
  'js/page_managers/one_column_view',
  'hbs!js/page_managers/templates/authentication-page-layout'
], function (
  PageManagerController,
  PageManagerView,
  PageManagerTemplate) {

  var PageManager = PageManagerController.extend({

    createView: function(options) {
      options = options || {};
      options.template = options.template || PageManagerTemplate;
      return new PageManagerView({template: PageManagerTemplate, className :  "s-authentication-page-layout s-100-height",  id : "user-page-layout"  })
    }
  });
  return PageManager;
});