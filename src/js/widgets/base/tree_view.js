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

      itemViewContainer: ".item-children",

      /**
       * You will need to provide the template of your choice
       * for the view to work
       */
      template: ItemTreeTemplate,

      events: {
        'click .widget-item': "onClick",
        'click .show-more': 'onShowMore'
      },

      onClick: function (ev) {
        ev.stopPropagation();
        this.$('.item-children:first').toggleClass('hide');
        this.model.set('selected', $(ev.target).is(':checked'));
        this.trigger('treeClicked'); // we don't need to pass data because marionette includes 'this'
      },

      enableShowMore: function() {
        this.$('.show-more').removeClass('hide');
      },

      disableShowMore: function() {
        this.$('.show-more').addClass('hide');
      },

      onShowMore: function() {
        ev.stopPropagation();
        console.log('on show more');
      }


    });

    TreeView.CollectionClass = TreeNodeCollection;
    TreeView.ModelClass = TreeNode;
    return TreeView;
  });