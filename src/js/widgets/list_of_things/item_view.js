define([
    'marionette',
    'backbone',
    'js/components/api_request',
    'js/components/api_query',
    'js/widgets/base/base_widget',
    'hbs!./templates/item-template',
    'js/modules/orcid/orcid_model_notifier/orcid_model'
  ],

  function (Marionette,
            Backbone,
            ApiRequest,
            ApiQuery,
            BaseWidget,
            ItemTemplate,
            OrcidModel
    ) {

    var ItemView = Marionette.ItemView.extend({
      tagName: "li",
      className: "col-sm-12 s-display-block",
      template: ItemTemplate,

      constructor: function (options) {
        var self = this;
        if (options) {
          _.defaults(options, _.pick(this, ['model', 'collectionEvents', 'modelEvents']));
        }

        this.listenTo(this, "item:rendered", this.onItemRendered);

        _.bindAll(this, 'resetToggle');

        OrcidModel.on('change:isInBulkInsertMode', this.resetToggle);

        return Marionette.ItemView.prototype.constructor.apply(this, arguments);
      },

      onItemRendered: function(ev, arg1, arg2) {
        if (OrcidModel.get('actionsVisible')){
          this.showOrcidActions();
        }
      },

      render: function () {

        this.model.set('orcidActionsVisible', OrcidModel.get('actionsVisible'));

        if (this.model.get('visible')) {
          return Marionette.ItemView.prototype.render.apply(this, arguments);
        }
        else if (this.$el) { // it was already rendered, so remove it
          this.$el.empty();
        }
        return this;
      },


      events: {
        'change input[name=identifier]': 'toggleSelect',
        'click .details-control' : "toggleDetails",
        'mouseenter .letter-icon': "showLinks",
        'mouseleave .letter-icon': "hideLinks",
        'click .orcid-action': "orcidAction",
        'click .letter-icon': "pinLinks"

      },

      modelEvents: {
        "change:visible": 'render',
        "change:showDetails" : 'render'
      },

      collectionEvents: {
        "add": "render",
        "change:visible": "render"
      },

      toggleSelect: function () {

        if (OrcidModel.get('isInBulkInsertMode')) {
          if (this.model.get('chosen')) {
            OrcidModel.removeFromBulkWorks(this.model.attributes);
          }
          else {
            OrcidModel.addToBulkWorks(this.model.attributes);
          }
        }

        this.$el.toggleClass("chosen");
        this.model.set('chosen', this.model.get('chosen') ? false : true);
      },

      resetToggle: function(){
        this.setToggleTo(false);
      },

      setToggleTo : function(to){

        var $checkbox = $('input[name=identifier]');
        if (to) {
          this.$el.addClass("chosen");
          this.model.set('chosen', true);
          $checkbox.prop('checked', true);
        }
        else
        {
          this.$el.removeClass("chosen");
          this.model.set('chosen', false);
          $checkbox.prop('checked', false);
        }
      },

      toggleDetails : function(){
        var newValue = this.model.get("showDetails") ? false : true;
        this.model.set("showDetails", newValue);
      },

      /*
       * adding this to make the dropdown
       * accessible, and so people can click to sticky
       * open the quick links
       * */

      removeActiveQuickLinkState: function ($node) {

        $node.removeClass("pinned");
        $node.find("i").removeClass("s-icon-draw-attention")
        $node.find(".link-details").addClass("hidden");
        $node.find('ul').attr('aria-expanded', false);

      },

      addActiveQuickLinkState: function ($node) {

        $node.find("i").addClass("s-icon-draw-attention")
        $node.find(".link-details").removeClass("hidden");
        $node.find('ul').attr('aria-expanded', true);

      },

      deactivateOtherQuickLinks: function ($c) {

        var $hasList = this.$(".letter-icon").filter(function () {
          if ($(this).find("i").hasClass("s-icon-draw-attention")) {
            return true
          }
        }).eq(0);

        //there should be max 1 other icon that is active

        if ($hasList.length && $hasList[0] !== $c[0]) {

          this.removeActiveQuickLinkState($hasList)
        }
      },

      pinLinks: function (e) {
        var $c = $(e.currentTarget);

        if (!$c.find(".active-link").length) {
          return
        }
        $c.toggleClass("pinned");
        if ($c.hasClass("pinned")) {
          this.deactivateOtherQuickLinks($c);
          this.addActiveQuickLinkState($c);
        }
        else {
          this.removeActiveQuickLinkState($c);
        }
      },

      showLinks: function (e) {
        var $c = $(e.currentTarget);
        if (!$c.find(".active-link").length) {
          return;
        }
        if ($c.hasClass("pinned")) {
          return;
        }
        else {
          this.deactivateOtherQuickLinks($c);
          this.addActiveQuickLinkState($c)
        }
      },

      hideLinks: function (e) {
        $c = $(e.currentTarget);
        if ($c.hasClass("pinned")) {
          return
        }
        this.removeActiveQuickLinkState($c)
      },

      showOrcidActions: function(){
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

        if (OrcidModel.isWorkInCollection(this.model.attributes)){
          $update.removeClass('hidden');
          $delete.removeClass('hidden');
          $icon.addClass('green');
        }
        else {
          $insert.removeClass('hidden');
          $icon.addClass('gray');
        }
      },

      hideOrcidActions: function(){
        var $orcidActions = this.$('.orcid-actions');
        $orcidActions.addClass('hidden');
      },

      orcidAction: function(e){
        e.preventDefault();
        e.stopPropagation();
        var $c = $(e.currentTarget);
        var $orcidActions = this.$('.orcid-actions');
        $orcidActions.addClass('orcid-wait');

        var actionType = '';

        if ($c.hasClass('orcid-action-insert')){
          actionType = 'insert';
        } else if ($c.hasClass('orcid-action-update')){
          actionType = 'update';
        } else if ($c.hasClass('orcid-action-delete')){
          actionType = 'delete';
        }

        var msg = {
          actionType : actionType,
          model: this.model.attributes,
          modelType: 'adsData'

        };

        this.trigger('OrcidAction', msg);

      }
    });

    return ItemView;
  });