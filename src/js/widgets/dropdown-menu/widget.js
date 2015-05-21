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

    var ContainerModel = Backbone.Model.extend({
      defaults : function(){
        return {
          // should we show the option to export only selected?
          "selectedOption" : false,
          //are there currently selected papers?
          "selectedPapers" : false,
          //should we only export selected? this is changed by the radio
          //by default it is true if the above two vals are true
          "onlySelected" : true
        }
      }
    });

    var DropdownView = Marionette.CompositeView.extend({

      initialize: function (options) {
        options = options || {};
      },

      modelEvents : {
        "change:selectedOption" : "render",
        "change:selectedPapers" : "render"
      },

      collectionEvents : {
        "change:selected" : function(){
          this.$el.removeClass("open");
        }
      },

      className : "btn-group s-dropdown-widget",

      itemView: DropdownItemView,
      itemViewContainer: ".link-container",
      template: dropdownTemplate,

      serializeData: function () {
        var data = this.model.toJSON();
        //what color should the button be?
        data.btnType = Marionette.getOption(this, "btnType") || "btn-default";
        data.dropdownTitle = Marionette.getOption(this, "dropdownTitle");
        data.iconClass = Marionette.getOption(this, "iconClass");
        //whether to right align the dropdown
        data.pullRight = Marionette.getOption(this, "rightAlign");
        return data
      },

      events : {
        "change input.papers" : function(e){
          var $t = $(e.target);
          $t.attr("value") == "all" ? this.model.set("onlySelected", false): this.model.set("onlySelected", true);
        },

        "click .dropdown-menu label": function(e){
         e.stopPropagation();
        }
    }

    });

    var DropdownWidget = BaseWidget.extend({

      initialize: function (options) {
        options = options || {};
        if (!options.links) {
          throw new Error("Dropdown menu will be empty!")
        }
        //selectedOption: do we want the option to switch from showing all papers vs showing selected papers?
        this.model = new ContainerModel({selectedOption : options.selectedOption });
        this.collection = new DropdownCollection(options.links);
        this.view = new DropdownView(_.extend({collection: this.collection, model : this.model}, options));
        this.listenTo(this.collection, "change:selected", this.emitNavigateEvent);

      },

      activate: function (beehive) {
        _.bindAll(this);
        this.pubsub = beehive.Services.get('PubSub');
        this.pubsub.subscribe(this.pubsub.STORAGE_PAPER_UPDATE, this.onStoragePaperChange);
      },

      onStoragePaperChange : function(numSelected){
        numSelected ? this.model.set("selectedPapers", true) : this.model.set("selectedPapers", false);
      },

      emitNavigateEvent: function (model, value) {
        var onlySelected = (this.model.get("selectedOption") && this.model.get("selectedPapers")  && this.model.get("onlySelected"));
        if (value) {
          this.pubsub.publish(this.pubsub.NAVIGATE, model.get("navEvent"), {"onlySelected" : onlySelected });
        }
        model.set("selected", false);
      }

    });

    return DropdownWidget
  });
