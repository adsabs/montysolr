define([
    'underscore',
    'bootstrap',
    'jquery',
    'xml2json',
    'backbone',
    'js/components/generic_module',
    'js/mixins/dependon',
    'js/services/orcid_api_constants',
    'js/components/pubsub_events',
    'js/mixins/link_generator_mixin',
    'js/widgets/orcid_model_notifier/orcid_model'

  ],
  function (_,
            Bootstrap,
            $,
            Xml2json,
            Backbone,
            GenericModule,
            Mixins,
            OrcidApiConstants,
			      PubSubEvents,
            LinkGeneratorMixin,
            OrcidModel
  ) {
    function addXmlHeadersToOrcidMessage(message) {
      var messageCopy = $.extend(true, {}, message);
      messageCopy.$ = {
        "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
        "xsi:schemaLocation": "http://www.orcid.org/ns/orcid https://raw.github.com/ORCID/ORCID-Source/master/orcid-model/src/main/resources/orcid-message-1.1.xsd",
        "xmlns": "http://www.orcid.org/ns/orcid"
      };
      return messageCopy;
    }

    // TODO: move this to some commonUtils.js
    String.prototype.format = function () {
      var args = arguments;
      return this.replace(/\{\{|\}\}|\{(\d+)\}/g, function (m, n) {
        if (m == "{{") {
          return "{";
        }
        if (m == "}}") {
          return "}";
        }
        return args[n];
      });
    };

    function getParameterByName(name) {
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
      return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }

    var ORCID_OAUTH_CLIENT_ID = 'APP-P5ANJTQRRTMA6GXZ';
    var ORCID_ENDPOINT = 'https://sandbox.orcid.org';
    var ORCID_API_ENDPOINT = 'https://api.sandbox.orcid.org/v1.1';
    var ORCID_PROFILE_URL = ORCID_API_ENDPOINT + '/{0}/orcid-profile';
    var ORCID_WORKS_URL = ORCID_API_ENDPOINT + '/{0}/orcid-works';
    var ORCID_OAUTH_LOGIN_URL = ORCID_ENDPOINT
      + "/oauth/authorize?scope=/orcid-profile/read-limited,/orcid-works/create,/orcid-works/update&response_type=code&access_type=offline"
      + "&client_id=" + ORCID_OAUTH_CLIENT_ID
      + "&redirect_uri={0}";
    var EXCHANGE_TOKEN_URI = 'http://localhost:3000/oauth/exchangeAuthCode';


    var OrcidApi = GenericModule.extend({
      orcidProxyUri: '',
      userData: {},

      routeOrcidPubSub: function(msg){

        switch(msg.msgType){
          case OrcidApiConstants.Events.LoginRequested:
            this.showLoginDialog();
            break;
          case OrcidApiConstants.Events.SignOut:
            this.signOut();
            break;
          case OrcidApiConstants.Events.OrcidAction:
            this.processOrcidAction(msg.data);
            break;
          case OrcidApiConstants.Events.RedirectToLogin:
            this.redirectToLogin();
            break;
        }
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.pubSub = this.getBeeHive().getService('PubSub').getHardenedInstance();
        this.pubSubKey = this.pubSub.getPubSubKey();

        var _that = this;

        window.oauthAuthCodeReceived = _.bind(this.oauthAuthCodeReceived, this);
        this.pubSub.subscribe(PubSubEvents.BOOTSTRAP_CONFIGURED, function(page) {
          var code = getParameterByName("code");
          _that.oauthAuthCodeReceived(code, window.location.origin, _that);
        });

        this.pubSub.subscribe(this.pubSub.ORCID_ANNOUNCEMENT, _.bind(this.routeOrcidPubSub, this));
      },
      initialize: function (options) {

      },
      cleanLoginWindow: function() {
        if (this.loginWindow) {
          this.loginWindow.onbeforeunload = null;
          this.loginWindow = null;
        }
      },
      redirectToLogin: function() {
        var url = ORCID_OAUTH_LOGIN_URL.format(window.location.origin);

        window.location.replace(url);
      },
      oauthAuthCodeReceived: function (code, redirectUri) {

        this.cleanLoginWindow();

        if (!code || !redirectUri) {
          return;
        }

        var deferred = $.Deferred();

        var _that = this;

        this.sendData({
          type: "GET",
          url: EXCHANGE_TOKEN_URI,
          data: {
            code: code,
            redirectUri: redirectUri
          }
        })
          .done(function (authData) {
            var beeHive = _that.getBeeHive();
            var LocalStorage = beeHive.getService("LocalStorage");
            LocalStorage.setObject("userSession", {
              authData: authData
            });

            _that.refreshUserProfile()
              .done(function () {

                deferred.resolve();

                var userSession = LocalStorage.getObject("userSession");

                var orcidProfile = userSession.orcidProfile['#document']['orcid-message']['orcid-profile'];

                var pubSub = _that.pubSub;
                pubSub.publish(pubSub.ORCID_ANNOUNCEMENT, {msgType: OrcidApiConstants.Events.LoginSuccess, data: orcidProfile});
              })
              .fail(function (error) {
                deferred.reject(error);
              });

          })
          .fail(function (error) {
            deferred.reject(error);
          });

        return deferred.promise();
      },

      signOut : function(){
        this.getBeeHive()
          .getService('LocalStorage')
          .setObject("userSession", {isEmpty:true});
      },


      getAdsIds: function(orcidWork){
        var extIdentifiersObj = orcidWork["work-external-identifiers"];

        if (!extIdentifiersObj) {
          return undefined;
        }

        var extIdentifiers = extIdentifiersObj["work-external-identifier"] || [];

        if (!(extIdentifiers instanceof Array)) {
          extIdentifiers = [extIdentifiers];
        }

        var adsExtIdentifiers = extIdentifiers.filter(function(extIdentifier) {
          return extIdentifier["work-external-identifier-id"].indexOf("ads:") != -1;
        });

        return adsExtIdentifiers;
      },
      isWorkFromAds: function(orcidWork) {

        return this.getAdsIds(orcidWork).length > 0;
      },

      formatOrcidWork: function(adsWork, putCode){
        var formatContributors = function(adsAuthors){

          var result = [];

          _.each(adsAuthors, function(author){
            result.push({
              "contributor": {
                "credit-name": author,
                "contributor-attributes": {
                  "contributor-role": "author"
                }
              }
            });
          });

          return result;
        };

        var result =
        {
          "work-title": {
            "$": {},
            "title": adsWork.title.join(' ')
          },

          "short-description": adsWork.abstract,
          "publication-date": {
            "year": adsWork.year
          },

          "work-external-identifiers": [
            {
              "work-external-identifier": {
                "work-external-identifier-type": 'bibcode',
                "work-external-identifier-id": adsWork.bibcode
              }
            },
            {
              "work-external-identifier": {
                "work-external-identifier-type": 'other-id',
                "work-external-identifier-id": 'ads:' + adsWork.id
              }
            }

            // TODO : add DOI, if available
          ],

          "work-type": "book",

          "work-contributors": formatContributors(adsWork.author),

          "url": adsWork.doi ? LinkGeneratorMixin.adsUrlRedirect("doi", adsWork.doi) : '' // TODO : in item_view model DOI is missing

        };


        if (putCode){
          result["$"] = {"put-code": putCode};
        }

        return result;
      },

      fillOrcidWorks : function(adsData){

        //"{"abstract":"Laser active imaging systems are widespread tools used in region surveillance and threat identification. However, the photoelectric imaging detector in the imaging systems is easy to be disturbed and this leads to errors of the recognition and even the missing of the target. In this paper, a novel wavelet-weighted multi-scale structural similarity (WWMS-SSIM) algorithm is proposed. 2-D four-level wavelet decomposition is performed for the original and disturbed images. Each image can be partitioned into one low-frequency subband (LL) and a series of octave high-frequency subbands (HL, LH and HH). Luminance, contrast and structure comparison are computed in different subbands with different weighting factors. Based on the results of the above, we can construct a modified WWMS-SSIM. Cross-distorted image quality assessment experiments show that the WWMS-SSIM algorithm is more suitable for the subjective visual feeling comparing with NMSE and SSIM. In the laser-dazzling image quality assessment experiments, the WWMS-SSIM gives more reasonable evaluations to the images with different power and laser spot positions, which can be useful to give the guidance of the laser active imaging system defense and application.","pub":"Optics Laser Technology","volume":"67","email":["-","-","-","-"],"bibcode":"2015OptLT..67..183Q","year":"2015","id":"10666236","keyword":["Image quality assessment","Laser-dazzling effect","Wavelet decomposition"],"author":["Qian, Fang","Guo, Jin","Sun, Tao","Wang, Tingfeng"],"aff":["State Key Laboratory of Laser Interaction with Matter, Changchun Institute of Optics, Fine Mechanics and Physics Chinese Academy of Sciences, Changchun 130033, Jilin, China","State Key Laboratory of Laser Interaction with Matter, Changchun Institute of Optics, Fine Mechanics and Physics Chinese Academy of Sciences, Changchun 130033, Jilin, China","State Key Laboratory of Laser Interaction with Matter, Changchun Institute of Optics, Fine Mechanics and Physics Chinese Academy of Sciences, Changchun 130033, Jilin, China","State Key Laboratory of Laser Interaction with Matter, Changchun Institute of Optics, Fine Mechanics and Physics Chinese Academy of Sciences, Changchun 130033, Jilin, China"],"title":["Quantitative assessment of laser-dazzling effects through wavelet-weighted multi-scale SSIM measurements"],"[citations]":{"num_citations":0,"num_references":2},"identifier":"2015OptLT..67..183Q","resultsIndex":0,"details":{"highlights":["-frequency subband <em>(LL)</em> and a series of octave high-frequency subbands (HL, LH and HH). Luminance, contrast"]},"num_citations":0,"links":{"text":[],"list":[{"letter":"R","title":"References (2)","link":"/#abs/2015OptLT..67..183Q/references"}],"data":[]},"emptyPlaceholder":false,"visible":true,"actionsVisible":true,"orcidActionsVisible":false}"

        var that = this;
        var formatWorks = function(adsWorks){

          var result = [];
          _.each(adsWorks,
            function(adsWork){
              result.push(that.formatOrcidWork(adsWork));
            }
          );

          return {'orcid-work': result};

        };

        var orcidWorksMessage = {
          "orcid-message": {
            "$": {
              "xmlns": "http://www.orcid.org/ns/orcid"
            },
            "message-version": "1.1",
            "orcid-profile": {
              "orcid-activities": {
                "$": {},
                "orcid-works": formatWorks(adsData)
              }
            }
          }
        };

        return orcidWorksMessage;

      },

      processOrcidAction: function(data){
        if (data.actionType == 'insert'){
          var orcidWorksMessage = this.fillOrcidWorks([data.model]);

          this.addWorks(orcidWorksMessage);
        }
        else if (data.actionType == 'bulkInsert'){
          var orcidWorksMessage = this.fillOrcidWorks(data.model);

          this.addWorks(orcidWorksMessage);
        }
        else if (data.actionType == 'delete') {

          if (data.modelType == 'adsData') {
            var adsIdsWithPutCodeList = OrcidModel.get('adsIdsWithPutCodeList');
            var formattedAdsId = "ads:" + data.model.id;

            // find putcode

            var putCodes = adsIdsWithPutCodeList.filter(function (e) {
              return e.adsId == formattedAdsId
            });

            if (putCodes.length < 1) {
              return;
            }

            this.deleteWorks([putCodes[0].putCode]);
          } else if (data.modelType == 'orcidData') {
            this.deleteWorks([data.model.putCode]);
          }
        }
        else if (data.actionType == 'update'){
          if (data.modelType == 'adsData'){
            // fill orcid message with putCode

            var that = this;
            var beeHive = this.getBeeHive();
            var LocalStorage = beeHive.getService("LocalStorage");
            var userSession = LocalStorage.getObject("userSession");

            var orcidWorks = userSession.orcidProfile['#document']['orcid-message']['orcid-profile']['orcid-activities']['orcid-works']['orcid-work'];

            var formattedAdsId = "ads:" + data.model.id;

            var foundOrcidWork =
            orcidWorks.filter(function (orcidWork) {
              var adsIds = that.getAdsIds(orcidWork);
              if (!adsIds || adsIds.length < 1)
                return false;
              return adsIds.filter(function (adsId) {
                  return adsId["work-external-identifier-id"] == formattedAdsId;
                }).length > 0;
            });

            if (foundOrcidWork.length < 1){
              return;
            }

            var putCode = foundOrcidWork[0].$['put-code'];

            var newOrcidWork = this.formatOrcidWork(data.model, putCode);

            this.updateWorks([newOrcidWork]);

          }
          else if (data.model.modelType == 'orcidData'){
            // probably ignore
            // should do the fetch of fresh ads data based on adsId
            // and create orcidData based on this
          }
        }
      },

      refreshUserProfile: function() {
        var _that = this;

        return this.getUserProfile()
          .done(function (orcidProfileXml) {
            var beeHive = _that.getBeeHive();
            var LocalStorage = beeHive.getService("LocalStorage");
            var userSession = LocalStorage.getObject("userSession");

            userSession.orcidProfile = $.xml2json(orcidProfileXml);

            LocalStorage.setObject("userSession", userSession);

            _that.pubSub.publish(_that.pubSub.ORCID_ANNOUNCEMENT,
              {
                msgType: OrcidApiConstants.Events.UserProfileRefreshed,
                data: userSession.orcidProfile['#document']['orcid-message']['orcid-profile']
              });
          })
      },

      getUserProfile: function() {
        var beeHive = this.getBeeHive();
        var LocalStorage = beeHive.getService("LocalStorage");
        var userSession = LocalStorage.getObject("userSession");

        return this.sendData({
          type: "GET",
          url: ORCID_PROFILE_URL.format(userSession.authData.orcid),
          headers: {
            Authorization: "Bearer {0}".format(userSession.authData.access_token)
          }
        })
      },

      showLoginDialog: function () {
        var ORCID_REDIRECT_URI = 'http://localhost:3000/oauthRedirect.html';

        var url = ORCID_OAUTH_LOGIN_URL.format(ORCID_REDIRECT_URI);

        var WIDTH = 600;
        var HEIGHT = 650;
        var left = (screen.width / 2) - (WIDTH / 2);
        var top = (screen.height / 2) - (HEIGHT / 2);

        this.cleanLoginWindow();

        this.loginWindow = window.open(url, "ORCID Login", 'width=' + WIDTH + ', height=' + HEIGHT + ', top=' + top + ', left=' + left);
        this.loginWindow.onbeforeunload = _.bind(function(e) {
          this.cleanLoginWindow();
          this.pubSub.publish(this.pubSub.ORCID_ANNOUNCEMENT,
            {
              msgType: OrcidApiConstants.Events.LoginCancelled
            });
        }, this);
      },

      addWorks: function(orcidWorks) {
        var beeHive = this.getBeeHive();
        var LocalStorage = beeHive.getService("LocalStorage");
        var userSession = LocalStorage.getObject("userSession");
        var Json2Xml = beeHive.getService("Json2Xml");

        orcidWorks = addXmlHeadersToOrcidMessage(orcidWorks);

        var _that = this;

        return this.sendData({
          type: "POST",
          url: ORCID_WORKS_URL.format(userSession.authData.orcid),
          data: Json2Xml.xml(orcidWorks, { attributes_key: '$', header: true }),
          headers: {
            Authorization: "Bearer {0}".format(userSession.authData.access_token),
            "Content-Type": "application/orcid+xml"
          }})
          .done(function() {
            _that.refreshUserProfile();
          });
      },

      deleteWorks: function(putCodes) {
        var _that = this;

        var deferred = $.Deferred();

        this.getUserProfile()
          .done(function(data) {
            var xml = $.xml2json(data);
            var message = xml['#document'];
            var orcidWorks = message['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"];

            // Exclude works not comming from ADS and works to delete
            orcidWorks["orcid-work"] = orcidWorks["orcid-work"].filter(function(orcidWork) {
              return _that.isWorkFromAds(orcidWork)
                && putCodes.indexOf(orcidWork.$["put-code"]) == -1;
            });

            if (orcidWorks["orcid-work"].length == 0) {
              delete orcidWorks["orcid-work"];
            }

            _that.replaceAllWorks(message)
              .done(function() {
                deferred.resolve();
              })
              .fail(function(err) {
                deferred.reject(err);
              });
          })
          .fail(function(err) {
            deferred.reject(err);
          });

        return deferred.promise();

      },

      updateWorks: function(orcidWorksToUpdate) {

        var putCodesToUpdate = orcidWorksToUpdate.map(function(item) {
          return item['$']["put-code"];
        });

        var _that = this;

        var deferred = $.Deferred();

        this.getUserProfile()
          .done(function(data) {
            var xml = $.xml2json(data);
            var message = xml['#document'];
            var orcidWorks = message['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"];

            // Exclude works not comming from ADS and works to update
            orcidWorks["orcid-work"] = orcidWorks["orcid-work"].filter(function(orcidWork) {
              return _that.isWorkFromAds(orcidWork)
                && putCodesToUpdate.indexOf(orcidWork.$["put-code"]) == -1;
            });

            orcidWorks["orcid-work"] = orcidWorks["orcid-work"].concat(orcidWorksToUpdate);

            if (orcidWorks["orcid-work"].length == 0) {
              delete orcidWorks["orcid-work"];
            }

            _that.replaceAllWorks(message)
              .done(function() {
                deferred.resolve();
              })
              .fail(function(err) {
                deferred.reject(err);
              });
          })
          .fail(function(err) {
            deferred.reject(err);
          });

        return deferred.promise();

      },

      replaceAllWorks: function(orcidWorks) {
        var beeHive = this.getBeeHive();
        var LocalStorage = beeHive.getService("LocalStorage");
        var userSession = LocalStorage.getObject("userSession");
        var Json2Xml = beeHive.getService("Json2Xml");

        orcidWorks = addXmlHeadersToOrcidMessage(orcidWorks);

        var _that = this;

        return this.sendData({
          type: "PUT",
          url: ORCID_WORKS_URL.format(userSession.authData.orcid),
          data: Json2Xml.xml(orcidWorks, { attributes_key: '$', header: true }),
          headers: {
            Authorization: "Bearer {0}".format(userSession.authData.access_token),
            "Content-Type": "application/orcid+xml"
          }})
          .done(function() {
            _that.refreshUserProfile();
          });
      },

      sendData: function (opts) {

        var request = '';

        var _opts = {
          type: 'GET',
          url: opts.url,
          dataType: 'json',
          data: opts.data,
          contentType: 'application/x-www-form-urlencoded',
          cache: false,
          headers: opts.headers || {},
          context: {request: request, api: self },
          done: opts.done,
          fail: opts.fail,
          always: opts.always
        };

        var jqXhr = $.ajax(opts)
          .always(_opts.always ? [this.always, _opts.always] : this.always)
          .done(_opts.done || this.done)
          .fail(_opts.fail || this.fail);

        jqXhr = jqXhr.promise(jqXhr);

        return jqXhr;
      },

      done: function () {

      },
      fail: function () {

      },
      always: function () {

      },

      getHardenedInstance: function () {
        // TODO : expose just necassary functions

        return this;
      }
    });

    _.extend(OrcidApi.prototype, Mixins.BeeHive);

    return OrcidApi;
  });