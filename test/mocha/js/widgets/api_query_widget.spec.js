/**
 * Created by rchyla on 3/19/14.
 */

define(['js/widgets/api_query/widget', 'js/components/api_query', 'backbone'], function(ApiQueryWidget, ApiQuery, Backbone) {
  describe("ApiQuery Widget (UI)", function () {
      it("should build a view of the ApiQuery values", function() {
        var q = new ApiQuery().load('foo=bar&foo=baz');
        var widget = new ApiQueryWidget(q);
        var html = $(widget.render()).html();
        expect(html).to.contain('<tr><td>foo</td><td>bar|baz</td><td> <a class="remove">remove</a></td></tr>');

        widget.initialize(new ApiQuery().load('foo=bar&boo=baz'));
        html = $(widget.render()).html();
        expect(html).to.contain('<tr><td>foo</td><td>bar</td><td> <a class="remove">remove</a></td></tr>');
        expect(html).to.contain('<tr><td>boo</td><td>baz</td><td> <a class="remove">remove</a></td></tr>');

        widget.update(new ApiQuery().load('foo=bar'));
        html = $(widget.render()).html();
        expect(html).to.contain('<tr><td>foo</td><td>bar</td><td> <a class="remove">remove</a></td></tr>');
        expect(html).to.not.contain('<tr><td>boo</td><td>baz</td><td> <a class="remove">remove</a></td></tr>');

        widget = new ApiQueryWidget(new ApiQuery());
        html = $(widget.render()).html();
        expect(html).to.not.contain('<a class="remove">remove</a></td></tr>');
      });

  });
});
