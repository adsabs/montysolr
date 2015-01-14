define([
  "js/wraps/paper_network",
  "js/components/json_response",
  'js/components/api_query',
  'js/bugutils/minimal_pubsub'
], function(
  PaperNetwork,
  JsonResponse,
  ApiQuery,
  MinPubSub
  ){

  describe("Paper Network UI Widget (paper_network_widget.spec.js)", function(){

    var testDataBig = {"data": {
      "fullGraph": {
        "directed": false,
        "graph": [],
        "links": [
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 0,
            "target": 116,
            "weight": 1
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1997ApJ...481..633D",
              "2001MNRAS.328.1039C",
              "1933AcHPh...6..110Z",
              "1989ApJS...70....1A",
              "1987MNRAS.227....1K",
              "2001ApJ...547..609E",
              "1986ApJ...302L...1D",
              "1989AJ.....98..755R",
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "1972ApJ...176....1G"
            ],
            "source": 0,
            "target": 117,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998ApJ...501..509V",
              "1982ApJ...262....9S",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "1989AJ.....98..755R",
              "1999MNRAS.303..188K",
              "1987MNRAS.227....1K",
              "1982AJ.....87..945K",
              "1997ApJ...490..493N"
            ],
            "source": 0,
            "target": 3,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "2004ApJ...610..745L",
              "1999ApJ...517L..23G",
              "2001ApJ...548L.139D",
              "2002AJ....124.1266R",
              "1999MNRAS.309..610D",
              "2000AJ....120.2338R",
              "1996ApJ...462...32C",
              "2003ApJ...585..205B",
              "2000AJ....119...44K",
              "1997ApJ...481..633D",
              "1999MNRAS.303..188K",
              "2003ApJ...592..819B",
              "1997ApJ...490..493N",
              "2002ApJ...567..716R",
              "1933AcHPh...6..110Z",
              "2000AJ....119.2038V",
              "1999MNRAS.307..529K",
              "1987MNRAS.227....1K",
              "2001ApJ...555..558R",
              "1997ApJ...478..462C",
              "1989AJ.....98..755R",
              "2002AJ....123..485S",
              "2000AJ....120..523R",
              "1998PASP..110..934K",
              "2003AJ....126.2152R"
            ],
            "source": 0,
            "target": 123,
            "weight": 18
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 0,
            "target": 124,
            "weight": 1
          },
          {
            "overlap": [
              "1933AcHPh...6..110Z",
              "1986ApJ...302L...1D"
            ],
            "source": 0,
            "target": 12,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998astro.ph..9268K"
            ],
            "source": 0,
            "target": 13,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1989AJ.....98..755R"
            ],
            "source": 0,
            "target": 15,
            "weight": 2
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 0,
            "target": 16,
            "weight": 1
          },
          {
            "overlap": [
              "1999MNRAS.303..188K",
              "2002AJ....124.1266R",
              "1997ApJ...478..462C",
              "1998PASJ...50..187F",
              "1998ApJ...492...45S",
              "1999ApJ...517L..23G",
              "2000AJ....119...44K",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "2000AJ....119.2038V",
              "1998astro.ph..9268K",
              "1982AJ.....87..945K",
              "1996MNRAS.281..799E",
              "1989AJ.....98..755R",
              "1998ApJ...501..509V",
              "1988AJ.....96.1775O",
              "2002AJ....123..485S",
              "1990ApJ...356..359H",
              "1989ApJS...70....1A",
              "1998PASP..110...79F",
              "1987MNRAS.227....1K",
              "2001ApJ...561L..41R",
              "1986ApJ...302L...1D",
              "2001ApJ...555..558R",
              "2001ApJ...548L.139D",
              "1995ApJ...445..578D",
              "2002ApJ...567..716R",
              "1999MNRAS.309..610D",
              "1999ApJ...518...69M",
              "2000AJ....120..523R",
              "1997ApJ...490..493N",
              "1999ApJ...517..627M",
              "2000AJ....120.2338R",
              "2001AJ....122.1289T",
              "1998PASP..110..934K",
              "2001MNRAS.328.1039C",
              "2001ApJ...547..609E",
              "1999MNRAS.307..529K",
              "2003ApJ...585..205B",
              "2001A&A...368..749F"
            ],
            "source": 0,
            "target": 119,
            "weight": 35
          },
          {
            "overlap": [
              "2001ApJ...556..601W",
              "1998PASP..110..934K",
              "2002ApJ...568..141G"
            ],
            "source": 0,
            "target": 9,
            "weight": 4
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1997ApJ...481..633D",
              "1997ApJ...490..493N",
              "1987MNRAS.227....1K",
              "1996MNRAS.281..799E",
              "1989AJ.....98..755R",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 20,
            "weight": 7
          },
          {
            "overlap": [
              "2003ApJS..148..175S"
            ],
            "source": 0,
            "target": 21,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 26,
            "weight": 1
          },
          {
            "overlap": [
              "2003MNRAS.344.1000B",
              "1998PASP..110..934K",
              "2003MNRAS.341...33K"
            ],
            "source": 0,
            "target": 29,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 35,
            "weight": 1
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1976ApJ...203..297S"
            ],
            "source": 0,
            "target": 36,
            "weight": 3
          },
          {
            "overlap": [
              "1982ApJ...262....9S",
              "1987MNRAS.227....1K",
              "1982AJ.....87..945K",
              "1976ApJ...203..297S",
              "1972ApJ...176....1G"
            ],
            "source": 0,
            "target": 38,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2003AJ....126.2152R"
            ],
            "source": 0,
            "target": 39,
            "weight": 2
          },
          {
            "overlap": [
              "2004ApJ...610..745L",
              "1997ilsn.proc...25S",
              "2000MNRAS.312..557L",
              "1990ApJ...356..359H",
              "1998PASP..110...79F",
              "1997A&AS..122..399P",
              "2001ApJ...561L..41R",
              "2002AJ....124.1266R",
              "2003ApJ...584..210G",
              "1999MNRAS.309..610D",
              "2001ApJ...560..566K",
              "2003ApJ...591...53T",
              "2000AJ....119.2498J",
              "2000AJ....120.3340N",
              "2001MNRAS.328.1039C",
              "1997ApJ...490..493N",
              "2003ApJS..149..289B",
              "1989ApJS...70....1A",
              "2004MNRAS.348.1355B",
              "1982AJ.....87..945K",
              "2001ApJ...555..558R",
              "2000ApJ...540..113B",
              "2001MNRAS.326..255C",
              "2001ApJ...547..609E",
              "1998PASJ...50..187F",
              "2002MNRAS.334..673L",
              "2002A&A...382..495A",
              "1996MNRAS.281..799E",
              "1989AJ.....98..755R",
              "2002AJ....123..485S",
              "1998PASP..110..934K",
              "2003AJ....126.2152R"
            ],
            "source": 0,
            "target": 40,
            "weight": 27
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 46,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 47,
            "weight": 1
          },
          {
            "overlap": [
              "1989ApJS...70....1A"
            ],
            "source": 0,
            "target": 48,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 52,
            "weight": 3
          },
          {
            "overlap": [
              "2003MNRAS.344.1000B",
              "1996A&AS..117..393B",
              "2003ApJS..148..175S",
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "2003ApJ...582..668B"
            ],
            "source": 0,
            "target": 53,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "2002AJ....123..485S"
            ],
            "source": 0,
            "target": 54,
            "weight": 2
          },
          {
            "overlap": [
              "1997A&AS..122..399P",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 55,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 0,
            "target": 56,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1999MNRAS.303..188K",
              "1990ApJ...356..359H",
              "1998PASP..110...79F",
              "1997ApJ...490..493N"
            ],
            "source": 0,
            "target": 57,
            "weight": 5
          },
          {
            "overlap": [
              "2003MNRAS.344.1000B",
              "2003ApJS..149..289B",
              "2003PASP..115..763C"
            ],
            "source": 0,
            "target": 60,
            "weight": 3
          },
          {
            "overlap": [
              "2003ApJS..148..175S"
            ],
            "source": 0,
            "target": 63,
            "weight": 1
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1982AJ.....87..945K"
            ],
            "source": 0,
            "target": 66,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1976ApJ...203..297S"
            ],
            "source": 0,
            "target": 68,
            "weight": 3
          },
          {
            "overlap": [
              "1988AJ.....96.1775O",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "2004ApJ...610..745L",
              "1989ApJS...70....1A",
              "1996MNRAS.281..799E",
              "1998PASP..110..934K",
              "2003AJ....126.2152R"
            ],
            "source": 0,
            "target": 72,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 78,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 0,
            "target": 79,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 80,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 0,
            "target": 82,
            "weight": 1
          },
          {
            "overlap": [
              "1998ApJ...492...45S",
              "1999ApJ...517L..23G",
              "1997ilsn.proc...25S",
              "2001ApJ...556L..17W",
              "1990ApJ...356..359H",
              "1998PASP..110...79F",
              "2001ApJ...548L.139D",
              "1999MNRAS.309..610D",
              "2000AJ....120.2338R",
              "2001AJ....122.1289T",
              "2001ApJ...560..566K",
              "1997ApJ...481..633D",
              "1998astro.ph..9268K",
              "2000AJ....119.2498J",
              "2000A&A...353..479A",
              "2000AJ....120.3340N",
              "1997ApJ...490..493N",
              "2000MNRAS.311..793B",
              "1933AcHPh...6..110Z",
              "1998MNRAS.293..315M",
              "2000ApJ...541....1B",
              "1982AJ.....87..945K",
              "2001ApJ...557..117B",
              "2000ApJ...540..113B",
              "1998ApJ...503L..45D",
              "1999ApJ...520..437K",
              "1997ApJ...475..421E",
              "1996A&A...312..397G",
              "2001MNRAS.326..255C",
              "1997ApJ...478..462C",
              "2000PASP..112.1008J",
              "2000AJ....120..523R",
              "2000ApJ...530...62G"
            ],
            "source": 0,
            "target": 84,
            "weight": 41
          },
          {
            "overlap": [
              "1976ApJ...203..297S",
              "2003ApJ...591..764C",
              "1998PASP..110..934K",
              "2003MNRAS.341...33K"
            ],
            "source": 0,
            "target": 85,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 0,
            "target": 86,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2003PASP..115..763C",
              "2001MNRAS.328.1039C",
              "2003MNRAS.344.1000B",
              "1998PASP..110..934K",
              "2003ApJS..149..289B",
              "2003MNRAS.341...33K"
            ],
            "source": 0,
            "target": 90,
            "weight": 6
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 0,
            "target": 91,
            "weight": 2
          },
          {
            "overlap": [
              "2003MNRAS.344.1000B",
              "1996A&AS..117..393B",
              "1976ApJ...203..297S",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 92,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 95,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S",
              "2002AJ....123..485S",
              "2003MNRAS.341...33K"
            ],
            "source": 0,
            "target": 96,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 0,
            "target": 97,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1976ApJ...203..297S",
              "1998PASP..110...79F",
              "1997A&AS..122..399P",
              "1998PASP..110..934K",
              "1995AJ....110.1507B",
              "1998MNRAS.294..193T"
            ],
            "source": 0,
            "target": 98,
            "weight": 7
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "2000AJ....119.2498J",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 101,
            "weight": 5
          },
          {
            "overlap": [
              "2003MNRAS.344.1000B",
              "2001MNRAS.328.1039C",
              "1986ApJ...302L...1D",
              "2003MNRAS.341...33K"
            ],
            "source": 0,
            "target": 103,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 105,
            "weight": 1
          },
          {
            "overlap": [
              "1994ApJS...95..107W"
            ],
            "source": 0,
            "target": 109,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 112,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 0,
            "target": 113,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 0,
            "target": 115,
            "weight": 1
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 1,
            "target": 67,
            "weight": 9
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 1,
            "target": 70,
            "weight": 23
          },
          {
            "overlap": [
              "2010ARIST..44....3K"
            ],
            "source": 1,
            "target": 43,
            "weight": 14
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 1,
            "target": 17,
            "weight": 21
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 1,
            "target": 107,
            "weight": 16
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 1,
            "target": 24,
            "weight": 23
          },
          {
            "overlap": [
              "2003BAAS...35.1241K"
            ],
            "source": 2,
            "target": 10,
            "weight": 20
          },
          {
            "overlap": [
              "1994BAAS...26.1370A"
            ],
            "source": 2,
            "target": 99,
            "weight": 23
          },
          {
            "overlap": [
              "2003BAAS...35.1241K"
            ],
            "source": 2,
            "target": 67,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "1989AJ.....98..755R",
              "1987MNRAS.227....1K"
            ],
            "source": 3,
            "target": 117,
            "weight": 11
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998ApJ...501..509V",
              "1986AJ.....92.1248T",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "1989AJ.....98..755R",
              "1999MNRAS.303..188K",
              "1996ApJ...470..724M",
              "1987MNRAS.227....1K",
              "1982AJ.....87..945K",
              "1997ApJ...490..493N",
              "1997ApJ...476L...7C"
            ],
            "source": 3,
            "target": 119,
            "weight": 22
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1986AJ.....92.1248T",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "1989AJ.....98..755R",
              "1999MNRAS.303..188K",
              "1987MNRAS.227....1K",
              "1997ApJ...490..493N",
              "1980ApJ...236..351D"
            ],
            "source": 3,
            "target": 123,
            "weight": 14
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 9,
            "weight": 2
          },
          {
            "overlap": [
              "1933AcHPh...6..110Z"
            ],
            "source": 3,
            "target": 12,
            "weight": 2
          },
          {
            "overlap": [
              "1989AJ.....98..755R"
            ],
            "source": 3,
            "target": 15,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1997ApJ...481..633D",
              "1989AJ.....98..755R",
              "1987MNRAS.227....1K",
              "1997ApJ...490..493N"
            ],
            "source": 3,
            "target": 20,
            "weight": 11
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 26,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 29,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 35,
            "weight": 2
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1990AJ....100.1405W",
              "1980ApJ...236..351D"
            ],
            "source": 3,
            "target": 36,
            "weight": 11
          },
          {
            "overlap": [
              "1982ApJ...262....9S",
              "1986AJ.....92.1248T",
              "1987MNRAS.227....1K",
              "1982AJ.....87..945K",
              "1987ApJ...313..121M",
              "1980ApJ...236..351D"
            ],
            "source": 3,
            "target": 38,
            "weight": 15
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 39,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1996ApJ...458..435C",
              "1982AJ.....87..945K",
              "1989AJ.....98..755R",
              "1997ApJ...490..493N",
              "1996ApJ...470..724M",
              "1997ApJ...476L...7C",
              "1980ApJ...236..351D"
            ],
            "source": 3,
            "target": 40,
            "weight": 14
          },
          {
            "overlap": [
              "1993MNRAS.264...71V"
            ],
            "source": 3,
            "target": 45,
            "weight": 3
          },
          {
            "overlap": [
              "1997AJ....114.2205G",
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 46,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 47,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 52,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 53,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..750K",
              "1997AJ....114.2205G",
              "1998PASP..110..934K",
              "1980ApJ...236..351D"
            ],
            "source": 3,
            "target": 55,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1996ApJ...458..435C",
              "1999MNRAS.303..188K",
              "1996ApJ...470..724M",
              "1997ApJ...490..493N",
              "1997ApJ...476L...7C",
              "1980ApJ...236..351D"
            ],
            "source": 3,
            "target": 57,
            "weight": 14
          },
          {
            "overlap": [
              "1986AJ.....92.1248T",
              "1982AJ.....87..945K",
              "1987ApJ...313..121M"
            ],
            "source": 3,
            "target": 66,
            "weight": 8
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1996ApJ...458..435C",
              "1995AJ....109.2368T",
              "1996ApJ...470..724M",
              "1997ApJ...476L...7C"
            ],
            "source": 3,
            "target": 68,
            "weight": 14
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992BICDS..41...31H",
              "1990AJ....100.1405W",
              "1997AJ....114.2205G"
            ],
            "source": 3,
            "target": 69,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 78,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1996ApJ...458..435C",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "1982AJ.....87..945K",
              "1997ApJ...490..493N",
              "1989ApJ...337...21H",
              "1993MNRAS.264...71V"
            ],
            "source": 3,
            "target": 84,
            "weight": 18
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 90,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 95,
            "weight": 4
          },
          {
            "overlap": [
              "1989AJ.....98.1143T"
            ],
            "source": 3,
            "target": 97,
            "weight": 5
          },
          {
            "overlap": [
              "1997AJ....114.2205G",
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1995AJ....109.2368T",
              "1990AJ....100.1405W",
              "1997AJ....114.2205G",
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 101,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "1993MNRAS.264...71V"
            ],
            "source": 3,
            "target": 111,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 3,
            "target": 113,
            "weight": 11
          },
          {
            "overlap": [
              "1997AJ....114.2205G"
            ],
            "source": 3,
            "target": 115,
            "weight": 3
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E"
            ],
            "source": 4,
            "target": 118,
            "weight": 8
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E"
            ],
            "source": 4,
            "target": 19,
            "weight": 28
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E"
            ],
            "source": 4,
            "target": 73,
            "weight": 13
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E"
            ],
            "source": 4,
            "target": 59,
            "weight": 11
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143..111G",
              "2000A&AS..143...61E"
            ],
            "source": 5,
            "target": 118,
            "weight": 28
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 7,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 17,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 18,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143....9W"
            ],
            "source": 5,
            "target": 22,
            "weight": 13
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143...23O",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143..111G",
              "2000A&AS..143....9W",
              "2000A&AS..143...61E",
              "2004SPIE.5493..137Q"
            ],
            "source": 5,
            "target": 23,
            "weight": 73
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143..111G",
              "2000A&AS..143...61E"
            ],
            "source": 5,
            "target": 28,
            "weight": 59
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 30,
            "weight": 33
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 32,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 33,
            "weight": 67
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143..111G",
              "2000A&AS..143...61E"
            ],
            "source": 5,
            "target": 37,
            "weight": 67
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 43,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...23O",
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 54,
            "weight": 9
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143..111G"
            ],
            "source": 5,
            "target": 59,
            "weight": 30
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 5,
            "target": 62,
            "weight": 20
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143..111G",
              "2000A&AS..143...61E"
            ],
            "source": 5,
            "target": 67,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 70,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143..111G",
              "2000A&AS..143...61E"
            ],
            "source": 5,
            "target": 73,
            "weight": 35
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 74,
            "weight": 33
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143..111G"
            ],
            "source": 5,
            "target": 83,
            "weight": 50
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 88,
            "weight": 33
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 94,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 99,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "1992ASPC...25...47M",
              "2000A&AS..143...61E"
            ],
            "source": 5,
            "target": 104,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143..111G",
              "2000A&AS..143....9W"
            ],
            "source": 5,
            "target": 106,
            "weight": 34
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 5,
            "target": 107,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 5,
            "target": 110,
            "weight": 17
          },
          {
            "overlap": [
              "2005IPM....41.1395K"
            ],
            "source": 6,
            "target": 7,
            "weight": 37
          },
          {
            "overlap": [
              "2005IPM....41.1395K"
            ],
            "source": 6,
            "target": 17,
            "weight": 37
          },
          {
            "overlap": [
              "2005IPM....41.1395K"
            ],
            "source": 6,
            "target": 10,
            "weight": 35
          },
          {
            "overlap": [
              "2005IPM....41.1395K"
            ],
            "source": 6,
            "target": 70,
            "weight": 40
          },
          {
            "overlap": [
              "2005IPM....41.1395K"
            ],
            "source": 6,
            "target": 94,
            "weight": 40
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 118,
            "weight": 6
          },
          {
            "overlap": [
              "2005IPM....41.1395K"
            ],
            "source": 7,
            "target": 10,
            "weight": 13
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 17,
            "weight": 41
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 18,
            "weight": 14
          },
          {
            "overlap": [
              "2005JASIS..56...36K"
            ],
            "source": 7,
            "target": 22,
            "weight": 14
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 23,
            "weight": 9
          },
          {
            "overlap": [
              "2006JEPub...9....2H"
            ],
            "source": 7,
            "target": 24,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 30,
            "weight": 35
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 7,
            "target": 32,
            "weight": 29
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 33,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 37,
            "weight": 17
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 43,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 54,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 59,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 62,
            "weight": 7
          },
          {
            "overlap": [
              "2006JEPub...9....2H",
              "2007LePub..20...16H",
              "2000A&AS..143...41K",
              "2005JASIS..56...36K",
              "2001Natur.411..521L"
            ],
            "source": 7,
            "target": 67,
            "weight": 31
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 70,
            "weight": 29
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 73,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 74,
            "weight": 35
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 83,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 88,
            "weight": 35
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 94,
            "weight": 29
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 7,
            "target": 99,
            "weight": 29
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 7,
            "target": 106,
            "weight": 24
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 107,
            "weight": 21
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 7,
            "target": 110,
            "weight": 6
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 8,
            "target": 53,
            "weight": 4
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 8,
            "target": 84,
            "weight": 6
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 8,
            "target": 35,
            "weight": 4
          },
          {
            "overlap": [
              "1997AJ....113..483R"
            ],
            "source": 9,
            "target": 116,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 117,
            "weight": 6
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 121,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "1993ApJ...404..441K",
              "1998SPIE.3355..285F",
              "1998ApJ...504..636H",
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 13,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 20,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 26,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998SPIE.3355..324R",
              "1998PASP..110..934K",
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 29,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 35,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998ApJ...504..636H",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 39,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 40,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 46,
            "weight": 2
          },
          {
            "overlap": [
              "1993ApJ...404..441K",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 47,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 52,
            "weight": 20
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 53,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 55,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1997AJ....113..483R"
            ],
            "source": 9,
            "target": 57,
            "weight": 4
          },
          {
            "overlap": [
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 60,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1993ApJ...404..441K",
              "1990ApJ...349L...1T",
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 61,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998SPIE.3355..324R",
              "2002SPIE.4836...73W",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 72,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 78,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 80,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998SPIE.3355..324R",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 85,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 90,
            "weight": 6
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K",
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 92,
            "weight": 7
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 95,
            "weight": 7
          },
          {
            "overlap": [
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 96,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "2002SPIE.4836...73W"
            ],
            "source": 9,
            "target": 109,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 113,
            "weight": 10
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 10,
            "target": 62,
            "weight": 7
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 10,
            "target": 34,
            "weight": 35
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2003BAAS...35.1241K"
            ],
            "source": 10,
            "target": 67,
            "weight": 12
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "2006cs........8027H"
            ],
            "source": 10,
            "target": 70,
            "weight": 29
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "1999ASPC..172..291A"
            ],
            "source": 10,
            "target": 94,
            "weight": 28
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 10,
            "target": 43,
            "weight": 8
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 10,
            "target": 44,
            "weight": 25
          },
          {
            "overlap": [
              "2005IPM....41.1395K"
            ],
            "source": 10,
            "target": 17,
            "weight": 13
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 10,
            "target": 18,
            "weight": 14
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 10,
            "target": 106,
            "weight": 12
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 10,
            "target": 107,
            "weight": 10
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 10,
            "target": 110,
            "weight": 6
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 10,
            "target": 83,
            "weight": 17
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 67,
            "weight": 9
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 71,
            "weight": 26
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 99,
            "weight": 23
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 73,
            "weight": 14
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 11,
            "target": 43,
            "weight": 14
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 104,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 19,
            "weight": 32
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 107,
            "weight": 16
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 22,
            "weight": 21
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 110,
            "weight": 19
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 11,
            "target": 56,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 106,
            "weight": 18
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 11,
            "target": 114,
            "weight": 32
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "1993adass...2..132K"
            ],
            "source": 11,
            "target": 59,
            "weight": 25
          },
          {
            "overlap": [
              "1976AJ.....81..952H",
              "1986ApJ...302L...1D"
            ],
            "source": 12,
            "target": 116,
            "weight": 4
          },
          {
            "overlap": [
              "1933AcHPh...6..110Z",
              "1986ApJ...302L...1D",
              "1982PASP...94..421G",
              "1988AJ.....95..985D"
            ],
            "source": 12,
            "target": 117,
            "weight": 7
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z"
            ],
            "source": 12,
            "target": 122,
            "weight": 5
          },
          {
            "overlap": [
              "1933AcHPh...6..110Z",
              "1980A&A....82..322D"
            ],
            "source": 12,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1983ApJ...264..356B"
            ],
            "source": 12,
            "target": 124,
            "weight": 4
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1980A&A....82..322D",
              "1988AJ.....95..985D",
              "1987ApJ...317..653F"
            ],
            "source": 12,
            "target": 14,
            "weight": 10
          },
          {
            "overlap": [
              "1987ApJ...313..505W",
              "1986ApJ...302L...1D"
            ],
            "source": 12,
            "target": 15,
            "weight": 4
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1982ApJ...257...23B",
              "1982PASP...94..421G",
              "1979SAOSR.385..119L"
            ],
            "source": 12,
            "target": 16,
            "weight": 9
          },
          {
            "overlap": [
              "1933AcHPh...6..110Z",
              "1986ApJ...302L...1D",
              "1980A&A....82..322D"
            ],
            "source": 12,
            "target": 119,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1980A&A....82..322D"
            ],
            "source": 12,
            "target": 31,
            "weight": 5
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1983ApJS...52...89H"
            ],
            "source": 12,
            "target": 36,
            "weight": 5
          },
          {
            "overlap": [
              "1987PhDT.......218C",
              "1980A&A....82..322D",
              "1983crvg.book.....P",
              "1979SAOSR.385..119L"
            ],
            "source": 12,
            "target": 38,
            "weight": 8
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 12,
            "target": 39,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1980A&A....82..322D"
            ],
            "source": 12,
            "target": 45,
            "weight": 5
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1958ApJS....3..211A",
              "1989ApJ...344...57R"
            ],
            "source": 12,
            "target": 48,
            "weight": 8
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z"
            ],
            "source": 12,
            "target": 50,
            "weight": 6
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1986ApJ...302L...1D",
              "1961cgcg.book.....Z"
            ],
            "source": 12,
            "target": 56,
            "weight": 5
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1989ApJ...344...57R"
            ],
            "source": 12,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 12,
            "target": 61,
            "weight": 1
          },
          {
            "overlap": [
              "1985ApJS...57..665B",
              "1986ApJ...302L...1D",
              "1988MNRAS.231..479S",
              "1983ApJ...270...20B",
              "1979SAOSR.385..119L"
            ],
            "source": 12,
            "target": 66,
            "weight": 10
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 12,
            "target": 68,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989ApJ...336...77F",
              "1982AJ.....87.1656H",
              "1988AJ.....95..284D",
              "1987A&AS...67...57P",
              "1961cgcg.book.....Z",
              "1976AJ.....81..952H",
              "1981rsac.book.....S"
            ],
            "source": 12,
            "target": 69,
            "weight": 6
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 12,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "1961cgcg.book.....Z"
            ],
            "source": 12,
            "target": 76,
            "weight": 4
          },
          {
            "overlap": [
              "1987ApJ...317..653F"
            ],
            "source": 12,
            "target": 79,
            "weight": 2
          },
          {
            "overlap": [
              "1933AcHPh...6..110Z"
            ],
            "source": 12,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1978MNRAS.183..341W"
            ],
            "source": 12,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 12,
            "target": 90,
            "weight": 1
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 12,
            "target": 91,
            "weight": 3
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1958ApJS....3..211A",
              "1983ApJ...270...20B"
            ],
            "source": 12,
            "target": 100,
            "weight": 7
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1986ApJ...302L...1D"
            ],
            "source": 12,
            "target": 103,
            "weight": 2
          },
          {
            "overlap": [
              "1987MNRAS.224..453C"
            ],
            "source": 12,
            "target": 109,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2005PASP..117.1411F"
            ],
            "source": 13,
            "target": 117,
            "weight": 6
          },
          {
            "overlap": [
              "1998astro.ph..9268K"
            ],
            "source": 13,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 13,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F"
            ],
            "source": 13,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "2005PASP..117.1411F"
            ],
            "source": 13,
            "target": 20,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2005ApJ...635L.125G",
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2002SPIE.4836...73W"
            ],
            "source": 13,
            "target": 29,
            "weight": 13
          },
          {
            "overlap": [
              "2007A&A...462..875S",
              "1986ApJ...306...30G",
              "2004MNRAS.350..893H",
              "2005ApJ...635L.125G",
              "1998SPIE.3355..285F",
              "2007ApJ...669..714M",
              "1995ApJ...449..460K",
              "2007A&A...462..459G",
              "2009ApJ...702..980K",
              "2012ApJ...748...56S",
              "2005A&A...442...43H",
              "2010ApJ...709..832G",
              "2008MNRAS.385..695B",
              "1998ApJ...504..636H",
              "2002ApJ...580L..97M",
              "2011MNRAS.413.1145B",
              "2007A&A...462..473M",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2001ApJ...557L..89W",
              "2007A&A...470..821D",
              "2002ApJ...575..640W"
            ],
            "source": 13,
            "target": 39,
            "weight": 42
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1993ApJ...404..441K",
              "2005ApJ...635L.125G",
              "2002PASJ...54..833M",
              "1995ApJ...449..460K",
              "2005PASP..117.1411F"
            ],
            "source": 13,
            "target": 47,
            "weight": 6
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 13,
            "target": 52,
            "weight": 6
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "2005ApJ...635L.125G",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W"
            ],
            "source": 13,
            "target": 53,
            "weight": 8
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2012ApJ...758...25H",
              "2002SPIE.4836...73W"
            ],
            "source": 13,
            "target": 60,
            "weight": 6
          },
          {
            "overlap": [
              "2007A&A...462..875S",
              "2012ApJ...750..168K",
              "2004MNRAS.350..893H",
              "1993ApJ...404..441K",
              "2005ApJ...635L.125G",
              "1998SPIE.3355..285F",
              "2007ApJ...669..714M",
              "2007A&A...462..459G",
              "2009ApJ...702..980K",
              "2012ApJ...758...25H",
              "2012ApJ...748...56S",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2001ApJ...557L..89W",
              "2002SPIE.4836...73W",
              "2012MNRAS.425.2287H",
              "2007A&A...470..821D",
              "2002ApJ...575..640W"
            ],
            "source": 13,
            "target": 61,
            "weight": 31
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 13,
            "target": 65,
            "weight": 7
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2004MNRAS.350..893H",
              "2002SPIE.4836...73W",
              "2007ApJ...669..714M",
              "2007A&A...462..459G",
              "2009ApJ...702..980K",
              "2005A&A...442...43H",
              "2005ApJ...635L.125G",
              "2008MNRAS.385..695B",
              "2002ApJ...580L..97M",
              "2007A&A...462..473M",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2001ApJ...557L..89W",
              "2007A&A...462..875S",
              "2007A&A...470..821D"
            ],
            "source": 13,
            "target": 72,
            "weight": 31
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 13,
            "target": 79,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 13,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1998astro.ph..9268K"
            ],
            "source": 13,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 13,
            "target": 85,
            "weight": 10
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "1998SPIE.3355..285F",
              "2012ApJ...758...25H",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 13,
            "target": 90,
            "weight": 10
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W",
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W"
            ],
            "source": 13,
            "target": 92,
            "weight": 10
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 13,
            "target": 95,
            "weight": 3
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 13,
            "target": 96,
            "weight": 8
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 13,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 13,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2010ApJ...709..832G",
              "2005PASP..117.1411F"
            ],
            "source": 13,
            "target": 103,
            "weight": 6
          },
          {
            "overlap": [
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W"
            ],
            "source": 13,
            "target": 109,
            "weight": 9
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 116,
            "weight": 3
          },
          {
            "overlap": [
              "1984ApJ...276...38J",
              "1988AJ.....95..985D"
            ],
            "source": 14,
            "target": 117,
            "weight": 6
          },
          {
            "overlap": [
              "1980A&A....82..322D",
              "1993ApJ...412..479D"
            ],
            "source": 14,
            "target": 119,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 121,
            "weight": 4
          },
          {
            "overlap": [
              "1980A&A....82..322D"
            ],
            "source": 14,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1984ApJ...283..495G",
              "1958ApJS....3..211A",
              "1984ApJ...276...38J"
            ],
            "source": 14,
            "target": 124,
            "weight": 11
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 16,
            "weight": 7
          },
          {
            "overlap": [
              "1980A&A....82..322D",
              "1990ApJ...364..433S"
            ],
            "source": 14,
            "target": 31,
            "weight": 8
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 36,
            "weight": 4
          },
          {
            "overlap": [
              "1980A&A....82..322D",
              "1976RC2...C......0D"
            ],
            "source": 14,
            "target": 38,
            "weight": 6
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 14,
            "target": 39,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1980A&A....82..322D"
            ],
            "source": 14,
            "target": 45,
            "weight": 8
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 14,
            "target": 48,
            "weight": 4
          },
          {
            "overlap": [
              "1976RC2...C......0D"
            ],
            "source": 14,
            "target": 50,
            "weight": 5
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 57,
            "weight": 5
          },
          {
            "overlap": [
              "1990ApJS...72..567G",
              "1958ApJS....3..211A"
            ],
            "source": 14,
            "target": 61,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 66,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1976RC2...C......0D",
              "1993AJ....106.1273Z"
            ],
            "source": 14,
            "target": 69,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 14,
            "target": 72,
            "weight": 3
          },
          {
            "overlap": [
              "1987ApJ...317..653F"
            ],
            "source": 14,
            "target": 79,
            "weight": 4
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 91,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 95,
            "weight": 5
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 14,
            "target": 100,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 101,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 102,
            "weight": 14
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 111,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 14,
            "target": 112,
            "weight": 4
          },
          {
            "overlap": [
              "1988ApJ...327..544D",
              "1986ApJ...302L...1D"
            ],
            "source": 15,
            "target": 116,
            "weight": 5
          },
          {
            "overlap": [
              "1989AJ.....98..755R",
              "1986ApJ...302L...1D"
            ],
            "source": 15,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "1989AJ.....98..755R",
              "1986ApJ...302L...1D",
              "1990ApJS...72..433H"
            ],
            "source": 15,
            "target": 117,
            "weight": 6
          },
          {
            "overlap": [
              "1989AJ.....98..755R"
            ],
            "source": 15,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "1983PASP...95.1021S"
            ],
            "source": 15,
            "target": 124,
            "weight": 3
          },
          {
            "overlap": [
              "1989AJ.....98..755R"
            ],
            "source": 15,
            "target": 20,
            "weight": 2
          },
          {
            "overlap": [
              "1990AJ.....99.1004B",
              "1986ApJ...302L...1D",
              "1990ApJS...72..433H"
            ],
            "source": 15,
            "target": 36,
            "weight": 10
          },
          {
            "overlap": [
              "1983PASP...95.1021S",
              "1985ApJS...57..423B"
            ],
            "source": 15,
            "target": 38,
            "weight": 5
          },
          {
            "overlap": [
              "1989AJ.....98..755R"
            ],
            "source": 15,
            "target": 40,
            "weight": 2
          },
          {
            "overlap": [
              "1988ApJ...327..544D"
            ],
            "source": 15,
            "target": 45,
            "weight": 3
          },
          {
            "overlap": [
              "1988ApJ...327..544D"
            ],
            "source": 15,
            "target": 48,
            "weight": 3
          },
          {
            "overlap": [
              "1990AJ.....99.1004B",
              "1983AJ.....88..439L",
              "1988ApJ...327..544D"
            ],
            "source": 15,
            "target": 50,
            "weight": 11
          },
          {
            "overlap": [
              "1987ApJ...313..629B",
              "1986ApJ...306L..55H",
              "1988ApJ...327..544D",
              "1986ApJ...302L...1D",
              "1988AJ.....96.1791F"
            ],
            "source": 15,
            "target": 56,
            "weight": 11
          },
          {
            "overlap": [
              "1990ApJS...72..433H"
            ],
            "source": 15,
            "target": 57,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 15,
            "target": 66,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1990ApJS...72..433H"
            ],
            "source": 15,
            "target": 68,
            "weight": 5
          },
          {
            "overlap": [
              "1990AJ.....99.1004B",
              "1991ApJ...377..349F",
              "1992AJ....103.1107S",
              "1988ApJ...327..544D",
              "1985ApJS...57..423B",
              "1988AJ.....96.1791F",
              "1990ApJS...72..433H"
            ],
            "source": 15,
            "target": 69,
            "weight": 6
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1988AJ.....96.1791F"
            ],
            "source": 15,
            "target": 91,
            "weight": 8
          },
          {
            "overlap": [
              "1990ApJS...72..433H",
              "1988AJ.....96.1791F"
            ],
            "source": 15,
            "target": 97,
            "weight": 9
          },
          {
            "overlap": [
              "1990AJ.....99.1004B"
            ],
            "source": 15,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1990ApJS...72..433H"
            ],
            "source": 15,
            "target": 101,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 15,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1988ApJ...327..544D"
            ],
            "source": 15,
            "target": 111,
            "weight": 3
          },
          {
            "overlap": [
              "1985ApJ...289...81R"
            ],
            "source": 15,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 116,
            "weight": 3
          },
          {
            "overlap": [
              "1982PASP...94..421G",
              "1981ApJ...243L.133F"
            ],
            "source": 16,
            "target": 117,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1958ApJS....3..211A",
              "1971ApJ...169..209S",
              "1976ApJ...203..297S",
              "1983ESASP.201...47K",
              "1982ApJ...254..437D"
            ],
            "source": 16,
            "target": 124,
            "weight": 19
          },
          {
            "overlap": [
              "1984ApJ...286..186F",
              "1980ApJS...42..565D",
              "1983ApJ...271..446A",
              "1985AJ.....90.1665K"
            ],
            "source": 16,
            "target": 31,
            "weight": 15
          },
          {
            "overlap": [
              "1976ApJ...203..297S",
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 36,
            "weight": 8
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1976ApJ...203..297S",
              "1983ApJ...271..446A",
              "1982ApJ...254..437D",
              "1974ApJ...194....1O",
              "1985AJ.....90.1665K",
              "1979SAOSR.385..119L"
            ],
            "source": 16,
            "target": 38,
            "weight": 19
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 16,
            "target": 39,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 16,
            "target": 45,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 16,
            "target": 48,
            "weight": 4
          },
          {
            "overlap": [
              "1982AJ.....87.1165B"
            ],
            "source": 16,
            "target": 55,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 57,
            "weight": 4
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 16,
            "target": 61,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1984ApJ...286..186F",
              "1977AJ.....82..187F",
              "1985AJ.....90.1665K",
              "1982ApJ...254..437D",
              "1979SAOSR.385..119L"
            ],
            "source": 16,
            "target": 66,
            "weight": 18
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1976ApJ...203..297S",
              "1982ApJ...254..437D",
              "1985AJ.....90.1665K"
            ],
            "source": 16,
            "target": 68,
            "weight": 12
          },
          {
            "overlap": [
              "1977AJ.....82..187F",
              "1979AJ.....84.1511T",
              "1983ApJ...268...47B"
            ],
            "source": 16,
            "target": 69,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 16,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 16,
            "target": 85,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 90,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 91,
            "weight": 5
          },
          {
            "overlap": [
              "1968ApJ...151..393S",
              "1976ApJ...203..297S"
            ],
            "source": 16,
            "target": 92,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 95,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 16,
            "target": 96,
            "weight": 3
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 16,
            "target": 97,
            "weight": 5
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 16,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 16,
            "target": 100,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1985AJ.....90.1665K"
            ],
            "source": 16,
            "target": 101,
            "weight": 6
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 102,
            "weight": 12
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 111,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 16,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 118,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 18,
            "weight": 14
          },
          {
            "overlap": [
              "2005JASIS..56...36K"
            ],
            "source": 17,
            "target": 22,
            "weight": 14
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 23,
            "weight": 9
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 17,
            "target": 24,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 30,
            "weight": 35
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 32,
            "weight": 29
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 33,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 37,
            "weight": 17
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 43,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 54,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 59,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 62,
            "weight": 7
          },
          {
            "overlap": [
              "2005JASIS..56..111K",
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 17,
            "target": 67,
            "weight": 18
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "2005JASIS..56..111K",
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 70,
            "weight": 45
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 73,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 74,
            "weight": 35
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 83,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 88,
            "weight": 35
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 94,
            "weight": 29
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 99,
            "weight": 29
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 106,
            "weight": 24
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2005JASIS..56..111K",
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 107,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 17,
            "target": 110,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 118,
            "weight": 6
          },
          {
            "overlap": [
              "2007ASPC..377..106H"
            ],
            "source": 18,
            "target": 21,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 23,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 30,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 32,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 33,
            "weight": 19
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 18,
            "target": 34,
            "weight": 40
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 37,
            "weight": 19
          },
          {
            "overlap": [
              "2007ASPC..377..106H"
            ],
            "source": 18,
            "target": 41,
            "weight": 29
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 43,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 54,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 59,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 62,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2002SPIE.4847..238K"
            ],
            "source": 18,
            "target": 67,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 70,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 73,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 74,
            "weight": 38
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 83,
            "weight": 39
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 88,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 94,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 99,
            "weight": 15
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 106,
            "weight": 26
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 107,
            "weight": 11
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 110,
            "weight": 6
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 19,
            "target": 64,
            "weight": 57
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E",
              "1996ASPC..101..569E"
            ],
            "source": 19,
            "target": 118,
            "weight": 19
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 71,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 67,
            "weight": 9
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 99,
            "weight": 23
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E",
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 73,
            "weight": 29
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 104,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 106,
            "weight": 18
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 107,
            "weight": 16
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 19,
            "target": 23,
            "weight": 15
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 22,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 110,
            "weight": 9
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 114,
            "weight": 32
          },
          {
            "overlap": [
              "1996ASPC..101..569E",
              "1997Ap&SS.247..189E",
              "1993ASPC...52..132K"
            ],
            "source": 19,
            "target": 59,
            "weight": 38
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1997ApJ...481..633D",
              "2006AJ....132.1275R",
              "1989AJ.....98..755R",
              "2011MNRAS.412..800S",
              "1987MNRAS.227....1K",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 117,
            "weight": 15
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 121,
            "weight": 2
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "2006AJ....132.1275R",
              "2011MNRAS.416.2882M",
              "2011MNRAS.412..800S",
              "1997ApJ...481..633D",
              "2010PASJ...62..811O",
              "2012ApJS..199...25P",
              "2012ApJ...757...22C",
              "1997ApJ...490..493N",
              "2013ApJ...764...58G",
              "1987MNRAS.227....1K",
              "1989AJ.....98..755R",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 123,
            "weight": 18
          },
          {
            "overlap": [
              "1997ApJ...481..633D",
              "1996MNRAS.281..799E",
              "1989AJ.....98..755R",
              "1987MNRAS.227....1K",
              "1999MNRAS.309..610D",
              "1997ApJ...490..493N",
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 119,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 26,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 29,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 35,
            "weight": 1
          },
          {
            "overlap": [
              "1987MNRAS.227....1K"
            ],
            "source": 20,
            "target": 38,
            "weight": 2
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "1998PASP..110..934K",
              "2001A&A...370..743H",
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 39,
            "weight": 7
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1998PASP..110..934K",
              "1996MNRAS.281..799E",
              "1989AJ.....98..755R",
              "1997ApJ...490..493N"
            ],
            "source": 20,
            "target": 40,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 46,
            "weight": 1
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "2012MNRAS.421.3147M",
              "2011ApJ...728L..39N",
              "2010PASJ...62..811O",
              "2010MNRAS.402...21N",
              "2011ApJ...742..117Z",
              "2012ApJS..199...25P",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 47,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 52,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 53,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 55,
            "weight": 2
          },
          {
            "overlap": [
              "1997ApJ...490..493N",
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "2011ApJS..193...29A",
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 61,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 65,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "1998PASP..110..934K",
              "2005PASP..117.1411F",
              "1996MNRAS.281..799E"
            ],
            "source": 20,
            "target": 72,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 78,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1997ApJ...490..493N",
              "1997ApJ...481..633D"
            ],
            "source": 20,
            "target": 84,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 85,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 95,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 96,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F"
            ],
            "source": 20,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2013ApJ...767...15R"
            ],
            "source": 20,
            "target": 109,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 20,
            "target": 113,
            "weight": 9
          },
          {
            "overlap": [
              "2003ApJS..148..175S"
            ],
            "source": 21,
            "target": 63,
            "weight": 4
          },
          {
            "overlap": [
              "2007ASPC..377..106H"
            ],
            "source": 21,
            "target": 41,
            "weight": 31
          },
          {
            "overlap": [
              "1993ASSL..182...21K",
              "2009arXiv0912.5235K"
            ],
            "source": 21,
            "target": 43,
            "weight": 22
          },
          {
            "overlap": [
              "2003ApJS..148..175S"
            ],
            "source": 21,
            "target": 53,
            "weight": 5
          },
          {
            "overlap": [
              "1993ASSL..182...21K"
            ],
            "source": 21,
            "target": 22,
            "weight": 17
          },
          {
            "overlap": [
              "2005JASIS..56...36K"
            ],
            "source": 22,
            "target": 32,
            "weight": 15
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 22,
            "target": 59,
            "weight": 8
          },
          {
            "overlap": [
              "2008PNAS..105.1118R",
              "2005JASIS..56...36K",
              "1993ASPC...52..132K"
            ],
            "source": 22,
            "target": 67,
            "weight": 18
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K"
            ],
            "source": 22,
            "target": 99,
            "weight": 30
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 22,
            "target": 73,
            "weight": 9
          },
          {
            "overlap": [
              "1993ASSL..182...21K",
              "2005JASIS..56...36K"
            ],
            "source": 22,
            "target": 43,
            "weight": 18
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 22,
            "target": 104,
            "weight": 5
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 22,
            "target": 110,
            "weight": 6
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K"
            ],
            "source": 22,
            "target": 107,
            "weight": 21
          },
          {
            "overlap": [
              "2000A&AS..143....9W"
            ],
            "source": 22,
            "target": 23,
            "weight": 10
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 22,
            "target": 71,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 22,
            "target": 114,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K",
              "2000A&AS..143....9W"
            ],
            "source": 22,
            "target": 106,
            "weight": 37
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143..111G",
              "1995VA.....39..217E",
              "2000A&AS..143...41K",
              "1994AAS...184.2802G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "1995ASPC...77...28E",
              "1994AAS...185.4104E",
              "1996ASPC..101..569E",
              "1992ald2.proc..387M"
            ],
            "source": 23,
            "target": 118,
            "weight": 48
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 23,
            "target": 28,
            "weight": 45
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 30,
            "weight": 25
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 32,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 23,
            "target": 33,
            "weight": 51
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 23,
            "target": 37,
            "weight": 51
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 43,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...23O"
            ],
            "source": 23,
            "target": 54,
            "weight": 7
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143..111G",
              "1995VA.....39..217E",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "1995ASPC...77...28E",
              "1996ASPC..101..569E",
              "1992ald2.proc..387M"
            ],
            "source": 23,
            "target": 59,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 23,
            "target": 62,
            "weight": 15
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 23,
            "target": 64,
            "weight": 26
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 23,
            "target": 67,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 70,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "1995VA.....39..217E",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "1995ASPC...77...28E",
              "1992ald2.proc..387M"
            ],
            "source": 23,
            "target": 73,
            "weight": 47
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 74,
            "weight": 25
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 83,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 88,
            "weight": 25
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 94,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 99,
            "weight": 10
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143..111G",
              "1994AAS...184.2802G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "1994AAS...185.4104E"
            ],
            "source": 23,
            "target": 104,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143....9W"
            ],
            "source": 23,
            "target": 106,
            "weight": 26
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 23,
            "target": 107,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 23,
            "target": 110,
            "weight": 13
          },
          {
            "overlap": [
              "1995ASPC...77...28E"
            ],
            "source": 23,
            "target": 114,
            "weight": 15
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 24,
            "target": 70,
            "weight": 16
          },
          {
            "overlap": [
              "2006JEPub...9....2H",
              "2005JASIS..56..111K"
            ],
            "source": 24,
            "target": 67,
            "weight": 13
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 24,
            "target": 107,
            "weight": 12
          },
          {
            "overlap": [
              "1982PhDT.........2K",
              "1976PASP...88..960S"
            ],
            "source": 25,
            "target": 95,
            "weight": 16
          },
          {
            "overlap": [
              "1976PASP...88..960S"
            ],
            "source": 25,
            "target": 56,
            "weight": 5
          },
          {
            "overlap": [
              "1976PASP...88..960S"
            ],
            "source": 25,
            "target": 91,
            "weight": 9
          },
          {
            "overlap": [
              "1982PhDT.........2K"
            ],
            "source": 25,
            "target": 43,
            "weight": 8
          },
          {
            "overlap": [
              "1971A&A....13..169B",
              "1982PhDT.........2K"
            ],
            "source": 25,
            "target": 115,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJS..162...38A",
              "2005ApJ...622L..33B",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2005ApJ...629..268H",
              "2005MNRAS.363..223G",
              "2005astro.ph.12344H",
              "2003ApJ...599.1129Y",
              "2003ApJ...593L..77H",
              "2005ApJ...634L.181E",
              "1988Natur.331..687H",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...628L.113S",
              "2005ApJ...634..344G",
              "1967BOTT....4...86P"
            ],
            "source": 26,
            "target": 125,
            "weight": 38
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 29,
            "weight": 3
          },
          {
            "overlap": [
              "2006ApJS..162...38A",
              "2005ApJ...622L..33B",
              "2005astro.ph..8193L",
              "2000ApJ...540..825Y",
              "1998PASP..110..934K",
              "2005ApJ...628..246E",
              "2003AJ....125..984M",
              "1961BAN....15..265B",
              "2005ApJ...629..268H",
              "2005MNRAS.363..223G",
              "1993ASPC...45..360L",
              "2005astro.ph.12344H",
              "2003ApJ...599.1129Y",
              "2006ApJ...636L..37F",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "1999MNRAS.310..645W",
              "2002AJ....123.3154D",
              "2002MNRAS.337...87C",
              "2005ApJ...634L.181E",
              "1988Natur.331..687H",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "1967BOTT....4...86P"
            ],
            "source": 26,
            "target": 35,
            "weight": 48
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 39,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998ApJ...500..525S"
            ],
            "source": 26,
            "target": 40,
            "weight": 4
          },
          {
            "overlap": [
              "2000ApJ...540..825Y",
              "1998PASP..110..934K",
              "1998ApJ...500..525S",
              "2002MNRAS.337...87C"
            ],
            "source": 26,
            "target": 46,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 47,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 52,
            "weight": 7
          },
          {
            "overlap": [
              "2006ApJS..162...38A",
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 53,
            "weight": 3
          },
          {
            "overlap": [
              "1995ApJS..101..117K",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "2002MNRAS.337...87C",
              "2000ApJ...540..825Y"
            ],
            "source": 26,
            "target": 54,
            "weight": 12
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 55,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 57,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 26,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 26,
            "target": 65,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "1998PASP..110..934K",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2005MNRAS.363..223G",
              "1993ASPC...45..360L",
              "1995ApJS..101..117K",
              "2003ApJ...599.1129Y",
              "2006ApJ...636L..37F",
              "2003AJ....126.1362B",
              "1955ApJ...121..161S",
              "1999MNRAS.310..645W",
              "2005ApJ...634L.181E",
              "1988Natur.331..687H",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "1967BOTT....4...86P"
            ],
            "source": 26,
            "target": 78,
            "weight": 35
          },
          {
            "overlap": [
              "2002MNRAS.337...87C",
              "2003AJ....125..984M",
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "1995ApJS..101..117K",
              "2003ApJ...599.1129Y",
              "2003AJ....126.1362B",
              "1999MNRAS.310..645W",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "1992A&AS...96..269S",
              "2000ApJ...540..825Y"
            ],
            "source": 26,
            "target": 80,
            "weight": 34
          },
          {
            "overlap": [
              "2003AJ....125..984M",
              "2003AJ....126.1362B",
              "2006ApJS..162...38A",
              "1998ApJ...500..525S",
              "2005ApJ...629..268H",
              "1995ApJS..101..117K",
              "2002MNRAS.337...87C"
            ],
            "source": 26,
            "target": 82,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "2000ApJ...540..825Y",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "2002MNRAS.337...87C"
            ],
            "source": 26,
            "target": 86,
            "weight": 8
          },
          {
            "overlap": [
              "2006ApJS..162...38A",
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "2005ApJ...628..246E",
              "2003AJ....125..984M",
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "2005astro.ph.12344H",
              "2006ApJ...636L..37F",
              "2003AJ....126.1362B",
              "2003ApJ...599.1129Y",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G",
              "2005A&A...444L..61H"
            ],
            "source": 26,
            "target": 89,
            "weight": 36
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2003AJ....125..984M"
            ],
            "source": 26,
            "target": 90,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 95,
            "weight": 4
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 101,
            "weight": 3
          },
          {
            "overlap": [
              "1955ApJ...121..161S"
            ],
            "source": 26,
            "target": 103,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998ApJ...500..525S",
              "2002AJ....123.3154D",
              "1992A&AS...96..269S"
            ],
            "source": 26,
            "target": 105,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 26,
            "target": 113,
            "weight": 11
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 27,
            "target": 116,
            "weight": 12
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 27,
            "target": 101,
            "weight": 12
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 27,
            "target": 45,
            "weight": 13
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 27,
            "target": 111,
            "weight": 12
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 27,
            "target": 57,
            "weight": 8
          },
          {
            "overlap": [
              "1995ASPC...77..496M",
              "1992ASPC...25..432K"
            ],
            "source": 27,
            "target": 115,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 28,
            "target": 118,
            "weight": 28
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 28,
            "target": 33,
            "weight": 83
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 28,
            "target": 67,
            "weight": 28
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 28,
            "target": 37,
            "weight": 83
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 28,
            "target": 62,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 28,
            "target": 73,
            "weight": 43
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 28,
            "target": 104,
            "weight": 23
          },
          {
            "overlap": [
              "2000A&AS..143..111G"
            ],
            "source": 28,
            "target": 106,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...61E"
            ],
            "source": 28,
            "target": 110,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A"
            ],
            "source": 28,
            "target": 83,
            "weight": 56
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A"
            ],
            "source": 28,
            "target": 59,
            "weight": 25
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..324R",
              "2009ApJS..182..543A",
              "2010ApJ...709..832G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 117,
            "weight": 15
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 121,
            "weight": 9
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 123,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 35,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2010ApJ...709..832G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 29,
            "target": 39,
            "weight": 18
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2005PASP..117..227K"
            ],
            "source": 29,
            "target": 40,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 46,
            "weight": 3
          },
          {
            "overlap": [
              "1997A&A...326..950F",
              "2005ApJ...635L.125G",
              "2009ApJS..182..543A",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 29,
            "target": 47,
            "weight": 7
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K",
              "1998SPIE.3355..324R"
            ],
            "source": 29,
            "target": 52,
            "weight": 21
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "2008PASP..120.1222F",
              "2000ApJ...539..718C",
              "2005ApJ...635L.125G",
              "1983ApJ...273..105B",
              "1998SPIE.3355..577M",
              "2010ApJ...708..534W",
              "2010ApJ...709..832G",
              "2004ApJ...613..898T",
              "2003MNRAS.344.1000B",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2000ApJ...533..682C",
              "2002SPIE.4836...73W"
            ],
            "source": 29,
            "target": 53,
            "weight": 28
          },
          {
            "overlap": [
              "1990PASP..102.1181B",
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 55,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 57,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2009ApJS..182..543A",
              "2004ApJ...613..898T",
              "2003MNRAS.344.1000B",
              "2005PASP..117..227K",
              "2000ApJ...533..682C"
            ],
            "source": 29,
            "target": 60,
            "weight": 16
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2005ApJ...635L.125G",
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2002SPIE.4836...73W"
            ],
            "source": 29,
            "target": 61,
            "weight": 11
          },
          {
            "overlap": [
              "2004ApJ...613..898T"
            ],
            "source": 29,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "1936ApJ....84..517H",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 29,
            "target": 65,
            "weight": 11
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2007AJ....134.1360K",
              "1998SPIE.3355..324R",
              "2002SPIE.4836...73W",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 29,
            "target": 72,
            "weight": 23
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 78,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 29,
            "target": 80,
            "weight": 5
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2007AJ....134.1360K",
              "2008PASP..120.1222F",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "1998SPIE.3355..324R",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2003MNRAS.341...33K",
              "2010AJ....139.1857W",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 29,
            "target": 85,
            "weight": 34
          },
          {
            "overlap": [
              "1998SPIE.3355..577M"
            ],
            "source": 29,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2007AJ....133..734B",
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "2010ApJ...708..534W",
              "2008PASP..120.1222F",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2003MNRAS.344.1000B",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2000ApJ...533..682C",
              "2003MNRAS.341...33K"
            ],
            "source": 29,
            "target": 90,
            "weight": 34
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "2008PASP..120.1222F",
              "2005ApJ...635L.125G",
              "1983ApJ...273..105B",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "1998SPIE.3355..324R",
              "2004ApJ...613..898T",
              "2003MNRAS.344.1000B",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2007PASP..119...30P",
              "2000ApJ...533..682C",
              "2002SPIE.4836...73W"
            ],
            "source": 29,
            "target": 92,
            "weight": 32
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 95,
            "weight": 7
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "2005ApJ...635L.125G",
              "1998SPIE.3355..577M",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "2006ApJ...643..128W",
              "2002SPIE.4836...73W",
              "2003MNRAS.341...33K"
            ],
            "source": 29,
            "target": 96,
            "weight": 22
          },
          {
            "overlap": [
              "2001AJ....121.2358B",
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 98,
            "weight": 6
          },
          {
            "overlap": [
              "2001AJ....121.2358B",
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 101,
            "weight": 5
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "2008PASP..120.1222F",
              "2005ApJ...635L.125G",
              "1983ApJ...273..105B",
              "2010ApJ...708..534W",
              "2010ApJ...709..832G",
              "2010AJ....139.1857W",
              "2003MNRAS.344.1000B",
              "2006ApJ...643..128W",
              "2000ApJ...533..682C",
              "2002SPIE.4836...73W",
              "2003MNRAS.341...33K"
            ],
            "source": 29,
            "target": 103,
            "weight": 19
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2008PASP..120.1222F",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2002SPIE.4836...73W",
              "2010MNRAS.405.1409C"
            ],
            "source": 29,
            "target": 109,
            "weight": 16
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 29,
            "target": 113,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 118,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 32,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 33,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 37,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 43,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 54,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 59,
            "weight": 21
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 62,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 67,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 70,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 73,
            "weight": 24
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 74,
            "weight": 92
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 83,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 88,
            "weight": 92
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 94,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 99,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 106,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 107,
            "weight": 27
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 30,
            "target": 110,
            "weight": 16
          },
          {
            "overlap": [
              "1980A&A....82..322D"
            ],
            "source": 31,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 31,
            "target": 122,
            "weight": 4
          },
          {
            "overlap": [
              "1980A&A....82..322D"
            ],
            "source": 31,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1953ApJ...118..502K"
            ],
            "source": 31,
            "target": 124,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 31,
            "target": 36,
            "weight": 4
          },
          {
            "overlap": [
              "1980A&A....82..322D",
              "1983ApJ...271..446A",
              "1985AJ.....90.1665K"
            ],
            "source": 31,
            "target": 38,
            "weight": 9
          },
          {
            "overlap": [
              "1981AJ.....86..476J",
              "1980A&A....82..322D"
            ],
            "source": 31,
            "target": 45,
            "weight": 8
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 31,
            "target": 48,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 31,
            "target": 50,
            "weight": 5
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1982SPIE..331..465V",
              "1981AJ.....86..476J"
            ],
            "source": 31,
            "target": 56,
            "weight": 9
          },
          {
            "overlap": [
              "1984ApJ...286..186F",
              "1974ApJ...191L..51H",
              "1986ApJ...308..530F",
              "1985AJ.....90.1665K"
            ],
            "source": 31,
            "target": 66,
            "weight": 14
          },
          {
            "overlap": [
              "1990SPIE.1235..747F",
              "1985AJ.....90.1665K"
            ],
            "source": 31,
            "target": 68,
            "weight": 7
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1986ApJ...308..530F",
              "1978AJ.....83..904S",
              "1991RC3...C......0D"
            ],
            "source": 31,
            "target": 69,
            "weight": 5
          },
          {
            "overlap": [
              "1990MNRAS.245..559E"
            ],
            "source": 31,
            "target": 72,
            "weight": 3
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 31,
            "target": 95,
            "weight": 5
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 31,
            "target": 100,
            "weight": 4
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1953ApJ...118..502K",
              "1990SPIE.1235..747F",
              "1985AJ.....90.1665K"
            ],
            "source": 31,
            "target": 101,
            "weight": 14
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 31,
            "target": 102,
            "weight": 14
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 31,
            "target": 103,
            "weight": 2
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 31,
            "target": 104,
            "weight": 3
          },
          {
            "overlap": [
              "1981AJ.....86..476J"
            ],
            "source": 31,
            "target": 111,
            "weight": 4
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 31,
            "target": 112,
            "weight": 4
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 31,
            "target": 115,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 118,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 33,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 37,
            "weight": 19
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 43,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 54,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 59,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 62,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56..786T",
              "2005JASIS..56...36K"
            ],
            "source": 32,
            "target": 67,
            "weight": 20
          },
          {
            "overlap": [
              "2003lisa.conf..223K",
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 70,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 73,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 74,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 83,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 88,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 94,
            "weight": 15
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 99,
            "weight": 32
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 106,
            "weight": 26
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 107,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 32,
            "target": 110,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 118,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 37,
            "weight": 95
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 43,
            "weight": 11
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 54,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 59,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 62,
            "weight": 28
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 67,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 70,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 33,
            "target": 73,
            "weight": 49
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 74,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 83,
            "weight": 71
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 88,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 94,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 99,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 33,
            "target": 104,
            "weight": 20
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 106,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 107,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 33,
            "target": 110,
            "weight": 24
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 34,
            "target": 67,
            "weight": 16
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 34,
            "target": 43,
            "weight": 24
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 34,
            "target": 106,
            "weight": 33
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 34,
            "target": 83,
            "weight": 49
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 117,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 121,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "1991AJ....101..562L",
              "2003ApJ...599.1129Y",
              "2005astro.ph.12344H",
              "2005ApJ...629..268H",
              "2005A&A...444L..61H",
              "2005ApJ...634L.181E",
              "2006ApJ...640L..35B",
              "1988Natur.331..687H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "1967BOTT....4...86P",
              "2005MNRAS.363..223G",
              "2006ApJS..162...38A",
              "2000ApJ...544..437P"
            ],
            "source": 35,
            "target": 125,
            "weight": 28
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 119,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 39,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998ApJ...500..525S"
            ],
            "source": 35,
            "target": 40,
            "weight": 2
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I"
            ],
            "source": 35,
            "target": 42,
            "weight": 3
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "2000ApJ...540..825Y",
              "2002MNRAS.337...87C",
              "1998ApJ...500..525S",
              "1998PASP..110..934K",
              "1973AJ.....78.1074O",
              "1994ApJS...94..687W",
              "1994AJ....108.1722K"
            ],
            "source": 35,
            "target": 46,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 47,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 52,
            "weight": 5
          },
          {
            "overlap": [
              "1999PASP..111..438F",
              "1998PASP..110..934K",
              "2006ApJS..162...38A"
            ],
            "source": 35,
            "target": 53,
            "weight": 3
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "2000ApJ...540..825Y",
              "2002MNRAS.337...87C",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "2004MNRAS.352..285C",
              "1994AJ....108.1722K"
            ],
            "source": 35,
            "target": 54,
            "weight": 11
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 55,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 57,
            "weight": 1
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 35,
            "target": 63,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 35,
            "target": 65,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 72,
            "weight": 1
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I"
            ],
            "source": 35,
            "target": 77,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "1997ApJ...482..677Y",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "2005MNRAS.363..223G",
              "1991AJ....101..562L",
              "1993ASPC...45..360L",
              "2004MNRAS.352..285C",
              "2006ApJ...636L..37F",
              "2003AJ....126.1362B",
              "2003AJ....125.1261D",
              "1999MNRAS.310..645W",
              "1998ARA&A..36..435M",
              "2003ApJ...599.1129Y",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "1967BOTT....4...86P",
              "2006ApJ...640L..35B",
              "2000ApJ...544..437P"
            ],
            "source": 35,
            "target": 78,
            "weight": 31
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I"
            ],
            "source": 35,
            "target": 79,
            "weight": 2
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "2000ApJ...540..825Y",
              "2002MNRAS.337...87C",
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "2003ApJ...599.1129Y",
              "2003AJ....125..984M",
              "2003AJ....126.1362B",
              "2004MNRAS.349..821L",
              "1999MNRAS.310..645W",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "1992A&AS...96..269S",
              "1994AJ....108.1722K"
            ],
            "source": 35,
            "target": 80,
            "weight": 26
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "2002MNRAS.337...87C",
              "2005MNRAS.362..349C",
              "2003AJ....125..984M",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "2004MNRAS.352..285C",
              "2005ApJ...629..268H",
              "2004AJ....128.2474M",
              "2005AJ....130.1097B",
              "2006ApJS..162...38A",
              "1994AJ....108.1722K"
            ],
            "source": 35,
            "target": 82,
            "weight": 14
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 35,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1998ARA&A..36..435M",
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 85,
            "weight": 3
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "2000ApJ...540..825Y",
              "2002MNRAS.337...87C",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "1994AJ....108.1722K"
            ],
            "source": 35,
            "target": 86,
            "weight": 8
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "1997ApJ...482..677Y",
              "2005ApJ...628..246E",
              "1995ApJ...453..214P",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "1991AJ....102..704H",
              "2005MNRAS.362..349C",
              "1991AJ....101..562L",
              "1993ASPC...45..360L",
              "2004MNRAS.350..615G",
              "2003ApJS..149...67B",
              "2003ApJ...599.1129Y",
              "2005MNRAS.364...59D",
              "2003AJ....125..984M",
              "2006ApJ...636L..37F",
              "2003AJ....126.1362B",
              "2005astro.ph.12344H",
              "2004MNRAS.352..285C",
              "2004AJ....128.2474M",
              "2005A&A...444L..61H",
              "2005AJ....130.1097B",
              "1998PASP..110..934K",
              "2005ApJ...634L.181E",
              "1973AJ.....78.1074O",
              "2006ApJ...640L..35B",
              "1988Natur.331..687H",
              "2002ApJ...573..359A",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "1994ApJS...94..687W",
              "2005MNRAS.363..223G",
              "2006ApJS..162...38A",
              "1996AJ....111.1748F",
              "2000ApJ...544..437P"
            ],
            "source": 35,
            "target": 89,
            "weight": 48
          },
          {
            "overlap": [
              "2003AJ....125..984M",
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 95,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K",
              "1996AJ....111.1748F"
            ],
            "source": 35,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "2002AJ....124..896S",
              "2003AJ....126.2048G",
              "1998AJ....116.1244T",
              "1998ApJ...500..525S",
              "1998ARA&A..36..435M",
              "2002AJ....123.3154D",
              "1998PASP..110..934K",
              "1992A&AS...96..269S",
              "1996ApJ...462..203Y",
              "2004ApJ...611L..93V"
            ],
            "source": 35,
            "target": 105,
            "weight": 17
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 35,
            "target": 113,
            "weight": 7
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1990ApJS...72..433H",
              "1989Sci...246..897G"
            ],
            "source": 36,
            "target": 117,
            "weight": 9
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 36,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 36,
            "target": 121,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1981ApJ...248L..57K"
            ],
            "source": 36,
            "target": 122,
            "weight": 8
          },
          {
            "overlap": [
              "1980ApJ...236..351D"
            ],
            "source": 36,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 36,
            "target": 124,
            "weight": 4
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1979AJ.....84.1511T",
              "1989Sci...246..897G"
            ],
            "source": 36,
            "target": 116,
            "weight": 10
          },
          {
            "overlap": [
              "1976ApJ...203..297S",
              "1988AJ.....95..267P",
              "1980ApJ...236..351D"
            ],
            "source": 36,
            "target": 38,
            "weight": 10
          },
          {
            "overlap": [
              "1984ApJ...281...95P",
              "1980ApJ...236..351D"
            ],
            "source": 36,
            "target": 40,
            "weight": 5
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 36,
            "target": 45,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G"
            ],
            "source": 36,
            "target": 48,
            "weight": 9
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1990AJ.....99.1004B",
              "1989Sci...246..897G"
            ],
            "source": 36,
            "target": 50,
            "weight": 15
          },
          {
            "overlap": [
              "1984ApJ...281...95P",
              "1980ApJ...236..351D"
            ],
            "source": 36,
            "target": 55,
            "weight": 6
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G",
              "1986ApJ...302L...1D",
              "1981ApJ...248L..57K"
            ],
            "source": 36,
            "target": 56,
            "weight": 12
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1984ApJ...281...95P",
              "1979AJ.....84.1511T",
              "1990ApJS...72..433H",
              "1980ApJ...236..351D"
            ],
            "source": 36,
            "target": 57,
            "weight": 13
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1979AJ.....84.1511T"
            ],
            "source": 36,
            "target": 66,
            "weight": 7
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1986ApJ...302L...1D",
              "1976ApJ...203..297S",
              "1990ApJS...72..433H",
              "1989Sci...246..897G"
            ],
            "source": 36,
            "target": 68,
            "weight": 17
          },
          {
            "overlap": [
              "1990AJ....100.1405W",
              "1983ApJS...52...89H",
              "1990AJ.....99.1004B",
              "1988AJ.....95.1602S",
              "1979AJ.....84.1511T",
              "1990ApJS...72..433H",
              "1990AJ.....99..463D",
              "1989Sci...246..897G",
              "1992ApJS...79..157F"
            ],
            "source": 36,
            "target": 69,
            "weight": 11
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 36,
            "target": 85,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1979AJ.....84.1511T"
            ],
            "source": 36,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1987ApJ...315L..93T",
              "1989ApJ...343....1D",
              "1979AJ.....84.1511T"
            ],
            "source": 36,
            "target": 91,
            "weight": 23
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 36,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 36,
            "target": 95,
            "weight": 5
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 36,
            "target": 96,
            "weight": 3
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1989Sci...246..897G",
              "1976ApJ...203..297S",
              "1990ApJS...72..433H",
              "1989ApJ...343....1D"
            ],
            "source": 36,
            "target": 97,
            "weight": 32
          },
          {
            "overlap": [
              "1990AJ.....99.1004B",
              "1976ApJ...203..297S"
            ],
            "source": 36,
            "target": 98,
            "weight": 6
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1981ApJ...248L..57K"
            ],
            "source": 36,
            "target": 100,
            "weight": 9
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1990AJ....100.1405W",
              "1979AJ.....84.1511T",
              "1990ApJS...72..433H",
              "1989Sci...246..897G"
            ],
            "source": 36,
            "target": 101,
            "weight": 18
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 36,
            "target": 102,
            "weight": 15
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1983ApJS...52...89H",
              "1989Sci...246..897G"
            ],
            "source": 36,
            "target": 103,
            "weight": 6
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1979AJ.....84.1511T"
            ],
            "source": 36,
            "target": 111,
            "weight": 7
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 36,
            "target": 112,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 118,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 43,
            "weight": 11
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 54,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 59,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 37,
            "target": 62,
            "weight": 28
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 67,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 70,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 73,
            "weight": 49
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 74,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 83,
            "weight": 71
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 88,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 94,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 99,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 37,
            "target": 104,
            "weight": 20
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 106,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 107,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 37,
            "target": 110,
            "weight": 24
          },
          {
            "overlap": [
              "1987MNRAS.227....1K",
              "1972ApJ...176....1G"
            ],
            "source": 38,
            "target": 117,
            "weight": 4
          },
          {
            "overlap": [
              "1980ApJ...236..351D",
              "1986AJ.....92.1248T",
              "1980A&A....82..322D",
              "1987MNRAS.227....1K"
            ],
            "source": 38,
            "target": 123,
            "weight": 6
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1980ApJS...43..305K",
              "1976ApJ...203..297S",
              "1983PASP...95.1021S",
              "1982ApJ...254..437D"
            ],
            "source": 38,
            "target": 124,
            "weight": 14
          },
          {
            "overlap": [
              "1986AJ.....92.1248T",
              "1982AJ.....87..945K",
              "1987MNRAS.227....1K",
              "1985ApJ...298....8H",
              "1980A&A....82..322D"
            ],
            "source": 38,
            "target": 119,
            "weight": 8
          },
          {
            "overlap": [
              "1982AJ.....87..945K",
              "1980ApJ...236..351D"
            ],
            "source": 38,
            "target": 40,
            "weight": 3
          },
          {
            "overlap": [
              "1980ApJS...43..305K",
              "1980A&A....82..322D"
            ],
            "source": 38,
            "target": 45,
            "weight": 6
          },
          {
            "overlap": [
              "1976RC2...C......0D"
            ],
            "source": 38,
            "target": 50,
            "weight": 4
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1980ApJ...236..351D"
            ],
            "source": 38,
            "target": 55,
            "weight": 4
          },
          {
            "overlap": [
              "1982ApJ...255..382H",
              "1980ApJ...236..351D"
            ],
            "source": 38,
            "target": 57,
            "weight": 4
          },
          {
            "overlap": [
              "1983AJ.....88..697K",
              "1986AJ.....92.1248T",
              "1982AJ.....87..945K",
              "1986AJ.....92.1238P",
              "1987ApJ...313..121M",
              "1985AJ.....90.1665K",
              "1982ApJ...254..437D",
              "1979SAOSR.385..119L"
            ],
            "source": 38,
            "target": 66,
            "weight": 20
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1976ApJ...203..297S",
              "1982ApJ...254..437D",
              "1985AJ.....90.1665K"
            ],
            "source": 38,
            "target": 68,
            "weight": 10
          },
          {
            "overlap": [
              "1973ugcg.book.....N",
              "1981PhDT........91B",
              "1981ApJS...47..139F",
              "1984AJ.....89..758H",
              "1975SoSAO..13...45K",
              "1985AJ.....90.2487B",
              "1985ApJS...57..423B",
              "1976RC2...C......0D",
              "1982ialo.coll..259L",
              "1982A&AS...49...73B"
            ],
            "source": 38,
            "target": 69,
            "weight": 9
          },
          {
            "overlap": [
              "1982AJ.....87..945K"
            ],
            "source": 38,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 38,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1973ugcg.book.....N",
              "1986AJ.....92.1238P"
            ],
            "source": 38,
            "target": 91,
            "weight": 8
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 38,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1975ApJ...197L..95W"
            ],
            "source": 38,
            "target": 95,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 38,
            "target": 96,
            "weight": 2
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 38,
            "target": 97,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 38,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1982ialo.coll..259L",
              "1985AJ.....90.1665K"
            ],
            "source": 38,
            "target": 101,
            "weight": 5
          },
          {
            "overlap": [
              "1981ApJ...244..805B",
              "1985ApJ...298....8H"
            ],
            "source": 38,
            "target": 105,
            "weight": 5
          },
          {
            "overlap": [
              "1980ApJS...43..305K"
            ],
            "source": 38,
            "target": 111,
            "weight": 3
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "2005PASP..117.1411F"
            ],
            "source": 39,
            "target": 117,
            "weight": 15
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 119,
            "weight": 1
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 121,
            "weight": 8
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "2011MNRAS.412.2095H",
              "1998PASP..110..934K",
              "2005PASP..117.1411F",
              "2003AJ....126.2152R",
              "2008ApJS..175..297A"
            ],
            "source": 39,
            "target": 123,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2003AJ....126.2152R"
            ],
            "source": 39,
            "target": 40,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 39,
            "target": 45,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 46,
            "weight": 1
          },
          {
            "overlap": [
              "1995ApJ...449..460K",
              "2006AJ....132.1275R",
              "2005ApJ...635L.125G",
              "2011MNRAS.412.2095H",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 39,
            "target": 47,
            "weight": 7
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 39,
            "target": 48,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 39,
            "target": 124,
            "weight": 2
          },
          {
            "overlap": [
              "2006ApJ...643..128W",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2010ApJ...709..832G",
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "2005PASP..117.1411F"
            ],
            "source": 39,
            "target": 53,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 55,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1958ApJS....3..211A"
            ],
            "source": 39,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "2005ApJ...635L.125G"
            ],
            "source": 39,
            "target": 60,
            "weight": 2
          },
          {
            "overlap": [
              "2007ApJ...669..714M",
              "2005ApJ...624...59H",
              "2007A&A...462..459G",
              "2004MNRAS.350..893H",
              "2005ApJ...635L.125G",
              "2007ApJ...660..239K",
              "2009PASJ...61..833H",
              "1998SPIE.3355..285F",
              "1958ApJS....3..211A",
              "2009ApJ...702..980K",
              "2012ApJ...748...56S",
              "2007A&A...462..875S",
              "2010ApJ...709..832G",
              "2011MNRAS.412.2095H",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2001ApJ...557L..89W",
              "2007A&A...470..821D",
              "2002ApJ...575..640W",
              "2008ApJ...684..794K"
            ],
            "source": 39,
            "target": 61,
            "weight": 33
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 39,
            "target": 65,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 52,
            "weight": 18
          },
          {
            "overlap": [
              "2005ApJ...624...59H",
              "2007ApJS..172..117M",
              "2007A&A...462..459G",
              "2006SAAS...33..269S",
              "2004MNRAS.350..893H",
              "1958ApJS....3..211A",
              "2000AJ....120.2747C",
              "1998SPIE.3355..324R",
              "2007ApJS..172....9T",
              "2009PASJ...61..833H",
              "1998SPIE.3355..285F",
              "2007ApJ...669..714M",
              "2006AJ....132.1275R",
              "2009ApJ...702..980K",
              "1996AJ....112.1335W",
              "2005ApJ...635L.125G",
              "2005A&A...442...43H",
              "2008MNRAS.385..695B",
              "2007A&A...462..875S",
              "2007ASPC..376..249M",
              "2002ApJ...580L..97M",
              "2007ApJS..172..219L",
              "2007A&A...470..821D",
              "2008ApJ...684..794K",
              "1998PASP..110..934K",
              "2007A&A...462..473M",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2001ApJ...557L..89W",
              "2007ApJ...660..239K",
              "2003AJ....126.2152R",
              "2007ApJS..170..377S"
            ],
            "source": 39,
            "target": 72,
            "weight": 63
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 78,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 39,
            "target": 80,
            "weight": 4
          },
          {
            "overlap": [
              "2007ApJS..172..117M",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2010ApJ...709..832G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 39,
            "target": 85,
            "weight": 19
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1958ApJS....3..211A",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W"
            ],
            "source": 39,
            "target": 90,
            "weight": 15
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2008ApJS..175..297A"
            ],
            "source": 39,
            "target": 92,
            "weight": 13
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 95,
            "weight": 6
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2008ApJS..175..297A",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 39,
            "target": 96,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 39,
            "target": 100,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2010ApJ...709..832G",
              "2005PASP..117.1411F"
            ],
            "source": 39,
            "target": 103,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 39,
            "target": 109,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 113,
            "weight": 9
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1989AJ.....98..755R",
              "1989ApJS...70....1A",
              "2001MNRAS.328.1039C",
              "2001ApJ...547..609E",
              "1998PASP..110..934K",
              "2003AJ....126.2152R"
            ],
            "source": 40,
            "target": 117,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F"
            ],
            "source": 40,
            "target": 121,
            "weight": 4
          },
          {
            "overlap": [
              "2004AJ....128.1078R",
              "2002AJ....124.1266R",
              "1999MNRAS.309..610D",
              "2004ApJ...610..745L",
              "1998PASP..110..934K",
              "1989AJ.....98..755R",
              "2002AJ....123..485S",
              "2001ApJ...555..558R",
              "1997ApJ...490..493N",
              "2003AJ....126.2152R",
              "1980ApJ...236..351D"
            ],
            "source": 40,
            "target": 123,
            "weight": 11
          },
          {
            "overlap": [
              "2002AJ....124.1266R",
              "1999MNRAS.309..610D",
              "1998PASP..110..934K",
              "1982AJ.....87..945K",
              "1996MNRAS.281..799E",
              "1989AJ.....98..755R",
              "2002AJ....123..485S",
              "1990ApJ...356..359H",
              "1989ApJS...70....1A",
              "1998PASP..110...79F",
              "2001MNRAS.328.1039C",
              "2001ApJ...555..558R",
              "1997ApJ...490..493N",
              "2001ApJ...547..609E",
              "1996ApJ...470..724M",
              "2001ApJ...559..606C",
              "1998PASJ...50..187F",
              "2001ApJ...561L..41R",
              "1997ApJ...476L...7C",
              "2000astro.ph.11458K"
            ],
            "source": 40,
            "target": 119,
            "weight": 24
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F",
              "1998ApJ...500..525S"
            ],
            "source": 40,
            "target": 46,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 47,
            "weight": 1
          },
          {
            "overlap": [
              "1989ApJS...70....1A"
            ],
            "source": 40,
            "target": 48,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 52,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2004MNRAS.353..713K",
              "1931ApJ....74...43H",
              "2001PASP..113.1449C",
              "2000ApJ...530..660B",
              "2003AJ....126.2152R",
              "2005PASP..117..227K"
            ],
            "source": 40,
            "target": 53,
            "weight": 8
          },
          {
            "overlap": [
              "2002AJ....123..485S",
              "1998PASP..110...79F",
              "1998ApJ...500..525S"
            ],
            "source": 40,
            "target": 54,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1984ApJ...281...95P",
              "1997A&AS..122..399P",
              "2000ApJ...530..660B",
              "1987ApJS...63..295V",
              "1989ApJ...347..152S",
              "1980ApJ...236..351D"
            ],
            "source": 40,
            "target": 55,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1996ApJ...458..435C",
              "1984ApJ...281...95P",
              "1990ApJ...356..359H",
              "1998PASP..110...79F",
              "1997ApJ...490..493N",
              "1996ApJ...470..724M",
              "1997ApJ...476L...7C",
              "1980ApJ...236..351D"
            ],
            "source": 40,
            "target": 57,
            "weight": 11
          },
          {
            "overlap": [
              "2003MNRAS.346.1055K",
              "2003ApJS..149..289B",
              "2005PASP..117..227K"
            ],
            "source": 40,
            "target": 60,
            "weight": 5
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 40,
            "target": 63,
            "weight": 1
          },
          {
            "overlap": [
              "1982AJ.....87..945K"
            ],
            "source": 40,
            "target": 66,
            "weight": 2
          },
          {
            "overlap": [
              "1996ApJ...458..435C",
              "1996ApJ...470..724M",
              "1997ApJ...476L...7C"
            ],
            "source": 40,
            "target": 68,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1997AJ....113..492C",
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 69,
            "weight": 2
          },
          {
            "overlap": [
              "2004ApJ...610..745L",
              "1998PASP..110..934K",
              "1996MNRAS.281..799E",
              "1989ApJS...70....1A",
              "2003AJ....126.2152R"
            ],
            "source": 40,
            "target": 72,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 78,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998ApJ...500..525S"
            ],
            "source": 40,
            "target": 82,
            "weight": 2
          },
          {
            "overlap": [
              "2001ApJ...560..566K",
              "2001MNRAS.326..255C",
              "1999MNRAS.309..610D",
              "1996ApJ...458..435C",
              "1982AJ.....87..945K",
              "2000ApJ...540..113B",
              "1997ilsn.proc...25S",
              "1990ApJ...356..359H",
              "1998PASP..110...79F",
              "2000AJ....119.2498J",
              "2000AJ....120.3340N",
              "1997ApJ...490..493N"
            ],
            "source": 40,
            "target": 84,
            "weight": 20
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998ApJ...500..525S"
            ],
            "source": 40,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F"
            ],
            "source": 40,
            "target": 89,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2001MNRAS.328.1039C",
              "2003ApJS..149..289B",
              "2005PASP..117..227K"
            ],
            "source": 40,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "2003MNRAS.346.1055K",
              "1998PASP..110..934K",
              "2001PASP..113.1449C",
              "2001ApJ...556..121K",
              "2000ApJ...530..660B",
              "2005PASP..117..227K"
            ],
            "source": 40,
            "target": 92,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F"
            ],
            "source": 40,
            "target": 95,
            "weight": 5
          },
          {
            "overlap": [
              "2000ApJS..126..331J",
              "2004AJ....127.2002K",
              "2002AJ....123..485S",
              "2005PASP..117..227K",
              "2001PASP..113.1449C"
            ],
            "source": 40,
            "target": 96,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1997A&AS..122..399P",
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 98,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F",
              "2000AJ....119.2498J"
            ],
            "source": 40,
            "target": 101,
            "weight": 5
          },
          {
            "overlap": [
              "2003MNRAS.346.1055K",
              "2001MNRAS.328.1039C",
              "2001ApJ...556..121K",
              "2004MNRAS.351.1151B",
              "2000ApJ...530..660B",
              "1987ApJS...63..295V"
            ],
            "source": 40,
            "target": 103,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998ApJ...500..525S"
            ],
            "source": 40,
            "target": 105,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 40,
            "target": 113,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 40,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 42,
            "target": 116,
            "weight": 5
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 42,
            "target": 48,
            "weight": 6
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 42,
            "target": 69,
            "weight": 2
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 42,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "1994Natur.370..194I"
            ],
            "source": 42,
            "target": 46,
            "weight": 3
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I"
            ],
            "source": 42,
            "target": 77,
            "weight": 6
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 42,
            "target": 50,
            "weight": 7
          },
          {
            "overlap": [
              "1994Natur.370..194I"
            ],
            "source": 42,
            "target": 82,
            "weight": 3
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 42,
            "target": 111,
            "weight": 5
          },
          {
            "overlap": [
              "1994Natur.370..194I"
            ],
            "source": 42,
            "target": 86,
            "weight": 4
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I",
              "1991AJ....101..892M"
            ],
            "source": 42,
            "target": 79,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 118,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 54,
            "weight": 3
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 43,
            "target": 56,
            "weight": 3
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 59,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 62,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2002SPIE.4847..238K",
              "2005JASIS..56...36K"
            ],
            "source": 43,
            "target": 67,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 70,
            "weight": 9
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 43,
            "target": 71,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 73,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 74,
            "weight": 22
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 83,
            "weight": 23
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 88,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 94,
            "weight": 9
          },
          {
            "overlap": [
              "1982PhDT.........2K"
            ],
            "source": 43,
            "target": 95,
            "weight": 6
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 99,
            "weight": 19
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 106,
            "weight": 23
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 107,
            "weight": 13
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 110,
            "weight": 8
          },
          {
            "overlap": [
              "1982PhDT.........2K"
            ],
            "source": 43,
            "target": 115,
            "weight": 4
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 44,
            "target": 62,
            "weight": 14
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 44,
            "target": 94,
            "weight": 28
          },
          {
            "overlap": [
              "1972Sci...178..471G"
            ],
            "source": 44,
            "target": 67,
            "weight": 12
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 44,
            "target": 107,
            "weight": 20
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 44,
            "target": 110,
            "weight": 12
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1988ApJ...327..544D",
              "1994ApJ...424L...1D"
            ],
            "source": 45,
            "target": 116,
            "weight": 12
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 45,
            "target": 117,
            "weight": 3
          },
          {
            "overlap": [
              "1993ASPC...37..185B"
            ],
            "source": 45,
            "target": 121,
            "weight": 4
          },
          {
            "overlap": [
              "1980A&A....82..322D"
            ],
            "source": 45,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1980ApJS...43..305K",
              "1958ApJS....3..211A"
            ],
            "source": 45,
            "target": 124,
            "weight": 7
          },
          {
            "overlap": [
              "1980A&A....82..322D"
            ],
            "source": 45,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1992csg..conf..351S",
              "1989Sci...246..897G",
              "1958ApJS....3..211A",
              "1988ApJ...327..544D",
              "1992ApJ...390..338L",
              "1990Natur.343..726B",
              "1990MNRAS.243..692M"
            ],
            "source": 45,
            "target": 48,
            "weight": 27
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1990MNRAS.243..692M"
            ],
            "source": 45,
            "target": 50,
            "weight": 14
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1981AJ.....86..476J",
              "1988ApJ...327..544D",
              "1994ApJ...424L...1D",
              "1990Natur.343..726B"
            ],
            "source": 45,
            "target": 56,
            "weight": 13
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1958ApJS....3..211A",
              "1992ASPC...25..432K",
              "1994ApJ...424L...1D"
            ],
            "source": 45,
            "target": 57,
            "weight": 9
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 45,
            "target": 61,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1994ApJ...424L...1D",
              "1992ApJ...390..338L",
              "1990Natur.343..726B"
            ],
            "source": 45,
            "target": 68,
            "weight": 12
          },
          {
            "overlap": [
              "1994ApJ...424L...1D",
              "1989Sci...246..897G",
              "1988ApJ...327..544D"
            ],
            "source": 45,
            "target": 69,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 45,
            "target": 72,
            "weight": 3
          },
          {
            "overlap": [
              "1993MNRAS.264...71V"
            ],
            "source": 45,
            "target": 84,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1958ApJS....3..211A"
            ],
            "source": 45,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 45,
            "target": 97,
            "weight": 6
          },
          {
            "overlap": [
              "1992ApJ...390..338L"
            ],
            "source": 45,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 45,
            "target": 100,
            "weight": 4
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1994ApJ...424L...1D",
              "1992ApJ...390..338L"
            ],
            "source": 45,
            "target": 101,
            "weight": 12
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 45,
            "target": 103,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...311..651K",
              "1993obco.symp..112K",
              "1980ApJS...43..305K",
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1981AJ.....86..476J",
              "1988ApJ...327..544D",
              "1984amd..conf..277K",
              "1992ApJ...384..396R",
              "1991ApJ...369..273D",
              "1993ASPC...37..185B",
              "1993ASPC...51..112K",
              "1993MNRAS.264...71V",
              "1992ApJS...83..237I",
              "1990Natur.343..726B",
              "1990MNRAS.243..692M"
            ],
            "source": 45,
            "target": 111,
            "weight": 55
          },
          {
            "overlap": [
              "1992ASPC...25..432K",
              "1994ApJ...424L...1D"
            ],
            "source": 45,
            "target": 115,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 117,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 121,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 47,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 52,
            "weight": 4
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 53,
            "weight": 2
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "2002ApJ...574L..39G",
              "1992MNRAS.257..225A",
              "1998ApJ...508..844G",
              "1998PASP..110...79F",
              "1998MNRAS.298..387D",
              "1998ApJ...500..525S",
              "1984jbp..bookQ....B",
              "2002MNRAS.337...87C",
              "1999AJ....117.2329W",
              "2000asqu.book....1C",
              "1999AJ....117..981B",
              "2003AJ....125.1309C",
              "2000AJ....120.1014P",
              "2000ApJ...540..825Y",
              "1996ApJ...459L..73M",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "2003MNRAS.339.1111A",
              "1989MNRAS.238..225S"
            ],
            "source": 46,
            "target": 54,
            "weight": 33
          },
          {
            "overlap": [
              "1997AJ....114.2205G",
              "1988ApJ...328..315M",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 55,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 57,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 46,
            "target": 63,
            "weight": 1
          },
          {
            "overlap": [
              "1997AJ....114.2205G",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 69,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 72,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 78,
            "weight": 2
          },
          {
            "overlap": [
              "1997A&A...320..823C"
            ],
            "source": 46,
            "target": 79,
            "weight": 2
          },
          {
            "overlap": [
              "2002MNRAS.337...87C",
              "1998ApJ...508..844G",
              "1998MNRAS.298..387D",
              "1990A&A...236..357C",
              "1999AJ....117.2308W",
              "1998PASP..110..934K",
              "2000ApJ...540..825Y",
              "2003AJ....125.1309C",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P"
            ],
            "source": 46,
            "target": 80,
            "weight": 18
          },
          {
            "overlap": [
              "2000AJ....120.1014P",
              "1998ApJ...500..525S",
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "2002MNRAS.337...87C",
              "2002ApJ...574L..39G",
              "2002ApJ...569..245N",
              "1992MNRAS.257..225A",
              "2002AJ....124..931B",
              "1998ApJ...508..844G",
              "2003MNRAS.339.1111A",
              "1996ApJ...465..278J",
              "1998PASP..110...79F",
              "1994Natur.370..194I",
              "2003ApJ...588..824Y",
              "1999AJ....117.2308W",
              "1999AJ....117..981B",
              "1999AJ....117.2329W",
              "2003AJ....125.1309C",
              "1996ApJ...459L..73M",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "1989MNRAS.238..225S"
            ],
            "source": 46,
            "target": 82,
            "weight": 27
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 46,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 85,
            "weight": 3
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998SPIE.3355..577M",
              "1993AJ....106..703S",
              "2003ApJ...588..824Y",
              "1992MNRAS.257..225A",
              "2002ApJ...569..245N",
              "1999AJ....117.2308W",
              "2002astro.ph.12116V",
              "1998ApJ...508..844G",
              "1998PASP..110...79F",
              "1992ApJ...386..663C",
              "2001ApJ...547L.133I",
              "1994Natur.370..194I",
              "1996ApJ...465..278J",
              "2001ApJ...554L..33V",
              "2002MNRAS.337...87C",
              "1999AJ....117.2329W",
              "1982AJ.....87.1515P",
              "2000asqu.book....1C",
              "2003AJ....125.1309C",
              "2000AJ....120..963I",
              "2000ApJ...540..825Y",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "2002AJ....124..931B",
              "1989MNRAS.238..225S"
            ],
            "source": 46,
            "target": 86,
            "weight": 35
          },
          {
            "overlap": [
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "1998PASP..110...79F",
              "1998PASP..110..934K",
              "1999AJ....117.2329W",
              "1973AJ.....78.1074O",
              "1994ApJS...94..687W"
            ],
            "source": 46,
            "target": 89,
            "weight": 9
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 95,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "2001AJ....122..714B",
              "1988ApJ...328..315M"
            ],
            "source": 46,
            "target": 96,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110...79F",
              "1997AJ....114.2205G",
              "1998ApJ...500..525S",
              "1998PASP..110..934K",
              "2000asqu.book....1C"
            ],
            "source": 46,
            "target": 98,
            "weight": 8
          },
          {
            "overlap": [
              "1997AJ....114.2205G",
              "2001AJ....122..714B",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 101,
            "weight": 7
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 105,
            "weight": 3
          },
          {
            "overlap": [
              "1984ApJS...56..257J"
            ],
            "source": 46,
            "target": 111,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 46,
            "target": 113,
            "weight": 7
          },
          {
            "overlap": [
              "1997AJ....114.2205G",
              "1998PASP..110...79F"
            ],
            "source": 46,
            "target": 115,
            "weight": 3
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "2005AJ....130.1482R",
              "2011ApJ...729..127U",
              "2009ApJS..182..543A",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 117,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 119,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 121,
            "weight": 1
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "2010ApJ...715L.180R",
              "2001MNRAS.321..559B",
              "2007ApJ...655...98N",
              "2005AJ....130.1482R",
              "2008MNRAS.390.1647B",
              "2009ApJ...691.1307H",
              "2008ApJ...685L...9B",
              "2011ApJ...729..127U",
              "2011MNRAS.412.2095H",
              "2010PASJ...62..811O",
              "2005ApJ...619L.143B",
              "2012ApJS..199...25P",
              "2008ApJS..174..117M",
              "1998PASP..110..934K",
              "2010AJ....139..580R",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 123,
            "weight": 12
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 52,
            "weight": 3
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2002ApJ...568...52W",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 53,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 55,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 57,
            "weight": 1
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2009ApJS..182..543A"
            ],
            "source": 47,
            "target": 60,
            "weight": 2
          },
          {
            "overlap": [
              "1993ApJ...404..441K",
              "2005ApJ...635L.125G",
              "2011MNRAS.412.2095H",
              "2005PASP..117.1411F",
              "2007ApJ...655...98N"
            ],
            "source": 47,
            "target": 61,
            "weight": 4
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 65,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 69,
            "weight": 0
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 72,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 78,
            "weight": 1
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 47,
            "target": 79,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 80,
            "weight": 1
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 85,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 89,
            "weight": 1
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "2005PASP..117.1411F",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2007ApJS..172...99C"
            ],
            "source": 47,
            "target": 92,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 95,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 96,
            "weight": 3
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 105,
            "weight": 1
          },
          {
            "overlap": [
              "2005PASP..117.1411F"
            ],
            "source": 47,
            "target": 109,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 112,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 47,
            "target": 113,
            "weight": 5
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1963MCG...C03....0V",
              "1988ApJ...327..544D",
              "1990AJ.....99.2019L",
              "1993AJ....106..676A",
              "1989spce.book.....L"
            ],
            "source": 48,
            "target": 116,
            "weight": 20
          },
          {
            "overlap": [
              "1989ApJS...70....1A"
            ],
            "source": 48,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 48,
            "target": 122,
            "weight": 4
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1989ApJS...70....1A"
            ],
            "source": 48,
            "target": 117,
            "weight": 6
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 48,
            "target": 124,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1990AJ.....99.2019L",
              "1989spce.book.....L",
              "1990MNRAS.243..692M"
            ],
            "source": 48,
            "target": 50,
            "weight": 30
          },
          {
            "overlap": [
              "1988MNRAS.232..431E"
            ],
            "source": 48,
            "target": 54,
            "weight": 3
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1990Natur.343..726B"
            ],
            "source": 48,
            "target": 56,
            "weight": 12
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1958ApJS....3..211A",
              "1989ApJ...344...57R"
            ],
            "source": 48,
            "target": 57,
            "weight": 7
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 48,
            "target": 61,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ApJ...390..338L",
              "1990Natur.343..726B"
            ],
            "source": 48,
            "target": 68,
            "weight": 10
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1990AJ.....99.2019L",
              "1993AJ....106..676A",
              "1993AJ....105.1271G"
            ],
            "source": 48,
            "target": 69,
            "weight": 7
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1989ApJS...70....1A"
            ],
            "source": 48,
            "target": 72,
            "weight": 6
          },
          {
            "overlap": [
              "1988MNRAS.232..431E"
            ],
            "source": 48,
            "target": 82,
            "weight": 2
          },
          {
            "overlap": [
              "1988MNRAS.232..431E",
              "1979ApJ...232..352S"
            ],
            "source": 48,
            "target": 85,
            "weight": 6
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1989Sci...246..897G"
            ],
            "source": 48,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "1979ApJ...232..352S"
            ],
            "source": 48,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 48,
            "target": 97,
            "weight": 6
          },
          {
            "overlap": [
              "1988MNRAS.232..431E",
              "1990AJ.....99.2019L",
              "1979ApJ...232..352S",
              "1992ApJ...390..338L"
            ],
            "source": 48,
            "target": 98,
            "weight": 11
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1958ApJS....3..211A"
            ],
            "source": 48,
            "target": 100,
            "weight": 8
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ApJ...390..338L"
            ],
            "source": 48,
            "target": 101,
            "weight": 7
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G"
            ],
            "source": 48,
            "target": 103,
            "weight": 4
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1990AJ.....99.2019L",
              "1990Natur.343..726B",
              "1990MNRAS.243..692M"
            ],
            "source": 48,
            "target": 111,
            "weight": 18
          },
          {
            "overlap": [
              "2008CTM....12.1009K",
              "2003JFM...476..267S",
              "1991RSPSA.435..459S",
              "2012CTM....16..650R",
              "2009IJCFD..23..503R",
              "1977ARPC...28...75L",
              "1986AIAAJ..24.1453A",
              "1978AcAau...5..971L",
              "2005CTM.....9..417S",
              "2010JEnMa..68..249K",
              "2000JCoPh.165..660V",
              "1970JAMTP..11..264Z",
              "2002CTM.....6..553K",
              "2011PhLA..375.1803L"
            ],
            "source": 49,
            "target": 120,
            "weight": 80
          },
          {
            "overlap": [
              "1990AJ.....99.2019L",
              "1989Sci...246..897G",
              "1989spce.book.....L",
              "1985ComAp..11...53F",
              "1988ApJ...327..544D"
            ],
            "source": 50,
            "target": 116,
            "weight": 20
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 50,
            "target": 117,
            "weight": 3
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z"
            ],
            "source": 50,
            "target": 122,
            "weight": 9
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z",
              "1988ApJ...327..544D"
            ],
            "source": 50,
            "target": 56,
            "weight": 13
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 50,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 50,
            "target": 68,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z",
              "1976RC2...C......0D",
              "1989Sci...246..897G",
              "1990AJ.....99.2019L",
              "1990AJ.....99.1004B",
              "1988ApJ...327..544D"
            ],
            "source": 50,
            "target": 69,
            "weight": 9
          },
          {
            "overlap": [
              "1961cgcg.book.....Z"
            ],
            "source": 50,
            "target": 76,
            "weight": 7
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 50,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 50,
            "target": 97,
            "weight": 7
          },
          {
            "overlap": [
              "1990AJ.....99.1004B",
              "1990AJ.....99.2019L"
            ],
            "source": 50,
            "target": 98,
            "weight": 6
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 50,
            "target": 100,
            "weight": 5
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 50,
            "target": 101,
            "weight": 4
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1983ApJS...52...89H"
            ],
            "source": 50,
            "target": 103,
            "weight": 4
          },
          {
            "overlap": [
              "1990AJ.....99.2019L",
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1990MNRAS.243..692M"
            ],
            "source": 50,
            "target": 111,
            "weight": 17
          },
          {
            "overlap": [
              "1996adass...5..569E"
            ],
            "source": 51,
            "target": 118,
            "weight": 12
          },
          {
            "overlap": [
              "1996adass...5..569E"
            ],
            "source": 51,
            "target": 59,
            "weight": 16
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K",
              "1998SPIE.3355..324R"
            ],
            "source": 52,
            "target": 117,
            "weight": 18
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K",
              "1998SPIE.3355..324R"
            ],
            "source": 52,
            "target": 121,
            "weight": 24
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 123,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 119,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 53,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 55,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 57,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 52,
            "target": 61,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 69,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K",
              "1998SPIE.3355..324R"
            ],
            "source": 52,
            "target": 72,
            "weight": 18
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 78,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 52,
            "target": 80,
            "weight": 14
          },
          {
            "overlap": [
              "2004ASPC..314..141M"
            ],
            "source": 52,
            "target": 82,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998SPIE.3355..324R",
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 85,
            "weight": 20
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 89,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K",
              "1998SPIE.3355..324R"
            ],
            "source": 52,
            "target": 90,
            "weight": 16
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 92,
            "weight": 15
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 95,
            "weight": 20
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 98,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 101,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 105,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 112,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 52,
            "target": 113,
            "weight": 28
          },
          {
            "overlap": [
              "2010ApJ...709..832G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 117,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 119,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 121,
            "weight": 2
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 53,
            "target": 125,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 123,
            "weight": 3
          },
          {
            "overlap": [
              "1981PASP...93....5B",
              "2000ApJ...530..660B",
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 55,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 57,
            "weight": 1
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "1996AJ....112..839C",
              "2004ApJ...613..898T",
              "2003MNRAS.344.1000B",
              "2005PASP..117..227K",
              "2006MNRAS.372..961K",
              "2000ApJ...533..682C",
              "1981PASP...93....5B"
            ],
            "source": 53,
            "target": 60,
            "weight": 13
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W"
            ],
            "source": 53,
            "target": 61,
            "weight": 6
          },
          {
            "overlap": [
              "2004ApJ...613..898T",
              "2003ApJS..148..175S",
              "1998ARA&A..36..189K"
            ],
            "source": 53,
            "target": 63,
            "weight": 3
          },
          {
            "overlap": [
              "2002A&A...386..446L",
              "2006ApJ...643..128W",
              "2005ApJ...635L.125G",
              "2006ApJS..162...38A",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 65,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998SPIE.3355..577M",
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 72,
            "weight": 11
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 78,
            "weight": 1
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 53,
            "target": 79,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 53,
            "target": 82,
            "weight": 1
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 53,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2008PASP..120.1222F",
              "1998SPIE.3355..577M",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005MNRAS.360...81D",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 53,
            "target": 85,
            "weight": 15
          },
          {
            "overlap": [
              "1998SPIE.3355..577M"
            ],
            "source": 53,
            "target": 86,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJS..162...38A",
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "1996AJ....112..839C",
              "2003MNRAS.344.1000B",
              "2006ApJ...643..128W",
              "2005A&A...437..883M",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2007ApJ...660L..47N",
              "2000ApJ...533..682C",
              "1998SPIE.3355..577M",
              "2010ApJ...708..534W",
              "2010ApJ...709..832G",
              "2008A&A...487...89V",
              "2008PASP..120.1222F",
              "2005PASP..117..227K",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 90,
            "weight": 21
          },
          {
            "overlap": [
              "2006AJ....132..197W",
              "1999ApJ...527...54B",
              "2002SPIE.4836...73W",
              "2003MNRAS.344.1000B",
              "2006ApJ...643..128W",
              "2007AJ....134..527W",
              "1981PASP...93....5B",
              "1996A&AS..117..393B",
              "1983ApJ...273..105B",
              "2005ApJ...635L.125G",
              "2004ApJ...613..898T",
              "2007ASPC..376..249M",
              "2000ApJ...530..660B",
              "2001PASP..113.1449C",
              "1996ApJ...464..641M",
              "2000ApJ...533..682C",
              "1998SPIE.3355..577M",
              "2005ApJ...625...23B",
              "2008MNRAS.385.1903L",
              "2008PASP..120.1222F",
              "2008AJ....135.1877E",
              "2005PASP..117..227K",
              "1998ARA&A..36..189K",
              "1998PASP..110..934K",
              "2008ApJS..175..128S",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 92,
            "weight": 31
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 95,
            "weight": 2
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2002SPIE.4836...73W",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "2008ApJS..175..128S",
              "2001PASP..113.1449C",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 96,
            "weight": 15
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 101,
            "weight": 3
          },
          {
            "overlap": [
              "2006AJ....132..197W",
              "1999ApJ...527...54B",
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2003MNRAS.344.1000B",
              "2006ApJ...643..128W",
              "2007AJ....134..527W",
              "1981PASP...93....5B",
              "1983ApJ...273..105B",
              "2000ApJ...530..660B",
              "2000ApJ...533..682C",
              "2010ApJ...709..832G",
              "2006AJ....132.2243G",
              "2008MNRAS.384..386C",
              "2010ApJ...708..534W",
              "2008PASP..120.1222F",
              "2008AJ....135.1877E",
              "1998ARA&A..36..189K",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 103,
            "weight": 18
          },
          {
            "overlap": [
              "2007AJ....134..527W",
              "1999ApJS..123....3L",
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 105,
            "weight": 4
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2008PASP..120.1222F",
              "2002SPIE.4836...73W",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W"
            ],
            "source": 53,
            "target": 109,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 113,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 118,
            "weight": 2
          },
          {
            "overlap": [
              "2002AJ....123..485S",
              "1998PASP..110...79F"
            ],
            "source": 54,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "2002AJ....123..485S"
            ],
            "source": 54,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 54,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 54,
            "target": 57,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 59,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 62,
            "weight": 3
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 54,
            "target": 63,
            "weight": 1
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 67,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 54,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 70,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 73,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 74,
            "weight": 13
          },
          {
            "overlap": [
              "2004MNRAS.352..285C",
              "1995ApJS..101..117K",
              "1998PASP..110...79F",
              "2003AJ....126.1362B"
            ],
            "source": 54,
            "target": 78,
            "weight": 6
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "2002MNRAS.337...87C",
              "1998ApJ...508..844G",
              "1995ApJS..101..117K",
              "1998MNRAS.298..387D",
              "2003AJ....126.1362B",
              "2004AJ....127..899S",
              "2004AJ....127.1555B",
              "2003AJ....125.1309C",
              "2000ApJ...540..825Y",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P"
            ],
            "source": 54,
            "target": 80,
            "weight": 28
          },
          {
            "overlap": [
              "1980MNRAS.193..295F",
              "1994AJ....108..538P",
              "2002ApJ...578..151S",
              "1998ApJ...508..844G",
              "1998PASP..110...79F",
              "2004AJ....127..899S",
              "1992ApJS...78...87M",
              "2000AJ....119.2843C",
              "1999AJ....117.2329W",
              "2000AJ....120.1014P",
              "2004AJ....127..914S",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "1991ApJ...380..104N",
              "1988MNRAS.232..431E",
              "1998AJ....116.1724M",
              "2004MNRAS.352..285C",
              "2003AJ....126.1362B",
              "1983ApJS...53..791P",
              "1999AJ....117..981B",
              "1999AJ....117.2308W",
              "2005AJ....129..466C",
              "1992MNRAS.257..225A",
              "1996AJ....112.2110L",
              "1998ApJ...500..525S",
              "2004AJ....127.1555B",
              "1996ApJ...459L..73M",
              "2003MNRAS.339.1111A",
              "1989MNRAS.238..225S",
              "2002ApJ...574L..39G",
              "1995ApJS..101..117K",
              "2003ASPC..298..137S",
              "2002MNRAS.337...87C",
              "2003AJ....125.1309C"
            ],
            "source": 54,
            "target": 82,
            "weight": 50
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 83,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 54,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1988MNRAS.232..431E"
            ],
            "source": 54,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998AJ....116.1724M",
              "1992MNRAS.257..225A",
              "1996AJ....112.2110L",
              "1999AJ....117.2308W",
              "1998ApJ...508..844G",
              "2003yCat.2246....0C",
              "1998PASP..110...79F",
              "2003AJ....126.1362B",
              "1992ApJS...78...87M",
              "2000AJ....119.2843C",
              "2002MNRAS.337...87C",
              "1999AJ....117.2329W",
              "2000asqu.book....1C",
              "2003AJ....125.1309C",
              "2000ApJ...540..825Y",
              "2002ApJ...578..151S",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "1989MNRAS.238..225S"
            ],
            "source": 54,
            "target": 86,
            "weight": 34
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 88,
            "weight": 13
          },
          {
            "overlap": [
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "2004MNRAS.352..285C",
              "1998PASP..110...79F",
              "2003AJ....126.1362B",
              "1999AJ....117.2329W"
            ],
            "source": 54,
            "target": 89,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 94,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 54,
            "target": 95,
            "weight": 3
          },
          {
            "overlap": [
              "2002AJ....123..485S"
            ],
            "source": 54,
            "target": 96,
            "weight": 2
          },
          {
            "overlap": [
              "1988MNRAS.232..431E",
              "1998PASP..110...79F",
              "1998ApJ...500..525S",
              "2000asqu.book....1C"
            ],
            "source": 54,
            "target": 98,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 99,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "2000AJ....120.1579Y"
            ],
            "source": 54,
            "target": 101,
            "weight": 5
          },
          {
            "overlap": [
              "2000AJ....120.1579Y"
            ],
            "source": 54,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 54,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 106,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 107,
            "weight": 4
          },
          {
            "overlap": [
              "2003AJ....126.2081A"
            ],
            "source": 54,
            "target": 109,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 110,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 54,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 121,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1980ApJ...236..351D"
            ],
            "source": 55,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1982AJ.....87.1165B"
            ],
            "source": 55,
            "target": 124,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 119,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1984ApJ...281...95P",
              "1980ApJ...236..351D"
            ],
            "source": 55,
            "target": 57,
            "weight": 5
          },
          {
            "overlap": [
              "1981PASP...93....5B"
            ],
            "source": 55,
            "target": 60,
            "weight": 2
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1995MmSAI..66..113R",
              "1994AJ....108.1476F",
              "1996ApJ...464...60L",
              "1993AJ....105..393K",
              "1997A&A...326..477Z"
            ],
            "source": 55,
            "target": 68,
            "weight": 14
          },
          {
            "overlap": [
              "1994AJ....108.1476F",
              "1997AJ....114.2205G",
              "1998PASP..110..934K",
              "1989ApJS...70..271B"
            ],
            "source": 55,
            "target": 69,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 78,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 90,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2000ApJ...530..660B",
              "1981PASP...93....5B"
            ],
            "source": 55,
            "target": 92,
            "weight": 5
          },
          {
            "overlap": [
              "1998ApJ...505...25B",
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 95,
            "weight": 6
          },
          {
            "overlap": [
              "1988ApJ...328..315M",
              "1992ApJ...388..310K"
            ],
            "source": 55,
            "target": 96,
            "weight": 4
          },
          {
            "overlap": [
              "1996ApJ...464...60L",
              "1995MmSAI..66..113R",
              "1994AJ....108.1476F",
              "1997A&AS..122..399P",
              "1997AJ....114.2205G",
              "1998PASP..110..934K",
              "1993AJ....105..393K",
              "1997A&A...326..477Z"
            ],
            "source": 55,
            "target": 98,
            "weight": 14
          },
          {
            "overlap": [
              "1995MmSAI..66..113R",
              "1997AJ....114.2205G",
              "1998PASP..110..934K",
              "1993AJ....105..393K"
            ],
            "source": 55,
            "target": 101,
            "weight": 9
          },
          {
            "overlap": [
              "2000ApJ...530..660B",
              "1987ApJS...63..295V",
              "1981PASP...93....5B"
            ],
            "source": 55,
            "target": 103,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "1993AJ....105..393K"
            ],
            "source": 55,
            "target": 111,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 55,
            "target": 113,
            "weight": 9
          },
          {
            "overlap": [
              "1997AJ....114.2205G"
            ],
            "source": 55,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1994ApJ...437L...1D",
              "1989Sci...246..897G",
              "1994ApJ...424L...1D",
              "1988ApJ...327..544D",
              "1986ApJ...302L...1D"
            ],
            "source": 56,
            "target": 116,
            "weight": 14
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1986ApJ...302L...1D"
            ],
            "source": 56,
            "target": 117,
            "weight": 4
          },
          {
            "overlap": [
              "1995adass...4...28E"
            ],
            "source": 56,
            "target": 118,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 56,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z",
              "1981ApJ...248L..57K"
            ],
            "source": 56,
            "target": 122,
            "weight": 8
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1994ApJ...424L...1D"
            ],
            "source": 56,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 56,
            "target": 59,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 56,
            "target": 66,
            "weight": 2
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1989Sci...246..897G",
              "1986ApJ...302L...1D",
              "1994ApJ...424L...1D",
              "1990Natur.343..726B"
            ],
            "source": 56,
            "target": 68,
            "weight": 11
          },
          {
            "overlap": [
              "1994ApJ...437L...1D",
              "1988AJ.....96.1791F",
              "1994ApJ...424L...1D",
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z",
              "1992ApJS...83...29S",
              "1989Sci...246..897G",
              "1994ApJ...428...43M",
              "1988ApJ...327..544D"
            ],
            "source": 56,
            "target": 69,
            "weight": 7
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 56,
            "target": 71,
            "weight": 3
          },
          {
            "overlap": [
              "1961cgcg.book.....Z"
            ],
            "source": 56,
            "target": 76,
            "weight": 4
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 56,
            "target": 90,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1976PASP...88..960S",
              "1988AJ.....96.1791F"
            ],
            "source": 56,
            "target": 91,
            "weight": 11
          },
          {
            "overlap": [
              "1976PASP...88..960S"
            ],
            "source": 56,
            "target": 95,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1988AJ.....96.1791F"
            ],
            "source": 56,
            "target": 97,
            "weight": 8
          },
          {
            "overlap": [
              "1994ApJ...428...43M"
            ],
            "source": 56,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1981ApJ...248L..57K"
            ],
            "source": 56,
            "target": 100,
            "weight": 6
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1994ApJ...424L...1D"
            ],
            "source": 56,
            "target": 101,
            "weight": 5
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G",
              "1986ApJ...302L...1D"
            ],
            "source": 56,
            "target": 103,
            "weight": 4
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1995adass...4...36A"
            ],
            "source": 56,
            "target": 110,
            "weight": 5
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1981AJ.....86..476J",
              "1988ApJ...327..544D",
              "1994ApJ...437..560W",
              "1990Natur.343..726B"
            ],
            "source": 56,
            "target": 111,
            "weight": 13
          },
          {
            "overlap": [
              "1994ApJ...424L...1D"
            ],
            "source": 56,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1998PASP..110..934K",
              "1990ApJS...72..433H"
            ],
            "source": 57,
            "target": 117,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K",
              "1992ASPC...25..417V"
            ],
            "source": 57,
            "target": 121,
            "weight": 9
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1979AJ.....84.1511T",
              "1994ApJ...424L...1D",
              "1997AJ....113..483R"
            ],
            "source": 57,
            "target": 116,
            "weight": 10
          },
          {
            "overlap": [
              "1980ApJ...236..351D",
              "1999MNRAS.303..188K",
              "1997ApJ...490..493N",
              "1998PASP..110..934K"
            ],
            "source": 57,
            "target": 123,
            "weight": 4
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 57,
            "target": 124,
            "weight": 2
          },
          {
            "overlap": [
              "1987gady.book.....B"
            ],
            "source": 57,
            "target": 125,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1997ApJ...476L...7C",
              "1999MNRAS.303..188K",
              "1998AJ....116.1573B",
              "1990ApJ...356..359H",
              "1996ApJ...470..724M",
              "1998PASP..110...79F",
              "1997ApJ...490..493N"
            ],
            "source": 57,
            "target": 119,
            "weight": 10
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1982ApJ...257..423H",
              "1996ApJ...457...61G"
            ],
            "source": 57,
            "target": 61,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 66,
            "weight": 2
          },
          {
            "overlap": [
              "1996ApJ...458..435C",
              "1989Sci...246..897G",
              "1996ApJ...470..724M",
              "1994ApJ...424L...1D",
              "1990ApJS...72..433H",
              "1997ApJ...476L...7C"
            ],
            "source": 57,
            "target": 68,
            "weight": 12
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1989Sci...246..897G",
              "1979AJ.....84.1511T",
              "1998PASP..110...79F",
              "1994ApJ...424L...1D",
              "1995AJ....109.1458R",
              "1994AJ....107..427D",
              "1990ApJS...72..433H"
            ],
            "source": 57,
            "target": 69,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1958ApJS....3..211A",
              "1992nrfa.book.....P"
            ],
            "source": 57,
            "target": 72,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F"
            ],
            "source": 57,
            "target": 78,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 57,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1992nrfa.book.....P"
            ],
            "source": 57,
            "target": 82,
            "weight": 2
          },
          {
            "overlap": [
              "1996ApJ...458..435C",
              "1990ApJ...356..359H",
              "1998PASP..110...79F",
              "1997ApJ...490..493N"
            ],
            "source": 57,
            "target": 84,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 57,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 57,
            "target": 86,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F",
              "1992nrfa.book.....P"
            ],
            "source": 57,
            "target": 89,
            "weight": 4
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1989Sci...246..897G",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K",
              "1998AJ....116....1D"
            ],
            "source": 57,
            "target": 90,
            "weight": 7
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 91,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 57,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 57,
            "target": 95,
            "weight": 8
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1990ApJS...72..433H"
            ],
            "source": 57,
            "target": 97,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F"
            ],
            "source": 57,
            "target": 98,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 57,
            "target": 100,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1979AJ.....84.1511T",
              "1998PASP..110...79F",
              "1994ApJ...424L...1D",
              "1990ApJS...72..433H"
            ],
            "source": 57,
            "target": 101,
            "weight": 13
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 102,
            "weight": 8
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 57,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 57,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 111,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1979AJ.....84.1511T",
              "1992nrfa.book.....P"
            ],
            "source": 57,
            "target": 112,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 57,
            "target": 113,
            "weight": 8
          },
          {
            "overlap": [
              "1992ASPC...25..432K",
              "1998PASP..110...79F",
              "1994ApJ...424L...1D"
            ],
            "source": 57,
            "target": 115,
            "weight": 5
          },
          {
            "overlap": [
              "1995ASPC...77...36A"
            ],
            "source": 58,
            "target": 110,
            "weight": 17
          },
          {
            "overlap": [
              "1995ASPC...77...36A"
            ],
            "source": 58,
            "target": 108,
            "weight": 99
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143..111G",
              "1994ExA.....5..205E",
              "1995ASSL..203..259S",
              "2000A&AS..143...85A",
              "1995VA.....39..217E",
              "1980CeMec..22...63M",
              "1997Ap&SS.247..189E",
              "1996adass...5..569E",
              "1994Sci...265..895S",
              "1996S&T....92...81E",
              "1995ASPC...77...28E",
              "2000A&AS..143...41K",
              "1996ASPC..101..569E",
              "1992ald2.proc..387M",
              "1991ASSL..171...79E"
            ],
            "source": 59,
            "target": 118,
            "weight": 59
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 62,
            "weight": 8
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 59,
            "target": 64,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 67,
            "weight": 14
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 70,
            "weight": 8
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1995ASSL..203..259S",
              "1993ASPC...52..132K",
              "1991ASSL..171...79E"
            ],
            "source": 59,
            "target": 71,
            "weight": 20
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1994ExA.....5..205E",
              "1993ASPC...52..132K",
              "2000A&AS..143...85A",
              "1995VA.....39..217E",
              "1997Ap&SS.247..189E",
              "1995ASPC...77...28E",
              "2000A&AS..143...41K",
              "1992ald2.proc..387M"
            ],
            "source": 59,
            "target": 73,
            "weight": 50
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 74,
            "weight": 21
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 83,
            "weight": 32
          },
          {
            "overlap": [
              "1994ExA.....5..205E"
            ],
            "source": 59,
            "target": 87,
            "weight": 16
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 88,
            "weight": 21
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 94,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 99,
            "weight": 17
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "1992ASPC...25...47M"
            ],
            "source": 59,
            "target": 104,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 106,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 107,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "1993adass...2..132K",
              "2000A&AS..143...41K"
            ],
            "source": 59,
            "target": 110,
            "weight": 14
          },
          {
            "overlap": [
              "1995ASPC...77...28E",
              "1993ASPC...52..132K"
            ],
            "source": 59,
            "target": 114,
            "weight": 25
          },
          {
            "overlap": [
              "2009ApJS..182..543A"
            ],
            "source": 60,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "2003SPIE.4834..161D"
            ],
            "source": 60,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "2012AJ....143..102G"
            ],
            "source": 60,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2012ApJ...758...25H",
              "2012AJ....143..102G"
            ],
            "source": 60,
            "target": 61,
            "weight": 7
          },
          {
            "overlap": [
              "1979A&A....80..155L",
              "1989agna.book.....O",
              "2002ApJS..142...35K",
              "2006ApJ...647..970L",
              "2004ApJ...613..898T",
              "2004ApJ...617..240K"
            ],
            "source": 60,
            "target": 63,
            "weight": 8
          },
          {
            "overlap": [
              "2005ApJ...635L.125G"
            ],
            "source": 60,
            "target": 65,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W"
            ],
            "source": 60,
            "target": 72,
            "weight": 4
          },
          {
            "overlap": [
              "2005ApJ...635L.125G"
            ],
            "source": 60,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "2012A&A...547A..79F",
              "2012ApJS..200....8K",
              "2012ApJ...758...25H",
              "2003PASP..115..763C",
              "1996AJ....112..839C",
              "2005ApJ...635L.125G",
              "2012AJ....143..102G",
              "2003MNRAS.344.1000B",
              "2005PASP..117..227K",
              "2004ApJ...617..240K",
              "2000ApJ...533..682C",
              "2003ApJS..149..289B",
              "2003SPIE.4834..161D"
            ],
            "source": 60,
            "target": 90,
            "weight": 23
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2005ApJ...635L.125G",
              "2003MNRAS.344.1000B",
              "2005PASP..117..227K",
              "2003MNRAS.346.1055K",
              "2000ApJ...533..682C",
              "1981PASP...93....5B",
              "2004ApJ...613..898T"
            ],
            "source": 60,
            "target": 92,
            "weight": 13
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2005PASP..117..227K"
            ],
            "source": 60,
            "target": 96,
            "weight": 6
          },
          {
            "overlap": [
              "2007ApJ...660L..43N",
              "2003MNRAS.346.1055K",
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2003MNRAS.344.1000B",
              "2012AJ....143..102G",
              "2000ApJ...533..682C",
              "1981PASP...93....5B"
            ],
            "source": 60,
            "target": 103,
            "weight": 10
          },
          {
            "overlap": [
              "2006ApJ...647..970L"
            ],
            "source": 60,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2012AJ....143..102G"
            ],
            "source": 60,
            "target": 109,
            "weight": 5
          },
          {
            "overlap": [
              "1989agna.book.....O"
            ],
            "source": 60,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2005PASP..117.1411F"
            ],
            "source": 61,
            "target": 117,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 61,
            "target": 121,
            "weight": 2
          },
          {
            "overlap": [
              "2009ApJ...692.1033V",
              "2004A&A...425..367B",
              "2012AJ....143..102G",
              "2011MNRAS.412.2095H",
              "2005PASP..117.1411F",
              "2007ApJ...655...98N"
            ],
            "source": 61,
            "target": 123,
            "weight": 7
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 61,
            "target": 124,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 61,
            "target": 65,
            "weight": 6
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2005ApJ...624...59H",
              "1958ApJS....3..211A",
              "2004MNRAS.350..893H",
              "2009PASJ...61..833H",
              "2002SPIE.4836...73W",
              "2007ApJ...669..714M",
              "2007A&A...462..459G",
              "2009ApJ...702..980K",
              "2005ApJ...635L.125G",
              "2008ApJ...673..163S",
              "2004A&A...425..367B",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2001ApJ...557L..89W",
              "2007ApJ...660..239K",
              "2007A&A...462..875S",
              "2007A&A...470..821D",
              "2008ApJ...684..794K"
            ],
            "source": 61,
            "target": 72,
            "weight": 33
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 61,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 61,
            "target": 85,
            "weight": 9
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1958ApJS....3..211A",
              "1998SPIE.3355..285F",
              "2012ApJ...758...25H",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2012AJ....143..102G",
              "2006ApJ...643..128W"
            ],
            "source": 61,
            "target": 90,
            "weight": 12
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W",
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W"
            ],
            "source": 61,
            "target": 92,
            "weight": 7
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 61,
            "target": 95,
            "weight": 3
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W"
            ],
            "source": 61,
            "target": 96,
            "weight": 7
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 61,
            "target": 100,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2010ApJ...709..832G",
              "2012AJ....143..102G",
              "2005PASP..117.1411F"
            ],
            "source": 61,
            "target": 103,
            "weight": 7
          },
          {
            "overlap": [
              "2012AJ....143..102G",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W"
            ],
            "source": 61,
            "target": 109,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "1988alds.proc..335H",
              "2000A&AS..143...61E"
            ],
            "source": 62,
            "target": 118,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 62,
            "target": 67,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 62,
            "target": 70,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 62,
            "target": 73,
            "weight": 14
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 62,
            "target": 74,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 62,
            "target": 83,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 62,
            "target": 88,
            "weight": 18
          },
          {
            "overlap": [
              "1999ASPC..172..291A",
              "2000A&AS..143...41K"
            ],
            "source": 62,
            "target": 94,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 62,
            "target": 99,
            "weight": 7
          },
          {
            "overlap": [
              "1997PASP..109.1278S",
              "2000A&AS..143...85A",
              "1999adass...8..287L",
              "1993adass...2..137W",
              "1995VA.....39R.272S",
              "1999ASPC..172..287L",
              "1988alds.proc..335H",
              "2000A&AS..143...61E",
              "1993ASPC...52..137W"
            ],
            "source": 62,
            "target": 104,
            "weight": 24
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 62,
            "target": 106,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "1998ASPC..153..107B",
              "1999ASPC..172..291A"
            ],
            "source": 62,
            "target": 107,
            "weight": 16
          },
          {
            "overlap": [
              "1988alds.proc..323E",
              "2000A&AS..143...41K",
              "1999adass...8..287L",
              "1999ASPC..172..291A",
              "1999ASPC..172..287L",
              "1995VA.....39R.272S",
              "1988alds.proc..335H",
              "2000A&AS..143...61E"
            ],
            "source": 62,
            "target": 110,
            "weight": 26
          },
          {
            "overlap": [
              "2006ApJ...647..303B"
            ],
            "source": 63,
            "target": 89,
            "weight": 1
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 63,
            "target": 98,
            "weight": 1
          },
          {
            "overlap": [
              "1989ApJ...345..245C",
              "2004ApJ...613..898T",
              "1998ARA&A..36..189K"
            ],
            "source": 63,
            "target": 92,
            "weight": 4
          },
          {
            "overlap": [
              "1998ARA&A..36..189K"
            ],
            "source": 63,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJ...647..303B"
            ],
            "source": 63,
            "target": 125,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJ...647..970L",
              "1989ApJ...347..875S",
              "1998ApJ...500..525S",
              "2006ApJ...647..303B"
            ],
            "source": 63,
            "target": 105,
            "weight": 6
          },
          {
            "overlap": [
              "2004ApJ...617..240K"
            ],
            "source": 63,
            "target": 90,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJ...647..303B"
            ],
            "source": 63,
            "target": 78,
            "weight": 1
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 63,
            "target": 82,
            "weight": 1
          },
          {
            "overlap": [
              "1989agna.book.....O"
            ],
            "source": 63,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 63,
            "target": 86,
            "weight": 1
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 64,
            "target": 118,
            "weight": 16
          },
          {
            "overlap": [
              "2005PASP..117.1411F"
            ],
            "source": 65,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "2002AJ....124.1810S",
              "2005PASP..117.1411F"
            ],
            "source": 65,
            "target": 123,
            "weight": 3
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 65,
            "target": 125,
            "weight": 2
          },
          {
            "overlap": [
              "2005MNRAS.359..237P",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 65,
            "target": 72,
            "weight": 10
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 65,
            "target": 82,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 65,
            "target": 85,
            "weight": 8
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 65,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2005ApJ...635L.125G",
              "2002AJ....124.1810S",
              "2006ApJ...643..128W"
            ],
            "source": 65,
            "target": 90,
            "weight": 8
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W"
            ],
            "source": 65,
            "target": 92,
            "weight": 6
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002AJ....124.1810S",
              "2001AJ....122.2267E",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 65,
            "target": 96,
            "weight": 13
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 65,
            "target": 103,
            "weight": 5
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2006ApJ...643..128W",
              "1987ApJ...313...59D"
            ],
            "source": 65,
            "target": 109,
            "weight": 9
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1979AJ.....84.1511T"
            ],
            "source": 66,
            "target": 116,
            "weight": 5
          },
          {
            "overlap": [
              "1986AJ.....92.1248T",
              "1982AJ.....87..945K",
              "1986ApJ...302L...1D"
            ],
            "source": 66,
            "target": 119,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 66,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 66,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1986AJ.....92.1248T"
            ],
            "source": 66,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1982ApJ...254..437D"
            ],
            "source": 66,
            "target": 124,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1985AJ.....90.1665K",
              "1982ApJ...254..437D"
            ],
            "source": 66,
            "target": 68,
            "weight": 8
          },
          {
            "overlap": [
              "1987ApJ...314..493K",
              "1979AJ.....84.1511T",
              "1986ApJ...308..530F",
              "1977AJ.....82..187F"
            ],
            "source": 66,
            "target": 69,
            "weight": 4
          },
          {
            "overlap": [
              "1982AJ.....87..945K"
            ],
            "source": 66,
            "target": 84,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 66,
            "target": 90,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1986AJ.....92.1238P",
              "1979AJ.....84.1511T"
            ],
            "source": 66,
            "target": 91,
            "weight": 13
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 66,
            "target": 95,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJ...270...20B"
            ],
            "source": 66,
            "target": 100,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1985AJ.....90.1665K"
            ],
            "source": 66,
            "target": 101,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 66,
            "target": 102,
            "weight": 11
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 66,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 66,
            "target": 111,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 66,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 67,
            "target": 118,
            "weight": 11
          },
          {
            "overlap": [
              "2005JASIS..56..111K",
              "2000A&AS..143...41K"
            ],
            "source": 67,
            "target": 70,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 67,
            "target": 71,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "1993ASPC...52..132K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 67,
            "target": 73,
            "weight": 21
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 67,
            "target": 74,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 67,
            "target": 83,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 67,
            "target": 88,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 67,
            "target": 94,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "1993ASPC...52..132K",
              "1992ald2.proc...85K",
              "2005JASIS..56...36K"
            ],
            "source": 67,
            "target": 99,
            "weight": 26
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 67,
            "target": 104,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2002SPIE.4847..238K",
              "2005JASIS..56...36K",
              "2000A&AS..143...41K",
              "1992ald2.proc...85K"
            ],
            "source": 67,
            "target": 106,
            "weight": 32
          },
          {
            "overlap": [
              "2005JASIS..56..111K",
              "2000A&AS..143...41K",
              "1993ASPC...52..132K",
              "2005JASIS..56...36K"
            ],
            "source": 67,
            "target": 107,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "1993ASPC...52..132K",
              "2000A&AS..143...61E"
            ],
            "source": 67,
            "target": 110,
            "weight": 11
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 67,
            "target": 114,
            "weight": 9
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1989Sci...246..897G",
              "1986ApJ...302L...1D",
              "1994ApJ...424L...1D",
              "1994AJ....108..437M",
              "1996ApJ...470..172S"
            ],
            "source": 68,
            "target": 116,
            "weight": 16
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1986ApJ...302L...1D",
              "1990ApJS...72..433H"
            ],
            "source": 68,
            "target": 117,
            "weight": 7
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1976ApJ...203..297S",
              "1982ApJ...254..437D"
            ],
            "source": 68,
            "target": 124,
            "weight": 9
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1996ApJ...470..724M",
              "1997ApJ...476L...7C"
            ],
            "source": 68,
            "target": 119,
            "weight": 5
          },
          {
            "overlap": [
              "1997ApJS..111....1S",
              "1994ApJ...424L...1D",
              "1995ApJS...99..391H",
              "1990ApJS...72..433H",
              "1994AJ....108.1476F",
              "1989Sci...246..897G",
              "1994ApJ...428...43M",
              "1996ApJS..104..199W"
            ],
            "source": 68,
            "target": 69,
            "weight": 7
          },
          {
            "overlap": [
              "1996ApJS..104..199W"
            ],
            "source": 68,
            "target": 75,
            "weight": 16
          },
          {
            "overlap": [
              "1996ApJ...458..435C"
            ],
            "source": 68,
            "target": 84,
            "weight": 3
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 68,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1996ApJ...470..172S"
            ],
            "source": 68,
            "target": 90,
            "weight": 4
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 68,
            "target": 91,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 68,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 68,
            "target": 96,
            "weight": 2
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1989Sci...246..897G",
              "1976ApJ...203..297S",
              "1990ApJS...72..433H"
            ],
            "source": 68,
            "target": 97,
            "weight": 19
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1996ApJ...470..172S",
              "1996ApJ...464...60L",
              "1995MmSAI..66..113R",
              "1994AJ....108.1476F",
              "1976ApJ...203..297S",
              "1992ApJ...390..338L",
              "1993AJ....105..393K",
              "1997A&A...326..477Z",
              "1996MNRAS.280..235E"
            ],
            "source": 68,
            "target": 98,
            "weight": 21
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1997A&A...325..954V",
              "1995MmSAI..66..113R",
              "1995AJ....109.2368T",
              "1989Sci...246..897G",
              "1995ApJS...99..391H",
              "1989AJ.....97..633G",
              "1994ApJ...424L...1D",
              "1996ApJS..104..199W",
              "1996ApJ...470..172S",
              "1992ApJ...390..338L",
              "1993AJ....105..393K",
              "1990ApJS...72..433H",
              "1990SPIE.1235..747F",
              "1985AJ.....90.1665K"
            ],
            "source": 68,
            "target": 101,
            "weight": 40
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1986ApJ...302L...1D"
            ],
            "source": 68,
            "target": 103,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1993AJ....105..393K",
              "1990Natur.343..726B"
            ],
            "source": 68,
            "target": 111,
            "weight": 9
          },
          {
            "overlap": [
              "1997A&A...325..954V",
              "1995ApJS...99..391H",
              "1994ApJ...424L...1D",
              "1996ApJ...470..172S"
            ],
            "source": 68,
            "target": 115,
            "weight": 10
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1994ApJ...437L...1D",
              "1976AJ.....81..952H",
              "1984AJ.....89.1310D",
              "1989Sci...246..897G",
              "1997AJ....113..185M",
              "1982ApJ...253..423D",
              "1979AJ.....84.1511T",
              "1994ApJ...431..569P",
              "1988ApJ...327..544D",
              "1994ApJ...424L...1D",
              "1990AJ.....99.2019L",
              "1993AJ....106..676A",
              "1995AJ....110..477M",
              "1993AJ....105.1637H",
              "1994AJ....108.1987A"
            ],
            "source": 69,
            "target": 116,
            "weight": 15
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1978AJ.....83.1549K",
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z",
              "1984A&A...138...85F",
              "1982AJ.....87.1355G"
            ],
            "source": 69,
            "target": 122,
            "weight": 5
          },
          {
            "overlap": [
              "1990ApJS...72..433H",
              "1989Sci...246..897G",
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 123,
            "weight": 0
          },
          {
            "overlap": [
              "1993AJ....105..788F",
              "1988AJ.....95..999C",
              "1988AJ.....96.1775O",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1968cgcg.book.....Z"
            ],
            "source": 69,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "1996ApJS..104..199W"
            ],
            "source": 69,
            "target": 75,
            "weight": 6
          },
          {
            "overlap": [
              "1961cgcg.book.....Z"
            ],
            "source": 69,
            "target": 76,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 78,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 80,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 69,
            "target": 82,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 69,
            "target": 84,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1996ApJS..105..209I"
            ],
            "source": 69,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 69,
            "target": 86,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 89,
            "weight": 1
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1989Sci...246..897G",
              "1998PASP..110..934K",
              "1982ApJ...253..423D"
            ],
            "source": 69,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1973ugcg.book.....N",
              "1987ApJ...318..161S",
              "1979AJ.....84.1511T",
              "1975ApJ...199....1G",
              "1988AJ.....96.1791F"
            ],
            "source": 69,
            "target": 91,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 95,
            "weight": 4
          },
          {
            "overlap": [
              "1987ApJ...318..161S",
              "1989Sci...246..897G",
              "1986AJ.....91..705B",
              "1990ApJS...72..433H",
              "1988AJ.....96.1791F"
            ],
            "source": 69,
            "target": 97,
            "weight": 8
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1990AJ.....99.1004B",
              "1990AJ.....99.2019L",
              "1994AJ....108.1476F",
              "1998PASP..110...79F",
              "1997AJ....114.2205G",
              "1997AJ....113..185M",
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 98,
            "weight": 6
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1982AJ.....87.1355G",
              "1978AJ.....83.1549K"
            ],
            "source": 69,
            "target": 100,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1994ApJ...424L...1D",
              "1995ApJS...99..391H",
              "1990ApJS...72..433H",
              "1979AJ.....84.1511T",
              "1989Sci...246..897G",
              "1998PASP..110..934K",
              "1982ialo.coll..259L",
              "1982ApJ...253..423D",
              "1990AJ....100.1405W",
              "1997AJ....114.2205G",
              "1996ApJS..104..199W"
            ],
            "source": 69,
            "target": 101,
            "weight": 11
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 69,
            "target": 102,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G"
            ],
            "source": 69,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 105,
            "weight": 1
          },
          {
            "overlap": [
              "1995MNRAS.276.1341J"
            ],
            "source": 69,
            "target": 109,
            "weight": 1
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1990AJ.....99.2019L",
              "1979AJ.....84.1511T",
              "1988ApJ...327..544D"
            ],
            "source": 69,
            "target": 111,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1979AJ.....84.1511T"
            ],
            "source": 69,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 113,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1994ApJ...424L...1D",
              "1995ApJS...99..391H",
              "1990A&AS...82..391B",
              "1997AJ....114.2205G"
            ],
            "source": 69,
            "target": 115,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 118,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 73,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 74,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 83,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 88,
            "weight": 38
          },
          {
            "overlap": [
              "2006cs........4061H",
              "2005IPM....41.1395K",
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 94,
            "weight": 48
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 99,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 106,
            "weight": 13
          },
          {
            "overlap": [
              "2005JASIS..56..111K",
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 107,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 70,
            "target": 110,
            "weight": 6
          },
          {
            "overlap": [
              "1995ASSL..203..259S",
              "1991ASSL..171...79E"
            ],
            "source": 71,
            "target": 118,
            "weight": 8
          },
          {
            "overlap": [
              "1997ASPC..125..357A",
              "1993ApJ...412..301A",
              "1975ApJ...201..249C",
              "1991ApJ...366...64C",
              "1981PASP...93..269A",
              "1993PASP..105..437A"
            ],
            "source": 71,
            "target": 93,
            "weight": 56
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 71,
            "target": 99,
            "weight": 9
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 71,
            "target": 73,
            "weight": 6
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 71,
            "target": 104,
            "weight": 3
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 71,
            "target": 107,
            "weight": 6
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1993ASPC...52..132K"
            ],
            "source": 71,
            "target": 110,
            "weight": 8
          },
          {
            "overlap": [
              "1995VA.....39..227O",
              "1993ASPC...52..132K"
            ],
            "source": 71,
            "target": 106,
            "weight": 15
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 71,
            "target": 114,
            "weight": 13
          },
          {
            "overlap": [
              "2006AJ....132.1275R",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1989ApJS...70....1A",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "2005PASP..117.1411F"
            ],
            "source": 72,
            "target": 117,
            "weight": 16
          },
          {
            "overlap": [
              "1996MNRAS.281..799E",
              "1989ApJS...70....1A",
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 119,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998SPIE.3355..324R",
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 121,
            "weight": 8
          },
          {
            "overlap": [
              "2004ApJ...610..745L",
              "2006AJ....132.1275R",
              "2004A&A...425..367B",
              "1998PASP..110..934K",
              "2003AJ....126.2152R",
              "2005PASP..117.1411F"
            ],
            "source": 72,
            "target": 123,
            "weight": 8
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1982ApJ...253..485P"
            ],
            "source": 72,
            "target": 124,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 78,
            "weight": 1
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 80,
            "weight": 4
          },
          {
            "overlap": [
              "1992nrfa.book.....P"
            ],
            "source": 72,
            "target": 82,
            "weight": 2
          },
          {
            "overlap": [
              "2007AJ....134.1360K",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "1998SPIE.3355..324R",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2007ApJS..172..117M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 72,
            "target": 85,
            "weight": 21
          },
          {
            "overlap": [
              "1998SPIE.3355..577M"
            ],
            "source": 72,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992nrfa.book.....P"
            ],
            "source": 72,
            "target": 89,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1958ApJS....3..211A",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 72,
            "target": 90,
            "weight": 15
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1998SPIE.3355..324R",
              "2002SPIE.4836...73W",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W"
            ],
            "source": 72,
            "target": 92,
            "weight": 15
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 95,
            "weight": 7
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 72,
            "target": 96,
            "weight": 13
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 72,
            "target": 100,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 72,
            "target": 103,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 72,
            "target": 109,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992nrfa.book.....P"
            ],
            "source": 72,
            "target": 112,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 72,
            "target": 113,
            "weight": 9
          },
          {
            "overlap": [
              "1995VA.....39..161C",
              "2000A&AS..143..111G",
              "1994ExA.....5..205E",
              "2000A&AS..143...41K",
              "1995VA.....39..217E",
              "2000A&AS..143...85A",
              "1997Ap&SS.247..189E",
              "2000A&AS..143...61E",
              "1995ASPC...77...28E",
              "1992ald2.proc..387M"
            ],
            "source": 73,
            "target": 118,
            "weight": 42
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 73,
            "target": 74,
            "weight": 24
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A"
            ],
            "source": 73,
            "target": 83,
            "weight": 37
          },
          {
            "overlap": [
              "1994ExA.....5..205E"
            ],
            "source": 73,
            "target": 87,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 73,
            "target": 88,
            "weight": 24
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 73,
            "target": 94,
            "weight": 10
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 73,
            "target": 99,
            "weight": 20
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 73,
            "target": 104,
            "weight": 14
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "1993ASPC...52..132K"
            ],
            "source": 73,
            "target": 106,
            "weight": 24
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "1993ASPC...52..132K"
            ],
            "source": 73,
            "target": 107,
            "weight": 14
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 73,
            "target": 110,
            "weight": 17
          },
          {
            "overlap": [
              "1995ASPC...77...28E",
              "1993ASPC...52..132K"
            ],
            "source": 73,
            "target": 114,
            "weight": 29
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 74,
            "target": 118,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 74,
            "target": 83,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 74,
            "target": 88,
            "weight": 92
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 74,
            "target": 94,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 74,
            "target": 99,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 74,
            "target": 106,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 74,
            "target": 107,
            "weight": 27
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 74,
            "target": 110,
            "weight": 16
          },
          {
            "overlap": [
              "1996ApJS..104..199W"
            ],
            "source": 75,
            "target": 101,
            "weight": 16
          },
          {
            "overlap": [
              "1979ApJ...234...13G",
              "1977ApJS...34..425D",
              "1977ApJ...217..385G",
              "1977ApJ...212L.107D",
              "1983ApJ...264....1S",
              "1983ApJ...273...16M",
              "1961cgcg.book.....Z",
              "1977AJ.....82..249S",
              "1982ApJ...263L..47K"
            ],
            "source": 76,
            "target": 122,
            "weight": 49
          },
          {
            "overlap": [
              "1961CGCG..C......0Z"
            ],
            "source": 76,
            "target": 97,
            "weight": 9
          },
          {
            "overlap": [
              "1983ApJ...264....1S",
              "1982ApJ...263L..47K",
              "1977ApJ...217..385G",
              "1979ApJ...234...13G",
              "1983ApJ...273...16M",
              "1977AJ.....82..249S",
              "1977ApJS...34..425D",
              "1977ApJ...212L.107D"
            ],
            "source": 76,
            "target": 100,
            "weight": 47
          },
          {
            "overlap": [
              "1991A&A...248..485D"
            ],
            "source": 77,
            "target": 125,
            "weight": 3
          },
          {
            "overlap": [
              "1997NewA....2..139K",
              "1983ApJ...266L..11A",
              "1989ApJ...341L..41K",
              "1995ApJ...442..142O",
              "1995MNRAS.277.1354I",
              "1993ApJ...409L..13K",
              "1996ASPC...92..424P",
              "1995AJ....109.1071P",
              "1996fogh.conf..424P"
            ],
            "source": 77,
            "target": 79,
            "weight": 33
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 117,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 121,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "2006ApJ...647..303B",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "2005MNRAS.363..223G",
              "1991AJ....101..562L",
              "2003ApJ...596.1015S",
              "2006ApJ...651..392S",
              "2003ApJ...599.1129Y",
              "1988Natur.331..687H",
              "2005ApJ...620..744G",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "1967BOTT....4...86P",
              "2006MNRAS.372..174B",
              "2006ApJ...640L..35B",
              "2000ApJ...544..437P"
            ],
            "source": 78,
            "target": 125,
            "weight": 32
          },
          {
            "overlap": [
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "1995ApJS..101..117K",
              "2003ApJ...599.1129Y",
              "2003ApJ...596.1015S",
              "2003AJ....126.1362B",
              "1999MNRAS.310..645W",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "2005ApJ...620..744G",
              "1992A&AS...96..269S"
            ],
            "source": 78,
            "target": 80,
            "weight": 21
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "2004MNRAS.352..285C",
              "2003AJ....126.1362B",
              "1995ApJS..101..117K"
            ],
            "source": 78,
            "target": 82,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 78,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1998ARA&A..36..435M",
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 85,
            "weight": 3
          },
          {
            "overlap": [
              "2003A&A...397..899S",
              "1998PASP..110...79F",
              "2003AJ....126.1362B",
              "2000ApJ...539..928Y"
            ],
            "source": 78,
            "target": 86,
            "weight": 6
          },
          {
            "overlap": [
              "2006ApJ...648..976M",
              "2006ApJ...653.1203L",
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "2006astro.ph..9046O",
              "1997ApJ...482..677Y",
              "2006ApJ...647..303B",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "1991AJ....101..562L",
              "1993ASPC...45..360L",
              "2004MNRAS.352..285C",
              "2007AJ....133..882K",
              "2006MNRAS.368..221G",
              "1998PASP..110...79F",
              "2007MNRAS.376L..29G",
              "2006ApJ...636L..37F",
              "2003AJ....126.1362B",
              "2003ApJ...599.1129Y",
              "2007ApJ...656..709P",
              "2006ApJ...651..392S",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "2005ApJ...620..744G",
              "2002MNRAS.333..463D",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G",
              "2006MNRAS.372..174B",
              "2005A&A...444L..61H",
              "2007A&A...461..651D",
              "2006ApJ...640L..35B",
              "2000ApJ...544..437P",
              "2006ApJ...653.1194B"
            ],
            "source": 78,
            "target": 89,
            "weight": 48
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 90,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 95,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 98,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 101,
            "weight": 4
          },
          {
            "overlap": [
              "1955ApJ...121..161S"
            ],
            "source": 78,
            "target": 103,
            "weight": 1
          },
          {
            "overlap": [
              "1998ARA&A..36..435M",
              "1998PASP..110..934K",
              "1992A&AS...96..269S",
              "2007ApJ...660..311B",
              "2006ApJ...647..303B"
            ],
            "source": 78,
            "target": 105,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 78,
            "target": 113,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 78,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 79,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1992AJ....104..340L"
            ],
            "source": 79,
            "target": 98,
            "weight": 5
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 79,
            "target": 101,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 80,
            "target": 117,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 121,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "1961BAN....15..265B",
              "2003ApJ...599.1129Y",
              "2003ApJ...596.1015S",
              "2003ApJ...586L.127G",
              "1988Natur.331..687H",
              "2005ApJ...620..744G",
              "1992A&AS...96..269S"
            ],
            "source": 80,
            "target": 125,
            "weight": 16
          },
          {
            "overlap": [
              "2003AJ....125..984M",
              "1998ApJ...508..844G",
              "2004AJ....127..899S",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "2003AJ....126.1362B",
              "1999AJ....117.2308W",
              "2004AJ....127.1555B",
              "1995ApJS..101..117K",
              "2002MNRAS.337...87C",
              "2003AJ....125.1309C"
            ],
            "source": 80,
            "target": 82,
            "weight": 19
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 80,
            "target": 85,
            "weight": 5
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "1998ApJ...508..844G",
              "2003AJ....126.1362B",
              "2002MNRAS.337...87C",
              "2003AJ....125.1309C",
              "2000ApJ...540..825Y",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P"
            ],
            "source": 80,
            "target": 86,
            "weight": 16
          },
          {
            "overlap": [
              "2003AJ....125..984M",
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "2003AJ....126.1362B",
              "2003ApJ...599.1129Y",
              "1988AJ.....96..560C",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "2005ApJ...620..744G",
              "1992A&AS...96..269S"
            ],
            "source": 80,
            "target": 89,
            "weight": 20
          },
          {
            "overlap": [
              "2003AJ....125..984M",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 90,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 80,
            "target": 92,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 80,
            "target": 95,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992A&AS...96..269S"
            ],
            "source": 80,
            "target": 105,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 113,
            "weight": 11
          },
          {
            "overlap": [
              "1998ASPC..145..466F"
            ],
            "source": 81,
            "target": 110,
            "weight": 17
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 82,
            "target": 119,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 82,
            "target": 121,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...629..268H",
              "2006ApJS..162...38A"
            ],
            "source": 82,
            "target": 125,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 82,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "1988MNRAS.232..431E"
            ],
            "source": 82,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "2003ApJ...599.1082M",
              "2002ApJ...569..245N",
              "1996AJ....112.1487H",
              "2002ApJ...578..151S",
              "1998ApJ...508..844G",
              "1998PASP..110...79F",
              "1996ApJ...465..278J",
              "1992ApJS...78...87M",
              "2000AJ....119.2843C",
              "1999AJ....117.2329W",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "1998AJ....116.1724M",
              "2003AJ....126.1362B",
              "1994Natur.370..194I",
              "2002AJ....124..931B",
              "1999AJ....117.2308W",
              "2003MNRAS.340L..21I",
              "1992MNRAS.257..225A",
              "1996AJ....112.2110L",
              "1998ApJ...500..525S",
              "2003ApJ...588..824Y",
              "1989MNRAS.238..225S",
              "2001AJ....122.1397H",
              "2002MNRAS.337...87C",
              "2003AJ....125.1309C"
            ],
            "source": 82,
            "target": 86,
            "weight": 34
          },
          {
            "overlap": [
              "2006ApJS..162...38A",
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "2003AJ....125..984M",
              "2004MNRAS.352..285C",
              "1998PASP..110...79F",
              "2003AJ....126.1362B",
              "2004AJ....128.2474M",
              "2005AJ....130.1097B",
              "1999AJ....117.2329W",
              "2005MNRAS.362..349C",
              "1992nrfa.book.....P"
            ],
            "source": 82,
            "target": 89,
            "weight": 15
          },
          {
            "overlap": [
              "2003AJ....125..984M"
            ],
            "source": 82,
            "target": 90,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 82,
            "target": 95,
            "weight": 2
          },
          {
            "overlap": [
              "1988MNRAS.232..431E",
              "1998PASP..110...79F",
              "1998ApJ...500..525S"
            ],
            "source": 82,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 82,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 82,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "1992nrfa.book.....P"
            ],
            "source": 82,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 82,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A"
            ],
            "source": 83,
            "target": 118,
            "weight": 24
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 83,
            "target": 88,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 83,
            "target": 94,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 83,
            "target": 99,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A"
            ],
            "source": 83,
            "target": 104,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 83,
            "target": 106,
            "weight": 48
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 83,
            "target": 107,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K"
            ],
            "source": 83,
            "target": 110,
            "weight": 16
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z"
            ],
            "source": 84,
            "target": 117,
            "weight": 7
          },
          {
            "overlap": [
              "1997ApJ...478..462C",
              "1998ApJ...492...45S",
              "1999ApJ...517L..23G",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "1998astro.ph..9268K",
              "1982AJ.....87..945K",
              "1990ApJ...356..359H",
              "1998PASP..110...79F",
              "2001ApJ...548L.139D",
              "1999MNRAS.309..610D",
              "2000AJ....120..523R",
              "1997ApJ...490..493N",
              "2000AJ....120.2338R",
              "2001AJ....122.1289T"
            ],
            "source": 84,
            "target": 119,
            "weight": 26
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 84,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1997ApJ...478..462C",
              "1999ApJ...517L..23G",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "2000AJ....120..523R",
              "1997ApJ...490..493N",
              "2000AJ....120.2338R",
              "2001ApJ...548L.139D"
            ],
            "source": 84,
            "target": 123,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 84,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 84,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 84,
            "target": 95,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 84,
            "target": 98,
            "weight": 2
          },
          {
            "overlap": [
              "2000AJ....119.2498J",
              "1998PASP..110...79F"
            ],
            "source": 84,
            "target": 101,
            "weight": 5
          },
          {
            "overlap": [
              "1993MNRAS.264...71V"
            ],
            "source": 84,
            "target": 111,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 84,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998SPIE.3355..324R",
              "2010ApJ...709..832G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 85,
            "target": 117,
            "weight": 12
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 121,
            "weight": 8
          },
          {
            "overlap": [
              "2010PASP..122.1258W",
              "2005AJ....130.1502M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 85,
            "target": 123,
            "weight": 6
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 85,
            "target": 124,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..577M"
            ],
            "source": 85,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "2008PASP..120.1222F",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2003MNRAS.341...33K"
            ],
            "source": 85,
            "target": 90,
            "weight": 22
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2008PASP..120.1222F",
              "1998SPIE.3355..285F",
              "1976ApJ...203..297S",
              "1998SPIE.3355..577M",
              "1998SPIE.3355..324R",
              "2005ApJ...635L.125G",
              "1979ApJ...232..352S",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 85,
            "target": 92,
            "weight": 21
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 95,
            "weight": 7
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "1976ApJ...203..297S",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2003MNRAS.341...33K"
            ],
            "source": 85,
            "target": 96,
            "weight": 18
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 85,
            "target": 97,
            "weight": 4
          },
          {
            "overlap": [
              "1988MNRAS.232..431E",
              "1997ApJ...482..104S",
              "1976ApJ...203..297S",
              "1998SPIE.3355..577M",
              "1979ApJ...232..352S",
              "2000ApJ...545..781D",
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 98,
            "weight": 14
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2008PASP..120.1222F",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2003MNRAS.341...33K",
              "2010AJ....139.1857W",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 85,
            "target": 103,
            "weight": 11
          },
          {
            "overlap": [
              "1998ARA&A..36..435M",
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 105,
            "weight": 4
          },
          {
            "overlap": [
              "2008PASP..120.1222F",
              "2010ApJ...709..832G",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 85,
            "target": 109,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 85,
            "target": 113,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 86,
            "target": 119,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 86,
            "target": 121,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "2003AJ....126.1362B",
              "1999AJ....117.2329W"
            ],
            "source": 86,
            "target": 89,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..577M"
            ],
            "source": 86,
            "target": 90,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..577M"
            ],
            "source": 86,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 86,
            "target": 95,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..577M"
            ],
            "source": 86,
            "target": 96,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998SPIE.3355..577M",
              "1998PASP..110...79F",
              "2000asqu.book....1C"
            ],
            "source": 86,
            "target": 98,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 86,
            "target": 101,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 86,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 86,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1994ExA.....5..205E"
            ],
            "source": 87,
            "target": 118,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 88,
            "target": 118,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 88,
            "target": 94,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 88,
            "target": 99,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 88,
            "target": 106,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 88,
            "target": 107,
            "weight": 27
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 88,
            "target": 110,
            "weight": 16
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 121,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "2006ApJ...647..303B",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "2005MNRAS.363..223G",
              "2005ApJ...628..246E",
              "2003ApJ...599.1129Y",
              "2006ApJS..162...38A",
              "2005astro.ph.12344H",
              "2006MNRAS.372..174B",
              "2006ApJ...651..392S",
              "1988Natur.331..687H",
              "2005ApJ...620..744G",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "1991AJ....101..562L",
              "2006ApJ...640L..35B",
              "2000ApJ...544..437P"
            ],
            "source": 89,
            "target": 125,
            "weight": 34
          },
          {
            "overlap": [
              "2003AJ....125..984M",
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 95,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K",
              "1996AJ....111.1748F"
            ],
            "source": 89,
            "target": 98,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 101,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992A&AS...96..269S",
              "2006ApJ...647..303B"
            ],
            "source": 89,
            "target": 105,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992nrfa.book.....P"
            ],
            "source": 89,
            "target": 112,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 89,
            "target": 113,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 89,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "2010ApJ...709..832G",
              "2001MNRAS.328.1039C",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2009MNRAS.399..683J",
              "2005PASP..117.1411F"
            ],
            "source": 90,
            "target": 117,
            "weight": 15
          },
          {
            "overlap": [
              "2001MNRAS.328.1039C",
              "1998PASP..110..934K"
            ],
            "source": 90,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K",
              "2003SPIE.4834..161D"
            ],
            "source": 90,
            "target": 121,
            "weight": 11
          },
          {
            "overlap": [
              "2003ApJ...594..186B",
              "2012AJ....143..102G",
              "2002AJ....124.1810S",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 90,
            "target": 123,
            "weight": 6
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1982ApJ...253..423D",
              "1979AJ.....84.1511T",
              "1996ApJ...470..172S"
            ],
            "source": 90,
            "target": 116,
            "weight": 8
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 90,
            "target": 124,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 90,
            "target": 91,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2008PASP..120.1222F",
              "2003MNRAS.344.1000B",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W",
              "2000ApJ...533..682C"
            ],
            "source": 90,
            "target": 92,
            "weight": 19
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 90,
            "target": 95,
            "weight": 8
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2002AJ....124.1810S",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "2006ApJ...643..128W",
              "2003MNRAS.341...33K"
            ],
            "source": 90,
            "target": 96,
            "weight": 17
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 90,
            "target": 97,
            "weight": 4
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1996ApJ...470..172S",
              "1998PASP..110..934K"
            ],
            "source": 90,
            "target": 98,
            "weight": 5
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 90,
            "target": 100,
            "weight": 2
          },
          {
            "overlap": [
              "1982ApJ...253..423D",
              "1989Sci...246..897G",
              "1979AJ.....84.1511T",
              "1996ApJ...470..172S",
              "1998PASP..110..934K"
            ],
            "source": 90,
            "target": 101,
            "weight": 10
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 90,
            "target": 102,
            "weight": 8
          },
          {
            "overlap": [
              "2012ApJ...752...64H",
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "2010ApJ...708..534W",
              "2008PASP..120.1222F",
              "2009ApJ...699.1595P",
              "1989Sci...246..897G",
              "2010AJ....140.1868W",
              "2010ApJ...709..832G",
              "2005ApJ...635L.125G",
              "2001MNRAS.328.1039C",
              "2012AJ....143..102G",
              "2003MNRAS.344.1000B",
              "2006ApJ...643..128W",
              "2000ApJ...533..682C",
              "2003MNRAS.341...33K"
            ],
            "source": 90,
            "target": 103,
            "weight": 18
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 90,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2008PASP..120.1222F",
              "2010ApJ...709..832G",
              "2012AJ....143..102G",
              "2006ApJ...643..128W"
            ],
            "source": 90,
            "target": 109,
            "weight": 10
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1979AJ.....84.1511T"
            ],
            "source": 90,
            "target": 111,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 90,
            "target": 112,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 90,
            "target": 113,
            "weight": 8
          },
          {
            "overlap": [
              "1996ApJ...470..172S"
            ],
            "source": 90,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1979AJ.....84.1511T"
            ],
            "source": 91,
            "target": 116,
            "weight": 9
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 91,
            "target": 117,
            "weight": 4
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 91,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 91,
            "target": 121,
            "weight": 5
          },
          {
            "overlap": [
              "1976PASP...88..960S",
              "1979AJ.....84.1511T"
            ],
            "source": 91,
            "target": 95,
            "weight": 13
          },
          {
            "overlap": [
              "1987ApJ...318..161S",
              "1989ApJ...343....1D",
              "1988AJ.....96.1791F"
            ],
            "source": 91,
            "target": 97,
            "weight": 24
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 91,
            "target": 101,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 91,
            "target": 102,
            "weight": 18
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 91,
            "target": 103,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 91,
            "target": 111,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 91,
            "target": 112,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 92,
            "target": 117,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 92,
            "target": 119,
            "weight": 1
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 92,
            "target": 121,
            "weight": 7
          },
          {
            "overlap": [
              "2008ApJS..175..297A",
              "1998PASP..110..934K",
              "2005PASP..117.1411F"
            ],
            "source": 92,
            "target": 123,
            "weight": 3
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 92,
            "target": 124,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 92,
            "target": 95,
            "weight": 5
          },
          {
            "overlap": [
              "2007ApJ...657..738L",
              "1999ApJ...527...54B",
              "2002SPIE.4836...73W",
              "1976ApJ...203..297S",
              "2004ApJ...615..209H",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2008ApJ...677..169V",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "2008ApJS..175..128S",
              "2008ApJS..175..297A",
              "2001PASP..113.1449C",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 92,
            "target": 96,
            "weight": 27
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 92,
            "target": 97,
            "weight": 3
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1976ApJ...203..297S",
              "1979ApJ...232..352S",
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 92,
            "target": 98,
            "weight": 8
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998PASP..110..934K"
            ],
            "source": 92,
            "target": 101,
            "weight": 4
          },
          {
            "overlap": [
              "2006AJ....132..197W",
              "2005PASP..117.1411F",
              "1999ApJ...527...54B",
              "1983ApJ...273..105B",
              "2002SPIE.4836...73W",
              "2005ApJ...635L.125G",
              "2008PASP..120.1222F",
              "2003MNRAS.344.1000B",
              "2009ApJ...691.1828P",
              "2008AJ....135.1877E",
              "1998ARA&A..36..189K",
              "2001ApJ...556..121K",
              "2000ApJ...530..660B",
              "2006ApJ...643..128W",
              "2003MNRAS.346.1055K",
              "2007AJ....134..527W",
              "2000ApJ...533..682C",
              "1981PASP...93....5B"
            ],
            "source": 92,
            "target": 103,
            "weight": 20
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2007AJ....134..527W"
            ],
            "source": 92,
            "target": 105,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W",
              "2008PASP..120.1222F",
              "2006ApJ...643..128W"
            ],
            "source": 92,
            "target": 109,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 92,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 92,
            "target": 113,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 94,
            "target": 118,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 94,
            "target": 99,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 94,
            "target": 106,
            "weight": 13
          },
          {
            "overlap": [
              "1999ASPC..172..291A",
              "2000A&AS..143...41K"
            ],
            "source": 94,
            "target": 107,
            "weight": 22
          },
          {
            "overlap": [
              "1999ASPC..172..291A",
              "2000A&AS..143...41K"
            ],
            "source": 94,
            "target": 110,
            "weight": 13
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 95,
            "target": 116,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 119,
            "weight": 5
          },
          {
            "overlap": [
              "1994SPIE.2198..251F",
              "1998SPIE.3355..285F",
              "1998PASP..110...79F",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 121,
            "weight": 22
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 117,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 98,
            "weight": 6
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1979AJ.....84.1511T",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 101,
            "weight": 15
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1979AJ.....84.1511T"
            ],
            "source": 95,
            "target": 102,
            "weight": 33
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 95,
            "target": 104,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 105,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 95,
            "target": 111,
            "weight": 4
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 112,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 95,
            "target": 113,
            "weight": 15
          },
          {
            "overlap": [
              "1994SPIE.2198..251F",
              "1991opos.conf..133K",
              "1982PhDT.........2K",
              "1986SPIE..627..733T",
              "1998PASP..110...79F"
            ],
            "source": 95,
            "target": 115,
            "weight": 18
          },
          {
            "overlap": [
              "2007ASPC..376..249M",
              "2005PASP..117.1411F"
            ],
            "source": 96,
            "target": 117,
            "weight": 4
          },
          {
            "overlap": [
              "2002AJ....123..485S"
            ],
            "source": 96,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "2004SPIE.5492..767F"
            ],
            "source": 96,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "2008ApJS..175..297A",
              "2002AJ....123..485S",
              "2002AJ....124.1810S",
              "2005PASP..117.1411F"
            ],
            "source": 96,
            "target": 123,
            "weight": 6
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 96,
            "target": 124,
            "weight": 3
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 96,
            "target": 97,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S",
              "1998SPIE.3355..577M"
            ],
            "source": 96,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "2001AJ....122..714B"
            ],
            "source": 96,
            "target": 101,
            "weight": 3
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2003MNRAS.341...33K"
            ],
            "source": 96,
            "target": 103,
            "weight": 8
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W"
            ],
            "source": 96,
            "target": 109,
            "weight": 8
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1990ApJS...72..433H"
            ],
            "source": 97,
            "target": 117,
            "weight": 8
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 97,
            "target": 124,
            "weight": 5
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 97,
            "target": 116,
            "weight": 5
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 97,
            "target": 98,
            "weight": 4
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1989Sci...246..897G",
              "1990ApJS...72..433H"
            ],
            "source": 97,
            "target": 101,
            "weight": 15
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 97,
            "target": 103,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 97,
            "target": 111,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 98,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 98,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 98,
            "target": 121,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 98,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1996ApJ...470..172S",
              "1990AJ.....99.2019L",
              "1997AJ....113..185M"
            ],
            "source": 98,
            "target": 116,
            "weight": 9
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 98,
            "target": 124,
            "weight": 2
          },
          {
            "overlap": [
              "1995PASP..107..945F",
              "1996A&AS..117..393B",
              "1996ApJ...470..172S",
              "1995MmSAI..66..113R",
              "2001AJ....121.2358B",
              "1998PASP..110...79F",
              "1997AJ....114.2205G",
              "1998PASP..110..934K",
              "2001MNRAS.324..825C",
              "1992ApJ...390..338L",
              "1993AJ....105..393K"
            ],
            "source": 98,
            "target": 101,
            "weight": 23
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 98,
            "target": 105,
            "weight": 4
          },
          {
            "overlap": [
              "1990AJ.....99.2019L",
              "1993AJ....105..393K"
            ],
            "source": 98,
            "target": 111,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 98,
            "target": 112,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 98,
            "target": 113,
            "weight": 8
          },
          {
            "overlap": [
              "1996ApJ...470..172S",
              "1998PASP..110...79F",
              "1997AJ....114.2205G"
            ],
            "source": 98,
            "target": 115,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 99,
            "target": 118,
            "weight": 6
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 99,
            "target": 104,
            "weight": 5
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K",
              "2000A&AS..143...41K",
              "1992ald2.proc...85K"
            ],
            "source": 99,
            "target": 106,
            "weight": 52
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 99,
            "target": 107,
            "weight": 34
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 99,
            "target": 110,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 99,
            "target": 114,
            "weight": 23
          },
          {
            "overlap": [
              "1983ApJ...267..465D"
            ],
            "source": 100,
            "target": 118,
            "weight": 3
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1983ApJ...264....1S",
              "1983ApJ...267..465D",
              "1982ApJ...263L..47K",
              "1977ApJ...217..385G",
              "1983ApJ...274..529S",
              "1979ApJ...234...13G",
              "1984ApJ...287L..55G",
              "1983ApJ...273...16M",
              "1982AJ.....87.1355G",
              "1977AJ.....82..249S",
              "1981ApJ...248L..57K",
              "1977ApJS...34..425D",
              "1978AJ.....83.1549K",
              "1977ApJ...212L.107D",
              "1979AJ.....84..951K"
            ],
            "source": 100,
            "target": 122,
            "weight": 62
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 100,
            "target": 124,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 100,
            "target": 103,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJ...267..465D"
            ],
            "source": 100,
            "target": 104,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1998PASP..110..934K",
              "1990ApJS...72..433H"
            ],
            "source": 101,
            "target": 117,
            "weight": 7
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 101,
            "target": 121,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 101,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "1953ApJ...118..502K"
            ],
            "source": 101,
            "target": 124,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1982ApJ...253..423D",
              "1992ASPC...25..432K",
              "1979AJ.....84.1511T",
              "1994ApJ...424L...1D",
              "1996ApJ...470..172S"
            ],
            "source": 101,
            "target": 116,
            "weight": 16
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 101,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1979AJ.....84.1511T"
            ],
            "source": 101,
            "target": 102,
            "weight": 23
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "2000AJ....120.1579Y"
            ],
            "source": 101,
            "target": 103,
            "weight": 3
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 101,
            "target": 104,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 101,
            "target": 105,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1979AJ.....84.1511T",
              "1993AJ....105..393K"
            ],
            "source": 101,
            "target": 111,
            "weight": 12
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 101,
            "target": 112,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 101,
            "target": 113,
            "weight": 11
          },
          {
            "overlap": [
              "1997A&A...325..954V",
              "1986SPIE..627..733T",
              "1997AJ....114.2205G",
              "1992ASPC...25..432K",
              "1995ApJS...99..391H",
              "1998PASP..110...79F",
              "1994ApJ...424L...1D",
              "1996ApJ...470..172S",
              "1992ASPC...25..439M"
            ],
            "source": 101,
            "target": 115,
            "weight": 23
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 102,
            "target": 116,
            "weight": 11
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 102,
            "target": 121,
            "weight": 13
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 102,
            "target": 104,
            "weight": 10
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 102,
            "target": 111,
            "weight": 12
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1979AJ.....84.1511T"
            ],
            "source": 102,
            "target": 112,
            "weight": 27
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 102,
            "target": 115,
            "weight": 11
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1986ApJ...302L...1D"
            ],
            "source": 103,
            "target": 116,
            "weight": 3
          },
          {
            "overlap": [
              "2005ApJ...624..463G",
              "1989Sci...246..897G",
              "2001MNRAS.328.1039C",
              "2010ApJ...709..832G",
              "1986ApJ...302L...1D",
              "2005PASP..117.1411F"
            ],
            "source": 103,
            "target": 117,
            "weight": 8
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "2001MNRAS.328.1039C"
            ],
            "source": 103,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 103,
            "target": 122,
            "weight": 2
          },
          {
            "overlap": [
              "2012AJ....143..102G",
              "2005PASP..117.1411F"
            ],
            "source": 103,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "2007AJ....134..527W"
            ],
            "source": 103,
            "target": 105,
            "weight": 1
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2010ApJ...709..832G",
              "2012AJ....143..102G",
              "2008PASP..120.1222F",
              "2005PASP..117.1411F"
            ],
            "source": 103,
            "target": 109,
            "weight": 9
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 103,
            "target": 111,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1994PhDT........54B",
              "1971BICDS...1....2J",
              "1983ApJ...267..465D",
              "1997AAS...191.3502E",
              "1995AAS...187.3801B",
              "1986ApJ...304...15B",
              "1999AAS...195.8209D",
              "1988ARA&A..26..245R",
              "2000A&AS..143...85A",
              "1997ApJ...491..421E",
              "1994AAS...185.4104E",
              "1973BICDS...4...27J",
              "1992ASPC...25...47M",
              "1994AAS...184.2802G",
              "1988alds.proc..335H",
              "2000A&AS..143...61E"
            ],
            "source": 104,
            "target": 118,
            "weight": 39
          },
          {
            "overlap": [
              "1983ApJ...267..465D"
            ],
            "source": 104,
            "target": 122,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "1996ASPC..101..581P"
            ],
            "source": 104,
            "target": 106,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143....1G"
            ],
            "source": 104,
            "target": 107,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1992PASA...10..134S",
              "1993ASPC...52..132K",
              "1992PASAu..10..134S",
              "1999adass...8..287L",
              "1999ASPC..172..287L",
              "1995VA.....39R.272S",
              "1988alds.proc..335H",
              "2000A&AS..143...61E"
            ],
            "source": 104,
            "target": 110,
            "weight": 21
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 104,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 104,
            "target": 114,
            "weight": 8
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 104,
            "target": 115,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 105,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1985ApJ...298....8H",
              "1998PASP..110..934K"
            ],
            "source": 105,
            "target": 119,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 105,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 105,
            "target": 123,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJ...647..303B",
              "1992A&AS...96..269S"
            ],
            "source": 105,
            "target": 125,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 105,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 105,
            "target": 113,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K"
            ],
            "source": 106,
            "target": 118,
            "weight": 10
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 106,
            "target": 107,
            "weight": 27
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 106,
            "target": 110,
            "weight": 16
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 106,
            "target": 114,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 107,
            "target": 118,
            "weight": 4
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "1999ASPC..172..291A",
              "2000A&AS..143...41K"
            ],
            "source": 107,
            "target": 110,
            "weight": 14
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 107,
            "target": 114,
            "weight": 16
          },
          {
            "overlap": [
              "1995ASPC...77...36A"
            ],
            "source": 108,
            "target": 110,
            "weight": 17
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2010ApJ...709..832G"
            ],
            "source": 109,
            "target": 117,
            "weight": 5
          },
          {
            "overlap": [
              "2012AJ....143..102G",
              "2011AJ....142..133G",
              "2005PASP..117.1411F"
            ],
            "source": 109,
            "target": 123,
            "weight": 5
          },
          {
            "overlap": [
              "1992MNRAS.254..389R"
            ],
            "source": 109,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1996prpe.book.....W",
              "2000A&AS..143...41K",
              "1988alds.proc..335H",
              "2000A&AS..143...61E"
            ],
            "source": 110,
            "target": 118,
            "weight": 14
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 110,
            "target": 114,
            "weight": 9
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1979AJ.....84.1511T",
              "1988ApJ...327..544D",
              "1990AJ.....99.2019L"
            ],
            "source": 111,
            "target": 116,
            "weight": 14
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 111,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1993ASPC...37..185B"
            ],
            "source": 111,
            "target": 121,
            "weight": 7
          },
          {
            "overlap": [
              "1980ApJS...43..305K"
            ],
            "source": 111,
            "target": 124,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 111,
            "target": 112,
            "weight": 3
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 111,
            "target": 115,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 112,
            "target": 117,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 112,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1998PASP..110..934K"
            ],
            "source": 112,
            "target": 121,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 112,
            "target": 123,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 112,
            "target": 116,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 112,
            "target": 113,
            "weight": 12
          },
          {
            "overlap": [
              "1993adass...2..173T",
              "1986SPIE..627..733T",
              "1993ASPC...52..173T"
            ],
            "source": 112,
            "target": 115,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 113,
            "target": 117,
            "weight": 9
          },
          {
            "overlap": [
              "2000ApJ...533L.183K",
              "1998PASP..110..934K"
            ],
            "source": 113,
            "target": 121,
            "weight": 26
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 113,
            "target": 123,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 113,
            "target": 119,
            "weight": 7
          },
          {
            "overlap": [
              "1995ASPC...77...28E"
            ],
            "source": 114,
            "target": 118,
            "weight": 9
          },
          {
            "overlap": [
              "1992ASPC...25..432K",
              "1994ApJ...424L...1D",
              "1996ApJ...470..172S"
            ],
            "source": 115,
            "target": 116,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 115,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1994SPIE.2198..251F",
              "1997ASPC..125..140T",
              "1998PASP..110...79F"
            ],
            "source": 115,
            "target": 121,
            "weight": 9
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1986ApJ...302L...1D"
            ],
            "source": 116,
            "target": 117,
            "weight": 4
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 116,
            "target": 119,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 116,
            "target": 121,
            "weight": 3
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 117,
            "target": 121,
            "weight": 8
          },
          {
            "overlap": [
              "2009ApJ...701.1336L",
              "1999MNRAS.309..610D",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "2006AJ....132.1275R",
              "1989AJ.....98..755R",
              "2011MNRAS.412..800S",
              "2005AJ....130.1482R",
              "2005MNRAS.363L..11B",
              "2011ApJ...729..127U",
              "1987MNRAS.227....1K",
              "1998PASP..110..934K",
              "2005ApJ...628L..97D",
              "2003AJ....126.2152R",
              "2005PASP..117.1411F"
            ],
            "source": 117,
            "target": 123,
            "weight": 19
          },
          {
            "overlap": [
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "1989AJ.....98..755R",
              "1989ApJS...70....1A",
              "1987MNRAS.227....1K",
              "1986ApJ...302L...1D",
              "1999MNRAS.309..610D",
              "1998PASP..110..934K",
              "2001MNRAS.328.1039C",
              "2001ApJ...547..609E"
            ],
            "source": 117,
            "target": 119,
            "weight": 15
          },
          {
            "overlap": [
              "1984ApJ...276...38J"
            ],
            "source": 117,
            "target": 124,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJ...267..465D"
            ],
            "source": 118,
            "target": 122,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 119,
            "target": 121,
            "weight": 4
          },
          {
            "overlap": [
              "1996MNRAS.282..263E",
              "2003NewA....8..439N",
              "1986AJ.....92.1248T",
              "1999MNRAS.303..188K",
              "2002AJ....124.1266R",
              "1997ApJ...478..462C",
              "1999ApJ...517L..23G",
              "2000AJ....119...44K",
              "1997ApJ...481..633D",
              "1933AcHPh...6..110Z",
              "2000AJ....119.2038V",
              "2002MNRAS.337.1417G",
              "1989AJ.....98..755R",
              "2002AJ....123..485S",
              "1987MNRAS.227....1K",
              "2001ApJ...555..558R",
              "2001ApJ...548L.139D",
              "2002ApJ...567..716R",
              "1937ApJ....86..217Z",
              "1999MNRAS.309..610D",
              "1998ApJ...505...74G",
              "2000AJ....120..523R",
              "1997ApJ...490..493N",
              "2000AJ....120.2338R",
              "1980A&A....82..322D",
              "1998PASP..110..934K",
              "1999MNRAS.307..529K",
              "2003ApJ...585..205B"
            ],
            "source": 119,
            "target": 123,
            "weight": 29
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 121,
            "target": 123,
            "weight": 2
          }
        ],
        "multigraph": false,
        "nodes": [
          {
            "citation_count": 62,
            "first_author": "Rines, Kenneth",
            "group": 0,
            "id": 0,
            "nodeWeight": 62,
            "node_name": "2004AJ....128.1078R",
            "read_count": 12.0,
            "title": "CAIRNS: The Cluster and Infall Region Nearby Survey. II. Environmental Dependence of Infrared Mass-to-Light Ratios"
          },
          {
            "citation_count": 0,
            "first_author": "Henneken, Edwin A.",
            "group": 1,
            "id": 1,
            "nodeWeight": 0,
            "node_name": "2014arXiv1406.4542H",
            "read_count": 21.0,
            "title": "Computing and Using Metrics in the ADS"
          },
          {
            "citation_count": 1,
            "first_author": "Accomazzi, A.",
            "group": 1,
            "id": 2,
            "nodeWeight": 1,
            "node_name": "2009arad.workE..32A",
            "read_count": 0.0,
            "title": "Towards a Resource-Centric Data Network for Astronomy"
          },
          {
            "citation_count": 85,
            "first_author": "Geller, M. J.",
            "group": 0,
            "id": 3,
            "nodeWeight": 85,
            "node_name": "1999ApJ...517L..23G",
            "read_count": 15.0,
            "title": "The Mass Profile of the Coma Galaxy Cluster"
          },
          {
            "citation_count": 1,
            "first_author": "Eichhorn, Guenther",
            "group": 1,
            "id": 4,
            "nodeWeight": 1,
            "node_name": "1998ASPC..153..277E",
            "read_count": 3.0,
            "title": "The Astrophysics Data System"
          },
          {
            "citation_count": 1,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 5,
            "nodeWeight": 1,
            "node_name": "2007ASPC..377...36E",
            "read_count": 11.0,
            "title": "Connectivity in the Astronomy Digital Library"
          },
          {
            "citation_count": 0,
            "first_author": "Hajjem, C.",
            "group": 1,
            "id": 6,
            "nodeWeight": 1,
            "node_name": "2006cs........6079H",
            "read_count": 121.0,
            "title": "Ten-Year Cross-Disciplinary Comparison of the Growth of Open Access and How it Increases Research Citation Impact"
          },
          {
            "citation_count": 4,
            "first_author": "Kurtz, Michael J.",
            "group": 1,
            "id": 7,
            "nodeWeight": 4,
            "node_name": "2007arXiv0709.0896K",
            "read_count": 25.0,
            "title": "Open Access does not increase citations for research articles from The Astrophysical Journal"
          },
          {
            "citation_count": 1,
            "first_author": "Mink, D. J.",
            "group": 0,
            "id": 8,
            "nodeWeight": 1,
            "node_name": "2003ASPC..295...51M",
            "read_count": 3.0,
            "title": "Federating Catalogs and Interfacing Them with Archives: A VO Prototype"
          },
          {
            "citation_count": 37,
            "first_author": "Geller, Margaret J.",
            "group": 0,
            "id": 9,
            "nodeWeight": 37,
            "node_name": "2005ApJ...635L.125G",
            "read_count": 11.0,
            "title": "SHELS: The Hectospec Lensing Survey"
          },
          {
            "citation_count": 2,
            "first_author": "Accomazzi, A.",
            "group": 1,
            "id": 10,
            "nodeWeight": 2,
            "node_name": "2007ASPC..377...69A",
            "read_count": 12.0,
            "title": "Creation and Use of Citations in the ADS"
          },
          {
            "citation_count": 4,
            "first_author": "Kurtz, Michael J.",
            "group": 1,
            "id": 11,
            "nodeWeight": 4,
            "node_name": "1998ASPC..153..293K",
            "read_count": 3.0,
            "title": "The Historical Literature of Astronomy, via ADS"
          },
          {
            "citation_count": 270,
            "first_author": "Zabludoff, Ann I.",
            "group": 0,
            "id": 12,
            "nodeWeight": 270,
            "node_name": "1990ApJS...74....1Z",
            "read_count": 10.0,
            "title": "The Kinematics of Abell Clusters"
          },
          {
            "citation_count": 2,
            "first_author": "Utsumi, Yousuke",
            "group": 0,
            "id": 13,
            "nodeWeight": 2,
            "node_name": "2014ApJ...786...93U",
            "read_count": 49.0,
            "title": "Reducing Systematic Error in Weak Lensing Cluster Surveys"
          },
          {
            "citation_count": 12,
            "first_author": "Mahdavi, Andisheh",
            "group": 0,
            "id": 15,
            "nodeWeight": 12,
            "node_name": "1996AJ....111...64M",
            "read_count": 3.0,
            "title": "The Lumpy Cluster Abell 1185"
          },
          {
            "citation_count": 24,
            "first_author": "Bothun, Gregory D.",
            "group": 0,
            "id": 16,
            "nodeWeight": 24,
            "node_name": "1992ApJ...395..347B",
            "read_count": 10.0,
            "title": "The Velocity-Distance Relation for Galaxies on a Bubble"
          },
          {
            "citation_count": 64,
            "first_author": "Fabricant, D.",
            "group": 0,
            "id": 17,
            "nodeWeight": 64,
            "node_name": "1986ApJ...308..530F",
            "read_count": 4.0,
            "title": "An X-Ray and Optical Study of the Cluster of Galaxies Abell 754"
          },
          {
            "citation_count": 3,
            "first_author": "Henneken, Edwin A.",
            "group": 1,
            "id": 18,
            "nodeWeight": 3,
            "node_name": "2012opsa.book..253H",
            "read_count": 8.0,
            "title": "The ADS in the Information Age - Impact on Discovery"
          },
          {
            "citation_count": 1,
            "first_author": "Henneken, E. A.",
            "group": 1,
            "id": 20,
            "nodeWeight": 1,
            "node_name": "2009ASPC..411..384H",
            "read_count": 0.0,
            "title": "Exploring the Astronomy Literature Landscape"
          },
          {
            "citation_count": 5,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 21,
            "nodeWeight": 5,
            "node_name": "1998ASPC..145..378E",
            "read_count": 3.0,
            "title": "New Capabilities of the ADS Abstract and Article Service"
          },
          {
            "citation_count": 4,
            "first_author": "Geller, Margaret J.",
            "group": 0,
            "id": 22,
            "nodeWeight": 4,
            "node_name": "2014ApJ...783...52G",
            "read_count": 38.0,
            "title": "A Redshift Survey of the Strong-lensing Cluster Abell 383"
          },
          {
            "citation_count": 4,
            "first_author": "Henneken, Edwin A.",
            "group": 1,
            "id": 23,
            "nodeWeight": 4,
            "node_name": "2011ApSSP...1..125H",
            "read_count": 7.0,
            "title": "Finding Your Literature Match - A Recommender System"
          },
          {
            "citation_count": 3,
            "first_author": "Kurtz, M. J.",
            "group": 1,
            "id": 24,
            "nodeWeight": 3,
            "node_name": "2010ASPC..434..155K",
            "read_count": 9.0,
            "title": "Using Multipartite Graphs for Recommendation and Discovery"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, Guenther",
            "group": 1,
            "id": 25,
            "nodeWeight": 0,
            "node_name": "2007BASI...35..717E",
            "read_count": 3.0,
            "title": "Access to the literature and connection to on-line data"
          },
          {
            "citation_count": 0,
            "first_author": "Henneken, Edwin A.",
            "group": 1,
            "id": 26,
            "nodeWeight": 0,
            "node_name": "2006AAS...20917302H",
            "read_count": 3.0,
            "title": "Finding Astronomical Communities Through Co-readership Analysis"
          },
          {
            "citation_count": 10,
            "first_author": "Lasala, J.",
            "group": 0,
            "id": 27,
            "nodeWeight": 10,
            "node_name": "1985PASP...97..605L",
            "read_count": 0.0,
            "title": "A fast, reliable spectral rectification technique."
          },
          {
            "citation_count": 69,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 28,
            "nodeWeight": 69,
            "node_name": "2006ApJ...640L..35B",
            "read_count": 21.0,
            "title": "A Successful Targeted Search for Hypervelocity Stars"
          },
          {
            "citation_count": 3,
            "first_author": "Mink, D. J.",
            "group": 0,
            "id": 29,
            "nodeWeight": 3,
            "node_name": "1998ASPC..145...93M",
            "read_count": 2.0,
            "title": "RVSAO 2.0 - A Radial Velocity Package for IRAF"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 30,
            "nodeWeight": 0,
            "node_name": "2005LPI....36.1207E",
            "read_count": 0.0,
            "title": "New Features in the ADS Abstract Service"
          },
          {
            "citation_count": 8,
            "first_author": "Westra, Eduard",
            "group": 0,
            "id": 32,
            "nodeWeight": 8,
            "node_name": "2010PASP..122.1258W",
            "read_count": 16.0,
            "title": "Empirical Optical k-Corrections for Redshifts \u2264 0.7"
          },
          {
            "citation_count": 3,
            "first_author": "Kurtz, Michael J.",
            "group": 1,
            "id": 33,
            "nodeWeight": 3,
            "node_name": "2003lisa.conf..223K",
            "read_count": 3.0,
            "title": "The NASA Astrophysics Data System: Obsolescence of Reads and Cites"
          },
          {
            "citation_count": 34,
            "first_author": "Fabricant, Daniel",
            "group": 0,
            "id": 34,
            "nodeWeight": 34,
            "node_name": "1993AJ....105..788F",
            "read_count": 2.0,
            "title": "A Study of the Rich Cluster of Galaxies A119"
          },
          {
            "citation_count": 5,
            "first_author": "Henneken, Edwin A.",
            "group": 1,
            "id": 35,
            "nodeWeight": 5,
            "node_name": "2009JInfo...3....1H",
            "read_count": 27.0,
            "title": "Use of astronomical literature - A report on usage patterns"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, Guenther",
            "group": 1,
            "id": 36,
            "nodeWeight": 0,
            "node_name": "2004tivo.conf..267E",
            "read_count": 4.0,
            "title": "The Astronomy Digital Library and the VO"
          },
          {
            "citation_count": 0,
            "first_author": "Henneken, E. A.",
            "group": 1,
            "id": 37,
            "nodeWeight": 0,
            "node_name": "2012LPI....43.1022H",
            "read_count": 0.0,
            "title": "Online Discovery: Search Paradigms and the Art of Literature Exploration"
          },
          {
            "citation_count": 77,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 38,
            "nodeWeight": 77,
            "node_name": "2006ApJ...647..303B",
            "read_count": 11.0,
            "title": "Hypervelocity Stars. I. The Spectroscopic Survey"
          },
          {
            "citation_count": 17,
            "first_author": "Thorstensen, John R.",
            "group": 0,
            "id": 39,
            "nodeWeight": 17,
            "node_name": "1995AJ....109.2368T",
            "read_count": 0.0,
            "title": "Redshifts for Fainter Galaxies in the First CFA Slice. III. To the Zwicky Catalog Limit"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 40,
            "nodeWeight": 0,
            "node_name": "2007ASPC..377...93E",
            "read_count": 8.0,
            "title": "Full Text Searching in the Astrophysics Data System"
          },
          {
            "citation_count": 49,
            "first_author": "Ostriker, Eve C.",
            "group": 0,
            "id": 41,
            "nodeWeight": 49,
            "node_name": "1988AJ.....96.1775O",
            "read_count": 5.0,
            "title": "The Kinematics and Dynamics of the Rich Cluster of Galaxies Abell 539"
          },
          {
            "citation_count": 8,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 42,
            "nodeWeight": 8,
            "node_name": "2012ApJ...750..168K",
            "read_count": 27.0,
            "title": "Testing Weak-lensing Maps with Redshift Surveys: A Subaru Field"
          },
          {
            "citation_count": 61,
            "first_author": "Rines, Kenneth",
            "group": 0,
            "id": 43,
            "nodeWeight": 61,
            "node_name": "2005AJ....130.1482R",
            "read_count": 14.0,
            "title": "CAIRNS: The Cluster and Infall Region Nearby Survey. III. Environmental Dependence of H\u03b1 Properties of Galaxies"
          },
          {
            "citation_count": 0,
            "first_author": "Henneken, E. A.",
            "group": 1,
            "id": 44,
            "nodeWeight": 0,
            "node_name": "2009LPI....40.1873H",
            "read_count": 0.0,
            "title": "The SAO/NASA Astrophysics Data System: A Gateway to the Planetary Sciences Literature"
          },
          {
            "citation_count": 12,
            "first_author": "Kleyna, J. T.",
            "group": 0,
            "id": 45,
            "nodeWeight": 12,
            "node_name": "1997AJ....113..624K",
            "read_count": 0.0,
            "title": "An Adaptive Kernel Approach to Finding dSph Galaxies Around the Milky Way"
          },
          {
            "citation_count": 1,
            "first_author": "Kurtz, Michael J.",
            "group": 1,
            "id": 46,
            "nodeWeight": 1,
            "node_name": "2011ApSSP...1...23K",
            "read_count": 46.0,
            "title": "The Emerging Scholarly Brain"
          },
          {
            "citation_count": 8,
            "first_author": "Henneken, Edwin A.",
            "group": 1,
            "id": 47,
            "nodeWeight": 8,
            "node_name": "2006JEPub...9....2H",
            "read_count": 26.0,
            "title": "Effect of E-printing on Citation Rates in Astronomy and Physics"
          },
          {
            "citation_count": 25,
            "first_author": "Willmer, C. N. A.",
            "group": 0,
            "id": 48,
            "nodeWeight": 25,
            "node_name": "1994ApJ...437..560W",
            "read_count": 0.0,
            "title": "A Medium-deep Redshift Survey of a Minislice at the North Galactic Pole"
          },
          {
            "citation_count": 21,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 49,
            "nodeWeight": 21,
            "node_name": "2003AJ....126.1362B",
            "read_count": 9.0,
            "title": "The Century Survey Galactic Halo Project. I. Stellar Spectral Analysis"
          },
          {
            "citation_count": 57,
            "first_author": "Coe, Dan",
            "group": 0,
            "id": 50,
            "nodeWeight": 57,
            "node_name": "2012ApJ...757...22C",
            "read_count": 39.0,
            "title": "CLASH: Precise New Constraints on the Mass Profile of the Galaxy Cluster A2261"
          },
          {
            "citation_count": 170,
            "first_author": "da Costa, L. Nicolaci",
            "group": 0,
            "id": 51,
            "nodeWeight": 170,
            "node_name": "1994ApJ...424L...1D",
            "read_count": 5.0,
            "title": "A Complete Southern Sky Redshift Survey"
          },
          {
            "citation_count": 0,
            "first_author": "Kurtz, Michael D.",
            "group": 6,
            "id": 52,
            "nodeWeight": 0,
            "node_name": "2014CTM....18..532K",
            "read_count": 0.0,
            "title": "Acoustic timescale characterisation of a one-dimensional model hot spot"
          },
          {
            "citation_count": 20,
            "first_author": "Alonso, Victoria M.",
            "group": 0,
            "id": 53,
            "nodeWeight": 20,
            "node_name": "1993AJ....106..676A",
            "read_count": 0.0,
            "title": "CCD Calibration of the Magnitude Scale for the Southern Sky Redshift Survey Extension Galaxy Sample"
          },
          {
            "citation_count": 0,
            "first_author": "Accomazzi, A.",
            "group": 1,
            "id": 54,
            "nodeWeight": 0,
            "node_name": "2003ASPC..295..309A",
            "read_count": 3.0,
            "title": "ADS Web Services for the Discovery and Linking of Bibliographic Records"
          },
          {
            "citation_count": 4,
            "first_author": "Mink, D. J.",
            "group": 0,
            "id": 55,
            "nodeWeight": 4,
            "node_name": "2005ASPC..347..228M",
            "read_count": 3.0,
            "title": "Creating Data that Never Die: Building a Spectrograph Data Pipeline in the Virtual Observatory Era"
          },
          {
            "citation_count": 21,
            "first_author": "Freedman Woods, Deborah",
            "group": 0,
            "id": 56,
            "nodeWeight": 21,
            "node_name": "2010AJ....139.1857F",
            "read_count": 20.0,
            "title": "Triggered Star Formation in Galaxy Pairs at z = 0.08-0.38"
          },
          {
            "citation_count": 18,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 57,
            "nodeWeight": 18,
            "node_name": "2005AJ....130.1097B",
            "read_count": 10.0,
            "title": "The Century Survey Galactic Halo Project. II. Global Properties and the Luminosity Function of Field Blue Horizontal Branch Stars"
          },
          {
            "citation_count": 68,
            "first_author": "Carter, B. J.",
            "group": 0,
            "id": 58,
            "nodeWeight": 68,
            "node_name": "2001ApJ...559..606C",
            "read_count": 11.0,
            "title": "Star Formation in a Complete Spectroscopic Survey of Galaxies"
          },
          {
            "citation_count": 0,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 59,
            "nodeWeight": 0,
            "node_name": "1995PASP..107..776K",
            "read_count": 0.0,
            "title": "Giant Shoulders: Data and Discovery in Astronomy"
          },
          {
            "citation_count": 61,
            "first_author": "Mahdavi, Andisheh",
            "group": 0,
            "id": 61,
            "nodeWeight": 61,
            "node_name": "1999ApJ...518...69M",
            "read_count": 8.0,
            "title": "The Dynamics of Poor Systems of Galaxies"
          },
          {
            "citation_count": 11,
            "first_author": "Eichhorn, G.",
            "group": 8,
            "id": 62,
            "nodeWeight": 11,
            "node_name": "1995ASPC...77...28E",
            "read_count": 4.0,
            "title": "The New Astrophysics Data System"
          },
          {
            "citation_count": 17,
            "first_author": "Eichhorn, Guenther",
            "group": 1,
            "id": 63,
            "nodeWeight": 17,
            "node_name": "2000A&AS..143...61E",
            "read_count": 93.0,
            "title": "The NASA Astrophysics Data System: The search engine and its user interface"
          },
          {
            "citation_count": 33,
            "first_author": "Zahid, H. Jabran",
            "group": 0,
            "id": 64,
            "nodeWeight": 33,
            "node_name": "2013ApJ...771L..19Z",
            "read_count": 61.0,
            "title": "The Chemical Evolution of Star-forming Galaxies over the Last 11 Billion Years"
          },
          {
            "citation_count": 2,
            "first_author": "Starikova, Svetlana",
            "group": 0,
            "id": 65,
            "nodeWeight": 2,
            "node_name": "2014ApJ...786..125S",
            "read_count": 14.0,
            "title": "Comparison of Galaxy Clusters Selected by Weak-lensing, Optical Spectroscopy, and X-Rays in the Deep Lens Survey F2 Field"
          },
          {
            "citation_count": 20,
            "first_author": "Grant, Carolyn S.",
            "group": 1,
            "id": 66,
            "nodeWeight": 20,
            "node_name": "2000A&AS..143..111G",
            "read_count": 10.0,
            "title": "The NASA Astrophysics Data System: Data holdings"
          },
          {
            "citation_count": 55,
            "first_author": "Kewley, Lisa J.",
            "group": 0,
            "id": 67,
            "nodeWeight": 55,
            "node_name": "2007AJ....133..882K",
            "read_count": 8.0,
            "title": "SDSS 0809+1729: Connections Between Extremely Metal-Poor Galaxies and Gamma-Ray Burst Hosts"
          },
          {
            "citation_count": 4,
            "first_author": "Accomazzi, A.",
            "group": 1,
            "id": 68,
            "nodeWeight": 4,
            "node_name": "1996ASPC..101..558A",
            "read_count": 4.0,
            "title": "The ADS Article Service Data Holdings and Access Methods"
          },
          {
            "citation_count": 13,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 69,
            "nodeWeight": 13,
            "node_name": "2007AJ....134.1360K",
            "read_count": 9.0,
            "title": "\u03bc-PhotoZ: Photometric Redshifts by Inverting the Tolman Surface Brightness Test"
          },
          {
            "citation_count": 78,
            "first_author": "Fabricant, Daniel G.",
            "group": 0,
            "id": 70,
            "nodeWeight": 78,
            "node_name": "1989ApJ...336...77F",
            "read_count": 2.0,
            "title": "A Combined Optical/X-Ray Study of the Galaxy Cluster Abell 2256"
          },
          {
            "citation_count": 8,
            "first_author": "Kurtz, Michael J.",
            "group": 1,
            "id": 71,
            "nodeWeight": 8,
            "node_name": "2010ARIST..44....3K",
            "read_count": 40.0,
            "title": "Usage Bibliometrics"
          },
          {
            "citation_count": 79,
            "first_author": "Geller, Margaret J.",
            "group": 0,
            "id": 72,
            "nodeWeight": 79,
            "node_name": "1997AJ....114.2205G",
            "read_count": 22.0,
            "title": "The Century Survey: A Deeper Slice of the Universe"
          },
          {
            "citation_count": 295,
            "first_author": "Falco, Emilio E.",
            "group": 0,
            "id": 73,
            "nodeWeight": 295,
            "node_name": "1999PASP..111..438F",
            "read_count": 63.0,
            "title": "The Updated Zwicky Catalog (UZC)"
          },
          {
            "citation_count": 4,
            "first_author": "Henneken, Edwin A.",
            "group": 1,
            "id": 74,
            "nodeWeight": 4,
            "node_name": "2007LePub..20...16H",
            "read_count": 11.0,
            "title": "E-prints and journal articles in astronomy: a productive co-existence"
          },
          {
            "citation_count": 6,
            "first_author": "Schulman, E.",
            "group": 1,
            "id": 75,
            "nodeWeight": 6,
            "node_name": "1997PASP..109.1278S",
            "read_count": 3.0,
            "title": "Trends in Astronomical Publication Between 1975 and 1996"
          },
          {
            "citation_count": 25,
            "first_author": "Geller, Margaret J.",
            "group": 0,
            "id": 76,
            "nodeWeight": 25,
            "node_name": "2010ApJ...709..832G",
            "read_count": 14.0,
            "title": "SHELS: Testing Weak-Lensing Maps with Redshift Surveys"
          },
          {
            "citation_count": 1,
            "first_author": "Eichhorn, Guenther",
            "group": 1,
            "id": 77,
            "nodeWeight": 1,
            "node_name": "2003lisa.conf..145E",
            "read_count": 3.0,
            "title": "Current and Future Holdings of the Historical Literature in the ADS"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 78,
            "nodeWeight": 0,
            "node_name": "2003LPI....34.1949E",
            "read_count": 3.0,
            "title": "Expanded Citations Database in the NASA ADS Abstract Service"
          },
          {
            "citation_count": 0,
            "first_author": "Willmer, C. N. A.",
            "group": 0,
            "id": 80,
            "nodeWeight": 0,
            "node_name": "1996ApJS..107..823W",
            "read_count": 0.0,
            "title": "A Medium-deep Redshift Survey of a Minislice at the North Galactic Pole. II. The Data: Erratum"
          },
          {
            "citation_count": 67,
            "first_author": "Geller, M. J.",
            "group": 10,
            "id": 81,
            "nodeWeight": 67,
            "node_name": "1984ApJ...287L..55G",
            "read_count": 3.0,
            "title": "The Shane-Wirtanen counts"
          },
          {
            "citation_count": 14,
            "first_author": "Kleyna, J.",
            "group": 4,
            "id": 83,
            "nodeWeight": 14,
            "node_name": "1999AJ....117.1275K",
            "read_count": 2.0,
            "title": "Measuring the Dark Matter Scale of Local Group Dwarf Spheroidals"
          },
          {
            "citation_count": 62,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 84,
            "nodeWeight": 62,
            "node_name": "2007ApJ...671.1708B",
            "read_count": 26.0,
            "title": "Hypervelocity Stars. III. The Space Density and Ejection History of Main-Sequence Stars from the Galactic Center"
          },
          {
            "citation_count": 49,
            "first_author": "Kleyna, J. T.",
            "group": 4,
            "id": 85,
            "nodeWeight": 49,
            "node_name": "1998AJ....115.2359K",
            "read_count": 3.0,
            "title": "A V and I CCD Mosaic Survey of the Ursa Minor Dwarf Spheroidal Galaxy"
          },
          {
            "citation_count": 159,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 86,
            "nodeWeight": 159,
            "node_name": "2005ApJ...622L..33B",
            "read_count": 43.0,
            "title": "Discovery of an Unbound Hypervelocity Star in the Milky Way Halo"
          },
          {
            "citation_count": 0,
            "first_author": "Accomazzi, A.",
            "group": 1,
            "id": 88,
            "nodeWeight": 0,
            "node_name": "1998ASPC..145..395A",
            "read_count": 3.0,
            "title": "Mirroring the ADS Bibliographic Databases"
          },
          {
            "citation_count": 26,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 89,
            "nodeWeight": 26,
            "node_name": "2008AJ....135..564B",
            "read_count": 15.0,
            "title": "The Century Survey Galactic Halo Project III: A Complete 4300 DEG<SUP>2</SUP> Survey of Blue Horizontal Branch Stars in the Metal-Weak Thick Disk and Inner Halo"
          },
          {
            "citation_count": 0,
            "first_author": "Accomazzi, A.",
            "group": 1,
            "id": 90,
            "nodeWeight": 0,
            "node_name": "2006ASPC..351..715A",
            "read_count": 9.0,
            "title": "Bibliographic Classification using the ADS Databases"
          },
          {
            "citation_count": 47,
            "first_author": "Rines, K.",
            "group": 0,
            "id": 91,
            "nodeWeight": 47,
            "node_name": "2001ApJ...561L..41R",
            "read_count": 9.0,
            "title": "Infrared Mass-to-Light Profile throughout the Infall Region of the Coma Cluster"
          },
          {
            "citation_count": 15,
            "first_author": "Geller, Margaret J.",
            "group": 0,
            "id": 92,
            "nodeWeight": 15,
            "node_name": "2012AJ....143..102G",
            "read_count": 22.0,
            "title": "The Faint End of the Luminosity Function and Low Surface Brightness Galaxies"
          },
          {
            "citation_count": 23,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 93,
            "nodeWeight": 23,
            "node_name": "2004AJ....127.1555B",
            "read_count": 5.0,
            "title": "Mapping the Inner Halo of the Galaxy with 2MASS-Selected Horizontal-Branch Candidates"
          },
          {
            "citation_count": 6,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 94,
            "nodeWeight": 6,
            "node_name": "1995VA.....39..217E",
            "read_count": 0.0,
            "title": "Access to the Astrophysics Science Information and Abstract System"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 96,
            "nodeWeight": 0,
            "node_name": "2002LPI....33.1298E",
            "read_count": 3.0,
            "title": "New Data and Search Features in the NASA ADS Abstract Service"
          },
          {
            "citation_count": 52,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 98,
            "nodeWeight": 52,
            "node_name": "2007ApJ...660..311B",
            "read_count": 15.0,
            "title": "Hypervelocity Stars. II. The Bound Population"
          },
          {
            "citation_count": 3,
            "first_author": "Geller, Margaret J.",
            "group": 0,
            "id": 99,
            "nodeWeight": 3,
            "node_name": "2014ApJS..213...35G",
            "read_count": 54.0,
            "title": "SHELS: A Complete Galaxy Redshift Survey with R \u3008= 20.6"
          },
          {
            "citation_count": 23,
            "first_author": "Thorstensen, J. R.",
            "group": 0,
            "id": 100,
            "nodeWeight": 23,
            "node_name": "1989AJ.....98.1143T",
            "read_count": 0.0,
            "title": "Redshifts for a Sample of Fainter Galaxies in the First CfA Survey Slice"
          },
          {
            "citation_count": 20,
            "first_author": "Westra, Eduard",
            "group": 0,
            "id": 101,
            "nodeWeight": 20,
            "node_name": "2010ApJ...708..534W",
            "read_count": 8.0,
            "title": "Evolution of the H\u03b1 Luminosity Function"
          },
          {
            "citation_count": 1,
            "first_author": "Schulman, Eric",
            "group": 1,
            "id": 102,
            "nodeWeight": 1,
            "node_name": "1997ASPC..125..361S",
            "read_count": 2.0,
            "title": "The Sociology of Astronomical Publication Using ADS and ADAMS"
          },
          {
            "citation_count": 6,
            "first_author": "Henneken, E.",
            "group": 1,
            "id": 103,
            "nodeWeight": 6,
            "node_name": "2007ASPC..377..106H",
            "read_count": 7.0,
            "title": "myADS-arXiv -- a Tailor-made, Open Access, Virtual Journal"
          },
          {
            "citation_count": 7,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 104,
            "nodeWeight": 7,
            "node_name": "2000ApJ...533L.183K",
            "read_count": 14.0,
            "title": "Eigenvector Sky Subtraction"
          },
          {
            "citation_count": 16,
            "first_author": "Fabricant, Daniel G.",
            "group": 0,
            "id": 105,
            "nodeWeight": 16,
            "node_name": "2008PASP..120.1222F",
            "read_count": 6.0,
            "title": "Spectrophotometry with Hectospec, the MMT's Fiber-Fed Spectrograph"
          },
          {
            "citation_count": 12,
            "first_author": "Wegner, Gary",
            "group": 0,
            "id": 106,
            "nodeWeight": 12,
            "node_name": "1990AJ....100.1405W",
            "read_count": 0.0,
            "title": "Redshifts for Fainter Galaxies in the First CfA Survey Slice. II."
          },
          {
            "citation_count": 37,
            "first_author": "Brown, Warren R.",
            "group": 0,
            "id": 107,
            "nodeWeight": 37,
            "node_name": "2001AJ....122..714B",
            "read_count": 13.0,
            "title": "V- and R-band Galaxy Luminosity Functions and Low Surface Brightness Galaxies in the Century Survey"
          },
          {
            "citation_count": 1,
            "first_author": "Kurtz, Michael",
            "group": 1,
            "id": 108,
            "nodeWeight": 1,
            "node_name": "2009astro2010P..28K",
            "read_count": 12.0,
            "title": "The Smithsonian/NASA Astrophysics Data System (ADS) Decennial Report"
          },
          {
            "citation_count": 0,
            "first_author": "Geller, M. J.",
            "group": 10,
            "id": 109,
            "nodeWeight": 0,
            "node_name": "1986NYASA.470..123G",
            "read_count": 0.0,
            "title": "The galaxy distribution and the large-scale structure of the Universe."
          },
          {
            "citation_count": 17,
            "first_author": "Wegner, Gary",
            "group": 0,
            "id": 110,
            "nodeWeight": 17,
            "node_name": "2001AJ....122.2893W",
            "read_count": 3.0,
            "title": "Redshifts for 2410 Galaxies in the Century Survey Region"
          },
          {
            "citation_count": 109,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 111,
            "nodeWeight": 109,
            "node_name": "1992ASPC...25..432K",
            "read_count": 4.0,
            "title": "XCSAO: A Radial Velocity Package for the IRAF Environment"
          },
          {
            "citation_count": 12,
            "first_author": "Hwang, Ho Seong",
            "group": 0,
            "id": 113,
            "nodeWeight": 12,
            "node_name": "2012ApJ...758...25H",
            "read_count": 25.0,
            "title": "SHELS: Optical Spectral Properties of WISE 22 \u03bcm Selected Galaxies"
          },
          {
            "citation_count": 60,
            "first_author": "Kurtz, Michael J.",
            "group": 1,
            "id": 114,
            "nodeWeight": 60,
            "node_name": "2000A&AS..143...41K",
            "read_count": 18.0,
            "title": "The NASA Astrophysics Data System: Overview"
          },
          {
            "citation_count": 15,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 115,
            "nodeWeight": 15,
            "node_name": "2007ApJ...666..231B",
            "read_count": 10.0,
            "title": "Stellar Velocity Dispersion of the Leo A Dwarf Galaxy"
          },
          {
            "citation_count": 1,
            "first_author": "Kurtz, M. J.",
            "group": 1,
            "id": 117,
            "nodeWeight": 1,
            "node_name": "2006ASPC..351..653K",
            "read_count": 6.0,
            "title": "Intelligent Information Retrieval"
          },
          {
            "citation_count": 15,
            "first_author": "Kurtz, Michael J.",
            "group": 1,
            "id": 118,
            "nodeWeight": 15,
            "node_name": "2005IPM....41.1395K",
            "read_count": 39.0,
            "title": "The Effect of Use and Access on Citations"
          },
          {
            "citation_count": 3,
            "first_author": "Accomazzi, A.",
            "group": 8,
            "id": 119,
            "nodeWeight": 3,
            "node_name": "1997ASPC..125..357A",
            "read_count": 3.0,
            "title": "Astronomical Information Discovery and Access: Design and Implementation of the ADS Bibliographic Services"
          },
          {
            "citation_count": 3,
            "first_author": "Fabricant, Daniel",
            "group": 0,
            "id": 120,
            "nodeWeight": 3,
            "node_name": "2013PASP..125.1362F",
            "read_count": 21.0,
            "title": "Measuring Galaxy Velocity Dispersions with Hectospec"
          },
          {
            "citation_count": 20,
            "first_author": "Accomazzi, Alberto",
            "group": 1,
            "id": 121,
            "nodeWeight": 20,
            "node_name": "2000A&AS..143...85A",
            "read_count": 18.0,
            "title": "The NASA Astrophysics Data System: Architecture"
          },
          {
            "citation_count": 11,
            "first_author": "Willmer, C. N. A.",
            "group": 0,
            "id": 122,
            "nodeWeight": 11,
            "node_name": "1996ApJS..104..199W",
            "read_count": 3.0,
            "title": "A Medium-deep Survey of a Minislice at the North Galactic Pole. II. The Data"
          },
          {
            "citation_count": 9,
            "first_author": "Barton, Elizabeth J.",
            "group": 0,
            "id": 123,
            "nodeWeight": 9,
            "node_name": "2000PASP..112..367B",
            "read_count": 2.0,
            "title": "Rotation Curve Measurement using Cross-Correlation"
          },
          {
            "citation_count": 2,
            "first_author": "Mink, D. J.",
            "group": 0,
            "id": 124,
            "nodeWeight": 2,
            "node_name": "2001ASPC..238..491M",
            "read_count": 4.0,
            "title": "SVDFIT: An IRAF Task for Eigenvector Sky Subtraction"
          },
          {
            "citation_count": 8,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 126,
            "nodeWeight": 8,
            "node_name": "1996ASPC..101..569E",
            "read_count": 4.0,
            "title": "Various Access Methods to the Abstracts in the Astrophysics Data System"
          },
          {
            "citation_count": 288,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 127,
            "nodeWeight": 288,
            "node_name": "1998PASP..110..934K",
            "read_count": 35.0,
            "title": "RVSAO 2.0: Digital Redshifts and Radial Velocities"
          },
          {
            "citation_count": 138,
            "first_author": "da Costa, L. Nicolaci",
            "group": 0,
            "id": 128,
            "nodeWeight": 138,
            "node_name": "1998AJ....116....1D",
            "read_count": 11.0,
            "title": "The Southern Sky Redshift Survey"
          },
          {
            "citation_count": 7,
            "first_author": "Geller, Margaret J.",
            "group": 0,
            "id": 129,
            "nodeWeight": 7,
            "node_name": "2011AJ....142..133G",
            "read_count": 21.0,
            "title": "Mapping the Universe: The 2010 Russell Lecture"
          },
          {
            "citation_count": 3,
            "first_author": "Eichhorn, Guenther",
            "group": 1,
            "id": 130,
            "nodeWeight": 3,
            "node_name": "2002Ap&SS.282..299E",
            "read_count": 5.0,
            "title": "The NASA Astrophysics Data System: Free Access to the Astronomical Literature On-line and through Email"
          },
          {
            "citation_count": 138,
            "first_author": "Rines, Kenneth",
            "group": 0,
            "id": 131,
            "nodeWeight": 138,
            "node_name": "2003AJ....126.2152R",
            "read_count": 38.0,
            "title": "CAIRNS: The Cluster and Infall Region Nearby Survey. I. Redshifts and Mass Profiles"
          },
          {
            "citation_count": 0,
            "first_author": "Kurtz, Michael Drew",
            "group": 6,
            "id": 132,
            "nodeWeight": 0,
            "node_name": "2014CTM....18..711K",
            "read_count": 0.0,
            "title": "Acoustic timescale characterisation of symmetric and asymmetric multidimensional hot spots"
          },
          {
            "citation_count": 167,
            "first_author": "Fabricant, Daniel",
            "group": 0,
            "id": 133,
            "nodeWeight": 167,
            "node_name": "2005PASP..117.1411F",
            "read_count": 43.0,
            "title": "Hectospec, the MMT's 300 Optical Fiber-Fed Spectrograph"
          },
          {
            "citation_count": 48,
            "first_author": "de Lapparent, Valerie",
            "group": 10,
            "id": 134,
            "nodeWeight": 48,
            "node_name": "1986ApJ...304..585D",
            "read_count": 2.0,
            "title": "The Shane-Wirtanen Counts: Systematics and Two-Point Correlation Function"
          },
          {
            "citation_count": 26,
            "first_author": "Rines, Kenneth",
            "group": 0,
            "id": 136,
            "nodeWeight": 26,
            "node_name": "2013ApJ...767...15R",
            "read_count": 61.0,
            "title": "Measuring the Ultimate Halo Mass of Galaxy Clusters: Redshifts and Mass Profiles from the Hectospec Cluster Survey (HeCS)"
          },
          {
            "citation_count": 28,
            "first_author": "Kurtz, M. J.",
            "group": 0,
            "id": 137,
            "nodeWeight": 28,
            "node_name": "1985AJ.....90.1665K",
            "read_count": 10.0,
            "title": "The X-ray cluster Abell 744."
          },
          {
            "citation_count": 55,
            "first_author": "Bromley, Benjamin C.",
            "group": 4,
            "id": 138,
            "nodeWeight": 55,
            "node_name": "2006ApJ...653.1194B",
            "read_count": 16.0,
            "title": "Hypervelocity Stars: Predicting the Spectrum of Ejection Velocities"
          }
        ]
      },
      "summaryGraph": {
        "directed": false,
        "graph": [],
        "links": [
          {
            "source": 0,
            "target": 0,
            "weight": 6486
          },
          {
            "source": 0,
            "target": 1,
            "weight": 78
          },
          {
            "source": 0,
            "target": 5,
            "weight": 146
          },
          {
            "source": 0,
            "target": 2,
            "weight": 759
          },
          {
            "source": 1,
            "target": 1,
            "weight": 9735
          },
          {
            "source": 1,
            "target": 5,
            "weight": 12
          },
          {
            "source": 1,
            "target": 2,
            "weight": 134
          },
          {
            "source": 1,
            "target": 4,
            "weight": 34
          },
          {
            "source": 2,
            "target": 2,
            "weight": 1033
          },
          {
            "source": 3,
            "target": 3,
            "weight": 80
          },
          {
            "source": 4,
            "target": 4,
            "weight": 99
          },
          {
            "source": 5,
            "target": 5,
            "weight": 158
          }
        ],
        "multigraph": false,
        "nodes": [
          {
            "id": 0,
            "node_label": {
              "abell": 20.61107285416508,
              "cluster": 52.99990162499593,
              "galaxies": 50.055462645829486,
              "mass": 20.61107285416508,
              "redshift": 52.99990162499593,
              "survey": 51.77971136794939
            },
            "node_name": 1,
            "paper_count": 60,
            "stable_index": 0,
            "top_common_references": {
              "1979AJ.....84.1511T": 0.25,
              "1989Sci...246..897G": 0.25,
              "1998PASP..110..934K": 0.43,
              "2005ApJ...635L.125G": 0.23,
              "2005PASP..117.1411F": 0.28
            },
            "total_citations": 2904,
            "total_reads": 907.0
          },
          {
            "id": 1,
            "node_label": {
              "ads": 23.372169270698247,
              "astrophysics": 18.6977354165586,
              "data": 18.972047772111416,
              "literature": 20.261626187458457,
              "service": 17.66663387499864,
              "system": 20.25588003460515
            },
            "node_name": 2,
            "paper_count": 46,
            "stable_index": 1,
            "top_common_references": {
              "1993ASPC...52..132K": 0.28,
              "2000A&AS..143...41K": 0.52,
              "2000A&AS..143...61E": 0.24,
              "2000A&AS..143...85A": 0.26,
              "2000A&AS..143..111G": 0.28
            },
            "total_citations": 227,
            "total_reads": 639.0
          },
          {
            "id": 4,
            "node_label": {
              "galactic": 9.005167194425981,
              "halo": 13.507750791638973,
              "hypervelocity": 17.66663387499864,
              "project": 8.83331693749932,
              "star": 20.261626187458457,
              "survey": 13.507750791638973
            },
            "node_name": 3,
            "paper_count": 13,
            "stable_index": 2,
            "top_common_references": {
              "1992A&AS...96..269S": 0.54,
              "1998ApJ...500..525S": 0.54,
              "1998PASP..110..934K": 0.54,
              "2002MNRAS.337...87C": 0.54,
              "2003AJ....126.1362B": 0.62
            },
            "total_citations": 640,
            "total_reads": 186.0
          },
          {
            "id": 6,
            "node_label": {
              "acoustic": 5.8888779583328805,
              "characterisation": 5.8888779583328805,
              "hot": 5.8888779583328805,
              "onedimensional": 2.9444389791664403,
              "spot": 5.8888779583328805,
              "timescale": 5.8888779583328805
            },
            "node_name": 5,
            "paper_count": 2,
            "stable_index": 3,
            "top_common_references": {
              "1991RSPSA.435..459S": 1.0,
              "2003JFM...476..267S": 1.0,
              "2008CTM....12.1009K": 1.0,
              "2009IJCFD..23..503R": 1.0,
              "2012CTM....16..650R": 1.0
            },
            "total_citations": 0,
            "total_reads": 0.0
          },
          {
            "id": 8,
            "node_label": {
              "astronomical": 1.8458266904983307,
              "bibliographic": 1.8458266904983307,
              "design": 2.9444389791664403,
              "implementation": 2.9444389791664403,
              "information": 2.2512917986064953,
              "services": 2.2512917986064953
            },
            "node_name": 6,
            "paper_count": 2,
            "stable_index": 4,
            "top_common_references": {
              "1995ASPC...77...36A": 1.0
            },
            "total_citations": 14,
            "total_reads": 7.0
          },
          {
            "id": 10,
            "node_label": {
              "correlation": 2.9444389791664403,
              "counts": 5.8888779583328805,
              "distribution": 2.9444389791664403,
              "shanewirtanen": 5.8888779583328805,
              "systematics": 2.9444389791664403,
              "twopoint": 2.9444389791664403
            },
            "node_name": 4,
            "paper_count": 3,
            "stable_index": 5,
            "top_common_references": {
              "1977ApJ...212L.107D": 1.0,
              "1977ApJ...217..385G": 1.0,
              "1977ApJS...34..425D": 1.0,
              "1979ApJ...234...13G": 1.0,
              "1983ApJ...264....1S": 1.0
            },
            "total_citations": 115,
            "total_reads": 5.0
          }
        ]
      }
    }, "msg" : {"numFound":100, "rows": 100}};

    var testDataSmall = {"data" : {
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
    }, "msg" : {"numFound":100, "rows": 100}};
//
//    var paperNetwork = new PaperNetwork();
//    paperNetwork.processResponse(new JsonResponse(testDataBig));
//
//    $("#test").append(paperNetwork.view.el);

    afterEach(function(){
    $("#test").empty();
    });

  it("should show angle of summary graph based on # of papers", function(){

      var paperNetwork = new PaperNetwork();
      paperNetwork.processResponse(new JsonResponse(testDataBig));

      $("#test").append(paperNetwork.view.el);
      var largeAdsGroupData = d3.selectAll("#vis-group-2").data()[0];
      var largeAstroGroupData = d3.selectAll("#vis-group-1").data()[0];

      //data contains number of papers
      expect(largeAdsGroupData.value).to.eql(46);
      expect(largeAstroGroupData.value).to.eql(60);

      //ratios of angles should roughly equal 60/46
      expect(((largeAstroGroupData.endAngle - largeAstroGroupData.startAngle)/(largeAdsGroupData.endAngle - largeAdsGroupData.startAngle)).toFixed(2)).to.eql((60/46).toFixed(2));
    });


    it("should allow the user to toggle to wedge sizing based on citations", function() {

      //also based on read_count, but that logic is identical

      var paperNetwork = new PaperNetwork();
      paperNetwork.processResponse(new JsonResponse(testDataBig));
      $("#test").append(paperNetwork.view.el);

      //test sizing by citation

      paperNetwork.view.graphView.model.set("mode", "total_citations");

        var largeAdsGroupData = d3.selectAll("#vis-group-2").data()[0];
        var largeAstroGroupData = d3.selectAll("#vis-group-1").data()[0];

        //data contains number of papers
        expect(largeAdsGroupData.value).to.eql(227);
        expect(largeAstroGroupData.value).to.eql(2904);

        //ratio of angles should roughly equal 2904/227
        expect(((largeAstroGroupData.endAngle - largeAstroGroupData.startAngle) / (largeAdsGroupData.endAngle - largeAdsGroupData.startAngle)).toFixed(2)).to.eql((2904 / 227).toFixed(2));

    });

    it("should allow the user to toggle to wedge sizing based on references in common", function() {

      var paperNetwork = new PaperNetwork();
      paperNetwork.processResponse(new JsonResponse(testDataBig));
      $("#test").append(paperNetwork.view.el);

      //test sizing by citation

      paperNetwork.view.graphView.model.set("mode", "references");

        var largeAdsGroupData = d3.selectAll("#vis-group-2").data()[0];
        var largeAstroGroupData = d3.selectAll("#vis-group-1").data()[0];

        //data contains number of papers
        expect(largeAdsGroupData.value).to.eql(9735);
        expect(largeAstroGroupData.value).to.eql(6486);

        //ratio of angles should roughly equal 6486/9735
        expect(((largeAstroGroupData.endAngle - largeAstroGroupData.startAngle) / (largeAdsGroupData.endAngle - largeAdsGroupData.startAngle)).toFixed(2)).to.eql((6486/9735).toFixed(2));


    });

    it("should show links corresponding to the summary node graph links", function(){

    var paperNetwork = new PaperNetwork();
    paperNetwork.processResponse(new JsonResponse(testDataBig));
    $("#test").append(paperNetwork.view.el);

      var linkNum = d3.selectAll(".link")[0].length;

      actualLinkNum = _.filter(testDataBig.data.summaryGraph.links, function(l){
        if(l.source !== l.target){
          return true
        }
      }).length;

      expect(linkNum).to.be.eql(actualLinkNum);

    });


    it("should display a view with several details about the group when the group is selected", function(){

      var paperNetwork = new PaperNetwork();
      paperNetwork.processResponse(new JsonResponse(testDataBig));
      $("#test").append(paperNetwork.view.el);

      //to change the detail view, set the current group in the container view model to the group id
      paperNetwork.view.graphView.model.set("selectedEntity", 0);

      //summary
      expect($(".network-detail-area p:first").text()).to.eql('This group consists of 60 papers, which have been cited, in total, 2904\n           times.');
     //most highly cited
      expect($(".network-detail-area .s-top-papers").find("li:first").html().trim()).to.eql('<a href="#abs/1999PASP..111..438F" data-bypass="" target="_blank"><b>The Updated Zwicky Catalog (UZC)</b></a>; <i>Falco, Emilio E.</i>\n                   (295 citations)');
     //most highly referenced
      expect($(".network-detail-area ul:last li:first").html().trim()).to.eql('<i class="fa fa-star-o"></i>\n                   \n                   <a href="#abs/" data-bypass="" target="_blank">2005PASP..117.1411F</a> (cited by 28% of papers\n                   in this group)');

    });

    it("should display a view with a list of most significant common references when a link between groups is selected", function(){

      var paperNetwork = new PaperNetwork();
      paperNetwork.processResponse(new JsonResponse(testDataBig));
      $("#test").append(paperNetwork.view.el);


     var group1 = d3.selectAll(".node-path").filter(function(d){return d.data.id == 0 })[0][0].__data__;
     var group2 = d3.selectAll(".node-path").filter(function(d){return d.data.id == 1 })[0][0].__data__;

      //to change the detail view, set the current group in the container view model to the group id
      paperNetwork.view.graphView.model.set("selectedEntity", [group1, group2]);

      expect($(".network-detail-area tbody tr:first").html().trim()).to.eql('<td>\n                <a href="#abs/1986SPIE..627..733T">1986SPIE..627..733T</a>\n            </td>\n            <td>\n                0.61%\n            </td>\n            <td>\n                0.69%\n            </td>')


    });



    it("has a filter capability that allows you to add or remove groups or individual nodes, and submit as a filter", function(){

      var paperNetwork = new PaperNetwork();
      paperNetwork.processResponse(new JsonResponse(testDataBig));
      $("#test").append(paperNetwork.view.el);

      // this would be the result of a click on group 1
      paperNetwork.view.graphView.model.set("selectedEntity", 8);
      $("#test").find("button.filter-add").click();
      paperNetwork.view.graphView.model.set("selectedEntity", 1);
      $("#test").find("button.filter-add").click();

      expect($("#test").find(".s-filter-names-container").text().trim()).to.eql('Group 6  OR \n        \n            Group 2')
      $("#test").find("button.filter-remove").click();

      expect($("#test").find(".s-filter-names-container").text().trim()).to.eql('Group 6');

      var minsub = new MinPubSub({verbose: false});

      paperNetwork.setCurrentQuery(new ApiQuery({q: "original search"}));
      paperNetwork.activate(minsub.beehive.getHardenedInstance());
      paperNetwork.pubsub.publish = sinon.stub();

      $("#test").find(".apply-filter").click();

      expect(paperNetwork.pubsub.publish.called).to.be.true;
      expect(paperNetwork.pubsub.publish.args[0][0]).to.eql("[PubSub]-New-Query");
      expect(paperNetwork.pubsub.publish.args[0][1].get("fq")).to.eql(["(bibcode:(1995ASPC...77...28E OR 1997ASPC..125..357A))"]);
    });



  });

});