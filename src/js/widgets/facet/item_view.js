
define(['marionette'], function(Marionette) {

  var BaseItemView = Marionette.ItemView.extend({

    className: "item-view hide"

    //most widgets are hidden by default until parent container view
    //explicitly shows them

  });

  return BaseItemView;
});