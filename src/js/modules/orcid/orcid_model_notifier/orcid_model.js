define([
    'backbone',
    'underscore',
    'js/modules/orcid/array_extensions',
  ],
  function(
    Backbone,
    _,
    ArrayExtensions
  ) {
    var OrcidModel = Backbone.Model.extend({
      initialize: function(){
        this.on("change:orcidProfile", function(model){
          // recompute map of putCode and adsId

          var orcidWorks = model.get("orcidProfile")["orcid-activities"]["orcid-works"];


          var adsIds =
            orcidWorks["orcid-work"]
              .map(function (e) {
                if (!e["work-external-identifiers"])
                  return undefined;

                var isAdsId = function (e) {
                  //work-external-identifier-id: "ads:1234"
                  //work-external-identifier-type: "other-id"

                  return e['work-external-identifier-type'] == 'other-id' && e['work-external-identifier-id'].indexOf('ads:') == 0;
                };

                var identifiers = e["work-external-identifiers"]["work-external-identifier"]
                var adsId = undefined;

                if (identifiers instanceof Array) {
                  var ids = identifiers.filter(isAdsId);
                  if (ids.length > 0)
                    adsId = ids[0];
                }
                else if (isAdsId(identifiers)) {
                  adsId = identifiers;
                }
                else{
                  return undefined;
                }

                return {
                  putCode: e.$['put-code'],
                  adsId: adsId['work-external-identifier-id']
                };
              })
              .filter(function(e){
                return e != undefined && e.adsId != undefined;
              });

          model.set('adsIdsWithPutCodeList', adsIds);
        });
      },

      defaults: function(){
        return {
          actionsVisible: false,
          orcidProfile : [],

          adsIdsWithPutCodeList: [],

          bulkInsertWorks: [],

          isInBulkInsertMode: false
        };
      }
    });

    _.extend(OrcidModel.prototype, {
      addToBulkWorks: function(adsWork){
        if (this.isWorkInCollection(adsWork))
        {
          return;
        }

        this.attributes.bulkInsertWorks.push(adsWork);
      },

      removeFromBulkWorks: function(adsWork){
        var toRemove =
          this.attributes.bulkInsertWorks.filter(function (item) {
            return item.id == adsWork.id;
          })[0];

        this.attributes.bulkInsertWorks.splice(toRemove, 1);
      },

      cancelBulkInsert: function(){
        this.set('isInBulkInsertMode', false);
        this.set('bulkInsertWorks', []);
      },

      triggerBulkInsert: function(){
        this.trigger('bulkInsert', this.attributes.bulkInsertWorks);
        this.set('isInBulkInsertMode', false);
        this.set('bulkInsertWorks', []);
      },

      isWorkInCollection : function(adsItem){
        var adsIdsWithPutCode = this.get('adsIdsWithPutCodeList');
        var formattedAdsId = "ads:" + adsItem.id;

        return adsIdsWithPutCode
          .filter(function(e){
            return e.adsId == formattedAdsId;
          })
          .length > 0;
      },
      isOrcidItemAdsItem: function (orcidItem) {
        return orcidItem.workExternalIdentifiers.filter(function (e) {
            return e.type == 'other-id' && e.id.indexOf('ads:') == 0  ;
          }).length > 0;
      }
    });

    _.extend(Array.prototype, ArrayExtensions);

    return new OrcidModel();

  });