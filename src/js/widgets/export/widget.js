

define([
    'js/widgets/base/base_widget',
    'js/components/api_query',
    'hbs!./templates/export-button',
    'hbs!./templates/export-menu'],
  function(
    BaseWidget,
    ApiQuery,
    ButtonTemplate,
    MenuTemplate){


    var ExportModel = Backbone.Model.extend({

      defaults : {
        //always initially starts at 0
        start : 0,
        //might initially start at numFound rather than default 300
        end: undefined
      }


    });


    var ExportView = Backbone.View.extend({

      tagName : "span",

      events : {
        "click .export-destination" : "triggerExport",
        "click .dropdown-menu": "preventClose",
        "click a": "closeDropdown",
        "change input" : "updateModel"
      },


      buttonTemplate : ButtonTemplate,

      menuTemplate : MenuTemplate,

      render : function(){

        this.$el.html(ButtonTemplate());

        this.renderMenu();

      },

      updateModel : function(e){

        var $t = $(e.target);
        var v =  $t.val();

        //user input is not zero indexed, but the model is


        if ($t.hasClass("export-start-val")){
          this.model.set("start", _.max([v - 1, 0]))
        }
        else if ($t.hasClass("export-end-val")){
          this.model.set("end", _.min([v, this.model.get("currentMax")]))
        }

        this.renderMenu();

      },

      preventClose : function(e){
        e.stopPropagation()
      },

      closeDropdown : function(){

        this.$(".btn-group").removeClass("open");

      },

      renderMenu : function(){

        var m = this.model.toJSON();

        var data = {};

        //display is not zero indexed, but the model is

        data.startVal = m.start + 1;

        data.endVal = m.end;

        this.$el.find(".dropdown-menu").empty().append(MenuTemplate(data))


      },

      triggerExport : function(e){

        var pathName = $(e.target).data("path");
        this.trigger("export", pathName)

      }

    });


    var ExportWidget = BaseWidget.extend({

      initialize : function(options){

        this.model = new ExportModel();
        this.view = new ExportView({model : this.model})
        this.listenTo(this.view, "export", this.requestExportData)

        this.defaultMax = options.defaultMax || 300;

        BaseWidget.prototype.initialize.apply(this, arguments);

      },

      activate: function (beehive) {

        _.bindAll(this, "getQueryInfo", "processResponse");
        this.pubsub = beehive.Services.get('PubSub');

        this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.getQueryInfo);
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },

      paths : {

        ADSClassic : "http://adsabs.harvard.edu/cgi-bin/nph-abs_connect"

      },

      getQueryInfo : function(apiQuery){

        this.setCurrentQuery(apiQuery);

        this.model.set("currentMax", this.defaultMax);

        var  q = apiQuery.clone();
        q.unlock();

        q.unset("fl");
        q.unset("facet");

        //XXX: use ApiQueryModifier class for this
        q.set("__forNumFound", "true");

        if (q) {
          var req = this.composeRequest(q);
          if (req) {
            this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
          }
        }


      },

      requestExportData : function(pathName){

        var path = this.paths[pathName];

        if (pathName === "ADSClassic"){

          this.callBack = function(params){
            this.post(path, params)
          }

        }

        var currentQuery = this.getCurrentQuery();

        var  q = currentQuery.clone();
        q.unlock();

        q.set("fl", "bibcode");
        q.unset("facet");


        var start = this.model.get("start");
        q.set("start", start);
        q.set("rows", this.model.get("end") - start);

        if (q) {
          var req = this.composeRequest(q);
          if (req) {
            this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, req);
          }
        }
      },


      processResponse : function(apiResponse){

        if (apiResponse.has('responseHeader.params.__forNumFound')){

          this.numFound = apiResponse.get("response.numFound");

          if (this.numFound < this.defaultMax){
            this.model.set("end", this.numFound);
            this.model.set("currentMax", this.numFound)
          }
          else {
            this.model.set("end", this.defaultMax)
          }

          this.view.render();

          return

        }

        var docs = apiResponse.get("response.docs");

        this.callBack(docs);

      },


      post : function (path, params, method) {
        method = method || "post"; // Set method to post by default if not specified.

        var $f = $("<form/>", {method : method, action : path, "target": "_blank"});

        if ($.isArray(params)){
          _.each(params, function(l,i){
            l = _.pairs(l)[0];
            var hiddenField = $("<input>", {type : "hidden", "name": l[0], value: l[1]});
            $f.append(hiddenField);
          })
        }
        $f.submit();
      }

    });


    return ExportWidget;
  });