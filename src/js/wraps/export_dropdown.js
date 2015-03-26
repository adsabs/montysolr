define([
  'js/widgets/dropdown-menu/widget'

], function(

  DropdownWidget

  ){

  //config

  var links = [
    {href : '/export/bibtex' , description : 'BibTEX' , navEvent: 'export-bibtex'},
    {href : '/export/aastex' , description : 'AASTex' , navEvent: 'export-aastex'},
    {href : '/export/endnote' , description : 'EndNote' , navEvent: 'export-endnote'},
  ];

  var btnType = "btn-primary-faded";
  var dropdownTitle = "Export";
  var iconClass = "icon-export";
  var rightAlign = true;


  return function(){

    var Dropdown = new DropdownWidget({
      links : links,
      btnType: btnType,
      dropdownTitle : dropdownTitle,
      iconClass: iconClass,
      rightAlign : rightAlign
    });

    return Dropdown;
  }
});