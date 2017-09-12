define([
  'js/page_managers/controller',
  'js/page_managers/one_column_view',
  'hbs!js/wraps/orcid-instructions-page-manager/orcid-instructions',
  'bootstrap'
], function (
    PageManagerController,
    PageManagerView,
    PageManagerTemplate,
    Bootstrap
) {

  var PageManager = PageManagerController.extend({

    createView: function(options) {
      options = options || {};
      options.template = PageManagerTemplate;
      return new PageManagerView({template: PageManagerTemplate, className :  "orcid-info" })
    }
  });

  return PageManager;

});
