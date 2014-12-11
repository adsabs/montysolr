define([
  'js/widgets/dropdown-menu/widget'

], function(

  DropdownWidget

  ){

  //config

  var links = [
    {href : '/results/metrics' , description : 'Metrics' , navEvent: 'show-metrics'}
  ];

  var btnType = "btn-info";

  var dropdownTitle = "Analyze";

  var iconClass = "icon-analyze";

  var MetricsDropdown = new DropdownWidget(
    {links : links,
    btnType: btnType,
    dropdownTitle : dropdownTitle,
    iconClass: iconClass });

  return MetricsDropdown;

});