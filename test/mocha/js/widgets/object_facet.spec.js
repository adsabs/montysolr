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
	'test/mocha/js/widgets/test_json/test_object_service_data',
	'js/widgets/base/paginated_multi_callback_widget',
	'js/components/api_query_updater',
		'js/wraps/object_facet'
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
	       ObjServiceData,
	       PaginatedMultiCallbackWidget,
	       ApiQueryUpdater,
		   ObjectFacet
    ) {

    describe("Object Facet Widget (object_facet.spec.js)", function () {

      var minsub;
      beforeEach(function () {
        this.minsub = minsub = new (MinimalPubSub.extend({
          request: function (request) {
			  if (request.get("target") == "search/query"){
				  // When we're called as a Solr search, we return Solr stub data
				  return SolrData();
			  } else {
				  // In all other cases we return stub data representing a response 
				  // from the object search micro service
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

      it("Gets object facet data from Solr docs and translates the SIMBAD object IDs", function() {
		// Create a new SIMBAD Objects facet
        var widget = new ObjectFacet();

        widget.activate(this.minsub.beehive.getHardenedInstance());

		var $w = $(widget.render().el);
        $('#test-area').append($w);
		window.$w = $w;
        // Initiate a search
        minsub.publish(minsub.START_SEARCH, minsub.createQuery({'q': 'foo'}));
        // This results in
		// 1. The initial search query results in Solr stub data being sent to the object facet
		// 2. The hierarchical object facet data in the Solr stub data initiates a call to
		//    the SIMBAD micro service which, through a "call back", results in micro service
		//    stub data being sent back, which will get cached (SIMBAD id as key, SIMBAD object
		//    name as value)
        // We now should have a facet called "SIMBAD Objects"
		expect($("#test-area .widget-name h3").text().trim()).to.eql("SIMBAD Objects")
		// We expect a top level entry "X-ray" with 1 correspnding record
		expect($("#test-area .widget-body").find(".item-view .size-graphic").text().indexOf('X-ray (1)')).to.be.greaterThan(0);
		// If the SIMBAD id translating worked as expected, there should be an entry for "GOODS SOUTHERN FIELD"
		expect($("#test-area .widget-body").find(".item-view .size-graphic").text().indexOf('GOODS SOUTHERN FIELD')).to.be.greaterThan(0);

      });


    })
  });