define([
  'js/components/api_response',
  'js/components/api_request',
  'js/components/api_query',
  'js/components/solr_response',
  'js/widgets/facet/factory',
  'js/components/api_targets',
  'analytics',
  'cache',
  'underscore'
], function (
  ApiResponse,
  ApiRequest,
  ApiQuery,
  SolrResponse,
  FacetFactory,
  ApiTargets,
  analytics,
  Cache,
  _
  ) {

  return function() {
    var widget = FacetFactory.makeHierarchicalCheckboxFacet({
      facetField: "simbad_object_facet_hier",
      facetTitle: "SIMBAD Objects",
      openByDefault: false,
      logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']},
      responseProcessors: [
        function (v) {
          var vv = v.split('/');
          return vv[vv.length - 1];
        }
      ]
    });
    // Helper function that returns a new cache object
    widget._getNewCache = function(options) {
      return new Cache(_.extend({
        'maximumSize': 150,
        'expiresAfterWrite':60*30 // 30 mins
      }, _.isObject(options) ? options : {}));
    };
    // Helper function to update facets, replacing the SIMBAD identifier with
    // the associated SIMBAD canonical object name in the facet entry 'title' attribute
    widget.updater = function(facet) {
      // this function is used to get the name
      function splitNum(value){
        if (value[0] === '1'){
          var vv = value.split('/');
          return vv[vv.length-1];
        }
      }
      if (facet instanceof Backbone.Model){
        //directly update the model
        var objId = splitNum(facet.get("value"));
        var title = facet.get("title");
        var oname = widget._cache.getIfPresent(objId);
        if (objId && objId === title && oname) {
          facet.set("title", oname);
        }
      } else {
        // just update the json data, it will be explicitly set into the collection
        // the processResponse function call updateCollectionAndView
        var objId = splitNum(facet.value);
        var title = facet.title;
        var oname = widget._cache.getIfPresent(objId);
        if (objId && objId === title && oname) {
          facet.title = oname;
        }
      }
    };
    // Main facet callback function
    widget.processFacetResponse = function(apiResponse, data) {
      // We received a response from the micro service, so we need to do the following:
      // 1. Add the mapping from the numerical object idetifier to the canonical object nane
      if (typeof this._cache === 'undefined') {
        this._cache = this._getNewCache();
      }
      //
      // Now we look at what kind of response was submitted
      if (!('attributes' in apiResponse)) {
        // We received a Solr response, so we need to do the following:
        // 1. Leave the top level object facet entries untouched (like "0/Galaxy"),
        //    and for deeper entries (like "1/Galaxy/<identifier>"), extract the identifiers
        // 2. Send the list of identifiers to the object service end point
        // 3. For the deeper entries, replace the identifiers with the canonical object names
        var info = this.registerResponse(apiResponse, data);
        var facets = this.extractFacets(apiResponse);
        // no data for us
        if (!facets) {
          console.warn('No facet data for:', this.facetField);
          return;
        }
        // construct a list of SIMBAD identifiers from entries like "1/Galaxy/<identifier>"
        var ident = facets.filter(function(v){return (typeof v === 'string');}).filter(function(v){return (v.charAt(0)==='1');});
        // For non-empty lists of facet strings, get the SIMBAD ids for which we need to know the object name
        if (ident.length > 0) {
          var identifiers = ident.map(function(v){
            var vv = v.split("/");
            return vv[vv.length-1];
          });
          this.getSIMBADobjects(identifiers);
        }
        var facetCollection = this.processFacets(apiResponse, facets);
        facetCollection.forEach(widget.updater);
        this.updateCollectionAndView(info, facetCollection);
      } else {
        // The response was a micro service response
        // Every entry provides a translation from SIMBAD object ID to
        // canonical object name
        for (var objId in apiResponse.attributes) {
          widget._cache.put(objId, apiResponse.attributes[objId]['canonical']);
        }
        widget.collection.forEach( function(v) {
          v.children.forEach(widget.updater);
        });
      }
    }
    // Get object information from the object search micro service
    widget.getSIMBADobjects = function (identifiers) {
      // This method provides a call to the object search micro service
      // Given a list of SIMBAD object identifiers (integers), it will return
      // a mapping, associating every identifier with a canonical object name
      var pubsub = this.getPubSub();
      var request =  new ApiRequest({
        target: ApiTargets.SERVICE_OBJECTS,
        query: new ApiQuery({"identifiers" : identifiers}),
        options : {
          type : "POST",
          contentType : "application/json"
        }
      });
      // Register a callback for this particular query
      this.registerCallback(request.get('query').url(), this.processFacetResponse);
      // Publish the micro service query to the PubSUb
      pubsub.publish(pubsub.EXECUTE_REQUEST, request);

    };
    return widget;
  };

});