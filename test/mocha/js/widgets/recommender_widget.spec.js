define([
  'js/widgets/recommender/widget',
  'js/components/json_response',
  'js/bugutils/minimal_pubsub'

], function (
  RecommenderWidget,
  JsonResponse,
  MinPubSub
  ) {

  describe("Recommender Widget (recommender_widget.spec.js)", function (){

    var testData = {"paper": "2010MNRAS.409.1719J", "recommendations": [
      {"bibcode": "1998ApJ...509..212S", "author": "Strong,+", "title": "Propagation of Cosmic-Ray Nucleons in the Galaxy"},
      {"bibcode": "1998ApJ...493..694M", "author": "Moskalenko,+", "title": "Production and Propagation of Cosmic-Ray Positrons and Electrons"},
      {"bibcode": "2007ARNPS..57..285S", "author": "Strong,+", "title": "Cosmic-Ray Propagation and Interactions in the Galaxy"},
      {"bibcode": "2011ApJ...737...67M", "author": "Murphy,+", "title": "Calibrating Extinction-free Star Formation Rate Diagnostics with 33 GHz Free-free Emission in NGC 6946"},
      {"bibcode": "1971JGR....76.7445R", "author": "Rygg,+", "title": "Balloon measurements of cosmic ray protons and helium over half a solar cycle 1965-1969"},
      {"bibcode": "1997ApJ...481..205H", "author": "Hunter,+", "title": "EGRET Observations of the Diffuse Gamma-Ray Emission from the Galactic Plane"},
      {"bibcode": "1978MNRAS.182..147B", "author": "Bell,+", "title": "The acceleration of cosmic rays in shock fronts - I."}
    ]};

    afterEach(function(){
      $("#test").empty();
    });

    it("should display a list of recommended articles", function(){

      var r = new RecommenderWidget();
      var $w = r.render().$el;

      $("#test").append($w);

      r.deferredObject = $.Deferred();

      r.processResponse(new JsonResponse(testData));

      debugger

      expect($w.find("li").length).to.eql(7);
      expect($w.find("li:first").text().trim()).to.eql('Propagation of Cosmic-Ray Nucleons in the Galaxy (Strong,+);');
    });


    it("should link directly to the abstract pages in Bumblebee", function(){

      var r = new RecommenderWidget();

      $("#test").append(r.render().el);

      r.deferredObject = $.Deferred();

      r.processResponse(new JsonResponse(testData));

      expect($("#test").find("li:first a").attr("href")).to.eql("#abs/1998ApJ...509..212S");
      expect($("#test").find("li:last a").attr("href")).to.eql("#abs/1978MNRAS.182..147B");
    });


    it("should have a help popover", function(){

      var r = new RecommenderWidget();

      $("#test").append(r.render().el);

      r.deferredObject = $.Deferred();

      r.processResponse(new JsonResponse(testData));
      
      expect($("#test").find("i.icon-help").data("content")).to.eql('These recommendations are based on a number of factors, including text similarity, citations, and co-readership information.')
    });

    it("extends from BaseWidget and can communicate with pubsub and its page controller through loadBibcodeData function", function() {

      var r = new RecommenderWidget();
      r.pubsub = {DELIVERING_REQUEST : "foo", publish : sinon.spy()}

      expect(r.loadBibcodeData).to.be.instanceof(Function);
      r.loadBibcodeData("fakeBibcode");

      var apiRequest = r.pubsub.publish.args[0][1];

      expect(apiRequest.toJSON().target).to.eql('recommender/fakeBibcode');
    });

//    it("Communicates through pubsub", function() {
//      var minsub = new (MinPubSub.extend({
//        request: function(apiRequest) {
//          return JSON.parse(JSON.stringify(testData));
//        }
//
//      }))({verbose: false});
//
//      var widget = new RecommenderWidget();
//      var onDisplayDocuments = sinon.spy(widget, 'onDisplayDocuments');
//      var loadBibcodeData = sinon.spy(widget, 'loadBibcodeData');
//      var processResponse = sinon.spy(widget, 'processResponse');
//
//      widget.activate(minsub.beehive.getHardenedInstance());
//
//      minsub.publish(minsub.DISPLAY_DOCUMENTS, minsub.createQuery({'q': 'bibcode:foo'}));
//      expect(onDisplayDocuments.callCount).to.be.eql(1);
//      expect(loadBibcodeData.callCount).to.be.eql(1);
//      expect(loadBibcodeData.lastCall.args[0]).to.be.eql('foo');
//      expect(processResponse.callCount).to.be.eql(1);
//
//      expect(widget.collection.models.length).to.be.eql(7);
//
//    });

  })
});