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
    'js/modules/orcid/orcid_api_constants',
    'hbs!./templates/container-template',
    'js/mixins/papers_utils',
    'js/components/api_query',
    'js/components/json_response',
    'js/modules/orcid/orcid_result_row_extension/extension'
  ],

  function (
    _,
    ListOfThingsWidget,
    BaseWidget,
    PaginationMixin,
    LinkGenerator,
    Formatter,
    OrcidApiConstants,
    ContainerTemplate,
    PapersUtilsMixin,
    ApiQuery,
    JsonResponse,
    OrcidResultRowExtension
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
        this.orcidModelNotifier = beehive.Services.get('OrcidModelNotifier');
        this.beehive = beehive;
        _.bindAll(this, 'processResponse');
        //this.pubsub.subscribe(this.pubsub.DELIVERING_RESPONSE, this.processResponse);

        this.pubsub.subscribe(this.pubsub.ORCID_ANNOUNCEMENT, _.bind(this.routeOrcidPubSub, this));

        if (this.activateResultsExtension)
        {
          this.activateResultsExtension(beehive);
        } // also current this is passed

      },

      routeOrcidPubSub: function (msg) {

        switch (msg.msgType) {
          case OrcidApiConstants.Events.UserProfileRefreshed:
          case OrcidApiConstants.Events.LoginSuccess:
            // flush the views
            this.reset();

            this.processResponse(new JsonResponse(msg.data['orcid-activities']['orcid-works']['orcid-work']));
            break;
          case OrcidApiConstants.Events.LoginRequested:
            //this.showLoading();
            break;
          case OrcidApiConstants.Events.LoginCancelled:
          case OrcidApiConstants.Events.SignOut:
            this.reset();
            break;
        }
      },


      /**
       * Must return list of documents (json structs)
       * @param jsonResponse
       */
      extractDocs: function(orcidWorks) {

        var works = [];

        var that = this;

        _.each(orcidWorks.attributes, function (work) {

          //var publicationData = work['publication-date'] != undefined ? work['publication-date']['year'] : "";
          var workTitle = work['work-title'] != undefined ? work['work-title']['title'] : "";
          //var workSourceUri = work['work-source'] != undefined ? work['work-source']['uri'] : "";
          //var workSourceHost = work['work-source'] != undefined ? work['work-source']['host'] : "";
          var contributors = "";

          if (work['work-contributors']) {
            if (work['work-contributors']['contributor']) {
              var contributors = work['work-contributors']['contributor'];
              contributors = Array.isArray(contributors) ? contributors : [contributors];
              contributors = contributors.map(function(item) {
                return item["credit-name"]._;
              });

              var maxContributors = 3;

              var extraContributors = 0;
              var shownContributors = "";
              if (contributors.length > maxContributors) {
                extraContributors = contributors.length - maxContributors;
                shownContributors = contributors.slice(0, maxContributors);
              }
              else {
                shownContributors = contributors;
              }

            }
          }

          var item = {
            //putCode: work['$']['put-code'],
            //publicationData: publicationData,
            workExternalIdentifiers: [],
            title: workTitle,
            //workType: work['work-type'],
            //workSourceUri: workSourceUri,
            //workSourceHost: workSourceHost,
            shownContributors: shownContributors,
            extraContributors: extraContributors
          };

          works.push(item);

          var addExternalIdentifier = function (workIdentifierNode) {

            if (workIdentifierNode) {

              var identifier = {
                id: workIdentifierNode['work-external-identifier-id'],
                type: workIdentifierNode['work-external-identifier-type']
              };
              item.workExternalIdentifiers.push(identifier);
            }
          };

          var workExternalIdentifiers = work['work-external-identifiers'];
          if (workExternalIdentifiers) {
            var workIdentifierNode = workExternalIdentifiers['work-external-identifier'];

            if (workIdentifierNode instanceof Array) {
              _.each(workIdentifierNode, addExternalIdentifier)
            }
            else {
              addExternalIdentifier(workIdentifierNode);
            }
          }

          item.isFromAds = that.orcidModelNotifier.isOrcidItemAdsItem(item);

          if (item.isFromAds){
            var bibcodes = item.workExternalIdentifiers.filter(function(e){
              return e.type == 'bibcode';
            });
            if (bibcodes.length > 0){
              item.identifier = bibcodes[0].id;
            }

            var adsIds = item.workExternalIdentifiers.filter(function(e){
              return e.type == 'other-id' && e.id.indexOf('ads:') == 0;
            })

            item.id = adsIds[0].id.replace('ads:','');

          }
        });

        works = works.sort(function(a, b){return a.isFromAds - b.isFromAds;}).reverse();

        return works;
      },

      processDocs: function(jsonResponse, docs, paginationInfo) {

        var start = 0;
        var docs = PaginationMixin.addPaginationToDocs(docs, start);
        var self = this;

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

    _.extend(ResultsWidget.prototype, OrcidResultRowExtension);

    return ResultsWidget;

  });
