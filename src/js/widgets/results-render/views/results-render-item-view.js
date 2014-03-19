define(['marionette', 'hbs!js/widgets/results-render/templates/item-template'],
  function(Marionette, itemTemplate){

      var ResultsItemView = Marionette.ItemView.extend({

      initialize : function(){
          // _.bindall(this, 'toggleExtraInfo')
      },

      template : itemTemplate,

      events : {'click .view-more' : 'toggleExtraInfo'},

      toggleExtraInfo : function(e){
        e.preventDefault();
        this.$(".more-info").toggle();
        }

    })

      return ResultsItemView
  }

)


