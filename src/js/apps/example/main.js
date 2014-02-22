// This is the 'bootloader' of the application; it is referenced
// from the html page. You can think of it as application creator
// (and hence also app destroyer)


console.log('loading: main.js');


// here we are creating a function which loads our application. 
// 'config' - is a dependency for our application

require(["config"], function() {
    
  // Kick off the application.
  // RC: I don't understand why it is nested, can it be
  // RC: ['config', 'app', 'router']
  console.log('Creating a function which kicks off the application');
  
  require(["app", "router"], function(app, Router) {
    
    console.log('main.js: app was created:');
    console.log(app);
    console.log('   Router=' + Router);
    
    // Define your master router on the application namespace and trigger all
    // navigation from this instance.
    app.router = new Router();

    // Trigger the initial route and enable HTML5 History API support, set the
    // root folder to '/' by default.  Change in app.js.
    Backbone.history.start({
      pushState: true,
      root: app.root
    });
  });
});