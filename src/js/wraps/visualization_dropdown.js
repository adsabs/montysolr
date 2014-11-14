define([
  'js/widgets/dropdown-menu/widget'

], function(

  DropdownWidget

  ){

  //config

  var links = [
    {href : '/results/author-network' , description : 'Author Network' , navEvent: 'ShowAuthorNetwork'}

  ];

  var btnType = "btn-primary-faded";

  var dropdownTitle = "Visualize";

  var VisDropdown = new DropdownWidget({links : links, btnType: btnType, dropdownTitle : dropdownTitle })

  return VisDropdown;


})