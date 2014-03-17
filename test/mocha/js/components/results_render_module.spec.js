define(['marionette', 'backbone', 'js/modules/results-render/controller/results-render-controller', 'js/components/api_response'], function(Marionette, Backbone, ResultsListController, Response){

    describe("render-results-module", function() {

     before(function () {
          this.jsonData = $.parseJSON(
          '{\
          "responseHeader":{\
            "status":0,\
              "QTime":88,\
              "params":{\
              "facet":"true",\
                "indent":"true",\
                "hl.simple.pre":"<em>",\
                "hl.fl":"title",\
                "wt":"json",\
                "hl":"true",\
                "rows":"5",\
                "fl":"title,bibcode,author",\
                "start":"10",\
                "facet.query":"title:star",\
                "q":"title:star",\
                "hl.simple.post":"</em>",\
                "facet.field":"author",\
                "fq":"database:astronomy"}},\
          "response":{"numFound":172978,"start":10,"docs":[\
            { "id": "166243",\
              "author":["Morrison, J."],\
              "bibcode":"1903PA.....11...88M",\
              "title":["The Star of Bethlehem"]},\
            { "id": "166285",\
              "author":["Morrison, J."],\
              "bibcode":"1903PA.....11..122M",\
              "title":["The Star of Bethlehem (cont.)"]},\
            { "id": "166785",\
              "author":["Wing, Daniel E."],\
              "bibcode":"1903PA.....11..481W",\
              "title":["Star Dust"]},\
            { "id": "192734",\
              "bibcode":"1906PA.....14R.507.",\
              "title":["Star Charts"]},\
            { "id": "139757",\
              "author":["Holmes, Edwin"],\
              "bibcode":"1894JBAA....5...26H",\
              "title":["Star Streams"]}]\
          },\
          "facet_counts":{\
            "facet_queries":{\
              "title:star":172978},\
            "facet_fields":{\
              "author":[\
                "heber, u",301,\
                "linsky, j l",299,\
                "mayor, m",273,\
                "kurtz, d w",270,\
                "pickering, edward c",262,\
                "maeder, a",260,\
                "schmitt, j h m m",259,\
                "campbell, leon",256,\
                "mundt, r",138]},\
            "facet_dates":{},\
            "facet_ranges":{}},\
          "highlighting":{\
            "166243":{\
              "title":["The <em>Star</em> of Bethlehem"]},\
            "166285":{\
              "title":["The <em>Star</em> of Bethlehem (cont.)"]},\
            "166785":{\
              "title":["<em>Star</em> Dust"]},\
            "192734":{\
              "title":["<em>Star</em> Charts"]},\
            "139757":{\
              "title":["<em>Star</em> Streams"]}}}'
          );

     this.jsonDataTakeTwo = $.parseJSON(
          '{\
          "responseHeader":{\
            "status":0,\
              "QTime":88,\
              "params":{\
              "facet":"true",\
                "indent":"true",\
                "hl.simple.pre":"<em>",\
                "hl.fl":"title",\
                "wt":"json",\
                "hl":"true",\
                "rows":"5",\
                "fl":"title,bibcode,author",\
                "start":"10",\
                "facet.query":"title:star",\
                "q":"title:star",\
                "hl.simple.post":"</em>",\
                "facet.field":"author",\
                "fq":"database:astronomy"}},\
          "response":{"numFound":172978,"start":10,"docs":[\
            { "id": "166243",\
              "author":["Morrison, J."],\
              "bibcode":"1903PA.....11...88M",\
              "title":["Update Test"]}]\
          },\
          "facet_counts":{\
            "facet_queries":{\
              "title:star":172978},\
            "facet_fields":{\
              "author":[\
                "heber, u",301,\
                "linsky, j l",299,\
                "mayor, m",273,\
                "kurtz, d w",270,\
                "pickering, edward c",262,\
                "maeder, a",260,\
                "schmitt, j h m m",259,\
                "campbell, leon",256,\
                "mundt, r",138]},\
            "facet_dates":{},\
            "facet_ranges":{}},\
          "highlighting":{\
            "166243":{\
              "title":["Update Test"]}}}'
          );

        });

describe("takes a response object, returns a composite view", function(){

        beforeEach(function(){

          r = new Response(this.jsonData)

          results = new ResultsListController(r)

        });

       it("should join highlights with their records in the collection", function(){

       expect(JSON.stringify(results.collection.get("139757").attributes.highlights)).to.equal(
            JSON.stringify({"title":["<em>Star</em> Streams"]})
              )
      });

       it("should return a Marionette.CompositeView object for insertion into the application layout", function(){

        expect(results.returnView()).to.be.instanceof(Backbone.Marionette.CompositeView)

       });


       it("should update automatically when update method is called and response object is provided", function(){

        var v = results.returnRenderedView()

        expect($(v).find("li:first p:first").text()).to.equal("The Star of Bethlehem");

        var secondResponse = new Response(this.jsonDataTakeTwo);

        results.update(secondResponse);

        expect($(v).find("li:first p:first").text()).to.equal("Update Test");




       });


      })

    })

  }
)