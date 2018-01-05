'use strict';
define([
  'react',
  'create-react-class'
], function (React, createReactClass) {

  var NoSources = createReactClass({
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
