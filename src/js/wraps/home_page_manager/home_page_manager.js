define([
  'js/page_managers/toc_controller',
  'js/page_managers/three_column_view',
  'hbs!js/wraps/home_page_manager/home-page-layout',
  'hbs!js/wraps/home_page_manager/home-nav'
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
      return new PageManagerView({template: PageManagerTemplate, className :  "s-home-layout s-100-height",  id : "home-layout"  })
    },

    navConfig : [],


  });
  return PageManager;
});
