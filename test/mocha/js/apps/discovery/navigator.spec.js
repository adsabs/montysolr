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

      var minsub  = new Minsub();

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

    //  n.catalog.get("orcid-page").execute();
    //
    //  //remove this flag that lets us know to show a special modal when user goes to #orcid-page
    //  expect(storage.remove.args[0][0]).to.eql("orcidAuthenticating");
    //
    //  //modal wasn't called
    //  expect(AlertsController.alert.callCount).to.eql(0);
    //
    //  //executestashednav returned true
    //  expect(  n.catalog.get("orcid-page").route).to.be.false;
    //
    //expect(MasterPageManager.show.callCount).to.eql(0);

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

      //executestashednav returned true
      expect(  n.catalog.get("orcid-page").route).to.eql("#user/orcid");

      expect(MasterPageManager.show.callCount).to.eql(1);

      expect(MasterPageManager.show.args[0][0]).to.eql("OrcidPage");








  });



  });

});