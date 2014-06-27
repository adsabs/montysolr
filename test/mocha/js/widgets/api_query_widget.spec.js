/**
 * Created by rchyla on 3/19/14.
 */

define(['js/widgets/api_query/widget', 'js/components/api_query', 'js/services/pubsub', 'js/components/beehive', 'backbone', 'jquery'],
  function(ApiQueryWidget, ApiQuery, PubSub, BeeHive, Backbone, $) {
  describe("ApiQuery Widget (UI)", function () {

      var clearMe = function(done) {
        var ta = $('#test-area');
        if (ta) {
          ta.empty();
        }
        done();
      }
      beforeEach(clearMe);
      afterEach(clearMe);

      it("can returns object of ApiQueryWidget", function(done) {
        expect(new ApiQueryWidget()).to.be.instanceof(ApiQueryWidget);
        done();
      });

      it("should build a view of the ApiQuery values", function(done) {
        var q = new ApiQuery().load('foo=bar&foo=baz');
        var widget = new ApiQueryWidget(q);
        var html = $(widget.render().el);

        expect(html.find('input[value="foo"]').length).to.be.eql(1); //contain('<tr><td class="key"><input type="text" name="key" value="foo"></td>');
        expect(html.find('input[value="bar|baz"]').length).to.be.eql(1); //.to.contain('<td class="value"><input type="text" name="value" value="bar|baz"></td>');
        expect(html.find('a.remove').length).to.be.eql(1); //contain('<td> <a class="remove">remove</a></td></tr>');
        expect($(widget.render().el).find('tr').length).to.be.equal(1);

        widget.initialize(new ApiQuery().load('foo=bar&boo=baz'));
        html = $(widget.render().el).html();
        expect($(widget.render().el).find('tr').length).to.be.equal(2);


        widget.onLoad(new ApiQuery().load('foo=bar'));
        html = $(widget.render().el).html();
        expect($(widget.render().el).find('tr').length).to.be.equal(1);

        widget = new ApiQueryWidget(new ApiQuery());
        html = $(widget.render().el);
        expect(html.find('a.remove').length).to.eql(0);//to.not.contain('<a class="remove">remove</a></td></tr>');
        expect($(widget.render().el).find('tr').length).to.be.equal(0);
        done();
      });

    it("has interactive features: load/add-new-iterm/remove/run", function(done) {
      var q = new ApiQuery().load('foo=bar&boo=baz&woo=waz');
      var widget = new ApiQueryWidget(q);
      var ta = $('#test-area');
      ta.append(widget.render().el);

      // widget is initialized with the query
      expect(ta.find('#api-query-input').val()).to.equal("boo=baz&foo=bar&woo=waz");

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

      ta.find('#api-query-run').click();
      // result will be updated
      expect(ta.find('#api-query-result').text()).to.equal("hey=joe&what=dydu");
      // also input will
      expect(ta.find('#api-query-input').val()).to.equal("hey=joe&what=dydu");
      done();
    });

    it("knows how to interact with pubsub", function(done) {

      var beehive = new BeeHive();
      var pubsub = new PubSub();
      beehive.addService('PubSub', pubsub);

      var q = new ApiQuery().load('foo=bar&boo=baz&woo=waz');
      var widget = new ApiQueryWidget(q);
      widget.activate(beehive.getHardenedInstance());
      var $w = $(widget.render().el);

      expect($w.find('#api-query-input').val()).to.equal("boo=baz&foo=bar&woo=waz");

      // send a new response trough the pubsub, widget should catch it and display
      pubsub.trigger(pubsub.NEW_QUERY, new ApiQuery({foo:'bar'}));

      expect($w.find('#api-query-input').val()).to.equal("foo=bar");

      done();
    });

  });
});
