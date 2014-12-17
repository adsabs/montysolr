
define([
    'underscore',
    'js/widgets/list_of_things/details_widget'
  ],
  function (
    _,
    ListOfThingsWidget
    ) {

    var Widget = ListOfThingsWidget.extend({
      customizeQuery: function(apiQuery) {
        var bibcode = this.extractValueFromQuery(apiQuery, 'q', 'bibcode');
        if (!bibcode) {
          return; // ignore
        }
        var q = ListOfThingsWidget.prototype.customizeQuery.apply(this, arguments);

        if (bibcode[13] == 'E'){ //take first fourteen
          q.set('q', 'bibcode:' + _.first(bibcode, 14).join("")+"*");
        }
        else { //take first thirteen
          q.set('q', 'bibcode:' + _.first(bibcode, 13).join("")+"*");
        }

        if (this.sortOrder){
          q.set("sort", this.sortOrder);
        }
        if (this.queryOperator) {
          q.set('q', this.queryOperator + '(' + q.get('q').join(' ') + ')');
        }
        return q;
      },

      description : "List of papers in the same volume as"

    });

    return Widget;
  });
