define([
    'underscore',
    'js/modules/orcid/orcid_api_constants',

  ],

  function (_,
            OrcidApiConstants
  ) {
    var ResultsExtension = {

      activateResultsExtension: function(beehive){
        this.orcidModelNotifier = beehive.getService('OrcidModelNotifier');

        // 'this' should already contain the pubsub
        _.bindAll(this, 'routeOrcidPubSub');
        this.pubsub.subscribe(this.pubsub.ORCID_ANNOUNCEMENT, this.routeOrcidPubSub);

        var that = this;

        this.orcidModelNotifier.model.on('change:isInBulkInsertMode',
          function(){
            that.view.children.call('resetToggle');
          }
        );

        this.orcidModelNotifier.model.on('change:orcidProfile',
          function(){
            that.view.children.call('showOrcidActions', that.orcidModelNotifier.isWorkInCollection);
          });

        this.orcidModelNotifier.model.on('change:actionsVisible',
          function(e){
            if(e.get('actionsVisible')){
              that.view.children.call('showOrcidActions', that.orcidModelNotifier.isWorkInCollection);
            } else{
              that.view.children.call('hideOrcidActions');
            }
          }
        );

        this.listenTo(this.view, 'composite:collection:rendered',
          function() {
            if (that.orcidModelNotifier.model.get('actionsVisible'))
              that.view.children.call('showOrcidActions', that.orcidModelNotifier.isWorkInCollection);
          });

        this.listenTo(this.view, "all", this.onAllInternalEvents_results);


      },

      onAllInternalEvents_results: function(ev, arg1, arg2) {
        if (ev == 'itemview:OrcidAction'){
          this.pubsub.publish(this.pubsub.ORCID_ANNOUNCEMENT, {msgType: OrcidApiConstants.Events.OrcidAction, data: arg2});
        } else if (ev == 'itemview:toggleSelect'){
          if (this.orcidModelNotifier.model.get('isInBulkInsertMode')){
            arg2.selected
              ? this.orcidModelNotifier.addToBulkWorks(arg2.data)
              : this.orcidModelNotifier.removeFromBulkWorks(arg2.data);
          }
        }
      },


      routeOrcidPubSub: function (msg) {

      }
    };


    return ResultsExtension;

  }
);