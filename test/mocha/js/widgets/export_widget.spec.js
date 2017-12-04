'use strict';
define([
  'module',
  'underscore',
  'jquery',
  'react',
  'redux',
  'redux-thunk',
  'redux-mock-store',
  'enzyme',
  'js/widgets/base/base_widget',
  'es6!js/widgets/export/reducers/index',
  'es6!js/widgets/export/actions/index',
  'es6!js/widgets/export/widget.jsx',
  'es6!js/widgets/export/components/Closer.jsx',
  'es6!js/widgets/export/components/ClipboardBtn.jsx',
  'es6!js/widgets/export/components/Export.jsx',
  'es6!js/widgets/export/components/Setup.jsx',
  'es6!js/widgets/export/containers/App.jsx',
  'js/bugutils/minimal_pubsub',
  'js/components/json_response'
], function (
  module, _, $, React, Redux, ReduxThunk, configureStore, Enzyme, BaseWidget,
  reducers, actions, ExportWidget, Closer, ClipboardBtn, Export, Setup, App,
  MinPubSub, JSONResponse
) {
  var timeout = 3000000;

  var desc = function (underTest, description, cb) {
    underTest = '[' + underTest + ']';
    describe(underTest + ' - ' + description, cb);
  };

  var mockResponse = {
    "responseHeader": {
      "status": 0, "QTime": 660,
      "params": {
        "q": "dark matter",
        "fl": "bibcode",
        "sort": "date desc, bibcode desc",
        "rows": "500",
        "wt": "json"
      }
    },
    "response": {
      "numFound": 268110, "start": 0, "docs": [
        {"bibcode": "2018NewA...60...69B"},
        {"bibcode": "2018NewA...60...48C"},
        {"bibcode": "2018NewA...60....1P"},
        {"bibcode": "2018CNSNS..57..276X"},
        {"bibcode": "2018CNSNS..57...26D"}
      ]
    }
  };

  var withServer = function (minsub) {
    var server = sinon.fakeServer.create();
    var reqCache = [];

    server.respondWith('POST', /export\/(.*)/, function (xhr, endpoint) {
      xhr.respond(200, {
        'Content-Type': 'application/json'
      }, JSON.stringify({ export: 'got call for ' + endpoint }));
    });

    server.respondWith('GET', /search\/query/, function (xhr) {
      xhr.respond(200, {
        'Content-Type': 'application/json'
      }, JSON.stringify(mockResponse));
    });

    var sendRequest = function (url, options, data) {
      var $dd = $.Deferred();

      $.ajax(url, _.extend(options, {
        data: JSON.stringify(data),
        dataType: 'json',
        headers: {
          'Content-Type': 'application/json'
        }
      }))
      .done(function () {
        options && options.done && options.done.apply(this, arguments);
        $dd.resolve.apply($dd, arguments);
      })
      .fail(function () {
        options && options.fail && options.fail.apply(this, arguments);
        $dd.reject.apply($dd, arguments);
      });

      server.respond();
      return $dd.promise().done(function (res) {
        reqCache.pop().call(null, new JSONResponse(res));
      });
    };

    minsub.request = _.constant('');

    sinon.stub(minsub.pubsub, 'subscribeOnce', function (key, action, cb) {
      reqCache.push(cb);
    });

    sinon.stub(minsub.pubsub, 'publish', function (key, action, apiRequest) {
      var url = apiRequest.get('target');
      var options = apiRequest.get('options');
      var data = (options && options.data) || {};
      return sendRequest(url, options, data);
    });

    return minsub;
  };

  var shallowWithStore = function (component, store) {
    var context = { store: store };

    return Enzyme.shallow(React.createElement(component), { context: context });
  };

  var wrapContains = function (ctx, type, str) {
    return ctx.find(type).findWhere(function (n) {
      return n.type() === type && n.text().indexOf(str) > -1;
    });
  };

  var init = function () {
    this.sb = sinon.sandbox.create();
    this.w = new ExportWidget();
    var minsub = withServer(new MinPubSub());
    this.w.activate(minsub.beehive.getHardenedInstance());
    this.mockStore = configureStore.default([]);
  };

  var test = function () {

    desc('Closer', 'Dumb Component', function () {
      beforeEach(init);

      it('renders correctly', function () {
        var props = { onClick: _.noop };
        var wrapper = Enzyme.shallow(React.createElement(Closer, props));
        expect(wrapper.find('a').exists()).to.eql(true);
      });

      it('clicking the link fires passed in handler', function () {
        var props = { onClick: sinon.spy() };
        var wrapper = Enzyme.shallow(React.createElement(Closer, props));
        wrapper.find('a').simulate('click', { preventDefault: _.noop });
        expect(props.onClick.calledOnce).to.eql(true);
      });
    });

    desc('Export', 'Dumb Component', function () {
      beforeEach(init);

      it('renders correctly', function () {
        var props = {
          output: '',
          isFetching: false,
          onDownloadFile: sinon.spy(),
          onCopy: sinon.spy()
        };
        var wrapper = Enzyme.shallow(React.createElement(Export, props));
        expect(wrapper.find('textarea').exists()).to.eql(true);
        var buttons = wrapper.find('button');
        expect(wrapper.wrap(buttons.get(0)).text()).to.have.string('Download');
        expect(wrapper.find(ClipboardBtn).exists()).to.eql(true);
      });

      it('shows progress bar when fetching', function () {
        var props = {
          output: '',
          isFetching: true,
          progress: 0,
          onDownloadFile: sinon.spy(),
          onCopy: sinon.spy()
        };
        var wrapper = Enzyme.shallow(React.createElement(Export, props));
        var textArea = wrapper.find('textarea');
        var buttons = wrapper.find('button');
        var progress = wrapper.find('div.progress-bar');
        expect(textArea.prop('disabled')).to.eql(true);
        expect(wrapper.wrap(buttons.get(0)).prop('disabled')).to.eql(true);
        expect(progress.exists()).to.eql(true);
      });

      it('updates progress bar', function () {
        var props = {
          output: '',
          isFetching: true,
          progress: 0,
          onDownloadFile: sinon.spy(),
          onCopy: sinon.spy()
        };
        var wrapper = Enzyme.shallow(React.createElement(Export, props));
        var progress = wrapper.find('div.progress-bar');
        expect(progress.prop('style').width).to.equal('0%');
        wrapper = Enzyme.shallow(React.createElement(Export, _.assign({}, props, { progress: 25 })));
        progress = wrapper.find('div.progress-bar');
        expect(progress.prop('style').width).to.equal('25%');
      });
    });

    desc('Setup', 'Dumb Component', function () {
      beforeEach(init);

      it('renders correctly', function () {
        var props = {
          formats: [{ value: 'test', label: 'TEST', id: '0' }],
          format: { value: 'test', label: 'TEST', id: '0' },
          setFormat: sinon.spy(),
          onApply: sinon.spy(),
          onCancel: sinon.spy(),
          count: '0',
          setCount: sinon.spy(),
          maxCount: 0,
          onGetNext: sinon.spy(),
          totalRecs: 0,
          onReset: sinon.spy(),
          showSlider: true,
          showReset: true,
          disabled: false
        };
        var wrapper = Enzyme.shallow(React.createElement(Setup, props));
        var dropdown = wrapper.find('select');
        var applyButton = wrapper.find('button');

        expect(dropdown.exists()).to.eql(true);
        expect(applyButton.exists()).to.eql(true);

        var options = dropdown.find('option');
        var option = wrapper.wrap(options.get(0));

        expect(options.length).to.eql(1);
        expect(option.props()).to.eql({ value: '0', children: 'TEST' });
      });

      it('disables elements correctly', function () {
        var props = {
          formats: [{ value: 'test', label: 'TEST', id: '0' }],
          format: { value: 'test', label: 'TEST', id: '0' },
          setFormat: sinon.spy(),
          onApply: sinon.spy(),
          onCancel: sinon.spy(),
          count: '0',
          setCount: sinon.spy(),
          maxCount: 0,
          onGetNext: sinon.spy(),
          totalRecs: 0,
          onReset: sinon.spy(),
          showSlider: true,
          showReset: true,
          disabled: true
        };
        var wrapper = Enzyme.shallow(React.createElement(Setup, props));
        var dropdown = wrapper.find('select');
        var applyBtn = wrapContains(wrapper, 'button', 'Apply');
        var resetBtn = wrapContains(wrapper, 'button', 'Reset');
        var getNextBtn = wrapContains(wrapper, 'button', 'Get Next');
        var cancelBtn = wrapContains(wrapper, 'button', 'Cancel');

        expect(dropdown.prop('disabled')).to.eql(true);
        expect(applyBtn.prop('disabled')).to.eql(true);
        expect(resetBtn.exists()).to.eql(false);
        expect(getNextBtn.exists()).to.eql(false);
        expect(cancelBtn.exists()).to.eql(true);
      });

      it('fires handler on change', function () {
        var props = {
          formats: [{ value: 'test', label: 'TEST', id: '0' }],
          format: { value: 'test', label: 'TEST', id: '0' },
          setFormat: sinon.spy(),
          onApply: sinon.spy(),
          onCancel: sinon.spy(),
          count: '0',
          setCount: sinon.spy(),
          maxCount: 0,
          onGetNext: sinon.spy(),
          totalRecs: 0,
          onReset: sinon.spy(),
          showSlider: true,
          showReset: true,
          disabled: false
        };
        var wrapper = Enzyme.shallow(React.createElement(Setup, props));
        var dropdown = wrapper.find('select');
        dropdown.simulate('change', { target: { value: '1' } });
        expect(props.setFormat.calledOnce).to.eql(true);
      });

      it('fires handler on click', function () {
        var props = {
          formats: [{ value: 'test', label: 'TEST', id: '0' }],
          format: { value: 'test', label: 'TEST', id: '0' },
          setFormat: sinon.spy(),
          onApply: sinon.spy(),
          onCancel: sinon.spy(),
          count: '0',
          setCount: sinon.spy(),
          maxCount: 0,
          onGetNext: sinon.spy(),
          totalRecs: 0,
          onReset: sinon.spy(),
          showSlider: true,
          showReset: true,
          disabled: false
        };
        var wrapper = Enzyme.shallow(React.createElement(Setup, props));
        var applyBtn = wrapContains(wrapper, 'button', 'Apply');
        applyBtn.simulate('click');
        expect(props.onApply.calledOnce).to.eql(true);
      });
    });

    desc('ClipboardBtn', 'Copy to clipboard button', function () {
      beforeEach(init);

      it('renders correctly', function () {
        var props = {
          disabled: false,
          target: '',
          onCopy: sinon.spy()
        };
        var wrapper = Enzyme.shallow(React.createElement(ClipboardBtn, props));
        var btn = wrapper.find('button');
        expect(btn.exists()).to.eql(true);
        expect(btn.text()).to.have.string('Copy');
      });
    });

    desc('Widget', 'Main Widget', function () {
      beforeEach(init);

      it('extends base widget', function () {
        expect(this.w instanceof BaseWidget).to.eql(true);
      });

      it('state is updated after render for query', function () {
        this.w.renderWidgetForCurrentQuery({
          currentQuery: { toJSON: _.constant({ q: 'star' })},
          numFound: 268110,
          format: 'bibtex'
        });
        var state = this.w.store.getState();
        expect(state.exports).to.eql({
          "isFetching": false,
          "output": "got call for bibtex",
          "progress": 100,
          "ids": [
            "2018NewA...60...69B",
            "2018NewA...60...48C",
            "2018NewA...60....1P",
            "2018CNSNS..57..276X",
            "2018CNSNS..57...26D"
          ],
          "count": 500,
          "page": 0,
          "maxCount": 500,
          "batchSize": 500,
          "ignore": false,
          "totalRecs": 268110,
          "snapshot": {
            "isFetching": false,
            "output": "got call for bibtex",
            "progress": 100,
            "ids": [
              "2018NewA...60...69B",
              "2018NewA...60...48C",
              "2018NewA...60....1P",
              "2018CNSNS..57..276X",
              "2018CNSNS..57...26D"
            ],
            "count": 500,
            "page": 0,
            "maxCount": 500,
            "batchSize": 500,
            "ignore": false,
            "totalRecs": 268110
          }
        });
      });

      it('state is updated after render for bibcodes', function () {
        this.w.renderWidgetForListOfBibcodes([
          "2018NewA...60...69B",
          "2018NewA...60...48C",
          "2018NewA...60....1P",
          "2018CNSNS..57..276X",
          "2018CNSNS..57...26D"
        ], { format: 'aastex' });
        var state = this.w.store.getState();
        expect(state.exports).to.eql({
          "isFetching": false,
          "output": "got call for aastex",
          "progress": 100,
          "ids": [
            "2018NewA...60...69B",
            "2018NewA...60...48C",
            "2018NewA...60....1P",
            "2018CNSNS..57..276X",
            "2018CNSNS..57...26D"
          ],
          "count": 5,
          "page": 0,
          "maxCount": 500,
          "batchSize": 500,
          "ignore": false,
          "totalRecs": 5,
          "snapshot": {
            "isFetching": false,
            "output": "got call for aastex",
            "progress": 100,
            "ids": [
              "2018NewA...60...69B",
              "2018NewA...60...48C",
              "2018NewA...60....1P",
              "2018CNSNS..57..276X",
              "2018CNSNS..57...26D"
            ],
            "count": 5,
            "page": 0,
            "maxCount": 500,
            "batchSize": 500,
            "ignore": false,
            "totalRecs": 5
          }
        });
      });
    });
  };

  describe('Export Widget (' + module.id.replace(/^.*[\\\/]/, '') + ')', sinon.test(test));
});
