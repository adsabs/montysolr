define([
  "js/components/library_controller",
  "js/bugutils/minimal_pubsub",
  "js/components/json_response"

], function(
  LibraryController,
  MinSub,
  JSONResponse

  ){


  describe("Library Controller (library_controller.spec.js)", function(){


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
    };

<<<<<<< HEAD


    it("should offer a hardened interface to widgets with the relevant library CRUD operations", function(){
=======
   expect(_.keys(l.getHardenedInstance())).to.eql(
     [
       "getAllMetadata",
       "getLibraryData",
       "createLibrary",
       "createLibAndAddBibcodes",
       "addBibcodesToLib",
       "deleteLibrary",
       "updateLibraryContents",
       "updateLibraryPermissions",
       "updateLibraryMetadata",
       "__facade__",
       "mixIn"
     ]
   )
>>>>>>>  basic library functionality, minus pagination for libraries, mostly working

      var l = new LibraryController();
      expect(_.keys(l.getHardenedInstance())).to.eql(
        ["getLibraryMetadata",
          "getLibraryRecords",
          "createLibrary",
          "createLibAndAddBibcodes",
          "addBibcodesToLib",
          "deleteLibrary",
          "updateLibraryContents",
          "updateLibraryPermissions",
          "updateLibraryMetadata",
          "__facade__",
          "mixIn"]
      )
    });

    it("should allow widgets to get information about libraries from /libraries and /libraries/[id]", function(){

      var l = new LibraryController();
      var minsub = new (MinSub.extend({
        request: function() {
          return {some: 'foo'}
        }
      }))({verbose: false});

      l.activate(minsub.beehive.getHardenedInstance());

      //function should request data from the api if it does not exist in the collection

      var a;

      l.composeRequest = sinon.spy(function(){
        var d = $.Deferred();
        d.resolve(stubMetadata);
        return d.promise();
      })

      var promise1 = l.getAllMetadata().done(
        function(coll){
        a = coll;
        });


      var req = l.composeRequest.args[0];

      expect(req[0]).to.eql("biblib/libraries");

      expect(req[1]).to.eql("GET");

      expect(a.libraries).to.eql(
        [
          {
            "name": "Aliens Among Us",
            "id": "1",
            "description": "Are you one of them?",
            "permission": "owner",
            "num_documents": 300,
            "date_created": "2015-04-03 04:30:04",
            "date_last_modified": "2015-04-09 06:30:04"
          },
          {
            "name": "Everything Sun",
            "id": "2",
            "description": "Where would we be without the sun?",
            "num_documents": 0,
            "permission": "admin",
            "date_created": "2014-01-03 04:30:04",
            "date_last_modified": "2015-01-09 06:30:04"
          },
          {
            "name": "Space Travel and You",
            "id": "7",
            "description": "",
            "permission": "write",
            "num_documents": 4000,
            "date_created": "2013-06-03 04:30:04",
            "date_last_modified": "2015-06-09 06:30:04"
          },
          {
            "name": "Space Travel and Me",
            "id": "3",
            "description": "interesting",
            "permission": "read",
            "num_documents": 400,
            "date_created": "2012-06-03 05:30:04",
            "date_last_modified": "2015-07-09 06:30:04"
          }
        ]);



      l.getLibraryData("2");

      var req = l.composeRequest.args[1];

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

      l.composeRequest = sinon.spy(function(){ var d = $.Deferred();d.resolve();return d.promise()});
      l.fetchAllLibraryData = sinon.spy();

      l.getPubSub().publish = sinon.spy();

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

      l.deleteLibrary("4");


      expect(l.composeRequest.args[1]).to.eql(["biblib/documents/4", "DELETE"]);

      //should result in 1 call to composeRequest and 2 calls to pubsub on successful completion

<<<<<<< HEAD
      expect(l.getPubSub().publish.args[0]).to.eql(["[Router]-Navigate-With-Trigger", "AllLibrariesWidget"]);
=======
      expect(l.pubsub.publish.args[0].slice(1)).to.eql(["[Router]-Navigate-With-Trigger", "AllLibrariesWidget", "libraries"]);
>>>>>>>  basic library functionality, minus pagination for libraries, mostly working

      expect(JSON.stringify(l.getPubSub().publish.args[1])).to.eql(JSON.stringify([
        "[Alert]-Message",
        {
          "code": 0,
          "msg": "Library <b>undefined</b> was successfully deleted"
        }
      ]));

    });

    it("should allow widgets to add/delete records from libraries", function(){

      var l = new LibraryController()
      l.composeRequest = sinon.spy();

      l.updateLibraryContents("3", {bibcode:[1,2,3], action: "add" });

      expect(l.composeRequest.args[0]).to.eql([
        "biblib/documents/3",
        "POST",
        {
          "data": {
            "bibcode": [
              1,
              2,
              3
            ],
            "action": "add"
          },
          "extraArguments": {
            "numBibcodesRequested": 3
          }
        }
      ]);

    });

    it("should allow widgets to update metadata associated with libraries", function(){

      var l = new LibraryController();
      l.composeRequest = sinon.spy();

      l.updateLibraryMetadata("3", { name : "foo", description : "boo"});

      expect(l.composeRequest.args[0]).to.eql([
        "biblib/documents/3",
        "PUT",
        {
          "data": {
            "name": "foo",
            "description": "boo"
          }
        }
      ]);

    });

    it("should allow widgets to update permission information associated with libraries", function(){

      var l = new LibraryController();
      l.composeRequest = sinon.spy();

      l.updateLibraryPermissions("3", {email : "aholachek@foobly.ru", permission : "admin", value : true});

      expect(l.composeRequest.args[0]).to.eql([
        "biblib/permissions/3",
        "POST",
        {
          "data": {
            "email": "aholachek@foobly.ru",
            "permission": "admin",
            "value": true
          }
        }
      ])

    });

    it("should offer convenience methods for interfacing with current query/ app storage, getting relevant bibcodes, and adding those bibcodes to libraries", function(){

      var l = new LibraryController();
      var minsub = new (MinSub.extend({
        request: function() {
          return {some: 'foo'}
        }
      }))({verbose: false});

      l._currentQuery = new MinSub.prototype.T.QUERY();
      l._executeApiRequest = sinon.spy(function(){
        var d = $.Deferred();
        d.resolve( new JSONResponse({response : {docs : [{bibcode : "1", bibcode : "2", bibcode : "3"}]}}));
        return d;
      });

      minsub.beehive.addObject('AppStorage', {getSelectedPapers : function(){return ["1", "2", "3"]}});
      l.activate(minsub.beehive);

      //get bibcodes from current  query
      var deferred1 =  l._getBibcodes({bibcodes : "all"});


      //get bibcodes from app storage
      var deferred2 = l._getBibcodes({bibcodes : "selected"});
      var bibs;

      deferred2.done(function(b){bibs = b});
      expect(bibs).to.eql( ["1", "2", "3"] );
    })
  })
});
