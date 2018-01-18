'use strict';
define([
  'react'
], function (React) {

  /**
   * Affiliation section of the row
   */
  const AffiliationRow = ({ years, name, selected, onChange }) => (
    <div>
      <div className="col-xs-8">
        <label
          className={ selected ? '' : 'auth-aff-label' }
        >
          <input
            type="checkbox"
            checked={selected}
            onChange={onChange}
          /> {name === '-' ? '(None)' : name}
        </label>
      </div>
      <div
        className={ 'col-xs-4' + (selected ? ' auth-aff-bold' : '') }
      >{years.join(', ')}</div>
    </div>
  );

  return AffiliationRow;
});
