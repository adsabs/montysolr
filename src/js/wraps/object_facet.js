define([
  'js/components/api_response',
  'js/components/api_request',
  'js/components/api_query',
  'js/components/json_response',
  'js/widgets/facet/factory',
  'js/components/api_targets',
  'analytics'
], function (
  ApiResponse,
  ApiRequest,
  ApiQuery,
  JsonResponse,
  FacetFactory,
  ApiTargets,
  analytics
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
	widget.processFacetResponse = function(apiResponse, data) {
	  	if (apiResponse instanceof JsonResponse) {
			var map = apiResponse.attributes;
			this.idmap = map;
			return
	  	}
        var info = this.registerResponse(apiResponse, data);
        var facets = this.extractFacets(apiResponse);
		var D = this.idmap;
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
			this.getSIMBADobjects(identifiers);
		};
		var updater = function(model) {
			var v = model.get('value');
			var objId;
			if (v.charAt(0)==='1') {
				var vv = v.split("/");
				var objId = vv[vv.length-1];
			}
			var title = model.get('title');
			if (objId && objId != title && D[objId.toString()]) {
			 	model.set('title', D[objId.toString()]['canonical'])
			};			 
		};
		if (this.collection && this.collection.length > 0) {
			this.collection.each(updater);
		};
//		this.collection.models.forEach(updater);
        var facetCollection = this.processFacets(apiResponse, facets);
        this.updateCollectionAndView(info, facetCollection);
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
		pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processFacetResponse);
		pubsub.publish(pubsub.EXECUTE_REQUEST, request);
    };
    return widget;
  };

});