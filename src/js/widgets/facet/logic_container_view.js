define(['js/widgets/base/container_view', 'hbs!./templates/tooltip', 'hbs!./templates/logic-container'],
  function (BaseFacetContainerView, FacetTooltipTemplate, LogicFacetContainerTemplate) {

    var SelectLogicModel = BaseFacetContainerView.ContainerModelClass.extend({
      defaults: function () {
        return {
          singleLogic: [
            "limit to", "exclude" ],
          multiLogic: [
            "and", "or", "exclude"
          ],
          selected: undefined,

          title: undefined
        }
      }
    });

    var SelectLogicContainerView = BaseFacetContainerView.extend({

      initialize: function (options) {

        //clear out logic template when collection is reset
        this.listenTo(this.collection, "reset", function () {
          this.$(".dropdown-menu").html(FacetTooltipTemplate({
            noneSelected: true
          }))
        });

        this.on("itemview:select", this.handleLogic);
        this.on("itemview:unselect", this.handleLogic);

        BaseFacetContainerView.prototype.initialize.call(this, options);

      },

      events: function () {
        var addEvents;
        addEvents = {
          "click .dropdown-toggle": "toggleLogic",
          "click .dropdown-menu .close": "closeLogic",
          //for this container, any click on a logic input emits an immediate
          //new query event
          "change .logic-container input": "changeLogicAndSubmit"

        };
        return _.extend(_.clone(BaseFacetContainerView.prototype.events), addEvents)
      },

      template: LogicFacetContainerTemplate,

      changeLogicAndSubmit: function (e) {
        //close the logic dropdown
        this.closeLogic();
        var val = $(e.target).val();
        this.model.set("selected", val);

        //because model is nested, the events won't work by themselves
        this.trigger("containerLogicSelected");
      },

      closeLogic: function () {
        this.$(".dropdown").removeClass("open");
      },


      toggleLogic: function () {
        this.$(".dropdown").toggleClass("open");
      },

      handleLogic: function () {

        var selected = this.$("input:checked")
        var numSelected = selected.length;

        if (numSelected >= 1) {
          //highlight filter
          this.$("i.glyphicon-filter").removeClass("inactive-style").addClass("active-style")
          //highlight caret
          this.$("i.main-caret").addClass("active-style")

        }
        else {
          //unhighlight filter
          this.$("i.glyphicon-filter").removeClass("active-style").addClass("inactive-style")
          //unhighlight caret
          this.$("i.main-caret").removeClass("active-style")

        }

        //open the dropdown
        if (numSelected === 1) {
          this.$(".dropdown-menu").html(FacetTooltipTemplate({
            single: true,
            logic: this.model.get("singleLogic")
          }));

          this.$(".dropdown").addClass("open");

        }
        else if (numSelected > 1) {
          var multiLogic = this.model.get("multiLogic");
          if (multiLogic === "fullSet") {
            /*any multiple selection automatically grabs the full set */
            this.$(".dropdown-menu").html(FacetTooltipTemplate({
              fullSet: true
            }))
          }
          else {
            this.$(".dropdown-menu").html(FacetTooltipTemplate({
              multiLogic: true,
              logic: multiLogic
            }))

          }
          this.$(".dropdown").addClass("open");

        }
        else {

          this.$(".dropdown-menu").html(FacetTooltipTemplate({
            noneSelected: true
          }))
          this.$(".dropdown").removeClass("open");

        }
        return numSelected
      }

    });

    SelectLogicContainerView.ContainerModelClass = SelectLogicModel;

    return SelectLogicContainerView;

  });