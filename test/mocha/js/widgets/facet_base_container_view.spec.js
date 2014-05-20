define([
    'jquery',
    'backbone',
    'marionette',
    'js/bugutils/minimal_pubsub',
    'js/widgets/facet/base_container_view',
    'js/widgets/facet/item_views',
    'js/widgets/facet/model',
    'js/widgets/facet/collection',
    'js/widgets/facet/item_view'
  ],
  function ($,
            Backbone,
            Marionette,
            MinimalPubsub,
            FacetBaseContainerView,
            ItemViews,
            FacetModel,
            FacetCollection,
            BaseItemView
    ) {

    describe("Facet Base Container View (UI)", function () {

      var minsub;

      beforeEach(function () {
        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            return {
              "responseHeader": {
                "status": 0,
                "QTime": 543,
                "params": {
                  "q": "star"
                }
              }
            };
          }
        }))({verbose: false});
      });

      afterEach(function () {
        minsub.close();
        return;
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
          itemView: ItemViews.CheckboxOneLevelView
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
          collection: new FacetCollection(),
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

        $('#test-area').append(view.render().el);
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

    });
  });