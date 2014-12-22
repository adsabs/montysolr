define([
    'underscore',
    'jquery',
    'js/components/generic_module',
    'js/mixins/dependon',
    'js/modules/orcid/orcid_api_constants',
    'js/components/api_query_updater',
    'js/components/api_query'
  ],
  function(
    _, 
    $, 
    GenericModule, 
    Mixins,
    OrcidApiConstants,
    ApiQueryUpdater,
    ApiQuery
    ) {

    var OrcidMediator = GenericModule.extend({

      initialize: function (options) {
        this.queryUpdater = new ApiQueryUpdater('q');
      },

      activate: function (beehive, app) {
        this.setBeeHive(beehive);

        this.pubsub.subscribe(this.pubsub.ORCID_ANNOUNCEMENT, _.bind(this.loginSuccess, this));

        //BC:rca - this was called OrchidApi (and there was not instance of OrchidApi - so it never worked)
        //var orcidApi = beehive.Services.get('OrcidApi');
        //orcidApi.activate(beehive);
        //this.orcidApi = orcidApi;
      },

      getOAuthCode: function () {
        var orcidApi = this.beehive.getService('OrcidApi');
        if (!orcidApi)
          throw new Error('OrcidApi is not available!');

        var result = orcidApi.getOAuthCode();
        return result;
      },

      loginSuccess: function (e) {
        if(e.msgType == OrcidApiConstants.Events.LoginSuccess) {
          var orcidProfile = e.data;

          var orcidBio = orcidProfile["orcid-bio"];
          if(orcidBio) {
            var personalDetails = orcidBio["personal-details"];
            if (personalDetails) {

              var givenNames = personalDetails["given-names"];
              var familyName = personalDetails["family-name"];

              var newQuery = 'author:' + this.queryUpdater.quote(familyName + ', ' + givenNames);
              var apiQuery = new ApiQuery({'q': newQuery});
              
              // this can be deactivated (based on some config)
              this.pubsub.publish(this.pubsub.START_SEARCH, apiQuery);
            }
          }
        }
      }
    });

    _.extend(OrcidMediator.prototype, Mixins.BeeHive);

    return OrcidMediator;

  }
);
