/**
 * Keeps track of changes to the ORCID model.
 * This component should be loaded as a service with name 'OrcidModelNotifier'
 * ie.
 *
 * services: {
 *   OrcidModelNotifier: 'js/modules/orcid/orcid_model_notifier'
 *   }
 */

define([
    'underscore',
    'marionette',
    'backbone',
    'js/components/generic_module',
    'js/modules/orcid/orcid_api_constants',
    './orcid_model'
  ],

  function (_, Marionette, Backbone, GenericModule, OrcidApiConstants, OrcidModel) {
    var OrcidNotifierModule = GenericModule.extend({

      initialize: function (options) {
        this.model = new OrcidModel();
      },


      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub').getHardenedInstance();

        _.bindAll(this, 'routeOrcidPubSub', 'isOrcidItemAdsItem', 'isWorkInCollection', 'getAdsIdsWithPutCodeList',
          'bulkInsert', 'cancelBulkInsert', 'triggerBulkInsert', 'addToBulkWorks', 'removeFromBulkWorks');

        this.pubsub.subscribe(this.pubsub.ORCID_ANNOUNCEMENT, this.routeOrcidPubSub);
      },


      routeOrcidPubSub: function (msg) {
        switch (msg.msgType) {
          case OrcidApiConstants.Events.LoginSuccess:
            this.model.set('actionsVisible', true);
            break;

          case OrcidApiConstants.Events.SignOut:
            this.model.set('actionsVisible', false);
            break;

          case OrcidApiConstants.Events.UserProfileRefreshed:
            this.model.set('orcidProfile', msg.data);
            break;
        }
      },

      bulkInsert: function (adsWorks) {
        this.pubsub.publish(this.pubsub.ORCID_ANNOUNCEMENT,
          {
            msgType: OrcidApiConstants.Events.OrcidAction,
            data: {
              actionType: 'bulkInsert', model: adsWorks
            }
          });
      },
      cancelBulkInsert: function () {
        this.model.set('isInBulkInsertMode', false);
        this.model.set('bulkInsertWorks', []);

        this.pubsub.publish(this.pubsub.ORCID_ANNOUNCEMENT, {msgType: OrcidApiConstants.Events.IsBulkInsertMode, data: false});
      },
      startBulkInsert: function () {
        this.model.set('isInBulkInsertMode', true);

        this.pubsub.publish(this.pubsub.ORCID_ANNOUNCEMENT, {msgType: OrcidApiConstants.Events.IsBulkInsertMode, data: true});
      },
      triggerBulkInsert: function () {
        this.bulkInsert(this.model.attributes.bulkInsertWorks);
        this.model.set('isInBulkInsertMode', false);

        this.pubsub.publish(this.pubsub.ORCID_ANNOUNCEMENT, {msgType: OrcidApiConstants.Events.IsBulkInsertMode, data: false});


        this.model.set('bulkInsertWorks', []);
      },
      addToBulkWorks: function (adsWork) {
        if (this.isWorkInCollection(adsWork)) {
          return;
        }

        this.model.attributes.bulkInsertWorks.push(adsWork);
      },
      removeFromBulkWorks: function (adsWork) {
        var toRemove =
          this.model.attributes.bulkInsertWorks.filter(function (item) {
            return item.id == adsWork.id;
          })[0];

        this.model.attributes.bulkInsertWorks.splice(toRemove, 1);
      },

      getAdsIdsWithPutCodeList: function () {
        return this.model.get('adsIdsWithPutCodeList');
      },
      isWorkInCollection: function (adsItem) {
        var adsIdsWithPutCode = this.model.get('adsIdsWithPutCodeList');
        var formattedAdsId = "ads:" + adsItem.id;

        return adsIdsWithPutCode
          .filter(function (e) {
            return e.adsId == formattedAdsId;
          })
          .length > 0;
      },
      isOrcidItemAdsItem: function (orcidItem) {
        return orcidItem.workExternalIdentifiers.filter(function (e) {
          return e.type == 'other-id' && e.id.indexOf('ads:') == 0;
        }).length > 0;
      },

      //XXX:rca - big problem
      getHardenedInstance: function () {
        return this;
      }

    });

    return OrcidNotifierModule;
  });
