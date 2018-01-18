define([
  '../actions',
  'react',
  'react-redux',
  'es6!./fullTextSources.jsx',
  'es6!./dataProducts.jsx',
  'es6!./loading.jsx',
  'es6!./noSources.jsx',
  'create-react-class'
], function (
  actions, React, ReactRedux, FullTextSources, DataProducts, LoadingIcon, NoSources, createReactClass) {

  var App = createReactClass({
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
          <NoSources noSources={this.props.noSources}/>
        </div>
      );
    }
  });

  // only show no sources message if we aren't loading and no sources are found
  var noSources = function (state) {
    return !state.isLoading &&
      !state.fullTextSources.length && !state.dataProducts.length;
  };

  var mapStateToProps = function (state) {
    return {
      fullTextSources: state.fullTextSources,
      dataProducts: state.dataProducts,
      isLoading: state.isLoading,
      noSources: noSources(state)
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
