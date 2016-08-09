define([
  'js/apps/discovery/navigator',
  'js/bugutils/minimal_pubsub',
  'js/components/api_feedback',
], function(
    Navigator,
    Minsub,
    ApiFeedback
){

  describe("Navigator", function(){

    it("should handle the orcid endpoint and authenticating if code is provided", function(){

      var n = new Navigator();

      //fake Services

      var orcidApi = {
        //only testing after orcid access has been provided
        hasAccess : function(){
          return true
        },
        getADSUserData : function(){
          var d = $.Deferred();
          //pretending user hasnt filled in data yet
          d.resolve({});
          return d.promise();
        }
      };
      var storage = {
        get : function(val){
          if (val == "orcidAuthenticating"){
            return true;
          }
        },
        remove : sinon.spy(function(val){
        })
      };

      var appStorage = {
        executeStashedNav : sinon.spy(function(){
          return true;
        }),

        get : function(a){
          if (a == "stashedNav") return ["UserPreferences", {"subView":"orcid"}];
        }
      };

      var AlertsController = {
        alert : sinon.spy()
      };

      var MasterPageManager = {
        show: sinon.spy(function () {
        })
      };

      var app = {

        getService: function (obj) {
          if (obj == "OrcidApi") {
            return orcidApi
          }
          else if (obj == "PersistentStorage") {
            return storage
          }
        },
        getObject: function (obj) {
          if (obj == "AppStorage") {
            return appStorage;
          }
          else if (obj == "User") {
            return {
              isLoggedIn: function () {
                return true
              }
            }
          }
          else if (obj === "MasterPageManager") {
            return MasterPageManager;
          }
          else if (obj === "LibraryController"){
            return {
              getLibraryBibcodes : function(){
                return ["1", "2", "3"]
              }
            }
          }
        },

        getController: function (obj) {
          if (obj == "AlertsController") {
            return AlertsController
          }
        },

        getWidget : function(obj){
          if (obj === "OrcidBigWidget"){
            return sinon.spy(function(){
              var d = $.Deferred();
              d.resolve();
              return d;
            })
          }
        }
      };


      n.start(app);

      //1. dont show the modal

      n.catalog.get("orcid-page").execute();
      //remove this flag that lets us know to show a special modal when user goes to #orcid-page
      expect(storage.remove.args[0][0]).to.eql("orcidAuthenticating");
      //modal wasn't called
      expect(AlertsController.alert.callCount).to.eql(0);

      //2. show the modal and redirect to orcidbigwidget

      var appStorage = {
        executeStashedNav : sinon.spy(function(){
          return false;
        }),

        get : function(a){
          if (a == "stashedNav") return undefined;
        }
      };

      n.catalog.get("orcid-page").execute();
      expect(AlertsController.alert.callCount).to.eql(1);
      expect(AlertsController.alert.args[0][0]).to.be.instanceof(ApiFeedback);
      expect(AlertsController.alert.args[0][0].title).to.eql("You are now logged in to ORCID");

      //remove this flag that lets us know to show a special modal when user goes to #orcid-page
      expect(storage.remove.args[0][0]).to.eql("orcidAuthenticating");

      expect(MasterPageManager.show.callCount).to.eql(2);

      expect(MasterPageManager.show.args[0][0]).to.eql("OrcidPage");


  });

    it("should have endpoints for library-export, library-metrics, and library-visualization", function(){


      var n = new Navigator();

      n.getPubSub = function(){
        return {
          publish : sinon.spy()
        }
      }

      var Export = {
        renderWidgetForListOfBibcodes : sinon.spy()
      };
      var Library = {
        setSubView : sinon.spy()
      };

      var MasterPageManager = {
        show : sinon.spy()
      };

      var app = {

        getObject: function (obj) {

          if (obj === "MasterPageManager") {
            return MasterPageManager;
          }
          else if (obj === "LibraryController"){
            return {
              getLibraryBibcodes : function(){
                return $.Deferred().resolve(["1", "2", "3"]);
              }
            }
          }
        },

        getWidget : function(obj){
          var d = $.Deferred();
          if (obj === "ExportWidget"){
            d.resolve(Export);
          }
          if (obj === "IndividualLibraryWidget"){
            d.resolve(Library);
          }
          return d;
        }
      };

      n.start(app);


      n.catalog.get("library-export").execute("library-export",    {
        "id": "1",
        "publicView": false,
        "subView": "export",
        "widgetName": "ExportWidget",
        "additional": {
          "format": "bibtex"
        }
      });

      expect(Export.renderWidgetForListOfBibcodes.callCount).to.eql(1);
      expect(Export.renderWidgetForListOfBibcodes.args[0]).to.eql([
        [
          "1",
          "2",
          "3"
        ],
        {
          "format": "bibtex"
        }
      ]);

      expect(Library.setSubView.callCount).to.eql(1);
      expect(Library.setSubView.args[0]).to.eql([
        {
          "subView": "export",
          "publicView": false,
          "id": "1"
        }
      ]);

      expect(MasterPageManager.show.callCount).to.eql(1)
      expect(MasterPageManager.show.args[0]).to.eql([
        "LibrariesPage",
        [
          "IndividualLibraryWidget",
          "UserNavbarWidget",
          "ExportWidget"
        ]
      ]);




    });



  });

});
