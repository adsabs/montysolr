define([
    'jquery',
    'backbone',
    'marionette',
    'js/widgets/base/container_view',
    'js/widgets/base/item_view'
  ],
  function ($,
            Backbone,
            Marionette,
            BaseContainerView,
            BaseItemView
    ) {

    describe("Widget Base Container View (UI)", function () {

      var wModel = Backbone.Model.extend({
        defaults: function () {
          return {
            title: undefined,
            value: undefined,
            selected: false,
            newValue: undefined
          }
        }
      });

      afterEach(function () {
        var ta = $('#test-area');
        if (ta) {
          ta.empty();
        }
      });


      it("returns FacetBaseContainerView object", function () {
        expect(new BaseContainerView()).to.be.instanceof(BaseContainerView);
        expect(new BaseContainerView()).to.be.instanceof(Marionette.CompositeView);
      });

      it("by default it shows nothing", function() {
        var view = new BaseContainerView({
          itemView: BaseItemView
        });
        var $v = $(view.render().el);
        expect($v.find('.widget-name').length).to.be.equal(1);
        expect($v.find('.widget-name > h5').text().trim()).to.be.equal("");
        expect($v.find('.widget-body').length).to.be.equal(1);


        var view = new BaseContainerView({
          itemView: BaseItemView,
          model: new BaseContainerView.ContainerModelClass({title: "Widget Title"})
        });
        var $v = $(view.render().el);
        expect($v.find('.widget-name > h5').text().trim()).to.be.equal("Widget Title");
      });

      it("to render something, it needs a collection", function() {

        var view = new BaseContainerView({
          itemView: BaseItemView,
          model: new BaseContainerView.ContainerModelClass({title: "Widget Title"}),
          collection: new Backbone.Collection(null, {model: wModel})
        });

        // this would normally be done by a controller
        view.collection.add(new Backbone.Model({title: 'foo', value: 'bar'}));

        var $v = $(view.render().el);
        expect($v.find('input').parent().text().trim()).to.be.equal('foo');
        expect($v.find('input').val()).to.be.equal('bar');

        // add item, it should re-paint
        view.collection.add(new Backbone.Model({title: 'woo', value: 'baz'}));
        expect($v.find('input[value="baz"]').parent().text().trim()).to.be.equal('woo');

        // by default the body of the facet is hidden
        expect($v.find('.widget-body').hasClass('hide')).to.be.true;

        // by clicking on title/caret it will be toggled
        $v.find('.widget-name > h5').click();
        expect($v.find('.widget-body').hasClass('hide')).to.be.false;
        $v.find('.widget-name > h5 > .main-caret').click();
        expect($v.find('.widget-body').hasClass('hide')).to.be.true;

        //$('#test-area').append(view.render().el);
      });

      it("can be started opened", function() {
        var c = new Backbone.Collection(null, {model: wModel});
        c.add(new Backbone.Model({title: 'foo', value: 'bar'}));
        c.add(new Backbone.Model({title: 'foo', value: 'baz'}));

        var view = new BaseContainerView({
          itemView: BaseItemView,
          model: new BaseContainerView.ContainerModelClass({title: "Widget Title"}),
          collection: c,
          openByDefault: true,
          showOptions: true
        });

        var $v = $(view.render().el);
        expect($v.find('.widget-body').hasClass('hide')).to.be.false;
        expect($v.find('.facet-options').hasClass('hide')).to.be.false;
      });

      it("has methods to indicate different states", function() {
        var view = new BaseContainerView();
        sinon.spy(view, "handleError");
        sinon.spy(view, "handleWaiting");
        sinon.spy(view, "revertState");
        var $v = $(view.render().el);


        view.handleSanity({error: {msg: 'foo'}});
        expect(view.handleError.called).to.be.true;
        expect($v.find('.widget-name').hasClass('error')).to.be.true;

        view.handleSanity({waiting: 500});
        expect(view.handleWaiting.called).to.be.true;
        expect($v.find('.widget-body').hasClass('waiting')).to.be.true;
        expect($v.find('.widget-name').hasClass('error')).to.be.true;

        view.handleSanity({ok: true});
        expect(view.revertState.callCount).to.be.equal(2);
        expect($v.find('.widget-body').hasClass('waiting')).to.be.false;
        expect($v.find('.widget-name').hasClass('error')).to.be.false;

      });

      it("accepts other collectionviews and plays nicely with them", function() {
        var c = new Backbone.Collection(null, {model: wModel});
        c.add(new Backbone.Model({title: 'foo', value: 'bar'}));
        c.add(new Backbone.Model({title: 'woo', value: 'baz'}));

        // we'll render container inside container - the nested one will be collapsed
        // by default
        var view = new BaseContainerView({
          itemView: BaseContainerView.extend({openByDefault:false}),
          model: new BaseContainerView.ContainerModelClass({title: "Widget Title"}),
          collection: c,
          openByDefault: true
        });

        var $v = $(view.render().el);

        //$('#test-area').append(view.render().el);

        expect($v.find('.widget-body:eq(0)').hasClass('hide')).to.be.false;
        expect($v.find('.widget-body .widget-body:nth(0)').hasClass('hide')).to.be.true;
        expect($v.find('.widget-body .widget-body:nth(1)').hasClass('hide')).to.be.true;

        // click the nested first container
        $v.find('.widget-body .widget-name:nth(0) > h5').click();
        expect($v.find('.widget-body:eq(0)').hasClass('hide')).to.be.false;
        expect($v.find('.widget-body:eq(1)').hasClass('hide')).to.be.false;
        expect($v.find('.widget-body:eq(2)').hasClass('hide')).to.be.true;

        // collapse the top container
        $v.find('.widget-name:eq(0) > h5').click();
        expect($v.find('.widget-body:eq(0)').hasClass('hide')).to.be.true;
        expect($v.find('.widget-body:eq(1)').hasClass('hide')).to.be.false;
        expect($v.find('.widget-body:eq(2)').hasClass('hide')).to.be.true;


        // reopen top container (the 0th widget should still be opened)
        $v.find('.widget-name:eq(0) > h5').click();
        expect($v.find('.widget-body:eq(0)').hasClass('hide')).to.be.false;
        expect($v.find('.widget-body:eq(1)').hasClass('hide')).to.be.false;
        expect($v.find('.widget-body:eq(2)').hasClass('hide')).to.be.true;


      });

      it("can handle clicks inside options", function() {
        var c = new Backbone.Collection(null, {model: wModel});
        c.add(new Backbone.Model({title: 'foo', value: 'bar'}));
        c.add(new Backbone.Model({title: 'foo', value: 'baz'}));

        var Modified = BaseContainerView.extend({
          onRender: function() {
            this._onRender();
            this.$(".widget-options.bottom").append($('<a>Load More</a>'));
          },
          onLoadMore: function(ev) {
            //console.log('load more');
          }
        });

        var view = new Modified({
          itemView: BaseItemView,
          model: new BaseContainerView.ContainerModelClass({title: "Widget Title"}),
          collection: c,
          openByDefault: true,
          showOptions: true
        });

        sinon.spy(view, 'onLoadMore');

        var $v = $(view.render().el);
        $v.find('.widget-options.bottom > a').click();

        expect(view.onLoadMore.called).to.be.true;

      });
    });


  });