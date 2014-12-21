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
          if (apiRequest.get('target').indexOf('export') > -1) {
            return {msg: 'Exported 6 records',
            data: '@INPROCEEDINGS{2015cshn.conv..198T,\n\
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
      sinon.spy(widget, 'export');
      widget.activate(minsub.beehive.getHardenedInstance());
      return widget;
    };

    it("extends BaseWidget", function () {
      expect(new ExportWidget()).to.be.instanceof(BaseWidget);
    });

    it("displays exported documents in pre-formatted area", function(done) {
      var widget = _getWidget();
      var $w = widget.render().$el;
      $('#scratch').append($w);

      widget.model.set('numFound', 5000);
      widget.view.buildSlider(0, 300);

      $w.find('#exportQuery').click();
      setTimeout(function() {
        expect(widget.export.called).to.be.equal(false);
        $w.find('#exportModal button.apply').click();
        setTimeout(function() {
          expect(widget.export.called).to.be.equal(true);
          expect($w.find('#exportData').text().indexOf('Takatsuka') > -1).to.eql(true);
          done();
        }, 500);
      }, 500);

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