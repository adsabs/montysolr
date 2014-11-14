define([
  'js/widgets/network_vis/network_widget'
], function(
  NetworkWidget
  ) {

  var options = {};

  options.endpoint = "author-network";

  options.networkType = "author";

  return new NetworkWidget(options);


})