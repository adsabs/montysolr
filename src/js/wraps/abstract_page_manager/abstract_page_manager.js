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

    TOCTemplate : TOCTemplate,

    createView: function(options) {
      options = options || {};
      options.template = options.template || PageManagerTemplate;
      return new PageManagerView({template: PageManagerTemplate})
    },

    activate: function (beehive) {
      this.pubsub = beehive.getHardenedInstance().Services.get('PubSub');
      this.debug = beehive.getDebug(); // XXX:rca - think of st better
      this.view = this.createView({debug : this.debug, widgets: this.widgets});
      this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, _.bind(this.addQuery, this));
      this.pubsub.subscribe(this.pubsub.DISPLAY_DOCUMENTS, _.bind(this.onDisplayDocuments, this));

    },

    // xxx:rca - this is just a quick hack (the best solution would be to have
    // the page manager re-render view (but only the non-widget parts of it)
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
    }


  });
  return PageManager;
});