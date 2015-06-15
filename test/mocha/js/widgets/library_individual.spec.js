define([
  "js/widgets/library_individual/widget",
  "js/bugutils/minimal_pubsub"

], function(

  LibraryWidget,
  MinSub

  ){


  describe("Library Widget (library_individual.spec.js)", function(){


    var stubData1  = {
      "documents": ["2015ASPC..492..150T", "2015ASPC..492..208G", "2015ASPC..492..204F"],
      "solr": {"responseHeader": {"status": 0, "QTime": 2, "params": {"q": "*:*", "fq": "{!bitset}", "wt": "json", "fl": "title,abstract,bibcode,author,aff,links_data,property,[citations],pub,pubdate", "rows": "1000"}}, "response": {"start": 0, "numFound": 3, "docs": [{"bibcode": "2015ASPC..492..150T", "pubdate": "2015-04-00", "author": ["Thompson, D. M.", "Henneken, E. A.", "Grant, C. S.", "Holachek, A.", "Accomazzi, A.", "Kurtz, M. J.", "Chyla, R.", "Luker, J.", "Murray, S. S."], "abstract": "A large portion of the astronomical research of the 19th and early 20th centuries was reported in publications written and distributed by individual observatories. Many of these collections were not widely distributed and complete sets of these volumes are now difficult to locate. The ADS has taken on the effort to put these publications online and make them searchable. In this paper I will outline the project and discuss some of the highlights and challenges the ADS has encountered over the duration of the project.", "title": ["Saving the Orphaned Astronomical Literature"], "links_data": ["{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"open\"}"], "pub": "Astronomical Society of the Pacific Conference Series", "[citations]": {"num_citations": 0, "num_references": 4}, "property": ["OPENACCESS", "PUB_OPENACCESS", "ARTICLE", "NOT REFEREED"], "aff": ["-", "-", "-", "-", "-", "-", "-", "-", "-"]}, {"bibcode": "2015ASPC..492..204F", "pubdate": "2015-04-00", "author": ["Frey, K.", "Erdmann, C.", "Accomazzi, A.", "Rubin, L.", "Biemesderfer, C.", "Gray, N.", "Soles, J."], "abstract": "The Unified Astronomy Thesaurus (UAT) is an open, interoperable, and community-supported thesaurus of astronomical and astrophysical concepts and their relationships. Management of the UAT is based on a community-supported approach. This will ensure that the thesaurus stays current by allowing users to suggest an addition or revision. These suggestions will then be subjected to a thorough expert review process before being accepted for addition to the UAT or rejected. Many leading astronomical institutions, professional associations, journal publishers, learned societies, and data repositories support the UAT as a standard astronomical terminology.", "title": ["Management of the Unified Astronomy Thesaurus"], "links_data": ["{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"open\"}"], "pub": "Astronomical Society of the Pacific Conference Series", "[citations]": {"num_citations": 0, "num_references": 1}, "property": ["OPENACCESS", "PUB_OPENACCESS", "ARTICLE", "NOT REFEREED"], "aff": ["Harvard-Smithsonian Center for Astrophysics, USA", "Harvard-Smithsonian Center for Astrophysics, USA", "SAO/NASA Astrophysics Data System, USA", "Harvard-Smithsonian Center for Astrophysics, USA", "American Astronomical Society, USA", "University of Glasgow, UK", "McGill University, Canada"]}, {"bibcode": "2015ASPC..492..208G", "pubdate": "2015-04-00", "author": ["Grant, C. S.", "Thompson, D. M.", "Chyla, R.", "Holachek, A.", "Accomazzi, A.", "Henneken, E. A.", "Kurtz, M. J.", "Luker, J.", "Murray, S. S."], "abstract": "For many years, users have wanted to search affiliations in the ADS in order to build institutional databases and to help with author disambiguation. Although we currently provide this capability upon request, we have yet to incorporate it as part of the operational Abstract Service. This is because it cannot be used reliably, primarily because of the lack of uniform representation of the affiliation data. In an effort to make affiliation searches more meaningful, we have designed a two-tiered hierarchy of standard institutional names based on Ringgold identifiers, with the expectation that this will enable us to implement a search by institution, which will work for the vast majority of institutions. It is our intention to provide the capability of searching the ADS both by standard affiliation name and original affiliation string, as well as to enable autosuggest of affiliations as a means of helping to disambiguate author identification. Some institutions are likely to require manual work, and we encourage interested librarians to assist us in standardizing the representation of their institutions in the affiliation field.", "title": ["Enabling Meaningful Affiliation Searches in the ADS"], "links_data": ["{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"open\"}"], "pub": "Astronomical Society of the Pacific Conference Series", "[citations]": {"num_citations": 2, "num_references": 0}, "property": ["OPENACCESS", "PUB_OPENACCESS", "ARTICLE", "NOT REFEREED"], "aff": ["Harvard-Smithsonian Center for Astrophysics, Cambridge, USA", "Harvard-Smithsonian Center for Astrophysics, Cambridge, USA", "Harvard-Smithsonian Center for Astrophysics, Cambridge, USA", "Harvard-Smithsonian Center for Astrophysics, Cambridge, USA", "Harvard-Smithsonian Center for Astrophysics, Cambridge, USA", "Harvard-Smithsonian Center for Astrophysics, Cambridge, USA", "Harvard-Smithsonian Center for Astrophysics, Cambridge, USA", "Harvard-Smithsonian Center for Astrophysics, Cambridge, USA", "Harvard-Smithsonian Center for Astrophysics, Cambridge, USA"]}]}},
      "metadata" :{name: "Aliens Among Us", id: "1", description: "Are you one of them?", permission : "owner", num_documents : 300, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'},
    }

    var stubData2  = {
      "documents": ["2015ASPC..492..150T", "2015ASPC..492..208G", "2015ASPC..492..204F"],
      "solr": {"responseHeader": {"status": 0, "QTime": 2, "params": {"q": "*:*", "fq": "{!bitset}", "wt": "json", "fl": "title,abstract,bibcode,author,aff,links_data,property,[citations],pub,pubdate", "rows": "1000"}}, "response": {"start": 0, "numFound": 3, "docs": []}},
      "metadata" :{name: "Aliens Among Us", id: "1", description: "Are you one of them?", permission : "read", num_documents : 0, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'}
    }


    var fakeLibraryController =   {
      getHardenedInstance : function(){return this},
      getLibraryData : function(id ){ var d =  $.Deferred(); if(id==1){d.resolve(stubData1)} else {d.resolve(stubData2)}; return d.promise() },
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

      w.model.set({view : "library", id : "1"});

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
      w.model.set({view : "library", id : "3"});

      expect($("#test .s-library-title h2").attr("contenteditable")).to.be.undefined;

      expect($("#test .tab[data-tab=admin]").length).to.eql(0);

    });

    it("should display a slightly different header view if the model's attribute publicView is set to true", function(){

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

      w.model.set({view : "library", id : "1", "publicView" : true});


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

      w.pubsub.publish = sinon.spy();

      $("#test").append(w.render().el);

      w.model.set({view:"library", id : "1"});

      //navigating to permissions

      expect($("#test .main .library-detail-view").length).to.eql(1);
      expect($("#test .main .library-admin-view").length).to.eql(0);

      $("#test .tab[data-tab=admin]").click();

      expect(spy.args[0]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "IndividualLibraryWidget",
        {
          "sub": "admin",
          "id": "1",
          "publicView": false
        }
      ]);

      $("#test li[data-tab=export-bibtex]").click();

      expect(spy.args[1]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "library-export",
        {
          "bibcodes": [
            "2015ASPC..492..150T",
            "2015ASPC..492..204F",
            "2015ASPC..492..208G"
          ],
          "sub": "bibtex",
          "id": "1",
          "publicView" : false

        }
      ]);


      $("#test .tab[data-tab=metrics]").click();

      expect(spy.args[2]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "library-metrics",
        {
          "bibcodes": [
            "2015ASPC..492..150T",
            "2015ASPC..492..204F",
            "2015ASPC..492..208G"
          ],
          "id": "1",
          "publicView": false,
          "sub" : undefined
        }
      ]);


      $("#test li[data-tab=visualization-authornetwork]").click();

      expect(spy.args[3]).to.eql([
        "[Router]-Navigate-With-Trigger",
        "library-visualization",
        {
          "bibcodes": [
            "2015ASPC..492..150T",
            "2015ASPC..492..204F",
            "2015ASPC..492..208G"
          ],
          "sub": "authornetwork",
          "id": "1",
          "publicView" : false

        }
      ]);


      //none of these options are available if the library has 0 bibcodes
      w.model.set({view : "library", id : "2"});

      //export, metrics, vis disabled

      expect($("#test li[data-tab=export-bibtex]").length).to.eql(0);
      expect($("#test li[data-tab=metrics]").length).to.eql(0);
      expect($("#test li[data-tab=visualization-authornetwork]").length).to.eql(0);


    });

    it("should show a library list that allows you to delete records from a library if you have owner/admin/write permissions", function(){

      sinon.spy(LibraryWidget.prototype, "updateView");

      var w = new LibraryWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);


      w.model.set({view : "library", id : "1"});

      //updateView will be called 1x
      expect(LibraryWidget.prototype.updateView.callCount).to.eql(1);

      //has ability to delete records

      expect($("#test .library-item:first").find("button.remove-record").length).to.eql(1);

      $("#test .library-item:first button.remove-record").click();

      expect(fakeLibraryController.updateLibraryContents.callCount).to.eql(1);

      //doesn't have ability to delete records

      w.model.set({view : "library", id : "3"});

      expect($("#test .library-item:first").find("button.remove-record").length).to.eql(0);

      LibraryWidget.prototype.updateView.restore();


    });


    it("should have an admin view that allows you to change the public/private status of your library", function(){


      var w = new LibraryWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);

      w.model.set({view :"admin", id : "1"});

      $("#test .public-button").click();

      expect(fakeLibraryController.updateLibraryMetadata.args[1]).to.eql([
        "1",
        {
          "public": true
        }
      ]);



    });


    it("should have and admin view that allows you to alter the permissions of members of a library")






  })



})