define([
    'marionette',
    'backbone',
    'js/components/api_request',
    'js/components/api_query',
    'js/widgets/base/base_widget',
    'hbs!./templates/item-template'
  ],

  function (Marionette,
            Backbone,
            ApiRequest,
            ApiQuery,
            BaseWidget,
            ItemTemplate
    ) {

    var ItemView = Marionette.ItemView.extend({
      tagName: "li",
      template: ItemTemplate,
      constructor: function (options) {
        var self = this;
        if (options) {
          _.defaults(options, _.pick(this, ['model', 'collectionEvents', 'modelEvents']));
        }
        _.bindAll(this, 'resetToggle');

        return Marionette.ItemView.prototype.constructor.apply(this, arguments);
      },

      render: function () {

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

        'focus .letter-icon': "showLinks",
        'mouseenter .letter-icon': "showLinks",
        'mouseleave .letter-icon': "hideLinks",
        'focusout .letter-icon': "hideLinks",

        'click .letter-icon button': "linkToFirst",
        //only relevant to results view for the moment
        'click .show-full-abstract' : "showFullAbstract",
        'click .hide-full-abstract' : "hideFullAbstract",
        'click .orcid-action': "orcidAction"
      },

      modelEvents: {
        "change:visible": 'render',
        "change:showAbstract" : 'render',
        "change:showHighlights" : 'render',
        "change:orcid": 'render',
        "change:chosen": 'render'
      },

      collectionEvents: {
        "add": "render",
        "change:visible": "render"
      },

      showFullAbstract : function(){
        this.$(".short-abstract").addClass("hidden");
        this.$(".full-abstract").removeClass("hidden");
      },

      hideFullAbstract : function(){
        this.$(".short-abstract").removeClass("hidden");
        this.$(".full-abstract").addClass("hidden");
      },

      toggleSelect: function () {
        var isChosen = !this.model.get('chosen');

        this.trigger('toggleSelect',
          {selected: isChosen,
            data : this.model.attributes }
        );
        this.model.set("chosen", isChosen);
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

      /*
       * adding this to make the dropdown
       * accessible, and so people can click to sticky
       * open the quick links
       * */

      removeActiveQuickLinkState: function ($node) {

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


      showLinks: function (e) {
        var $c = $(e.currentTarget);
        if (!$c.find(".active-link").length) {
          return;
        }

        else {
          this.deactivateOtherQuickLinks($c);
          this.addActiveQuickLinkState($c)
        }
      },

      hideLinks: function (e) {
        var $c = $(e.currentTarget);
        this.removeActiveQuickLinkState($c)
      },

      orcidAction: function (e) {
        if (!e) return;

        e.preventDefault();
        e.stopPropagation();
        var $target = $(e.currentTarget);

        var msg = {
          action: $target.data('action') ? $target.data('action') : $target.text().trim(),
          model: this.model,
          view: this,
          target: $target
        };
        this.trigger('OrcidAction', msg);
      }
    });

    return ItemView;
  });