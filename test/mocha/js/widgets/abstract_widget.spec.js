/**
 * Created by alex on 5/19/14.
 */
define(['backbone', 'marionette', 'jquery', 'js/widgets/abstract/widget',
    'js/widgets/base/base_widget','js/bugutils/minimal_pubsub' ],
  function (Backbone, Marionette, $, AbstractWidget, BaseWidget, MinimalPubSub) {

    var aw, view, testJSON;

    testJSON = {  "responseHeader":{    "status":0,    "QTime":62,    "params":{      "fl":"abstract,title,author,aff,pub,pubdate,keyword",      "indent":"true",      "start":"4",      "q":"planet\n",      "wt":"json",      "rows":"1"}},  "response":{"numFound":238540,"start":4,"docs":[      {    "keyword":["HARMONY OF THE UNIVERSE","THEORY OF MUSIC","PLATO'S BODIES"], "author":["Lieske, J. H.",          "Standish, E. M."],        "abstract":"In the past twenty years there has been a great amount of growth in radiometric observing methods.",  "pub":"IAU Colloq. 56: Reference Coordinate Systems for Earth Dynamics", "pubdate":"1981-00-00",        "title":["Planetary Ephemerides"],        "aff":["Heidelberg, Universität, Heidelberg, Germany",          "California Institute of Technology, Jet Propulsion Laboratory, Pasadena, CA"]}]  }}
    beforeEach(function(){
      aw = new AbstractWidget({data:testJSON});
      aw.render();
      view = aw.view;

    })

    afterEach(function(){
      $("#test").empty()

    })

    describe("Abstract Renderer (UI Widget)", function(){

      it("should be a simple widget consisting of Base Widget, an ItemView, and a Backbone Model", function(){

        expect(aw).to.be.instanceof(BaseWidget)
        expect(view).to.be.instanceof(Marionette.ItemView)
        expect(aw.model).to.be.instanceof(Backbone.Model)
      })

      it("should have a model that takes raw solr data and parses it to template-ready condition", function(){
        expect(aw.model.attributes.pubdate).to.equal("1981-00-00");
        expect(aw.model.attributes.pub).to.equal("IAU Colloq. 56: Reference Coordinate Systems for Earth Dynamics");
        expect(aw.model.attributes.authorAff[0][0]).to.equal("Lieske, J. H.");
        expect(aw.model.attributes.authorAff[0][1]).to.equal("Heidelberg, Universität, Heidelberg, Germany");
        expect(aw.model.attributes.authorAff[1][0]).to.equal("Standish, E. M.");
        expect(aw.model.attributes.authorAff[1][1]).to.equal( "California Institute of Technology, Jet Propulsion Laboratory, Pasadena, CA");

      });

      it("should render a view with the properly rendered information and 'view more' user interactions", function(){
        $("#test").append(view.el);

        expect(view.$(".affiliation").filter(".hide").length).to.equal(view.$(".affiliation").length)

        $("#test").find("#toggle-aff").click();

        expect(view.$(".affiliation").filter(".hide").length).to.equal(0);
        expect(view.$("#abstract-content").text()).to.match(/In the past twenty years there has been a great amount of growth in radiometric observing methods./);


      });

      it("should interact properly with pubsub", function(){

        var m = new MinimalPubSub()


      })


    })


  });