
define([
    'js/widgets/list_of_things/details_widget'
  ],

  function (  DetailsWidget) {

    var References = function(){
      var options = {
        queryOperator : "references",
        sortOrder: "first_author asc",
        description : "Papers referenced by",
        //show how to get this info from solr
        operator : true
      }
      return new DetailsWidget(options);
    }

    return References;

  });
