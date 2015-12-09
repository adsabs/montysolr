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
    'js/components/api_query',
    'js/components/json_response',
    'hbs!./templates/empty-template',
    'js/modules/orcid/extension'
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
    ApiQuery,
    JsonResponse,
    EmptyViewTemplate,
    OrcidExtension
    ) {

    var ResultsWidget = ListOfThingsWidget.extend({
      initialize : function(options){
        ListOfThingsWidget.prototype.initialize.apply(this, arguments);

        //now adjusting the List Model
        this.view.getEmptyView = function () {
          return Marionette.ItemView.extend({
            template: EmptyViewTemplate
          });
        };

        _.extend(this.view.events, {
          "click .search-author-name" : function(){
            var searchTerm = "author:\"" + this.model.get("orcidLastName") + "," + this.model.get("orcidFirstName") + "\"";
            this.trigger("search-author-name", searchTerm);
          }
        });

        this.view.delegateEvents();

        this.view.template = ContainerTemplate;
        this.view.model.set({"mainResults": true}, {silent : true});
        this.listenTo(this.collection, "reset", this.checkDetails);
        this.listenTo(this.view, "search-author-name", function(searchTerm){
         var pubsub = this.getPubSub(), query = new ApiQuery({q : searchTerm});
          pubsub.publish(pubsub.START_SEARCH, query);
        })

        this.on('orcid-update-finished', this.mergeDuplicateRecords);
      },

      orcidWidget : true,

      activate: function (beehive) {
        this.setBeeHive(beehive);

        _.bindAll(this, 'processResponse');
        this.on('orcidAction:delete', function(model) {
          this.collection.remove(model);
        });
      },


      /**
       * Go through all the recs and remove the ones that are duplicates
       * (we'll keep the ADS version, and indicate the provenance of the
       * removed record)
       *
       * This func is called after the resolution of the bibcodes, so you
       * can expect to have a canonical bibcode in the 'identifier' field
       */
      mergeDuplicateRecords: function(docs) {


        var dmap = {};
        var id, dupsFound, c = 0;
        if (docs) {
          _.each(docs, function(doc) {
            doc._dupIdx = c++;
            id = doc.identifier;
            if (id) {
              id = id.toLowerCase();
              if (dmap[id]) {
                dmap[id].push(doc);
                dupsFound = true;
              }
              else {
                dmap[id] = [doc];
              }
            }
          });
        }
        else {
          this.hiddenCollection.each(function(model) {
            id = model.get('identifier');
            model.attributes._dupIdx = c++;
            if (id) {
              id = id.toLowerCase();
              if (dmap[id]) {
                dmap[id].push(model.attributes);
                dupsFound = true;
              }
              else {
                dmap[id] = [model.attributes];
              }
            }
          });
        }

        if (dupsFound) {
          var toRemove = {}, toUpdate = [];

          _.each(dmap, function(value, key) {
            if (value.length > 1) {

              // decide which record is ours (or pick the first one)
              var toPick = 0;
              _.each(value, function(doc, idx) {
                if (doc['source_name'] && doc['source_name'].toLowerCase() == 'nasa ads') {
                  toPick = idx;
                }
              });

              // update 'provenance' field in the picked record
              var authoritativeRecord = value[toPick];
              value.splice(toPick, 1);
              toUpdate.push(authoritativeRecord._dupIdx);
              authoritativeRecord['source_name'] = authoritativeRecord['source_name'] || '';

              _.each(value, function(doc) {
                if (doc['source_name'])
                  authoritativeRecord['source_name'] += '; ' + doc['source_name'];
                toRemove[doc._dupIdx] = true;
              });

            }
          });

          var recomputeIndexes = function(models) {
            var i = 0;
            _.each(models, function(m) {
              m.resultsIndex = i++;
              m.indexToShow = i;
            });
          };


          if (docs) { // we are updating the data before they get displayed
            toRemove = _.keys(toRemove);
            toRemove.sort(function(a, b){return b-a}); // reverse order
            _.each(toRemove, function(idx) {
              docs.splice(idx, 1); // will be wasty for large collections
            });
            recomputeIndexes(docs);
          }
          else { // we are updating the collection (it was already displayed
            _.each(toUpdate, function(idx) {
              var model = this.hiddenCollection.models[idx]; // force re-paint
              model.set('source_name', model.attributes['source_name'], {silent: true});
            });
            var newModels = [];
            this.hiddenCollection.each(function(model) {
              if (!toRemove[model.attributes._dupIdx])
                newModels.push(model.attributes);
            });
            recomputeIndexes(newModels);
            this.hiddenCollection.reset(newModels);
            this.updatePagination({numFound: newModels.length});
          }
        }

      },

      processDocs: function(jsonResponse, docs, paginationInfo) {
        var start = 0;
        var docs = PaginationMixin.addPaginationToDocs(docs, start);
        _.each(docs, function(d,i){
          docs[i] = PapersUtilsMixin.prepareDocForViewing(d);
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
        var pageData = this._getPageDataDatastruct(jsonResponse.getApiQuery() || new ApiQuery({'orcid': 'author X'}),
          page, numAround, perPage, numFound);

        //should we show a "back to first page" button?
        var showFirst = (_.pluck(pageData, "p").indexOf(1) !== -1) ? false : true;

        return {
          numFound: numFound,
          perPage: perPage,
          start: start,
          page: page,
          showRange: showRange,
          pageData: pageData,
          currentQuery: jsonResponse.getApiQuery() || new ApiQuery({'orcid': 'author X'})
        }
      },

      onShow: function() {
        var oApi = this.getBeeHive().getService('OrcidApi');
        var self = this;
        if (oApi) {
          console.log('onShow');

        if (!oApi.hasAccess())
          return;

        oApi.getOrcidProfileInAdsFormat()
          .done(function(data) {
            var response = new JsonResponse(data);
            var params = response.get('responseHeader.params');

            // name/surname can be empty
            params = _.reduce(_.pairs(params), function(p, v) {
              if (v[1])
                p[v[0]] = v[1];
              return p;
            }, {});

            response.setApiQuery(new ApiQuery(params));
            self.processResponse(response);
            self.model.set({orcidUserName : params.firstName + " " + params.lastName,
                            orcidFirstName : params.firstName,
                            orcidLastName : params.lastName
            });
          });
        }
      },

      /**
       * function to update what we are displaying; it always works with the existing
       * models - does not fetch new data
       *
       * @param sortBy
       * @param filterBy
       *  - allowed values are: 'ads', 'both', 'others', null
       *
       *    'ads' means the record was created by ADS Orcid client
       *    'others' that it was created by some other app AND we have
       *          a bibcode
       *    'both' - we have a bibcode and it was created by ADS or by
       *          others
       *    null - the record was created by an external client and we
       *           dont have a bibcode for it
       */
      update: function(options) {
        options = options || {};

        if (this.hiddenCollection && this.view.collection) {

          if (!this._originalCollection) {
            this._originalCollection = new this.hiddenCollection.constructor(this.hiddenCollection.models);
          }

          var coll = this._originalCollection;
          var allowedVals = ['ads', 'both', 'others', null];
          if (_.has(options, 'filterBy')) {

            var cond = options.filterBy;
            if (!_.isArray(cond)) {
              cond = [cond];
            }
            for (var c in cond) {
              if (!_.contains(allowedVals, cond[c]))
                throw Error('Unknown value for the filter: ' + cond[c]);
            }

            var predicate = function(model) {
              if (model.attributes.orcid && _.contains(cond, model.attributes.orcid.provenance))
                return true;
            };
            coll = new this.hiddenCollection.constructor(coll.filter(predicate));
          }


          if (_.has(options, 'sortBy') && options.sortBy) {
            var idx = 0;
            coll = new this.hiddenCollection.constructor(_.map(coll.sortBy(options.sortBy), function(x) {
              x.attributes.resultsIndex = idx++;
              return x;
            }));
          }

          this.hiddenCollection.reset(coll.models);
          this.updatePagination({});
        }
      }
    });
    return OrcidExtension(ResultsWidget);

  });
