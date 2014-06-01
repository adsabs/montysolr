/**
 * Created by alex on 5/24/14.
 */
define([
  'backbone',
  'marionette',
  'jquery',
  'js/widgets/contents_manager/widget',
  'js/widgets/abstract/widget'], function (
  Backbone,
  Marionette,
  $,
  ContentsWidget,
  AbstractWidget) {

  var abstractWidget, contentsWidget, testJSON1, testJSON2;

  testJSON1 = {  "responseHeader": {    "status": 0, "QTime": 62, "params": {      "fl": "abstract,title,author,aff,pub,pubdate", "indent": "true", "start": "4", "q": "planet\n", "wt": "json", "rows": "1"}}, "response": {"numFound": 238540, "start": 4, "docs": [
    {        "author": ["Lieske, J. H.", "Standish, E. M."], "abstract": "In the past twenty years there has been a great amount of growth in radiometric observing methods.", "pub": "IAU Colloq. 56: Reference Coordinate Systems for Earth Dynamics", "pubdate": "1981-00-00", "title": ["Planetary Ephemerides"], "aff": ["Heidelberg, Universität, Heidelberg, Germany", "California Institute of Technology, Jet Propulsion Laboratory, Pasadena, CA"]}
  ]  }};

  testJSON2 = {  "responseHeader": {    "status": 0, "QTime": 257, "params": {      "fl": "abstract,title,author,aff,pub,pubdate", "indent": "true", "start": "5", "q": "planet\n", "wt": "json", "rows": "1"}}, "response": {"numFound": 549499, "start": 5, "docs": [
    {        "author": ["Núnez, J.", "et al."], "abstract": "A program of astrometric observations of the minor planets included in the HIPPARCOS mission is described.", "pub": "Hipparcos. Scientific Aspects of the Input Catalogue Preparation", "pubdate": "1985-08-00", "title": ["Complementary Astrometric Observations of Minor Planets at Barcelona"], "aff": ["Observatorio Fabra"]}
  ]  }}

  beforeEach(function () {

    abstractWidgetOne = new AbstractWidget({data: testJSON1});
    abstractWidgetTwo = new AbstractWidget({data: testJSON2});

    contentsWidget = new ContentsWidget({title: "Test title",
      widgetTitleMapping: {"abstractOne": {widget: abstractWidgetOne, default: true },
        "abstractTwo": {widget: abstractWidgetTwo}
      }})

  });

  afterEach(function () {
    $("#test").empty()

  });

  describe("Contents Manager (UI Widget)", function () {

    it("should be an instance of Marionette Layout with its own special close and getView methods", function () {

      expect(contentsWidget).to.be.instanceof(Marionette.Layout);

    });

    it("should generate a table of contents as a Marionette CompositeView from the title mapping passed in as a parameter", function () {

      expect(contentsWidget.navCollection).to.be.instanceof(Backbone.Collection);
      expect(JSON.stringify(contentsWidget.navCollection.toJSON())).to.equal('[{"title":"abstractOne","show":true},{"title":"abstractTwo","show":false}]');

      expect(contentsWidget.navCollectionView).to.be.instanceof(Marionette.CompositeView);


      $("#test").append(contentsWidget.render().el);

      expect($(".table-of-contents").find("a").eq(0).text()).to.equal("abstractOne")
      expect($(".table-of-contents").find("a").eq(1).text()).to.equal("abstractTwo")


    });

    it("should render a static title for the layout if a title is passed in as a parameter", function () {

      $("#test").append(contentsWidget.render().el);


      expect($("#layout-title").text()).to.match(/Test title/);

    });

    it("should automatically render the default view", function () {

      $("#test").append(contentsWidget.render().el);

      expect($("#layout-content .author").eq(0).html()).to.match(/Lieske, J. H./)

    });

    it("should listen to click events on the table of contents and automatically show the corresponding view", function () {

      $("#test").append(contentsWidget.render().el);


      $(".table-of-contents").find("a").eq(1).click();

      expect($("#layout-content .author").eq(0).html()).to.match(/Núnez, J./);

    });

  })

});