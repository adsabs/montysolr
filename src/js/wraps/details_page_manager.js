define([
  'js/page_managers/toc_controller',
  'js/page_managers/three_column_view',
  'hbs!js/page_managers/templates/toc-page-layout'
  ], function (
  PageManagerController,
  PageManagerView,
  PageManagerTemplate) {

  var PageManager = PageManagerController.extend({
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
        ret.$el.find('.s-back-button-container').empty().html('<a href="#search/' + this.view.model.get('query') + '" class="back-button"> <i class="fa fa-arrow-left"></i> Back to search results</a>');
      }
      return ret;
    }

  });
  return PageManager;
});