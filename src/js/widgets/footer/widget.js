define([
  "marionette",
  "hbs!./footer"
], function(
  Marionette,
  FooterTemplate
  ){



  var Footer = Marionette.ItemView.extend({

    template : FooterTemplate,

    className : "footer s-footer"

  })


  return Footer


})