define([
  'jquery',
  'js/bugutils/minimal_pubsub',
  'js/components/api_query',
  './test_json/test1',
  './test_json/test2',
  'js/widgets/list_of_things/widget',
  'js/widgets/list_of_things/details_widget',
  'js/wraps/citations',
  'js/wraps/coreads',
  'js/wraps/references',
  'js/wraps/table_of_contents'

], function(
  $,
  MinPubSub,
  ApiQuery,
  test1,
  test2,
  LoTWidget,
  DetailsLoTWidget,
  CitationWidget,
  CoreadsWidget,
  ReferencesWidget,
  TableOfContentsWidget
  ){

  describe("Various show-detail LoT Widgets (lot_derivates.spec.js)", function(){

    var minsub, counter;

    beforeEach(function(){
      counter = 0;
      minsub = new (MinPubSub.extend({
        request: function(apiRequest) {
          counter++;
          var q = apiRequest.get('query');
          var ret = test1();
          if (counter % 2 == 0)
            ret = test2();

          _.each(q.keys(), function(k) {
            ret.responseHeader.params[k] = q.get(k)[0];
          });
          //_.extend(ret.responseHeader.params, q.toJSON());
          return ret;
        }
      }))({verbose: false});
    });



    it("Extends Details LoT widget", function() {
      expect(new DetailsLoTWidget()).to.be.instanceof(LoTWidget);
      expect(new CitationWidget()).to.be.instanceof(DetailsLoTWidget);
      expect(new ReferencesWidget()).to.be.instanceof(DetailsLoTWidget);
      expect(new CoreadsWidget()).to.be.instanceof(DetailsLoTWidget);
      expect(new TableOfContentsWidget()).to.be.instanceof(DetailsLoTWidget);
    });

    it("Details LoT widget has certain methods", function() {
      var widget = new DetailsLoTWidget();
      expect(widget.extractValueFromQuery(new ApiQuery({'q': 'bibcode:foo'}), 'q', 'bibcode')).to.eql('foo');
      expect(widget.extractValueFromQuery(new ApiQuery({'q': '"bibcode:foo"'}), 'q', 'bibcode')).to.eql('foo');
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

      expect($w.find(".s-list-description").text()).to.eql("Papers referenced by");

      widget.trigger("page-manager-message", "broadcast-payload", {title: "foo"})

      expect($w.find(".s-article-title").text()).to.eql("foo");


    });

    it("Show coreads", function(){
      var widget = new CoreadsWidget();
      widget.activate(minsub.beehive.getHardenedInstance());

      var $w = widget.render().$el;
      //$('#test').append($w);

      minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'foo:bar'}));
      expect($w.find("label").length).to.equal(20);

      expect($w.find(".s-list-description").text()).to.eql("Papers also read by those who read");

      widget.trigger("page-manager-message", "broadcast-payload", {title: "foo"})

      expect($w.find(".s-article-title").text()).to.eql("foo");


    });

    it("Show TableOfContents", function(){
      var widget = new TableOfContentsWidget();
      widget.activate(minsub.beehive.getHardenedInstance());

      var $w = widget.render().$el;
      //$('#test').append($w);

      minsub.publish(minsub.DISPLAY_DOCUMENTS, new ApiQuery({'q': 'bibcode:bar'}));
      expect($w.find("label").length).to.equal(20);

      expect($w.find(".s-list-description").text()).to.eql("Papers in the same volume as");

      widget.trigger("page-manager-message", "broadcast-payload", {title: "foo"})

      expect($w.find(".s-article-title").text()).to.eql("foo");


    });

  })
});