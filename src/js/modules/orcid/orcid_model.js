/**
 * this is the main datastructure holding to data we know about
 * the user (who logged in with ORCID) and also it contains
 * the papers that were/will be claimed
 */

define([
    'backbone',
    'underscore'
  ],
  function (Backbone, _) {
    var OrcidModel = Backbone.Model.extend({
      initialize: function () {

        // recompute map of putCode and adsId
        this.on("change:orcidProfile", function (model) {

          var orcidWorks = model.get("orcidProfile")["orcid-activities"]["orcid-works"];

          //XXX:rca - error handling is missing? default values?
          var adsIds =
            orcidWorks["orcid-work"]
              .map(function (elem) {
                if (!elem["work-external-identifiers"])
                  return undefined;

                var isAdsId = function (e) {
                  //work-external-identifier-id: "ads:1234"
                  //work-external-identifier-type: "other-id"

                  return e['work-external-identifier-type'] == 'other-id' && e['work-external-identifier-id'].indexOf('ads:') == 0;
                };

                var identifiers = elem["work-external-identifiers"]["work-external-identifier"];
                var adsId = undefined;

                if (identifiers instanceof Array) {
                  var ids = identifiers.filter(isAdsId);
                  if (ids.length > 0)
                    adsId = ids[0];
                }
                else if (isAdsId(identifiers)) {
                  adsId = identifiers;
                }
                else {
                  return undefined;
                }

                return {
                  putCode: elem.$['put-code'],
                  adsId: adsId['work-external-identifier-id']
                };
              })
              .filter(function (el) {
                return el != undefined && el.adsId != undefined;
              });

          model.set('adsIdsWithPutCodeList', adsIds);
        });
      },

      defaults: function () {
        return {
          actionsVisible: false,
          orcidProfile: {'orcid-activities': {'orcid-works': {'orcid-work': []}}},
          adsIdsWithPutCodeList: [],
          bulkInsertWorks: [],
          isInBulkInsertMode: false
        };
      }
    });
    return OrcidModel;

  });