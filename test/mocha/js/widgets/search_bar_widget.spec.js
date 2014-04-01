require(['js/widgets/search_bar/search_bar_view'], function(SearchBarView, SearchBarTemplate) {

    describe("Search Bar (UI Widget)", function() {

        it("should render a search bar and a submit button", function() {

        	var a = new SearchBarView();
        	a.render();
            
        	expect(a.$(".q").length).to.equal(1);
        	expect(a.$(".search-submit").length).to.equal(1);
        });

        it("should submit the query when the search-submit button is pressed", function(){



        });

        it("should notify the system of any user interaction in the query input", function(){


        })



    })

})