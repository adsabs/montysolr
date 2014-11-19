define([], function(){


  var f = {}

  /*
  * takes a number or string, returns a string
  * */

  f.formatNum = function(num){
    var withCommas = [];
    num = num+"";
    if (num.length < 4){
      return num
    }
    else {
      num  = num.split("").reverse();
      _.each(num, function(n, i){
        withCommas.unshift(n)
        if ((i+1) % 3 === 0 && i !== num.length -1){
          withCommas.unshift(",")
        }
      })
    }
    return withCommas.join("");
  }



  return f
})