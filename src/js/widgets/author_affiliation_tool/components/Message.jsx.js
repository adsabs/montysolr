'use strict';
define([
  'react'
], function (React) {

  const Message = ({ type, message, show }) => (
    <div className="col-xs-12">
      {show &&
        <div className={`text-center alert alert-${type}`}>{message}</div>
      }
    </div>
  );

  return Message;
});
