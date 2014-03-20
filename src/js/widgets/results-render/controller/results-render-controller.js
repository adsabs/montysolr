define(['marionette',
  'js/widgets/results-render/collections/results-render-collection',
  'js/widgets/results-render/views/results-render-list-view'],

  function(Marionette, ListCollection, ResultsListView){

    //expects a response object, on initialization it returns a computed dom element to be
    //appended to the layout by the app

    //if there is a new response, all you need to do is call update on the controller with the new response, and
    //the list will re-render automatically

    //this will probably have to be tweaked later
    var preprocess = function(r) {
      var docs = r.response.docs;
      var highlights = r.highlighting;

      docs = _.map(docs, function(d){
        var id = d.id;
        var h = {'highlights': highlights[id]};
        return _.extend(d, h);
      });

      return docs;
    };

    var ResultsListController = Marionette.Controller.extend({

      initialize : function(apiResponse){
        if (apiResponse){
          this.collection = new ListCollection(preprocess(apiResponse.toJSON()));
          this.view = new ResultsListView({collection: this.collection});
        }
      },

      returnRenderedView : function(){
        this.view.render();
        return this.view.el;
      },

      returnView : function(){
        return this.view;
      },

      update : function(apiResponse) {
        if (this.collection) {
          this.collection.reset(preprocess(r.toJSON()));
        }
        else {
          this.initialize(apiResponse);
        }
      }

    })

    return ResultsListController;

  });

