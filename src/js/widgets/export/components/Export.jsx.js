'use strict';

define([
  'react', 'react-prop-types', 'es6!./ClipboardBtn.jsx'
], function (React, ReactPropTypes, ClipboardBtn) {

  const Export = ({
    output, isFetching, progress, onDownloadFile, onCopy
  }) => (
    <div>
      <div className="row">
        <div className="form-group">
          <textarea
            className="export-textarea form-control"
            readOnly="true"
            value={output}
            disabled={isFetching}
          />
        </div>
      </div>

      { isFetching &&
        <div className="progress export-progress">
          <div
            className="progress-bar"
            role="progressbar"
            aria-valuenow={progress}
            aria-valuemin="0"
            aria-valuemax="100"
            style={{ width: `${progress}%` }}
          >
            <span className="sr-only">{progress}% Complete</span>
          </div>
          <div className="text-center">Loading...</div>
        </div>
      }

      <div className="row">
        <div className="col-sm-12 btn-group">
          <button
            className="btn btn-default"
            disabled={isFetching}
            onClick={onDownloadFile}
          >
            <i className="fa fa-download fa-fw"/>
            Download to File
          </button>
          <ClipboardBtn
            disabled={isFetching}
            onCopy={onCopy}
            target=".export-textarea"
          />
        </div>
      </div>
    </div>
  );

  Export.propTypes = {
    output: ReactPropTypes.string.isRequired,
    isFetching: ReactPropTypes.bool.isRequired,
    progress: ReactPropTypes.number,
    onDownloadFile: ReactPropTypes.func.isRequired,
    onCopy: ReactPropTypes.func.isRequired
  };

  return Export;
});
