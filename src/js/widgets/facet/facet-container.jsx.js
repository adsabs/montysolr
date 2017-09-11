define([
  'react',
  'react-redux',
  'underscore',
  'es6!./toggle_list.jsx',
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

    createDropdown : function (){

      //no dropdown if no selected facets!
      if (this.props.activeFacets.length === 0) return '';

      var arr, dropdownContent;

      if (this.props.activeFacets.length > 25)
        return <div className='facet__dropdown'>select no more than 25 facets at a time</div>

      if (this.props.activeFacets.length === 1){
          arr = this.props.reduxState.config.logicOptions.single
        }
      else {
        arr = this.props.reduxState.config.logicOptions.multiple;
      }

      if (arr[0] == 'invalid choice') return <div className='facet__dropdown'>invalid choice!</div>

      else {
          return (<div className='facet__dropdown'>
          <div className='facet__dropdown__title'><b>{ this.props.activeFacets.length }</b> selected</div>
          { arr.map(function(val){
          return <label key={val}>
                    <input type='radio' onChange ={_.partial(this.props.submitFilter, val)}/> {val}
                </label>
        }, this) }
          </div>);
        }
    },

    render : function(){

      var header = (<h3 className='facet__header' onClick={ _.partial(this.props.toggleFacet, undefined) }>
        {this.props.reduxState.config.facetTitle}
      </h3>);

      return (
          <div className='facet__container'>
            <React.addons.CSSTransitionGroup transitionName='swoop' transitionEnterTimeout={300} transitionLeaveTimeout={300}>
              { this.createDropdown() }
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
    },


  });

  return ReactRedux.connect(mapStateToProps, mapDispatchToProps )(ContainerComponent);

});
