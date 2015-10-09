define([
  'js/page_managers/toc_controller',
  'js/page_managers/three_column_view',
  'hbs!./abstract-page-layout',
  'hbs!./abstract-nav'
  ], function (
  PageManagerController,
  PageManagerView,
  PageManagerTemplate,
  TOCTemplate
  ) {

  var PageManager = PageManagerController.extend({

    persistentWidgets : ["SearchWidget", "ShowAbstract", "ShowCitations", "ShowReferences", "tocWidget"],

    TOCTemplate : TOCTemplate,

    createView: function(options) {
      options = options || {};
      options.template = options.template || PageManagerTemplate;
      return new PageManagerView({template: PageManagerTemplate})
    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.debug = beehive.getDebug(); // XXX:rca - think of st better
      this.view = this.createView({debug : this.debug, widgets: this.widgets});
      var pubsub = this.getPubSub();
      pubsub.subscribe(pubsub.DISPLAY_DOCUMENTS, _.bind(this.onDisplayDocuments, this));

    },

    assemble: function(app) {
      PageManagerController.prototype.assemble.apply(this, arguments);
      var storage = app.getObject('AppStorage');
      if (storage && storage.hasCurrentQuery())
        this.addQuery(storage.getCurrentQuery());
    },

    addQuery: function(apiQuery) {
      if (this.view.model)
        this.view.model.set('query', apiQuery.url());
    },

    show: function(pageName){
      var ret = PageManagerController.prototype.show.apply(this, arguments);
      if (this.view.model && this.view.model.has('query')) {
        ret.$el.find('.s-back-button-container').empty().html('<a href="#search/' + this.view.model.get('query') + '" class="back-button btn btn-sm btn-default"> <i class="fa fa-arrow-left"></i> Back to results</a>');
      }
      return ret;
    },

    onDisplayDocuments : function(apiQuery){
      var bibcode = apiQuery.get('q');
      if (bibcode.length > 0 && bibcode[0].indexOf('bibcode:') > -1) {
        bibcode = bibcode[0].replace('bibcode:', '');
        this.widgets.tocWidget.model.set("bibcode", bibcode);
      };
    },
    
    navConfig : {
      ShowAbstract : {"title": "Abstract", "path":"abstract", "showCount": false, "isSelected":true, "category":"view","alwaysThere":"true"},
      ShowCitations : {"title": "Citations", "path":"citations", "category":"view"},
      ShowReferences : {"title": "References", "path":"references", "category":"view"},
      ShowCoreads : {"title": "Co-Reads", "path":"coreads","category":"view", "showCount": false},
      ShowTableOfContents : {"title": "Volume Content", "path":"tableofcontents", "category":"view", "showCount": false},
      ShowSimilar : {"title": "Similar Papers", "path":"similar", "category":"view"},
      ShowGraphics : {"title": "Graphics", "path":"graphics", "showCount": false, "category":"view"},
      ShowMetrics : {"title": "Metrics", "path":"metrics", "showCount": false, "category":"view"},
      ShowPaperExport__bibtex : {"title": "in BibTEX", "path":"export/bibtex", "category":"export", "alwaysThere":"true"},
      ShowPaperExport__aastex : {"title": "in AASTex", "path":"export/aastex", "category":"export", "alwaysThere":"true"},
      ShowPaperExport__endnote : {"title": "in Endnote", "path":"export/endnote", "category":"export", "alwaysThere":"true"}
    }


  });
  return PageManager;
});