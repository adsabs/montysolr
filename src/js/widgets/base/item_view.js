
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
      this.model.set('selected', ev.target.checked);
      this.trigger('itemClicked'); // we don't need to pass data because marionette includes 'this'
    }

  });

  return BaseItemView;
});