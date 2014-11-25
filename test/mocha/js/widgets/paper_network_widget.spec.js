define([
  "js/wraps/paper_network",
  "js/components/json_response"
], function(
  PaperNetwork,
  JsonResponse
  ){

  describe("Paper Network Widget (UI Widget)", function(){

    var testData = {
      "fullGraph": {
      "directed": false,
        "graph": [],
        "links": [
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 6,
          "weight": 17
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 7,
          "weight": 7
        },
        {
          "overlap": [
            "2000A&AS..143...61E",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 10,
          "weight": 19
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 12,
          "weight": 7
        },
        {
          "overlap": [
            "1999ASPC..172..291A"
          ],
          "source": 0,
          "target": 13,
          "weight": 7
        },
        {
          "overlap": [
            "2000A&AS..143...41K",
            "2000A&AS..143...85A",
            "2000A&AS..143...61E"
          ],
          "source": 0,
          "target": 14,
          "weight": 27
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 15,
          "weight": 7
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 19,
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
          "source": 0,
          "target": 20,
          "weight": 24
        },
        {
          "overlap": [
            "1999ASPC..172..291A"
          ],
          "source": 0,
          "target": 21,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 22,
          "weight": 17
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 23,
          "weight": 17
        },
        {
          "overlap": [
            "2000A&AS..143...41K",
            "2000A&AS..143...85A",
            "1988alds.proc..335H",
            "2000A&AS..143...61E"
          ],
          "source": 0,
          "target": 24,
          "weight": 12
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 25,
          "weight": 6
        },
        {
          "overlap": [
            "2000A&AS..143...41K",
            "1998ASPC..153..107B",
            "1999ASPC..172..291A"
          ],
          "source": 0,
          "target": 27,
          "weight": 16
        },
        {
          "overlap": [
            "1995VA.....39R.272S"
          ],
          "source": 0,
          "target": 28,
          "weight": 6
        },
        {
          "overlap": [
            "1988alds.proc..323E",
            "1995VA.....39R.272S",
            "2000A&AS..143...41K",
            "1999adass...8..287L",
            "1999ASPC..172..291A",
            "1999ASPC..172..287L",
            "1988alds.proc..335H",
            "2000A&AS..143...61E"
          ],
          "source": 0,
          "target": 29,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 37,
          "weight": 14
        },
        {
          "overlap": [
            "2000A&AS..143...41K",
            "2000A&AS..143...85A"
          ],
          "source": 0,
          "target": 40,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 42,
          "weight": 10
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 44,
          "weight": 14
        },
        {
          "overlap": [
            "2000A&AS..143...41K",
            "1999ASPC..172..291A"
          ],
          "source": 0,
          "target": 46,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K",
            "2000A&AS..143...85A",
            "2000A&AS..143...61E"
          ],
          "source": 0,
          "target": 47,
          "weight": 27
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 48,
          "weight": 6
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 0,
          "target": 49,
          "weight": 8
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E"
          ],
          "source": 0,
          "target": 51,
          "weight": 21
        },
        {
          "overlap": [
            "1995ASPC...77...36A"
          ],
          "source": 1,
          "target": 29,
          "weight": 17
        },
        {
          "overlap": [
            "1995ASPC...77...36A"
          ],
          "source": 1,
          "target": 33,
          "weight": 98
        },
        {
          "overlap": [
            "2006JEPub...9....2H"
          ],
          "source": 2,
          "target": 45,
          "weight": 20
        },
        {
          "overlap": [
            "2011ASPC..442..415A"
          ],
          "source": 2,
          "target": 16,
          "weight": 15
        },
        {
          "overlap": [
            "2004ASPC..314..181A",
            "2004ASPC..314..605R"
          ],
          "source": 3,
          "target": 43,
          "weight": 57
        },
        {
          "overlap": [
            "2004ASPC..314..181A"
          ],
          "source": 3,
          "target": 17,
          "weight": 35
        },
        {
          "overlap": [
            "1996ASPC..101..569E"
          ],
          "source": 4,
          "target": 44,
          "weight": 26
        },
        {
          "overlap": [
            "1996ASPC..101..569E"
          ],
          "source": 4,
          "target": 32,
          "weight": 56
        },
        {
          "overlap": [
            "1996ASPC..101..569E"
          ],
          "source": 4,
          "target": 49,
          "weight": 22
        },
        {
          "overlap": [
            "1996ASPC..101..569E"
          ],
          "source": 4,
          "target": 24,
          "weight": 16
        },
        {
          "overlap": [
            "2009arXiv0906.2549P",
            "2003BAAS...35.1241K",
            "1994BAAS...26.1370A"
          ],
          "source": 5,
          "target": 28,
          "weight": 51
        },
        {
          "overlap": [
            "1994BAAS...26.1370A"
          ],
          "source": 5,
          "target": 12,
          "weight": 23
        },
        {
          "overlap": [
            "2003BAAS...35.1241K"
          ],
          "source": 5,
          "target": 13,
          "weight": 20
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 27,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 29,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 23,
          "weight": 87
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 15,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 7,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 37,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 10,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 12,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 40,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 14,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 44,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 46,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 47,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 48,
          "weight": 33
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 49,
          "weight": 20
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 19,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 22,
          "weight": 87
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 42,
          "weight": 50
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 24,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 6,
          "target": 25,
          "weight": 29
        },
        {
          "overlap": [
            "2002SPIE.4847..238K"
          ],
          "source": 7,
          "target": 8,
          "weight": 40
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 10,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 12,
          "weight": 15
        },
        {
          "overlap": [
            "2002SPIE.4847..238K"
          ],
          "source": 7,
          "target": 13,
          "weight": 14
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 14,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 15,
          "weight": 15
        },
        {
          "overlap": [
            "2007ASPC..377..106H"
          ],
          "source": 7,
          "target": 18,
          "weight": 28
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 19,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 22,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 23,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 24,
          "weight": 6
        },
        {
          "overlap": [
            "2002SPIE.4847..238K",
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 25,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 27,
          "weight": 10
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 29,
          "weight": 6
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 37,
          "weight": 9
        },
        {
          "overlap": [
            "2007ASPC..377..106H"
          ],
          "source": 7,
          "target": 38,
          "weight": 18
        },
        {
          "overlap": [
            "2002SPIE.4847..238K",
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 40,
          "weight": 38
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 42,
          "weight": 21
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 44,
          "weight": 10
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 46,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 47,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 48,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 7,
          "target": 49,
          "weight": 8
        },
        {
          "overlap": [
            "2002SPIE.4847..238K"
          ],
          "source": 8,
          "target": 13,
          "weight": 34
        },
        {
          "overlap": [
            "2002SPIE.4847..238K"
          ],
          "source": 8,
          "target": 40,
          "weight": 49
        },
        {
          "overlap": [
            "2002SPIE.4847..238K"
          ],
          "source": 8,
          "target": 25,
          "weight": 32
        },
        {
          "overlap": [
            "1997Ap&SS.247..189E"
          ],
          "source": 9,
          "target": 37,
          "weight": 13
        },
        {
          "overlap": [
            "1997Ap&SS.247..189E"
          ],
          "source": 9,
          "target": 24,
          "weight": 8
        },
        {
          "overlap": [
            "1997Ap&SS.247..189E"
          ],
          "source": 9,
          "target": 49,
          "weight": 11
        },
        {
          "overlap": [
            "1997Ap&SS.247..189E"
          ],
          "source": 9,
          "target": 32,
          "weight": 28
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 12,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...61E",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 14,
          "weight": 65
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 15,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 19,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "1992ASPC...25...47M",
            "2000A&AS..143...61E"
          ],
          "source": 10,
          "target": 20,
          "weight": 19
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 22,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 23,
          "weight": 31
        },
        {
          "overlap": [
            "1992ASPC...25...47M",
            "2000A&AS..143..111G",
            "2000A&AS..143...61E",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 24,
          "weight": 27
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143....9W",
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 25,
          "weight": 33
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 27,
          "weight": 9
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...61E",
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 29,
          "weight": 16
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...61E",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 37,
          "weight": 34
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 40,
          "weight": 48
        },
        {
          "overlap": [
            "2000A&AS..143....9W"
          ],
          "source": 10,
          "target": 41,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 42,
          "weight": 18
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
          "source": 10,
          "target": 44,
          "weight": 72
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 46,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...61E",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 47,
          "weight": 65
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 48,
          "weight": 12
        },
        {
          "overlap": [
            "1992ASPC...25...47M",
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 10,
          "target": 49,
          "weight": 29
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G"
          ],
          "source": 10,
          "target": 51,
          "weight": 57
        },
        {
          "overlap": [
            "2005JASIS..56..111K"
          ],
          "source": 11,
          "target": 27,
          "weight": 16
        },
        {
          "overlap": [
            "2005JASIS..56..111K"
          ],
          "source": 11,
          "target": 15,
          "weight": 23
        },
        {
          "overlap": [
            "2005JASIS..56..111K"
          ],
          "source": 11,
          "target": 45,
          "weight": 23
        },
        {
          "overlap": [
            "2005JASIS..56..111K"
          ],
          "source": 11,
          "target": 48,
          "weight": 21
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 14,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 15,
          "weight": 15
        },
        {
          "overlap": [
            "2005JASIS..56...36K",
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 19,
          "weight": 31
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 12,
          "target": 20,
          "weight": 5
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 22,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 23,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 24,
          "weight": 6
        },
        {
          "overlap": [
            "1992ald2.proc...85K",
            "1993ASPC...52..132K",
            "2000A&AS..143...41K",
            "2005JASIS..56...36K"
          ],
          "source": 12,
          "target": 25,
          "weight": 51
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2000A&AS..143...41K",
            "2005JASIS..56...36K"
          ],
          "source": 12,
          "target": 27,
          "weight": 33
        },
        {
          "overlap": [
            "1994BAAS...26.1370A"
          ],
          "source": 12,
          "target": 28,
          "weight": 12
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 29,
          "weight": 13
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 12,
          "target": 32,
          "weight": 22
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 12,
          "target": 35,
          "weight": 22
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 37,
          "weight": 19
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 40,
          "weight": 18
        },
        {
          "overlap": [
            "2005JASIS..56...36K",
            "1993ASPC...52..132K"
          ],
          "source": 12,
          "target": 41,
          "weight": 29
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 42,
          "weight": 21
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 44,
          "weight": 10
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 46,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 47,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...41K",
            "2005JASIS..56...36K"
          ],
          "source": 12,
          "target": 48,
          "weight": 28
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2000A&AS..143...41K"
          ],
          "source": 12,
          "target": 49,
          "weight": 17
        },
        {
          "overlap": [
            "1999ASPC..172..291A"
          ],
          "source": 13,
          "target": 27,
          "weight": 10
        },
        {
          "overlap": [
            "1999ASPC..172..291A"
          ],
          "source": 13,
          "target": 29,
          "weight": 6
        },
        {
          "overlap": [
            "2003BAAS...35.1241K"
          ],
          "source": 13,
          "target": 28,
          "weight": 10
        },
        {
          "overlap": [
            "2002SPIE.4847..238K"
          ],
          "source": 13,
          "target": 40,
          "weight": 17
        },
        {
          "overlap": [
            "2005IPM....41.1395K",
            "2006cs........8027H"
          ],
          "source": 13,
          "target": 15,
          "weight": 28
        },
        {
          "overlap": [
            "2005IPM....41.1395K",
            "1999ASPC..172..291A"
          ],
          "source": 13,
          "target": 46,
          "weight": 28
        },
        {
          "overlap": [
            "2005IPM....41.1395K"
          ],
          "source": 13,
          "target": 48,
          "weight": 13
        },
        {
          "overlap": [
            "1999ASPC..172..291A"
          ],
          "source": 13,
          "target": 21,
          "weight": 24
        },
        {
          "overlap": [
            "2002SPIE.4847..238K"
          ],
          "source": 13,
          "target": 25,
          "weight": 11
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 15,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 19,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...61E"
          ],
          "source": 14,
          "target": 20,
          "weight": 19
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 22,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 23,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K",
            "2000A&AS..143...61E"
          ],
          "source": 14,
          "target": 24,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 25,
          "weight": 30
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 27,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...41K",
            "2000A&AS..143...61E"
          ],
          "source": 14,
          "target": 29,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K",
            "2000A&AS..143...61E"
          ],
          "source": 14,
          "target": 37,
          "weight": 48
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 40,
          "weight": 69
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 42,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G",
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 44,
          "weight": 49
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 46,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K",
            "2000A&AS..143...61E"
          ],
          "source": 14,
          "target": 47,
          "weight": 92
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 48,
          "weight": 17
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 14,
          "target": 49,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...61E"
          ],
          "source": 14,
          "target": 51,
          "weight": 81
        },
        {
          "overlap": [
            "2003lisa.conf..223K",
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 19,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 22,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 23,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 24,
          "weight": 6
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 25,
          "weight": 12
        },
        {
          "overlap": [
            "2005JASIS..56..111K",
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 27,
          "weight": 22
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 29,
          "weight": 6
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 37,
          "weight": 9
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 40,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 42,
          "weight": 21
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 44,
          "weight": 10
        },
        {
          "overlap": [
            "2005JASIS..56..111K"
          ],
          "source": 15,
          "target": 45,
          "weight": 16
        },
        {
          "overlap": [
            "2006cs........4061H",
            "2005IPM....41.1395K",
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 46,
          "weight": 47
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 47,
          "weight": 18
        },
        {
          "overlap": [
            "2005JASIS..56..111K",
            "2005IPM....41.1395K",
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 48,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 15,
          "target": 49,
          "weight": 8
        },
        {
          "overlap": [
            "2004BAAS...36..805E"
          ],
          "source": 16,
          "target": 43,
          "weight": 12
        },
        {
          "overlap": [
            "2004ASPC..314..181A"
          ],
          "source": 17,
          "target": 43,
          "weight": 20
        },
        {
          "overlap": [
            "2007ASPC..377..106H"
          ],
          "source": 18,
          "target": 38,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 22,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 23,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 24,
          "weight": 6
        },
        {
          "overlap": [
            "2005JASIS..56...36K",
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 25,
          "weight": 25
        },
        {
          "overlap": [
            "2005JASIS..56...36K",
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 27,
          "weight": 22
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 29,
          "weight": 6
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 37,
          "weight": 9
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 40,
          "weight": 18
        },
        {
          "overlap": [
            "2005JASIS..56...36K"
          ],
          "source": 19,
          "target": 41,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 42,
          "weight": 21
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 44,
          "weight": 10
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 46,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 47,
          "weight": 18
        },
        {
          "overlap": [
            "2005JASIS..56...36K",
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 48,
          "weight": 28
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 19,
          "target": 49,
          "weight": 8
        },
        {
          "overlap": [
            "1995VA.....39R.272S"
          ],
          "source": 20,
          "target": 28,
          "weight": 4
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
          "source": 20,
          "target": 29,
          "weight": 20
        },
        {
          "overlap": [
            "2000A&AS..143....1G",
            "1993ASPC...52..132K"
          ],
          "source": 20,
          "target": 27,
          "weight": 8
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 20,
          "target": 32,
          "weight": 8
        },
        {
          "overlap": [
            "1992PASAu..10..134S"
          ],
          "source": 20,
          "target": 34,
          "weight": 10
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 20,
          "target": 35,
          "weight": 8
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G",
            "1993ASPC...52..132K"
          ],
          "source": 20,
          "target": 37,
          "weight": 13
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 20,
          "target": 41,
          "weight": 5
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A"
          ],
          "source": 20,
          "target": 40,
          "weight": 13
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
          "source": 20,
          "target": 44,
          "weight": 21
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...61E"
          ],
          "source": 20,
          "target": 47,
          "weight": 19
        },
        {
          "overlap": [
            "1992ASPC...25...47M",
            "2000A&AS..143..111G",
            "1993ASPC...52..132K",
            "2000A&AS..143...85A"
          ],
          "source": 20,
          "target": 49,
          "weight": 12
        },
        {
          "overlap": [
            "1992PASAu..10..134S"
          ],
          "source": 20,
          "target": 50,
          "weight": 8
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G"
          ],
          "source": 20,
          "target": 51,
          "weight": 23
        },
        {
          "overlap": [
            "1999AAS...195.8209D"
          ],
          "source": 20,
          "target": 42,
          "weight": 8
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "1994PhDT........54B",
            "1971BICDS...1....2J",
            "1983ApJ...267..465D",
            "1995AAS...187.3801B",
            "1997AAS...191.3502E",
            "1999AAS...195.8209D",
            "1988ARA&A..26..245R",
            "2000A&AS..143...85A",
            "1997ApJ...491..421E",
            "1994AAS...185.4104E",
            "1973BICDS...4...27J",
            "1992ASPC...25...47M",
            "1994AAS...184.2802G",
            "1988alds.proc..335H",
            "1986ApJ...304...15B",
            "2000A&AS..143...61E"
          ],
          "source": 20,
          "target": 24,
          "weight": 38
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "1993ASPC...52..132K",
            "1996ASPC..101..581P"
          ],
          "source": 20,
          "target": 25,
          "weight": 13
        },
        {
          "overlap": [
            "1999ASPC..172..291A"
          ],
          "source": 21,
          "target": 27,
          "weight": 20
        },
        {
          "overlap": [
            "1999ASPC..172..291A"
          ],
          "source": 21,
          "target": 29,
          "weight": 12
        },
        {
          "overlap": [
            "1999ASPC..172..291A"
          ],
          "source": 21,
          "target": 46,
          "weight": 28
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 27,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 29,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 44,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 37,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 40,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 42,
          "weight": 50
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 46,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 47,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 48,
          "weight": 33
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 49,
          "weight": 20
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 23,
          "weight": 87
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 24,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 22,
          "target": 25,
          "weight": 29
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 27,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 29,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 44,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 37,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 42,
          "weight": 50
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 46,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 47,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 48,
          "weight": 33
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 49,
          "weight": 20
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 40,
          "weight": 44
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 24,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 23,
          "target": 25,
          "weight": 29
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...41K"
          ],
          "source": 24,
          "target": 25,
          "weight": 10
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 24,
          "target": 27,
          "weight": 4
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "1996prpe.book.....W",
            "2000A&AS..143...41K",
            "1988alds.proc..335H",
            "2000A&AS..143...61E"
          ],
          "source": 24,
          "target": 29,
          "weight": 13
        },
        {
          "overlap": [
            "1996adass...5..569E"
          ],
          "source": 24,
          "target": 30,
          "weight": 12
        },
        {
          "overlap": [
            "1997Ap&SS.247..189E",
            "1996ASPC..101..569E"
          ],
          "source": 24,
          "target": 32,
          "weight": 19
        },
        {
          "overlap": [
            "1995ASPC...77...28E"
          ],
          "source": 24,
          "target": 35,
          "weight": 9
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "1995VA.....39..161C",
            "1994ExA.....5..205E",
            "1995ASPC...77...28E",
            "1995VA.....39..217E",
            "1997Ap&SS.247..189E",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G",
            "2000A&AS..143...41K",
            "1992ald2.proc..387M"
          ],
          "source": 24,
          "target": 37,
          "weight": 41
        },
        {
          "overlap": [
            "1994ExA.....5..205E"
          ],
          "source": 24,
          "target": 39,
          "weight": 12
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 24,
          "target": 40,
          "weight": 23
        },
        {
          "overlap": [
            "1999AAS...195.8209D",
            "2000A&AS..143...41K"
          ],
          "source": 24,
          "target": 42,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "1994AAS...185.4104E",
            "1995VA.....39..217E",
            "1995ASPC...77...28E",
            "1996ASPC..101..569E",
            "2000A&AS..143...61E",
            "1994AAS...184.2802G",
            "2000A&AS..143..111G",
            "2000A&AS..143...41K",
            "1992ASPC...25...47M",
            "1992ald2.proc..387M"
          ],
          "source": 24,
          "target": 44,
          "weight": 47
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 24,
          "target": 46,
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
          "target": 47,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 24,
          "target": 48,
          "weight": 6
        },
        {
          "overlap": [
            "1992ASPC...25...47M",
            "2000A&AS..143..111G",
            "1994ExA.....5..205E",
            "1995ASSL..203..259S",
            "1980CeMec..22...63M",
            "1995VA.....39..217E",
            "2000A&AS..143...85A",
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
          "source": 24,
          "target": 49,
          "weight": 58
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...61E"
          ],
          "source": 24,
          "target": 51,
          "weight": 27
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2005JASIS..56...36K",
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 27,
          "weight": 27
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "1993ASPC...52..132K",
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 29,
          "weight": 15
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 25,
          "target": 32,
          "weight": 18
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 25,
          "target": 35,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "1993ASPC...52..132K",
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 37,
          "weight": 24
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2002SPIE.4847..238K",
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 40,
          "weight": 46
        },
        {
          "overlap": [
            "2005JASIS..56...36K",
            "2000A&AS..143....9W",
            "1993ASPC...52..132K"
          ],
          "source": 25,
          "target": 41,
          "weight": 36
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 42,
          "weight": 17
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...41K",
            "2000A&AS..143....9W"
          ],
          "source": 25,
          "target": 44,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 46,
          "weight": 12
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 47,
          "weight": 30
        },
        {
          "overlap": [
            "2005JASIS..56...36K",
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 48,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "1993ASPC...52..132K",
            "2000A&AS..143...41K"
          ],
          "source": 25,
          "target": 49,
          "weight": 20
        },
        {
          "overlap": [
            "2000A&AS..143..111G"
          ],
          "source": 25,
          "target": 51,
          "weight": 18
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2000A&AS..143...41K",
            "1999ASPC..172..291A"
          ],
          "source": 27,
          "target": 29,
          "weight": 14
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 27,
          "target": 32,
          "weight": 16
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 27,
          "target": 35,
          "weight": 16
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2000A&AS..143...41K"
          ],
          "source": 27,
          "target": 37,
          "weight": 14
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 27,
          "target": 40,
          "weight": 13
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2005JASIS..56...36K"
          ],
          "source": 27,
          "target": 41,
          "weight": 21
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 27,
          "target": 42,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 27,
          "target": 44,
          "weight": 7
        },
        {
          "overlap": [
            "2005JASIS..56..111K"
          ],
          "source": 27,
          "target": 45,
          "weight": 11
        },
        {
          "overlap": [
            "1999ASPC..172..291A",
            "2000A&AS..143...41K"
          ],
          "source": 27,
          "target": 46,
          "weight": 22
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 27,
          "target": 47,
          "weight": 13
        },
        {
          "overlap": [
            "2005JASIS..56..111K",
            "2005JASIS..56...36K",
            "2000A&AS..143...41K"
          ],
          "source": 27,
          "target": 48,
          "weight": 31
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "2000A&AS..143...41K"
          ],
          "source": 27,
          "target": 49,
          "weight": 12
        },
        {
          "overlap": [
            "1995VA.....39R.272S"
          ],
          "source": 28,
          "target": 29,
          "weight": 5
        },
        {
          "overlap": [
            "2009arXiv0912.5235K"
          ],
          "source": 28,
          "target": 38,
          "weight": 13
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 29,
          "target": 32,
          "weight": 9
        },
        {
          "overlap": [
            "1995ASPC...77...36A"
          ],
          "source": 29,
          "target": 33,
          "weight": 17
        },
        {
          "overlap": [
            "1992PASAu..10..134S"
          ],
          "source": 29,
          "target": 34,
          "weight": 12
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 29,
          "target": 35,
          "weight": 9
        },
        {
          "overlap": [
            "1998ASPC..145..466F"
          ],
          "source": 29,
          "target": 36,
          "weight": 17
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "1993ASPC...52..132K",
            "2000A&AS..143...41K",
            "2000A&AS..143...61E"
          ],
          "source": 29,
          "target": 37,
          "weight": 16
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...41K"
          ],
          "source": 29,
          "target": 40,
          "weight": 15
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 29,
          "target": 41,
          "weight": 6
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 29,
          "target": 42,
          "weight": 9
        },
        {
          "overlap": [
            "2000A&AS..143...61E",
            "2000A&AS..143..111G",
            "2000A&AS..143...41K"
          ],
          "source": 29,
          "target": 44,
          "weight": 12
        },
        {
          "overlap": [
            "1999ASPC..172..291A",
            "2000A&AS..143...41K"
          ],
          "source": 29,
          "target": 46,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...41K",
            "2000A&AS..143...61E"
          ],
          "source": 29,
          "target": 47,
          "weight": 23
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 29,
          "target": 48,
          "weight": 6
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "1993ASPC...52..132K",
            "1993adass...2..132K",
            "2000A&AS..143...41K"
          ],
          "source": 29,
          "target": 49,
          "weight": 14
        },
        {
          "overlap": [
            "1992PASAu..10..134S"
          ],
          "source": 29,
          "target": 50,
          "weight": 10
        },
        {
          "overlap": [
            "2000A&AS..143...61E",
            "2000A&AS..143..111G"
          ],
          "source": 29,
          "target": 51,
          "weight": 18
        },
        {
          "overlap": [
            "1996adass...5..569E"
          ],
          "source": 30,
          "target": 49,
          "weight": 16
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 32,
          "target": 35,
          "weight": 31
        },
        {
          "overlap": [
            "1997Ap&SS.247..189E",
            "1993ASPC...52..132K"
          ],
          "source": 32,
          "target": 37,
          "weight": 29
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 32,
          "target": 41,
          "weight": 21
        },
        {
          "overlap": [
            "1996ASPC..101..569E"
          ],
          "source": 32,
          "target": 44,
          "weight": 15
        },
        {
          "overlap": [
            "1997Ap&SS.247..189E",
            "1993ASPC...52..132K",
            "1996ASPC..101..569E"
          ],
          "source": 32,
          "target": 49,
          "weight": 37
        },
        {
          "overlap": [
            "2009ASPC..411..179G",
            "1992PASAu..10..134S"
          ],
          "source": 34,
          "target": 50,
          "weight": 80
        },
        {
          "overlap": [
            "1995ASPC...77...28E",
            "1993ASPC...52..132K"
          ],
          "source": 35,
          "target": 37,
          "weight": 29
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 35,
          "target": 41,
          "weight": 21
        },
        {
          "overlap": [
            "1995ASPC...77...28E"
          ],
          "source": 35,
          "target": 44,
          "weight": 15
        },
        {
          "overlap": [
            "1993ASPC...52..132K",
            "1995ASPC...77...28E"
          ],
          "source": 35,
          "target": 49,
          "weight": 25
        },
        {
          "overlap": [
            "1994ExA.....5..205E"
          ],
          "source": 37,
          "target": 39,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143..111G",
            "2000A&AS..143...41K"
          ],
          "source": 37,
          "target": 40,
          "weight": 35
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 37,
          "target": 41,
          "weight": 9
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 37,
          "target": 42,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "1995ASPC...77...28E",
            "1995VA.....39..217E",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G",
            "2000A&AS..143...41K",
            "1992ald2.proc..387M"
          ],
          "source": 37,
          "target": 44,
          "weight": 46
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 37,
          "target": 46,
          "weight": 9
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K",
            "2000A&AS..143...61E"
          ],
          "source": 37,
          "target": 47,
          "weight": 48
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 37,
          "target": 48,
          "weight": 9
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "1994ExA.....5..205E",
            "1995ASPC...77...28E",
            "1995VA.....39..217E",
            "1997Ap&SS.247..189E",
            "2000A&AS..143..111G",
            "1993ASPC...52..132K",
            "2000A&AS..143...41K",
            "1992ald2.proc..387M"
          ],
          "source": 37,
          "target": 49,
          "weight": 49
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G"
          ],
          "source": 37,
          "target": 51,
          "weight": 42
        },
        {
          "overlap": [
            "1993ASSL..182...21K"
          ],
          "source": 38,
          "target": 41,
          "weight": 17
        },
        {
          "overlap": [
            "1994ExA.....5..205E"
          ],
          "source": 39,
          "target": 49,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 40,
          "target": 42,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143..111G",
            "2000A&AS..143...41K"
          ],
          "source": 40,
          "target": 44,
          "weight": 37
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 40,
          "target": 46,
          "weight": 18
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 40,
          "target": 47,
          "weight": 69
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 40,
          "target": 48,
          "weight": 17
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 40,
          "target": 49,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A"
          ],
          "source": 40,
          "target": 51,
          "weight": 54
        },
        {
          "overlap": [
            "2000A&AS..143....9W"
          ],
          "source": 41,
          "target": 44,
          "weight": 10
        },
        {
          "overlap": [
            "2005JASIS..56...36K"
          ],
          "source": 41,
          "target": 48,
          "weight": 14
        },
        {
          "overlap": [
            "1993ASPC...52..132K"
          ],
          "source": 41,
          "target": 49,
          "weight": 8
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 42,
          "target": 44,
          "weight": 13
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 42,
          "target": 46,
          "weight": 21
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 42,
          "target": 47,
          "weight": 25
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 42,
          "target": 48,
          "weight": 19
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 42,
          "target": 49,
          "weight": 11
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 44,
          "target": 46,
          "weight": 10
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K",
            "2000A&AS..143...61E"
          ],
          "source": 44,
          "target": 47,
          "weight": 49
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 44,
          "target": 48,
          "weight": 9
        },
        {
          "overlap": [
            "1992ASPC...25...47M",
            "2000A&AS..143..111G",
            "1995VA.....39..217E",
            "2000A&AS..143...85A",
            "1995ASPC...77...28E",
            "2000A&AS..143...41K",
            "1996ASPC..101..569E",
            "1992ald2.proc..387M"
          ],
          "source": 44,
          "target": 49,
          "weight": 45
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G"
          ],
          "source": 44,
          "target": 51,
          "weight": 43
        },
        {
          "overlap": [
            "2005JASIS..56..111K"
          ],
          "source": 45,
          "target": 48,
          "weight": 15
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 46,
          "target": 47,
          "weight": 18
        },
        {
          "overlap": [
            "2005IPM....41.1395K",
            "2000A&AS..143...41K"
          ],
          "source": 46,
          "target": 48,
          "weight": 29
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 46,
          "target": 49,
          "weight": 8
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 47,
          "target": 48,
          "weight": 17
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A",
            "2000A&AS..143...41K"
          ],
          "source": 47,
          "target": 49,
          "weight": 31
        },
        {
          "overlap": [
            "2000A&AS..143...85A",
            "2000A&AS..143...61E",
            "2000A&AS..143..111G"
          ],
          "source": 47,
          "target": 51,
          "weight": 81
        },
        {
          "overlap": [
            "2000A&AS..143...41K"
          ],
          "source": 48,
          "target": 49,
          "weight": 7
        },
        {
          "overlap": [
            "2000A&AS..143..111G",
            "2000A&AS..143...85A"
          ],
          "source": 49,
          "target": 51,
          "weight": 24
        }
      ],
        "multigraph": false,
        "nodes": [
        {
          "citation_count": 20,
          "first_author": "Grant, Carolyn S.",
          "group": 0,
          "id": 0,
          "nodeName": "2000A&AS..143..111G",
          "nodeWeight": 20,
          "title": "The NASA Astrophysics Data System: Data holdings"
        },
        {
          "citation_count": 3,
          "first_author": "Accomazzi, A.",
          "group": 1,
          "id": 1,
          "nodeName": "1997ASPC..125..357A",
          "nodeWeight": 3,
          "title": "Astronomical Information Discovery and Access: Design and Implementation of the ADS Bibliographic Services"
        },
        {
          "citation_count": 0,
          "first_author": "Henneken, E. A.",
          "group": 2,
          "id": 2,
          "nodeName": "2012ASPC..461..763H",
          "nodeWeight": 0,
          "title": "Linking to Data: Effect on Citation Rates in Astronomy"
        },
        {
          "citation_count": 1,
          "first_author": "Accomazzi, A.",
          "group": 3,
          "id": 3,
          "nodeName": "2007ASPC..376..467A",
          "nodeWeight": 1,
          "title": "Closing the Loop: Linking Datasets to Publications and Back"
        },
        {
          "citation_count": 4,
          "first_author": "Accomazzi, A.",
          "group": 4,
          "id": 4,
          "nodeName": "1996ASPC..101..558A",
          "nodeWeight": 4,
          "title": "The ADS Article Service Data Holdings and Access Methods"
        },
        {
          "citation_count": 1,
          "first_author": "Accomazzi, A.",
          "group": 5,
          "id": 5,
          "nodeName": "2009arad.workE..32A",
          "nodeWeight": 1,
          "title": "Towards a Resource-Centric Data Network for Astronomy"
        },
        {
          "citation_count": 0,
          "first_author": "Eichhorn, G.",
          "group": 2,
          "id": 6,
          "nodeName": "2002LPI....33.1298E",
          "nodeWeight": 0,
          "title": "New Data and Search Features in the NASA ADS Abstract Service"
        },
        {
          "citation_count": 1,
          "first_author": "Henneken, E. A.",
          "group": 2,
          "id": 7,
          "nodeName": "2009ASPC..411..384H",
          "nodeWeight": 1,
          "title": "Exploring the Astronomy Literature Landscape"
        },
        {
          "citation_count": 0,
          "first_author": "Henneken, E. A.",
          "group": 2,
          "id": 8,
          "nodeName": "2012LPI....43.1022H",
          "nodeWeight": 0,
          "title": "Online Discovery: Search Paradigms and the Art of Literature Exploration"
        },
        {
          "citation_count": 1,
          "first_author": "Eichhorn, Guenther",
          "group": 4,
          "id": 10,
          "nodeName": "1998ASPC..153..277E",
          "nodeWeight": 1,
          "title": "The Astrophysics Data System"
        },
        {
          "citation_count": 1,
          "first_author": "Eichhorn, G.",
          "group": 0,
          "id": 11,
          "nodeName": "2007ASPC..377...36E",
          "nodeWeight": 1,
          "title": "Connectivity in the Astronomy Digital Library"
        },
        {
          "citation_count": 0,
          "first_author": "Henneken, Edwin A.",
          "group": 2,
          "id": 12,
          "nodeName": "2014arXiv1406.4542H",
          "nodeWeight": 0,
          "title": "Computing and Using Metrics in the ADS"
        },
        {
          "citation_count": 1,
          "first_author": "Kurtz, Michael",
          "group": 2,
          "id": 13,
          "nodeName": "2009astro2010P..28K",
          "nodeWeight": 1,
          "title": "The Smithsonian/NASA Astrophysics Data System (ADS) Decennial Report"
        },
        {
          "citation_count": 2,
          "first_author": "Accomazzi, A.",
          "group": 2,
          "id": 15,
          "nodeName": "2007ASPC..377...69A",
          "nodeWeight": 2,
          "title": "Creation and Use of Citations in the ADS"
        },
        {
          "citation_count": 0,
          "first_author": "Eichhorn, G.",
          "group": 0,
          "id": 16,
          "nodeName": "2007ASPC..377...93E",
          "nodeWeight": 0,
          "title": "Full Text Searching in the Astrophysics Data System"
        },
        {
          "citation_count": 4,
          "first_author": "Henneken, Edwin A.",
          "group": 2,
          "id": 17,
          "nodeName": "2007LePub..20...16H",
          "nodeWeight": 4,
          "title": "E-prints and journal articles in astronomy: a productive co-existence"
        },
        {
          "citation_count": 1,
          "first_author": "Accomazzi, Alberto",
          "group": 3,
          "id": 18,
          "nodeName": "2012SPIE.8448E..0KA",
          "nodeWeight": 1,
          "title": "Telescope bibliographies: an essential component of archival data management and operations"
        },
        {
          "citation_count": 3,
          "first_author": "Delmotte, Nausicaa",
          "group": 3,
          "id": 19,
          "nodeName": "2005Msngr.119...50D",
          "nodeWeight": 3,
          "title": "The ESO Telescope Bibliography Web Interface - Linking Publications and Observations"
        },
        {
          "citation_count": 0,
          "first_author": "Henneken, E. A.",
          "group": 5,
          "id": 22,
          "nodeName": "2009LPI....40.1873H",
          "nodeWeight": 0,
          "title": "The SAO/NASA Astrophysics Data System: A Gateway to the Planetary Sciences Literature"
        },
        {
          "citation_count": 5,
          "first_author": "Henneken, Edwin A.",
          "group": 2,
          "id": 23,
          "nodeName": "2009JInfo...3....1H",
          "nodeWeight": 5,
          "title": "Use of astronomical literature - A report on usage patterns"
        },
        {
          "citation_count": 60,
          "first_author": "Kurtz, Michael J.",
          "group": 0,
          "id": 24,
          "nodeName": "2000A&AS..143...41K",
          "nodeWeight": 60,
          "title": "The NASA Astrophysics Data System: Overview"
        },
        {
          "citation_count": 8,
          "first_author": "Henneken, Edwin A.",
          "group": 2,
          "id": 25,
          "nodeName": "2006JEPub...9....2H",
          "nodeWeight": 8,
          "title": "Effect of E-printing on Citation Rates in Astronomy and Physics"
        },
        {
          "citation_count": 0,
          "first_author": "Eichhorn, G.",
          "group": 2,
          "id": 26,
          "nodeName": "2003LPI....34.1949E",
          "nodeWeight": 0,
          "title": "Expanded Citations Database in the NASA ADS Abstract Service"
        },
        {
          "citation_count": 3,
          "first_author": "Kurtz, Michael J.",
          "group": 2,
          "id": 27,
          "nodeName": "2003lisa.conf..223K",
          "nodeWeight": 3,
          "title": "The NASA Astrophysics Data System: Obsolescence of Reads and Cites"
        },
        {
          "citation_count": 3,
          "first_author": "Eichhorn, Guenther",
          "group": 4,
          "id": 29,
          "nodeName": "2002Ap&SS.282..299E",
          "nodeWeight": 3,
          "title": "The NASA Astrophysics Data System: Free Access to the Astronomical Literature On-line and through Email"
        },
        {
          "citation_count": 1,
          "first_author": "Kurtz, M. J.",
          "group": 2,
          "id": 30,
          "nodeName": "2006ASPC..351..653K",
          "nodeWeight": 1,
          "title": "Intelligent Information Retrieval"
        },
        {
          "citation_count": 3,
          "first_author": "Accomazzi, A.",
          "group": 11,
          "id": 31,
          "nodeName": "2011ASPC..442..415A",
          "nodeWeight": 3,
          "title": "Semantic Interlinking of Resources in the Virtual Observatory Era"
        },
        {
          "citation_count": 14,
          "first_author": "Kurtz, Michael J.",
          "group": 2,
          "id": 32,
          "nodeName": "2005IPM....41.1395K",
          "nodeWeight": 14,
          "title": "The Effect of Use and Access on Citations"
        },
        {
          "citation_count": 2,
          "first_author": "Accomazzi, A.",
          "group": 5,
          "id": 33,
          "nodeName": "2010ASPC..433..273A",
          "nodeWeight": 2,
          "title": "Astronomy 3.0 Style"
        },
        {
          "citation_count": 20,
          "first_author": "Accomazzi, Alberto",
          "group": 1,
          "id": 34,
          "nodeName": "2000A&AS..143...85A",
          "nodeWeight": 20,
          "title": "The NASA Astrophysics Data System: Architecture"
        },
        {
          "citation_count": 0,
          "first_author": "Accomazzi, A.",
          "group": 4,
          "id": 35,
          "nodeName": "2003ASPC..295..309A",
          "nodeWeight": 0,
          "title": "ADS Web Services for the Discovery and Linking of Bibliographic Records"
        },
        {
          "citation_count": 6,
          "first_author": "Accomazzi, Alberto",
          "group": 12,
          "id": 36,
          "nodeName": "1999ASPC..172..291A",
          "nodeWeight": 6,
          "title": "The ADS Bibliographic Reference Resolver"
        },
        {
          "citation_count": 5,
          "first_author": "Eichhorn, G.",
          "group": 4,
          "id": 37,
          "nodeName": "1998ASPC..145..378E",
          "nodeWeight": 5,
          "title": "New Capabilities of the ADS Abstract and Article Service"
        },
        {
          "citation_count": 11,
          "first_author": "Eichhorn, G.",
          "group": 1,
          "id": 38,
          "nodeName": "1995ASPC...77...28E",
          "nodeWeight": 11,
          "title": "The New Astrophysics Data System"
        },
        {
          "citation_count": 0,
          "first_author": "Accomazzi, Alberto",
          "group": 1,
          "id": 39,
          "nodeName": "2014arXiv1403.6656A",
          "nodeWeight": 0,
          "title": "The Unified Astronomy Thesaurus"
        },
        {
          "citation_count": 8,
          "first_author": "Eichhorn, G.",
          "group": 4,
          "id": 40,
          "nodeName": "1996ASPC..101..569E",
          "nodeWeight": 8,
          "title": "Various Access Methods to the Abstracts in the Astrophysics Data System"
        },
        {
          "citation_count": 0,
          "first_author": "Accomazzi, A.",
          "group": 1,
          "id": 41,
          "nodeName": "1998ASPC..145..395A",
          "nodeWeight": 0,
          "title": "Mirroring the ADS Bibliographic Databases"
        },
        {
          "citation_count": 1,
          "first_author": "Eichhorn, Guenther",
          "group": 4,
          "id": 42,
          "nodeName": "2003lisa.conf..145E",
          "nodeWeight": 1,
          "title": "Current and Future Holdings of the Historical Literature in the ADS"
        },
        {
          "citation_count": 4,
          "first_author": "Henneken, Edwin A.",
          "group": 5,
          "id": 43,
          "nodeName": "2011ApSSP...1..125H",
          "nodeWeight": 4,
          "title": "Finding Your Literature Match - A Recommender System"
        },
        {
          "citation_count": 6,
          "first_author": "Eichhorn, G.",
          "group": 4,
          "id": 44,
          "nodeName": "1995VA.....39..217E",
          "nodeWeight": 6,
          "title": "Access to the Astrophysics Science Information and Abstract System"
        },
        {
          "citation_count": 0,
          "first_author": "Accomazzi, A.",
          "group": 0,
          "id": 45,
          "nodeName": "2006ASPC..351..715A",
          "nodeWeight": 0,
          "title": "Bibliographic Classification using the ADS Databases"
        },
        {
          "citation_count": 3,
          "first_author": "Kurtz, M. J.",
          "group": 4,
          "id": 46,
          "nodeName": "2010ASPC..434..155K",
          "nodeWeight": 3,
          "title": "Using Multipartite Graphs for Recommendation and Discovery"
        },
        {
          "citation_count": 0,
          "first_author": "Rey-Bakaikoa, V.",
          "group": 2,
          "id": 47,
          "nodeName": "2002ASPC..281..471R",
          "nodeWeight": 0,
          "title": "Managing the ADS Citation Database"
        },
        {
          "citation_count": 3,
          "first_author": "Accomazzi, Alberto",
          "group": 3,
          "id": 48,
          "nodeName": "2011ApSSP...1..135A",
          "nodeWeight": 3,
          "title": "Linking Literature and Data: Status Report and Future Efforts"
        },
        {
          "citation_count": 0,
          "first_author": "Eichhorn, Guenther",
          "group": 0,
          "id": 49,
          "nodeName": "2007BASI...35..717E",
          "nodeWeight": 0,
          "title": "Access to the literature and connection to on-line data"
        },
        {
          "citation_count": 0,
          "first_author": "Henneken, Edwin A.",
          "group": 2,
          "id": 50,
          "nodeName": "2006AAS...20917302H",
          "nodeWeight": 0,
          "title": "Finding Astronomical Communities Through Co-readership Analysis"
        },
        {
          "citation_count": 6,
          "first_author": "Henneken, E.",
          "group": 2,
          "id": 51,
          "nodeName": "2007ASPC..377..106H",
          "nodeWeight": 6,
          "title": "myADS-arXiv -- a Tailor-made, Open Access, Virtual Journal"
        },
        {
          "citation_count": 0,
          "first_author": "Eichhorn, Guenther",
          "group": 0,
          "id": 52,
          "nodeName": "2004tivo.conf..267E",
          "nodeWeight": 0,
          "title": "The Astronomy Digital Library and the VO"
        },
        {
          "citation_count": 3,
          "first_author": "Henneken, Edwin A.",
          "group": 2,
          "id": 53,
          "nodeName": "2012opsa.book..253H",
          "nodeWeight": 3,
          "title": "The ADS in the Information Age - Impact on Discovery"
        },
        {
          "citation_count": 17,
          "first_author": "Eichhorn, Guenther",
          "group": 4,
          "id": 54,
          "nodeName": "2000A&AS..143...61E",
          "nodeWeight": 17,
          "title": "The NASA Astrophysics Data System: The search engine and its user interface"
        },
        {
          "citation_count": 0,
          "first_author": "Accomazzi, A.",
          "group": 1,
          "id": 56,
          "nodeName": "2014ASPC..485..461A",
          "nodeWeight": 0,
          "title": "The Unified Astronomy Thesaurus"
        },
        {
          "citation_count": 0,
          "first_author": "Eichhorn, G.",
          "group": 0,
          "id": 57,
          "nodeName": "2005LPI....36.1207E",
          "nodeWeight": 0,
          "title": "New Features in the ADS Abstract Service"
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
          "weight": 170
        },
        {
          "source": 0,
          "target": 2,
          "weight": 1630
        },
        {
          "source": 0,
          "target": 4,
          "weight": 827
        },
        {
          "source": 0,
          "target": 5,
          "weight": 10
        },
        {
          "source": 1,
          "target": 2,
          "weight": 151
        },
        {
          "source": 1,
          "target": 4,
          "weight": 67
        },
        {
          "source": 1,
          "target": 5,
          "weight": 5
        },
        {
          "source": 2,
          "target": 3,
          "weight": 15
        },
        {
          "source": 2,
          "target": 4,
          "weight": 683
        },
        {
          "source": 2,
          "target": 5,
          "weight": 111
        },
        {
          "source": 4,
          "target": 5,
          "weight": 17
        }
      ],
        "multigraph": false,
        "nodes": [
        {
          "id": 0,
          "nodeName": {
            "classification": 2.6390573296152584,
            "data": 2.6509965136742353,
            "digital": 5.278114659230517,
            "library": 5.278114659230517,
            "overview": 2.6390573296152584,
            "searching": 2.6390573296152584
          },
          "paperCount": 8,
          "size": 81
        },
        {
          "id": 1,
          "nodeName": {
            "architecture": 2.6390573296152584,
            "design": 2.6390573296152584,
            "implementation": 2.6390573296152584,
            "mirroring": 2.6390573296152584,
            "thesaurus": 5.278114659230517,
            "unified": 5.278114659230517
          },
          "paperCount": 6,
          "size": 34
        },
        {
          "id": 2,
          "nodeName": {
            "ads": 7.207335920268107,
            "citation": 15.83434397769155,
            "effect": 7.917171988845775,
            "rates": 5.278114659230517,
            "report": 3.8918202981106265,
            "use": 7.917171988845775
          },
          "paperCount": 18,
          "size": 48
        },
        {
          "id": 3,
          "nodeName": {
            "back": 2.6390573296152584,
            "linking": 4.621335122841447,
            "management": 2.6390573296152584,
            "operations": 2.6390573296152584,
            "publications": 5.278114659230517,
            "telescope": 5.278114659230517
          },
          "paperCount": 4,
          "size": 8
        },
        {
          "id": 4,
          "nodeName": {
            "abstract": 4.621335122841447,
            "article": 5.278114659230517,
            "astrophysics": 4.236489301936018,
            "methods": 5.278114659230517,
            "service": 4.621335122841447,
            "system": 4.236489301936018
          },
          "paperCount": 10,
          "size": 48
        },
        {
          "id": 5,
          "nodeName": {
            "30": 2.6390573296152584,
            "network": 2.6390573296152584,
            "planetary": 2.6390573296152584,
            "recommender": 2.6390573296152584,
            "style": 2.6390573296152584,
            "your": 2.6390573296152584
          },
          "paperCount": 4,
          "size": 7
        },
        {
          "id": 11,
          "nodeName": {
            "era": 2.6390573296152584,
            "interlinking": 2.6390573296152584,
            "observatory": 2.6390573296152584,
            "resources": 2.6390573296152584,
            "semantic": 2.6390573296152584,
            "virtual": 1.9459101490553132
          },
          "paperCount": 1,
          "size": 3
        },
        {
          "id": 12,
          "nodeName": {
            "ads": 1.0296194171811581,
            "bibliographic": 1.252762968495368,
            "reference": 2.6390573296152584,
            "resolver": 2.6390573296152584
          },
          "paperCount": 1,
          "size": 6
        }
      ]
    }
    }

    beforeEach(function(){

      PaperNetwork.processResponse(new JsonResponse(testData));

      $("#test").append(PaperNetwork.view.$el)


    })


    afterEach(function(){

      $("#test").empty();


    })


    it("should render small word clouds for the summary nodes", function(){

//      expect(d3.select(".summary-node-group").text()).to.eql("classificationdatadigitallibraryoverviewsearching")
    })




  })




})