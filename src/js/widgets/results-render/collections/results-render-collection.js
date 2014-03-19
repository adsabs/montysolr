define(['js/widgets/results-render/models/results-render-item-model', 'backbone'],
	function(ItemModel, Backbone){

	var ListCollection = Backbone.Collection.extend({

	    model : ItemModel

	})

	return ListCollection

	}

)