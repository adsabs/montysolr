define([
    'underscore'
],
  function(
    _
  ) {

    function reducer(state, action) {
      if (!state) state = reducer.getDefaultState();

      switch (action.type) {

        case 'DATA_REQUESTED':
          return reducer.dataRequested(state, action.id);
        case 'DATA_RECEIVED':
          return reducer.dataReceived(state, action.data, action.id);
        case 'INCREASE_VISIBLE':
          return reducer.increaseVisible(state, action.num, action.id);
        case 'RESET_VISIBLE':
          return reducer.resetVisible(state, action.id);
        case 'FACET_SELECTED':
          return reducer.facetSelected(state, action.id);
        case 'FACET_UNSELECTED':
          return reducer.facetUnselected(state, action.id);
        case 'FACET_TOGGLED':
          return reducer.facetToggled(state, action.open, action.id);
        case 'SUBMIT_FILTER':
          return reducer.submitFilter(state, action.logicOption);
        case 'RESET_STATE':
          return reducer.getDefaultState(state);
        default:
          return state;
      }
    }

    //utility function called by react components and facet widget
    reducer.getActiveFacets = function(state, selected) {
      //if it's a hierarchical facet, remove the children
      return selected.map(function(s) {
        if (selected.indexOf(getParentName(s)) > -1) return false;
        return s;
      }).filter(function(s) {
        if (s) return s
      });
    }

    reducer.facetSelected = function(state, id) {
      //if it's a hierarchical facet, select all the children (might not be loaded yet)
      var selected = _.unique(state.state.selected.concat(state.facets[id].children.concat([id])));
      //or, if all children are selected, but parent is not, select the parent
      _.forEach(state.facets, function(v, k) {
        if (!v.children.length) return;
        if (selected.indexOf(k) > -1) return;
        var add = true;
        for (var i = 0; i < v.children.length; i++) {
          if (selected.indexOf(v.children[i]) == -1) {
            add = false;
            break;
          }
        }
        if (add) selected.push(k);
      });

      return _.assign({}, state, {
        state: _.assign({}, state.state, {
          selected: selected
        })
      })

    };

    function getParentName(id) {
      var parts = id.split('/');
      if (parts.length > 2) {
        var parent = parts[parts.length - 2];
        parent = parseInt(id[0]) - 1 + '/' + parent;
        return parent;
      }
    }

    reducer.facetUnselected = function(state, id) {
      var selected = _.without(state.state.selected, id);
      var parent = getParentName(id);


      //parent can't be selected if not all children are selected
      if (parent) {
        selected = _.without(selected, parent);
      }

      //unselect all the children
      selected = _.without.apply(undefined, [selected].concat(state.facets[id].children))

      return _.assign({}, state, {
        state: _.assign({}, state.state, {
          selected: selected
        })
      })
    };

    //utility function for immutable updating
    function updateKey(state, id, newData, key) {
      key = key || 'state';
      if (!id) {
        var newState =  _.assign({}, state);
        newState[key] =  _.assign({}, state[key], newData);
        return newState;
      } else {
        var facet = {};
        facet[id] = _.assign({}, state.facets[id]);
        facet[id][key] = _.assign({}, state.facets[id][key], newData);

        return _.assign({}, state, {
          facets : _.assign({}, state.facets, facet)
        });
      }
    }

    reducer.resetVisible = function(state, id) {
      return updateKey(state, id, {
        visible: 5
      });
    }

    reducer.increaseVisible = function(state, num, id) {
      var data = id ? {
        visible: state.facets[id].state.visible + num
      } : {
        visible: state.state.visible + num
      };
      return updateKey(state, id, data);
    }

    reducer.facetToggled = function(state, open, id) {
      return updateKey(state, id, {
        open: open
      });
    };

    // set loading state
    reducer.dataRequested = function(state, id) {
      if (!id) {
        var paginationData = _.assign({}, state.pagination, {
          state: 'loading'
        });
      } else {
        var paginationData = _.assign({}, state.facets[id].pagination, {
            state: 'loading'
          })
        }
      return updateKey(state, id, paginationData, 'pagination');
    };

    //for dataReceived reducer
    function processData(data, preprocessors) {

      preprocessors = preprocessors || [];
      if (_.isFunction(preprocessors)) preprocessors = [preprocessors];

      var data = data.map(
        function(d, i) {
          if (i % 2 === 0) {
            return {
              name: d.split('/')[d.split('/').length - 1],
              value: d,
              count: data[i + 1],
              pagination: _.assign({}, _.cloneDeep(reducer.defaultState.pagination)),
              children: [],
              state: _.assign({}, _.cloneDeep(reducer.defaultState.state))
            }
          }
        }
      ).filter(function(d) {
        if (d) return d;
      });

      //now reduce the result with the preprocessors (if they exist)
      return preprocessors.reduce(function(data, fn) {
        return fn(data);
      }, data);

    }

    reducer.dataReceived = function(state, data, id) {
      var facets = data.facet_counts.facet_fields[Object.keys(data.facet_counts.facet_fields)[0]];
      var finished = (facets.length / 2 == parseInt(data.responseHeader.params['facet.limit'])) ? false : true;
      facets = processData(facets, state.config.preprocessors);
      //for children array
      var dataIds = facets.map(function(d) {
        return d.value
      });
      //turn data into object to merge into the facets key in state
      facets = _.object(dataIds, facets);

      if (!id) {
        return _.assign({}, state, {
          pagination: _.assign({}, state.pagination, {
            state: 'success',
            finished: finished
          }),
          facets: _.assign({}, state.facets, facets),
          children: state.children.concat(dataIds)
        });
      } else {
        var facet = _.assign({}, state.facets[id], {
          children: state.facets[id].children.concat(dataIds),
          pagination: _.assign({}, state.facets[id].pagination, {
            state: 'success',
            finished: finished
          })
        });

        var newFacetObj = _.assign({}, state.facets, facets);
        newFacetObj[id] = facet;

        if (state.state.selected.indexOf(id) > -1) {
          //show that these facets are selected
          var newSelected = _.uniq(state.state.selected.concat(dataIds));
        } else {
          var newSelected = state.state.selected;
        }

        return _.assign({}, state, {
          facets: newFacetObj,
          state: _.assign({}, state.state, {
            selected: newSelected
          })
        });
      }
    };

    reducer.submitFilter = function(logicOption) {
      throw new Error('this should be overridden by the widget')
    }

    reducer.defaultState = {
      config: {
        preprocessors: [],
        hierMaxLevels: 1,
        facetField: undefined,
        openByDefault: false
      },
      state: {
        open: false,
        visible: 5,
        selected: []
      },
      pagination: {
        state: undefined,
        finished: false
      },
      children: [],
      facets: {}
    };

    reducer.getDefaultState = function(state) {
      //reset the state, but keep the config
      return _.assign({}, this.defaultState, { config : state.config })
    };

    return reducer

  })
