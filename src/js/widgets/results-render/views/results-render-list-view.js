define(['marionette', 'hbs!js/widgets/results-render/templates/list-template', 'js/widgets/results-render/views/results-render-item-view' ],
	function(Marionette, listTemplate, ResultsItemView){

		var ResultsListView = Marionette.CompositeView.extend({
		    template : listTemplate,
		    itemView : ResultsItemView,
		    itemViewContainer: "#results"

		});

		return ResultsListView
	}

)
