/**
 * Created by rchyla on 3/19/14.
 *
 * This is for interactive testing of UI widgets. You can load different modules
 * and attach them to DOM like so:
 *
 * http://localhost:8000/test/test-widgets.html?top-region-left=js/widgets/api_query/widget&top-region-right=js/widgets/api_request/widget&middle-region-left=js/widgets/api_response/widget
 *
 * Each widget must:
 *   - be instantiable without parameters
 *   - have render() (returns DOM to attach to the region)
 *   - have activate() (called after loading the module; it will receive BeeHive with PubSub service)
 *
 *
 */

require(['js/components/application',
  'jquery',
  'underscore'],
  function(Application,
           $,
           _) {

  var d = window.location.search.substring(1);

  if (!d) {
    console.log('You need to supply list of plugins in the url');
    window.location.search = "top-region-left=js/widgets/api_query/widget&top-region-right=js/widgets/api_request/widget&middle-region-left=js/widgets/api_response/widget";
    return;
  }

  var app = new Application();
  var config = {
    core: {
      services: {
        PubSub: 'js/services/pubsub',
        Api: 'js/services/api'
      },
      objects: {
        QueryMediator: 'js/components/query_mediator'
      }
    },
    widgets: {
    }
  };

  // discover components that should be loaded dynamically
  var components = {};
  _.each(d.split('&'), function(element, index, list) {
    var kv = element.split('=');
    components[kv[0].trim()]=kv[1].trim();
  });

  config.widgets = components;

  var promise = app.loadModules(config);
  promise.done(function() {

    app.activate();

    // print debugging info to console
    app.getBeeHive().getService('PubSub').on('all', function() {console.log('[all:PubSub]', arguments[0], JSON.stringify(arguments[1].toJSON()), arguments[2].getId(), _.toArray(arguments).slice(3))});
    app.getBeeHive().getService('Api').on('all', function() {console.log('[all:Api]', arguments[0], _.toArray(arguments).slice(1))});

    // manually render all widgets (we'll discover their parent element)
    _.each(app.getAllWidgets(), function(w) {
      var widget_name = w[0];
      var widget = w[1];

      // find parent of the widget (if exists)
      var parent = 'body';
      var parts = widget_name.split('-');
      var i = parts.length;
      while (i > 0) {
        var new_name = parts.slice(0, i).join('-');
        if ($.find('#' + new_name).length) {
          parent = '#' + new_name;
          break;
        }
        else if ($.find(new_name).length) {
          parent = new_name;
          break;
        }
        i -= 1;
      }

      var el;
      if (widget_name.substring(0,1) == '#') {
        el = $(widget_name);
      }
      else if ($.find(widget_name).length) {
        el = $(widget_name);
      }
      else {
        el = $('#' +widget_name);
      }
      var ww = widget.render();
      if (ww.el) {
        el.append(ww.el);
      }
      else {
        el.append(ww);
      }

      $(parent).append(el);

    });

  });

});

