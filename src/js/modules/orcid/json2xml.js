define([
    'underscore',
    'bootstrap',
    'js/components/generic_module',
    'js/mixins/dependon'
  ],
  function(_, Bootstrap, GenericModule, Mixins) {

    // BC:rca - I don't want global exports (unless it is required by json2xml - and i'd be very suspicious of
    // that library, if it was); please move to a standalone function
    // TODO: move this to some commonUtils.js
    String.prototype.format = function () {
      var args = arguments;
      return this.replace(/\{\{|\}\}|\{(\d+)\}/g, function (m, n) {
        if (m == "{{") {
          return "{";
        }
        if (m == "}}") {
          return "}";
        }
        return args[n];
      });
    };

    var settings = {
      attributes_key:false,
      header:false
    };

    var xml = function(json, opts) {
      if(opts){
        Object.keys(settings).forEach(function(k){
          if(typeof opts[k]==='undefined'){
            opts[k] = settings[k];
          }
        });
      } else {
        opts = settings;
      }

      var result = opts.header?'<?xml version="1.0" encoding="UTF-8"?>':'';
      opts.header = false;
      type = json.constructor.name;

      if(type==='Array'){

        result = [];
        json.forEach(function(node){
          result.push(xml(node, opts));
        });

      } else if(type ==='Object' && typeof json === "object") {

        Object.keys(json).forEach(function(key){
          if(key!==opts.attributes_key){
            var node = json[key],
              attributes = '';

            if(opts.attributes_key && node[opts.attributes_key]){
              Object.keys(node[opts.attributes_key]).forEach(function(k){
                attributes += ' {0}="{1}"'.format(k, node[opts.attributes_key][k]);
              });
            }
            var inner = xml(node,opts);

            if(inner){
              if (key == "_") {
                result += node;
              }
              else {
                var next = xml(node, opts);

                next = Array.isArray(next) ? next : [next];

                next.forEach(function(item) {
                  result += "<{0}{1}>{2}</{3}>".format(key, attributes, item, key);
                });
              }
            } else {
              result += "<{0}{1}/>".format(key, attributes);
            }
          }
        });
      } else {
        return json.toString().replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
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



