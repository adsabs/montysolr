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
            if (data.bibcode == '2013arXiv1305.3460H') {
              return {
                isCreatedByUs: true,
                isCreatedByOthers: false
              }
            }
            else if (data.bibcode == '2008PhDT.......169R') {
              return {
                isCreatedByUs: true,
                isCreatedByOthers: true
              }
            }
            else if (data.bibcode == '1987sbge.proc..355F') {
              return {
                isCreatedByUs: false,
                isCreatedByOthers: true
              }
            }
            else {
              return {
                isCreatedByUs: false,
                isCreatedByOthers: false
              }
            }
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

      it("has method to add record", function(done) {
        var w = _getWidget();

        var oApi = minsub.beehive.getService('OrcidApi');

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
    })
  }
);