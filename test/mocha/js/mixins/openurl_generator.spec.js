define(
    ['js/mixins/openurl_generator'],
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

                var openURL = new OpenURLGenerator();

                var first_author = "Han, Hillary S. W.";

                parsed_first_author = openURL.parseAuthor(first_author);
                console.log(parsed_first_author);
                expect(parsed_first_author['lastname']).to.eql('Han');
                expect(parsed_first_author['firstnames']).to.eql('Hillary S. W.');

                var first_author = undefined;
                parsed_first_author = openURL.parseAuthor(first_author);
                expect(parsed_first_author['lastname']).to.eql(false);
                expect(parsed_first_author['firstnames']).to.eql(false);

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

            it('parses IDs correctly', function (){
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
                var openURL = new OpenURLGenerator();
                doctype_parsed = openURL.parseGenre(undefined);

                expect(doctype_parsed).to.eql('article');


            });

            it('creates a ContextObject based on the meta-data of the document', function(){

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
                console.log(openURL.openURL);

                // Hackish comparison
                // Split both urls based on the &
                var expected_URL = expected_openURL.replace('test?','').split('&');

                for (var i=0; i < expected_URL.length; i++) {
                    expect(decodeURIComponent(openURL.openURL)).to.include(decodeURIComponent(expected_URL[i]));
                }

            });
        })

  });