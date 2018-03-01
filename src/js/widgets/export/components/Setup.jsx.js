'use strict';

define([
  'react',
  'react-prop-types'
], function (React, ReactPropTypes) {

  const Setup = ({
       onApply, setFormat, disabled, onCancel, batchSize, hasMore, onReset,
       format, formats, count, setCount, maxCount, onGetNext, totalRecs,
        showSlider, showReset, remaining, autoSubmit
    }) => (
    <div>
      <div className="row">
        <div className="col-sm-10">
          <label htmlFor="ex-dropdown">Select Export Format</label>
          <select
            className="form-control"
            autoFocus="true"
            id="ex-dropdown"
            value={format.id}
            onChange={e => setFormat(e.target.value)}
            disabled={disabled}
          >
            {formats.map(f => <option key={f.id} value={f.id} title={f.help}>{f.label}</option>)}
          </select>
        </div>
        { disabled &&
        <div className="col-sm-2">
          <div className="export-loading-icon fa fa-spinner fa-spin fa-2x"/>
        </div>
        }
      </div>

      { showSlider &&
        <div className="row">
          <div className="col-sm-10">
            <label htmlFor="ex-range">
              Limit to <strong>{count}</strong> {count > 1 ? 'records' : 'record'}
            </label>
            <input
              type="range"
              id="ex-range"
              min="1"
              max={totalRecs < batchSize ? totalRecs : batchSize}
              step="1"
              value={count}
              disabled={disabled}
              onChange={e => setCount(e.target.value)}
            />
          </div>
        </div>
      }

      <div className="row">
        <div className="col-sm-12 btn-toolbar">
          {!autoSubmit &&
          <button
            className="btn btn-primary"
            onClick={onApply}
            disabled={disabled}
          >
            Apply
          </button>
          }
          {!disabled && showReset &&
          <button
            className="btn btn-info"
            onClick={onReset}
          >
            Reset
          </button>
          }
          {!disabled && hasMore &&
            <button
              className="btn btn-link"
              onClick={onGetNext}
            >
              Get Next {remaining} Record(s)
            </button>
          }
          {disabled && !autoSubmit &&
            <button
              className="btn btn-warning"
              onClick={onCancel}
            >
              Cancel
            </button>
          }
        </div>
      </div>
    </div>
  );

  Setup.propTypes = {
    setFormat: ReactPropTypes.func.isRequired,
    format: ReactPropTypes.shape({
      id: ReactPropTypes.string,
      value: ReactPropTypes.string,
      label: ReactPropTypes.string
    }).isRequired,
    onApply: ReactPropTypes.func.isRequired,
    onCancel: ReactPropTypes.func.isRequired,
    formats: ReactPropTypes.arrayOf(ReactPropTypes.shape({
      id: ReactPropTypes.string,
      value: ReactPropTypes.string,
      label: ReactPropTypes.string
    })).isRequired,
    disabled: ReactPropTypes.bool.isRequired,
    count: ReactPropTypes.string.isRequired,
    setCount: ReactPropTypes.func.isRequired,
    maxCount: ReactPropTypes.number.isRequired,
    onGetNext: ReactPropTypes.func.isRequired,
    totalRecs: ReactPropTypes.number.isRequired,
    onReset: ReactPropTypes.func.isRequired,
    showSlider: ReactPropTypes.bool.isRequired,
    showReset: ReactPropTypes.bool.isRequired,
    autoSubmit: ReactPropTypes.bool.isRequired
  };

  return Setup;
});
