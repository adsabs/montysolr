/**
 *
 *
 **/

define([], function() {

    var ContextObject = function(metadata){


    };

    var OpenURLGenerator = function (metadata){

        // Fixed values
        this.baseURL = 'test';
        this.metadata = metadata
        this.url_ver = 'Z39.88-2004';
        this.rft_val_fmt = 'info:ofi/fmt:kev:mtx:';

        this.parseAuthor = function(author) {
            /** Parses the author returned from Solr docs that is the full author name, not their second name.
             * The first and last names are comma separated.
             *
             * @param {string} author - author string
             */
            return (author !== undefined) ? author.split(',')[0] : false;
        };

        this.parseFirstPage = function(page) {
            /** Parses the pages such that it returns the first page. There are two scenarios, one where there
             * is a page range, and one where there is only a single page.
             *
             * @param {array} page - array containing a page or page range
             */
            return (page !== undefined) ? page[0].split('-')[0] : false;
        };

        this.parseContent = function() {
            // Parseable values
            this.rft_spage = (metadata.page !== undefined) ? this.parseFirstPage(metadata.page) : false;
            this.rft_issue = metadata.issue || false;
            this.rft_vol = metadata.volume || false;
            this.rft_jtitle = metadata.pub || false;
            this.rft_atitle = metadata.title[0] || false;
            this.rft_id = metadata.doi[0] || false;
            this.rft_au = this.parseAuthor(metadata.first_author);
            this.rft_date = metadata.pubdate || false;
        };

        this.createContextObject = function() {
            this.contextObject = {
                'url_ver': this.url_ver,
                'rft_val_fmt': this.rft_val_fmt,
                'rft_id': this.rft_id,
                'rft.spage': this.rft_spage,
                'rft.issue': this.rft_issue,
                'rft.volume': this.rft_vol,
                'rft.jtitle': this.rft_jtitle,
                'rft.atitle': this.rft_atitle,
                'rft.aulast': this.rft_au,
                'rft.date': this.rft_date
            };
            console.log(this.contextObject);
        };

    };

    return OpenURLGenerator;

});

