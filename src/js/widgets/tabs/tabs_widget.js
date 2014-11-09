/**
 * Created by alex on 5/12/14.
 */
define([
  'underscore',
  'marionette',
  'bootstrap',
  'hbs!./templates/tabs_inner',
  'hbs!./templates/tabs_outer',
  'hbs!./templates/tabs_title'],
  function (
    _,
    Marionette,
    Bootstrap,
    innerTemplate,
    outerTemplate,
    titleTemplate) {

  var TabsWidget = Marionette.ItemView.extend({

    // expects in options a list of views like this:
    // {tabs: [{title : (title for tab) , widget: (actual widget), id : (unique id)}, {default : true/false} (a tab widget has only one default tab)]}

    /**
     * Activates all widgets (our children)
     * @param beehive (hardened version)
     */
    activate: function(beehive) {
      _.each(this.widgets, function (w) {
        w.activate(beehive.getHardenedInstance());
      });
    },

    initialize: function (options) {
      this.tabs = options.tabs || [];
      this.widgets = _.map(this.tabs, function (t, i) {
        if (!t.widget) {
          throw new Error('Missing "widget" for: ' + t.title +  ' [' + i + ']');
        }
        return t.widget
      });
    },

    //overriding marionette render method
    render: function () {
      if (this.beforeRender) {
        this.beforeRender();
      }
      this.trigger("before:render", this);
      this.trigger("item:before:render", this);

      var $tempEl = $(outerTemplate());
      var $nav = $tempEl.find("ul.nav"),
        $tab = $tempEl.find("div.tab-content");

      _.each(this.tabs, function (t) {

        $nav.append(titleTemplate({"id": t.id, title: t.title, default: t.default}));
        $tab.append(innerTemplate({"id": t.id, default: t.default}));
      }, this);

      this.$el.html($tempEl.html());
      _.each(this.tabs, function (t) {
        this.$("#" + t.id).append(t.widget.render().el)
      }, this);

      this.bindUIElements();

      if (this.onRender) {
        this.onRender();
      }
      this.trigger("render", this);
      this.trigger("item:rendered", this);
      return this;

    },

    onClose: function () {
      _.each(this.tabs, function (t) {
        if (t.widget.close) {
          t.widget.close();
        }
        else if (t.widget.remove) {
          t.widget.remove();
        }
      }, this)

    }

  });

  return TabsWidget

});