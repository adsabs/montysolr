/**
 * Created by rchyla on 3/19/14.
 */

define(['underscore', 'jquery', 'backbone', 'marionette',
  'js/components/api_query',
  'hbs!./templates/widget-view',
  'hbs!./templates/item-view'

],

  function(_, $, Backbone, Marionette,
           ApiQuery,
           WidgetTemplate, ItemTemplate){

    // Model
    var KeyValue = Backbone.Model.extend({ });

    // Collection of data
    var KeyValueCollection = Backbone.Collection.extend({
      model : KeyValue
    });

    var ItemView = Marionette.ItemView.extend({
      tagName: 'tr',
      template : ItemTemplate,
      events : {
        'click .remove' : 'removeItem',
        'blur .key>input': 'onChange',
        'blur .value>input': 'onChange'
      },
      onChange: function(ev) {
        var container = $(ev.target);
        var attr = _.clone(this.model.attributes);
        var newVal = $(ev.target).val();

        if (container.attr('name') == 'key' && attr.key != newVal) {
          this.model.set('key', newVal);
        }
        else if (container.attr('name') == 'value' && attr.value != newVal) {
          this.model.set('value', newVal);
        }
      },
      removeItem : function(ev){
        ev.preventDefault();
        this.model.destroy();
        // I'd prefer to call controller - instead of changing the model
        // from inside the view; but this means one has to setup
        // complicated controller->collection->view event chain
        //var key = $(ev.target.parentElement.parentElement).find('td.key');
        //if (key) {
        //  this.trigger('remove-key', key.text());
        //}
      }
    });

    var WidgetView = Marionette.CompositeView.extend({
      template : WidgetTemplate,
      itemView : ItemView,
      itemViewContainer: "#api-query-values",
      events: {
        'click button#api-query-load': 'loadApiQuery',
        'click button#api-query-add': 'addNewItem',
        'click button#api-query-run': 'runApiQuery',
        'submit form': 'loadApiQuery',
        'update-loader': 'updateLoader',
        'remove-key': 'removeKey'
      },
      addNewItem: function(ev) {
        ev.preventDefault();
        this.trigger('add-new-item');
      },
      loadApiQuery: function(ev) {
        ev.preventDefault();
        var data = $('input#api-query-input').val();
        if (data && _.isString(data) && data.trim().length > 0) {
          this.trigger('load-api-query', data);
        }
      },
      runApiQuery: function(ev) {
        ev.preventDefault();
        var q = new ApiQuery();
        _.map(this.collection.models, function(a) {
          var attr = a.attributes;
          if (attr.key) {
            q.set(attr.key, attr.value.split('|'));
          }
        });
        $('#api-query-result').text(q.url());
        this.trigger('run-api-query', q);
      },
      updateLoader: function(data) {
        $('input#api-query-input').val(data);
      },
      removeKey: function(data) {
        this.trigger('remove-key', data);
      }
    });

    var WidgetController = Marionette.Controller.extend({

      getData: function(apiQuery) {
        if (!apiQuery) {
          throw Error("Wrong input!");
        }
        if (_.isString(apiQuery)) {
          apiQuery = new ApiQuery().load(apiQuery);
        }

        var pairs = _.map(apiQuery.pairs(), function(pair){
          return {key: pair[0], value: pair[1].join('|')};
        });
        return pairs;
      },

      initialize : function(apiQuery){
        if (!apiQuery || !('toJSON' in apiQuery)) {
          apiQuery = new ApiQuery(); // empty
        }
        this.collection = new KeyValueCollection(this.getData(apiQuery));
        this.view = new WidgetView({collection: this.collection});
        this.listenTo(this.view, 'load-api-query', this.update);
        this.listenTo(this.view, 'add-new-item', this.addNewItem);
        this.listenTo(this.view, 'run-api-query', this.runApiQuery);
        return this;
      },

      render : function(){
        this.view.render()
        return this.view.el
      },

      returnView : function(){
        return this.view
      },

      update : function(apiQuery) {
        if (this.collection) {
          this.collection.reset(this.getData(apiQuery))
        }
        else {
          this.initialize(apiQuery);
        }
      },

      addNewItem: function() {
        this.collection.add({key:'', value:''});
      },

      runApiQuery: function(apiQuery) {
        console.log('runApiQuery:', apiQuery);
      }

    });

    return WidgetController;
  });


