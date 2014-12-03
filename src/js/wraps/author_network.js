define([
  'js/widgets/network_vis/network_widget'
], function(
  NetworkWidget
  ) {

  var options = {};

  options.endpoint = "services/vis/author-network";

  options.networkType = "author";

  return new NetworkWidget(options);


});