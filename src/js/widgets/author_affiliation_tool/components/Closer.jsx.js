'use strict';

define([
  'react',
  'react-prop-types'
], function (React, ReactPropTypes) {

  const style = {
    position: 'absolute',
    right: '5px'
  };

  const Closer = ({ onClick }) => {

    const handleClick = (e) => {
      e.preventDefault();
      onClick();
    };

    return (
      <a href="#" style={style} onClick={e => handleClick(e)}>
        <i className="fa fa-times fa-2x"/>
      </a>
    );
  };

  Closer.propTypes = {
    onClick: ReactPropTypes.func.isRequired
  };

  return Closer;
});