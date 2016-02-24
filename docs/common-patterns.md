
##widget can publish an alert

  //raise an error
            var pubsub = this.getPubSub();
            pubsub.publish(pubsub.ALERT, new ApiFeedback({
              code: ApiFeedback.CODES.ALERT,
              msg: "Page requested that does not exist: " + page,
              type: "danger",
              modal : true
            }));



## widgets stay in sync with data from server

for situations where a widget can update a value that is propagated the the rest of
the app and maybe to a server, the most convenient pattern in bumblebee is

1. when being activated, widget requests current value
2. when widget updates value, it calls a controller method that propagates it to rest of app/contacts server
3. Once #2 is complete, the controller publishes the change to all interested widgets
4. only then does the widget that initiated the change see the change in the ui

so widgets initialize with correct value and then stay updated based on pubsub

this pattern is used in libraries and for the page number preferences


## widgets that display bibcodes should have the following methods:

1. renderWidgetForCurrentQuery(data)
-- widget uses internal getCurrentQuery function and renders itself
@param {object} data

2.  renderWidgetForListOfBibcodes(bibcodeArray, data)
@param {array} bibcodeArray
@param {object} data
