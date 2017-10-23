'use strict';
define([
  'underscore',
  'jsonpath'
], function (_, jp) {

  var PATHS = {
    createdDate:            '$["created-date"].value',
    lastModifiedDate:       '$["last-modified-date"].value',
    sourceOrcidIdUri:       '$.source["source-orcid"].uri',
    sourceOrcidIdPath:      '$.source["source-orcid"].path',
    sourceOrcidIdHost:      '$.source["source-orcid"].host',
    sourceClientIdUri:      '$.source["source-client-id"].uri',
    sourceClientIdPath:     '$.source["source-client-id"].path',
    sourceClientIdHost:     '$.source["source-client-id"].host',
    sourceName:             '$.source["source-name"].value',
    putCode:                '$["put-code"]',
    path:                   '$.path',
    title:                  '$["title"].title.value',
    subtitle:               '$["title"].subtitle.value',
    translatedTitle:        '$["title"]["translated-title"].value',
    translatedTitleLang:    '$["title"]["translated-title"]["language-code"]',
    journalTitle:           '$["journal-title"].value',
    shortDescription:       '$["short-description"]',
    citationType:           '$.citation["citation-type"]',
    citationValue:          '$.citation["citation-value"]',
    type:                   '$.type',
    publicationDateYear:    '$["publication-date"].year.value',
    publicationDateMonth:   '$["publication-date"].month.value',
    publicationDateDay:     '$["publication-date"].day.value',
    publicationDateMedia:   '$["publication-date"]["media-type"]',
    url:                    '$.url.value',
    contributorOrcidUri:    '$["contributors"].contributor..["contributor-orcid"].uri',
    contributorOrcidPath:   '$["contributors"].contributor..["contributor-orcid"].path',
    contributorOrcidHost:   '$["contributors"].contributor..["contributor-orcid"].host',
    contributorName:        '$["contributors"].contributor..["credit-name"].value',
    contributorEmail:       '$["contributors"].contributor..["contributor-email"].value',
    contributorAttributes:  '$["contributors"].contributor..["contributor-attributes"]',
    contributorSequence:    '$["contributors"].contributor..["contributor-attributes"]["contributor-sequence"]',
    contributorRole:        '$["contributors"].contributor..["contributor-attributes"]["contributor-role"]',
    externalIdValue:        '$["external-ids"]["external-id"]..["external-id-value"]',
    externalIdType:         '$["external-ids"]["external-id"]..["external-id-type"]',
    externalIdUrl:          '$["external-ids"]["external-id"]..["external-id-url"]',
    externalIdRelationship: '$["external-ids"]["external-id"]..["external-id-relationship"]',
    country:                '$.country.value',
    visibility:             '$.visibility.value'
  };

  /**
   * Convenience class that allows easy conversion between ADS and ORCiD works.
   * @module Work
   * @param work
   * @constructor
   */
  var Work = function Work(work) {
    work = work || {};
    this._root = (work['work-summary']) ? work['work-summary'][0] : work;

    /**
     * Get the value at path
     *
     * @param {string} path - path on _root element to find
     * @returns {*} - value found at path
     */
    this.get = function (path) {
      var val = jp.query(this._root, path);
      if (_.isEmpty(val)) {
        return null;
      } else if (_.isArray(val) && val.length <= 1) {
        return val[0];
      }
      return val;
    };

    /**
     * Returns the generated ORCiD work from the current _root object.
     * The object will be based on the paths in PATHS
     *
     * @returns {*} - ORCiD formatted object
     */
    this.getAsOrcid = function () {
      return _.reduce(PATHS, _.bind(function (res, p) {
        var val = this.get(p);
        if (val) {
          if (_.isArray(val)) {
            _.forEach(val, function (v, i) {
              jp.value(res, p.replace('..', '[' + i + ']'), v);
            });
          } else {
            jp.value(res, p, val);
          }
        }
        return res;
      }, this), {});
    };

    /**
     * Creates an ADS formatted object
     *
     * @returns {*} - ADS formatted object
     */
    this.toADSFormat = function () {
      var ids = this.getExternalIds();
      if (ids.doi) {
        ids.doi = [ids.doi];
      }
      return _.extend({}, ids, {
        author: this.getContributorName(),
        title: [this.getTitle()],
        formattedDate: this.getFormattedPubDate(),
        abstract: this.getShortDescription(),
        source_name: this.getSourceName(),
        pub: this.getJournalTitle(),
        _work: this
      });
    };

    /**
     * Format publication date
     *
     * @returns {string} - formatted pub date
     */
    this.getFormattedPubDate = function () {
      var year = this.getPublicationDateYear() || '????';
      var month = this.getPublicationDateMonth() || '??';
      return year + '/' + month;
    };

    /**
     * Creates an object containing all external ids
     * @example
     * { bibcode: ["2018CNSNS..56..270Q"], doi: [...] }
     *
     * @returns {Object} - object containing external ids
     */
    this.getExternalIds = function () {
      var types = this.getExternalIdType();
      var values = this.getExternalIdValue();
      types = _.isArray(types) ? types : [types];
      values = _.isArray(values) ? values : [values];
      if (types.length !== values.length) {
        return {};
      }

      return _.reduce(types, function (res, t, i) {
        res[t] = values[i];
        return res;
      }, {});
    };

    /**
     * Convenience method for distinguishing a particular identifier by priority.
     * Given a set of external ids, this will return the value of the identifier.
     *
     * @example
     * pickIdentifier(['bibcode', 'doi']);
     * // returns: "2018CNSNS..56..270Q"
     *
     * @example
     * pickIdentifier(['doi', 'bibcode']);
     * // returns: "10.1016/j.cnsns.2017.08.014"
     *
     * @param {String[]} props - priority of the chosen ids
     * @returns {String} - value of the chosen identifier
     */
    this.pickIdentifier = function (props) {
      var ids = this.getExternalIds();
      var out = {};
      _.eachRight(props, function (p) {
        if (ids[p]) {
          out = p;
        }
      });
      return out;
    };

    // create getters for each of the PATHS
    _.reduce(PATHS, function (obj, p, k) {
      if (_.isString(k) && k.slice) {
        var prop = k[0].toUpperCase() + k.slice(1);
        obj['get' + prop] = _.partial(obj.get, p);
      }
      return obj;
    }, this);
  };

  /**
   * Creates an ORCiD Work from an ADS record.
   *
   * @static
   * @param {Object} adsWork - the ads record
   * @param {Number} [putCode] putCode - putcode to apply to work
   * @returns {Object} - the ORCiD work
   */
  Work.adsToOrcid = function (adsWork, putCode) {
    var ads = {
      pubdate: '$.pubdate',
      abstract: '$.abstract',
      bibcode: '$.bibcode',
      pub: '$.pub',
      doi: '$.doi[0]',
      author: '$.author[*]',
      title: '$.title[0]',
      type: '$.doctype'
    };

    var put = function (obj, p, val) {
      if (val) {
        if (_.isArray(val)) {
          _.forEach(val, function (v, i) {
            jp.value(obj, p.replace('..', '[' + i + ']'), v);
          });
        } else {
          jp.value(obj, p, val);
        }
      }
      return obj;
    };
    var get = function (path) {
      var val = jp.query(adsWork, path);
      if (_.isEmpty(val)) {
        return null;
      } else if (_.isArray(val) && val.length <= 1) {
        return val[0];
      }
      return val;
    };
    var work = {};
    var worktype = function(adsType) {
      var oType = {
        article: 'JOURNAL-ARTICLE',
        inproceedings: 'CONFERENCE-PAPER',
        abstract: 'CONFERENCE-ABSTRACT',
        eprint: 'WORKING-PAPER',
        phdthesis: 'DISSERTATION',
        techreport: 'RESEARCH-TECHNIQUE',
        inbook: 'BOOK-CHAPTER',
        circular: 'RESEARCH-TOOL',
        misc: 'OTHER',
        book: 'BOOK',
        proceedings: 'BOOK',
        bookreview: 'BOOK-REVIEW',
        erratum: 'JOURNAL-ARTICLE',
        proposal: 'OTHER',
        newsletter: 'NEWSLETTER-ARTICLE',
        catalog: 'DATA-SET',
        intechreport: 'RESEARCH-TECHNIQUE',
        mastersthesis: 'DISSERTATION',
        obituary: 'OTHER',
        pressrelease: 'OTHER',
        software: 'RESEARCH-TECHNIQUE',
        talk: 'LECTURE-SPEECH'
      };
      return oType[adsType] || 'JOURNAL-ARTICLE';
    };
    try {

      var exIds = {
        types: [],
        values: [],
        relationships: []
      };

      // handle doi or bibcode not existing
      var bib = get(ads.bibcode);
      var doi = get(ads.doi);
      if (bib) {
        exIds.types.push('bibcode');
        exIds.values.push(bib);
        exIds.relationships.push('SELF');
      }
      if (doi) {
        exIds.types.push('doi');
        exIds.values.push(doi);
        exIds.relationships.push('SELF');
      }

      put(work, PATHS.publicationDateYear, get(ads.pubdate).split('-')[0]);
      put(work, PATHS.publicationDateMonth, get(ads.pubdate).split('-')[1]);
      put(work, PATHS.shortDescription, get(ads.abstract));
      put(work, PATHS.externalIdType, exIds.types);
      put(work, PATHS.externalIdValue, exIds.values);
      put(work, PATHS.externalIdRelationship, exIds.relationships);
      put(work, PATHS.journalTitle, get(ads.pub));
      put(work, PATHS.type, worktype(get(ads.type)));
      var author = get(ads.author);
      author = (_.isArray(author)) ? author : [author];
      put(work, PATHS.contributorName, author);
      var roles = _.map(author, function () {
        return 'AUTHOR';
      });
      put(work, PATHS.contributorRole, roles);
      put(work, PATHS.title, get(ads.title));
      if (putCode) {
        put(work, PATHS.putCode, putCode);
      }
    } catch (e) {
      return null;
    }

    return work;
  };

  return Work;
});
