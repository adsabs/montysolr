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


    var xmlXX = function(json, opts) {
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

        json.forEach(function(node){
          result += xml(node, opts);
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
              result += "<{0}{1}>{2}</{3}>".format(key, attributes, xml(node,opts), key);
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

      var result = opts.header ? ['<?xml version="1.0" encoding="UTF-8"?>'] : [];

      opts.header = false;
      var type = json.constructor.name;

      if (type === 'Array') {

        result = [];
        json.forEach(function(node){
          var item = _toArrayOfXmls(node, opts);
          result.push(item);
        });
      }
      else if (type === 'Object' && typeof json === "object") {

        Object.keys(json).forEach(function(key) {
          if(key !== opts.attributes_key) {
            var node = json[key];
            var attributes = _getAttributes(node, opts);

            var inner = _toArrayOfXmls(node,opts);

            if (inner){
              if (key == "_") {
                result.push(node);
              }
              else {
                var next = _toArrayOfXmls(node, opts);

                if (node.constructor.name !== "Array") {
                  next = [next];
                  node = [node];
                }

                for (var i = 0; i < node.length; i++) {
                  var item = next[i];
                  result.push(["<",
                    key,
                    _getAttributes(node[i], opts),
                    ">",
                    item,
                    "</",
                    key,
                    ">"]);
                }
              }
            }
            else {
              result.push(
                ["<",
                  key,
                  attributes,
                  "/>"]);
            }
          }
        });
      }
      else {
        result.push(json.toString().replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;'));
      }

      return result;
    };

    var _getAttributes = function(node, opts) {
      var attributes = [];

      if(opts.attributes_key && node[opts.attributes_key]){
        Object.keys(node[opts.attributes_key]).forEach(function(k){
          attributes.push([' ',
            k,
            '="',
            node[opts.attributes_key][k],
            '"'])
        });
      }

      return attributes;
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



