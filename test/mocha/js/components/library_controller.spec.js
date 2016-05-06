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
        "getPublicLibraryMetadata",
        "getLibraryMetadata",
        "createLibrary",
        "createLibAndAddBibcodes",
        "addBibcodesToLib",
        "deleteLibrary",
        "updateLibraryContents",
        "updateLibraryMetadata",
        "importLibraries",
        "getLibraryBibcodes",
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

    it("should allow widgets to get a list of bibcodes from a library", function(){

      var l = new LibraryController();

      var minsub = new (MinSub.extend({
        request: function () {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeApi = {
        getHardenedInstance : function(){return this},
        request : sinon.spy(function(apiRequest) {

          var libJSON = {
            "solr": {
              "responseHeader": {
                "status": 0,
                "QTime": 2,
                "params": {
                  "sort": "date desc",
                  "fq": "{!bitset}",
                  "rows": "100",
                  "q": "*:*",
                  "start": "0",
                  "wt": "json",
                  "fl": "bibcode"
                }
              },
              "response": {
                "start": 0,
                "numFound": 179,
                "docs": [
                  {
                    "bibcode": "2015ASPC..495..401C"
                  },
                  {
                    "bibcode": "2015IAUGA..2257982A"
                  },
                  {
                    "bibcode": "2015IAUGA..2257768A"
                  },
                  {
                    "bibcode": "2015IAUGA..2257639R"
                  },
                  {
                    "bibcode": "2015ASPC..492..204F"
                  },
                  {
                    "bibcode": "2015ASPC..492...85E"
                  },
                  {
                    "bibcode": "2015ASPC..492..150T"
                  },
                  {
                    "bibcode": "2015ASPC..492..189A"
                  },
                  {
                    "bibcode": "2015ASPC..492..208G"
                  },
                  {
                    "bibcode": "2015ASPC..492...80H"
                  },
                  {
                    "bibcode": "2015AAS...22533656H"
                  },
                  {
                    "bibcode": "2015AAS...22533655A"
                  },
                  {
                    "bibcode": "2014ASPC..485..461A"
                  },
                  {
                    "bibcode": "2014AAS...22325503A"
                  },
                  {
                    "bibcode": "2014AAS...22325525A"
                  },
                  {
                    "bibcode": "2013ASPC..475....7M"
                  },
                  {
                    "bibcode": "2013A&C.....1....1A"
                  },
                  {
                    "bibcode": "2013AAS...22124028G"
                  },
                  {
                    "bibcode": "2013AAS...22124030A"
                  },
                  {
                    "bibcode": "2012AGUFMED21A0703H"
                  },
                  {
                    "bibcode": "2012ASPC..461..867A"
                  },
                  {
                    "bibcode": "2012SPIE.8448E..0KA"
                  },
                  {
                    "bibcode": "2012ASPC..461..763H"
                  },
                  {
                    "bibcode": "2012opsa.book..253H"
                  },
                  {
                    "bibcode": "2012LPI....43.1022H"
                  },
                  {
                    "bibcode": "2011ASPC..442.....E"
                  },
                  {
                    "bibcode": "2011AAS...21813102E"
                  },
                  {
                    "bibcode": "2011AAS...21813105A"
                  },
                  {
                    "bibcode": "2011ASSP...24.....A"
                  },
                  {
                    "bibcode": "2011AAS...21711608H"
                  },
                  {
                    "bibcode": "2011AAS...21714501C"
                  },
                  {
                    "bibcode": "2011ASSP...24..125H"
                  },
                  {
                    "bibcode": "2011ASSP...24..135A"
                  },
                  {
                    "bibcode": "2010ASPC..434..155K"
                  },
                  {
                    "bibcode": "2010ASPC..433..273A"
                  },
                  {
                    "bibcode": "2010ASSP...20..141H"
                  },
                  {
                    "bibcode": "2009AGUFM.P43B1437H"
                  },
                  {
                    "bibcode": "2009ASPC..411..384H"
                  },
                  {
                    "bibcode": "2009ASPC..410..160O"
                  },
                  {
                    "bibcode": "2009LPI....40.1873H"
                  },
                  {
                    "bibcode": "2009AAS...21347301A"
                  },
                  {
                    "bibcode": "2009astro2010P..64W"
                  },
                  {
                    "bibcode": "2009astro2010P..44O"
                  },
                  {
                    "bibcode": "2009astro2010P..34L"
                  },
                  {
                    "bibcode": "2009AAS...21347302H"
                  },
                  {
                    "bibcode": "2009arad.workE..32A"
                  },
                  {
                    "bibcode": "2009JInfo...3....1H"
                  },
                  {
                    "bibcode": "2009astro2010P...6B"
                  },
                  {
                    "bibcode": "2009astro2010P..28K"
                  },
                  {
                    "bibcode": "2007BASI...35..717E"
                  },
                  {
                    "bibcode": "2007AAS...211.4731G"
                  },
                  {
                    "bibcode": "2007AAS...211.4732A"
                  },
                  {
                    "bibcode": "2007AAS...211.4730K"
                  },
                  {
                    "bibcode": "2007ASPC..377...97G"
                  },
                  {
                    "bibcode": "2007DPS....39.2708H"
                  },
                  {
                    "bibcode": "2007ASPC..377...23K"
                  },
                  {
                    "bibcode": "2007ASPC..377...93E"
                  },
                  {
                    "bibcode": "2007ASPC..376..467A"
                  },
                  {
                    "bibcode": "2007ASPC..377...69A"
                  },
                  {
                    "bibcode": "2007ASPC..377..102T"
                  },
                  {
                    "bibcode": "2007ASPC..377...36E"
                  },
                  {
                    "bibcode": "2007ASPC..377..106H"
                  },
                  {
                    "bibcode": "2007HiA....14..605E"
                  },
                  {
                    "bibcode": "2007APS..MARU20009K"
                  },
                  {
                    "bibcode": "2007LPI....38.1240E"
                  },
                  {
                    "bibcode": "2007LePub..20...16H"
                  },
                  {
                    "bibcode": "2006AAS...20921809K"
                  },
                  {
                    "bibcode": "2006AAS...20917302H"
                  },
                  {
                    "bibcode": "2006AAS...20921808E"
                  },
                  {
                    "bibcode": "2006DPS....38.4605E"
                  },
                  {
                    "bibcode": "2006JEPub...9....2H"
                  },
                  {
                    "bibcode": "2006ASPC..351..715A"
                  },
                  {
                    "bibcode": "2006ASPC..351..653K"
                  },
                  {
                    "bibcode": "2006LPI....37.1691E"
                  },
                  {
                    "bibcode": "2005AAS...207.3405K"
                  },
                  {
                    "bibcode": "2005AAS...207.3404E"
                  },
                  {
                    "bibcode": "2005AGUFMIN33A1162E"
                  },
                  {
                    "bibcode": "2005DPS....37.1302E"
                  },
                  {
                    "bibcode": "2005LPI....36.1207E"
                  },
                  {
                    "bibcode": "2005Msngr.119...50D"
                  },
                  {
                    "bibcode": "2005coas.conf...80E"
                  },
                  {
                    "bibcode": "2005JASIS..56..111K"
                  },
                  {
                    "bibcode": "2005JASIS..56...36K"
                  },
                  {
                    "bibcode": "2004AGUFMED13D0746E"
                  },
                  {
                    "bibcode": "2004DPS....36.1314E"
                  },
                  {
                    "bibcode": "2004ASPC..314..181A"
                  },
                  {
                    "bibcode": "2004LPI....35.1602T"
                  },
                  {
                    "bibcode": "2004LPI....35.1267E"
                  },
                  {
                    "bibcode": "2004tivo.conf..294M"
                  },
                  {
                    "bibcode": "2004tivo.conf..267E"
                  },
                  {
                    "bibcode": "2004ccdm.conf..521D"
                  },
                  {
                    "bibcode": "2003AGUFMED32C1207E"
                  },
                  {
                    "bibcode": "2003AAS...203.2006H"
                  },
                  {
                    "bibcode": "2003AAS...203.2004E"
                  },
                  {
                    "bibcode": "2003AAS...203.2005K"
                  },
                  {
                    "bibcode": "2003AfrSk...8....7E"
                  },
                  {
                    "bibcode": "2003DPS....35.3302E"
                  },
                  {
                    "bibcode": "2003EAEJA....12295E"
                  },
                  {
                    "bibcode": "2003LPI....34.1949E"
                  },
                  {
                    "bibcode": "2003APS..MARX31001E"
                  }
                ]
              }
            }
          };

          arguments[0].toJSON().options.done(libJSON);

        })

      };

      minsub.beehive.removeService("Api");
      minsub.beehive.addService("Api", fakeApi);

      l.activate(minsub.beehive);

      //it should paginate through and collect all bibcodes up to 3000

      var data;

      l.getLibraryBibcodes("2").done(function(d){
        data= d;
      });

      expect(data.length).to.eql(200);

      expect(fakeApi.request.callCount).to.eql(2);

      var req = fakeApi.request.args[0][0];

      expect(req.get('target')).to.eql("biblib/libraries/2");
      expect(req.get("query").toJSON()).to.eql({
        "rows": [
          100
        ],
        "fl": [
          "bibcode"
        ],
        "start": [
          0
        ]
      });


      expect(fakeApi.request.args[1][0].get("query").toJSON()).to.eql({
        "rows": [
          100
        ],
        "fl": [
          "bibcode"
        ],
        "start": [
          100
        ]
      });

      //it should cache the bibcodes

      expect(l._libraryBibcodeCache["2"]).to.be.instanceof(Array);
      expect(l._libraryBibcodeCache["2"].length).to.eql(200);

      //it should empty the cache for that bibcode if it changes

      l.collection.trigger("change", new Backbone.Model({id : "2"}));
      expect(l._libraryBibcodeCache["2"]).to.be.undefined;

    });


    it("should offer widgets a method to get library metadata", function(done){

      var l = new LibraryController();

      l.composeRequest = sinon.spy(function(){
        var d = $.Deferred();
        d.resolve(stubMetadata);
        return d.promise();
      });

      //should fetch data if _metadataLoaded === false

      expect(l._metadataLoaded).to.be.undefined;
      expect(l.collection.toJSON()).to.eql([]);
      expect(l.composeRequest.callCount).to.eql(0);

      l.getLibraryMetadata();

      expect(l.composeRequest.callCount).to.eql(1);

      expect(l._metadataLoaded).to.be.true;
      expect(l.collection.length).to.eql(4);

      //should just return collection (or model json if lib id is provided) if _metadataLoaded

      l.getLibraryMetadata("3").done(function(data){
        expect(data.id).to.eql("3")
        done();
      });

      expect(l.composeRequest.callCount).to.eql(1);

    });

    it("should offer widgets a method to get library bibcodes, and cache the bibcodes for future use", function(){



    });

    it("should have an internal method, composeRequest, that actually composes API request and returns and resolves a promise (so public functions can return the promise)", function(){

      var l = new LibraryController();

      var minsub = new (MinSub.extend({
        request: function() {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var fakeApi = {
        getHardenedInstance : function(){return this}, request : function(){
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


    it("should have an importLibraries function to import from classic or 2.0", function(){

      var l = new LibraryController();

      var minsub = new (MinSub.extend({
        request: function() {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var requestSpy = sinon.spy(
          function(){
            arguments[0].toJSON().options.done();
          }
      )

      var fakeApi = {
        getHardenedInstance : function(){return this},
        request : requestSpy
      };

      minsub.beehive.removeService("Api");
      minsub.beehive.addService("Api", fakeApi);

      var fetchSpy = sinon.stub(l, "_fetchAllMetadata");

      l.activate(minsub.beehive.getHardenedInstance());

      var test;

      l.importLibraries("classic").done(function(){
        test = "foo"
      });

      expect(requestSpy.args[0][0].get("target")).to.eql("biblib/classic");

      expect(fetchSpy.callCount).to.eql(1);

      expect(test).to.eql("foo");



    })
  });

})