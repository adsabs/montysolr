define([
  'underscore',
  'js/bugutils/minimal_pubsub',
  'es6!js/widgets/orcid-selector/widget.jsx'
], function (_, MinPubSub, Widget) {

  var before = function () {
    this.sb = sinon.sandbox.create();
    this.w = new Widget();
    this.minsub = new MinPubSub();
    this.pubsub = this.minsub.pubsub.getHardenedInstance();
    this.w.activate(this.minsub.beehive.getHardenedInstance());
    this.state = _.bind(function () {
      return this.w.store.getState().get('OrcidSelectorApp');
    }, this);
  };

  var after = function () {
    this.sb.restore();
    this.w.destroy();
    this.w = null;
  };

  describe('Orcid Selector Widget (orcid_selector_widget.spec.js)', function () {
    beforeEach(before);
    afterEach(after);

    it('Does not render if orcidModeIsOn is false', function () {

      // check to make sure mode is off
      expect(this.state().get('mode')).to.equal(false);

      // render and see that it's empty
      expect(this.w.view.render().$el.children().length).to.equal(0);
    });

    it('Does render if orcidModeIsOn is true', function () {
      this.minsub.beehive.addObject('User', { isOrcidModeOn: _.constant(true) });

      this.w.activate(this.minsub.beehive);

      // check to make sure mode is on
      expect(this.state().get('mode')).to.equal(true);

      // render and see that it's empty
      expect(this.w.view.render().$el.children().length).to.be.gt(0);
    });

    it('State is updated correctly when mode is changed', function () {

      // should be off initially
      expect(this.state().get('mode')).to.equal(false);

      // trigger a user event
      this.pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, '', {
        isOrcidModeOn: true
      });

      // should now be on
      expect(this.state().get('mode')).to.equal(true);
    });

    it('State is update correctly when number of papers is changed', function () {
      this.minsub.beehive.addObject('AppStorage', {
        getSelectedPapers: function () { return ['foo', 'bar']; }
      });

      this.w.activate(this.minsub.beehive);

      expect(this.state().get('selected').toJS().length).to.equal(0);

      this.pubsub.publish(this.pubsub.STORAGE_PAPER_UPDATE);

      expect(this.state().get('selected').length).to.equal(2);
    });

    it('Clicking claim button publishes the proper claim event', function () {
      this.minsub.beehive.addObject('User', { isOrcidModeOn: _.constant(true) });
      this.minsub.beehive.addObject('AppStorage', {
        getSelectedPapers: function () { return ['foo', 'bar']; }
      });
      this.w.activate(this.minsub.beehive);

      // render and grab element
      var $el = this.w.view.$el;
      $('#test-area').append($el);
      this.w.view.render();

      // create subscription
      var spy = this.sb.spy();
      this.pubsub.subscribeOnce(this.pubsub.CUSTOM_EVENT, spy);

      // click the claim button
      $('button:contains("Claim")', $el).click();

      // then click the apply button
      $('button:contains("Apply")', $el).click();

      expect(spy.args[0][0]).is.equal('orcid-bulk-claim');
      expect(spy.args[0][1]).is.equal(this.state().get('selected'));
    });

    it('Clicking delete button publishes the proper delete event', function () {
      this.minsub.beehive.addObject('User', { isOrcidModeOn: _.constant(true) });
      this.minsub.beehive.addObject('AppStorage', {
        getSelectedPapers: function () { return ['foo', 'bar']; }
      });
      this.w.activate(this.minsub.beehive);

      // render and grab hold of the element
      var $el = this.w.view.$el;
      $('#test-area').append($el);
      this.w.view.render();

      // add a subscription
      var spy = this.sb.spy();
      this.pubsub.subscribeOnce(this.pubsub.CUSTOM_EVENT, spy);

      // click the claim button
      $('button:contains("Delete")', $el).click();

      // then click the apply button
      $('button:contains("Apply")', $el).click();

      expect(spy.args[0][0]).is.equal('orcid-bulk-delete');
      expect(spy.args[0][1]).is.equal(this.state().get('selected'));
    });

  });

});
