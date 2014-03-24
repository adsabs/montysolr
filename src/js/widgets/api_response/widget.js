/**
 * Created by rchyla on 3/19/14.
 *
 * Simple widget for debugging API response; it allows to copy&paste
 * JSON input directly into the text area and have it loaded.
 */

define(['underscore', 'jquery', 'backbone', 'marionette',
  'js/components/api_response',
  'hbs!./templates/widget-view'
],

  function(_, $, Backbone, Marionette,
           ApiResponse,
           WidgetTemplate){

    var Model = Backbone.Model.extend({ });

    var WidgetView = Marionette.ItemView.extend({
      template : WidgetTemplate,
      events: {
        'click button#api-response-load': '_load',
        'click button#api-response-run': '_run',
        'submit form': '_load',
        'blur textarea#api-response-input': '_onChange'
      },

      // this is alternative way to trigger re-rendering
      // upon model change
      //modelEvents: {
      //  'change': 'fieldsChanged'
      //},
      //fieldsChanged: function() {
      //  this.render();
      //},

      _onChange: function() {
        this._changed = this.model.input != $('textarea#api-response-input').val();
      },
      _load: function(ev) {
        ev.preventDefault();
        var data = $('textarea#api-response-input').val();
        this.options.controller.update(data);
      },
      _run: function(ev) {
        ev.preventDefault();
        if (this._changed) {
          this._load(ev);
        }
        this.options.controller.run(this.model.attributes);
      },
      _error: function(msg) {
        $('#api-response-error').empty();
        $('#api-response-error').append(msg);
      }
    });

    var WidgetController = Marionette.Controller.extend({


      _getModel: function(data) {
        var model = null;
        if (_.isString(data)) {
          var r = new ApiResponse(JSON.parse(data));
          model = new Model({key: r.url(), input: data, result: 'new ApiResponse(' + JSON.stringify(r.toJSON()) + ')', R: r});
        }
        else if (data && _.isObject(data) && 'url' in data) {
          model = new Model({key: data.url(), input: JSON.stringify(data.toJSON()), result: 'new ApiResponse(' + JSON.stringify(data.toJSON()) + ')', R: data});
        }
        else {
          model = new Model({key: '', input: '', result: '{}', R: {}});
        }

        return model;
      },

      initialize : function(apiResponse){
        this.model = this._getModel(apiResponse);
        this.view = new WidgetView({model: this.model, controller: this});
        return this;
      },

      render : function(){
        this.view.render()
        return this.view.el
      },

      update : function(apiResponse) {
        try {
          var m = this._getModel(apiResponse);
          this.model.set(m.attributes);
          this.view.render();
          this.view._error('');
        }
        catch(e) {
          if (this.view) {
            console.error(e.message, apiResponse);
            this.view._error(e.message);
          }
          throw e;
        }
      },

      run: function(model) {
        //console.log(model.R);
      }
    });

    return WidgetController;
  });


