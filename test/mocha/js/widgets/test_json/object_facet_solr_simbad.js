define([], function() {
  // Solr response for top level SIMBAD object facet
  //from query:
  // curl 'https://dev.adsabs.harvard.edu/v1/search/query?__original_query=object%3A%2251%20Peg%20b%22&__original_url=q%3Dobject%253A%252251%2520Peg%2520b%2522%26sort%3Ddate%2520desc%252C%2520bibcode%2520desc&facet=true&facet.field=simbad_object_facet_hier&facet.limit=20&facet.mincount=1&facet.offset=0&facet.prefix=0%2F&fl=id&q=simbid%3A%221471968%22&sort=date%20desc%2C%20bibcode%20desc' -H 'authorization: Bearer:VszBO4ikm5QGFMcFQdriwxWgog568RYx4e7oKXKR' -H 'accept-language: en-US,en;q=0.9' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36' -H 'content-type: application/x-www-form-urlencoded' -H 'accept: application/json, text/javascript, */*; q=0.01' -H 'referer: https://dev.adsabs.harvard.edu/' -H 'authority: dev.adsabs.harvard.edu' -H 'x-requested-with: XMLHttpRequest' --compressed

  var solrResponse_top = {
    "responseHeader":{
		"status":0,
	    "QTime":10057,
	    "params":{
			"__original_url":"q=object%3A%2251%20Peg%20b%22&sort=date%20desc%2C%20bibcode%20desc",
	        "facet.limit":"20",
	        "q":"simbid:\"1471968\"",
	        "facet.field":"simbad_object_facet_hier",
	        "fl":"id",
			"facet.prefix":"0/",
	        "sort":"date desc, bibcode desc",
	        "facet.mincount":"1","wt":"json",
			"facet":"true",
	        "__original_query":"object:\"51 Peg b\"",
			"facet.offset":"0"
		}
	},
	"response":{
		"numFound":423,
		"start":0,
		"docs":[
			{"id":"25496096"},
			{"id":"25496103"},
			{"id":"25447309"},
			{"id":"14947909"},
			{"id":"25454770"},
			{"id":"25419427"},
			{"id":"25414059"},
			{"id":"14926513"},
			{"id":"14947167"},
			{"id":"14925064"}]
		},
		"facet_counts":{
			"facet_queries":{},
			"facet_fields":{
				"simbad_object_facet_hier":[
					"0/Other",423,
					"0/Star",378,
					"0/Galaxy",22,
					"0/HII Region",3,
					"0/Nebula",3,
					"0/Infrared",2,
					"0/X-ray",2]
				},
				"facet_ranges":{},
				"facet_intervals":{},
				"facet_heatmaps":{}
		}
  };
  
  // Solr response for next level SIMBAD object facet (for Galaxy facet)
  //from query:
  // curl 'https://dev.adsabs.harvard.edu/v1/search/query?__original_query=object%3A%2251%20Peg%20b%22&__original_url=q%3Dobject%253A%252251%2520Peg%2520b%2522%26sort%3Ddate%2520desc%252C%2520bibcode%2520desc&facet=true&facet.field=simbad_object_facet_hier&facet.limit=20&facet.mincount=1&facet.offset=0&facet.prefix=1%2FGalaxy&fl=id&q=simbid%3A%221471968%22&sort=date%20desc%2C%20bibcode%20desc' -H 'authorization: Bearer:VszBO4ikm5QGFMcFQdriwxWgog568RYx4e7oKXKR' -H 'accept-language: en-US,en;q=0.9' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36' -H 'content-type: application/x-www-form-urlencoded' -H 'accept: application/json, text/javascript, */*; q=0.01' -H 'referer: https://dev.adsabs.harvard.edu/' -H 'authority: dev.adsabs.harvard.edu' -H 'x-requested-with: XMLHttpRequest' --compressed


  var solrResponse_next = {
    "responseHeader":{
		"status":0,
	    "QTime":10,
	    "params":{
			"__original_url":"q=object%3A%2251%20Peg%20b%22&sort=date%20desc%2C%20bibcode%20desc",
			"facet.limit":"20",
			"q":"simbid:\"1471968\"",
			"facet.field":"simbad_object_facet_hier",
			"fl":"id","facet.prefix":"1/Galaxy",
			"sort":"date desc, bibcode desc",
			"facet.mincount":"1",
			"wt":"json",
			"facet":"true",
			"__original_query":"object:\"51 Peg b\"",
			"facet.offset":"0"
		}
	},
	"response":{
		"numFound":423,
		"start":0,
		"docs":[
			{"id":"25496096"},
			{"id":"25496103"},
			{"id":"25447309"},
			{"id":"14947909"},
			{"id":"25454770"},
			{"id":"25419427"},
			{"id":"25414059"},
			{"id":"14926513"},
			{"id":"14947167"},
			{"id":"14925064"}]
		},
		"facet_counts":{
			"facet_queries":{},
			"facet_fields":{
				"simbad_object_facet_hier":[
					"1/Galaxy/3280200",11,
					"1/Galaxy/1575544",5,
					"1/Galaxy/2406698",4,
					"1/Galaxy/3133169",4,
					"1/Galaxy/3253618",4,
					"1/Galaxy/3375707",4,
					"1/Galaxy/3481828",3,
					"1/Galaxy/433918",3,
					"1/Galaxy/1188481",2,
					"1/Galaxy/1522778",2,
					"1/Galaxy/1569144",2,
					"1/Galaxy/1937165",2,
					"1/Galaxy/2044606",2,
					"1/Galaxy/2395134",2,
					"1/Galaxy/2576378",2,
					"1/Galaxy/3195806",2,
					"1/Galaxy/3754378",2,
					"1/Galaxy/533416",2,
					"1/Galaxy/533745",2,
					"1/Galaxy/775675",2]
				},
				"facet_ranges":{},
				"facet_intervals":{},
				"facet_heatmaps":{}
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
