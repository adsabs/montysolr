
define([
    'js/widgets/list_of_things/details_widget'
  ],

  function ( DetailsWidget) {

    var CoReads = function(){
      var options = {
        queryOperator : "trending",
        description : "Papers also read by those who read",
        operator : true,
        sortOrder : "date desc",
        //don't allow the record itself to be returned in trending search results
        removeSelf : true
      };
      return new DetailsWidget(options);
    }


    return CoReads;

  });
