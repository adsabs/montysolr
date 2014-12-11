This document presents brief overview of the design/architecture of bumblebee


Design principles
=================

* constant change:  bumblebee is engineered for change. No component is expected to live long.

* mini-apps: We have multiple single-page applications (right one, only one is completed: `discovery`),
  they are self contained and can be loaded/unloaded and inserted into a webpage. A mini-app will
  contain multiple widgets/modules and plugins. In fact, a mini-app can be a very complex beast.

* encapsulation: Bumblebee doesn't export global objects, everything is contained 
  inside mini-app namespace. We do not export anything into `window` or any other global element. 
  This means that several mini-apps can be loaded into one page, and run without interference.

* trusted vs untrusted components: there exists concept of `core` and the `periphery` inside mini-app.
  Core is the `application` object, with `services` and `controllers`. All `widgets` and `plugins`
  are considered 'untrusted'. The isolation is enforced through `facade pattern`; this means that 
  widgets/plugins have no chance to reach into core (and change)
  
* pubsub: communication between core and periphery happens through publish-subscribe queue.
  (note: right now, the pubsub is open, but in the future only objects of certain type will be allowed
  to travel through it - e.g. we'll not allow widgets to send `functions` - so it is advisable to limit
  yourself to `ApiQuery` and `ApiRequest` objects)

  
High-level overview
===================

It is important to understand relationships between components of the mini-app.

* `application` - a container that loads all modules/plugins/objects etc; it lives as long as the 
  mini-app itself is alive. It has *absolute control* over its own components,
  can load/unload them at will. But in reality, the `application` object is not very smart,
  it does not interfere with the internal life of its components. It just provides environment.
  
  * `controllers` - these components have elevated access rights, they can reach into the application,
     load/unload/destroy other components (example: `feedback_mediator` - listens to internal
     feedback messages and modifies the state of the application/UI based on them)
       
  * `beehive` - this is a central component, almost every component has access to it. It provides
    access to services and objects. 
    Beehive is amorphous access point. It has one face for 'core components', and different face to 'periphery' 
    - i.e. widgets cannot change it (and nothing inside it). This is because every widget will 
    receive `hardened` version of beehive (the real object is hidden behind a facade and the widget has no way 
    to access/change the real object; this holds true for beehive and to anything that Beehive contains)
    
    * `Services` - beehive exposes (potentially) many services, they provide functionality to the periphery
      e.g. `Api` (communicates with remote web server, fetches data) or `PubSub` (controls the publish-subscribe
      queue)
      
    * `Objects` - container that provides access to data, e.g. `User` (has info about the current user) or
      `Environment` (holds info about the browser, display port etc)
      
  * `page managers` - containers for the UI elements. A `page manager` can contain other page managers, 
    (when you look at bumblebee UI, you are looking at contents that was built by one of the page-managers
  
    * `widget` - widget provides a view on some data and allows user to interact with them, it is isolated
       the only way how widget communicates through the mini-app is `pubsub`
      
  



How a mini-app is loaded
========================

All our components follow AMD principles, and are loaded through require.js, the mini-app
is loaded like so:

```
  <script data-main="./discovery.config" src="./libs/requirejs/require.js"></script>
```
  
When require.js loads the config, it will trigger load of `apps/<mini-app>/main.js`. 
`main.js` will create an `application` instance object (this is a simplified pseudocode example):

```
  var app = new Application()    // Application=src/js/components/application.js
  app.loadModules(configuration) // config as loaded in the first step
  app.activate()                 // all loaded components are activated
  app.configure()                // app is given chance to load (remote) configuration
  app.boostrap().done(           // each mini-app's custom logic that sets things up
    app.start()                  // after this point, app is ready, router/history works
  )
```  