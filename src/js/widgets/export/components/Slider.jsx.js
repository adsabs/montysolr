'use strict';

define([
  'react',
  'react-prop-types'
], function (React, ReactPropTypes) {

  const Slider = ({ count, setCount }) => (
    <div>
      <label htmlFor="ex-range">
        Limit to first <strong>{count}</strong> records
      </label>
      <input
        type="range"
        id="ex-range"
        min="0"
        max="3000"
        step="1"
        defaultValue={count}
        onChange={e => setCount(e.target.value)}
      />
    </div>
  );

  Slider.propTypes = {
    count: ReactPropTypes.number.isRequired,
    setCount: ReactPropTypes.func.isRequired
  };

  return Slider;
});
