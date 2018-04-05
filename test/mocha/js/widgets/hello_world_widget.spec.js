define([
    'marionette',
    'backbone',
    'js/widgets/base/base_widget',
    'js/components/api_response',
    'js/components/api_request',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub',
    'js/widgets/hello_world/widget',
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
           HelloWorldWidget,
           TestData
    ) {

    describe("Hello Worlds Widget (hello_world_widget.spec.js)", function () {

      beforeEach(function () {
        this.minsub = new (MinimalPubSub.extend({
          request: function (apiRequest) {
            return TestData();
          }
        }))({verbose: false});
      });

      afterEach(function () {
        $("#test").empty();
      });


      it("returns API object", function() {
        expect(new HelloWorldWidget()).to.be.instanceof(BaseWidget);
      });

      it("should display stuff", function () {
        var widget = new HelloWorldWidget();
        var $w = widget.render().$el;
        $('#test').append($w); // open your chrome/firefox web developer console and put brakpoint here

        expect($w.text().indexOf("I'm the new beautiful widget.") > -1).to.be.true;
        expect($w.find('.message').text()).to.be.eql('');

        widget.model.set('msg', 'Hello World!');
        expect($w.find('.message').text()).to.be.eql('Hello World!');

        // set name and click 'OK'
        $w.find('input').val('BumBleBee');
        $w.find('button').click();

        expect(widget.model.get('name')).to.eql('wonderful BumBleBee');
      });

      it("knows to listen to the pubsub", function() {
        var widget = new HelloWorldWidget();
        widget.activate(this.minsub.beehive.getHardenedInstance()); // this is normally done by application

        var $w = widget.render().$el;
        $('#test').append($w);

        var minsub = this.minsub;
        minsub.publish(minsub.START_SEARCH, minsub.createQuery({'q': 'hello'}));

        expect($w.find('.message').text()).to.be.eql('The query found: 841359 results.');
      });


    })
  });
