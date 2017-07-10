'use strict';
define([
  'react'
], function (React) {

  /**
   * Create the Link class
   * @param props
   * @returns {XML}
   * @constructor
   */
  function Icons(props) {
    if (props.openUrl) {
      return (
        <i
          className="fa fa-university"
          data-toggle="tooltip"
          data-placement="top"
          title="This resource is available through your institution."
          aria-hidden="true"
        ></i>
      );
    } else if (props.openAccess) {
      return (
        <i
          className="s-open-access-image"
          data-toggle="tooltip"
          data-placement="top"
          title="This is an open access item."
          aria-hidden="true"
        ></i>
      );
    } else {
      return null;
    }
  }

  /**
   * Create the FullTextSources class
   * @param props
   * @returns {*}
   * @constructor
   */
  function FullTextSources(props) {
    var sources = props.sources;
    if (!sources || !sources.length) {
      return null;
    }


    var links = sources.map(function (source) {
      var onClick = props.onLinkClick.bind(this, source.title);
      return (
        <li key={source.title}>
          <a
            href={source.link}
            target="_blank"
            onClick={onClick}
            >
            {source.title + ' '}
            <Icons openAccess={source.openAccess} openUrl={source.openUrl} />
          </a>
        </li>
      );
    });

    return (
      <div>
        <h3 className="s-right-col-widget-title">
          <i className="icon-text" aria-hidden="true"></i> Full Text Sources
        </h3>
        <ul className="list-unstyled">
          {links}
        </ul>
      </div>
    );
  }

  return FullTextSources;
});
