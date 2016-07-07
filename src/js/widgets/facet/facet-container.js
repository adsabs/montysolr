define([
  'react',
  'react-redux',
  'underscore',
  'es6!./toggle_list',
  './reducers'
],  function(
    React,
    ReactRedux,
    _,
    ToggleList,
    Reducers
) {

  function mapStateToProps (state, ownProps){
      return  {
        reduxState : state,
        activeFacets : Reducers.getActiveFacets(state, state.state.selected)
      }
  }

//ownProps contains the widget's actions object which has
//overridden certain methods to be unique to the widget
  function mapDispatchToProps (dispatch, ownProps) {

    return {
      selectFacet : function(id) {
        dispatch(ownProps.actions.select_facet(id));
      },
      unselectFacet : function (id){
        dispatch(ownProps.actions.unselect_facet(id));
      },
      toggleFacet : function(id, open){
        dispatch(ownProps.actions.toggle_facet(id, open));
      },
      showMoreFacets : function (id) {
        dispatch(ownProps.actions.increase_visible(id));
      },
      resetVisibleFacets : function (id) {
        dispatch(ownProps.actions.reset_visible(id));
      },
      submitFilter : function(logicOption) {
        dispatch(ownProps.actions.submit_filter(logicOption));
      }
    }
  }

  var ContainerComponent = React.createClass({

    render : function(){

      var header = (<h3 className='facet__header' onClick={ _.partial(this.props.toggleFacet, undefined) }>
        {this.props.reduxState.config.facetTitle}
      </h3>);

      function createDropdown(arr){
        if (arr[0] == 'invalid choice'){
          return 'invalid choice!'
        }
        return <div className='facet__dropdown'>
          { arr.map(function(val){
          return <label key={val}>
                    <input type='radio' onChange ={_.partial(this.props.submitFilter, val)}/> {val}
                </label>
        }, this) }
        </div>
      }

      var dropdownContainer;

      if (this.props.activeFacets.length === 1) {
       dropdownContainer = createDropdown.call(this, this.props.reduxState.config.logicOptions.single);
      }
      else if (this.props.activeFacets.length > 1){
       dropdownContainer = createDropdown.call(this, this.props.reduxState.config.logicOptions.multiple);
      }

      return (
          <div className='facet__container'>
            <React.addons.CSSTransitionGroup transitionName='swoop' transitionEnterTimeout={300} transitionLeaveTimeout={300}>
              { dropdownContainer }
            </React.addons.CSSTransitionGroup>

            <ToggleList
                reduxState = { this.props.reduxState }
                currentLevel = { 1 }
                showMoreFacets = { this.props.showMoreFacets }
                resetVisibleFacets = { this.props.resetVisibleFacets }
                toggleFacet = { this.props.toggleFacet }
                selectFacet = { this.props.selectFacet }
                unselectFacet = { this.props.unselectFacet }
            >
              {header}
            </ToggleList>
          </div>
      )
    }
  });

  return ReactRedux.connect(mapStateToProps, mapDispatchToProps )(ContainerComponent);

});
