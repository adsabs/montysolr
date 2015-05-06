define([
    'underscore',
    'js/widgets/list_of_things/widget',
    'js/bugutils/minimal_pubsub',
    '../../widgets/test_json/test1',
    '../../widgets/test_json/test2',
    'js/modules/orcid/extension'
  ],
  function(
    _,
    ListOfThingsWidget,
    MinimalPubsub,
    Test1,
    Test2,
    OrcidExtension
    ) {
    describe("Orcid Extension (orcid_extension.spec.js)", function() {
      var minsub;
      beforeEach(function (done) {
        minsub = new (MinimalPubsub.extend({
          request: function (apiRequest) {
            if (this.requestCounter % 2 === 0) {
              return Test2();
            } else {
              return Test1();
            }
          }
        }))({verbose: false});

        minsub.beehive.addService('OrcidApi', {
          hasAccess: function() {return true;},
          getRecordInfo: function(data) {
            var defer = $.Deferred();

            if (data.bibcode == '2013arXiv1305.3460H') {
              defer.resolve({
                isCreatedByUs: true,
                isCreatedByOthers: false
              });
            }
            else if (data.bibcode == '2008PhDT.......169R') {
              defer.resolve({
                isCreatedByUs: true,
                isCreatedByOthers: true
              });
            }
            else if (data.bibcode == '1987sbge.proc..355F') {
              defer.resolve({
                isCreatedByUs: false,
                isCreatedByOthers: true
              });
            }
            else {
              defer.resolve({
                isCreatedByUs: false,
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
        minsub.close();
        var ta = $('#test');
        if (ta) {
          ta.empty();
        }
        done();
      });

      var _getWidget = function() {
        var Widget = OrcidExtension(ListOfThingsWidget);
        var widget = new Widget();

        minsub.beehive.addObject('User', {getHardenedInstance: function() {return this}, isOrcidModeOn: function() {return true;}});

        widget.activate(minsub.beehive.getHardenedInstance());
        minsub.publish(minsub.DISPLAY_DOCUMENTS, minsub.createQuery({
          q: "star"
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

      it("has methods to manipulate records", function(done) {
        var w = _getWidget();

        var oApi = minsub.beehive.getService('OrcidApi');
        sinon.spy(w.widget, 'mergeADSAndOrcidData');

        oApi.updateOrcid = function(action, data) {
          expect(action).to.eql(expectedAction);
          expect(data.bibcode).to.eql(expectedBibcode);
          var d = $.Deferred();
          return d.promise();
        };

        var expectedAction = 'update';
        var expectedBibcode = '2013arXiv1305.3460H'; // our rec
        expect(w.widget.view.children.findByIndex(0).$el.find('.orcid-add').length).to.eql(0);
        expect(w.widget.view.children.findByIndex(0).$el.find('.orcid-update').length).to.eql(1);
        expect(w.widget.view.children.findByIndex(0).$el.find('.orcid-delete').length).to.eql(1);
        expect(w.widget.view.children.findByIndex(0).$el.find('.orcid-view').length).to.eql(0);

        w.widget.view.children.findByIndex(0).$el.find('.orcid-update').click();
        expect(w.widget.mergeADSAndOrcidData.called).to.eql(true);

        expectedAction = 'delete';
        w.widget.view.children.findByIndex(0).$el.find('.orcid-delete').click();

        expectedBibcode = '2008PhDT.......169R'; // both have it
        expect(w.widget.view.children.findByIndex(1).$el.find('.orcid-add').length).to.eql(0);
        expect(w.widget.view.children.findByIndex(1).$el.find('.orcid-update').length).to.eql(1);
        expect(w.widget.view.children.findByIndex(1).$el.find('.orcid-delete').length).to.eql(1);
        expect(w.widget.view.children.findByIndex(1).$el.find('.orcid-view').length).to.eql(1);

        expectedAction = 'view';
        w.widget.view.children.findByIndex(1).$el.find('.orcid-view').click();

        expectedBibcode = '1987sbge.proc..355F'; // they have it
        expect(w.widget.view.children.findByIndex(2).$el.find('.orcid-add').length).to.eql(1);
        expect(w.widget.view.children.findByIndex(2).$el.find('.orcid-update').length).to.eql(0);
        expect(w.widget.view.children.findByIndex(2).$el.find('.orcid-delete').length).to.eql(0);
        expect(w.widget.view.children.findByIndex(2).$el.find('.orcid-view').length).to.eql(1);

        expectedAction = 'add';
        w.widget.view.children.findByIndex(2).$el.find('.orcid-add').click();


        done();
      });

      it("when displaying a record, it can handle the 'pending' state", function(done) {
        var w = _getWidget();
        var spy = sinon.spy();
        w.widget.on('orcid-update-finished', spy);

        var oApi = minsub.beehive.getService('OrcidApi');
        var d = $.Deferred();

        oApi.getRecordInfo = function() {
          return d.promise();
        };

        // force new batch (render)
        minsub.publish(minsub.DISPLAY_DOCUMENTS, minsub.createQuery({
          q: "star"
        }));

        // widgets are in the 'loading' state
        expect(w.widget.view.children.findByIndex(1).$el.find('.s-orcid-loading').length).to.eql(1);
        expect(spy.called).to.eql(false);

        // simulate the data has arrived
        d.resolve({
          isCreatedByUs: true,
          isCreatedByOthers: true
        });

        // the widget displays orcid actions
        expect(w.widget.view.children.findByIndex(1).$el.find('.s-orcid-loading').length).to.eql(0);
        expect(w.widget.view.children.findByIndex(1).$el.find('.orcid-update').length).to.eql(1);
        expect(spy.called).to.eql(true);

        done();
      });

      it("merges ADS data before sending them to orcid", function(done) {
        var w = _getWidget();
        var widget = w.widget;
        widget.pubsub = sinon.spy();

        var model = widget.model;
        widget.mergeADSAndOrcidData(model);
        expect(widget.pubsub.called).to.eql(false);

        model.set('bibcode', 'foo');
        var s = widget.beehive.getService;
        widget.beehive.getService = function() {return {}};

        widget.mergeADSAndOrcidData(model);
        expect(widget.pubsub.called).to.eql(false);

        model.set('bibcode', null);
        model.set('identifier', 'foo');

        widget.pubsub.publish = sinon.spy();
        widget.mergeADSAndOrcidData(model);

        expect(widget.pubsub.publish.called).to.eql(true);
        expect(widget.pubsub.publish.lastCall.args[1].get('query').get('q')).to.eql(['identifier:foo']);
        done();
      });
    })
  }
);