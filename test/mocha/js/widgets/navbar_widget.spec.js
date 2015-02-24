define([
  'js/widgets/navbar/widget'

], function(

  NavBarWidget

  ){



 describe("navigation bar widget", function(){

  afterEach(function(){
    $("#test").empty();
  });


   it("should  query initial logged in / logged out state states in order to render the correct values", function(){

     var t = new NavBarWidget();
     $("#test").append(t.view.render().el)



   })

   it("should listen for app events like orcid sign in or out and re-render in response", function(){


   })

   it("should notify the orcid api on user signin or signout", function(){



   })











 })




})