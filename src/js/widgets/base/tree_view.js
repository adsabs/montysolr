define(['marionette', 'hbs!./templates/item-tree'],
  function (Marionette, ItemTreeTemplate) {


    var TreeNode = Backbone.Model.extend({
      initialize: function () {
        var children = this.get("children");
        if (children) {
          this.children = new TreeNodeCollection(children);
          this.unset("children");
        }
      }
    });

    var TreeNodeCollection = Backbone.Collection.extend({
      model: TreeNode
    });

    var TreeView = Marionette.CompositeView.extend({

      initialize: function () {
        this.collection = this.model.children;
        this.displayNum = Marionette.getOption(this, "displayNum") || 5;
        this.maxDisplayNum = Marionette.getOption(this, "maxDisplayNum") || 200;
      },

      /**
       * The view will be inside div.[className]
       */
      className: function () {
        if (Marionette.getOption(this, "hide") === true) {
          return "hide item-view";
        } else {
          return "item-view";
        }
      },

      itemViewOptions: function (model, index) {
        if (index < this.displayNum) {
          return {hide: false};
        }
        else {
          return {hide: true};
        }
      },

      itemViewContainer: ".item-children",

      /**
       * You will need to provide the template of your choice
       * for the view to work
       */
      template: ItemTreeTemplate,

      events: {
        'click .widget-item': "onClick",
        'click .show-more:last': 'onShowMore'
      },

      onClick: function (ev) {
        ev.stopPropagation();
        this.$('.item-children:first').toggleClass('hide');
        this.model.set('selected', $(ev.target).is(':checked'));
        this.trigger('treeClicked'); // we don't need to pass data because marionette includes 'this'
      },

      setCurrentQuery: function(q) {
        this._q = q;
      },

      getCurrentQuery: function() {
        return this._q;
      },

      displayMore: function(howMany) {
        //show hidden data
        this.$('.item-children:first').children().filter('.hide').slice(0, howMany).removeClass("hide");
      },

      enableShowMore: function() {
        this.$('.show-more:last').removeClass('hide');
      },

      disableShowMore: function() {
        this.$('.show-more:last').addClass('hide');
      },

      onShowMore: function(ev) {
        ev.stopPropagation();
        this.trigger('fetchMore', this.$('.item-children:first').children().filter('.hide').length,
          {view: this, collection: this.collection, query: this.getCurrentQuery()});
      }


    });

    TreeView.CollectionClass = TreeNodeCollection;
    TreeView.ModelClass = TreeNode;
    return TreeView;
  });