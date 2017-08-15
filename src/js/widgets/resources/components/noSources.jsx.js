'use strict';
define([
  'react'
], function (React) {

  var NoSources = React.createClass({
    render: function () {
      if (this.props.noSources) {
        return (
          <div>
            <h3 className="s-right-col-widget-title">
              No Sources Found
            </h3>
          </div>
        );
      }
      return null;
    }
  });

  return NoSources;
});
