define(['./base_container_view'], function (BaseContainerView) {
  var ChangeApplyContainerView = BaseContainerView.extend({

    initialize: function (options) {
      //a change in the "newValue" attribute for any model
      //in the collection will trigger an autosubmit
      this.listenTo(this.collection, "change:newValue", this.submitFacet);
      BaseContainerView.prototype.initialize.call(this, options);
    },

    submitFacet: function () {
      this.trigger("changeApplySubmit")
    }

  });
  return ChangeApplyContainerView;
});