/**
 * Created by rchyla on 3/19/14.
 */

require(['js/components/api_query', 'js/widgets/api_query/widget', 'jquery', 'underscore'], function(ApiQuery, ApiQueryWidget, $, _) {

  var query_widget = new ApiQueryWidget(new ApiQuery({foo: 'bar'}));
  $('#top-region').append(query_widget.render());
});

