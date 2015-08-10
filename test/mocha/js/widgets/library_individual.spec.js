define([
  "js/widgets/library_individual/widget",
  "js/bugutils/minimal_pubsub"

], function(

  LibraryWidget,
  MinSub

  ){


  describe("Library Widget (library_individual.spec.js)", function(){


    var stubMetadata  = {

      libraries : [
        {name: "Aliens Among Us", id: "1", description: "Are you one of them?", permission : "owner", num_documents : 300, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'},
        {name: "Everything Sun", id: "2", description: "Where would we be without the sun?", num_documents : 0, permission : "admin", date_created: '2014-01-03 04:30:04', date_last_modified: '2015-01-09 06:30:04'},
        {name: "Space Travel and You", id: "7", description: "", permission : "write", num_documents : 4000, date_created: '2013-06-03 04:30:04', date_last_modified: '2015-06-09 06:30:04'},
        {name: "Space Travel and Me", id: "3", description: "interesting", permission : "read", num_documents : 400, date_created: '2012-06-03 05:30:04', date_last_modified: '2015-07-09 06:30:04'}

      ]
    }

    var stubData  = {
      documents : ["bibcode1", "bibcode2", "bibcode3"]
    }

    var fakeLibraryController =   {
      getHardenedInstance : function(){return this},
      getLibraryMetadata : function(id){var d = $.Deferred(); d.resolve(_.findWhere(stubMetadata.libraries, {id : id})); return d.promise();},
      getLibraryRecords : function(id ){ var d =  $.Deferred(); d.resolve(stubData); return d.promise() },
      updateLibraryContents : function(updateData){var d = $.Deferred(); d.resolve(_.extend({name: "Aliens Among Us", id: 1, description: "Are you one of them?", permission : "owner", num_papers : 45, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'}, updateData )); return d},
      updateLibraryMetadata : sinon.spy(function(updateData){
        var d = $.Deferred();
        d.resolve(_.extend({name: "Aliens Among Us", id: 1, description: "Are you one of them?", permission : "owner", num_papers : 45, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'}, updateData ));
        return d }),
      updateLibraryContents : sinon.spy(function(updateData){var d = $.Deferred(); d.resolve(); return d})

    };

    afterEach(function(){
      $("#test").empty();
    });


    it("should display different header views depending on a person's permissions, allowing admin/owners to edit title/description ", function(){

      var w = new LibraryWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      minsub.beehive.addObject("LibraryController", fakeLibraryController);
      w.activate(minsub.beehive.getHardenedInstance());

      var spy = sinon.spy();
      w.getPubSub = function() {return {publish : spy}};

      $("#test").append(w.render().el);

      w.setSubView("library", "1");

      expect($("#test h2.edit-indicator").next().hasClass("no-show")).to.be.true;

      expect($("#test .s-library-title h2").attr("contenteditable")).to.eql('true');

      //editing title
      $("#test h2.edit-indicator").click();

      expect($("#test h2.edit-indicator").next().hasClass("no-show")).to.be.false;

      expect($("#test h2.edit-indicator").next().hasClass("fadeIn")).to.be.true;

      $("#test h2.edit-indicator").text("goo");

      expect(fakeLibraryController.updateLibraryMetadata.args[0]).to.eql(undefined);

      $("#test h2.edit-indicator").next().find(".submit-edit").click();

      expect(fakeLibraryController.updateLibraryMetadata.args[0][0]).to.eql("1");
      expect(fakeLibraryController.updateLibraryMetadata.args[0][1]).to.eql({name: "goo"});

      //navigating to permissions

      expect($("#test .main .library-detail-view").length).to.eql(1);
      expect($("#test .main .library-admin-view").length).to.eql(0);

      expect($("#test .tab[data-tab=admin]").length).to.eql(1);

      //neither above are possible if you dont have admin privileges,
      //the data sent back by stub function will have "read" permisions
      w.setSubView("library", "3");

      expect($("#test .s-library-title h2").attr("contenteditable")).to.be.undefined;

      expect($("#test .tab[data-tab=admin]").length).to.eql(0);

    });

    it("should allow users to navigate to other subviews (export, metrics, vis, admin)", function(){

      var w = new LibraryWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      w.activate(minsub.beehive.getHardenedInstance());

      var spy = sinon.spy();
      w.getPubSub = function() {return {publish : spy, NAVIGATE: minsub.NAVIGATE}};

      $("#test").append(w.render().el);

      w.setSubView("library", "1");


      //navigating to permissions

      expect($("#test .main .library-detail-view").length).to.eql(1);
      expect($("#test .main .library-admin-view").length).to.eql(0);

      $("#test .tab[data-tab=admin]").click();

      expect(spy.args[0]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "IndividualLibraryWidget",
        {
          "sub": "admin",
          "id": "1"
        }
      ]);

      $("#test li[data-tab=export-bibtex]").click();

      expect(spy.args[1]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "library-export",
        {
          "bibcodes": [
            "bibcode1",
            "bibcode2",
            "bibcode3"
          ],
          "sub": "bibtex",
          "id": "1"
        }
      ]);


      $("#test .tab[data-tab=metrics]").click();

      expect(spy.args[2]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "library-metrics",
        {
          "bibcodes": [
            "bibcode1",
            "bibcode2",
            "bibcode3"
          ],
          "id": "1",
          "sub" : undefined
        }
      ]);


      $("#test li[data-tab=visualization-authornetwork]").click();

      expect(spy.args[3]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "library-visualization",
        {
          "bibcodes": [
            "bibcode1",
            "bibcode2",
            "bibcode3"
          ],
          "sub": "authornetwork",
          "id": "1"
        }
      ]);


      //none of these options are available if the library has 0 bibcodes
      w.setSubView("library", "2");

      //export, metrics, vis disabled

      expect($("#test li[data-tab=export-bibtex]").length).to.eql(0);
      expect($("#test li[data-tab=metrics]").length).to.eql(0);
      expect($("#test li[data-tab=visualization-authornetwork]").length).to.eql(0);


    });

    it("should show a library list that allows you to delete records from a library if you have owner/admin/write permissions", function(){

      var w = new LibraryWidget();

      sinon.spy(LibraryWidget.prototype, "updateView");

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);


      w.setSubView("library", "1");

      //updateView should have been called only once
      expect(LibraryWidget.prototype.updateView.callCount).to.eql(1);

      //has ability to delete records

      expect($("#test .library-item:first").find("button.remove-record").length).to.eql(1);

      $("#test .library-item:first button.remove-record").click();

      expect(fakeLibraryController.updateLibraryContents.callCount).to.eql(1);

      //doesn't have ability to delete records

      w.setSubView("library", "3");

      expect($("#test .library-item:first").find("button.remove-record").length).to.eql(0);


    });






  })



})