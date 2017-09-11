define([
   'react',
   'underscore',
   'd3'
], function(
  React,
   _,
   d3
    ) {

  //putting FacetCheckbox and ToggleList in one file
  //since requirejs forbids circular imports and they import eachother

  var FacetCheckbox = React.createClass({

        format: function(count) {
          return d3.format("s")(count).replace(/\.\d{2,}/, function(m) {
            return m.slice(0, 2)
          }).replace(".0", "");
        },

        updateFacetSelect: function(e) {
          if (e.target.checked) {
            //toggle open author hierarchical facets here, so users can see what the hierarchy means
            this.props.selectFacet(this.props.id);
            if (this.props.reduxState && this.props.reduxState.config.hierMaxLevels === 2) {
              this.props.toggleFacet(this.props.id, true);
            }
          } else {
            this.props.unselectFacet(this.props.id);
          }
        },

        render: function() {
          var checkbox = (
            <label className="facet-label">
              <input type="checkbox" onChange={this.updateFacetSelect} checked={this.props.isChecked} />
              &nbsp;
              <span>
                <span className="facet-label__title">{this.props.name}</span>
                <span className="facet-label__amount" title={this.props.count}>{this.format(this.props.count)}</span>
              </span>
            </label>
          );

          if (this.props.hierarchical) {
            return (
              <ToggleList id={this.props.value} reduxState={this.props.reduxState} currentLevel={this.props.currentLevel + 1} showMoreFacets={this.props.showMoreFacets} resetVisibleFacets={this.props.resetVisibleFacets} toggleFacet={this.props.toggleFacet} selectFacet={this.props.selectFacet} unselectFacet={this.props.unselectFacet}>
                {checkbox}
              </ToggleList>
            )
          } else {
            return checkbox;
          }
        },
        propTypes: {
          isChecked: React.PropTypes.bool.isRequired,
          name: React.PropTypes.string.isRequired,
          count: React.PropTypes.number.isRequired,
          reduxState: React.PropTypes.object,
          currentLevel: React.PropTypes.number,
          showMoreFacets: React.PropTypes.func,
          resetVisibleFacets: React.PropTypes.func,
          toggleFacet: React.PropTypes.func,
          selectFacet: React.PropTypes.func,
          unselectFacet: React.PropTypes.func
        }

      });

  var ToggleList = React.createClass({

    render: function() {

      var data = (this.props.currentLevel === 1)
          ? this.props.reduxState
          : this.props.reduxState.facets[this.props.id],
        open = data.state.open,
        visible = data.state.visible,
        finished = data.pagination.finished || false,
        facets = _.values(_.pick(this.props.reduxState.facets, data.children));

      var stateMessage = "";
      if (data.pagination.state === "loading") {
        stateMessage = <span>loading...</span>
      } else if (data.pagination.state === "failure") {
        stateMessage = <span>
          <i className="icon-danger"></i>
          request failed</span>
      } else if (data.pagination.state === "success" && !facets.length) {
        stateMessage = <span>no data retrieved</span>
      }

      if (!facets.length) {
        var list = <li></li>;
      } else {
        var list = facets.slice(0, visible).map(function(c) {

          var props = {
            isChecked: this.props.reduxState.state.selected.indexOf(c.value) > -1
              ? true
              : false,
            name: c.name,
            count: c.count,
            hierarchical: this.props.reduxState.config.hierMaxLevels > this.props.currentLevel
              ? true
              : false,
            value: c.value,
            id: c.value,
            selectFacet: this.props.selectFacet,
            unselectFacet: this.props.unselectFacet
          };
          //if it's hierarchical, pass down some more data so that the checkbox can instantiate its own toggleList
          if (props.hierarchical) {
            _.extend(props, _.pick(this.props, ["showMoreFacets", "resetVisibleFacets",
            "toggleFacet", "reduxState", "currentLevel"]));
          }

          return <li key={c.value}>
            <FacetCheckbox {...props}/>
          </li>
        }, this);
      }

      var showMore = !finished || (facets.length > visible),
        moreButtonClasses = showMore
          ? "btn btn-default btn-xs facet__pagination-button"
          : " hidden",
        lessButtonClasses = visible > 5
          ? "btn btn-default btn-xs facet__pagination-button"
          : "hidden";

      var facetList;
      //level will be either 1 or 2
      var parentClass = this.props.currentLevel > 1 ? "facet__list-container facet__list-container--child"
        : "facet__list-container";

      if (open) {
        facetList = (
          <div className={parentClass}>
              <ul className="facet__list">
                {list}
              </ul>
          <div className="facet__state-message">
            {stateMessage}
          </div>
          <div className="facet__pagination-container">
            <button className={lessButtonClasses} onClick={_.partial(this.props.resetVisibleFacets, this.props.id)}>
              less
            </button>
            <button className={moreButtonClasses} onClick={_.partial(this.props.showMoreFacets, this.props.id)}>
              more
            </button>
          </div>
        </div>
      )
      }

      return <div>
        <div className="facet__toggle">
          <i className={open
            ? "facet__icon facet__icon--open"
            : "facet__icon facet__icon--closed"}
            onClick={_.partial(this.props.toggleFacet, this.props.id, !open)}/> {this.props.children}
        </div>
          {facetList}
      </div>
    },

    propTypes: {
      reduxState: React.PropTypes.object.isRequired,
      currentLevel: React.PropTypes.number.isRequired,
      showMoreFacets: React.PropTypes.func.isRequired,
      resetVisibleFacets: React.PropTypes.func.isRequired,
      toggleFacet: React.PropTypes.func.isRequired,
      selectFacet: React.PropTypes.func.isRequired,
      unselectFacet: React.PropTypes.func.isRequired
    }

  });

  return ToggleList;

});
