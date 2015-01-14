define([
  'marionette',
  'js/widgets/network_vis/network_widget',
  'js/bugutils/minimal_pubsub',
  'js/components/api_query',
  'js/components/json_response',
  'underscore'

], function (Marionette, NetworkWidget, MinimalPubsub, ApiQuery, JsonResponse, _) {

  describe("Network Visualization Widget", function () {

    var testDataSmall, testDataLarge, testDataEmpty, networkWidget;

    testDataSmall = {"data": {"fullGraph": {"links": [
      {"source": 10, "target": 15, "value": 13.509433962264154},
      {"source": 9, "target": 11, "value": 28.962264150943401},
      {"source": 18, "target": 9, "value": 32.641509433962256},
      {"source": 1, "target": 3, "value": 40.0},
      {"source": 12, "target": 1, "value": 1.0},
      {"source": 2, "target": 8, "value": 17.924528301886795},
      {"source": 2, "target": 13, "value": 7.622641509433965},
      {"source": 14, "target": 3, "value": 17.924528301886795},
      {"source": 10, "target": 18, "value": 32.641509433962256},
      {"source": 8, "target": 3, "value": 17.924528301886795},
      {"source": 3, "target": 7, "value": 38.528301886792462},
      {"source": 12, "target": 7, "value": 1.0},
      {"source": 0, "target": 6, "value": 1.0},
      {"source": 10, "target": 0, "value": 23.075471698113216},
      {"source": 17, "target": 3, "value": 24.54716981132076},
      {"source": 3, "target": 16, "value": 13.509433962264154},
      {"source": 12, "target": 3, "value": 1.0},
      {"source": 12, "target": 0, "value": 1.0},
      {"source": 12, "target": 9, "value": 1.0},
      {"source": 0, "target": 11, "value": 23.075471698113216},
      {"source": 8, "target": 13, "value": 7.622641509433965},
      {"source": 0, "target": 1, "value": 23.075471698113216},
      {"source": 0, "target": 9, "value": 23.075471698113216},
      {"source": 18, "target": 1, "value": 32.641509433962256},
      {"source": 3, "target": 5, "value": 7.622641509433965},
      {"source": 18, "target": 7, "value": 32.641509433962256},
      {"source": 0, "target": 7, "value": 23.075471698113216},
      {"source": 2, "target": 5, "value": 7.622641509433965},
      {"source": 7, "target": 6, "value": 1.0},
      {"source": 2, "target": 16, "value": 13.509433962264154},
      {"source": 1, "target": 6, "value": 1.0},
      {"source": 6, "target": 11, "value": 1.0},
      {"source": 15, "target": 3, "value": 13.509433962264154},
      {"source": 2, "target": 14, "value": 17.924528301886795},
      {"source": 3, "target": 6, "value": 1.0},
      {"source": 3, "target": 13, "value": 7.622641509433965},
      {"source": 10, "target": 7, "value": 38.528301886792462},
      {"source": 18, "target": 3, "value": 32.641509433962256},
      {"source": 15, "target": 1, "value": 13.509433962264154},
      {"source": 9, "target": 1, "value": 37.056603773584911},
      {"source": 8, "target": 5, "value": 7.622641509433965},
      {"source": 10, "target": 3, "value": 39.264150943396231},
      {"source": 12, "target": 6, "value": 1.0},
      {"source": 14, "target": 5, "value": 7.622641509433965},
      {"source": 14, "target": 13, "value": 7.622641509433965},
      {"source": 9, "target": 6, "value": 1.0},
      {"source": 3, "target": 11, "value": 28.962264150943401},
      {"source": 12, "target": 11, "value": 1.0},
      {"source": 9, "target": 3, "value": 37.056603773584911},
      {"source": 10, "target": 12, "value": 1.0},
      {"source": 1, "target": 11, "value": 28.962264150943401},
      {"source": 10, "target": 6, "value": 1.0},
      {"source": 15, "target": 9, "value": 13.509433962264154},
      {"source": 14, "target": 8, "value": 17.924528301886795},
      {"source": 10, "target": 9, "value": 37.056603773584911},
      {"source": 10, "target": 1, "value": 37.056603773584911},
      {"source": 8, "target": 16, "value": 13.509433962264154},
      {"source": 2, "target": 3, "value": 17.924528301886795},
      {"source": 10, "target": 11, "value": 28.962264150943401},
      {"source": 4, "target": 3, "value": 24.54716981132076},
      {"source": 13, "target": 5, "value": 7.622641509433965},
      {"source": 0, "target": 3, "value": 23.075471698113216},
      {"source": 14, "target": 16, "value": 13.509433962264154},
      {"source": 7, "target": 11, "value": 28.962264150943401},
      {"source": 1, "target": 7, "value": 34.113207547169807},
      {"source": 9, "target": 7, "value": 34.113207547169807},
      {"source": 18, "target": 11, "value": 25.283018867924529},
      {"source": 0, "target": 18, "value": 18.660377358490567}
    ], "nodes": [
      {"nodeName": "Bohlen, E", "nodeWeight": 13.936607317550379},
      {"nodeName": "Henneken, E", "nodeWeight": 37.994521620035215},
      {"nodeName": "Dell\u0027Antonio, I", "nodeWeight": 11.090132394182483},
      {"nodeName": "Kurtz, M", "nodeWeight": 150.0},
      {"nodeName": "Brody, T", "nodeWeight": 14.26759277375595},
      {"nodeName": "Utsumi, Y", "nodeWeight": 6.3239418248222785},
      {"nodeName": "Rosvall, M", "nodeWeight": 5.0},
      {"nodeName": "Murray, S", "nodeWeight": 29.256505576208173},
      {"nodeName": "Geller, M", "nodeWeight": 11.090132394182483},
      {"nodeName": "Grant, C", "nodeWeight": 26.079045196634706},
      {"nodeName": "Accomazzi, A", "nodeWeight": 34.022696145568375},
      {"nodeName": "Thompson, D", "nodeWeight": 17.341029152807668},
      {"nodeName": "Bergstrom, C", "nodeWeight": 5.0},
      {"nodeName": "Miyazaki, S", "nodeWeight": 6.3239418248222785},
      {"nodeName": "Fabricant, D", "nodeWeight": 11.090132394182483},
      {"nodeName": "Di Milia, G", "nodeWeight": 7.1183069197156463},
      {"nodeName": "Wyatt, W", "nodeWeight": 7.1183069197156463},
      {"nodeName": "Bollen, J", "nodeWeight": 14.26759277375595},
      {"nodeName": "Eichhorn, G", "nodeWeight": 18.664970977629945}
    ]}}, "msg": {"numFound": 10, "rows": 10}};

    testDataLarge = {"data": {
      "bibcode_dict": {
        "1974SPIE...45..171K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Found! Some More Practical Applications For Holograms"
        },
        "1981romi.work.....K": {
          "authors": [
            "Jannaway, W",
            "Kurtz, M",
            "Stone, G"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Experience with on-line generator partial discharge tests"
        },
        "1982BICDS..23...13K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Automatic Spectral Classification"
        },
        "1982PhDT.........2K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 11,
          "read_count": 6.0,
          "title": "Automatic Spectral Classification."
        },
        "1983ESASP.201...47K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 7,
          "read_count": 0,
          "title": "Classification methods: An introductory survey (Review)"
        },
        "1984BAAS...16..457K": {
          "authors": [
            "Beers, T",
            "Geller, M",
            "Gioia, I",
            "Huchra, J",
            "Kurtz, M",
            "Maccacaro, T",
            "Schild, R",
            "Stauffer, J"
          ],
          "citation_count": 0,
          "read_count": 4.0,
          "title": "The Dynamics of the X-ray Cluster Abell 744"
        },
        "1984mpsc.conf..136K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 17,
          "read_count": 0,
          "title": "Progress in Automation Techniques for MK Classification."
        },
        "1985AJ.....90.1665K": {
          "authors": [
            "Beers, T",
            "Geller, M",
            "Gioia, I",
            "Huchra, J",
            "Kurtz, M",
            "Maccacaro, T",
            "Schild, R",
            "Stauffer, J"
          ],
          "citation_count": 28,
          "read_count": 14.0,
          "title": "The X-ray cluster Abell 744."
        },
        "1986BICDS..30..171K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Topics in Observational Cosmology"
        },
        "1987BICDS..32...97K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Remarks on the Effects of Systematic Error"
        },
        "1988ESOC...28..113K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 2,
          "read_count": 0,
          "title": "The search for structure: object classification in large data sets."
        },
        "1988prai.proc..317K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Astronomical object classification"
        },
        "1989LNP...329...89K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Classification and knowledge"
        },
        "1989LNP...329...91K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 1,
          "read_count": 2.0,
          "title": "Classification and knowledge"
        },
        "1989daa..conf..155K": {
          "authors": [
            "Falco, E",
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Tests of Reduction and Analysis Algorithms for Astronomical Images"
        },
        "1990PaReL..11..507K": {
          "authors": [
            "Kurtz, M",
            "Mussio, P",
            "Ossorio, P"
          ],
          "citation_count": 1,
          "read_count": 0,
          "title": "A cognitive system for astronomical image interpretation"
        },
        "1990ebua.conf..363K": {
          "authors": [
            "Falco, E",
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "The Systematic Effect of Shape on Galaxy Magnitudes"
        },
        "1991opos.conf..133K": {
          "authors": [
            "Kurtz, M",
            "LaSala, J"
          ],
          "citation_count": 4,
          "read_count": 0,
          "title": "Towards AN Automatic Spectral Classification"
        },
        "1992ASPC...25..432K": {
          "authors": [
            "Fabricant, D",
            "Kriss, G",
            "Kurtz, M",
            "Mink, D",
            "Tonry, J",
            "Torres, G",
            "Wyatt, W"
          ],
          "citation_count": 109,
          "read_count": 3.0,
          "title": "XCSAO: A Radial Velocity Package for the IRAF Environment"
        },
        "1992ESOC...43...85K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 3,
          "read_count": 0,
          "title": "Second Order Knowledge: Information Retrieval in the Terabyte Era"
        },
        "1992IAUTB..21..331K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "The Need for Automation in the Reduction and Analysis of Stellar Spectra"
        },
        "1993AAS...182.0306K": {
          "authors": [
            "Eichhorn, G",
            "Karakashian, T",
            "Kurtz, M",
            "Murray, S",
            "Ossorio, P",
            "Stern, C",
            "Stoner, J",
            "Watson, J"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "The ADS Abstract Service"
        },
        "1993ASPC...52..132K": {
          "authors": [
            "Eichhorn, G",
            "Grant, C",
            "Karakashian, T",
            "Kurtz, M",
            "Murray, S",
            "Ossorio, P",
            "Stoner, J",
            "Watson, J"
          ],
          "citation_count": 19,
          "read_count": 3.0,
          "title": "Intelligent Text Retrieval in the NASA Astrophysics Data System"
        },
        "1993ASSL..182...21K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 5,
          "read_count": 2.0,
          "title": "Advice from the Oracle: Really Intelligent Information Retrieval"
        },
        "1994AAS...184.3801K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Giant Shoulders: Data and Discovery in Astronomy"
        },
        "1994IAUS..161..331K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 1,
          "read_count": 0,
          "title": "The Future of Memory: Archiving Astronomical Information"
        },
        "1995AAS...186.0202K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Enhancements to the NASA Astrophysics Science Information and Abstract Service"
        },
        "1995PASP..107..776K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Giant Shoulders: Data and Discovery in Astronomy"
        },
        "1996AAS...189.0607K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 1,
          "read_count": 2.0,
          "title": "Journal Citations: An ADS-AAS Collaboration"
        },
        "1996VA.....40..393K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Indexing: what you find is what you get"
        },
        "1996dkcw.proc..123K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "The NASA Astrophysics Data System: A Heterogeneous Distributed Data Environment"
        },
        "1997AAS...191.1701K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 1,
          "read_count": 0,
          "title": "The Relative Effectiveness of Astronomy Journals"
        },
        "1997ASSL..212D..23K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Conquering Ignorance: Data, Information and Knowledge"
        },
        "1997DPS....29.2705K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Green, D",
            "Kurtz, M",
            "Marsden, B",
            "Murray, S",
            "Williams, G"
          ],
          "citation_count": 0,
          "read_count": 2.0,
          "title": "The IAU Circulars and Minor Planet Electronic Circulars are Now Available Through the NASA Astrophysics Data System"
        },
        "1998AAS...193.1311K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 0,
          "read_count": 2.0,
          "title": "ADS on WWW: Doubling Yearly for Five Years"
        },
        "1998ASPC..145..478K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 0,
          "read_count": 3.0,
          "title": "Keeping Bibliographies using ADS"
        },
        "1998ASPC..153..293K": {
          "authors": [
            "Eichhorn, G",
            "Kurtz, M"
          ],
          "citation_count": 4,
          "read_count": 4.0,
          "title": "The Historical Literature of Astronomy, via ADS"
        },
        "1998PASP..110..934K": {
          "authors": [
            "Kurtz, M",
            "Mink, D"
          ],
          "citation_count": 288,
          "read_count": 37.0,
          "title": "RVSAO 2.0: Digital Redshifts and Radial Velocities"
        },
        "1999AAS...194.4407K": {
          "authors": [
            "Accomazzi, A",
            "Demleitner, M",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 0,
          "read_count": 2.0,
          "title": "A Model for Readership of Astronomical Journals, From an Analysis of ADS Readership"
        },
        "1999Dlib....5.....K": {
          "authors": [
            "Accomazzi, A",
            "Demleitner, M",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 0,
          "read_count": 2.0,
          "title": "The NASA ADS Abstract Service and the Distributed Astronomy Digital Library"
        },
        "1999ascl.soft12003K": {
          "authors": [
            "Kurtz, M",
            "Mink, D"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "RVSAO 2.0: Digital Redshifts and Radial Velocities"
        },
        "2000A&AS..143...41K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S",
            "Watson, J"
          ],
          "citation_count": 60,
          "read_count": 18.0,
          "title": "The NASA Astrophysics Data System: Overview"
        },
        "2000ApJ...533L.183K": {
          "authors": [
            "Kurtz, M",
            "Mink, D"
          ],
          "citation_count": 7,
          "read_count": 15.0,
          "title": "Eigenvector Sky Subtraction"
        },
        "2001JChEd..78.1122K": {
          "authors": [
            "Holden, B",
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Analysis of a Distance-Education Program in Organic Chemistry"
        },
        "2001SPIE.4477..186K": {
          "authors": [
            "Eichhorn, G",
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Evolution of Urania into the AVO"
        },
        "2002AAS...201.0904K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "The NASA Astrophysics Data System: Bibliometrics Investigations"
        },
        "2002SPIE.4847..238K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 7,
          "read_count": 2.0,
          "title": "Second order bibliometric operators in the Astrophysics Data System"
        },
        "2003AAS...203.2005K": {
          "authors": [
            "Accomazzi, A",
            "Bohlen, E",
            "Eichhorn, G",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M",
            "Murray, S",
            "Thompson, D"
          ],
          "citation_count": 4,
          "read_count": 2.0,
          "title": "The myADS Update Service"
        },
        "2003lisa.conf..223K": {
          "authors": [
            "Accomazzi, A",
            "Bohlen, E",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S",
            "Thompson, D"
          ],
          "citation_count": 3,
          "read_count": 0,
          "title": "The NASA Astrophysics Data System: Obsolescence of Reads and Cites"
        },
        "2005AAS...207.3405K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M",
            "Murray, S",
            "Thompson, D"
          ],
          "citation_count": 0,
          "read_count": 2.0,
          "title": "The significance of e-printed papers in Astronomy and Physics"
        },
        "2005IPM....41.1395K": {
          "authors": [
            "Accomazzi, A",
            "Demleitner, M",
            "Eichhorn, G",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 15,
          "read_count": 34.0,
          "title": "The Effect of Use and Access on Citations"
        },
        "2005JASIS..56...36K": {
          "authors": [
            "Accomazzi, A",
            "Demleitner, M",
            "Eichhorn, G",
            "Grant, C",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 12,
          "read_count": 15.0,
          "title": "Worldwide Use and Impact of the NASA Astrophysics Data System Digital Library"
        },
        "2005JASIS..56..111K": {
          "authors": [
            "Accomazzi, A",
            "Demleitner, M",
            "Eichhorn, G",
            "Elwell, B",
            "Grant, C",
            "Kurtz, M",
            "Martimbeau, N",
            "Murray, S"
          ],
          "citation_count": 9,
          "read_count": 10.0,
          "title": "The Bibliometric Properties of Article Readership Information"
        },
        "2006AAS...20921809K": {
          "authors": [
            "Accomazzi, A",
            "Bohlen, E",
            "Eichhorn, G",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M",
            "Murray, S",
            "Thompson, D"
          ],
          "citation_count": 0,
          "read_count": 4.0,
          "title": "The New Physics and Astronomy Education Portal of the Smithsonian/NASA Astrophysics Data System"
        },
        "2006ASPC..351..653K": {
          "authors": [
            "Accomazzi, A",
            "Eichhorn, G",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 1,
          "read_count": 6.0,
          "title": "Intelligent Information Retrieval"
        },
        "2006oaks.book...45K": {
          "authors": [
            "Brody, T",
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "The impact lost to authors and research"
        },
        "2007AAS...211.4730K": {
          "authors": [
            "Accomazzi, A",
            "Bergstrom, C",
            "Bohlen, E",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M",
            "Murray, S",
            "Rosvall, M",
            "Thompson, D"
          ],
          "citation_count": 1,
          "read_count": 0,
          "title": "Mapping The Astronomy Literature"
        },
        "2007AJ....134.1360K": {
          "authors": [
            "Dell'Antonio, I",
            "Fabricant, D",
            "Geller, M",
            "Kurtz, M",
            "Wyatt, W"
          ],
          "citation_count": 13,
          "read_count": 9.0,
          "title": "\u03bc-PhotoZ: Photometric Redshifts by Inverting the Tolman Surface Brightness Test"
        },
        "2007APS..MARU20009K": {
          "authors": [
            "Accomazzi, A",
            "Bohlen, E",
            "Eichhorn, G",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M",
            "Murray, S",
            "Thompson, D"
          ],
          "citation_count": 0,
          "read_count": 6.0,
          "title": "myADS-arXiv: A fully customized, open access virtual journal"
        },
        "2007ASPC..377...23K": {
          "authors": [
            "Accomazzi, A",
            "Bohlen, E",
            "Eichhorn, G",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M",
            "Murray, S",
            "Thompson, D"
          ],
          "citation_count": 0,
          "read_count": 8.0,
          "title": "The Future of Technical Libraries"
        },
        "2007arXiv0709.0896K": {
          "authors": [
            "Henneken, E",
            "Kurtz, M"
          ],
          "citation_count": 4,
          "read_count": 25.0,
          "title": "Open Access does not increase citations for research articles from The Astrophysical Journal"
        },
        "2007fpca.conf..141K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "The ADS Position on Open Access"
        },
        "2009astro2010P..28K": {
          "authors": [
            "Accomazzi, A",
            "Kurtz, M",
            "Murray, S"
          ],
          "citation_count": 1,
          "read_count": 13.0,
          "title": "The Smithsonian/NASA Astrophysics Data System (ADS) Decennial Report"
        },
        "2010ARIST..44....3K": {
          "authors": [
            "Bollen, J",
            "Kurtz, M"
          ],
          "citation_count": 8,
          "read_count": 43.0,
          "title": "Usage Bibliometrics"
        },
        "2010ASPC..434..155K": {
          "authors": [
            "Accomazzi, A",
            "Di Milia, G",
            "Grant, C",
            "Henneken, E",
            "Kurtz, M"
          ],
          "citation_count": 3,
          "read_count": 8.0,
          "title": "Using Multipartite Graphs for Recommendation and Discovery"
        },
        "2011ApSSP...1...23K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 1,
          "read_count": 50.0,
          "title": "The Emerging Scholarly Brain"
        },
        "2011ApSSP...1..143K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "FPCA-II Concluding Remarks"
        },
        "2012ApJ...750..168K": {
          "authors": [
            "Dell'Antonio, I",
            "Fabricant, D",
            "Geller, M",
            "Kurtz, M",
            "Miyazaki, S",
            "Utsumi, Y"
          ],
          "citation_count": 8,
          "read_count": 23.0,
          "title": "Testing Weak-lensing Maps with Redshift Surveys: A Subaru Field"
        },
        "2014CTM....18..532K": {
          "authors": [
            "Kurtz, M",
            "Regele, J"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Acoustic timescale characterisation of a one-dimensional model hot spot"
        },
        "2014CTM....18..711K": {
          "authors": [
            "Kurtz, M",
            "Regele, J"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Acoustic timescale characterisation of symmetric and asymmetric multidimensional hot spots"
        },
        "2014PhDT.......168K": {
          "authors": [
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 0,
          "title": "Acoustic timescale characterization of hot spot detonability"
        },
        "2014bbmb.book..243K": {
          "authors": [
            "Henneken, E",
            "Kurtz, M"
          ],
          "citation_count": 0,
          "read_count": 23.0,
          "title": "Finding and Recommending Scholarly Articles"
        }
      },
      "link_data": [
        [
          2,
          3,
          26.999999999999968
        ],
        [
          2,
          45,
          26.999999999999968
        ],
        [
          2,
          28,
          10.189655172413781
        ],
        [
          3,
          9,
          10.189655172413781
        ],
        [
          3,
          44,
          26.999999999999968
        ],
        [
          3,
          42,
          26.999999999999968
        ],
        [
          3,
          30,
          30.36206896551721
        ],
        [
          6,
          38,
          17.810344827586185
        ],
        [
          9,
          45,
          10.189655172413781
        ],
        [
          11,
          30,
          16.465517241379292
        ],
        [
          15,
          38,
          29.689655172413758
        ],
        [
          25,
          38,
          16.465517241379292
        ],
        [
          28,
          42,
          10.189655172413781
        ],
        [
          28,
          44,
          10.189655172413781
        ],
        [
          28,
          30,
          27.224137931034452
        ],
        [
          30,
          45,
          30.36206896551721
        ],
        [
          32,
          38,
          29.689655172413758
        ],
        [
          38,
          43,
          16.465517241379292
        ],
        [
          42,
          45,
          26.999999999999968
        ],
        [
          44,
          45,
          26.999999999999968
        ]
      ],
      "root": {
        "children": [
          {
            "children": [
              {
                "citation_count": 134,
                "name": "Murray, S",
                "numberName": 3,
                "papers": [
                  "2000A&AS..143...41K",
                  "1993ASPC...52..132K",
                  "2005IPM....41.1395K",
                  "2005JASIS..56...36K",
                  "2005JASIS..56..111K",
                  "2002SPIE.4847..238K",
                  "2003AAS...203.2005K",
                  "2003lisa.conf..223K",
                  "2006ASPC..351..653K",
                  "1997AAS...191.1701K",
                  "2007AAS...211.4730K",
                  "1996AAS...189.0607K",
                  "2009astro2010P..28K",
                  "2002AAS...201.0904K",
                  "1993AAS...182.0306K",
                  "1998AAS...193.1311K",
                  "1998ASPC..145..478K",
                  "1997DPS....29.2705K",
                  "2007APS..MARU20009K",
                  "1999Dlib....5.....K",
                  "1995AAS...186.0202K",
                  "1996dkcw.proc..123K",
                  "2005AAS...207.3405K",
                  "2006AAS...20921809K",
                  "1999AAS...194.4407K",
                  "2007ASPC..377...23K"
                ],
                "read_count": 136.0,
                "size": 21.74795149131432
              },
              {
                "citation_count": 9,
                "name": "Martimbeau, N",
                "numberName": 7,
                "papers": [
                  "2005JASIS..56..111K"
                ],
                "read_count": 10.0,
                "size": 5.055446301758987
              },
              {
                "citation_count": 3,
                "name": "Di Milia, G",
                "numberName": 8,
                "papers": [
                  "2010ASPC..434..155K"
                ],
                "read_count": 8.0,
                "size": 5.354856331257511
              },
              {
                "citation_count": 1,
                "name": "Rosvall, M",
                "numberName": 10,
                "papers": [
                  "2007AAS...211.4730K"
                ],
                "read_count": 0,
                "size": 5.0
              },
              {
                "citation_count": 118,
                "name": "Accomazzi, A",
                "numberName": 11,
                "papers": [
                  "2000A&AS..143...41K",
                  "2005IPM....41.1395K",
                  "2005JASIS..56...36K",
                  "2005JASIS..56..111K",
                  "2002SPIE.4847..238K",
                  "2003AAS...203.2005K",
                  "2003lisa.conf..223K",
                  "2010ASPC..434..155K",
                  "2006ASPC..351..653K",
                  "1997AAS...191.1701K",
                  "2007AAS...211.4730K",
                  "1996AAS...189.0607K",
                  "2009astro2010P..28K",
                  "2002AAS...201.0904K",
                  "1998AAS...193.1311K",
                  "1998ASPC..145..478K",
                  "1997DPS....29.2705K",
                  "2007APS..MARU20009K",
                  "1999Dlib....5.....K",
                  "1995AAS...186.0202K",
                  "1996dkcw.proc..123K",
                  "2005AAS...207.3405K",
                  "2006AAS...20921809K",
                  "1999AAS...194.4407K",
                  "2007ASPC..377...23K"
                ],
                "read_count": 141.0,
                "size": 21.548344804981973
              },
              {
                "citation_count": 1,
                "name": "Bergstrom, C",
                "numberName": 14,
                "papers": [
                  "2007AAS...211.4730K"
                ],
                "read_count": 0,
                "size": 5.0
              },
              {
                "citation_count": 0,
                "name": "Green, D",
                "numberName": 16,
                "papers": [
                  "1997DPS....29.2705K"
                ],
                "read_count": 2.0,
                "size": 5.055446301758987
              },
              {
                "citation_count": 8,
                "name": "Bohlen, E",
                "numberName": 18,
                "papers": [
                  "2003AAS...203.2005K",
                  "2003lisa.conf..223K",
                  "2007AAS...211.4730K",
                  "2007APS..MARU20009K",
                  "2006AAS...20921809K",
                  "2007ASPC..377...23K"
                ],
                "read_count": 20.0,
                "size": 7.566371681415928
              },
              {
                "citation_count": 0,
                "name": "Marsden, B",
                "numberName": 26,
                "papers": [
                  "1997DPS....29.2705K"
                ],
                "read_count": 2.0,
                "size": 5.055446301758987
              },
              {
                "citation_count": 28,
                "name": "Henneken, E",
                "numberName": 27,
                "papers": [
                  "2005IPM....41.1395K",
                  "2003AAS...203.2005K",
                  "2007arXiv0709.0896K",
                  "2010ASPC..434..155K",
                  "2006ASPC..351..653K",
                  "2007AAS...211.4730K",
                  "2007APS..MARU20009K",
                  "2005AAS...207.3405K",
                  "2006AAS...20921809K",
                  "2014bbmb.book..243K",
                  "2007ASPC..377...23K"
                ],
                "read_count": 118.0,
                "size": 13.592592592592592
              },
              {
                "citation_count": 136,
                "name": "Grant, C",
                "numberName": 28,
                "papers": [
                  "2000A&AS..143...41K",
                  "1993ASPC...52..132K",
                  "2005IPM....41.1395K",
                  "2005JASIS..56...36K",
                  "2005JASIS..56..111K",
                  "2002SPIE.4847..238K",
                  "2003AAS...203.2005K",
                  "2003lisa.conf..223K",
                  "2010ASPC..434..155K",
                  "2006ASPC..351..653K",
                  "1997AAS...191.1701K",
                  "2007AAS...211.4730K",
                  "1996AAS...189.0607K",
                  "2002AAS...201.0904K",
                  "1998AAS...193.1311K",
                  "1998ASPC..145..478K",
                  "1997DPS....29.2705K",
                  "2007APS..MARU20009K",
                  "1999Dlib....5.....K",
                  "1995AAS...186.0202K",
                  "1996dkcw.proc..123K",
                  "2005AAS...207.3405K",
                  "2006AAS...20921809K",
                  "1999AAS...194.4407K",
                  "2007ASPC..377...23K"
                ],
                "read_count": 131.0,
                "size": 20.71665027859718
              },
              {
                "citation_count": 9,
                "name": "Elwell, B",
                "numberName": 29,
                "papers": [
                  "2005JASIS..56..111K"
                ],
                "read_count": 10.0,
                "size": 5.055446301758987
              },
              {
                "citation_count": 36,
                "name": "Demleitner, M",
                "numberName": 39,
                "papers": [
                  "2005IPM....41.1395K",
                  "2005JASIS..56...36K",
                  "2005JASIS..56..111K",
                  "1999Dlib....5.....K",
                  "1999AAS...194.4407K"
                ],
                "read_count": 63.0,
                "size": 7.621817983174914
              },
              {
                "citation_count": 0,
                "name": "Williams, G",
                "numberName": 40,
                "papers": [
                  "1997DPS....29.2705K"
                ],
                "read_count": 2.0,
                "size": 5.055446301758987
              },
              {
                "citation_count": 8,
                "name": "Thompson, D",
                "numberName": 41,
                "papers": [
                  "2003AAS...203.2005K",
                  "2003lisa.conf..223K",
                  "2007AAS...211.4730K",
                  "2007APS..MARU20009K",
                  "2005AAS...207.3405K",
                  "2006AAS...20921809K",
                  "2007ASPC..377...23K"
                ],
                "read_count": 22.0,
                "size": 8.136676499508358
              },
              {
                "citation_count": 136,
                "name": "Eichhorn, G",
                "numberName": 45,
                "papers": [
                  "2000A&AS..143...41K",
                  "1993ASPC...52..132K",
                  "2005IPM....41.1395K",
                  "2005JASIS..56...36K",
                  "2005JASIS..56..111K",
                  "2002SPIE.4847..238K",
                  "1998ASPC..153..293K",
                  "2003AAS...203.2005K",
                  "2003lisa.conf..223K",
                  "2006ASPC..351..653K",
                  "1997AAS...191.1701K",
                  "1996AAS...189.0607K",
                  "2001SPIE.4477..186K",
                  "2002AAS...201.0904K",
                  "1993AAS...182.0306K",
                  "1998AAS...193.1311K",
                  "1998ASPC..145..478K",
                  "1997DPS....29.2705K",
                  "2007APS..MARU20009K",
                  "1999Dlib....5.....K",
                  "1995AAS...186.0202K",
                  "1996dkcw.proc..123K",
                  "2005AAS...207.3405K",
                  "2006AAS...20921809K",
                  "1999AAS...194.4407K",
                  "2007ASPC..377...23K"
                ],
                "read_count": 127.0,
                "size": 23.96580356167377
              }
            ],
            "name": 1
          },
          {
            "children": [
              {
                "citation_count": 109,
                "name": "Torres, G",
                "numberName": 4,
                "papers": [
                  "1992ASPC...25..432K"
                ],
                "read_count": 3.0,
                "size": 5.12673440402054
              },
              {
                "citation_count": 122,
                "name": "Wyatt, W",
                "numberName": 6,
                "papers": [
                  "1992ASPC...25..432K",
                  "2007AJ....134.1360K"
                ],
                "read_count": 12.0,
                "size": 5.92516114934994
              },
              {
                "citation_count": 21,
                "name": "Dell'Antonio, I",
                "numberName": 15,
                "papers": [
                  "2007AJ....134.1360K",
                  "2012ApJ...750..168K"
                ],
                "read_count": 32.0,
                "size": 6.020211952365345
              },
              {
                "citation_count": 109,
                "name": "Kriss, G",
                "numberName": 17,
                "papers": [
                  "1992ASPC...25..432K"
                ],
                "read_count": 3.0,
                "size": 5.12673440402054
              },
              {
                "citation_count": 404,
                "name": "Mink, D",
                "numberName": 19,
                "papers": [
                  "1998PASP..110..934K",
                  "1992ASPC...25..432K",
                  "2000ApJ...533L.183K",
                  "1999ascl.soft12003K"
                ],
                "read_count": 55.0,
                "size": 11.114934993991039
              },
              {
                "citation_count": 8,
                "name": "Utsumi, Y",
                "numberName": 25,
                "papers": [
                  "2012ApJ...750..168K"
                ],
                "read_count": 23.0,
                "size": 5.2217852070359445
              },
              {
                "citation_count": 130,
                "name": "Fabricant, D",
                "numberName": 32,
                "papers": [
                  "1992ASPC...25..432K",
                  "2007AJ....134.1360K",
                  "2012ApJ...750..168K"
                ],
                "read_count": 35.0,
                "size": 6.590516770457773
              },
              {
                "citation_count": 109,
                "name": "Tonry, J",
                "numberName": 34,
                "papers": [
                  "1992ASPC...25..432K"
                ],
                "read_count": 3.0,
                "size": 5.12673440402054
              },
              {
                "citation_count": 8,
                "name": "Miyazaki, S",
                "numberName": 43,
                "papers": [
                  "2012ApJ...750..168K"
                ],
                "read_count": 23.0,
                "size": 5.2217852070359445
              }
            ],
            "name": 2
          },
          {
            "children": [
              {
                "citation_count": 28,
                "name": "Huchra, J",
                "numberName": 0,
                "papers": [
                  "1985AJ.....90.1665K",
                  "1984BAAS...16..457K"
                ],
                "read_count": 18.0,
                "size": 5.554463017589861
              },
              {
                "citation_count": 28,
                "name": "Stauffer, J",
                "numberName": 5,
                "papers": [
                  "1985AJ.....90.1665K",
                  "1984BAAS...16..457K"
                ],
                "read_count": 18.0,
                "size": 5.554463017589861
              },
              {
                "citation_count": 28,
                "name": "Maccacaro, T",
                "numberName": 12,
                "papers": [
                  "1985AJ.....90.1665K",
                  "1984BAAS...16..457K"
                ],
                "read_count": 18.0,
                "size": 5.554463017589861
              },
              {
                "citation_count": 28,
                "name": "Beers, T",
                "numberName": 20,
                "papers": [
                  "1985AJ.....90.1665K",
                  "1984BAAS...16..457K"
                ],
                "read_count": 18.0,
                "size": 5.554463017589861
              },
              {
                "citation_count": 28,
                "name": "Schild, R",
                "numberName": 21,
                "papers": [
                  "1985AJ.....90.1665K",
                  "1984BAAS...16..457K"
                ],
                "read_count": 18.0,
                "size": 5.554463017589861
              },
              {
                "citation_count": 28,
                "name": "Gioia, I",
                "numberName": 31,
                "papers": [
                  "1985AJ.....90.1665K",
                  "1984BAAS...16..457K"
                ],
                "read_count": 18.0,
                "size": 5.554463017589861
              },
              {
                "citation_count": 49,
                "name": "Geller, M",
                "numberName": 38,
                "papers": [
                  "1985AJ.....90.1665K",
                  "2007AJ....134.1360K",
                  "2012ApJ...750..168K",
                  "1984BAAS...16..457K"
                ],
                "read_count": 50.0,
                "size": 7.018245384027095
              }
            ],
            "name": 3
          },
          {
            "children": [
              {
                "citation_count": 19,
                "name": "Stoner, J",
                "numberName": 2,
                "papers": [
                  "1993ASPC...52..132K",
                  "1993AAS...182.0306K"
                ],
                "read_count": 3.0,
                "size": 5.554463017589861
              },
              {
                "citation_count": 0,
                "name": "Stern, C",
                "numberName": 9,
                "papers": [
                  "1993AAS...182.0306K"
                ],
                "read_count": 0,
                "size": 5.055446301758987
              },
              {
                "citation_count": 79,
                "name": "Watson, J",
                "numberName": 30,
                "papers": [
                  "2000A&AS..143...41K",
                  "1993ASPC...52..132K",
                  "1993AAS...182.0306K"
                ],
                "read_count": 21.0,
                "size": 6.219818638697695
              },
              {
                "citation_count": 1,
                "name": "Mussio, P",
                "numberName": 35,
                "papers": [
                  "1990PaReL..11..507K"
                ],
                "read_count": 0,
                "size": 5.887140828143778
              },
              {
                "citation_count": 20,
                "name": "Ossorio, P",
                "numberName": 42,
                "papers": [
                  "1993ASPC...52..132K",
                  "1990PaReL..11..507K",
                  "1993AAS...182.0306K"
                ],
                "read_count": 3.0,
                "size": 6.885174259805527
              },
              {
                "citation_count": 19,
                "name": "Karakashian, T",
                "numberName": 44,
                "papers": [
                  "1993ASPC...52..132K",
                  "1993AAS...182.0306K"
                ],
                "read_count": 3.0,
                "size": 5.554463017589861
              }
            ],
            "name": 4
          },
          {
            "children": [
              {
                "citation_count": 0,
                "name": "Jannaway, W",
                "numberName": 13,
                "papers": [
                  "1981romi.work.....K"
                ],
                "read_count": 0,
                "size": 5.887140828143778
              },
              {
                "citation_count": 0,
                "name": "Stone, G",
                "numberName": 37,
                "papers": [
                  "1981romi.work.....K"
                ],
                "read_count": 0,
                "size": 5.887140828143778
              }
            ],
            "name": 5
          },
          {
            "children": [
              {
                "citation_count": 0,
                "name": "Falco, E",
                "numberName": 23,
                "papers": [
                  "1989daa..conf..155K",
                  "1990ebua.conf..363K"
                ],
                "read_count": 0,
                "size": 8.548563312575112
              }
            ],
            "name": 6
          },
          {
            "children": [
              {
                "citation_count": 0,
                "name": "Regele, J",
                "numberName": 46,
                "papers": [
                  "2014CTM....18..532K",
                  "2014CTM....18..711K"
                ],
                "read_count": 0,
                "size": 8.548563312575112
              }
            ],
            "name": 7
          },
          {
            "children": [
              {
                "citation_count": 0,
                "name": "Brody, T",
                "numberName": 1,
                "papers": [
                  "2006oaks.book...45K"
                ],
                "read_count": 0,
                "size": 6.552496449251612
              }
            ],
            "name": 8
          },
          {
            "children": [
              {
                "citation_count": 4,
                "name": "LaSala, J",
                "numberName": 22,
                "papers": [
                  "1991opos.conf..133K"
                ],
                "read_count": 0,
                "size": 6.552496449251612
              }
            ],
            "name": 9
          },
          {
            "children": [
              {
                "citation_count": 8,
                "name": "Bollen, J",
                "numberName": 33,
                "papers": [
                  "2010ARIST..44....3K"
                ],
                "read_count": 43.0,
                "size": 6.552496449251612
              }
            ],
            "name": 10
          },
          {
            "children": [
              {
                "citation_count": 0,
                "name": "Holden, B",
                "numberName": 36,
                "papers": [
                  "2001JChEd..78.1122K"
                ],
                "read_count": 0,
                "size": 6.552496449251612
              }
            ],
            "name": 11
          }
        ],
        "name": [
          {
            "delete": true,
            "nodeName": "Kurtz, M",
            "nodeWeight": 150.0
          }
        ]
      }
    }, "msg": {"numFound": 1500, "rows": 1000, "start": 0}};

    testDataEmpty = {"data": {"fullGraph": {"nodes": [], "links": []}}, "msg": {numFound: 0, rows:0}};

    afterEach(function () {
      $("#test").empty();
    });

//    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
//    $("#test").append(networkWidget.view.el);
//    //this should show a single, simple graph
//    networkWidget.processResponse(new JsonResponse(testDataLarge));


  it("communicates with pubsub to get current query info and to request network data", function () {

    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
    $("#test").append(networkWidget.view.el);

    var minsub = new MinimalPubsub({verbose: false});
    var q = new ApiQuery({q: 'star'});

    networkWidget.activate(minsub.beehive.getHardenedInstance());
    minsub.publish(minsub.START_SEARCH, q);

    expect(networkWidget.getCurrentQuery().get("q")).to.eql(q.get("q"));
    networkWidget.pubsub.publish = sinon.stub();

    networkWidget.onShow();
    expect(networkWidget.pubsub.publish.calledOnce).to.be.true;
    expect(networkWidget.pubsub.publish.args[0][1].url()).to.eql('author-network?q=star&rows=300');
  });

  it("renders a pie/sunburst chart if the data is large enough", function () {

    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
    $("#test").append(networkWidget.view.el);

    //this should show not enough data template
    networkWidget.processResponse(new JsonResponse(testDataSmall));
    expect($("#test").find(".network-container").text().trim()).to.eql("There wasn't enough data returned by your search to form a visualization.")

    //this should also show not enough data template
    networkWidget.processResponse(new JsonResponse(testDataEmpty));
    expect($("#test").find(".network-container").text().trim()).to.eql("There wasn't enough data returned by your search to form a visualization.")

    //this should show a donut chart with hidden link layer, representing the data
    networkWidget.processResponse(new JsonResponse(testDataLarge));

    expect($("#test").find(".link").length).to.eql(testDataLarge.data.link_data.length);

    var totalChildren = [];
    _.each(testDataLarge.data.root.children, function (group) {
      var children = group.children;
      Array.prototype.push.apply(totalChildren, children);
    });
    expect($("#test").find(".author-node").length).to.eql(totalChildren.length);

  });

  it("allows you to click to view more data about a node or a group", function () {

    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
    networkWidget.processResponse(new JsonResponse(testDataLarge));
    $("#test").append(networkWidget.view.el);

    // this would be the result of a click on group 1
    networkWidget.view.graphView.model.set("selectedEntity", d3.select(".node-path")[0][0]);

    //paper description div should contain an entry for every relevant paper from group 1
    var children = testDataLarge.data.root.children[0].children;
    var bibcodes = [];
    _.each(children, function (c) {
      Array.prototype.push.apply(bibcodes, c.papers)
    });
    bibcodes = _.uniq(bibcodes);
    expect($("#test").find(".paper-description li").length).to.eql(bibcodes.length);

    //testing with a focus on accomazzi
    var accomazziNode = d3.selectAll(".node-path").filter(function (d) {
      return d.name == "Accomazzi, A"
    })[0][0];
    networkWidget.view.graphView.model.set("selectedEntity", accomazziNode);
    expect($("#test").find(".paper-description li").length).to.eql(25);

    //and this should remove the center div
    networkWidget.view.graphView.model.set("selectedEntity", undefined);

    expect($("#test").find(".paper-description").length).to.eql(0);

  });

  it("has a filter capability that allows you to add or remove groups or individual nodes, and submit as a filter", function () {

    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
    networkWidget.processResponse(new JsonResponse(testDataLarge));
    $("#test").append(networkWidget.view.el);

    // this would be the result of a click on group 1
    networkWidget.view.graphView.model.set("selectedEntity", d3.select(".node-path")[0][0]);
    $("#test").find("button.filter-add").click();
    var accomazziNode = d3.selectAll(".node-path").filter(function (d) {
      return d.name == "Accomazzi, A"
    })[0][0];
    networkWidget.view.graphView.model.set("selectedEntity", accomazziNode);
    $("#test").find("button.filter-add").click();

    expect($("#test").find(".s-filter-names-container").text().trim()).to.eql('at least one member of Group 1  OR \n        \n            Accomazzi, A');

    $("#test").find("button.filter-remove").click();

    expect($("#test").find(".s-filter-names-container").text().trim()).to.eql('at least one member of Group 1');

    //putting accomazzi back
    $("#test").find("button.filter-add").click();

    var minsub = new MinimalPubsub({verbose: false});

    networkWidget.setCurrentQuery(new ApiQuery({q: "original search"}));
    networkWidget.activate(minsub.beehive.getHardenedInstance());
    networkWidget.pubsub.publish = sinon.stub();

    $("#test").find(".apply-filter").click();
    expect(networkWidget.pubsub.publish.called).to.be.true;
    expect(networkWidget.pubsub.publish.args[0][0]).to.eql("[PubSub]-New-Query");
    expect(networkWidget.pubsub.publish.args[0][1].get("fq")[0]).to.eql('(author:("Accomazzi, A" OR ("Accomazzi, A" OR "Bergstrom, C" OR "Bohlen, E" OR "Demleitner, M" OR "Di Milia, G" OR "Eichhorn, G" OR "Elwell, B" OR "Grant, C" OR "Green, D" OR "Henneken, E" OR "Marsden, B" OR "Martimbeau, N" OR "Murray, S" OR "Rosvall, M" OR "Thompson, D" OR "Williams, G")))');

  });

  it("allows you to toggle between three arc sizing modes, and correctly calculates the arcs' size based on different variables", function () {

    //checking ratios of the first and second groups

    var group1 = testDataLarge.data.root.children[0];
    var group2 = testDataLarge.data.root.children[1];
    var size1 = 0, size2 = 0;
    _.each(group1.children, function (c) {
      size1 += c.size;
    });
    _.each(group2.children, function (c) {
      size2 += c.size;
    });
    var sizeRatio = size1 / size2;

    var citation1 = 0, citation2 = 0;
    _.each(group1.children, function (c) {
      citation1 += c.citation_count;
    });
    _.each(group2.children, function (c) {
      citation2 += c.citation_count;
    });
    var citationRatio = citation1 / citation2;

    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
    networkWidget.processResponse(new JsonResponse(testDataLarge));
    $("#test").append(networkWidget.view.el);

    var group1Path = $("#test").find("g.node-containers:not(.author-node)").eq(1).find("path").eq(0)[0];
    var group2Path = $("#test").find("g.node-containers:not(.author-node)").eq(2).find("path").eq(0)[0];

    expect(group1Path.__data__.value / group2Path.__data__.value).to.eql(sizeRatio);

    $("#test input[value=citation_count]").click();

    expect(group1Path.__data__.value / group2Path.__data__.value).to.eql(citationRatio);

  });

  it("has a method to completely remove the graphView", function () {
    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
    $("#test").append(networkWidget.view.el);

    var minsub = new MinimalPubsub({verbose: false});
    networkWidget.processResponse(new JsonResponse(testDataLarge));
    expect(networkWidget.resetWidget).to.be.instanceof(Function);

    expect(networkWidget.view.graphView.$el.children().length).not.be.eql(0);
    expect(_.keys(networkWidget.view.graphView._listeners).length).not.be.eql(0);
    networkWidget.resetWidget();

    expect(networkWidget.view.graphView.$el.children().length).to.be.eql(0);
    expect(networkWidget.model.get('data')).to.be.undefined;
    expect(_.keys(networkWidget.view.graphView._listeners).length).to.be.eql(0);
  });

  it("has a help popover that is accessible by hovering over the question mark icon", function () {

    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
    $("#test").append(networkWidget.view.el);

    var minsub = new MinimalPubsub({verbose: false});
    networkWidget.processResponse(new JsonResponse(testDataLarge));

    //it uses the standard bootstrap popover, you just need to make sure the data-content attribute is correct
    expect(networkWidget.view.$(".icon-help").attr("data-content")).to.eql("test");
    expect($("div.popover").length).to.eql(0);

    networkWidget.view.$(".icon-help").mouseover();
    expect($("div.popover").length).to.eql(1);
  });

  it("should allow the user to request a different number of documents", function (done) {


    var minsub = new (MinimalPubsub.extend({
      request: function (apiRequest) {
        this.counter = this.counter || 0;
        if (this.counter == 0) {
          this.counter++;
          var data = testDataLarge;
          data.msg.rows = 1000
          return data;
        }
        else if (this.counter == 1) {
          var data = testDataLarge;
          data.msg.rows = 400
          return data;

        }
      }
    }))({verbose: false});

    var networkWidget = new NetworkWidget({networkType: "author", endpoint: "author-network", helpText: "test"});
    $("#test").append(networkWidget.view.el);

    networkWidget.activate(minsub.beehive.getHardenedInstance());

    //provide widget with current query
    minsub.publish(minsub.START_SEARCH, new ApiQuery({q: "star"}));

    //trigger show event, should prompt dispatchRequest
    networkWidget.onShow();

    expect($("#test").find(".network-metadata").text().trim()).to.eql('Currently viewing data for 1000 papers.\n\nChange to first  papers (max is 1000).\n Submit');
    sinon.spy(networkWidget.pubsub, "publish");
    $("#test").find(".network-metadata input").val("400");
    $("#test").find(".network-metadata button.submit-rows").trigger("click");

    setTimeout(function () {
        expect(networkWidget.pubsub.publish.args[0][0]).to.eql(minsub.EXECUTE_REQUEST);
        expect(networkWidget.pubsub.publish.args[0][1].get("query").toJSON().rows).to.eql([400]);
        expect($("#test").find(".network-metadata").text().trim()).to.eql('Currently viewing data for 400 papers.\n\nChange to first  papers (max is 1000).\n Submit');
        done();
    }, 800);

  });

});

});