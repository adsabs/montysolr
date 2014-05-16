define(['marionette', 'backbone', 'js/components/api_request', 'js/components/api_query',
    'js/widgets/base/paginated_base_widget', 'hbs!./templates/item-template',
    'hbs!./templates/results-container-template'],

  function (Marionette, Backbone, ApiRequest, ApiQuery, PaginatedBaseWidget, ItemTemplate, ResultsContainerTemplate) {

    var ItemModel = Backbone.Model.extend({

    });

    var ListCollection = Backbone.Collection.extend({

      model: ItemModel,

      //visual representation
      orderNum: 1,

      //this function takes the output of apiResponse.toJSON() and builds individual models for 
      //the collection.
      parse: function (raw) {
        var that = this;
        var docs = raw.response.docs;
        var highlights = raw.highlighting;

        docs = _.map(docs, function (d) {
          var id = d.id;
          var h = (function () {

            var hl = highlights[id];
            var finalList = [];
            //adding abstract,title, etc highlights to one big list
            _.each(hl, function (val, key) {
              finalList.push(val);
            });
            finalList = _.flatten(finalList);

            return {
              "highlights": finalList
            }
          }());

          var m = _.extend(d, h, {
            orderNum: that.orderNum
          });
          that.orderNum++;
          return m

        });

        return docs;
      }

    });

    var ResultsItemView = Marionette.ItemView.extend({


      //should it be hidden initially?
      className: function () {
        if (Marionette.getOption(this, "hide") === true) {
          return "hide row results-item"
        } else {
          return "row results-item"
        }
      },

      template: ItemTemplate,

      serializeData: function () {
        var data = this.model.toJSON();
        var shownAuthors;

        if (data.author && data.author.length > 3) {
          data.extraAuthors = data.author.length - 3;
          shownAuthors = data.author.splice(0, 3);
        } else if (data.author) {
          shownAuthors = data.author
        }

        if (data.author) {
          data.authorFormatted = _.map(shownAuthors, function (d, i) {
            if (i === shownAuthors.length - 1) {
              //last one
              return d
            } else {
              return d + ";"
            }
          })
        }
        return data
      },

      events: {
        'click .view-more': 'toggleExtraInfo',
        'change input[name=bibcode]': 'toggleSelect'
      },

      toggleSelect: function (e) {
        if (this.$el.hasClass("chosen")) {
          this.$el.removeClass("chosen")

        } else {
          this.$el.addClass("chosen")
        }

      },

      toggleExtraInfo: function (e) {
        e.preventDefault();
        this.$(".more-info").toggleClass("hide");
        if (this.$(".more-info").hasClass("hide")) {
          this.$(".view-more").text("more info")
        } else {
          this.$(".view-more").text("hide info")
        }
      }

    });

    var ResultsListView = Marionette.CompositeView.extend({

      initialize: function (options) {
        this.displayNum = options.displayNum;
        this.paginator = options.paginator;
      },

      id: "search-results",
      itemView: ResultsItemView,

      itemViewOptions: function (model, index) {
        //if this is the initial round, hide fetchnum - displaynum
        if (this.paginator.isInitial() && (index < this.displayNum)) {
          return {}
        }
        else {
          //otherwise, hide everything
          return {
            hide: true
          }
        }
      },

      itemViewContainer: ".results-list",
      events: {
        "click .load-more button": "fetchMore"
      },
      template: ResultsContainerTemplate,

      fetchMore: function () {
        //prefetch if only on cycle left
        if (this.$(".results-item").filter(".hide").length === this.displayNum) {
          this.trigger("fetchMore")
        }
        //show hidden data
        this.$(".results-item").filter(".hide").slice(0, 20).removeClass("hide");
      }

    });


    var ResultsListController = PaginatedBaseWidget.extend({


      initialize: function (options) {
        options.rows = options.rows || 40;

        PaginatedBaseWidget.prototype.initialize.call(this, options)

        this.collection = new ListCollection();

        this.displayNum = Marionette.getOption(this, "displayNum", 20)
        this.view = new ResultsListView({
          collection: this.collection,
          //so it has reference to the pagination object
          paginator: this.paginator,
          displayNum: this.displayNum || this.paginator.pagination.rows / 2
        });
        this.listenTo(this.view, "fetchMore", this.dispatchFollowUpRequest);

      },

      //will be requested in composeRequest
      defaultQueryArguments: {
        hl: "true",
        "hl.fl": "title,abstract"
      },

      dispatchRequest: function (apiQuery) {
        console.log("I got a response")

        //resetting collection's knowledge of pagination
        this.collection.orderNum = 1;

        PaginatedBaseWidget.prototype.dispatchRequest.call(this, apiQuery)

      },

      processResponse: function (apiResponse) {

        if (this.paginator.isInitial()) {
          //it's the first set of results
          this.collection.reset(apiResponse.toJSON(), {
            parse: true
          })

        } else {
          //it's in response to "load more"
          this.collection.add(apiResponse.toJSON(), {
            parse: true
          })
        }
      }
    });

    return ResultsListController;

  });
