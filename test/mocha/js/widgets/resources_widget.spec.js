
define([
    'jquery',
    'react',
    'enzyme',
    'es6!js/widgets/resources/widget.jsx',
    'es6!js/widgets/resources/components/app.jsx',
    'es6!js/widgets/resources/components/fullTextSources.jsx',
    'es6!js/widgets/resources/components/dataProducts.jsx',
    'es6!js/widgets/resources/components/loading.jsx',
    'js/widgets/resources/actions',
    'js/widgets/base/base_widget',
    'js/bugutils/minimal_pubsub'
  ],
  function ($, React, Enzyme, ResourcesWidget, App, FullTextSources, DataProducts, LoadingIcon, actions, BaseWidget, MinPubSub) {

  var mockResponse = {
    get: function () {
      return this.response.docs[0];
    },
    "responseHeader": {
      "status": 0,
      "QTime": 1,
      "params": {
        "wt": "json",
        "q": "bibcode:2017MNRAS.467.4015H",
        "fl": "links_data"
      }
    },
    "response": {
      "numFound": 1,
      "start": 0,
      "docs": [
        {
          "links_data": [
            "{\"title\":\"\", \"type\":\"pdf\", \"instances\":\"\", \"access\":\"\"}",
            "{\"title\":\"\", \"type\":\"ned\", \"instances\":\"1\", \"access\":\"\"}",
            "{\"title\":\"\", \"type\":\"preprint\", \"instances\":\"\", \"access\":\"open\"}",
            "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"
          ]
        }
      ]
    }
  };

  var mockApiQuery = {
    get: function () {
      return ['bibcode:2017MNRAS.467.4015H'];
    }
  };

  var mockLinksData = {
    "links_data": [
      "{\"title\":\"\", \"type\":\"pdf\", \"instances\":\"\", \"access\":\"\"}",
      "{\"title\":\"\", \"type\":\"ned\", \"instances\":\"1\", \"access\":\"\"}",
      "{\"title\":\"\", \"type\":\"preprint\", \"instances\":\"\", \"access\":\"open\"}",
      "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"
    ],
    "link_server": "http://sfx.galib.uga.edu/sfx_git1"
  };

  var mockLinksParsedData = {
    "links_data": [
      "{\"title\":\"\", \"type\":\"pdf\", \"instances\":\"\", \"access\":\"\"}",
      "{\"title\":\"\", \"type\":\"ned\", \"instances\":\"1\", \"access\":\"\"}",
      "{\"title\":\"\", \"type\":\"preprint\", \"instances\":\"\", \"access\":\"open\"}",
      "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"
    ],
    "link_server": "http://sfx.galib.uga.edu/sfx_git1",
    "fullTextSources": [
      {
        "openAccess": false,
        "title": "Publisher Article",
        "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=undefined&link_type=EJOURNAL"
      },
      {
        "openAccess": false,
        "title": "Publisher PDF",
        "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=undefined&link_type=ARTICLE"
      },
      {
        "openAccess": true,
        "title": "arXiv e-print",
        "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=undefined&link_type=PREPRINT"
      }
    ],
    "dataProducts": [
      {
        "title": "NED objects (1)",
        "link": "http://adsabs.harvard.edu/cgi-bin/nph-data_query?bibcode=undefined&link_type=NED"
      }
    ]
  };

  var mockBeehiveUserInstance = function () {
    return {
      getUserData: function () {
        return {
          link_server: 'http://sfx.galib.uga.edu/sfx_git1'
        };
      }
    };
  };

  describe('Resources Widget', function () {
    var sandbox, widget, minsub, request, responseMock,
      apiQueryMock, beehive, getState, dispatchRequestStub;

    beforeEach(function () {
      sandbox = sinon.sandbox.create();
      widget = new ResourcesWidget();
      minsub = new MinPubSub();
      beehive = minsub.beehive.getHardenedInstance();
      responseMock = sandbox.mock(mockResponse);
      apiQueryMock = sandbox.mock(mockApiQuery);
      dispatchRequestStub = sandbox.stub(widget, 'dispatchRequest', function () {
        minsub.publish(minsub.DELIVERING_RESPONSE, mockResponse);
      });
      sandbox.stub(beehive, 'getObject', mockBeehiveUserInstance);
      getState = widget.store.getState;
    });

    afterEach(function () {
      sandbox.restore();
    });

    it('extends from baseWidget', function () {
      expect(widget).to.be.instanceOf(BaseWidget);
    });

    it('correctly sends request to api for fields', function () {
      widget.activate(beehive);
      minsub.publish(minsub.DISPLAY_DOCUMENTS, mockApiQuery);
      var query = dispatchRequestStub.args[0][0];
      expect(dispatchRequestStub.called).to.be.true;
      expect(query.get('q')[0]).to.equal('bibcode:2017MNRAS.467.4015H');
    });

    it('display documents correctly updates query and parses', function (done) {
      widget.activate(beehive);
      sandbox.stub(actions, 'loadBibcodeData', function (bibcode) {
        return { value: bibcode }
      });

      var expectedActions = [
        { type: 'UPDATE_QUERY', value: mockApiQuery },
        { value: '2017MNRAS.467.4015H' },
        { type: 'IS_LOADING', value: true }
      ];

      var dispatchMock = function (action) {
        var expectedAction = expectedActions.shift();
        expect(action).to.deep.equal(expectedAction);
        if (!expectedAction.length) {
          done();
        }
      };

      actions.displayDocuments(mockApiQuery)(dispatchMock);
    });

    it('displayDocuments handles bad input correctly', function (done) {
      widget.activate(beehive);

      sandbox.stub(actions, 'handleError', function (message) {
        return { value: message }
      });

      var mockQuery = {
        get: function () {
          return ['']
        }
      };

      var expectedActions = [
        { type: 'UPDATE_QUERY', value: mockQuery },
        { value: 'NO_BIBCODE' }
      ];

      var dispatchMock = function (action) {
        var expectedAction = expectedActions.shift();
        expect(action).to.deep.equal(expectedAction);
        if (!expectedActions.length) {
          done();
        }
      };

      actions.displayDocuments(mockQuery)(dispatchMock);
    });

    it('loadBibcodeData dispatch request correctly', function (done) {
      widget.activate(beehive);

      var expectedActions = [
        { type: 'UPDATE_BIBCODE', value: '2017MNRAS.467.4015H' }
      ];

      var dispatchMock = function (action) {
        var expectedAction = expectedActions.shift();
        expect(action).to.deep.equal(expectedAction);
        if (!expectedAction.length) {
          done();
        }
      };

      actions.loadBibcodeData('2017MNRAS.467.4015H')
        (dispatchMock, widget.store.getState, widget);

      expect(dispatchRequestStub.calledOnce).to.be.true;
      var query = dispatchRequestStub.args[0][0];
      expect(query.get('q')[0]).to.equal('bibcode:2017MNRAS.467.4015H');
      expect(query.get('fl')[0]).to.equal('links_data');
    });

    it('loadBibcodeData triggers widget-ready', function () {
      widget.activate(beehive);

      widget.trigger = sandbox.spy();
      widget.store.dispatch(actions.updateBibcode('2017MNRAS.467.4015H'));

      actions.loadBibcodeData('2017MNRAS.467.4015H')
      (null, widget.store.getState, widget);

      expect(widget.trigger.calledOnce).to.be.true;
      expect(widget.trigger.args[0]).to.deep.equal([
        'page-manager-event', 'widget-ready', { 'isActive': true }
      ]);
    });

    it('processResponse correctly parses resource data', function (done) {
      widget.activate(beehive);

      sandbox.stub(actions, 'parseResources', function (data) {
        return { value: data }
      });

      var expectedActions = [
        { type: 'UPDATE_API_RESPONSE', value: mockResponse },
        { value: mockLinksData }
      ];

      var dispatchMock = function (action) {
        var expectedAction = expectedActions.shift();
        expect(action).to.deep.equal(expectedAction);
        if (!expectedAction.length) {
          done();
        }
      };

      actions.processResponse(mockResponse)
        (dispatchMock, widget.store.getState, widget);
    });

    it('processResponse handles empty response', function (done) {
      widget.activate(beehive);

      sandbox.stub(actions, 'handleError', function (message) {
        return { value: message }
      });

      var emptyResponseMock = {
        get: function () {
          return [];
        }
      };

      var expectedActions = [
        { type: 'UPDATE_API_RESPONSE', value: emptyResponseMock },
        { message: 'EMPTY_RESPONSE' }
      ];

      var dispatchMock = function (action) {
        var expectedAction = expectedActions.shift();
        expect(action).to.deep.equal(expectedAction);
        if (!expectedAction.length) {
          done();
        }
      };

      actions.processResponse(emptyResponseMock)
        (dispatchMock, widget.store.getState, widget);
    });

    it('parseResources correctly updates parsed data', function (done) {
      widget.activate(beehive);

      sandbox.stub(widget, 'parseResourcesData', function () {
        return mockLinksParsedData;
      });

      var expectedActions = [
        {
          type: 'UPDATE_RESOURCES',
          fullTextSources: mockLinksParsedData.fullTextSources,
          dataProducts: mockLinksParsedData.dataProducts
        },
        { type: 'IS_LOADING', value: 'false' }
      ];

      var dispatchMock = function (action) {
        var expectedAction = expectedActions.shift();
        expect(action).to.deep.equal(expectedAction);
        if (!expectedAction.length) {
          done();
        }
      };

      actions.parseResources(mockLinksData)
        (dispatchMock, widget.store.getState, widget);
    });

    it('renders fullTextSources component correctly', function () {
      widget.activate(beehive);
      var props = {
        sources: mockLinksParsedData.fullTextSources,
        onLinkClick: sandbox.spy()
      };

      var wrapNoProps = Enzyme.shallow(React.createElement(FullTextSources));
      var wrapWithProps = Enzyme.shallow(React.createElement(FullTextSources, props));
      
      // no sources, no output
      expect(wrapNoProps.find('div').exists()).to.be.false;

      // has sources, should return proper markup
      expect(wrapWithProps.find('div').exists()).to.be.true;
      
      // should have 3 links
      expect(wrapWithProps.find('a').length).to.equal(props.sources.length);

      wrapWithProps.find('a').forEach(function (node, idx) {
        var source = mockLinksParsedData.fullTextSources[idx];

        // check the name of the link
        expect(node.text()).to.have.string(source.title);

        // check the href of the link
        expect(node.prop('href')).to.equal(source.link);

        // check if the icons show up
        var renderedNode = node.render();
        if (source.openAccess || source.openUrl) {
          expect(renderedNode.find('i').length).to.be.gte(0);
        } else {
          expect(renderedNode.find('i').length).to.be.equal(0);
        }

        node.simulate('click');
      });

      var args = props.onLinkClick.args;
      expect(args.length).to.equal(props.sources.length);
      args.forEach(function (arg, idx) {
        var source = mockLinksParsedData.fullTextSources[idx];
        expect(arg[0]).to.equal(source.title);
      });
    });

    it('renders dataProducts component correctly', function () {
      widget.activate(beehive);
      var props = {
        products: mockLinksParsedData.dataProducts,
        onLinkClick: sandbox.spy()
      };

      var wrapNoProps = Enzyme.shallow(React.createElement(DataProducts));
      var wrapWithProps = Enzyme.shallow(React.createElement(DataProducts, props));
      
      // no products, no output
      expect(wrapNoProps.find('div').exists()).to.be.false;

      // has products, should return proper markup
      expect(wrapWithProps.find('div').exists()).to.be.true;
      
      // should have 3 links
      expect(wrapWithProps.find('a').length).to.equal(props.products.length);

      wrapWithProps.find('a').forEach(function (node, idx) {
        var source = mockLinksParsedData.dataProducts[idx];

        // check the name of the link
        expect(node.text()).to.have.string(source.title);

        // check the href of the link
        expect(node.prop('href')).to.equal(source.link);

        node.simulate('click');
      });

      var args = props.onLinkClick.args;
      expect(args.length).to.equal(props.products.length);
      args.forEach(function (arg, idx) {
        var source = mockLinksParsedData.dataProducts[idx];
        expect(arg[0]).to.equal(source.title);
      });
    });

    it('renders no sources correctly', function () {
      widget.activate(beehive);
      var props = {
        fullTextSources: [],
        dataProducts: [],
        isLoading: true
      };

      var wrapNoProps = Enzyme.shallow(React.createElement(App));
      var wrapWithProps = Enzyme.shallow(React.createElement(App, props));
    });

    it('renders Loading component correctly', function () {
      widget.activate(beehive);

      var wrapNoShow = Enzyme.shallow(React.createElement(LoadingIcon, {
        show: false 
      }));
      var wrapWithShow = Enzyme.shallow(React.createElement(LoadingIcon, { 
        show: true 
      }));
      
      // No output
      expect(wrapNoShow.find('span').exists()).to.be.false;

      // should return the proper output
      expect(wrapWithShow.find('span').exists()).to.be.true;
      expect(wrapWithShow.find('i').exists()).to.be.true;
    });
  });
});
