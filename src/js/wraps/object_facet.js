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
      openByDefault: true,
      logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']},
      responseProcessors: [
        function (v) {
          var vv = v.split('/');
          return vv[vv.length - 1];
        }
      ]
    });
	widget._getNewCache = function(options) {
	  	return new Cache(_.extend({
	  		'maximumSize': 150,
			'expiresAfterWrite':60*30 // 30 mins
	  	}, _.isObject(options) ? options : {}));
	};
	widget.processFacetResponse = function(apiResponse, data) {
	    if (!('attributes' in apiResponse)) {
			console.log("processing Solr data");
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
	        // construct a list of SIMBAD identifiers
			var ident = facets.filter(function(v){return (typeof v === 'string');}).filter(function(v){return (v.charAt(0)==='1');});
			//
	        // For non-empty lists of facet strings, translate the SIMBAD ids
			if (ident.length > 0) {
				var identifiers = ident.map(function(v){
					var vv = v.split("/");
					return vv[vv.length-1]
				});
//				this.getSIMBADobjects(identifiers);
			};
	        var facetCollection = this.processFacets(apiResponse, facets);
			var updater = function(facet) {
	            var v = facet.value;
				var objId;
				if (v.charAt(0)==='1') {
					var vv = v.split("/");
					var objId = vv[vv.length-1];
				}
				var title = facet.title;
				var oname = widget._cache.getSync(objId);
				if (objId && objId === title && oname) {
				 	facet.title = oname;
				};			 
			};
//			facetCollection.forEach(updater);
	        this.updateCollectionAndView(info, facetCollection);
	  	} else {
			console.log("processing micro service data");
			// We received a response from the micro service, so we need to do the following:
			// 1. Add the mapping from the numerical object idetifier to the canonical object nane
			if (typeof this._cache === 'undefined') {
				widget._cache = widget._getNewCache();
			} else {
				console.log("cache already exists");
			}
			for (var objId in apiResponse.attributes) {
				widget._cache.put(objId, apiResponse.attributes[objId]['canonical']);
			};
		}
	};
    widget.getSIMBADobjects = function (identifiers) {
		var pubsub = this.getPubSub();
        var request =  new ApiRequest({
          target: ApiTargets.SERVICE_OBJECTS,
          query: new ApiQuery({"identifiers" : identifiers}),
		  options : {
            type : "POST",
            contentType : "application/json"
		  }
        });
		pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, this.processFacetResponse);
		pubsub.publish(pubsub.EXECUTE_REQUEST, request);
    };
    return widget;
  };

});