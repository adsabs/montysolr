'use strict';

define([
  'react'
], function (React) {

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

  return Closer;
});