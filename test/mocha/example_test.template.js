define([
    'marionette',
    'backbone',
    'js/widgets/base/base_widget',
    'js/components/api_response',
    'js/components/api_request',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub',
    'test/mocha/js/widgets/test_json/test1'
  ],
  function(
           Marionette,
           Backbone,
           BaseWidget,
           ApiResponse,
           ApiRequest,
           ApiQuery,
           MinimalPubSub,
           TestData
    ) {

    describe("Example Widget (example_test.template.js)", function () {

      beforeEach(function () {
        this.minsub = minsub = new (MinimalPubSub.extend({
          request: function (apiRequest) {
            return TestData();
          }
        }))({verbose: false});
      });

      afterEach(function () {
        $("#test").empty();
      });


      it("returns API object", function() {
        //expect(new SomeWidget()).to.be.instanceof(BaseWidget);
      });

      it("should do something", function () {
        return; // remove me
        var widget = new SomeWidget();
        widget.model.set('msg', 'Hello World!');
        var $w = widget.render().$el;
        $('#test').append($w);
        expect($w.find('.message').text().trim()).to.eql('Hello World!');
      });


    })
  });