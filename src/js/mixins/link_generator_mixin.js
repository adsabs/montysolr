define(["underscore", "js/mixins/openurl_generator"], function(_, OpenURLGenerator){


var linkGenerator = {

  /*
  * requires the following solr fields to work correctly:
  *
  * links_data,[citations],property,bibcode,first_author,year
  * page,pub,pubdate,title,volume,doi,issue,issn
  *
  * */

  //function that can turn links_data into a list of actual links

  //using Giovanni's function from beer
  adsUrlRedirect: function (type, id) {

    var adsClassicBaseUrl = "http://adsabs.harvard.edu/";
    var bumblebeeBaseUrl = 'https://ui.adsabs.harvard.edu/';

    switch (type) {
      case 'webrecord':
        return bumblebeeBaseUrl + '#abs/' + id;
      case "doi":
        return adsClassicBaseUrl + "cgi-bin/nph-abs_connect?fforward=http://dx.doi.org/" + id;
      case "data":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=DATA";
      case "electr":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=EJOURNAL";
      case "gif":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=GIF";
      case "article":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=ARTICLE";
      case "preprint":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=PREPRINT";
      case "arXiv":
        //in this case id should be arxivid, not bibcode
        return adsClassicBaseUrl + "cgi-bin/nph-abs_connect?fforward=http://arxiv.org/abs/" + id;
      case "simbad":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=SIMBAD";
      case "ned":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=NED";
      case "openurl":
        return adsClassicBaseUrl + "cgi-bin/nph-data_query?bibcode=" + id + "&link_type=OPENURL";
      default:
        throw new Exception('Unknown type: ' + type);

    }
  },
  /*
   *   Takes data--a json object from apiResponse--and augments it with a "links"
   *   object. This is used for item views in the results widget. This is to be called
   *   by the processData method of a widget.
   *
   */

  parseLinksData: function (data) {
    var dataWithLinks;

    dataWithLinks = _.map(data, function (d) {
      return this.parseLinksDataForModel(d)
    }, this);

    return dataWithLinks;

  },

  getTextAndDataLinks: function (links_data, bib, data) {

    var link_types, links = { text : [], data : []},
        scan_available, article_identifier, openURL,
        archival, single, groups, order;

    link_types = _.filter(_.map(links_data, function (d) {
      try {
        return JSON.parse(d);
      }
      catch (SyntaxError) {
        console.error("Error parsing links_data value", bib, d);
      }
    }));

    _.each(link_types, function (l) {

      var openAccess = l.access === "open" ? true : false;

      switch (l.type) {

        case "preprint":
          links.text.push({openAccess: openAccess, title: "arXiv e-print", link: this.adsUrlRedirect("preprint", bib)});
          break;
        case "electr":

          scan_available =_.where(link_types, {"type": "gif"}).length > 0;

          data = data || {};

          // Determine if the article has any identifiers
          article_identifier = data.doi || data.issn || data.isbn;

          // Only create an openURL if the following is true:
          //   - The article HAS a DOI
          //   - There is NO open access available
          //   - There is NO scan available from the ADS
          //   - The user is authenticated
          //   - the user HAS a library link server

          if (!l.access && !scan_available && data.link_server && article_identifier){
            openURL = new OpenURLGenerator(data, data.link_server);
            openURL.createOpenURL();
            links.text.push({openAccess: openAccess, title: "Find it at your institution", link: openURL.openURL, openUrl: true});
          }
          //always add the electronic link article, even if there was an openurl added above
          links.text.push({openAccess: openAccess, title: "Publisher Article", link: this.adsUrlRedirect('electr', bib)});
          break;
        case "pdf":
          links.text.push({openAccess: openAccess, title: "Publisher PDF", link: this.adsUrlRedirect('article', bib)});
          break;
        case "article":
          links.text.push({openAccess: openAccess, title: "ADS PDF", link: this.adsUrlRedirect('article', bib)});
          break;
        case "gif":
          links.text.push({openAccess: openAccess, title: "ADS Scanned Article", link: this.adsUrlRedirect('gif', bib)});
          break;
        case "data":
          var title = l.instances ? "Archival Data (" + l.instances + ")" : "Archival Data";
          links.data.push({title: title, link: this.adsUrlRedirect('data', bib)});
          break;
        case "simbad":
          var title = l.instances ? "SIMBAD objects (" + l.instances + ")" : "SIMBAD objects";
          links.data.push({title: title, link: this.adsUrlRedirect('simbad', bib)});
          break;
        case "ned":
          var title = l.instances ? "NED objects (" + l.instances + ")" : "NED objects";
          links.data.push({title: title, link: this.adsUrlRedirect('ned', bib)});
          break;
      }

    }, this);

    //get rid of links.data duplicates for the "Archival Data" (but add a parenthesis indicating how many)
    // since I guess the "instances" key isn't working correctly in this case
     archival = _.where(links.data, {link :  this.adsUrlRedirect('data', bib) });
    if (archival.length > 1 ){
      single = {title : "Archival Data (" + archival.length + ")", link : this.adsUrlRedirect('data', bib)};
      //remove all archival links
      links.data = _.filter(links.data, function(d){if (!d.title.match("Archival Data")){return true }});
      links.data.push(single);
    }

    //get rid of text duplicates and default to open access
    groups = _.groupBy(links.text, "title");
    _.each(groups, function(v,k){

      var singleVersion;

      if (v.length > 1){
        //remove duplicates from links
        links.text = _.filter(links.text, function(l){
          return (l.title !== k);
        });

        singleVersion = _.findWhere(v, {"openAccess" : true}) || v[0];
        links.text.push(singleVersion);
      }

    });

    //finally, sort the links.text
    order = [
      "ADS PDF",
      "ADS Scanned Article",
      "Find it at your institution",
      "Publisher Article",
      "Publisher PDF",
      "arXiv e-print"
    ];
    links.text = _.sortBy(links.text, function(l){
      return order.indexOf(l.title);
    });

    return links
  },

  // this function is used by list-of-things to add quick links to an item
  parseLinksDataForModel: function (data) {

    var links = {list : [], data : [], text : []};

    if (data.links_data) {
      _.extend(links, this.getTextAndDataLinks(data.links_data, data.bibcode, data));
    }

    if (data["[citations]"]) {

      var nc = data["[citations]"].num_citations;
      var nr = data["[citations]"].num_references;
      if (nc >= 1) {
        links.list.push({letter: "C", title: "Citations (" + nc + ")", link: "#abs/" + data.bibcode + "/citations" })
      }
      if (nr >= 1) {
        links.list.push({ letter: "R", title: "References (" + nr + ")", link: "#abs/" + data.bibcode + "/references"})
      }
    }

    if (data.property) {
      if (_.contains(data.property, "TOC")) {
        links.list.push({letter: "T", title: "Table of Contents", link: "#abs/" + data.bibcode + "/tableofcontents"})
      }

    }

    data.links = links;

    return data

  },

  //this function is used as a widget on the abstract page
  parseResourcesData: function (data) {
    /**
     * Assuming the following input:
     * data = {....,
     *         ....,
     *         'link_server': link_server_string}
     */

    if (data.links_data) {
      var links = this.getTextAndDataLinks(data.links_data, data.bibcode, data);
      data.fullTextSources = links.text;
      data.dataProducts = links.data;
    }

    return data;

  }
}

  return linkGenerator
})