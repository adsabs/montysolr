define(['js/components/generic_module', 'js/services/pubsub',
  'backbone'], function(GenericModule, PubSub, Backbone) {

  describe("PubSub (Service)", function () {
      
    it("should return PubSub object (generic module)", function() {
      expect(new PubSub()).to.be.an.instanceof(GenericModule);
      expect(new PubSub()).to.be.an.instanceof(PubSub);
    });

    it("has trigger/listenTo/once - but it doesn't have on/off/bin/unbind", function() {

      var spy = sinon.spy();
      var allSpy = sinon.spy();
      var pubs = new PubSub();
      var eventer = _.extend({}, Backbone.Events);

      expect(spy.called).to.be.false;

      eventer.listenTo(pubs, 'event:a', spy);
      eventer.listenTo(pubs, allSpy);

      expect(pubs.trigger('event:0'));
      expect(pubs.trigger('event:a'));
      expect(pubs.trigger('event:b'));

      expect(spy.called).to.be.true;

      expect(function() {new PubSub().on('foo', function() {})}).to.throw(/PubSub forbids calling this method.*/);
      expect(function() {new PubSub().bind('foo', function() {})}).to.throw(/PubSub forbids calling this method.*/);

      expect(function() {new PubSub().off('foo')}).to.throw(/PubSub forbids calling this method.*/);
      expect(function() {new PubSub().unbind('foo')}).to.throw(/PubSub forbids calling this method.*/);
    });

  });
});
