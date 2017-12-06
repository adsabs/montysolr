
define([
    'marionette',
    'backbone',
    'js/widgets/base/base_widget',
    'js/components/api_response',
    'js/components/api_request',
    'js/components/api_query',
    'js/bugutils/minimal_pubsub',
    'js/widgets/sort/widget',
    'js/components/api_feedback'

    ],
  function(Marionette, Backbone,
           BaseWidget,
           ApiResponse,
           ApiRequest,
           ApiQuery,
           MinimalPubSub,
           SortWidget,
           ApiFeedback
  ) {

    describe("Sort Widget (sort_widget.spec.js)", function(){

      afterEach(function() {
        $("#test").empty()
      });

      it("should render the correct current sort value", function(){

        var w = new SortWidget();
        $("#test").append(w.render().el);

        var minsub = new (MinimalPubSub.extend({
          request: function(apiRequest) {
            return {}
        }}))({verbose: false});

        w.activate(minsub.beehive.getHardenedInstance());

        var fakeFeedback = {
          code : ApiFeedback.CODES.SEARCH_CYCLE_STARTED,
          query : new ApiQuery({
            "q":"star",
            "sort": "read_count desc, bibcode desc"
          })
        }

        //default
        expect($("#test").find("#sort-select").val()).to.eql("author_count desc");

        minsub.publish(minsub.FEEDBACK, fakeFeedback);

        expect($("#test").find("#sort-select").val()).to.eql("read_count desc");

        var fakeFeedback = {
          code : ApiFeedback.CODES.SEARCH_CYCLE_STARTED,
          query : new ApiQuery({
            "q":"star",
            "sort": "classic_factor asc"
          })
        }

        minsub.publish(minsub.FEEDBACK, fakeFeedback);

        expect($("#test").find("#sort-select").val()).to.eql("classic_factor asc");


      });

      it("should call start_search when user makes a sort change", function(){

        var w = new SortWidget();
        $("#test").append(w.render().el);

        var minsub = new (MinimalPubSub.extend({
          request: function(apiRequest) {
            return {}
          }}))({verbose: false});

        w.activate(minsub.beehive.getHardenedInstance());

        w.getPubSub().publish = sinon.spy();

        var fakeFeedback = {
          code : ApiFeedback.CODES.SEARCH_CYCLE_STARTED,
          query : new ApiQuery({
            "q":"star",
            "sort": "read_count desc, bibcode desc"
          })
        }

        minsub.publish(minsub.FEEDBACK, fakeFeedback);

        $("#sort-select").val("date desc");
        $("#sort-select").trigger("change");


        expect(JSON.stringify(w.getPubSub().publish.args[0])).to.eql(JSON.stringify([
          "[PubSub]-New-Query",
          {
            "q": [
              "star"
            ],
            "sort": [
              "date desc"
            ]
          }
        ]));

      });

    });

  });