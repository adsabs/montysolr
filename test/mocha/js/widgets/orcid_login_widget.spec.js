define(['marionette',
    'backbone',
    'js/bugutils/minimal_pubsub',
    'js/modules/orcid/orcid_login/widget',
    'js/modules/orcid/orcid_api_constants',
    'js/components/pubsub_events'],
  function (Marionette,
            Backbone,
            MinimalPubsub,
            OrcidLogin,
            OrcidApiConstants,
            PubSubEvents) {

    describe('Orcid login widget', function(){

      var orcidProfileJson = {
        "orcid-bio": {
          "$": {},
          "personal-details": {
            "$": {},
            "given-names": "Martin",
            "family-name": "Obrátil"
          }
        }
      };

      var minsub;
      beforeEach(function(done) {

        minsub = new MinimalPubsub({verbose: false});

        done();
      });


      it('should show login button', function(done){

        var widget = _getWidget();

        var $renderResult = widget.render();

        var loginButton = $renderResult.$el.find("button.login-orcid-button")[0];

        expect(loginButton.innerText == "Orcid login ").to.be.true;

        done();
      });

      it('should show wait spinner next to login button', function(done){

        var widget = _getWidget();

        widget.onAllInternalEvents('loginwidget:loginRequested');

        var $renderResult = widget.render();

        expect($renderResult.$el.find(".orcid-wait").length == 1).to.be.true;

        done();
      });

      it('should show name of user after successful login', function(done){

        var widget = _getWidget();
        var $renderResult = widget.render();

        minsub.publish(PubSubEvents.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.LoginSuccess,
          data: orcidProfileJson
        });

        expect($renderResult.$el.find(".orcid-bio div")[0].innerText == "Orcid name: Obrátil, Martin").to.be.true;
        expect($renderResult.$el.find(".signout-orcid-button")[0].innerText == "sign out").to.be.true;

        done();

      });

      it('should show login button after sign out', function(done){

        var widget = _getWidget();

        minsub.publish(PubSubEvents.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.LoginSuccess,
          data: orcidProfileJson
        });

        minsub.publish(PubSubEvents.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.SignOut
        });

        var $renderResult = widget.render();

        var loginButton = $renderResult.$el.find("button.login-orcid-button")[0];

        expect(loginButton.innerText == "Orcid login ").to.be.true;

        done();
      });

      var _getWidget = function() {
        var widget = new OrcidLogin();
        widget.activate(minsub.beehive.getHardenedInstance());
        return widget;
      };
    })
  });