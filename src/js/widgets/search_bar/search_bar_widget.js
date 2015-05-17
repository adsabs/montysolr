define([
    'marionette',
    'js/components/api_query',
    'js/widgets/base/base_widget',
    'hbs!./templates/search_bar_template',
    'hbs!./templates/search_form_template',
    'js/components/query_builder/plugin',
    'js/components/api_feedback',
    './autocomplete',
    'bootstrap', // if bootstrap is missing, jQuery events get propagated
    'analytics',
    'jquery-ui'
  ],
  function (
    Marionette,
    ApiQuery,
    BaseWidget,
    SearchBarTemplate,
    SearchFormTemplate,
    QueryBuilderPlugin,
    ApiFeedback,
    autocompleteArray,
    bootstrap,
    analytics,
    jqueryUI
    ) {


    // following functions are used for the autocomplete

    $.fn.selectRange = function (start, end) {
      if (!end) end = start;
      return this.each(function () {
        if (this.setSelectionRange) {
          this.focus();
          this.setSelectionRange(start, end);
        } else if (this.createTextRange) {
          var range = this.createTextRange();
          range.collapse(true);
          range.moveEnd('character', end);
          range.moveStart('character', start);
          range.select();
        }
      });
    };

    function setInputSelection(input, startPos, endPos) {
      input.focus();
      if (typeof input.selectionStart != "undefined") {
        input.selectionStart = startPos;
        input.selectionEnd = endPos;
      } else if (document.selection && document.selection.createRange) {
        // IE branch
        input.select();
        var range = document.selection.createRange();
        range.collapse(true);
        range.moveEnd("character", endPos);
        range.moveStart("character", startPos);
        range.select();
      }
    }

    function getSelectedText() {
      var text = "";
      if (window.getSelection) {
        text = window.getSelection().toString();
      } else if (document.selection && document.selection.type != "Control") {
        text = document.selection.createRange().text;
      }
      return text;
    }

    //splits the part of the text that the autocomplete cares about
    function findActiveAndInactive(textString){

      var split = _.filter(textString.split(/\s+/), function(x){
        if (x) return true;
      });

      var toReturn =  {active: split[split.length - 1]};

      if (split.length > 1){
        split.pop();
        toReturn.inactive =split.join(" ");
      }
      else {
        toReturn.inactive = "";
      }
      return toReturn;
    };


    var SearchBarView = Marionette.ItemView.extend({

      template: SearchBarTemplate,

      render : function(){
        Marionette.ItemView.prototype.render.apply(this, arguments);
        this.render = function(){ return this}
      },

      className : "s-search-bar-widget",

      initialize: function (options) {
        _.bindAll(this, "tempFieldInsert", "tempFieldClear");
        this.queryBuilder = new QueryBuilderPlugin();
      },

      activate: function(beehive) {
        this.queryBuilder.setQTreeGetter(QueryBuilderPlugin.buildQTreeGetter(beehive));
        var that = this;
        this.queryBuilder.attachHeartBeat(function() {
          that.onBuilderChange();
        });
        this.beehive = beehive;
      },

      onBuilderChange: function() {
        if (this.queryBuilder.isDirty()) {
          var newQuery = this.queryBuilder.getQuery();
          this.setFormVal(newQuery);
        }
      },

      onRender: function () {

        this.$("#search-form-container").append(SearchFormTemplate);
        this.$("#search-gui").append(this.queryBuilder.$el);

        var $input = this.$("input.q");

        $input.autocomplete({
          minLength: 1,
          autoFocus : true,
          //default delay is 300
          delay : 0,
          source:  function( request, response ) {
            var toMatch, matcher, toReturn;

            toMatch = findActiveAndInactive(request.term.trim()).active;
            if (!toMatch)
                return
            //testing each entry's "match" var in autocomplete array against the toMatch segment
            //then returning a uniqued array of matches
            matcher = new RegExp("^" + $.ui.autocomplete.escapeRegex(toMatch), "i");
            toReturn  = $.grep(autocompleteArray, function (item) {
                return matcher.test(item.match);
              });
            toReturn = _.uniq(toReturn, false, function(item){
                return item.label
              });
            response(toReturn);
          },

          /* insert a suggestion: requires autofocus:true
           * to be set if you want to show by default without user
           * keyboard navigation or mouse hovering
           * */
          focus: function( event, ui ) {

            var val = $input.val().replace(/^\s+/,"");
              suggest = ui.item.value;

            var exists, toMatch, confirmedQuery, splitQuery;

            var currentlySelected = getSelectedText();
            //might be moving down the autocomplete list
            if (currentlySelected){
              exists = val.slice(0, val.length - currentlySelected.length)
            }
            else {
              exists = val;
            }

            splitQuery = findActiveAndInactive(exists);

            toMatch = splitQuery.active,
            confirmedQuery = splitQuery.inactive;

            if (confirmedQuery){
              //suggestedQ will be inserted if user accepts it
              $input.data("ui-autocomplete").suggestedQ = confirmedQuery + " " + ui.item.value + ui.item.end;
            }
            else {
              $input.data("ui-autocomplete").suggestedQ = ui.item.value + ui.item.end;
            }

            // only insert text if the words match from the beginning
            // not, for instance, if user typed "refereed" and the matching string is "property:refereed"
            if ( suggest.indexOf(toMatch) == 0 ) {

              var text, rest, all;

              text = confirmedQuery ? confirmedQuery + " " + toMatch : toMatch;
              rest = suggest.slice(toMatch.length);
              all = text + rest;

              $input.val(all);
              setInputSelection($input[0], text.length, all.length);

            }
            else {
             $input.val(exists);
            }

            return false;
          },

          //re-insert actual text w/ optional addition of autocompleted stuff
          select : function( event, ui ){
            $input.val( $input.data("ui-autocomplete").suggestedQ);
            //move cursor before final " or )
            if (ui.item.end){
              $input.selectRange($input.val().length - 1);
            }
            return false;
          }

        }).data("ui-autocomplete")._renderItem = function( ul, item ) {
          if (item.desc){
            return $( "<li>" )
              .append( "<a>" + item.label + "<span class=\"s-auto-description\">&nbsp;&nbsp;" + item.desc + "</span></a>" )
              .appendTo( ul );
          }
          else {
            return $( "<li>" )
              .append( "<a>" + item.label + "</a>" )
              .appendTo( ul );
          }
        };
       },

      events: {
        "click #field-options button" : "tempFieldInsert",
        "keypress .q": function(e){
          this.highlightFields(e);
          this.setAddField();
        },
        "blur .q": "unHighlightFields",
        "click #search-form-container": function (e) {
          e.stopPropagation();
        },
        "click #search-form-container .title": "toggleFormSection",
        "click .show-form": "onShowForm",
        "submit form[name=main-query]": "submitQuery",
        "keyup .q" : "toggleClear",
        "click .icon-clear" : "clearInput"
      },

      toggleClear : function(){
        var display = Boolean(this.$(".q").val());
        if (display){
          this.$(".icon-clear").removeClass("hidden");
        }
        else {
          this.$(".icon-clear").addClass("hidden");
        }
      },

      clearInput : function(e){
        this.$(".q").val("");
        this.$(".icon-clear").addClass("hidden");
      },

      getFormVal: function() {
        return this.$(".q").val();
      },

      setFormVal: function(v) {
        return this.$(".q").val(v);
      },

      onShowForm: function() {

        analytics('send', 'event', 'interaction', 'click', 'query-assistant');

        var formVal = this.getFormVal();
        if (formVal.trim().length > 0) {
          this.queryBuilder.updateQueryBuilder(formVal);
        }
        else { // display a default form
          this.queryBuilder.setRules({
            "condition": "AND",
            "rules": [
              {
                "id": "author",
                "field": "author",
                "type": "string",
                "input": "text",
                "operator": "is",
                "value": ""
              }
            ]
          });
        }

        // show the form
        this.specifyFormWidth();
      },

      specifyFormWidth: function () {
        this.$("#search-form-container").width(this.$(".input-group").width());
      },

      toggleFormSection: function (e) {
        var $p = $(e.target).parent();
        $p.next().toggleClass("hide");
        $p.toggleClass("search-form-header-active");
      },

      highlightFields: function () {
        this.$(".show-form").addClass("draw-attention")
      },

      unHighlightFields: function () {
        this.$(".show-form").removeClass("draw-attention")
      },

      tempFieldInsert: function (e) {
        e.preventDefault();
        var currentVal, newVal, df, punc, $target;

        currentVal = this.getFormVal();
        this.priorVal = currentVal;
        $target = $(e.target);

        df = $target.attr("data-field");
        punc = $target.attr("data-punc");

        if (punc){
          //checking if first author
          if (df == "first-author") {
            newVal = currentVal + " author:\"^\"";
          } else if (punc == "\"") {
            newVal = currentVal + " " + df + ":\"\"";
          }
          else if (punc == "("){
            newVal = currentVal + " " + df + ":()";
          }
          this.setFormVal(newVal);
          this.$(".q").focus();
          this.$(".q").selectRange(this.$(".q").val().length - 1);
        }

        else {
          //only 'year'
          newVal = currentVal + " " + df + ":";
          this.setFormVal(newVal);
          this.$(".q").focus();
        }
        //figure out if clear button needs to be there
        this.toggleClear();
      },

      tempFieldClear: function (e) {
        if (this.addField === true) {
          this.$(".q").focus();
          this.$(".q").selectRange(this.$(".q").val().length - 1);
          this.addField = false;
        }
        else {
          //assumption that final entry in query bar needs to be cleared
          this.$(".q").val(this.priorVal);
          this.priorVal = undefined;
        }
      },

      setAddField: function (e) {
        this.addField = true;
      },

      unsetAddField : function (e) {
        this.addField = false;
      },

      submitQuery: function(e) {
        e.preventDefault();
        e.stopPropagation();

        var query = this.getFormVal();
        this.trigger("start_search", query);
      }
    });

    var SearchBarWidget = BaseWidget.extend({

      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');

        // search widget doesn't need to execute queries (but it needs to listen to them)
        this.pubsub.subscribe(this.pubsub.FEEDBACK, _.bind(this.handleFeedback, this));
        this.view.activate(beehive);
      },

      defaultQueryArguments: {
        //sort: 'date desc',
        fl: 'id'
      },

      handleFeedback: function(feedback) {
        switch (feedback.code) {
          case ApiFeedback.CODES.SEARCH_CYCLE_STARTED:
            this.setCurrentQuery(feedback.query);
            this.view.setFormVal(feedback.query.get('q').join(' '));
            break;
        }
      },

      initialize: function (options) {

        this.currentQuery = undefined;
        this.view = new SearchBarView();
        this.listenTo(this.view, "start_search", function (query) {
          var newQuery = new ApiQuery({
            q: query
          });

          this.changeDefaultSort(newQuery);
          this.navigate(newQuery);
        });

        this.listenTo(this.view, "render", function () {
          var query = this.getCurrentQuery().get("q");
          if (query) {
            this.view.setFormVal(query);
            this.view.$(".icon-clear").removeClass("hidden");
          }else {
            this.view.$(".icon-clear").addClass("hidden");
          }
        });

        BaseWidget.prototype.initialize.call(this, options)
      },

      changeDefaultSort : function(query) {

        //make sure not to override an explicit sort if there is one
        if (!query.has("sort")){
          var queryVal, toMatch, operator;
          queryVal = query.get("q")[0];

          //citations operator should be sorted by pubdate too
          toMatch = ["trending(", "instructive(", "useful(", "references("];
          operator = _.find(toMatch, function(e) {
            if (queryVal.indexOf(e) !== -1) {
              return e
            }
          });

          if (operator && operator === "references(" ){
            query.set("sort", "first_author asc")
          }
          if (!operator) {
            query.set("sort", "date desc");
          }
        }
      },

      navigate: function (newQuery) {
        this.pubsub.publish(this.pubsub.START_SEARCH, newQuery);
      },

      openQueryAssistant: function(queryString) {
        if (queryString) {
          this.view.setFormVal(queryString);
        }
        this.view.$el.find('.show-form').click();
      }
    });
    return SearchBarWidget;
  });