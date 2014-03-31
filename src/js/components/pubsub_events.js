/**
 * Created by rchyla on 3/30/14.
 */

/**
 * Catalogue of PubSub events; we assume this:
 *
 *  - IC = Inner City; the component lives in the 'Forbidden city'
 *         inside Application, typically this is a PubSub or Api, Mediator
 *         or any component with elevated access
 *
 *  - OC = Outer Sity: the suburbs of the application; these are typically
 *         UI components (behind the wall), untrusted citizens of the
 *         BumbleBee state
 *
 */

define([], function() {
  var PubSubEvents = {

    /**
     * Usually called by OC's as a first step in the query processing.
     * It means: 'user did something', we need to start reacting. The OC
     * will build a new ApiQuery and send it together with this event
     */
    NEW_QUERY: 'query-changed',

    /**
     * Called by IC's (usually: Mediator) - this is a signal to *all* OC's
     * they should receive ApiQuery object, compare it against their
     * own query; find diff and create a new ApiResponse (asking for a data)
     * and send that back
     */
    WANTING_REQUEST: 'query-from-inner-city',

    /**
     * Will be called by OC's, this is response to ApiQuery input.
     */
    NEW_REQUEST: 'request-for-inner-city',


    /**
     * Called by Mediator; this is a request for data (ApiResponse)
     *  - input: ApiRequest
     *  - output: ApiResponse
     */
    WANTING_RESPONSE: 'want-data-from-treasury',

    /**
     * Published by IC's - typically Api - when response has been retrieved
     * for a given ApiRequest
     *
     *  - input: ApiRequest
     *  - output: ApiResponse
     */
    NEW_RESPONSE: 'response-from-treasury',


    /**
     * This event will be published by IC's - typically 'Mediator'. It
     * got data from API and now it is passing it to the OC's. OC's
     * should subscribe to this event when they want to receive data
     * from the treasury (api)
     */
    SENDING_RESPONSE: 'response-from-inner-city'

  };

  return PubSubEvents;
})
