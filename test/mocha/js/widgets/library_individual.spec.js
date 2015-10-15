define([
  "js/widgets/library_individual/widget",
  "js/bugutils/minimal_pubsub"

], function(

  LibraryWidget,
  MinSub

  ){


  describe("Library Widget (library_individual.spec.js)", function(){

    var stubLibraryMetadata =   [
      {name: "Aliens Among Us", id: "1", description: "Are you one of them?", permission : "owner", num_documents : 300, date_created: '2015-04-03 04:30:04', date_last_modified: '2015-04-09 06:30:04'},
      {name: "Everything Sun", id: "2", description: "Where would we be without the sun?", num_documents : 0, permission : "admin", date_created: '2014-01-03 04:30:04', date_last_modified: '2015-01-09 06:30:04'},
      {name: "Space Travel and You", id: "7", description: "", permission : "write", num_documents : 4000, date_created: '2013-06-03 04:30:04', date_last_modified: '2015-06-09 06:30:04'},
      {name: "Space Travel and Me", id: "3", description: "interesting", permission : "read", num_documents : 400, date_created: '2012-06-03 05:30:04', date_last_modified: '2015-07-09 06:30:04'}

    ];


    var stubData1  = {
      "documents": [
        "2015IAUGA..2257982A",
        "2015IAUGA..2257768A",
        "2015IAUGA..2257639R"
      ],
      "metadata": {
        "date_created": "2015-08-06T17:13:10.830175",
        "date_last_modified": "2015-08-06T19:12:42.261850",
        "description": "My ADS library",
        "id": "ieW0QRG-QSeNNXLjgGNjhg",
        "name": "test test tess",
        "num_documents": 3,
        "num_users": 1,
        "owner": "aholachek",
        "permission": "owner",
        "public": true
      },
      "solr": {
        "response": {
          "docs": [
            {
              "[citations]": {
                "num_citations": 1,
                "num_references": 0
              },
              "abstract": "Whether or not scholarly publications are going through an evolution or revolution, one comforting certainty remains: the NASA Astrophysics Data System (ADS) is here to help the working astronomer and librarian navigate through the increasingly complex communication environment we find ourselves in. Born as a bibliographic database, today's ADS is best described as a an \"aggregator\" of scholarly resources relevant to the needs of researchers in astronomy and physics. In addition to indexing content from a variety of publishers, data and software archives, the ADS enriches its records by text-mining and indexing the full-text articles, enriching its metadata through the extraction of citations and acknowledgments and the ingest of bibliographies and data links maintained by astronomy institutions and data archives. In addition, ADS generates and maintains citation and co-readership networks to support discovery and bibliometric analysis.In this talk I will summarize new and ongoing curation activities and technology developments of the ADS in the face of the ever-changing world of scholarly publishing and the trends in information-sharing behavior of astronomers. Recent curation efforts include the indexing of non-standard scholarly content (such as software packages, IVOA documents and standards, and NASA award proposals); the indexing of additional content (full-text of articles, acknowledgments, affiliations, ORCID ids); and enhanced support for bibliographic groups and data links. Recent technology developments include a new Application Programming Interface which provides access to a variety of ADS microservices, a new user interface featuring a variety of visualizations and bibliometric analysis, and integration with ORCID services to support paper claiming.",
              "aff": [
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory"
              ],
              "author": [
                "Accomazzi, Alberto",
                "Kurtz, Michael J.",
                "Henneken, Edwin",
                "Grant, Carolyn S.",
                "Thompson, Donna",
                "Chyla, Roman",
                "Holachek, Alexandra",
                "Sudilovsky, Vladimir",
                "Elliott, Jonathan",
                "Murray, Stephen S."
              ],
              "bibcode": "2015IAUGA..2257768A",
              "property": [
                "ARTICLE",
                "NOT REFEREED"
              ],
              "pub": "IAU General Assembly",
              "pubdate": "2015-01-00",
              "title": [
                "The NASA Astrophysics Data System joins the Revolution"
              ]
            },
            {
              "[citations]": {
                "num_citations": 5,
                "num_references": 0
              },
              "abstract": "This presentation discusses the current curation of archive bibliographies and their indexing in the NASA Astrophysics Data System (ADS). Integration of these bibliographies provides convenient cross-linking of resources between ADS and the data archives, affording greater visibility to both data products and the literature associated with them. There are practical incentives behind this effort: it has been shown that astronomy articles which provide links to on-line datasets have a citation advantage over similar articles which don\u2019t link to data. Additionally, the availability of paper-based metrics makes it possible for archivists and program managers use them in order to assess the impact of an instrument, facility, funding or observing program.The primary data curated by ADS is bibliographic information provided by publishers or harvested by ADS from conference proceeding sites and repositories. This core bibliographic information is then further enriched by ADS via the generation of citations and usage data, and through the aggregation of external bibliographic information. Important sources of such additional information are the metadata describing observing proposals from the major missions and archives, the curated bibliographies for data centers, and the sets of links between archival observations and published papers.While ADS solicits and welcomes the inclusion of this data from US and foreign data centers, the curation of bibliographies, observing proposals and links to data products is left to the archives which host the data and which have the expertise and resources to properly maintain them. In this regard, the role of ADS is one of resource aggregation through crowdsourced curation, providing a lightweight discovery mechanism through its search capabilities. While limited in scope, this level of aggregation can still be quite useful in supporting the discovery and selection of data products associated with publications. For instance, a user can use ADS to find papers which have been classified in the bibliography for HST, Chandra, and Spitzer, which typically yields multi-spectral studies making use of data from NASA\u2019s Great Observatories. I will discuss how curators and institutions can participate in this effort.",
              "aff": [
                "Smithsonian Astrophysical Observatory"
              ],
              "author": [
                "Accomazzi, Alberto"
              ],
              "bibcode": "2015IAUGA..2257982A",
              "property": [
                "ARTICLE",
                "NOT REFEREED"
              ],
              "pub": "IAU General Assembly",
              "pubdate": "2011-08-00",
              "title": [
                "Curation and integration of observational metadata in ADS"
              ]
            },
            {
              "[citations]": {
                "num_citations": 3,
                "num_references": 0
              },
              "abstract": "Observatory bibliographies have traditionally served to provide a record of accomplishments for the managers' reports. However, by linking the published articles with the individual datasets that they used and present, we have the opportunity to join the bibliographic metadata (including keywords, subjects, objects, data references from other observatories, etc.) with the metadata associated with the observational datasets. This creates a very rich information field that is ripe for far more sophisticated datamining than the two repositories (publications and data) would afford individually.We have been maintaining such an extensive bibliography for the Chandra Data Archive that is complete for the entire mission.The linking is implemented through the use of persistent dataset identifiers (PID). We have used the PID set defined by the NASA Astrophysics Data Centers since their introduction a decade ago, as there were no obvious viable alternatives at the time, but these could readily be translated into more modern PIDs, such as DOI. We welcome the submission of higher level data products (especially those associated with publications) by our users. The bibliography allows seamless data discovery in the literature and the data repository, with the benefit of this wealth of metadata.With PIDs defined for datasets and articles, the next step is obviously to link data across repositories covering multiple missions and different parts of the spectrum.The next enticing possibility is to extend the functionality of the PIDs, allowing them to drill down into complex datasets (and that includes articles) to extract specific bits and pieces, by adding a level of more sophisticated intelligence in data discovery through PIDs.This work has been supported by NASA under contract NAS 8-03060 to the Smithsonian Astrophysical Observatory for operation of the Chandra X-ray Center. It depends critically on the services provided by the ADS, which is funded by NASA Grant NNX12AG54G.",
              "aff": [
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory",
                "Smithsonian Astrophysical Observatory"
              ],
              "author": [
                "Rots, Arnold H.",
                "Winkelman, Sherry",
                "Accomazzi, Alberto"
              ],
              "bibcode": "2015IAUGA..2257639R",
              "property": [
                "ARTICLE",
                "NOT REFEREED"
              ],
              "pub": "IAU General Assembly",
              "pubdate": "2015-09-00",
              "title": [
                "Bibliographies and Data Archives: a Rich Data Mining Tool"
              ]
            }
          ],
          "numFound": 3,
          "start": 0
        },
        "responseHeader": {
          "QTime": 7,
          "params": {
            "fl": "title,abstract,bibcode,author,aff,links_data,property,[citations],pub,pubdate",
            "fq": "{!bitset}",
            "q": "*:*",
            "rows": "1000",
            "wt": "json"
          },
          "status": 0
        }
      },
      "updates": {
        "duplicates_removed": 0,
        "num_updated": 0,
        "update_list": []
      }
    };


    var stubData2  = {
      "documents": [
        "2015APS..MAR.L8003E",
        "2015arXiv150508012D",
        "2015APS..MAR.L8004M"
      ],
      "metadata": {
        "date_created": "2015-08-07T12:43:37.110309",
        "date_last_modified": "2015-08-07T12:43:37.110316",
        "description": "My ADS library",
        "id": "mDYchVSfQ-aOnFyzFsn9YQ",
        "name": "fake",
        "num_documents": 3,
        "num_users": 1,
        "owner": "aholachek",
        "permission": "owner",
        "public": false
      },
      "solr": {
        "response": {
          "docs": [
            {
              "[citations]": {
                "num_citations": 0,
                "num_references": 0
              },
              "abstract": "We describe the analysis, based on rate equations, of the hot precursor model mentioned in the previous talk. Two key parameters are the competing times of ballistic monomers decaying into thermalized monomers vs. being captured by an island, which naturally define a ``thermalization'' scale for the system. We interpret the energies and dimmensionless parameters used in the model, and provide both an implicit analytic solution and a convenient asymptotic approximation. Further analysis reveals novel scaling regimes and nonmonotonic crossovers between them. To test our model, we applied it to experiments on parahexaphenyl (6P) on sputtered mica. With the resulting parameters, the curves derived from our analytic treatment account very well for the data at the 4 different temperatures. The fit shows that the high-flux regime corresponds not to ALA (attachment-limited aggregation) or HMA (hot monomer aggregation) but rather to an intermediate scaling regime related to DLA (diffusion-limited aggregation). We hope this work stimulates further experimental investigations.Work at UMD supported by NSF CHE 13-05892.",
              "aff": [
                "-",
                "-",
                "-"
              ],
              "author": [
                "Morales-Cifuentes, Josue",
                "Einstein, T. L.",
                "Pimpinelli, Alberto"
              ],
              "bibcode": "2015APS..MAR.L8004M",
              "property": [
                "NONARTICLE",
                "NOT REFEREED"
              ],
              "pub": "APS March Meeting Abstracts",
              "pubdate": "2015-03-00",
              "title": [
                "How Hot Precursor Modify Island Nucleation: A Rate-Equation Model"
              ]
            },
            {
              "[citations]": {
                "num_citations": 0,
                "num_references": 0
              },
              "abstract": "Analyzing capture-zone distributions (CZD) using the generalized Wigner distribution (GWD) has proved a powerful way to access the critical nucleus size i. Of the several systems to which the GWD has been applied, we consider 6P on mica, for which Winkler's group found i ~ 3 . Subsequently they measured the growth exponent \u03b1 (island density ~F<SUP>\u03b1</SUP> , for flux F) of this system and found good scaling but different values at small and large F, which they attributed to DLA and ALA dynamics, but with larger values of i than found from the CZD analysis. We investigate this result in some detail. The third talk of this group describes a new universal relation between \u03b1 and the characteristic exponent \u03b2 of the GWD. The second talk reports the results of a proposed model that takes long-known transient ballistic adsorption into account, for the first time in a quantitative way. We find several intermediate scaling regimes, with distinctive values of \u03b1 and an effective activation energy. One of these, rather than ALA, gives the best fit of the experimental data and a value of i consistent with the CZD analysis.Work at UMD supported by NSF CHE 13-05892.",
              "aff": [
                "-",
                "-",
                "-"
              ],
              "author": [
                "Einstein, T. L.",
                "Morales-Cifuentes, Josue",
                "Pimpinelli, Alberto"
              ],
              "bibcode": "2015APS..MAR.L8003E",
              "property": [
                "NONARTICLE",
                "NOT REFEREED"
              ],
              "pub": "APS March Meeting Abstracts",
              "pubdate": "2015-03-00",
              "title": [
                "Characterizing Submonolayer Growth of 6P on Mica: Capture Zone Distributions vs. Growth Exponents and the Role of Hot Precursors"
              ]
            },
            {
              "[citations]": {
                "num_citations": 0,
                "num_references": 12
              },
              "abstract": "Newly developed high peak power lasers have opened the possibilities of driving coherent light sources operating with laser plasma accelerated beams and wave undulators. We speculate on the combination of these two concepts and show that the merging of the underlying technologies could lead to new and interesting possibilities to achieve truly compact, coherent radiator devices.",
              "aff": [
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                "-",
                "-"
              ],
              "author": [
                "Dattoli, G.",
                "Di Palma, E.",
                "Petrillo, V.",
                "Rau, J. V.",
                "Sabia, E.",
                "Spassovsky, I.",
                "Biedron, S. G.",
                "Einstein, J.",
                "Milton, S. V."
              ],
              "bibcode": "2015arXiv150508012D",
              "links_data": [
                "{\"title\":\"\", \"type\":\"preprint\", \"instances\":\"\", \"access\":\"open\"}"
              ],
              "property": [
                "OPENACCESS",
                "EPRINT_OPENACCESS",
                "ARTICLE",
                "NOT REFEREED"
              ],
              "pub": "ArXiv e-prints",
              "pubdate": "2015-05-00",
              "title": [
                "Pathway to a Compact SASE FEL Device"
              ]
            }
          ],
          "numFound": 3,
          "start": 0
        },
        "responseHeader": {
          "QTime": 8,
          "params": {
            "fl": "title,abstract,bibcode,author,aff,links_data,property,[citations],pub,pubdate",
            "fq": "{!bitset}",
            "q": "*:*",
            "rows": "1000",
            "wt": "json"
          },
          "status": 0
        }
      },
      "updates": {
        "duplicates_removed": 0,
        "num_updated": 0,
        "update_list": []
      }
    };



    var fakeLibraryController =   {
      getHardenedInstance : function(){return this},
      getAllMetadata : function(){return stubLibraryMetadata},
      isDataLoaded : function(){return true},
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

      $("#test").append(w.getEl());

      w.model.set({view : "library", id : "1"});


      expect($("#test .editable-item form").eq(0).hasClass("hidden")).to.be.true;

      $("#test .editable-item").eq(0).find(".toggle-form").click();

      expect($("#test .editable-item form").eq(0).hasClass("hidden")).to.be.false;

      $("#test .editable-item form").eq(0).find("input").val("here is a better, newer title");

      expect(fakeLibraryController.updateLibraryMetadata.args[0]).to.eql(undefined);

      $("#test .editable-item form").eq(0).find("button.btn-success").click();

      expect(fakeLibraryController.updateLibraryMetadata.args[0][0]).to.eql("1");
      expect(fakeLibraryController.updateLibraryMetadata.args[0][1]).to.eql({name: "here is a better, newer title"});

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

      $("#test").append(w.getEl());

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

      var spy = sinon.spy();

      w.getBeeHive().getService("PubSub").publish = spy;

      $("#test").append(w.getEl());

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
            "2015IAUGA..2257639R",
            "2015IAUGA..2257768A",
            "2015IAUGA..2257982A"
          ],
          "sub": "bibtex",
          "id": "1",
          "publicView": false
        }
      ]);


      $("#test .tab[data-tab=metrics]").click();

      expect(spy.args[2]).to.eql([

        "[Router]-Navigate-With-Trigger",
        "library-metrics",
        {
          "bibcodes":  [
            "2015IAUGA..2257639R",
            "2015IAUGA..2257768A",
            "2015IAUGA..2257982A"
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
            "2015IAUGA..2257639R",
            "2015IAUGA..2257768A",
            "2015IAUGA..2257982A"
          ],
          "sub": "authornetwork",
          "id": "1",
          "publicView": false
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

      sinon.spy(LibraryWidget.prototype, "updateWidget");

      var w = new LibraryWidget();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.getEl());


      w.model.set({view : "library", id : "1"});

      //updateView will be called 1x
      expect(LibraryWidget.prototype.updateWidget.callCount).to.eql(1);

      //has ability to delete records

      expect($("#test .library-item:first").find("button.remove-record").length).to.eql(1);

      $("#test .library-item:first button.remove-record").click();

      expect(fakeLibraryController.updateLibraryContents.callCount).to.eql(1);

      //doesn't have ability to delete records

      w.model.set({view : "library", id : "3"});

      expect($("#test .library-item:first").find("button.remove-record").length).to.eql(0);

      LibraryWidget.prototype.updateWidget.restore();


    });

    it("library list should paginate by and allow sorting based on pubdate/read_count/citation_count", function(){

      var w = new LibraryWidget({perPage:3});

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      minsub.beehive.addObject("LibraryController", fakeLibraryController);

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.getEl());


      w.model.set({view : "library", id : "1"});

      //should be default sorted on pubdate

      expect($("#test .library-item div[aria-label='date published']").map(function(){return $(this).text().trim()}).get()).to.eql(["2015/09", "2015/01", "2011/08"]);

      //sort on citation count

      $("#test button[data-sort=num_citations]").click();

      expect($("#test .library-item div[title='citation count']").map(function(){return $(this).text().trim()}).get()).to.eql([ 'cited: 5', 'cited: 3', 'cited: 1' ]);

      w.libraryCollection.reset(stubData1.solr.response.docs.concat(stubData2.solr.response.docs));

      expect(w.libraryCollection.length).to.eql(6);

      expect($("#test .library-item").length).to.eql(3);

      expect($("#test .library-item div.identifier").map(function(){return $(this).text().trim()}).get()).to.eql(["2015IAUGA..2257982A", "2015IAUGA..2257639R", "2015IAUGA..2257768A"]);

      $("#test a[data-page='2']").click();

      expect($("#test .library-item").length).to.eql(3);

      expect($("#test .library-item div.identifier").map(function(){return $(this).text().trim()}).get()).to.eql(["2015APS..MAR.L8004M", "2015APS..MAR.L8003E", "2015arXiv150508012D"]);


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

      $("#test").append(w.getEl());


      w.setSubView({id : "1"});
      //shows library view first

      //now change to admin view

      w.setSubView({view :"admin"});


      $("#test .public-button").click();

      expect(fakeLibraryController.updateLibraryMetadata.args[1]).to.eql([
        "1",
        {
          "public": true
        }
      ]);



    });


    it.skip("should have and admin view that allows you to alter the permissions of members of a library")

  });



})