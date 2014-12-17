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
              "2005JASIS..56..111K"
            ],
            "source": 0,
            "target": 11,
            "weight": 21
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 0,
            "target": 15,
            "weight": 23
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 0,
            "target": 48,
            "weight": 9
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 0,
            "target": 51,
            "weight": 23
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 0,
            "target": 82,
            "weight": 16
          },
          {
            "overlap": [
              "2010ARIST..44....3K"
            ],
            "source": 0,
            "target": 28,
            "weight": 14
          },
          {
            "overlap": [
              "1994BAAS...26.1370A"
            ],
            "source": 1,
            "target": 75,
            "weight": 23
          },
          {
            "overlap": [
              "2003BAAS...35.1241K"
            ],
            "source": 1,
            "target": 48,
            "weight": 10
          },
          {
            "overlap": [
              "2003BAAS...35.1241K"
            ],
            "source": 1,
            "target": 6,
            "weight": 20
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E"
            ],
            "source": 2,
            "target": 40,
            "weight": 11
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E"
            ],
            "source": 2,
            "target": 12,
            "weight": 29
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 3,
            "target": 6,
            "weight": 24
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 3,
            "target": 38,
            "weight": 28
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 3,
            "target": 43,
            "weight": 14
          },
          {
            "overlap": [
              "1972Sci...178..471G"
            ],
            "source": 3,
            "target": 48,
            "weight": 12
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 3,
            "target": 82,
            "weight": 20
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 3,
            "target": 85,
            "weight": 12
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 4,
            "target": 22,
            "weight": 4
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 4,
            "target": 62,
            "weight": 6
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 4,
            "target": 34,
            "weight": 4
          },
          {
            "overlap": [
              "1993ApJ...404..441K",
              "1998SPIE.3355..285F",
              "2002SPIE.4836...73W",
              "1998ApJ...504..636H"
            ],
            "source": 5,
            "target": 8,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 9,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 16,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 22,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 25,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 30,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 33,
            "weight": 21
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2002SPIE.4836...73W"
            ],
            "source": 5,
            "target": 34,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 36,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 39,
            "weight": 10
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1993ApJ...404..441K",
              "1990ApJ...349L...1T",
              "2002SPIE.4836...73W"
            ],
            "source": 5,
            "target": 42,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 50,
            "weight": 1
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "1998SPIE.3355..324R",
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 5,
            "target": 53,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 56,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 59,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 63,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 67,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K",
              "2002SPIE.4836...73W"
            ],
            "source": 5,
            "target": 69,
            "weight": 7
          },
          {
            "overlap": [
              "2002SPIE.4836...73W"
            ],
            "source": 5,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 74,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "2002SPIE.4836...73W"
            ],
            "source": 5,
            "target": 84,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1997AJ....113..483R"
            ],
            "source": 5,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 5,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 6,
            "target": 91,
            "weight": 17
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "1999ASPC..172..291A"
            ],
            "source": 6,
            "target": 38,
            "weight": 28
          },
          {
            "overlap": [
              "2005IPM....41.1395K"
            ],
            "source": 6,
            "target": 11,
            "weight": 13
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 6,
            "target": 71,
            "weight": 14
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 6,
            "target": 43,
            "weight": 7
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 6,
            "target": 21,
            "weight": 34
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2003BAAS...35.1241K"
            ],
            "source": 6,
            "target": 48,
            "weight": 12
          },
          {
            "overlap": [
              "2006cs........8027H",
              "2005IPM....41.1395K"
            ],
            "source": 6,
            "target": 51,
            "weight": 29
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 6,
            "target": 81,
            "weight": 11
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 6,
            "target": 82,
            "weight": 10
          },
          {
            "overlap": [
              "1999ASPC..172..291A"
            ],
            "source": 6,
            "target": 85,
            "weight": 6
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 6,
            "target": 28,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 88,
            "weight": 32
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 7,
            "target": 37,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "1993adass...2..132K"
            ],
            "source": 7,
            "target": 40,
            "weight": 25
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 12,
            "weight": 32
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 14,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 75,
            "weight": 22
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 48,
            "weight": 9
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 79,
            "weight": 8
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 52,
            "weight": 26
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 81,
            "weight": 18
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 82,
            "weight": 16
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1993ASPC...52..132K"
            ],
            "source": 7,
            "target": 85,
            "weight": 19
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 7,
            "target": 28,
            "weight": 14
          },
          {
            "overlap": [
              "1998astro.ph..9268K"
            ],
            "source": 8,
            "target": 62,
            "weight": 2
          },
          {
            "overlap": [
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W"
            ],
            "source": 8,
            "target": 84,
            "weight": 9
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 8,
            "target": 33,
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
            "source": 8,
            "target": 34,
            "weight": 8
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
            "source": 8,
            "target": 69,
            "weight": 10
          },
          {
            "overlap": [
              "2007A&A...462..875S",
              "2012ApJ...750..168K",
              "2001ApJ...557L..89W",
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
              "2002SPIE.4836...73W",
              "2012MNRAS.425.2287H",
              "2007A&A...470..821D",
              "2002ApJ...575..640W"
            ],
            "source": 8,
            "target": 42,
            "weight": 31
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 8,
            "target": 72,
            "weight": 8
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 8,
            "target": 74,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 8,
            "target": 46,
            "weight": 7
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2010ApJ...709..832G",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 8,
            "target": 63,
            "weight": 10
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2004MNRAS.350..893H",
              "2005ApJ...635L.125G",
              "2007ApJ...669..714M",
              "2007A&A...462..459G",
              "2009ApJ...702..980K",
              "2005A&A...442...43H",
              "2008MNRAS.385..695B",
              "2002ApJ...580L..97M",
              "2007A&A...462..473M",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2001ApJ...557L..89W",
              "1998SPIE.3355..285F",
              "2007A&A...462..875S",
              "2007A&A...470..821D"
            ],
            "source": 8,
            "target": 53,
            "weight": 31
          },
          {
            "overlap": [
              "1998astro.ph..9268K"
            ],
            "source": 8,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 8,
            "target": 59,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 8,
            "target": 56,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 8,
            "target": 58,
            "weight": 3
          },
          {
            "overlap": [
              "1967BOTT....4...86P",
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "1995ApJS..101..117K",
              "2003ApJ...599.1129Y",
              "1999MNRAS.310..645W",
              "2003AJ....126.1362B",
              "1955ApJ...121..161S",
              "1998PASP..110..934K",
              "2006ApJ...636L..37F",
              "1988Natur.331..687H",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G"
            ],
            "source": 9,
            "target": 16,
            "weight": 35
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "1997ApJ...482..677Y",
              "2005ApJ...628..246E",
              "1999MNRAS.310..645W",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "2000ApJ...544..437P",
              "1991AJ....101..562L",
              "2006ApJ...636L..37F",
              "2003AJ....126.1362B",
              "2003AJ....125.1261D",
              "2003ApJ...599.1129Y",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G",
              "1993ASPC...45..360L",
              "1967BOTT....4...86P",
              "2006ApJ...640L..35B",
              "1998ARA&A..36..435M",
              "2004MNRAS.352..285C"
            ],
            "source": 9,
            "target": 22,
            "weight": 31
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 25,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 30,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 33,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 34,
            "weight": 1
          },
          {
            "overlap": [
              "1995ApJS..101..117K",
              "1998PASP..110...79F",
              "2003AJ....126.1362B",
              "2004MNRAS.352..285C"
            ],
            "source": 9,
            "target": 35,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 36,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 39,
            "weight": 7
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "2006ApJ...647..303B",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "2000ApJ...544..437P",
              "1991AJ....101..562L",
              "2003ApJ...596.1015S",
              "2006ApJ...651..392S",
              "2003ApJ...599.1129Y",
              "1988Natur.331..687H",
              "2005ApJ...620..744G",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G",
              "2006MNRAS.372..174B",
              "1967BOTT....4...86P",
              "2006ApJ...640L..35B"
            ],
            "source": 9,
            "target": 41,
            "weight": 32
          },
          {
            "overlap": [
              "2006ApJ...647..303B"
            ],
            "source": 9,
            "target": 44,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 50,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 53,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 56,
            "weight": 4
          },
          {
            "overlap": [
              "1999MNRAS.310..645W",
              "1961BAN....15..265B",
              "1995ApJS..101..117K",
              "2003ApJ...596.1015S",
              "2003AJ....126.1362B",
              "2003ApJ...599.1129Y",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "2005ApJ...620..744G",
              "1992A&AS...96..269S",
              "1993ASPC...45..360L"
            ],
            "source": 9,
            "target": 59,
            "weight": 21
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "2004MNRAS.352..285C",
              "2003AJ....126.1362B",
              "1995ApJS..101..117K"
            ],
            "source": 9,
            "target": 61,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 9,
            "target": 62,
            "weight": 2
          },
          {
            "overlap": [
              "1998ARA&A..36..435M",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 63,
            "weight": 3
          },
          {
            "overlap": [
              "2006ApJ...648..976M",
              "2006ApJ...653.1203L",
              "2007ApJ...656..709P",
              "2005ApJ...622L..33B",
              "2006astro.ph..9046O",
              "1997ApJ...482..677Y",
              "2006ApJ...647..303B",
              "2005ApJ...628..246E",
              "2007AJ....133..882K",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "2000ApJ...544..437P",
              "1991AJ....101..562L",
              "1993ASPC...45..360L",
              "2004MNRAS.352..285C",
              "1998PASP..110...79F",
              "1988Natur.331..687H",
              "2007MNRAS.376L..29G",
              "2003AJ....126.1362B",
              "2003ApJ...599.1129Y",
              "2006ApJ...651..392S",
              "2005A&A...444L..61H",
              "2006ApJ...636L..37F",
              "1998PASP..110..934K",
              "2005ApJ...634L.181E",
              "2005ApJ...620..744G",
              "1992A&AS...96..269S",
              "2002MNRAS.333..463D",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G",
              "2006MNRAS.372..174B",
              "2007A&A...461..651D",
              "2006ApJ...640L..35B",
              "2006ApJ...653.1194B"
            ],
            "source": 9,
            "target": 67,
            "weight": 48
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
              "1998PASP..110..934K",
              "1998PASP..110...79F"
            ],
            "source": 9,
            "target": 74,
            "weight": 3
          },
          {
            "overlap": [
              "2006ApJ...647..303B",
              "1998ARA&A..36..435M",
              "1998PASP..110..934K",
              "1992A&AS...96..269S",
              "2007ApJ...660..311B"
            ],
            "source": 9,
            "target": 80,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 9,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 9,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1994Natur.370..194I"
            ],
            "source": 10,
            "target": 61,
            "weight": 3
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 10,
            "target": 31,
            "weight": 7
          },
          {
            "overlap": [
              "1994Natur.370..194I"
            ],
            "source": 10,
            "target": 30,
            "weight": 3
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 10,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 10,
            "target": 74,
            "weight": 4
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I"
            ],
            "source": 10,
            "target": 77,
            "weight": 6
          },
          {
            "overlap": [
              "1990AJ.....99.2019L"
            ],
            "source": 10,
            "target": 50,
            "weight": 2
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I"
            ],
            "source": 10,
            "target": 22,
            "weight": 3
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I",
              "1991AJ....101..892M"
            ],
            "source": 10,
            "target": 58,
            "weight": 12
          },
          {
            "overlap": [
              "2005JASIS..56...36K"
            ],
            "source": 11,
            "target": 14,
            "weight": 14
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 11,
            "target": 15,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 18,
            "weight": 12
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 20,
            "weight": 29
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 24,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 11,
            "target": 28,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 35,
            "weight": 5
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 38,
            "weight": 29
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 40,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 43,
            "weight": 7
          },
          {
            "overlap": [
              "2005JASIS..56..111K",
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 11,
            "target": 48,
            "weight": 18
          },
          {
            "overlap": [
              "2005IPM....41.1395K",
              "2005JASIS..56..111K",
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 51,
            "weight": 44
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 54,
            "weight": 35
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 65,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 66,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 71,
            "weight": 14
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 11,
            "target": 75,
            "weight": 29
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 81,
            "weight": 24
          },
          {
            "overlap": [
              "2005JASIS..56..111K",
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 82,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 85,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 11,
            "target": 91,
            "weight": 17
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 88,
            "weight": 32
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 12,
            "target": 45,
            "weight": 57
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 12,
            "target": 65,
            "weight": 15
          },
          {
            "overlap": [
              "1997Ap&SS.247..189E",
              "1993ASPC...52..132K",
              "1996ASPC..101..569E"
            ],
            "source": 12,
            "target": 40,
            "weight": 38
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 14,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 75,
            "weight": 22
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 48,
            "weight": 9
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 79,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 52,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 81,
            "weight": 18
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 82,
            "weight": 16
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 12,
            "target": 85,
            "weight": 9
          },
          {
            "overlap": [
              "2003ApJS..148..175S"
            ],
            "source": 13,
            "target": 34,
            "weight": 5
          },
          {
            "overlap": [
              "2007ASPC..377..106H"
            ],
            "source": 13,
            "target": 71,
            "weight": 18
          },
          {
            "overlap": [
              "2003ApJS..148..175S"
            ],
            "source": 13,
            "target": 44,
            "weight": 4
          },
          {
            "overlap": [
              "1993ASSL..182...21K"
            ],
            "source": 13,
            "target": 14,
            "weight": 17
          },
          {
            "overlap": [
              "2007ASPC..377..106H"
            ],
            "source": 13,
            "target": 26,
            "weight": 31
          },
          {
            "overlap": [
              "1993ASSL..182...21K",
              "2009arXiv0912.5235K"
            ],
            "source": 13,
            "target": 28,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 14,
            "target": 88,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 14,
            "target": 40,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143....9W"
            ],
            "source": 14,
            "target": 65,
            "weight": 10
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 14,
            "target": 52,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K"
            ],
            "source": 14,
            "target": 75,
            "weight": 30
          },
          {
            "overlap": [
              "2008PNAS..105.1118R",
              "2005JASIS..56...36K",
              "1993ASPC...52..132K"
            ],
            "source": 14,
            "target": 48,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143....9W"
            ],
            "source": 14,
            "target": 18,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 14,
            "target": 79,
            "weight": 5
          },
          {
            "overlap": [
              "2005JASIS..56...36K"
            ],
            "source": 14,
            "target": 20,
            "weight": 15
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K",
              "2000A&AS..143....9W"
            ],
            "source": 14,
            "target": 81,
            "weight": 37
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2005JASIS..56...36K"
            ],
            "source": 14,
            "target": 82,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 14,
            "target": 85,
            "weight": 6
          },
          {
            "overlap": [
              "1993ASSL..182...21K",
              "2005JASIS..56...36K"
            ],
            "source": 14,
            "target": 28,
            "weight": 18
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 15,
            "target": 82,
            "weight": 12
          },
          {
            "overlap": [
              "2006JEPub...9....2H",
              "2005JASIS..56..111K"
            ],
            "source": 15,
            "target": 48,
            "weight": 13
          },
          {
            "overlap": [
              "2005JASIS..56..111K"
            ],
            "source": 15,
            "target": 51,
            "weight": 16
          },
          {
            "overlap": [
              "1967BOTT....4...86P",
              "2005ApJ...622L..33B",
              "2005astro.ph..8193L",
              "2005ApJ...634L.181E",
              "2002MNRAS.337...87C",
              "2005ApJ...628..246E",
              "2003AJ....125..984M",
              "1961BAN....15..265B",
              "2005ApJ...629..268H",
              "1993ASPC...45..360L",
              "2005astro.ph.12344H",
              "2003ApJ...599.1129Y",
              "1999MNRAS.310..645W",
              "2006ApJS..162...38A",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "2002AJ....123.3154D",
              "1998PASP..110..934K",
              "2006ApJ...636L..37F",
              "1988Natur.331..687H",
              "2000ApJ...540..825Y",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G"
            ],
            "source": 16,
            "target": 22,
            "weight": 48
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 25,
            "weight": 4
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K",
              "2000ApJ...540..825Y",
              "2002MNRAS.337...87C"
            ],
            "source": 16,
            "target": 30,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 33,
            "weight": 7
          },
          {
            "overlap": [
              "2006ApJS..162...38A",
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 34,
            "weight": 3
          },
          {
            "overlap": [
              "2000ApJ...540..825Y",
              "1995ApJS..101..117K",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "2002MNRAS.337...87C"
            ],
            "source": 16,
            "target": 35,
            "weight": 12
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 36,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 39,
            "weight": 11
          },
          {
            "overlap": [
              "1967BOTT....4...86P",
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2005ApJ...629..268H",
              "2005astro.ph.12344H",
              "2003ApJ...599.1129Y",
              "2006ApJS..162...38A",
              "2003ApJ...593L..77H",
              "1988Natur.331..687H",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...628L.113S",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G"
            ],
            "source": 16,
            "target": 41,
            "weight": 38
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 16,
            "target": 44,
            "weight": 2
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 16,
            "target": 46,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 50,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 53,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 56,
            "weight": 3
          },
          {
            "overlap": [
              "2002MNRAS.337...87C",
              "2003AJ....125..984M",
              "1961BAN....15..265B",
              "1995ApJS..101..117K",
              "2003ApJ...599.1129Y",
              "2003AJ....126.1362B",
              "1999MNRAS.310..645W",
              "1998PASP..110..934K",
              "1988Natur.331..687H",
              "1992A&AS...96..269S",
              "2000ApJ...540..825Y",
              "1993ASPC...45..360L"
            ],
            "source": 16,
            "target": 59,
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
            "source": 16,
            "target": 61,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "2005astro.ph.12344H",
              "2003AJ....125..984M",
              "1988Natur.331..687H",
              "2006ApJS..162...38A",
              "2003AJ....126.1362B",
              "2003ApJ...599.1129Y",
              "2005A&A...444L..61H",
              "2006ApJ...636L..37F",
              "1998PASP..110..934K",
              "2005ApJ...634L.181E",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "2005MNRAS.363..223G"
            ],
            "source": 16,
            "target": 67,
            "weight": 36
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 69,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 74,
            "weight": 4
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "2002AJ....123.3154D",
              "1998PASP..110..934K",
              "1992A&AS...96..269S"
            ],
            "source": 16,
            "target": 80,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 16,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 17,
            "target": 29,
            "weight": 13
          },
          {
            "overlap": [
              "1995ASPC...77..496M",
              "1992ASPC...25..432K"
            ],
            "source": 17,
            "target": 89,
            "weight": 22
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 17,
            "target": 90,
            "weight": 12
          },
          {
            "overlap": [
              "1992ASPC...25..432K"
            ],
            "source": 17,
            "target": 56,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 91,
            "weight": 50
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143..111G",
              "2000A&AS..143...61E",
              "2000A&AS..143...85A",
              "2000A&AS..143...23O",
              "2000A&AS..143....9W",
              "2000A&AS..143...41K",
              "2004SPIE.5493..137Q"
            ],
            "source": 18,
            "target": 65,
            "weight": 73
          },
          {
            "overlap": [
              "2000A&AS..143...23O",
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 35,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 18,
            "target": 66,
            "weight": 67
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 51,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 38,
            "weight": 13
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 40,
            "weight": 30
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 71,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 18,
            "target": 43,
            "weight": 20
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 75,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 18,
            "target": 48,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 54,
            "weight": 33
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "1992ASPC...25...47M",
              "2000A&AS..143...61E"
            ],
            "source": 18,
            "target": 79,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 20,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143....9W",
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 81,
            "weight": 34
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 82,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...61E",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 24,
            "weight": 67
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 18,
            "target": 85,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 18,
            "target": 28,
            "weight": 8
          },
          {
            "overlap": [
              "1981AJ.....86..476J",
              "1980A&A....82..322D"
            ],
            "source": 19,
            "target": 29,
            "weight": 8
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 19,
            "target": 89,
            "weight": 3
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 19,
            "target": 31,
            "weight": 5
          },
          {
            "overlap": [
              "1980A&A....82..322D"
            ],
            "source": 19,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1981AJ.....86..476J",
              "1982SPIE..331..465V"
            ],
            "source": 19,
            "target": 37,
            "weight": 9
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 19,
            "target": 76,
            "weight": 4
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 19,
            "target": 86,
            "weight": 4
          },
          {
            "overlap": [
              "1984ApJ...286..186F",
              "1974ApJ...191L..51H",
              "1986ApJ...308..530F",
              "1985AJ.....90.1665K"
            ],
            "source": 19,
            "target": 47,
            "weight": 14
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 19,
            "target": 78,
            "weight": 14
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1991RC3...C......0D",
              "1986ApJ...308..530F",
              "1978AJ.....83..904S"
            ],
            "source": 19,
            "target": 50,
            "weight": 5
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 19,
            "target": 79,
            "weight": 3
          },
          {
            "overlap": [
              "1990MNRAS.245..559E"
            ],
            "source": 19,
            "target": 53,
            "weight": 3
          },
          {
            "overlap": [
              "1990SPIE.1235..747F",
              "1985AJ.....90.1665K"
            ],
            "source": 19,
            "target": 49,
            "weight": 7
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 19,
            "target": 23,
            "weight": 4
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1953ApJ...118..502K",
              "1990SPIE.1235..747F",
              "1985AJ.....90.1665K"
            ],
            "source": 19,
            "target": 56,
            "weight": 14
          },
          {
            "overlap": [
              "1980A&A....82..322D",
              "1990ApJ...364..433S"
            ],
            "source": 19,
            "target": 57,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 91,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 65,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 35,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 66,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 38,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 40,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 71,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 43,
            "weight": 7
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 75,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56..786T",
              "2005JASIS..56...36K"
            ],
            "source": 20,
            "target": 48,
            "weight": 20
          },
          {
            "overlap": [
              "2003lisa.conf..223K",
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 51,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 54,
            "weight": 38
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 81,
            "weight": 26
          },
          {
            "overlap": [
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 82,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 24,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 20,
            "target": 85,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 20,
            "target": 28,
            "weight": 19
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 21,
            "target": 91,
            "weight": 49
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 21,
            "target": 71,
            "weight": 40
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 21,
            "target": 48,
            "weight": 16
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 21,
            "target": 81,
            "weight": 32
          },
          {
            "overlap": [
              "2002SPIE.4847..238K"
            ],
            "source": 21,
            "target": 28,
            "weight": 24
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 25,
            "weight": 2
          },
          {
            "overlap": [
              "1994ApJS...94..687W",
              "1999AJ....117.2308W",
              "1998PASP..110..934K",
              "1994AJ....108.1722K",
              "1998ApJ...500..525S",
              "2002MNRAS.337...87C",
              "1973AJ.....78.1074O",
              "2000ApJ...540..825Y"
            ],
            "source": 22,
            "target": 30,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 33,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1999PASP..111..438F",
              "2006ApJS..162...38A"
            ],
            "source": 22,
            "target": 34,
            "weight": 3
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "2004MNRAS.352..285C",
              "1994AJ....108.1722K",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "2002MNRAS.337...87C",
              "2000ApJ...540..825Y"
            ],
            "source": 22,
            "target": 35,
            "weight": 11
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 36,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 39,
            "weight": 7
          },
          {
            "overlap": [
              "1967BOTT....4...86P",
              "2005ApJ...622L..33B",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "2003ApJ...599.1129Y",
              "2006ApJS..162...38A",
              "2005astro.ph.12344H",
              "2005ApJ...629..268H",
              "2005A&A...444L..61H",
              "2005ApJ...634L.181E",
              "1988Natur.331..687H",
              "1992A&AS...96..269S",
              "1991AJ....101..562L",
              "2005MNRAS.363..223G",
              "2006ApJ...640L..35B",
              "2000ApJ...544..437P",
              "2005ApJ...634..344G"
            ],
            "source": 22,
            "target": 41,
            "weight": 28
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 22,
            "target": 44,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 22,
            "target": 46,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 50,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 53,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 56,
            "weight": 2
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I"
            ],
            "source": 22,
            "target": 58,
            "weight": 2
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "1998PASP..110..934K",
              "2004MNRAS.349..821L",
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "1994AJ....108.1722K",
              "2003ApJ...599.1129Y",
              "2003AJ....125..984M",
              "1999MNRAS.310..645W",
              "2003AJ....126.1362B",
              "2002MNRAS.337...87C",
              "1988Natur.331..687H",
              "2000ApJ...540..825Y",
              "1992A&AS...96..269S"
            ],
            "source": 22,
            "target": 59,
            "weight": 26
          },
          {
            "overlap": [
              "1999AJ....117.2308W",
              "2005MNRAS.362..349C",
              "2004MNRAS.352..285C",
              "1994AJ....108.1722K",
              "2003AJ....125..984M",
              "2006ApJS..162...38A",
              "1998ApJ...500..525S",
              "2003AJ....126.1362B",
              "2004AJ....128.2474M",
              "2005ApJ...629..268H",
              "2005AJ....130.1097B",
              "2002MNRAS.337...87C"
            ],
            "source": 22,
            "target": 61,
            "weight": 14
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 22,
            "target": 62,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998ARA&A..36..435M"
            ],
            "source": 22,
            "target": 63,
            "weight": 3
          },
          {
            "overlap": [
              "1994ApJS...94..687W",
              "2003ApJS..149...67B",
              "2005ApJ...622L..33B",
              "2005MNRAS.362..349C",
              "1997ApJ...482..677Y",
              "1998PASP..110..934K",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "1991AJ....102..704H",
              "1993ASPC...45..360L",
              "2004MNRAS.350..615G",
              "2004MNRAS.352..285C",
              "1995ApJ...453..214P",
              "2003ApJ...599.1129Y",
              "2005MNRAS.364...59D",
              "2003AJ....125..984M",
              "2006ApJS..162...38A",
              "2003AJ....126.1362B",
              "2005astro.ph.12344H",
              "2004AJ....128.2474M",
              "2005A&A...444L..61H",
              "2005AJ....130.1097B",
              "2006ApJ...636L..37F",
              "2005ApJ...634L.181E",
              "1973AJ.....78.1074O",
              "1988Natur.331..687H",
              "2002ApJ...573..359A",
              "1992A&AS...96..269S",
              "1991AJ....101..562L",
              "2005MNRAS.363..223G",
              "1996AJ....111.1748F",
              "2006ApJ...640L..35B",
              "2000ApJ...544..437P",
              "2005ApJ...634..344G"
            ],
            "source": 22,
            "target": 67,
            "weight": 48
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998ApJ...500..525S",
              "1996AJ....111.1748F"
            ],
            "source": 22,
            "target": 74,
            "weight": 4
          },
          {
            "overlap": [
              "1995MNRAS.277.1354I"
            ],
            "source": 22,
            "target": 77,
            "weight": 2
          },
          {
            "overlap": [
              "2002AJ....124..896S",
              "1998PASP..110..934K",
              "2003AJ....126.2048G",
              "1998AJ....116.1244T",
              "1998ApJ...500..525S",
              "2002AJ....123.3154D",
              "1992A&AS...96..269S",
              "1996ApJ...462..203Y",
              "1998ARA&A..36..435M",
              "2004ApJ...611L..93V"
            ],
            "source": 22,
            "target": 80,
            "weight": 17
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 22,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 23,
            "target": 29,
            "weight": 4
          },
          {
            "overlap": [
              "1983ApJS...52...89H"
            ],
            "source": 23,
            "target": 31,
            "weight": 5
          },
          {
            "overlap": [
              "1983ApJ...270...20B"
            ],
            "source": 23,
            "target": 47,
            "weight": 3
          },
          {
            "overlap": [
              "1983ApJ...267..465D"
            ],
            "source": 23,
            "target": 79,
            "weight": 3
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1981ApJ...248L..57K"
            ],
            "source": 23,
            "target": 37,
            "weight": 6
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 23,
            "target": 42,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1981ApJ...248L..57K"
            ],
            "source": 23,
            "target": 76,
            "weight": 9
          },
          {
            "overlap": [
              "1978AJ.....83.1549K",
              "1983ApJS...52...89H",
              "1982AJ.....87.1355G"
            ],
            "source": 23,
            "target": 50,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 23,
            "target": 53,
            "weight": 3
          },
          {
            "overlap": [
              "1977AJ.....82..249S",
              "1983ApJ...273...16M",
              "1982ApJ...263L..47K",
              "1977ApJ...217..385G",
              "1977ApJS...34..425D",
              "1977ApJ...212L.107D",
              "1979ApJ...234...13G",
              "1983ApJ...264....1S"
            ],
            "source": 23,
            "target": 55,
            "weight": 47
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 23,
            "target": 57,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 91,
            "weight": 71
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 24,
            "target": 65,
            "weight": 51
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 35,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 24,
            "target": 66,
            "weight": 95
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 38,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 40,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 71,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 24,
            "target": 43,
            "weight": 28
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 24,
            "target": 85,
            "weight": 24
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 75,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 24,
            "target": 48,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 54,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 51,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 81,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 82,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 24,
            "target": 79,
            "weight": 20
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 24,
            "target": 28,
            "weight": 11
          },
          {
            "overlap": [
              "1989AJ.....98..755R"
            ],
            "source": 25,
            "target": 27,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 30,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 33,
            "weight": 4
          },
          {
            "overlap": [
              "2000ApJ...530..660B",
              "2004MNRAS.353..713K",
              "1931ApJ....74...43H",
              "2005PASP..117..227K",
              "1998PASP..110..934K",
              "2001PASP..113.1449C",
              "2003AJ....126.2152R"
            ],
            "source": 25,
            "target": 34,
            "weight": 8
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110...79F",
              "2002AJ....123..485S"
            ],
            "source": 25,
            "target": 35,
            "weight": 4
          },
          {
            "overlap": [
              "2000ApJ...530..660B",
              "1987ApJS...63..295V",
              "1997A&AS..122..399P",
              "1998PASP..110..934K",
              "1984ApJ...281...95P",
              "1989ApJ...347..152S",
              "1980ApJ...236..351D"
            ],
            "source": 25,
            "target": 36,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 39,
            "weight": 7
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 25,
            "target": 44,
            "weight": 1
          },
          {
            "overlap": [
              "1982AJ.....87..945K"
            ],
            "source": 25,
            "target": 47,
            "weight": 2
          },
          {
            "overlap": [
              "1996ApJ...458..435C",
              "1996ApJ...470..724M",
              "1997ApJ...476L...7C"
            ],
            "source": 25,
            "target": 49,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1997AJ....113..492C",
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 50,
            "weight": 2
          },
          {
            "overlap": [
              "2004ApJ...610..745L",
              "1996MNRAS.281..799E",
              "1989ApJS...70....1A",
              "1998PASP..110..934K",
              "2003AJ....126.2152R"
            ],
            "source": 25,
            "target": 53,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "2000AJ....119.2498J",
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 56,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 59,
            "weight": 2
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110...79F"
            ],
            "source": 25,
            "target": 61,
            "weight": 2
          },
          {
            "overlap": [
              "2001ApJ...560..566K",
              "2001MNRAS.326..255C",
              "1999MNRAS.309..610D",
              "1996ApJ...458..435C",
              "1997ilsn.proc...25S",
              "2000ApJ...540..113B",
              "1990ApJ...356..359H",
              "1998PASP..110...79F",
              "2000AJ....119.2498J",
              "2000AJ....120.3340N",
              "1982AJ.....87..945K",
              "1997ApJ...490..493N"
            ],
            "source": 25,
            "target": 62,
            "weight": 20
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 67,
            "weight": 3
          },
          {
            "overlap": [
              "2003MNRAS.346.1055K",
              "2005PASP..117..227K",
              "2001ApJ...556..121K",
              "1998PASP..110..934K",
              "2000ApJ...530..660B",
              "2001PASP..113.1449C"
            ],
            "source": 25,
            "target": 69,
            "weight": 8
          },
          {
            "overlap": [
              "2000ApJS..126..331J",
              "2004AJ....127.2002K",
              "2002AJ....123..485S",
              "2005PASP..117..227K",
              "2001PASP..113.1449C"
            ],
            "source": 25,
            "target": 72,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F",
              "1997A&AS..122..399P",
              "1998ApJ...500..525S"
            ],
            "source": 25,
            "target": 74,
            "weight": 5
          },
          {
            "overlap": [
              "1984ApJ...281...95P",
              "1980ApJ...236..351D"
            ],
            "source": 25,
            "target": 76,
            "weight": 5
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 80,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 25,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 25,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1999MNRAS.309..610D",
              "1998PASJ...50..187F",
              "1996MNRAS.281..799E",
              "1990ApJ...356..359H",
              "1989ApJS...70....1A",
              "1998PASP..110...79F",
              "1996ApJ...470..724M",
              "2001ApJ...561L..41R",
              "2001MNRAS.328.1039C",
              "1982AJ.....87..945K",
              "2001ApJ...555..558R",
              "2002AJ....124.1266R",
              "1997ApJ...490..493N",
              "2001ApJ...547..609E",
              "2001ApJ...559..606C",
              "1998PASP..110..934K",
              "1997ApJ...476L...7C",
              "1989AJ.....98..755R",
              "2000astro.ph.11458K",
              "2002AJ....123..485S"
            ],
            "source": 25,
            "target": 92,
            "weight": 24
          },
          {
            "overlap": [
              "2007ASPC..377..106H"
            ],
            "source": 26,
            "target": 71,
            "weight": 29
          },
          {
            "overlap": [
              "1988ApJ...327..544D"
            ],
            "source": 27,
            "target": 29,
            "weight": 3
          },
          {
            "overlap": [
              "1988ApJ...327..544D",
              "1986ApJ...302L...1D"
            ],
            "source": 27,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 27,
            "target": 47,
            "weight": 3
          },
          {
            "overlap": [
              "1989AJ.....98..755R",
              "1986ApJ...302L...1D"
            ],
            "source": 27,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1987ApJ...313..629B",
              "1986ApJ...306L..55H",
              "1988AJ.....96.1791F",
              "1988ApJ...327..544D",
              "1986ApJ...302L...1D"
            ],
            "source": 27,
            "target": 37,
            "weight": 11
          },
          {
            "overlap": [
              "1990ApJS...72..433H"
            ],
            "source": 27,
            "target": 56,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1988AJ.....96.1791F"
            ],
            "source": 27,
            "target": 68,
            "weight": 8
          },
          {
            "overlap": [
              "1990AJ.....99.1004B",
              "1983AJ.....88..439L",
              "1988ApJ...327..544D"
            ],
            "source": 27,
            "target": 31,
            "weight": 11
          },
          {
            "overlap": [
              "1990ApJS...72..433H",
              "1988AJ.....96.1791F"
            ],
            "source": 27,
            "target": 73,
            "weight": 9
          },
          {
            "overlap": [
              "1990AJ.....99.1004B"
            ],
            "source": 27,
            "target": 74,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1990ApJS...72..433H",
              "1990AJ.....99.1004B"
            ],
            "source": 27,
            "target": 76,
            "weight": 10
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1990ApJS...72..433H"
            ],
            "source": 27,
            "target": 49,
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
            "source": 27,
            "target": 50,
            "weight": 6
          },
          {
            "overlap": [
              "1985ApJ...289...81R"
            ],
            "source": 27,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 35,
            "weight": 3
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 28,
            "target": 37,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 38,
            "weight": 9
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 40,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 43,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2002SPIE.4847..238K",
              "2005JASIS..56...36K"
            ],
            "source": 28,
            "target": 48,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 51,
            "weight": 9
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 28,
            "target": 52,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 54,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 65,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 66,
            "weight": 11
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2002SPIE.4847..238K"
            ],
            "source": 28,
            "target": 71,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 28,
            "target": 75,
            "weight": 19
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2005JASIS..56...36K",
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 81,
            "weight": 23
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 28,
            "target": 82,
            "weight": 13
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "2000A&AS..143...41K"
            ],
            "source": 28,
            "target": 85,
            "weight": 8
          },
          {
            "overlap": [
              "1982PhDT.........2K"
            ],
            "source": 28,
            "target": 89,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2002SPIE.4847..238K"
            ],
            "source": 28,
            "target": 91,
            "weight": 23
          },
          {
            "overlap": [
              "1992ASPC...25..432K",
              "1994ApJ...424L...1D"
            ],
            "source": 29,
            "target": 89,
            "weight": 6
          },
          {
            "overlap": [
              "1993MNRAS.264...71V"
            ],
            "source": 29,
            "target": 62,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1990MNRAS.243..692M"
            ],
            "source": 29,
            "target": 31,
            "weight": 14
          },
          {
            "overlap": [
              "1980A&A....82..322D"
            ],
            "source": 29,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1988ApJ...327..544D",
              "1994ApJ...424L...1D"
            ],
            "source": 29,
            "target": 90,
            "weight": 12
          },
          {
            "overlap": [
              "1994ApJ...424L...1D",
              "1989Sci...246..897G",
              "1981AJ.....86..476J",
              "1988ApJ...327..544D",
              "1990Natur.343..726B"
            ],
            "source": 29,
            "target": 37,
            "weight": 13
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 29,
            "target": 42,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 29,
            "target": 73,
            "weight": 6
          },
          {
            "overlap": [
              "1992ApJ...390..338L"
            ],
            "source": 29,
            "target": 74,
            "weight": 2
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 29,
            "target": 76,
            "weight": 4
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1994ApJ...424L...1D",
              "1992ApJ...390..338L",
              "1990Natur.343..726B"
            ],
            "source": 29,
            "target": 49,
            "weight": 12
          },
          {
            "overlap": [
              "1988ApJ...327..544D",
              "1989Sci...246..897G",
              "1994ApJ...424L...1D"
            ],
            "source": 29,
            "target": 50,
            "weight": 3
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 29,
            "target": 53,
            "weight": 3
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1992ASPC...25..432K",
              "1994ApJ...424L...1D",
              "1992ApJ...390..338L"
            ],
            "source": 29,
            "target": 56,
            "weight": 13
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1980A&A....82..322D"
            ],
            "source": 29,
            "target": 57,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 33,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 34,
            "weight": 2
          },
          {
            "overlap": [
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "1998ApJ...500..525S",
              "2002ApJ...574L..39G",
              "2000ApJ...540..825Y",
              "1998ApJ...508..844G",
              "1998PASP..110...79F",
              "1998MNRAS.298..387D",
              "1999AJ....117.2308W",
              "1984jbp..bookQ....B",
              "2002MNRAS.337...87C",
              "1999AJ....117.2329W",
              "1992MNRAS.257..225A",
              "2000asqu.book....1C",
              "1999AJ....117..981B",
              "2003AJ....125.1309C",
              "2000AJ....120.1014P",
              "1996ApJ...459L..73M",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "2003MNRAS.339.1111A",
              "1989MNRAS.238..225S"
            ],
            "source": 30,
            "target": 35,
            "weight": 33
          },
          {
            "overlap": [
              "1997AJ....114.2205G",
              "1988ApJ...328..315M",
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 36,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 39,
            "weight": 7
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 30,
            "target": 44,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K",
              "1997AJ....114.2205G"
            ],
            "source": 30,
            "target": 50,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 53,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2001AJ....122..714B",
              "1997AJ....114.2205G",
              "1998PASP..110...79F"
            ],
            "source": 30,
            "target": 56,
            "weight": 7
          },
          {
            "overlap": [
              "1997A&A...320..823C"
            ],
            "source": 30,
            "target": 58,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2000ApJ...540..825Y",
              "1998ApJ...508..844G",
              "1998MNRAS.298..387D",
              "1990A&A...236..357C",
              "1999AJ....117.2308W",
              "2002MNRAS.337...87C",
              "2003AJ....125.1309C",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P"
            ],
            "source": 30,
            "target": 59,
            "weight": 18
          },
          {
            "overlap": [
              "2002ApJ...569..245N",
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "1998ApJ...500..525S",
              "2002ApJ...574L..39G",
              "2003ApJ...588..824Y",
              "1998ApJ...508..844G",
              "1998PASP..110...79F",
              "1996ApJ...465..278J",
              "1994Natur.370..194I",
              "1999AJ....117.2308W",
              "2002MNRAS.337...87C",
              "1999AJ....117.2329W",
              "1992MNRAS.257..225A",
              "1999AJ....117..981B",
              "2003AJ....125.1309C",
              "2000AJ....120.1014P",
              "1996ApJ...459L..73M",
              "2002AJ....124..931B",
              "1994AJ....108.1722K",
              "1991ApJ...375..121P",
              "2003MNRAS.339.1111A",
              "1989MNRAS.238..225S"
            ],
            "source": 30,
            "target": 61,
            "weight": 26
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 30,
            "target": 62,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 63,
            "weight": 3
          },
          {
            "overlap": [
              "1994ApJS...94..687W",
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "1998PASP..110..934K",
              "1998PASP..110...79F",
              "1999AJ....117.2329W",
              "1973AJ.....78.1074O"
            ],
            "source": 30,
            "target": 67,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..577M"
            ],
            "source": 30,
            "target": 69,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "2001AJ....122..714B",
              "1988ApJ...328..315M"
            ],
            "source": 30,
            "target": 72,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..577M",
              "1998PASP..110...79F",
              "1998ApJ...500..525S",
              "1997AJ....114.2205G",
              "2000asqu.book....1C"
            ],
            "source": 30,
            "target": 74,
            "weight": 8
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 80,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1997AJ....114.2205G",
              "1998PASP..110...79F"
            ],
            "source": 30,
            "target": 89,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 30,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1990AJ.....99.2019L",
              "1985ComAp..11...53F",
              "1989spce.book.....L",
              "1989Sci...246..897G",
              "1988ApJ...327..544D"
            ],
            "source": 31,
            "target": 90,
            "weight": 20
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z",
              "1989Sci...246..897G",
              "1988ApJ...327..544D"
            ],
            "source": 31,
            "target": 37,
            "weight": 14
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 31,
            "target": 73,
            "weight": 7
          },
          {
            "overlap": [
              "1990AJ.....99.1004B",
              "1990AJ.....99.2019L"
            ],
            "source": 31,
            "target": 74,
            "weight": 6
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1990AJ.....99.1004B",
              "1989Sci...246..897G"
            ],
            "source": 31,
            "target": 76,
            "weight": 15
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 31,
            "target": 49,
            "weight": 4
          },
          {
            "overlap": [
              "1988ApJ...327..544D",
              "1983ApJS...52...89H",
              "1989Sci...246..897G",
              "1990AJ.....99.2019L",
              "1961cgcg.book.....Z",
              "1976RC2...C......0D",
              "1990AJ.....99.1004B"
            ],
            "source": 31,
            "target": 50,
            "weight": 10
          },
          {
            "overlap": [
              "1961cgcg.book.....Z"
            ],
            "source": 31,
            "target": 55,
            "weight": 7
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 31,
            "target": 56,
            "weight": 4
          },
          {
            "overlap": [
              "1976RC2...C......0D"
            ],
            "source": 31,
            "target": 57,
            "weight": 5
          },
          {
            "overlap": [
              "1996adass...5..569E"
            ],
            "source": 32,
            "target": 40,
            "weight": 16
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 34,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 36,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 39,
            "weight": 29
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 33,
            "target": 42,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 50,
            "weight": 2
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K",
              "1998SPIE.3355..324R"
            ],
            "source": 33,
            "target": 53,
            "weight": 18
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 56,
            "weight": 7
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 59,
            "weight": 14
          },
          {
            "overlap": [
              "2004ASPC..314..141M"
            ],
            "source": 33,
            "target": 61,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 63,
            "weight": 20
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 67,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 69,
            "weight": 15
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 74,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 80,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 86,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 33,
            "target": 92,
            "weight": 5
          },
          {
            "overlap": [
              "1981PASP...93....5B",
              "2000ApJ...530..660B",
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 36,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 39,
            "weight": 7
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 34,
            "target": 41,
            "weight": 1
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2005ApJ...635L.125G",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2002SPIE.4836...73W"
            ],
            "source": 34,
            "target": 42,
            "weight": 6
          },
          {
            "overlap": [
              "2004ApJ...613..898T",
              "2003ApJS..148..175S",
              "1998ARA&A..36..189K"
            ],
            "source": 34,
            "target": 44,
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
            "source": 34,
            "target": 46,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 50,
            "weight": 1
          },
          {
            "overlap": [
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998SPIE.3355..577M",
              "2005PASP..117.1411F",
              "1998PASP..110..934K",
              "2003AJ....126.2152R"
            ],
            "source": 34,
            "target": 53,
            "weight": 11
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 56,
            "weight": 3
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 34,
            "target": 58,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 59,
            "weight": 2
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 34,
            "target": 61,
            "weight": 1
          },
          {
            "overlap": [
              "1999PASP..111..438F"
            ],
            "source": 34,
            "target": 62,
            "weight": 2
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2008PASP..120.1222F",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2010ApJ...709..832G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005MNRAS.360...81D",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 34,
            "target": 63,
            "weight": 15
          },
          {
            "overlap": [
              "2006ApJS..162...38A",
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 67,
            "weight": 2
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
              "1998SPIE.3355..577M",
              "2005ApJ...625...23B",
              "2008ApJS..175..128S",
              "2008MNRAS.385.1903L",
              "2005PASP..117.1411F",
              "2008PASP..120.1222F",
              "2008AJ....135.1877E",
              "2005PASP..117..227K",
              "1998ARA&A..36..189K",
              "1998PASP..110..934K",
              "2000ApJ...533..682C"
            ],
            "source": 34,
            "target": 69,
            "weight": 31
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "2001PASP..113.1449C",
              "1998SPIE.3355..577M",
              "2008ApJS..175..128S",
              "2005PASP..117.1411F",
              "2005PASP..117..227K"
            ],
            "source": 34,
            "target": 72,
            "weight": 15
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998PASP..110..934K",
              "1998SPIE.3355..577M"
            ],
            "source": 34,
            "target": 74,
            "weight": 4
          },
          {
            "overlap": [
              "2007AJ....134..527W",
              "1999ApJS..123....3L",
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 80,
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
            "source": 34,
            "target": 84,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 34,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 38,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 40,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 43,
            "weight": 3
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 35,
            "target": 44,
            "weight": 1
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 48,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 35,
            "target": 50,
            "weight": 1
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 51,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 54,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "2000AJ....120.1579Y"
            ],
            "source": 35,
            "target": 56,
            "weight": 5
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
            "source": 35,
            "target": 59,
            "weight": 28
          },
          {
            "overlap": [
              "1980MNRAS.193..295F",
              "1994AJ....108..538P",
              "2002ApJ...578..151S",
              "1998ApJ...508..844G",
              "1998PASP..110...79F",
              "2003ASPC..298..137S",
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
              "2002MNRAS.337...87C",
              "2003AJ....125.1309C"
            ],
            "source": 35,
            "target": 61,
            "weight": 50
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 35,
            "target": 62,
            "weight": 2
          },
          {
            "overlap": [
              "1988MNRAS.232..431E"
            ],
            "source": 35,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...23O"
            ],
            "source": 35,
            "target": 65,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 66,
            "weight": 6
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
            "source": 35,
            "target": 67,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 71,
            "weight": 5
          },
          {
            "overlap": [
              "2002AJ....123..485S"
            ],
            "source": 35,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "1988MNRAS.232..431E",
              "1998PASP..110...79F",
              "1998ApJ...500..525S",
              "2000asqu.book....1C"
            ],
            "source": 35,
            "target": 74,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 75,
            "weight": 5
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 35,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 81,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 82,
            "weight": 4
          },
          {
            "overlap": [
              "2003AJ....126.2081A"
            ],
            "source": 35,
            "target": 84,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 85,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 35,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 35,
            "target": 91,
            "weight": 6
          },
          {
            "overlap": [
              "2002AJ....123..485S",
              "1998PASP..110...79F"
            ],
            "source": 35,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 36,
            "target": 39,
            "weight": 9
          },
          {
            "overlap": [
              "1982AJ.....87.1165B",
              "1996ApJ...464...60L",
              "1994AJ....108.1476F",
              "1993AJ....105..393K",
              "1997A&A...326..477Z",
              "1995MmSAI..66..113R"
            ],
            "source": 36,
            "target": 49,
            "weight": 14
          },
          {
            "overlap": [
              "1994AJ....108.1476F",
              "1997AJ....114.2205G",
              "1998PASP..110..934K",
              "1989ApJS...70..271B"
            ],
            "source": 36,
            "target": 50,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 36,
            "target": 53,
            "weight": 2
          },
          {
            "overlap": [
              "1995MmSAI..66..113R",
              "1997AJ....114.2205G",
              "1998PASP..110..934K",
              "1993AJ....105..393K"
            ],
            "source": 36,
            "target": 56,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 36,
            "target": 59,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 36,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 36,
            "target": 67,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2000ApJ...530..660B",
              "1981PASP...93....5B"
            ],
            "source": 36,
            "target": 69,
            "weight": 5
          },
          {
            "overlap": [
              "1988ApJ...328..315M",
              "1992ApJ...388..310K"
            ],
            "source": 36,
            "target": 72,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1996ApJ...464...60L",
              "1994AJ....108.1476F",
              "1997A&AS..122..399P",
              "1997AJ....114.2205G",
              "1993AJ....105..393K",
              "1997A&A...326..477Z",
              "1995MmSAI..66..113R"
            ],
            "source": 36,
            "target": 74,
            "weight": 14
          },
          {
            "overlap": [
              "1984ApJ...281...95P",
              "1980ApJ...236..351D"
            ],
            "source": 36,
            "target": 76,
            "weight": 6
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 36,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 36,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1997AJ....114.2205G"
            ],
            "source": 36,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 36,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 37,
            "target": 40,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 37,
            "target": 47,
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
            "source": 37,
            "target": 49,
            "weight": 11
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1994ApJ...437L...1D",
              "1983ApJS...52...89H",
              "1961cgcg.book.....Z",
              "1988AJ.....96.1791F",
              "1994ApJ...424L...1D",
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1992ApJS...83...29S"
            ],
            "source": 37,
            "target": 50,
            "weight": 7
          },
          {
            "overlap": [
              "1993adass...2..132K"
            ],
            "source": 37,
            "target": 52,
            "weight": 3
          },
          {
            "overlap": [
              "1961cgcg.book.....Z"
            ],
            "source": 37,
            "target": 55,
            "weight": 4
          },
          {
            "overlap": [
              "1994ApJ...424L...1D",
              "1989Sci...246..897G"
            ],
            "source": 37,
            "target": 56,
            "weight": 5
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1976PASP...88..960S",
              "1988AJ.....96.1791F"
            ],
            "source": 37,
            "target": 68,
            "weight": 11
          },
          {
            "overlap": [
              "1989Sci...246..897G",
              "1988AJ.....96.1791F"
            ],
            "source": 37,
            "target": 73,
            "weight": 8
          },
          {
            "overlap": [
              "1994ApJ...428...43M"
            ],
            "source": 37,
            "target": 74,
            "weight": 2
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1989Sci...246..897G",
              "1986ApJ...302L...1D",
              "1981ApJ...248L..57K"
            ],
            "source": 37,
            "target": 76,
            "weight": 12
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1995adass...4...36A"
            ],
            "source": 37,
            "target": 85,
            "weight": 5
          },
          {
            "overlap": [
              "1994ApJ...424L...1D"
            ],
            "source": 37,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1994ApJ...437L...1D",
              "1994ApJ...424L...1D",
              "1989Sci...246..897G",
              "1988ApJ...327..544D",
              "1986ApJ...302L...1D"
            ],
            "source": 37,
            "target": 90,
            "weight": 14
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 37,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 48,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 91,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 65,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 66,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 54,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 40,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 71,
            "weight": 15
          },
          {
            "overlap": [
              "1999ASPC..172..291A",
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 43,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 75,
            "weight": 15
          },
          {
            "overlap": [
              "2006cs........4061H",
              "2005IPM....41.1395K",
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 51,
            "weight": 48
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 81,
            "weight": 13
          },
          {
            "overlap": [
              "1999ASPC..172..291A",
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 82,
            "weight": 22
          },
          {
            "overlap": [
              "1999ASPC..172..291A",
              "2000A&AS..143...41K"
            ],
            "source": 38,
            "target": 85,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 69,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 80,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 74,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 63,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 50,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 67,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 53,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 92,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 56,
            "weight": 11
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 86,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 39,
            "target": 59,
            "weight": 11
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 43,
            "weight": 8
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 40,
            "target": 45,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 48,
            "weight": 14
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 51,
            "weight": 8
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1995ASSL..203..259S",
              "1991ASSL..171...79E",
              "1993ASPC...52..132K"
            ],
            "source": 40,
            "target": 52,
            "weight": 20
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 54,
            "weight": 21
          },
          {
            "overlap": [
              "1994ExA.....5..205E"
            ],
            "source": 40,
            "target": 64,
            "weight": 16
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "1995VA.....39..217E",
              "1995ASPC...77...28E",
              "1996ASPC..101..569E",
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "1992ASPC...25...47M",
              "1992ald2.proc..387M"
            ],
            "source": 40,
            "target": 65,
            "weight": 47
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 66,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 71,
            "weight": 8
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 75,
            "weight": 17
          },
          {
            "overlap": [
              "1992ASPC...25...47M",
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...85A"
            ],
            "source": 40,
            "target": 79,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 81,
            "weight": 21
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 82,
            "weight": 12
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993adass...2..132K",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 85,
            "weight": 14
          },
          {
            "overlap": [
              "1995ASPC...77...28E",
              "1993ASPC...52..132K"
            ],
            "source": 40,
            "target": 88,
            "weight": 25
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 40,
            "target": 91,
            "weight": 32
          },
          {
            "overlap": [
              "2005ApJ...629..268H",
              "2006ApJS..162...38A"
            ],
            "source": 41,
            "target": 61,
            "weight": 3
          },
          {
            "overlap": [
              "2005ApJ...622L..33B",
              "2005ApJ...634L.181E",
              "2006ApJ...647..303B",
              "2005ApJ...628..246E",
              "1961BAN....15..265B",
              "2006MNRAS.368..221G",
              "2003ApJ...599.1129Y",
              "1988Natur.331..687H",
              "2006ApJS..162...38A",
              "2005astro.ph.12344H",
              "2000ApJ...544..437P",
              "2005ApJ...620..744G",
              "2005A&A...444L..61H",
              "1992A&AS...96..269S",
              "2005ApJ...634..344G",
              "1991AJ....101..562L",
              "2005MNRAS.363..223G",
              "2006MNRAS.372..174B",
              "2006ApJ...640L..35B",
              "2006ApJ...651..392S"
            ],
            "source": 41,
            "target": 67,
            "weight": 34
          },
          {
            "overlap": [
              "2006ApJ...647..303B"
            ],
            "source": 41,
            "target": 44,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 41,
            "target": 46,
            "weight": 2
          },
          {
            "overlap": [
              "1991A&A...248..485D"
            ],
            "source": 41,
            "target": 77,
            "weight": 3
          },
          {
            "overlap": [
              "2006ApJ...647..303B",
              "1992A&AS...96..269S"
            ],
            "source": 41,
            "target": 80,
            "weight": 4
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
            "source": 41,
            "target": 59,
            "weight": 16
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2010ApJ...709..832G",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 42,
            "target": 63,
            "weight": 9
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W",
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W"
            ],
            "source": 42,
            "target": 69,
            "weight": 7
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2002SPIE.4836...73W"
            ],
            "source": 42,
            "target": 72,
            "weight": 7
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 42,
            "target": 46,
            "weight": 6
          },
          {
            "overlap": [
              "2005ApJ...624...59H",
              "2002SPIE.4836...73W",
              "1958ApJS....3..211A",
              "2004MNRAS.350..893H",
              "2005ApJ...635L.125G",
              "2008ApJ...673..163S",
              "2009PASJ...61..833H",
              "2007ApJ...669..714M",
              "2007A&A...462..459G",
              "2009ApJ...702..980K",
              "2008ApJ...684..794K",
              "2004A&A...425..367B",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2001ApJ...557L..89W",
              "2007ApJ...660..239K",
              "1998SPIE.3355..285F",
              "2007A&A...462..875S",
              "2007A&A...470..821D"
            ],
            "source": 42,
            "target": 53,
            "weight": 33
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2012AJ....143..102G",
              "2010ApJ...709..832G",
              "2006ApJ...643..128W",
              "2002SPIE.4836...73W"
            ],
            "source": 42,
            "target": 84,
            "weight": 10
          },
          {
            "overlap": [
              "1958ApJS....3..211A",
              "1990ApJS...72..567G"
            ],
            "source": 42,
            "target": 57,
            "weight": 5
          },
          {
            "overlap": [
              "1998SPIE.3355..285F"
            ],
            "source": 42,
            "target": 59,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 43,
            "target": 48,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 51,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 54,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 65,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 43,
            "target": 66,
            "weight": 28
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 71,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 75,
            "weight": 7
          },
          {
            "overlap": [
              "1997PASP..109.1278S",
              "2000A&AS..143...85A",
              "1999adass...8..287L",
              "1999ASPC..172..287L",
              "1993adass...2..137W",
              "1995VA.....39R.272S",
              "1988alds.proc..335H",
              "2000A&AS..143...61E",
              "1993ASPC...52..137W"
            ],
            "source": 43,
            "target": 79,
            "weight": 24
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 81,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "1998ASPC..153..107B",
              "1999ASPC..172..291A"
            ],
            "source": 43,
            "target": 82,
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
            "source": 43,
            "target": 85,
            "weight": 26
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 43,
            "target": 91,
            "weight": 18
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 44,
            "target": 61,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJ...647..303B"
            ],
            "source": 44,
            "target": 67,
            "weight": 1
          },
          {
            "overlap": [
              "1989ApJ...345..245C",
              "2004ApJ...613..898T",
              "1998ARA&A..36..189K"
            ],
            "source": 44,
            "target": 69,
            "weight": 4
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 44,
            "target": 74,
            "weight": 1
          },
          {
            "overlap": [
              "2006ApJ...647..970L",
              "1989ApJ...347..875S",
              "1998ApJ...500..525S",
              "2006ApJ...647..303B"
            ],
            "source": 44,
            "target": 80,
            "weight": 6
          },
          {
            "overlap": [
              "1989agna.book.....O"
            ],
            "source": 44,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1996ASPC..101..569E"
            ],
            "source": 45,
            "target": 65,
            "weight": 26
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 46,
            "target": 61,
            "weight": 2
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 46,
            "target": 63,
            "weight": 8
          },
          {
            "overlap": [
              "2006ApJS..162...38A"
            ],
            "source": 46,
            "target": 67,
            "weight": 2
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2005ApJ...635L.125G",
              "2006ApJ...643..128W"
            ],
            "source": 46,
            "target": 69,
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
            "source": 46,
            "target": 72,
            "weight": 13
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2005MNRAS.359..237P",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 46,
            "target": 53,
            "weight": 10
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1987ApJ...313...59D",
              "2006ApJ...643..128W"
            ],
            "source": 46,
            "target": 84,
            "weight": 9
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1986ApJ...302L...1D"
            ],
            "source": 47,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "1986AJ.....92.1248T",
              "1982AJ.....87..945K",
              "1986ApJ...302L...1D"
            ],
            "source": 47,
            "target": 92,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1985AJ.....90.1665K"
            ],
            "source": 47,
            "target": 56,
            "weight": 5
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1986AJ.....92.1238P",
              "1979AJ.....84.1511T"
            ],
            "source": 47,
            "target": 68,
            "weight": 13
          },
          {
            "overlap": [
              "1982AJ.....87..945K"
            ],
            "source": 47,
            "target": 62,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 47,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1986ApJ...302L...1D"
            ],
            "source": 47,
            "target": 76,
            "weight": 7
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 47,
            "target": 78,
            "weight": 11
          },
          {
            "overlap": [
              "1987ApJ...314..493K",
              "1979AJ.....84.1511T",
              "1986ApJ...308..530F",
              "1977AJ.....82..187F"
            ],
            "source": 47,
            "target": 50,
            "weight": 4
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1982ApJ...254..437D",
              "1985AJ.....90.1665K"
            ],
            "source": 47,
            "target": 49,
            "weight": 8
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 47,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "2005JASIS..56..111K",
              "2000A&AS..143...41K"
            ],
            "source": 48,
            "target": 51,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 48,
            "target": 52,
            "weight": 4
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 48,
            "target": 54,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 48,
            "target": 65,
            "weight": 17
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 48,
            "target": 66,
            "weight": 32
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2002SPIE.4847..238K"
            ],
            "source": 48,
            "target": 71,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K",
              "1992ald2.proc...85K",
              "2005JASIS..56...36K"
            ],
            "source": 48,
            "target": 75,
            "weight": 26
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 48,
            "target": 79,
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
            "source": 48,
            "target": 81,
            "weight": 32
          },
          {
            "overlap": [
              "2005JASIS..56..111K",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 48,
            "target": 82,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 48,
            "target": 85,
            "weight": 11
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 48,
            "target": 88,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 48,
            "target": 91,
            "weight": 32
          },
          {
            "overlap": [
              "1997A&A...325..954V",
              "1995ApJS...99..391H",
              "1994ApJ...424L...1D",
              "1996ApJ...470..172S"
            ],
            "source": 49,
            "target": 89,
            "weight": 10
          },
          {
            "overlap": [
              "1996ApJ...458..435C"
            ],
            "source": 49,
            "target": 62,
            "weight": 3
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 49,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1996ApJ...470..724M",
              "1997ApJ...476L...7C"
            ],
            "source": 49,
            "target": 92,
            "weight": 5
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
            "source": 49,
            "target": 56,
            "weight": 40
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 49,
            "target": 68,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 49,
            "target": 69,
            "weight": 2
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
            "source": 49,
            "target": 90,
            "weight": 16
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 49,
            "target": 72,
            "weight": 2
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1989Sci...246..897G",
              "1976ApJ...203..297S",
              "1990ApJS...72..433H"
            ],
            "source": 49,
            "target": 73,
            "weight": 19
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1976ApJ...203..297S",
              "1996ApJ...464...60L",
              "1994AJ....108.1476F",
              "1996ApJ...470..172S",
              "1992ApJ...390..338L",
              "1993AJ....105..393K",
              "1997A&A...326..477Z",
              "1996MNRAS.280..235E",
              "1995MmSAI..66..113R"
            ],
            "source": 49,
            "target": 74,
            "weight": 21
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1976ApJ...203..297S",
              "1986ApJ...302L...1D",
              "1990ApJS...72..433H",
              "1989Sci...246..897G"
            ],
            "source": 49,
            "target": 76,
            "weight": 17
          },
          {
            "overlap": [
              "1995ApJS...99..391H",
              "1994ApJ...428...43M",
              "1989Sci...246..897G",
              "1997ApJS..111....1S",
              "1994ApJ...424L...1D",
              "1994AJ....108.1476F",
              "1990ApJS...72..433H",
              "1996ApJS..104..199W"
            ],
            "source": 49,
            "target": 50,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1968cgcg.book.....Z"
            ],
            "source": 50,
            "target": 53,
            "weight": 2
          },
          {
            "overlap": [
              "1961cgcg.book.....Z"
            ],
            "source": 50,
            "target": 55,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1989Sci...246..897G",
              "1990AJ....100.1405W",
              "1997AJ....114.2205G",
              "1982ApJ...253..423D",
              "1998PASP..110...79F",
              "1995ApJS...99..391H",
              "1994ApJ...424L...1D",
              "1996ApJS..104..199W",
              "1998PASP..110..934K",
              "1990ApJS...72..433H",
              "1982ialo.coll..259L"
            ],
            "source": 50,
            "target": 56,
            "weight": 11
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1976RC2...C......0D",
              "1993AJ....106.1273Z"
            ],
            "source": 50,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 50,
            "target": 59,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 50,
            "target": 61,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 50,
            "target": 62,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1996ApJS..105..209I"
            ],
            "source": 50,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 50,
            "target": 67,
            "weight": 1
          },
          {
            "overlap": [
              "1973ugcg.book.....N",
              "1987ApJ...318..161S",
              "1979AJ.....84.1511T",
              "1975ApJ...199....1G",
              "1988AJ.....96.1791F"
            ],
            "source": 50,
            "target": 68,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 50,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "1987ApJ...318..161S",
              "1989Sci...246..897G",
              "1986AJ.....91..705B",
              "1990ApJS...72..433H",
              "1988AJ.....96.1791F"
            ],
            "source": 50,
            "target": 73,
            "weight": 8
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1990AJ.....99.1004B",
              "1998PASP..110..934K",
              "1997AJ....113..185M",
              "1990AJ.....99.2019L",
              "1994AJ....108.1476F",
              "1998PASP..110...79F",
              "1997AJ....114.2205G"
            ],
            "source": 50,
            "target": 74,
            "weight": 6
          },
          {
            "overlap": [
              "1983ApJS...52...89H",
              "1990ApJS...72..433H",
              "1990AJ.....99.1004B",
              "1979AJ.....84.1511T",
              "1990AJ.....99..463D",
              "1989Sci...246..897G",
              "1990AJ....100.1405W",
              "1988AJ.....95.1602S",
              "1992ApJS...79..157F"
            ],
            "source": 50,
            "target": 76,
            "weight": 11
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 50,
            "target": 78,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 50,
            "target": 80,
            "weight": 1
          },
          {
            "overlap": [
              "1995MNRAS.276.1341J"
            ],
            "source": 50,
            "target": 84,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1979AJ.....84.1511T"
            ],
            "source": 50,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1995ApJS...99..391H",
              "1994ApJ...424L...1D",
              "1990A&AS...82..391B",
              "1997AJ....114.2205G"
            ],
            "source": 50,
            "target": 89,
            "weight": 4
          },
          {
            "overlap": [
              "1994ApJ...437L...1D",
              "1988ApJ...327..544D",
              "1993AJ....105.1637H",
              "1994AJ....108.1987A",
              "1994ApJ...428...43M",
              "1989Sci...246..897G",
              "1994ApJ...424L...1D",
              "1990AJ.....99.2019L",
              "1993AJ....106..676A",
              "1976AJ.....81..952H",
              "1994ApJ...431..569P",
              "1995AJ....110..477M",
              "1984AJ.....89.1310D",
              "1997AJ....113..185M",
              "1982ApJ...253..423D",
              "1979AJ.....84.1511T"
            ],
            "source": 50,
            "target": 90,
            "weight": 15
          },
          {
            "overlap": [
              "1988AJ.....96.1775O",
              "1988AJ.....95..999C",
              "1993AJ....105..788F",
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 50,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 51,
            "target": 54,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 51,
            "target": 65,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 51,
            "target": 66,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 51,
            "target": 71,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 51,
            "target": 75,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 51,
            "target": 81,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143...41K",
              "2005JASIS..56..111K"
            ],
            "source": 51,
            "target": 82,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 51,
            "target": 85,
            "weight": 6
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 51,
            "target": 91,
            "weight": 19
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 52,
            "target": 88,
            "weight": 13
          },
          {
            "overlap": [
              "1997ASPC..125..357A",
              "1993ApJ...412..301A",
              "1975ApJ...201..249C",
              "1991ApJ...366...64C",
              "1993PASP..105..437A",
              "1981PASP...93..269A"
            ],
            "source": 52,
            "target": 70,
            "weight": 56
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 52,
            "target": 75,
            "weight": 9
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 52,
            "target": 79,
            "weight": 3
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "1995VA.....39..227O"
            ],
            "source": 52,
            "target": 81,
            "weight": 15
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 52,
            "target": 82,
            "weight": 6
          },
          {
            "overlap": [
              "1993adass...2..132K",
              "1993ASPC...52..132K"
            ],
            "source": 52,
            "target": 85,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 56,
            "weight": 2
          },
          {
            "overlap": [
              "1958ApJS....3..211A"
            ],
            "source": 53,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998SPIE.3355..285F"
            ],
            "source": 53,
            "target": 59,
            "weight": 5
          },
          {
            "overlap": [
              "1992nrfa.book.....P"
            ],
            "source": 53,
            "target": 61,
            "weight": 2
          },
          {
            "overlap": [
              "2007ApJS..172..117M",
              "2007AJ....134.1360K",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 53,
            "target": 63,
            "weight": 22
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992nrfa.book.....P"
            ],
            "source": 53,
            "target": 67,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "1998SPIE.3355..577M",
              "1998SPIE.3355..324R",
              "2002SPIE.4836...73W",
              "1998SPIE.3355..285F",
              "2005ApJ...635L.125G",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2006ApJ...643..128W"
            ],
            "source": 53,
            "target": 69,
            "weight": 15
          },
          {
            "overlap": [
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "1998SPIE.3355..577M",
              "2007ASPC..376..249M",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 53,
            "target": 72,
            "weight": 13
          },
          {
            "overlap": [
              "1998SPIE.3355..577M",
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 74,
            "weight": 4
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
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W"
            ],
            "source": 53,
            "target": 84,
            "weight": 7
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992nrfa.book.....P"
            ],
            "source": 53,
            "target": 86,
            "weight": 5
          },
          {
            "overlap": [
              "1996MNRAS.281..799E",
              "1989ApJS...70....1A",
              "1998PASP..110..934K"
            ],
            "source": 53,
            "target": 92,
            "weight": 5
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 91,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 65,
            "weight": 25
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 66,
            "weight": 46
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 71,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 75,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 81,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 82,
            "weight": 27
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 54,
            "target": 85,
            "weight": 16
          },
          {
            "overlap": [
              "1961CGCG..C......0Z"
            ],
            "source": 55,
            "target": 73,
            "weight": 9
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 56,
            "target": 57,
            "weight": 3
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 56,
            "target": 58,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 56,
            "target": 59,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 56,
            "target": 61,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "2000AJ....119.2498J"
            ],
            "source": 56,
            "target": 62,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 56,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 56,
            "target": 67,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 56,
            "target": 68,
            "weight": 4
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998PASP..110..934K"
            ],
            "source": 56,
            "target": 69,
            "weight": 4
          },
          {
            "overlap": [
              "2001AJ....122..714B"
            ],
            "source": 56,
            "target": 72,
            "weight": 3
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1989Sci...246..897G",
              "1990ApJS...72..433H"
            ],
            "source": 56,
            "target": 73,
            "weight": 15
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1995MmSAI..66..113R",
              "1997AJ....114.2205G",
              "1998PASP..110...79F",
              "2001AJ....121.2358B",
              "1996ApJ...470..172S",
              "1998PASP..110..934K",
              "2001MNRAS.324..825C",
              "1992ApJ...390..338L",
              "1993AJ....105..393K",
              "1995PASP..107..945F"
            ],
            "source": 56,
            "target": 74,
            "weight": 23
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1990ApJS...72..433H",
              "1979AJ.....84.1511T",
              "1989Sci...246..897G",
              "1990AJ....100.1405W"
            ],
            "source": 56,
            "target": 76,
            "weight": 18
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1979AJ.....84.1511T"
            ],
            "source": 56,
            "target": 78,
            "weight": 23
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 56,
            "target": 79,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 56,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1979AJ.....84.1511T",
              "1986SPIE..627..733T",
              "1998PASP..110..934K"
            ],
            "source": 56,
            "target": 86,
            "weight": 9
          },
          {
            "overlap": [
              "1997A&A...325..954V",
              "1986SPIE..627..733T",
              "1997AJ....114.2205G",
              "1992ASPC...25..432K",
              "1998PASP..110...79F",
              "1995ApJS...99..391H",
              "1994ApJ...424L...1D",
              "1996ApJ...470..172S",
              "1992ASPC...25..439M"
            ],
            "source": 56,
            "target": 89,
            "weight": 23
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
            "source": 56,
            "target": 90,
            "weight": 16
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 56,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1980A&A....82..322D",
              "1993ApJ...412..479D"
            ],
            "source": 57,
            "target": 92,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 68,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 76,
            "weight": 4
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 78,
            "weight": 14
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 57,
            "target": 86,
            "weight": 4
          },
          {
            "overlap": [
              "1987ApJ...317..653F"
            ],
            "source": 57,
            "target": 58,
            "weight": 4
          },
          {
            "overlap": [
              "1996A&AS..117..393B"
            ],
            "source": 58,
            "target": 69,
            "weight": 2
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1992AJ....104..340L"
            ],
            "source": 58,
            "target": 74,
            "weight": 5
          },
          {
            "overlap": [
              "1997NewA....2..139K",
              "1983ApJ...266L..11A",
              "1989ApJ...341L..41K",
              "1995ApJ...442..142O",
              "1995MNRAS.277.1354I",
              "1996ASPC...92..424P",
              "1993ApJ...409L..13K",
              "1995AJ....109.1071P",
              "1996fogh.conf..424P"
            ],
            "source": 58,
            "target": 77,
            "weight": 33
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
            "source": 59,
            "target": 61,
            "weight": 19
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 59,
            "target": 63,
            "weight": 5
          },
          {
            "overlap": [
              "1961BAN....15..265B",
              "1993ASPC...45..360L",
              "2003AJ....125..984M",
              "1988Natur.331..687H",
              "2003AJ....126.1362B",
              "2003ApJ...599.1129Y",
              "1988AJ.....96..560C",
              "1998PASP..110..934K",
              "2005ApJ...620..744G",
              "1992A&AS...96..269S"
            ],
            "source": 59,
            "target": 67,
            "weight": 20
          },
          {
            "overlap": [
              "1998SPIE.3355..285F",
              "1998PASP..110..934K"
            ],
            "source": 59,
            "target": 69,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 59,
            "target": 74,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992A&AS...96..269S"
            ],
            "source": 59,
            "target": 80,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 59,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 59,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1998ASPC..145..466F"
            ],
            "source": 60,
            "target": 85,
            "weight": 17
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 61,
            "target": 62,
            "weight": 2
          },
          {
            "overlap": [
              "1988MNRAS.232..431E"
            ],
            "source": 61,
            "target": 63,
            "weight": 2
          },
          {
            "overlap": [
              "1991ApJ...380..104N",
              "1994AJ....108..538P",
              "2005MNRAS.362..349C",
              "2004MNRAS.352..285C",
              "1998PASP..110...79F",
              "2003AJ....125..984M",
              "2006ApJS..162...38A",
              "2003AJ....126.1362B",
              "2004AJ....128.2474M",
              "2005AJ....130.1097B",
              "1999AJ....117.2329W",
              "1992nrfa.book.....P"
            ],
            "source": 61,
            "target": 67,
            "weight": 15
          },
          {
            "overlap": [
              "1988MNRAS.232..431E",
              "1998PASP..110...79F",
              "1998ApJ...500..525S"
            ],
            "source": 61,
            "target": 74,
            "weight": 4
          },
          {
            "overlap": [
              "1998ApJ...500..525S"
            ],
            "source": 61,
            "target": 80,
            "weight": 2
          },
          {
            "overlap": [
              "1992nrfa.book.....P"
            ],
            "source": 61,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 61,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 61,
            "target": 92,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 62,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2000AJ....120..523R",
              "1999MNRAS.309..610D",
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
              "1997ApJ...490..493N",
              "2001AJ....122.1289T",
              "2000AJ....120.2338R"
            ],
            "source": 62,
            "target": 92,
            "weight": 26
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 62,
            "target": 67,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 62,
            "target": 74,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 63,
            "target": 67,
            "weight": 2
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "1976ApJ...203..297S",
              "2008PASP..120.1222F",
              "1998SPIE.3355..324R",
              "1998SPIE.3355..285F",
              "1998SPIE.3355..577M",
              "2005ApJ...635L.125G",
              "1979ApJ...232..352S",
              "2007ASPC..376..249M",
              "1998PASP..110..934K",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 63,
            "target": 69,
            "weight": 21
          },
          {
            "overlap": [
              "1999ApJ...527...54B",
              "2005ApJ...635L.125G",
              "1976ApJ...203..297S",
              "1998SPIE.3355..577M",
              "2007ASPC..376..249M",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F",
              "2003MNRAS.341...33K"
            ],
            "source": 63,
            "target": 72,
            "weight": 19
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 63,
            "target": 73,
            "weight": 5
          },
          {
            "overlap": [
              "1988MNRAS.232..431E",
              "1976ApJ...203..297S",
              "1998PASP..110..934K",
              "1998SPIE.3355..577M",
              "1979ApJ...232..352S",
              "1997ApJ...482..104S",
              "2000ApJ...545..781D"
            ],
            "source": 63,
            "target": 74,
            "weight": 14
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 63,
            "target": 76,
            "weight": 3
          },
          {
            "overlap": [
              "1998ARA&A..36..435M",
              "1998PASP..110..934K"
            ],
            "source": 63,
            "target": 80,
            "weight": 4
          },
          {
            "overlap": [
              "2008PASP..120.1222F",
              "2010ApJ...709..832G",
              "2005PASP..117.1411F",
              "2006ApJ...643..128W"
            ],
            "source": 63,
            "target": 84,
            "weight": 10
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 63,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 63,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "2000A&AS..143...61E",
              "2000A&AS..143..111G",
              "2000A&AS..143...41K"
            ],
            "source": 65,
            "target": 66,
            "weight": 51
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 65,
            "target": 71,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 65,
            "target": 75,
            "weight": 10
          },
          {
            "overlap": [
              "2000A&AS..143...85A",
              "1994AAS...185.4104E",
              "2000A&AS..143...61E",
              "1994AAS...184.2802G",
              "2000A&AS..143..111G",
              "1992ASPC...25...47M"
            ],
            "source": 65,
            "target": 79,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143....9W"
            ],
            "source": 65,
            "target": 81,
            "weight": 26
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 65,
            "target": 82,
            "weight": 7
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 65,
            "target": 85,
            "weight": 13
          },
          {
            "overlap": [
              "1995ASPC...77...28E"
            ],
            "source": 65,
            "target": 88,
            "weight": 15
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 65,
            "target": 91,
            "weight": 38
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...41K"
            ],
            "source": 66,
            "target": 91,
            "weight": 71
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 66,
            "target": 71,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 66,
            "target": 75,
            "weight": 19
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A",
              "2000A&AS..143...61E"
            ],
            "source": 66,
            "target": 79,
            "weight": 20
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K"
            ],
            "source": 66,
            "target": 81,
            "weight": 31
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 66,
            "target": 82,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K",
              "2000A&AS..143...61E"
            ],
            "source": 66,
            "target": 85,
            "weight": 24
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 67,
            "target": 69,
            "weight": 1
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1996AJ....111.1748F",
              "1998PASP..110...79F"
            ],
            "source": 67,
            "target": 74,
            "weight": 5
          },
          {
            "overlap": [
              "2006ApJ...647..303B",
              "1998PASP..110..934K",
              "1992A&AS...96..269S"
            ],
            "source": 67,
            "target": 80,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1992nrfa.book.....P"
            ],
            "source": 67,
            "target": 86,
            "weight": 5
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 67,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1998PASP..110..934K"
            ],
            "source": 67,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1979AJ.....84.1511T"
            ],
            "source": 68,
            "target": 90,
            "weight": 9
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 68,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1987ApJ...315L..93T",
              "1989ApJ...343....1D",
              "1979AJ.....84.1511T"
            ],
            "source": 68,
            "target": 76,
            "weight": 23
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 68,
            "target": 86,
            "weight": 5
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 68,
            "target": 78,
            "weight": 18
          },
          {
            "overlap": [
              "1987ApJ...318..161S",
              "1989ApJ...343....1D",
              "1988AJ.....96.1791F"
            ],
            "source": 68,
            "target": 73,
            "weight": 24
          },
          {
            "overlap": [
              "2007ApJ...657..738L",
              "1999ApJ...527...54B",
              "2005ApJ...635L.125G",
              "2002SPIE.4836...73W",
              "1976ApJ...203..297S",
              "2004ApJ...615..209H",
              "1998SPIE.3355..577M",
              "2008ApJ...677..169V",
              "2005PASP..117..227K",
              "2007ASPC..376..249M",
              "2008ApJS..175..128S",
              "2008ApJS..175..297A",
              "2001PASP..113.1449C",
              "2006ApJ...643..128W",
              "2005PASP..117.1411F"
            ],
            "source": 69,
            "target": 72,
            "weight": 27
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 69,
            "target": 73,
            "weight": 4
          },
          {
            "overlap": [
              "1996A&AS..117..393B",
              "1998SPIE.3355..577M",
              "1976ApJ...203..297S",
              "1979ApJ...232..352S",
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 74,
            "weight": 8
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 69,
            "target": 76,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "2007AJ....134..527W"
            ],
            "source": 69,
            "target": 80,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2008PASP..120.1222F",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W"
            ],
            "source": 69,
            "target": 84,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 69,
            "target": 86,
            "weight": 2
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
              "2000A&AS..143...41K"
            ],
            "source": 71,
            "target": 75,
            "weight": 15
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 71,
            "target": 81,
            "weight": 26
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 71,
            "target": 82,
            "weight": 11
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 71,
            "target": 85,
            "weight": 6
          },
          {
            "overlap": [
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 71,
            "target": 91,
            "weight": 39
          },
          {
            "overlap": [
              "2002AJ....123..485S"
            ],
            "source": 72,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 72,
            "target": 73,
            "weight": 5
          },
          {
            "overlap": [
              "1976ApJ...203..297S",
              "1998SPIE.3355..577M"
            ],
            "source": 72,
            "target": 74,
            "weight": 4
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 72,
            "target": 76,
            "weight": 3
          },
          {
            "overlap": [
              "2005PASP..117.1411F",
              "2002SPIE.4836...73W",
              "2006ApJ...643..128W"
            ],
            "source": 72,
            "target": 84,
            "weight": 8
          },
          {
            "overlap": [
              "1989Sci...246..897G"
            ],
            "source": 73,
            "target": 90,
            "weight": 5
          },
          {
            "overlap": [
              "1976ApJ...203..297S"
            ],
            "source": 73,
            "target": 74,
            "weight": 4
          },
          {
            "overlap": [
              "1989AJ.....98.1143T",
              "1989Sci...246..897G",
              "1990ApJS...72..433H",
              "1976ApJ...203..297S",
              "1989ApJ...343....1D"
            ],
            "source": 73,
            "target": 76,
            "weight": 32
          },
          {
            "overlap": [
              "1990AJ.....99.1004B",
              "1976ApJ...203..297S"
            ],
            "source": 74,
            "target": 76,
            "weight": 6
          },
          {
            "overlap": [
              "1998ApJ...500..525S",
              "1998PASP..110..934K"
            ],
            "source": 74,
            "target": 80,
            "weight": 4
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 74,
            "target": 86,
            "weight": 2
          },
          {
            "overlap": [
              "1998PASP..110...79F",
              "1997AJ....114.2205G",
              "1996ApJ...470..172S"
            ],
            "source": 74,
            "target": 89,
            "weight": 6
          },
          {
            "overlap": [
              "1994ApJ...428...43M",
              "1997AJ....113..185M",
              "1990AJ.....99.2019L",
              "1996ApJ...470..172S"
            ],
            "source": 74,
            "target": 90,
            "weight": 9
          },
          {
            "overlap": [
              "1998PASP..110..934K",
              "1998PASP..110...79F"
            ],
            "source": 74,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 75,
            "target": 79,
            "weight": 5
          },
          {
            "overlap": [
              "1992ald2.proc...85K",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 75,
            "target": 81,
            "weight": 52
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 75,
            "target": 82,
            "weight": 33
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 75,
            "target": 85,
            "weight": 13
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 75,
            "target": 88,
            "weight": 22
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 75,
            "target": 91,
            "weight": 19
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 76,
            "target": 78,
            "weight": 15
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 76,
            "target": 86,
            "weight": 4
          },
          {
            "overlap": [
              "1986ApJ...302L...1D",
              "1979AJ.....84.1511T",
              "1989Sci...246..897G"
            ],
            "source": 76,
            "target": 90,
            "weight": 11
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 76,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 78,
            "target": 89,
            "weight": 11
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 78,
            "target": 90,
            "weight": 11
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 78,
            "target": 79,
            "weight": 10
          },
          {
            "overlap": [
              "1986SPIE..627..733T",
              "1979AJ.....84.1511T"
            ],
            "source": 78,
            "target": 86,
            "weight": 27
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "1996ASPC..101..581P"
            ],
            "source": 79,
            "target": 81,
            "weight": 13
          },
          {
            "overlap": [
              "2000A&AS..143....1G",
              "1993ASPC...52..132K"
            ],
            "source": 79,
            "target": 82,
            "weight": 8
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1992PASA...10..134S",
              "1993ASPC...52..132K",
              "1995VA.....39R.272S",
              "1992PASAu..10..134S",
              "1999adass...8..287L",
              "1999ASPC..172..287L",
              "1988alds.proc..335H",
              "2000A&AS..143...61E"
            ],
            "source": 79,
            "target": 85,
            "weight": 21
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 79,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 79,
            "target": 88,
            "weight": 8
          },
          {
            "overlap": [
              "1986SPIE..627..733T"
            ],
            "source": 79,
            "target": 89,
            "weight": 2
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...85A"
            ],
            "source": 79,
            "target": 91,
            "weight": 13
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1985ApJ...298....8H",
              "1998PASP..110..934K"
            ],
            "source": 80,
            "target": 92,
            "weight": 3
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K",
              "2005JASIS..56...36K"
            ],
            "source": 81,
            "target": 82,
            "weight": 27
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "1993ASPC...52..132K",
              "2000A&AS..143...41K"
            ],
            "source": 81,
            "target": 85,
            "weight": 16
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 81,
            "target": 88,
            "weight": 18
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2002SPIE.4847..238K",
              "2000A&AS..143...41K"
            ],
            "source": 81,
            "target": 91,
            "weight": 48
          },
          {
            "overlap": [
              "1993ASPC...52..132K",
              "2000A&AS..143...41K",
              "1999ASPC..172..291A"
            ],
            "source": 82,
            "target": 85,
            "weight": 14
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 82,
            "target": 88,
            "weight": 16
          },
          {
            "overlap": [
              "2000A&AS..143...41K"
            ],
            "source": 82,
            "target": 91,
            "weight": 13
          },
          {
            "overlap": [
              "1995ASPC...77...36A"
            ],
            "source": 83,
            "target": 85,
            "weight": 17
          },
          {
            "overlap": [
              "1995ASPC...77...36A"
            ],
            "source": 83,
            "target": 87,
            "weight": 99
          },
          {
            "overlap": [
              "1992MNRAS.254..389R"
            ],
            "source": 84,
            "target": 86,
            "weight": 3
          },
          {
            "overlap": [
              "1995ASPC...77...36A"
            ],
            "source": 85,
            "target": 87,
            "weight": 17
          },
          {
            "overlap": [
              "1993ASPC...52..132K"
            ],
            "source": 85,
            "target": 88,
            "weight": 9
          },
          {
            "overlap": [
              "2000A&AS..143..111G",
              "2000A&AS..143...41K"
            ],
            "source": 85,
            "target": 91,
            "weight": 16
          },
          {
            "overlap": [
              "1993adass...2..173T",
              "1986SPIE..627..733T",
              "1993ASPC...52..173T"
            ],
            "source": 86,
            "target": 89,
            "weight": 9
          },
          {
            "overlap": [
              "1979AJ.....84.1511T"
            ],
            "source": 86,
            "target": 90,
            "weight": 3
          },
          {
            "overlap": [
              "1998PASP..110..934K"
            ],
            "source": 86,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1992ASPC...25..432K",
              "1994ApJ...424L...1D",
              "1996ApJ...470..172S"
            ],
            "source": 89,
            "target": 90,
            "weight": 8
          },
          {
            "overlap": [
              "1998PASP..110...79F"
            ],
            "source": 89,
            "target": 92,
            "weight": 2
          },
          {
            "overlap": [
              "1986ApJ...302L...1D"
            ],
            "source": 90,
            "target": 92,
            "weight": 2
          }
        ],
        "multigraph": false,
        "nodes": [
          {
            "citation_count": 0,
            "first_author": "Henneken, Edwin A.",
            "group": 0,
            "id": 0,
            "nodeName": "2014arXiv1406.4542H",
            "nodeWeight": 0,
            "title": "Computing and Using Metrics in the ADS"
          },
          {
            "citation_count": 1,
            "first_author": "Accomazzi, A.",
            "group": 0,
            "id": 1,
            "nodeName": "2009arad.workE..32A",
            "nodeWeight": 1,
            "title": "Towards a Resource-Centric Data Network for Astronomy"
          },
          {
            "citation_count": 1,
            "first_author": "Eichhorn, Guenther",
            "group": 1,
            "id": 2,
            "nodeName": "1998ASPC..153..277E",
            "nodeWeight": 1,
            "title": "The Astrophysics Data System"
          },
          {
            "citation_count": 8,
            "first_author": "Henneken, Edwin A.",
            "group": 0,
            "id": 3,
            "nodeName": "2006JEPub...9....2H",
            "nodeWeight": 8,
            "title": "Effect of E-printing on Citation Rates in Astronomy and Physics"
          },
          {
            "citation_count": 1,
            "first_author": "Mink, D. J.",
            "group": 2,
            "id": 4,
            "nodeName": "2003ASPC..295...51M",
            "nodeWeight": 1,
            "title": "Federating Catalogs and Interfacing Them with Archives: A VO Prototype"
          },
          {
            "citation_count": 36,
            "first_author": "Geller, Margaret J.",
            "group": 2,
            "id": 5,
            "nodeName": "2005ApJ...635L.125G",
            "nodeWeight": 36,
            "title": "SHELS: The Hectospec Lensing Survey"
          },
          {
            "citation_count": 2,
            "first_author": "Accomazzi, A.",
            "group": 0,
            "id": 6,
            "nodeName": "2007ASPC..377...69A",
            "nodeWeight": 2,
            "title": "Creation and Use of Citations in the ADS"
          },
          {
            "citation_count": 4,
            "first_author": "Kurtz, Michael J.",
            "group": 1,
            "id": 7,
            "nodeName": "1998ASPC..153..293K",
            "nodeWeight": 4,
            "title": "The Historical Literature of Astronomy, via ADS"
          },
          {
            "citation_count": 2,
            "first_author": "Utsumi, Yousuke",
            "group": 2,
            "id": 8,
            "nodeName": "2014ApJ...786...93U",
            "nodeWeight": 2,
            "title": "Reducing Systematic Error in Weak Lensing Cluster Surveys"
          },
          {
            "citation_count": 62,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 10,
            "nodeName": "2007ApJ...671.1708B",
            "nodeWeight": 62,
            "title": "Hypervelocity Stars. III. The Space Density and Ejection History of Main-Sequence Stars from the Galactic Center"
          },
          {
            "citation_count": 12,
            "first_author": "Kleyna, J. T.",
            "group": 4,
            "id": 11,
            "nodeName": "1997AJ....113..624K",
            "nodeWeight": 12,
            "title": "An Adaptive Kernel Approach to Finding dSph Galaxies Around the Milky Way"
          },
          {
            "citation_count": 3,
            "first_author": "Henneken, Edwin A.",
            "group": 0,
            "id": 12,
            "nodeName": "2012opsa.book..253H",
            "nodeWeight": 3,
            "title": "The ADS in the Information Age - Impact on Discovery"
          },
          {
            "citation_count": 5,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 14,
            "nodeName": "1998ASPC..145..378E",
            "nodeWeight": 5,
            "title": "New Capabilities of the ADS Abstract and Article Service"
          },
          {
            "citation_count": 4,
            "first_author": "Henneken, Edwin A.",
            "group": 1,
            "id": 15,
            "nodeName": "2011ApSSP...1..125H",
            "nodeWeight": 4,
            "title": "Finding Your Literature Match - A Recommender System"
          },
          {
            "citation_count": 3,
            "first_author": "Kurtz, M. J.",
            "group": 1,
            "id": 16,
            "nodeName": "2010ASPC..434..155K",
            "nodeWeight": 3,
            "title": "Using Multipartite Graphs for Recommendation and Discovery"
          },
          {
            "citation_count": 0,
            "first_author": "Henneken, Edwin A.",
            "group": 0,
            "id": 17,
            "nodeName": "2006AAS...20917302H",
            "nodeWeight": 0,
            "title": "Finding Astronomical Communities Through Co-readership Analysis"
          },
          {
            "citation_count": 68,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 18,
            "nodeName": "2006ApJ...640L..35B",
            "nodeWeight": 68,
            "title": "A Successful Targeted Search for Hypervelocity Stars"
          },
          {
            "citation_count": 2,
            "first_author": "Mink, D. J.",
            "group": 4,
            "id": 19,
            "nodeName": "1998ASPC..145...93M",
            "nodeWeight": 2,
            "title": "RVSAO 2.0 - A Radial Velocity Package for IRAF"
          },
          {
            "citation_count": 1,
            "first_author": "Eichhorn, G.",
            "group": 0,
            "id": 21,
            "nodeName": "2007ASPC..377...36E",
            "nodeWeight": 1,
            "title": "Connectivity in the Astronomy Digital Library"
          },
          {
            "citation_count": 34,
            "first_author": "Fabricant, Daniel",
            "group": 4,
            "id": 22,
            "nodeName": "1993AJ....105..788F",
            "nodeWeight": 34,
            "title": "A Study of the Rich Cluster of Galaxies A119"
          },
          {
            "citation_count": 5,
            "first_author": "Henneken, Edwin A.",
            "group": 0,
            "id": 23,
            "nodeName": "2009JInfo...3....1H",
            "nodeWeight": 5,
            "title": "Use of astronomical literature - A report on usage patterns"
          },
          {
            "citation_count": 0,
            "first_author": "Henneken, E. A.",
            "group": 0,
            "id": 24,
            "nodeName": "2012LPI....43.1022H",
            "nodeWeight": 0,
            "title": "Online Discovery: Search Paradigms and the Art of Literature Exploration"
          },
          {
            "citation_count": 76,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 25,
            "nodeName": "2006ApJ...647..303B",
            "nodeWeight": 76,
            "title": "Hypervelocity Stars. I. The Spectroscopic Survey"
          },
          {
            "citation_count": 0,
            "first_author": "Geller, M. J.",
            "group": 4,
            "id": 26,
            "nodeName": "1986NYASA.470..123G",
            "nodeWeight": 0,
            "title": "The galaxy distribution and the large-scale structure of the Universe."
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, G.",
            "group": 0,
            "id": 27,
            "nodeName": "2007ASPC..377...93E",
            "nodeWeight": 0,
            "title": "Full Text Searching in the Astrophysics Data System"
          },
          {
            "citation_count": 59,
            "first_author": "Rines, Kenneth",
            "group": 2,
            "id": 28,
            "nodeName": "2005AJ....130.1482R",
            "nodeWeight": 59,
            "title": "CAIRNS: The Cluster and Infall Region Nearby Survey. III. Environmental Dependence of Hα Properties of Galaxies"
          },
          {
            "citation_count": 0,
            "first_author": "Henneken, E. A.",
            "group": 1,
            "id": 29,
            "nodeName": "2009LPI....40.1873H",
            "nodeWeight": 0,
            "title": "The SAO/NASA Astrophysics Data System: A Gateway to the Planetary Sciences Literature"
          },
          {
            "citation_count": 23,
            "first_author": "Bothun, Gregory D.",
            "group": 4,
            "id": 30,
            "nodeName": "1992ApJ...395..347B",
            "nodeWeight": 23,
            "title": "The Velocity-Distance Relation for Galaxies on a Bubble"
          },
          {
            "citation_count": 1,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 31,
            "nodeName": "2011ApSSP...1...23K",
            "nodeWeight": 1,
            "title": "The Emerging Scholarly Brain"
          },
          {
            "citation_count": 25,
            "first_author": "Willmer, C. N. A.",
            "group": 4,
            "id": 32,
            "nodeName": "1994ApJ...437..560W",
            "nodeWeight": 25,
            "title": "A Medium-deep Redshift Survey of a Minislice at the North Galactic Pole"
          },
          {
            "citation_count": 21,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 33,
            "nodeName": "2003AJ....126.1362B",
            "nodeWeight": 21,
            "title": "The Century Survey Galactic Halo Project. I. Stellar Spectral Analysis"
          },
          {
            "citation_count": 20,
            "first_author": "Alonso, Victoria M.",
            "group": 4,
            "id": 34,
            "nodeName": "1993AJ....106..676A",
            "nodeWeight": 20,
            "title": "CCD Calibration of the Magnitude Scale for the Southern Sky Redshift Survey Extension Galaxy Sample"
          },
          {
            "citation_count": 0,
            "first_author": "Accomazzi, A.",
            "group": 1,
            "id": 35,
            "nodeName": "2003ASPC..295..309A",
            "nodeWeight": 0,
            "title": "ADS Web Services for the Discovery and Linking of Bibliographic Records"
          },
          {
            "citation_count": 4,
            "first_author": "Mink, D. J.",
            "group": 2,
            "id": 36,
            "nodeName": "2005ASPC..347..228M",
            "nodeWeight": 4,
            "title": "Creating Data that Never Die: Building a Spectrograph Data Pipeline in the Virtual Observatory Era"
          },
          {
            "citation_count": 20,
            "first_author": "Freedman Woods, Deborah",
            "group": 2,
            "id": 37,
            "nodeName": "2010AJ....139.1857F",
            "nodeWeight": 20,
            "title": "Triggered Star Formation in Galaxy Pairs at z = 0.08-0.38"
          },
          {
            "citation_count": 17,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 38,
            "nodeName": "2005AJ....130.1097B",
            "nodeWeight": 17,
            "title": "The Century Survey Galactic Halo Project. II. Global Properties and the Luminosity Function of Field Blue Horizontal Branch Stars"
          },
          {
            "citation_count": 67,
            "first_author": "Carter, B. J.",
            "group": 4,
            "id": 39,
            "nodeName": "2001ApJ...559..606C",
            "nodeWeight": 67,
            "title": "Star Formation in a Complete Spectroscopic Survey of Galaxies"
          },
          {
            "citation_count": 0,
            "first_author": "Kurtz, Michael J.",
            "group": 4,
            "id": 40,
            "nodeName": "1995PASP..107..776K",
            "nodeWeight": 0,
            "title": "Giant Shoulders: Data and Discovery in Astronomy"
          },
          {
            "citation_count": 6,
            "first_author": "Henneken, E.",
            "group": 0,
            "id": 41,
            "nodeName": "2007ASPC..377..106H",
            "nodeWeight": 6,
            "title": "myADS-arXiv -- a Tailor-made, Open Access, Virtual Journal"
          },
          {
            "citation_count": 2,
            "first_author": "Mink, D. J.",
            "group": 2,
            "id": 42,
            "nodeName": "2001ASPC..238..491M",
            "nodeWeight": 2,
            "title": "SVDFIT: An IRAF Task for Eigenvector Sky Subtraction"
          },
          {
            "citation_count": 17,
            "first_author": "Eichhorn, Guenther",
            "group": 1,
            "id": 43,
            "nodeName": "2000A&AS..143...61E",
            "nodeWeight": 17,
            "title": "The NASA Astrophysics Data System: The search engine and its user interface"
          },
          {
            "citation_count": 54,
            "first_author": "Bromley, Benjamin C.",
            "group": 2,
            "id": 44,
            "nodeName": "2006ApJ...653.1194B",
            "nodeWeight": 54,
            "title": "Hypervelocity Stars: Predicting the Spectrum of Ejection Velocities"
          },
          {
            "citation_count": 1,
            "first_author": "Starikova, Svetlana",
            "group": 2,
            "id": 45,
            "nodeName": "2014ApJ...786..125S",
            "nodeWeight": 1,
            "title": "Comparison of Galaxy Clusters Selected by Weak-lensing, Optical Spectroscopy, and X-Rays in the Deep Lens Survey F2 Field"
          },
          {
            "citation_count": 20,
            "first_author": "Grant, Carolyn S.",
            "group": 0,
            "id": 46,
            "nodeName": "2000A&AS..143..111G",
            "nodeWeight": 20,
            "title": "The NASA Astrophysics Data System: Data holdings"
          },
          {
            "citation_count": 55,
            "first_author": "Kewley, Lisa J.",
            "group": 2,
            "id": 47,
            "nodeName": "2007AJ....133..882K",
            "nodeWeight": 55,
            "title": "SDSS 0809+1729: Connections Between Extremely Metal-Poor Galaxies and Gamma-Ray Burst Hosts"
          },
          {
            "citation_count": 4,
            "first_author": "Accomazzi, A.",
            "group": 1,
            "id": 48,
            "nodeName": "1996ASPC..101..558A",
            "nodeWeight": 4,
            "title": "The ADS Article Service Data Holdings and Access Methods"
          },
          {
            "citation_count": 13,
            "first_author": "Kurtz, Michael J.",
            "group": 2,
            "id": 49,
            "nodeName": "2007AJ....134.1360K",
            "nodeWeight": 13,
            "title": "μ-PhotoZ: Photometric Redshifts by Inverting the Tolman Surface Brightness Test"
          },
          {
            "citation_count": 78,
            "first_author": "Fabricant, Daniel G.",
            "group": 4,
            "id": 50,
            "nodeName": "1989ApJ...336...77F",
            "nodeWeight": 78,
            "title": "A Combined Optical/X-Ray Study of the Galaxy Cluster Abell 2256"
          },
          {
            "citation_count": 8,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 51,
            "nodeName": "2010ARIST..44....3K",
            "nodeWeight": 8,
            "title": "Usage Bibliometrics"
          },
          {
            "citation_count": 79,
            "first_author": "Geller, Margaret J.",
            "group": 4,
            "id": 52,
            "nodeName": "1997AJ....114.2205G",
            "nodeWeight": 79,
            "title": "The Century Survey: A Deeper Slice of the Universe"
          },
          {
            "citation_count": 294,
            "first_author": "Falco, Emilio E.",
            "group": 4,
            "id": 53,
            "nodeName": "1999PASP..111..438F",
            "nodeWeight": 294,
            "title": "The Updated Zwicky Catalog (UZC)"
          },
          {
            "citation_count": 4,
            "first_author": "Henneken, Edwin A.",
            "group": 0,
            "id": 54,
            "nodeName": "2007LePub..20...16H",
            "nodeWeight": 4,
            "title": "E-prints and journal articles in astronomy: a productive co-existence"
          },
          {
            "citation_count": 6,
            "first_author": "Schulman, E.",
            "group": 1,
            "id": 55,
            "nodeName": "1997PASP..109.1278S",
            "nodeWeight": 6,
            "title": "Trends in Astronomical Publication Between 1975 and 1996"
          },
          {
            "citation_count": 24,
            "first_author": "Geller, Margaret J.",
            "group": 2,
            "id": 56,
            "nodeName": "2010ApJ...709..832G",
            "nodeWeight": 24,
            "title": "SHELS: Testing Weak-Lensing Maps with Redshift Surveys"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, G.",
            "group": 0,
            "id": 57,
            "nodeName": "2003LPI....34.1949E",
            "nodeWeight": 0,
            "title": "Expanded Citations Database in the NASA ADS Abstract Service"
          },
          {
            "citation_count": 67,
            "first_author": "Geller, M. J.",
            "group": 4,
            "id": 58,
            "nodeName": "1984ApJ...287L..55G",
            "nodeWeight": 67,
            "title": "The Shane-Wirtanen counts"
          },
          {
            "citation_count": 17,
            "first_author": "Wegner, Gary",
            "group": 4,
            "id": 60,
            "nodeName": "2001AJ....122.2893W",
            "nodeWeight": 17,
            "title": "Redshifts for 2410 Galaxies in the Century Survey Region"
          },
          {
            "citation_count": 12,
            "first_author": "Mahdavi, Andisheh",
            "group": 4,
            "id": 61,
            "nodeName": "1996AJ....111...64M",
            "nodeWeight": 12,
            "title": "The Lumpy Cluster Abell 1185"
          },
          {
            "citation_count": 49,
            "first_author": "Kleyna, J. T.",
            "group": 4,
            "id": 62,
            "nodeName": "1998AJ....115.2359K",
            "nodeWeight": 49,
            "title": "A V and I CCD Mosaic Survey of the Ursa Minor Dwarf Spheroidal Galaxy"
          },
          {
            "citation_count": 158,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 63,
            "nodeName": "2005ApJ...622L..33B",
            "nodeWeight": 158,
            "title": "Discovery of an Unbound Hypervelocity Star in the Milky Way Halo"
          },
          {
            "citation_count": 0,
            "first_author": "Accomazzi, A.",
            "group": 0,
            "id": 64,
            "nodeName": "1998ASPC..145..395A",
            "nodeWeight": 0,
            "title": "Mirroring the ADS Bibliographic Databases"
          },
          {
            "citation_count": 26,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 65,
            "nodeName": "2008AJ....135..564B",
            "nodeWeight": 26,
            "title": "The Century Survey Galactic Halo Project III: A Complete 4300 DEG<SUP>2</SUP> Survey of Blue Horizontal Branch Stars in the Metal-Weak Thick Disk and Inner Halo"
          },
          {
            "citation_count": 46,
            "first_author": "Rines, K.",
            "group": 2,
            "id": 66,
            "nodeName": "2001ApJ...561L..41R",
            "nodeWeight": 46,
            "title": "Infrared Mass-to-Light Profile throughout the Infall Region of the Coma Cluster"
          },
          {
            "citation_count": 13,
            "first_author": "Geller, Margaret J.",
            "group": 2,
            "id": 67,
            "nodeName": "2012AJ....143..102G",
            "nodeWeight": 13,
            "title": "The Faint End of the Luminosity Function and Low Surface Brightness Galaxies"
          },
          {
            "citation_count": 6,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 68,
            "nodeName": "1995VA.....39..217E",
            "nodeWeight": 6,
            "title": "Access to the Astrophysics Science Information and Abstract System"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, Guenther",
            "group": 0,
            "id": 69,
            "nodeName": "2007BASI...35..717E",
            "nodeWeight": 0,
            "title": "Access to the literature and connection to on-line data"
          },
          {
            "citation_count": 0,
            "first_author": "Eichhorn, Guenther",
            "group": 0,
            "id": 71,
            "nodeName": "2004tivo.conf..267E",
            "nodeWeight": 0,
            "title": "The Astronomy Digital Library and the VO"
          },
          {
            "citation_count": 52,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 73,
            "nodeName": "2007ApJ...660..311B",
            "nodeWeight": 52,
            "title": "Hypervelocity Stars. II. The Bound Population"
          },
          {
            "citation_count": 23,
            "first_author": "Thorstensen, J. R.",
            "group": 4,
            "id": 74,
            "nodeName": "1989AJ.....98.1143T",
            "nodeWeight": 23,
            "title": "Redshifts for a Sample of Fainter Galaxies in the First CfA Survey Slice"
          },
          {
            "citation_count": 19,
            "first_author": "Westra, Eduard",
            "group": 2,
            "id": 75,
            "nodeName": "2010ApJ...708..534W",
            "nodeWeight": 19,
            "title": "Evolution of the Hα Luminosity Function"
          },
          {
            "citation_count": 1,
            "first_author": "Schulman, Eric",
            "group": 1,
            "id": 76,
            "nodeName": "1997ASPC..125..361S",
            "nodeWeight": 1,
            "title": "The Sociology of Astronomical Publication Using ADS and ADAMS"
          },
          {
            "citation_count": 1,
            "first_author": "Henneken, E. A.",
            "group": 0,
            "id": 77,
            "nodeName": "2009ASPC..411..384H",
            "nodeWeight": 1,
            "title": "Exploring the Astronomy Literature Landscape"
          },
          {
            "citation_count": 16,
            "first_author": "Fabricant, Daniel G.",
            "group": 2,
            "id": 78,
            "nodeName": "2008PASP..120.1222F",
            "nodeWeight": 16,
            "title": "Spectrophotometry with Hectospec, the MMT's Fiber-Fed Spectrograph"
          },
          {
            "citation_count": 12,
            "first_author": "Wegner, Gary",
            "group": 4,
            "id": 79,
            "nodeName": "1990AJ....100.1405W",
            "nodeWeight": 12,
            "title": "Redshifts for Fainter Galaxies in the First CfA Survey Slice. II."
          },
          {
            "citation_count": 37,
            "first_author": "Brown, Warren R.",
            "group": 4,
            "id": 80,
            "nodeName": "2001AJ....122..714B",
            "nodeWeight": 37,
            "title": "V- and R-band Galaxy Luminosity Functions and Low Surface Brightness Galaxies in the Century Survey"
          },
          {
            "citation_count": 1,
            "first_author": "Kurtz, Michael",
            "group": 0,
            "id": 81,
            "nodeName": "2009astro2010P..28K",
            "nodeWeight": 1,
            "title": "The Smithsonian/NASA Astrophysics Data System (ADS) Decennial Report"
          },
          {
            "citation_count": 17,
            "first_author": "Thorstensen, John R.",
            "group": 4,
            "id": 82,
            "nodeName": "1995AJ....109.2368T",
            "nodeWeight": 17,
            "title": "Redshifts for Fainter Galaxies in the First CFA Slice. III. To the Zwicky Catalog Limit"
          },
          {
            "citation_count": 14,
            "first_author": "Kleyna, J.",
            "group": 4,
            "id": 83,
            "nodeName": "1999AJ....117.1275K",
            "nodeWeight": 14,
            "title": "Measuring the Dark Matter Scale of Local Group Dwarf Spheroidals"
          },
          {
            "citation_count": 109,
            "first_author": "Kurtz, Michael J.",
            "group": 4,
            "id": 84,
            "nodeName": "1992ASPC...25..432K",
            "nodeWeight": 109,
            "title": "XCSAO: A Radial Velocity Package for the IRAF Environment"
          },
          {
            "citation_count": 60,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 86,
            "nodeName": "2000A&AS..143...41K",
            "nodeWeight": 60,
            "title": "The NASA Astrophysics Data System: Overview"
          },
          {
            "citation_count": 15,
            "first_author": "Brown, Warren R.",
            "group": 2,
            "id": 87,
            "nodeName": "2007ApJ...666..231B",
            "nodeWeight": 15,
            "title": "Stellar Velocity Dispersion of the Leo A Dwarf Galaxy"
          },
          {
            "citation_count": 1,
            "first_author": "Kurtz, M. J.",
            "group": 0,
            "id": 88,
            "nodeName": "2006ASPC..351..653K",
            "nodeWeight": 1,
            "title": "Intelligent Information Retrieval"
          },
          {
            "citation_count": 14,
            "first_author": "Kurtz, Michael J.",
            "group": 0,
            "id": 89,
            "nodeName": "2005IPM....41.1395K",
            "nodeWeight": 14,
            "title": "The Effect of Use and Access on Citations"
          },
          {
            "citation_count": 3,
            "first_author": "Accomazzi, A.",
            "group": 11,
            "id": 90,
            "nodeName": "1997ASPC..125..357A",
            "nodeWeight": 3,
            "title": "Astronomical Information Discovery and Access: Design and Implementation of the ADS Bibliographic Services"
          },
          {
            "citation_count": 2,
            "first_author": "Fabricant, Daniel",
            "group": 2,
            "id": 91,
            "nodeName": "2013PASP..125.1362F",
            "nodeWeight": 2,
            "title": "Measuring Galaxy Velocity Dispersions with Hectospec"
          },
          {
            "citation_count": 20,
            "first_author": "Accomazzi, Alberto",
            "group": 0,
            "id": 92,
            "nodeName": "2000A&AS..143...85A",
            "nodeWeight": 20,
            "title": "The NASA Astrophysics Data System: Architecture"
          },
          {
            "citation_count": 9,
            "first_author": "Barton, Elizabeth J.",
            "group": 4,
            "id": 93,
            "nodeName": "2000PASP..112..367B",
            "nodeWeight": 9,
            "title": "Rotation Curve Measurement using Cross-Correlation"
          },
          {
            "citation_count": 11,
            "first_author": "Eichhorn, G.",
            "group": 11,
            "id": 94,
            "nodeName": "1995ASPC...77...28E",
            "nodeWeight": 11,
            "title": "The New Astrophysics Data System"
          },
          {
            "citation_count": 8,
            "first_author": "Eichhorn, G.",
            "group": 1,
            "id": 96,
            "nodeName": "1996ASPC..101..569E",
            "nodeWeight": 8,
            "title": "Various Access Methods to the Abstracts in the Astrophysics Data System"
          },
          {
            "citation_count": 285,
            "first_author": "Kurtz, Michael J.",
            "group": 4,
            "id": 97,
            "nodeName": "1998PASP..110..934K",
            "nodeWeight": 285,
            "title": "RVSAO 2.0: Digital Redshifts and Radial Velocities"
          },
          {
            "citation_count": 138,
            "first_author": "da Costa, L. Nicolaci",
            "group": 4,
            "id": 98,
            "nodeName": "1998AJ....116....1D",
            "nodeWeight": 138,
            "title": "The Southern Sky Redshift Survey"
          },
          {
            "citation_count": 0,
            "first_author": "Accomazzi, A.",
            "group": 0,
            "id": 99,
            "nodeName": "2006ASPC..351..715A",
            "nodeWeight": 0,
            "title": "Bibliographic Classification using the ADS Databases"
          },
          {
            "citation_count": 137,
            "first_author": "Rines, Kenneth",
            "group": 2,
            "id": 100,
            "nodeName": "2003AJ....126.2152R",
            "nodeWeight": 137,
            "title": "CAIRNS: The Cluster and Infall Region Nearby Survey. I. Redshifts and Mass Profiles"
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
            "weight": 4343
          },
          {
            "source": 0,
            "target": 1,
            "weight": 972
          },
          {
            "source": 0,
            "target": 2,
            "weight": 95
          },
          {
            "source": 0,
            "target": 4,
            "weight": 34
          },
          {
            "source": 0,
            "target": 3,
            "weight": 35
          },
          {
            "source": 1,
            "target": 1,
            "weight": 590
          },
          {
            "source": 1,
            "target": 2,
            "weight": 12
          },
          {
            "source": 1,
            "target": 3,
            "weight": 14
          },
          {
            "source": 2,
            "target": 2,
            "weight": 2005
          },
          {
            "source": 2,
            "target": 3,
            "weight": 490
          },
          {
            "source": 3,
            "target": 3,
            "weight": 1395
          },
          {
            "source": 4,
            "target": 4,
            "weight": 99
          }
        ],
        "multigraph": false,
        "nodes": [
          {
            "id": 0,
            "nodeName": {
              "ads": 10.783115286630043,
              "astronomy": 9.242670245682895,
              "citation": 10.556229318461034,
              "literature": 7.783640596221253,
              "system": 6.26381484247684,
              "use": 7.917171988845775
            },
            "paperCount": 26,
            "size": 156
          },
          {
            "id": 1,
            "nodeName": {
              "abstract": 5.8377304471659395,
              "ads": 7.702225204735745,
              "astrophysics": 6.26381484247684,
              "literature": 5.8377304471659395,
              "service": 5.8377304471659395,
              "system": 7.516577810972208
            },
            "paperCount": 13,
            "size": 59
          },
          {
            "id": 2,
            "nodeName": {
              "cluster": 9.729550745276565,
              "halo": 13.195286648076292,
              "hypervelocity": 15.83434397769155,
              "infall": 7.917171988845775,
              "star": 19.45910149055313,
              "survey": 21.405011639608446
            },
            "paperCount": 27,
            "size": 999
          },
          {
            "id": 4,
            "nodeName": {
              "galaxies": 17.51319134149782,
              "galaxy": 9.729550745276565,
              "radial": 7.917171988845775,
              "redshift": 15.567281192442506,
              "slice": 10.556229318461034,
              "survey": 19.45910149055313
            },
            "paperCount": 25,
            "size": 1423
          },
          {
            "id": 11,
            "nodeName": {
              "ads": 1.540445040947149,
              "design": 2.6390573296152584,
              "implementation": 2.6390573296152584,
              "information": 1.540445040947149,
              "new": 1.9459101490553132,
              "services": 1.9459101490553132
            },
            "paperCount": 2,
            "size": 14
          }
        ]
      }
    };


    var testDataSmall = {
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

    afterEach(function(){
    $("#test").empty();
    });

    it("should have a help popover", function(){

      var paperNetwork = new PaperNetwork();

      paperNetwork.processResponse(new JsonResponse(testDataBig));

      $("#test").append(paperNetwork.view.el);
      $("#test").find("i.icon-help").mouseover();

      expect($(".popover").text()).to.eql("About This Network VisualizationThe paper network groups papers from your search results based on how many references they have in common. Papers with many references in common are more likely to discuss similar topics.If your search results returned a large enough set of papers, you will see two views: a summary view, which shows groups of tightly linked papers, and a detail view  which shows you the individual papers from a group and how they are connected. The size of the circles in the summary node graph are based on the cumulative number of citations shared by the group, and the titles of the summary nodes are small word clouds based on the words from the titles of the papers in the group.");
      expect($(".popover").css("display")).to.eql("block");

      $("#test").find("i.icon-help").mouseout();
    });


    it("should add mouseover interactions for the detail graph", function(){

      var paperNetwork = new PaperNetwork();
      paperNetwork.processResponse(new JsonResponse(testDataBig));

      $("#test").append(paperNetwork.view.el);
      paperNetwork.view.graphView.model.set("currentGroup", $(".summary-node-group:first-of-type").get(0));

      expect( $(".detail-node:first-of-type").data("content")).to.eql("<b>Title: </b>Computing and Using Metrics in the ADS<br/><b>First Author: </b>Henneken, Edwin A.<br/><b>Citation Count: </b>0");
      expect( $(".detail-node:last-of-type").data("content")).to.eql("<b>Title: </b>Bibliographic Classification using the ADS Databases<br/><b>First Author: </b>Accomazzi, A.<br/><b>Citation Count: </b>0");
    });

    if (!(window.PHANTOMJS || window.mochaPhantomJS)) {
      it("should add mouseover interactions for the detail graph (Non-PhantomJS)", function () {

        var paperNetwork = new PaperNetwork();
        paperNetwork.processResponse(new JsonResponse(testDataBig));

        $("#test").append(paperNetwork.view.el);

        //trigger detail view sadly can't trigger mouse events on svg in phantomjs
        $(".summary-node-group:first-of-type").click();
        $(".detail-node:first-of-type").mouseover();

        expect($(".popover").length).to.eql(1);
        expect($(".popover").text()).to.eql("2014arXiv1406.4542HTitle: Computing and Using Metrics in the ADSFirst Author: Henneken, Edwin A.Citation Count: 0");
        expect($(".popover").css("display")).to.eql("block");
        $(".detail-node:first-of-type").mouseout();

      })
    }

  });

});