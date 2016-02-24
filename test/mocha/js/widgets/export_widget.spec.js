define([
  'jquery',
  'js/widgets/export/widget',
  'js/widgets/base/base_widget',
  'js/bugutils/minimal_pubsub',
  './test_json/test1'
], function (
  $,
  ExportWidget,
  BaseWidget,
  MinimalPubSub,
  Test
  ) {

  describe("Export Widget (export_widget.spec.js)", function () {

    var minsub;
    beforeEach(function (done) {
      minsub = new (MinimalPubSub.extend({
        request: function (apiRequest) {
          if (apiRequest.url().indexOf('export') > -1) {
            return {msg: 'Exported 6 records',
            export: '@INPROCEEDINGS{2015cshn.conv..198T,\n\
              author = {{Takatsuka}, T. and {Hatsuda}, T. and {Masuda}, K.},\n\
              title = "{Massive hybrid stars with strangeness}",\n\
              adsurl = {http://adsabs.harvard.edu/abs/2015cshn.conv..198T},\n\
              adsnote = {Provided by the SAO/NASA Astrophysics Data System}\n\
              }'
            }
          }
          else {
            return Test();
          }
        }
      }))({verbose: false});
      done();
    });

    afterEach(function (done) {
      minsub.destroy();
      var ta = $('#test');
      if (ta) {
        ta.empty();
      }
      done();
    });

    var _getWidget = function() {
      var widget = new ExportWidget();
      widget.activate(minsub.beehive.getHardenedInstance());
      return widget;
    };

    it("extends BaseWidget", function () {
      expect(new ExportWidget()).to.be.instanceof(BaseWidget);
    });

    it("shows appropriate templates depending on what data is in the model", function(done) {
      var widget = _getWidget();
      var $w = widget.render().$el;
      $('#test').append($w);

      //shows loading function by default
      expect($("#test div div").text().trim()).to.eql("Loading data...");


      //shows export view if model has "exports"

      widget.model.reset();
      widget.model.set("current", 20);
      widget.model.set("numFound", 800);
      widget.model.set("export", "some fake exports");

      expect($("#test").find("h3").text().trim()).to.eql('Currently viewing 20\n            \n            records in \n                \n                BibTeX\n                \n                \n                \n             format.')
      expect($("#test textarea").val()).to.eql("some fake exports");
      expect($("#test").find(".change-rows").text().trim()).to.eql('');


      widget.model.set("query", "foo");
      widget.view.render();

      //only can change record #s if we have a query
      expect($("#test").find(".change-rows").text().trim()).to.eql( 'Change to first\n            \n            record(s) (max is 500).\n             Submit');

      //will automatically recalculate the "max" value when numFound changes
      widget.model.set("numFound", 80);

      widget.view.render();

      expect($("#test").find(".change-rows").text().trim()).to.eql( 'Change to first\n            \n            record(s) (max is 80).\n             Submit');

      //if model has an error, shows that instead of exports
      widget.model.set("error", "foo");

      expect($("#test div div").hasClass("alert")).to.be.true;
      expect($("#test div div").text().trim()).to.eql("foo");


      done();

    });

    it("can be called programatically", function() {
      var widget = _getWidget();
      var $w = widget.render().$el;
      $('#test').append($w);

      widget.renderWidgetForListOfBibcodes(['one', 'two'], { format : 'bibtex'});

      expect($("#test textarea").text().indexOf('Takatsuka') > -1).to.eql(true);
      expect($w.find(".change-rows").length).to.be.eql(0);

      widget.model.reset();
      expect($("#test textarea").length).to.eql(0);

    });



    it.skip("catches ctrl-a to select only the contents of the text-area", function() {

    });

    it.skip("can open the documents in new window or make them downloadable", function() {

    });

    it.skip("by default, it exports first N results of the query; or selected docs (if there are any)", function() {

    });

    it.skip("has a post method that expects a list of dicts, and mimics a post form submittal", function () {

      var fakeJquerySubmit = sinon.stub($.fn, "submit").returnsThis();

      w.post("fake/path", [
        {test: "parameter"}
      ]);

      var f = fakeJquerySubmit.returnValues[0];


      expect(f.eq(0).attr("action")).to.eql("fake/path");
      expect(f[0].tagName).to.eql("FORM");
      expect(f.children(0).attr("name")).to.eql("test");
      expect(f.children(0).attr("value")).to.eql("parameter");

    });


  })

});