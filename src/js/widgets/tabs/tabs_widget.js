/**
 * Created by alex on 5/12/14.
 */
define(['marionette', 'bootstrap', 'hbs!./templates/tabs_inner', 'hbs!./templates/tabs_outer', 'hbs!./templates/tabs_title'], function (Marionette, Bootstrap, innerTemplate, outerTemplate, titleTemplate) {

    var TabsWidget = Marionette.ItemView.extend({

      // expects in options a list of views like this:
      // {tabs: [{title : (title for tab) , view: (actual view), id : (unique id)}, {default : true/false} (a tab widget has only one default tab)]}

      initialize: function (options) {
        this.tabs = options.tabs || [];
      },

      //overriding marionette render method
      render : function () {
        if (this.beforeRender) {
          this.beforeRender();
        }
        this.trigger("before:render", this);
        this.trigger("item:before:render", this);

        var $tempEl = $(outerTemplate());
        var $nav = $tempEl.find("ul.nav"),
          $tab = $tempEl.find("div.tab-content");

        _.each(this.tabs, function (t) {

          $nav.append(titleTemplate({"id": t.id, title: t.title, default: t.default}))
          $tab.append(innerTemplate({"id": t.id, default: t.default}));
        }, this);

        this.$el.html($tempEl.html());

        _.each(this.tabs, function(t){
         this.$("#"+ t.id).append(t.view.render().el)
        }, this);

        this.bindUIElements();

        if (this.onRender) {
          this.onRender();
        }
        this.trigger("render", this);
        this.trigger("item:rendered", this);
        return this;

      },

      onClose : function(){
        _.each(this.tabs, function(t){
          if (t.view.close) {t.view.close(); }
          else if (t.view.remove) {t.view.remove(); }
        }, this)

      }

    })

    return TabsWidget

  });