define([
    'redux',
    'js/widgets/facet/actions',
    'js/widgets/facet/reducers',
    'js/widgets/facet/create_store',
    'js/wraps/author_facet',
    'js/bugutils/minimal_pubsub',
    'react',
    'react-dom',
    ],

  function(
    Redux,
    Actions,
    Reducers,
    createStore,
    AuthorFacet,
    MinSub,
    React,
    ReactDOM
  ) {

    describe('FacetWidget', function() {

      var sampleResponse = {
        'responseHeader': {
          'status': 0,
          'QTime': 18,
          'params': {
            'facet.limit': '25',
            'q': 'author:"accomazzi,a"',
            'facet.field': 'author_facet_hier',
            'fl': 'id',
            'facet.prefix': '0/',
            'sort': 'date desc',
            'facet.mincount': '1',
            'facet': 'true',
            'wt': 'json',
            'facet.offset': '0'
          }
        },
        'response': {
          'numFound': 186,
          'start': 0,
          'docs': [{
              'id': '11310415'
              }, {
              'id': '11266285'
              }, {
              'id': '11121250'
              }, {
              'id': '11057812'
              }
            ]
        },
        'facet_counts': {
          'facet_queries': {},
          'facet_fields': {
            'author_facet_hier': ['0/Accomazzi, A', 186, '0/Kurtz, M', 151, '0/Grant, C', 146, '0/Murray, S', 142]
          },
          'facet_dates': {},
          'facet_ranges': {}
        }
      }

      afterEach(function(){
        document.querySelector("#test").innerHTML = '';
      })

      it('should have the appropriate default state given individual widget presets', function() {

        var store = createStore();

        expect(JSON.stringify(store.getState())).to.eql(JSON.stringify({
          'config': {
            'preprocessors': [],
            'hierMaxLevels': 1,
            'openByDefault': false
          },
          'state': {
            'open': false,
            'visible': 5,
            'selected': []
          },
          'pagination': {
            'finished': false
          },
          'children': [],
          'facets': {}
        }));

        //now provide it with author config
        var widget = AuthorFacet();

        //ensure that the config object is located in each widget's store rather than on the widget object itself

        expect(Object.keys(widget.store.getState())).to.eql(['config', 'state', 'pagination', 'children', 'facets']);

        expect(JSON.stringify(widget.store.getState())).to.eql(JSON.stringify({
          'config': {
            'preprocessors': [],
            'hierMaxLevels': 2,
            'facetField': 'author_facet_hier',
            'openByDefault': true,
            'facetTitle': 'Authors',
            'logicOptions': {
              'single': [
                  'limit to',
                  'exclude'
                ],
              'multiple': [
                  'and',
                  'or',
                  'exclude'
                ]
            }
          },
          'state': {
            'open': false,
            'visible': 5,
            'selected': []
          },
          'pagination': {
            'finished': false
          },
          'children': [],
          'facets': {}
        }))

      });

      it('should handle data request and response events properly', function() {

        var widget = AuthorFacet();

        widget.store.dispatch(Actions().data_requested());

        //only pagination "loading" key is changed
        expect(JSON.stringify(_.pick(widget.store.getState(), 'state', 'pagination'))).to.eql(
          JSON.stringify({
            'state': {
              'open': false,
              'visible': 5,
              'selected': []
            },
            'pagination': {
              'state': 'loading',
              'finished': false
            }
          })
        );

        widget.store.dispatch(Actions().data_received(sampleResponse));

        expect(JSON.stringify(widget.store.getState())).to
          .eql(JSON.stringify({
            'config': {
              'preprocessors': [],
              'hierMaxLevels': 2,
              'facetField': 'author_facet_hier',
              'openByDefault': true,
              'facetTitle': 'Authors',
              'logicOptions': {
                'single': [
                      'limit to',
                      'exclude'
                    ],
                'multiple': [
                      'and',
                      'or',
                      'exclude'
                    ]
              }
            },
            'state': {
              'open': false,
              'visible': 5,
              'selected': []
            },
            'pagination': {
              'state': 'success',
              'finished': true
            },
            'children': [
                  '0/Accomazzi, A',
                  '0/Kurtz, M',
                  '0/Grant, C',
                  '0/Murray, S'
                ],
            'facets': {
              '0/Accomazzi, A': {
                'name': 'Accomazzi, A',
                'value': '0/Accomazzi, A',
                'count': 186,
                'pagination': {
                  'finished': false
                },
                'children': [],
                'state': {
                  'open': false,
                  'visible': 5,
                  'selected': []
                }
              },
              '0/Kurtz, M': {
                'name': 'Kurtz, M',
                'value': '0/Kurtz, M',
                'count': 151,
                'pagination': {
                  'finished': false
                },
                'children': [],
                'state': {
                  'open': false,
                  'visible': 5,
                  'selected': []
                }
              },
              '0/Grant, C': {
                'name': 'Grant, C',
                'value': '0/Grant, C',
                'count': 146,
                'pagination': {
                  'finished': false
                },
                'children': [],
                'state': {
                  'open': false,
                  'visible': 5,
                  'selected': []
                }
              },
              '0/Murray, S': {
                'name': 'Murray, S',
                'value': '0/Murray, S',
                'count': 142,
                'pagination': {
                  'finished': false
                },
                'children': [],
                'state': {
                  'open': false,
                  'visible': 5,
                  'selected': []
                }
              }
            }
          }))

        //now test fetching data for child facet
        widget.store.dispatch(Actions().data_requested('0/Accomazzi, A'));

        expect(JSON.stringify(widget.store.getState().facets['0/Accomazzi, A'])).to.eql(JSON.stringify({
          'name': 'Accomazzi, A',
          'value': '0/Accomazzi, A',
          'count': 186,
          'pagination': {
            'state': 'loading',
            'finished': false
          },
          'children': [],
          'state': {
            'open': false,
            'visible': 5,
            'selected': []
          }
        }));

        //test hierarchical facet data received
        widget.store.dispatch(Actions().data_received(sampleResponse, '0/Accomazzi, A'));

        expect(JSON.stringify(widget.store.getState().facets['0/Accomazzi, A'])).to.eql(JSON.stringify({
            'name': 'Accomazzi, A',
            'value': '0/Accomazzi, A',
            'count': 186,
            'pagination': {
              'state': 'success',
              'finished': true
            },
            'children': [
              '0/Accomazzi, A',
              '0/Kurtz, M',
              '0/Grant, C',
              '0/Murray, S'
            ],
            'state': {
              'open': false,
              'visible': 5,
              'selected': []
            }
          }

        ));

      });

      it('should only set the finished param in the pagination state if fewer facets return than were requested', function() {

        var widget = AuthorFacet();
        var shortResponse = _.cloneDeep(sampleResponse);

        //should return only 5 facets instead of the requested 25
        shortResponse.facet_counts.facet_fields.author_facet_hier = shortResponse.facet_counts.facet_fields.author_facet_hier.slice(0, 10);
        widget.store.dispatch(Actions().data_received(shortResponse));

        expect(JSON.stringify(widget.store.getState().pagination)).to.eql(JSON.stringify({
          state: 'success',
          finished: true
        }));

        //finished = true got set bc response number was < 25
        //now mock a longer response
        var widget = AuthorFacet();
        var longResponse = _.cloneDeep(sampleResponse);
        //representing 25 responses
        longResponse.facet_counts.facet_fields.author_facet_hier = _.range(50).map(function(n) {
          if (n % 2 === 0) {
            return '0/fake name'
          } else {
            return 5
          }
        });

        widget.store.dispatch(Actions().data_received(longResponse));

        expect(JSON.stringify(widget.store.getState().pagination)).to.eql(JSON.stringify({
          'state': 'success',
          'finished': false
        }))

      });

      it('should mediate requests/responses through the widget\'s connection with pub sub', function() {

        var longResponse = _.cloneDeep(sampleResponse);
        //representing 25 responses
        longResponse.facet_counts.facet_fields.author_facet_hier = _.range(40).map(function(n) {
          if (n % 2 === 0) {
            return '0/fake name'
          } else {
            return 5
          }
        });

        var minsub = new(MinSub.extend({
          request: sinon.spy()
        }))({
          verbose: false
        });

        var widget = AuthorFacet();
        widget.activate(minsub.beehive.getHardenedInstance());

        //set in some initial data
        widget.store.dispatch(widget.actions.data_received(longResponse));

        //request
        minsub.publish(minsub.INVITING_REQUEST, new minsub.T.QUERY({
          q: 'star'
        }));

        //initial data should be totally cleared
        expect(Object.keys(widget.store.getState().facets).length).to.eql(0);

        //request was made
        expect(JSON.stringify(minsub.request.args[0][0].get('query').toJSON())).to.eql(JSON.stringify({
          'q': [
              'star'
            ],
          'facet': [
              'true'
            ],
          'facet.mincount': [
              '1'
            ],
          'facet.limit': [
              20
            ],
          'fl': [
              'id'
            ],
          'facet.prefix': [
              '0/'
            ],
          'facet.field': [
              'author_facet_hier'
            ],
          'facet.offset': [
              0
            ],
          'rows': [
              1
            ]
        }))

        //facet state was toggled open, since it's open by default
        expect(widget.store.getState().state.open).to.be.true;

        //response
        widget.store.dispatch(widget.actions.data_received(longResponse));

        //now test pagination
        widget.store.dispatch(widget.actions.fetch_data());

        expect(JSON.stringify(minsub.request.args[1][0].get('query').toJSON())).to.eql(JSON.stringify({
          'q': [
                'star'
              ],
          'facet': [
                'true'
              ],
          'facet.mincount': [
                '1'
              ],
          'facet.limit': [
                20
              ],
          'fl': [
                'id'
              ],
          'facet.prefix': [
                '0/'
              ],
          'facet.field': [
                'author_facet_hier'
              ],
          'facet.offset': [
                20
              ],
          'rows': [
                1
              ]
        }));

      });

      it('should fetch the first set of facets automatically when facet or facet child is toggled for the first time', function() {

        var store = createStore();
        var actions = Actions();
        actions.fetch_data = sinon.spy(function() {
          return {
            type: 'fake'
          }
        });

        expect(actions.fetch_data.callCount).to.eql(0);
        expect(store.getState().state).to.eql({
          open: false,
          visible: 5,
          selected: []
        })

        store.dispatch(actions.toggle_facet());
        expect(actions.fetch_data.callCount).to.eql(1);

        expect(store.getState().state).to.eql({
          open: true,
          visible: 5,
          selected: []
        })

        store.dispatch(actions.toggle_facet(false));
        expect(store.getState().state).to.eql({
          open: false,
          visible: 5,
          selected: []
        })

        //now do the same thing, but this time with pre-loaded facets, nothing should be requested

        store.dispatch(Actions().data_received(sampleResponse));

        store.dispatch(actions.toggle_facet());
        expect(store.getState().state).to.eql({
          open: true,
          visible: 5,
          selected: []
        })

        expect(actions.fetch_data.callCount).to.eql(1);

      });

      it('should have an action to totally reset the widget state while maintaining the individual widget config', function() {

        var widget = AuthorFacet();

        widget.store.dispatch(widget.actions.data_received(sampleResponse));
        widget.store.dispatch(widget.actions.reset_state());
        expect(JSON.stringify(widget.store.getState())).to.eql(JSON.stringify({
          'config': {
            'preprocessors': [],
            'hierMaxLevels': 2,
            'facetField': 'author_facet_hier',
            'openByDefault': true,
            'facetTitle': 'Authors',
            'logicOptions': {
              'single': [
                'limit to',
                'exclude'
              ],
              'multiple': [
                'and',
                'or',
                'exclude'
              ]
            }
          },
          'state': {
            'open': false,
            'visible': 5,
            'selected': []
          },
          'pagination': {
            'finished': false
          },
          'children': [],
          'facets': {}
        }));
      });

      it('should automatically open, load and select children of hierarchical facets when they are selected', function() {
        var widget = AuthorFacet();
        //happens in the view
        $('#test').append(widget.render().el);

        widget.store.dispatch(widget.actions.data_received(sampleResponse));

        var hierarchicalResponse = _.cloneDeep(sampleResponse);
        hierarchicalResponse.facet_counts.facet_fields.author_facet_hier = ['1/Accomazzi, A/Accomazzi, Alberto', 5, '1/Accomazzi, A/Accomazzi, A A', 10]
        widget.store.dispatch(widget.actions.data_received(hierarchicalResponse, '0/Accomazzi, A'));

        //should be closed by default
        expect($('#test').find('.facet__icon:first').hasClass('facet__icon--closed')).to.be.true;
        expect($('#test').find('.facet__icon:first').hasClass('facet__icon--open')).to.be.false;

        //open
        widget.store.dispatch(widget.actions.toggle_facet());

        expect($('#test').find('.facet__icon:first').hasClass('facet__icon--closed')).to.be.false;
        expect($('#test').find('.facet__icon:first').hasClass('facet__icon--open')).to.be.true;

        expect(widget.store.getState().state.selected).to
          .eql([]);
        widget.store.dispatch(widget.actions.select_facet('0/Accomazzi, A'));

        expect(widget.store.getState().state.selected).to
          .eql([
          "1/Accomazzi, A/Accomazzi, Alberto",
          "1/Accomazzi, A/Accomazzi, A A",
          "0/Accomazzi, A"
        ]);

        //but logic dropdown should show as if only 1 choice were selected
        expect($('#test').find('.facet__dropdown label').map(function(i, el) {
            return el.textContent
          }).get()).to
          .eql([' limit to', ' exclude']);

        $('#test').empty();

      });

      it('should be able to submit facets', function() {

        var widget = AuthorFacet();
        widget.store.dispatch(widget.actions.data_received(sampleResponse));
        widget.store.dispatch(widget.actions.select_facet('0/Accomazzi, A'));
        expect(JSON.stringify(widget.store.getState().state)).to.eql(JSON.stringify({
          open: false,
          visible: 5,
          selected: ['0/Accomazzi, A']
        }));
        widget.store.dispatch(widget.actions.select_facet('0/Kurtz, M'));
        expect(JSON.stringify(widget.store.getState().state)).to.eql(JSON.stringify({
          open: false,
          visible: 5,
          selected: ['0/Accomazzi, A', '0/Kurtz, M']
        }));

        widget.setCurrentQuery(new MinSub.prototype.T.QUERY({
          q: 'star'
        }))
        widget.dispatchNewQuery = sinon.spy();
        widget.store.dispatch(widget.actions.submit_filter('or'));
        expect(JSON.stringify(widget.dispatchNewQuery.args[0][0])).to.eql(JSON.stringify({
          'q': [
                  'star'
                ],
          'fq_author': [
                  '(author_facet_hier:"0/Accomazzi, A" OR author_facet_hier:"0/Kurtz, M")'
                ],
          '__author_facet_hier_fq_author': [
                  'OR',
                  'author_facet_hier:"0/Accomazzi, A"',
                  'author_facet_hier:"0/Kurtz, M"'
                ],
          'fq': [
                  '{!type=aqp v=$fq_author}'
                ]
        }))

      });

      it("should have a dropdown that shows the user the appropriate logic options", function(){

        var widget = AuthorFacet();

        var albertoResponse = _.cloneDeep(sampleResponse);
        albertoResponse.facet_counts.facet_fields.author_facet_hier = albertoResponse.facet_counts.facet_fields.author_facet_hier.slice(0, 2);

        widget.store.dispatch(widget.actions.data_received(albertoResponse));
        //now mock a hierarchical child response
        var hierarchicalResponse = _.cloneDeep(sampleResponse);
        // generate a long list of fake sub alberto names

        function randomString(len) {
          var alphabet = 'abcdefghijklmnopqrstuvwxyz'.split('');
          var s = '';
          while(len > 0) {
            s+= alphabet[Math.floor(Math.random() * 26)];
            len-=1;
          }
          return s;
        }

        var hierNames = _.range(100).map(function(n, i){
          if (i % 2 !== 0) return 5;
          return '1/Accomazzi, A/' + randomString(12);
        });

        hierarchicalResponse.facet_counts.facet_fields.author_facet_hier = hierNames;
        widget.store.dispatch(widget.actions.data_received(hierarchicalResponse, '0/Accomazzi, A'));

        $('#test').append(widget.render().el);

        widget.store.dispatch(widget.actions.toggle_facet());
        widget.store.dispatch(widget.actions.toggle_facet('0/Accomazzi, A'));

        //mimicking the user pushing the 'more' button a bunch of times
        widget.store.dispatch(widget.actions.increase_visible('0/Accomazzi, A'));
        widget.store.dispatch(widget.actions.increase_visible('0/Accomazzi, A'));
        widget.store.dispatch(widget.actions.increase_visible('0/Accomazzi, A'));
        widget.store.dispatch(widget.actions.increase_visible('0/Accomazzi, A'));

          //select 2 facets
          Object.keys(widget.store.getState().facets).slice(2,4).forEach(function(id){
              widget.store.dispatch(widget.actions.select_facet(id))
          });

        expect([].slice.apply(document.querySelectorAll('.facet__dropdown label'))
        .map(function(l){return l.textContent}))
        .to.eql([" and", " or", " exclude"]);

        //select the parent (Just 'Alberto, A')
        widget.store.dispatch(widget.actions.select_facet('0/Accomazzi, A'))

          //this should select all the ~~VISIBLE~~ children
          expect(widget.store.getState().facets['0/Accomazzi, A'].state.visible).to.eql(25);
          expect(widget.store.getState().state.selected.length).to.eql(26)

          expect([].slice.apply(document.querySelectorAll('.facet__dropdown label'))
          .map(function(l){return l.textContent})).to.eql([" limit to", " exclude"]);

          // now showing 30 facets
          widget.store.dispatch(widget.actions.increase_visible('0/Accomazzi, A'));

          //now deselect one of the facets
          Object.keys(widget.store.getState().facets).slice(2,3).forEach(function(id){
              widget.store.dispatch(widget.actions.unselect_facet(id))
          });

          expect(document.querySelector('.facet__dropdown').textContent).to.eql(
            'select no more than 25 facets at a time'
          )


      });

      it('should only submit necessary facets on submit (for hierarchical facets, avoid sending children)', function() {

        var widget = AuthorFacet();

        widget.store.dispatch(widget.actions.data_received(sampleResponse));
        //now mock a hierarchical child response
        var hierarchicalResponse = _.cloneDeep(sampleResponse);
        hierarchicalResponse.facet_counts.facet_fields.author_facet_hier = ['1/Accomazzi, A/Accomazzi, Alberto', 5, '1/Accomazzi, A/Accomazzi, A A', 10]
        widget.store.dispatch(widget.actions.data_received(hierarchicalResponse, '0/Accomazzi, A'));

        expect(widget.store.getState().facets['0/Accomazzi, A'].children).to.eql(
          [
          '1/Accomazzi, A/Accomazzi, Alberto',
          '1/Accomazzi, A/Accomazzi, A A'
        ]);

        widget.store.dispatch(widget.actions.select_facet('0/Accomazzi, A'));

        //should contain parent AND children
        expect(widget.store.getState().state.selected).to.eql([
          '1/Accomazzi, A/Accomazzi, Alberto',
          '1/Accomazzi, A/Accomazzi, A A',
          '0/Accomazzi, A'
        ]);

        widget.setCurrentQuery(new MinSub.prototype.T.QUERY({
          q: 'star'
        }));
        widget.dispatchNewQuery = sinon.spy();
        widget.submitFilter('and');

        //only submitting parent
        expect(JSON.stringify(widget.dispatchNewQuery.args[0][0])).to.eql(JSON.stringify({
          'q': [
              'star'
            ],
          'fq_author': [
              '(author_facet_hier:"0/Accomazzi, A")'
            ],
          '__author_facet_hier_fq_author': [
              'AND',
              'author_facet_hier:"0/Accomazzi, A"'
            ],
          'fq': [
              '{!type=aqp v=$fq_author}'
            ]
        }))
      })
    });
  });
