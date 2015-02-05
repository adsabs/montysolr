define([
    'js/components/generic_module',
    'js/mixins/dependon',
    'jquery',
    'js/modules/orcid/orcid_api',
    'underscore',
    'js/modules/orcid/json2xml',
    'xml2json'
  ],
  function (
    GenericModule,
    Mixins,
    $,
    OrcidApi,
    _,
    json2xml,
    xml2json
    ) {

    function objectEquals(x, y) {

      if (x === null || x === undefined || y === null || y === undefined) { return x === y; }
      // after this just checking type of one would be enough
      if (x.constructor !== y.constructor) { return false; }
      // if they are functions they should exactly refer to same one
      if (x instanceof Function) { return x === y; }
      if (x === y || x.valueOf() === y.valueOf()) { return true; }
      if (Array.isArray(x) && x.length !== y.length) { return false; }

      // if they are dates, they must had equal valueOf
      if (x instanceof Date) { return false; }

      // if they are strictly equal, they both need to be object at least
      if (!(x instanceof Object)) { return false; }
      if (!(y instanceof Object)) { return false; }

      // recursive object equality check
      var p = Object.keys(x);
      return Object.keys(y).every(function (i) { return p.indexOf(i) !== -1; }) ?
        p.every(function (i) { return objectEquals(x[i], y[i]); }) :
        false;
    }

    describe("JSON to XML", function () {

      it('simple json2xml', function (done) {
        var originalJson = {
          root: {
            $: {
              a: "a"
            },
            b: "b"
          }
        };

        var xml = json2xml.transform(originalJson, { attributes_key: '$' });
        expect(xml).to.equal("<root a=\"a\"><b>b</b></root>");

        var json = $.xml2json(xml);

        expect(objectEquals(originalJson, json)).to.be.true;

        done();
      });

      it('complex json2xml', function (done) {
        var originalJson = {
          "orcid-message": {
            "$": {
              "xmlns": "http://www.orcid.org/ns/orcid"
            },
            "message-version": "1.1",
            "orcid-profile": {
              "$": {
                "type": "user"
              },
              "orcid-identifier": {
                "$": {},
                "uri": "http://sandbox.orcid.org/0000-0001-7016-4666",
                "path": "0000-0001-7016-4666",
                "host": "sandbox.orcid.org"
              },
              "orcid-preferences": {
                "$": {},
                "locale": "en"
              },
              "orcid-history": {
                "$": {},
                "creation-method": "website",
                "submission-date": "2014-12-05T09:58:18.525Z",
                "last-modified-date": "2014-12-11T14:27:18.151Z",
                "claimed": "true"
              },
              "orcid-bio": {
                "$": {},
                "personal-details": {
                  "$": {},
                  "given-names": "Martin",
                  "family-name": "Obrátil"
                }
              },
              "orcid-activities": {
                "$": {},
                "orcid-works": {
                  "$": {},
                  "orcid-work": [
                    {
                      "$": {
                        "put-code": "454227",
                        "visibility": "public"
                      },
                      "work-title": {
                        "$": {},
                        "title": "Testing publiction"
                      },
                      "work-type": "test",
                      "work-source": {
                        "$": {},
                        "uri": "http://sandbox.orcid.org/0000-0001-7016-4666",
                        "path": "0000-0001-7016-4666",
                        "host": "sandbox.orcid.org"
                      }
                    },
                    {
                      "$": {
                        "put-code": "99999",
                        "visibility": "public"
                      },
                      "work-title": {
                        "$": {},
                        "title": "Testing publiction 2"
                      },
                      "work-type": "test",
                      "work-source": {
                        "$": {},
                        "uri": "http://sandbox.orcid.org/0000-0001-7016-4666",
                        "path": "0000-0001-7016-4666",
                        "host": "sandbox.orcid.org"
                      }
                    }
                  ]
                }
              }
            }
          }
        };

        var xml = json2xml.transform(originalJson , { attributes_key: '$' });
        var expected = '<orcid-message xmlns="http://www.orcid.org/ns/orcid"><message-version>1.1</message-version>' +
          '<orcid-profile type="user"><orcid-identifier><uri>http://sandbox.orcid.org/0000-0001-7016-4666</uri>' +
          '<path>0000-0001-7016-4666</path><host>sandbox.orcid.org</host></orcid-identifier>' +
          '<orcid-preferences><locale>en</locale></orcid-preferences><orcid-history><creation-method>website</creation-method>' +
          '<submission-date>2014-12-05T09:58:18.525Z</submission-date><last-modified-date>2014-12-11T14:27:18.151Z</last-modified-date>' +
          '<claimed>true</claimed></orcid-history><orcid-bio><personal-details><given-names>Martin</given-names>' +
          '<family-name>Obrátil</family-name></personal-details></orcid-bio>' +
          '<orcid-activities><orcid-works><orcid-work put-code="454227" visibility="public"><work-title><title>Testing publiction</title></work-title>' +
          '<work-type>test</work-type><work-source><uri>http://sandbox.orcid.org/0000-0001-7016-4666</uri>' +
          '<path>0000-0001-7016-4666</path><host>sandbox.orcid.org</host></work-source></orcid-work>' +
          '<orcid-work put-code="99999" visibility="public"><work-title><title>Testing publiction 2</title></work-title>' +
          '<work-type>test</work-type><work-source><uri>http://sandbox.orcid.org/0000-0001-7016-4666</uri><path>0000-0001-7016-4666</path>' +
          '<host>sandbox.orcid.org</host></work-source></orcid-work></orcid-works></orcid-activities></orcid-profile></orcid-message>';

        expect(xml).to.equal(expected);

        var json = $.xml2json(xml);

        expect(objectEquals(originalJson, json)).to.be.true;

        done();
      });

      it('more complex xml2json', function (done) {
        var originalXml = '<orcid-work put-code="456255" visibility="public"><work-title><title>Simultaneous laser oscillation at R&lt;SUB&gt;1&lt;/SUB&gt;and R&lt;SUB&gt;2&lt;/SUB&gt;wavelengths in ruby</title></work-title><work-type>book</work-type><publication-date><year>1965</year></publication-date><work-external-identifiers><work-external-identifier><work-external-identifier-type>bibcode</work-external-identifier-type><work-external-identifier-id>1965IJQE....1..132C</work-external-identifier-id></work-external-identifier><work-external-identifier><work-external-identifier-type>other-id</work-external-identifier-type><work-external-identifier-id>ads:2468360</work-external-identifier-id></work-external-identifier></work-external-identifiers><work-contributors><contributor><credit-name visibility="public">Calviello, J.</credit-name><contributor-attributes><contributor-role>author</contributor-role></contributor-attributes></contributor><contributor><credit-name visibility="public">Fisher, E.</credit-name><contributor-attributes><contributor-role>author</contributor-role></contributor-attributes></contributor><contributor><credit-name visibility="public">Heller, Z.</credit-name><contributor-attributes><contributor-role>author</contributor-role></contributor-attributes></contributor></work-contributors></orcid-work>';
        var json = $.xml2json(originalXml);
        var xml = json2xml.transform(json , { attributes_key: '$' });

        expect(originalXml === xml).to.be.true;
        done();
      });
    });
  }
);