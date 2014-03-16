/**
 * Created by rchyla on 3/16/14.
 */

define(['backbone', 'underscore', 'js/components/facade', 'pubsub_service_impl'], function(Backbone, _, Facade, PubSubImplementation) {

  if (_.isUndefined(PubSubImplementation)) {
    PubSubImplementation = require('js/services/default_pubsub');
  }

  return function(options) {

    var allowed = _.pick(options, ['strict', 'handleErrors', 'errWarningCount']);

    var interface = {
      start: 'start the queue',
      stop: 'close the queue',
      subscribe: 'register callback',
      publish: 'send data to the queue',
      getPubSubKey: 'get secret key'
    };

    return new Facade(interface, new PubSubImplementation({}, allowed));

  }

});
