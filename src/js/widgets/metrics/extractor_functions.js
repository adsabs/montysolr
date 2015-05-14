define([

], function () {

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
    ];

  };

  //function that creates a plot for the Reads histogram
  DataExtractor.plot_readshist = function (options) {
    var data, returnArray = [];

    if (!options.norm)
      data = [options.readshist_data["refereed reads"], getNonRef(options.readshist_data["refereed reads"], options.readshist_data["all reads"])];
    else
      data = [options.readshist_data["refereed reads normalized"], getNonRef(options.readshist_data["all reads normalized"], options.readshist_data["refereed reads normalized"])];

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
    ];

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


    return  [
      {key: "Ref. citations to ref. papers", values: returnArray[0]},
      {key: "Ref. citations to non ref. papers", values: returnArray[1]},
      {key: "Non ref. citations to ref. papers", values: returnArray[2]},
      {key: "Non ref. citations to non ref. papers", values: returnArray[3]}
    ];

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
