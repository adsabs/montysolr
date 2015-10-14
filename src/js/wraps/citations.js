
define([
    'js/widgets/list_of_things/details_widget'
  ],

  function (
    DetailsWidget
    ) {

    var Citations = function(){
      var options = {
        queryOperator : "citations",
        sortOrder : "date desc",
        description : "Papers which cite",
        operator : true
      }
      return new DetailsWidget(options);
    }

    return Citations

  });
