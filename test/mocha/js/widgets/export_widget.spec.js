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
      minsub.close();
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

    it("shows controls when necessary", function(done) {
      var widget = _getWidget();
      var $w = widget.render().$el;
      $('#test').append($w);

      // export query shows only if there was a query/identifiers
      expect($w.find('#exportQuery').length).to.be.eql(0);
      expect($w.find('#exportRecords').length).to.be.eql(0);

      minsub.publish(minsub.FEEDBACK, minsub.createFeedback({code: minsub.T.FEEDBACK.CODES.SEARCH_CYCLE_STARTED,
        query: minsub.createQuery({'q': 'crazy lazy'})}));
      expect($w.find('#exportQuery').length).to.be.eql(1);

      // should show button to export identifiers
      widget.model.set('numIdentifiers', 200);
      widget.model.set('identifiers', ['one', 'two']);
      expect($w.find('#exportRecords').length).to.be.eql(1);

      // clicking produces actions
      $w.find('#exportQuery').click();
      expect($w.find('#exportData').text().indexOf('Takatsuka') > -1).to.eql(true);

      widget.reset();
      expect($w.find('#exportData').text().indexOf('Takatsuka') > -1).to.eql(false);

      $w.find('#exportRecords').click();
      expect($w.find('#exportData').text().indexOf('Takatsuka') > -1).to.eql(true);

      done();

    });

    it("can be called programatically", function() {
      var widget = _getWidget();
      var $w = widget.render().$el;
      $('#test').append($w);

      widget.exportRecords('bibtex', ['one', 'two']);
      expect($w.find('#exportData').text().indexOf('Takatsuka') > -1).to.eql(true);
      expect($w.find('#exportQuery').length).to.be.eql(0);
      expect($w.find('#exportRecords').length).to.be.eql(1);

      widget.reset();
      expect($w.find('#exportData').text().indexOf('Takatsuka') > -1).to.eql(false);

      widget.exportQuery('bibtex', minsub.createQuery({'q': 'foo:bar'}));
      expect(widget.model.get('numFound')).to.be.eql(841359);
      expect(widget.model.get('maxExport')).to.be.eql(300);

      expect($w.find('#exportData').text().indexOf('Takatsuka') > -1).to.eql(true);
      expect($w.find('#exportQuery').length).to.be.eql(1);
      expect($w.find('#exportRecords').length).to.be.eql(1);
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