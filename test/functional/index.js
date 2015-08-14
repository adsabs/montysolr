define(function (require) {
  var registerSuite = require('intern!object');
  var assert = require('intern/chai!assert');
  var expect = require('intern/chai!expect');

  registerSuite({
    name: 'landing page',

    'verify layout': function () {
      return this.remote
        .get('http://localhost:8000/')
        .setFindTimeout(3000)

        .findByCssSelector('div[data-widget="NavbarWidget"]')
          .getVisibleText()
          .then(function (text) {
            expect(text).contains('Feedback');
            expect(text).contains('Help');
            expect(text).contains('Sign Up');
            expect(text).contains('Log In');
          })
          .end()
        .findByCssSelector('div[data-widget="SearchWidget"]')
          .isDisplayed()
          .then(function (res) {
            expect(res).to.eql(true);
          })
          .end()
        .findByCssSelector('div[data-widget="FooterWidget"]')
          .isDisplayed()
          .then(function (res) {
            expect(res).to.eql(true);
          })
          .end()
        ;
    },

    'user interacts with the search form (pressing enter and clicking submit)': function () {
      return this.remote
        .get('http://localhost:8000/')
        .setFindTimeout(2000)
        .findByCssSelector('div[data-widget="SearchWidget"]')
          .end()
        .pressKeys(['f', 'o', 'o', "\n"])
          .end()
        .findByCssSelector('ul.results-list')
          .end()
        .findByCssSelector('div[data-widget="SearchWidget"] input.q')
          .getProperty('value')
          .then(function(t) {
            expect(t).to.eql('foo')
          })
          .end()
        .findByCssSelector('span.num-found-container')
          .getVisibleText()
          .then(function(v) {
            expect(parseInt(v.replace(',', ''))).to.be.above(11000);
          })
          .end()
        .goBack()
        .pressKeys([' ', 't', 'i', 't', 'l', 'e', ':', 'f', 'o', 'o']) //' title:foo'
        .findByCssSelector('div[data-widget="SearchWidget"] button[type=submit]')
          .click()
          .end()
        .findByCssSelector('ul.results-list')
          .end()
        .findByCssSelector('span.num-found-container')
          .getVisibleText()
          .then(function(v) {
            expect(parseInt(v.replace(',', ''))).to.be.above(5);
          })
          .end()
        .end()
        ;
    }

  });
});
