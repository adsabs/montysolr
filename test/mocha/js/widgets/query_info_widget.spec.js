define([
  "js/widgets/query_info/query_info_widget",
  "js/bugutils/minimal_pubsub",
  "js/components/app_storage"
], function(
  QueryInfo,
  MinSub,
  AppStorage
  ){

  describe("Query Info Widget", function(){

    afterEach(function(){

      $("#test").empty();

    });


    it("should show key information about the query", function(){

      var w = new QueryInfo();

      var minsub = new (MinSub.extend({
      request: function(apiRequest) {
        return {some: 'foo'}
        }
      }))({verbose: false});

      w.activate(minsub.beehive.getHardenedInstance());

      minsub.publish(minsub.FEEDBACK, minsub.createFeedback({
        code: minsub.T.FEEDBACK.CODES.SEARCH_CYCLE_STARTED,
        query: new minsub.T.QUERY({q: "foo", "fq" : "a filter"}),
        numFound: 841359
      }));


      $("#test").append(w.render().el);

      expect($("#test").find(".active-filters").text().trim()).to.eql('Active Filters:\n        \n        show' );

      $(".show-filter").click();

      expect($("#test").find(".active-filters").text().trim()).to.eql('Active Filters:\n        \n        \n            \n            a filter\n            \n        \n        hide');


    });

    it("should listen to updates from app_storage about selected papers, and allow user to clear app storage", function(){

      var w = new QueryInfo();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var s =   new AppStorage();

      s.clearSelectedPapers = sinon.spy();

      minsub.beehive.addObject("AppStorage", s)

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);

      expect($(".currently-selected").text().trim()).to.eql('0 selected\n        \n        \n              select all on page');

      minsub.publish(minsub.STORAGE_PAPER_UPDATE, 10);

      expect($(".currently-selected").text().trim().split(/\n/)[0]).to.eql('10 selected');

      expect(s.clearSelectedPapers.callCount).to.eql(0);

      $("#test").find(".clear-selected").click();

      expect(s.clearSelectedPapers.callCount).to.eql(1);


    });


   it.skip("should allow authenticated user to input selected/all papers into a pre-existing library", function(done){

     var w = new QueryInfo();

     var minsub = new (MinSub.extend({
       request: function(apiRequest) {
         return {some: 'foo'}
       }
     }))({verbose: false});

     var fakeLibraryController =   {getHardenedInstance : function(){return this},
       getLibraryMetadata : function(){return []},
       addBibcodesToLib : sinon.spy(function(){ var d = $.Deferred(); d.resolve(); return d.promise()}),
       createLibAndAddBibcodes : sinon.spy(function(){ var d = $.Deferred(); d.resolve(); return d.promise()})
     };

     var fakeUser = {getHardenedInstance : function(){return this}, isLoggedIn : function() {return true} }

     minsub.beehive.addObject("LibraryController", fakeLibraryController);

     minsub.beehive.addObject("User", fakeUser)

     w.activate(minsub.beehive.getHardenedInstance());

     var response = new minsub.T.RESPONSE({"responseHeader": {
         "params": {
         }
       },
         "response": {
           "numFound": 841359
         }
       }
     );
     response.setApiQuery(new minsub.T.QUERY({q: "foo", "fq" : "a filter"}));


     minsub.publish(minsub.DELIVERING_RESPONSE, response);

     $("#test").append(w.render().el);

     minsub.publish(minsub.STORAGE_PAPER_UPDATE, 10);

     minsub.publish(minsub.LIBRARY_CHANGE, [{id: "1", name: "Stars? Stars!!!"}, {id: "2", name : "I See the Moon"}]);

     expect($("#test .library-add-title").length).to.eql(0);

     expect($("#test #library-console").hasClass("in")).to.be.false;

     minsub.publish(minsub.USER_ANNOUNCEMENT, "user_info_change");

     //widget will set loggedIn to true and re-render
     //open the drawer
    $("#test .library-add-title").click();

     setTimeout(function(){

       expect($("#test #library-console").hasClass("in")).to.be.true;

       expect($("#test #all-vs-selected")[0].options[0].value).to.eql("selected");
       expect($("#test #all-vs-selected")[0].options[1].value).to.eql("all");

       expect($("#test #library-select")[0].options[0].value).to.eql("1");
       expect($("#test #library-select")[0].options[0].textContent).to.eql("Stars? Stars!!!");


       $("#test .submit-add-to-library").click();

       expect(fakeLibraryController.addBibcodesToLib.args[0][0]).to.eql({library: "1", bibcodes: "selected"});

       $("#test input[name=new-library-name]").val("fakeName")

       $("#test .submit-create-library").click();

       expect(fakeLibraryController.createLibAndAddBibcodes.args[0][0]).to.eql({ bibcodes: "selected", name : "fakeName" });

       done();

     }, 500);


   });

    it.skip("should allow authenticated user to input selected/all papers into a pre-existing library", function(done){

      var w = new QueryInfo();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeLibraryController =   {getHardenedInstance : function(){return this},
        getLibraryMetadata : function(){return []},
        addBibcodesToLib : sinon.spy(function(){ var d = $.Deferred(); d.resolve(); return d.promise()}),
        createLibAndAddBibcodes : sinon.spy(function(){ var d = $.Deferred(); d.resolve(); return d.promise()})
      };

      var fakeUser = {getHardenedInstance : function(){return this}, isLoggedIn : function() {return true} }

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      minsub.beehive.addObject("User", fakeUser)

      w.activate(minsub.beehive.getHardenedInstance());

      var response = new minsub.T.RESPONSE({"responseHeader": {
          "params": {
          }
        },
          "response": {
            "numFound": 841359
          }
        }
      );
      response.setApiQuery(new minsub.T.QUERY({q: "foo", "fq" : "a filter"}));


      minsub.publish(minsub.DELIVERING_RESPONSE, response);

      $("#test").append(w.render().el);

      minsub.publish(minsub.STORAGE_PAPER_UPDATE, 10);

      minsub.publish(minsub.LIBRARY_CHANGE, [{id: "1", name: "Stars? Stars!!!"}, {id: "2", name : "I See the Moon"}]);

      expect($("#test .library-add-title").length).to.eql(0);

      expect($("#test #library-console").hasClass("in")).to.be.false;

      minsub.publish(minsub.USER_ANNOUNCEMENT, "user_info_change", "USER");

      //widget will set loggedIn to true and re-render
      //open the drawer
     $("#test .library-add-title").click();

      setTimeout(function(){

        expect($("#test #library-console").hasClass("in")).to.be.true;

        expect($("#test #all-vs-selected")[0].options[0].value).to.eql("selected");
        expect($("#test #all-vs-selected")[0].options[1].value).to.eql("all");

        expect($("#test #library-select")[0].options[0].value).to.eql("1");
        expect($("#test #library-select")[0].options[0].textContent).to.eql("Stars? Stars!!!");


        $("#test .submit-add-to-library").click();

        expect(fakeLibraryController.addBibcodesToLib.args[0][0]).to.eql({library: "1", bibcodes: "selected"});

        $("#test input[name=new-library-name]").val("fakeName")

        $("#test .submit-create-library").click();

        expect(fakeLibraryController.createLibAndAddBibcodes.args[0][0]).to.eql({ bibcodes: "selected", name : "fakeName" });

        done();

      }, 500);


    });


    it("should show appropriate feedback", function(){




    })





  });


});