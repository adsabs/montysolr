define([
  "js/widgets/form_accordion/widget"
], function(
  FormWidget
  ){

  describe("Form Accordion Widget (UI Widget)", function(){


    afterEach(function(){
      $("#test").empty();
    })

    it("should have an author view", function(){

      var s = FormWidget.prototype.submitForm;
      FormWidget.prototype.submitForm = sinon.spy();

      var f = new FormWidget();
      $("#test").append(f.render().el);
      f.onShow();

      var authorView = f.view.regionManager._regions["author-form"].currentView;

      f.view.$("#headingOne").click();

      expect(authorView.$("button[type=submit]").prop("disabled")).to.be.true;

      authorView.$(".author-input:first").val("Accomazzi,a");

      authorView.$(".author-input:first").trigger("keyup");

      expect(authorView.$("button[type=submit]").prop("disabled")).to.be.false;

      authorView.$(".add-author").click();

      authorView.$(".author-input").eq(1).val("Kurtz,M");

      authorView.$(".author-input").eq(1).next().click();

      authorView.$("button[type=submit]").click();

      expect( FormWidget.prototype.submitForm.args[0][0]).to.eql('author:("Accomazzi,a" "^Kurtz,M")');

      FormWidget.prototype.submitForm = s;


    })

    it("should have a paper view", function(){

      var s = FormWidget.prototype.submitForm;
      FormWidget.prototype.submitForm = sinon.spy();

      var f = new FormWidget();
      $("#test").append(f.render().el);
      f.onShow();

      var paperView = f.view.regionManager._regions["paper-form"].currentView;

      f.view.$("#headingTwo").click()

      expect(paperView.$("button[type=submit]").prop("disabled")).to.be.true;

      paperView.$("#pub-input").val("APJ");
      paperView.$("#year-input").val("1999");
      paperView.$("#volume-input").val("3");
      paperView.$("#page-input").val("4");

      paperView.$("#pub-input:first").trigger("keyup");
      expect(paperView.$("button[type=submit]").prop("disabled")).to.be.false;

      paperView.$("button[type=submit]").click();

      expect( FormWidget.prototype.submitForm.args[0][0]).to.eql('bibstem:APJ year:1999 volume:3 page:4' );

      FormWidget.prototype.submitForm = s;

    });

    it("should have a topic view", function(){

      var s = FormWidget.prototype.submitForm;
      FormWidget.prototype.submitForm = sinon.spy();

      var f = new FormWidget();
      $("#test").append(f.render().el);
      f.onShow();

      var topicView = f.view.regionManager._regions["topic-form"].currentView;

      f.view.$("#headingThree").click();

      expect(topicView.$("button[type=submit]").prop("disabled")).to.be.true;

      topicView.$("#topic-search").val("bees");

      topicView.$("#topic-search").trigger("keyup");

      expect(topicView.$("button[type=submit]").prop("disabled")).to.be.false;

      topicView.$("button[type=submit]").click();

      expect(FormWidget.prototype.submitForm.args[0][0]).to.eql('abstract:(bees) OR title:(bees)');

      FormWidget.prototype.submitForm = s;

    });



  });






})