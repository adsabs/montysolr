Confusion has sneaked inside the minds of developers with regards to how pubsub and the search cycle work together.
This doc provides brief, but mightily powerful cure:


Basic concepts:
==============

  - ApiQuery: an instance that holds query parameters, basically a dictionary where all values are arrays 
    ```{foo: ['bar']}```
    
  - ApiRequest: an instance which holds information for *how to execute* the query; it is given to the API
    ```new ApiRequest({'query': apiQuery, 'target': '/search'})```
    
  - ApiResponse: an instance that holds the response from the server (can be a class of SolrResponse or 
     JsonResponse or something else). This is what you receive from Api, when ApiRequest was executed
     
  - PubSub: service, it doesn't accept functions, you can only exchange ApiQuery, ApiRequest, ApiResponse through it
  
  - Api: service responsible for getting data
  
  - QueryMediator: controller which decides how queries get executed; widget has no knowledge of this
     controller (and shouldn't), but this is where all the smartness (or stupidity) lives. Query mediator
     *actually* receives the requests from widgets, broadcasts it other interested widgets, collects
     requests, executes them and then delivers responses
     
     
How to:
=======
     
If you want to write a widget, you need to follow these simple rules:
     
     - ```pubsub.publish(START_SEARCH, apiQuery)``` will initiate global search. You are basically
     letting other widgets (in the applicaiton) know that this is the new query. 
     
     *BE CAREFUL*: the query in this
     phase should NOT CONTAIN information that is irrelevant for other widgets. E.g. if your widget is getting
     highlights, don't insert this information into the query now. Do it later! Don't force 
     other widgets to get highlights (because they absolutely have no clue why and what to do with it!)
     
     - in order to receive the ```ApiQuery```, the widget must listen to signal INVITING_REQUEST; i.e.
     ```pubsub.subscribe(pubsub.INVITING_REQUEST, _.bind(this.onInviteRequest, this))```. 
     
     When the widget
     receives the new query, it will decide whether it wants to get data for it - i.e. it will
     modify the query (e.g. add new parameters), create ```ApiRequest``` and send it back to the queue
     ```pubsub.publish(DELIVERING_REQUEST, apiRequest)```: 
     
     - the response will arrive (eventually) - to get it, the widget must listen to DELIVERING_RESPONSE, i.e.
     ```pubsub.subscribe(pubsub.DELIVERING_RESPONSE, _.bind(this.onDeliverResponse, this))```
     
     That is the basic cycle: START_SEARCH -> DELIVERING_REQUEST -> DELIVERING_RESPONSE
     
A common mistake is to listen to START_SEARCH: don't do that, if you are a widget developer, you hardly
ever need to listen to that signal. You just need to issue it and then listen to 'INVITING_REQUEST'


There are some other useful signals for a widget:
     
     - ```pubsub.publish(EXECUTE_REQUEST, apiQuery)```: to get data from Api (individually), without
     starting a new search cycle
     
     - ```pubsub.subscribe(FEEDBACK, _.bind(this.onFeedback, this)```: here you can get info
     about the global state of request, e.g.
        - SEARCH_CYCLE_STARTED
        - SEARCH_CYCLE_FAILED_TO_START
        - SEARCH_CYCLE_FINISHED

        
Want to dig deeper?
===================

Every widget is isolated from the rest; and Bumblebee is enforcing it - this means that your world (as a widget
developer) is very simple. You don't need to care about anything that happens outside of your domain,  
you cant influence it anyway....

But if you are writing a controller (something that has access to the application, or to other widgets), then 
you will need to understand more. But if you need to understand more, go into code
and read the following:

  - api.js
  - default_pubsub.js 
  - query_mediator.js 
  - feedback_mediator.js (and derived ...._mediator.js libraries; this is where error handling happens)
  - api_feedback.js (list of self-diagnostic messages)
  
  
All of these are very well tested, if you get lost, look at their unittests. Good luck!  