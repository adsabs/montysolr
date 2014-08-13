define(["marionette", "hbs!./templates/landing-page-layout",
    "js/widgets/base/base_widget"
  ],
  function (Marionette, fullLengthLayout, BaseWidget) {

    var history;

    var API = {

      insertTemplate: function () {
        $("#body-template-container").empty()
          .append(fullLengthLayout())

      },

      displayLandingPage: function () {
        this.insertTemplate();

        $("#row-2-content").append(this.widgetDict.searchBar.render().el);

        //opening the form
        $(".show-form").click();

      }

    };

    var LandingPageController = BaseWidget.extend({


      initialize: function (options) {

        options = options || {};


        this.widgetDict = options.widgetDict;
        _.extend(this, API);


        history = options.history;


      },

      activate: function (beehive) {

      },

      showPage: function (page) {


        this.displayLandingPage()


      }

    });


    return LandingPageController


  });