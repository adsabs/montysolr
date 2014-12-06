define([], function() {

  var tests  = [
      '/components/multi_params.spec.js',
      '/components/solr_params.spec.js',
      '/components/api_query.spec.js',
      '/components/json_response.spec.js',
      '/components/api_response.spec.js',
      '/components/generic_module.spec.js',
      '/components/pubsub_key.spec.js',
      '/components/facade.spec.js',
      '/components/beehive.spec.js',
      '/components/api_request.spec.js',
      '/components/query_mediator.spec.js',
      '/components/services_container.spec.js',
      '/components/application.spec.js',
      '/components/api_query_updater.spec.js',
      '/components/api_feedback.spec.js',
      '/components/feedback_mediator.spec.js',
      '/components/history_manager.spec.js',

      '/services/backbone.events.spec.js',
      '/services/pubsub.spec.js',
      '/services/api.spec.js',

      '/mixins/dependon.spec.js',
      '/mixins/link_generator_mixin.spec.js',
      '/mixins/add_stable_index_to_collection.spec.js',
      '/mixins/page-manager-mixin.spec.js'
  ];
  return tests;
});