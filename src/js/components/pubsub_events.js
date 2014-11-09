/**
 * Created by rchyla on 3/30/14.
 */

/**
 * Catalogue of PubSub events; we assume this:
 *
 *  - FC = the component lives in the 'Forbidden City'
 *         inside Application, typically this is a PubSub or Api, Mediator
 *         or any component with elevated access
 *
 *  - OC = Outer City: the suburbs of the application; these are typically
 *         UI components (behind the wall), untrusted citizens of the
 *         BumbleBee state
 *
 *  WARNING: do not use spaces; events with spaces are considered to be
 *        multiple events! (e.g. '[PubSub] New-Query' will be two events)
 *
 */

define([], function() {
  var PubSubEvents = {

    /**
     * Usually called by OC's as a first step in the query processing.
     * It means: 'user did something', we need to start reacting. The OC
     * will build a new ApiQuery and send it together with this event
     */
    START_SEARCH: '[PubSub]-New-Query',

    /**
     * Called by FC's (usually: Mediator) - this is a signal to *all* OC's
     * they should receive ApiQuery object, compare it against their
     * own query; find diff and create a new ApiRequest (asking for a data)
     * and send that back
     */
    INVITING_REQUEST: '[PubSub]-Inviting-Request',

    /**
     * Will be called by OC's, this is response to ApiQuery input.
     */
    DELIVERING_REQUEST: '[PubSub]-New-Request',

    /**
     * Published by FC's - typically Mediator - when a response has been retrieved
     * for a given ApiRequest.
     *
     * OC's should subscribe to this event when they want to receive data
     * from the treasury (api)
     *
     *  - input: ApiRequest
     *  - output: ApiResponse
     */
    DELIVERING_RESPONSE: '[PubSub]-New-Response',


    /**
     * The walls of the FC are being closed; and no new requests will be served
     */
    CLOSING_GATES: '[PubSub]-Closing',

    /**
     * PubSub will not receive any requests any more
     */
    CLOSED_FOR_BUSINESS: '[PubSub]-Closed',

    /**
     * ForbiddenCity is about to receive requests
     */
    OPENING_GATES: '[PubSub]-Opening',

    /**
     * Called after PubSub became ready - it is fully operational
     */
    OPEN_FOR_BUSINESS: '[PubSub]-Ready',

    /**
     *  Set of error warnings issues by PubSub - or by some other FC's - to
     *  deal with congestion or other problems
     */
    SMALL_FIRE: '[PubSub]-Problem',
    BIG_FIRE: '[PubSub]-Big-Problem',
    CITY_BURNING: '[PubSub]-Disaster',


    /**
     * A message containing feedback from the FC; traveling towards OC
     * The feedback will be instance of ApiFeedback
     */
    DELIVERING_FEEDBACK: '[FC]-FeedBack',


    /**
     * A message from the router requesting showing citizens of the
     * city
     */
    DISPLAY_DOCUMENTS: '[Router]-Display-Documents',

    /**
     * Used by OC to request parsed query tree - to check a query
     * for example
     */
    GET_QTREE: '[FC]-GetQTree',

    NAVIGATE : '[Router]-Navigate-Without-Trigger',

    /*for custom widget-to-widget events*/

    CUSTOM_EVENT : '[PubSub]-Custom-Event',


    ARIA_ANNOUNCEMENT : "[PubSub]-Aria-Announcement"


};

  return PubSubEvents;
});
