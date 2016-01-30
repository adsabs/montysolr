define([
	'underscore',
    'marionette',
    'backbone',
	'cache',
    'js/widgets/base/base_widget',
    'js/components/api_response',
    'js/components/api_request',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub',
	'js/widgets/facet/factory',
    'test/mocha/js/widgets/test_json/test_SIMBAD',
	'test/mocha/js/widgets/test_json/test_object_service_data'
  ],
  function(
	       _,
           Marionette,
           Backbone,
	       Cache,
           BaseWidget,
           ApiResponse,
           ApiRequest,
           ApiQuery,
           MinimalPubSub,
	       FacetFactory,
           SolrData,
	       ObjServiceData
    ) {

    describe("Object Facet Widget (object_facet.spec.js)", function () {

      var minsub;
      beforeEach(function () {
        this.minsub = minsub = new (MinimalPubSub.extend({
          request: function (request) {
			  if (request.get("target") == "search/query"){
				  console.log("Getting Solr data");
				  return SolrData();
			  } else {
				  console.log("Getting micro service data");
				  return ObjServiceData();
			  }
          }
        }))({verbose: false});
      });

      /*
      afterEach(function () {
        $("#test-area").empty();
      });
	  */

      it("knows to listen to the pubsub", function() {
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
					this.getSIMBADobjects(identifiers);
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
				facetCollection.forEach(updater);
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
			var request =  new ApiRequest();
			minsub.subscribeOnce(minsub.DELIVERING_RESPONSE, this.processFacetResponse);
			minsub.publish(minsub.EXECUTE_REQUEST, request);
	    };

        widget.activate(this.minsub.beehive.getHardenedInstance());

		var $w = $(widget.render().el);
        $('#test-area').append($w);
		window.$w = $w;

        minsub.publish(minsub.START_SEARCH, minsub.createQuery({'q': 'foo'}));

        // We now should have a facet called "SIMBAD Objects"
		expect($("#test-area .widget-name h3").text().trim()).to.eql("SIMBAD Objects")
		// We expect a top level entry "X-ray" with 1 correspnding record
		expect($("#test-area .widget-body").find(".item-view .size-graphic").text().indexOf('X-ray (1)')).to.be.greaterThan(0);
		// If the SIMBAD id translating worked as expected, there should be an entry for "GOODS SOUTHERN FIELD"
		expect($("#test-area .widget-body").find(".item-view .size-graphic").text().indexOf('GOODS SOUTHERN FIELD')).to.be.greaterThan(0);

      });


    })
  });