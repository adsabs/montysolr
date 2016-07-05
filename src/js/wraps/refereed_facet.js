define([
  'js/widgets/facet/factory',
  'analytics'
], function (
  FacetFactory,
  analytics
  ) {

  return function() {
    var widget = FacetFactory.makeBasicCheckboxFacet({
      facetField: "property",
      facetTitle: "Refereed",
      openByDefault: true,
      defaultQueryArguments: {
        "facet.query": 'property:refereed',
      },

      preprocessors: function (facetList) {
       return facetList
          .filter(function(f){ return (f.value === "notrefereed" || f.value === "refereed") })
          .map(function(f) {
            if (f.name === "notrefereed") f.name = "non-refereed";
            return f;
          });
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

        analytics('send', 'event', 'interaction', 'facet-applied', JSON.stringify({name : this.facetField, logic : operator, conditions : conditions }));

      }
    };
    return widget;
  };

});