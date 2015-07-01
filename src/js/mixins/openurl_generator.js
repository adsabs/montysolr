/**
 *
 *
 **/

define([], function() {

    var OpenURLGenerator = function (metadata, link_server){
        /**
         * Generates a class that can be used to create an OpenURL and a ContextObject for OpenURLs
         * OpenURL semantics have been followed using the standard Z39.88-2004, v0.1 and v1.0 should
         * both be supported, in principle.
         *
         * @param {object} metadata - should contain the docs from a Solr response
         * @param {string} link_server - library link server to be used as the base of the OpenURL
         */

        // Fixed values
        this.baseURL = link_server;
        this.metadata = (metadata !== undefined) ? this.metadata = metadata : {};

        this.url_ver = 'Z39.88-2004';
        this.rft_val_fmt = 'info:ofi/fmt:kev:mtx:';
        this.rfr_id = 'info:sid/ADS';
        this.sid = 'ADS';

        this.parseAuthor = function(author) {
            /** Parses the author returned from Solr docs that is the full author name, not their second name.
             * The first and last names are comma separated.
             *
             * @param {string} author - author string
             */

            if (author === undefined) {
                return {
                    'firstnames': false,
                    'lastname': false
                }
            } else {
                return {
                    'firstnames': author.split(', ')[1],
                    'lastname': author.split(', ')[0]
                }
            }
        };

        this.parseFirstPage = function(page) {
            /** Parses the pages such that it returns the first page. There are two scenarios, one where there
             * is a page range, and one where there is only a single page.
             *
             * @param {array} page - array containing a page or page range
             */
            return (page !== undefined) ? page[0].split('-')[0] : false;
        };

        this.parseRFTInfo = function(metadata) {
            /**
             * Extracts the possible types of IDs from the meta data. Currently there are two types of IDs. One
             * is the DOI, and the second is a bibcode.
             *
             * @param {dictionary} metadata - should contain 'doi' and 'bibcode' keys
             */

            var doi = (metadata.doi !== undefined) ? 'info:doi/' + metadata.doi[0] : false;
            var bibcode = (metadata.bibcode !== undefined) ? 'info:bibcode/' + metadata.bibcode : false;

            return [doi, bibcode];
        };

        this.parseID = function(doi) {
            /**
             * Parses the incoming DOI array and returns the ContextObject specific format
             *
             * @param {array} doi - doi of the article in an array
             */
            return (doi !== undefined) ? 'doi:' + doi[0] : false;
        };

        this.parseGenre = function(doctype) {
            /**
             * Parses the incoming doctype to define the type of genre this should fit in.
             * Currently, this is not implemented within the search engine, such that the doctype is
             * not indexed. This will be updated in the future, for now we default to genre:article
             *
             * @param {string} doctype - doctype
             */

            return (doctype !== undefined) ? doctype : 'article';

        };

        this.parseContent = function() {
            /**
             * Parses the metadata of the object and fills all the correct attributes required for making a
             * ContextObject
             */

            console.log('here');
            console.log(this.metadata);

            this.rft_spage = this.parseFirstPage(this.metadata.page);
            this.id = this.parseID(this.metadata.doi);
            this.genre = this.parseGenre(this.metadata.doctype);
            this.rft_id = this.parseRFTInfo(this.metadata);
            var author_name = this.parseAuthor(this.metadata.first_author);
            this.rft_aulast = author_name['lastname'];
            this.rft_aufirst = author_name['firstnames'];

            this.rft_issue = (this.metadata.issue !== undefined) ? this.metadata.issue : false;
            this.rft_vol = (this.metadata.volume !== undefined) ? this.metadata.volume : false;
            this.rft_jtitle = (this.metadata.pub !== undefined) ? this.metadata.pub : false;
            this.rft_date = (this.metadata.year !== undefined) ? this.metadata.year : false;
            this.rft_atitle = (this.metadata.title !== undefined) ? this.metadata.title[0] : false;
            this.rft_issn = (this.metadata.issn !== undefined ) ? this.metadata.issn[0] : false;

            this.rft_val_fmt += this.genre;

            console.log(this.rft_id);

        };

        this.createContextObject = function() {
            /**
             * Creates a ContextObject that is used for the generation of OpenURLs either by the service or
             * by third-party plugins
             *
             */

            // Parse the objects content first
            this.parseContent();

            // Generate the ContextObject, which for now will look like a dictionary
            this.contextObject = {
                'url_ver': this.url_ver,
                'rfr_id': this.rfr_id,
                'rft_val_fmt': this.rft_val_fmt,
                'rft_id': this.rft_id,
                'rft.spage': this.rft_spage,
                'rft.issue': this.rft_issue,
                'rft.volume': this.rft_vol,
                'rft.jtitle': this.rft_jtitle,
                'rft.atitle': this.rft_atitle,
                'rft.aulast': this.rft_aulast,
                'rft.aufirst': this.rft_aufirst,
                'rft.date': this.rft_date,
                'rft.issn': this.rft_issn,
                'rft.genre': this.genre,
                'sid': this.sid,
                'spage': this.rft_spage,
                'volume': this.rft_vol,
                'title': this.rft_jtitle,
                'atitle': this.rft_atitle,
                'aulast': this.rft_aulast,
                'aufirst': this.rft_aufirst,
                'date': this.rft_date,
                'issn': this.rft_issn,
                'id': this.id,
                'genre': this.genre
            };
        };

        this.createOpenURL = function() {
            /**
             * Generates the openURL using the context object
             */

            // Create the context object
            this.createContextObject();

            // Modify the base to allow the key values to be appended
            this.openURL = this.baseURL + '?';

            // Append all the keys
            for (var key in this.contextObject) {
                if (this.contextObject.hasOwnProperty(key)) {

                    // Escape if the object is set to false
                    if (this.contextObject[key] === false) {
                        continue;
                    }

                    if (this.contextObject[key] instanceof Array) {
                        for (var i=0; i < this.contextObject[key].length; i++){

                            // Escape if this element is set to false
                            if (this.contextObject[key][i] === false) {
                                continue;
                            }

                            this.openURL += '&' + key + '=' + this.contextObject[key][i];
                        }
                    } else {

                        this.openURL += '&' + key + '=' + this.contextObject[key];
                    }

                }
            }

            // Encode the URL
            this.openURL = encodeURI(this.openURL);
        }

    };

    return OpenURLGenerator;

});

