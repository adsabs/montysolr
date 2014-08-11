define(['js/page_managers/abstract_page_controller',
        'js/bugutils/minimal_pubsub', 'js/widgets/abstract/widget',
      'js/components/history_manager', 'js/widgets/base/base_widget'
], function(AbstractPageController, MinimalPubSub, AbstractWidget,  bumblebeeHistory, BaseWidget){

  var widget, nav;


  before(function(){
   nav = sinon.stub(AbstractPageController.prototype, "subPageNavigate");
  });


  beforeEach(function(){

    var a  = new AbstractWidget();

    widget = new AbstractPageController({widgetDict : {abstract : a },
     abstractSubViews : {"abstract": {widget: a, title:"Abstract", descriptor: "Abstract for:"}},
      history : new bumblebeeHistory()});

  });

  afterEach(function(){

    $("#test").empty();
  });



describe("Abstract Page Controller (Page Manager)", function(){


  it("should inherit from BaseWidget and have a showPage function that renders the page if it is not already rendered", function(){

    expect(widget).to.be.instanceOf(BaseWidget)
    expect(widget.showPage).to.be.instanceof(Function);

  })


  it("should have a title view that allows users to page through a list of results/if viewing a bibcode from that set of results", function(){

     expect(widget.titleView).to.be.instanceof(Backbone.View);

    widget.collection.reset([ {
      bibcode: "test1",
        title: "testtitle1",
        originalSearchResult : true
    },
      {
        bibcode: "test2",
        title: "testtitle2",
        originalSearchResult : true
      }]);

    widget.setCurrentBibcode("test1");

    widget.titleView.render();

    expect(widget.titleView.$(".abstract-paginator-next").length).to.eql(1);
    expect(widget.titleView.$(".abstract-paginator-next").attr("href")).to.eql("/abs/test2");

    expect(widget.titleView.$(".abstract-paginator-prev").length).to.eql(0);

    widget.collection.reset([ {
      bibcode: "test1",
      title: "testtitle1",
      originalSearchResult : false
    },
      {
        bibcode: "test2",
        title: "testtitle2",
        originalSearchResult : true
      }]);

    widget.setCurrentBibcode("test1");

    widget.titleView.render();
    expect(widget.titleView.$(".abstract-paginator-next").length).to.eql(0);
    expect(widget.titleView.$(".abstract-paginator-prev").length).to.eql(0);


  })

  it("should have a nav view that allows the user to toggle between sub views", function(){

    var  navigateMethod;

    widget.setCurrentBibcode("test1");

    $("#test").append(widget.navView.render("abstract").el)

    navigateMethod = sinon.stub(widget.navView, "emitNavigateEvent");

    expect($("#abstract").length).to.eql(1);
    expect($("#abstract").hasClass("s-abstract-nav-active")).to.be.true;
    expect($("#abstract").hasClass("s-abstract-nav-inactive")).to.be.false;

    $("#abstract").click();

    expect(navigateMethod.callCount).to.eql(0);

    $("#references").click();

    expect(navigateMethod.callCount).to.eql(1);

    expect($("#references").hasClass("s-abstract-nav-active")).to.be.true;
    expect($("#references").hasClass("s-abstract-nav-inactive")).to.be.true;
    expect($("#abstract").hasClass("s-abstract-nav-active")).to.be.false;



  })

  it("should listen to navigate events from the nav view and show the correct sub view, triggering NAVIGATE_WITHOUT_TRIGGER on pubsub", function(){

    widget.navView.trigger("navigate");

    expect(nav.callCount).to.eql(1);

    $("#test").append($("<div></div>", {id: "s-middle-col-container"}));

    widget.pubsub = {publish : function(){}, NAVIGATE_WITHOUT_TRIGGER: "navigate_without_trigger"};

    var publish = sinon.spy(widget.pubsub, "publish")

    widget.showAbstractSubView("abstract");

    expect(publish.calledWith("navigate_without_trigger")).to.be.true;

    expect($("#test").find("#abstract-metadata").length).to.eql(1);



  })







})




})