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
      className: "col-sm-12 s-display-block",
      template: ItemTemplate,

      constructor: function (options) {
        var self = this;
        if (options) {
          _.defaults(options, _.pick(this, ['model', 'collectionEvents', 'modelEvents']));
        }
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

      /**
       * This method prepares data for consumption by the template
       *
       * @returns {*}
       */
      serializeData: function () {

        var data , shownAuthors;
        data = this.model.toJSON();

        var maxAuthorNames = 3;

        if (data.author && data.author.length > maxAuthorNames) {
          data.extraAuthors = data.author.length - maxAuthorNames;
          shownAuthors = data.author.slice(0, maxAuthorNames);
        } else if (data.author) {
          shownAuthors = data.author
        }

        if (data.author) {
          var l = shownAuthors.length - 1;
          data.authorFormatted = _.map(shownAuthors, function (d, i) {
            if (i == l || l == 0) {
              return d; //last one, or only one
            } else {
              return d + ";";
            }
          })
        }
        //if details/highlights
        if (data.details) {
          data.highlights = data.details.highlights
        }

        data.orderNum = this.model.get("resultsIndex") + 1;
        return data;
      },

      events: {
        'change input[name=identifier]': 'toggleSelect',
        'mouseenter .letter-icon': "showLinks",
        'mouseleave .letter-icon': "hideLinks",
        'click .letter-icon': "pinLinks"
      },

      modelEvents: {
        "change:visible": 'render'
      },

      collectionEvents: {
        "add": "render",
        "change:visible": "render"
      },

      toggleSelect: function () {
        this.$el.toggleClass("chosen");
        this.model.set('chosen', this.model.get('chosen') ? false : true);
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
      }
    });

    return ItemView;
  });