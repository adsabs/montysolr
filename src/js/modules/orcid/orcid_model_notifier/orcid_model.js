define([
    'backbone',
    'underscore'
  ],
  function(
    Backbone,
    _) {
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

                var identifiers = e["work-external-identifiers"]["work-external-identifier"];
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
    return OrcidModel;

  });