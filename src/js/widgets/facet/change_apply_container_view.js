define(['./container_view'], function (BaseContainerView) {
  var ChangeApplyContainerView = BaseContainerView.extend({

    initialize: function (options) {
      //a change in the "newValue" attribute for any model
      //in the collection will trigger an autosubmit
      this.listenTo(this.collection, "change:newValue", this.submitFacet);
      BaseContainerView.prototype.initialize.call(this, options);
    },

    // XXX:rca - this mechanism is not maintainable; better to
    // listen to all signals inside controller and decide what
    // to do with them
    submitFacet: function () {
      this.trigger("changeApplySubmit");
    }

  });
  return ChangeApplyContainerView;
});