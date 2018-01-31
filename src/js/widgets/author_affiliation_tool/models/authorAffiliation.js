'use strict';
define([
  'underscore'
], function (_) {

  /**
   * Parse and format author affiliation data
   *
   * This factory will create a new entry for the table that has an author and all
   * their corresponding affiliations and active dates.
   *
   * @param {string} author
   * @param {array} affs
   * @returns {{id: string, selected: boolean, author: *, affiliations: Array, lastActiveDates: Array}}
   */
  const authorAffiliationFactory = function (author, affs) {

    let out = {
      id: _.uniqueId(),
      selected: true,
      author: author,
      affiliations: [],
      lastActiveDates: []
    };

    out.affiliations = affs.map((a, i) => ({
      id: _.uniqueId(),
      selected: i === 0,
      name: a.affiliations.name,
      years: a.affiliations.years
    }));

    out.lastActiveDates = affs.map((a, i) => ({
      id: _.uniqueId(),
      selected: i === 0,
      date: a.affiliations.lastActiveDate
    }));

    return out;
  };

  return {
    create: authorAffiliationFactory
  };
});
