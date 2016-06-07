define([
  "js/widgets/libraries_all/widget",
  "js/bugutils/minimal_pubsub",
  "moment"

], function(
  LibrariesWidget,
  MinSub,
  moment
  ){

  describe("Libraries Home Widget (UI Widget)", function(){


    afterEach(function(){
      $("#test").empty();
    });

    var stubMetadata  = {

      libraries : [
        {name: "Aliens Among Us", id: "1", description: "Are you one of them?", permission : "owner", num_documents : 300, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2015-06-11T19:57:41.889100"},
        {name: "Everything Sun", id: "2", description: "Where would we be without the sun?", num_documents : 0, permission : "admin", date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2014-06-11T19:57:41.889100"},
        {name: "Space Travel and You", id: "7", description: "", permission : "write", num_documents : 4000, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2013-06-11T19:57:41.889100"},
        {name: "Space Travel and Me", id: "3", description: "interesting", permission : "read", num_documents : 400, date_created: "2015-06-11T19:57:41.889100", date_last_modified: "2012-06-11T19:57:41.889100"}
      ]
    }

    var stubData  = {
      documents : ["bibcode1", "bibcode2", "bibcode3"]
    }

    var fakeLibraryController =   {
      getHardenedInstance : function(){return this},
      getLibraryMetadata : function(){ return $.Deferred().resolve(stubMetadata.libraries) },
      getLibraryData : function(id ){ var d =  $.Deferred(); d.resolve(stubData); return d.promise() },
      updateLibraryContents : function(updateData){var d = $.Deferred(); d.resolve(_.extend({name: "Aliens Among Us", id: 1, description: "Are you one of them?", permission : "owner", num_papers : 45, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'}, updateData )); return d},
      updateLibraryMetadata : sinon.spy(function(updateData){
        var d = $.Deferred();
        d.resolve(_.extend({name: "Aliens Among Us", id: 1, description: "Are you one of them?", permission : "owner", num_papers : 45, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'}, updateData ));
        return d }),
      updateLibraryContents : sinon.spy(function(updateData){var d = $.Deferred(); d.resolve(); return d})

    };


    it("should render a table with an entry for each library", function(){

      var w = new LibrariesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});


      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);

      w.setSubView({view : "libraries"});

      //4 entries
      expect($("#test tbody tr").length).to.eql(4);

      //hack for system date time
      var entry  = normalizeSpace($("#test tbody tr:first").html());
      if (entry.match("7:57pm")){
        expect(entry).to.eql(
        '<td> 1 </td> <td> <i class="fa fa-lg fa-lock" title="this library is private"></i> </td> <td> <a href="#user/libraries/1"><h3 class="s-library-title">Aliens Among Us</h3></a> <p>Are you one of them?</p> </td> <td>300</td> <td>owner</td> <td>Jun 11 2015, 7:57pm</td>'
        )
      }
      else {
        expect(entry).to.eql(
            '<td> 1 </td> <td> <i class="fa fa-lg fa-lock" title="this library is private"></i> </td> <td> <a href="#user/libraries/1"><h3 class="s-library-title">Aliens Among Us</h3></a> <p>Are you one of them?</p> </td> <td>300</td> <td>owner</td> <td>Jun 11 2015, 3:57pm</td>'
        )
      }

    });

    it ("the table should be sortable", function(){

      var w = new LibrariesWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);

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


        var dates = $("#test tbody td:nth-of-type(6)").map(function(i,d){return $(d).text()}).get();

        if (dates[0] === "Jun 11 2015, 3:57pm"){
          expect(dates).to.eql(["Jun 11 2015, 3:57pm","Jun 11 2014, 3:57pm","Jun 11 2013, 3:57pm","Jun 11 2012, 3:57pm"])
        }
      else {
          //hack, assume UTC
          expect(dates).to.eql(["Jun 11 2015, 7:57pm","Jun 11 2014, 7:57pm","Jun 11 2013, 7:57pm","Jun 11 2012, 7:57pm"])
        }

    })



  })



})