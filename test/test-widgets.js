/**
 * Created by rchyla on 3/19/14.
 *
 * This is for interactive testing of UI widgets. You can load different modules
 * and attach them to DOM like so:
 *
 * http://localhost:8000/test/test-widgets.html?top-region-left=js/widgets/api_query/widget&top-region-right=js/widgets/api_response/widget
 *
 * Each widget must:
 *   - be instantiable without parameters
 *   - have render() (returns DOM to attach to the region)
 *   - have activate() (called after loading the module; it will receive BeeHive with PubSub service)
 *
 *
 */

require(['js/components/beehive', 'js/services/pubsub',
  'jquery', 'underscore'], function(BeeHive, PubSub, $, _) {

  var d = window.location.search.substring(1);

  if (!d) {
    return;
  }

  var beehive = new BeeHive();
  beehive.addService('PubSub', new PubSub());

  var components = {};
  _.each(d.split('&'), function(element, index, list) {
    var kv = element.split('=');
    components[kv[0].trim()]=kv[1].trim();
  });

  var regions = _.keys(components);
  var modules = _.values(components);

  if (modules.length > 0) {
    console.log('loading', modules);
    require(modules, function() {

      var implementations = arguments;
      _.each(regions, function(element, index, list) {
        var impl = implementations[index]; // implementation of the module
        var region_id = element;
        var impl_name = modules[index];
        console.log('creating', impl_name, 'for', region_id);

        // find element to attach the widget to
        var parent = 'body';
        var parts = region_id.split('-');
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

        // instantiate the widget and attach to parent
        try {
          var widget = new impl();

          if ('activate' in widget) {
            widget.activate(beehive);
          }
          var el;
          if (region_id.substring(0,1) == '#') {
            el = $(region_id);
          }
          else if ($.find(region_id).length) {
            el = $(region_id);
          }
          else {
            el = $('#' +region_id);
          }
          el.append(widget.render());
          $(parent).append(el);
        }
        catch(e) {
          console.warn(e.message);
        }

      });

    });
  }

});

