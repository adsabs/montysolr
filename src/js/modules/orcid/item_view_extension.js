/**
 * This is not currently used; it is essentially the same code (or maybe slightly different)
 * that lives inside item_view - that means that item_view has orcid events and functions
 * hardcoded; maybe in the future we'll want to extract them (in a similar way the
 * orcid/extension provides functionality for the widget controllers)
 *
 */
define([
    'backbone',
    'underscore'
  ],
  function (Backbone, _) {

    return {
      showOrcidActions: function (isWorkInCollection) {
        var $icon = this.$('.mini-orcid-icon');
        $icon.removeClass('green');
        $icon.removeClass('gray');


        var $orcidActions = this.$('.orcid-actions');
        $orcidActions.removeClass('hidden');
        $orcidActions.removeClass('orcid-wait');
        var $update = $orcidActions.find('.orcid-action-update');
        var $insert = $orcidActions.find('.orcid-action-insert');
        var $delete = $orcidActions.find('.orcid-action-delete');

        $update.addClass('hidden');
        $insert.addClass('hidden');
        $delete.addClass('hidden');

        if (isWorkInCollection(this.model.attributes)) {
          if (!(this.model.attributes.isFromAds === true)) {
            $update.removeClass('hidden');
          }
          $delete.removeClass('hidden');
          $icon.addClass('green');
        }
        else {
          if (this.model.attributes.isFromAds === false) {
            // the nonAds item from orcid
            // nothing to do
          }
          else {
            $insert.removeClass('hidden');
            $icon.addClass('gray');
          }
        }
      },

      hideOrcidActions: function () {
        var $orcidActions = this.$('.orcid-actions');
        $orcidActions.addClass('hidden');
      },

      orcidAction: function (e) {
        e.preventDefault();
        e.stopPropagation();
        var $c = $(e.currentTarget);
        var $orcidActions = this.$('.orcid-actions');
        $orcidActions.addClass('orcid-wait');

        var actionType = '';

        if ($c.hasClass('orcid-action-insert')) {
          actionType = 'insert';
        } else if ($c.hasClass('orcid-action-update')) {
          actionType = 'update';
        } else if ($c.hasClass('orcid-action-delete')) {
          actionType = 'delete';
        }

        var msg = {
          actionType: actionType,
          model: this.model.attributes,
          modelType: 'adsData'

        };

        this.trigger('OrcidAction', msg);

      }
    }

  });