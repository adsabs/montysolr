define([
      'underscore',
      'marionette',
      'js/components/api_query',
      'js/widgets/base/base_widget',
      'hbs!js/widgets/search_bar/templates/search_bar_template',
      'hbs!js/widgets/search_bar/templates/search_form_template',
      'hbs!js/widgets/search_bar/templates/option-dropdown',
      'js/components/query_builder/plugin',
      'js/components/api_request',
      'js/components/api_targets',
      'js/components/api_feedback',
      'js/mixins/formatter',
      './autocomplete',
      'bootstrap', // if bootstrap is missing, jQuery events get propagated
      'jquery-ui',
      'js/mixins/dependon',
      'analytics',
      'js/components/query_validator',
      'select2',
      'libs/select2/matcher'
    ],
    function (
        _,
        Marionette,
        ApiQuery,
        BaseWidget,
        SearchBarTemplate,
        SearchFormTemplate,
        OptionDropdownTemplate,
        QueryBuilderPlugin,
        ApiRequest,
        ApiTargets,
        ApiFeedback,
        FormatMixin,
        autocompleteArray,
        bootstrap,
        jqueryUI,
        Dependon,
        analytics,
        QueryValidator,
        select2,
        oldMatcher
    ) {

      $.fn.getCursorPosition = function() {
        var input = this.get(0);
        if (!input) return; // No (input) element found
        if ('selectionStart' in input) {
          // Standard-compliant browsers
          return input.selectionStart;
        } else if (document.selection) {
          // IE
          input.focus();
          var sel = document.selection.createRange();
          var selLen = document.selection.createRange().text.length;
          sel.moveStart('character', -input.value.length);
          return sel.text.length - selLen;
        }
      };

      //manually highlight a selection of text, or just move the cursor if no end val is given
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

      //get what is currently selected in the window
      function getSelectedText(el) {

        var text = "";
        if (window.getSelection) {
          //can't just get substring because of firefox bug
          text = el.value.substring(el.selectionStart, el.selectionEnd);
        } else if (document.selection && document.selection.type != "Control") {
          text = document.selection.createRange().text;
        }
        return text;
      }

      //splits out the part of the text that the autocomplete cares about
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

      var SearchBarModel = Backbone.Model.extend({
          defaults : function(){
            return {
              citationCount : undefined,
              numFound : undefined,
              bigquery : false,
              bigquerySource : undefined

            }
          }
      });

      var SearchBarView = Marionette.ItemView.extend({

        template: SearchBarTemplate,

        className : "s-search-bar-widget",

        initialize: function (options) {
          _.bindAll(this, "fieldInsert");
          this.queryBuilder = new QueryBuilderPlugin();
          this.queryValidator = new QueryValidator();
        },

        activate: function(beehive) {
          this.setBeeHive(beehive);
          this.queryBuilder.setQTreeGetter(QueryBuilderPlugin.buildQTreeGetter(beehive));
          var that = this;
          this.queryBuilder.attachHeartBeat(function() {
            that.onBuilderChange();
          });
        },

        onBuilderChange: function() {
          if (this.queryBuilder.isDirty(this.getFormVal())) {
            var newQuery = this.queryBuilder.getQuery();
            this.setFormVal(newQuery);
          }
        },

        modelEvents : {
          'change' : 'render'
        },

        onRender: function () {
          var that = this;
          /*
            select
           */
          this.$('#option-dropdown-container').append(OptionDropdownTemplate);

        function matchStart (term, text) {
            if (text.toUpperCase().indexOf(term.toUpperCase()) == 0) { return true; }
            return false;
          };

          var $select = this.$(".quick-add-dropdown");

          $select.select2({
            placeholder: "All Search Terms",
            matcher: oldMatcher( matchStart )
          })
          .on("change", function(e){
            var val = e.target.value;
            //prevent infinite loop!
            if (!val) return;
            var $option = $(this).find('option[value="' + e.target.value + '"]');

            // Grab any default value that is present on the element
            var defaultValue = $option.data('defaultValue');
            var label = $option.closest("optgroup").attr("label");
            $select.val(null).trigger('change');
            setTimeout(function(){
              that.selectFieldInsert(val, label, defaultValue);
              //not entirely sure why this timeout is necessary...
              //without it, focus is moved from the main query bar
            }, 100);
          })
          //this seems to be necessary to show the placeholder on initial render
          .val(null).trigger('change');

          /*
            end code for select
           */

          this.$("#search-gui").append(this.queryBuilder.$el);

          var $input = this.$("input.q");
          this.$input = $input;
          var performSearch = true;

          $input.autocomplete({
            minLength: 1,
            autoFocus : true,
            //default delay is 300
            delay : 0,
            source:  function( request, response ) {

              var toMatch, matcher, toReturn;

              //don't look for a match if the keydown event was a backspace
              if (!performSearch){
                $input.autocomplete("close");
                return;
              }

              //dont look for a match if cursor is not at the end of search bar
              if ($input.getCursorPosition() !== $input.val().length) {
                $input.autocomplete("close");
                return;
              }

              toMatch = findActiveAndInactive(request.term).active;
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

              var val = $input.val().replace(/^\s+/,""),
                  suggest = ui.item.value;

              var exists, toMatch, confirmedQuery, splitQuery;

              var currentlySelected = getSelectedText($input[0]);
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
                $input.data("ui-autocomplete").suggestedQ = confirmedQuery + " " + ui.item.value;
              }
              else {
                $input.data("ui-autocomplete").suggestedQ = ui.item.value;
              }

              // only insert text if the words match from the beginning
              // not, for instance, if user typed "refereed" and the matching string is "property:refereed"
              if ( suggest.indexOf(toMatch) == 0 ) {

                var text, rest, all;

                text = confirmedQuery ? confirmedQuery + " " + toMatch : toMatch;
                rest = suggest.slice(toMatch.length);
                all = text + rest;

                $input.val(all);
                $input.selectRange(text.length, all.length);

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
              var final = ui.item.value.split("").reverse()[0];
              if ( final == '"' || final == ")" ){
                $input.selectRange($input.val().length - 1);
              }
              else {
                //just move cursor to the end, e.g. for property: refereed
                $input.selectRange($input.val().length);
              }

              analytics('send', 'event', 'interaction', 'autocomplete-used', ui.item.value);
              return false;

            }

          });

          $input.data("ui-autocomplete")._renderItem = function( ul, item ) {
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

          $input.keydown(function (event) {
            if (event.keyCode == 8) {
              performSearch = false; //backspace, do not perform the search
            }
            else if (event.keyCode == 32 ){ //space, do not perform the search
              performSearch = false;
            } else {
              performSearch = true; //perform the search
            }
          });

          this.$('[data-toggle="tooltip"]').tooltip();

        },

        events: {
          "click #field-options button" : "fieldInsert",
          "keyup .q" : "toggleClear",
          "click .show-form": "onShowForm",
          "submit form[name=main-query]": "submitQuery",
          "click .icon-clear" : "clearInput",
          "keyup .q" : "storeCursorInfo",
          "select .q" : "storeCursorInfo",
          "click .q" : "storeCursorInfo",
          'click .bigquery-close' : 'clearBigquery'
        },

        toggleClear : function(){
          var display = Boolean(this.$input.val());
          if (display){
            this.$(".icon-clear").removeClass("hidden");
          }
          else {
            this.$(".icon-clear").addClass("hidden");
          }
        },

        clearInput : function(){
          this.$input.val("");
          this.toggleClear();
        },

        getFormVal: function() {
          return this.$input.val();
        },

        setFormVal: function(v) {
          /*
            bigquery special case: don't show the confusing *:*, just empty bar
           */
         if ( this.model.get('bigquery') && v === '*:*' ) {
           this.$(".q").val('');
         } else {
           this.$(".q").val(v);
         }
          this.toggleClear();
        },

        serializeData : function(){
          var j = this.model.toJSON();
          j.numFound = j.numFound ?  this.formatNum(j.numFound) : 0;
          if (this.model.get("bigquerySource")){
            if ( this.model.get('bigquerySource').match(/library/i) ){
              this.model.set({libraryName : this.model.get('bigquerySource').match(/library:(.*)/i)[1]});
            }
          }
          return j;
        },

        onShowForm: function() {

          var formVal = this.getFormVal();
          if (formVal.trim().length > 0) {
            if (this.queryBuilder.isDirty(this.getFormVal()) || _.isEmpty(this.queryBuilder.getRules())) {
              this.queryBuilder.updateQueryBuilder(formVal);
            }
            else {
              this.queryBuilder.setRules(this.queryBuilder.getRules());
            }
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

        toggleFormSection: function (e) {
          var $p = $(e.target).parent();
          $p.next().toggleClass("hide");
          $p.toggleClass("search-form-header-active");
        },

        //used for the "field insert" function
        _cursorInfo : {
          selected : "",
          startIndex: 0
        },

        storeCursorInfo : function(e){
          var selected = getSelectedText(e.currentTarget);
          var startIndex = this.$input.getCursorPosition();
          this._cursorInfo = {selected : selected, startIndex : startIndex};
          this.toggleClear();
        },

        selectFieldInsert : function(val, label, initialValue){

          var newVal, specialCharacter;
          var highlightedText = this._cursorInfo.selected;
          var startIndex = this._cursorInfo.startIndex;
          var currentVal = this.getFormVal();

          // By default, selected will be the highlighted text surrounded by double qoutes
          var selected = '\"' + highlightedText + '\"';

          // If there was no highlighted text and an initial value was passed, use the initial value
          if (highlightedText.length === 0 && initialValue) {
            selected = initialValue;
          }

          //selected will be "" if user didn't highlight any text
          //newVal = df + ":\"" + selected + "\"";
          //
          switch (label) {
            case 'fields': {
                if (val === 'first-author') {
                  val = 'author';
                  selected = selected.replace(/"/, '"^');
                } else if (val === 'year') {
                  selected = selected.replace(/"/g, '');
                }
                newVal = val + ":" + selected;
              }
              break;
            case 'operators':
                newVal = val + "(" + selected + ")";
              break;
            case 'special characters':
              if (val === '=') {
                newVal = val + selected;
              } else {
                newVal = selected + val;
              }
              specialCharacter = true;
              break;
          }

          if (highlightedText.length) {
            this.setFormVal(currentVal.substr(0, startIndex) +  newVal + currentVal.substr(startIndex + selected.length));
          }
          else { //append to the end
            var newString = currentVal ?  (currentVal + " " + newVal) : newVal;
            this.setFormVal( newString );
            if (specialCharacter){
              this.$input.selectRange( newString.length );
            }
            else {
              this.$input.selectRange( newString.length -1);
            }
          }
        },

        fieldInsert: function (e) {
          e.preventDefault();
          var newVal, operator,
              currentVal = this.getFormVal(),
              $target = $(e.target),
              df = $target.attr("data-field"),
              punc = $target.attr("data-punc");

          var startIndex = this._cursorInfo.startIndex,
              selected = this._cursorInfo.selected;
          //selected will be "" if user didn't highlight any text

          if ( df.indexOf("operator-") > -1) {
            operator = df.split("-").reverse()[0];
            punc = "(";
            if (selected){
              newVal = operator + "(" + selected + ")";
            }
            else {
              //enclose the full query, set it in and return
              newVal = operator + "(" + currentVal + ")";
              currentVal = "";
              this.setFormVal(newVal);
              return
            }

          } else if (df == "first-author") {
            newVal = " author:\"^" + selected + "\"";
          } else if (punc == "\"") {
            newVal = df + ":\"" + selected + "\"";
          }
          else if (punc == "("){
            newVal = df + ":(" + selected + ")";
          }
          else if (!punc){
            //year
            newVal = df + ":" + selected;
          }

          if (selected) {
            this.setFormVal(currentVal.substr(0, startIndex) +  newVal + currentVal.substr(startIndex + selected.length));
          }
          else { //append to the end
            var newString = currentVal + " " + newVal;
            this.setFormVal( newString );

            if (punc){
              this.$input.selectRange( newString.length -1);
            }
            else {
              this.$input.selectRange( newString.length );
            }
          }

          analytics('send', 'event', 'interaction', 'field-insert-button-pressed', df);

        },

        submitQuery: function(e) {
          var fields, fielded, query;

          e.preventDefault();
          e.stopPropagation();

          query = this.getFormVal();

          //replace uppercased fields with lowercase
          query = query.replace(/([A-Z])\w+:/g, function(letter){return letter.toLowerCase()});
//          // store the query in case it gets changed (which happens when there is an object query)
          this.original_query = query;

          //if we're within a bigquery, translate an empty query to "*:*"
          if (!query && this.model.get('bigquery')){
            query = '*:*';
          }

          var newQuery = new ApiQuery({
            q: query
          });

          // Perform a quick validation on the query
          var validated = this.queryValidator.validate(newQuery);
          if (!validated.result) {
            var tokens = _.pluck(validated.tests, 'token');
            tokens = (tokens.length > 1) ? tokens.join(', ') : tokens[0];
            var pubsub = this.getPubSub();
            pubsub.publish(pubsub.ALERT, new ApiFeedback({
              code: 0,
              msg: '<p><i class="fa fa-exclamation-triangle fa-2x" aria-hidden="true"></i> ' +
              'Sorry! We aren\'t able to understand: <strong><i>' + tokens + '</i></strong></p>' +
              '<p><strong><a href="/">Try looking at the search examples on the home page</a></strong> or ' +
              '<strong><a href="https://adsabs.github.io/help/search/search-syntax">reading our help page.</a></strong></p>',
              type : 'info',
              fade : true
            }));
            return;
          }
          this.trigger("start_search", newQuery);

          //let analytics know what type of query it was
          fields = _.chain(autocompleteArray)
              .pluck("value")
              .map(function(b){
                var m =  b.match(/\w+:|\w+\(/);
                if (m && m.length) return m[0]
              })
              .unique()
              .value();

          fielded = false;

          _.each(fields, function(f){
            if (query.indexOf(f) > -1) {
              fielded = true;
            }
          });

          if (fielded){
            analytics('send', 'event', 'interaction', 'fielded-query-submitted-from-search-bar', query);
          }
          else {
            analytics('send', 'event', 'interaction', 'unfielded-query-submitted-from-search-bar', query);
          }

          //was querybuilder used?
          if (this._queryBuilderUsed){
            analytics('send', 'event', 'interaction', 'querybuilder-used', query);
          }

          //reset
          this._queryBuilderUsed = false;
        },

        clearBigquery : function(){
          this.trigger("clear_big_query");
        }
      });

      _.extend(SearchBarView.prototype, FormatMixin, Dependon.BeeHive);

      var SearchBarWidget = BaseWidget.extend({

        initialize: function (options) {

          this.model = new SearchBarModel();

          this.view = new SearchBarView({model : this.model});

          this.listenTo(this.view, "start_search", function (query) {
            this.changeDefaultSort(query);
            this.navigate(query);
            this.updateState('loading');
            this.view.setFormVal(query.get('q'));
          });

          this.listenTo(this.view, "clear_big_query", function (query) {

            var query = this._currentQuery.clone();
            //awkward but need to remove qid + provide __clearBigQuery
            //for querymediator to do the correct thing
            query.unset('__qid');
            query.unset('__bigquerySource');
            query.set('__clearBiqQuery', 'true');
            this.navigate(query);
          });

          this.listenTo(this.view, "render", function () {
            if (this.model.get('loading')) {
              return;
            }
            var newQueryString = '';
            var query = this.getCurrentQuery();
            var oldQueryString = query.get('q');

            if (oldQueryString) {
              // Grab the original (no simbid refs) query string for the view
              // This is re-run here in case the view is not updated and
              // simbid refs show up
              newQueryString = query.get("__original_query") ?
                query.get("__original_query")[0] : oldQueryString.join(" ");
            }

            if (newQueryString) {
              this.view.setFormVal(newQueryString);
            }
            this.view.toggleClear();
          });

          BaseWidget.prototype.initialize.call(this, options)
        },

        activate: function (beehive) {
          this.setBeeHive(beehive);
          this.activateWidget();
          var pubsub = this.getPubSub();

          // search widget doesn't need to execute queries (but it needs to listen to them)
          pubsub.subscribe(pubsub.FEEDBACK, _.bind(this.handleFeedback, this));
          pubsub.subscribe(pubsub.NAVIGATE, _.bind(this.focusInput, this));
          this.view.activate(beehive.getHardenedInstance());
          pubsub.subscribe(pubsub.INVITING_REQUEST, this.dispatchRequest);
          pubsub.subscribe(pubsub.DELIVERING_RESPONSE, this.processResponse);
        },

        processResponse : function(apiResponse){
          if ( apiResponse.toJSON().stats && apiResponse.get('responseHeader.params').sort ){
            var citationCount = apiResponse.get('stats.stats_fields.citation_count.sum');
            var citationSort = apiResponse.get('responseHeader.params.sort').match(/citation_count/) ? true : false;
            citationCount = citationSort ? citationCount : undefined;
            this.model.set({citationCount : citationCount });
          }
        },

        defaultQueryArguments: {
          'stats': 'true',
          'stats.field' : 'citation_count',
          fl: 'id'
        },

        /*
         * when users return to index page, we should re-focus on the search bar
         * */

        focusInput : function(page){

          if (page == "index-page"){
            this.view.$("input.q").focus();
          }
        },

        handleFeedback: function(feedback) {

          if (feedback.code === ApiFeedback.CODES.SEARCH_CYCLE_STARTED ||
            feedback.code ===  ApiFeedback.CODES.SEARCH_CYCLE_FAILED_TO_START ) {

            var query = feedback.query ? feedback.query : feedback.request.get("query");

            // Grab the original (no simbid refs) query string for the view
            var newq = query.get("__original_query") ?
              query.get("__original_query")[0] : query.get("q").join(" ");

            this.setCurrentQuery(query);

            this.model.set({
              bigquerySource : query.get('__bigquerySource') ?  query.get('__bigquerySource')[0] : 'Bulk query',
              bigquery : query.get('__qid') ? true : false,
              numFound: feedback.numFound
            });

            this.view.setFormVal(newq);
            this.updateState('idle');
          }
        },

        changeDefaultSort : function(query) {

          //make sure not to override an explicit sort if there is one
          if (!query.has("sort")){
            var queryVal = query.get("q")[0];

            //citations operator should be sorted by pubdate, so it isn't added here
            var toMatch = ["trending(", "instructive(", "useful(", "references(", "reviews("];

            //if there are multiple, this will just match the first operator
            var operator = _.find(toMatch, function(e) {
              if (queryVal.indexOf(e) !== -1) {
                return e
              }
            });

            if (operator == "references("){
              query.set("sort", "first_author asc");
            }
            else if (operator){
              query.set("sort", "score desc");
            }
            else if (!operator) {
              query.set("sort", "date desc");
            }
          }
        },

        navigate: function (newQuery) {

          this.getPubSub().publish(this.getPubSub().START_SEARCH, newQuery);
        },

        openQueryAssistant: function(queryString) {
          if (queryString) {
            this.view.setFormVal(queryString);
          }
          this.view.$el.find('.show-form').click();
        },

        onShow : function(){
          this.view.$("input[name=q]").focus();
        },

        onDestroy: function () {
          this.view.queryBuilder.destroy();
          this.view.destroy();
        },

        onLoading: function () {
          this.model.set('loading', true);
        },

        onIdle: function () {
          this.model.set('loading', false);
        }
      });

      _.extend(SearchBarWidget.prototype, Dependon.BeeHive);
      return SearchBarWidget;
    });
