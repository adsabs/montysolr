/**
 *
 * This widget here constitues one complete test with different stages of
 * the query for graph facet
 *
 */
define([
    'underscore',
    'backbone',
    'js/widgets/facet/factory',
    'js/bugutils/minimal_pubsub'],
  function(
    _,
    Backbone,
    FacetFactory,
    MinimalPubSub
    ) {


    describe("Graph Facet", function() {

      var minsub, data;
      beforeEach(function(done) {
        data = [];
        minsub = new (MinimalPubSub.extend({
          request: function(apiRequest) {
            var q = apiRequest.get('query');
            var o = data.shift();
            expect(q.get('facet.limit').toString()).to.eql(o.responseHeader.params["facet.limit"]);
            expect(q.get('facet.mincount').toString()).to.eql(o.responseHeader.params["facet.mincount"]);
            return o;
          }
        }))({verbose: true});
        done();
      });

      afterEach(function(done) {
        minsub.destroy();
        $('#test').empty();
        done();
      });

      it("knows to handle numeric data", function(done) {
        this.timeout(5000);

        var widget = FacetFactory.makeGraphFacet({
          facetField: "year",
          facetTitle: "Year",
          xAxisTitle: "Year",
          openByDefault: true
        });
        widget.activate(minsub.beehive.getHardenedInstance());

        var $w = $(widget.render().el);
        $('#test').append($w);
        window.$w = $w;


        // q=title:planet
        // http://adswhy.cfa.harvard.edu:9005/solr/collection1/select?q=title%3Aplanet&fl=id&wt=json&facet=true&facet.field=year&facet.limit=100&facet.offset=0&facet.mincount=1
        data.push({"responseHeader":{"status":0,"QTime":6,"params":{"facet":"true","fl":"id","facet.offset":"0","facet.mincount":"1","q":"title:planet","facet.limit":"100","facet.field":"year","wt":"json"}},"response":{"numFound":77850,"start":0,"docs":[{"id":"5583392"},{"id":"76257"},{"id":"3893826"},{"id":"4694833"},{"id":"6907315"},{"id":"6913610"},{"id":"7099339"},{"id":"1246450"},{"id":"6186907"},{"id":"6624974"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"year":["2012",4457,"2011",4369,"2009",4296,"2010",3903,"2007",3895,"2008",3799,"2006",2962,"2004",2524,"2005",2498,"2013",2447,"2003",2250,"2002",1372,"2001",1188,"2000",1179,"1993",1058,"1996",1018,"1999",1013,"1997",1001,"1998",999,"1989",942,"1991",931,"1992",907,"1985",892,"1995",892,"1990",877,"1981",875,"1984",858,"1986",840,"1994",829,"1982",824,"1987",820,"1988",810,"2014",795,"1983",783,"1980",769,"1978",764,"1979",740,"1977",647,"1976",587,"1974",538,"1975",538,"1973",505,"1971",447,"1972",435,"1969",422,"1970",409,"1968",324,"1966",214,"1967",204,"1904",195,"1905",187,"1901",179,"1903",171,"1907",171,"1906",170,"1964",168,"1930",167,"1965",164,"1896",161,"1897",159,"1900",157,"1898",155,"1899",154,"1902",147,"1949",143,"1908",142,"1962",138,"1913",133,"1951",130,"1892",128,"1958",128,"1963",128,"1935",124,"1874",123,"1911",123,"1909",121,"1910",121,"1936",121,"1891",120,"1895",120,"1950",120,"1912",118,"1931",113,"1866",112,"1948",112,"1956",112,"1934",110,"1872",109,"1916",109,"1925",106,"1893",103,"1861",101,"1894",101,"1927",101,"1938",101,"1961",101,"1933",99,"1960",99,"1937",97,"1914",96]},"facet_dates":{},"facet_ranges":{}}});


        // issue a new query
        minsub.publish(minsub.START_SEARCH, minsub.createQuery({'q': 'title:planet'}));

        expect($w.find(".show-slider-data-first").val()).to.be.eql('1861');
        expect($w.find(".show-slider-data-second").val()).to.be.eql('2014');

        // q=title:galaxy
        data.push({"responseHeader":{"status":0,"QTime":71,"params":{"facet":"true","fl":"id","facet.offset":"0","facet.mincount":"1","q":"title:galaxy","facet.limit":"100","facet.field":"year","wt":"json"}},"response":{"numFound":125105,"start":0,"docs":[{"id":"3975730"},{"id":"393277"},{"id":"5566429"},{"id":"5584510"},{"id":"8282729"},{"id":"3801984"},{"id":"4615571"},{"id":"8232212"},{"id":"1183879"},{"id":"407011"}]},"facet_counts":{"facet_queries":{},"facet_fields":{"year":["2010",4492,"2011",4412,"2007",4371,"2004",4314,"2001",4250,"1999",4245,"2008",4177,"2009",4170,"2006",4100,"2012",4056,"2005",4052,"2003",3985,"2013",3977,"2000",3788,"1997",3731,"2002",3574,"1996",3502,"1998",3395,"1993",3101,"1994",3050,"1995",2989,"1990",2567,"1987",2481,"1991",2400,"1992",2385,"1988",2357,"1989",2320,"2014",1981,"1986",1945,"1984",1897,"1985",1806,"1983",1797,"1982",1631,"1979",1526,"1981",1505,"1980",1476,"1978",1295,"1977",1121,"1976",1089,"1975",1006,"1973",840,"1974",824,"1972",735,"1971",669,"1970",661,"1968",524,"1969",514,"1967",397,"1966",291,"1965",289,"1964",277,"1963",237,"1962",233,"1961",208,"1960",148,"1958",143,"1959",136,"1957",110,"1956",78,"1955",74,"1951",64,"1954",63,"1936",53,"1953",48,"1931",45,"1940",44,"1937",43,"1950",43,"1952",40,"1934",39,"1939",38,"1938",37,"1946",37,"1948",36,"1949",35,"1920",31,"1942",31,"1929",30,"1932",29,"1941",29,"1935",27,"1930",26,"1933",26,"1917",25,"1923",25,"1925",24,"1927",19,"1928",19,"1943",19,"1918",18,"1944",16,"1912",14,"1913",13,"1919",13,"1947",12,"1922",11,"1945",10,"1926",8,"1908",7,"1914",7]},"facet_dates":{},"facet_ranges":{}}});
        minsub.publish(minsub.START_SEARCH, minsub.createQuery({'q': 'title:galaxy'}));

        expect($w.find(".show-slider-data-first").val()).to.be.eql('1908');
        expect($w.find(".show-slider-data-second").val()).to.be.eql('2014');
        expect($w.find('.axis.x-axis g:last').text()).to.eql("2012-2014");

        $w.find('.sort-options label:eq(1)').click(); // click to sort by size
        setTimeout(function() {
          expect($w.find('.axis.x-axis g:eq(9)').text()).to.eql("2012-2014");
          expect($w.find("g.bar").length).to.eql(14);
          done();
        }, 1000)


      });



    });

  });