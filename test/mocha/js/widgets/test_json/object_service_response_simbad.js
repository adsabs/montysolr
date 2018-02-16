define([], function() {

  //from query:
  // curl 'https://dev.adsabs.harvard.edu/v1/objects' -H 'origin: https://dev.adsabs.harvard.edu' -H 'x-requested-with: XMLHttpRequest' -H 'accept-language: en-US,en;q=0.9' -H 'authorization: Bearer:VszBO4ikm5QGFMcFQdriwxWgog568RYx4e7oKXKR' -H 'content-type: application/json' -H 'accept: application/json, text/javascript, */*; q=0.01' -H 'referer: https://dev.adsabs.harvard.edu/' -H 'authority: dev.adsabs.harvard.edu' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36' --data-binary '{"identifiers":["3280200","1575544","2406698","3133169","3253618","3375707","3481828","433918","1188481","1522778","1569144","1937165","2044606","2395134","2576378","3195806","3754378","533416","533745","775675"]}' --compressed


  var apiResponse = {
	  "3280200": {
		  "id": "3280200", 
		  "canonical": "NGC   104"
	  }, 
	  "1522778": {
		  "id": "1522778", 
		  "canonical": "M  33"
	  }, 
	  "3375707": {
		  "id": "3375707", 
		  "canonical": "NGC  5139"
	  }, 
	  "3481828": {
		  "id": "3481828", 
		  "canonical": "NGC  6397"
	  }, 
	  "533416": {
		  "id": "533416", 
		  "canonical": "NGC  3690"
	  }, 
	  "1569144": {
		  "id": "1569144", 
		  "canonical": "M  32"
	  }, 
	  "2406698": {
		  "id": "2406698",
		   "canonical": "M   4"
	  }, 
	  "3195806": {
		  "id": "3195806",
		   "canonical": "Magellanic Clouds"
	  }, 
	  "2576378": {
		  "id": "2576378", 
		  "canonical": "NGC  6822"
	  }, 
	  "775675": {
		  "id": "775675",
		   "canonical": "NGC  1851"
	  }, 
	  "1937165": {
		  "id": "1937165",
		   "canonical": "Virgo Group"
	  }, 
	  "2044606": {
		  "id": "2044606", 
		  "canonical": "ACO  1656"
	  }, 
	  "3754378": {
		  "id": "3754378", 
		  "canonical": "Local Group"
	  }, 
	  "1575544": {
		  "id": "1575544", 
		  "canonical": "M  31"
	  }, 
	  "533745": {
		  "id": "533745", 
		  "canonical": "NGC 3690 West"
	  }, 
	  "433918": {
		  "id": "433918", 
		  "canonical": "M  82"
	  }, 
	  "3133169": {
		  "id": "3133169", 
		  "canonical": "LMC"
	  }, 
	  "1188481": {
		  "id": "1188481", 
		  "canonical": "Fornax Dwarf Spheroidal"
	  }, 
	  "2395134": {
		  "id": "2395134", 
		  "canonical": "SDG"
	  },
	  "3253618": {
		  "id": "3253618", 
		  "canonical": "SMC"
	  }
	};

  return function() {return JSON.parse(JSON.stringify(apiResponse))};

});
