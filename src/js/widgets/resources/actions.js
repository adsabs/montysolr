'use strict';
define([
  'underscore',
  'js/components/api_query',
  'analytics'
], function (_, ApiQuery, analytics) {

  var FIELDS = [
    'links_data',
    '[citations]',
    'property',
    'bibcode',
    'first_author',
    'year',
    'page',
    'pub',
    'pubdate',
    'title',
    'volume',
    'doi',
    'issue',
    'issn'
  ];
  var API_QUERY_DELAY = 300;

  var actions = {};

  actions.updateResources = function (value) {
    return {
      type: 'UPDATE_RESOURCES',
      fullTextSources: value.fullTextSources,
      dataProducts: value.dataProducts
    };
  };

  actions.updateBibcode = function (value) {
    return {
      type: 'UPDATE_BIBCODE',
      value: value
    };
  };

  actions.loading = function () {
    return {
      type: 'IS_LOADING',
      value: true
    }
  };

  actions.loaded = function () {
    return {
      type: 'IS_LOADING',
      value: false
    }
  };

  actions.error = function (code, error) {
    return {
      type: 'IS_ERRORED',
      code: code,
      value: error
    }
  };

  actions.updateApiResponse = function (value) {
    return {
      type: 'UPDATE_API_RESPONSE',
      value: value
    }
  };

  actions.updateQuery = function (value) {
    return {
      type: 'UPDATE_QUERY',
      value: value
    }
  };

  /**
   * Emit an analytics event
   * @param text
   * @returns {Function}
   */
  actions.emitAnalytics = function (text) {
    return function () {
      analytics('send', 'event', 'interaction', 'full-text-link-followed', text);
    }
  };

  /**
   * Get the current query, clean up bibcode and send it off to load
   * the links
   * @param {object} apiQuery
   * @returns {Function}
   */
  actions.displayDocuments = function (apiQuery) {
    return function (dispatch) {
      dispatch(actions.updateQuery(apiQuery));
      var bibcode = apiQuery.get('q');

      if (bibcode && /bibcode:/.test(bibcode[0])) {
        bibcode = bibcode[0].replace('bibcode:', '');
        dispatch(actions.loadBibcodeData(bibcode));
      } else {
        return dispatch(actions.handleError('NO_BIBCODE', null));
      }
      dispatch(actions.loading());
    };
  };

  /**
   * Handle Errors
   * @param code
   * @param error
   * @returns {Function}
   */
  actions.handleError = function (code, error) {
    return function (dispatch, getState, widget) {

      // TODO: should be communicating state to the controller
      dispatch(actions.error(code, error));

      // Destroy the widget, something went wrong
      widget.destroy();
    }
  };

  /**
   * Handle the initial response from the pubsub
   * @param apiResponse
   * @returns {Function}
   */
  actions.processResponse = function (apiResponse) {
    return function (dispatch, getState, widget) {
      dispatch(actions.updateApiResponse(apiResponse));
      var data = apiResponse.get('response.docs[0]');

      if (!data || _.isEmpty(data)) {
        return dispatch(
          actions.handleError('EMPTY_RESPONSE', null));
      }

      // get the link server info, if exists
      data.link_server = widget
      .getBeeHive().getObject('User').getUserData('USER_DATA').link_server;

      // Send off the data to be parsed
      dispatch(actions.parseResources(data));
    };
  };

  /**
   * Create query to retrieve the link data
   * @param {String} bibcode
   * @returns {Function}
   */
  actions.loadBibcodeData = function (bibcode) {
    return function (dispatch, getState, widget) {
      if (bibcode === getState().bibcode) {

        // Trigger widget ready event
        widget.trigger('page-manager-event', 'widget-ready', {
          'isActive': true
        });
      } else {
        dispatch(actions.updateBibcode(bibcode));
        var searchTerm = 'bibcode:' + bibcode;

        // Dispatch new query
        widget.dispatchRequest(new ApiQuery({
          'q': searchTerm,
          fl: FIELDS.join(',')
        }));
      }
    };
  };

  /**
   * Parse the data links into separate types
   * @param data
   * @returns {Function}
   */
  actions.parseResources = function (data) {
    return function (dispatch, getState, widget) {
      try {

        // Parse the data links
        data = widget.parseResourcesData(data);
      } catch (e) {
        dispatch(actions.handleError('PARSER_ERROR', e));
      }

      // Update the store with the new resources
      dispatch(actions.updateResources(data));

      // Turn off the loading icon
      dispatch(actions.loaded());
    };
  };

  /**
   * Attempt to recover from failed api-query
   * @param feedback
   * @returns {Function}
   */
  actions.handleFeedback = function (feedback) {
    return function (dispatch, getState) {
      var stateQuery = getState().query.get('q')[0];
      if (feedback && feedback.request) {
        var feedbackQuery = feedback.request.get('query').get('q')[0];
        if (feedbackQuery === stateQuery) {

          // reset bibcode on error, so we don't prematurely say the widget is ready
          dispatch(actions.updateBibcode(null));

          _.delay(function () {
            dispatch(actions.handleError('API_QUERY_FAILED', null));
          }, API_QUERY_DELAY);
        }
      }
    };
  };

  return actions;
});
