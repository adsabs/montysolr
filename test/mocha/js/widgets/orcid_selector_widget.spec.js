define([
  'underscore',
  'js/bugutils/minimal_pubsub',
  'es6!js/widgets/orcid-selector/widget.jsx'
], function (_, MinPubSub, Widget) {

  var init = function () {
    this.sb = sinon.sandbox.create();
    this.pubsub = new (MinPubSub.extend({
      request: _.identity
    }))({ verbose: false });
    this.state = function (w) {
      return w.store.getState().get('OrcidSelectorApp');
    };
  };

  var teardown = function () {
    this.sb.restore();
    this.state = null;
    $('#test-area').empty();
  };

  describe('Orcid Selector Widget (orcid_selector_widget.spec.js)', function () {
    beforeEach(init);
    afterEach(teardown);

    it('Does not render if orcidModeIsOn is false', function () {
      const w = new Widget();
      w.activate(this.pubsub.beehive);

      // check to make sure mode is off
      expect(this.state(w).get('mode')).to.equal(false);

      // render and see that it's empty
      expect(w.view.render().$el.children().length).to.equal(0);
    });

    it('Does render if orcidModeIsOn is true', function () {
      const w = new Widget();
      const beehive = this.pubsub.beehive;
      beehive.addObject('User', { isOrcidModeOn: _.constant(true) });
      w.activate(beehive);

      // check to make sure mode is on
      expect(this.state(w).get('mode')).to.equal(true);

      // render and see that it's empty
      expect(w.view.render().$el.children().length).to.be.gt(0);
    });

    it('State is updated correctly when mode is changed', function () {
      const w = new Widget();
      w.activate(this.pubsub.beehive);
      // should be off initially
      expect(this.state(w).get('mode')).to.equal(false);

      // trigger a user event
      this.pubsub.publish(this.pubsub.USER_ANNOUNCEMENT, '', {
        isOrcidModeOn: true
      });

      // should now be on
      expect(this.state(w).get('mode')).to.equal(true);
    });

    it('State is update correctly when number of papers is changed', function () {
      const w = new Widget();
      const beehive = this.pubsub.beehive;
      beehive.addObject('AppStorage', {
        getSelectedPapers: function () { return ['foo', 'bar']; }
      });
      w.activate(beehive);

      expect(this.state(w).get('selected').toJS().length).to.equal(0);

      this.pubsub.publish(this.pubsub.STORAGE_PAPER_UPDATE);

      expect(this.state(w).get('selected').length).to.equal(2);
    });

    it('Clicking claim button publishes the proper claim event', function () {
      const w = new Widget();
      const beehive = this.pubsub.beehive;
      beehive.addObject('User', { isOrcidModeOn: _.constant(true) });
      beehive.addObject('AppStorage', {
        getSelectedPapers: function () { return ['foo', 'bar']; }
      });
      w.activate(beehive);

      // render and grab element
      var $el = w.view.$el;
      $('#test-area').append($el);
      w.view.render();

      // create subscription
      var spy = this.sb.spy();
      this.pubsub.subscribeOnce(this.pubsub.CUSTOM_EVENT, spy);

      // click the claim button
      $('button:contains("Claim")', $el).click();

      // then click the apply button
      $('button:contains("Apply")', $el).click();

      expect(spy.args[0][0]).is.equal('orcid-bulk-claim');
      expect(spy.args[0][1]).is.equal(this.state(w).get('selected'));
    });

    it('Clicking delete button publishes the proper delete event', function () {
      const w = new Widget();
      const beehive = this.pubsub.beehive;
      beehive.addObject('User', { isOrcidModeOn: _.constant(true) });
      beehive.addObject('AppStorage', {
        getSelectedPapers: function () { return ['foo', 'bar']; }
      });
      w.activate(beehive);

      // render and grab hold of the element
      var $el = w.view.$el;
      $('#test-area').append($el);
      w.view.render();

      // add a subscription
      var spy = this.sb.spy();
      this.pubsub.subscribeOnce(this.pubsub.CUSTOM_EVENT, spy);

      // click the claim button
      $('button:contains("Delete")', $el).click();

      // then click the apply button
      $('button:contains("Apply")', $el).click();

      expect(spy.args[0][0]).is.equal('orcid-bulk-delete');
      expect(spy.args[0][1]).is.equal(this.state(w).get('selected'));
    });
  });
});
