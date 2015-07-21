/**
 * Heavily (ad-nauseam) annotated widget example
 *
 * This widget has a corresponding unittest at test/mocha/js/widget/hello_world_widget.spec.js
 *  - actually, every widget in BBB has a test
 *  - the path corresponds to the location of the widget
 *
 * The tests are included into a test-suite
 *  - test/mocha/ui-suite.js
 *  - and can be run in a browser: http://localhost:8000/test/mocha/tests.html?bbbSuite=ui-suite
 *  - or on command line: grunt test
 *
 *
 *  Little bit of theory first:
 *    - widget is a 'thing' that can be inserted into an application
 *    - it has a visual component (view) and the non-visual component (model and controller)
 *    - view just displays things, controller controls the logic (and model is just sitting there and think how
 *      important he is)
 *
 *
 */

/**
 * define() is a 'require.js' thing - it defines a model; the signature is:
 *  define(['list', 'of', 'imports'], function(my, foo, bar) {return something})
 */
define([
    // here we are importing using symbolic names, e.g. 'underscore' - this name
    // is mapped to the underscore library (in our discovery.config.js)

    'underscore', // very useful utilities
    'jquery', // if you don't know jquery, then shame on you!
    'backbone', // Model-View-Controller framework (for building applications)
    'marionette', // Extension on top of Backbone; does useful things for us

    // here we are importing our own components, you will notice the path is relative
    // to the src/ folder

    'js/components/api_query', // the holy trinity of BBB - query says what to do
    'js/components/api_request', // requests says where and how
    'js/components/api_response', // and response brings it back

    'js/widgets/base/base_widget', // (almost all) BBB widgets extend base_widget

    'js/bugutils/minimal_pubsub', // will create a test pubsub queue (very useful!)
    'test/mocha/js/widgets/test_json/test1', // and example response (to give to widget)

    // 'hbs!' is 'handlebars' template pre-processor, it will load the file and give you
    // executable template
    'hbs!js/widgets/hello_world/templates/layout'
  ],
  function( // all of the following names must correspond to the import above
    _,
    $,
    Backbone,
    Marionette,
    ApiQuery,
    ApiRequest,
    ApiResponse,
    BaseWidget,
    MinimalPubSub,
    TestData,
    WidgetTemplate
    ){

    /**
     * Model is a very useful thing, it has methods to save, load, and query itself.
     * For most part, you just need to know these:
     *  - get()
     *  - set()
     *
     */

    var Model = Backbone.Model.extend({
      defaults : {
        msg: undefined
      }
    });


    /**
     * View is 'bound' to the model, so everytime the model changes, the View will
     * update itself (ie. redraw)
     *
     * The idea is simple: your controller will update the model, and the view
     * will then show the model. Your controller will not be updating the view
     * directly. But it can ask the view to provide some information.
     *
     *
     * There exist several view types in Marionette and BBB:
     *
     *  Marionette.ItemView
     *    - the most basic view, it displays one model
     *  Marionette.CollectionView
     *    - the view for multiple models (i.e. list of items)
     *  Marionette.CompositeView
     *    - for multiple models + one global model (ie. if you
     *      want to display list of items, but at the same time
     *      some extra information around)
     *
     *  Bumblebee also has some views,the notable is:
     *
     *  js/widgets/list_of_things/paginated_view
     *    - use it when you want to have (you guessed it) pagination
     *      requires that you use special model (js/widgets/list_of_things/model)
     *
     *  js/widgets/facet/tree_view
     *    - for hierarchical (nested) view on facets
     */
    var View = Marionette.ItemView.extend({

      tagName : "div", // this view will create <div class="s-hello">....</div> html node
      className : "s-hello",

      template: WidgetTemplate,

      events: {
        'click button.hello': 'onButtonClick'
      },

      modelEvents: {
        // if we say 'change': 'render' the view will call 'render' on every change
        // 'change:msg' will ignore changes in 'name'
        "change" : 'render'
      },

      onButtonClick: function(ev) {
        if (ev) {
          ev.stopPropagation();
        }
        // send the data back to the controller, which will decide what to do with it
        this.trigger('name-changed', this.$el.find('input').val());
      }

    });


    var Controller = BaseWidget.extend({

      // you can use this to track changes happening to the model (ie. the view can put something
      // inside the model)
      //modelEvents: {
      //  'change': 'onModelChange'
      //},

      // you can also track the view
      viewEvents: {
         'name-changed': 'onNameChanged'
      },

      initialize : function(options){
        this.model = new Model();
        this.view = new View({model : this.model});
        BaseWidget.prototype.initialize.apply(this, arguments);
      },

      /**
       * This function is important: you can start listening to internal
       * chatter of the BBB application. Beehive is a central component,
       * you can get access to other services/objects through it.
       *
       * Here, we subscribe to a search.
       * You can read more about the search signals in:
       *
       *  docs/search-cycle.md
       *
       * @param beehive
       */
      activate: function (beehive) {
        this.setBeeHive(beehive); // most widgets will hold reference, to query BeeHive later...
        var pubsub = beehive.getService('PubSub');
        pubsub.subscribe(pubsub.INVITING_REQUEST, _.bind(this.onRequest, this));
        pubsub.subscribe(pubsub.DELIVERING_RESPONSE, _.bind(this.onResponse, this));
      },

      // triggered externally (e.g. by an user submitting a query)
      onRequest: function(apiQuery) {
        if (!(apiQuery instanceof ApiQuery) || !apiQuery.has('q'))
          throw new Error('You are kidding me!');

        var q = apiQuery.clone(); // we are bit parranoid, the queries arrive locked against changes
        q.unlock(); // so you have to create a copy and unlock it for modifications
        q.set('foo', this.model.get('name') || 'world');

        this.dispatchRequest(q); // calling out parent's method
      },

      // triggered externally, by a query-mediator, when it receives data for our query
      onResponse: function(apiResponse) {
        if (apiResponse.has('response.numFound')) {
          this.model.set('msg', 'The query found: ' + apiResponse.get('response.numFound') + ' results.');
        }
      },

      // this is triggered from the view
      onNameChanged: function(name) {
        if (name.toLowerCase() == 'bumblebee') {
          this.model.set('name', 'wonderful ' + name);
        }
        else {
          this.model.set('name', name);
        }
      }
    });


    return Controller;
  });