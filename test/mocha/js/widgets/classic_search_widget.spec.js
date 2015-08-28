define([
  "js/widgets/classic_form/widget",

], function(
    ClassicForm
  ){

  describe("Classic Form (UI Widget)", function(){

    afterEach(function(){

      $("#test").empty();

    })

    it("should turn a classic form into an apiQuery on submit", function(){


      var w = new ClassicForm();

      var publishSpy = sinon.spy();
      var fakeBeehive = {Services : {get : function(){return {publish : publishSpy }}}}
      w.activate(fakeBeehive);

      $("#test").append(w.render().el);

      w.onShow();


      expect($("button[type=submit]").prop("disabled")).to.eql(true);

      w.view.$("div[data-field=database]").find("input").attr("checked", true);
      w.view.$("div[data-field=author]").find("textarea").val("Accomazzi,a\rKurtz,M");
      w.view.$("div[data-field=author]").find("textarea").trigger("keyup");

      w.view.$("div[data-field=date]").find("input[name=month_from]").val(10);
      w.view.$("div[data-field=date]").find("input[name=year_from]").val(2010);

      w.view.$("div[data-field=title]").find("input[type=text]").val('star planet "gliese 581"');

      //setting input[value=OR] prop "checked" to true isn't working in phantomjs for some reason :(
      w.view.$("div[data-field=title] input[name=title-logic]").val("OR")

      w.view.$("div[data-field=abstract]").find("input[type=text]").val('-hawaii star');
      w.view.$("div[data-field=abstract] input[name=abstract-logic]").val("BOOLEAN")

      w.view.$("div[data-field=bibstem]").find("input[name=bibstem]").val("apj,mnras,");

      w.view.$("div[data-field=property]").find("input[name=refereed]").click();
      w.view.$("div[data-field=property]").find("input[name=article]").click();

      expect(w.view.$("button[type=submit]").prop("disabled")).to.eql(false);


      w.view.$("button[type=submit]").eq(0).click();

      expect(publishSpy.args[0][1].toJSON()).to.eql({
        "q": [
          "property:refereed property:article author:(\"Accomazzi,a\" AND \"Kurtz,M\") title:(star OR planet OR \"gliese 581\") abstract:(-hawaii star) bibstem:(apj OR mnras)"
        ],
        "sort": [
          "date desc"
        ],
        "fq": [
          "database:(astronomy OR physics) pubdate:[2010-10-0 TO 9999-12-0]"
        ]
      });

      //one more

      w.view.render();

      w.view.$("div[data-field=author]").find("textarea").val("Accomazzi,a");
      w.view.$("div[data-field=author]").find("textarea").trigger("keyup");

      w.view.$("div[data-field=date]").find("input[name=month_from]").val(10);
      w.view.$("div[data-field=date]").find("input[name=year_from]").val(2010);
      w.view.$("div[data-field=date]").find("input[name=year_to]").val(2012);

      w.view.$("div[data-field=title]").find("input[type=text]").val('star planet "gliese 581"');
      //setting input[value=OR] prop "checked" to true isn't working in phantomjs for some reason :(
      w.view.$("div[data-field=title] input[name=title-logic]").val("OR")

      w.view.$("div[data-field=abstract]").find("input[type=text]").val('-hawaii star');
      w.view.$("div[data-field=abstract] input[name=abstract-logic]").val("BOOLEAN")

      w.view.$("div[data-field=bibstem]").find("input[name=bibstem]").val("    apj,     ");

      w.view.$("div[data-field=property]").find("input[name=refereed]").click();

      w.view.$("button[type=submit]").eq(0).click();

      expect(publishSpy.args[1][1].toJSON()).to.eql({
        "q": [
          "property:refereed author:\"Accomazzi,a\" title:(star OR planet OR \"gliese 581\") abstract:(-hawaii star) bibstem:apj"
        ],
        "sort": [
          "date desc"
        ],
        "fq": [
          "database:astronomy pubdate:[2010-10-0 TO 2012-12-0]"
        ]
      });

    })



  });


})