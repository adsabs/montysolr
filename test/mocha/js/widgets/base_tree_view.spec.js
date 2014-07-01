define([
    'jquery',
    'backbone',
    'marionette',
    'js/widgets/base/container_view',
    'js/widgets/base/tree_view'
  ],
  function ($,
            Backbone,
            Marionette,
            BaseContainerView,
            TreeView
    ) {

    describe("Tree view (nested items)", function() {

      afterEach(function () {
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
      });

      it("should display nested collection of items", function() {
        var coll = new TreeView.CollectionClass();

        coll.add(new TreeView.ModelClass({title: 'foo', value: 'bar' , 'children': []}));
        coll.add(new TreeView.ModelClass({title: 'boo', value: 'bab' , 'children': []}));
        coll.add(new TreeView.ModelClass({title: 'doo', value: 'bad' , 'children': []}));
        coll.add(new TreeView.ModelClass({title: 'woo', value: 'baw' , 'children': []}));
        coll.add(new TreeView.ModelClass({title: 'zoo', value: 'baz' , 'children': []}));
        coll.add(new TreeView.ModelClass({title: 'ooo', value: 'bao' , 'children': []}));

        var view = new BaseContainerView({
          itemView: TreeView,
          model: new BaseContainerView.ContainerModelClass({title: "Widget Title"}),
          collection: coll,
          openByDefault:true
        });

        var spy = sinon.spy();
        view.on('all', spy);

        var $v = $(view.render().el);
        $('#test').append($v);

        expect($v.find('.widget-body > div.item-view').length).to.be.equal(6);
        expect($($v.find('.widget-body > .item-view input:eq(0)')).val()).to.be.equal('bar');
        expect($($v.find('.widget-body > .item-view input:eq(1)')).val()).to.be.equal('bab');

        view.collection.models[0].children.add(
          new TreeView.ModelClass({title: 'hey', value: 'joe' , 'children': []}));

        spy.reset();


        // click on the first item
        $v.find('.widget-item:first').click();
        expect($($v.find('input[value="joe"]')).is(':visible')).to.be.true;
        expect(spy.callCount).to.be.eql(1);
        expect(spy.args[0][1].collection.models[0].get('title')).to.be.equal('hey');
        expect(spy.args[0][1].model.get('selected')).to.be.equal(true);


        // click again
        $v.find('.item-leaf:first').click();
        expect($($v.find('input[value="joe"]')).is(':visible')).to.be.false;

      });
    });

  });