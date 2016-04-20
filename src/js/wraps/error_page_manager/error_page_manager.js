define([
  'js/page_managers/controller',
  'js/page_managers/one_column_view',
  'hbs!/404',
], function (
    PageManagerController,
    PageManagerView,
    PageManagerTemplate
) {

  var PageManager = PageManagerController.extend({

    createView: function(options) {
      options = options || {};
      return new PageManagerView({template: PageManagerTemplate, className :  "error-page-layout s-100-height"   })
    },



  });
  return PageManager;
});