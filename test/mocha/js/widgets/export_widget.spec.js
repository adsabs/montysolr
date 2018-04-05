'use strict';
define([
  'module',
  'underscore',
  'jquery',
  'react',
  'redux',
  'redux-thunk',
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
  module, _, $, React, Redux, ReduxThunk, Enzyme, BaseWidget,
  reducers, actions, ExportWidget, Closer, ClipboardBtn, Export, Setup, App,
  MinPubSub, JSONResponse
) {

  const mockProps = {
    setup: {
      formats: [{value: 'test', label: 'TEST', id: '0', help: 'help'}],
      format: {value: 'test', label: 'TEST', id: '0', help: 'help'},
      count: '0',
      maxCount: 0,
      totalRecs: 0,
      showSlider: true,
      showReset: true,
      disabled: false,
      setFormat: _.noop,
      onApply: _.noop,
      onCancel: _.noop,
      setCount: _.noop,
      onGetNext: _.noop,
      onReset: _.noop
    },
    clipboard: {
      disabled: false,
      target: '',
      onCopy: _.noop
    }
  };

  const wrapContains = function (ctx, type, str) {
    return ctx.find(type).findWhere(function (n) {
      return n.type() === type && n.text().indexOf(str) > -1;
    });
  };

  const init = function () {
    this.sb = sinon.sandbox.create();
    this.request = this.sb.stub();
    this.pubsub = new (MinPubSub.extend({
      request: this.request
    }))({ verbose: false });
    this.beehive = this.pubsub.beehive.getHardenedInstance();
  };

  const teardown = function () {
    this.sb.restore();
    this.pubsub.destroy();
    this.pubsub = null;
    this.beehive = null;
  };

  describe('Export Widget, (export_widget.spec.js)', function () {
    describe('Closer Component', function () {
      beforeEach(init);
      afterEach(teardown);
      it('Renders Correctly', function (done) {
        const props = { onClick: _.noop };
        const wrap = Enzyme.shallow(React.createElement(Closer, props));
        expect(wrap.find('a').exists()).to.eql(true);
        done();
      });
      it('Fires handler on click', function (done) {
        const props = { onClick: this.sb.spy() };
        const wrapper = Enzyme.shallow(React.createElement(Closer, props));
        wrapper.find('a').simulate('click', { preventDefault: _.noop });
        expect(props.onClick.calledOnce).to.eql(true);
        done();
      });
    });
    describe('Export Component', function () {
      beforeEach(init);
      afterEach(teardown);
      it('Renders Correctly', function (done) {
        const props = {
          output: '',
          isFetching: false
        };
        const wrapper = Enzyme.shallow(React.createElement(Export, props));
        expect(wrapper.find('textarea').exists()).to.eql(true);
        const buttons = wrapper.find('button');
        expect(wrapper.wrap(buttons.get(0)).text()).to.have.string('Download');
        expect(wrapper.find(ClipboardBtn).exists()).to.eql(true);
        done();
      });
      it('Displays the content passed to it', function (done) {
        const props = {
          output: 'TESTING',
          isFetching: false
        };
        const wrap = Enzyme.shallow(React.createElement(Export, props));
        expect(wrap.find('textarea').exists()).to.eql(true);
        expect(wrap.find('textarea').get(0).props.value).to.eql(props.output);
        done();
      });
    });
    describe('Setup Component', function () {
      beforeEach(init);
      afterEach(teardown);
      it('Renders Correctly', function (done) {
        const props = _.assign(mockProps.setup, {});
        const wrap = Enzyme.shallow(React.createElement(Setup, props));
        const dropdown = wrap.find('select');
        const applyButton = wrap.find('button');

        expect(dropdown.exists()).to.eql(true);
        expect(applyButton.exists()).to.eql(true);

        const options = dropdown.find('option');
        const option = wrap.wrap(options.get(0));

        expect(options.length).to.eql(1);
        expect(option.props()).to.eql({value: '0', children: 'TEST', title: 'help' });
        done();
      });
      it('Disables elements correctly', function (done) {
        const props = _.assign(mockProps.setup, {
          disabled: true
        });
        const wrapper = Enzyme.shallow(React.createElement(Setup, props));
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
        done();
      });
      it('Fires handler on change', function (done) {
        const props = _.assign(mockProps.setup, {
          setFormat: this.sb.spy()
        });
        const wrapper = Enzyme.shallow(React.createElement(Setup, props));
        const dropdown = wrapper.find('select');
        dropdown.simulate('change', {target: {value: '1'}});
        expect(props.setFormat.calledOnce).to.eql(true);
        done();
      });
      it('Fires handler on click', function (done) {
        const props = _.assign(mockProps.setup, {
          onApply: this.sb.spy()
        });
        const wrapper = Enzyme.shallow(React.createElement(Setup, props));
        const applyBtn = wrapContains(wrapper, 'button', 'Apply');
        applyBtn.simulate('click');
        expect(props.onApply.calledOnce).to.eql(true);
        done();
      });
    });
    describe('ClipboardBtn Component', function () {
      beforeEach(init);
      afterEach(teardown);
      it('Renders correctly', function (done) {
        const props = _.assign(mockProps.clipboard, {});
        const wrapper = Enzyme.shallow(React.createElement(ClipboardBtn, props));
        const btn = wrapper.find('button');
        expect(btn.exists()).to.eql(true);
        expect(btn.text()).to.have.string('Copy');
        done();
      });
      it('sets target correctly', function (done) {
        const props = _.assign(mockProps.clipboard, {
          target: 'test'
        });
        const wrapper = Enzyme.shallow(React.createElement(ClipboardBtn, props));
        const btn = wrapper.find('button');
        expect(btn.get(0).props['data-clipboard-target']).to.eql(props.target);
        done();
      });
      it('Is disabled if passed the prop', function (done) {
        const props = _.assign(mockProps.clipboard, {
          disabled: true
        });
        const wrapper = Enzyme.shallow(React.createElement(ClipboardBtn, props));
        const btn = wrapper.find('button');
        expect(btn.get(0).props.disabled).to.eql(true);
        done();
      });
    });
    describe('Main Component', function () {
      beforeEach(init);
      afterEach(teardown);
      it('Extends base widget', function (done) {
        const w = new ExportWidget();
        expect(w instanceof BaseWidget).to.eql(true);
        done();
      });
      it('State is updated after render for query', function (done) {
        const w = new ExportWidget();
        const mocks = {
          requestOne: {
            response: {
              docs: _.map(_.range(5), function (d, i) {
                return { bibcode: 'test_' + i };
              }),
              numFound: 5
            }
          },
          requestTwo: { export: 'TEST' },
          forCurrentQuery: {
            currentQuery: { toJSON: _.constant({ q: 'star' }) },
            numFound: 5,
            format: 'bibtex'
          }
        };
        this.request.onFirstCall().returns(mocks.requestOne);
        this.request.onSecondCall().returns(mocks.requestTwo);
        w.activate(this.beehive);
        w.renderWidgetForCurrentQuery(mocks.forCurrentQuery);
        const expected = {
          count: 5,
          ids: ['test_0', 'test_1', 'test_2', 'test_3', 'test_4'],
          output: 'TEST',
          isFetching: false
        };
        const actual = _.pick(w.store.getState().exports, _.keys(expected));
        expect(expected).to.deep.equal(actual);
        done();
      });
      it('State is updated after render for bibcodes', function (done) {
        const w = new ExportWidget();
        const mocks = {
          request: { export: 'TEST' },
          forBibcodes: {
            recs: ['test_0', 'test_1', 'test_2', 'test_3', 'test_4'],
            data: { format: 'bibtex' }
          }
        };
        this.request.returns(mocks.request);
        w.activate(this.beehive);
        w.renderWidgetForListOfBibcodes(mocks.forBibcodes.recs, mocks.forBibcodes.data);
        const expected = {
          count: 5,
          ids: ['test_0', 'test_1', 'test_2', 'test_3', 'test_4'],
          output: 'TEST',
          isFetching: false
        };
        const actual = _.pick(w.store.getState().exports, _.keys(expected));
        expect(expected).to.deep.equal(actual);
        done();
      });
    });
  });
});
