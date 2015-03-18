/**
 * Extension for a controller; it adds a functionality that allows a widget
 * to handle ORCID actions.
 *
 * It is installed as return OrcidExtension(WidgetClass)
 */

define([
    'underscore'
  ],

  function (
    _
    ) {

    return function(WidgetClass) {
      var processDocs = WidgetClass.prototype.processDocs;
      var activate = WidgetClass.prototype.activate;
      var onAllInternalEvents = WidgetClass.prototype.onAllInternalEvents;

      WidgetClass.prototype.activate = function(beehive) {
        activate.apply(this, arguments);
        var orcidApi = beehive.getService('OrcidApi');
        //if (!orcidApi) {
        //  throw new Error('OrcidApi is missing');
        //}
        this.beehive = beehive; // TODO:rca - use beehive mixin
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
          msg.provenance = 'both';
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
          var orcidApi = this.beehive.getService('OrcidApi');

          if (!orcidApi || !orcidApi.hasAccess()) {
            return docs;
          }

          var recInfo;

          _.each(docs, function(d) {
            recInfo = orcidApi.getRecordInfo(d);
            if (recInfo.state() == 'pending') {
              recInfo.done(function(rInfo) {
                //console.log('pending: ' + d.bibcode + JSON.stringify(rInfo));
                var actions = self._getOrcidInfo(rInfo);
                // get the model for this document
                if (self.collection && self.collection.findWhere) {
                  var model = self.collection.findWhere({bibcode: d.bibcode});
                  if (model) {
                    model.set('orcid', actions); // if not found, we can ignore this update (the view changed already)
                  }
                }
              });
              d.orcid = {pending: true};
            }
            else {
              recInfo.done(function(rInfo) {
                //console.log('ready: ' + d.bibcode + JSON.stringify(rInfo));
                d.orcid = self._getOrcidInfo(rInfo);
              });
            }

          });
          return docs;
        };

      WidgetClass.prototype.processDocs = function() {
        var docs = processDocs.apply(this, arguments);
        var user = this.beehive.getObject('User');
        if (user && user.isOrcidModeOn()){
          return this.addOrcidInfo(docs);
        }
        return docs;
      };

      WidgetClass.prototype.onAllInternalEvents = function(ev, arg1, arg2) {
        if (ev == 'itemview:OrcidAction') {
          var self = this;
          var data = arg2;
          var orcidApi = this.beehive.getService('OrcidApi');

          var update = function(action, model) {
            var oldOrcidInfo = model.get('orcid');
            model.set('orcid', {pending: true});
            orcidApi.updateOrcid(action, data.model.attributes)
              .done(function(recInfo) {
                model.set('orcid', self._getOrcidInfo(recInfo));
                self.trigger('orcidAction:' + action, model);
              })
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
            console.log('Viewing not implemented yet');
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