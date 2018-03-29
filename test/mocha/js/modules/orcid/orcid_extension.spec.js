'use strict';
define([
    'underscore',
    'js/widgets/list_of_things/details_widget',
    'js/bugutils/minimal_pubsub',
    '../../widgets/test_json/test1',
    '../../widgets/test_json/test2',
    'js/modules/orcid/extension',
    './helpers'
  ],
  function(
    _,
    DetailsWidget,
    MinimalPubsub,
    Test1,
    Test2,
    OrcidExtension,
    helpers
    ) {
    describe("Orcid Extension (orcid_extension.spec.js)", function() {
      var minsub;
      beforeEach(function (done) {
        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            var fakeSolrResponse = this.requestCounter % 2 == 0 ? Test2() : Test1();
            if (apiRequest.get("query").get("start"))
             fakeSolrResponse.response.start = apiRequest.get("query").get("start")[0];
            return fakeSolrResponse;
          }
        }))({verbose: false});

        minsub.beehive.addService('OrcidApi', {
          hasAccess: function() {return true;},
          getOrcidProfileInAdsFormat: function() {
            var defer = $.Deferred();
            defer.resolve({
              response: {
                docs: []
              }
            });
            return defer.promise();
          },
          getWork: function (putCode) {
            return helpers.api.getWork(putCode);
          },
          getUserProfile: function () {
            return helpers.api.getUserProfile();
          },
          getRecordInfo: function(data) {
            var defer = $.Deferred();

            if (data.bibcode === '2013arXiv1305.3460H') {
              defer.resolve({
                isCreatedByADS: true,
                isCreatedByOthers: false
              });
            }
            else if (data.bibcode === '2008PhDT.......169R') {
              defer.resolve({
                isCreatedByADS: true,
                isCreatedByOthers: true
              });
            }
            else if (data.bibcode === '1987sbge.proc..355F') {
              defer.resolve({
                isCreatedByADS: false,
                isCreatedByOthers: true
              });
            } else if (data.bibcode === '1993sfgi.conf..324C') {
              defer.resolve({
                isCreatedByADS: false,
                isCreatedByOthers: true,
                isKnownToADS: true
              })
            }
            else {
              defer.resolve({
                isCreatedByADS: false,
                isCreatedByOthers: false
              });
            }
            return defer.promise();
          },
          getHardenedInstance: function() {
            return this;
          }
        });
        done();
      });

      afterEach(function (done) {
        minsub.destroy();
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });

      var _getWidget = function() {
        var Widget = OrcidExtension(DetailsWidget);
        var widget = new Widget();

        minsub.beehive.addObject('User', {
          getHardenedInstance: function() {return this},
          isOrcidModeOn: function() {return true;}
        });

        widget.activate(minsub.beehive.getHardenedInstance());
        minsub.publish(minsub.DISPLAY_DOCUMENTS, minsub.createQuery({
         q : "bibcode:star"
        }));

        var $w = $(widget.render().el);
        $('#test').append($w);
        return {widget: widget, '$w': $w};
      };


      it("Should wrap the widget (adding orcid actions)", function(done) {
        var w = _getWidget();
        expect(w.$w.find('.orcid-update').length).to.be.gt(1);
        done();
      });

      it('_getOrcidInfo correctly updates record', function () {
        var record = {
          isCreatedByOthers: false,
          isCreatedByADS: false,
          isKnownToADS: false,
          provenance: null
        };

        var widget = _getWidget().widget;
        var test = function (rec, actions, provenance) {
          var out = widget._getOrcidInfo(_.extend({}, record, rec));
          expect(out.provenance).to.be.equal(provenance);
          var keys = _.keys(out.actions);
          expect(_.isEqual(keys, actions)).to.be.equal(true);
        };

        test({}, ['add'], null);
        test({
          isCreatedByADS: true,
          isCreatedByOthers: true
        }, ['update', 'delete', 'view'], 'ads');
        test({ isCreatedByADS: true }, ['update', 'delete'], 'ads');
        test({ isCreatedByOthers: true }, ['view'], 'others');
        test({
          isCreatedByOthers: true,
          isKnownToADS: true
        }, ['add', 'view'], 'others');
      });

      it("has methods to manipulate records", function(done) {
        var w = _getWidget();
        var oApi = minsub.beehive.getService('OrcidApi');
        sinon.spy(w.widget, 'mergeADSAndOrcidData');

        var get = function (idx, prop) {
          return w.widget.view.children.findByIndex(idx).$el.find('.orcid-' + prop);
        };

        var check = function (idx, prop, exp) {
          var len = get(idx, prop).length;
          expect(len === 1).to.eql(exp, prop + ' on element ' + idx + ' is correct');
        };

        // this is checking for presence of this option on each record

        check(0, 'add', false);
        check(0, 'update', true);
        check(0, 'delete', true);
        check(0, 'view', false);

        check(1, 'add', false);
        check(1, 'update', true);
        check(1, 'delete', true);
        check(1, 'view', true);

        check(2, 'add', false);
        check(2, 'update', false);
        check(2, 'delete', false);
        check(2, 'view', true);

        check(3, 'add', true);
        check(3, 'update', false);
        check(3, 'delete', false);
        check(3, 'view', true);

        done();
      });

      it('handles a successful getRecordInfo request', function (done) {
        var w = _getWidget();
        var spy = sinon.spy();
        w.widget.on('orcid-update-finished', spy);

        var oApi = minsub.beehive.getService('OrcidApi');
        var d = $.Deferred();

        oApi.getRecordInfo = _.constant(d.promise());

        minsub.publish(minsub.DISPLAY_DOCUMENTS, minsub.createQuery({
          q: "bibcode:star"
        }));

        // widgets are in the 'loading' state
        expect(w.widget.view.children.findByIndex(1).$el.find('.s-orcid-loading').length).to.eql(1);
        expect(spy.called).to.eql(false);

        // simulate the data has arrived
        d.resolve({
          isCreatedByADS: true,
          isCreatedByOthers: true
        });

        // the widget displays orcid actions
        expect(w.widget.view.children.findByIndex(1).$el.find('.s-orcid-loading').length).to.eql(0);
        expect(w.widget.view.children.findByIndex(1).$el.find('.orcid-update').length).to.eql(1);
        expect(spy.called).to.eql(true);
        done();
      });

      it('handles a rejected getRecordInfo request', function (done) {
        var w = _getWidget();
        var spy = sinon.spy();
        w.widget.on('orcid-update-finished', spy);

        var oApi = minsub.beehive.getService('OrcidApi');
        var d = $.Deferred();

        oApi.getRecordInfo = _.constant(d.promise());

        minsub.publish(minsub.DISPLAY_DOCUMENTS, minsub.createQuery({
          q: "bibcode:star"
        }));

        // widgets are in the 'loading' state
        expect(w.widget.view.children.findByIndex(1).$el.find('.s-orcid-loading').length).to.eql(1);
        expect(spy.called).to.eql(false);

        // simulate error
        d.reject();

        _.delay(function () {
          // the widget displays orcid actions
          expect(w.widget.view.children.findByIndex(1).$el.find('.s-orcid-loading').length).to.eql(0);
          expect(spy.called).to.eql(true);
  
          // but the style is 'default' should be default actions
          expect(w.widget.view.children.findByIndex(1).$el.find('button.btn-default').length).to.eql(1);
          done();
        }, 500);
      });

      it("merges ADS data before sending them to orcid", function(done) {
        var w = _getWidget();
        var widget = w.widget;
        var spy = sinon.spy();
        sinon.spy(widget.getPubSub(), 'publish');

        var model = widget.model;
        widget.mergeADSAndOrcidData(model);
        expect(widget.getPubSub().publish.called).to.eql(false);

        model.set('bibcode', 'foo');
        widget.mergeADSAndOrcidData(model);
        expect(widget.getPubSub().publish.called).to.eql(false);

        model.set('bibcode', null);
        model.set('source_name', 'ads');
        model.set('identifier', 'foo');
        model.set('_work', {
          getPutCode: _.constant(99999)
        });

        widget._findWorkByModel = function (model) {
          return $.Deferred().resolve(model).promise();
        };

        widget.mergeADSAndOrcidData(model);
        expect(widget.getPubSub().publish.called).to.eql(true);
        expect(widget.getPubSub().publish.lastCall.args[1].get('query').get('q')).to.eql(['identifier:foo']);
        
        done();
      });
    })
  }
);
