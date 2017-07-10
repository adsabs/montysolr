'use strict';
define([
  'react'
], function (React) {

  /**
   * Create the Loading Icon
   *
   * Only shown when *show* is true
   * @param props
   * @returns {*}
   * @constructor
   */
  function LoadingIcon(props) {
    if (!props.show) {
      return null;
    }

    return (
      <span>
        <i className="icon-loading" aria-hidden="true"></i> Loading...
      </span>
    );
  }

  return LoadingIcon;
});
