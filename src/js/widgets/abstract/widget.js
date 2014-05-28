/**
 * Created by alex on 5/19/14.
 */
define(['marionette', 'backbone', 'jquery', 'underscore',
    'js/widgets/base/base_widget', 'hbs!./templates/abstract_template'],
  function (Marionette, Backbone, $, _ , BaseWidget, abstractTemplate) {

    var AbstractModel = Backbone.Model.extend({
      defaults : function(){
        return {
          abstract: undefined,
          title: undefined,
          authorAff: undefined,
          pub: undefined,
          pubdate: undefined,
          keywords : undefined
        }
      },

      parse : function(response){
        var items, authorAff, hasAffiliation;

        items = response.response.docs[0];

        items.aff = items.aff || [];
        if(items.aff.length){
          hasAffiliation = true
        }
        // joining author and aff
        authorAff = _.zip(items.author, items.aff);

        return {
          hasAffiliation : hasAffiliation,
          abstract: items.abstract,
          title: items.title[0],
          authorAff: authorAff,
          pub: items.pub,
          pubdate: items.pubdate,
          keyword : items.keyword

        }

      }
    });

    var AbstractView = Marionette.ItemView.extend({

      initialize : function(){
        this.listenTo(this.model, "change", this.render)
      },

      template : abstractTemplate,
      events : {
        "click #toggle-aff": "toggleAffiliation"
      },

      toggleAffiliation : function(){
        this.$(".affiliation").toggleClass("hide")
      }

    });

    var AbstractWidget = BaseWidget.extend({
      initialize : function(options) {
        options = options || {};
        this.model = options.data ? new AbstractModel(options.data, {parse: true}) : new AbstractModel()
        this.view = new AbstractView({model: this.model});

        BaseWidget.prototype.initialize.apply(this, arguments);

      },

       defaultQueryArguments :  {
          fl: 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,volume,year'
        },

       processResponse : function(apiResponse){
         var q = apiResponse.getApiQuery();
         this.setCurrentQuery(q);

         var r = apiResponse.toJSON();
         this.model.set(this.model.parse(r));
      }

    });


    return AbstractWidget;
  });