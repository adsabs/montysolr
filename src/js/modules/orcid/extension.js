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

      WidgetClass.prototype.activate = function (beehive) {
        this.setBeeHive(beehive);
        activate.apply(this, arguments);
        var pubsub = beehive.hasService('PubSub') && beehive.getService('PubSub');
        pubsub.subscribe(pubsub.CUSTOM_EVENT, _.bind(this.onCustomEvent));
      };

      WidgetClass.prototype.onCustomEvent = function (event, bibcodes) {

        /**
         * Find the models for each of the bibcodes
         * Filter out ones that can't perform the action
         * Trigger the action on each of the views
         */
        var orcidAction = _.bind(function (action, bibcodes) {
          var models = _.filter(this.collection.models, function (m) {
            return _.contains(bibcodes, m.get('bibcode'));
          });

          // go through each model and grab the view for triggering
          _.forEach(models, _.bind(function (m) {

            // only continue if the model has the action available
            var actions = _.map(m.get('orcid').actions, 'action');

            if (_.contains(actions, action)) {
              var view = this.view.children.findByModel(m);

              if (view) {
                view.trigger('OrcidAction', {
                  action: action,
                  view: view,
                  model: m
                });
              }
            }
          }, this));
        }, this);

        switch (event) {
          case 'orcid-bulk-claim': orcidAction('orcid-add', bibcodes); break;
          case 'orcid-bulk-update': orcidAction('orcid-update', bibcodes); break;
          case 'orcid-bulk-delete': orcidAction('orcid-delete', bibcodes); break;
        }
      };

      /**
       * Apply messages or other information to the orcid control under
       * each record.  This will return a set of actions based on the
       * metadata passed in.
       *
       * @example
       * { isSourcedByADS: true, isCreatedByOthers: false, isCreatedByADS: true }
       *
       * //returns:
       * { actions: { update: {...}, delete: {...}, view: {...} }, provenance: 'ads' }
       *
       * @returns {object} - orcid action options
       */
      WidgetClass.prototype._getOrcidInfo = function (recInfo) {
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

      /**
       * Takes in a set of documents, should be a result of a search or orcid
       * record page.  In either case, it will match the models by bibcode and
       * update their orcid metadata
       *
       * @param {object[]} docs - the docs to update
       * @returns {object[]} - the updated docs
       */
      WidgetClass.prototype.addOrcidInfo = function (docs) {
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
              if (self.hiddenCollection && self.hiddenCollection.findWhere) {
                var model = self.hiddenCollection.findWhere({ bibcode: d.bibcode });
                if (model) {
                  model.set('orcid', actions);
                }
              }

              if (counter === 0) {
                self.trigger('orcid-update-finished');
              }
            });

            recInfo.fail(function (data) {
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

      WidgetClass.prototype._paginationUpdate = function () {
        var self = this;
        _.forEach(this.hiddenCollection.models, function (hiddenModel) {
          var match = _.find(self.collection.models, function (model) {
            return model.get('bibcode') === hiddenModel.get('bibcode');
          });

          if (match && match.get('orcid').pending) {
            match.set('orcid', hiddenModel.get('orcid'));
          }
        });
      };

      WidgetClass.prototype._updateModelsWithOrcid = function () {
        var modelsToUpdate = _.filter(this.collection.models, function (m) {
          return !m.has('_work') && (m.has('bibcode') || m.has('doi'));
        });

        if (_.isEmpty(modelsToUpdate)) {
          return;
        }

        // this creates a connection between model->orcid, and updates source name
        var oApi = this.getBeeHive().getService('OrcidApi');
        oApi.getUserProfile().done(function (profile) {
          var works = profile.getWorks();
          _.forEach(modelsToUpdate, function (m) {
            var exIds = _.pick(m.attributes, ['bibcode', 'doi']);
            _.forEach(works, function (w) {
              var wIds = w.getExternalIds();
              var doi = _.any(exIds.doi, wIds.doi);

              if (exIds.bibcode === wIds.bibcode || doi) {
                m.set({
                  'source_name': w.getSourceName(),
                  '_work': w
                });
              }
            });
          });
        });
      };

      WidgetClass.prototype.processDocs = function (apiResponse, docs, pagination) {
        docs = processDocs.apply(this, arguments);
        var user = this.getBeeHive().getObject('User');
        //for results list only show if orcidModeOn, for orcid big widget show always
        if (user && user.isOrcidModeOn() || this.orcidWidget ){
          var result = this.addOrcidInfo(docs);
          if (pagination.numFound !== result.length) {
            _.extend(pagination, this.getPaginationInfo(apiResponse, docs));
          }
          this._updateModelsWithOrcid();
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
            fl: 'title,abstract,bibcode,author,pub,pubdate,doi,doctype'
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

      /**
       * Finds a work by comparing a model to what we retrieve from ORCID,
       * It can also take a profile param that bypasses the call to orcid, if
       * it isn't necessary to get the most up-to-date data.
       *
       * @param {object} [model] model
       * @param {Profile} _profile
       * @private
       */
      WidgetClass.prototype._findWorkByModel = function (model, _profile) {
        var $dd = $.Deferred();
        var oApi = this.getBeeHive().getService('OrcidApi');
        var exIds = _.pick(model.attributes, ['bibcode', 'doi']);
        var oldOrcid = _.clone(model.get('orcid') || {});
        var profile = null;
        if (!_profile) {
          profile = oApi.getUserProfile();
        }

        var success = function (profile) {
          var works = profile.getWorks();
          var matchedWork = _.find(works, function (w) {
            var wIds = w.getExternalIds();
            var doi = _.any(exIds.doi, wIds.doi);

            return exIds.bibcode === wIds.bibcode || doi;
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
        };

        var fail = function () {
          $dd.reject.apply($dd, arguments);
          var msg = 'Error retrieving ORCiD profile';
          console.error.apply(console, [msg].concat(arguments));
          model.set('orcid', _.extend(oldOrcid, {
            pending: null,
            error: msg
          }));
        };

        if (!_profile) {
          profile.done(success);
          profile.fail(fail);
        } else {
          success(_profile);
        }

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

                  model.set({
                    orcid: self._getOrcidInfo(recInfo),
                    'source_name': work.sources.join('; ')
                  });

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

              var deleteSuccess = function () {

                // Remove entry from collection after delete
                if (self.orcidWidget) {
                  var idx = model.resultsIndex;
                  self.hiddenCollection.remove(model);
                  var models = self.hiddenCollection.models;
                  _.forEach(_.rest(models, idx), function (m) {
                    m.set('resultsIndex', m.get('resultsIndex') - 1);
                    m.set('indexToShow', m.get('indexToShow') - 1);
                  });

                  var showRange = self.model.get('showRange');
                  var range = _.range(showRange[0], showRange[1] + 1);
                  var visible = [];
                  _.forEach(range, function (i) {
                    if (models[i] && models[i].set) {
                      models[i].set('visible', true);
                      models[i].resultsIndex = i;
                      models[i].set('resultsIndex', i);
                      models[i].set('indexToShow', i + 1);
                      visible.push(models[i]);
                    }
                  });
                  self.hiddenCollection.reset(models);
                  self.collection.reset(visible);

                  // reset the total number of papers
                  self.model.set('totalPapers', models.length);
                } else {
                  // reset orcid actions
                  model.set('orcid', self._getOrcidInfo({}));
                }
                self.trigger('orcidAction:' + action, model);
              };

              var performDelete = function (work) {
                orcidApi.deleteWork(work.getPutCode())
                .done(deleteSuccess)
                .fail(function deleteFail(xhr, error, state) {

                  /*
                    record not found, treat like the delete worked
                    Subsequent deletes on an already deleted entity can cause
                    404s
                   */
                  if (state === 'NOT FOUND') {
                    return deleteSuccess();
                  }

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
        }
        return onAllInternalEvents.apply(this, arguments);
      };

      return WidgetClass;
    }

  });
