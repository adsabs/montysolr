'use strict';
define([
  'react',
  'react-prop-types'
], function (React, PropTypes) {

  const styles = {
    button: {
      width: '50%'
    },
    img: {
      marginBottom: 7
    }
  };

  const initialState = {
    action: '',
    fire: () => {},
    showConfirm: false
  };

  class OrcidSelectorApp extends React.Component {
    constructor(props) {
      super(props);

      this.state = initialState;
    }

    onClaim() {
      this.setState({
        action: 'claim',
        fire: this.props.onClaim,
        showConfirm: true
      });
    }

    onDelete() {
      this.setState({
        action: 'delete',
        fire: this.props.onDelete,
        showConfirm: true
      });
    }

    onConfirm(confirm) {
      let cb = this.state.fire;
      this.setState(initialState, () => confirm && cb());
    }

    componentWillReceiveProps(next) {

      // if nothing incoming, just reset
      if (next.app.get('selected').length === 0) {
        this.setState(initialState);
      }
    }

    render() {
      let len = this.props.app.get('selected').length;

      if (this.props.app.get('mode')) {
        return (
          <div className="s-right-col-widget-container container-fluid">
            <div className="row">
              <div className="sr-only">
                Orcid Bulk Actions
              </div>
              <div className="text-center">
                <img
                  src="../../styles/img/orcid-active.svg"
                  alt="orcid logo active"
                  className="s-orcid-img"
                  aria-hidden="true"
                  style={styles.img}
                />
                <span>
                <strong>&nbsp;Bulk Actions</strong>
              </span>
              </div>
            </div>
            <div className="row">
              <div className="col-xs-12">
                <div className="btn-group btn-group-justified btn-group-sm" role="group">
                  <button
                    type="button"
                    className={`btn btn-primary-faded ${len === 0 ? 'disabled' : ''}`}
                    title="Claim all selected papers from Orcid"
                    style={styles.button}
                    onClick={() => this.onClaim()}
                  >Claim</button>
                  <button
                    type="button"
                    className={`btn btn-danger ${len === 0 ? 'disabled' : ''}`}
                    title="Delete all selected papers from Orcid"
                    style={styles.button}
                    onClick={() => this.onDelete()}
                  >Delete</button>
                </div>
              </div>
            </div>
            {this.state.showConfirm &&
            <div className="row" style={{ marginTop: 5 }}>
              <div className="col-xs-12 text-center">
                Attempt to {this.state.action} {len} paper{`${len > 1 ? 's' : ''}`}?
              </div>
              <div className="col-xs-12" style={{ marginTop: 5 }}>
                <div className="btn-group btn-group-sm pull-right" role="group">
                  <button
                    className="btn btn-sm btn-success"
                    title={`${this.state.action} selected papers`}
                    onClick={() => this.onConfirm(true)}>Apply</button>
                  <button
                    title={`Cancel ${this.state.action} selected papers`}
                    className="btn btn-sm btn-danger"
                    onClick={() => this.onConfirm(false)}>Cancel</button>
                </div>
              </div>
            </div>
            }
          </div>
        );
      }

      return null;
    }
  }

  OrcidSelectorApp.propTypes = {
    app: PropTypes.object.isRequired
  };

  return OrcidSelectorApp;
});
