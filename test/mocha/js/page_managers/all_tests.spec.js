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
    'hbs!/js/wraps/abstract_page_manager/abstract-page-layout',
    '../widgets/test_json/test1',
    'js/components/api_response',
    'js/components/api_query',
    'hbs!/test/mocha/js/page_managers/master-manager',
    'hbs!/test/mocha/js/page_managers/simple',
    'js/components/beehive',
    'js/services/pubsub'
  ],
  function( _,
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
    SimpleTemplate,
    Beehive,
    PubSub
    ){

    describe("PageManager (all_tests.spec.js)", function () {

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
            ShowAbstract: 'js/widgets/abstract/widget',
            ShowReferences: 'js/wraps/references',

            PageManager: 'js/page_managers/controller'
          }
        };
      });

      describe("Three column page manager", function() {
        it("should create page manager object", function() {
          expect(new PageManagerController()).to.be.instanceof(BaseWidget);
        });

        it("should give children the same pubsub instance", function() {
          var beehive = new Beehive();
          beehive.addService('PubSub', new PubSub());
          beehive.activate();
          var pm = new PageManagerController();

          pm.activate(beehive);

          expect(pm.getPubSub()).to.eql(pm.getPubSub());
          expect(pm.getPubSub().__facade__).to.be.true;
        });


        it("assembles/disassembles the page view", function(done) {
          var app = new Application({debug: false});
          delete config.core.objects.Navigator;
          config.widgets.PageManager = 'js/wraps/abstract_page_manager/abstract_page_manager';

          app.loadModules(config).done(function() {

            var pageManager = app._getWidget("PageManager");
            app.activate();
            pageManager.assemble(app);

            //$('#test').append(pageManager.view.el);
            expect(_.keys(pageManager.widgets).length).to.be.gt(1);

            var $w = pageManager.view.$el;
            expect($w.find('[data-widget="ShowAbstract"]').children().length).to.be.equal(1);
            expect($w.find('[data-widget="SearchWidget"]').children().length).to.be.equal(1);

            // normally called by master page-manager
            expect(pageManager.assembled).to.eql(true);
            expect(app.__widgets['widget:SearchWidget']).to.be.defined;

            pageManager.disAssemble(app);
            expect(app.returnWidget('PageManager')).to.eql(0);
            expect(pageManager.assembled).to.eql(false);

            expect(app.__widgets['widget:SearchWidget']).to.be.undefined;
            done();
          });
        });

        it("the three-col view reacts to user actions", function() {

          var view = new ThreeColumnView();

          expect(view.model.get('left')).to.be.eql('open');
          expect(view.model.get('right')).to.be.eql('open');
          expect(view.model.get('user_left')).to.be.eql(null);
          expect(view.model.get('user_right')).to.be.eql(null);

          view.showCols({left: true, right: false});

          expect(view.model.get('left')).to.be.eql('open');
          expect(view.model.get('right')).to.be.eql('closed');
          expect(view.model.get('user_left')).to.be.eql(null);
          expect(view.model.get('user_right')).to.be.eql(null);

          view.model.set('user_left', 'open');
          view.showCols({left: false, right: false});

          expect(view.model.get('left')).to.be.eql('open');
          expect(view.model.get('right')).to.be.eql('closed');
          expect(view.model.get('user_left')).to.be.eql('open');
          expect(view.model.get('user_right')).to.be.eql(null);

          view.showCols({left: false, right: true, force: true});
          expect(view.model.get('left')).to.be.eql('closed');
          expect(view.model.get('right')).to.be.eql('open');
          expect(view.model.get('user_left')).to.be.eql(null);
          expect(view.model.get('user_right')).to.be.eql(null);
        });
      });

      describe("One column page manager", function() {

        it("assembles the page view", function(done) {
          var app = new Application();
          app.loadModules(config).done(function() {

            // hack (normally this will not be the usage pattern)
            var pm = app.__widgets.get('PageManager');
            app.__widgets.remove('PageManager');
            app.__widgets.add('PageManager', function() {
              var x = new pm();
              x.createView = function(options) {
                var TV = OneColumnView.extend({template: OneColumnTemplate});
                return new TV(options)
              };
              return x;
            });

            app.getWidget("PageManager").done(function(pageManager) {
              app.activate();
              pageManager.assemble(app);

              //$('#test').append(pageManager.view.el);
              var $w = pageManager.view.$el;
              expect($w.find('[data-widget="SearchWidget"]').children().length).to.be.equal(1);

              done();
            });
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

            // hack (normally this will not be the usage pattern)
            var pm = app.__widgets.get('FirstPageManager');
            app.__widgets.remove('FirstPageManager');
            app.__widgets.add('FirstPageManager', function() {
              var x = new pm();
              x.createView = function(options) {
                var TV = ThreeColumnView.extend({template: ThreeColSearchResultsTemplate});
                return new TV(options);
              };
              return x;
            });
            var pm2 = app.__widgets.get('SecondPageManager');
            app.__widgets.remove('SecondPageManager');
            app.__widgets.add('SecondPageManager', function() {
              var x = new pm2();
              x.createView = function(options) {
                var TV = ThreeColumnView.extend({template: TOCTemplate});
                return new TV(options);
              };
              return x;
            });

            var navigator = app.getObject('Navigator');
            navigator.router = new Backbone.Router();

            var firstPageManager = app._getWidget("FirstPageManager");
            var secondPageManager = app._getWidget("SecondPageManager");


            app.activate();
            firstPageManager.assemble(app);
            secondPageManager.assemble(app);

            //var $body = $('#test');
            var $body = $('<div/>');

            navigator.set('show-stuff', function() {
              $body.children().detach();
              $body.append(app._getWidget('SecondPageManager').show().el);
            });


            $body.append(firstPageManager.show().el);

            $body.find('[data-widget="SearchWidget"] input.q').val('foo');
            expect($body.find('[data-widget="SearchWidget"] input.q').val()).to.be.equal('foo');
            expect($body.find('[data-widget="AuthorFacet"]').length).to.be.equal(1);

            var pubsub = app.getService('PubSub').getHardenedInstance();
            pubsub.publish(pubsub.NAVIGATE, 'show-stuff');

            expect($body.find('[data-widget="SearchWidget"] input.q').val()).to.be.equal('foo');
            expect($body.find('[data-widget="AuthorFacet"]').length).to.be.equal(0);

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

            // hack (normally this will not be the usage pattern)
            var pm = app.__widgets.get('FirstPageManager');
            app.__widgets.remove('FirstPageManager');
            app.__widgets.add('FirstPageManager', function() {
              var x = new pm();
              x.createView = function(options) {
                var TV = ThreeColumnView.extend({template: ThreeColSearchResultsTemplate});
                return new TV(options);
              };
              return x;
            });
            var pm2 = app.__widgets.get('SecondPageManager');
            app.__widgets.remove('SecondPageManager');
            app.__widgets.add('SecondPageManager', function() {
              var x = new pm2();
              x.createView = function(options) {
                var TV = ThreeColumnView.extend({template: TOCTemplate});
                return new TV(options);
              };
              return x;
            });

            var navigator = app.getObject('Navigator');
            navigator.router = new Backbone.Router();

            var masterPageManager = app.getObject('PageManager');
            var firstPageManager = app._getWidget("FirstPageManager");
            var secondPageManager = app._getWidget("SecondPageManager");


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

            var pubsub = app.getService('PubSub').getHardenedInstance();
            pubsub.publish(pubsub.NAVIGATE, 'show-stuff');

            expect(masterPageManager.view.$el.find('[data-widget="SearchWidget"] input.q').val()).to.be.equal('foo');
            expect(masterPageManager.view.$el.find('[data-widget="AuthorFacet"]').length).to.be.equal(0);

            var firstChild = masterPageManager.getCurrentActiveChild();
            sinon.spy(firstChild, 'disAssemble');
            expect(firstChild.widgets.SearchWidget).to.be.defined;
            masterPageManager.show('SecondPageManager');
            expect(firstChild.widgets.SearchWidget).to.be.undefined;
            expect(firstChild.disAssemble.called).to.eql(true);

            // would happen only if the master is nested
            _.each(masterPageManager.collection.models, function(model) {
              expect(model.get('object')).to.not.eql(null);
            });
            masterPageManager.disAssemble();
            _.each(masterPageManager.collection.models, function(model) {
              expect(model.get('object')).to.eql(null);
            });
            done();
          });
        });
      });
    });
  });