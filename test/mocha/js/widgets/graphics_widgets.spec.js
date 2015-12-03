define([
  'js/widgets/graphics/widget',
  'js/components/json_response',
  'js/components/api_request',
  'js/bugutils/minimal_pubsub'

],function(
  GraphicsWidget,
  JsonResponse,
  ApiRequest,
  MinPubSub

  ){


  describe("Graphics Widget", function(){

    var titleData = {
      "responseHeader": {
        "status": 0,
        "QTime": 1,
        "params": {
          "fl": "title",
          "q": "bibcode:1995ApJ...447L..37W",
          "wt": "json"
        }
      },
      "response": {
        "numFound": 1,
        "start": 0,
        "docs": [
          {
            "title": [
              "Deep Circulation in Red Giant Stars: A Solution to the Carbon and Oxygen Isotope Puzzles?"
            ]
          }
        ]
      }
    };

    var testData = {
      "ADSlink": [],
      "bibcode": "1995ApJ...447L..37W",
      "doi": "10.1086/309555",
      "eprint": false,
      "figures": [
        {
          "figure_caption": "",
          "figure_id": "10_1086_309555_fg1",
          "figure_label": "Figure 1",
          "images": [
            {
              "format": "gif",
              "highres": "http://www.astroexplorer.org/details/10_1086_309555_fg1",
              "image_id": "10_1086_309555_fg1",
              "thumbnail": "https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg1_tb.gif"
            }
          ]
        },
        {
          "figure_caption": "",
          "figure_id": "10_1086_309555_fg2",
          "figure_label": "Figure 2",
          "images": [
            {
              "format": "gif",
              "highres": "http://www.astroexplorer.org/details/10_1086_309555_fg2",
              "image_id": "10_1086_309555_fg2",
              "thumbnail": "https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg2_tb.gif"
            }
          ]
        },
        {
          "figure_caption": "",
          "figure_id": "10_1086_309555_fg3",
          "figure_label": "Figure 3",
          "images": [
            {
              "format": "gif",
              "highres": "http://www.astroexplorer.org/details/10_1086_309555_fg3",
              "image_id": "10_1086_309555_fg3",
              "thumbnail": "https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg3_tb.gif"
            }
          ]
        },
        {
          "figure_caption": "",
          "figure_id": "10_1086_309555_fg4",
          "figure_label": "Figure 4",
          "images": [
            {
              "format": "gif",
              "highres": "http://www.astroexplorer.org/details/10_1086_309555_fg4",
              "image_id": "10_1086_309555_fg4",
              "thumbnail": "https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg4_tb.gif"
            }
          ]
        }
      ],
      "header": "Every image links to the <a href=\"http://www.astroexplorer.org/\" target=\"_new\">IOP \"Astronomy Image Explorer\"</a> for more detail.",
      "id": 9,
      "modtime": null,
      "number": 4,
      "pick": "<a href=\"graphics\" border=0><img src=\"https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg2_tb.gif\"></a>",
      "query": "OK",
      "query_class": null,
      "source": "IOP",
      "widgets": [
        "<div class=\"imageSingle\"><div class=\"image\"><a href=\"http://www.astroexplorer.org/details/10_1086_309555_fg1\" target=\"_new\" border=0><img src=\"https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg1_lr.gif\" width=\"100px\"></a></div><div class=\"footer\">Figure 1</div></div>",
        "<div class=\"imageSingle\"><div class=\"image\"><a href=\"http://www.astroexplorer.org/details/10_1086_309555_fg2\" target=\"_new\" border=0><img src=\"https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg2_lr.gif\" width=\"100px\"></a></div><div class=\"footer\">Figure 2</div></div>",
        "<div class=\"imageSingle\"><div class=\"image\"><a href=\"http://www.astroexplorer.org/details/10_1086_309555_fg3\" target=\"_new\" border=0><img src=\"https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg3_lr.gif\" width=\"100px\"></a></div><div class=\"footer\">Figure 3</div></div>",
        "<div class=\"imageSingle\"><div class=\"image\"><a href=\"http://www.astroexplorer.org/details/10_1086_309555_fg4\" target=\"_new\" border=0><img src=\"https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg4_lr.gif\" width=\"100px\"></a></div><div class=\"footer\">Figure 4</div></div>"
      ]
    };

    afterEach(function(){
      $("#test").empty();
    });

    it("requests graphics data from graphics endpoint and title data from search endpoint, then resolves/rejects deferred for toc widget", function(){

      var g = new GraphicsWidget();

      var apiRequest, graphicsRequest;
      var minsub = new (MinPubSub.extend({
        request: function(request) {
          if (request.get("target") == "search/query"){
            apiRequest = request;
            return titleData
          }
          else {
            graphicsRequest = request;
            return testData;

          }
        }}))({verbose: false});

      g.activate(minsub.beehive.getHardenedInstance());

      var eventSpy = sinon.spy();

      g.on("page-manager-event", eventSpy);

      g.onDisplayDocuments(new minsub.createQuery({q : "bibcode:fakeBibcode"}));

      expect(graphicsRequest.get("target")).to.eql("graphics/fakeBibcode");
      expect(graphicsRequest.get("query").toJSON()).to.eql({})
      expect(apiRequest.get("target")).to.eql("search/query");
      expect(apiRequest.get("query").toJSON().q).to.eql(["bibcode:fakeBibcode"]);


      expect(eventSpy.args[0][0]).to.eql("widget-ready");
      expect(eventSpy.args[0][1].isActive).to.eql(true);
      expect(eventSpy.callCount).to.eql(1);

      expect(g.model.toJSON()).to.eql({
        "graphics": {
          "Figure 1": {
            "format": "gif",
            "highres": "http://www.astroexplorer.org/details/10_1086_309555_fg1",
            "image_id": "10_1086_309555_fg1",
            "thumbnail": "https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg1_tb.gif"
          },
          "Figure 2": {
            "format": "gif",
            "highres": "http://www.astroexplorer.org/details/10_1086_309555_fg2",
            "image_id": "10_1086_309555_fg2",
            "thumbnail": "https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg2_tb.gif"
          },
          "Figure 3": {
            "format": "gif",
            "highres": "http://www.astroexplorer.org/details/10_1086_309555_fg3",
            "image_id": "10_1086_309555_fg3",
            "thumbnail": "https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg3_tb.gif"
          },
          "Figure 4": {
            "format": "gif",
            "highres": "http://www.astroexplorer.org/details/10_1086_309555_fg4",
            "image_id": "10_1086_309555_fg4",
            "thumbnail": "https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg4_tb.gif"
          }
        },
        "title": "Deep Circulation in Red Giant Stars: A Solution to the Carbon and Oxygen Isotope Puzzles?",
        "linkSentence": "Every image links to the <a href=\"http://www.astroexplorer.org/\" target=\"_new\">IOP \"Astronomy Image Explorer\"</a> for more detail."
      });


      minsub.request =  function(request) {
        if (request.get("target") == "search/query"){
          apiRequest = request;
          return titleData
        }
        else {
          graphicsRequest = request;
          return {Error: "no info found"};

        }
      };

      g.onDisplayDocuments(new minsub.createQuery({q : "bibcode:fakeBibcode2"}));

      //did not send a "widget ready" event
      expect(eventSpy.callCount).to.eql(1);

      //model has been emptied
      expect(g.model.toJSON()).to.eql({
        "title": "Deep Circulation in Red Giant Stars: A Solution to the Carbon and Oxygen Isotope Puzzles?"
      });


    });

    it("by default renders a grid of images", function(){

      var g = new GraphicsWidget();
      g.processResponse(new JsonResponse(testData));
      $("#test").append(g.view.el);

      expect($(".s-grid-cell").length).to.eql(4);

      expect($(" .s-graphics-grid a:nth-of-type(2)").attr("href")).to.eql('http://www.astroexplorer.org/details/10_1086_309555_fg2');
      expect($(".s-grid-cell figcaption:first").text()).to.eql('\n                Figure 1\n            ');



    });

    it("can also have a variant that renders a single thumbnail and, when clicked, triggers a nav event to the main widget", function(){

      var g = new GraphicsWidget({sidebar : true});
      g.processResponse(new JsonResponse(testData))
      $("#test").append(g.view.el);

      var minsub = new (MinPubSub.extend({
        request: function(request) {
          if (request.get("target") == "search/query"){
            apiRequest = request;
            return titleData
          }
          else {
            graphicsRequest = request;
            return testData;

          }
        }}))({verbose: false});

      g.activate(minsub.beehive.getHardenedInstance());

      expect($(".s-graphics-sidebar").html()).to.eql('\n\n<div class="s-right-col-widget-container">\n\n    <div class="graphics-container s-graphics-container">\n\n        <h4 class="s-right-col-widget-title">Graphics</h4>\n\n        <div class="s-grid-cell">\n\n        <img src="https://s3.amazonaws.com/aasie/images/1538-4357/447/1/L37/10_1086_309555_fg1_tb.gif" alt="figure from paper">\n\n        <p><i>Click to view more</i></p>\n\n          </div>\n\n    </div>\n</div>\n\n \n\n\n');

      var spy = sinon.spy();
      g.getPubSub = function() {return {publish : spy, NAVIGATE: minsub.NAVIGATE}};

      $(".graphics-container").click();

      expect(spy.args[0][0]).to.eql("[Router]-Navigate-With-Trigger");
      expect(spy.args[0][1]).to.eql("ShowGraphics");


    })







  })



})