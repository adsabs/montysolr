define(["marionette", "hbs!./templates/landing-page-layout",
    "js/widgets/base/base_widget"
  ],
  function(Marionette, fullLengthLayout, BaseWidget){

  var widgetDict, history;

  var API = {

    insertTemplate : function(){
      $("#body-template-container").children().detach().end()
        .append(fullLengthLayout())

    },

    displayLandingPage : function(){
      this.insertTemplate();

      $("#row-2-content").append(widgetDict.searchBar.render().el)

      //opening the form
      $(".show-form").click();


    }

  };

  var LandingPageController = BaseWidget.extend({



   initialize : function(options){

    options = options || {};

    this.API = API;

     widgetDict = options.widgetDict;
     history = options.history;

   },

   activate : function(beehive){

   },

  showPage : function(page){

      console.log("displayingLandingpage!!")
      API.displayLandingPage()


  }

  })


 return LandingPageController


})