define([
    "js/widgets/preferences/widget",
    "js/bugutils/minimal_pubsub",
    "js/components/user"
  ],
  function(
    PreferencesWidget,
    MinSub,
    User
    ){

  describe("Preferences Widget (UI Widget)", function(){

    afterEach(function(){
      $("#test").empty();
    });


    var fakeURLConfig = [
      {
        "name": "ohio wesleyan",
        "link": "ohio_wesleyan.edu"
      },
      {
        "name": "virginia wesleyan",
        "link": "virginia_wesleyan.edu"
      },
      {
        "name": "wesleyan university",
        "link": "wesleyan.edu"
      }
    ];

    var fakeMyADS = {
      link_server : "wesleyan.edu",
      anotherVal : "foo"
      };


    it("should have a model that contains updated myads data and a collection of openurl endpoints", function(){

      var p = new PreferencesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeUser = {getHardenedInstance : function(){return this},
        getMyADSData : function() {
          return  fakeMyADS;
        },
        getOpenURLConfig : function(){
          var d = $.Deferred();
          d.resolve(fakeURLConfig);
          return d;

        },
        USER_INFO_CHANGE: User.prototype.USER_INFO_CHANGE
      };

      minsub.beehive.addObject("User", fakeUser);

      p.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.APP_STARTED);

      expect(p.openURLCollection.toJSON()).to.eql([
        {
          "name": "ohio wesleyan",
          "link": "ohio_wesleyan.edu"
        },
        {
          "name": "virginia wesleyan",
          "link": "virginia_wesleyan.edu"
        },
        {
          "name": "wesleyan university",
          "link": "wesleyan.edu"
        }
        ]);

      minsub.publish(minsub.USER_ANNOUNCEMENT, User.prototype.USER_INFO_CHANGE, fakeMyADS);

      expect(p.model.toJSON()).to.eql({
        "link_server": "wesleyan.edu",
        "anotherVal": "foo"
      });

    });

    it("should show a view that allows user to set open url link server", function(){

      var p = new PreferencesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeUser = {getHardenedInstance : function(){return this},
        getMyADSData : function() {
          return  fakeMyADS;
        },
        getOpenURLConfig : function(){
          var d = $.Deferred();
          d.resolve(fakeURLConfig);
          return d;

        },
        setUserData : sinon.spy(),
        USER_INFO_CHANGE: User.prototype.USER_INFO_CHANGE
        };

      minsub.beehive.addObject("User", fakeUser);

      p.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.APP_STARTED);
      minsub.publish(minsub.USER_ANNOUNCEMENT, User.prototype.USER_INFO_CHANGE, fakeMyADS);

      $("#test").append(p.getEl());

      expect($("#test .current-link-server").length).to.eql(1);

      expect($(".preferences-widget .openurl-container p:first").text().trim()).to.eql("Your Library Link Server is set to wesleyan university.");

      expect($(".preferences-widget select").val()).to.eql('wesleyan.edu');

      $(".preferences-widget button.submit").click();

      //submits the currently selected institution's url
      expect(fakeUser.setUserData.args[0]).to.eql( [ { link_server : 'wesleyan.edu' } ] );

    });

    it("should show a view that allows user to set open url link server", function(){

      var p = new PreferencesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeUser = {getHardenedInstance : function(){return this},

        getOpenURLConfig : function(){
          var d = $.Deferred();
          d.resolve(fakeURLConfig);
          return d;

        },
        setMyADSData : sinon.spy()

      };

      minsub.beehive.addObject("User", fakeUser);

      p.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.APP_STARTED);
      minsub.publish(minsub.USER_ANNOUNCEMENT, User.prototype.USER_INFO_CHANGE, {
        link_server : undefined,
        anotherVal : "foo"
      } );

      $("#test").append(p.getEl());

      expect($("#test .current-link-server").length).to.eql(0);




    });


  });



  })