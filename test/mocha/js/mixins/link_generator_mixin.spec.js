define(['js/mixins/link_generator_mixin'],
  function(LinkGeneratorMixin){

    describe("Link Generator Mixin", function(){
      var mixin;

      beforeEach(function(){

        mixin = LinkGeneratorMixin;
      })



      it("should have a list of fields for the abstract and results page to facilitate link building", function(){

        //added to defaultQueryFields

        expect(mixin.abstractPageFields).to.equal("links_data,ids_data,[citations],property,bibcode");
        expect(mixin.resultsPageFields).to.equal("links_data,ids_data,[citations],property");

      })

      it("should have a adsUrlRedirect function used by other functions that takes an identifier and a type, and returns a classic ads url resolver link", function(){
        //this function directly uses Giovanni's logic from beer

        expect(mixin.adsUrlRedirect).to.be.instanceof(Function);
        expect(mixin.adsUrlRedirect('article', 'fakeBibcode')).to.eql("http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=fakeBibcode&link_type=ARTICLE");
        expect(mixin.adsUrlRedirect('doi', 'fakeBibcode')).to.eql("http://adsabs.harvard.edu/cgi-bin/nph-abs_connect?fforward=http://dx.doi.org/fakeBibcode");

      });

      it("should have a parseLinksData method used to add links for a list of results", function(){

        var dataWithLinks = mixin.parseLinksData([{

          "bibcode": "1993A&A...277..309L",

          "ids_data": [
            "{\"identifier\":\"1993A&A...277..309L\", \"alternate_bibcode\":\"\", \"deleted_bibcode\":\"\", \"description\":\"ADS bibcode\"}"
          ],
          "[citations]": {
            "num_citations": 62,
            "num_references": 8
          },
          "property": [
            "OPENACCESS",
            "REFEREED",
            "ADS_SCAN",
            "ARTICLE"
          ],

          "orderNum": 5
        }]);

        expect(mixin.parseLinksData).to.be.instanceof(Function);

        expect(dataWithLinks[0]["links"]).to.eql([
          {
            "openAccess": true,
            "letter": "G",
            "title": "ADS Scanned Article (Open Access)",
            "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=1993A&A...277..309L&link_type=GIF"
          },
          {
            "letter": "C",
            "title": "Citations (62)",
            "link": "#abs/1993A&A...277..309L/citations"
          },
          {
            "letter": "R",
            "title": "References (8)",
            "link": "#abs/1993A&A...277..309L/references"
          }
        ])

      })

      it("should have a parseResourcesData method used by the resources widget on the abstract page", function(){

        var dataWithLinks = mixin.parseResourcesData({
          "bibcode": "1989RMxAA..18..125C",
          "ids_data": [
            "{\"identifier\":\"1989RMxAA..18..125C\", \"alternate_bibcode\":\"\", \"deleted_bibcode\":\"\", \"description\":\"ADS bibcode\"}"
          ],
          "links_data": [
            "{\"title\":\"\", \"type\":\"simbad\", \"instances\":\"3\"}",
            "{\"title\":\"\", \"type\":\"ned\", \"instances\":\"1\"}",
            "{\"title\":\"\", \"type\":\"ADSlink\", \"instances\":\"\"}"
          ],
          "property": [
            "OPENACCESS",
            "NOT REFEREED",
            "ADS_SCAN",
            "TOC",
            "ARTICLE"
          ],
          "[citations]": {
            "num_citations": 9,
            "num_references": 48
          }
        });

        expect(mixin.parseResourcesData).to.be.instanceof(Function);

        expect(dataWithLinks.fullTextSources).to.eql([
          {
            "openAccess": true,
            "title": "ADS Scanned Article",
            "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=1989RMxAA..18..125C&link_type=GIF"
          }
        ]);

        expect(dataWithLinks.dataProducts).to.eql([
          {
            "title": "SIMBAD Objects",
            "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=1989RMxAA..18..125C&link_type=SIMBAD"
          },
          {
            "title": "NED Objects",
            "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=1989RMxAA..18..125C&link_type=NED"
          }
        ]);



      })


    })



})