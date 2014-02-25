define(function(require, exports, module) {
  "use strict";

  // External dependencies.
  var Backbone = require("backbone");
  var app = require("app");
  
  // Todo Router
	// ----------
	module.exports = Backbone.Router.extend({
		routes: {
			'*filter': 'setFilter'
		},

		setFilter: function (param) {
			// Set the current filter to be used
			app.TodoFilter = param || '';

			// Trigger a collection filter event, causing hiding/unhiding
			// of Todo view items
			// XXX: bad design, we are not exposing api
			app.todos.trigger('filter');
		}
	});

});
