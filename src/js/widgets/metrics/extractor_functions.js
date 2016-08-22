define([

], function () {

  function hasNonZero (arr){
    return arr.filter(function(x){ return x.y > 0}).length > 0;
  }

var DataExtractor = {};

  function getNonRef(ref, all){
    var nonRef = {};
    _.each(all, function(v, k){
      if (ref[k]){
        nonRef[k] = v - ref[k];
      }
      else {
        nonRef[k] = v;
      }
    })
    return nonRef
  }

//function that create a plot for the paper history
  DataExtractor.plot_paperhist = function (options) {

    var data, returnArray = [],  p = options.paperhist_data;

    if (!options.norm)
      data = [p["refereed publications"], getNonRef(p["refereed publications"], p["all publications"])]
    else
      data = [p["refereed publications normalized"], getNonRef(p["refereed publications normalized"], p["all publications normalized"])];

    _.each(data, function (a, index) {
      var transformedArray = [];
      _.each(a, function (v,k) {
        transformedArray.push({x: k, y: v });
      });
      returnArray.push(transformedArray);
    });

    return [
      {key: "Refereed", values: returnArray[0]},
      {key: "Non-refereed", values: returnArray[1]}
    ].filter(
         function(x, i){
            return hasNonZero(x.values);
         }
       );

  };

  //function that creates a plot for the Reads histogram
  DataExtractor.plot_readshist = function (options) {
    var data, returnArray = [];

    if (!options.norm)
      data = [options.readshist_data["refereed reads"], getNonRef(options.readshist_data["refereed reads"], options.readshist_data["all reads"])];
    else
      data = [options.readshist_data["refereed reads normalized"], getNonRef(options.readshist_data["refereed reads normalized"], options.readshist_data["all reads normalized"])];

    _.each(data, function (a, index) {
      var transformedArray = [];
      _.each(a, function (v,k) {
        transformedArray.push({x: k, y: v });
      });
      returnArray.push(transformedArray);
    });

    return [
      {key: "Refereed", values: returnArray[0]},
      {key: "Non-refereed", values: returnArray[1]}
    ].filter(
         function(x, i){
            return hasNonZero(x.values);
         }
       );

  };

//function that creates a plot for the Citations histogram
  DataExtractor.plot_citshist = function (options) {

    var returnArray = [], data;
    var c = options.citshist_data;

    if (!options.norm)
      data = [c["refereed to refereed"], c["refereed to nonrefereed"], c["nonrefereed to refereed"], c["nonrefereed to nonrefereed"]];
    else
      data = [c["refereed to refereed normalized"], c["refereed to nonrefereed normalized"], c["nonrefereed to refereed normalized"], c["nonrefereed to nonrefereed normalized"]]

    _.each(data, function (a, index) {
      var transformedArray = [];
      _.each(a, function (v,k) {
        transformedArray.push({x: k, y: v });
      });
      returnArray.push(transformedArray);
    });

    //now, filter to only include arrays with at least 1 non-zero val

    return [
     "Ref. citations to ref. papers",
     "Ref. citations to non ref. papers",
     "Non ref. citations to ref. papers",
     "Non ref. citations to non ref. papers"
   ].map(
     function(x,i){
       return {
         key : x,
         values : returnArray[i]
       }
     }
   )
   .filter(
     function(x, i){
        return hasNonZero(x.values);
     }
   )

  };

  //function that creates a plot for the indexes graph
  DataExtractor.plot_series = function(options) {

    var data_to_plot = options.series_data;
    var data = [ data_to_plot.h, data_to_plot.g, data_to_plot.i10, data_to_plot.tori, data_to_plot.i100, data_to_plot.read10 ];
    var returnArray = [];

    _.each(data, function (a, index) {
      var transformedArray = [];
      _.each(a, function (v,k) {
        transformedArray.push({x: k, y: v });
      });
      returnArray.push(transformedArray);
    });

    //normalize read10 data by dividing it by 10
    returnArray[5] = _.map(returnArray[5], function(obj){
      return {x : obj.x, y: obj.y/10 }
    });

    return [
      {key: "h Index", values: returnArray[0]},
      {key: "g Index", values: returnArray[1]},
      {key: "i10 Index", values: returnArray[2]},
      {key: "tori Index", values: returnArray[3]},
      {key: "i100 Index", values: returnArray[4]},
      {key: "read10 Index", values: returnArray[5]}
    ];

  };

  return DataExtractor;

})
