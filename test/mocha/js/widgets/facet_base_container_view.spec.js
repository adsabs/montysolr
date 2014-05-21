define([
    'jquery',
    'backbone',
    'marionette',
    'js/widgets/facet/base_container_view',
    'js/widgets/facet/model',
    'js/widgets/facet/collection',
    'js/widgets/facet/item_view',
    'js/widgets/facet/collection_view'
  ],
  function ($,
            Backbone,
            Marionette,
            FacetBaseContainerView,
            FacetModel,
            FacetCollection,
            BaseItemView,
            FacetCollectionView
    ) {

    describe("Facet Base Container View (UI)", function () {


      afterEach(function () {
        var ta = $('#test-area');
        if (ta) {
          ta.empty();
        }
      });


      it("returns FacetBaseContainerView object", function () {
        expect(new FacetBaseContainerView()).to.be.instanceof(FacetBaseContainerView);
        expect(new FacetBaseContainerView()).to.be.instanceof(Marionette.CompositeView);
      });

      it("by default it shows nothing", function() {
        var view = new FacetBaseContainerView({
          itemView: BaseItemView
        });
        var $v = $(view.render().el);
        expect($v.find('.facet-name').length).to.be.equal(1);
        expect($v.find('.facet-name > h5').text()).to.be.equal("");
        expect($v.find('.facet-body').length).to.be.equal(1);


        var view = new FacetBaseContainerView({
          itemView: BaseItemView,
          model: new FacetBaseContainerView.ContainerModelClass({title: "Facet Title"})
        });
        var $v = $(view.render().el);
        expect($v.find('.facet-name > h5').text()).to.be.equal("Facet Title");
      });

      it("to render something, it needs a collection", function() {
        var view = new FacetBaseContainerView({
          itemView: BaseItemView,
          model: new FacetBaseContainerView.ContainerModelClass({title: "Facet Title"}),
          collection: new FacetCollection()
        });

        // this would normally be done by a controller
        view.collection.add(new FacetModel({title: 'foo', value: 'bar'}));

        var $v = $(view.render().el);
        expect($v.find('input').parent().text().trim()).to.be.equal('foo');
        expect($v.find('input').val()).to.be.equal('bar');

        // add item, it should re-paint
        view.collection.add(new FacetModel({title: 'woo', value: 'baz'}));
        expect($v.find('input[value="baz"]').parent().text().trim()).to.be.equal('woo');

        // by default the body of the facet is hidden
        expect($v.find('.facet-body').hasClass('hide')).to.be.true;

        // by clicking on title/caret it will be toggled
        $v.find('.facet-name > h5').click();
        expect($v.find('.facet-body').hasClass('hide')).to.be.false;
        $v.find('.facet-name > h5 > .main-caret').click();
        expect($v.find('.facet-body').hasClass('hide')).to.be.true;

      });

      it("can be started opened", function() {
        var c = new FacetCollection();
        c.add(new FacetModel({title: 'foo', value: 'bar'}));
        c.add(new FacetModel({title: 'foo', value: 'baz'}));

        var view = new FacetBaseContainerView({
          itemView: BaseItemView,
          model: new FacetBaseContainerView.ContainerModelClass({title: "Facet Title"}),
          collection: c,
          openByDefault: true,
          showOptions: true
        });

        var $v = $(view.render().el);
        expect($v.find('.facet-body').hasClass('hide')).to.be.false;
        expect($v.find('.facet-options').hasClass('hide')).to.be.false;
      });

      it("has methods to indicate different states", function() {
        var view = new FacetBaseContainerView();
        sinon.spy(view, "handleError");
        sinon.spy(view, "handleWaiting");
        sinon.spy(view, "revertState");
        var $v = $(view.render().el);


        view.handleSanity({error: {msg: 'foo'}});
        expect(view.handleError.called).to.be.true;
        expect($v.find('.facet-name').hasClass('error')).to.be.true;

        view.handleSanity({waiting: 500});
        expect(view.handleWaiting.called).to.be.true;
        expect($v.find('.facet-body').hasClass('waiting')).to.be.true;
        expect($v.find('.facet-name').hasClass('error')).to.be.true;

        view.handleSanity({ok: true});
        expect(view.revertState.callCount).to.be.equal(2);
        expect($v.find('.facet-body').hasClass('waiting')).to.be.false;
        expect($v.find('.facet-name').hasClass('error')).to.be.false;

      });

      it("accepts other collectionviews and plays nicely with them", function() {
        var c = new FacetCollection();
        c.add(new FacetModel({title: 'foo', value: 'bar'}));
        c.add(new FacetModel({title: 'woo', value: 'baz'}));

        var view = new FacetBaseContainerView({
          itemView: FacetBaseContainerView,
          model: new FacetBaseContainerView.ContainerModelClass({title: "Facet Title"}),
          collection: c,
          openByDefault: true
        });

        // TODO:rca - make the collection view nested inside the container
        // and provide the selector functionality

        var $v = $(view.render().el);
        console.log($v.html());
        $('#test-area').append(view.render().el);

      });

    });
  });