define([
  'js/widgets/citation_helper/widget',
  'js/components/json_response',
  'js/bugutils/minimal_pubsub',
    'js/components/api_query',
    'js/components/api_response',
    'js/components/api_targets'

], function (
  CitationHelperWidget,
  JsonResponse,
  MinPubSub,
  ApiQuery,
  ApiResponse,
  ApiTargets
  ) {

  describe("Citation Helper Widget (citation_helper_widget.spec.js)", function (){
    // this is the response straight from Edwin's API
    // the query was for "1980ApJS...44..169S","1980ApJS...44..193S"
    var testData = [{'title': 'Electron Impact Excitation of Positive Ions', 'bibcode': '1970RSPTA.266..225B', 'score': 2, 'author': 'Burgess, et al.'},
                    {'title': 'A perturbation analysis of current methods of calculating relativistic energies', 'bibcode': '1973JPhB....6....1E', 'score': 2, 'author': 'Ermolaev, et al.'},
                    {'title': 'Electron-impact excitation cross-sections for complex ions. I. Theory for ions with one and two valence electrons.', 'bibcode': '1974ApJS...28..309S', 'score': 2, 'author': 'Sampson, et al.'},
                    {'title': 'Electron-impact excitation of highly charged berylliumlike ions with inclusion of configuration mixing', 'bibcode': '1977PhRvA..15.1382P', 'score': 2, 'author': 'Parks,+'},
                    {'title': 'Exact Slater integrals', 'bibcode': '1978CoPhC..14..255G', 'score': 2, 'author': 'Golden, et al.'},
                    {'title': 'Intermediate-coupling collision strengths for fine-structure transitions between S and P levels and S and D levels in highly charged He-like ions', 'bibcode': '1978PhRvA..17.1619S', 'score': 2, 'author': 'Sampson, et al.'},
                    {'title': 'Intermediate Coupling Collision Strengths for \u0394n - O Transitions Produced by Electron Impact on Highly Charged He- and Be-like Ions', 'bibcode': '1980ADNDT..25..185G', 'score': 2, 'author': 'Goett, et al.'},
                    {'title': 'Intermediate coupling collision strengths for delta N = 0 transitions produced by electron impact on highly charged ions. III - Transitions within the 1/2s/2s2p and 1/2s/2/2p/ configurations and between the 1/2s/2/2s/ and 1/2s/2/2p/ configurations in beryllium-like ions', 'bibcode': '1980ApJS...44..215C', 'score': 2, 'author': 'Clark, et al.'},
                    {'title': 'Scaled Collision Strengths for Hydrogenic Ions', 'bibcode': '1981ApJS...45..603G', 'score': 2, 'author': 'Golden, et al.'},
                    {'title': 'Intermediate-coupling collision strengths and line strengths for certain transitions to n = 3 levels in highly charged Be-like ions', 'bibcode': '1981PhRvA..24.2979S', 'score': 2, 'author': 'Sampson, et al.'}];

    afterEach(function(){
      $("#test").empty();
    });

    it("should display a list of suggested articles", function(){

      var r = new CitationHelperWidget();
      var $w = r.render().$el;

      $("#test").append($w);

      r.deferredObject = $.Deferred();

      r.processResponse(new JsonResponse(testData));
      // We expect 20 because every first author is listed in a <li> element as well
      expect($w.find("li").length).to.eql(20);
    });


    it("should link directly to the abstract pages in Bumblebee", function(){

      var r = new CitationHelperWidget();

      $("#test").append(r.render().el);

      r.deferredObject = $.Deferred();

      r.processResponse(new JsonResponse(testData));

      expect($("#test").find("li a").first().attr("href")).to.eql("#abs/1970RSPTA.266..225B");
      expect($("#test").find("li a").last().attr("href")).to.eql("#abs/1981PhRvA..24.2979S");
    });

    it("should have a function that empties the main view", function(){

      var r = new CitationHelperWidget();
      r.processResponse(JSON.parse(JSON.stringify(testData)));

      $("#test").append(r.view.el);
      r.reset();

      //check to see that the rendered views are inserted
      expect($("#test").find((".list-unstyled *")).length).to.eql(0);

    });

      it("should request data from pubsub, then send that data to the citation helper endpoint using the Api, then render the template", function(done){

          var r = new CitationHelperWidget();

          var minsub = new (MinPubSub.extend({
              request: function(apiRequest) {
                  if (apiRequest.toJSON().target === ApiTargets.SEARCH) {
                      return {
                          "responseHeader": {
                              "status": 0,
                              "QTime": 1,
                              "params": {
                                  "fl": "bibcode",
                                  "indent": "true",
                                  "rows": 200,
                                  "wt": "json",
                                  "q": "bibcode:(\"1980ApJS...44..169S\" OR \"1980ApJS...44..193S\")\n"}},
                          "response": {"numFound": 2, "start": 0, "docs": [
                              {
                                  "bibcode": "1980ApJS...44..169S"},
                              {
                                  "bibcode": "1980ApJS...44..193S"}
                          ]
                          }};
                  }
                  else if (apiRequest.toJSON().target == ApiTargets.SERVICE_CITATION_HELPER) {
                      return JSON.parse(JSON.stringify(testData));
                  }
              }}))({verbose: false});

          r.activate(minsub.beehive.getHardenedInstance());

          //provide widget with current query
          minsub.publish(minsub.START_SEARCH, new ApiQuery({q : "star"}));

          //trigger show event, should prompt dispatchRequest
          r.renderWidgetForCurrentQuery();

          setTimeout(function() {
              //if the views received the data, the 2 step request process worked
              expect(r.model.attributes.items.length).to.eql(10);
              done();
          }, 5);
      });

      it("can be called programatically", function() {
          var r = new CitationHelperWidget();

          var minsub = new (MinPubSub.extend({
              request: function(apiRequest) {
                  if (apiRequest.toJSON().target === ApiTargets.SEARCH) {
                      return {
                          "responseHeader": {
                              "status": 0,
                              "QTime": 1,
                              "params": {
                                  "fl": "bibcode",
                                  "indent": "true",
                                  "rows": 200,
                                  "wt": "json",
                                  "q": "bibcode:(\"1980ApJS...44..169S\" OR \"1980ApJS...44..193S\")\n"}},
                          "response": {"numFound": 2, "start": 0, "docs": [
                              {
                                  "bibcode": "1980ApJS...44..169S"},
                              {
                                  "bibcode": "1980ApJS...44..193S"}
                          ]
                          }};
                  }
                  else if (apiRequest.toJSON().target == ApiTargets.SERVICE_CITATION_HELPER) {
                      return JSON.parse(JSON.stringify(testData));
                  }
              }}))({verbose: false});

          r.activate(minsub.beehive.getHardenedInstance());

          var $w = r.render().$el;
          $('#test').append($w);
          var data = {'libid': 'foo', 'permission':'owner', 'name':'bar'};
          r.renderWidgetForListOfBibcodes(["1980ApJS...44..169S", "1980ApJS...44..193S"],data);

          expect($("#test").text().indexOf('Burgess') > -1).to.eql(true);

      });
  })
});