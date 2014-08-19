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
        //this.on('all', function(){console.log(arguments)});
      },


      className: function () {
        if (Marionette.getOption(this, "hide") === true) {
          return "hide item-view";
        } else {
          return "item-view";
        }
      },


      onRender: function(view) {
        // if we want to give controller chance to load more data (the children of this view)
        //if (!view.$el.hasClass('hide')) {
          //view.trigger('treeNodeDisplayed');
        //}
      },

      itemViewContainer: ".item-children",

      /**
       * You will need to provide the template of your choice
       * for the view to work
       */
      template: ItemTreeTemplate,

      events: {
        'click .widget-item': "onClick",
        'click .item-caret ': "toggleChildren",
        'click .show-more': 'onShowMore'
      },

      toggleChildren : function(e){
        if (e) {
          e.stopPropagation();
        }

        this.$('.item-body:first').toggleClass('hide');
        if (this.$(".item-caret:first").hasClass("item-open")){
          this.$(".item-caret:first").removeClass("item-open").addClass("item-closed");
        }
        else {
          if (this.$('.item-body').find('.item-view:first').hasClass('hide')) {
            this.displayMore(this.displayNum);
          }
          this.$(".item-caret:first").removeClass("item-closed").addClass("item-open")

        }

      },

      onClick: function (ev) {
        ev.stopPropagation();
        this.$('.item-children').toggleClass('hide');
        this.model.set('selected', $(ev.target).is(':checked'));
        this.trigger('itemClicked'); // we don't need to pass data because marionette includes 'this'
      },

      setCurrentQuery: function(q) {
        this._q = q;
      },

      getCurrentQuery: function() {
        return this._q;
      },

      /**
       * As opposed to other views, this view will show the hidden item
       * and trigger 'treeNodeDisplayed' on its view to give controller
       * a chance to load children data (as soon as the item was displayed)
       *
       * @param howMany
       */
      displayMore: function(howMany) {
        //show hidden data
        var hidden = this.$('.item-children:first').children().filter('.hide');
        this.$('.item-children:first').children().filter('.hide').slice(0,howMany).removeClass('hide')
        if (hidden.length > 0) {
          var offset = this.collection.models.length - hidden.length;
          var max = this.children.length-offset;
          for (var i=0;i<howMany && i<max;i++) {
            $(hidden[i]).removeClass('hide');
            this.children.findByIndex(offset+i).trigger('treeNodeDisplayed');
          }
        }
      },

      enableShowMore: function() {
        this.$('.item-caret:first').removeClass('hide');
        this.$('.show-more:last').removeClass('hide');
      },

      disableShowMore: function() {
        this.$('.item-caret:first').removeClass('hide');
        if (this.collection.length == 0) {
          this.$(".item-caret:first").removeClass("item-open").removeClass("item-closed").addClass('item-end');
          return;
        }
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