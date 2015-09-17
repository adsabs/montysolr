define([
    'underscore',
    'jquery',
    'js/page_managers/toc_controller',
    'js/bugutils/minimal_pubsub',
    'js/widgets/hello_world/widget'

  ],
  function(
    _,
    $,
    TocController,
    MinSub,
    HelloWorld
  ) {

    describe("TOC Manager", function () {

      it("broadcasts messages", function(done) {
        var minsub = new MinSub();
        var toc = new TocController();

        sinon.spy(toc, 'onPageManagerEvent');
        sinon.spy(toc, 'broadcast');
        var allSpy = sinon.spy();
        minsub.subscribe('all', allSpy);

        toc.activate(minsub.beehive);

        toc.assemble({
          hasWidget: function(n) {
            if (n == 'SearchWidget')
              return true;
          },
          _getWidget: function(n) {
            var w = new HelloWorld();
            w.activate(minsub.beehive.getHardenedInstance());
            return w;
          }
        });

        expect(toc.widgets.SearchWidget).to.be.defined;

        toc.widgets.SearchWidget.trigger('page-manager-event', 'widget-ready');
        console.log(JSON.stringify(toc.broadcast.lastCall.args))
        expect(toc.broadcast.lastCall.args).to.eql(["page-manager-message","widget-ready",{"widgetId":"SearchWidget","isActive":false}]);

        toc.widgets.SearchWidget.trigger('page-manager-event', 'widget-selected', {idAttribute: 'foo'});
        expect(allSpy.lastCall.args.slice(0, 3)).to.eql(["[Router]-Navigate-With-Trigger","foo",{"idAttribute":"foo"}]);

        toc.widgets.SearchWidget.trigger('page-manager-event', 'broadcast-payload', {title: 'foo'});
        expect(toc.broadcast.lastCall.args).to.eql(["page-manager-message","broadcast-payload",{"title":"foo"}]);

        //console.log(JSON.stringify(toc.broadcast.lastCall.args))
        //console.log(JSON.stringify(allSpy.lastCall.args))

        done();
      });
    });
  });