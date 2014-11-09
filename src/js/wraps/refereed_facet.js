define(['js/widgets/facet/factory' ], function ( FacetFactory) {

  return function() {
    var widget = FacetFactory.makeBasicCheckboxFacet({
      facetField: "property",
      facetTitle: "Refereed Status",
      openByDefault: true,
      defaultQueryArguments: {
        "facet": "true",
        "facet.mincount": "1",
        "fl": "id",
        "facet.query": 'property:refereed'
      },
      // this is optimization, we'll execute only one query (we don't even facet on
      // other values). There is a possibility is is OK (but could also be wrong;
      // need to check)
      extractionProcessors: function (apiResponse) {
        var returnList = [];
        if (apiResponse.get('response.numFound') <= 0) {
          return returnList;
        }

        if (apiResponse.has('facet_counts.facet_queries')) {
          var queries = apiResponse.get('facet_counts.facet_queries');
          var v, found = 0;
          _.each(_.keys(queries), function (k) {
            v = queries[k];
            if (k.indexOf(':refereed') > -1) {
              found = v;
              returnList.push("refereed", v);
            }
          });

          returnList.push('notrefereed', apiResponse.get('response.numFound') - found);
          return returnList;
        }
      },
      logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['invalid choice']}

    });

    widget.handleLogicalSelection = function (operator) {
      var q = this.getCurrentQuery();
      var paginator = this.findPaginator(q).paginator;
      var conditions = this.queryUpdater.removeTmpEntry(q, 'SelectedItems');

      //XXX:rca - hack ; this logic is triggerd multiple times
      // we need to prevent that

      var self = this;

      if (conditions && _.keys(conditions).length > 0) {


        conditions = _.values(conditions);
        _.each(conditions, function (c, i, l) {
          l[i] = 'property:' + self.queryUpdater.escapeInclWhitespace(c.value);
        });

        q = q.clone();

        var fieldName = 'fq_' + this.facetField;

        if (operator == 'and' || operator == 'limit to') {
          this.queryUpdater.updateQuery(q, fieldName, 'limit', conditions);
        }
        else if (operator == 'or') {
          this.queryUpdater.updateQuery(q, fieldName, 'expand', conditions);
        }
        else if (operator == 'exclude' || operator == 'not') {
          this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
        }

        var fq = '{!type=aqp v=$' + fieldName + '}';
        var fqs = q.get('fq');
        if (!fqs) {
          q.set('fq', [fq]);
        }
        else {
          var i = _.indexOf(fqs, fq);
          if (i == -1) {
            fqs.push(fq);
          }
          q.set('fq', fqs);
        }

        this.dispatchNewQuery(paginator.cleanQuery(q));
      }
    };
    return widget;
  };

});