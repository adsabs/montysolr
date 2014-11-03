define(['jquery', 'js/widgets/export/widget', 'js/widgets/base/base_widget'
], function ($, ExportWidget, BaseWidget) {

  describe("Export Widget (UI Widget)", function () {

    var w;

    beforeEach(function () {
      w = new ExportWidget();

    });

    it("extends BaseWidget", function () {
      expect(w).to.be.instanceof(BaseWidget)
    });

    it("has a post method that expects a list of dicts, and mimics a post form submittal", function () {

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

    it("adjusts the export form input values based on number of results");

    it("offers options to submit selected records or a numerical range of records if user has selected records");

  })

});