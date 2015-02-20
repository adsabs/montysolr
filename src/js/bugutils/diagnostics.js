/**
 * Set of utilities for debugging BBB and Api in general
 *
 * To compare two Api endpoints (but you must have access to the solr instance):
 *
 *    d = bbb.getController('Diagnostics');u1='http://54.174.175.209:8983';u2='http://54.173.87.140:8983'
 *    d.compareTwoSearchInstances(u1,u2).done(d.printComparison)
 */

define([
  'jquery',
  'underscore',
  'js/components/api_query',
  'js/components/api_request',
  'js/components/api_response',
  'js/mixins/dependon',
  'js/components/generic_module',
  'sprintf',
  'js/components/api_targets'
], function(
  $,
  _,
  ApiQuery,
  ApiRequest,
  ApiResponse,
  Dependon,
  GenericModule,
  sprintf,
  ApiTargets
  ) {

  var Diagnostics = GenericModule.extend({

    activate: function(beehive, app) {
      this.setApp(app);
      this.setBeeHive(beehive);
    },

    getFirstDoc: function(queryString, options) {
      var opts = {
        query: {'q': queryString, fl: 'title,abstract,bibcode,id,author'},
        target: ApiTargets.SEARCH
      };
      if (options) {
        _.extend(opts, options);
      }
      var promise = $.Deferred();
      this.apiRequest(opts)
        .done(function(res) {
          promise.resolve(res['response']['docs'][0]);
        });
      return promise.promise();
    },

    /**
     * Helper methods, you can pass in json structure to make api request
     *
     * @param options
     *    {
     *      target: 'search/query',
     *      query: {'q': 'title:foo'}
     *    }
     * @returns Deferred
     */
    apiRequest: function(options, reqOptions) {
      var app = this.getApp();
      var api = app.getService('Api');
      options = options || {};
      if (!options.query)
        throw Error('You must pass in "query"');
      if (!(options.query instanceof ApiQuery))
        options.query = new ApiQuery(options.query);

      var r = new ApiRequest({'target': options.target || ApiTargets.SEARCH, query: options.query});
      return api.request(r, reqOptions);
    },

    /**
     * Makes $.ajax() request adding Authorization header
     * usage:
     *  disc.ajax({'foo': 'bar'}).done(function(data) {.....})
     *
     * @param options
     * @returns {*}
     */
    ajax: function(options) {
      var app = this.getApp();
      var api = app.getService('Api');
      options.headers = options.header || {};
      options.headers.Authorization = api.access_token;
      return $.ajax(options);
    },

    /**
     * Makes $.ajax() solr to SOLR, useful for cross-site
     * requests when you have access to the real solr
     *
     * @param options
     */
    jsonp: function(options, jsonpParameter) {
      options.dataType = 'jsonp';
      options.beforeSend = function(promise, xhr) {
        var parts = xhr.url.split('&');
        _.each(parts, function(p) {
          if (p.startsWith('callback=')) {
            xhr.url += '&' + (jsonpParameter || 'json.wrf') + '=' + p.replace('callback=', ''); // jQuery generates a random callback, we'll pass it to solr
          }
        })
      };
      return this.ajax(options);
    },

    printComparison: function(result) {
      var res = [];

      var format= '%40s %40s %40s %10s';
      res.push(sprintf.sprintf(format, 'field', result.first, result.second, 'diff'));
      res.push('------------------------------------------------------------------------------------------------------------------------------------');

      for (var f in result.fields) {
        var x = result.fields[f];
        res.push(sprintf.sprintf(format, f, x.first, x.second, x.diff));
      }
      console.log(res.join('\n'));
      return res;
    },

    compareTwoSearchInstances: function(url1, url2, listOfFields, listOfFields2) {
      var self = this;
      var result = $.Deferred();

      if (!listOfFields || !listOfFields2) {
        var d1 = this.getListOfFields(url1);
        var d2 = this.getListOfFields(url2);
        $.when(d1, d2).done(function(v1, v2) {

          self.compareTwoSearchInstances(url1, url2, v1, v2)
            .done(function(res) {
              result.resolve(res);
            })
        });
      }
      else {

        var c1 = this.countDocsInFields(url1, listOfFields);
        var c2 = this.countDocsInFields(url2, listOfFields2);
        $.when(c1, c2)
          .done(function(v1, v2) {
            var fV1 = _.keys(v1.fields);
            var fV2 = _.keys(v2.fields);
            var fAll = _.unique(fV1.concat(fV2)).sort();
            var res = {};

            for (var k in fAll) {
              k = fAll[k];
              var o = v1.fields[k];
              var n = v2.fields[k];
              res[k] = {
                first: o ? o.numFound : '--',
                second: n ? n.numFound : '--',
                diff: ((o && n) ? n.numFound - o.numFound : '--')
              }
            }
            result.resolve({first: url1, second: url2, fields: res});
          });
      }

      return result;
    },

    getListOfFields: function(url) {
      var defer = $.Deferred();
      this.jsonp({
        url: url + '/solr/collection1/admin/luke?numTerms=0&wt=json&indent=true',
        timeout: 60000
      })
      .done(function(data) {
        var fields = [];
        for(var fname in data.fields) {
          if (fname.startsWith('_'))
            continue;
          fields.push(fname);
        }
        defer.resolve(fields);
      });
      return defer;
    },

    /**
     * Get the count of numDocs and numFound for every field in the index
     * this requires either a list of fields, or it can discovery fields
     * (if /solr/collection1/admin/luke is available
     *
     * @returns {
   *   index: {data about index},
   *   fields: {
   *     name: {'numDocs' : xxxxx, 'numFound': xxxxx}
   *     }
   *   }
     */
    countDocsInFields: function(url, listOfFields){

      var result = $.Deferred();
      var self = this;

      var cycle = [];
      var cycleR = {};
      var collectedData = {};
      var finalResult = {
        fields: collectedData
      };

      for(var ix in listOfFields) {
        var fname = listOfFields[ix];
        var de = $.Deferred();
        var q = '{!lucene}' + fname + ':*';

        console.log('Getting num docs for: ' + q);

        var c = self.jsonp({
          url: url + '/solr/collection1/select?q=' + q + '&fl=id&wt=json&indent=true',
          context: {field: fname, finalResult:finalResult, cycleR: cycleR},
          timeout: 300000 // 5mins
        })
          .done(function(data) {
            this.finalResult.fields[this.field] = {numFound: data.response.numFound};
            var c = 0;
            var togo = [];
            for (var x in this.cycleR) {
              if (this.cycleR[x].state() == 'pending') { //resolved and rejected count as done
                c += 1;
                togo.push(x);
              }
            }
            console.log(url + ' Got response for: ' + this.field + ' numFound: ' + data.response.numFound + ' requests to go: ' + c + ' ' + (togo.length < 5 ? togo.join(', ') : ''));
            if (c == 0)
              result.resolve(this.finalResult);
          });
        cycleR[fname] = c;
        cycle.push(c);
      }

      // for some stupid reason this is not working, and i dont know why
      //var d = $.when.call($.when, cycle)
      //  .done(function(data) {
      //    result.resolve(collectedData);
      //  });
      return result;
    },

    request: function(options) {
      return $.ajax(options);
    },


    /**
     * ==================================================================
     * TEST CASES; these are to be run manually from the console and only
     * by those who understand what they are doing ;-)
     * ==================================================================
     */


    testOrcidLogin: function() {
      var oa = this.getApp().getService('OrcidApi');
      oa.signOut();
      window.location = oa.config.loginUrl;
    },

    /**
     * Upload one document to orcid;
     * Prerequisite: testOrcidLogin()
     */
    testOrcidSendingData: function() {
      var app = this.getApp();
      var self = this;
      this.getFirstDoc('bibcode:1978yCat.1072....0C').done(
        function(doc) {
          var oa = app.getService('OrcidApi');
          oa.addWorks([doc])
            .done(function(result) {
              console.log('result:', result, 'expected: {}');
            })
        }
      )
    }

  });

  _.extend(Diagnostics.prototype, Dependon.App, Dependon.BeeHive);

  return Diagnostics;
});