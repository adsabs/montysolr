define(function(require) {
  "use strict";

  var Backbone = require("backbone");
  var Router = require("router");

  // Test that the Router exists.
  describe("Router", function() {
    it("should exist", function() {
      expect(Router).to.exist;
      expect(new Router()).to.be.an.instanceof(Backbone.Router);
    });

    it("should fail when history is started without todos initialized", function() {
      chai.assert.throw(function() {Backbone.history.start();}, Error, "Cannot call method");
    });
  });
});