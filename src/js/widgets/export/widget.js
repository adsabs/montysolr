
/*
* for future refrence: a sample request to classic endpoint (handled by proxy api)
* curl -X POST  -H "User-Agent: ADS Script Request Agent" -d 'bibcode=2015IJMPB..2930001X&data_type=BIBTEX&sort=NONE'  http://adsabs.harvard.edu/cgi-bin/nph-abs_connect
* */

define([
    'marionette',
    'js/widgets/base/base_widget',
    'js/components/api_query',
    'hbs!./templates/export_template',
    "hbs!../network_vis/templates/loading-template",
    "hbs!./templates/classic_submit_form",
    'js/components/api_feedback',
    'jquery',
    'jquery-ui',
    'module',
    'js/components/api_targets',
    "zeroclipboard",
     "filesaver"
  ],
  function(
    Marionette,
    BaseWidget,
    ApiQuery,
    WidgetTemplate,
    LoadingTemplate,
    ClassicFormTemplate,
    ApiFeedback,
    $,
    $ui,
    WidgetConfig,
    ApiTargets,
    ZeroClipboard,
    FileSaver
    ){

    //modified from userChangeRows mixin
    var ExportModel = Backbone.Model.extend({

      initialize : function(){
        this.on("change:numFound", this.updateMax);
        this.on("change:max", this.updateCurrent);
      },

      updateMax : function() {
        var m = _.min([this.get("defaultMax"), this.get("numFound")]);
        this.set("max", m);
      },

      defaults : function(){
        return {
          format: 'bibtex',
          //the actual data
          export: undefined,
          //current system wide query
          query: undefined,
          //something the user needs to know about
          msg: undefined,
          identifiers: [],

          defaultMax: 500,
          //we will originally ask for 200 records
          default: 200,
          // returned by api_feedback after starting_search_cycle
          numFound : undefined,
          // total currently being shown
          current : undefined,
          // the smaller of either numFound or maxAllowed
          max : undefined,
          //records that user has requested
          userVal: undefined
        }
      },

      reset : function(){
        this.clear({silent : true}).set(this.defaults(), {silent : true});
        this.trigger("change:export");
      }

    });


    var ExportView = Marionette.ItemView.extend({

      className : "s-export",

      ui :  {
        "triggerCopy" : ".btn-clipboard",
        "triggerDownload" : ".btn-download"
      },

      template: WidgetTemplate,
      loadingTemplate : LoadingTemplate,

      events: {
        "click @ui.triggerDownload" : "downloadRecords",
        "click .close-widget": "signalCloseWidget",
        "click .submit-rows" : "setUserVal"
      },

      modelEvents: {
        "change:export": 'render',
        'change:error' : 'render'
      },

      setUserVal: function() {
        this.model.set('userVal', _.min([parseInt($(".user-input").val()), this.model.get("max")]));
      },

      contentTypes : {
        endnote : "application/x-endnote-refer",
        bibtex : "application/x-bibtex",
        aastex : "text/plain"
      },

      downloadRecords : function(){
        var that = this, type, blob;

        type = this.contentTypes[this.model.get("format")];
        blob = new Blob([this.model.get("export")], {type: type });
        saveAs(blob, this.model.get("format") + "-records.txt");
        this.ui.triggerDownload.html('<i class="fa fa-lg fa-download"></i> Downloaded!');

        setTimeout(function(){
          that.ui.triggerDownload.html('<i class="fa fa-lg fa-download"></i> Download file');
        }, 1000);
      },

      exportRecords: function(ev) {
        if (ev)
          ev.preventDefault()
        this.trigger('export-records');
      },

      signalCloseWidget: function(ev) {
        this.trigger('close-widget');
      },

      onRender : function(){

        //set up copy/paste functionality here
        var that = this;
        var client = new ZeroClipboard(that.ui.triggerCopy);

        client.on("copy", function (event) {
          var clipboard = event.clipboardData;
          clipboard.setData( "text/plain", that.model.get("export"));
        });

        client.on("aftercopy", function(event){
          that.ui.triggerCopy.html('<i class="fa fa-lg fa-clipboard"></i> Copied!');
          setTimeout(function(){
            that.ui.triggerCopy.html('<i class="fa fa-lg fa-clipboard"></i> Copy to clipboard')
          }, 1000);

        });
      }

    });


    var ExportWidget = BaseWidget.extend({

      initialize: function(options){
        this.model = new ExportModel();
        this.view = new ExportView({model : this.model});
        BaseWidget.prototype.initialize.apply(this, arguments);
      },

      activate: function (beehive) {
        _.bindAll(this, "processResponse");
        this.pubsub = beehive.Services.get('PubSub');
      },

      viewEvents: {
        'export-records': 'exportRecords',
        'export-query': 'exportQuery',
        'close-widget': 'closeWidget'
      },

      modelEvents : {
        "change:userVal" : "changeRows"
      },

      //query information already exists, user just wants different rows
      changeRows : function(){
        //remove current model's "export" param to indicate we are awaiting data
        this.model.set("export", undefined);

        var q = this.model.get("query");
        q = q.clone();
        q.unlock();
        q.set('rows', this.model.get("userVal"));
        q.set('fl', 'bibcode');
        this.initiateQueryBasedRequest(q)
      },

      /**
       * Export data directly - use the supplied query (must be supplied)
       * @param info (object with apiQuery, numFound, and format params)

       */
      exportQuery: function(info) {

        this.model.reset();

        //navigator hands off these values, they originally come from app storage
        this.model.set({
        'query': info.currentQuery,
        'format' : info.format,
        'numFound': info.numFound
        });

        if (this.model.get("numFound") == -1) {
          self.model.set('error', 'Unknown number of results');
        }

        //now initiating request to export api endpoint
        var q = this.model.get("query").clone();
        q.unlock();
        q.set('rows', this.model.get("default"));
        q.set('fl', 'bibcode');

        this.initiateQueryBasedRequest(q);
    },

      /*
      * takes an apiQuery, gets bibcodes, then requests them from
      * the export endpoint
      */

      initiateQueryBasedRequest : function(apiQuery){
        var self = this;

        // collect bibcodes of the query
        this._executeApiRequest(apiQuery)
          .done(function(apiResponse) {
            // export documents by their ids
            var ids = _.map(apiResponse.get('response.docs'), function(d) {return d.bibcode});
            //tell model how many bibcodes we're planning on showing
            self.model.set("current", ids.length);
            //this will get the exports and register a callback to put them in the model
            self._getExports(self.model.get('format') || 'bibtex', ids)
              });
      },

      exportRecords: function(format, recs) {
        if (!_.isArray(recs)) throw new Error('Identifiers must be an array');
        if (recs.length <= 0) console.warn('Do you want to export nothing? Let me be!');
        this.model.set('current', recs.length);
        this.model.set('format', format);
        this.model.set('identifiers');
        this._getExports(format, recs);
      },

      //special case, will eventually be removed
      openClassicExports : function(options){
        if (options.bibcodes){
           var $form =  $(ClassicFormTemplate({bibcodes: options.bibcodes}));
          $form.submit();
        }
        else if (options.currentQuery ) {
          var q = options.currentQuery.clone();
          q.set("rows", this.model.get("defaultMax"));
          q.set("fl", "bibcode");
          this._executeApiRequest(q)
            .done(function(apiResponse) {
              // export documents by their ids
              var ids = _.map(apiResponse.get('response.docs'), function(d) {return d.bibcode});
              var $form =  $(ClassicFormTemplate({ bibcodes: ids }));
              $form.submit();
            });
        }
        else {
          throw new Error("can't export with no bibcodes or query");
        }
      },

      /**
       * Fetches data from the export api and saves into the model
       *
       * @param identifiers
       * @param format
       * @returns {*}
       * @private
       */
      _getExports : function(format, identifiers){

        format = format || this.model.get('format') || 'bibtex';
        //export endpoints
        var tbl = {
          endnote: 'endnote',
          aastex: 'aastex',
          bibtex: 'bibtex'
        };
        format = tbl[format.toLowerCase()] || format.toLowerCase();

        if (!_.isArray(identifiers)) throw new Error('Identifiers must be an array');
        if (identifiers.length <= 0) throw new Error('Do you want to export nothing? Let me be!');

        var q = new ApiQuery();
        //export parameter is "bibcode"
        q.set('bibcode', identifiers);
        q.set('sort', 'NONE');

        var req = this.composeRequest(q);
        req.set("target",  ApiTargets.EXPORT + format);

        //use post, although get is also possible
        var reqOptions = {
          type: 'POST'
        };

        req.set('options', reqOptions);

        if (req) {
          this.pubsub.subscribeOnce(this.pubsub.DELIVERING_RESPONSE, _.bind(function(exports) {
            if (exports && exports.has('export')) {
              this.model.set('export', exports.get('export'));
            }
            else {
              console.error('The export did return some garbage, tfuj!', exports);
            }
          }, this));

          this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, req);
        }
        else {
          throw new Error('Well, this is unexpected behaviour! Who wrote this software?');
        }
        this.model.set('identifiers', identifiers);
      },

      _executeApiRequest: function(apiQuery){
        if (!apiQuery) throw new Error('Damn, and I thought you knew what you do!');

        var req = this.composeRequest(apiQuery);
        var defer = $.Deferred();
        if (req) {
          this.pubsub.subscribeOnce(this.pubsub.DELIVERING_RESPONSE, _.bind(function(data) {
            defer.resolve(data);
          }), this);
          this.pubsub.publish(this.pubsub.EXECUTE_REQUEST, req);
        }
        else {
          throw new Error('Well, this is unexpected behaviour! Who wrote this software?');
        }
        return defer;
      },

      closeWidget: function () {
        this.pubsub.publish(this.pubsub.NAVIGATE, "results-page");
      }
    });
    return ExportWidget;
  });