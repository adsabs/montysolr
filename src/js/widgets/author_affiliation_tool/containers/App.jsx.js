'use strict';
define([
  'underscore',
  'react',
  'redux',
  'react-redux',
  'es6!../actions/index',
  'es6!../constants/actionNames',
  'es6!../components/ExportFormatControl.jsx',
  'es6!../components/SelectionButtons.jsx',
  'es6!../components/Row.jsx',
  'es6!../components/Message.jsx',
  'es6!../components/Loading.jsx',
  'es6!../components/Closer.jsx',
], function (
  _, React, Redux, ReactRedux, actions, ACTIONS,
  ExportFormatControl, SelectionButtons, Row, Message, Loading, Closer
) {

  const makeOptions = (arr, allVal) => {
    return _.map(arr, (i) => {
      if (i === 'All') {
        return (<option key={allVal} value={allVal}>All</option>);
      }
      return (<option key={i} value={i}>{i}</option>);
    });
  };

  // actions
  const {
    toggleSelection,
    toggleAll,
    reset,
    doExport,
    closeWidget,
    updateYear,
    updateAuthor
  } = actions;

  /**
   * Main component
   *
   * This is a container component, it is the only type of component
   * that is connected directly to the store.  All dispatches and
   * state changes should happen here.
   */
  class App extends React.Component {

    /**
     * Start the export
     */
    doExport() {
      const { dispatch } = this.props;
      dispatch(doExport());
    }

    /**
     * On new format selection, dispatch an update to the store
     *
     * @param {string} value
     */
    onFormatSelection(value) {
      const { dispatch } = this.props;
      dispatch({ type: ACTIONS.setFormat, value });
    }

    /**
     * On new selection (toggle all, reset, etc.) make the corresponding dispatch
     *
     * @param {string} type
     */
    onSelectionClick(type) {
      const { dispatch } = this.props;
      switch(type) {
        case 'toggleall': return dispatch(toggleAll());
        case 'reset': return dispatch(reset());
        default: return;
      }
    }

    /**
     * Dispatch close when we want to close the widget
     */
    onCloseClick() {
      const { dispatch } = this.props;
      dispatch(closeWidget());
    }

    /**
     * When a checkbox is toggled, dispatch an event
     *
     * @param {object} authorData
     * @param {object} affData
     */
    onCheckboxChange(authorData, affData) {
      const { dispatch } = this.props;
      dispatch(toggleSelection(authorData, affData));
    }

    onYearChange(year) {
      const { dispatch } = this.props;
      dispatch(updateYear(year));
    }

    onAuthorChange(author) {
      const { dispatch } = this.props;
      dispatch(updateAuthor(author));
    }

    render() {
      const {
        data, formats, format, message, loading, exporting, year, author, currentYear
      } = this.props;
      return (
        <div>

          {/* Close widget button */}
          <Closer onClick={() => this.onCloseClick()}/>

          {/* Main Container */}
          <div className="container auth-aff-tool">
            <div className="row" style={{ marginTop: 40 }}>

              {/* Only show title banner if we are not loading */}
              {!loading &&
                <div>
                  <h4 className="col-xs-12 col-sm-6" style={{ marginTop: 0 }}>
                    Viewing Affiliation Data For <strong>{data.length}</strong> Authors<br/>
                    <small>
                      {((year !== 10000) ? `From ${currentYear - year} to ${currentYear}` : '')}
                      {(year !== 10000 && author !== 0) && ' | ' }
                      {((author !== 0) ? `${author} authors from each work` : '')}
                    </small>
                  </h4>
                  <div className="col-xs-12 col-sm-6">
                    <div className="col-xs-12 col-sm-offset-4 col-sm-8 no-padding-right">
                      <div className="col-xs-3 text-right" style={{ marginTop: 4 }}>
                        <label htmlFor="max-author-select">Authors:</label>
                      </div>
                      <div className="col-xs-3 no-padding-right">
                        <select
                          id="max-author-select"
                          className="form-control input-sm"
                          title="Select the number of authors from each article to be included (default is 4)"
                          value={author}
                          onChange={val => this.onAuthorChange(val.target.value)}
                        >
                          {makeOptions([1, 2, 3, 4, 5, 10, 'All'], 0)}
                        </select>
                      </div>
                      <div className="col-xs-3 text-right" style={{ marginTop: 4 }}>
                        <label htmlFor="year-select">Years:</label>
                      </div>
                      <div className="col-xs-3 no-padding-right">
                        <select
                          id="year-select"
                          className="form-control input-sm"
                          title="Select the number of years to include in the results (default is 4)"
                          value={year}
                          onChange={val => this.onYearChange(val.target.value)}
                        >
                          {makeOptions([1, 2, 3, 4, 5, 10, 'All'], 10000)}
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              }

              {/* Error message component */}
              <Message {...message}/>
            </div>

            {/* Loading area */}
            {loading ?

              // show loading screen (spinning icon)
              <Loading/>
              :

              // if not loading, we can show the top/bottom bar
              <div>
                <div className="row">
                  <div className="col-xs-12 col-sm-6">

                    {/* Export format selector (dropdown) */}
                    <ExportFormatControl
                      formats={formats}
                      format={format}
                      onChange={val => this.onFormatSelection(val)}
                    />

                  </div>
                  <div className="col-xs-12 col-sm-2">
                    <button
                      className="btn btn-primary"
                      onClick={() => this.doExport()}
                      disabled={!!exporting}
                    >
                      {/* If exporting, show a loading icon in the button */}
                      {
                        exporting ?
                          <i className="fa fa-spinner fa-fw fa-spin"/>
                        : 'Export'
                      }
                    </button>
                  </div>
                  <div className="col-xs-12 col-sm-4">

                    {/* Buttons that perform actions on the whole set */}
                    <SelectionButtons
                      onClick={(type) => this.onSelectionClick(type)}
                    />

                  </div>
                </div>

                {/* Start of main content (table) */}
                <div className="row well">
                  <div className="row auth-aff-heading">
                    <div className="col-xs-2">Author</div>
                    <div className="col-xs-8">
                      <div className="col-xs-8">Affiliations</div>
                      <div className="col-xs-4">Years</div>
                    </div>
                    <div className="col-xs-2">Last Active Date</div>
                  </div>
                  <hr className="hr"/>

                  {/* Map the data here into rows */}
                  {_.map(data, (d, i) =>
                    <div key={d.id}>
                      <Row data={d} onChange={(el) => this.onCheckboxChange(d, el)} />
                      {(data.length - 1 > i) && <hr className="hr"/>}
                    </div>
                  )}

                </div>

                {/* Repeat the message above */}
                <div className="row">
                  <Message {...message}/>
                </div>
                <div className="row">
                  <div className="col-xs-12 col-sm-6">

                    {/* Export format selector (dropdown) */}
                    <ExportFormatControl
                      formats={formats}
                      format={format}
                      onChange={val => this.onFormatSelection(val)}
                    />

                  </div>
                  <div className="col-xs-12 col-sm-2">
                    <button
                      className="btn btn-primary"
                      onClick={() => this.doExport()}
                      disabled={!!exporting}
                    >
                      {/* If exporting, show a loading icon in the button */}
                      {
                        exporting ?
                          <i className="fa fa-spinner fa-fw fa-spin"/>
                          : 'Export'
                      }
                    </button>
                  </div>
                  <div className="col-xs-12 col-sm-4">

                    {/* Buttons that perform actions on the whole set */}
                    <SelectionButtons
                      onClick={(type) => this.onSelectionClick(type)}
                    />

                  </div>
                </div>
              </div>
            }
          </div>
        </div>
      );
    }
  }

  const mapStateToProps = (state) => ({
    data: state.data,
    formats: state.formats,
    format: state.format,
    message: state.message,
    loading: state.loading,
    exporting: state.exporting,
    currentYear: state.currentYear,
    year: state.year,
    author: state.author
  });

  return ReactRedux.connect(mapStateToProps)(App);
});
