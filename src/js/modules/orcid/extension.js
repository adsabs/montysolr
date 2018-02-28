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
    'js/mixins/dependon'
  ],

  function (
    _,
    ApiQuery,
    ApiRequest,
    ApiQueryUpdater,
    ApiTargets

    ) {

    return function(WidgetClass) {
      var queryUpdater = new ApiQueryUpdater('OrcidExtension');
      var processDocs = WidgetClass.prototype.processDocs;
      var activate = WidgetClass.prototype.activate;
      var onAllInternalEvents = WidgetClass.prototype.onAllInternalEvents;

      WidgetClass.prototype.activate = function(beehive) {
        this.setBeeHive(beehive);
        activate.apply(this, arguments);
        var orcidApi = beehive.getService('OrcidApi');
        //if (!orcidApi) {
        //  throw new Error('OrcidApi is missing');
        //}
      };

      WidgetClass.prototype._getOrcidInfo = function(recInfo) {
        var msg = {actions: {} , provenance : null};

        if (recInfo.isCreatedByOthers && recInfo.isCreatedByUs){

          msg.actions.update = {title: 'update in ORCID', caption:'Update ADS version with latest data', action: 'orcid-update'};
          msg.actions.delete = {title: 'delete from ORCID', caption:'Delete ADS version from ORCID', action: 'orcid-delete'};
          msg.actions.view = {title: 'view in ORCID', caption:'Another version exists (we don\'t have rights to update it)', action: 'orcid-view'};
        }
        else if (recInfo.isCreatedByOthers && !recInfo.isCreatedByUs) {

          msg.actions.add = {title: 'add ADS version ORCID', caption:'ORCID already has a record for this article (we don\'t have rights to update it).', action: 'orcid-add'};
          msg.actions.view = {title: 'view in ORCID', caption:'Another version exists (we don\'t have rights to update it)', action: 'orcid-view'};

        }
        else if (!recInfo.isCreatedByOthers && recInfo.isCreatedByUs) {

          msg.actions.update = {title: 'update in ORCID', caption:'Update ADS version with latest data', action: 'orcid-update'};
          msg.actions.delete = {title: 'delete from ORCID', caption:'Delete ADS version from ORCID', action: 'orcid-delete'};
        }
        else {
          msg.actions.add = {title: 'add to ORCID', caption:'Add ADS version to ORCID', action: 'orcid-add'};
        }

        if (recInfo.isCreatedByUs && recInfo.isCreatedByOthers) {
          msg.provenance = 'ads'; // duplicate, but just to be explicit
        }
        else if (recInfo.isCreatedByUs) {
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
            if (recInfo.state() == 'pending') {
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

                if (counter == 0) {
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

                if (counter == 0) {
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
                if (!d.identifier && rInfo.bibcode)
                  d.identifier = rInfo.bibcode;
              });
            }
          });

          if (counter == 0) {
            self.trigger('orcid-update-finished', docs);
          }

          return docs;
        };

      WidgetClass.prototype.processDocs = function(apiResponse, docs, pagination) {
        var docs = processDocs.apply(this, arguments);
        var user = this.getBeeHive().getObject('User');
        //for results list only show if orcidModeOn, for orcid big widget show always
        if (user && user.isOrcidModeOn() || this.orcidWidget ){
          var result = this.addOrcidInfo(docs);
          if (pagination.numFound != result.length) {
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
        var promise = $.Deferred();

        if (model.get('bibcode') && model.get('source_name')) { // no need to do anything
          promise.resolve(model);
          return promise.promise();
        }

        if (model.get('source_name')) { // need to get ADS metadata
          var q, req;
          q = new ApiQuery({'q': 'identifier:' + queryUpdater.quoteIfNecessary(model.get('identifier')),
            'fl': 'title,abstract,bibcode,author,keyword,id,links_data,property,pub,aff,email,volume,pubdate,doi'});
          req = new ApiRequest({query: q, target: ApiTargets.SEARCH, options: {
            done: function (resp) {
              if (resp.response && resp.response.docs && resp.response.docs[0]) {
                var sourceName = model.attributes.source_name ? model.attributes.source_name + '; NASA ADS' : 'NASA ADS';
                model.attributes = _.extend(model.attributes, resp.response.docs[0], {source_name: sourceName});
              }
              promise.resolve(model);
            },
            fail: function () {
              promise.fail();
            }
          }});
          self.getPubSub().publish(self.getPubSub().EXECUTE_REQUEST, req);
        }
        else { // we have ADS recs, need to extend them with Orcid metadata
          var oApi = self.getBeeHive().getService('OrcidApi');
          oApi.getOrcidProfileInAdsFormat()
            .done(function(docs) {
              _.each(docs.response.docs, function(d) {
                if (d.bibcode && d.bibcode == model.attributes.bibcode) {
                  model.attributes = _.defaults(model.attributes, d);
                }
              });
              promise.resolve(model);
            })
            .fail(function() {
              promise.fail();
            });
        }

        return promise.promise();
      };

      WidgetClass.prototype.onAllInternalEvents = function(ev, arg1, arg2) {
        if (ev == 'childview:OrcidAction') {
          var self = this;
          var data = arg2;
          var orcidApi = this.getBeeHive().getService('OrcidApi');

          var update = function(action, model) {

            $('.s-results-orcid-container button').attr('disabled', 'disabled');

            self.mergeADSAndOrcidData(model)
              .done(function(model) {
                var oldOrcidInfo = _.clone(model.get('orcid') || {});
                model.set('orcid', {pending: true});
                orcidApi.updateOrcid(action, data.model.attributes)
                  .done(function(recInfo) {
                    if (action == 'delete') {
                      var parts = model.attributes.source_name.split('; ');
                      if (parts.indexOf('NASA ADS') > -1) {
                        parts.splice(parts.indexOf('NASA ADS'), 1);
                      }
                      model.attributes.source_name = parts.join('; ');
                    }
                    else {
                      model.set(model.attributes, {silent: true});
                    }
                    model.set('orcid', self._getOrcidInfo(recInfo));

                    if (action == 'delete' && model.get('source_name')) {
                      return; // do nothing, we want to keep seeing the record
                    }
                    self.trigger('orcidAction:' + action, model);
                  })
                  .fail(function() {
                    model.set('orcid', _.extend(oldOrcidInfo, {pending: null, error: 'Error updaing record, please retry'}));
                  })
              })
              .fail(function() {
                console.log('Failed merging data, we need to handle this error');
                var o = _.clone(model.get('orcid'));
                o.error = 'Failed merging ORCID data with ADS data';
                delete o.pending;
                model.set('orcid', o);
              })
              .always(function () {
                $('.s-results-orcid-container button').removeAttr('disabled');
              });
          };

          var action = data.action;
          if (action.indexOf('add') > -1) {
            update('add', data.model);
          }
          else if (action.indexOf('delete') > -1) {
            update('delete', data.model);
          }
          else if (action.indexOf('update') > -1) {
            update('update', data.model);
          }
          else if(action.indexOf('view') > -1) {
            console.log('Viewing of individual records not implemented (by ORCID)');
          }
          else if(action.indexOf('login') > -1) {
            orcidApi.signIn();
          }
          else if(action.indexOf('logout') > -1) {
            orcidApi.signOut();
          }
          else {
            throw new Error('Unknown action: ' + action);
          }
        }
        else {
          return onAllInternalEvents.apply(this, arguments);
        }
      };

      return WidgetClass;
    }

  });
