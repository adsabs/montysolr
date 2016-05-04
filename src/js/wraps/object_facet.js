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
      facetTitle: "Objects",
      logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']}
    });

    widget._dispatchRequest = function (id, offset) {

      var pubsub = this.getPubSub(),
          that = this;

      this.store.dispatch(this.actions.data_requested(id));

      if (!id){
        //top level
        pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, function (apiResponse) {
          that.store.dispatch(that.actions.data_received(apiResponse.toJSON(), id));
        });
      }
      else {
        pubsub.subscribeOnce(pubsub.DELIVERING_RESPONSE, function (apiResponse) {
         that.translateSimbid(apiResponse, id);
        });
      }

      var q = this.customizeQuery(this.getCurrentQuery());

      var children = id ? this.store.getState().facets[id].children : this.store.getState().children;
      var offset = children.length || 0;

      q.set("facet.offset", offset);
      // set prefix from 0/ to 1/
      if (id)  q.set("facet.prefix", id.replace("0/", "1/"));

      var req = this.composeRequest(q);
      pubsub.publish(pubsub.DELIVERING_REQUEST, req);

    };

    widget.translateSimbid = function (apiResponse, id) {

      var that = this;

      var simbids = apiResponse.toJSON().facet_counts.facet_fields.simbad_object_facet_hier
          .map(function(id, i) {
            if (i%2 == 0) return id.split("/")[id.split("/").length -1]
          }).filter(function(id){ if (id) return id });

      function done (data){
       var enhancedResponse =  apiResponse.toJSON();
        enhancedResponse.facet_counts.facet_fields.simbad_object_facet_hier = enhancedResponse.facet_counts.facet_fields.simbad_object_facet_hier.map(
            function(facet, i){
                if (i % 2 == 0){
                  var facetParts = facet.split("/");
                  var simbid =  facetParts[facetParts.length -1];
                  return facetParts.slice(0, 2).concat([data[simbid].canonical]).join("/")
                }
                 return facet;
        });
        that.store.dispatch(that.actions.data_received(enhancedResponse, id));

      };

      var request = new ApiRequest({
        target: ApiTargets.SERVICE_OBJECTS,
        options : {
          type : "POST",
          contentType : "application/json",
          data: JSON.stringify({ identifiers : simbids  }),
          done : done
        }
      });

      this.getBeeHive().getService("Api").request(request);

    };

    widget.submitFilter = function (operator) {

      var q = this.getCurrentQuery().clone();
      q.unlock();

      var facetField = this.store.getState().config.facetField;

      var conditions = Reducers.getActiveFacets(this.store.getState(), this.store.getState().state.selected)
          .map(function (c) {
            return facetField + ":\"" + c + "\"";
          });

      var fieldName = 'fq_' + facetField.replace("_facet_hier", "");

      if (operator == 'and' || operator == 'limit to') {
        this.queryUpdater.updateQuery(q, fieldName, 'limit', conditions);
      }
      else if (operator == 'or') {
        this.queryUpdater.updateQuery(q, fieldName, 'expand', conditions);
      }
      else if (operator == 'exclude' || operator == 'not') {
        if (q.get(fieldName)) {
          this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
        }
        else {
          conditions.unshift('*:*');
          this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
        }
      }

      var fq = '{!type=aqp v=$' + fieldName + '}';
      var fqs = q.get('fq') || [];
      fqs.push(fq);
      q.set('fq', _.unique(fqs));

      // In the case of the SIMBAD objects facet, we need to do something in order to get the
      // object names into the breadcrumbs. Without doing the exercise below, we would end up
      // with the SIMBAD identifiers in the breadcrumbs, rather than the corresponding object
      // names. If somebody selects multiple entries in the facet simultaneously, we can rely on
      // the contents of "conditions" to make this translation, but this fails with applying
      // filters sequentially. Here the cache we created in the object facet widget comes to the
      // rescue, which we can access as "self._cache".
      if (facetField == 'simbad_object_facet_hier') {
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
        var current_values = q.get('__simbad_object_facet_hier_fq_simbad_object_facet_hier');
        // The array 'new_values' will replace 'current_values'
        var new_values = [];
        for (var e in current_values) {
          var val = current_values[e];
          // Get the SIMBAD identifier, taking into account that there may be a trailing bracket
          var identifier = val.split('/').pop().replace(/\)$/, "");
          // Get the associated canonical object name (or 'none')
          var oname = this._cache.getIfPresent(identifier);
          // Add the object name to the list with new values or keep the old value
          if (oname) {
            new_values[e] = oname;
          } else {
            new_values[e] = identifier;
          }
        }
        q.set('__simbad_object_facet_hier_fq_simbad_object_facet_hier', new_values);
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