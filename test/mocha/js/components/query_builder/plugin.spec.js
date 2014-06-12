
define(['jquery',
  'underscore',
  'js/components/query_builder/plugin',
  'js/components/generic_module'
  ], function(
  $,
  _,
  QueryBuilderPlugin,
  GenericModule) {

  describe("QueryBuilder (UI Component/plugin)", function () {

      it("returns QueryBuilder object", function () {
        expect(new QueryBuilderPlugin()).to.be.instanceof(QueryBuilderPlugin);
        expect(new QueryBuilderPlugin()).to.be.instanceof(GenericModule);
      });

      it("can load CSS", function() {
        var p = new QueryBuilderPlugin();
        p.loadCss();

        var url = require.toUrl('jquery-querybuilder') + '.css';

        expect($(document.getElementsByTagName("head")[0]).find('link[href=\''+url+'\']').length).to.be.eql(1);
        p.loadCss();
        expect($(document.getElementsByTagName("head")[0]).find('link[href=\''+url+'\']').length).to.be.eql(1);
      });

      it("can insert itself into a page", function() {
        var p = new QueryBuilderPlugin({el: '#test'});
        console.log(p.$el);
      });
    }
  );
});