define([
    'marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/widgets/results/widget',
    'js/components/api_query',
    './test_json/test1',
    './test_json/test2',
    'js/widgets/list_of_things/widget',
    'js/components/api_response',
    'js/components/api_query',
    'js/components/app_storage'
  ],
  function (
    Marionette,
    Backbone,
    MinimalPubsub,
    ResultsWidget,
    ApiQuery,
    Test1,
    Test2,
    ListOfThingsWidget,
    ApiResponse,
    ApiQuery,
    AppStorage
    ) {

    describe("Render Results UI Widget (results_render_widget.spec.js)", function () {

      var minsub;
      beforeEach(function (done) {

        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return Test1();
            } else {
              var ret = Test1();
              ret.response.start = 10;
              return ret;
            }
          }
        }))({verbose: false});
        done();


      });

      afterEach(function (done) {
        minsub.destroy();
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });

      it("returns ResultsWidget object", function (done) {
        expect(new ResultsWidget()).to.be.instanceof(ResultsWidget);
        expect(new ResultsWidget()).to.be.instanceof(ListOfThingsWidget);
        done();
      });

      var _getWidget = function (beforeActivate) {

        var widget = new ResultsWidget();

        var fakeUserObject = {getHardenedInstance : function(){return this},
          isOrcidModeOn : function(){return false},
          getUserData : function(){ return {link_server :  "foo"}},
          getLocalStorage : function(){return { perPage : 50 }}

        };
        var fakeDocStashController = {getHardenedInstance :function(){return this}, stashDocs : sinon.spy()};
        minsub.beehive.addObject("DocStashController", fakeDocStashController );

        minsub.beehive.addObject("User", fakeUserObject);

        // give tests the chance to set up stubs or whatever before activation
        beforeActivate && beforeActivate.call(widget, widget);

        widget.activate(minsub.beehive.getHardenedInstance());
        return widget;
      };

      it("should listen to START_SEARCH and automatically request and render data (with fields augmented by abstract widget)", function (done) {

        /*
          WILDCARD = NO HL.Q
         */

        var widget = _getWidget();
        widget.foox = 1;
        expect(widget.collection.length).to.eql(0);
        expect(widget.getCurrentQuery().toJSON()).to.eql({});
        minsub.publish(minsub.START_SEARCH, new ApiQuery({q: "star isbn:* *:*"}));
        setTimeout(function() {



          expect(widget.model.get('currentQuery').toJSON()).to.eql({
              "q": [
                "star isbn:* *:*"
              ],
              "hl": [
                "true"
              ],
              "hl.fl": [
                "title,abstract,body,ack"
              ],
              "hl.maxAnalyzedChars": [
                "150000"
              ],
              "hl.requireFieldMatch": [
                "true"
              ],
              "hl.usePhraseHighlighter": [
                "true"
              ],
              "fl": [
                "title,abstract,comment,bibcode,author,keyword,id,citation_count,[citations],pub,aff,volume,pubdate,doi,pub_raw,page,links_data,property,email"
              ],
              "rows": [
                25
              ],
              "start": [
                0
              ]
            });

          expect(widget.model.get('currentQuery').url()).to.eql('fl=title%2Cabstract%2Ccomment%2Cbibcode%2Cauthor%2Ckeyword%2Cid%2Ccitation_count%2C%5Bcitations%5D%2Cpub%2Caff%2Cvolume%2Cpubdate%2Cdoi%2Cpub_raw%2Cpage%2Clinks_data%2Cproperty%2Cemail&hl=true&hl.fl=title%2Cabstract%2Cbody%2Cack&hl.maxAnalyzedChars=150000&hl.requireFieldMatch=true&hl.usePhraseHighlighter=true&q=star%20isbn%3A*%20*%3A*&rows=25&start=0');
          expect(widget.collection.length).to.eql(10);
          done();
        }, 50);
      });


      it("should listen to START_SEARCH and automatically request and render data (with fields augmented by abstract widget)", function (done) {

        /*
          NO WILDCARD =  HL.Q
         */

        var widget = _getWidget();
        widget.foox = 1;
        expect(widget.collection.length).to.eql(0);
        expect(widget.getCurrentQuery().toJSON()).to.eql({});
        minsub.publish(minsub.START_SEARCH, new ApiQuery({q: "star"}));
        setTimeout(function() {

          expect(widget.model.get('currentQuery').toJSON()).to.eql({
            "q": [
              "star"
            ],
            "hl": [
              "true"
            ],
            "hl.fl": [
              "title,abstract,body,ack"
            ],
            "hl.maxAnalyzedChars": [
              "150000"
            ],
            "hl.requireFieldMatch": [
              "true"
            ],
            "hl.usePhraseHighlighter": [
              "true"
            ],
            "fl": [
              "title,abstract,comment,bibcode,author,keyword,id,citation_count,[citations],pub,aff,volume,pubdate,doi,pub_raw,page,links_data,property,email"
            ],
            "rows": [
              25
            ],
            "start": [
              0
            ],
            "hl.q": [
              "star"
            ]
          });

          expect(widget.model.get('currentQuery').url()).to.eql('fl=title%2Cabstract%2Ccomment%2Cbibcode%2Cauthor%2Ckeyword%2Cid%2Ccitation_count%2C%5Bcitations%5D%2Cpub%2Caff%2Cvolume%2Cpubdate%2Cdoi%2Cpub_raw%2Cpage%2Clinks_data%2Cproperty%2Cemail&hl=true&hl.fl=title%2Cabstract%2Cbody%2Cack&hl.maxAnalyzedChars=150000&hl.q=star&hl.requireFieldMatch=true&hl.usePhraseHighlighter=true&q=star&rows=25&start=0');
          expect(widget.collection.length).to.eql(10);
          done();
        }, 50);
      });

      it("provides the openurl linkserver info to each model's data for the link_generator_mixin", function(){

        //each model will interact with the link generator mixin, which expectes the link_server param

        var widget = _getWidget();

        minsub.publish(minsub.START_SEARCH, new ApiQuery({q: "star"}));

        expect(widget.collection.pluck("link_server")).to.eql(["foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo"]);

      });

      //TODO: re-enable this test, skipping for now
      it.skip("should join highlights with their records on a model by model basis", function (done) {
        var widget = _getWidget();
        minsub.publish(minsub.START_SEARCH, new ApiQuery({q: "star"}));
        setTimeout(function() {
          expect(widget.collection.findWhere({"recid": 4189917}).get("highlights")[0]).to.eql("External triggers of <em>star</em> formation.");
          done();
        },5);
      });

      it("should show three authors with semicolons in the correct places and, if there are more, show the number of the rest", function (done) {
        var widget = _getWidget();
        minsub.publish(minsub.START_SEARCH, new ApiQuery({q: "star"}));
        setTimeout(function () {
          var authorsString = $('.just-authors', widget.view.$el).last().text().trim().replace(/[\n\s]+\W/g, ' ');
          expect(authorsString).to.equal('Montmerle, T.; Fake Author 1; Fake Author 2 and 3 more');
          done();
        }, 5);
      });

      it.skip("should listen to INVITING_REQUEST event", function (done) {

        var stub = function (apiResponse) {
          // if we don't timeout, and get here then we are good
          //expect(apiResponse).to.not.be.undefined;
          done();
        };
        var widget = _getWidget(function () {
          sinon.stub(this, 'dispatchRequest', stub);
        });

        // get widget to request info
        minsub.publish(minsub.INVITING_REQUEST, new ApiQuery({
          q: "star"
        }));

        widget.dispatchRequest.restore();
      });

      it("should render the show snippets button only if highlights exist given the paginated docs", function () {

        var widget = _getWidget();
        var responseWithHighlights = new ApiResponse({
          "responseHeader": {
            "status": 0,
            "QTime": 11,
            "params": {
              "fl": "id",
              "indent": "true",
              "q": "author:accomazzi,a",
              "hl.simple.pre": "<em>",
              "hl.simple.post": "</em>",
              "wt": "json",
              "hl": "true"}},
          "response": {"numFound": 175, "start": 0, "docs": [
            {
              "id": "10406064"},
            {
              "id": "3513629"},
            {
              "id": "5422941"}
          ]
          },
          "highlighting": {
            "10406064": {"title": "fooblydoo"},
            "3513629": {"abstract": ""}
          }});
        responseWithHighlights.setApiQuery(new ApiQuery({start : 0, rows : 25}));
        widget.processResponse(responseWithHighlights);
        expect(widget.model.get('showHighlights')).to.eql('open');

        var $w = widget.render().$el;
        $('#test').append($w);

        //expect results button;
        expect(widget.view.render().$el.find(".show-highlights").length).to.eql(1);


        widget.model.set('showHighlights', false);
        expect(widget.view.render().$el.find(".show-highlights").length).to.eql(0);


        var responseWithoutHighlights = new ApiResponse({
          "responseHeader": {
            "status": 0,
            "QTime": 11,
            "params": {
              "fl": "id",
              "indent": "true",
              "q": "author:accomazzi,a",
              "hl.simple.pre": "<em>",
              "hl.simple.post": "</em>",
              "wt": "json",
              "hl": "true"}},
          "response": {"numFound": 3, "start": 0, "docs": [
            {
              "id": "10406064"},
            {
              "id": "3513629"},
            {
              "id": "5422941"}
          ]
          },
          "highlighting": {
            "10406064": {"title": ""},
            "3513629": {"abstract": ""}
          }});
        responseWithoutHighlights.setApiQuery(new ApiQuery());

        widget.reset();
        expect(widget.model.get('showHighlights')).to.be.false;
        widget.processResponse(responseWithoutHighlights);
        expect(widget.model.get('showHighlights')).to.be.false;


      });


      it("has a view that displays records for each model in the collection", function(done){

        var widget = new ResultsWidget({perPage: 10});

        var fakeUserObject = {getHardenedInstance : function(){return this},
          isOrcidModeOn : function(){return false},
          getUserData : function(){ return {link_server :  "foo"}},
          getLocalStorage : function(){return { perPage : 50 }}

        };
        minsub.beehive.addObject("User", fakeUserObject);
        var fakeDocStashController = {getHardenedInstance :function(){return this}, stashDocs : sinon.spy()};
        minsub.beehive.addObject("DocStashController", fakeDocStashController );

        widget.activate(minsub.beehive.getHardenedInstance());


        var $w = widget.render().$el;
        $("#test").append($w);

        minsub.publish(minsub.START_SEARCH, new ApiQuery({'q': 'foo:bar'}));

        //now check to make sure it was rendered correctly
        setTimeout(function() {

          //checking first record
          expect($w.find(".s-identifier:first").text().trim()).to.eql("2013arXiv1305.3460H");
          expect($w.find(".s-identifier:first a").attr("href").trim()).to.eql("#abs/2013arXiv1305.3460H/abstract");
          /// expect($w.find(".s-results-links:first").find('div:not(.orcid-actions)').find("a").text().trim()).to.eql("arXiv eprint"); // without .orcid-actions
          expect($w.find("h3:first").text().trim()).to.eql("A bijection for tri-cellular maps");
          expect($w.find(".article-author:first").text().trim()).to.eql("Han, Hillary S. W.;");

          //checking last record
          expect($w.find(".s-identifier:last").text().trim()).to.eql("1987sbge.proc...47M");
          expect($w.find(".s-identifier:last a").attr("href").trim()).to.eql("#abs/1987sbge.proc...47M/abstract");
          /// expect($w.find(".s-results-links:last").find('div:not(.orcid-actions)').find("a").text().trim()).to.eql("Table of Contents"); // without .orcid-actions
          expect($w.find("h3:last").text().trim()).to.eql("Diffuse high-energy radiation from regions of massive star formation.");

          //checking render order of more than 3 authors
          expect($w.find(".just-authors:last").text().replace(/\s+/g, '')).to.eql("Montmerle,T.;FakeAuthor1;FakeAuthor2and3more");
          done();
        }, 5);
      });


      it("should have the num formatter", function() {
        var widget  = _getWidget();
        expect(widget.formatNum(889899)).to.be.eql('889,899');
      });


      it("should mark papers as selected", function() {
        var s = new AppStorage();
        s.activate(minsub.beehive);
        minsub.beehive.addObject('AppStorage', s);

        var widget  = _getWidget();
        var $w = widget.render().$el;

        s.addSelectedPapers('2013arXiv1305.3460H');
        minsub.publish(minsub.START_SEARCH, new ApiQuery({'q': 'foo:bar'}));

        var $w = widget.render().$el;
        $("#test").append($w);

        // select a paper and observe it gets into the storage
        expect(s.isPaperSelected('1993sfgi.conf..324C')).to.eql(false);
        $w.find('input[value="1993sfgi.conf..324C"]').click();
        expect(s.isPaperSelected('1993sfgi.conf..324C')).to.eql(true);



      });
    })
  });
