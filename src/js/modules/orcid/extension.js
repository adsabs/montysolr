/**
 * Extension for a controller; it adds a functionality that allows a widget
 * to handle ORCID actions.
 *
 * It is installed as return OrcidExtension(WidgetClass)
 */

define([
    'underscore',
    'js/components/api_query',
    'js/components/api_request',
    'js/components/api_query_updater',
    'js/components/api_targets',
    'js/modules/orcid/work',
    'js/mixins/dependon'
  ],

  function (
    _,
    ApiQuery,
    ApiRequest,
    ApiQueryUpdater,
    ApiTargets,
    Work
    ) {

    return function(WidgetClass) {
      var queryUpdater = new ApiQueryUpdater('OrcidExtension');
      var processDocs = WidgetClass.prototype.processDocs;
      var activate = WidgetClass.prototype.activate;
      var onAllInternalEvents = WidgetClass.prototype.onAllInternalEvents;

      WidgetClass.prototype.activate = function(beehive) {
        this.setBeeHive(beehive);
        activate.apply(this, arguments);
      };

      WidgetClass.prototype._getOrcidInfo = function(recInfo) {
        var msg = {actions: {} , provenance : null};

        if (recInfo.isCreatedByOthers && recInfo.isCreatedByADS){

          msg.actions.update = {title: 'update in ORCID', caption:'Update ADS version with latest data', action: 'orcid-update'};
          msg.actions.delete = {title: 'delete from ORCID', caption:'Delete ADS version from ORCID', action: 'orcid-delete'};
          msg.actions.view = {title: 'view in ORCID', caption:'Another version exists (we don\'t have rights to update it)', action: 'orcid-view'};
        }
        else if (recInfo.isCreatedByOthers && !recInfo.isCreatedByADS) {

          if (recInfo.isKnownToADS) {
            msg.actions.add = {title: 'add ADS version ORCID', caption:'ORCID already has a record for this article (we don\'t have rights to update it).', action: 'orcid-add'};
          }
          msg.actions.view = {title: 'view in ORCID', caption:'Another version exists (we don\'t have rights to update it)', action: 'orcid-view'};

        }
        else if (!recInfo.isCreatedByOthers && recInfo.isCreatedByADS) {

          msg.actions.update = {title: 'update in ORCID', caption:'Update ADS version with latest data', action: 'orcid-update'};
          msg.actions.delete = {title: 'delete from ORCID', caption:'Delete ADS version from ORCID', action: 'orcid-delete'};
        }
        else {
          msg.actions.add = {title: 'add to ORCID', caption:'Add ADS version to ORCID', action: 'orcid-add'};
        }

        if (recInfo.isCreatedByADS && recInfo.isCreatedByOthers) {
          msg.provenance = 'ads'; // duplicate, but just to be explicit
        }
        else if (recInfo.isCreatedByADS) {
          msg.provenance = 'ads';
        }
        else if (recInfo.isCreatedByOthers) {
          msg.provenance = 'others';
        }
        else {
          msg.provenance = null;
        }
        return msg;
      },

      WidgetClass.prototype.addOrcidInfo = function(docs) {
        var self = this;
        // add orcid info to the documents
        var orcidApi = this.getBeeHive().getService('OrcidApi');

        if (!orcidApi || !orcidApi.hasAccess()) {
          return docs;
        }

        var recInfo;
        var counter = 0;

        _.each(docs, function(d) {
          recInfo = orcidApi.getRecordInfo(d);
          if (recInfo.state() === 'pending') {
            counter += 1;
            recInfo.done(function(rInfo) {
              counter -= 1;

              var actions = self._getOrcidInfo(rInfo);

              // get the model for this document
              if (self.collection && self.collection.findWhere) {
                var model = self.collection.findWhere({bibcode: d.bibcode});
                if (model) {
                  model.set('orcid', actions); // if not found, we can ignore this update (the view changed already)
                }
              }

              if (counter === 0) {
                self.trigger('orcid-update-finished');
              }
            });
            recInfo.fail(function(data) {
              counter -= 1;

              // very likely, the request timed out
              // keep the actions and let user redo the operation
              if (self.collection && self.collection.findWhere) {
                var model = self.collection.findWhere({bibcode: d.bibcode});
                if (model) {
                  var o = _.extend({}, model.get('orcid') || {});
                  delete o.pending;
                  o.error = 'Orcid API reported error';
                  model.set('orcid', o); //TODO: distinguish different types of errors
                }
              }

              if (counter === 0) {
                self.trigger('orcid-update-finished');
              }
            });
            d.orcid = {pending: true};
          }
          else {
            recInfo.done(function(rInfo) {
              d.orcid = self._getOrcidInfo(rInfo);
              // enhance the ORCID record with an identifier
              // the bibcode, if there, was discovered from our api
              if (!d.identifier && rInfo.bibcode) {
                d.identifier = rInfo.bibcode;
              } else if (!d.identifier && rInfo.doi) {
                d.identifier = rInfo.doi;
              }
            });
          }
        });

        if (counter === 0) {
          self.trigger('orcid-update-finished', docs);
        }

        return docs;
      };

      WidgetClass.prototype.processDocs = function(apiResponse, docs, pagination) {
        var self = this;
        var docs = processDocs.apply(this, arguments);
        var user = this.getBeeHive().getObject('User');
        //for results list only show if orcidModeOn, for orcid big widget show always
        if (user && user.isOrcidModeOn() || this.orcidWidget ){
          var result = this.addOrcidInfo(docs);
          if (pagination.numFound !== result.length) {
            _.extend(pagination, this.getPaginationInfo(apiResponse, docs));
          }
          return result;
        }
        return docs;
      };

      /**
       * Enhances the model with ADS metadata (iff we are coming from orcid)
       * or with Orcid metadata, iff we are coming from ADS. So, it works
       * both ways and can be used in both the search results views and in
       * the pure orcid listings
       *
       * @param model
       * @returns {*}
       */
      WidgetClass.prototype.mergeADSAndOrcidData = function(model) {
        var self = this;
        var final = $.Deferred();
        var oApi = self.getBeeHive().getService('OrcidApi');

        /**
         *
         * @param fullOrcidWork
         * @param adsResponse
         */
        var onRecieveFullADSWork = function (fullOrcidWork, adsResponse) {
          var adsWork = adsResponse.response &&
            adsResponse.response.docs && adsResponse.response.docs[0];

          model.attributes = _.extend(model.attributes,
            fullOrcidWork.toADSFormat(), adsWork);

          final.resolve(model);
        };

        /**
         *
         */
        var onADSFailure = function () {
          console.error('Error retrieving doc from ADS');
          final.reject.apply(final, arguments);
        };

        /**
         *
         */
        var onOrcidFailure = function () {
          console.error('Error retrieving doc from ORCiD');
          final.reject.apply(final, arguments);
        };

        /**
         *
         * @param fullOrcidWork
         */
        var onRecieveFullOrcidWork = function (fullOrcidWork) {
          var identifier = model.get('identifier') ||
            fullOrcidWork.pickIdentifier(['bibcode', 'doi']);
          if (!identifier) {
            throw Error('Unable to determine suitable identifier');
          }

          var q = new ApiQuery({
            q: 'identifier:' + queryUpdater.quoteIfNecessary(identifier),
            fl: 'title,abstract,bibcode,author,pub,pubdate,doi'
          });

          var req = new ApiRequest({
            query: q,
            target: ApiTargets.SEARCH,
            options: {
              done: _.partial(onRecieveFullADSWork, fullOrcidWork),
              fail: onADSFailure
            }
          });

          var pubSub = self.getPubSub();
          pubSub.publish(pubSub.EXECUTE_REQUEST, req);
        };

        var work = model.get('_work');
        if (work) {
          oApi.getWork(work.getPutCode())
            .done(onRecieveFullOrcidWork)
            .fail(onOrcidFailure);
        } else {
          this._findWorkByModel(model)
          .done(function (work) {
            oApi.getWork(work.getPutCode())
            .done(onRecieveFullOrcidWork)
            .fail(onOrcidFailure);
          })
          .fail(onOrcidFailure);
        }

        return final.promise();
      };

      WidgetClass.prototype._findWorkByModel = function (model) {
        var $dd = $.Deferred();
        var oApi = this.getBeeHive().getService('OrcidApi');
        var exIds = _.pick(model.attributes, ['bibcode', 'doi']);
        var oldOrcid = _.clone(model.get('orcid') || {});
        var profile = oApi.getUserProfile();

        profile.done(function (profile) {
          var works = profile.getWorks();
          var matchedWork = _.find(works, function (w) {
            var wIds = w.getExternalIds();
            return exIds.bibcode === wIds.bibcode ||
              exIds.doi === wIds.doi || exIds.doi[0] === wIds.doi;
          });
          if (matchedWork) {
            $dd.resolve(matchedWork);
          } else {
            $dd.reject();
            var msg = 'Could not find a matching ORCiD Record';
            console.error.apply(console, [msg].concat(arguments));
            model.set('orcid', _.extend(oldOrcid, {
              pending: null,
              error: msg
            }));
          }
        });

        profile.fail(function () {
          $dd.reject.apply($dd, arguments);
          var msg = 'Error retrieving ORCiD profile';
          console.error.apply(console, [msg].concat(arguments));
          model.set('orcid', _.extend(oldOrcid, {
            pending: null,
            error: msg
          }));
        });

        return $dd.promise();
      };

      WidgetClass.prototype.onAllInternalEvents = function(ev, arg1, arg2) {
        if (ev === 'childview:OrcidAction') {
          var self = this;
          var data = arg2;
          var action = data.action;
          var orcidApi = this.getBeeHive().getService('OrcidApi');
          var oldOrcid = _.clone(data.model.get('orcid') || {});

          var handlers = {
            'orcid-add': function (model) {

              model.set('orcid', {pending: true});

              var onAddComplete = function () {
                self._findWorkByModel(model)
                .done(function (work) {

                  // we should be able to assume some record info
                  var recInfo = {
                    isCreatedByADS: true,
                    isCreatedByOthers: false,
                    isKnownToADS: true,
                    provenance: 'ads'
                  };

                  model.set('orcid', self._getOrcidInfo(recInfo));
                  model.set('source_name', work.getSourceName());
                  self.trigger('orcidAction:' + action, model);
                });
              };

              var doAdd = function doAdd(adsWork) {
                orcidApi.addWork(adsWork)
                .done(onAddComplete)
                .fail(function addFailed (xhr, error, state) {

                  // there is a conflicting record, update record
                  if (state === 'CONFLICT') {
                    return onAddComplete();
                  }

                  var msg = 'Failed to add entry, please try again';
                  console.error.apply(console, [msg].concat(arguments));
                  model.set('orcid', _.extend(oldOrcid, {
                    pending: null,
                    error: msg
                  }));
                })
              };

              var newWork = Work.adsToOrcid(model.attributes);
              if (newWork) {
                doAdd(newWork);
              } else {
                var msg = 'There was a problem adding the record, try again';
                console.error.apply(console, [msg].concat(arguments));
                model.set('orcid', _.extend(oldOrcid, {
                  pending: null,
                  error: msg
                }));
              }
            },
            'orcid-delete': function (model) {

              model.set('orcid', {pending: true});

              var performDelete = function (work) {
                orcidApi.deleteWork(work.getPutCode())
                .done(function deleteSuccess() {

                  // Remove entry from collection after delete
                  if (self.orcidWidget) {
                    self.collection.reset();
                    self.hiddenCollection.reset();
                    self.onShow();
                  } else {
                    // reset orcid actions
                    model.set('orcid', self._getOrcidInfo({}));
                  }
                  self.trigger('orcidAction:' + action, model);
                })

                .fail(function deleteFail() {
                  var msg = 'Error deleting record, please try again';
                  console.error.apply(console, [msg].concat(arguments));
                  model.set('orcid', _.extend(oldOrcid, {
                    pending: null,
                    error: msg
                  }));
                });
              };

              var work = model.get('_work');
              if (work) {
                performDelete(work);
              } else {
                self._findWorkByModel(model).done(performDelete);
              }
            },
            'orcid-update': function (model) {

              model.set('orcid', {pending: true});

              self.mergeADSAndOrcidData(model)

              // done merging, begin update
              .done(function (model) {
                var putCode = model.get('_work').getPutCode();

                orcidApi.updateWork(Work.adsToOrcid(model.attributes, putCode))

                // update successful
                .done(function doneUpdating(orcidWork) {
                  var work = new Work(orcidWork);
                  orcidApi.getRecordInfo(work.toADSFormat())

                  // done getting record info, update model
                  .done(function doneGettingRecordInfo(recInfo) {
                    model.set('orcid', self._getOrcidInfo(recInfo));
                    self.trigger('orcidAction:' + action, model);
                  });
                })

                // update failed, update model accordingly
                .fail(function failedUpdating() {
                  var msg = 'Error updating record, please try again';
                  console.error.apply(console, [msg].concat(arguments));
                  model.set('orcid', _.extend(oldOrcid, {
                    pending: null,
                    error: msg
                  }));
                });

                // update the model with the updated data
                model.set(model.attributes, {silent: true});
              })

              // merging failed, update the model
              .fail(function failedMerging() {
                var msg = 'Failed to merge ORCiD and ADS data';
                console.error.apply(console, [msg].concat(arguments));
                model.set('orcid', _.extend(oldOrcid, {
                  pending: null,
                  error: msg
                }));
              });
            },
            'orcid-login': function () {
              orcidApi.signIn();
            },
            'orcid-logout': function () {
              orcidApi.signOut();
            },
            'orcid-view': function (model) {
              // do nothing for now
            }
          };
          handlers[action] && handlers[action](data.model);
        } else {
          return onAllInternalEvents.apply(this, arguments);
        }
      };

      return WidgetClass;
    }

  });
