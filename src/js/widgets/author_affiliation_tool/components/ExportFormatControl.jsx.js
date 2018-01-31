'use strict';
define([
  'react'
], function (React) {

  /**
   * Dropdown containing export format strings for the user to select
   */
  const ExportFormatControl = ({ formats, format, onChange }) => (
    <select
      onChange={e => onChange(e.target.value)}
      value={format}
      className="form-control"
      title="Select a format"
    >
      {formats.map(f =>
        <option key={f} value={f}>{f}</option>
      )}
    </select>
  );

  return ExportFormatControl;
});
