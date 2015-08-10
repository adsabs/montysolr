/*
 * A generic class that holds the application storage
 */

define([
  'backbone',
  'underscore',
  'js/components/api_query',
  'js/mixins/hardened',
  'js/mixins/dependon'
], function(
  Backbone,
  _,
  ApiQuery,
  Hardened,
  Dependon
  ) {


  var AppStorage = Backbone.Model.extend({

      activate: function(beehive) {
        this.setBeeHive(beehive);
        _.bindAll(this, "onPaperSelection", "onBulkPaperSelection");
        var pubsub = this.getPubSub();
        pubsub.subscribe(pubsub.PAPER_SELECTION, this.onPaperSelection);
        pubsub.subscribe(pubsub.BULK_PAPER_SELECTION, this.onBulkPaperSelection);
      },

      initialize: function() {
        var that = this;
        this.on('change:selectedPapers', function(model) {
          this._updateNumSelected();
          if (this.hasPubSub())
            var pubsub = this.getPubSub();
            pubsub.publish(pubsub.STORAGE_PAPER_UPDATE, this.getNumSelectedPapers());
        });
      },

      /**
       * functions related to remembering/removing the
       * current query object
       * @param apiQuery
       */
      setCurrentQuery: function(apiQuery) {
        if (!apiQuery)
          apiQuery = new ApiQuery();

        if (!(apiQuery instanceof ApiQuery)) {
          throw new Error('You must save ApiQuery instance');
        }
        this.set('currentQuery', apiQuery);
      },

      setCurrentNumFound : function(numFound){
        this.set("numFound", numFound);
      },

      getCurrentQuery: function() {
        return this.get('currentQuery');
      },

      hasCurrentQuery: function() {
        return this.has('currentQuery');
      },

      /**
       * Functions to remember save bibcodes (that were
       * selected by an user)
       *
       * @returns {*}
       */
      hasSelectedPapers: function() {
        return !!_.keys(this.get('selectedPapers')).length;
      },

      getSelectedPapers: function() {
        return _.keys(this.get('selectedPapers') || {});
      },

      clearSelectedPapers : function(){
         this.set("selectedPapers", {});
      },

      addSelectedPapers: function(identifiers) {
        var data = _.clone(this.get('selectedPapers') || {});
        var updated = false;

        if (_.isArray(identifiers)) {
          _.each(identifiers, function(idx) {
            if (!data[idx]) {
              data[idx] = true;
              updated = true;
            }
          })
        }
        else {
          if (!data[identifiers]) {
            data[identifiers] = true;
            updated = true;
          }
        }
        if (updated)
          this.set('selectedPapers', data);
      },

      isPaperSelected: function(identifier) {
        if (_.isArray(identifier))
          throw new Error('Identifier must be a string or a number');
        var data = this.get('selectedPapers') || {};
        return data[identifier] ? true : false;
      },

      removeSelectedPapers: function(identifiers) {
        var data = _.clone(this.get('selectedPapers') || {});
        if (_.isArray(identifiers)) {
          _.each(identifiers, function(i) {
            delete data[i];
          })
        }
        else if (identifiers) {
          delete data[identifiers];
        }
        else {
          data = {};
        }
        this.set('selectedPapers', data);
      },

      getNumSelectedPapers: function() {
        return this._numSelectedPapers || 0;
      },

      _updateNumSelected: function() {
        this._numSelectedPapers = _.keys(this.get('selectedPapers') || {}).length;
      },

      onPaperSelection: function(data) {
        if (this.isPaperSelected(data)) {
          this.removeSelectedPapers(data);
        }
        else {
          this.addSelectedPapers(data);
        }
      },

      onBulkPaperSelection : function(bibs){
        this.addSelectedPapers(bibs);
      },

      //this is used by the auth and user settings widgets
      setConfig : function(conf){
        this.set("dynamicConfig", conf);
     },

      getConfigCopy : function(){
        return JSON.parse(JSON.stringify(this.get("dynamicConfig")));
      },

      hardenedInterface:  {
        getNumSelectedPapers: 'getNumSelectedPapers',
        isPaperSelected: 'isPaperSelected',
        hasSelectedPapers: 'hasSelectedPapers',
        getSelectedPapers: 'getSelectedPapers',
        clearSelectedPapers: 'clearSelectedPapers',
        getCurrentQuery: 'getCurrentQuery',
        hasCurrentQuery: 'hasCurrentQuery',
        getConfigCopy : 'get read-only copy of dynamic config',
        set : 'set a value into app storage',
        get : 'get a val from app storage'
      }
    }
  );

  _.extend(AppStorage.prototype, Hardened, Dependon.BeeHive);
  return AppStorage;

});