
define(['jquery',
  'underscore',
  'js/components/query_builder/plugin',
  'js/components/generic_module'
], function(
  $,
  _,
  QueryBuilderPlugin,
  GenericModule) {

  describe("QueryBuilder (translation rules)", function () {


      it("query #1", function() {
        var p = new QueryBuilderPlugin();

        p.setRules({
          "condition": "AND",
          "rules": [
            {
              "id": "author",
              "field": "author",
              "type": "string",
              "input": "text",
              "operator": "is",
              "value": "Roman"
            },
            {
              "condition": "OR",
              "rules": [
                {
                  "id": "title",
                  "field": "title",
                  "type": "string",
                  "input": "text",
                  "operator": "contains",
                  "value": "galaxy"
                },
                {
                  "id": "abstract",
                  "field": "abstract",
                  "type": "string",
                  "input": "text",
                  "operator": "contains",
                  "value": "42"
                }
              ]
            }
          ]
        });

        expect(p.getQuery()).to.be.eql("author:Roman AND (title:galaxy OR abstract:42)");

      });

      it("query #2 - operator:starts_with", function() {
        var p = new QueryBuilderPlugin();

        p.setRules({
          "condition": "AND",
          "rules": [
            {
              "id": "author",
              "field": "author",
              "type": "string",
              "input": "text",
              "operator": "is",
              "value": "Roman"
            },
            {
              "condition": "OR",
              "rules": [
                {
                  "id": "keyword",
                  "field": "keyword",
                  "type": "string",
                  "input": "text",
                  "operator": "starts_with", // starts_With is not compatible with multi-val fiels (such as title)
                  "value": "galaxy"
                },
                {
                  "id": "abstract",
                  "field": "abstract",
                  "type": "string",
                  "input": "text",
                  "operator": "contains",
                  "value": "42"
                }
              ]
            }
          ]
        });

        expect(p.getQuery()).to.be.eql("author:Roman AND (keyword:galaxy* OR abstract:42)");

        p.setRules({
          "condition": "AND",
          "rules": [
            {
              "id": "author",
              "field": "author",
              "type": "string",
              "input": "text",
              "operator": "is",
              "value": "Roman"
            },
            {
              "condition": "OR",
              "rules": [
                {
                  "id": "keyword",
                  "field": "keyword",
                  "type": "string",
                  "input": "text",
                  "operator": "starts_with", // starts_With is not compatible with multi-val fiels (such as title)
                  "value": "galaxy sta"
                },
                {
                  "id": "abstract",
                  "field": "abstract",
                  "type": "string",
                  "input": "text",
                  "operator": "contains",
                  "value": "42"
                }
              ]
            }
          ]
        });

        expect(p.getQuery()).to.be.eql("author:Roman AND (keyword:\"galaxy sta*\" OR abstract:42)");
      });

    }
  );
});