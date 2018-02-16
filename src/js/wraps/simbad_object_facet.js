define([
  'js/components/api_response',
  'js/components/api_request',
  'js/components/api_query',
  'js/components/solr_response',
  'js/widgets/facet/factory',
  'js/components/api_targets',
  'analytics',
  'cache',
  'underscore',
  'js/widgets/facet/reducers'
], function(
  ApiResponse,
  ApiRequest,
  ApiQuery,
  SolrResponse,
  FacetFactory,
  ApiTargets,
  analytics,
  Cache,
  _,
  Reducers
) {

  return function() {

    var widget = FacetFactory.makeHierarchicalCheckboxFacet({
      facetField: "simbad_object_facet_hier",
      facetTitle: "SIMBAD Objects",
      logicOptions: {
        single: ['limit to', 'exclude'],
        'multiple': ['and', 'or', 'exclude']
      }
    });


    widget._dispatchRequest = function(id, offset) {

      var pubsub = this.getPubSub(),
          that = this;

      this.store.dispatch(this.actions.data_requested(id));

      if (!id) {
        //top level
        pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, function(apiResponse) {
          that.store.dispatch(that.actions.data_received(apiResponse.toJSON(), id));
        });
      } else {
        pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, function(apiResponse) {
          that.translateSimbid(apiResponse, id);
        });
      }

      var q = this.customizeQuery(this.getCurrentQuery());
      var children = id ? this.store.getState().facets[id].children : this.store.getState().children;
      var offset = children.length || 0;

      q.set("facet.offset", offset);
      // set prefix from 0/ to 1/
      if (id) q.set("facet.prefix", id.replace("0/", "1/"));
      var req = this.composeRequest(q);
      pubsub.publish(pubsub.DELIVERING_REQUEST, req);

    };

    widget._simbidCache = {};

   /**
    * called with facet data, fetches and returns human readable names for simibds
    */
    widget.translateSimbid = function(apiResponse, id) {

      var that = this;
      var simbids = apiResponse.toJSON().facet_counts.facet_fields.simbad_object_facet_hier
        .map(function(id, i) {
          if (i % 2 == 0) return id.split("/")[id.split("/").length - 1]
        }).filter(function(id) {
          if (id) return id
        });

      function done(data) {
        var enhancedResponse = apiResponse.toJSON();
        enhancedResponse.facet_counts
          .facet_fields.simbad_object_facet_hier = enhancedResponse
          .facet_counts.facet_fields.simbad_object_facet_hier.map(
            function(facet, i) {
              if (i % 2 == 0) {
                var facetParts = facet.split("/");
                var simbid = facetParts[facetParts.length - 1];
                //in the form "1/Star/* bet Pic"
                var facetVal = facetParts.slice(0, 2).concat([data[simbid].canonical]).join("/");
                //store it in case the widget is submitted later
                widget._simbidCache[facetVal] = simbid;
                return facetVal;
              }
              return facet;
            }, this)
        that.store.dispatch(that.actions.data_received(enhancedResponse, id));
      };
      
      var request = new ApiRequest({
        target: ApiTargets.SERVICE_OBJECTS,
        options: {
          type: "POST",
          contentType: "application/json",
          data: JSON.stringify({
            identifiers: simbids
          }),
          done: done
        }
      });

	  var pubsub = this.getPubSub();
	  pubsub.publish(pubsub.EXECUTE_REQUEST, request);
    };

    widget.submitFilter = function(operator) {

      var q = this.getCurrentQuery().clone();
      q.unlock();

      var facetField = this.store.getState().config.facetField;
      var fieldName = 'fq_' + facetField;
      var selectedFacets = Reducers.getActiveFacets(this.store.getState(), this.store.getState().state.selected);

      var conditions = selectedFacets.map(function(c) {
          //it's a second level facet, replace the name part with the simbid (unique for object facet)
          if ( this._simbidCache[c] ){
            var facetName = c.split('/').slice(0,2)
            .concat([this._simbidCache[c]])
            .join('/');
          }
          else {
            var facetName = c;
          }
          return facetField + ":\"" + facetName + "\"";
        }, this);

      if (operator == 'and' || operator == 'limit to') {
        this.queryUpdater.updateQuery(q, fieldName, 'limit', conditions);
      } else if (operator == 'or') {
        this.queryUpdater.updateQuery(q, fieldName, 'expand', conditions);
      } else if (operator == 'exclude' || operator == 'not') {
        if (q.get(fieldName)) {
          this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
        } else {
          conditions.unshift('*:*');
          this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
        }
      }

      var fq = '{!type=aqp v=$' + fieldName + '}';
      var fqs = q.get('fq') || [];
      fqs.push(fq);
      q.set('fq', _.unique(fqs));

      if (facetField === 'simbad_object_facet_hier') {
        // In the case of the SIMBAD objects facet, we need to do something in order to get the
        // object names into the breadcrumbs. Without doing the exercise below, we would end up
        // with the SIMBAD identifiers in the breadcrumbs, rather than the corresponding object
        // names. If somebody selects multiple entries in the facet simultaneously, we can rely on
        // the contents of "conditions" to make this translation, but this fails with applying
        // filters sequentially. Here the cache we created in the object facet widget comes to the
        // rescue, which we can access as "self._cache".
        // The attribute '__simbad_object_facet_hier_fq_simbad_object_facet_hier' was defined to
        // carry over information that will allow us to replace SIMBAD identifiers with their
        // associated object names elsewhere in the application, in the case of processing a
        // filter query. It is a list of values like
        //    ["AND", "simbad_object_facet_hier:1\/Galaxy\/3133169"]
        // and when you select two facets entries: first one, 'limit to', second one, 'limit to'
        // you will get an entry like
        //    ["AND", "(simbad_object_facet_hier:1\/Galaxy\/3133169)", "simbad_object_facet_hier:1\/Galaxy\/1575544"]
        // If we do nothing, the facet strings will appear in the breadcrumbs. So, we will need to replace them
        // by their corresponding object names. That is where the SIMBAD identifiers come in (3133169
        // and 1575544 in this example). These should be present as keys in the cache generated in
        // the object facet widget, returning the associated (canonical) object names
        //create an array with just the user-friendly names of the facets
        var logic = q.get('__simbad_object_facet_hier_fq_simbad_object_facet_hier')[0];
        q.set('__simbad_object_facet_hier_fq_simbad_object_facet_hier', [logic].concat(selectedFacets));
      }

      q.unset('facet.prefix');
      q.unset('facet');
      q.unset("start");
      q.unset("rows");
      this.dispatchNewQuery(q);

      analytics('send', 'event', 'interaction', 'facet-applied', JSON.stringify({
        name: facetField,
        logic: operator,
        conditions: conditions
      }));
    }
    return widget;
  };

});
