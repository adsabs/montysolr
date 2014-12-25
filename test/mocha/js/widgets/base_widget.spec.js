define([
    'marionette',
    'backbone',
    'js/widgets/base/base_widget',
    'js/components/api_response',
    'js/components/api_request',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub',
    'js/widgets/widget_states',
    'js/widgets/base/item_view'
  ],
  function(
    Marionette,
    Backbone,
    BaseWidget,
    ApiResponse,
    ApiRequest,
    ApiQuery,
    MinimalPubSub,
    WidgetStates,
    ItemView
    ) {

    describe("Base Widget (base_widget.spec.js)", function() {

      var minsub;
      beforeEach(function() {

        minsub = new (MinimalPubSub.extend({
          request: function(apiRequest) {
            return {
              "responseHeader": {
                "status": 0,
                "QTime": 543,
                "params": {
                  "q": "star"
                }
              }
            }
          }
        }))({verbose: false});
      });

      afterEach(function() {
        minsub.close();
      });

      it("returns BaseWidget object", function() {
        expect(new BaseWidget()).to.be.instanceof(BaseWidget);
      });

      it("listens to certain signals", function(done) {
        var W = BaseWidget.extend({
          processResponse: function(response) {
            this.setCurrentQuery(response.getApiQuery());
          }
        });

        var widget = new W();
        _.each(['dispatchRequest', 'customizeQuery', 'composeRequest', 'processResponse'], function(name) {
          sinon.spy(widget, name);
        });

        widget.activate(minsub.beehive.getHardenedInstance());


        minsub.publish(minsub.START_SEARCH, new ApiQuery({q: 'pluto'}));

        expect(widget.dispatchRequest.firstCall.args[0].url()).to.eql('q=pluto');
        expect(widget.customizeQuery.calledOnce).to.be.true;
        expect(widget.customizeQuery.calledBefore(widget.composeRequest)).to.be.true;
        expect(widget.processResponse.called).to.be.true;
        done();

      });

      it("has the activate, getView, and close methods necessary for most/all ui widgets", function(){
        var widget = new BaseWidget();

        expect(widget.activate).to.be.instanceof(Function);
        expect(widget.close).to.be.instanceof(Function);
        expect(widget.getView).to.be.instanceof(Function);
        expect(widget.render).to.be.instanceof(Function);
      });

      it("has methods to indicate change in the widget state", function(){

        var widget = new BaseWidget({view: new ItemView()});

        expect(widget.changeState).to.be.Function;
        expect(widget.widgetStateHandlers).to.be.defined;

        var $w = widget.render().$el;

        $("#test").append($w);

        widget.changeState({state: WidgetStates.WAITING});
        expect($w.find(".s-loading").length).to.eql(1);

        widget.changeState({state: WidgetStates.RESET});
        expect($w.find(".s-loading").length).to.eql(0);

        expect(widget._states.length).to.be.eql(0);

        widget.widgetStateHandlers[WidgetStates.ERRORED].revert = sinon.spy();
        widget.changeState({state: WidgetStates.ERRORED});
        expect($w.hasClass("s-error")).to.be.true;


        widget.changeState({state: WidgetStates.RESET});

        expect(widget.widgetStateHandlers[WidgetStates.ERRORED].revert.called).to.be.true;
        expect(widget._states.length).to.be.eql(0);

      });


    })

  });
