define([
  './core-suite',
  './ui-suite',
  './qb-suite'
], function(
  core,
  ui,
  qb
  ) {


  return core.concat(ui).concat(qb);
});