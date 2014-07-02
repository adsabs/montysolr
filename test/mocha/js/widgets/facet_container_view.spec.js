define([
    'jquery',
    'backbone',
    'marionette',
    'js/widgets/facet/container_view',
    'js/widgets/facet/model',
    'js/widgets/facet/collection',
    'js/widgets/base/item_view',
    'hbs!js/widgets/facet/templates/logic-container'
  ],
  function ($,
            Backbone,
            Marionette,
            FacetContainerView,
            FacetModel,
            FacetCollection,
            BaseItemView,
            LogicSelectionContainerTemplate
    ) {

    describe("FacetContainerView (UI)", function () {

        afterEach(function (done) {
          var ta = $('#test-area');
          if (ta) {
            ta.empty();
          }
          done();
        });


        it("returns FacetBaseContainerView object", function (done) {
          expect(new FacetContainerView()).to.be.instanceof(FacetContainerView);
          expect(new FacetContainerView()).to.be.instanceof(Marionette.CompositeView);
          done();
        });

        it("can load more results and page through them (and hide load-more if there is none)", function (done) {
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
          $('#test-area').append($v);

          expect($v.find('.widget-options.bottom').hasClass('hide')).to.be.false;
          expect($v.find('.widget-options.top').hasClass('hide')).to.be.false;

          // expect 5 items in the facet (all hidden)
          expect($v.find('.item-view').length).to.be.equal(5);
          expect($v.find('.item-view').filter('.hide').length).to.be.equal(5);

          // display the first batch
          view.displayMore(view.displayNum);

          expect($v.find('button[wtarget="ShowMore"]').text()).to.be.equal('show more');

          var cc = all.callCount;

          // click on the load more
          $v.find('button[wtarget="ShowMore"]').click();

          expect(view.onShowMore.called).to.be.true;
          expect(all.callCount).to.be.equal(cc + 2);

          // the controller should call displayMore() - so let's emulate it
          view.displayMore(2);
          expect($v.find('.item-view').length).to.be.equal(5);
          expect($v.find('.item-view').filter('.hide').length).to.be.equal(0);

          view.disableShowMore("foo");
          expect($v.find('button[wtarget="ShowMore"]').text()).to.be.equal('');

          done();
        });


        it("can show multiple logic selection boxes and handle them", function (done) {
          var view = new FacetContainerView({
            itemView: BaseItemView,
            model: new FacetContainerView.ContainerModelClass({title: "Facet Title"}),
            collection: new FacetCollection(),
            displayNum: 3,
            maxDisplayNum: 10,
            openByDefault: true,
            showOptions: true,
            template: LogicSelectionContainerTemplate,
            logicOptions: {single: ["limit to", "exclude"], multiple: ["and", "or", "exclude"]}
          });

          sinon.spy(view, 'refreshLogicTooltip');
          sinon.spy(view, 'enableLogic');
          sinon.spy(view, 'closeLogic');

          var fired = sinon.spy();
          view.on('containerLogicSelected', fired);

          var $v = $(view.render().el);
          $('#test-area').append(view.render().el);

          view.collection.add(new Backbone.Model({title: 'foo1', value: 'bar1'}));
          view.collection.add(new Backbone.Model({title: 'foo2', value: 'bar2'}));
          view.collection.add(new Backbone.Model({title: 'foo3', value: 'bar3'}));

          expect(view.refreshLogicTooltip.callCount).to.be.equal(6);
          expect(view.enableLogic.callCount).to.be.equal(2);
          expect(view.closeLogic.callCount).to.be.equal(2);

          // selecting one of the options should activate the tooltip
          $v.find('.widget-item:first').click();

          expect($v.find('.logic-container').is(':visible')).to.be.true;
          expect($v.find('.logic-container > label:eq(0) > input').val()).to.eql('limit to');
          expect($v.find('.logic-container > label:eq(1) > input').val()).to.eql('exclude');

          // selecting from the optins triggers onLogic()

          $v.find('.logic-container label:first input').attr('checked', 'checked').trigger('change');

          expect($v.find('.logic-container').is(':visible')).to.be.false;
          expect(fired.callCount).to.be.equal(1);
          expect(fired.args[0][0]).to.be.equal('limit to');

          // selecting two options, activates tooltip with different options
          $v.find('.widget-item:eq(2)').click();

          expect($v.find('.logic-container').is(':visible')).to.be.true;
          expect($v.find('.logic-container > label:eq(0) > input').val()).to.eql('and');
          expect($v.find('.logic-container > label:eq(1) > input').val()).to.eql('or');
          expect($v.find('.logic-container > label:eq(2) > input').val()).to.eql('exclude');

          // resetting the collection should close the tooltip
          view.collection.reset();
          expect($v.find('.logic-container').is(':visible')).to.be.false;
          done();
        });

      });
  });