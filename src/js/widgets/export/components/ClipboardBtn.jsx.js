'use strict';

define([
  'clipboard', 'react', 'react-prop-types'
], function (Clipboard, React, ReactPropTypes) {

  class ClipboardBtn extends React.Component {

    componentWillUnmount() {
      this.clipboard.destroy();
    }

    componentDidMount() {
      this.clipboard = new Clipboard(this.element);
      this.clipboard.on('success', this.props.onCopy);
    }

    render() {
      const { disabled, target } = this.props;
      return (
        <button
          className="btn btn-default"
          disabled={disabled}
          ref={e => this.element = e}
          data-clipboard-target={target}
        >
          <i className="fa fa-copy fa-fw"/>
          Copy to Clipboard
        </button>
      );
    }
  }

  ClipboardBtn.propTypes = {
    disabled: ReactPropTypes.bool.isRequired,
    target: ReactPropTypes.string.isRequired,
    onCopy: ReactPropTypes.func.isRequired
  };

  return ClipboardBtn;
});

