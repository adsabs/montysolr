define([
'underscore'
], function (_) {

  /**
   * Validator object
   * provides value checking
   * @param match
   * @param opts
   * @constructor
   */
  function Validator (match, opts) {
    var matcher = new RegExp(match, opts);

    return function test (str) {
      return (matcher.test(str)) ? null : str;
    };
  }

  /**
   * QueryToken object for holding the field and value
   * @param field
   * @param value
   * @param token
   * @constructor
   */
  function QueryToken (field, value, token) {
    this.field = field;
    this.value = value;
    this.token = token;
  }

  /**
   * Parse and validate queries
   * @constructor
   */
  function QueryValidator () {

    /**
     * Parse the query into new validator objects
     * @param q
     * @returns {Array}
     */
    var parseTokens = function (q) {
      var splitter = new RegExp(/[:(]/);
      var tokens = q.split(/\s+/);
      var parsedTokens = [];
      for (var j = 0; j < tokens.length; j++) {
        var subTokens = tokens[j].split(splitter);

        if (subTokens.length !== 2) {

          // Unable to split or nested fields, either way continue on
          continue;
        }

        // clean value of closing paren, if necessary
        subTokens[1] = subTokens[1].replace(')', '');

        parsedTokens.push(new QueryToken(subTokens[0], subTokens[1], tokens[j]));
      }
      return parsedTokens;
    };

    /**
     * Composed function finisher, returns boolean equivalent of result
     * @param res
     * @returns {boolean}
     */
    var completeValidation = function (res) {
      return !!res;
    };

    /**
     * Create composed function from validators
     * return the boolean result
     * @param validators
     * @param tokens
     * @returns {Array}
     */
    var test = function (validators, tokens) {
      var tests = [];
      for (var i in tokens) {
        var res = _.compose.apply(_, validators)(tokens[i].value);
        if (!res) {
          tests.push({
            token: tokens[i].token,
            result: res
          });
        }
      }
      return tests;
    };

    /**
     * Validate the query string
     * @param apiQuery
     * @returns {object}
     */
    this.validate = function (apiQuery) {
      var output = {
        result: true
      };

      // Validators - This should match things you DON'T want in the query
      // any confirmed match will make query invalid
      var validators = [
        completeValidation,
        new Validator(/^("")?$/),
        new Validator(/^"?\^"?$/)
      ];

      // Attempt to parse the query string to tokens
      try {
        var q = apiQuery.get('q').join(' ').trim();
        if (!q.length) {

          // query is empty string, continue on
          return output;
        }
        var parsedTokens = parseTokens(q);
      } catch (e) {

        // parsing error, allow it to continue on
        return output;
      }

      var tests = test(validators, parsedTokens);
      output.result = _.every(_.pluck(tests, 'result'));
      if (tests.length) {
        output.tests = tests;
      }

      return output
    };
  }

  return QueryValidator;
});
