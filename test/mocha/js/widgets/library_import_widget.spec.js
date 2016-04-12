define([
'js/widgets/library_import/widget',
 'js/bugutils/minimal_pubsub',
'js/components/api_targets'

], function(
LibraryImportWidget,
MinimalPubsub,
ApiTargets
){

  describe("Library Import Widget for Signed In User (UI Widget)", function(){

    var minsub, requestSpy, alreadyAuthorized, importLibraries;
    beforeEach(function(){

      minsub = new (MinimalPubsub.extend({
        request: function(apiRequest) {
        }
      }))({verbose: false});

      requestSpy =  sinon.spy(function(request){

        if (request.get("target")== ApiTargets.LIBRARY_IMPORT_CLASSIC_MIRRORS){
          request.get("options").done([
            'classic_mirror_1',
            'classic_mirror_2',
            'classic_mirror_3'
          ]);
        }
        else if (request.get("target") == ApiTargets.LIBRARY_IMPORT_CLASSIC_AUTH){
          request.get("options").done(request.get("options").data);
        }
        else if (request.get("target") == ApiTargets.LIBRARY_IMPORT_CLASSIC_TO_BBB){
          request.get("options").done(
              [{"action": "created", "library_id": "fdsfsfsdfdsfds", "name": "Name", "num_added": 4, "description": "Description"},
                {"action": "created", "library_id": "dsadsadsadsa", "name": "Name2", "num_added": 4, "description": "Description2"}]
          )
        }
        else if (request.get("target") == ApiTargets.LIBRARY_IMPORT_CREDENTIALS){
          if (alreadyAuthorized) {
            request.get("options").done({"classic_email": "fake@fakitifake.com", "classic_mirror": "mirror", "twopointoh_email": "fakeads2@gmail.com"})
          } else {
            request.get("options").done({"classic_email": "", "classic_mirror": "", "twopointoh_email": ""})
          }

        }

      });

      var fakeApi = {
        getHardenedInstance : function(){return this},
        request : requestSpy
      }

      minsub.beehive.removeService("Api", fakeApi);
      minsub.beehive.addService("Api", fakeApi);

      importLibraries = sinon.spy(function(service){

          var d = $.Deferred();

          if (service === "classic"){
            d.resolve(
                [{"action": "created", "library_id": "fdsfsfsdfdsfds", "name": "Name", "num_added": 4, "description": "Description"},
                  {"action": "created", "library_id": "dsadsadsadsa", "name": "Name2", "num_added": 4, "description": "Description2"}]
            )
          }
          else if (service === "twopointoh"){
            d.resolve([{"action": "created", "library_id": "fdsfsfsdfdsfds", "name": "a library", "num_added": 4, "description": "Description"},
              {"action": "created", "library_id": "dsadsadsadsa", "name": "anotherlibrary", "num_added": 4, "description": "Description2"}])
          }

          return d.promise();

        });



      var fakeLibraryController = {
        getHardenedInstance : function(){return this},
        importLibraries : importLibraries
      }

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

    })

    afterEach(function(){

      $("#test").empty();

    });


    it("should be a marionette parent view using bootstrap tab widget w two tabs: classic & 2.0", function(){

      $("#test").append( new LibraryImportWidget().render().el);
      expect($("div.tab-pane.active").attr("id")).to.eql("classic-import-tab");
      $("a[href=#twopointoh-import-tab]").click();
      expect($("div.tab-pane.active").attr("id")).to.eql("twopointoh-import-tab");

    });

    it("should request a list of classic mirror sites on initial activation", function(){

      var l = new LibraryImportWidget();
      $("#test").append( l.render().el);

      expect($("#classic-mirror-list").find("option").text()).to.eql("");

      l.activate(minsub.beehive.getHardenedInstance());

      expect($("#classic-mirror-list").find("option").text()).to.eql("classic_mirror_1classic_mirror_2classic_mirror_3")

    });

    it("should request authentication info for both classic + 2.0 when it is shown the first time", function(){

      alreadyAuthorized = true;

      var l = new LibraryImportWidget();
      l.activate(minsub.beehive.getHardenedInstance());

      $("#test").append( l.render().el);

      expect(requestSpy.args[1][0].toJSON().target).to.eql( "harbour/user" );
    //check that both forms show the correct authentication text
      expect($(".library-import-form>div:first-of-type").text().replace(/\s/g, "")).to
          .eql("Youarecurrentlyauthenticatedasfake@fakitifake.comwithmirrorsitemirror.Youarecurrentlyauthenticatedasfakeads2@gmail.com.")

    });

    it("both views should by default show a form to enter authentication information ", function(){

      alreadyAuthorized = true;

      var l = new LibraryImportWidget();
      l.activate(minsub.beehive.getHardenedInstance());

      $("#test").append( l.render().el);

      $("input#classic-username").val("Alex");
      $("input#classic-password").val("Foo");
      $("select#classic-mirror-list").val("classic_mirror_2");
      $("#classic-import-tab button.submit-credentials").click();

      expect(requestSpy.args[2][0].get("options").data).to.eql({classic_email: "Alex", classic_password: "Foo", classic_mirror: "classic_mirror_2"});
      expect(requestSpy.args[2][0].get("target")).to.eql(ApiTargets.LIBRARY_IMPORT_CLASSIC_AUTH);


      $("input#twopointoh-username").val("AlexH");
      $("input#twopointoh-password").val("Foo2");
      $("#twopointoh-import-tab button.submit-credentials").click();

      expect(requestSpy.args[3][0].get("options").data).to.eql({twopointoh_email: "AlexH", twopointoh_password: "Foo2"});
      expect(requestSpy.args[3][0].get("target")).to.eql(ApiTargets.LIBRARY_IMPORT_ADS2_AUTH);

      //now both forms should show the authenticated view (which has the import-all-libraries button)
      expect($(".import-all-libraries").get().length).to.eql(2);

    });


    it("should transmit library import request to libraryController and show proper success/fail message", function(){

      alreadyAuthorized = true;

      var l = new LibraryImportWidget();
      l.activate(minsub.beehive.getHardenedInstance());

      $("#test").append( l.render().el);

      $("#classic-import-tab .import-all-libraries").click();

      expect(importLibraries.args[0][0]).to.eql("classic");

      expect($("#classic-import-tab .status").text().trim().replace(/\s+/g, " ")).to.eql("× Success! The following libraries were successfully imported for the first time: Name Name2");
      expect($("#twopointoh-import-tab .status").text().trim()).to.eql("");


      $("#twopointoh-import-tab .import-all-libraries").click();

      expect(importLibraries.args[1][0]).to.eql("twopointoh");

      expect($("#twopointoh-import-tab .status").text().trim().replace(/\s+/g, " ")).to.eql('× Success! The following libraries were successfully imported for the first time: a library anotherlibrary');

    });

    it("should offer in 2.0 view option to download all libraries as zotero bibtex", function(){

      alreadyAuthorized = true;

      var l = new LibraryImportWidget();
      l.activate(minsub.beehive.getHardenedInstance());

      $("#test").append( l.render().el);
      
      $("#twopointoh-import-tab .bibtex-import[data-target=zotero]").click();

      expect(requestSpy.args[2][0].toJSON().target).to.eql("harbour/export/twopointoh/zotero");

      $("#twopointoh-import-tab .bibtex-import[data-target=mendeley]").click();

      expect(requestSpy.args[3][0].toJSON().target).to.eql("harbour/export/twopointoh/mendeley");


    });

  });



});