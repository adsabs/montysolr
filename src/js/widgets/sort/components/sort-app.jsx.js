'use strict';
define([
  'react',
  'react-prop-types'
], function (React, PropTypes) {


  const SortApp = ({ setSort, setDirection, app }) => {
    const options = app.get('options');
    const sort = app.get('sort');
    const direction = app.get('direction');
    const locked = app.get('locked');

    /**
     * Call the handler after a selection is made from the dropdown
     *
     * @param {object} item - the sort option
     * @param {object} e - the event object
     */
    const onSelect = (item, e) => {
      e.preventDefault();
      setSort(item);
    };

    return (
      <div className="btn-group">
        <button
          disabled={locked}
          aria-disabled={locked}
          type="button"
          className="btn btn-default"
          onClick={setDirection}
          title={direction === 'asc'
            ? 'Change sort direction to descending'
            : 'Change sort direction to ascending'
          }
          >
          <i className={`fa fa-sort-amount-${direction}`} aria-hidden="true"/>
        </button>
        <button
          style={{ minWidth: 100 }}
          disabled={locked}
          aria-disabled={locked}
          type="button"
          className="btn btn-default dropdown-toggle"
          data-toggle="dropdown"
          title="Select a sort option"
          >
          {sort.get('text')} <span className="caret" aria-hidden="true"/>
        </button>
        <ul className="dropdown-menu" role="menu">
          {
            options.map(o => (
              <li key={o.get('id')}>
                <a
                  href="#"
                  title={o.get('desc')}
                  onClick={(e) => onSelect(o, e)}
                >{o.get('text')}</a>
              </li>
            ))
          }
        </ul>
      </div>
    );
  };

  SortApp.propTypes = {
    app: PropTypes.object.isRequired,
    setSort: PropTypes.func.isRequired,
    setDirection: PropTypes.func.isRequired
  };

  return SortApp;
});
