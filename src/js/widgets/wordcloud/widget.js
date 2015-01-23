define([
  'jquery',
  'backbone',
  'marionette',
  'd3',
  'js/components/api_request',
  'js/widgets/base/base_widget',
  'hbs!./templates/wordcloud-template',
  'hbs!./templates/selected-list-template',
  'js/components/api_targets',
  'jquery-ui',
  'bootstrap',
  'd3-cloud'
], function ($,
             Backbone,
             Marionette,
             d3,
             ApiRequest,
             BaseWidget,
             WordCloudTemplate,
             SelectedListTemplate,
             ApiTargets
  ) {


  var helpText = "<p>This word cloud allows you to view interesting words from the titles and abstracts of your search results.</p>"+
    "<p> Move the slider towards <strong> Frequent</strong> to view a word cloud that is simply composed of the words that appeared most"+
    " frequently in your results. (This word cloud is likely to feature generic terms like 'observations' prominently.) </p> <p>Move the slider towards" +
    " <strong>Unique</strong> to see a word cloud that shows words that appeared in your results but which appear very rarely in the rest of the ADS corpus.</p>" +
    "<p>To facet your ADS search, select words from the word cloud and click the 'Apply Filter to Search' button.</p>";


  var ListModel = Backbone.Model.extend({

    initialize : function(){
      this.on("selected", function(name){this.add(name)})
      this.on("unselected", function(name){this.remove(name)})
    },

    add: function(word){
      this.get("selectedWords").push(word)
      this.trigger("userChange:selectedWords", word)

    },

    remove: function(word){
      var word = word.trim();
      var l = _.without(this.get("selectedWords"), word);
      this.set("selectedWords", l)
      this.trigger("userChange:selectedWords", word)
    },

    reset : function(){

      this.set("selectedWords", []);

    },

    defaults: {

      selectedWords: []

    }

  });

  var ListView = Backbone.View.extend({

    initialize : function(options){
      options = options || {};
      this.model = new ListModel();

      this.listenTo(this.model, "userChange:selectedWords", this.render);

    },

    template: SelectedListTemplate,

    render : function(){
      this.$el.html(this.template(this.model.toJSON()));

      return this;

    },

    events : {
      "click .close" : "removeWord",
      "click .apply-vis-facet" :  "submitFacet"
    },

    removeWord :function(e){

      var word = $(e.target).parent().find(".selected-word").text();
      this.model.remove(word)
    },

    submitFacet : function(){
      //only trigger event if there is at least one word selected
      if (this.model.get("selectedWords").length){
        this.trigger("submitFacet");
      }
    }

  });

  var WordCloudView = Marionette.ItemView.extend({

    className: "s-wordcloud-widget",

    events: {
      "click .close-widget": "signalCloseWidget"
    },

    initialize: function (options) {

      options = options || {};

      this.listView = options.listView;

      this.listenTo(this.model, "change:processedWordList", this.onProcessedWordChange);
      this.listenTo(this.listView.model, "userChange:selectedWords", this.toggleHighlight);

    },

    signalCloseWidget: function() {
      this.trigger('close-widget');
    },

    toggleHighlight : function(word){

      var w = word.trim();
      d3.selectAll(this.$("text")).filter(function(){
        if (this.textContent.trim() == w) {
          var d3This =  d3.select(this);
          d3This.classed("selected", !d3This.classed("selected"));
        }
      });
    },

    template: WordCloudTemplate,

    render: function() {

      this.$el.html(WordCloudTemplate({helpText : helpText}));

      this.listView.setElement(this.$(".selected-word-list")).render();

      //add popover listener

      this.$(".icon-help").popover({trigger: "hover", placement: "right", html: true});

      return this
    },

    onProcessedWordChange: function () {

      //prevents word cloud from drawing if no information
      if (this.model.get("processedWordList").length > 0){

        //remove loading view if it's there
        if (this.$(".s-loading").length){

          this.render();

        }

        this.buildSlider();
        this.buildCloudLayout();

      }

    },

    buildCloudLayout : function(){

      d3.layout.cloud()
        .size([1000, 600])
        .words(this.model.get("processedWordList"))
        .padding(3)
        .rotate(function() { return 0})
        .font("Arial")
        .fontSize(function(d) { return d.size; })
        .on("end", _.bind(this.draw, this))
        .start();

    },

    buildSlider: function () {

      var that = this;

      this.$("#word-choice").slider({
        value: that.model.get("currentSliderVal"),
        min  : 1,
        max  : 5,
        step : 1,
        slide: function (event, ui) {
          that.model.set({currentSliderVal: ui.value})
        }
      })
    },


    draw: function () {

      var that = this;

      var renderVals = this.model.get("renderVals");
      var height = 600;
      var width = 1000;

      var svg = d3.select(this.$("#wordcloud-svg")[0]);

      var words = this.model.get("processedWordList");

      //set up code, only runs the first time

      if (!this.$("#words-group").length){
        var g = svg
          .append("g")
          .attr('id', 'words-group')
          .attr("width", width)
          .attr("height", height)
          .attr("transform", function()
          {
            return "translate(" + width/2 + " " + height/2 + ")"
          });


        //using event delegation for click events
        //this seems to reduce time spent rendering
        svg[0][0].addEventListener("click", function(e){

          var classList = e.target.classList;

          //only interested in wordcloud text elements

          if (!classList.contains("s-wordcloud-text")){
            return
          }
          if (!classList.contains("selected")){
            that.listView.model.trigger("selected", e.target.textContent);
          }
          else {
            that.listView.model.trigger("unselected", e.target.textContent);
          }

        })

      }
      else {
        g = d3.select(this.$("#wordcloud-svg")[0]).select("#words-group")
      }

      var text =  g.selectAll("text")
        .data(words, function (d) {
          return d.text;
        });

      var selectedWords = this.listView.model.get("selectedWords");

      //enter selection
      text.enter()
        .append("text")
        .classed("s-wordcloud-text", true)
        .text(function(d) { return d.text; })
        .style("fill", function(d, i) {

          return  renderVals.fill(d.origSize);
        })

        //has this element been selected?
        .classed("selected", function(d,i){
          if (_.contains(selectedWords, d.text)){

            return true
          }
        })
        .attr("transform", function(d, i) {
          //split into 4 groups to come from 4 diff directions
          if (i < 15) {
            return "translate(" + [Math.random()*width, - height/2] + ")";
          }
          else if (i< 30)
          {
            return "translate(" + [Math.random()* width, + height/2 ] + ")";
          }
          else if ( i < 45)
          {
            return "translate(" + [- width/2, +Math.random() * height] + ")";
          }
          else
          {
            return "translate(" + [width/2, -Math.random()* height] + ")";
          }
        })

      //exit selection
      text
        .exit()
        .style("opacity", 0)
        .remove();

      var that = this;

      // update selection
      text
        .style("font-size", function(d) {return d.size})
        .style("fill", function(d, i) {return renderVals.fill(d.origSize);})
        .transition()
        .duration(1000)
        .attr("transform", function(d)
        {
          return "translate(" + [d.x, d.y] + ")";
        });
    }

  });

  var WordCloudModel = Backbone.Model.extend({

    initialize : function(){
      this.on("change:tfidfData", this.buildWCDict);
      this.on("change:currentSliderVal", this.buildWCDict)

    },

    defaults: {
      //raw data
      tfidfData   : undefined,
      currentSliderVal : 3,
      //this is what the view uses to render a cloud
      processedWordList : [],

      renderVals : {
        fill : undefined,
        glowScale : undefined,
        blur : undefined

      },
      //is this the right place to put this?
      colorRange: ["#80E6FF", "#7575FF", "#7575FF", "#47008F"],
      sliderRange: {'1':[1,0], '2':[.75, .25], '3':[.5,.5], '4':[.25,.75], '5':[0,1]}
    },

    reset: function () {
      this.set(this.defaults, {silent : true});
    },

    buildWCDict: function () {

      var dict, numWords, meanTF, meanIDF, wordDict, min, max;
      var sliderRange, currentSliderVal;
      var freq, idf, modifiedVal, wordList, renderVals;

      dict = this.get("tfidfData");

      numWords = _.size(dict);

      meanTF = _.reduce(_.map(_.values(dict), function (x) {
        return x['total_occurrences']
      }), function (m, n) {
        return m + n
      }, 0) / numWords;

      meanIDF = _.reduce(_.map(_.values(dict), function (x) {
        if (x['idf']) {
          return x['idf']
        }
        else {
          return 0
        }
      }), function (m, n) {
        return m + n
      }, 0) / numWords;


      sliderRange = this.get("sliderRange");
      currentSliderVal = this.get("currentSliderVal")

      wordDict = _.map(dict, function (val, key) {

        freq = val['total_occurrences'] / meanTF;
        idf = val['idf'] / meanIDF

        modifiedVal = sliderRange[currentSliderVal][0] * idf + sliderRange[currentSliderVal][1] * freq;
        // some stuff might be NaN, so do || 0
        return [key, modifiedVal || 0]
      });

      // sort to get 50 top candidates
      wordDict = _.last(_.sortBy(wordDict, function (l) {
        return l[1]
      }), 50);

      wordDict = _.object(wordDict);
      min = _.min(_.values(wordDict));
      max = _.max(_.values(wordDict));

      wordList = [];

      renderVals = {};

      renderVals.fill = d3.scale.log().domain([min, max]);
      renderVals.fill.domain([0, .25, 0.5, 0.75, 1].map(renderVals.fill.invert))
        .range(this.get("colorRange")).clamp(true);

      var pixelScale = d3.scale.log().domain([min, max]).range([30, 70]);

      for (var entry in wordDict) {
        wordList.push({text: entry, size: pixelScale(wordDict[entry]), select: false, origSize: wordDict[entry]})
      }

      this.set("renderVals", renderVals);
      this.set("processedWordList", wordList);

    }

  });


  var WordCloudWidget = BaseWidget.extend({

    initialize: function (options) {
      options = options || {};
      this.listView = new ListView();
      this.model = new WordCloudModel();
      this.view = new WordCloudView({model: this.model, listView : this.listView});
      this.max_rows = options.max_rows || 150;
      this.on("all", this.onAllInternalEvents);
      this.listenTo(this.listView, "all", this.onAllInternalEvents);
      this.listenTo(this.view, "close-widget", _.bind(this.closeWidget, this));
    },

    activate: function (beehive) {

      _.bindAll(this, "setCurrentQuery", "processResponse");

      this.pubsub = beehive.Services.get('PubSub');

      //custom dispatchRequest function goes here
      this.pubsub.subscribe(this.pubsub.INVITING_REQUEST, this.setCurrentQuery);

      //custom handleResponse function goes here
      this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);

    },

    //fetch data
    onShow: function () {

      var request = new ApiRequest({

        target: Marionette.getOption(this, "endpoint") || ApiTargets.SERVICE_WORDCLOUD,
        query: this.customizeQuery(this.getCurrentQuery())
      });

      this.pubsub.publish(this.pubsub.DELIVERING_REQUEST, request);

    },

    customizeQuery: function (apiQuery) {
      var q = apiQuery.clone();
      q.unlock();

      if (this.defaultQueryArguments) {
        q = this.composeQuery(this.defaultQueryArguments, q);
      }
      var r = this.max_rows;
      if (q.has('rows')) {
        r = q.get('rows')[0];
      }
      q.set('rows', Math.min(r, this.max_rows));
      return q;
    },

    processResponse: function (data) {

      data = data.toJSON();

      this.model.set("tfidfData", data);

    },

    close : function(){

      this.listView.close();
      this.view.close();
      Marionette.Controller.prototype.close.apply(this, arguments);
    },

    setCurrentQuery: function () {
      //make sure to reset the model's processed word list
      this.listView.model.reset();

      //resetting model
      this.model.reset();

      BaseWidget.prototype.setCurrentQuery.apply(this, arguments);
    },

    onAllInternalEvents : function(ev) {
      if (ev === "submitFacet"){
        var filterList, q, newQ;

        filterList = this.listView.model.get("selectedWords");
        q = this.getCurrentQuery();
        q = q.clone();
        q.unlock();

        newQ =  q.get("q") + " AND (\"" + filterList.join("\" OR \"") + "\")";
        q.set("q", newQ);

        this.pubsub.publish(this.pubsub.START_SEARCH, q);
      }
    },

    closeWidget: function () {
      this.pubsub.publish(this.pubsub.NAVIGATE, "results-page");
    }

  });

  return WordCloudWidget

});

