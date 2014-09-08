define([
    'jquery',
  'backbone',
  'd3',
  'js/widgets/base/base_widget',
  'hbs!./templates/wordcloud-template',
  'hbs!./templates/selected-list-template',
  'jquery-ui',
  'd3-cloud'

  ], function ($,
  Backbone,
  d3,
  BaseWidget,
  WordCloudTemplate,
  SelectedListTemplate) {


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

          return this

        },

       events : {
         "click .x-remove" : "removeWord",
         "click .apply-vis-facet" :  "submitFacet"
       },

        removeWord :function(e){

          var word = $(e.target).parent().text();
          this.model.remove(word)
        },

        submitFacet : function(){

          this.trigger("submitFacet");

        }

      });

      var WordCloudView = Backbone.View.extend({

        initialize: function (options) {

          options = options || {};

          this.listView = options.listView;

          this.listenTo(this.model, "change:processedWordList", this.onRender);
          this.listenTo(this.listView.model, "userChange:selectedWords", this.toggleHighlight);

          this.on("render", this.onRender);

        },

        toggleHighlight : function(word){

        var wordToHighlight = this.$("text").filter(function(){if (this.textContent.trim() === word.trim()){return true}})[0]

        wordToHighlight.classList.toggle("selected")

        },

        template: WordCloudTemplate,

        render: function () {

          this.$el.html(WordCloudTemplate());

          this.listView.setElement(this.$(".selected-word-list")).render();

          this.trigger("render");

          return this
        },

        onRender: function () {

          //prevents word cloud from drawing if no information
          if (this.model.get("processedWordList").length > 0){

            this.buildSlider();
            this.buildCloudLayout();

          }

        },

        buildCloudLayout : function(){

          this.model.get("renderVals").width = this.$("svg").width() ;
          this.model.get("renderVals").height = this.$("svg").height() ;

          d3.layout.cloud()
            .size([this.model.get("renderVals").width, this.model.get("renderVals").height])
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

          var renderVals = this.model.get("renderVals");
          var height = renderVals.height;
          var width = renderVals.width;

          var svg = d3.select(this.$("#wordcloud-svg")[0]);

          var words = this.model.get("processedWordList");

          if (!this.$("#words-group").length){
           var g = svg
              .append("g")
              .attr('id', 'words-group')
              .attr("width", width)
              .attr("height", height)
              .attr("transform", function()
              {
                return "translate(" + width/2 + " " + height/2 + ")"
              })
          }
          else {
            g = d3.select(this.$("#wordcloud-svg")[0]).select("#words-group")
          }

          var text =  g.selectAll("text")
            .data(words, function (d) {
            return d.text;
          });


          //enter selection
          text.enter()
            .append("text")
            .classed("s-wordcloud-text", true)
            .text(function(d) { return d.text; })
            .style("fill", function(d, i) {return  renderVals.fill(d.origSize);})
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
            .transition()
            .style("opacity", 0)
            //getting weird memory leaks because d3 remove just detaches dom tree???
            .remove();

          var that = this;

          // update selection
          text
            .style("font-size", function(d) {return d.size})
            .style("fill", function(d, i) {return renderVals.fill(d.origSize);})

            .attr("text-anchor", "middle")
            .transition()
            .attr("transform", function(d)
            {
              return "translate(" + [d.x, d.y] + ")";
            })

            text
            .on("click", function(d){
                if (!this.classList.contains("selected")){
                  that.listView.model.trigger("selected", this.textContent);
                }
                else {
                  that.listView.model.trigger("unselected", this.textContent);
                }
            })

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
          width : undefined,
          height: undefined,
          blur : undefined

          },
          //is this the right place to put this?
          colorRange: ["#80E6FF", "#7575FF", "#47008F"],
          sliderRange: {'1':[1,0], '2':[.75, .25], '3':[.5,.5], '4':[.25,.75], '5':[0,1]}
        },

        reset: function () {
          this.set(this.defaults)
        },

        buildWCDict: function () {

          var dict, numWords, meanTF, meanIDF, wordDict, min, max;
          var sliderRange, currentSliderVal;
          var freq, idf, modifiedVal, wordList, renderVals;

          dict = this.get("tfidfData");

          numWords = _.size(dict);

          meanTF = _.reduce(_.map(_.values(dict), function (x) {
              return x['total_occurences']
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

            freq = val['total_occurences'] / meanTF;
            idf = val['idf'] / meanIDF

            modifiedVal = sliderRange[currentSliderVal][0] * idf + sliderRange[currentSliderVal][1] * freq;
            // some stuff might be NaN, so do || 0
            return [key, modifiedVal || 0]
          });

          // sort to get 60 top candidates
          wordDict = _.last(_.sortBy(wordDict, function (l) {
            return l[1]
          }), 60)

          wordDict = _.object(wordDict);
          min = _.min(_.values(wordDict));
          max = _.max(_.values(wordDict));

          wordList = [];

          renderVals = {}

          renderVals.fill = d3.scale.log();
          renderVals.fill.domain([0, .25, 0.5, 0.75, 1].map(renderVals.fill.invert));
          renderVals.fill.range(this.get("colorRange")).clamp(true);

          renderVals.glowScale = d3.scale.log().domain([min, max]).range([1.5, 4]);

          var pixelScale = d3.scale.log().domain([min, max]).range([15, 40]);

          for (var entry in wordDict) {
            wordList.push({text: entry, size: pixelScale(wordDict[entry]), select: false, origSize: wordDict[entry]})
          }

          this.set("renderVals", renderVals)

          this.set("processedWordList", wordList);

        }

      });

      var WordCloudWidget = BaseWidget.extend({

        initialize: function (options) {

          options = options || {};

          this.listView = new ListView();

          this.model = new WordCloudModel();

          this.view = new WordCloudView({model: this.model, listView : this.listView});

          this.on("all", this.onAllInternalEvents)
          this.listenTo(this.listView, "all", this.onAllInternalEvents)
        },

        activate: function (pubsub) {

        },

        dispatchRequest: function () {

          this.model.restoreDefaults();
        },

        processResponse : function() {

        },

        onAllInternalEvents : function(ev, arg1, arg2){

          if (ev === "submitFacet"){

            var filterList = this.listView.model.get("selectedWords");

            var q = this.getCurrentQuery().clone();

            var newQ =  q.get("q") + " AND (" + filterList.join(" OR ") + ")"

            q.set("q", newQ);

              this.pubsub.publish(this.pubsub.START_SEARCH, q);

          }
      }

  });

  return WordCloudWidget

})

