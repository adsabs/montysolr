define([
  'js/widgets/base/base_widget',
  'js/components/api_request',
  'js/components/api_targets',
  'hbs!./templates/tab-container',
  'hbs!./templates/import-view-ads2',
  'hbs!./templates/import-view-classic',
  'hbs!./templates/success-template',
  'bootstrap'

], function (BaseWidget,
             ApiRequest,
             ApiTargets,
             TabContainerTemplate,
             ADS2ImportView,
             ClassicImportView,
             SuccessTemplate,
             Bootstrap) {
  /* config vars */

  var CLASSIC = 'classic';
  //this is used in the template for the name param -- is sent to server
  // 'twopointoh' is the name used on the server
  var ADS2 = 'twopointoh';

  var ImportModel = Backbone.Model.extend({

    defaults: function () {
      return {}
    }
  });


  var ImportView = Marionette.ItemView.extend({

    initialize: function (options) {
      if (options.endpoint !== CLASSIC && options.endpoint !== ADS2) {
        throw new Error('we don\'t recognize that endpoint: ' + options.endpoint);
      }
      this.model = new ImportModel({endpoint: options.endpoint});

      this.template = ( options.endpoint === CLASSIC ) ? ClassicImportView : ADS2ImportView;

    },

    className: "library-import-form",

    events: {
      'click button.submit-credentials': 'authenticate',
    },

    triggers : {
      'click button.import-all-libraries': 'library-import',
      'click button.import-zotero' : 'zotero-import'
    },

    modelEvents: {
      'change': 'render'
    },

    authenticate: function (e) {
      e.preventDefault();

      var data = this.$("form").serializeArray();

      var toReturn = {};

      data.forEach(function (obj) {
        toReturn[obj.name] = obj.value;
      });

      this.trigger("submit-credentials", toReturn);

    }


  });


  var ContainerView = Marionette.LayoutView.extend({

    initialize: function (options) {
      this.classicView = new ImportView({endpoint: CLASSIC});
      this.ads2View = new ImportView({endpoint: ADS2});
    },

    regions: {
      classic: "#" + CLASSIC + "-import-tab",
      ads2: "#" + ADS2 + "-import-tab"
    },

    template: TabContainerTemplate,

    onRender: function () {
      this.classic.show(this.classicView);
      this.ads2.show(this.ads2View);
    }

  });


  var ImportWidget = BaseWidget.extend({

    initialize: function (options) {
      var options = options || {};
      this.view = new ContainerView();
      var that = this;

      /*
       * subscribe to 1) credential submit events
       * */
      function submitCredentials(endpoint, view, data) {
        this.getBeeHive().getService("Api").request(new ApiRequest({
              target: endpoint,
              options: {
                type: "POST",
                data: data,
                done: function (data) {
                  view.model.set(data, {silent : true});
                  view.model.trigger("change");
                },
                fail : function(data){
                  view.model.set({
                    successMessage: "",
                    errorMessage: data.responseJSON.error
                  }, {silent : true});
                  view.model.trigger("change");
                  //doing it this way (silent then trigger change)
                  // so that user can close the alert and a new version of the
                  //same alert can later still be shown if necessary
                }
              }
            }));
      }

      submitCredentials = submitCredentials.bind(this);

      this.view.classicView.on("submit-credentials", function (data) {
        submitCredentials(ApiTargets.LIBRARY_IMPORT_CLASSIC_AUTH, that.view.classicView, data);
      });

      this.view.ads2View.on("submit-credentials", function (data) {
        submitCredentials(ApiTargets.LIBRARY_IMPORT_ADS2_AUTH, that.view.ads2View, data);
      });

      /*
       * subscribe to library import events
       * this goes through the library controller as the controller
       * has to update its internal store of library metadata on successful import
       * */
      function importLibraries(endpoint, view) {

        this.getBeeHive()
            .getObject("LibraryController")
            .importLibraries(endpoint)
            .done(function(data){
              var successData = {};
              successData.updated = data.filter(function(d){return d.action === "updated"})
                                        .map(function(d){return {name : d.name, link : "#user/libraries/" + d.library_id } });

              successData.created = data.filter(function(d){return d.action === "created"})
                                        .map(function(d){return {name : d.name, link : "#user/libraries/" + d.library_id } });
              view.model.set({
                    successMessage: SuccessTemplate(successData),
                    errorMessage: ""
                  }, {silent : true});

              view.model.trigger("change");
              //doing it this way so that user can close the alert and a new version of the
              //same alert can still be shown if necessary

            })
            .fail(function(){

              view.model.set({
                successMessage: "",
                errorMessage: "There was a problem and libraries were not imported."
              }, {silent:true});

              view.model.trigger("change");

            });

      }

      importLibraries = importLibraries.bind(this);

      this.view.classicView.on("library-import", function () {
        importLibraries(CLASSIC, that.view.classicView);
      });

      this.view.ads2View.on("library-import", function () {
        importLibraries(ADS2, that.view.ads2View);
      });

      //finally, attach listener for zotero import event

      this.view.ads2View.on("zotero-import", function (data) {
        that.getBeeHive().getService("Api").request(new ApiRequest({
          target: ApiTargets.LIBRARY_IMPORT_ZOTERO
        }))
      });

    },

    activate: function (beehive) {
      this.setBeeHive(beehive);
      _.bindAll(this);

      var that = this;
      var pubsub = this.getPubSub();

        //need to load list of classic mirror sites
        that.getBeeHive().getService("Api").request(new ApiRequest(
            {
              target: ApiTargets.LIBRARY_IMPORT_CLASSIC_MIRRORS,
              options: {
                done: function (data) {
                  //set the most used mirror as the default
                  if (data.indexOf("adsabs.harvard.edu") > -1){
                    data = _.without(data, "adsabs.harvard.edu");
                    data.unshift("adsabs.harvard.edu");
                  }
                  that.view.classicView.model.set({
                    mirrors: data
                  })
                },
                fail : function(){
                  console.error("couldn't load classic mirrors")
                }
              }
            }
        ));

        //need to check if already authenticated
        that.getBeeHive().getService("Api").request(new ApiRequest({
          target: ApiTargets.LIBRARY_IMPORT_CREDENTIALS,
          options : {
            done : function (data) {
              /*  returns data in the form
               * {"classic_email": "fake@fakitifake.com", "classic_mirror": "mirror", "twopointoh_email": "fakeads2@gmail.com"}
               */
              that.view.classicView.model.set(data);
              that.view.ads2View.model.set(data);
            },
            fail : function(response, status){
              //if user hasnt registered yet
              if (response.responseJSON.error == "This user has not setup an ADS Classic account") return;
              else console.error(response);
            }
          }
        }));

    }

  });

  return ImportWidget;

});