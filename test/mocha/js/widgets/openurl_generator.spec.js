define(
    ['js/widgets/openurl_generator/widget'],
    function(OpenURLGenerator) {

        describe('OpenURL generator function (openurl_generator.spec.js)', function () {

            // Comment out for now
            /*
            beforeEach(function () {

                this.minsub = minsub = new (MinimalPubSub.extend({
                    request: function (apiRequest) {
                        return TestData();
                    }
                }))({verbose: false});
            });

            afterEach(function () {
                $("#test").empty();
            });
            */

            it.skip('(the OpenURL generator) receives an API response object', function(){

            });

            it('parses author names correctly', function(){

                var openURL = new OpenURLGenerator();

                var first_author = "Han, Hillary S. W.";
                var expected_first_author = "Han";
                parsed_first_author = openURL.parseAuthor(first_author);
                expect(parsed_first_author).to.eql(expected_first_author);

                var first_author = undefined;
                parsed_first_author = openURL.parseAuthor(first_author);
                expect(parsed_first_author).to.eql(false);
            });

            it('parses page arrays correctly', function(){

                var openURL = new OpenURLGenerator();

                var page_in = ['3460'];
                var page_expected = '3460';
                page_parsed = openURL.parseFirstPage(page_in);
                expect(page_parsed).to.eql(page_expected)

                var page_in = ['3460-3461'];
                var page_expected = '3460';
                page_parsed = openURL.parseFirstPage(page_in);
                expect(page_parsed).to.eql(page_expected)

            });


            it('creates a ContextObject based on the meta-data of the document', function(){

                var stub_meta_data = {
                    "first_author": "Han, Hillary S. W.",
                    "first_author_norm": "Han, H",
                    "date": "2013-05-01T00:00:00Z",
                    "id": "9803035",
                    "page": ["3460"],
                    "pub": "ArXiv e-prints",
                    "pubdate": "2013-05-00",
                    "title": ["A bijection for tri-cellular maps"],
                    "volume": "25",
                    "doi": ["10.1023/A:1014597702936"]
                };

                var openURL = new OpenURLGenerator(stub_meta_data);

                openURL.parseContent();
                openURL.createContextObject();

                expect(openURL).to.have.property('baseURL');

                // Check URL versioning type
                expect(openURL.contextObject).to.have.property('url_ver');
                expect(openURL.contextObject.url_ver).to.eql('Z39.88-2004');

                // Check the Registry Information format
                expect(openURL.contextObject).to.have.property('rft_val_fmt');
                expect(openURL.contextObject.rft_val_fmt).to.eql('info:ofi/fmt:kev:mtx:');

                expect(openURL.contextObject).to.have.property('rft.spage');

                // This stub data will not have this property and should be false
                expect(openURL.contextObject).to.have.property('rft.issue');
                expect(openURL.contextObject['rft.issue']).to.eql(false);

                expect(openURL.contextObject).to.have.property('rft.volume');
                expect(openURL.contextObject).to.have.property('rft.jtitle');
                expect(openURL.contextObject).to.have.property('rft.atitle');
                expect(openURL.contextObject).to.have.property('rft_id');
                expect(openURL.contextObject).to.have.property('rft.aulast');
                expect(openURL.contextObject).to.have.property('rft.date');

            });

            it.skip('requests the preferences of the user from myADS and receives expected content', function(){

            });

            it.skip('creates an OpenURL hyperlink based on the users base URL and the ContextObject', function(){

            });
        })

  });