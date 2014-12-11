/**
 * Created by rchyla on 5/24/14.
 *
 * Set of utilities for manipulating ApiQuery object. These are mainly
 * useful for widgets that often do the same operations with the
 * query.
 *
 * The object must be initialized with an 'identifier' -- this identifier
 * will represent a context; so we'll be touching all elements that
 * belong to the context
 *
 * TODO: need to distinguish globalOperator from 'operator' inside clauses
 * TODO: the 'globalOperator' joins clauses; 'operator' joins elements of the
 * TODO: clauses
 *
 */

define(['underscore', 'js/components/api_query'], function (_, ApiQuery) {

  var ApiQueryUpdater = function(contextIdentifier, options) {
    if (!contextIdentifier || !_.isString(contextIdentifier)) {
      throw new Error("You must initialize the ApiQueryUpdater with a context (which is a string)")
    }
    this.context = contextIdentifier;
    this.defaultOperator = ' ';
    this.operators = [' ', 'AND', 'OR', 'NOT', 'NEAR'];
    this.defaultMode = 'limit';
    this.operationModes = ['limit', 'exclude', 'expand'];
    this.impossibleString = "\uFFFC\uFFFC\uFFFC";
    _.extend(this, options);
  };



  _.extend(ApiQueryUpdater.prototype, {

    /**
     * Modifies the query - it will search for a string inside the query (using previously
     * saved state) and update the query 'parameter'
     *
     * @param field
     *      this is the name of the parameter we are changing inside apiQeury
     * @param apiQuery
     *      the apiQuery object we are updating
     * @param queryCondition
     *      String|[String] - new conditions to set
     * @param operator
     *      String: this will serve as concatenator for the conditions
     */
    updateQuery: function(apiQuery, field, mode, queryCondition) {

      if (!field || !_.isString(field)) {
        throw new Error("You must tell us what parameter to update in the ApiQuery");
      }

      queryCondition = this._sanitizeConditionAsArray(queryCondition);
      mode = this._sanitizeMode(mode);

      var operator;
      if (mode == 'limit') {
        operator = 'AND';
      }
      else if (mode == 'exclude') {
        operator = 'NOT';
      }
      else if (mode == 'expand') {
        operator = 'OR';
      }
      else {
        throw new Error("Unsupported mode/operator:", mode);
      }

      if (!(apiQuery.has(field))) {
        var conditions = [operator].concat(queryCondition);
        apiQuery.set(field, this._buildQueryFromConditions(conditions));
        apiQuery.set(this._n(field), conditions);
        return;
      }


      //globalOperator = this._sanitizeOperator(globalOperator);

      // local name
      var n = this._n(field);

      // create copy of the field
      var q = _.clone(apiQuery.get(field));

      var oldConditionAsString, newConditionAsString, newConditions, existingConditions;

      // first check if we have any existing conditions
      existingConditions = this._getExistingVals(apiQuery, n);

      if (existingConditions) {

        // if the operators differ, it means we cannot safely update the query
        // we must treat it as a new query
        if (existingConditions[0] !== operator) {
          this._closeExistingVals(apiQuery, n);
          return this.updateQuery(apiQuery, field, mode, queryCondition);
        }

        oldConditionAsString = this._buildQueryFromConditions(existingConditions);
      }
      else {
        existingConditions = [operator]; // first value is always operator
        if (q.length == 1 && q[0].indexOf(' ') > -1) { //simple string
          oldConditionAsString = q[0];
          existingConditions.push(q[0]);
        }
        else {
          oldConditionAsString = this.impossibleString;
        }

      }

      // 'limit' means that the broader query will become 'narrower'
      // by gaining more AND'ed 'conditions'
      // 'expand' means that the query is becoming broader by gaining
      // more conditions (these are OR'ed)
      if (mode == 'limit' || mode == 'expand') {

        // join the old and the new conditoins (remove the duplicates)
        // we are basically trying to update the existing query
        // by adding more conditions into the same clause

        newConditions = _.union(existingConditions, queryCondition);
        newConditionAsString = this._buildQueryFromConditions(newConditions);

        var testq = _.clone(q);
        // try to find the pre-condition and replace it with a new value
        if (this._modifyArrayReplaceString(testq, oldConditionAsString, newConditionAsString)) {
          apiQuery.set(field, testq); //success
          // save the values inside the query (so that we can use them if we are called next time)
          apiQuery.set(n, newConditions);
          return;
        }

      }


      // 'exclude' means that we are ADDING more conditions to the query; the query
      // was broader; not it will explicitly 'exclude' some documents; again - there
      // can also be 'exclude' conditions; so if possible, we'll enlarge their number

      if (mode == 'exclude') {


        var modifiedExisting = _.clone(existingConditions);
        modifiedExisting[0] = 'OR';
        oldConditionAsString = ' NOT ' + this._buildQueryFromConditions(modifiedExisting);

        newConditions = _.union(existingConditions, queryCondition);
        var modifiedConditions = _.clone(newConditions);
        modifiedConditions[0] = 'OR';
        newConditionAsString = ' NOT ' + this._buildQueryFromConditions(modifiedConditions);

        var testq = _.clone(q);
        // try to find the pre-condition and replace it with a new value
        if (this._modifyArrayReplaceString(testq, oldConditionAsString, newConditionAsString)) {
          apiQuery.set(field, testq); //success
          // save the values inside the query (so that we can use them if we are called next time)
          apiQuery.set(n, newConditions);
          return;
        }
      }


      // we didn't find an old query that could be updated, so this means that we have
      // to add a new logical condition to the existing query string.

      if (mode == 'limit') {
        newConditions = this._modifyArrayAddString(q, queryCondition, 'AND');
      }
      else if (mode == 'exclude') {
        newConditions = this._modifyArrayAddString(q, queryCondition, 'NOT');
      }
      else if (mode == 'expand') {
        newConditions = this._modifyArrayAddString(q, queryCondition, 'OR');
      }
      apiQuery.set(n, newConditions);
      apiQuery.set(field, q);

    },


    /**
     * Tells whether the string needs escaping (it ignores empty space)
     *
     * @param value
     */
    needsEscape: function(s) {
      var sb = [], c;
      for (var i = 0; i < s.length; i++) {
        c = s[i];
        // These characters are part of the query syntax and must be escaped
        if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')'
          || c == ':' || c == '^' || c == '[' || c == ']' || c == '"'
          || c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
          || c == '|' || c == '&' || c == '/' ) {
          return true;
        }
      }
      return false;
    },

    /**
     * Escapes special characters (but not whitespace)
     *
     * @param value
     */
    escape: function(s) {
      var sb = [], c;
      for (var i = 0; i < s.length; i++) {
        c = s[i];
        // These characters are part of the query syntax and must be escaped
        if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')'
          || c == ':' || c == '^' || c == '[' || c == ']' || c == '"'
          || c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
          || c == '|' || c == '&' || c == '/') {
          sb.push('\\');
        }
        sb.push(c);
      }
      return sb.join('');
    },


    escapeInclWhitespace: function(s) {
      var sb = [], c;
      for (var i = 0; i < s.length; i++) {
        c = s[i];
        // These characters are part of the query syntax and must be escaped
        if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')'
          || c == ':' || c == '^' || c == '[' || c == ']' || c == '"'
          || c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
          || c == '|' || c == '&' || c == '/' || c == ' ' || c == '\t') {
          sb.push('\\');
        }
        sb.push(c);
      }
      return sb.join('');
    },

    /**
     * Wraps string between quotes - and escapes any quotes if present
     * @param s
     * @returns {string}
     */
    quoteIfNecessary: function(s, quoteChar, quoteCharEnd) {
      return this.quote(s, quoteChar, quoteCharEnd, true);
    },

    /**
     * Wraps string between quotes - and escapes any quotes if present
     * @param s
     * @returns {string}
     */
    quote: function(s, quoteChar, quoteCharEnd, onlyIfNecessary) {
      if (!quoteChar)
        quoteChar = '"';
      if (!quoteCharEnd)
        quoteCharEnd = quoteChar;

      if (_.isUndefined(onlyIfNecessary))
        onlyIfNecessary = false;

      var sb = [], c;
      var needsQuotes = false;
      for (var i = 0; i < s.length; i++) {
        c = s[i];
        if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')'
          || c == ':' || c == '^' || c == '[' || c == ']' || c == '"'
          || c == '{' || c == '}' || c == '~' //|| c == '*' || c == '?'
          || c == '|' || c == '&' || c == '/' || c == ' ' || c == '\t') {
          needsQuotes = true;
        }
        if ((c == quoteChar || c == quoteCharEnd) && i>0 && s[i-1] !== '\\') {
          sb.push('\\');
        }
        sb.push(c);
      }
      if (needsQuotes || onlyIfNecessary == false) {
        return quoteChar + sb.join('') + quoteCharEnd;
      }
      return sb.join('');
    },


    /**
     * Attaches to the ApiQuery object a storage of tmp values; these are
     * not affecting anything inside the query; but the query is carrying them
     * around as long as it was not cloned() etc
     *
     * @param key
     * @param value
     */
    saveTmpEntry: function(apiQuery, key, value) {
      var storage = this._getTmpStorage(apiQuery, true);
      var oldVal;
      if (key in storage) {
        oldVal = storage[key];
      }
      storage[key] = value;
      return oldVal;
    },

    removeTmpEntry: function(apiQuery, key) {
      var storage = this._getTmpStorage(apiQuery, true);
      var val = storage[key];
      delete storage[key];
      return val;
    },

    getTmpEntry: function(apiQuery, key, defaultValue) {
      var storage;
      if (defaultValue) {
        storage = this._getTmpStorage(apiQuery, true);
      }
      else {
        storage = this._getTmpStorage(apiQuery, false);
      }

      if (key in storage) {
        return storage[key];
      }
      else {
        storage[key] = defaultValue;
        return defaultValue;
      }
    },

    hasTmpEntry: function(apiQuery, key) {
      var storage = this._getTmpStorage(apiQuery);
      return key in storage;
    },

    _getTmpStorage: function(apiQuery, createIfNotExists) {
      var n = this._n('__tmpStorage');
      if (!apiQuery.hasOwnProperty(n)) {
        if (!createIfNotExists)
          return {};
        apiQuery[n] = {};
      }
      return apiQuery[n];
    },

    _n: function(name) {
      return '__' + this.context + '_' + name;
    },



    _buildQueryFromConditions: function(conditions) {
      if (conditions.length <= 1) {
        throw new Error("Violation of contract: first condition is always an operator");
      }
      var op = conditions[0];
      if (op != ' ') {
        op = ' ' + op + ' ';
      }
      return '(' + conditions.slice(1).join(op) + ')';
    },

    /**
     * Searches for values inside the array and replaces sections
     * Returns number of modifications made
     *
     * @param arr
     * @param search
     * @param replace
     * @param maxNumMod
     *    maximum number of modifications to make; you can choose to
     *    replace only the first value found
     * @returns {integer}
     * @private
     */
    _modifyArrayReplaceString: function(arr, search, replace, maxNumMod) {
      var numMod = 0;
      if (!maxNumMod)
        maxNumMod = -1;

      if (!search) {
        throw new Error("Your search is empty, you fool");
      }
      var modified = false;
      _.each(arr, function(text, i) {
        if (maxNumMod > 0 && numMod > maxNumMod) {
          return;
        }
        if (text.indexOf(search) > -1) {
          arr[i] = text.replace(search, replace);
          numMod += 1;
        }
      });
      return numMod;
    },

    /**
     * Adds the new value into the array
     * @param arr
     * @param conditions
     * @private
     */
    _modifyArrayAddString: function(arr, conditions, operator) {
      // will always add to the latest string
      if ((arr.length == 0 || arr[arr.length-1].trim() == '' )&& (operator == 'NOT' || operator == 'NEAR')) {
        throw new Error('Invalid operation; cannot apply NOT/NEAR on single clause');
      }
      var newQ = arr[arr.length-1];

      var newConditions = [operator, newQ].concat(conditions);
      arr[arr.length-1] = this._buildQueryFromConditions(newConditions);
      return newConditions;
    },

    /**
     * Gets the valus of the condition
     * @param apiQuery
     * @param key
     *    the conditon 'name', typically st like __condition_author_q; it differs based on the
     *    operation (type of update/widget/filter etc)
     * @param defaults
     *    what to return if 'key' is not present
     * @returns {*}
     * @private
     */
    _getExistingVals: function(apiQuery, key, defaults) {
      if (apiQuery.has(key)) {
        return apiQuery.get(key);
      }
      return defaults;
    },

    /**
     * When we have conditions for the previous context; but their operator is
     * different, it means that the new conditions represent a new clause. So
     * we need to do something with the old conditions (we could stack them
     * but for now, the simple/robust solution is to simply remove them ->
     * this means they will not be available for updates/manipulation -->
     * theh query will be extended)
     *
     * @param condName
     * @private
     */
    _closeExistingVals: function(apiQuery, condName) {
      apiQuery.unset(condName);
    },

    _sanitizeMode: function(mode) {
      if (!mode) {
        return this.defaultMode;
      }
      var i = _.indexOf(this.operationModes, mode);
      if (i == -1) {
        throw new Error("Unkwnown mode: " + mode);
      }
      return this.operationModes[i];
    },

    _sanitizeOperator: function(operator) {
      if (!operator) {
        return this.defaultOperator;
      }
      if (_.isString(operator)) {
        if (operator.trim() == '') {
          return this.defaultOperator;
        }
      }
      else {
        throw new Error('Operator must be a string');
      }
      operator = operator.toUpperCase();
      var i = _.indexOf(this.operators, operator);
      if (i == -1) {
        throw new Error("Unknown operator: ", operator);
      }
      return this.operators[i];
    },

    _sanitizeConditionAsArray: function(condition) {
      if (!condition) {
        throw new Error("The condition must be set (string/array of strings)");
      }
      if (_.isString(condition)) {
        return [condition];
      }
      condition = _.without(_.flatten(condition), "", false, null, undefined, NaN);
      if (condition.length == 0) {
        throw new Error("After removing empty values, no condition was left");
      }
      return condition;
    },

    /**
     * Cleans up the *clone* of the apiQuery by removing all the entries
     * that are inserted into ApiQuery by the query updater.
     *
     * @param apiQuery
     */
    clean: function(apiQuery) {
      var q = {};
      if (apiQuery && apiQuery.keys) {
        _.each(apiQuery.keys(), function(key) {
          if (!(key.substring(0, 2) == '__')) {
            q[key] = apiQuery.get(key);
          }
        });
      }
      return new ApiQuery(q);
    }

  });

  return ApiQueryUpdater;

});