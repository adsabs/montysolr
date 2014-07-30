define(['js/page_managers/abstract_page_controller',
        'js/bugutils/minimal_pubsub', 'js/widgets/abstract/widget',
        'js/widgets/references/widget', 'js/components/history_manager'
], function(AbstractPageController, MinimalPubSub, AbstractWidget,
  ReferencesWidget, bumbleBeeHistory){

  var widget;

  beforeEach(function(){


   widget = new AbstractController({widgetDict :
    {abstract : new AbstractWidget(),
      references : new ReferencesWidget(),
    },
      history : bumblebeeHistory});

  })



describe("Abstract Page Controller (Page Manager)", function(){


  it("should have a showPage function that renders the page if it is not already rendered", function(){

  })


  it("should render a nav view that allows the user to view different widgets", function(){


  })

  it("should have a pubsub-enabled title view that shows some summary information for the abstract page", function(){


  })







})




})