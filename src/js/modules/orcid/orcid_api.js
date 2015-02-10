define([
    'underscore',
    'bootstrap',
    'jquery',
    'xml2json',
    'backbone',
    'js/components/generic_module',
    'js/mixins/dependon',
    'js/mixins/string_utils',
    'js/modules/orcid/orcid_api_constants',
    'js/components/pubsub_events',
    'js/mixins/link_generator_mixin',
    'js/modules/orcid/json2xml',
    'js/components/api_query',
    'js/components/api_request'
  ],
  function (
    _,
    Bootstrap,
    $,
    Xml2json,
    Backbone,
    GenericModule,
    Mixins,
    StringUtils,
    OrcidApiConstants,
    PubSubEvents,
    LinkGeneratorMixin,
    Json2Xml,
    ApiQuery,
    ApiRequest
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


    var OrcidApi = GenericModule.extend({

      initialize: function() {
        this.orcidProxyUri = '';
        this.userData = {};
        this.authData = null;
      },

      activate: function (beehive) {
        this.setBeeHive(beehive);
        this.pubsub = this.getBeeHive().getService('PubSub').getHardenedInstance();

        var config = beehive.getObject('DynamicConfig');
        this.config = {};
        _.extend(this.config, config['Orcid']);

        var storage = beehive.getService('PersistentStorage');
        if (storage) {
          var orcid = storage.get('Orcid');
          if (orcid && orcid.authData) {
            //TODO:rca - check the access_token works (by loading profile)
            this.saveAccessData(orcid.authData);
          }
        }
      },


      hasAccess: function() {
        if (this.authData)
          return true;
        return false;
      },

      /**
       * Redirects to ORCID where the user logs in and ORCID will forward
       * user back to us
       */
      signIn: function () {
        this.pubsub.publish(this.pubsub.APP_EXIT, {url: this.config.loginUrl});
      },

      /**
       * Forgets the OAuth access_token
       */
      signOut: function () {
        this.authData = null;
        //this.getBeeHive()
        //  .getService('LocalStorage')
        //  .setObject("userSession", {isEmpty: true});
      },

      hasExchangeCode: function (searchString) {
        var code = this.getUrlParameter('code', searchString || window.location.search);
        if (code)
          return true;
      },

      getExchangeCode: function (searchString) {
        return this.getUrlParameter('code', searchString || window.location.search);
      },

      /**
       * Using access code retrieve the access_token from ORCID
       *
       * Example response:
       *  {"access_token":"4274a0f1-36a1-4152-9a6b-4246f166bafe","token_type":"bearer","ex
       *  pires_in":3599,"scope":"/orcid-works/create /orcid-profile/read-limited /orcid-w
       *  orks/update","orcid":"0000-0001-8178-9506","name":"Roman Chyla"}
       *
       * @param oAuthCode
       * @returns {*}
       */
      getAccessData: function (oAuthCode) {
        var api = this.getBeeHive().getService('Api');
        var promise = $.Deferred();
        api.request(
          new ApiRequest({target: this.config.exchangeTokenUrl, query: new ApiQuery({code: oAuthCode})}),
          {
            url: this.config.exchangeTokenUrl,
            done: function (data, textStatus, jqXHR) {
              promise.resolve(data);
            },
            fail: function (jqXHR, textStatus, errorThrown) {
              promise.reject(jqXHR);
            },
            headers: {
              Accept: 'application/json'
            }
          }
        );
        return promise.promise();
      },

      saveAccessData: function(authData) {
        var beehive = this.getBeeHive();
        //var LocalStorage = beeHive.getService("LocalStorage");
        //LocalStorage.setObject("userSession", {
        //  authData: authData
        //});
        this.authData = authData;
        var storage = beehive.getService('PersistentStorage');
        if (storage) {
          var orcid = storage.get('Orcid') || {};
          orcid.authData = authData;
          storage.set('Orcid', orcid);
        }
      },

      /**
       * This function was there (and it is not clear where it was used)
       * blocshop got totally confused (their code duplicated) and i removed
       * the awful cycle of redirections
       *
       * XXX:rca - find a place to use this
       */
      getOrcidProfile: function() {
        var userSession = LocalStorage.getObject("userSession");
        var orcidProfile = {};
        if (userSession.orcidProfile['#document'] != undefined) {
          orcidProfile = userSession.orcidProfile['#document']['orcid-message']['orcid-profile'];
        }
        else {
          orcidProfile = userSession.orcidProfile['orcid-message']['orcid-profile'];
        }
        return orcidProfile;
      },

      /**
       * Extract values from the URL (used to get code from redirects)
       *
       * @param sParam
       * @returns {string}
       */
      getUrlParameter: function (sParam, searchString) {
        var sPageURL = searchString.substring(1);
        var sURLVariables = sPageURL.split('&');
        for (var i = 0; i < sURLVariables.length; i++) {
          var sParameterName = sURLVariables[i].split('=');
          if (sParameterName[0] == sParam) {
            return decodeURIComponent(sParameterName[1]);
          }
        }
      },


      /**
       * From the ORCID response extract all IDs that are known to ADS
       *
       * @param orcidWork
       * @returns {*}
       */
      getAdsIds: function (orcidWork) {
        var extIdentifiersObj = orcidWork["work-external-identifiers"];
        if (!extIdentifiersObj) {
          return [];
        }

        var extIdentifiers = extIdentifiersObj["work-external-identifier"] || [];
        if (!(extIdentifiers instanceof Array)) {
          extIdentifiers = [extIdentifiers];
        }

        var adsExtIdentifiers = extIdentifiers.filter(function (extIdentifier) {
          return extIdentifier["work-external-identifier-id"].indexOf("ads:") != -1;
        });

        return adsExtIdentifiers;
      },

      isWorkFromAds: function (orcidWork) {
        // XXX:rca this is very stupid
        var result = this.getAdsIds(orcidWork);
        return result != undefined && result.length > 0;
      },

      formatOrcidWork: function (adsWork, putCode) {
        var formatContributors = function (adsAuthors) {

          var result = [];

          _.each(adsAuthors, function (author) {
            result.push({
              "credit-name": author,
              "contributor-attributes": {
                "contributor-role": "author"
              }
            });
          });

          return {
            contributor: result
          };
        };

        var result =
        {
          "work-title": {
            "$": {},
            "title": adsWork.title.join(' ')
          },

          "short-description": adsWork.abstract,
          //"publication-date": {
          //  "year": adsWork.pubdate.split(' ')[1]
          //},

          "work-external-identifiers": {
            "work-external-identifier": [
              {
                "work-external-identifier-type": 'bibcode',
                "work-external-identifier-id": adsWork.bibcode
              },
              {
                "work-external-identifier-type": 'other-id',
                "work-external-identifier-id": 'ads:' + adsWork.id
              }
              // TODO : add DOI, if available
            ]
          },

          "work-type": "book",

          "work-contributors": formatContributors(adsWork.author),

          "url": adsWork.doi ? LinkGeneratorMixin.adsUrlRedirect("doi", adsWork.doi) : '' // TODO : in item_view model DOI is missing

        };

        if (adsWork.pubdate && adsWork.pubdate.length > 0 && adsWork.pubdate.indexOf(' ') > 0) {
          result['publication-date'] = {
            "year": adsWork.pubdate.split(' ')[1]
          }
        }

        if (putCode) {
          result["$"] = {"put-code": putCode};
        }

        return result;
      },

      fillOrcidWorks: function (adsData) {

        //"{"abstract":"Laser active imaging systems are widespread tools used in region surveillance and threat identification. However, the photoelectric imaging detector in the imaging systems is easy to be disturbed and this leads to errors of the recognition and even the missing of the target. In this paper, a novel wavelet-weighted multi-scale structural similarity (WWMS-SSIM) algorithm is proposed. 2-D four-level wavelet decomposition is performed for the original and disturbed images. Each image can be partitioned into one low-frequency subband (LL) and a series of octave high-frequency subbands (HL, LH and HH). Luminance, contrast and structure comparison are computed in different subbands with different weighting factors. Based on the results of the above, we can construct a modified WWMS-SSIM. Cross-distorted image quality assessment experiments show that the WWMS-SSIM algorithm is more suitable for the subjective visual feeling comparing with NMSE and SSIM. In the laser-dazzling image quality assessment experiments, the WWMS-SSIM gives more reasonable evaluations to the images with different power and laser spot positions, which can be useful to give the guidance of the laser active imaging system defense and application.","pub":"Optics Laser Technology","volume":"67","email":["-","-","-","-"],"bibcode":"2015OptLT..67..183Q","year":"2015","id":"10666236","keyword":["Image quality assessment","Laser-dazzling effect","Wavelet decomposition"],"author":["Qian, Fang","Guo, Jin","Sun, Tao","Wang, Tingfeng"],"aff":["State Key Laboratory of Laser Interaction with Matter, Changchun Institute of Optics, Fine Mechanics and Physics Chinese Academy of Sciences, Changchun 130033, Jilin, China","State Key Laboratory of Laser Interaction with Matter, Changchun Institute of Optics, Fine Mechanics and Physics Chinese Academy of Sciences, Changchun 130033, Jilin, China","State Key Laboratory of Laser Interaction with Matter, Changchun Institute of Optics, Fine Mechanics and Physics Chinese Academy of Sciences, Changchun 130033, Jilin, China","State Key Laboratory of Laser Interaction with Matter, Changchun Institute of Optics, Fine Mechanics and Physics Chinese Academy of Sciences, Changchun 130033, Jilin, China"],"title":["Quantitative assessment of laser-dazzling effects through wavelet-weighted multi-scale SSIM measurements"],"[citations]":{"num_citations":0,"num_references":2},"identifier":"2015OptLT..67..183Q","resultsIndex":0,"details":{"highlights":["-frequency subband <em>(LL)</em> and a series of octave high-frequency subbands (HL, LH and HH). Luminance, contrast"]},"num_citations":0,"links":{"text":[],"list":[{"letter":"R","title":"References (2)","link":"/#abs/2015OptLT..67..183Q/references"}],"data":[]},"emptyPlaceholder":false,"visible":true,"actionsVisible":true,"orcidActionsVisible":false}"

        var that = this;
        var formatWorks = function (adsWorks) {

          var result = [];
          _.each(adsWorks,
            function (adsWork) {
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

      processOrcidAction: function (data) {
        if (data.actionType == 'insert') {
          var orcidWorksMessage = this.fillOrcidWorks([data.model]);

          this.addWorks(orcidWorksMessage);
        }
        else if (data.actionType == 'bulkInsert') {
          var orcidWorksMessage = this.fillOrcidWorks(data.model);

          this.addWorks(orcidWorksMessage);
        }
        else if (data.actionType == 'delete') {

          if (data.modelType == 'adsData') {
            var adsIdsWithPutCodeList = this.getBeeHive().getService('OrcidModelNotifier').getAdsIdsWithPutCodeList();
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
        else if (data.actionType == 'update') {
          if (data.modelType == 'adsData') {
            // fill orcid message with putCode

            var that = this;
            var beeHive = this.getBeeHive();
            var LocalStorage = beeHive.getService("LocalStorage");
            var userSession = LocalStorage.getObject("userSession");

            var orcidProfile = {};

            if (userSession.orcidProfile['#document'] != undefined) {
              orcidProfile = userSession.orcidProfile['#document']['orcid-message']['orcid-profile'];
            }
            else {
              orcidProfile = userSession.orcidProfile['orcid-message']['orcid-profile'];
            }

            var orcidWorks = orcidProfile['orcid-activities']['orcid-works']['orcid-work'];

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

            if (foundOrcidWork.length < 1) {
              return;
            }

            var putCode = foundOrcidWork[0].$['put-code'];

            var newOrcidWork = this.formatOrcidWork(data.model, putCode);

            this.updateWorks([newOrcidWork]);

          }
          else if (data.model.modelType == 'orcidData') {
            // probably ignore
            // should do the fetch of fresh ads data based on adsId
            // and create orcidData based on this
          }
        }
      },

      refreshUserProfile: function () {
        var that = this;
        return this.getUserProfile()
          .done(function (orcidProfileXml) {
            var beeHive = that.getBeeHive();
            var LocalStorage = beeHive.getService("LocalStorage");
            var userSession = LocalStorage.getObject("userSession");

            userSession.orcidProfile = $.xml2json(orcidProfileXml);

            LocalStorage.setObject("userSession", userSession);

            var orcidProfile = {};

            if (userSession.orcidProfile['#document'] != undefined) {
              orcidProfile = userSession.orcidProfile['#document']['orcid-message']['orcid-profile'];
            }
            else {
              orcidProfile = userSession.orcidProfile['orcid-message']['orcid-profile'];
            }

            that.pubsub.publish(that.pubsub.ORCID_ANNOUNCEMENT,
              {
                msgType: OrcidApiConstants.Events.UserProfileRefreshed,
                data: orcidProfile
              });
          })
      },

      getUserProfile: function () {
        var beeHive = this.getBeeHive();
        var LocalStorage = beeHive.getService("LocalStorage");
        var userSession = LocalStorage.getObject("userSession");

        return this.sendData({
          type: "GET",
          url: StringUtils.format(this.config.profileUrl, userSession.authData.orcid),
          headers: {
            Authorization: StringUtils.format("Bearer {0}", userSession.authData.access_token)
          }
        })
      },


      addWorks: function (orcidWorks) {
        var beeHive = this.getBeeHive();
        var LocalStorage = beeHive.getService("LocalStorage");
        var userSession = LocalStorage.getObject("userSession");

        orcidWorks = addXmlHeadersToOrcidMessage(orcidWorks);

        var _that = this;

        return this.sendData({
          type: "POST",
          url: StringUtils.format(this.config.worksUrl, userSession.authData.orcid),
          data: Json2Xml.transform(orcidWorks, { attributes_key: '$', header: true }),
          headers: {
            Authorization: StringUtils.format("Bearer {0}", userSession.authData.access_token),
            "Content-Type": "application/orcid+xml"
          }})
          .done(function () {
            _that.refreshUserProfile();
          });
      },

      deleteWorks: function (putCodes) {
        var _that = this;

        var deferred = $.Deferred();

        this.getUserProfile()
          .done(function (data) {
            var xml = $.xml2json(data);
            var message = xml['#document'];

            if (xml['#document'] != undefined) {
              message = xml['#document'];
            }
            else {
              message = xml;
            }

            var orcidWorks = message['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"];

            // Exclude works not comming from ADS and works to delete
            orcidWorks["orcid-work"] = orcidWorks["orcid-work"].filter(function (orcidWork) {
              return _that.isWorkFromAds(orcidWork)
                && putCodes.indexOf(orcidWork.$["put-code"]) == -1;
            });

            if (orcidWorks["orcid-work"].length == 0) {
              delete orcidWorks["orcid-work"];
            }

            _that.replaceAllWorks(message)
              .done(function () {
                deferred.resolve();
              })
              .fail(function (err) {
                deferred.reject(err);
              });
          })
          .fail(function (err) {
            deferred.reject(err);
          });

        return deferred.promise();

      },

      updateWorks: function (orcidWorksToUpdate) {

        var putCodesToUpdate = orcidWorksToUpdate.map(function (item) {
          return item['$']["put-code"];
        });

        var _that = this;

        var deferred = $.Deferred();

        this.getUserProfile()
          .done(function (data) {
            var xml = $.xml2json(data);
            var message = xml['#document'];

            if (xml['#document'] != undefined) {
              message = xml['#document'];
            }
            else {
              message = xml;
            }

            var orcidWorks = message['orcid-message']['orcid-profile']["orcid-activities"]["orcid-works"];

            // Exclude works not comming from ADS and works to update
            orcidWorks["orcid-work"] = orcidWorks["orcid-work"].filter(function (orcidWork) {
              return _that.isWorkFromAds(orcidWork)
                && putCodesToUpdate.indexOf(orcidWork.$["put-code"]) == -1;
            });

            orcidWorks["orcid-work"] = orcidWorks["orcid-work"].concat(orcidWorksToUpdate);

            if (orcidWorks["orcid-work"].length == 0) {
              delete orcidWorks["orcid-work"];
            }

            _that.replaceAllWorks(message)
              .done(function () {
                deferred.resolve();
              })
              .fail(function (err) {
                deferred.reject(err);
              });
          })
          .fail(function (err) {
            deferred.reject(err);
          });

        return deferred.promise();

      },

      replaceAllWorks: function (orcidWorks) {
        var beeHive = this.getBeeHive();
        var LocalStorage = beeHive.getService("LocalStorage");
        var userSession = LocalStorage.getObject("userSession");

        orcidWorks = addXmlHeadersToOrcidMessage(orcidWorks);

        var _that = this;

        return this.sendData({
          type: "PUT",
          url: StringUtils.format(this.config.worksUrl, userSession.authData.orcid),

          headers: {
            Authorization: StringUtils.format("Bearer {0}", userSession.authData.access_token),
            "Content-Type": "application/orcid+xml"
          }})
          .done(function () {
            _that.refreshUserProfile();
          });
      },

      /**
       * Use Api service to make a request to the ORCID api
       *
       * @param url - remote endpoint
       * @param data - JSON object
       * @param opts
       * @returns {*}
       */
      sendData: function (url, data, opts) {

        var result = $.Deferred();

        var options = {
          type: 'GET',
          url: url,
          dataType: 'application/xml',
          data: Json2Xml.transform(data, { attributes_key: '$', header: true }),
          cache: false,
          done: function(data) {
            result.resolve(data);
          },
          fail: function(error) {
            result.reject(error);
          }
        };
        _.extend(options, opts);

        if (!options.headers)
          options.headers = {};
        if (!options.headers.Authorization && this.authData)
          options.headers.Authorization = "Bearer " + this.authData.access_token;
        if (!options.headers["Content-Type"])
          options.headers["Content-Type"] = "application/orcid+xml";
        if (!options.headers["Accept"])
          options.headers["Accept"] = "application/json";

        var api = this.getBeeHive().getService('Api');
        api.request(new ApiRequest({target: url, query: new ApiQuery(), options: options}));
        return result.promise();
      },


      hardenedInstance: {
        hasAccess: 'boolean indicating access to ORCID Api',
        signIn: 'login',
        signOut: 'logout'
      }
    });

    _.extend(OrcidApi.prototype, Mixins.BeeHive);

    return OrcidApi;
  });