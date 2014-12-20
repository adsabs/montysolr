define(["underscore"], function(_){


var linkGenerator = {

  abstractPageFields: "links_data,[citations],property,bibcode",

  resultsPageFields : "links_data,[citations],property",

  //function that can turn links_data into a list of actual links

  //using Giovanni's function from beer
  adsUrlRedirect : function(type, id) {

    var adsClassicBaseUrl = "http://adsabs.harvard.edu/";

    switch (type) {
      case "doi":
        return adsClassicBaseUrl + "cgi-bin/nph-abs_connect?fforward=http://dx.doi.org/" + id
      case "data":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=DATA"
      case "electr":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=EJOURNAL"
      case "gif":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=GIF"
      case "article":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=ARTICLE"
      case "preprint":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=PREPRINT"
      case "arXiv":
        //in this case id should be arxivid, not bibcode
        return adsClassicBaseUrl + "cgi-bin/nph-abs_connect?fforward=http://arxiv.org/abs/" + id
      case "simbad":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=SIMBAD"
      case "ned":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=NED"
      case "openurl":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=OPENURL"

    }
  },
      /*
    *   Takes data--a json object from apiResponse--and augments it with a "links"
    *   object.  I used mostly Giovanni's logic here as well. This is to be called
    *   by the processData method of a widget.
    * */

     parseLinksData : function(data) {
       var dataWithLinks;

       dataWithLinks = _.map(data, function (d) {
         return this.parseLinksDataForModel(d)
       }, this);

       return dataWithLinks;

     },

  // this function is used by list-of-things to add quick links to an item
     parseLinksDataForModel : function(data){

       var links, bib, openAccess, ADSScan;

       ADSScan = false;
       bib = data.bibcode;

       links = {text : [], list : [], data : []};

       if(data["[citations]"]){

         var nc = data["[citations]"].num_citations;
         var nr = data["[citations]"].num_references;
         if (nc >= 1){
           links.list.push({letter: "C", title: "Citations ("+ nc + ")",   link:"/#abs/"+ bib + "/citations" })

         }
         if (nr >= 1){
           links.list.push({ letter: "R", title: "References ("+ nr + ")" , link:"/#abs/"+ bib + "/references"})
         }
       }

       if (data.property){
         if (_.contains(data.property, "TOC")){
           links.list.push({letter: "T", title: "Table of Contents", link:"/#abs/"+ bib + "/tableofcontents"})
         }

         if (_.contains(data.property, "ADS_SCAN")){
           //this will also be accessed by links_data below
           ADSScan = true;
           links.text.push({ openAccess : true, letter: "G", title: "ADS Scanned Article", link: this.adsUrlRedirect('gif', bib)});
         }
       }

       if (data.links_data){

         var link_types;

         link_types = _.map(data.links_data, function(d){
           try{
             return JSON.parse(d);
           }
           catch(SyntaxError){
             console.warn(d, "was not parsed");
           }
         });

         _.each(link_types, function(l){

           var openAccess = l.access === "open" ? true : false;

           //limit to only one in links list, even though there may be multiple articles
           if (l.type === 'electr' && !_.findWhere(links.text, {title: "Publisher article"})){

             links.text.push({openAccess : openAccess, letter: "E",title: "Publisher article", link : this.adsUrlRedirect(l.type, bib)});

           }

           else if (l.type === "preprint"){

             links.text.push({openAccess: openAccess, letter : "X",  title: "arXiv eprint", link: this.adsUrlRedirect(l.type, bib)});

           }

           else if (l.type === 'pdf'){

             //first, find the title, which is dependent on ADSSCAN
             var title = ADSScan ? "ADS PDF" : "Publisher PDF";

             links.text.push({ openAccess : openAccess, letter: "F", title: title, link : this.adsUrlRedirect('article', bib)});

           }
           else if (l.type === 'simbad' && !_.findWhere(links.data, {title: "SIMBAD Objects"})){
             links.data.push({ letter: "S", title: "SIMBAD Objects", link : this.adsUrlRedirect(l.type, bib)})

           }
           else if (l.type === 'ned' && !_.findWhere(links.data, {title: "NED Objects"})){
             links.data.push({ letter: "N", title: "NED Objects", link : this.adsUrlRedirect(l.type, bib)})

           }
           else if (l.type === 'data' && !_.findWhere(links.data, {title: "Archival Data"})){
             links.data.push({letter: "D", title: "Archival Data", link : this.adsUrlRedirect(l.type, bib)})

           }

         }, this);

       }

       data.links  = links;
       return data

     },

  //this function is used as a widget on the abstract page
    parseResourcesData : function(data) {

      var fullTextSources, dataProducts, bib, ADSScan;

      fullTextSources = [];
      dataProducts = [];

      ADSScan = false;
      bib = data.bibcode;

      if (data.links_data) {

        var link_types;

        link_types = _.map(data.links_data, function (d) {
          try{
            return JSON.parse(d);
          }
          catch(SyntaxError){
            console.warn(d, "was not parsed")
          }

        });

        if (data.property) {

          if (_.contains(data.property, "ADS_SCAN")) {
            //this will also be accessed by links_data below
            ADSScan = true;
            fullTextSources.push({openAccess: true, title: "ADS Scanned Article", link: this.adsUrlRedirect('gif', bib)});
          }
        }

        _.each(link_types, function (l) {

          var openAccess = l.access === "open" ? true : false;

          if (l.type == "preprint"){

            fullTextSources.push({openAccess: openAccess, title: "arXiv eprint", link: this.adsUrlRedirect("preprint", bib)});

          }

          if (l.type === "electr" && !_.findWhere(fullTextSources, {title: "Publisher article"}))  {

            fullTextSources.push({openAccess: openAccess, title: "Publisher article", link: this.adsUrlRedirect('electr', bib)})
          }

          else if (l.type === 'pdf'){
            //default to adsscan, otherwise publisher pdf
            var title = ADSScan ? "ADS PDF" : "Publisher PDF";

              fullTextSources.push({openAccess : true,  title: title, link : this.adsUrlRedirect('article', bib)});
          }

          else if (l.type === 'data' &&  !_.findWhere(dataProducts, {title: "Archival data"})){
            dataProducts.push({title : "Archival data", link : this.adsUrlRedirect('data', bib)})
          }
          else if (l.type === 'simbad' && !_.findWhere(dataProducts, {title: "SIMBAD objects"})){
            dataProducts.push({title : "SIMBAD objects", link : this.adsUrlRedirect('simbad', bib)})

          }
          else if (l.type === 'ned' && !_.findWhere(dataProducts, {title: "NED objects"})){
            dataProducts.push({title : "NED objects", link : this.adsUrlRedirect('ned', bib)})

          }

        }, this);

      }

      data.fullTextSources = fullTextSources;
      data.dataProducts = dataProducts;

      return data;
    }

}

  return linkGenerator
})