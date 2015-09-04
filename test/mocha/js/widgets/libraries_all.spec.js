define([
  "js/widgets/libraries_all/widget",
  "js/bugutils/minimal_pubsub",

], function(

  LibrariesWidget,
  MinSub

  ){

  describe("Libraries Home Widget (libraries.spec.js)", function(){


    afterEach(function(){
      $("#test").empty();
    });

    var stubMetadata  = {

      libraries : [
        {name: "Aliens Among Us", id: "1", description: "Are you one of them?", permission : "owner", num_documents : 300, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2015-06-11T19:57:41.889100"},
        {name: "Everything Sun", id: "2", description: "Where would we be without the sun?", num_documents : 0, permission : "admin", date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2014-06-11T19:57:41.889100"},
        {name: "Space Travel and You", id: "7", description: "", permission : "write", num_documents : 4000, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2013-06-11T19:57:41.889100"},
        {name: "Space Travel and Me", id: "3", description: "interesting", permission : "read", num_documents : 400, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2012-06-11T19:57:41.889100"}

      ],

      libraries_version_2 : [
        {name: "Aliens Among Us", id: "1", description: "Are you one of them?", permission : "owner", num_documents : 300, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2015-06-11T19:57:41.889100"},
        {name: "Everything Sun", id: "2", description: "Where would we be without the sun?", num_documents : 0, permission : "admin", date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2014-06-11T19:57:41.889100"},
        {name: "Space Travel and You", id: "7", description: "", permission : "write", num_documents : 4000, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2013-06-11T19:57:41.889100"},
        {name: "Space Travel and Me", id: "3", description: "interesting", permission : "read", num_documents : 400, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2012-06-11T19:57:41.889100"},
        {name: "Another title", id: "4", description: "interesting", permission : "read", num_documents : 400, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2012-06-11T19:57:41.889100"}
      ]
    }

    var stubData  = {
      documents : ["bibcode1", "bibcode2", "bibcode3"]
    }


    it("should render a table with an entry for each library", function(){

      var w = new LibrariesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});


      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);

      minsub.publish(minsub.LIBRARY_CHANGE, stubMetadata.libraries);

      w.setSubView({view : "libraries"});

      //4 entries
      expect($("#test tbody tr").length).to.eql(4);

      expect($("#test tbody tr:first").html()).to.eql('\n\n<td>\n    1\n</td>\n<td>\n    \n    <i class="fa fa-lg fa-lock" title="this library is private"></i>\n    \n</td>\n\n<td>\n\n    <a href="#user/libraries/1"><h3 class="s-library-title">Aliens Among Us</h3></a>\n    <p>Are you one of them?</p> \n\n</td>\n\n<td>300</td>\n\n<td>owner</td>\n\n<td>6/4/15 7:57p</td>\n\n');

      //keeps in sync with controller: adding a record
      minsub.publish(minsub.LIBRARY_CHANGE, stubMetadata.libraries_version_2);

      //5 entries
      expect($("#test tbody tr").length).to.eql(5);


    });

    it ("the table should be sortable", function(){

      var w = new LibrariesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);

      minsub.publish(minsub.LIBRARY_CHANGE, stubMetadata.libraries);

      w.setSubView({view : "libraries"});

      //default sort is ascending by title

      expect($("#test tbody tr h3").map(function(e){return this.textContent}).get()).to.eql(["Aliens Among Us", "Everything Sun", "Space Travel and Me", "Space Travel and You"]);

      //title (desc)
      $("#test button.s-sort-active").click();

      expect($("#test tbody tr h3").map(function(e){return this.textContent}).get()).to.eql(["Space Travel and You", "Space Travel and Me", "Everything Sun", "Aliens Among Us"]);

      //sort by num papers (desc)

      $("#test thead th").eq(3).find("button").click();
      $("#test thead th").eq(3).find("button").click();

      expect($("#test tbody td:nth-of-type(4)").map(function(e){return this.textContent}).get()).to.eql(  [ '4000', '400', '300', '0' ] );

      //sort by permission

      $("#test thead th").eq(4).find("button").click();

      expect($("#test tbody td:nth-of-type(5)").map(function(e){return this.textContent}).get()).to.eql(["read", "write", "admin", "owner"]);

      //sort by date last modified (desc)

      $("#test thead th").eq(5).find("button").click();
      $("#test thead th").eq(5).find("button").click();

      expect($("#test tbody td:nth-of-type(6)").map(function(e){return this.textContent}).get()).to.eql(["6/4/15 7:57p", "6/3/14 7:57p", "6/2/13 7:57p", "6/1/12 7:57p"]);

    })



    it("should emit the proper NAVIGATE events when the user clicks on a table row", function(){

      var w = new LibrariesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      w.activate(minsub.beehive.getHardenedInstance());

      sinon.spy(w.pubsub, "publish");

      $("#test").append(w.render().el);

      minsub.publish(minsub.LIBRARY_CHANGE, stubMetadata.libraries);

      w.setSubView({view : "libraries"});

      $("#test h3.s-library-title:first").click();

      expect(w.pubsub.publish.args[0]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "IndividualLibraryWidget",
        {
          "sub": "library",
          "id": "1"
        }
      ]);

    });




  })



})