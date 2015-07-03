define(
    ['js/mixins/openurl_generator'],
    function(OpenURLGenerator) {

        describe('OpenURL generator function (openurl_generator.spec.js)', function () {

            // A generic piece of stub data to be used in the simple unit tests
            var stub_meta_data = {
                "bibcode": "2015MNRAS.451.4686F",
                "first_author": "Friis, M.",
                "year": "2015",
                "page": ["4686-4690"],
                "pub": "Monthly Notices of the Royal Astronomical Society",
                "pubdate": "2015-05-00",
                "title": ["The warm, the excited, and the molecular gas: " +
                "GRB 121024A shining through its star-forming galaxy"],
                "volume": "451",
                "doi": ["10.1093/mnras/stv960"],
                "issue": 1,
                "issn": ["0035-8711"]
            };

            it('parses author names correctly', function(){
                /**
                 * Checks that the first and last author names are extracted correctly from the
                 * string that is returned by solr in the 'first_author' field
                 */

                var openURL = new OpenURLGenerator();

                var first_author = "Han, Hillary S. W.";

                parsed_first_author = openURL.parseAuthor(first_author);
                expect(parsed_first_author['lastname']).to.eql('Han');
                expect(parsed_first_author['firstnames']).to.eql('Hillary S. W.');

                var first_author = undefined;
                parsed_first_author = openURL.parseAuthor(first_author);
                expect(parsed_first_author['lastname']).to.eql(false);
                expect(parsed_first_author['firstnames']).to.eql(false);

            });

            it('parses page arrays correctly', function(){
                /**
                 * Checks that the first page is parsed correctly from the 'pages' field returned
                 * from Solr. The format of this field is an array that contains sometimes just the
                 * first page, but sometimes the page range. It should parase all possible ways correctly.
                 */

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

            it('parses IDs correctly', function (){
                /**
                 * This checks for common types of identifies given back from Solr that are accepted in
                 * the openURL standard. In openURL v1.0 there is a range of possible IDs that can be placed
                 * within the 'rft_id' key, and repeated.
                 */
                var metadata = {
                    'doi': ["10.1023/A:1014597702936"],
                    'bibcode': 'bibcode'
                }

                var openURL = new OpenURLGenerator();

                var rft_id_out = ['info:doi/10.1023/A:1014597702936', 'info:bibcode/bibcode'];

                doi_parsed = openURL.parseRFTInfo(metadata);
                expect(doi_parsed).to.eql(rft_id_out);
            });

            it('parses article doctype correctly', function() {
                /**
                 * This ensures that the article type is determined correctly using the field 'doctype'
                 * returned from Solr. Currently this is not indexed by Solr, and for now it will default
                 * to genre 'article'.
                 */
                var openURL = new OpenURLGenerator();
                doctype_parsed = openURL.parseGenre(undefined);

                expect(doctype_parsed).to.eql('article');

            });

            it('can parse content properly when there is no metadata', function() {
                /**
                 * Ensures that the OpenURLGenerator class behaves in a sane manner when no metadata object is
                 * passed
                 */
                var openURL = new OpenURLGenerator();
                openURL.parseContent();
                expect(openURL.rft_id).to.eql([false, false]);
            });

            it('creates a ContextObject based on the meta-data of the document', function(){
                /**
                 * Checks that a ContextObject is generated that contains all the expected data from the stub
                 * data that is supplied. It is possible that this ContextObject can be embedded within the
                 * page if we want to allow other people to use plugins to access full text content.
                 */

                var link_server = 'test';
                var openURL = new OpenURLGenerator(stub_meta_data, link_server);

                openURL.createContextObject();

                expect(openURL).to.have.property('baseURL');

                // Check URL versioning type
                expect(openURL.contextObject.url_ver).to.eql('Z39.88-2004');

                // Check the Registry Information format
                expect(openURL.contextObject.rft_val_fmt).to.eql('info:ofi/fmt:kev:mtx:article');
                expect(openURL.contextObject['rft.spage']).to.eql('4686');
                expect(openURL.contextObject['rft.issue']).to.eql(1);
                expect(openURL.contextObject['rft.volume']).to.eql('451');
                expect(openURL.contextObject['rft.jtitle']).to.eql('Monthly Notices of the Royal Astronomical Society');
                expect(openURL.contextObject['rft.atitle']).to.eql('The warm, the excited, and the molecular gas: ' +
                    'GRB 121024A shining through its star-forming galaxy');
                expect(openURL.contextObject['rft_id']).to.eql(['info:doi/10.1093/mnras/stv960',
                    'info:bibcode/2015MNRAS.451.4686F']);
                expect(openURL.contextObject['rft.aulast']).to.eql('Friis');
                expect(openURL.contextObject['rft.date']).to.eql('2015');
                expect(openURL.contextObject['rft.genre']).to.eql('article');
                expect(openURL.contextObject['rft.issn']).to.eql('0035-8711');
                expect(openURL.contextObject['rfr_id']).to.eql('info:sid/ADS');
                expect(openURL.contextObject['spage']).to.eql(openURL.contextObject['rft.spage']);
                expect(openURL.contextObject['volume']).to.eql(openURL.contextObject['rft.volume']);
                expect(openURL.contextObject['title']).to.eql(openURL.contextObject['rft.jtitle']);
                expect(openURL.contextObject['atitle']).to.eql(openURL.contextObject['rft.atitle']);
                expect(openURL.contextObject['aufirst']).to.eql(openURL.contextObject['rft.aufirst']);
                expect(openURL.contextObject['aulast']).to.eql(openURL.contextObject['rft.aulast']);
                expect(openURL.contextObject['date']).to.eql(openURL.contextObject['rft.date']);
                expect(openURL.contextObject['genre']).to.eql(openURL.contextObject['rft.genre']);
                expect(openURL.contextObject['issn']).to.eql(openURL.contextObject['rft.issn']);
                expect(openURL.contextObject['id']).to.eql('doi:10.1093/mnras/stv960');
            });

            it('creates an OpenURL hyperlink based on the users base URL and the ContextObject', function(){
                /**
                 * Checks that an OpenURL is created and that the OpenURL is what is expected.
                 */

                var expected_openURL = 'test?date=2015&id=doi%3A10.1093/mnras/stv960&genre=article&issn=0035-8711&' +
                    'title=Monthly%20Notices%20of%20the%20Royal%20Astronomical%20Society&volume=451&spage=4686&' +
                    'atitle=The%20warm,%20the%20excited,%20and%20the%20molecular%20gas%3a%20GRB%20121024A%20shining' +
                    '%20through%20its%20star-forming%20galaxy&aulast=Friis&aufirst=M.&sid=ADS&url_ver=Z39%2E88-2004' +
                    '&rft_id=info%3Adoi%2F10.1093/mnras/stv960&rft_id=info%3Abibcode%2F2015MNRAS.451.4686F&' +
                    'rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Aarticle&rft.genre=article&' +
                    'rft.jtitle=Monthly%20Notices%20of%20the%20Royal%20Astronomical%20Society&rft.issn=0035-8711&' +
                    'rft.volume=451&rft.spage=4686&rft.date=2015&rft.aulast=Friis&rft.atitle=The%20warm,%20the' +
                    '%20excited,%20and%20the%20molecular%20gas%3a%20GRB%20121024A%20shining%20through%20its' +
                    '%20star-forming%20galaxy&rfr_id=info:sid/ADS';

                var openURL = new OpenURLGenerator(stub_meta_data);

                // Create the open URL
                openURL.createOpenURL();

                // Hackish comparison
                // Split both urls based on the &
                var expected_URL = expected_openURL.replace('test?','').split('&');

                for (var i=0; i < expected_URL.length; i++) {
                    expect(decodeURIComponent(openURL.openURL)).to.include(decodeURIComponent(expected_URL[i]));
                }

            });

            it('creates an OpenURL hyperlink with no false values', function(){
                /**
                 * If some fields that are wanted for the ContextObject are not found within the Solr response, false
                 * values will be set for them. If this is the case, we do not want them appearing in the openURL as
                 * false, and so this test ensures that the text 'false' does not appear in the openURL.
                 * @type {string}
                 */
                var expected_openURL = 'test?' +
                    'date=2015&' +
                    'genre=article&' +
                    'title=Monthly%20Notices%20of%20the%20Royal%20Astronomical%20Society&' +
                    'sid=ADS&' +
                    'url_ver=Z39%2E88-2004&' +
                    'rft_id=info%3Abibcode%2F2015MNRAS.451.4686F&' +
                    'rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Aarticle&' +
                    'rft.genre=article&' +
                    'rft.jtitle=Monthly%20Notices%20of%20the%20Royal%20Astronomical%20Society&' +
                    'rft.date=2015&' +
                    'rfr_id=info:sid/ADS';

                var false_meta_data = {
                    "bibcode": "2015MNRAS.451.4686F",
                    "year": "2015",
                    "pub": "Monthly Notices of the Royal Astronomical Society",
                };

                var openURL = new OpenURLGenerator(false_meta_data);

                // Create the open URL
                openURL.createOpenURL();
                expect(openURL.openURL).to.not.contain('false');

                // Hackish comparison
                // Split both urls based on the &
                var expected_URL = expected_openURL.replace('test?','').split('&');

                for (var i=0; i < expected_URL.length; i++) {
                    expect(decodeURIComponent(openURL.openURL)).to.include(decodeURIComponent(expected_URL[i]));
                }

            });

            it('works on a range of documents', function() {

                /**
                 * Runs the openURL generator on a selection of bibcodes for which the results were obtained directly
                 * from the current openURL service, and the expected content from the current Solr instance.
                 *
                 * More examples can be included if it is found the generator does not work in certain situations.
                 *
                 * The following has been modified:
                 *
                 * 1. All ISSN references have been added to the Solr docs automatically - they do not show up
                 *    at the moment.
                 * 2. It is assumed we do not handle any letter normalisation
                 * 3. Currently, there is no way to handle 'rft.degree'.
                 * 4. For books, seemingly jtitle == atitle, but this is not done in this version yet
                 */

                // Stub data from Solr
                var stub_meta_data_1 = {
                    "first_author": "Dunham, David",
                    "links_data": [
                        "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"
                    ],
                    "pub": "Bulletin of the American Astronomical Society",
                    "volume": "43",
                    "doi": [
                        "10.3847/BAASOBIT2011023"
                    ],
                    "year": "2011",
                    "date": "2011-12-01T00:00:00Z",
                    "page": [
                        "023"
                    ],
                    "bibcode": "2011BAAS...43..023D",
                    "title": [
                        "Obituary: Thomas C. Van Flandern (1940-2009)"
                    ]
                };
                // ISSN was added manually - it was not in Solr?
                // Author output was modified, i.e., o-umlaut to o+e. I do not think this will be important given that
                // the link server just wants the author name.
                var stub_meta_data_2 = {
                    "first_author": "Hoeflich, P.",
                    "links_data": [
                        "{\"title\":\"\", \"type\":\"pdf\", \"instances\":\"\", \"access\":\"open\"}",
                        "{\"title\":\"\", \"type\":\"preprint\", \"instances\":\"\", \"access\":\"open\"}",
                        "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"open\"}",
                        "{\"title\":\"\", \"type\":\"spires\", \"instances\":\"\", \"access\":\"\"}"
                    ],
                    "year": "1998",
                    "bibcode": "1998ApJ...492..228H",
                    "issue": "1",
                    "pub": "The Astrophysical Journal",
                    "volume": "492",
                    "doi": [
                        "10.1086/305018"
                    ],
                    "title": [
                        "Hard X-Rays and Gamma Rays from Type IA Supernovae"
                    ],
                    "page": [
                        "228"
                    ],
                    "issn": ["0004-637X"]
                };
                // ISSN was added manually - it was not in Solr?
                var stub_meta_data_3 = {
                    "first_author": "Miller Bertolami, M. M.",
                    "links_data": [
                        "{\"title\":\"\", \"type\":\"preprint\", \"instances\":\"\", \"access\":\"open\"}",
                        "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"
                    ],
                    "pub": "19th European Workshop on White Dwarfs",
                    "volume": "493",
                    "year": "2015",
                    "page": [
                        "133"
                    ],
                    "issn": ["1050-3390"],
                    "bibcode": "2015ASPC..493..133M",
                    "title": [
                        "Testing Fundamental Particle Physics with the Galactic White Dwarf Luminosity Function"
                    ]
                };
                var stub_meta_data_4 = {
                    "first_author": "Dufour, P.",
                    "pub": "Astronomical Society of the Pacific Conference Series",
                    "volume": "493",
                    "year": "2015",
                    "page": [
                        "0"
                    ],
                    "bibcode": "2015ASPC..493.....D",
                    "title": [
                        "19th European Workshop on White Dwarfs"
                    ],
                    "issn": ["1050-3390"]
                };
                var stub_meta_data_5 = {
                    "isbn": [
                        "9781321674552"
                    ],
                    "first_author": "Laskaris, Georgios",
                    "links_data": [
                        "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"
                    ],
                    "pub": "Ph.D. Thesis",
                    "year": "2015",
                    "page": [
                        "s International, Volume: 76-08(E), Section: B.; 236 p"
                    ],
                    "bibcode": "2015PhDT........35L",
                    "title": [
                        "Photodisintegration of 3He with Double Polarizations"
                    ]
                };
                var stub_meta_data_6 = {
                    "isbn": [
                        "0-471-38151-9"
                    ],
                    "first_author": "Bloomfield, Louis A.",
                    "links_data": [
                        "{\"title\":\"\", \"type\":\"library\", \"instances\":\"\", \"access\":\"\"}",
                        "{\"title\":\"\", \"type\":\"electr\", \"instances\":\"\", \"access\":\"\"}"
                    ],
                    "year": "2000",
                    "page": [
                        "536"
                    ],
                    "bibcode": "2000htwt.book.....B",
                    "title": [
                        "How Things Work: The Physics of Everyday Life, 2nd Edition"
                    ]
                };
                var stub_meta_data_7 = {
                    "isbn": [
                        "0-201-09924-1"
                    ],
                    "first_author": "Pais, Abraham",
                    "pub": "Some Strangeness in the Proportion",
                    "year": "1980",
                    "page": [
                        "197"
                    ],
                    "bibcode": "1980ssp..conf..197P",
                    "title": [
                        "Einstein on Particles, Fields, and the Quantum Theory"
                    ]
                };

                // Stub data from openURL service on ADS classic
                // Note: all genres have been altered in the URLs to be article until doctype is indexed by Solr
                var expected_open_url_1 = 'test?date=2011&id=doi%3A10.3847/BAASOBIT2011023&' +
                    'genre=article&title=Bulletin%20of%20the%20American%20Astronomical' +
                    '%20Society&volume=43&spage=023&atitle=Obituary%3a%20Thomas%20C.%20Van%20' +
                    'Flandern%20%281940-2009%29&aulast=Dunham&aufirst=&sid=ADS&url_ver=Z39%2E88-2004' +
                    '&rft_id=info%3Adoi%2F10.3847/BAASOBIT2011023&rft_id=info%3Abibcode' +
                    '%2F2011BAAS...43..023D&rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Aarticle' +
                    '&rft.genre=article&rft.jtitle=Bulletin%20of%20the%20American%20Astronomical' +
                    '%20Society&rft.volume=43&rft.spage=023&rft.date=2011&rft.aulast=Dunham' +
                    '&rft.atitle=Obituary%3a%20Thomas%20C.%20Van%20Flandern%20%281940-2009%29&rfr_id=info:sid/ADS';

                var expected_open_url_2 = 'test?date=1998&id=doi%3A10.1086/305018&genre=article' +
                    '&issn=0004-637X&title=The%20Astrophysical%20Journal&volume=492&spage=228' +
                    '&atitle=Hard%20X-Rays%20and%20Gamma%20Rays%20from%20Type%20IA%20Supernovae' +
                    '&aulast=Hoeflich&aufirst=&sid=ADS&url_ver=Z39%2E88-2004&rft_id=info%3Adoi' +
                    '%2F10.1086/305018&rft_id=info%3Abibcode%2F1998ApJ...492..228H&rft_val_fmt=info' +
                    '%3Aofi%2Ffmt%3Akev%3Amtx%3Aarticle&rft.genre=article&rft.jtitle=The%20Astrophysical' +
                    '%20Journal&rft.issn=0004-637X&rft.volume=492&rft.spage=228&rft.date=1998' +
                    '&rft.aulast=Hoeflich&rft.atitle=Hard%20X-Rays%20and%20Gamma%20Rays%20from%20Type' +
                    '%20IA%20Supernovae&rfr_id=info:sid/ADS';

                var expected_open_url_3 = 'test?date=2015&genre=article&issn=1050-3390&title=19th' +
                    '%20European%20Workshop%20on%20White%20Dwarfs&volume=493&spage=133&atitle=Testing' +
                    '%20Fundamental%20Particle%20Physics%20with%20the%20Galactic%20White%20Dwarf' +
                    '%20Luminosity%20Function&aulast=Miller';

                var expected_open_url_4 = 'test?date=2015&genre=article&issn=1050-3390&title=19th' +
                    '%20European%20Workshop%20on%20White%20Dwarfs&volume=493&spage=&atitle=19th%20European' +
                    '%20Workshop%20on%20White%20Dwarfs&aulast=Dufour&aufirst=&sid=ADS&url_ver=Z39%2E88-2004' +
                    '&rft_id=info%3Abibcode%2F2015ASPC..493.....D&rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx' +
                    '%3Aarticle&rft.genre=article&' +
                    'rft.issn=1050-3390&rft.volume=493&rft.spage=&rft.date=2015&rft.aulast=Dufour' +
                    '&rft.atitle=19th%20European%20Workshop%20on%20White%20Dwarfs&rfr_id=info:sid/ADS';

                // For now have removed rft.degree=PhD - this should be included when doctype is indexed
                var expected_open_url_5 = 'test?date=2015&genre=article&isbn=9781321674552&' +
                    'title=Photodisintegration%20of%203He%20with%20Double%20Polarizations&aulast=Laskaris' +
                    '&aufirst=&sid=ADS&url_ver=Z39%2E88-2004&rft_id=info%3Abibcode%2F2015PhDT........35L&' +
                    'rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Aarticle&rft.genre=article' +
                    '&rft.atitle=Photodisintegration%20of%203He%20with%20Double%20Polarizations' +
                    '&rft.isbn=9781321674552&rft.date=2015&rft.aulast=Laskaris&rfr_id=info:sid/ADS';

                // If doctype == book, then jtitle and atitle are the same? I do not think this seems logical, and
                // would just leave jtitle empty.
                var expected_open_url_6 = 'test?date=2000&genre=article&title=How%20Things%20Work%3a%20The%20Physics' +
                    '%20of%20Everyday%20Life,%202nd%20Edition' +
                    '&spage=' +
                    '&atitle=How%20Things%20Work%3a%20The%20Physics%20of%20Everyday%20Life,%202nd%20Edition&' +
                    'aulast=Bloomfield&aufirst=&sid=ADS&url_ver=Z39%2E88-2004&rft_id=info%3Abibcode' +
                    '%2F2000htwt.book.....B&rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Aarticle&rft.genre=article';

                var expected_open_url_7 = 'test?date=1980&genre=article&title=Some%20Strangeness%20in%20the' +
                    '%20Proportion&spage=197&atitle=Einstein%20on%20Particles,%20Fields,%20and%20the' +
                    '%20Quantum%20Theory&aulast=Pais&aufirst=&sid=ADS&url_ver=Z39%2E88-2004&rft_id=info%3Abibcode' +
                    '%2F1980ssp..conf..197P&rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Aarticle' +
                    '&rft.genre=article&rft.jtitle=Some%20Strangeness%20in%20the%20Proportion&rft.spage=197' +
                    '&rft.date=1980&rft.aulast=Pais&rft.atitle=Einstein%20on%20Particles,%20Fields,%20and' +
                    '%20the%20Quantum%20Theory&rfr_id=info:sid/ADS';

                // Some place holders for the stub data
                var stub_meta_data_list = [
                    stub_meta_data_1,
                    stub_meta_data_2,
                    stub_meta_data_3,
                    stub_meta_data_4,
                    stub_meta_data_5,
                    stub_meta_data_6,
                    stub_meta_data_7
                ];
                var expected_open_url_list = [
                    expected_open_url_1,
                    expected_open_url_2,
                    expected_open_url_3,
                    expected_open_url_4,
                    expected_open_url_5,
                    expected_open_url_6,
                    expected_open_url_7
                ];

                var expected_URL, openURL, i;

                // Check that all the openURLs are created correctly
                for (var i=0; i < stub_meta_data_list.length; i++) {
                    openURL = new OpenURLGenerator(stub_meta_data_list[i]);
                    openURL.createOpenURL();

                    expected_URL = expected_open_url_list[i].replace('test?','').split('&');

                    for (var j=0; j < expected_URL.length; j++) {
                        expect(decodeURIComponent(openURL.openURL)).to.include(decodeURIComponent(expected_URL[j]));
                    }

                }

            });

        })

  });