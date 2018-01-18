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

  const {
    toggleSelection,
    toggleAll,
    reset,
    doExport,
    closeWidget
  } = actions;

  class App extends React.Component {
    doExport() {
      const { dispatch } = this.props;
      dispatch(doExport());
    }

    onFormatSelection(value) {
      const { dispatch } = this.props;
      dispatch({ type: ACTIONS.setFormat, value });
    }

    onSelectionClick(type) {
      const { dispatch } = this.props;
      switch(type) {
        case 'toggleall': return dispatch(toggleAll());
        case 'reset': return dispatch(reset());
        default: return;
      }
    }

    onCloseClick() {
      const { dispatch } = this.props;
      dispatch(closeWidget());
    }

    onCheckboxChange(authorData, affData) {
      const { dispatch } = this.props;
      dispatch(toggleSelection(authorData, affData));
    }

    render() {
      const { data, formats, format,
        count, message, loading, exporting } = this.props;
      return (
        <div>
          <Closer onClick={() => this.onCloseClick()}/>
          <div className="container auth-aff-tool">
            <div className="row" style={{ marginTop: 10 }}>
              {!loading &&
                <div className="col-xs-12" style={{ fontSize: 24 }}>
                  Viewing Author Affiliation Data for <strong>{count}</strong> Records
                </div>
              }
              <Message {...message}/>
            </div>

            {loading ?
              <Loading/>
              :
              <div>
                <div className="row">
                  <div className="col-xs-6">
                    <ExportFormatControl
                      formats={formats}
                      format={format}
                      onChange={val => this.onFormatSelection(val)}
                    />
                  </div>
                  <div className="col-xs-2">
                    <button
                      className="btn btn-primary"
                      onClick={() => this.doExport()}
                      disabled={!!exporting}
                    >
                      {
                        exporting ?
                          <i className="fa fa-spinner fa-fw fa-spin"/>
                        : 'Export'
                      }
                    </button>
                  </div>
                  <div className="col-xs-4">
                    <SelectionButtons
                      onClick={(type) => this.onSelectionClick(type)}
                    />
                  </div>
                </div>
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
                  {_.map(data, (d, i) =>
                    <div key={d.id}>
                      <Row data={d} onChange={(el) => this.onCheckboxChange(d, el)} />
                      {(data.length - 1 > i) && <hr className="hr"/>}
                    </div>
                  )}
                </div>
                <div className="row">
                  <Message {...message}/>
                </div>
                <div className="row">
                  <div className="col-xs-6">
                    <ExportFormatControl
                      formats={formats}
                      format={format}
                      onChange={val => this.onFormatSelection(val)}
                    />
                  </div>
                  <div className="col-xs-2">
                    <button
                      className="btn btn-primary"
                      onClick={() => this.doExport()}
                    >Export</button>
                  </div>
                  <div className="col-xs-4">
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
    count: state.count,
    message: state.message,
    loading: state.loading,
    exporting: state.exporting
  });

  return ReactRedux.connect(mapStateToProps)(App);
});
