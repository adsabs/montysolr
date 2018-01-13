'use strict';
define([
  'jquery',
  'react',
  'enzyme',
  'react-redux',
  'js/components/api_query',
  'js/bugutils/minimal_pubsub',
  'es6!js/widgets/author_affiliation_tool/constants/actionNames',
  'es6!js/widgets/author_affiliation_tool/widget.jsx',
  'es6!js/widgets/author_affiliation_tool/containers/App.jsx'
], function (
  $, React, Enzyme, ReactRedux, ApiQuery,
  MinPubSub, ACTIONS, AuthorAffiliationWidget, App
) {

  var initialState = {
    "data": [],
    "formats": [
      "| Lastname, Firstname | Affiliation | Last Active Date | [csv]",
      "| Lastname | Firstname | Affiliation | Last Active Date | [csv]",
      "| Lastname, Firstname | Affiliation | Last Active Date | [excel]",
      "| Lastname | Firstname | Affiliation | Last Active Date | [excel]",
      "Lastname, Firstname(Affiliation)Last Active Date[text]",
      "Lastname, Firstname(Affiliation)Last Active Date[browser]"
    ],
    "format": "| Lastname, Firstname | Affiliation | Last Active Date | [csv]",
    "toggle": false,
    "count": 0,
    "message": {
      "type": "success",
      "message": "",
      "show": false
    },
    "loading": false,
    "exporting": false
  };
  var offGroupedData = [{"id":"22","selected":false,"author":"Aygün, Sezgin","affiliations":[{"id":"23","selected":false,"name":"Department of Physics, Canakkale Onsekiz Mart University, Arts and Sciences Faculty, Terzioglu Campus, 17020, Turkey","years":["2018"]}],"lastActiveDates":[{"id":"24","selected":false,"date":"2018/04"}]},{"id":"25","selected":false,"author":"Baushev, A. N.","affiliations":[{"id":"26","selected":false,"name":"Bogoliubov Laboratory of Theoretical Physics, Joint Institute for Nuclear Research, 141980 Dubna, Moscow Region, Russia","years":["2018"]}],"lastActiveDates":[{"id":"27","selected":false,"date":"2018/04"}]},{"id":"28","selected":false,"author":"Bishi, Binaya K.","affiliations":[{"id":"29","selected":false,"name":"Department of Mathematics, Lovely Professional University, Phagwara, Jalandhar, Punjab 144401, India","years":["2018"]}],"lastActiveDates":[{"id":"30","selected":false,"date":"2018/04"}]},{"id":"31","selected":false,"author":"Chen, Yong","affiliations":[{"id":"32","selected":false,"name":"Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China","years":["2018"]}],"lastActiveDates":[{"id":"33","selected":false,"date":"2018/04"}]},{"id":"34","selected":false,"author":"Ghiasi, Talieh S.","affiliations":[{"id":"35","selected":false,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"36","selected":false,"date":"2018/03"}]},{"id":"37","selected":false,"author":"Goel, Mayank","affiliations":[{"id":"38","selected":false,"name":"Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA","years":["2018"]}],"lastActiveDates":[{"id":"39","selected":false,"date":"2018/04"}]},{"id":"40","selected":false,"author":"Myrzakulov, R.","affiliations":[{"id":"41","selected":false,"name":"Eurasian International Center for Theoretical Physics and Department of General Theoretical Physics, Eurasian National University, Astana 010008, Kazakhstan","years":["2018"]}],"lastActiveDates":[{"id":"42","selected":false,"date":"2018/04"}]},{"id":"43","selected":false,"author":"Quereda, Jorge","affiliations":[{"id":"44","selected":false,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"45","selected":false,"date":"2018/03"}]},{"id":"46","selected":false,"author":"Sahoo, P. K.","affiliations":[{"id":"47","selected":false,"name":"Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India","years":["2018"]}],"lastActiveDates":[{"id":"48","selected":false,"date":"2018/04"}]},{"id":"49","selected":false,"author":"Sahoo, Parbati","affiliations":[{"id":"50","selected":false,"name":"Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India","years":["2018"]}],"lastActiveDates":[{"id":"51","selected":false,"date":"2018/04"}]},{"id":"52","selected":false,"author":"Samanta, G. C.","affiliations":[{"id":"53","selected":false,"name":"Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA","years":["2018"]}],"lastActiveDates":[{"id":"54","selected":false,"date":"2018/04"}]},{"id":"55","selected":false,"author":"Xu, Tao","affiliations":[{"id":"56","selected":false,"name":"Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China","years":["2018"]}],"lastActiveDates":[{"id":"57","selected":false,"date":"2018/04"}]},{"id":"58","selected":false,"author":"van Wees, Bart J.","affiliations":[{"id":"59","selected":false,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"60","selected":false,"date":"2018/03"}]},{"id":"61","selected":false,"author":"van Zwol, Feitze A.","affiliations":[{"id":"62","selected":false,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"63","selected":false,"date":"2018/03"}]},{"id":"64","selected":false,"author":"van der Wal, Caspar H.","affiliations":[{"id":"65","selected":false,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"66","selected":false,"date":"2018/03"}]}];
  var mockResponse = {"data":[{"affiliations":{"lastActiveDate":"2018/04","name":"Department of Physics, Canakkale Onsekiz Mart University, Arts and Sciences Faculty, Terzioglu Campus, 17020, Turkey","years":["2018"]},"authorName":"Aygün, Sezgin"},{"affiliations":{"lastActiveDate":"2018/04","name":"Bogoliubov Laboratory of Theoretical Physics, Joint Institute for Nuclear Research, 141980 Dubna, Moscow Region, Russia","years":["2018"]},"authorName":"Baushev, A. N."},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of Mathematics, Lovely Professional University, Phagwara, Jalandhar, Punjab 144401, India","years":["2018"]},"authorName":"Bishi, Binaya K."},{"affiliations":{"lastActiveDate":"2018/04","name":"Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China","years":["2018"]},"authorName":"Chen, Yong"},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"Ghiasi, Talieh S."},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA","years":["2018"]},"authorName":"Goel, Mayank"},{"affiliations":{"lastActiveDate":"2018/04","name":"Eurasian International Center for Theoretical Physics and Department of General Theoretical Physics, Eurasian National University, Astana 010008, Kazakhstan","years":["2018"]},"authorName":"Myrzakulov, R."},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"Quereda, Jorge"},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India","years":["2018"]},"authorName":"Sahoo, P. K."},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India","years":["2018"]},"authorName":"Sahoo, Parbati"},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA","years":["2018"]},"authorName":"Samanta, G. C."},{"affiliations":{"lastActiveDate":"2018/04","name":"Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China","years":["2018"]},"authorName":"Xu, Tao"},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"van Wees, Bart J."},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"van Zwol, Feitze A."},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"van der Wal, Caspar H."}]};
  var mockGroupedData = [{"id":"22","selected":true,"author":"Aygün, Sezgin","affiliations":[{"id":"23","selected":true,"name":"Department of Physics, Canakkale Onsekiz Mart University, Arts and Sciences Faculty, Terzioglu Campus, 17020, Turkey","years":["2018"]}],"lastActiveDates":[{"id":"24","selected":true,"date":"2018/04"}]},{"id":"25","selected":true,"author":"Baushev, A. N.","affiliations":[{"id":"26","selected":true,"name":"Bogoliubov Laboratory of Theoretical Physics, Joint Institute for Nuclear Research, 141980 Dubna, Moscow Region, Russia","years":["2018"]}],"lastActiveDates":[{"id":"27","selected":true,"date":"2018/04"}]},{"id":"28","selected":true,"author":"Bishi, Binaya K.","affiliations":[{"id":"29","selected":true,"name":"Department of Mathematics, Lovely Professional University, Phagwara, Jalandhar, Punjab 144401, India","years":["2018"]}],"lastActiveDates":[{"id":"30","selected":true,"date":"2018/04"}]},{"id":"31","selected":true,"author":"Chen, Yong","affiliations":[{"id":"32","selected":true,"name":"Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China","years":["2018"]}],"lastActiveDates":[{"id":"33","selected":true,"date":"2018/04"}]},{"id":"34","selected":true,"author":"Ghiasi, Talieh S.","affiliations":[{"id":"35","selected":true,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"36","selected":true,"date":"2018/03"}]},{"id":"37","selected":true,"author":"Goel, Mayank","affiliations":[{"id":"38","selected":true,"name":"Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA","years":["2018"]}],"lastActiveDates":[{"id":"39","selected":true,"date":"2018/04"}]},{"id":"40","selected":true,"author":"Myrzakulov, R.","affiliations":[{"id":"41","selected":true,"name":"Eurasian International Center for Theoretical Physics and Department of General Theoretical Physics, Eurasian National University, Astana 010008, Kazakhstan","years":["2018"]}],"lastActiveDates":[{"id":"42","selected":true,"date":"2018/04"}]},{"id":"43","selected":true,"author":"Quereda, Jorge","affiliations":[{"id":"44","selected":true,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"45","selected":true,"date":"2018/03"}]},{"id":"46","selected":true,"author":"Sahoo, P. K.","affiliations":[{"id":"47","selected":true,"name":"Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India","years":["2018"]}],"lastActiveDates":[{"id":"48","selected":true,"date":"2018/04"}]},{"id":"49","selected":true,"author":"Sahoo, Parbati","affiliations":[{"id":"50","selected":true,"name":"Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India","years":["2018"]}],"lastActiveDates":[{"id":"51","selected":true,"date":"2018/04"}]},{"id":"52","selected":true,"author":"Samanta, G. C.","affiliations":[{"id":"53","selected":true,"name":"Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA","years":["2018"]}],"lastActiveDates":[{"id":"54","selected":true,"date":"2018/04"}]},{"id":"55","selected":true,"author":"Xu, Tao","affiliations":[{"id":"56","selected":true,"name":"Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China","years":["2018"]}],"lastActiveDates":[{"id":"57","selected":true,"date":"2018/04"}]},{"id":"58","selected":true,"author":"van Wees, Bart J.","affiliations":[{"id":"59","selected":true,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"60","selected":true,"date":"2018/03"}]},{"id":"61","selected":true,"author":"van Zwol, Feitze A.","affiliations":[{"id":"62","selected":true,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"63","selected":true,"date":"2018/03"}]},{"id":"64","selected":true,"author":"van der Wal, Caspar H.","affiliations":[{"id":"65","selected":true,"name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]}],"lastActiveDates":[{"id":"66","selected":true,"date":"2018/03"}]}];
  var mockTable = 'Viewing Author Affiliation Data for 2 Records| Lastname, Firstname | Affiliation | Last Active Date | [csv]| Lastname | Firstname | Affiliation | Last Active Date | [csv]| Lastname, Firstname | Affiliation | Last Active Date | [excel]| Lastname | Firstname | Affiliation | Last Active Date | [excel]Lastname, Firstname(Affiliation)Last Active Date[text]Lastname, Firstname(Affiliation)Last Active Date[browser]ExportToggle AllResetAuthorAffiliationsYearsLast Active Date Aygün, Sezgin Department of Physics, Canakkale Onsekiz Mart University, Arts and Sciences Faculty, Terzioglu Campus, 17020, Turkey2018 2018/04 Baushev, A. N. Bogoliubov Laboratory of Theoretical Physics, Joint Institute for Nuclear Research, 141980 Dubna, Moscow Region, Russia2018 2018/04 Bishi, Binaya K. Department of Mathematics, Lovely Professional University, Phagwara, Jalandhar, Punjab 144401, India2018 2018/04 Chen, Yong Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China2018 2018/04 Ghiasi, Talieh S. Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands2018 2018/03 Goel, Mayank Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA2018 2018/04 Myrzakulov, R. Eurasian International Center for Theoretical Physics and Department of General Theoretical Physics, Eurasian National University, Astana 010008, Kazakhstan2018 2018/04 Quereda, Jorge Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands2018 2018/03 Sahoo, P. K. Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India2018 2018/04 Sahoo, Parbati Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India2018 2018/04 Samanta, G. C. Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA2018 2018/04 Xu, Tao Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China2018 2018/04 van Wees, Bart J. Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands2018 2018/03 van Zwol, Feitze A. Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands2018 2018/03 van der Wal, Caspar H. Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands2018 2018/03| Lastname, Firstname | Affiliation | Last Active Date | [csv]| Lastname | Firstname | Affiliation | Last Active Date | [csv]| Lastname, Firstname | Affiliation | Last Active Date | [excel]| Lastname | Firstname | Affiliation | Last Active Date | [excel]Lastname, Firstname(Affiliation)Last Active Date[text]Lastname, Firstname(Affiliation)Last Active Date[browser]ExportToggle AllReset';
  var exportSelection = {"selected":["Aygün, Sezgin|Department of Physics, Canakkale Onsekiz Mart University, Arts and Sciences Faculty, Terzioglu Campus, 17020, Turkey|2018/04","Baushev, A. N.|Bogoliubov Laboratory of Theoretical Physics, Joint Institute for Nuclear Research, 141980 Dubna, Moscow Region, Russia|2018/04","Bishi, Binaya K.|Department of Mathematics, Lovely Professional University, Phagwara, Jalandhar, Punjab 144401, India|2018/04","Chen, Yong|Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China|2018/04","Ghiasi, Talieh S.|Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands|2018/03","Goel, Mayank|Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA|2018/04","Myrzakulov, R.|Eurasian International Center for Theoretical Physics and Department of General Theoretical Physics, Eurasian National University, Astana 010008, Kazakhstan|2018/04","Quereda, Jorge|Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands|2018/03","Sahoo, P. K.|Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India|2018/04","Sahoo, Parbati|Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India|2018/04","Samanta, G. C.|Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA|2018/04","Xu, Tao|Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China|2018/04","van Wees, Bart J.|Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands|2018/03","van Zwol, Feitze A.|Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands|2018/03","van der Wal, Caspar H.|Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands|2018/03"],"format":"| Lastname, Firstname | Affiliation | Last Active Date | [csv]"};
  var mockIds = [
    "2018NewA...60...80S",
    "2018NewA...60...74S"
  ];

  var test = function () {
    var init = function () {
      var self = this;
      this.testSpies = {
        authorAffSearch: sinon.spy(),
        searchQuery: sinon.spy(),
        authorAffExport: sinon.spy()
      };
      var beehive = new (MinPubSub.extend({
        request: function (apiRequest) {
          switch(apiRequest.get('target')) {
            case 'author-affiliation/search':
              self.testSpies.authorAffSearch();
              _.delay(function () {
                apiRequest.get('options').done(JSON.stringify(mockResponse));
              }, 10); break;
            case 'search/query':
              self.testSpies.authorAffSearch();
              _.delay(function () {
                apiRequest.get('options').done({
                  response: { docs: [{ bibcode: 'foo' }, { bibcode: 'bar' }] }
                });
              }, 10); break;
            case 'author-affiliation/export':
              self.testSpies.authorAffExport();
              _.delay(function () {
                apiRequest.get('options').done({ file: 'SUCCESS' });
              }, 10); break;
            default:
              return {
                response: {
                  docs: _.map(mockIds, function (i) { return { bibcode: i }})
                }
              }
          }
        }
      }))({ verbose: false }).beehive.getHardenedInstance();
      this.w = new AuthorAffiliationWidget();
      this.w.activate(beehive);
      this.component = React.createElement(ReactRedux.Provider, {
        store: this.w.store
      }, React.createElement(App));
    };

    var teardown = function () {
      this.testSpies = null;
      this.w.destroy();
      this.w = null;
      this.component = null;
    };

    describe('Author Affiliation Tool (author_affiliation_tool.spec.js)', function () {

      describe('initial state', function () {
        beforeEach(init);
        afterEach(teardown);
        it('matches initial state', function () {
          expect(this.component.props.store.getState()).to.eql(initialState);
        });

        it('table was rendered empty initially', function () {
          var el = Enzyme.mount(this.component);
          expect(el.find('.container').exists()).to.eql(true);
        });
      });

      describe('renders for current query', function () {
        beforeEach(init);
        afterEach(teardown);

        it('fetch affiliation data using ids', function (done) {
          var self = this;
          this.w.fetchAffiliationData = sinon.spy();
          this.w.renderWidgetForCurrentQuery({});
          _.delay(function () {
            expect(self.w.fetchAffiliationData.called).to.eql(false);
            self.w.renderWidgetForCurrentQuery({
              currentQuery: new ApiQuery({ q: 'foo' })
            });
            _.delay(function () {
              expect(self.w.fetchAffiliationData.called).to.eql(true);
              done();
            }, 25);
          }, 25);
        });
        
        it('updates store correctly', function (done) {
          var state = _.bind(function () {
            return this.component.props.store.getState();
          }, this);
          expect(state()).to.eql(initialState);
          this.w.renderWidgetForCurrentQuery({
            currentQuery: new ApiQuery({ q: 'foo' })
          });
          _.delay(function () {
            expect(state().count).to.eql(2);
            expect(state().data).to.eql([]);
            expect(state().loading).to.eql(true);
            _.delay(function () {
              expect(state().data.length).to.eql(mockGroupedData.length);
              expect(state().loading).to.eql(false);
              done();
            }, 15);
          }, 15);
        });

        it('updates the table with data', function (done) {
          var com = this.component;
          this.w.renderWidgetForCurrentQuery({
            currentQuery: new ApiQuery({ q: 'foo' })
          });
          _.delay(function () {
            var el = Enzyme.render(com);
            expect(el.text()).to.eql('Loading...');
            _.delay(function () {
              var el = Enzyme.render(com);
              expect(el.text()).to.eql(mockTable);
              done();
            }, 15);
          }, 15);
        });
      });

      describe('renders for list of bibcodes', function () {
        beforeEach(init);
        afterEach(teardown);

        it('store has correct state', function (done) {
          var state = _.bind(function () {
            return this.component.props.store.getState();
          }, this);
          expect(state()).to.eql(initialState);
          this.w.renderWidgetForListOfBibcodes(mockIds);
          expect(state().count).to.eql(2);
          expect(state().data).to.eql([]);
          expect(state().loading).to.eql(true);
          _.delay(function () {
            expect(state().data.length).to.eql(mockGroupedData.length);
            expect(state().loading).to.eql(false);
            done();
          }, 25);
        });

        it('table has been rendered', function (done) {
          var com = this.component;
          this.w.renderWidgetForListOfBibcodes(mockIds);
          var el = Enzyme.render(com);
          expect(el.text()).to.eql('Loading...');
          _.delay(function () {
            var el = Enzyme.render(com);
            expect(el.text()).to.eql(mockTable);
            done();
          }, 25);
        });
      });

      describe('form actions', function () {
        beforeEach(init);
        afterEach(teardown);
        it('toggle all works correctly', function () {
          var com = this.component;
          com.props.store.dispatch({ type: 'SET_DATA', value: mockGroupedData });
          var el = Enzyme.mount(com);
          var toggleBtn = el.findWhere(function (e) {
            return e.text() === 'Toggle All';
          }).first();

          var state = function () {
            return com.props.store.getState();
          };
          expect(state().data).to.eql(mockGroupedData);
          toggleBtn.simulate('click');
          expect(state().data).to.eql(offGroupedData);
        });
        it('reset works correctly', function () {
          var com = this.component;
          com.props.store.dispatch({ type: 'SET_DATA', value: mockGroupedData });
          var el = Enzyme.mount(com);
          var resetBtn = el.findWhere(function (e) {
            return e.text() === 'Reset';
          }).first();

          var state = function () {
            return com.props.store.getState();
          };

          // starts off with initial data
          expect(state().data).to.eql(mockGroupedData);

          // small change to the data
          el.find('input[type="checkbox"]').first().simulate('change');

          expect(state().data[0].selected).to.eql(false);

          resetBtn.simulate('click');

          expect(state().data[0].selected).to.eql(true);

          expect(state().data).to.eql(mockGroupedData);
        });
        it('format selection works correctly', function () {
          var com = this.component;
          com.props.store.dispatch({ type: 'SET_DATA', value: mockGroupedData });
          var el = Enzyme.mount(com);
          var formatSelect = el.find('select').first();

          var state = function () {
            return com.props.store.getState();
          };

          // verify it matches the state
          expect(formatSelect.props().value).to.eql(state().format);

          formatSelect.simulate('change', { target: {
            value: state().formats[3]
          }});

          expect(state().format).to.eql(state().formats[3]);
        });
      });

      describe('exporting', function () {
        beforeEach(init);
        afterEach(teardown);
        it('export button properly exports', function () {
          var eAffData = sinon.stub(this.w, 'exportAffiliationData', function () {
            return $.Deferred().promise();
          });
          var com = this.component;
          com.props.store.dispatch({ type: 'SET_DATA', value: mockGroupedData });
          var el = Enzyme.mount(com);
          var exportBtn = el.find('button').filterWhere(function (e) {
            return e.text() === 'Export'
          }).first();

          var state = function () {
            return com.props.store.getState();
          };

          expect(state().exporting).to.eql(false);
          exportBtn.simulate('click');
          expect(state().exporting).to.eql(true);
          expect(eAffData.calledOnce).to.eql(true);
          expect(eAffData.args[0][0]).to.eql(exportSelection);
        });
      });
    });
  };

  sinon.test(test)();
});
