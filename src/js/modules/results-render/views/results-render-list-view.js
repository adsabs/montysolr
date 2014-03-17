define(['marionette', 'hbs!js/modules/results-render/templates/list-template', 'js/modules/results-render/views/results-render-item-view' ],
	function(Marionette, listTemplate, ResultsItemView){

		var ResultsListView = Marionette.CompositeView.extend({
		    template : listTemplate,
		    itemView : ResultsItemView,
		    itemViewContainer: "#results"

		});

		return ResultsListView
	}

)
