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
      this.setBeeHive(beehive);
      this.debug = beehive.getDebug(); // XXX:rca - think of st better
      this.view = this.createView({debug : this.debug, widgets: this.widgets});
    },

    navConfig : {
      UserPreferences__librarylink : {"title": "Library Link Server", "path":"user/settings/librarylink","category":"preferences" },
      UserPreferences__orcid : {"title": "ORCID Settings", "path":"user/settings/orcid","category":"preferences" },
      UserSettings__email : {"title": "Change Email", "path":"user/settings/email","category":"settings"},
      UserSettings__password : {"title": "Change Password", "path":"user/settings/password","category":"settings"},
      UserSettings__token : {"title": "API Token", "path":"user/settings/token","category":"settings"},
      UserSettings__delete : {"title": "Delete Account", "path":"user/settings/delete","category":"settings"}
    }

  });
  return PageManager;
});
