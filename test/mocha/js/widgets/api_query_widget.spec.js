/**
 * Created by rchyla on 3/19/14.
 */

define(['js/widgets/api_query/widget', 'js/components/api_query', 'backbone', 'jquery'],
  function(ApiQueryWidget, ApiQuery, Backbone, $) {
  describe("ApiQuery Widget (UI)", function () {

      var clearMe = function() {
        var ta = $('#test-area');
        if (ta) {
          ta.empty();
        }
      }
      beforeEach(clearMe);
      afterEach(clearMe);

      it("can returns object of ApiQueryWidget", function() {
        expect(new ApiQueryWidget()).to.be.instanceof(ApiQueryWidget);
      });

      it("should build a view of the ApiQuery values", function() {
        var q = new ApiQuery().load('foo=bar&foo=baz');
        var widget = new ApiQueryWidget(q);
        var html = $(widget.render()).html();

        expect(html).to.contain('<tr><td class="key"><input type="text" name="key" value="foo"></td>');
        expect(html).to.contain('<td class="value"><input type="text" name="value" value="bar|baz"></td>');
        expect(html).to.contain('<td> <a class="remove">remove</a></td></tr>');
        expect($(widget.render()).find('tr').length).to.be.equal(1);

        widget.initialize(new ApiQuery().load('foo=bar&boo=baz'));
        html = $(widget.render()).html();
        expect($(widget.render()).find('tr').length).to.be.equal(2);


        widget.update(new ApiQuery().load('foo=bar'));
        html = $(widget.render()).html();
        expect($(widget.render()).find('tr').length).to.be.equal(1);

        widget = new ApiQueryWidget(new ApiQuery());
        html = $(widget.render()).html();
        expect(html).to.not.contain('<a class="remove">remove</a></td></tr>');
        expect($(widget.render()).find('tr').length).to.be.equal(0);
      });

    it("has interactive features: load/add-new-iterm/remove/run", function() {
      var q = new ApiQuery().load('foo=bar&boo=baz&woo=waz');
      var widget = new ApiQueryWidget(q);
      var ta = $('#test-area');
      ta.append(widget.render());

      // we have three items
      expect(ta.find('tr').length).to.be.equal(3);
      expect(ta.find('tr:nth-child(1) >> input[name=key]').val()).to.equal('foo');
      expect(ta.find('tr:nth-child(3) >> input[name=key]').val()).to.equal('woo');
      expect(ta.find('tr:nth-child(1) >> input[name=value]').val()).to.equal('bar');

      // click on the second item remove
      $(ta.find('a')[2]).click();
      // now there should be only two lines there
      expect(ta.find('tr').length).to.be.equal(2);
      // click on 'add'
      ta.find('#api-query-add').click();
      // now we have three items again (the last one is empty)
      expect(ta.find('tr').length).to.be.equal(3);
      expect($(ta.find('tr')[2]).find('input[name=key]').val()).to.be.equal("");
      expect($(ta.find('tr')[2]).find('input[name=value]').val()).to.be.equal("");

      // edit the new row
      ta.find('tr:nth-child(3) >> input[name=key]').focus().val('hey').blur();
      expect(widget.collection.models[2].attributes).to.eql({key:'hey', value:''});
      ta.find('tr:nth-child(3) >> input[name=value]').focus().val('joe').blur();
      expect(widget.collection.models[2].attributes).to.eql({key:'hey', value:'joe'});

      // click on the 'run'
      ta.find('#api-query-run').click();
      // it should generate the serialized version on the api-query
      expect(ta.find('#api-query-result').text()).to.equal("boo=baz&foo=bar&hey=joe");

      // and the whole thing can be reloaded
      ta.find('#api-query-input').val('what=dydu&hey=joe');
      // click on 'load'
      ta.find('#api-query-load').click();
      // should have two elements
      expect(ta.find('tr').length).to.be.equal(2);
      // TODO: have the query reload automatically
      ta.find('#api-query-run').click();
      expect(ta.find('#api-query-result').text()).to.equal("hey=joe&what=dydu");

    });

  });
});
