define([
    'js/widgets/facet/collection',
    'js/bugutils/minimal_pubsub',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/facet/widget',
    'js/widgets/base/paginated_multi_callback_widget',
    'js/widgets/facet/container_view',
    'js/widgets/facet/collection'
  ],

  function (FacetCollection,
    MinimalPubsub,
    test1,
    test2,
    FacetWidget,
    FacetWidgetSuperClass,
    FacetContainerView,
    FacetCollection) {

    describe("Facet Widget (UI)", function () {

      var minsub;
      beforeEach(function() {

        minsub = new (MinimalPubsub.extend({
          request: function(apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return test1;
            } else {
              return test2;
            }
          }
        }))({verbose: true});
      });

      afterEach(function() {
        minsub.close();
        var ta = $('#test-area');
        if (ta) {
          //ta.empty();
        }
      });

      it("should return FacetWidget", function() {
        var w = new FacetWidget({
          view: new FacetContainerView({
            model: new FacetContainerView.ContainerModelClass({title: "Facet Title"}),
            collection: new FacetCollection(),
            displayNum: 3,
            maxDisplayNum: 10,
            openByDefault: true,
            showOptions: true
          })
        });

        expect(w).to.be.instanceof(FacetWidget);
        expect(w).to.be.instanceof(FacetWidgetSuperClass);

        // it is empty by default (this is inconsistent! because view.render() returns obj and not obj.el)
        $w = $(w.render());
        expect($w.find('h5').text()).to.be.equal('Facet Title');
        expect($w.find('.widget-body').text().trim()).to.be.equal('No content to display.');
      });

      it("should throw errors when you instantiate it without proper variables", function() {
        expect(function() {new FacetWidget()}).to.throw.Error;
      });


      it("communicates with pubsub", function(done) {
        var widget = new FacetWidget({
          defaultQueryArguments: {
            "facet": "true",
            "facet.field": "author_facet_hier",
            "facet.mincount": "1"
          },
          view: new FacetContainerView({
            model: new FacetContainerView.ContainerModelClass({title: "Facet Title"}),
            collection: new FacetCollection(),
            displayNum: 3,
            maxDisplayNum: 10,
            openByDefault: true,
            showOptions: true
          })
        });

        sinon.spy(widget, "dispatchRequest");
        sinon.spy(widget, "processResponse");

        widget.activate(minsub.beehive.getHardenedInstance());

        $('#test-area').append(widget.render());

        minsub.publish(minsub.NEW_QUERY, minsub.createQuery({'q': 'star'}));
        expect(widget.dispatchRequest.called).to.be.true;
        expect(widget.processResponse.called).to.be.true;

      });


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
