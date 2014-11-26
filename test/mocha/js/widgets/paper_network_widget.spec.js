define([
  "js/wraps/paper_network",
  "js/components/json_response"
], function(
  PaperNetwork,
  JsonResponse
  ){

  describe("Paper Network Widget (UI Widget)", function(){

     var testDataBig = {
       "fullGraph": {
         "directed": false,
         "graph": [],
         "links": [
           {
             "overlap": [
               "1962AdA&A...1...47H",
               "1979ApJS...41..743C",
               "1982VA.....26..159G"
             ],
             "source": 0,
             "target": 18,
             "weight": 4
           },
           {
             "overlap": [
               "1970ApJ...159L.151L",
               "1977ApJ...211..693R",
               "1979PASP...91..589B",
               "1981ApJ...248...47F",
               "1984ApJS...54...33B",
               "1987ApJ...323L.113R",
               "1989ApJ...336L..13L",
               "1990ApJ...353L...7S",
               "1990MNRAS.242P..33U",
               "1991ApJ...367..126C",
               "1991MNRAS.252...72W"
             ],
             "source": 1,
             "target": 2,
             "weight": 25
           },
           {
             "overlap": [
               "1984ApJS...54...33B",
               "1991ARA&A..29..543H",
               "1992AJ....103..691H"
             ],
             "source": 1,
             "target": 3,
             "weight": 6
           },
           {
             "overlap": [
               "1970ApJ...159L.151L",
               "1977ApJ...211..693R",
               "1984Natur.310..733F",
               "1989ApJ...336L..13L",
               "1990A&A...240..254J",
               "1990ApJ...353L...7S",
               "1990MNRAS.242P..33U",
               "1990MNRAS.246..477P",
               "1991ApJ...367..126C",
               "1992AJ....103..691H"
             ],
             "source": 1,
             "target": 6,
             "weight": 26
           },
           {
             "overlap": [
               "1983AJ.....88..439L"
             ],
             "source": 1,
             "target": 10,
             "weight": 3
           },
           {
             "overlap": [
               "1979PASP...91..589B"
             ],
             "source": 1,
             "target": 11,
             "weight": 4
           },
           {
             "overlap": [
               "1985IAUS..113..541W",
               "1991ARA&A..29..543H"
             ],
             "source": 1,
             "target": 19,
             "weight": 3
           },
           {
             "overlap": [
               "1987ApJ...323...54E",
               "1989ApJ...336..734E"
             ],
             "source": 1,
             "target": 22,
             "weight": 5
           },
           {
             "overlap": [
               "1984ApJS...54...33B",
               "1987nngp.proc...18S",
               "1987nngp.proc...47B",
               "1991A&A...245...31L",
               "1991AJ....101..677H"
             ],
             "source": 2,
             "target": 3,
             "weight": 9
           },
           {
             "overlap": [
               "1965ApJ...142.1351B",
               "1970ApJ...159L.151L",
               "1973ApJ...185..809D",
               "1977ApJ...211..693R",
               "1979ApJ...230..667K",
               "1983ApJ...272...29C",
               "1989ApJ...336L..13L",
               "1989ApJ...345..245C",
               "1990A&A...240...70N",
               "1990AJ....100.1805S",
               "1990ApJ...353L...7S",
               "1990MNRAS.242P..33U",
               "1991ApJ...367..126C",
               "1991MNRAS.249..560H"
             ],
             "source": 2,
             "target": 6,
             "weight": 31
           },
           {
             "overlap": [
               "1981A&AS...46...79V"
             ],
             "source": 2,
             "target": 10,
             "weight": 2
           },
           {
             "overlap": [
               "1974AJ.....79..745L",
               "1979PASP...91..589B",
               "1989PASP..101..445L"
             ],
             "source": 2,
             "target": 11,
             "weight": 11
           },
           {
             "overlap": [
               "1981A&AS...46...79V"
             ],
             "source": 2,
             "target": 12,
             "weight": 4
           },
           {
             "overlap": [
               "1981A&AS...46...79V"
             ],
             "source": 2,
             "target": 17,
             "weight": 2
           },
           {
             "overlap": [
               "1991ARA&A..29..543H"
             ],
             "source": 3,
             "target": 19,
             "weight": 1
           },
           {
             "overlap": [
               "1992AJ....103..691H",
               "1993AJ....105..877R"
             ],
             "source": 3,
             "target": 6,
             "weight": 4
           },
           {
             "overlap": [
               "1968MNRAS.138..495L",
               "1969ApJ...158...17I"
             ],
             "source": 5,
             "target": 20,
             "weight": 7
           },
           {
             "overlap": [
               "2002A&A...391..195G"
             ],
             "source": 7,
             "target": 9,
             "weight": 1
           },
           {
             "overlap": [
               "1955ApJ...121..161S"
             ],
             "source": 7,
             "target": 18,
             "weight": 1
           },
           {
             "overlap": [
               "1955ApJ...121..161S",
               "2000A&A...354..836L",
               "2009A&A...494..539L",
               "2010A&A...521A..22F",
               "2010ApJ...724..296P",
               "2011A&A...529A..25S"
             ],
             "source": 7,
             "target": 19,
             "weight": 5
           },
           {
             "overlap": [
               "1999ApJ...527L..81Z"
             ],
             "source": 7,
             "target": 14,
             "weight": 2
           },
           {
             "overlap": [
               "1979ApJS...39..135J",
               "1985ApJS...58..711V"
             ],
             "source": 8,
             "target": 10,
             "weight": 6
           },
           {
             "overlap": [
               "1985ApJS...58..711V"
             ],
             "source": 8,
             "target": 21,
             "weight": 6
           },
           {
             "overlap": [
               "1995ARA&A..33..381F",
               "2010ARA&A..48..339B"
             ],
             "source": 9,
             "target": 19,
             "weight": 2
           },
           {
             "overlap": [
               "1981A&AS...46...79V"
             ],
             "source": 10,
             "target": 12,
             "weight": 5
           },
           {
             "overlap": [
               "1967lmc..book.....H",
               "1981A&AS...46...79V",
               "1983ApJ...272..488F",
               "1984ApJ...285L..53S"
             ],
             "source": 10,
             "target": 17,
             "weight": 11
           },
           {
             "overlap": [
               "1985ApJS...58..711V"
             ],
             "source": 10,
             "target": 21,
             "weight": 5
           },
           {
             "overlap": [
               "1982AJ.....87.1165B"
             ],
             "source": 10,
             "target": 22,
             "weight": 2
           },
           {
             "overlap": [
               "1981A&AS...46...79V",
               "1985ApJ...299..211E",
               "1988A&A...196...84C",
               "1988AJ.....96.1383E"
             ],
             "source": 12,
             "target": 17,
             "weight": 20
           },
           {
             "overlap": [
               "1985ApJ...299..211E",
               "1988A&A...196...84C"
             ],
             "source": 12,
             "target": 22,
             "weight": 9
           },
           {
             "overlap": [
               "1998ARA&A..36..435M",
               "1998MNRAS.300..200K",
               "1999AJ....118.1551W",
               "2000NewA....5..305F",
               "2000msc..conf..241F",
               "2000msc..conf..254H"
             ],
             "source": 13,
             "target": 14,
             "weight": 40
           },
           {
             "overlap": [
               "1987degc.book.....S"
             ],
             "source": 14,
             "target": 19,
             "weight": 1
           },
           {
             "overlap": [
               "1986A&A...162...21B"
             ],
             "source": 15,
             "target": 17,
             "weight": 11
           },
           {
             "overlap": [
               "1982ApJ...253..655E"
             ],
             "source": 16,
             "target": 18,
             "weight": 2
           },
           {
             "overlap": [
               "1983ApJ...266..105P",
               "1985ApJ...299..211E",
               "1986ApJ...311..113M",
               "1988A&A...196...84C"
             ],
             "source": 17,
             "target": 22,
             "weight": 9
           },
           {
             "overlap": [
               "1955ApJ...121..161S"
             ],
             "source": 18,
             "target": 19,
             "weight": 0
           },
           {
             "overlap": [
               "1966AJ.....71...64K"
             ],
             "source": 19,
             "target": 20,
             "weight": 2
           }
         ],
         "multigraph": false,
         "nodes": [
           {
             "citation_count": 6,
             "first_author": "Myers, P. C.",
             "group": 0,
             "id": 0,
             "nodeName": "1991ASPC...13...73M",
             "nodeWeight": 6,
             "title": "The role of dense cores in isolated and cluster star formation."
           },
           {
             "citation_count": 52,
             "first_author": "Richer, Harvey B.",
             "group": 1,
             "id": 1,
             "nodeName": "1993AJ....105..877R",
             "nodeWeight": 52,
             "title": "Star and Cluster Formation in NGC 1275"
           },
           {
             "citation_count": 375,
             "first_author": "Holtzman, J. A.",
             "group": 1,
             "id": 2,
             "nodeName": "1992AJ....103..691H",
             "nodeWeight": 375,
             "title": "Planetary Camera Observations of NGC 1275: Discovery of a Central Population of Compact Massive Blue Star Clusters"
           },
           {
             "citation_count": 486,
             "first_author": "Whitmore, Bradley C.",
             "group": 1,
             "id": 3,
             "nodeName": "1995AJ....109..960W",
             "nodeWeight": 486,
             "title": "Hubble Space Telescope Observations of Young Star Clusters in NGC 4038/4039, \"The Antennae\" Galaxies"
           },
           {
             "citation_count": 0,
             "first_author": "Han, Hillary S. W.",
             "group": 2,
             "id": 4,
             "nodeName": "2013arXiv1305.3460H",
             "nodeWeight": 0,
             "title": "A bijection for tri-cellular maps"
           },
           {
             "citation_count": 47,
             "first_author": "Shapiro, S. L.",
             "group": 0,
             "id": 5,
             "nodeName": "1985IAUS..113..373S",
             "nodeWeight": 47,
             "title": "Monte Carlo simulations of the 2+1 dimensional Fokker-Planck equation: spherical star clusters containing massive, central black holes."
           },
           {
             "citation_count": 13,
             "first_author": "Ferruit, P.",
             "group": 1,
             "id": 6,
             "nodeName": "1994A&A...288...65F",
             "nodeWeight": 13,
             "title": "Sub-arcsecond resolution 2D spectrography of the central regions of NGC 1275 with TIGER."
           },
           {
             "citation_count": 16,
             "first_author": "Cook, David O.",
             "group": 0,
             "id": 7,
             "nodeName": "2012ApJ...751..100C",
             "nodeWeight": 16,
             "title": "The ACS Nearby Galaxy Survey Treasury. X. Quantifying the Star Cluster Formation Efficiency of nearby Dwarf Galaxies"
           },
           {
             "citation_count": 41,
             "first_author": "Eggen, Olin J.",
             "group": 3,
             "id": 8,
             "nodeName": "1992AJ....104.1482E",
             "nodeWeight": 41,
             "title": "The Hyades Supercluster in FK5"
           },
           {
             "citation_count": 7,
             "first_author": "Hasan, Priya",
             "group": 0,
             "id": 9,
             "nodeName": "2011MNRAS.413.2345H",
             "nodeWeight": 7,
             "title": "Mass segregation in diverse environments"
           },
           {
             "citation_count": 6,
             "first_author": "Schommer, R. A.",
             "group": 3,
             "id": 10,
             "nodeName": "1986AJ.....92.1334S",
             "nodeWeight": 6,
             "title": "E2: an intermediate-age LMC cluster."
           },
           {
             "citation_count": 39,
             "first_author": "Benedict, G. F.",
             "group": 1,
             "id": 11,
             "nodeName": "1993AJ....105.1369B",
             "nodeWeight": 39,
             "title": "NGC 4313. II. Hubble Space Telescope I-Band Surface Photometry of the Nuclear Region"
           },
           {
             "citation_count": 0,
             "first_author": "Pandey, A. K.",
             "group": 3,
             "id": 12,
             "nodeName": "1991ASPC...13..167P",
             "nodeWeight": 0,
             "title": "Age calibration and age distribution of LMC clusters."
           },
           {
             "citation_count": 0,
             "first_author": "Fellhauer, M.",
             "group": 4,
             "id": 13,
             "nodeName": "2001A&AT...20...85F",
             "nodeWeight": 0,
             "title": "Dwarf-galaxy-objects formed out of merging star-clusters"
           },
           {
             "citation_count": 21,
             "first_author": "Fellhauer, M.",
             "group": 4,
             "id": 14,
             "nodeName": "2002CeMDA..82..113F",
             "nodeWeight": 21,
             "title": "Merging Timescales and Merger Rates of Star Clusters in Dense Star Cluster Complexes"
           },
           {
             "citation_count": 109,
             "first_author": "Bica, E.",
             "group": 3,
             "id": 15,
             "nodeName": "1986A&AS...66..171B",
             "nodeWeight": 109,
             "title": "A grid of star cluster properties for stellar population synthesis."
           },
           {
             "citation_count": 16,
             "first_author": "Efremov, Y. N.",
             "group": 0,
             "id": 16,
             "nodeName": "1982SvAL....8..357E",
             "nodeWeight": 16,
             "title": "The age and dimensions of star complexes."
           },
           {
             "citation_count": 59,
             "first_author": "Bica, E.",
             "group": 3,
             "id": 17,
             "nodeName": "1992AJ....103.1859B",
             "nodeWeight": 59,
             "title": "Bar Stars Clusters in the LMC: Formation History From UBV Integrated Photometry"
           },
           {
             "citation_count": 1943,
             "first_author": "Shu, F. H.",
             "group": 0,
             "id": 18,
             "nodeName": "1987ARA&A..25...23S",
             "nodeWeight": 1943,
             "title": "Star formation in molecular clouds: observation and theory."
           },
           {
             "citation_count": 13,
             "first_author": "Larsen, S. S.",
             "group": 0,
             "id": 19,
             "nodeName": "2012A&A...546A..53L",
             "nodeWeight": 13,
             "title": "Detailed abundance analysis from integrated high-dispersion spectroscopy: globular clusters in the Fornax dwarf spheroidal"
           },
           {
             "citation_count": 21,
             "first_author": "Katz, J.",
             "group": 0,
             "id": 20,
             "nodeName": "1978ApJ...223..299K",
             "nodeWeight": 21,
             "title": "Steepest descent technique and stellar equilibrium statistical mechanics. IV. Gravitating systems with an energy cutoff."
           },
           {
             "citation_count": 5,
             "first_author": "Milone, E. F.",
             "group": 3,
             "id": 21,
             "nodeName": "1991ASPC...13..427M",
             "nodeWeight": 5,
             "title": "A royal road to clusters and stellar evolution: binaries-in-clusters applications to cluster studies."
           },
           {
             "citation_count": 9,
             "first_author": "Chiosi, C.",
             "group": 3,
             "id": 22,
             "nodeName": "1989RMxAA..18..125C",
             "nodeWeight": 9,
             "title": "Properties of star clusters in the Large Magellanic Cloud."
           }
         ]
       },
       "summaryGraph": {
         "directed": false,
         "graph": [],
         "links": [
           {
             "source": 0,
             "target": 1,
             "weight": 4
           },
           {
             "source": 0,
             "target": 4,
             "weight": 3
           },
           {
             "source": 1,
             "target": 3,
             "weight": 16
           }
         ],
         "multigraph": false,
         "nodes": [
           {
             "id": 0,
             "nodeName": {
               "dwarf": 3.2188758248682006,
               "mass": 3.2188758248682006,
               "mechanics": 1.6094379124341003,
               "monte": 1.6094379124341003,
               "nearby": 3.2188758248682006,
               "spherical": 1.6094379124341003
             },
             "paperCount": 8,
             "size": 2069
           },
           {
             "id": 1,
             "nodeName": {
               "hubble": 3.2188758248682006,
               "ngc": 8.047189562170502,
               "observations": 3.2188758248682006,
               "region": 3.2188758248682006,
               "space": 3.2188758248682006,
               "telescope": 3.2188758248682006
             },
             "paperCount": 5,
             "size": 965
           },
           {
             "id": 2,
             "nodeName": {
               "bijection": 1.6094379124341003,
               "maps": 1.6094379124341003,
               "tricellular": 1.6094379124341003
             },
             "paperCount": 1,
             "size": 0
           },
           {
             "id": 3,
             "nodeName": {
               "age": 1.8325814637483102,
               "calibration": 1.6094379124341003,
               "cloud": 1.6094379124341003,
               "lmc": 4.828313737302301,
               "properties": 3.2188758248682006,
               "stellar": 1.8325814637483102
             },
             "paperCount": 7,
             "size": 229
           },
           {
             "id": 4,
             "nodeName": {
               "dwarfgalaxyobjects": 1.6094379124341003,
               "formed": 1.6094379124341003,
               "merger": 1.6094379124341003,
               "merging": 3.2188758248682006,
               "rates": 1.6094379124341003,
               "timescales": 1.6094379124341003
             },
             "paperCount": 2,
             "size": 21
           }
         ]
       }
     };


    testDataSmall = {
      "fullGraph": {
        "links": [],
        "nodes": [
          {
            "citation_count": 6,
            "first_author": "Myers, P. C.",
            "nodeName": "1991ASPC...13...73M",
            "nodeWeight": 6,
            "read_count": 9.0,
            "title": "The role of dense cores in isolated and cluster star formation.",
            "year": "1991"
          },
          {
            "citation_count": 16,
            "first_author": "Efremov, Y. N.",
            "nodeName": "1982SvAL....8..357E",
            "nodeWeight": 16,
            "read_count": 5.0,
            "title": "The age and dimensions of star complexes.",
            "year": "1982"
          },
          {
            "citation_count": 41,
            "first_author": "Eggen, Olin J.",
            "nodeName": "1992AJ....104.1482E",
            "nodeWeight": 41,
            "read_count": 11.0,
            "title": "The Hyades Supercluster in FK5",
            "year": "1992"
          },
          {
            "citation_count": 7,
            "first_author": "Hasan, Priya",
            "nodeName": "2011MNRAS.413.2345H",
            "nodeWeight": 7,
            "read_count": 47.0,
            "title": "Mass segregation in diverse environments",
            "year": "2011"
          },
          {
            "citation_count": 0,
            "first_author": "Han, Hillary S. W.",
            "nodeName": "2013arXiv1305.3460H",
            "nodeWeight": 0,
            "read_count": 5.0,
            "title": "A bijection for tri-cellular maps",
            "year": "2013"
          },
          {
            "citation_count": 9,
            "first_author": "Chiosi, C.",
            "nodeName": "1989RMxAA..18..125C",
            "nodeWeight": 9,
            "read_count": 5.0,
            "title": "Properties of star clusters in the Large Magellanic Cloud.",
            "year": "1989"
          }
        ]
      }
    }


    beforeEach(function(){
      PaperNetwork.processResponse(new JsonResponse(testDataBig));

      $("#test").append(PaperNetwork.view.el)


    });


    afterEach(function(){

      $("#test").empty();


    });


    it("should render small word clouds for the summary nodes", function(){

//      expect(d3.select(".summary-node-group").text()).to.eql("classificationdatadigitallibraryoverviewsearching")
    });




  });




});