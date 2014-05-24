
define(['marionette', 'hbs!./templates/item-checkbox'],
  function(Marionette, ItemCheckBoxTemplate) {

  var BaseItemView = Marionette.ItemView.extend({

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
    template: ItemCheckBoxTemplate,

    events: {
      'click .widget-item': "onClick"
    },

    onClick: function(ev) {
      this.trigger('itemClicked', this.model);
    }

  });

  return BaseItemView;
});