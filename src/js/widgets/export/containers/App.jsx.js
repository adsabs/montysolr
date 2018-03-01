'use strict';

define([
  'underscore',
  'react', 'react-redux',
  'react-prop-types', '../actions/index',
  'es6!../components/Closer.jsx',
  'es6!../components/Setup.jsx',
  'es6!../components/Export.jsx'
], function (
  _, React, ReactRedux, ReactPropTypes,
  actions, Closer, Setup, Export
) {

  const {
    closeComponent,
    setFormat,
    getNextBatch,
    fetchUsingQuery,
    fetchUsingIds,
    setCount,
    cancelRequest,
    reset,
    downloadFile
  } = actions;

  class App extends React.Component {
    constructor(props) {
      super(props);
      _.bindAll(this, [
        'handleCloseClick',
        'handleApplyClick',
        'handleFormatChange',
        'handleCountChange',
        'handleCancelClick',
        'handleGetNextClick',
        'handleResetClick',
        'handleDownloadFileClick',
        'onCopyText'
      ]);

      /**
       * The count update is debounced, to make sure that lots of changes don't
       * send many requests.
       */
      this.updateCount = _.debounce((val) => {
        const { dispatch } = this.props;
        dispatch(setCount(parseInt(val)));
      }, 500);

      this.state = {
        count: '0',
        showAlert: false
      };
    }

    /**
     * On file download click, dispatch the download file action
     */
    handleDownloadFileClick () {
      this.props.dispatch(downloadFile());
    }

    /**
     * On copy button click, show a message which dissappears after 5 seconds
     */
    onCopyText() {
      this.setState({
        showAlert: true,
        alertMsg: 'Text Copied!'
      });
      _.delay(() => this.setState({ showAlert: false }), 5000);
    }

    /**
     * On close click, close the widget
     */
    handleCloseClick() {
      this.props.dispatch(closeComponent());
    }

    /**
     * On cancel, attempt to cancel request
     *
     * -- this mainly resets the form and ignores the pending request
     */
    handleCancelClick() {
      this.props.dispatch(cancelRequest());
    }

    /**
     * When the count is updated, update the state accordingly
     *
     * @param {number} val - the count
     */
    handleCountChange(val) {
      this.setState({ count: val }, () => this.updateCount(val));
    }

    /**
     * On Apply, the export process is begun
     */
    handleApplyClick() {
      const { dispatch, ids, query } = this.props;

      if (_.isEmpty(query) && !_.isEmpty(ids)) {
        dispatch(fetchUsingIds());
      } else {
        dispatch(fetchUsingQuery()).done(() => dispatch(fetchUsingIds()));
      }
    }

    /**
     * Dispatch a form reset
     */
    handleResetClick() {
      this.props.dispatch(reset());
    }

    /**
     * Update the format on the state when the user selects a new one
     *
     * @param {string} id - format id
     */
    handleFormatChange(id) {
      const { dispatch, formats, autoSubmit } = this.props;
      let format = _.find(formats, { id: id });
      dispatch(setFormat(format));

      // if autoSubmit, then hit apply as the format changes
      autoSubmit && this.handleApplyClick();
    }

    /**
     * Get the next set of items
     */
    handleGetNextClick() {
      const { dispatch } = this.props;
      dispatch(getNextBatch());
    }

    componentWillReceiveProps (next) {
      let remaining = next.totalRecs - next.maxCount;
      this.setState({
        count: '' + next.count,
        hasMore: remaining > 0,
        remaining: remaining > next.batchSize ? next.batchSize : remaining
      });
    }

    render() {
      const {
        format, formats, isFetching, output, batchSize, showCloser, showReset,
        progress, maxCount, hasError, errorMsg, totalRecs, showSlider, splitCols,
        autoSubmit
      } = this.props;
      const { count, hasMore, showAlert, alertMsg, remaining } = this.state;

      const lower = maxCount - batchSize;
      const upper = Number(count) + (maxCount - batchSize);

      return (
        <div className="container-fluid export-container">
          <span>
            {showCloser &&
              <Closer onClick={this.handleCloseClick}/>
            }
            <div className="h4">
              Exporting record(s) <strong>{lower}</strong> to <strong>{upper}</strong> <small>(total: {totalRecs})</small>
            </div>
          </span>
          <div>
            <div className={splitCols ? 'col-sm-6' : 'col-sm-12'}>
              <Setup
                format={format}
                formats={formats}
                disabled={isFetching}
                count={count}
                maxCount={maxCount}
                totalRecs={totalRecs}
                batchSize={batchSize}
                hasMore={hasMore}
                showSlider={showSlider}
                showReset={showReset}
                autoSubmit={autoSubmit}
                remaining={remaining}
                onReset={this.handleResetClick}
                onApply={this.handleApplyClick}
                onCancel={this.handleCancelClick}
                setFormat={this.handleFormatChange}
                onGetNext={this.handleGetNextClick}
                setCount={this.handleCountChange}
              />
              {hasError &&
              <div className="row">
                <div className="col-sm-10">
                  <div className="alert alert-danger">{errorMsg}</div>
                </div>
              </div>
              }

              {showAlert &&
              <div className="row">
                <div className="col-sm-10">
                  <div className="alert alert-info">{alertMsg}</div>
                </div>
              </div>
              }
            </div>
            <div className={splitCols ? 'col-sm-6' : 'col-sm-12'}>
              <Export
                output={output}
                isFetching={isFetching}
                progress={progress}
                onDownloadFile={this.handleDownloadFileClick}
                onCopy={this.onCopyText}
              />
            </div>
          </div>
        </div>
      );
    };
  }

  App.propTypes = {
    dispatch: ReactPropTypes.func.isRequired,
    format: ReactPropTypes.shape({
      id: ReactPropTypes.string,
      value: ReactPropTypes.string,
      label: ReactPropTypes.string
    }).isRequired,
    formats: ReactPropTypes.arrayOf(ReactPropTypes.shape({
      id: ReactPropTypes.string,
      value: ReactPropTypes.string,
      label: ReactPropTypes.string
    })).isRequired,
    isFetching: ReactPropTypes.bool.isRequired,
    output: ReactPropTypes.string.isRequired,
    progress: ReactPropTypes.number.isRequired,
    count: ReactPropTypes.number.isRequired,
    maxCount: ReactPropTypes.number.isRequired,
    hasError: ReactPropTypes.bool.isRequired,
    errorMsg: ReactPropTypes.string.isRequired,
    batchSize: ReactPropTypes.number.isRequired,
    totalRecs: ReactPropTypes.number.isRequired,
    showCloser: ReactPropTypes.bool.isRequired,
    showSlider: ReactPropTypes.bool.isRequired,
    splitCols: ReactPropTypes.bool.isRequired,
    showReset: ReactPropTypes.bool.isRequired,
    autoSubmit: ReactPropTypes.bool.isRequired
  };

  const mapStateToProps = state => ({
    format: state.format,
    formats: state.formats,
    output: state.exports.output,
    isFetching: state.exports.isFetching,
    progress: state.exports.progress,
    count: state.exports.count,
    maxCount: state.exports.maxCount,
    hasError: state.error.hasError,
    errorMsg: state.error.errorMsg,
    batchSize: state.exports.batchSize,
    totalRecs: state.exports.totalRecs,
    showCloser: state.main.showCloser,
    showSlider: state.main.showSlider,
    autoSubmit: state.main.autoSubmit,
    splitCols: state.main.splitCols,
    showReset: state.main.showReset,
    ids: state.exports.ids,
    query: state.main.query
  });

  return ReactRedux.connect(mapStateToProps)(App);
});
