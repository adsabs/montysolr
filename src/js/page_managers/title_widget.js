define(["backbone",
    'hbs!./templates/abstract-title',
    'hbs!./templates/abstract-title-nav-descriptor'
  ],
  function(Backbone,
           abstractTitleTemplate,
           abstractTitleNavDescriptor
    ) {

    var AbstractTitleModel = Backbone.Model.extend({

      defaults: {
        bibcode: undefined,
        title: undefined,
        numResults: undefined,
        queryUrl: undefined
      }
    });

    var AbstractTitleView = Backbone.View.extend({

      template: abstractTitleTemplate,

      descriptorTemplate: abstractTitleNavDescriptor,

      initialize: function () {
        this.model = new AbstractTitleModel();
        this.listenTo(this.model, "change", this.render);
        this.listenTo(this.collection, "subPageChanged", this._render);
        this.on("render", this.onRender)
      },

      showLoad: true,

      render: function () {
        this.$el.html(this.template(this.model.toJSON()));
        this.trigger("render");
        return this;
      },

      _render: function () {
        var descriptor = this.collection.subPage ? this.collection.subPage.descriptor : "";
        this.$(".nav-descriptor").html(this.descriptorTemplate({descriptor: descriptor}));
      },


      onRender: function () {
        this._render();
      }
    });
  });