define([
    'jquery',
    'backbone',
    'marionette',
    'js/widgets/facet/container_view',
    'js/widgets/facet/model',
    'js/widgets/facet/collection',
    'js/widgets/base/item_view'
  ],
  function ($,
            Backbone,
            Marionette,
            FacetContainerView,
            FacetModel,
            FacetCollection,
            BaseItemView
    ) {

    describe("Facet Base Container View (UI)", function () {

      afterEach(function () {
        var ta = $('#test-area');
        if (ta) {
          //ta.empty();
        }
      });


      it("returns FacetBaseContainerView object", function () {
        expect(new FacetContainerView()).to.be.instanceof(FacetContainerView);
        expect(new FacetContainerView()).to.be.instanceof(Marionette.CompositeView);
      });

      it("can load more results and page through them (and hide load-more if there is none)", function(done) {
        var view = new FacetContainerView({
          itemView: BaseItemView,
          model: new FacetContainerView.ContainerModelClass({title: "Facet Title"}),
          collection: new FacetCollection(),
          displayNum: 3,
          maxDisplayNum: 10,
          openByDefault: true,
          showOptions: true
        });

        sinon.spy(view, 'onShowMore');
        var all = sinon.spy();
        view.listenTo(view, 'all', all);

        // this would normally be done by a controller
        view.collection.add(new Backbone.Model({title: 'foo1', value: 'bar1'}));
        view.collection.add(new Backbone.Model({title: 'foo2', value: 'bar2'}));
        view.collection.add(new Backbone.Model({title: 'foo3', value: 'bar3'}));
        view.collection.add(new Backbone.Model({title: 'foo4', value: 'bar4'}));
        view.collection.add(new Backbone.Model({title: 'foo5', value: 'bar5'}));

        var $v = $(view.render().el);
        //$('#test-area').append($v);

        expect($v.find('.widget-options.bottom').hasClass('hide')).to.be.false;
        expect($v.find('.widget-options.top').hasClass('hide')).to.be.false;

        // expect 3 items in the facet
        expect($v.find('.item-view').length).to.be.equal(5);
        expect($v.find('.item-view').filter('.hide').length).to.be.equal(2);
        expect($v.find('a[target="ShowMore"]').text()).to.be.equal('Show More');

        var cc = all.callCount;

        // click on the load more
        $v.find('a[target="ShowMore"]').click();

        expect(view.onShowMore.called).to.be.true;
        expect(all.callCount).to.be.equal(cc+2);

        // the controller should call displayMore() - so let's emulate it
        view.displayMore(2);
        expect($v.find('.item-view').length).to.be.equal(5);
        expect($v.find('.item-view').filter('.hide').length).to.be.equal(0);

        view.disableShowMore("foo");
        expect($v.find('a[target="ShowMore"]').text()).to.be.equal('');

        done();
      });

    });
  });