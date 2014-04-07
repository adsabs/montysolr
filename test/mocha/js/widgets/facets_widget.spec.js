define(['js/widgets/facets/facets-widget', 'js/widgets/facets/test', 'js/components/api_response', 'js/components/beehive', 'js/services/pubsub', 'js/widgets/facets/facets-config'],
  function(FacetController, test, ApiResponse, Beehive, Pubsub, config) {

    describe("Facets (UI Widget)", function() {
      var createWidget = function() {

        var beehive = new Beehive();
        var pubsub = new Pubsub();
        var key = pubsub.getPubSubKey();

        beehive.addService("PubSub", pubsub);

        var f = function(request) {
          var a = new ApiResponse(test);
          a.setApiQuery(request.get("query"))
          console.log(a)
          pubsub.publish(key, pubsub.DELIVERING_RESPONSE, a)
        };

        var f2 = sinon.spy(function(apiQuery) {
          pubsub.publish(key, pubsub.INVITING_REQUEST, apiQuery);

        })

        pubsub.subscribe(key, pubsub.NEW_QUERY, f2);

        pubsub.subscribe(key, pubsub.DELIVERING_REQUEST, f);

        this.facets = new FacetController();

        this.facets.activate(beehive.getHardenedInstance());

      };

      var destroyWidget = function() {
        delete this.facets;
        $("#test").empty();

      };

      beforeEach(createWidget);
      afterEach(destroyWidget);


      it("should be subscribed to two events: pubsub.INVITING_REQUEST and pubsub.DELIVERING_RESPONSE");

      it("should be a Marionette Controller with a Marionette CompositeView and a Backbone Collection", function() {

        expect(this.facets).to.be.instanceof(Backbone.Marionette.Controller);
        expect(this.facets.returnView()).to.be.instanceof(Backbone.Marionette.CompositeView);
        expect(this.facets.collection).to.be.instanceof(Backbone.Collection)

      });

      it("should render facets from the specifications in the config file", function() {
        var renderedItemsLength = this.facets.returnView().render().$el.find(".facet-item-view").length;
        expect(config.length).to.equal(this.facets.collection.models.length).to.equal(renderedItemsLength);

      })

      it("should react to facet selection by user by showing logic dropdown", function(done) {
        $("#test").append(this.facets.returnView().render().el);
        //highlighting an item
        var firstFacetDiv = $(".facet-item-view").eq(0);
        firstFacetDiv.find("label").eq(0).trigger("click");
        //expect logic tooltip to appear
        expect(firstFacetDiv.find(".logic-container").length).to.equal(1)
        done();

      })

      it("should react to user click on the 'apply' button (thus filtering query) by triggering a NEW_QUERY event")

      it("should render a value change ")




    });

  });
