require(['js/components/beehive', 'js/services/pubsub', 'js/components/query_mediator', 'js/services/api',
  'jquery', 'underscore',
  'js/widgets/search_bar/search_bar_widget', 'js/widgets/results_render/results_render_widget'
], function(BeeHive, PubSub, QueryMediator, Api, $, _, SearchBar, ResultsRender) {


  var beehive = new BeeHive();
  beehive.addService('PubSub', new PubSub());
  beehive.addService('Api', new Api());
  var queryMediator = new QueryMediator();
  queryMediator.activate(beehive);

  var s = new SearchBar();
  console.log(s)
  var r = new ResultsRender()

  s.activate(beehive.getHardenedInstance())
  r.activate(beehive.getHardenedInstance())

  $("#top").append(s.render())
  $("#middle").append(r.render())




})
