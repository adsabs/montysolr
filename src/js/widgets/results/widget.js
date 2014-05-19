/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 * 
 */

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

      //this function takes the output of apiResponse.toJSON() and builds individual models for the collection.
      parse: function (raw) {
        var that = this;
        var docs = raw.response.docs;
        var highlights = raw.highlighting;

        docs = _.map(docs, function (d) {
          var id = d.id;
          var h = {};

          if (highlights) {
            h = (function () {

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
          };

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
              return d;
            } else {
              return d + ";";
            }
          })
        }
        return data
      },

      events: {
        'change input[name=bibcode]': 'toggleSelect'
      },

      toggleSelect: function () {
       this.$el.toggleClass("chosen")
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
        if (this.paginator.getCycle() <= 1 && (index < this.displayNum)) {
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
        "click #load-more-results": "fetchMore",
        "click #show-results-snippets": "showDetails"
      },

      template: ResultsContainerTemplate,

      fetchMore: function () {
        this.trigger('fetchMore', this.$(".results-item").filter(".hide").length);
      },

      displayMore: function(howMany) {
        //show hidden data
        this.$(".results-item").filter(".hide").slice(0, howMany).removeClass("hide");
      },

      //XXX:alex - we need something better, I have no idea how to render templates
      // and if jquery modifications are OK
      disableLoadMore: function(text) {
        this.$('.load-more').hide();
      },

      enableLoadMore: function(text) {
        this.$('.load-more').show();
      },

      toggleDetailsButton : function(visible){
        if (visible) {
          this.$("#show-results-snippets").removeClass('hide');
        }
        else {
          this.$("#show-results-snippets").addClass('hide');
        }

      },

      showDetails: function () {
        this.$(".more-info").toggleClass("hide");
        if (this.$(".more-info").hasClass("hide")) {
          this.$("#show-results-snippets").text("show details");
        } else {
          this.$("#show-results-snippets").text("hide details");
        }
      }

    });


    var ResultsWidget = PaginatedBaseWidget.extend({


      initialize: function (options) {
        options.rows = options.rows || 40;

        PaginatedBaseWidget.prototype.initialize.call(this, options);

        this.collection = new ListCollection();

        this.displayNum = this.displayNum || options.displayNum || 20;

        this.maxDisplayNum = this.maxDisplayNum || options.maxDisplayNum || 300;

        this.view = new ResultsListView({
          collection: this.collection,
          //so it has reference to the pagination object
          paginator: this.paginator,
          displayNum: this.displayNum || this.paginator.rows / 2
        });
        this.listenTo(this.view, "all", this.onAllInternalEvents);
        this.listenTo(this.view.collection, "all", this.onAllInternalEvents);

        this.resetPagination = true;
      },


      //will be requested in composeRequest
      defaultQueryArguments: {
        hl: "true",
        "hl.fl": "title,abstract",
        fl: 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,email,volume,year'
      },

      dispatchRequest: function (apiQuery) {

        // by default we consider every request to be new one - unless it is clear
        if (this.resetPagination) {
          this.collection.orderNum = 1;
          this.paginator.reset();
        }
        else {
          this.resetPagination = true;
        }

        PaginatedBaseWidget.prototype.dispatchRequest.call(this, apiQuery)
      },

      processResponse: function (apiResponse) {

        this.setCurrentQuery(apiResponse.getApiQuery());

        if (this.paginator.getCycle() <= 1) {
          //it's the first set of results
          this.collection.reset(apiResponse.toJSON(), {
            parse: true
          });
          this.paginator.setMaxNum(apiResponse.get('response.numFound'));
          if (this.paginator.maxNum > this.displayNum) {
            this.view.enableLoadMore();
          }
          else {
            this.view.disableLoadMore();
          }
        } else {
          //it's in response to "load more"
          this.collection.add(apiResponse.toJSON(), {
            parse: true
          })
        }
      },

      _adjustMaxDisplay: function(currentLen, toDisplay) {
        var allowedMax = this.maxDisplayNum-currentLen;
        if (allowedMax < toDisplay) {
          return allowedMax;
        }
        return toDisplay;
      },

      onAllInternalEvents: function(ev, arg1, arg2) {

        console.log(ev);

        if (ev == 'composite:rendered') {
          this.view.disableLoadMore();
        }
        else if (ev == 'reset') {
          console.log('inside reset');
          if (this.collection.models.length > 0) {
            this.view.toggleDetailsButton(true);
          }
          else {
            this.view.toggleDetailsButton(false);
          }

        }
        else if (ev == "fetchMore") {

          if (this.paginator.hasMore()) {

            // sanity check - there is a maximum that we'll allow to display
            // even if we may load slightly more
            var realDisplayLength = this.collection.models.length - arg1;

            if (realDisplayLength >= this.maxDisplayNum) {
              this.view.disableLoadMore("Reached max " + this.maxDisplayNum);
              return;
            }


            if (arg1 > 0) { // we have some docs (hidden)
              var toDisplay = this.displayNum;
              if (arg1 > this.displayNum) { // if it is more then necessary, we just display them
                this.view.displayMore(this._adjustMaxDisplay(realDisplayLength, toDisplay));
                return;
              }
              else {
                var cachedDisplay = this._adjustMaxDisplay(realDisplayLength, this.paginator.rows - this.displayNum);
                this.view.displayMore(cachedDisplay); // display one part from the hidden items
                realDisplayLength += cachedDisplay;
                toDisplay = this._adjustMaxDisplay(realDisplayLength, toDisplay - (this.paginator.rows - this.displayNum));
              }
            }


            //console.log('toDisplay', toDisplay);

            if (toDisplay > 0) {

              // we'll wait 50 mills after the first item was added to the collection
              // and then show the remaining items
              this.view.once('after:item:added', function() {
                var self = this;
                setTimeout(function(){
                  //console.log('DisplayMore', toDisplay);
                  self.view.displayMore(toDisplay)}, 50)},
                this);
            }

            if (toDisplay + realDisplayLength >= this.maxDisplayNum) {
              this.view.disableLoadMore("Reached max " + this.maxDisplayNum);
            }

            // ask for more data
            this.resetPagination = false;
            this.dispatchRequest(this.getCurrentQuery());

          }
          else {
            this.view.displayMore(arg1);
            this.view.disableLoadMore();
          }
        }
      }

    });

    return ResultsWidget;

  });
