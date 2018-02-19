define([], function() {
  // Solr response for top level SIMBAD object facet
  //from query:
  // curl 'https://api.adsabs.harvard.edu/v1/search/query?__original_query=object%3ALMC&__original_query=simbid%3A3133169&__original_query=simbid%3A3133169&__original_url=q%3Dobject%253ALMC%26sort%3Ddate%2520desc%252C%2520bibcode%2520desc&facet=true&facet.field=ned_object_facet_hier&facet.limit=20&facet.mincount=1&facet.offset=0&facet.prefix=0%2F&fl=id&q=simbid%3A3133169&sort=date%20desc%2C%20bibcode%20desc' -H 'authorization: Bearer:OwSW0a6gx7jaZm7RpwUHfZMSgFQAxeMQrGK26Veu' -H 'origin: http://localhost:8000' -H 'accept-language: en-US,en;q=0.9' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36' -H 'content-type: application/x-www-form-urlencoded' -H 'accept: application/json, text/javascript, */*; q=0.01' -H 'referer: http://localhost:8000/' -H 'authority: api.adsabs.harvard.edu' --compressed

  var solrResponse_top = {
    "facet_counts": {
        "facet_fields": {
            "ned_object_facet_hier": [
                "0/galaxy",
                7020,
                "0/star",
                1548,
                "0/other",
                735,
                "0/hii region",
                320,
                "0/radio",
                161,
                "0/nebula",
                142,
                "0/infrared",
                125,
                "0/galactic object",
                117,
                "0/uv",
                68
            ]
        },
        "facet_heatmaps": {},
        "facet_intervals": {},
        "facet_queries": {},
        "facet_ranges": {}
    },
    "response": {
        "docs": [
            {
                "id": "25496564"
            },
            {
                "id": "25524274"
            },
            {
                "id": "25500804"
            },
            {
                "id": "25523171"
            },
            {
                "id": "25496128"
            },
            {
                "id": "25496131"
            },
            {
                "id": "25496182"
            },
            {
                "id": "25491817"
            },
            {
                "id": "25491875"
            },
            {
                "id": "25491873"
            }
        ],
        "numFound": 13071,
        "start": 0
    },
    "responseHeader": {
        "QTime": 24,
        "params": {
            "__original_query": [
                "object:LMC",
                "simbid:3133169",
                "simbid:3133169"
            ],
            "__original_url": "q=object%3ALMC&sort=date%20desc%2C%20bibcode%20desc",
            "facet": "true",
            "facet.field": "ned_object_facet_hier",
            "facet.limit": "20",
            "facet.mincount": "1",
            "facet.offset": "0",
            "facet.prefix": "0/",
            "fl": "id",
            "q": "simbid:3133169",
            "sort": "date desc, bibcode desc",
            "wt": "json"
        },
        "status": 0
    }
};
  
  // Solr response for next level SIMBAD object facet (for Galaxy facet)
  //from query:
  // curl 'https://api.adsabs.harvard.edu/v1/search/query?__original_query=object%3ALMC&__original_query=simbid%3A3133169&__original_query=simbid%3A3133169&__original_url=q%3Dobject%253ALMC%26sort%3Ddate%2520desc%252C%2520bibcode%2520desc&facet=true&facet.field=ned_object_facet_hier&facet.limit=20&facet.mincount=1&facet.offset=0&facet.prefix=1%2Fgalaxy&fl=id&q=simbid%3A3133169&sort=date%20desc%2C%20bibcode%20desc' -H 'authorization: Bearer:OwSW0a6gx7jaZm7RpwUHfZMSgFQAxeMQrGK26Veu' -H 'origin: http://localhost:8000' -H 'accept-language: en-US,en;q=0.9' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36' -H 'content-type: application/x-www-form-urlencoded' -H 'accept: application/json, text/javascript, */*; q=0.01' -H 'referer: http://localhost:8000/' -H 'authority: api.adsabs.harvard.edu' --compressed


  var solrResponse_next = {
    "facet_counts": {
        "facet_fields": {
            "ned_object_facet_hier": [
                "1/galaxy/large_magellanic_cloud",
                4098,
                "1/galaxy/small_magellanic_cloud",
                1928,
                "1/galaxy/messier_031",
                774,
                "1/galaxy/messier_033",
                623,
                "1/galaxy/ngc_6822",
                340,
                "1/galaxy/messier_081",
                306,
                "1/galaxy/ic_1613",
                270,
                "1/galaxy/messier_101",
                268,
                "1/galaxy/ngc_6946",
                248,
                "1/galaxy/messier_083",
                242,
                "1/galaxy/fornax_dwarf_spheroidal",
                235,
                "1/galaxy/ngc_2403",
                229,
                "1/galaxy/messier_082",
                227,
                "1/galaxy/ngc_0253",
                218,
                "1/galaxy/ic_0010",
                207,
                "1/galaxy/ngc_0300",
                203,
                "1/galaxy/wlm",
                198,
                "1/galaxy/ngc_5128",
                183,
                "1/galaxy/ngc_5253",
                179,
                "1/galaxy/carina_dwarf",
                178
            ]
        },
        "facet_heatmaps": {},
        "facet_intervals": {},
        "facet_queries": {},
        "facet_ranges": {}
    },
    "response": {
        "docs": [
            {
                "id": "25496564"
            },
            {
                "id": "25524274"
            },
            {
                "id": "25500804"
            },
            {
                "id": "25523171"
            },
            {
                "id": "25496128"
            },
            {
                "id": "25496131"
            },
            {
                "id": "25496182"
            },
            {
                "id": "25491817"
            },
            {
                "id": "25491875"
            },
            {
                "id": "25491873"
            }
        ],
        "numFound": 13071,
        "start": 0
    },
    "responseHeader": {
        "QTime": 11235,
        "params": {
            "__original_query": [
                "object:LMC",
                "simbid:3133169",
                "simbid:3133169"
            ],
            "__original_url": "q=object%3ALMC&sort=date%20desc%2C%20bibcode%20desc",
            "facet": "true",
            "facet.field": "ned_object_facet_hier",
            "facet.limit": "20",
            "facet.mincount": "1",
            "facet.offset": "0",
            "facet.prefix": "1/galaxy",
            "fl": "id",
            "q": "simbid:3133169",
            "sort": "date desc, bibcode desc",
            "wt": "json"
        },
        "status": 0
    }
};

  return function(id) {
	  if (id === 0) {
	  	return JSON.parse(JSON.stringify(solrResponse_top))
	  } else {
	  	return JSON.parse(JSON.stringify(solrResponse_next))
	  }  
  };

});
