define([], function() {
  var config = {

    /**
     * You can change the application
     */

    /**
     * Where to insert the application
     */
    targetElement: 'div#embed',
    TargetWidget: 'js/widgets/green_button/widget',

    map: {
      '*': {
        'pubsub_service_impl': 'js/services/default_pubsub',
        'analytics_config': 'embed.vars',
        'dynamic_config2': 'embed.dyn'
      }
    },

    config: {
      'js/apps/bumblebox/main': {
        widgets: {
          TargetWidget: 'js/widgets/hello_world/widget'
        }
      }
    }
  };
  return config;
});
