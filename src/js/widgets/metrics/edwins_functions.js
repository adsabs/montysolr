define([

], function(

  ){

  //function that checks if a string is actually a number
  function isNumber (o)
  {
    return ! isNaN (o-0);
  };

//function that rounds a number to a certain decimal
  function roundNumber(num, dec)
  {
    var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
    return result;
  }
//function to check if a number is an integer or not
  function isInt(n)
  {
    return n % 1 == 0;
  }
//function to check if a number is a float
  function isFloat(n)
  {
    return n===+n && n!==(n|0);
  }

  var dataExtractorObject = {};





})


/**
 * Javascript for the Metrics page
 */



//some global variables to draw the plots
var MODE_PAPERHIST = 'hist';
var MODE_CITSHIST = 'hist';
var MODE_READSHIST = 'hist';
var MODE_SERIES = 'points';
var SIZE_PAPERHIST = 1;
var SIZE_CITSHIST = 1;
var SIZE_READSHIST = 1;
var SIZE_SERIES = 1;
var SIZE_PAPERHIST = 1;
var NORM_PAPERHIST = false;
var NORM_CITSHIST = false;
var NORM_READSHIST = false;
var NORM_SERIES = false;

//function that extracts the data for a plot
function extract_data_to_plot(variable, multidata, startidx)
{
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
  for (j in variable)
  {
    if(isNumber(j))
    {
      years.push(j)
    }
  }
  years.sort();

  //if I have only year and 2 kind of data to plot
  if (!multidata)
  {
    //basic variables where to store data
    var data1_refereed_to_plot = new Array();
    var data2_not_refereed_to_plot = new Array();

    for (x in years)
    {
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
      if (isNumber(data_assoc[startidx+1]))
        var data1_refereed = parseFloat(data_assoc[startidx+1]);
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
  else
  {
    //basic variables where to store data
    var data_to_plot = new Array();

    //I calculate how many values there are in the data
    var base_year = years[0];
    var base_data_assoc = variable[base_year];
    base_data_assoc = base_data_assoc.split(':');
    var num_of_params_to_plot = base_data_assoc.length;
    //I create an array for each parameter to store the data
    for (var p=0; p < num_of_params_to_plot; p++)
    {
      data_to_plot.push(new Array());
    }

    for (x in years)
    {
      //I extract the year
      var i = years[x];
      //I extract the data
      var data_assoc = variable[i];
      //I split the values
      data_assoc = data_assoc.split(':');
      //for each value I convert it in a number
      for (var p=0; p < num_of_params_to_plot; p++)
      {
        if (isNumber(data_assoc[p]))
        {
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
dataExtractorObject.plot_paperhist = function(){
  {
    //I extract the data to plot
    //if I don't have to extract the normalized values I start to take the values from 0
    if (!NORM_PAPERHIST)
      var data = extract_data_to_plot(paperhist_data, false, 0)
    //otherwise I start to take the values from 2
    else
      var data = extract_data_to_plot(paperhist_data, false, 2)


    var data1_refereed_to_plot = data[0];
    var data2_not_refereed_to_plot = data[1];

    //more processing here


  }



//function that creates a plot for the Citations histogram
function plot_citshist()
{
  //I extract the data to plot
  var data = extract_data_to_plot(citshist_data, true)
  if (!NORM_CITSHIST)
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
  for (var i=0; i<from_all_to_all.length; i++)
  {
    //I grab the numbers from the two lists I need
    from_all_to_notref.push(new Array(from_all_to_all[i][0], (from_all_to_all[i][1] - from_all_to_ref[i][1])));
    from_ref_to_notref.push(new Array(from_ref_to_all[i][0], (from_ref_to_all[i][1] - from_ref_to_ref[i][1])));
    from_notref_to_ref.push(new Array(from_all_to_ref[i][0], (from_all_to_ref[i][1] - from_ref_to_ref[i][1])));
    from_notref_to_notref.push(new Array(from_all_to_notref[i][0], (from_all_to_notref[i][1] - from_ref_to_notref[i][1])));
    //I check if the max value of the current data is higher than the one I already have
    if (from_all_to_all[i][1] > max)
      max = from_all_to_all[i][1];
  }
  var incr = Math.round((max * 5) / 100);
  if (incr == 0)
    incr = 1;
  var max = max + incr;

  //console.log(from_all_to_all)
  //console.log(from_ref_to_ref)
  //console.log(from_ref_to_notref)
  //console.log(from_notref_to_ref)
  //console.log(from_notref_to_notref)
  //return

  //I define the size of the plot
  var plot_size = 300 * SIZE_CITSHIST;
  $("#citshist").css('height', plot_size+'px').css('width', plot_size+'px');

  //I define the kind of plot I want
  //by default I want a histogram
  var show_bar = true;
  var show_lines = false;
  var show_points = false;
  if (MODE_CITSHIST == 'hist')
  {
    show_bar = true;
    show_lines = false;
  }
  else if (MODE_CITSHIST == 'lines')
  {
    show_bar = false;
    show_lines = true;
  }
  else if (MODE_CITSHIST == 'points')
  {
    show_bar = false;
    show_lines = true;
    show_points = true;
  }

  //I set Up the options
  var options = {
    //bars: {show: true, barWidth: 0.8},
    //lines: { show: true, fill: true, steps: false },
    xaxis: {tickDecimals: 0},
    yaxis: {min: 0, max: max, tickDecimals: 0},
    grid: { hoverable: true, clickable: true },
    legend:{container: $("#citshist_legend")},
    series: {
      stack: true,
      lines: { show: show_lines, fill: true, steps: false },
      bars: { show: show_bar, barWidth: 0.8 },
      points: { show: show_points },
    }
  };

  //I plot the graph
  $.plot($("#citshist"),
    [
      {
        label: "Ref. citations to ref. papers",
        data:from_ref_to_ref,
      },
      {
        label: "Ref. citations to non ref. papers",
        data:from_ref_to_notref,
      },
      {
        label: "Non ref. citations to ref. papers",
        data:from_notref_to_ref,
      },
      {
        label: "Non ref. citations to non ref. papers",
        data:from_notref_to_notref,
      }
    ],
    options
  );

  //I bind the event of the mouse over the graph to show the current value
  $("#citshist").bind("plothover", function (event, pos, item) {

    if (item) {
      //console.log([previousPoint, item.dataIndex, item])
      if ((previousPoint != item.dataIndex) || (previousLabel != item.series.label)) {
        previousPoint = item.dataIndex;
        previousLabel = item.series.label;

        $("#tooltip").remove();
        var x = item.datapoint[0];//the year in the plot
        var y = item.datapoint[1] - item.datapoint[2];//the single value without the staked
        //If it is a float I round it
        if (isFloat(y))
          y = roundNumber(y, 2);
        showTooltip(pos.pageX, pos.pageY,
            item.series.label + " <br/>Year: " + x + " <br/>Citations: " + y);
      }
    }
    else {
      $("#tooltip").remove();
      previousPoint = null;
      previousLabel = null;
    }
  });
};

//function that changes the visualization mode
function show_plot_citshist(mode)
{
  if (mode == 'hist')
    MODE_CITSHIST = 'hist';
  else if (mode == 'lines')
    MODE_CITSHIST = 'lines';
  else if (mode == 'points')
    MODE_CITSHIST = 'points';
  plot_citshist();
};
//function that changes the size
function size_plot_citshist(size)
{
  if (parseInt(size) == 1)
    SIZE_CITSHIST = 1;
  else if (parseInt(size) == 2)
    SIZE_CITSHIST = 2;
  plot_citshist();
};
//function called from the interface to switch from regular to normalized mode
function switch_plot_citshist(mode, position, ids_start)
{
  if (mode == 'regular')
  {
    //first of all I change the parameter to switch from regular to normalized
    NORM_CITSHIST = false;
  }
  else if (mode == 'normalized')
  {
    //first of all I change the parameter to switch from regular to normalized
    NORM_CITSHIST = true;
  }

  //then I fix some CSS
  var this_id = ids_start + position;
  if (position == 'right')
  {
    var other_id = ids_start + 'left';
    $('#'+this_id).removeClass('plot_tabs_right_unselected');
    $('#'+other_id).addClass('plot_tabs_left_unselected');
    $('#'+this_id).off('click');
    $('#'+other_id).on('click', function(event){switch_plot_citshist('regular', 'left', 'citshist_tabs_');});
  }
  else if (position == 'left')
  {
    var other_id = ids_start + 'right';
    $('#'+this_id).removeClass('plot_tabs_left_unselected');
    $('#'+other_id).addClass('plot_tabs_right_unselected');
    $('#'+this_id).off('click');
    $('#'+other_id).on('click', function(event){switch_plot_citshist('normalized', 'right', 'citshist_tabs_');});
  }
  //finally I plot the graph
  plot_citshist();
};


//function that creates a plot for the Reads histogram
function plot_readshist()
{
  //I extract the data to plot
  //if I don't have to extract the normalized values I start to take the values from 0
  if (!NORM_READSHIST)
    var data = extract_data_to_plot(readshist_data, false, 0)
  //otherwise I start to take the values from 2
  else
    var data = extract_data_to_plot(readshist_data, false, 2)

  var data1_refereed_to_plot = data[0];
  var data2_not_refereed_to_plot = data[1];
  var max = data[2];

  //I define the size of the plot
  var plot_size = 300 * SIZE_READSHIST;
  $("#readshist").css('height', plot_size+'px').css('width', plot_size+'px');

  //I define the kind of plot I want
  //by default I want a histogram
  var show_bar = true;
  var show_lines = false;
  var show_points = false;
  if (MODE_READSHIST == 'hist')
  {
    show_bar = true;
    show_lines = false;
    show_points = false;
  }
  else if (MODE_READSHIST == 'lines')
  {
    show_bar = false;
    show_lines = true;
    show_points = false;
  }
  else if (MODE_READSHIST == 'points')
  {
    show_bar = false;
    show_lines = true;
    show_points = true;
  }

  //I set Up the options
  var options = {
    //bars: {show: true, barWidth: 0.8},
    //lines: { show: true, fill: true, steps: false },
    xaxis: {tickDecimals: 0},
    yaxis: {min: 0, max: max, tickDecimals: 0},
    grid: { hoverable: true, clickable: true },
    legend:{container: $("#readshist_legend")},
    series: {
      stack: true,
      lines: { show: show_lines, fill: true, steps: false },
      bars: { show: show_bar, barWidth: 0.8 },
      points: { show: show_points },
    }
  };

  //I plot the graph
  $.plot($("#readshist"),
    [
      {
        label: "Refereed",
        data:data1_refereed_to_plot,
        //color:'red'
      },
      {
        label: "Not Refereed",
        data:data2_not_refereed_to_plot,
        //color:'yellow'
      }
    ],
    options
  );

  //I bind the event of the mouse over the graph to show the current value
  $("#readshist").bind("plothover", function (event, pos, item) {

    if (item) {
      //console.log([previousPoint, item.dataIndex, item])
      if ((previousPoint != item.dataIndex) || (previousLabel != item.series.label)) {
        previousPoint = item.dataIndex;
        previousLabel = item.series.label;

        $("#tooltip").remove();
        var x = item.datapoint[0];//the year in the plot
        var y = item.datapoint[1] - item.datapoint[2];//the single value without the staked
        //If it is a float I round it
        if (isFloat(y))
          y = roundNumber(y, 2);

        showTooltip(pos.pageX, pos.pageY,
            item.series.label + " <br/>Year: " + x + " <br/>Reads: " + y);
      }
    }
    else {
      $("#tooltip").remove();
      previousPoint = null;
      previousLabel = null;
    }
  });
};

//function that changes the visualization mode
function show_plot_readshist(mode)
{
  if (mode == 'hist')
    MODE_READSHIST = 'hist';
  else if (mode == 'lines')
    MODE_READSHIST = 'lines';
  else if (mode == 'points')
    MODE_READSHIST = 'points';
  plot_readshist();
};
//function that changes the size
function size_plot_readshist(size)
{
  if (parseInt(size) == 1)
    SIZE_READSHIST = 1;
  else if (parseInt(size) == 2)
    SIZE_READSHIST = 2;
  plot_readshist();
};
//function called from the interface to switch from regular to normalized mode
function switch_plot_readshist(mode, position, ids_start)
{
  if (mode == 'regular')
  {
    //first of all I change the parameter to switch from regular to normalized
    NORM_READSHIST = false;
  }
  else if (mode == 'normalized')
  {
    //first of all I change the parameter to switch from regular to normalized
    NORM_READSHIST = true;
  }

  //then I fix some CSS
  var this_id = ids_start + position;
  if (position == 'right')
  {
    var other_id = ids_start + 'left';
    $('#'+this_id).removeClass('plot_tabs_right_unselected');
    $('#'+other_id).addClass('plot_tabs_left_unselected');
    $('#'+this_id).off('click');
    $('#'+other_id).on('click', function(event){switch_plot_readshist('regular', 'left', 'readshist_tabs_');});
  }
  else if (position == 'left')
  {
    var other_id = ids_start + 'right';
    $('#'+this_id).removeClass('plot_tabs_left_unselected');
    $('#'+other_id).addClass('plot_tabs_right_unselected');
    $('#'+this_id).off('click');
    $('#'+other_id).on('click', function(event){switch_plot_readshist('normalized', 'right', 'readshist_tabs_');});
  }
  //finally I plot the graph
  plot_readshist();
};



//function that creates a plot for the indexes graph
function plot_series()
{
  //I extract the data to plot
  var data_to_plot = extract_data_to_plot(series_data, true);

  //I define the size of the plot
  var plot_size = 300 * SIZE_SERIES;
  $("#series").css('height', plot_size+'px').css('width', plot_size+'px');

  //I define the kind of plot I want
  //by default I want lines with points
  var show_points = true;
  if (MODE_SERIES == 'points')
  {
    show_points = true;
  }
  else if (MODE_SERIES == 'lines')
  {
    show_points = false;
  }

  //I set Up the options
  var options = {
    //bars: {show: true, barWidth: 0.8},
    //lines: { show: true, fill: true, steps: false },
    xaxis: {tickDecimals: 0},
    yaxis: {
      min: 0,
      tickDecimals: 0,
    },
    grid: { hoverable: true, clickable: true },
    legend:{container: $("#series_legend")},
    lines: { show: true },
    points: { show: show_points },
  };
  var options_log = {
    //bars: {show: true, barWidth: 0.8},
    //lines: { show: true, fill: true, steps: false },
    xaxis: {tickDecimals: 0},
    yaxis: {
      min: 0.001,
      tickDecimals: 1,
      ticks: [0.1,1,10,100,1000,10000],
      transform:  function(v) {return Math.log(v+0.0001); /*move away from zero*/} ,
    },
    grid: { hoverable: true, clickable: true },
    legend:{container: $("#series_legend")},
    lines: { show: true },
    points: { show: show_points },
  };
  //I plot the graph
  $.plot($("#series"),
    [
      {
        label: "h-index",
        data:data_to_plot[0],
        //color:'red'
      },
      {
        label: "g-index",
        data:data_to_plot[1],
      },
      {
        label: "i10-index",
        data:data_to_plot[2],
      },
      {
        label: "i100-index",
        data:data_to_plot[6],
      },
      {
        label: "tori-index",
        data:data_to_plot[3],
      },
      {
        label: "read10-index",
        data:data_to_plot[7],
      },
      //for the moment I ignore the 4 and 5
      /*{
       label: "m-index",
       data:data_to_plot[4],
       },
       {
       label: "read10-index",
       data:data_to_plot[7],
       },
       {
       label: "roq-index",
       data:data_to_plot[5],
       }*/
    ],
    options
  );

  //I bind the event of the mouse over the graph to show the current value
  $("#series").bind("plothover", function (event, pos, item) {

    if (item) {
      //console.log([previousPoint, item.dataIndex, item])
      if ((previousPoint != item.dataIndex) || (previousLabel != item.series.label)) {
        previousPoint = item.dataIndex;
        previousLabel = item.series.label;

        $("#tooltip").remove();
        var x = item.datapoint[0],//the year in the plot
          y = item.datapoint[1];//the value

        showTooltip(pos.pageX, pos.pageY,
            item.series.label + " <br/>Year: " + x + " <br/>Value: " + y);
      }
    }
    else {
      $("#tooltip").remove();
      previousPoint = null;
      previousLabel = null;
    }
  });
}

//function that changes the visualization mode
function show_plot_series(mode)
{
  if (mode == 'lines')
    MODE_SERIES = 'lines';
  else if (mode == 'points')
    MODE_SERIES = 'points';
  plot_series();
};

//function that changes the size
function size_plot_series(size)
{
  if (parseInt(size) == 1)
    SIZE_SERIES = 1;
  else if (parseInt(size) == 2)
    SIZE_SERIES = 2;
  plot_series();
};

//function that shows the tooltip for the graph
function showTooltip(x, y, contents)
{
  if ($("#content").length > 0) {
    TARGET = "#content";
  } else {
    TARGET = "body";
  }
  $('<div id="tooltip">' + contents + '</div>').css( {
    position: 'absolute',
    display: 'none',
    top: y + 10,
    left: x + 10,
    border: '1px solid #777',
    padding: '2px',
    'background-color': '#EEE',
    opacity: 0.90,
    'font-size':'0.9em',
    'font-weight':'bold',
  }).appendTo(TARGET).fadeIn(200);
}


//when all the resources are available I plot the graphs
$(document).ready(
  function(){
    //check if I'm loading the initial form
    if ((typeof paperhist_data === 'undefined') && (typeof citshist_data === 'undefined') && (typeof readshist_data === 'undefined') && (typeof series_data === 'undefined'))
      return
    if (typeof paperhist_data !== 'undefined')
    {
      $("#paperhist_description").html('Publications per year');
      plot_paperhist();
      $('#paperhist_buttons').show();
    }
    if (typeof readshist_data !== 'undefined')
    {
      $("#readshist_description").html('Reads per year');
      plot_readshist();
      $('#readshist_buttons').show();
    }
    if (typeof citshist_data !== 'undefined')
    {
      $("#citshist_description").html('Citations per year');
      plot_citshist();
      $('#citshist_buttons').show();
    }
    if (typeof series_data !== 'undefined')
    {
      $("#series_description").html('Indices');
      plot_series();
      $('#series_buttons').show();
    }

    previousPoint = null;
    previousLabel = null;
  }
);
