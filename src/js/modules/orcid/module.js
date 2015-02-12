/**
 * ORCID module is the main component for enabling communication with ORCID Api
 *
 * When the module is activated, it will insert into BeeHive its dependencies:
 *  OrcidApi - service that controls jobs
 *  OrcidNotifier - service which gets notified of new changes
 *
 */

define([
    'backbone',
    'underscore',
    'js/components/generic_module',
    './orcid_api',
    './orcid_model_notifier'
  ],
  function (
    Backbone,
    _,
    GenericModule,
    OrcidApi,
    OrcidNotifier
    ) {


    var OrcidModule = GenericModule.extend({

      activate: function(beehive) {

        var config = beehive.getObject('DynamicConfig');
        if (!config) {
          throw new Error('DynamicConfig is not available to Orcid module');
        }

        var redirectUrlBase = config.orcidRedirectUrlBase || (location.protocol + '//' + location.host);
        var orcidClientId = config.orcidClientId;
        var orcidApiEndpoint = config.orcidApiEndpoint;
        var orcidLoginEndpoint = config.orcidLoginEndpoint;

        if (!orcidClientId || !orcidApiEndpoint) {
          throw new Error('Missing configuration for ORCID module: orcidApiEndpoint, orcidClientId');
        }

        var opts = {
          apiEndpoint: orcidApiEndpoint,
          clientId: orcidClientId,
          worksUrl: orcidApiEndpoint + '/{0}/orcid-works',
          loginUrl: orcidLoginEndpoint
            + "?scope=/orcid-profile/read-limited%20/orcid-works/create%20/orcid-works/update&response_type=code&access_type=offline"
            + "&client_id=" + orcidClientId
            + "&redirect_uri=" + encodeURIComponent(redirectUrlBase + '/#/user/orcid'),
          exchangeTokenUrl: orcidApiEndpoint + '/exchangeOAuthCode'
        };

        _.extend(config, {Orcid: opts });

        this.activateDependencies(beehive);
      },

      activateDependencies: function(beehive) {
        var orcidApi, orcidNotifier;
        orcidApi = beehive.getService('OrcidApi');
        orcidNotifier = beehive.getService('OrcidNotifier');

        if (orcidApi && orcidNotifier) // already activated
          return;

        // must be first
        orcidNotifier = new OrcidNotifier();
        orcidNotifier.activate(beehive);
        beehive.addService('OrcidNotifier', orcidNotifier);

        orcidApi = new OrcidApi();
        orcidApi.activate(beehive);
        beehive.addService('OrcidApi', orcidApi);
      }

    });

    return function() {
      return {
        activate: function(beehive) {
          var om = new OrcidModule();
          om.activate(beehive);
        }
      }
    }
  });