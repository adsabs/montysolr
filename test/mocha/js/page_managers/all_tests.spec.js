define([
    'underscore',
    'jquery',
    'marionette',
    'js/components/application',
    'js/widgets/base/base_widget',
    'js/page_managers/three_column_view',
    'js/page_managers/controller',
    'hbs!/test/mocha/js/page_managers/one-column',
    'hbs!/test/mocha/js/page_managers/three-column',
    'js/page_managers/one_column_view',
    'js/page_managers/toc_controller',
    'hbs!/test/mocha/js/page_managers/toc-layout',
    '../widgets/test_json/test1',
    'js/components/api_response',
    'js/components/api_query',
    'hbs!/test/mocha/js/page_managers/master-manager',
    'hbs!/test/mocha/js/page_managers/simple'
  ],
  function(
    _,
    $,
    Marionette,
    Application,
    BaseWidget,
    ThreeColumnView,
    PageManagerController,
    OneColumnTemplate,
    ThreeColSearchResultsTemplate,
    OneColumnView,
    TOCPageManagerController,
    TOCTemplate,
    testData,
    ApiResponse,
    ApiQuery,
    MasterTemplate,
    SimpleTemplate

    ) {

  describe("Three column PageManager", function () {

    var config = null;
    beforeEach(function() {
      config = {
        core: {
          services: {
            'Api': 'js/services/api',
            'PubSub': 'js/services/pubsub'
          },
          modules: {
            QM: 'js/components/query_mediator'
          },
          objects: {
            'Navigator': 'js/components/navigator'
          }
        },
        widgets: {
          SearchWidget: 'js/widgets/search_bar/search_bar_widget',
          Results: 'js/widgets/results/widget',
          AuthorFacet: 'js/wraps/author_facet',
          GraphTabs : 'js/wraps/graph_tabs',

          TOCWidget: 'js/page_managers/toc_widget',
          ShowAbstract: 'js/widgets/abstract/widget',
          ShowReferences: 'js/widgets/references/widget',

          PageManager: 'js/page_managers/controller'
        }
      };
    });

    describe("Three column page manager", function() {
      it("should create page manager object", function() {
        expect(new PageManagerController()).to.be.instanceof(BaseWidget);
      });

      it("assembles the page view", function(done) {
        var app = new Application();
        app.loadModules(config).done(function() {

          // hack (normally this will not be the usage pattern)
          var pageManager = app.getWidget("PageManager");
          pageManager.createView = function(options) {
            var TV = ThreeColumnView.extend({template: ThreeColSearchResultsTemplate});
            return new TV(options);
          };
          // var pageManager = new (PageManagerController.extend({
          // createView: function(options) {return new ThreeColumnView(options)}
          // }))();

          app.activate();
          pageManager.assemble(app);

          //$('#test').append(pageManager.view.el);
          expect(_.keys(pageManager.widgets).length).to.be.gt(1);

          var $w = pageManager.view.$el;
          expect($w.find('[data-widget="AuthorFacet"]').children().length).to.be.equal(1);
          expect($w.find('[data-widget="Results"]').children().length).to.be.equal(1);
          expect($w.find('[data-widget="GraphTabs"]').children().length).to.be.equal(1);

          done();
        });

      });
    });

    describe("One column page manager", function() {

      it("assembles the page view", function(done) {
        var app = new Application();
        app.loadModules(config).done(function() {

          // hack (normally this will not be the usage pattern)
          var pageManager = app.getWidget("PageManager");
          pageManager.createView = function(options) {
            var TV = OneColumnView.extend({template: OneColumnTemplate});
            return new TV(options)
          };

          app.activate();
          pageManager.assemble(app);

          //$('#test').append(pageManager.view.el);
          var $w = pageManager.view.$el;
          expect($w.find('[data-widget="SearchWidget"]').children().length).to.be.equal(1);

          done();
        });

      });
    });

    describe("TOC page manager", function() {

      it("assembles the page view", function(done) {
        var app = new Application({debug: false});
        delete config.core.objects.Navigator;
        config.widgets.PageManager = 'js/page_managers/toc_controller';

        app.loadModules(config).done(function() {

          // hack (normally this will not be the usage pattern)
          var pageManager = app.getWidget("PageManager");
          pageManager.createView = function(options) {
            var TV = ThreeColumnView.extend({template: TOCTemplate});
            return new TV(options)
          };

          app.activate();
          pageManager.assemble(app);

          //$('#test').append(pageManager.view.el);
          var $w = pageManager.view.$el;
          expect($w.find('[data-widget="SearchWidget"]').children().length).to.be.equal(1);
          expect($w.find('[data-widget="ShowAbstract"]').children().length).to.be.equal(1);
          expect($w.find('[data-widget="ShowReferences"]').children().length).to.be.equal(1);

          pageManager.show('SearchWidget', 'ShowAbstract', 'TOCWidget');

          // deliver data to the widget for display
          var abstract = app.getWidget('ShowAbstract');
          var references = app.getWidget('ShowReferences');
          var r = new ApiResponse(testData);
          r.setApiQuery(new ApiQuery({q: 'foo'}));

          abstract.processResponse(r);

          // the navigation must turn active
          expect(pageManager.view.$el.find('[data-widget-id="ShowAbstract"]').hasClass('s-abstract-nav-inactive')).to.be.false;
          expect(pageManager.view.$el.find('[data-widget-id="ShowReferences"]').hasClass('s-abstract-nav-inactive')).to.be.true;

          // simulated late arrival
          references.processResponse(r);
          expect(pageManager.view.$el.find('[data-widget-id="ShowAbstract"]').hasClass('s-abstract-nav-inactive')).to.be.false;
          expect(pageManager.view.$el.find('[data-widget-id="ShowReferences"]').hasClass('s-abstract-nav-inactive')).to.be.false;

          // click on the link (NAVIGATE event should be triggered)
          var pubsub = app.getService('PubSub').getHardenedInstance();
          var spy = sinon.spy();
          pubsub.subscribe(pubsub.NAVIGATE, spy);
          pageManager.view.$el.find('[data-widget-id="ShowReferences"]').click();
          expect(spy.callCount).to.be.eql(1);

          // it has to be selected and contain numcount
          expect(pageManager.view.$el.find('[data-widget-id="ShowReferences"]').hasClass('s-abstract-nav-active')).to.be.true;
          expect($(pageManager.view.$el.find('div[data-widget-id="ShowReferences"] span')).text().trim()).to.eql('(841359)');
          done();
        });

      });
    });

    describe("Master page manager", function() {
      it("swapping of page managers in/out manually", function(done) {
        var app = new Application({debug: false});
        delete config.widgets.PageManager;
        config.widgets.FirstPageManager = 'js/page_managers/controller';
        config.widgets.SecondPageManager = 'js/page_managers/toc_controller';

        app.loadModules(config).done(function() {

          var navigator = app.getObject('Navigator');
          navigator.router = new Backbone.Router();

          var firstPageManager = app.getWidget("FirstPageManager");
          var secondPageManager = app.getWidget("SecondPageManager");

          firstPageManager.createView = function(options) {
            var TV = ThreeColumnView.extend({template: ThreeColSearchResultsTemplate});
            return new TV(options);
          };
          secondPageManager.createView = function(options) {
            var TV = ThreeColumnView.extend({template: TOCTemplate});
            return new TV(options);
          };

          app.activate();
          firstPageManager.assemble(app);
          secondPageManager.assemble(app);

          //var $body = $('#test');
          var $body = $('<div/>');

          navigator.set('show-stuff', function() {
            $body.children().detach();
            $body.append(app.getWidget('SecondPageManager').show().el);
          });


          $body.append(firstPageManager.show().el);

          $body.find('[data-widget="SearchWidget"] input.q').val('foo');
          expect($body.find('[data-widget="SearchWidget"] input.q').val()).to.be.equal('foo');
          expect($body.find('[data-widget="AuthorFacet"]').length).to.be.equal(1);
          expect($body.find('[data-widget="TOCWidget"]').length).to.be.equal(0);

          var pubsub = app.getService('PubSub').getHardenedInstance();
          pubsub.publish(pubsub.NAVIGATE, 'show-stuff');

          expect($body.find('[data-widget="SearchWidget"] input.q').val()).to.be.equal('foo');
          expect($body.find('[data-widget="AuthorFacet"]').length).to.be.equal(0);
          expect($body.find('[data-widget="TOCWidget"]').length).to.be.equal(1);

          done();
        });

      });

      it("using PageManager object", function(done) {
          var app = new Application({debug: false});
          delete config.widgets.PageManager;
          config.core.objects.PageManager = 'js/page_managers/master';
          config.widgets.FirstPageManager = 'js/page_managers/controller';
          config.widgets.SecondPageManager = 'js/page_managers/toc_controller';

          app.loadModules(config).done(function() {

            var navigator = app.getObject('Navigator');
            navigator.router = new Backbone.Router();

            var masterPageManager = app.getObject('PageManager');
            var firstPageManager = app.getWidget("FirstPageManager");
            var secondPageManager = app.getWidget("SecondPageManager");

            firstPageManager.createView = function(options) {
              var TV = ThreeColumnView.extend({template: ThreeColSearchResultsTemplate});
              return new TV(options);
            };
            secondPageManager.createView = function(options) {
              var TV = ThreeColumnView.extend({template: TOCTemplate});
              return new TV(options);
            };

            app.activate();
            masterPageManager.assemble(app);

            navigator.set('show-stuff', function() {
              app.getObject('PageManager').show('SecondPageManager');
            });

            //var $body = $('#test'); $body.append(masterPageManager.view.el);

            masterPageManager.show('FirstPageManager');

            masterPageManager.view.$el.find('[data-widget="SearchWidget"] input.q').val('foo');
            expect(masterPageManager.view.$el.find('[data-widget="SearchWidget"] input.q').val()).to.be.equal('foo');
            expect(masterPageManager.view.$el.find('[data-widget="AuthorFacet"]').length).to.be.equal(1);
            expect(masterPageManager.view.$el.find('[data-widget="TOCWidget"]').length).to.be.equal(0);

            var pubsub = app.getService('PubSub').getHardenedInstance();
            pubsub.publish(pubsub.NAVIGATE, 'show-stuff');

            expect(masterPageManager.view.$el.find('[data-widget="SearchWidget"] input.q').val()).to.be.equal('foo');
            expect(masterPageManager.view.$el.find('[data-widget="AuthorFacet"]').length).to.be.equal(0);
            expect(masterPageManager.view.$el.find('[data-widget="TOCWidget"]').length).to.be.equal(1);

            done();
          });
      });
    });
  });
});