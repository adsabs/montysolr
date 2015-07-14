define(['marionette',
    'backbone',
    'underscore',
    'js/components/api_request',
    'js/components/api_query',
    'js/widgets/base/base_widget',
    'hbs!./query_info_template',
    'hbs!./feedback-template',
    'hbs!./library-options',
    'js/mixins/formatter',
    'bootstrap',
    'js/components/api_feedback'
  ],

  function(Marionette,
           Backbone,
           _,
           ApiRequest,
           ApiQuery,
           BaseWidget,
           queryInfoTemplate,
           FeedbackTemplate,
           LibraryOptionsTemplate,
           FormatMixin,
           Bootstrap,
           ApiFeedback
    ) {


    var QueryModel = Backbone.Model.extend({

      defaults: {
        selected: 0,
        //for libraries
        libraryDrawerOpen : false,
        //for rendering library select
        libraries : [],
        loggedIn : false
      }
    });

    var QueryDisplayView = Marionette.ItemView.extend({

   	  className : "query-info-widget s-query-info-widget",
      template: queryInfoTemplate,

      serializeData : function(){
        var data = this.model.toJSON();
        data.selected = this.formatNum(data.selected);
        return data;
      },

      modelEvents : {
        "change:selected" : "render",
        "change:loggedIn" : "render",
        "change:libraries" : "renderLibraries"
      },

      triggers : {
        "click .clear-selected" : "clear-selected",
        "click .page-bulk-add" : "page-bulk-add"
      },

      events : {

        "click .library-add-title" : "toggleLibraryDrawer",
        "click .submit-add-to-library" : "libraryAdd",
        "click .submit-create-library" : "libraryCreate"
      },

//      libraryAdd : function(){
//        var data = {};
//
//        data.libraryID = this.$("#library-select").val();
//
//        if (this.model.get("selected")){
//          data.recordsToAdd = this.$("#all-vs-selected").val();
//        } else {
//          data.recordsToAdd = "all";
//        }
//        this.trigger("library-add", data);
//      },
//
//      libraryCreate : function(){
//        var data = {};
//
//        if (this.model.get("selected")){
//          data.recordsToAdd = this.$("#all-vs-selected").val();
//        } else {
//          data.recordsToAdd = "all";
//        }
//
//        data.name = $("input[name='new-library-name']").val().trim();
//
//        this.trigger("library-create", data);
//      },

      toggleLibraryDrawer : function(){
        this.model.set("libraryDrawerOpen", !this.model.get("libraryDrawerOpen"), {silent : true});
      },

      onRender : function(){
        this.$(".icon-help").popover({trigger: "hover", placement: "right", html: true});
        this.renderLibraries();
      },

      renderLibraries : function(){
        this.$(".libraries-container").html(LibraryOptionsTemplate(this.model.toJSON()));
      }

    });

    _.extend(QueryDisplayView.prototype, FormatMixin);


    var Widget = BaseWidget.extend({

      initialize: function(options) {
        this.model = new QueryModel();
        this.view = new QueryDisplayView({model : this.model});
        BaseWidget.prototype.initialize.call(this, options)
      },

      viewEvents : {
        "clear-selected" : "clearSelected",
        "page-bulk-add" : "triggerBulkAdd",
        "library-add" : "libraryAddSubmit",
        "library-create" : "libraryCreateSubmit"
      },

      activate: function(beehive) {
        this.beehive = beehive;
        _.bindAll(this);
        this.pubsub = beehive.getService('PubSub');
        var pubsub = this.pubsub;

        pubsub.subscribe(pubsub.STORAGE_PAPER_UPDATE, this.onStoragePaperChange);
        pubsub.subscribe(pubsub.LIBRARY_CHANGE, this.processLibraryInfo);
        pubsub.subscribe(pubsub.USER_ANNOUNCEMENT, this.handleUserAnnouncement);

      },

      handleUserAnnouncement : function(event, arg){
        if (event == "user_signed_in"){
          this.model.set("loggedIn", true);
        }
        else if (event == "user_signed_out"){
          this.model.set("loggedIn", false);
        }
      },

      onStoragePaperChange : function(numSelected){
       this.model.set("selected", numSelected);
      },

      processLibraryInfo : function(listOfLibraries){
       this.model.set("libraries", listOfLibraries);
     },

      clearSelected : function(){
        this.beehive.getObject("AppStorage").clearSelectedPapers();
      },

      triggerBulkAdd : function(){
        this.pubsub.publish(this.pubsub.CUSTOM_EVENT, "add-all-on-page");
      },

      libraryAddSubmit : function(data){
        var options = {};
        options.library = data.libraryID;
        //are we adding the current query or just the selected bibcodes?
        options.bibcodes = data.recordsToAdd;

        var name = _.findWhere(this.model.get("libraries"), {id : data.libraryID }).name;

        //this returns a promise
        this.beehive.getObject("LibraryController").addBibcodesToLib(options)
          .done(function(response, status){
            if (status == "error"){
              this.$(".feedback").html(FeedbackTemplate({error : true, name : name, id : data.libraryID }))
            }
            else if (status == "success"){
              this.$(".feedback").html(FeedbackTemplate({
                success : true,
                name : name,
                id : response.id,
                numRecords: response.number_added
              }))

            }
          })
          .fail();

      },

      libraryCreateSubmit : function(data){
        var that = this, options = {};
        //are we adding the current query or just the selected bibcodes?
        options.bibcodes = data.recordsToAdd;
        options.name = data.name;
        this.beehive.getObject("LibraryController").createLibAndAddBibcodes(options)
          .done(function(response, status){
            if (status == "error"){
              this.$(".feedback").html(FeedbackTemplate({ error : true, name : data.name, create : true }));
            }
            else if (status == "success"){
              this.$(".feedback").html(FeedbackTemplate({
                  create: true,
                  success : true,
                  name : data.name,
                  id : response.id,
                  numRecords : response.bibcode.length
            }));
            }
          })
          .fail();

      }

    });

    return Widget

  });