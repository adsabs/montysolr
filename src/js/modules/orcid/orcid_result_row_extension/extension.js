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
        switch (msg.msgType) {
          case OrcidApiConstants.Events.IsBulkInsertMode:
            this.view.children.call('resetToggle');
            break;
          case OrcidApiConstants.Events.UserProfileRefreshed:
            this.view.children.call('showOrcidActions', this.orcidModelNotifier.isWorkInCollection);
          case OrcidApiConstants.Events.LoginSuccess:

            this.view.children.call('showOrcidActions', this.orcidModelNotifier.isWorkInCollection);

            break;
          case OrcidApiConstants.Events.SignOut:
            this.view.children.call('hideOrcidActions');
            break;
        }
      }
    };


    return ResultsExtension;

  }
);