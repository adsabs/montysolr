
define(['marionette', 'hbs!./templates/item-tree'],
  function(Marionette, ItemTreeTemplate) {

  var TreeView = Marionette.CompositeView.extend({

    initialize: function() {
      this.collection = this.model.nodes;
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

    /**
     * You will need to provide the template of your choice
     * for the view to work
     */
    template: ItemTreeTemplate,

    events: {
      'click .widget-item': "onClick"
    },

    onClick: function(ev) {
      ev.stopPropagation();
      this.model.set('selected', ev.target.checked);
      this.trigger('itemClicked'); // we don't need to pass data because marionette includes 'this'
    }

  });

  return TreeView;
});