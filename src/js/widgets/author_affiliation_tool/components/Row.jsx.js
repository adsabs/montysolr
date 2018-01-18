'use strict';
define([
  'react',
  'es6!../components/LastActiveDateRow.jsx',
  'es6!../components/AffiliationRow.jsx'
], function (React, LastActiveDateRow, AffiliationRow) {

  /**
   * Simple Row which contains the Affiliation and LastActiveDate sections
   */
  const Row = ({ onChange, data }) => {
    const { author, selected, affiliations, lastActiveDates } = data;

    return (
      <div className="row">
        <div className="col-xs-2">
          <label
            className={ selected ? '' : 'auth-aff-label' }
          >
            <input
              checked={selected}
              type="checkbox"
              onChange={() => onChange()}
            /> {author}
          </label>
        </div>
        <div className="col-xs-8">
          {affiliations.map(a =>
            <AffiliationRow
              key={a.id}
              onChange={() => onChange(a)}
              {...a} />
          )}
        </div>
        <div className="col-xs-2">
          {lastActiveDates.map(d =>
            <LastActiveDateRow
              key={d.id}
              onChange={() => onChange(d)}
              {...d}
            />
          )}
        </div>
      </div>
    );
  };

  return Row;
});
