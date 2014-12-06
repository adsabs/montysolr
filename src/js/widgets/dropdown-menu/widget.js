define([
    'marionette',
    'js/widgets/base/base_widget',
    'hbs!./templates/dropdown',
    'hbs!./templates/dropdown-item'
  ],
  function (Marionette, BaseWidget, dropdownTemplate, dropdownItemTemplate) {

    /*
     *
     * To use this widget to generate a dropdown list,
     * you should pass a configuration object to the
     * widget called links, in the form
     *  links : [{href : '' , description : '' , navEvent: ''}]
     *
     * */

    var DropdownModel = Backbone.Model.extend({

      defaults: function () {
        return {
          selected: false,
          href: undefined,
          description: undefined,
          navEvent: undefined
        }
      }
    });


    var DropdownCollection = Backbone.Collection.extend({

      initialize: function (models, options) {
        this.on("change:selected", this.removeOtherSelected)
      },

      model: DropdownModel,

      //only allow 1 selected model at a time
      removeOtherSelected: function (selectedModel, val) {

        if (val === false) {
          return
        }
        this.each(function (m) {
          if (m !== selectedModel) {
            m.set("selected", false)
          }
        })
      }
    });

    var DropdownItemView = Marionette.ItemView.extend({

      tagName: "li",

      events: {
        "click": "setSelected"
      },

      setSelected: function (e) {
        e.preventDefault();
        this.model.set("selected", true);
      },
      template: dropdownItemTemplate
    });

    var DropdownView = Marionette.CompositeView.extend({

      initialize: function (options) {
        options = options || {};
      },

      itemView: DropdownItemView,
      itemViewContainer: ".dropdown-menu",
      template: dropdownTemplate,

      serializeData: function () {
        var data = {};
        //what color should the button be?
        data.btnType = Marionette.getOption(this, "btnType") || "btn-default";
        data.dropdownTitle = Marionette.getOption(this, "dropdownTitle");
        data.iconClass = Marionette.getOption(this, "iconClass");
        return data
      }
    });


    var DropdownWidget = BaseWidget.extend({

      initialize: function (options) {
        options = options || {};
        if (!options.links) {
          throw new Error("Dropdown menu will be empty!")
        }

        this.collection = new DropdownCollection(options.links);
        this.view = new DropdownView(_.extend({collection: this.collection}, options));
        this.listenTo(this.collection, "change:selected", this.emitNavigateEvent);
      },

      emitNavigateEvent: function (model, value) {
        //checking to make sure selected == true
        if (value) {
          this.pubsub.publish(this.pubsub.NAVIGATE, model.get("navEvent"));
        }
        //for now, just set it to false, but later listen to nav event to remove
        model.set("selected", false);
      },

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');
      }
    });

    return DropdownWidget
  });