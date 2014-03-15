/**
 * Created by rchyla on 3/14/14.
 */

define(['js/components/pubsub_key', 'backbone'], function(PubSubKey, Backbone) {
  describe("PubSubKey allows entry into PubSub", function() {
    it("should be a class", function() {
      expect( PubSubKey.newInstance()).to.be.instanceof(PubSubKey);
      expect( new PubSubKey()).to.be.instanceof(PubSubKey);
    });
    it("should contain a special key", function() {
      expect( PubSubKey.newInstance().getId()).to.not.be.undefined;
      expect( PubSubKey.newInstance().getCreator()).to.be.equal(null);

      expect( PubSubKey.newInstance({id: 'foo'}).getId()).to.be.equal('foo');
      var c = {};
      expect( PubSubKey.newInstance({creator: c}).getCreator()).to.be.equal(c);
      var k = PubSubKey.newInstance({creator: c, id: 'foo'});

      expect(k.private).to.be.undefined;
      expect(k.key).to.be.undefined;
      expect(k.creator).to.be.undefined;
    });
  })
});
