define([
  'js/widgets/dropdown-menu/widget'

], function(

  DropdownWidget

  ){

  //config

  var links = [
    {href : '/results/metrics' , description : 'Citation Metrics' , navEvent: 'show-metrics'},
    {href : '/results/author-network' , description : 'Author Network' , navEvent: 'show-author-network'},
    {href : '/results/paper-network' , description : 'Paper Network' , navEvent: 'show-paper-network'},
    {href : '/results/wordcloud' , description : 'Word Cloud', navEvent: 'show-wordcloud'}
  ];

  var btnType = "btn-primary-faded";

  var dropdownTitle = "Explore";

  var iconClass = "icon-explore";

  var rightAlign = true;


  return function(){

    var VisDropdown = new DropdownWidget({
      links : links,
      btnType: btnType,
      dropdownTitle : dropdownTitle,
      iconClass: iconClass,
      rightAlign : rightAlign
    });

    return VisDropdown;

  }


});