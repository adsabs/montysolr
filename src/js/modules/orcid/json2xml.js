define([
    'underscore',
    'bootstrap',
    'js/components/generic_module',
    'js/mixins/dependon'
  ],
  function(_, Bootstrap, GenericModule, Mixins) {

    var settings = {
      attributes_key:false,
      header:false
    };

    var xml = function(json, opts) {
      return _.flatten(_toArrayOfXmls(json, opts)).join("");
    };

    var _toArrayOfXmls = function(json, opts) {
      if(opts){
        Object.keys(settings).forEach(function(k){
          if(typeof opts[k]==='undefined'){
            opts[k] = settings[k];
          }
        });
      } else {
        opts = settings;
      }

      var result = [];
      result.push(opts.header ? '<?xml version="1.0" encoding="UTF-8"?>' : '');

      opts.header = false;
      type = json.constructor.name;

      if(type==='Array'){

        result = [];
        json.forEach(function(node){
          result.push(_toArrayOfXmls(node, opts));
        });
      } else if(type ==='Object' && typeof json === "object") {

        Object.keys(json).forEach(function(key){
          if(key!==opts.attributes_key){
            var node = json[key],
              attributes = [];

            if(opts.attributes_key && node[opts.attributes_key]){
              Object.keys(node[opts.attributes_key]).forEach(function(k){
                attributes.push([' ',
                                k,
                                '="',
                                node[opts.attributes_key][k],
                                '"'])
              });
            }
            var inner = _toArrayOfXmls(node,opts);

            if(inner){
              if (key == "_") {
                result.push(node);
              }
              else {
                var next = _toArrayOfXmls(node, opts);

                next = node.constructor.name === "Array" ? next : [next];

                next.forEach(function(item) {
                 result.push(["<",
                              key,
                              attributes,
                              ">",
                              item,
                              "</",
                              key,
                              ">"]);
                });
              }
            } else {
              result.push(
                ["<",
                  key,
                  attributes,
                  "/>"]);
            }
          }
        });
      } else {
        result.push(json.toString().replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;'));
      }

      return result;
    };

    var getHardenedInstance = function () {
      return this;
    };

    var Json2Xml = GenericModule.extend({
      xml: xml,
      getHardenedInstance: getHardenedInstance
    });

    _.extend(Json2Xml.prototype, Mixins.BeeHive);

    return Json2Xml;
  });



