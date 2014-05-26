define(['backbone', 'marionette',
    'hbs!./templates/item-checkbox'
  ],
  function(Backbone, Marionette, ItemCheckBoxTemplate) {
    var FacetCollectionView = Marionette.CollectionView.extend({

      /**
       * The view will be inside div.[className]
       */
      className: "item-view",

      /**
       * You will need to provide the template of your choice
       * for the view to work
       */
      template: ItemCheckBoxTemplate,

      /**
       * The container nested inside className object
       */
      itemViewContainer: ".facet-items"


    });

    return FacetCollectionView;
  });