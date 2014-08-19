define(["underscore"], function(_){


var linkGenerator = {

  abstractPageFields: "links_data,ids_data,[citations],property,bibcode",

  resultsPageFields : "links_data,ids_data,[citations],property",

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
       }, this)

       return dataWithLinks

     },

     parseLinksDataForModel : function(data){

       var links, bib, openAccess, ADSScan;

       openAccess = false;
       ADSScan = false;
       bib = data.bibcode;

       links = {text : [], list : [], data : []};

       if (data.ids_data){
         _.each(data.ids_data, function(d){
           var idDict = JSON.parse(d);
           if (idDict.description === "arXiv") {
             links.text.push({openAccess: true, title: "arXiv eprint", link: this.adsUrlRedirect("arXiv", idDict.identifier)})
           }
         }, this)
       }



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
         if (_.contains(data.property, "PUB_OPENACCESS")){
           //this will be accessed by the links_data part below
           openAccess = true;
         }
         if (_.contains(data.property, "ADS_SCAN")){
           //this will also be accessed by links_data below
           ADSScan = true;
           links.text.push({ openAccess : true, letter: "G", title: "ADS Scanned Article", link: this.adsUrlRedirect('gif', bib)})

         }
       }

       if (data.links_data){

         var link_types;

         link_types = _.map(data.links_data, function(d){
           try{
             return JSON.parse(d);
           }
           catch(SyntaxError){
             console.warn(d, "was not parsed")
           }
         });

         link_types = _.filter(link_types, function(l){if (l!== undefined){
           return true;
         }});

         _.each(link_types, function(l){

           //limit to only one in links list, even though there may be multiple articles
           if (l.type === 'electr' && openAccess && !_.findWhere(links.text, {title: "Publisher article"})){

             links.text.push({openAccess : true, letter: "E",title: "Publisher article", link : this.adsUrlRedirect(l.type, bib)})

           }
           else if (l.type === 'electr' && !openAccess  && !_.findWhere(links.text, {title: "Publisher article"})){
             links.text.push({letter: "E", title: "Publisher article", link : this.adsUrlRedirect(l.type, bib)})

           }
           //has some more involved logic
           else if (l.type === 'pdf'){
             //default to adsscan
             if (ADSScan){
               links.text.push({ openAccess : true, letter: "F", title: "ADS PDF", link : this.adsUrlRedirect('article', bib)})
             }
             else {
               if (openAccess){
                 links.text.push({ openAccess : true, letter: "F", title: "Publisher PDF", link : this.adsUrlRedirect('article', bib)})
               }
               else {
                 links.text.push({letter: "F", title: "Publisher PDF", link : this.adsUrlRedirect('article', bib)})

               }

             }

           }
           else if (l.type === 'simbad'){
             links.data.push({ letter: "S", title: "SIMBAD Objects", link : this.adsUrlRedirect(l.type, bib)})

           }
           else if (l.type === 'ned'){
             links.data.push({ letter: "N", title: "NED Objects", link : this.adsUrlRedirect(l.type, bib)})

           }
           else if (l.type === 'data'){
             links.data.push({letter: "D", title: "Archival Data", link : this.adsUrlRedirect(l.type, bib)})

           }

         }, this);

       }

       data.links  = links;

       return data

     },

    parseResourcesData : function(data) {

      var fullTextSources, dataProducts, bib, openAccess, ADSScan;

      fullTextSources = [];
      dataProducts = [];

      openAccess = false;
      ADSScan = false;
      links = [];
      bib = data.bibcode;

      if (data.ids_data) {
        _.each(data.ids_data, function (d) {
          var idDict = JSON.parse(d);
          if (idDict.description === "arXiv") {
            fullTextSources.push({openAccess: true, title: "arXiv eprint", link: this.adsUrlRedirect("arXiv", idDict.identifier)})
          }
        }, this)
      }

      if (data.property) {

        if (_.contains(data.property, "PUB_OPENACCESS")) {
          //this will be accessed by the links_data part below
          openAccess = true;
        }
        if (_.contains(data.property, "ADS_SCAN")) {
          //this will also be accessed by links_data below
          ADSScan = true;
          fullTextSources.push({openAccess: true, title: "ADS Scanned Article", link: this.adsUrlRedirect('gif', bib)})
        }
      }

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

        link_types = _.filter(link_types, function(l){if (l!== undefined){
          return true;
        }});

        _.each(link_types, function (l) {

          if (l.type === "electr" && openAccess) {
            fullTextSources.push({openAccess: true, title: "Publisher article", link: this.adsUrlRedirect('electr', bib)})
          }

          else if (l.type === "electr" && !openAccess) {

            fullTextSources.push({title: "Publisher article", link: this.adsUrlRedirect('electr', bib)})

          }

         //has some more involved logic
          else if (l.type === 'pdf'){
            //default to adsscan
            if (ADSScan){
              fullTextSources.push({openAccess : true,  title: "ADS PDF", link : this.adsUrlRedirect('article', bib)})
            }
            else {
              if (openAccess){
                fullTextSources.push({openAccess : true,  title: "Publisher PDF", link : this.adsUrlRedirect('article', bib)})
              }
              else {
                fullTextSources.push({title: "Publisher PDF", link : this.adsUrlRedirect('article', bib)})

              }
            }
          }

          else if (l.type === 'data'){
            dataProducts.push({title : "Archival data", link : this.adsUrlRedirect('data', bib)})
          }
          else if (l.type === 'simbad'){
            dataProducts.push({title : "SIMBAD objects", link : this.adsUrlRedirect('simbad', bib)})

          }
          else if (l.type === 'ned'){
            dataProducts.push({title : "NED objects", link : this.adsUrlRedirect('ned', bib)})

          }

        }, this);

      };

      data.fullTextSources = fullTextSources;
      data.dataProducts = dataProducts;

      return data
    }

}

  return linkGenerator
})