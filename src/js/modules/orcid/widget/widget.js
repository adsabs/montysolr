/**
 * Widget to display list of result hits - it allows to paginate through them
 * and display details
 *
 */

define([
    'underscore',
    'js/widgets/list_of_things/widget',
    'js/widgets/base/base_widget',
    'js/mixins/add_stable_index_to_collection',
    'js/mixins/link_generator_mixin',
    'js/mixins/formatter',
    'hbs!./templates/container-template',
    'js/mixins/papers_utils',
    'js/components/api_query'
  ],

  function (
    _,
    ListOfThingsWidget,
    BaseWidget,
    PaginationMixin,
    LinkGenerator,
    Formatter,
    ContainerTemplate,
    PapersUtilsMixin,
    ApiQuery
    ) {

    var ResultsWidget = ListOfThingsWidget.extend({
      initialize : function(options){
        ListOfThingsWidget.prototype.initialize.apply(this, arguments);
        //now adjusting the List Model
        this.view.template = ContainerTemplate;
        this.view.model.set({"mainResults": true}, {silent : true});
        this.listenTo(this.collection, "reset", this.checkDetails);
      },


      activate: function (beehive) {
        this.pubsub = beehive.Services.get('PubSub');
        this.beehive = beehive;
        _.bindAll(this, 'processResponse');
        this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);
      },


      /**
       * Must return list of documents (json structs)
       * @param jsonResponse
       */
      extractDocs: function(jsonResponse) {
        //  BC:rca - this should check against errors
        return jsonResponse.get('orcid-message.orcid-profile.orcid-activities.orcid-works');
      },

      processDocs: function(jsonResponse, docs, paginationInfo) {
        var start = 0;
        var docs = PaginationMixin.addPaginationToDocs(docs, start);
        var self = this;

        var orcidApi = this.beehive.getService('OrcidApi');

        //any preprocessing before adding the resultsIndex is done here
        docs = _.map(docs, function (d) {
          d.identifier = orcidApi.getADSIdentifier(d.identifier);
          return d;
        });
        return docs;
      },

      getPaginationInfo: function(jsonResponse, docs) {

        // this information is important for calcullation of pages
        var numFound = docs.length;
        var perPage =  this.model.get('perPage') || 10;
        var start = 0;

        // compute the page number of this request
        var page = PaginationMixin.getPageVal(start, perPage);

        // compute which documents should be made visible
        var showRange = [page*perPage, ((page+1)*perPage)-1];

        // compute paginations (to be inserted into navigation)
        var numAround = this.model.get('numAround') || 2;
        var pageData = this._getPageDataDatastruct(new ApiQuery({'orcid': 'author X'}), page, numAround, perPage, numFound);

        //should we show a "back to first page" button?
        var showFirst = (_.pluck(pageData, "p").indexOf(1) !== -1) ? false : true;

        return {
          numFound: numFound,
          perPage: perPage,
          start: start,
          page: page,
          showRange: showRange,
          pageData: pageData,
          currentQuery: new ApiQuery({'orcid': 'author X'})
        }
      }

    });

    return ResultsWidget;

  });
