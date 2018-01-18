'use strict';
define([
  'react'
], function (React) {

  const style = {
    icon: {
      fontSize: 120
    },
    text: {
      fontSize: 32
    }
  };

  /**
   * Loading Message/Icon
   */
  const Loading = ({}) => (
    <div className="row text-center" role="alert" aria-busy={true}>
      <div className="col-xs-12" style={style.icon}>
        <i className="fa fa-spinner fa-spin"/>
      </div>
      <div className="col-xs-12" style={style.text}>
        Loading...
      </div>
    </div>
  );

  return Loading;
});
