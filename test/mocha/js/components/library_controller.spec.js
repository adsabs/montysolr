define([
  "js/components/library_controller",
  "js/bugutils/minimal_pubsub",
  "js/components/json_response"

], function(
  LibraryController,
  MinSub,
  JSONResponse

  ){


  describe("Library Controller", function(){


    var stubMetadata  = {

      libraries : [
        {name: "Aliens Among Us", id: "1", description: "Are you one of them?", permission : "owner", num_documents : 300, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'},
        {name: "Everything Sun", id: "2", description: "Where would we be without the sun?", num_documents : 0, permission : "admin", date_created: '2014-01-03 04:30:04', date_last_modified: '2015-01-09 06:30:04'},
        {name: "Space Travel and You", id: "7", description: "", permission : "write", num_documents : 4000, date_created: '2013-06-03 04:30:04', date_last_modified: '2015-06-09 06:30:04'},
        {name: "Space Travel and Me", id: "3", description: "interesting", permission : "read", num_documents : 400, date_created: '2012-06-03 05:30:04', date_last_modified: '2015-07-09 06:30:04'}

      ]
    };


    // stub of what will be returned from a GET to /libraries/[library ID]
    var stubLibraryData  = {
      documents : ["bibcode1", "bibcode2", "bibcode3"]
    }



    it("should offer a hardened interface to widgets with the relevant library CRUD operations", function(){

      var l = new LibraryController();

      expect(_.keys(l.getHardenedInstance())).to.eql([
        "getAllMetadata",
        "getLibraryData",
        "createLibrary",
        "createLibAndAddBibcodes",
        "addBibcodesToLib",
        "deleteLibrary",
        "updateLibraryContents",
        "updateLibraryMetadata",
        "__facade__",
        "mixIn"
      ]);

    });

    it("should automatically keep the libraries metadata collection in sync throughout different CRUD operations", function(){

      var l = new LibraryController();


      var minsub = new (MinSub.extend({
        request: function() {
          return {some: 'foo'}
        }
      }))({verbose: false});


      l.activate(minsub.beehive);


      //all calls to compose request will resolve w/done
      l.composeRequest = sinon.spy(function(target, method){
        var d = $.Deferred();
        //update metadata
        if (target ==  "biblib/documents/2" && method == "PUT"){
          d.resolve({name: "nothing sun", id: "2", description: "Where would we be without the sun?", num_documents : 0, permission : "admin", date_created: '2014-01-03 04:30:04', date_last_modified: '2015-01-09 06:30:04'})
        }
        //add records
        else if ( target == "biblib/documents/7" && method == "POST"){
          d.resolve({ number_added : 4 });
        }

        else {
          d.resolve(stubMetadata);
        }
        return d.promise();
      });


      //causes library controller to fetch its data
      minsub.publish(minsub.USER_ANNOUNCEMENT, "user_signed_in");

      expect(l.composeRequest.args[0]).to.eql(["biblib/libraries", "GET"]);

      // actions that can affect the library metadata collection:
      // add lib, delete lib, update metadata,
      // change permissions (not implemented yet), add/remove items

      l.createLibrary({name: "fake", bibcodes : ["fake"]});

      //second request to endpoint is POST to de
      expect(l.composeRequest.args[1]).to.eql(["biblib/libraries", "POST", {
        "data": {
          "name": "fake",
          "bibcodes": [
            "fake"
          ]
        }
      }]);

      expect(l.composeRequest.args[2]).to.eql(["biblib/libraries", "GET"]);

      l.collection.reset(stubMetadata.libraries);

      expect(l.collection.get(1)).to.be.instanceOf(Backbone.Model);


      l.deleteLibrary(1);

      expect(l.composeRequest.args[3]).to.eql(["biblib/documents/1", "DELETE"]);

      //record was removed
      expect(l.collection.get(1)).to.be.undefined;


      l.updateLibraryMetadata(2, {name: "nothing sun"});

      expect(l.composeRequest.args[4]).to.eql([
        "biblib/documents/2",
        "PUT",
        {
          "data": {
            "name": "nothing sun"
          }
        }
      ]);

      expect(l.collection.get(2).get("name")).to.eql("nothing sun");
      expect(l.collection.get(7).get("num_documents")).to.eql(4000);
      l.updateLibraryContents(7, {bibcode : [1,2,3,4]})
      expect(l.collection.get(7).get("num_documents")).to.eql(4004);

    });

    it("should notify widgets of the status of the collection", function() {

      var l = new LibraryController();

      var minsub = new (MinSub.extend({
        request: function () {
          return {some: 'foo'}
        }
      }))({verbose: false});

      l.activate(minsub.beehive);

      //all calls to compose request will resolve w/done
      l.composeRequest = sinon.spy(function (target, method) {
        var d = $.Deferred();
        d.resolve(stubMetadata);
        return d.promise();
      });

      l.getBeeHive().getService("PubSub").publish = sinon.spy();

      l._fetchAllMetadata();

      //when collection changed, pubsub event was sent that notifies widgets of
      //the new collection, what is in it, and what the event was

      expect(JSON.stringify(l.getBeeHive().getService("PubSub").publish.args[0])).to.eql('["[PubSub]-Library-Change",[{"name":"Aliens Among Us","id":"1","description":"Are you one of them?","permission":"owner","num_documents":300,"date_created":"2015-04-03 04:30:04","date_last_modified":"2015-04-09 06:30:04","public":false,"num_users":1,"title":""},{"name":"Everything Sun","id":"2","description":"Where would we be without the sun?","num_documents":0,"permission":"admin","date_created":"2014-01-03 04:30:04","date_last_modified":"2015-01-09 06:30:04","public":false,"num_users":1,"title":""},{"name":"Space Travel and You","id":"7","description":"","permission":"write","num_documents":4000,"date_created":"2013-06-03 04:30:04","date_last_modified":"2015-06-09 06:30:04","public":false,"num_users":1,"title":""},{"name":"Space Travel and Me","id":"3","description":"interesting","permission":"read","num_documents":400,"date_created":"2012-06-03 05:30:04","date_last_modified":"2015-07-09 06:30:04","public":false,"num_users":1,"title":""}],{"ev":"reset"}]');

    });

    it("should allow widgets to get information from /libraries/[id]", function(){

      var l = new LibraryController();


      l.composeRequest = sinon.spy(function(){
        var d = $.Deferred();
        d.resolve(stubMetadata);
        return d.promise();
      });


      l.getLibraryData("2");

      var req = l.composeRequest.args[0];

      expect(req[0]).to.eql("biblib/libraries/2");
      expect(req[1]).to.eql("GET");


    });

    it("should have an internal method, composeRequest, that actually composes API request and returns and resolves a promise (so public functions can return the promise)", function(){

      var l = new LibraryController();

      var minsub = new (MinSub.extend({
        request: function() {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeApi = {getHardenedInstance : function(){return this}, request : function(){
        arguments[0].toJSON().options.done();
      }};

      minsub.beehive.removeService("Api");
      minsub.beehive.addService("Api", fakeApi);

      l.activate(minsub.beehive.getHardenedInstance());

      l.fetchAllLibraryData = sinon.spy();

      l._broadcastLibraryMetadata = sinon.spy();

      var promise = l.composeRequest("fakeTarget", "POST");

      expect(promise.state()).to.eql("resolved");


    });

    it("should allow widgets to create + delete libraries", function(){

      var l = new LibraryController();

      var minsub = new (MinSub.extend({
        request: function() {
          return {some: 'foo'}
        }
      }))({verbose: false});

      l.activate(minsub.beehive.getHardenedInstance());

      l.composeRequest = sinon.spy(function(){ var d = $.Deferred();d.resolve(stubMetadata.libraries);return d.promise()});
      l.fetchAllLibraryData = sinon.spy();

      l.getBeeHive().getService("PubSub").publish = sinon.spy();

      l.createLibrary( {name : "fake library name" });

      expect(l.composeRequest.args[0]).to.eql([
        "biblib/libraries",
        "POST",
        {
          "data": {
            "name": "fake library name"
          }
        }
      ]);

      var getBibcodesStub = sinon.stub(l, "_getBibcodes", function(){
        var d = new $.Deferred();
        d.resolve({
          bibcodes : [
              "bib1",
              "bib2",
              "bib3"
          ]
        });
        return d.promise();
      });
      var createLibrarySpy = sinon.spy(l, "createLibrary");

      l.createLibAndAddBibcodes({});

      expect(JSON.stringify(createLibrarySpy.args[0][0])).to.eql('{"bibcode":{"bibcodes":["bib1","bib2","bib3"]}}');

      l.deleteLibrary("4");

      expect(l.composeRequest.args[4]).to.eql(["biblib/documents/4", "DELETE"]);

      //should result in 1 call to composeRequest and 2 calls to pubsub on successful completion

      expect(l.getBeeHive().getService("PubSub").publish.args[2]).to.eql(["[Router]-Navigate-With-Trigger", "AllLibrariesWidget", "libraries"]);

      expect(JSON.stringify(l.getBeeHive().getService("PubSub").publish.args[3])).to.eql(JSON.stringify([
        "[Alert]-Message",
        {
          "code": 0,
          "msg": "Library <b>undefined</b> was successfully deleted"
        }
      ]));

      getBibcodesStub.restore();
      createLibrarySpy.restore();

    });

    it("should allow widgets to add bibcodes to libraries", function(){

      var l = new LibraryController();

      var minsub = new (MinSub.extend({
        request: function() {
          return {some: 'foo'}
        }
      }))({verbose: false});

      l.activate(minsub.beehive.getHardenedInstance());

      l.composeRequest = sinon.spy(function(){ var d = $.Deferred();d.resolve(stubMetadata.libraries);return d.promise()});

      var getBibcodesStub = sinon.stub(l, "_getBibcodes", function(){
        var d = new $.Deferred();
        d.resolve({
          bibcodes : [
            "bib1",
            "bib2",
            "bib3"
          ]
        });
        return d.promise();
      });

      var updateLibraryContentsSpy = sinon.spy(l, "updateLibraryContents");

      l.collection.reset(stubMetadata.libraries);

      l.addBibcodesToLib({ library : "7" });

      expect(JSON.stringify(updateLibraryContentsSpy.args[0])).to.eql('["7",{"bibcode":{"bibcodes":["bib1","bib2","bib3"]},"action":"add"}]')

      getBibcodesStub.restore();
      updateLibraryContentsSpy.restore();

    });



    it("should offer convenience methods for interfacing with current query/ app storage, getting relevant bibcodes, and adding those bibcodes to libraries", function(){

      var l = new LibraryController();

      l._currentQuery = new MinSub.prototype.T.QUERY();

      l._executeApiRequest = sinon.spy(function(){
        var d = $.Deferred();
        d.resolve( new JSONResponse({response : {docs : [{bibcode : "1", bibcode : "2", bibcode : "3"}]}}));
        return d;
      });

      l.getBeeHive = function(){return {getObject : function(){ return {getSelectedPapers : function(){return ["1", "2", "3"]}}}}};

      //get bibcodes from current  query
      var deferred1 =  l._getBibcodes({bibcodes : "all"});

      //get bibcodes from app storage
      var deferred2 = l._getBibcodes({bibcodes : "selected"});

      var bibs;

      deferred2.done(function(b){bibs = b});

      expect(bibs).to.eql( ["1", "2", "3"] );

    });
  });

})