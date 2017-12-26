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

  var mockResponse = {"data":[{"affiliations":{"lastActiveDate":"2018/04","name":"Department of Physics, Canakkale Onsekiz Mart University, Arts and Sciences Faculty, Terzioglu Campus, 17020, Turkey","years":["2018"]},"authorName":"Aygün, Sezgin"},{"affiliations":{"lastActiveDate":"2018/04","name":"Bogoliubov Laboratory of Theoretical Physics, Joint Institute for Nuclear Research, 141980 Dubna, Moscow Region, Russia","years":["2018"]},"authorName":"Baushev, A. N."},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of Mathematics, Lovely Professional University, Phagwara, Jalandhar, Punjab 144401, India","years":["2018"]},"authorName":"Bishi, Binaya K."},{"affiliations":{"lastActiveDate":"2018/04","name":"Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China","years":["2018"]},"authorName":"Chen, Yong"},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"Ghiasi, Talieh S."},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA","years":["2018"]},"authorName":"Goel, Mayank"},{"affiliations":{"lastActiveDate":"2018/04","name":"Eurasian International Center for Theoretical Physics and Department of General Theoretical Physics, Eurasian National University, Astana 010008, Kazakhstan","years":["2018"]},"authorName":"Myrzakulov, R."},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"Quereda, Jorge"},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India","years":["2018"]},"authorName":"Sahoo, P. K."},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of Mathematics, Birla Institute of Technology and Science-Pilani Hyderabad Campus, Hyderabad, 500078, India","years":["2018"]},"authorName":"Sahoo, Parbati"},{"affiliations":{"lastActiveDate":"2018/04","name":"Department of MathematicsBirla Institute of Technology and Science (BITS) Pilani, K K Birla Goa Campus, Goa-403726, INDIA","years":["2018"]},"authorName":"Samanta, G. C."},{"affiliations":{"lastActiveDate":"2018/04","name":"Shanghai Key Laboratory of Trustworthy Computing, East China Normal University, Shanghai 200062, China; MOE International Joint Lab of Trustworthy Software, East China Normal University, Shanghai 200062, China","years":["2018"]},"authorName":"Xu, Tao"},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"van Wees, Bart J."},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"van Zwol, Feitze A."},{"affiliations":{"lastActiveDate":"2018/03","name":"Faculty of Science and Engineering, Physics of Nanodevices, Zernike Institute for Advanced Materials, University of Groningen, Groningen, Netherlands","years":["2018"]},"authorName":"van der Wal, Caspar H."}]};

  var mockIds = [
    "2018NewA...60...80S",
    "2018NewA...60...74S",
    "2018NewA...60...69B",
    "2018CNSNS..57..276X",
    "2018TDM.....5a5004Q"
  ];

  var test = function () {
    var init = function () {
      var beehive = new (MinPubSub.extend({
        request: function (apiRequest) {
          switch(apiRequest.get('target')) {
            case 'author-affiliation/search':
              return JSON.stringify(mockResponse);
            case 'search/query':
              _.delay(function () {
                apiRequest.get('options').done({
                  response: { docs: [ { bibcode: 'foo' }, { bibcode: 'bar' }] }
                });
              }, 100); break;
            case 'author-affiliation/export':
              return JSON.stringify({ file: 'SUCCESS' });
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
    };

    describe('Author Affiliation Tool (author_affiliation_tool.spec.js)', function () {

      describe('renders correctly', function () {
        beforeEach(init);
        this.timeout(999999999);
        it('renders for current query', function (done) {
          var w = this.w;
          var component = function () {
            return React.createElement(ReactRedux.Provider, {
              store: w.store
            }, React.createElement(App));
          };

          var wrap = Enzyme.mount(component(), { attachTo: document.getElementById('test') });

          this.w.renderWidgetForCurrentQuery({
            currentQuery: new ApiQuery({ q: 'foo' })
          });

          expect(wrap.text()).to.eql('Loading...');

          _.delay(function () {
            console.log(wrap);
            done();
          }, 2000);
        });


        it('renders', function (done) {


        });
      });

    });
  };

  sinon.test(test)();
});
