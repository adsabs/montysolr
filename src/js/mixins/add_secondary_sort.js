define([], function () {

  return {
    addSecondarySort : function(apiQuery) {
      //add secondary sort with correct asc/desc
      if (apiQuery.get("sort")){
        //only get before the first column to prevent adding redundant secondary sort
        var primarySort = apiQuery.get("sort")[0].split(",")[0];
        var secondarySort = primarySort.indexOf(" asc") > -1 ? "bibcode asc" : "bibcode desc";
        apiQuery.set("sort", primarySort + ", " + secondarySort);
      }
    }
  }

});