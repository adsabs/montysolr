define([
  "marionette",
  "hbs!../templates/libraries-list-container",
  "hbs!../templates/library-item",
  "hbs!../templates/no-libraries",
  "hbs!../templates/loading-libraries",


], function(
  Marionette,
  LibraryContainer,
  LibraryItem,
  NoLibrariesTemplate,
  LoadingTemplate

  ){



  var LibraryItemView = Marionette.ItemView.extend({

    formatDate : function(d){
      var d = new Date(d);

      function formatAMPM(date) {
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'p' : 'a';
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0'+minutes : minutes;
        var strTime = hours + ':' + minutes  + ampm;
        return strTime;
      }

      var year = d.getFullYear().toString().slice(2,4),
        month = d.getMonth() + 1,
        day = d.getDay(),
        time = formatAMPM(d);

      return month + "/" + day + "/" + year + " " + time;

    },

    serializeData : function(){

      var data = this.model.toJSON();
      data.libNum = Marionette.getOption(this, "libNum")
      data.date_last_modified = this.formatDate(data.date_last_modified);
      return data;
    },

    template : LibraryItem,

    tagName : "tr",

    triggers : {
      "click" : "navigateToLibrary"
    }

  });



  var LibraryCollectionView = Marionette.CompositeView.extend({

    initialize : function(options){
      options = options || {};
    },

    template : LibraryContainer,

    childViewContainer : ".libraries-list-container tbody",

    childView : LibraryItemView,

    childViewOptions: function(model, index) {
      return {
        libNum : index + 1
      }
    },

    childEvents : {
      "navigateToLibrary" : "triggerNavigate"
    },

    events : {
      "click thead button" : "sortCollection"
    },


    modelEvents :   {
      "change" : function(){
        this.collection.sort();
        this.render();
      }
    },

    collectionEvents : {
      "reset" :  function(){
        this.collection.sort();
        this.render();
      }
    },

    triggerNavigate : function(childView){
      this.trigger("navigate:library", childView.model.get("id"));
    },

    sortCollection : function(e){

      var sortData = $(e.currentTarget).data("sort"), sort = sortData.sort;

      var order =  (sort !== this.model.get("sort") ) ? "asc" : (this.model.get("order") == "asc") ? "desc" : "asc";
      this.model.set({"sort" : sort, type : sortData.type, "order" : order});

    },

    render : function(){

      if ( !this.collection.length  ){
        this.$el.html(NoLibrariesTemplate());
        return this
      }

      else {
        return Marionette.CompositeView.prototype.render.apply(this, arguments);
      }
    }

  });

  return LibraryCollectionView;

})