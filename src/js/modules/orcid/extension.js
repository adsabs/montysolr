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
        var msg = {actions: []};

        if (recInfo.isCreatedByOthers && recInfo.isCreatedByUs) {
          msg.actions.push({title: 'update in ORCID', caption:'Update ADS version with latest data', action: 'orcid-update'});
          msg.actions.push({title: 'delete in ORCID', caption:'Delete ADS version from ORCID', action: 'orcid-delete'});
          msg.actions.push({title: 'view in ORCID', caption:'Another version exists (we don\'t have rights to update it)', action: 'orcid-view'});

        }
        else if (recInfo.isCreatedByOthers && !recInfo.isCreatedByUs) {
          msg.actions.push({title: 'add to ORCID', caption:'Add ADS version to ORCID', action: 'orcid-add'});
          msg.actions.push({title: 'view in ORCID', caption:'Another version exists (we don\'t have rights to update it)', action: 'orcid-view'});
        }
        else if (!recInfo.isCreatedByOthers && recInfo.isCreatedByUs) {
          msg.actions.push({title: 'update in ORCID', caption:'Update ADS version with latest data', action: 'orcid-update'});
          msg.actions.push({title: 'delete in ORCID', caption:'Delete ADS version from ORCID', action: 'orcid-delete'});
        }
        else {
          msg.actions.push({title: 'add to ORCID', caption:'Add ADS version to ORCID', action: 'orcid-add'});
        }


        if (recInfo.isCreatedByUs) {
          msg.provenance = 'ADS';
        }
        else if (recInfo.isCreatedByOthers) {
          msg.provenance = 'Others';
        }
        else {
          msg.provenance = null;
        }
        return msg;
      },

      WidgetClass.prototype.processDocs = function() {
        var docs = processDocs.apply(this, arguments);
        var self = this;

        // add orcid info to the documents
        var orcidApi = this.beehive.getService('OrcidApi');
        if (!orcidApi) {
          return docs;
        }

        var isLoggedIn = orcidApi.hasAccess();
        var msg, recInfo;
        _.each(docs, function(d) {
          if (isLoggedIn) {
            recInfo = orcidApi.getRecordInfo(d);
            d.orcid = self._getOrcidInfo(recInfo);
          }
          else {
            d.orcid = {needLogin: true};
          }
        });
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