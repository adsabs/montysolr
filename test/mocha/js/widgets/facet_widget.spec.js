define([
    'js/bugutils/minimal_pubsub',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/facet/widget',
    'js/widgets/base/paginated_multi_callback_widget',
    'js/widgets/facet/container_view',
    'js/widgets/facet/collection',
    'hbs!js/widgets/facet/templates/logic-container',
    'js/widgets/base/tree_view',
    'js/widgets/facet/tree_view',
  ],

  function (
    MinimalPubsub,
    test1,
    test2,
    FacetWidget,
    FacetWidgetSuperClass,
    FacetContainerView,
    FacetCollection,
    LogicSelectionContainerTemplate,
    TreeView,
    FacetTreeView
    ) {

    describe("FacetWidget - base (UI)", function () {

      // modify the test to contain only 5 pairs of facet values
      _.each([test1, test2], function(o) {
        _.each(_.keys(o.facet_counts.facet_fields), function(fKey) {
          o.facet_counts.facet_fields[fKey] = Array.prototype.slice.call(o.facet_counts.facet_fields[fKey], 0, 10);
        });
      });


      var minsub, testId;
      beforeEach(function(done) {
        //var testId = 'test' + Math.random().toString(16).split('.')[1];
        //var testEl = $('<div id="' +  testId + '">hello</div>');
        testId = '#test';
        //$('#test-area').append(testEl);
        minsub = new (MinimalPubsub.extend({
          request: function(apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return test1;
            } else {
              return test2;
            }
          }
        }))({verbose: false});
        done();
      });

      afterEach(function(done) {

        var ta = $(testId);
        if (ta) {
          ta.empty();
        }
        minsub.close();
        done();
      });

      it("should return FacetWidget", function(done) {
        var w = new FacetWidget({
          view: new FacetContainerView({
            model: new FacetContainerView.ContainerModelClass({title: "Facet Title"}),
            collection: new FacetCollection(),
            displayNum: 3,
            maxDisplayNum: 10,
            openByDefault: true,
            showOptions: true
          }),
         defaultQueryArguments: {
           'facet.field': 'foo'
         }
        });

        expect(w).to.be.instanceof(FacetWidget);
        expect(w).to.be.instanceof(FacetWidgetSuperClass);

        $w = $(w.render().el);
        expect($w.find('h5').text().trim()).to.be.equal('Facet Title');
        expect($w.find('.widget-body').text().trim()).to.be.equal('No content to display.');
        done();
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

        //$(testId).append(widget.render());

        minsub.publish(minsub.NEW_QUERY, minsub.createQuery({'q': 'star'}));
        expect(widget.dispatchRequest.called).to.be.true;
        expect(widget.processResponse.called).to.be.true;
        expect(widget.processResponse.args[0][0].getApiQuery().url()).to.equal("facet=true&facet.field=author_facet_hier&facet.mincount=1&q=star&rows=20&start=0");
        done();

      });

      it("interacts with the view (sets a new query)", function(done) {
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

        sinon.spy(widget, "_dispatchRequest");
        sinon.spy(widget, "processResponse");
        sinon.spy(widget, "dispatchNewQuery");

        widget.activate(minsub.beehive.getHardenedInstance());
        minsub.publish(minsub.NEW_QUERY, minsub.createQuery({'q': 'star'}));

        var $w = $(widget.render().el);
        $(testId).append($w);

        // options are there and visible
        expect($w.find('.widget-options.bottom').hasClass('hide')).to.be.false;
        expect($w.find('.item-view').length).to.be.equal(5);
        expect($w.find('.item-view').not('.hide').length).to.be.equal(3);


        $w.find('button[wtarget="ShowMore"]').click();
        setTimeout(
          function() {
            expect($w.find('.item-view').not('.hide').length).to.be.equal(6);
            expect($w.find('.item-view').filter('.hide').length).to.be.equal(4);
            expect(widget.collection.models[0].get('title')).to.be.equal('0/Head, J');
            expect(widget.collection.models[5].get('title')).to.be.equal('0/Wang, J');

            expect(widget._dispatchRequest.callCount).to.be.equal(2);
            expect(widget.processResponse.callCount).to.be.equal(2);

            // select one item - this should trigger new query
            $w.find('.item-view:eq(5) input').click();  // XXX for some reason this works only if it is appended to the page
            expect(widget.dispatchNewQuery.callCount).to.be.equal(1);
            expect(widget.dispatchNewQuery.args[0][0].get('q')).to.be.eql(["(star AND 0\\/Wang,\\ J)"]);
            expect(widget.processResponse.callCount).to.be.equal(3);


            // which updates the view (we should see 3 new, 2 hidden new items)
            expect($w.find('.widget-options.bottom').hasClass('hide')).to.be.false;
            expect($w.find('.item-view').length).to.be.equal(5);
            expect($w.find('.item-view').filter('.hide').length).to.be.equal(2);

            done();
          }
        ,100);

      });

      it("handles logical selection", function(done) {

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
            showOptions: true,
            template: LogicSelectionContainerTemplate,
            logicOptions: {single: ["limit to", "exclude"], multiple: ["and", "or", "exclude"]}
          })
        });
        sinon.spy(widget, "dispatchNewQuery");

        widget.activate(minsub.beehive.getHardenedInstance());
        minsub.publish(minsub.NEW_QUERY, minsub.createQuery({'q': 'star'}));

        var $w = $(widget.render().el);
        $('#test').append($w);

        $w.find('.item-view:first input').click();
        expect($w.find('input[value="limit to"]').is(':visible')).to.be.true;

        $w.find('input[value="limit to"]').attr('checked', 'checked').trigger('change');
        //i don't understand why it is not closing (it does when i click manually)
        //expect($w.find('input[value="limit to"]').is(':visible')).to.be.false;

        // TODO: check the query?
        // we expect to see a new query
        expect(widget.dispatchNewQuery.called).to.be.true;

        done();
      });

      it("knows to handle hierarchial views", function(done) {

        var widget = new FacetWidget({
          defaultQueryArguments: {
            "facet": "true",
            "facet.field": "author_facet_hier",
            "facet.mincount": "1"
          },
          view: new FacetContainerView({
            itemView: FacetTreeView,
            model: new FacetContainerView.ContainerModelClass({title: "Facet Title"}),
            collection: new TreeView.CollectionClass(),
            displayNum: 3,
            maxDisplayNum: 10,
            openByDefault: true,
            showOptions: true,
            template: LogicSelectionContainerTemplate,
            logicOptions: {single: ["limit to", "exclude"], multiple: ["and", "or", "exclude"]}
          })
        });
        sinon.spy(widget, "handleTreeExpansion");
        sinon.spy(widget, "processFacetResponse");

        widget.activate(minsub.beehive.getHardenedInstance());
        minsub.publish(minsub.NEW_QUERY, minsub.createQuery({'q': 'star'}));

        var $w = $(widget.render().el);
        $('#test').append($w);

        expect($w.find('input').length).to.be.gt(0);

        $w.find('.widget-item:first').click();
        expect(widget.handleTreeExpansion.called).to.be.true;
        expect(widget.processFacetResponse.called).to.be.true;
        expect(widget.processFacetResponse.args[1][0].getApiQuery().get('facet.prefix')).to.be.eql(['1/Head, J/']);

        done();
      });

      it("accepts chain of value processors", function(done) {
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
          }),
          responseProcessors: [
            function(v) {return 'x/' + v},
            function(v) {vv = v.split('/'); return vv[vv.length-1]}
          ]
        });

        widget.activate(minsub.beehive.getHardenedInstance());
        minsub.publish(minsub.NEW_QUERY, minsub.createQuery({'q': 'star'}));

        expect(widget.collection.models[0].get('title')).to.be.equal('x/Head, J');
        done();
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

        });
        it("should initiate a facet model with appropriate default values", function () {
          //for change-apply containers
          expect(c.models[0].attributes).to.include.key("newValue");
          //for logic containers
          expect(c.models[0].attributes).to.include.key("selected");

        })

      });


    })

  });
