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

    describe('Orcid login widget (orcid_login_widget.spec.js)', function(){

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
        var $w = widget.render().$el;
        expect($w.find('button.login-orcid-button').text().trim()).to.be.eql('Orcid login');
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
        var $w = widget.render().$el;

        minsub.publish(PubSubEvents.ORCID_ANNOUNCEMENT, {
          msgType: OrcidApiConstants.Events.LoginSuccess,
          data: orcidProfileJson
        });

        expect($w.find(".orcid-bio div").text().trim()).to.be.eql("Orcid name: Obrátil, Martin");
        expect($w.find(".signout-orcid-button").text().trim()).to.be.eql("sign out");

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

        var $w = widget.render().$el;
        expect($w.find("button.login-orcid-button").text().trim()).to.be.eql('Orcid login');

        done();
      });

      var _getWidget = function() {
        var widget = new OrcidLogin();
        widget.activate(minsub.beehive.getHardenedInstance());
        return widget;
      };
    })
  });