define([
  './widget'
],
  function(
    ListOfThings
    ) {
    var DetailsWidget = ListOfThings.extend({
      defaultQueryArguments: {
        fl: 'id',
        rows : 25,
        start : 0
      },
      customizeQuery: function() {
        var q = ListOfThings.prototype.customizeQuery.apply(this, arguments);
        if (this.sortOrder){
          q.set("sort", this.sortOrder)
        }
      }
    });
    return DetailsWidget;
  });