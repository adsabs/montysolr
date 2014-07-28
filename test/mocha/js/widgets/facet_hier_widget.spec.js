/**
 * So I got fed up with all the variations in the hierarchical facet widget;
 * and clicking....
 *
 * This widget here constitues one complete test with different stages of
 * the query
 *
 * TODO: paging is wrong (last item; new query)
 * TODO: when it reaches last item; it starts to show first level again
 * TODO: optimization (set number of levels to bother asking for data)
 *
 */
define([
    'underscore',
    'backbone',
    'js/widgets/facet/factory',
    'js/bugutils/minimal_pubsub'],
  function(
    _,
    Backbone,
    FacetFactory,
    MinimalPubSub
    ) {


    describe("Hierarchical Facet", function() {

      var minsub, data;
      beforeEach(function(done) {
        data = [];
        minsub = new (MinimalPubSub.extend({
          request: function(apiRequest) {
            var q = apiRequest.get('query');
            var o = data.shift();
            expect(q.get('facet.offset').toString()).to.eql(o.responseHeader.params["facet.offset"]);
            expect(q.get('facet.limit').toString()).to.eql(o.responseHeader.params["facet.limit"]);
            expect(q.get('facet.prefix').toString()).to.eql(o.responseHeader.params["facet.prefix"]);
            return o;
          }
        }))({verbose: false});
        done();
      });

      /*
      afterEach(function() {
        minsub.close();
        $('#test-area').empty();
        done();
      });
*/

      it("knows to handle nested data", function(done) {

        var widget = FacetFactory.makeHierarchicalCheckboxFacet({
          facetField: "author_facet_hier",
          facetTitle: "Authors",
          openByDefault: true,
          logicOptions: {single: ['limit to', 'exclude'], 'multiple': ['and', 'or', 'exclude']},
          responseProcessors: [
            function(v) {var vv = v.split('/'); return vv[vv.length-1]}
          ]
        });
        widget.activate(minsub.beehive.getHardenedInstance());

        var $w = $(widget.render().el);
        $('#test-area').append($w);
        window.$w = $w;


        // q=planet facet.prefix=0/
        data.push({"responseHeader":{"status":0,"QTime":163,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"0/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":["0/Head, J",1815,"0/Russell, C",1374,"0/Solomon, S",1079,"0/Smith, D",782,"0/Larson, S",756,"0/Greeley, R",755,"0/Wang, Y",751,"0/Neukum, G",731,"0/Johnson, J",724,"0/Bell, J",715,"0/Li, J",695,"0/Henning, T",691,"0/Brown, R",689,"0/Pieters, C",659,"0/Walker, R",659,"0/Zuber, M",654,"0/Williams, R",653,"0/Taylor, G",635,"0/Lunine, J",626,"0/Smith, M",624]},"facet_dates":{},"facet_ranges":{}}});

        // five items were displayed ==> 5 queries to discover nested level

        data.push({"responseHeader":{"status":0,"QTime":246,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Head, J/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":["1/Head, J/Head, J W",1132,"1/Head, J/Head, James W",297,"1/Head, J/Head, J W, III",276,"1/Head, J/Head, J",44,"1/Head, J/Head, James W, III",35,"1/Head, J/Head, J N",14,"1/Head, J/Head, James",12,"1/Head, J/Head, James N",2,"1/Head, J/Head, J, III",1,"1/Head, J/Head, James, III",1,"1/Head, J/Head, Jim W, III",1]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Russell, C/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":237,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Solomon, S/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":["1/Solomon, S/Solomon, S C",606,"1/Solomon, S/Solomon, Sean C",309,"1/Solomon, S/Solomon, S",71,"1/Solomon, S/Solomon, Susan",40,"1/Solomon, S/Solomon, Stanley C",38,"1/Solomon, S/Solomon, Sorin",5,"1/Solomon, S/Solomon, Sean",3,"1/Solomon, S/Solomon, S J",2,"1/Solomon, S/Solomon, Stan",2,"1/Solomon, S/Solomon, S M",1,"1/Solomon, S/Solomon, Semere",1,"1/Solomon, S/Solomon, Stanley",1]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":167,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Smith, D/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":["1/Smith, D/Smith, D E",283,"1/Smith, D/Smith, D",80,"1/Smith, D/Smith, David E",80,"1/Smith, D/Smith, Deborah K",29,"1/Smith, D/Smith, D M",27,"1/Smith, D/Smith, D F",24,"1/Smith, D/Smith, D R",24,"1/Smith, D/Smith, D A",23,"1/Smith, D/Smith, David",17,"1/Smith, D/Smith, Dean F",15,"1/Smith, D/Smith, D B",12,"1/Smith, D/Smith, D W",12,"1/Smith, D/Smith, Douglas",12,"1/Smith, D/Smith, Dale W",10,"1/Smith, D/Smith, D L",9,"1/Smith, D/Smith, D J",8,"1/Smith, D/Smith, David C",8,"1/Smith, D/Smith, D H",7,"1/Smith, D/Smith, D G W",6,"1/Smith, D/Smith, D K",6]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":232,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Larson, S/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":["1/Larson, S/Larson, S M",631,"1/Larson, S/Larson, S",76,"1/Larson, S/Larson, Stephen M",19,"1/Larson, S/Larson, S A",11,"1/Larson, S/Larson, Shane L",8,"1/Larson, S/Larson, Stephen",6,"1/Larson, S/Larson, Sven Ake",5,"1/Larson, S/Larson, Steve",3,"1/Larson, S/Larson, S L",2,"1/Larson, S/Larson, Susan M",2,"1/Larson, S/Larson, Sarah",1,"1/Larson, S/Larson, Stefan M",1,"1/Larson, S/Larson, Steven",1]},"facet_dates":{},"facet_ranges":{}}});



        // issue a new query
        minsub.publish(minsub.START_SEARCH, minsub.createQuery({'q': 'planet'}));


        expect($w.find('.widget-body').children().not('.hide').length).to.be.eql(5);
        expect($w.find('.widget-body').children().filter('.hide').length).to.be.eql(15);


        // there should be 4 carets pointing right; and 1 circle (no nested items)

        expect($w.find('.widget-body').children().slice(0,5).children('.item-caret.item-end').not('.hide').length).to.be.equal(1);
        expect($w.find('.widget-body').children().slice(0,5).children('.item-caret.item-closed').not('.hide').length).to.be.equal(4);

        // click on the 'Show More' for the Solomon, S
        // 5 more items are added to the view - and their queries fired
        // the current query at this point is: facet.prefix=1/Solomon, S facet.offset=10 facet.rows=10
        data.push({"responseHeader":{"status":0,"QTime":143,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, S C/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":158,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, Sean C/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":161,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, S/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":161,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, Susan/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":160,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, Stanley C/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});


        // click on the 3rd item: 5 items should be displayed automatically
        $w.find('.widget-body').children().eq(2).children('.item-caret').click();
        expect($w.find('.widget-body').children().eq(2).find('.item-children:first').children().not('.hide').length).to.be.eql(5);
        expect($w.find('.widget-body').children().eq(2).find('.item-children:first').children().filter('.hide').length).to.be.eql(7);


        // clicking next will askk for more data
        data.push({"responseHeader":{"status":0,"QTime":143,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, Sorin/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":158,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, Sean/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":161,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, S J/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":161,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, Stan/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":160,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, S M/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});


        $w.find('.widget-body').children().eq(2).children('.item-body').children('.show-more').click();

        expect(data.length).to.be.equal(0);
        expect($w.find('.widget-body').children().eq(2).find('.item-children:first').children().not('.hide').length).to.be.eql(10);
        expect($w.find('.widget-body').children().eq(2).find('.item-children:first').children().filter('.hide').length).to.be.eql(2);




        // click again on ShowMore - only two more items are there
        // two nested queries fire
        data.push({"responseHeader":{"status":0,"QTime":144,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, Semere/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":144,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"2/Solomon, S/Solomon, Stanley/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});

        // also the query to discover if there are more items on the root level
        data.push({"responseHeader":{"status":0,"QTime":159,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"20","q":"planet","facet.limit":"20","facet.prefix":"0/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});


        $w.find('.widget-body').children().eq(2).children('.item-body').children('.show-more').click();

        expect(data.length).to.be.equal(0);
        expect($w.find('.widget-body').children().eq(2).find('.item-children:first').children().not('.hide').length).to.be.eql(12);
        expect($w.find('.widget-body').children().eq(2).find('.item-children:first').children().filter('.hide').length).to.be.eql(0);

        // there is nothing; the 'ShowMore' button disappears
        expect($w.find('.widget-body').children().eq(2).children('.item-body').children('.show-more').hasClass('hide')).to.be.true;


        // all 12 items on the 2nd level show there is nothing beneath them
        expect($w.find('.widget-body').children().eq(2).find('.item-children:first').children().not('.hide').children('.item-end').length).to.be.eql(12);

        // the top level is pointing downwardds
        expect($w.find('.widget-body').children().eq(2).children('.item-caret').hasClass('.item-open'));

        // clicking 'Solomon, S' collapses the section
        $w.find('.widget-body').children().eq(2).children('.item-caret').click();
        expect($w.find('.widget-body').children().eq(2).children('.item-caret').hasClass('.item-closed'));



        // clicking on 'ShowMore' of the container, shows another
        // 5...
        // 5...
        // 5... items and issues...new query
        // q=planet facet.prefix=0/ facet.offset=20
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Greeley, R/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Wang, Y/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Neukum, G/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Johnson, J/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Bell, J/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        $w.find('.s-widget .widget-options').children('a[target="ShowMore"]').click();
        expect($w.find('.widget-body').children().not('.hide').length).to.be.eql(10);
        expect($w.find('.widget-body').children().filter('.hide').length).to.be.eql(10);

        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Li, J/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Henning, T/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Brown, R/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Pieters, C/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Walker, R/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        $w.find('.s-widget .widget-options').children('a[target="ShowMore"]').click();
        expect($w.find('.widget-body').children().not('.hide').length).to.be.eql(15);
        expect($w.find('.widget-body').children().filter('.hide').length).to.be.eql(5);

        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Zuber, M/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Williams, R/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Taylor, G/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Lunine, J/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});
        data.push({"responseHeader":{"status":0,"QTime":155,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"0","q":"planet","facet.limit":"20","facet.prefix":"1/Smith, M/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});


        data.push({"responseHeader":{"status":0,"QTime":163,"params":{"facet":"true","fl":"id","facet.mincount":"1","facet.offset":"20","q":"planet","facet.limit":"20","facet.prefix":"0/","facet.field":"author_facet_hier","wt":"json"}},"response":{"numFound":549528,"start":0,"docs":[{"id":"6738335"},{"id":"7148914"},{"id":"4005969"},{"id":"2319574"},{"id":"2319588"},{"id":"2803214"},{"id":"5198582"},{"id":"4008026"},{"id":"1019984"},{"id":"8751391"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"author_facet_hier":[]},"facet_dates":{},"facet_ranges":{}}});

        $w.find('.s-widget .widget-options').children('a[target="ShowMore"]').click();
        expect($w.find('.widget-body').children().not('.hide').length).to.be.eql(20);
        expect($w.find('.widget-body').children().filter('.hide').length).to.be.eql(0);


        expect($w.find('.s-widget .widget-options').children('a[target="ShowMore"]').hasClass('hide')).to.be.true;


        done();
      });



    });

  });