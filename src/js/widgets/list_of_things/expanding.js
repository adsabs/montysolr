/**
 * This widget can paginate through the list of 'things' - it accepts a query
 * as an input. It listens to:
 *
 *    INVITING_REQUEST
 *    DELIVERING_RESPONSE
 *
 * If you need to, change the activate() method to listen to other signals
 * or stop listening altogether
 *
 */

define([
    'marionette',
    'backbone',
    'js/components/api_request',
    'js/components/api_query',
    'js/widgets/base/paginated_base_widget',
    'hbs!./templates/expanding-item-template',
    'hbs!./templates/expanding-results-container-template',
    'js/mixins/widget_pagination'],

  function (Marionette,
            Backbone,
            ApiRequest,
            ApiQuery,
            PaginatedBaseWidget,
            ItemTemplate,
            ResultsContainerTemplate,
            WidgetPagination) {

    var ItemModel = Backbone.Model.extend({
      defaults: function () {
        return {
          abstract: undefined,
          title: undefined,
          authorAff: undefined,
          pub: undefined,
          pubdate: undefined,
          keywords: undefined,
          bibcode: undefined,
          pub_raw: undefined,
          doi: undefined,
          details: undefined
        }
      },

      parse: function (doc) {
        // do some processing here
        return doc;
      }
    });

    var ListCollection = Backbone.Collection.extend({
      model: ItemModel
    });

    var ItemView = Marionette.ItemView.extend({

      //should it be hidden initially?
      className: function () {
        if (Marionette.getOption(this, "hide") === true) {
          return "hide row results-item"
        } else {
          return "row results-item"
        }
      },

      template: ItemTemplate,

      /**
       * This method prepares data for consumption by the template
       *
       * @returns {*}
       */
      serializeData: function () {
        var data = this.model.toJSON();
        return data;
      },

      events: {
        'change input[name=identifier]': 'toggleSelect'
      },

      toggleSelect: function () {
        this.$el.toggleClass("chosen");
      }

    });

    var ListView = Marionette.CompositeView.extend({

      initialize: function (options) {
        this.displayNum = options.displayNum;
        this.paginator = options.paginator;
      },


      className: "list-of-things",
      itemView: ItemView,

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
        "click .load-more-results": "fetchMore",
        "click .show-details": "showDetails"
      },

      template: ResultsContainerTemplate,

      fetchMore: function (ev) {
        if (ev)
          ev.stopPropagation();
        this.trigger('fetchMore', this.$(".results-item").filter(".hide").length);
      },

      /**
       * Displays loaded, but hidden items
       *
       * @param howMany
       */
      displayMore: function(howMany) {
        this.$(".results-item").filter(".hide").slice(0, howMany).removeClass("hide");
      },

      /**
       * Hides the area where 'show more' button lives; this is needed for
       * the pagination widged
       *
       * @param text
       */
      disableShowMore: function(text) {
        this.$('.load-more:first').addClass('hide');
      },

      /**
       * Displays the area where 'show more' button lives; this is needed for
       * the pagination widged
       *
       * @param text
       */
      enableShowMore: function(text) {
        this.$('.load-more:first').removeClass('hide');
      },

      /**
       * Un/Hides the area where details controls are
       *
       * @param text
       */
      toggleDetailsControls: function(visible){
        if (visible) {
          this.$(".results-controls:first").removeClass('hide');
        }
        else {
          this.$(".results-controls:first").addClass('hide');
        }

      },

      /**
       * Displays the are inside of every item-view
       * with details (this place is normally hidden
       * by default)
       */
      showDetails: function (ev) {
        if (ev)
          ev.stopPropagation();
        this.$(".more-info").toggleClass("hide");
        if (this.$(".more-info").hasClass("hide")) {
          this.$(".show-details").text("Show details");
        } else {
          this.$(".show-details").text("Hide details");
        }
      }

    });


    var ResultsWidget = PaginatedBaseWidget.extend({


      ItemModelClass: ItemModel,
      ItemViewClass: ItemView,
      CollectionClass: ListCollection,
      CollectionViewClass: ListView,

      initialize: function (options) {
        options.rows = options.rows || 40;

        PaginatedBaseWidget.prototype.initialize.call(this, options);


        this.collection = new this.CollectionClass();

        this.displayNum = this.displayNum || options.displayNum || 20;

        this.maxDisplayNum = this.maxDisplayNum || options.maxDisplayNum || 300;

        this.view = new this.CollectionViewClass({
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
        fl: 'title,abstract,bibcode,author,keyword,id,citation_count,pub,aff,email,volume,year'
      },

      dispatchRequest: function (apiQuery) {

        // by default we consider every request to be new one - unless it is clear
        // that we are paginating
        if (this.resetPagination) {
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
          this.collection.reset(this.parseResponse(apiResponse, 1), {
            parse: true
          });
          this.paginator.setMaxNum(apiResponse.get('response.numFound'));
          if (this.paginator.maxNum > this.displayNum) {
            this.view.enableShowMore();
          }
          else {
            this.view.disableShowMore();
          }
        } else {
          //it's in response to "load more"
          this.collection.add(this.parseResponse(apiResponse, this.collection.models.length+1), {
            parse: true
          })
        }
      },

      onAllInternalEvents: function(ev, arg1, arg2) {

        //console.log(ev);

        if (ev == 'composite:rendered') {
          this.view.disableShowMore();
          this.view.toggleDetailsControls(false);
        }
        else if (ev == 'reset') {
          var details = _.filter(this.view.collection.models, function(m) {return m.has('details')});
          if (details.length > 0) {
            this.view.toggleDetailsControls(true);
          }
          else {
            this.view.toggleDetailsControls(false);
          }

        }
        else if (ev == "fetchMore") {

          var p = this.handlePagination(this.displayNum, this.maxDisplayNum, arg1, this.paginator, this.view, this.collection);
          if (p && p.before) {
            p.before();
          }
          if (p && p.runQuery) {
            // ask for more data
            this.resetPagination = false;
            this.dispatchRequest(this.getCurrentQuery());
          }
        }
      },

      /**
       * This functions takes apiResponse and returns an array of objects
       * that will be passed to the collection. Model can do additional
       * parsing on it. See {ItemModel}
       *
       * If you specified defaultQueryArguments['fl'] only these values
       * will be returned.
       *
       * Each object will also contain 'orderNum' key; to indicate its
       * position in the collection
       *
       * @param apiResponse
       * @returns {*}
       */
      parseResponse: function (apiResponse, orderNum) {
        var raw = apiResponse.toJSON();
        orderNum = orderNum || 1;

        if (!this.defaultQueryArguments.fl) {
          return _.map(raw.response.docs, function(d) {orderNum+=1; d['orderNum'] = orderNum; return d});
        }

        var keys = _.map(this.defaultQueryArguments.fl.split(','), function(v) {return v.trim()});


        var docs = _.map(raw.response.docs, function (d) {
          orderNum += 1;
          var vals = _.pick(d, keys);
          vals[orderNum] = orderNum;
          return vals;
        });

        return docs;
      }

    });

    // add mixins
    _.extend(ResultsWidget.prototype, WidgetPagination);


    return ResultsWidget;

  });