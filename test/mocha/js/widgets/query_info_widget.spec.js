define([
  "js/widgets/query_info/query_info_widget",
  "js/bugutils/minimal_pubsub",
  "js/components/app_storage"
], function(
  QueryInfo,
  MinSub,
  AppStorage
  ){

  describe("Query Info Widget", function(){

    afterEach(function(){

      $("#test").empty();

    });


    it("should show key information about the query", function(){

      var w = new QueryInfo();

      var minsub = new (MinSub.extend({
      request: function(apiRequest) {
        return {some: 'foo'}
        }
      }))({verbose: false});

      w.activate(minsub.beehive.getHardenedInstance());

      var response = new minsub.T.RESPONSE({"responseHeader": {
          "params": {
          }
        },
          "response": {
            "numFound": 841359
          }
        }
      );
      response.setApiQuery(new minsub.T.QUERY({q: "foo", "fq" : "a filter"}));

      minsub.publish(minsub.DELIVERING_RESPONSE, response);

      $("#test").append(w.render().el);

      expect($("#test").find(".num-found").text()).to.eql('841,359 search results\n');
      expect($("#test").find(".active-filters").text().trim()).to.eql('Active Filters:\n             \n                show' );

      $(".show-filter").click();

      expect($("#test").find(".active-filters").text().trim()).to.eql('Active Filters:\n             \n                \n                    \n                        a filter\n                    \n                \n                hide' );


    });

    it("should listen to updates from app_storage about selected papers, and allow user to clear app storage", function(){

      var w = new QueryInfo();

      var minsub = new (MinSub.extend({
        request: function(apiRequest) {
          return {some: 'foo'}
        }
      }))({verbose: false});

      var s =   new AppStorage();

      s.clearSelectedPapers = sinon.spy();

      minsub.beehive.addObject("AppStorage", s)

      w.activate(minsub.beehive.getHardenedInstance());

      $("#test").append(w.render().el);

      expect($(".currently-selected").text().trim()).to.eql('0 currently selected\n        \n      select all on this page');

      minsub.publish(minsub.STORAGE_PAPER_UPDATE, 10);

      expect($(".currently-selected").text().trim()).to.eql('10 currently selected\n        \n             x clear all\n        \n      select all on this page');

      expect(s.clearSelectedPapers.callCount).to.eql(0);

      $("#test").find(".clear-selected").click();

      expect(s.clearSelectedPapers.callCount).to.eql(1);


    });


  });


});