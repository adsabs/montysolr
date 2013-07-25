function expand(details_id){
 document.getElementById(details_id).className = "page_details_expanded";
}

function collapse(details_id){
 document.getElementById(details_id).className = "page_details";
}

function change(details_id){
  if(document.getElementById(details_id).className.match("page_details_expanded")){
     collapse(details_id);
  }
  else {
     expand(details_id);
  }
}
function zp(num,count) {
  var ret = num + '';
  while(ret.length < count) {
    ret = "0" + ret;
  }
  return ret;
}

function doClick(graphId, ev, msec, pts) {
  d = new Date(msec);
  top.location = d.getFullYear() + "." + zp(1+d.getMonth(), 2) + "." + zp(d.getDate(), 2) + "." + zp(d.getHours(), 2) + "." + zp(d.getMinutes(), 2) + "." + zp(d.getSeconds(), 2) + "/" + graphId + "/test-view.html";
}

function doGraph(graphId) {
  return new Dygraph(
    document.getElementById(graphId),
    graphId + ".csv",
    {title: graphId, 
     xlabel: "Date", 
     ylabel: "Avg response time (ms)", 
     labelsKMB: true,
     clickCallback: function c(ev, msec, pts) {             
    doClick(graphId, ev, msec, pts);
   },
     labelsDivStyles: {'background-color': 'transparent'}, 
     errorBars: true, 
     sigma: 1, 
     showRoller: true,
       rollPeriod: 5,
       }
  );
}