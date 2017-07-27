define([
  'js/components/query_validator'
], function (QueryValidator) {

  var getMockQuery = function () {
    var args = arguments;
    return {
      get: function () {
        return Array.prototype.concat.apply([], args);
      }
    };
  };

  describe('Query Validator', function () {
    var qv;
    beforeEach(function () {
      qv = new QueryValidator();
    });
    afterEach(function () {
      qv = null;
    });

    it('correctly parses single input', function () {
      var q = getMockQuery('abs:""');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.false;
      expect(res.tests.length).to.equal(1);
      expect(res.tests[0].result).to.be.false;
      expect(res.tests[0].token).to.equal('abs:""')
    });

    it('correctly parses single input with caret', function () {
      var q = getMockQuery('abs:"^"');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.false;
      expect(res.tests.length).to.equal(1);
      expect(res.tests[0].result).to.be.false;
      expect(res.tests[0].token).to.equal('abs:"^"')
    });

    it('correctly parses single input with paren', function () {
      var q = getMockQuery('citation("")');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.false;
      expect(res.tests.length).to.equal(1);
      expect(res.tests[0].result).to.be.false;
      expect(res.tests[0].token).to.equal('citation("")')
    });

    it('correctly parses empty input', function () {
      var q = getMockQuery('');
      var res = qv.validate(q);
      expect(res.result).to.be.true;
    });

    it('correctly parses multiple empty input', function () {
      var q = getMockQuery('', '', '', '     ', '    ');
      var res = qv.validate(q);
      expect(res.result).to.be.true;
    });

    it('correctly parses multiple input', function () {
      var q = getMockQuery('abs:""', 'bibcode:""', 'full:""', 'author:""', 'bibgroup:""');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.false;
      expect(res.tests.length).to.equal(5);
      expect(res.tests[0].result).to.be.false;
      expect(res.tests[0].token).to.equal('abs:""');

      expect(res.tests[1].result).to.be.false;
      expect(res.tests[1].token).to.equal('bibcode:""');

      expect(res.tests[2].result).to.be.false;
      expect(res.tests[2].token).to.equal('full:""');

      expect(res.tests[3].result).to.be.false;
      expect(res.tests[3].token).to.equal('author:""');

      expect(res.tests[4].result).to.be.false;
      expect(res.tests[4].token).to.equal('bibgroup:""');
    });

    it('correctly parses mixture of bad and good input', function () {
      var q = getMockQuery('abs:"df"', 'bibcode:""', 'full:"test"', 'author:""', 'bibgroup:"test"');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.false;
      expect(res.tests.length).to.equal(2);

      expect(res.tests[0].result).to.be.false;
      expect(res.tests[0].token).to.equal('bibcode:""');

      expect(res.tests[1].result).to.be.false;
      expect(res.tests[1].token).to.equal('author:""');
    });

    it('correctly parses good input, and does not fail', function () {
      var q = getMockQuery('abs:"test"', 'bibcode:"test"', 'full:"test"', 'author:"test"', 'bibgroup:"test"');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.true;
      expect(res.tests).to.be.undefined;
    });

    it('correctly handles nested fields', function () {
      var q = getMockQuery('abs:"abs:"""', 'citation("abs:""")');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.true;
    });

    it('correctly handles garbled inputs', function () {
      var q = getMockQuery('sldkfjsdlf:::::::lsdfkjsldjf ::::LDSjfks; :LSKJ;:Ldskjf ((((SD:LFKJ((S(DF S(D F');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.true;
    });

    it('correctly handles mixture of garbled and bad inputs', function () {
      var q = getMockQuery(':()dslkj: ;sd lf:ee :ekjdL: ', 'abs:""', 'test:"test"');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.false;
      expect(res.tests.length).to.equal(1);

      expect(res.tests[0].result).to.be.false;
      expect(res.tests[0].token).to.equal('abs:""');
    });

    it('correctly handles mixture of garbled and good inputs', function () {
      var q = getMockQuery(':()ds:lkj: ;sd lf:ee :ekjdL: ', 'abs:"test"', 'test:"test"');
      var res = qv.validate(q);

      expect(res).to.be.instanceOf(Object);
      expect(res.result).to.be.true;
    });
  });
});
