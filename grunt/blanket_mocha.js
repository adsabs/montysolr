'use strict';
/**
 * Options for the `blanket-mocha` grunt task
 *
 * @module grunt/blanket-mocha
 */
module.exports = {
  full: {
    options: {
      urls: [
        'http://localhost:<%= local.port || 8000 %>/test/mocha/coverage.html?bbbSuite=discovery-suite'
      ],
      threshold: 0,
      globalThreshold: 75,
      log: true,
      logErrors: true,
      moduleThreshold: 80,
      modulePattern: '../../js/(.*)',
      customModuleThreshold: {

        'components/api_query_updater.js': 78,
        'widgets/facet/graph-facet/h_index_graph.js': 38,

        'widgets/facet/graph-facet/widget.js': 60,
        'widgets/facet/graph-facet/year_graph.js': 58,
        'widgets/facet/graph-facet/h_index_graph.js ': 38,
        'apps/discovery/navigator.js': 30,
        'apps/discovery/router.js': 37,
        'wraps/graph_tabs.js': 5,
        'widgets/facet/graph-facet/base_graph.js': 8,
        'widgets/export/widget.js': 23,
        'mixins/widget_mixin_method.js': 37,
        'page_managers/three_column_view.js': 60,
        'mixins/widget_utility.js': 40,
        'components/query_builder/rules_translator.js': 45,
        'components/csrf_manager.js': 25,
        'widgets/base/tree_view.js': 50,
        'widgets/list_of_things/item_view.js': 50,
        'widgets/base/base_widget.js': 51,
        'widgets/breadcrumb/widget.js': 55,
        'components/navigator.js': 60,
        'mixins/dependon.js': 61,
        'widgets/query_info/query_info_widget.js': 46,
        'widgets/resources/widget.js': 72,
        'wraps/table_of_contents.js': 73,
        'bugutils/minimal_pubsub.js': 74,
        'components/history_manager.js': 75,
        'components/api_feedback.js': 77,
        'components/transition.js': 77,
        'widgets/dropdown-menu/widget.js': 78,
        'wraps/paper_network.js': 77, // some tests don't run properly in phantomjs,
        'wraps/paper_export.js': 68,
        'widgets/recommender/widget.js': 65,
        'wraps/discovery_mediator.js': 5, // these two guys are complex to test (but i've already started)
        'mixins/feedback_handling.js': 35,
        'mixins/discovery_bootstrap.js': 1,
        'widgets/navbar/widget.js': 53,
        'widgets/success/view.js': 60,
        'components/library_controller.js': 74,
        'widgets/wordcloud/widget.js': 78,
        'components/analytics.js': 71,
        'wraps/landing_page_manager/landing_page_manager': 48,
        'widgets/libraries_all/views/view_all_libraries.js': 78
      }
    }
  },
  test: {
    options : {
      urls: [
        'http://localhost:<%= local.port || 8000 %>/test/mocha/coverage.html?bbbSuite=core-suite'
      ],
      threshold : 0,
      globalThreshold : 63,
      log : true,
      logErrors: true,
      moduleThreshold : 0,
      modulePattern : '../../src/js/(.*)'
    }
  }
};
