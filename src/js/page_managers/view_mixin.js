define([
  'underscore',
  'jquery'
  ],
  function(
    _,
    $
    ) {

    var PageManagerMixin = {
      getWidgetsFromTemplate: function(template, isDebug) {
        var widgets = {};
        var widgetTargets = $(template).find('[data-widget]');
        if (widgetTargets.length > 0) {
          _.each(widgetTargets, function(widgetTarget) {
            var widgetName = widgetTarget.getAttribute('data-widget');
            var isDebug = widgetTarget.getAttribute('data-debug');

            if (isDebug && isDebug == "true" && !isDebug) {
              return;
            }

            widgets[widgetName] = widgetTarget;
          })
        }
        return widgets;
      }
    };

    return PageManagerMixin;
});