// This is the 'bootloader' of the application; it is referenced
// from the html page. You can think of it as application creator
// (and hence also app controller)

require(["config"], function() {
    
  // Kick off the application.
  // RC: I don't understand why it is nested, can it be
  // RC: ['config', 'app', 'router']
  require(["app", "router", 'app-view', 'todo-view', 'todo-model', 'todo-collection'], function(app, Router, AppView, TodoView, TodoModel, TodoCollection) {
    
    app.TodoRouter = new Router();
    
    app.Todo = new TodoModel();
    app.todos = new TodoCollection(TodoModel);
    app.TodoView = TodoView; // just a reference, will get instantiated by the collection
    
    app.AppView = new AppView();
    
    Backbone.history.start();
    
  });
});