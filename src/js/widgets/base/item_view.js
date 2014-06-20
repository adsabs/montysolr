
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

    //  utility function (better place to put this?)
    formatNum: function(num){
      var withCommas = [];
      num = num+"";
      if (num.length < 4){
        return num
      }
      else {
        num  = num.split("").reverse();
        _.each(num, function(n, i){
          withCommas.splice(0,0, n)
          if (i > 0 && (i+1) %3 === 0){
            withCommas.splice(0,0, ",")
          }

        })
      }
      withCommas = withCommas.join("");
      return withCommas.replace(/^\,(.+)/, "$1")
    },

    addCount : function(){
      var val;
      val = this.model.get("count")
      this.$(".facet-amount").html("&nbsp;(" + this.formatNum(val) + ")" )
    },

    returnName : function(){
      this.$(".facet-amount").empty();

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