define([
  "js/widgets/base/base_widget",
  "js/components/api_query",
   "./topterms",
  "hbs!./templates/form_container",
  "hbs!./templates/author_form",
  "hbs!./templates/paper_form",
  "hbs!./templates/topic_form",
  "bootstrap",
  "jquery-ui"
], function (BaseWidget,
             ApiQuery,
             AutocompleteData,
             ContainerTemplate,
             AuthorTemplate,
             PaperTemplate,
             TopicTemplate,
             Bootstrap,
             jQueryUI
  ) {

  var ContainerView, AuthorView, PaperView, TopicView, FormWidget;

  AuthorView = Marionette.ItemView.extend({

    template: AuthorTemplate,

    events: {
      "click button.add-author": "addAuthor",
      "click input.first-author" : "limitOneFirst",
      "click .clear" : function(e){
        e.preventDefault();
        this.render();
      },
      "click button[type=submit]" : "submitForm",
      "keyup" : "checkDisabled"
    },

    limitOneFirst : function(e){
      var checked = e.target.checked;
      this.$("input.first-author").prop("checked", false).each(function(){
        var $p = $(this).prev();
        $p.val($p.val().replace(/^\^(.*)/, '$1'));
      });
      if (checked){
        var $input = $(e.target).prev();
        $input.val( "^" + $input.val());
      }
      $(e.target).prop("checked", checked);
    },

    addAuthor: function(e){
      var inputGroup = this.$(".author-entry-template").find(".author-entry").clone();
      this.$(".add-author").before(inputGroup);
      $(inputGroup).find(".author-input").focus();
      e.preventDefault();
    },

    checkDisabled : function(e){

      //require at least 1 character to be in at least 1 input field
      var authors = this.$(".author-input:visible").map(function(){
        return $(this).val();
      }).get();

      if (authors.join("").match(/[A-Za-z]+/)){
        this.$("button[type=submit]").prop("disabled", false);
      }
      else {
        this.$("button[type=submit]").prop("disabled", true);
      }
    },

    submitForm : function(e){

      var authors = this.$(".author-input:visible").map(function(){
              var $t = $(this),
              name = $t.val(),
              first = $t.next().prop("checked");

        if (first){
          //ensure that a caret is there, even if user has removed it for some reason
          var name = name.replace(/^([^\^].*)/, "^$1");
          return '"'+ name + '"';
        }
        else if (name) {
          return "\"" + name + "\"";
        }
        else {
          return "";
        }

        return  (first ? ("^\"" + name + "\"") : "\"" + name + "\"");

      });

      if (authors.get().length == 1){
        var queryString = "author:"+ authors.get()[0];
      }
      else {
        var queryString = "author:(" + authors.get().join(" ") + ")";
      }

      this.trigger("submit", queryString);
      e.preventDefault();
    }


  });

  PaperView = Marionette.ItemView.extend({

    template: PaperTemplate,

    events: {
      "click .clear" : function(e){
        e.preventDefault();
        this.render();
      },
      "keyup input" : "checkDisabled",
      "click button[type=submit]" : "submitForm",
      "click button.parse" : "parseReference"
    },

    onRender : function(e){
      this.$("#pub-input").autocomplete({ source : AutocompleteData, minLength : 1 , autoFocus : true });
    },

    checkDisabled : function(){
      //require at least 1 character to be in at least 1 input field
      var fields= this.$("input:not(.parse-reference)").map(function(){
        return $(this).val();
      }).get();

      if (fields.join("").match(/\w+/)){
        this.$("button[type=submit]").prop("disabled", false);
      }
      else {
        this.$("button[type=submit]").prop("disabled", true);
      }
    },

    parseReference : function(){
     var str = $("input.parse-reference").val();
     if (str){
       try {
         var match = str.match(/^.+\s*(\d{4}),\s*(\w{2,5}),\s*(\d{1,5}),\s*(\d{1,4})\s*$/);

         this.$("#year-input").val(match[1]);
         this.$("#pub-input").val(match[2]);
         this.$("#volume-input").val(match[3]);
         this.$("#page-input").val(match[4]);

         this.checkDisabled();

         var btnClass = "btn-success";

       } catch (e){
         console.log("couldn't parse");
         var btnClass = "btn-danger";

       }

       this.$("button.parse").removeClass("btn-info").addClass(btnClass);

       setTimeout(function(){
         this.$("button.parse").removeClass(btnClass).addClass("btn-info");
       }, 1000);

     }

    },

    submitForm : function(e){

     var terms = this.$("input:not(.parse-reference)").map(function(){
       var $t = $(this);
       $t.val() ? toReturn = $t.data("term") + ":" + $t.val() : toReturn =  undefined;
       return toReturn;
     }).get();

      terms = _.filter(terms, function(t){if (t){return t}});

      this.trigger("submit", terms.join(" "));
      e.preventDefault();
    }

  });

  TopicView = Marionette.ItemView.extend({

    template: TopicTemplate,

    events : {
      "click button[type=submit]" : "submitForm",
      "keyup input" : "checkDisabled"
    },

    checkDisabled : function(e){

     if (this.$("input").val().match(/\w+/)){
       this.$("button[type=submit]").prop("disabled", false);

     }
      else {
       this.$("button[type=submit]").prop("disabled", true);
     }

    },

    submitForm : function(e){

      var val = this.$("#topic-search").val();

      var queryString = "abstract:(" + val + ") OR title:(" + val + ")";
      this.trigger("submit", queryString);

      e.preventDefault();
    }

  });

  ContainerView = Marionette.LayoutView.extend({

    assemble: function () {

      var authorView = new AuthorView(),
          paperView = new PaperView(),
          topicView = new TopicView();

      this.listenTo(authorView, "submit", this.forwardSubmit);
      this.listenTo(paperView, "submit", this.forwardSubmit);
      this.listenTo(topicView, "submit", this.forwardSubmit);

      this.showChildView('author-form', authorView);
      this.showChildView('paper-form', paperView);
      this.showChildView('topic-form', topicView);

    },

    className: "form-accordion-widget s-form-accordion-widget",

    template: ContainerTemplate,

    regions: {
      "paper-form": ".paper-form",
      "author-form": ".author-form",
      "topic-form": ".topic-form"
    },

    forwardSubmit : function(queryString){
      this.trigger("submit", queryString);
    },

    events : {
      "click .panel-heading" : "focusForm"
    },

    focusForm : function(e){

      this.$("#accordion").one("shown.bs.collapse", function(){
        $(e.currentTarget).next().find("input:first").focus();
      });
    }

  });

  FormWidget = BaseWidget.extend({

    initialize: function (options) {

      options = options || {};
      this.view = new ContainerView();
      this.listenTo(this.view, "submit", this.submitForm);

    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      this.pubsub = beehive.Services.get('PubSub');
    },

    onShow: function () {
      this.view.assemble();
      //close accordion in case it's open
      this.view.$(".panel-collapse").removeClass("in");
      this.view.$(".panel-heading").addClass("collapsed");
    },

    submitForm : function(query){

      var newQuery = new ApiQuery({
        q: query
      });

      this.pubsub.publish(this.pubsub.START_SEARCH, newQuery);

    }

  });

  return FormWidget;

});