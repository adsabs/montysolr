'use strict';
define([
  'underscore'
], function (_) {

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
