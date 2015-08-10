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
    }

  });
  return PageManager;
});