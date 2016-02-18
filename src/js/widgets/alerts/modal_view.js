
define([
    'marionette',
  'hbs!./templates/modal_template',
], function(
    Marionette,
    ModalTemplate
){

  var ModalView = Marionette.ItemView.extend({

    id : "#alert-modal-content",
    template : ModalTemplate,

    showModal: function() {
      $('#alert-modal').modal('show');
    },

    closeModal : function(){
      $('#alert-modal').modal('hide');
    },

    modelEvents : {
      'change' : 'render'
    },

    render: function() {
      //append parent container at end of html, where it needs to be
      //this will prevent creation of infinite modals at the end of the document as before
      if (!$("#modal-alert-content").length){
        //append to end of document
        $("body").append('<div class="modal fade" id="alert-modal" tabindex="-1" role="dialog" aria-labelledby="alert-modal-label" aria-hidden="true"></div>');
        this.setElement($("#alert-modal")[0]);
      }

      if (!this.model.get("modal")) return;

      //log the error to console as well
      if (this.model.get("type") === "danger"){
        console.error("error feedback: ", this.model.get("title"), this.model.get("msg"));
      }
      return Marionette.ItemView.prototype.render.apply(this, arguments);
    },

    onRender : function(){
      this.showModal();
    }


  });

  return ModalView

});