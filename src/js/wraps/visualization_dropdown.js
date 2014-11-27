define([
  'js/widgets/dropdown-menu/widget'

], function(

  DropdownWidget

  ){

  //config

  var links = [
    {href : '/results/author-network' , description : 'Author Network' , navEvent: 'show-author-network'}
  ];

  var btnType = "btn-primary-faded";

  var dropdownTitle = "Visualize";

  var iconClass = "icon-visualize";

  var VisDropdown = new DropdownWidget({links : links,
    btnType: btnType,
    dropdownTitle : dropdownTitle,
    iconClass: iconClass });

  return VisDropdown;

});