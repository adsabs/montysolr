define([
  "js/widgets/base/base_widget",
  "js/components/api_query",
  "js/components/api_query_updater",
  "hbs!./form",
  "jquery-ui",
  "js/widgets/paper_search_form/topterms",
  "analytics"
], function(
  BaseWidget,
  ApiQuery,
  ApiQueryUpdater,
  FormTemplate,
  JQueryUI,
  AutocompleteData,
  analytics
  ){

  //for autocomplete
  function split( val ) {
    return val.split( /,\s*/ );
  }
  function extractLast( term ) {
    return split( term ).pop();
  }


  var FormView = Marionette.ItemView.extend({

    template : FormTemplate,

    className : "classic-form",

    events  : {
        "click button[type=submit]" : "submitForm",
        "input input" : "checkValid",
        "input textarea" : "checkValid",

    },

    checkValid : function(){

      var allVals = this.$("input[type=text], textarea").map(function(){return $(this).val()}).get().join("");
      if (allVals) {
        this.$("button[type=submit]").prop("disabled", false);
       }
        else {
          this.$("button[type=submit]").prop("disabled", true);
        }
    },

    submitForm : function(e){

      e.preventDefault();
      var queryDict = this.serializeClassic();
      if (!queryDict.q.length){
        //allow searching of just dates (which is a filter)
        queryDict.q.push("*");
      }
      this.trigger("submit", queryDict);
      this.$("button[type=submit]").each(function(){
        $(this).html('<i class="icon-loading"/>  Loading...')
      });

    },

    serializeClassic : function(){
      var qDict = {q : [], fq: []}, database, pubdateVals, dates, datestring, matchers;
      var pubdateDefaults = {
        month_from: "01",
        year_from: "0000",
        month_to:  "12",
        year_to: "9999"
      };

     //database filters
     database = this.$("div[data-field=database] input:checked").map(
       function(){return $(this).attr("name")}
     );

     if (database.length == 2) {
       qDict.fq.push("database:" + "(astronomy OR physics)");
     }
      else if (database.length == 1){
       qDict.fq.push("database:" + database[0]);
     }

     //article and prop refereed
     this.$("div[data-field=property] input:checked").each(function(){
       qDict.q.push("property:" + $(this).attr("name"));
     });

     //date special case (it's also a filter, not a q)
     //do we need a pubdate entry in the first place?
     pubdateVals = this.$("div[data-field=date] input")
       .map(function(){return $(this).val()}).get().join("");

     if (pubdateVals){
       dates = this.$("div[data-field=date] input").map(function(){
         var $t = $(this);
         var val = $t.val() || pubdateDefaults[$t.attr("name")];
         return val;
       });

       datestring = "[" + dates[1] + "-" + dates[0] + "-0 TO " + dates[3] + "-" + dates[2] + "-0]";
       qDict.q.push("pubdate:" + datestring);

      }

      matchers = {
        default: /=?"[^"]+"|[=\w]+/g,
        author:  /.+/gm,
        bibstem: /[^,^\s]+/g
      };

     //all input/textarea fields other than date
      this.$("div[data-textInput=true]").each(function(){

        var $t = $(this), logic, field, matcher, phrases,
            val = $t.find("input[type=text], textarea").val();

        if (val !== ""){
          logic = $t.find(".logic-group input:checked").val();
          field = $t.data("field");
          if (logic == "BOOLEAN"){
            val = val.replace("\n", " ");
            qDict.q.push(field + ":(" + val + ")");
          }
          else {
            logic = " " + logic + " ";
            if (matchers[field]){
              matcher = matchers[field];
            }
            else {
              matcher = matchers.default;
            }
            phrases = val.match(matcher);

            //quote matches if field == author
            phrases = field == "author" ? _.map(phrases, function(p){ return '"' + p + '"'}) : phrases;
            //use parentheses always (bc of = parsing issue)
            phrases = phrases.length > 1 ? phrases.join(logic) : phrases[0];
            qDict.q.push(field + ":(" + phrases +")" );
          }

        }
      });
      return qDict;
    },

    onRender : function(e){
      this.$("input[name=bibstem]")
        .on( "keydown", function( event ) {
          if ( event.keyCode === $.ui.keyCode.TAB &&
            $( this ).autocomplete( "instance" ).menu.active ) {
            event.preventDefault();
          }
        })
        .autocomplete({
        source : AutocompleteData,
        minLength : 2 ,
        autoFocus : true,
        source: function( request, response ) {
          // delegate back to autocomplete, but extract the last term
          response( $.ui.autocomplete.filter(
            AutocompleteData, extractLast( request.term ) ) );
        },
        focus: function() {
          // prevent value inserted on focus
          return false;
        },
        select: function( event, ui ) {
          var terms = split( this.value );
          // remove the current input
          terms.pop();
          // add the selected item
          terms.push( ui.item.value );
          // add placeholder to get the comma-and-space at the end
          terms.push( "" );
          this.value = terms.join( ", " );
          return false;
        }
      });
    }
  });

  FormWidget = BaseWidget.extend({

    initialize: function (options) {
      options = options || {};
      this.view = new FormView();
      this.listenTo(this.view, "submit", this.submitForm);

    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
    },

    onShow : function(){
      //clear the loading button
      this.view.$("button[type=submit]").each(function(){
        $(this).html('<i class="fa fa-search"></i> Search')
      });
      //set focus to author field
      this.view.$("#classic-author").focus();
    },

    submitForm : function(queryDict){

      var newQuery = {
        q: queryDict.q.join(" "),
        sort: "date desc"
      };

      if (queryDict.fq.length) newQuery.fq = queryDict.fq.join(" ");

      newQuery = new ApiQuery(newQuery);
      this.getPubSub().publish(this.getPubSub().START_SEARCH, newQuery);

      analytics('send', 'event', 'interaction', 'classic-form-submit', JSON.stringify(queryDict));

    },

    //notify application to keep me around in memory indefinitely
    //this is so the form and anything the user has entered into it can stay around
    dontKillMe : true

  });

  return FormWidget;

})
