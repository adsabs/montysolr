/**
 * This module contains additional item views for facets; these are the views
 * made from combination of different options
 */

define(['./item_view', './hierarchical_item_view', 'hbs!./templates/item-checkbox'
], function (BaseItemView, BaseHierarchicalItemView, ItemCheckBoxTemplate) {


  var checkboxMultiselectMixin = {

    //when someone clicks a checkbox
    toggleHighlight: function (e) {
      e.stopPropagation();
      $(e.target).parent().toggleClass("selected");
      this.model.set({
        "selected": !this.model.get("selected")
      });
      if (this.model.get("selected")) {
        this.trigger("selected");
      }
      else {
        this.trigger("unselected")
      }
    },

    events: {
      "change input": "toggleHighlight"
    }
  };

  var CheckboxHierarchicalView = BaseHierarchicalItemView.extend(checkboxMultiselectMixin).extend({
    template: ItemCheckBoxTemplate
  });
  var CheckboxOneLevelView = BaseItemView.extend(checkboxMultiselectMixin).extend({
    template: ItemCheckBoxTemplate
  });

  var ItemViews = {
    CheckboxHierarchicalView: CheckboxHierarchicalView,
    CheckboxOneLevelView: CheckboxOneLevelView
  };

  return ItemViews;

});

