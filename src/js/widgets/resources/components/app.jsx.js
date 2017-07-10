define([
  '../actions',
  'react',
  'react-redux',
  'es6!./fullTextSources.jsx',
  'es6!./dataProducts.jsx',
  'es6!./loading.jsx'
], function (
  actions, React, ReactRedux, FullTextSources, DataProducts, LoadingIcon) {

  var App = React.createClass({
    render: function () {
      return (
        <div className="s-right-col-widget-container">
          <LoadingIcon show={this.props.isLoading}/>
          <FullTextSources
            sources={this.props.fullTextSources}
            onLinkClick={this.props.onLinkClick}
          />
          <DataProducts
            products={this.props.dataProducts}
            onLinkClick={this.props.onLinkClick}
          />
        </div>
      );
    }
  });

  var mapStateToProps = function (state) {
    return {
      fullTextSources: state.fullTextSources,
      dataProducts: state.dataProducts,
      isLoading: state.isLoading
    };
  };

  var mapDispatchToProps = function (dispatch) {
    return {
      onLinkClick: function (text) {
        return dispatch(actions.emitAnalytics(text));
      }
    };
  };

  return ReactRedux.connect(mapStateToProps, mapDispatchToProps)(App);
});
