define([
  'jquery',
  'js/bugutils/minimal_pubsub',
  'js/components/api_query',
  './test_json/test1',
  './test_json/test2',
  'js/wraps/citations',
  'js/wraps/coreads',
  'js/wraps/references'

], function(
  $,
  MinPubSub,
  ApiQuery,
  test1,
  test2,
  CitationWidget,
  CoreadsWidget,
  ReferencesWidget
  ){

  describe("Various show-detail Widgets (Based on ListOfThings)", function(){

    var minsub, counter;

    beforeEach(function(){
      counter = 0;
      minsub = new (MinPubSub.extend({
        request: function(apiRequest) {
          counter++;
          var q = apiRequest.get('query');
          var ret = test1;
          if (counter % 2 == 0)
            ret = test2;

          ret = _.clone(ret);
          _.each(q.keys(), function(k) {
            ret.responseHeader.params[k] = q.get(k)[0];
          });
          //_.extend(ret.responseHeader.params, q.toJSON());
          return ret;
        }
      }))({verbose: false});
    });



    it("Show citations", function(){
      var widget = new CitationWidget();
      widget.activate(minsub.beehive.getHardenedInstance());

      var $w = widget.render().$el;
      //$('#test').append($w);

      minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'foo:bar'}));
      expect($w.find("label").length).to.equal(20);
    });

    it("Show references", function(){
      var widget = new ReferencesWidget();
      widget.activate(minsub.beehive.getHardenedInstance());

      var $w = widget.render().$el;
      //$('#test').append($w);

      minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'foo:bar'}));
      expect($w.find("label").length).to.equal(20);
    });

    it("Show coreads", function(){
      var widget = new CoreadsWidget();
      widget.activate(minsub.beehive.getHardenedInstance());

      var $w = widget.render().$el;
      //$('#test').append($w);

      minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'foo:bar'}));
      expect($w.find("label").length).to.equal(20);
    });


  })
});