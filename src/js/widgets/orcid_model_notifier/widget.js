define([
    'underscore',
    'marionette',
    'backbone',
    'js/widgets/base/base_widget',
    'js/services/orcid_api_constants',
    './orcid_model'
  ],

  function (_,
            Marionette,
            Backbone,
            BaseWidget,
            OrcidApiConstants,
            OrcidModel
  ) {
    var OrcidNotifier = BaseWidget.extend({
      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        _.bindAll(this, 'routeOrcidPubSub');

        this.pubsub.subscribe(this.pubsub.ORCID_ANNOUNCEMENT, this.routeOrcidPubSub);
      },

      initialize: function(options){
        _.bindAll(this, 'bulkInsert');

        OrcidModel.on('bulkInsert', this.bulkInsert);
      },

      bulkInsert : function(adsWorks){
        this.pubsub.publish(this.pubsub.ORCID_ANNOUNCEMENT,
          {
            msgType:OrcidApiConstants.Events.OrcidAction,
            data: {
              actionType:'bulkInsert', model: adsWorks
            }
          });
      },

      routeOrcidPubSub : function(msg){
        switch (msg.msgType){
          case OrcidApiConstants.Events.LoginSuccess:

            OrcidModel.set('actionsVisible', true);

            break;
          case OrcidApiConstants.Events.SignOut:
            OrcidModel.set('actionsVisible', false);
            break;

          case OrcidApiConstants.Events.UserProfileRefreshed:
            OrcidModel.set('orcidProfile', msg.data);
            break;
        }
      }

    });

    return OrcidNotifier;
  });
