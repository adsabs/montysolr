

define([
   "js/bugutils/minimal_pubsub",
  'js/components/history_manager'
],
    function(
        Minsub,
        HistoryManager
    ){

  describe("History Manager (Component)", function(){

   it("should record navigation signals and allow the previous and penultimate nav events to be queried", function(){

      var manager = new HistoryManager();

     var minsub = new (Minsub.extend({
       request: function(apiRequest) {
         return {some: 'foo'}
       }
     }))({verbose: false});

     manager.activate(minsub.beehive.getHardenedInstance());

     minsub.publish(minsub.NAVIGATE, "index-page");

     minsub.publish(minsub.NAVIGATE, "results-page");

     expect(JSON.stringify(manager.getCurrentNav())).to.eql('["results-page",{}]');
     expect(JSON.stringify(manager.getPreviousNav())).to.eql('["index-page",{}]');



   })
  })
});