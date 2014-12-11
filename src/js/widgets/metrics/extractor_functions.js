
/*
*
* Mostly by Edwin, with some modification by Alex
*
* */

define([

], function () {

  //function that checks if a string is actually a number
  function isNumber(o) {
    return !isNaN(o - 0);
  };

//function that rounds a number to a certain decimal
  function roundNumber(num, dec) {
    var result = Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
    return result;
  }

//function to check if a number is an integer or not
  function isInt(n) {
    return n % 1 == 0;
  }

//function to check if a number is a float
  function isFloat(n) {
    return n === +n && n !== (n | 0);
  }

  var DataExtractor = {};


  /**
   * Javascript for the Metrics page
   */

//function that extracts the data for a plot
  function extract_data_to_plot(variable, multidata, startidx) {
    //I check the optional parameters
    if (multidata == null)
      multidata = false;
    if (startidx == null)
      startidx = 0;

    //#######
    //common operations
    //value for max y
    var max_y = 0;

    var years = new Array();
    //I extract the indexes
    for (j in variable) {
      if (isNumber(j)) {
        years.push(j)
      }
    }
    years.sort();

    //if I have only year and 2 kind of data to plot
    if (!multidata) {
      //basic variables where to store data
      var data1_refereed_to_plot = new Array();
      var data2_not_refereed_to_plot = new Array();

      for (x in years) {
        //I extract the year
        var i = years[x];
        //I extract the data
        var data_assoc = variable[i];
        //I split the two values
        data_assoc = data_assoc.split(':');
        //I convert the values in numbers
        if (isNumber(data_assoc[startidx]))
          var data0_total = parseFloat(data_assoc[startidx]);
        else
          var data0_total = 0;
        if (isNumber(data_assoc[startidx + 1]))
          var data1_refereed = parseFloat(data_assoc[startidx + 1]);
        else
          var data1_refereed = 0;
        //I extract the not refereed papers
        var data2_not_refereed = data0_total - data1_refereed;

        data1_refereed_to_plot.push(new Array(parseInt(i), data1_refereed));
        data2_not_refereed_to_plot.push(new Array(parseInt(i), data2_not_refereed))
        //I set the max value for the y axis
        if (data0_total > max_y)
          max_y = data0_total;
      }

      //I add some space on top of the y axis of the plot
      var incr = Math.round((max_y * 5) / 100);
      if (incr == 0)
        incr = 1;
      var max = max_y + incr;
      return new Array(data1_refereed_to_plot, data2_not_refereed_to_plot, max);
    }
    else {
      //basic variables where to store data
      var data_to_plot = new Array();

      //I calculate how many values there are in the data
      var base_year = years[0];
      var base_data_assoc = variable[base_year];
      base_data_assoc = base_data_assoc.split(':');
      var num_of_params_to_plot = base_data_assoc.length;
      //I create an array for each parameter to store the data
      for (var p = 0; p < num_of_params_to_plot; p++) {
        data_to_plot.push(new Array());
      }

      for (x in years) {
        //I extract the year
        var i = years[x];
        //I extract the data
        var data_assoc = variable[i];
        //I split the values
        data_assoc = data_assoc.split(':');
        //for each value I convert it in a number
        for (var p = 0; p < num_of_params_to_plot; p++) {
          if (isNumber(data_assoc[p])) {
            var datap = parseFloat(data_assoc[p]);
            if (!isInt(datap))
              datap = roundNumber(datap, 1)
          }
          else
            var datap = 0;
          //and then I append the value to the right parameter to return
          data_to_plot[p].push(new Array(i, datap));
        }
      }

      return data_to_plot;

    }
  };

//function that create a plot for the paper history
  DataExtractor.plot_paperhist = function (options) {

    //I extract the data to plot
    //if I don't have to extract the normalized values I start to take the values from 0
    if (!options.norm)
      var data = extract_data_to_plot(options.paperhist_data, false, 0)
    //otherwise I start to take the values from 2
    else
      var data = extract_data_to_plot(options.paperhist_data, false, 2)

    var refereed = data[0];

    var notrefereed = data[1];

    var returnArray = [refereed, notrefereed];

    _.each(returnArray, function (a, index) {
      returnArray[index] = _.map(a, function (list) {
        return {x: list[0], y: list[1]}
      })
    })

    var vals = [];

    vals.push({key: "Refereed", values: returnArray[0]});
    vals.push({key: "Non-refereed", values: returnArray[1]});

    return vals;

  };


//function that creates a plot for the Citations histogram
  DataExtractor.plot_citshist = function (options) {
    //I extract the data to plot
    var data = extract_data_to_plot(options.citshist_data, true);

    if (!options.norm)
      data = data.slice(0, 4);
    else
      data = data.slice(4);

    //then I extract the 4 lists of data
    var from_all_to_all = data[0];
    var from_ref_to_all = data[1];
    var from_all_to_ref = data[2];
    var from_ref_to_ref = data[3];
    //then I define the array to compute what I need
    var from_all_to_notref = new Array(),
      from_ref_to_notref = new Array(),
      from_notref_to_ref = new Array(),
      from_notref_to_notref = new Array(),
      max = 0;
    //then I compute the missing lists
    for (var i = 0; i < from_all_to_all.length; i++) {
      //I grab the numbers from the two lists I need
      from_all_to_notref.push(new Array(from_all_to_all[i][0], (from_all_to_all[i][1] - from_all_to_ref[i][1])));
      from_ref_to_notref.push(new Array(from_ref_to_all[i][0], (from_ref_to_all[i][1] - from_ref_to_ref[i][1])));
      from_notref_to_ref.push(new Array(from_all_to_ref[i][0], (from_all_to_ref[i][1] - from_ref_to_ref[i][1])));
      from_notref_to_notref.push(new Array(from_all_to_notref[i][0], (from_all_to_notref[i][1] - from_ref_to_notref[i][1])));
      //I check if the max value of the current data is higher than the one I already have
      if (from_all_to_all[i][1] > max)
        max = from_all_to_all[i][1];
    }

    var returnArray = [from_ref_to_ref, from_ref_to_notref, from_notref_to_ref, from_notref_to_notref];

    _.each(returnArray, function (a, index) {
      returnArray[index] = _.map(a, function (list) {
        return {x: list[0], y: list[1]}
      })
    })

    var vals = [];

    vals.push({key: "Ref. citations to ref. papers", values: returnArray[0]})
    vals.push({key: "Ref. citations to non ref. papers", values: returnArray[1]})
    vals.push({key: "Non ref. citations to ref. papers", values: returnArray[2]})
    vals.push({key: "Non ref. citations to non ref. papers", values: returnArray[3]})


    return vals

  };


//function that creates a plot for the Reads histogram
  DataExtractor.plot_readshist = function (options) {
    //I extract the data to plot
    //if I don't have to extract the normalized values I start to take the values from 0
    if (!options.norm)
      var data = extract_data_to_plot(options.readshist_data, false, 0)
    //otherwise I start to take the values from 2
    else
      var data = extract_data_to_plot(options.readshist_data, false, 2)

    var refereed = data[0];

    var notrefereed = data[1];

    var returnArray = [refereed, notrefereed];

    _.each(returnArray, function (a, index) {
      returnArray[index] = _.map(a, function (list) {
        return {x: list[0], y: list[1]}
      })
    })

    var vals = [];

    vals.push({key: "Refereed", values: returnArray[0]});
    vals.push({key: "Non-refereed", values: returnArray[1]});


    return vals;

  };


  //function that creates a plot for the indexes graph
  DataExtractor.plot_series = function(options) {

    //I extract the data to plot
    var data_to_plot = extract_data_to_plot(options.series_data, true);

    var hIndex = data_to_plot[0],
        gIndex = data_to_plot[1],
        i10Index = data_to_plot[2],
        i100Index = data_to_plot[6],
        toriIndex = data_to_plot[3],
        read10Index = data_to_plot[7];

    /*these are not being shown, maybe in the future?
    var mIndex = data_to_plot[4],
        read10Index = data_to_plot[7],
        roqIndex = data_to_plot[5];
     */

    var returnArray = [hIndex, gIndex, i10Index, i100Index, toriIndex, read10Index];

    _.each(returnArray, function (a, index) {
      returnArray[index] = _.map(a, function (list) {
        return {x: list[0], y: list[1]}
      })
    })

    var vals = [];

    vals.push({key: "h Index", values: returnArray[0]});
    vals.push({key: "g Index", values: returnArray[2]});
    vals.push({key: "i10 Index", values: returnArray[3]});
    vals.push({key: "i100 Index", values: returnArray[4]});
    vals.push({key: "tori Index", values: returnArray[5]});
    vals.push({key: "read10 Index", values: returnArray[5]});

    return vals;

  };



  return DataExtractor;


})
