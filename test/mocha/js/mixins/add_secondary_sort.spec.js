define([
      'js/mixins/add_secondary_sort',
      'js/components/api_query'
    ],

    function(
        SecondarySort,
        ApiQuery
    ){

      describe("SecondarySort", function(){

        it ("should add the appropriate secondary sort ot the apiquery sort string", function(){

                var q = new ApiQuery({ sort  : "date desc" });
                SecondarySort.addSecondarySort(q);
                expect(q.get("sort")).to.eql(["date desc, bibcode desc"]);

                var q = new ApiQuery({ sort  : "date asc" });
                SecondarySort.addSecondarySort(q);
                expect(q.get("sort")).to.eql(["date asc, bibcode asc"]);

        });


      });


    });