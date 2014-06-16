
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
      'click .widget-item': "onClick",
      'mouseenter label' : "addCount",
      'mouseleave label' : "returnName"
    },

    addCount : function(){
      var val;
      val = this.model.get("count")
      this.$(".facet-amount").html("&nbsp;(" + val + ")" )
    },

    returnName : function(){
      this.$(".facet-amount").empty();

    },

    serializeData : function(){
      var j = this.model.toJSON();
      j.title = "&nbsp;" + this.model.get("title").replace(/\s+/g, "&nbsp;").replace(/–|-/g, "&ndash;")
      return j

    },


    onClick: function(ev) {
      ev.stopPropagation();
      this.model.set('selected', ev.target.checked);
      this.$("label").toggleClass("s-facet-selected");
      this.trigger('itemClicked'); // we don't need to pass data because marionette includes 'this'
    },
    onRender : function(){
      var percent = this.model.get("count") / this.model.get("total")
      this.$(".size-graphic").width(percent*100 +"%")

    }

  });

  return BaseItemView;
});