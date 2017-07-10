'use strict';
define([
  'react'
], function (React) {

  /**
   * Create the DataProducts Section
   * @param props
   * @returns {*}
   * @constructor
   */
  function DataProducts(props) {
    var products = props.products;
    if (!products || !products.length) {
      return null;
    }

    var links = products.map(function (product) {
      var onClick = props.onLinkClick.bind(this, product.title);
      return (
        <li key={product.title}>
          <a
            href={product.link}
            onClick={onClick}
            target="_blank"
            >{product.title}</a>
        </li>
      );
    });

    return (
      <div>
        <h3 className="s-right-col-widget-title">
          <i className="icon-data" aria-hidden="true"></i> Data Products
        </h3>
        <ul className="list-unstyled">
          {links}
        </ul>
      </div>
    );
  };

  return DataProducts;
});
