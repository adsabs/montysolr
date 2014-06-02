define(['underscore', 'js/components/api_query', 'js/components/api_query_updater'], function(_, ApiQuery, ApiQueryUpdater) {
  describe("ApiQuery Updater (Utility)", function () {
      

    it("should return bare API object", function() {
      expect(new ApiQueryUpdater('foo')).to.be.instanceof(ApiQueryUpdater);
      expect(function(){new ApiQueryUpdater()}).to.throw.Error;
    });

    it("supports limit/exclude operations", function() {
      var q = new ApiQuery({'q': 'bar'});
      var u = new ApiQueryUpdater('q');

      u.updateQuery(q, 'q', 'limit', 'baz');
      expect(q.get('q')).to.be.eql(['(bar AND baz)']);
      expect(u._getExistingVals(q, u._n('q'))).to.eql(['AND', 'bar', 'baz']);

      u.updateQuery(q, 'q', 'limit', 'baw');
      expect(q.get('q')).to.be.eql(['(bar AND baz AND baw)']);
      expect(u._getExistingVals(q, u._n('q'))).to.eql(['AND', 'bar', 'baz', 'baw']);

      u.updateQuery(q, 'q', 'exclude', 'bar');
      expect(q.get('q')).to.be.eql(['((bar AND baz AND baw) NOT bar)']);
      expect(u._getExistingVals(q, u._n('q'))).to.eql(['NOT', '(bar AND baz AND baw)', 'bar']);


      q = new ApiQuery({'q': 'bar'});
      u = new ApiQueryUpdater('q');

      u.updateQuery(q, 'q', 'expand', 'baz');
      expect(q.get('q')).to.be.eql(['(bar OR baz)']);
      expect(u._getExistingVals(q, u._n('q'))).to.eql(['OR', 'bar', 'baz']);


      // it can update empty field
      u.updateQuery(q, 'qx', 'expand', 'baz');
      expect(q.get('qx')).to.be.eql(['(baz)']);
      expect(u._getExistingVals(q, u._n('qx'))).to.eql(['OR', 'baz']);

      u.updateQuery(q, 'qx', 'expand', 'bar');
      expect(q.get('qx')).to.be.eql(['(baz OR bar)']);
      expect(u._getExistingVals(q, u._n('qx'))).to.eql(['OR', 'baz', 'bar']);
    });

    it.skip("can update existing query:add/replace (any field)", function() {
      var q;
      var u = new ApiQueryUpdater('q');

      // simple case; when starting with one string
      // bar + AND baz -> (bar AND baz)
      q = new ApiQuery({'q': 'bar'});
      u.updateQuery(q, 'q', 'add', 'baz', 'AND');
      expect(q.get('q')).to.be.eql(['(bar AND baz)']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['AND', 'bar', 'baz']);

      // bar + AND baz vaz -> (bar AND baz AND vaz)
      q = new ApiQuery({'q': 'bar'});
      u.updateQuery(q, 'q', 'add', ['baz', 'vaz'], 'AND');
      expect(q.get('q')).to.be.eql(['(bar AND baz AND vaz)']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['AND', 'bar', 'baz', 'vaz']);

      // (bar AND baz AND vaz) + OR foo -> (bar AND baz AND vaz) OR foo
      u.updateQuery(q, 'q', 'add', 'foo', 'OR');
      expect(q.get('q')).to.be.eql(['((bar AND baz AND vaz) OR foo)']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['OR', '(bar AND baz AND vaz)', 'foo']);

      // duplicate value
      u.updateQuery(q, 'q', 'add', 'foo', 'OR');
      expect(q.get('q')).to.be.eql(['((bar AND baz AND vaz) OR foo)']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['OR', '(bar AND baz AND vaz)', 'foo']);

      // duplicate + new value
      u.updateQuery(q, 'q', 'add', ['foo', 'boo'], 'OR');
      expect(q.get('q')).to.be.eql(['((bar AND baz AND vaz) OR foo OR boo)']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['OR', '(bar AND baz AND vaz)', 'foo', 'boo']);

      // remove value of the same operator
      u.updateQuery(q, 'q', 'remove', ['foo'], 'OR');
      expect(q.get('q')).to.be.eql(['((bar AND baz AND vaz) OR boo)']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['OR', '(bar AND baz AND vaz)', 'boo']);

      // remove value (different operator) - should trigger boolean logic action
      u.updateQuery(q, 'q', 'remove', 'woo', 'AND');
      expect(q.get('q')).to.be.eql(['(((bar AND baz AND vaz) OR boo)) NOT woo)']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['OR', '(bar AND baz AND vaz)', 'boo']);

      // apply two exclusions
      u.updateQuery(q, 'q', 'remove', ['woo', 'too'], 'NOT');

      u.updateQuery(q, 'q', 'baz', 'AND', 'remove');
      expect(q.get('q')).to.be.eql(['bar']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['AND']);
    });

    it("can escape", function() {

      var u = new ApiQueryUpdater('q');
      expect(u.escape('\\+-!():^[]"{}~*?|&/')).to.equal('\\\\\\+\\-\\!\\(\\)\\:\\^\\[\\]\\"\\{\\}\\~\\*\\?\\|\\&\\/');

      expect(u.escapeInclWhitespace('hey \\+-!():^[]"{}~*?|&/')).to.equal('hey\\ \\\\\\+\\-\\!\\(\\)\\:\\^\\[\\]\\"\\{\\}\\~\\*\\?\\|\\&\\/');
    });

    it("can save tmp values into the apiquery (these get forgotten)", function() {
      var q = new ApiQuery({'q': 'bar'});
      var u = new ApiQueryUpdater('q');

      expect(q.url()).to.be.equal('q=bar');

      u.saveTmpEntry(q, 'foo', [1,2,3]);
      expect(u.hasTmpEntry(q, 'foo')).to.be.true;
      expect(u.hasTmpEntry(q, 'foos')).to.be.false;
      expect(u.getTmpEntry(q, 'foo')).to.be.eql([1,2,3]);

      var q2 = q.clone();
      expect(q2.__tmpStorage).to.be.undefined;

    });

    it("can clean the apiquery", function() {
      var q = new ApiQuery({'q': 'bar'});
      var u = new ApiQueryUpdater('q');
      u.updateQuery(q, 'q', 'limit', 'baz');
      expect(q.get('__q_q')).to.eql(['AND', 'bar', 'baz']);

      var q2 = u.clean(q);

      expect(q.get('__q_q')).to.eql(['AND', 'bar', 'baz']);
      expect(q2.get('__q_q')).to.eql(undefined);
      expect(q2.url()).to.be.eql('q=(bar+AND+baz)');
    });

  });

});
