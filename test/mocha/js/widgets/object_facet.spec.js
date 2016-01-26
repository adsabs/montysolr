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
				  return SolrData();
			  } else {
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

        widget.activate(this.minsub.beehive.getHardenedInstance());

		var $w = $(widget.render().el);
        $('#test-area').append($w);
		window.$w = $w;

        minsub.publish(minsub.START_SEARCH, minsub.createQuery({'q': 'foo'}));
		console.log($w);

        expect($w.find('.widget-body').children().not('.hide').length).to.be.eql(5);
        expect($w.find('.widget-body').children().filter('.hide').length).to.be.eql(95);
      });


    })
  });