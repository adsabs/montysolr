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

      var fakeUser = {
        getHardenedInstance : function(){return this},
        getMyADSData : function() {
          return  fakeMyADS;
        },
        getOpenURLConfig : function(){
          var d = $.Deferred();
          d.resolve(fakeURLConfig);
          return d;

        },
        USER_INFO_CHANGE: User.prototype.USER_INFO_CHANGE,
        getUserData : function() {return {}}
      };

      var fakeOrcid = {
        getHardenedInstance : function(){return this},
        hasAccess : function(){return true}
      };
      minsub.beehive.addService("OrcidApi", fakeOrcid);

      minsub.beehive.addObject("User", fakeUser);

      p.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.APP_STARTED);

      expect(p.model.get("openURLConfig")).to.eql([
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

      expect(JSON.stringify(p.model.toJSON())).to.eql('{"openURLConfig":[{"name":"ohio wesleyan","link":"ohio_wesleyan.edu"},{"name":"virginia wesleyan","link":"virginia_wesleyan.edu"},{"name":"wesleyan university","link":"wesleyan.edu"}],"link_server":"wesleyan.edu","anotherVal":"foo"}');

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
        setUserData : sinon.spy(function(){
          var d = $.Deferred();
          d.resolve({});
          return d.promise();
        }),
        USER_INFO_CHANGE: User.prototype.USER_INFO_CHANGE,
        getUserData : function() {return {}}

      };

      minsub.beehive.addObject("User", fakeUser);

      var fakeOrcid = {
        getHardenedInstance : function(){return this},
        hasAccess : function(){return true}
      };
      minsub.beehive.addService("OrcidApi", fakeOrcid);

      p.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.APP_STARTED);
      minsub.publish(minsub.USER_ANNOUNCEMENT, User.prototype.USER_INFO_CHANGE, fakeMyADS);

      $("#test").append(p.getEl());

      p.setSubView("librarylink");

      expect($("#test .current-link-server").length).to.eql(1);

      expect($(".preferences-widget  p:first").text().trim()).to.eql("Your Library Link Server is set to wesleyan university.");

      expect($(".preferences-widget  select").val()).to.eql('wesleyan.edu');

      $(".preferences-widget button.submit").click();

      //submits the currently selected institution's url
      expect(fakeUser.setUserData.callCount).to.eql(0);

      $(".preferences-widget select").val("ohio_wesleyan.edu");

      $(".preferences-widget button.submit").click();

      expect(fakeUser.setUserData.callCount).to.eql(1);

      expect(fakeUser.setUserData.args[0][0]).to.eql( {link_server: "ohio_wesleyan.edu"});

    });

    it("should show a view that allows the user to update ORCID information ", function(){

      var p = new PreferencesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeUser = {
        getHardenedInstance : function(){return this},
        getMyADSData : function() {
          return  fakeMyADS;
        },
        getOpenURLConfig : function(){
          var d = $.Deferred();
          d.resolve(fakeURLConfig);
          return d;

        },
        USER_INFO_CHANGE: User.prototype.USER_INFO_CHANGE,
        getUserData : function() {return {}}
      };

      var fakeOrcid = {
        getHardenedInstance : function(){return this},
        hasAccess : function(){return true},
        getUserProfile : function(){
          var d = $.Deferred();
          d.resolve({
            "orcid-bio" : { "personal-details" : {"given-names" : {"value" : "Alex"}, "family-name" : {"value" : "Holachek"}}}
          });
          return d.promise();
        },
        setADSUserData : sinon.spy(function(){
          var d = $.Deferred();
          d.resolve({});
          return d.promise();
        }),
        getADSUserData : function(){
          var d = $.Deferred();
          d.resolve({});
          return d.promise();
        }
      };
      minsub.beehive.addService("OrcidApi", fakeOrcid);

      minsub.beehive.addObject("User", fakeUser);

      p.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.APP_STARTED);

      $("#test").append(p.getEl());

      p.setSubView("orcid");

      expect($(".preferences-widget .panel-heading").text()).to.eql('\n\n    ORCID Settings\n    \n    You are signed in to ORCID as Alex Holachek\n\n     Not you?\n        Sign into ORCID as a different user\n    \n    \n\n')

      //should be checked by default if we don't pre-fill the data for the form
      expect($(".authorized-ads-user").is(":checked")).to.be.true;

     $("#aff-input").val("wesleyan university");

      $(".authorized-ads-user").click();

      expect($("#original-orcid-name").val()).to.eql("Holachek, Alex");

      expect($(".orcid-name-row").length).to.eql(0);

      $(".add-another-orcid-name").click();
      $(".add-another-orcid-name").click();

      expect($(".orcid-name-row").length).to.eql(2);

      $("button.remove-name:last").click();

      expect($(".orcid-name-row").length).to.eql(1);

      $(".orcid-name-row input").val("Holachek, Alfred");

      $(".submit").click();

      expect(fakeOrcid.setADSUserData.args[0][0]).to.eql({
        "currentAffiliation": "wesleyan university",
        "authorizedUser": false,
        "nameVariations": [
          "Holachek, Alfred"
        ]
      })

    });

    it("should prepopulate the above mentioned orcid form with user data if available", function(){

      var p = new PreferencesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeUser = {
        getHardenedInstance : function(){return this},
        getMyADSData : function() {
          return  fakeMyADS;
        },
        getOpenURLConfig : function(){
          var d = $.Deferred();
          d.resolve(fakeURLConfig);
          return d;

        },
        USER_INFO_CHANGE: User.prototype.USER_INFO_CHANGE,
        getUserData : function() {return {}}
      };

      var fakeOrcid = {
        getHardenedInstance : function(){return this},
        hasAccess : function(){return true},
        getUserProfile : function(){
          var d = $.Deferred();
          d.resolve({
            "orcid-bio" : { "personal-details" : {"given-names" : {"value" : "Alex"}, "family-name" : {"value" : "Holachek"}}}
          });
          return d.promise();
        },

        getADSUserData : function(){
          var d = $.Deferred();
          d.resolve({
            "currentAffiliation": "wesleyan university",
            "authorizedUser": false,
            "nameVariations": [
              "Holachek, Alfred"
            ]
          });
          return d.promise();
        }
      };
      minsub.beehive.addService("OrcidApi", fakeOrcid);

      minsub.beehive.addObject("User", fakeUser);

      p.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.APP_STARTED);

      $("#test").append(p.getEl());

      p.setSubView("orcid");

      expect($("#aff-input").val()).to.eql("wesleyan university");
      expect($("#original-orcid-name").val()).to.eql("Holachek, Alex");
      expect($(".orcid-name-row input").val()).to.eql("Holachek, Alfred");
      expect($(".authorized-ads-user").is(":checked")).to.be.false;



    })

    it("should allow the user to sign into ORCID from the orcid preferences view", function(){

      var p = new PreferencesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeAppStorage = {
        getHardenedInstance : function(){return this},
        setStashedNav : sinon.spy()
        };

      var fakeOrcid = {
        getHardenedInstance : function(){return this},
        signIn : sinon.spy(),
        hasAccess : sinon.spy(function(){return false}),
      };

      var fakeUser = {
        getHardenedInstance : function(){return this},
        getMyADSData : function() {
          return  fakeMyADS;
        },
        getOpenURLConfig : function() {
          var d = $.Deferred();
          d.resolve(fakeURLConfig);
          return d;
        }

        };

      minsub.beehive.addService("OrcidApi", fakeOrcid);
      minsub.beehive.addObject("AppStorage", fakeAppStorage);
      minsub.beehive.addObject("User", fakeUser);

      p.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.APP_STARTED);

      $("#test").append(p.getEl());

      p.setSubView("orcid");

      expect(fakeOrcid.hasAccess.callCount).to.eql(1);
      expect(fakeOrcid.signIn.callCount).to.eql(0);

      $("button.orcid-authenticate").click();

      expect(fakeOrcid.signIn.callCount).to.eql(1);
      expect(fakeAppStorage.setStashedNav.args[0]).to.eql([
        "UserPreferences",
        {
          "subView": "orcid"
        }
      ]);






    });

  });

 });