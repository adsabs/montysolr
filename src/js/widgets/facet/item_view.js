
define(['marionette', 'hbs!./templates/item-checkbox'],
  function(Marionette, ItemCheckBoxTemplate) {

  var BaseItemView = Marionette.ItemView.extend({

    /**
     * The view will be inside div.[className]
     */
    className: "item-view",

    /**
     * You will need to provide the template of your choice
     * for the view to work
     */
    template: ItemCheckBoxTemplate

  });

  return BaseItemView;
});