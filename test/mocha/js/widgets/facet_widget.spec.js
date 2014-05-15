define([
    'js/widgets/facet/collection'
  ],

  function (FacetCollection) {
    describe("facet widget (ui widget)", function () {

      describe("facet collection", function () {
        /*this is checkbox-type data; data for graphs (change-apply) will consist of a series of 
         x-y values for aggregation and visualization*/
        var fakeFacetData = [
          {
            title          : " Wang, J (1496)",
            valWithoutSlash: "Wang, J",
            value          : "0/Wang, J"
          }
        ];

        var c = new FacetCollection(fakeFacetData);

        it("should have text preprocessing functions for raw solr data", function () {
          expect(c).to.have.property("titleCase");
          expect(c).to.have.property("allCaps");
          expect(c).to.have.property("removeSlash");

        })
        it("should initiate a facet model with appropriate default values", function () {
          //for change-apply containers
          expect(c.models[0].attributes).to.include.key("newValue");
          //for logic containers
          expect(c.models[0].attributes).to.include.key("selected");

        })

      });


    })

  })
