define(['js/widgets/facet/factory' ], function ( FacetFactory) {

  return function() {
    var widget = FacetFactory.makeHierarchicalCheckboxFacet({
      facetField: "grant_facet_hier",
      facetTitle: "Grants",
      openByDefault: false,
      logicOptions: {single: ['limit to', 'exclude'], multiple: ["or", "exclude"]},
      responseProcessors: [
        function (v) {
          var vv = v.split('/');
          return vv[vv.length - 1]
        }
      ]
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
          l[i] = "grant:\"" + c.title + "\"";
        });

        q = q.clone();

        var fieldName = 'fq_grant';

        if (operator == 'and' || operator == 'limit to') {
          this.queryUpdater.updateQuery(q, fieldName, 'limit', conditions);
        }
        else if (operator == 'or') {
          this.queryUpdater.updateQuery(q, fieldName, 'expand', conditions);
        }
        else if (operator == 'exclude' || operator == 'not') {
          this.queryUpdater.updateQuery(q, fieldName, 'exclude', conditions);
        }

        var fq = '{!type=aqp cache=false cost=150 v=$' + fieldName + '}';
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
        q.unset('facet.prefix');
        q.unset('facet');
        this.dispatchNewQuery(paginator.cleanQuery(q));
      }
    };
    return widget;
  };

});