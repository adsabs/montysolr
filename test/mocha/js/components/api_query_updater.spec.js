define(['underscore', 'js/components/api_query', 'js/components/api_query_updater'], function(_, ApiQuery, ApiQueryUpdater) {
  describe("ApiQuery Updater (Utility)", function () {
      

    it("should return bare API object", function() {
      expect(new ApiQueryUpdater('foo')).to.be.instanceof(ApiQueryUpdater);
      expect(function(){new ApiQueryUpdater()}).to.throw.Error;
    });
    
    it("can update existing query (any parameter) - both 'add' and 'replace'", function() {
      var q = new ApiQuery({'q': 'bar'});
      var u = new ApiQueryUpdater('q');

      u.updateQuery('q', q, 'baz', 'AND', 'add');
      expect(q.get('q')).to.be.eql(['bar', 'baz']);
      expect(u._getExistingVals(q, u._n('conditions_q'))).to.eql(['AND', 'baz']);

      u.updateQuery('q', q, 'baz', 'AND', 'remove');
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
      u.updateQuery('q', q, 'baz', 'AND', 'add');
      expect(q.get('__q_conditions_q')).to.eql(['AND', 'baz']);

      var q2 = u.clean(q);

      expect(q.get('__q_conditions_q')).to.eql(['AND', 'baz']);
      expect(q2.get('__q_conditions_q')).to.eql(undefined);
      expect(q2.url()).to.be.eql('q=bar+AND+baz');
    });

  });

});
