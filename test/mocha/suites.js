'use strict';
define([
  'underscore'
], function (_) {

  // define testing suites
  var suites = {
    'core': [
     '/apps/discovery/router.spec',
     '/apps/discovery/navigator.spec',

     '/components/app_storage.spec',
     '/components/csrf_manager.spec',
     '/components/multi_params.spec',
     '/components/solr_params.spec',
     '/components/api_query.spec',
     '/components/json_response.spec',
     '/components/api_response.spec',
     '/components/generic_module.spec',
     '/components/pubsub_key.spec',
     '/components/facade.spec',
     '/components/beehive.spec',
     '/components/api_request.spec',
     '/components/query_mediator.spec',
     '/components/query_validator.spec',
     '/components/services_container.spec',
    // '/components/analytics.spec', // too slow
     '/components/application.spec',
     '/components/api_query_updater.spec',
     '/components/api_feedback.spec',
     '/components/feedback_mediator.spec',
     '/components/history_manager.spec',
     '/components/user.spec',
     '/components/session.spec',
     '/components/library_controller.spec',
     '/components/recaptcha_manager.spec',

     '/components/discovery_mediator.spec',
     '/components/alerts_mediator.spec',
     '/components/persistent_storage.spec',

     '/services/backbone.events.spec',
     '/services/pubsub.spec',
     '/services/api.spec',

     '/mixins/dependon.spec',
     '/mixins/link_generator_mixin.spec',
     '/mixins/add_stable_index_to_collection.spec',
     '/mixins/page-manager-mixin.spec',
     '/mixins/papers_utils.spec',
     '/mixins/openurl_generator.spec',
     '/mixins/add_secondary_sort.spec',
     '/mixins/widget_state_manager.spec'
    ],
    'ui': [
     '/page_managers/all_tests.spec.js',
     '/page_managers/toc_controller.spec.js',
     '/page_managers/toc_manager_test.spec.js',
     '/widgets/authentication_widget.spec.js',
     '/widgets/abstract_widget.spec.js',
     '/widgets/alerts_widget.spec.js',
     '/widgets/api_query_widget.spec.js',
     '/widgets/api_request_widget.spec.js',
     '/widgets/api_response_widget.spec.js',
     '/widgets/base_widget.spec.js',
     '/widgets/breadcrumb_widget.spec.js',
     '/widgets/bubble_chart.spec.js',
     '/widgets/citation_graph_facet_widget.spec.js',
     '/widgets/dropdown_widget.spec.js',
     '/widgets/classic_search_widget.spec.js',
     '/widgets/export_widget.spec.js',

     '/widgets/facet_widget.spec.js',

     '/widgets/filter_visualizer_widget.spec.js',
     '/widgets/graphics_widgets.spec.js',
     '/widgets/libraries_all.spec.js',
     '/widgets/library_individual.spec.js',
     '/widgets/library_list_widget.spec.js',
     '/widgets/library_import_widget.spec.js',
     '/widgets/list_of_things_widget.spec.js',
     '/widgets/lot_derivates.spec.js',
     '/widgets/metrics_widget.spec.js',
     '/widgets/navbar_widget.spec.js',
     '/widgets/network_widget.spec.js',
     '/widgets/orcid_selector_widget.spec.js',
     '/widgets/paper_network_widget.spec.js',
     '/widgets/preferences_widget.spec.js',
     '/widgets/query_info_widget.spec.js',
     '/widgets/reads_graph_facet_widget.spec.js',
     '/widgets/recommender_widget.spec.js',
     '/widgets/resources_widget.spec.js',
     '/widgets/results_render_widget.spec.js',
     '/widgets/search_bar_widget.spec.js',
     '/widgets/sort_widget.spec.js',
     '/widgets/citation_helper_widget.spec.js',
     '/widgets/author_affiliation_tool.spec.js',
     '/widgets/simbad_object_facet.spec',
     '/widgets/ned_object_facet.spec',

     //TBD 24/09/14 '/widgets/similar_widget.spec.js',
     '/widgets/tabs_widget.spec.js',
     '/widgets/user_settings_widget.spec.js',
     '/widgets/wordcloud_widget.spec.js',
     '/widgets/year_graph_facet_widget.spec.js',
     '/widgets/hello_world_widget.spec.js',
     '/widgets/meta_tags_widget.spec.js'
    ],
    'orcid': [
     '/modules/orcid/orcid_api.spec',
     '/modules/orcid/orcid_extension.spec',
     '/modules/orcid/orcid_widget.spec',
     '/modules/orcid/work.spec',
     '/modules/orcid/profile.spec'
    ],
    'qb': [
     '/components/query_builder/plugin.spec',
     '/components/query_builder/rules_translator.spec',
     '/components/query_builder/translation_query2ui.spec',
     '/components/query_builder/translation_ui2query.spec',
     '/components/query_builder/plugin.testcases.spec'
    ],
    'wraps': [
      '/wraps/alerts_mediator.spec'
    ]
  };

  // discovery is an aggregation of all suites in one
  suites.discovery = [].concat.apply([], _.values(suites));

  return suites;
});
