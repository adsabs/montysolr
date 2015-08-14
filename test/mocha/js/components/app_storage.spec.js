define([
  'js/components/app_storage',
  'js/components/api_query',
  'backbone',
  'js/bugutils/minimal_pubsub'
  ], function(
    AppStorage,
    ApiQuery,
    Backbone,
    MinimalPubsub
  ) {
  describe("AppStorage (app_storage.spec.js)", function () {
      

    it("should be an Backbone object", function() {
      expect(new AppStorage()).to.be.instanceof(Backbone.Model);
    });


    it("query related funcs", function() {
      var s = new AppStorage();

      expect(s.getCurrentQuery).to.be.defined;
      s.setCurrentQuery(new ApiQuery({q: 'foo'}));
      expect(s.hasCurrentQuery()).to.eql(true);
      expect(s.getCurrentQuery()).to.be.instanceof(ApiQuery);
      expect(function() {s.setCurrentQuery({q: 'foo'})}).to.throw(Error);
    });

    it("basket related funcs", function() {
      var s = new AppStorage();
      var minsub = new MinimalPubsub();
      s.activate(minsub.beehive);

      expect(s.hasSelectedPapers).to.be.defined;
      expect(s.getSelectedPapers).to.be.defined;
      expect(s.addSelectedPapers).to.be.defined;
      expect(s.isPaperSelected).to.be.defined;
      expect(s.removeSelectedPapers).to.be.defined;

      expect(s.hasSelectedPapers()).to.eql(false);
      expect(s.getSelectedPapers()).to.eql([]);

      s.addSelectedPapers('foo');
      s.addSelectedPapers(['foo', 'bar']);

      expect(s.getSelectedPapers()).to.eql(['foo', 'bar']);
      s.getSelectedPapers().push('baz');
      expect(s.getSelectedPapers()).to.eql(['foo', 'bar']);
      expect(s.getNumSelectedPapers()).to.eql(2);

      expect(s.isPaperSelected('baz')).to.eql(false);
      expect(s.isPaperSelected('foo')).to.eql(true);

      s.removeSelectedPapers(['foo']);
      expect(s.getSelectedPapers()).to.eql(['bar']);
      expect(s.getNumSelectedPapers()).to.eql(1);

      s.removeSelectedPapers();
      expect(s.getNumSelectedPapers()).to.eql(0);
    });

    it("hardened iface", function() {
      var s = new AppStorage();
      var minsub = new MinimalPubsub();
      s.activate(minsub.beehive);
      var h = s.getHardenedInstance();
      s.addSelectedPapers(['foo', 'bar']);
      expect(h.getSelectedPapers()).to.eql(['foo', 'bar']);
      expect(h.addSelectedPapers).to.eql(undefined);

      expect(h.addSelectedPapers).to.be.defined;
      expect(h.hasSelectedPapers).to.be.defined;
      expect(h.getNumSelectedPapers).to.be.defined;
      expect(h.isPaperSelected).to.be.defined;
    });

    it("listens to pubsub signals", function() {
      var s = new AppStorage();
      var minsub = new MinimalPubsub();
      s.activate(minsub.beehive);
      minsub.publish(minsub.PAPER_SELECTION, 'foo');
      expect(s.getSelectedPapers()).to.eql(['foo'])

      //should be able to re-add foo in bulk selection and not have it toggled off
      minsub.publish(minsub.BULK_PAPER_SELECTION, ['foo', 'boo', 'goo']);
      expect(s.getSelectedPapers()).to.eql(['foo', 'boo', 'goo']);
    });

  });
});
