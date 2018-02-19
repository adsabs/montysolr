define([], function() {

  //from query:
  // curl 'https://api.adsabs.harvard.edu/v1/objects' -H 'origin: http://localhost:8000' -H 'accept-language: en-US,en;q=0.9' -H 'authorization: Bearer:OwSW0a6gx7jaZm7RpwUHfZMSgFQAxeMQrGK26Veu' -H 'content-type: application/json' -H 'accept: application/json, text/javascript, */*; q=0.01' -H 'referer: http://localhost:8000/' -H 'authority: api.adsabs.harvard.edu' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36' --data-binary '{"source":"NED","identifiers":["large_magellanic_cloud","small_magellanic_cloud","messier_031","messier_033","ngc_6822","messier_081","ic_1613","messier_101","ngc_6946","messier_083","fornax_dwarf_spheroidal","ngc_2403","messier_082","ngc_0253","ic_0010","ngc_0300","wlm","ngc_5128","ngc_5253","carina_dwarf"]}' --compressed | python -m json.tool


  var apiResponse = {
    "carina_dwarf": {
        "canonical": "carina dwarf",
        "id": "carina_dwarf"
    },
    "fornax_dwarf_spheroidal": {
        "canonical": "fornax dwarf spheroidal",
        "id": "fornax_dwarf_spheroidal"
    },
    "ic_0010": {
        "canonical": "ic 0010",
        "id": "ic_0010"
    },
    "ic_1613": {
        "canonical": "ic 1613",
        "id": "ic_1613"
    },
    "large_magellanic_cloud": {
        "canonical": "large magellanic cloud",
        "id": "large_magellanic_cloud"
    },
    "messier_031": {
        "canonical": "messier 031",
        "id": "messier_031"
    },
    "messier_033": {
        "canonical": "messier 033",
        "id": "messier_033"
    },
    "messier_081": {
        "canonical": "messier 081",
        "id": "messier_081"
    },
    "messier_082": {
        "canonical": "messier 082",
        "id": "messier_082"
    },
    "messier_083": {
        "canonical": "messier 083",
        "id": "messier_083"
    },
    "messier_101": {
        "canonical": "messier 101",
        "id": "messier_101"
    },
    "ngc_0253": {
        "canonical": "ngc 0253",
        "id": "ngc_0253"
    },
    "ngc_0300": {
        "canonical": "ngc 0300",
        "id": "ngc_0300"
    },
    "ngc_2403": {
        "canonical": "ngc 2403",
        "id": "ngc_2403"
    },
    "ngc_5128": {
        "canonical": "ngc 5128",
        "id": "ngc_5128"
    },
    "ngc_5253": {
        "canonical": "ngc 5253",
        "id": "ngc_5253"
    },
    "ngc_6822": {
        "canonical": "ngc 6822",
        "id": "ngc_6822"
    },
    "ngc_6946": {
        "canonical": "ngc 6946",
        "id": "ngc_6946"
    },
    "small_magellanic_cloud": {
        "canonical": "small magellanic cloud",
        "id": "small_magellanic_cloud"
    },
    "wlm": {
        "canonical": "wlm",
        "id": "wlm"
    }
};

  return function() {return JSON.parse(JSON.stringify(apiResponse))};

});
