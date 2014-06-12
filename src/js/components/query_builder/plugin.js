

define([
  'underscore',
  'bootstrap',
  'jquery',
  'jquery-querybuilder',
  'js/components/generic_module'
  ],

  function(
  _,
  Bootstrap,
  $,
  jQueryQueryBuilderPlugin,
  GenericModule
  ) {

    var QueryBuilder = GenericModule.extend({
      initialize: function(options) {
        if (options.el) {
          this.$el = $(options.el);
        }
        else {
          this.$el = $('<div/>');
        }

        this.$el.queryBuilder({
          sortable: true,

          filters: [
            /*
             * basic
             */
            {
              id: 'name',
              label: 'Name',
              type: 'string'
            },
            /*
             * select
             */
            {
              id: 'category',
              label: 'Category',
              type: 'integer',
              input: 'select',
              values: {
                1: 'Books',
                2: 'Movies',
                3: 'Music',
                4: 'Tools',
                5: 'Goodies',
                6: 'Clothes'
              },
              operators: ['equal', 'not_equal', 'in', 'not_in', 'is_null', 'is_not_null']
            },
            /*
             * radio
             */
            {
              id: 'in_stock',
              label: 'In stock',
              type: 'integer',
              input: 'radio',
              values: {
                1: 'Yes',
                0: 'No'
              },
              operators: ['equal']
            },
            /*
             * double
             */
            {
              id: 'price',
              label: 'Price',
              type: 'double',
              validation: {
                min: 0,
                step: 0.01
              }
            },
            /*
             * placeholder and regex validation
             */
            {
              id: 'id',
              label: 'Identifier',
              type: 'string',
              placeholder: '____-____-____',
              operators: ['equal', 'not_equal'],
              validation: {
                format: /^.{4}-.{4}-.{4}$/
              }
            },
            /*
             * custom input
             */
            {
              id: 'coord',
              label: 'Coordinates',
              type: 'string',
              validation: {
                format: /^[A-C]{1}.[1-6]{1}$/
              },
              input: function($rule, filter) {
                var $container = $rule.find('.rule-value-container');

                $container.on('change', '[name=coord_1]', function(){
                  var h = '';

                  switch ($(this).val()) {
                    case 'A':
                      h = '<option value="-1">-</option> <option value="1">1</option> <option value="2">2</option>';
                      break;
                    case 'B':
                      h = '<option value="-1">-</option> <option value="3">3</option> <option value="4">4</option>';
                      break;
                    case 'C':
                      h = '<option value="-1">-</option> <option value="5">5</option> <option value="6">6</option>';
                      break;
                  }

                  $container.find('[name=coord_2]').html(h).toggle(h!='');
                });

                return '\
      <select name="coord_1"> \
        <option value="-1">-</option> \
        <option value="A">A</option> \
        <option value="B">B</option> \
        <option value="C">C</option> \
      </select> \
      <select name="coord_2" style="display:none;"></select>';
              },
              valueParser: function($rule, value, filter, operator) {
                return $rule.find('[name=coord_1]').val()
                  +'.'+$rule.find('[name=coord_2]').val();
              },
              onAfterSetValue: function($rule, value, filter, operator) {
                if (operator.accept_values) {
                  var val = value.split('.');

                  $rule.find('[name=coord_1]').val(val[0]).trigger('change');
                  $rule.find('[name=coord_2]').val(val[1]);
                }
              }
            }]
        });
      },

      receiveQuery: function(query) {

      },
      returnQuery: function() {

      },

      loadCss: function() {
        var url = require.toUrl('jquery-querybuilder') + '.css';

        if ($(document.getElementsByTagName("head")[0]).find('link[href=\''+url+'\']').length == 0) {
          var link = document.createElement("link");
          link.type = "text/css";
          link.rel = "stylesheet";
          link.href = url;
          document.getElementsByTagName("head")[0].appendChild(link);
        }
      }
    });

    return QueryBuilder;
  });