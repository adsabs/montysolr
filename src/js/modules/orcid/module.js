/**
 * ORCID module is the main component for enabling communication with ORCID Api
 *
 * It should be installed through discovery.config.js, it needs to go into
 * the controllers section (because it will create an instance of the OrcidApi
 * and insert that into services)
 *
 * You config should look like:
 *
 *   'js/apps/discovery/main': {
 *
 *      core: {
 *       controllers: {
 *         Orcid: 'js/modules/orcid/module'
 *         ....
 *         }
 *   }
 *
 */

define([
    'backbone',
    'underscore',
    'js/components/generic_module',
    './orcid_api',
  ],
  function (
    Backbone,
    _,
    GenericModule,
    OrcidApi
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

        //TODO:rca - clean up this
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

        if (orcidApi) // already activated
          return;

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