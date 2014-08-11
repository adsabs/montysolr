

define(['js/components/history_manager'], function(HistoryManager){

  describe("History Manager (Component)", function(){

    var manager;

    beforeEach(function(){

      manager = new HistoryManager();

    })

    it("should return the prior page (rather than prior route) using its getPriorPage function", function(){

      manager.addEntry({page: "landingPage", data:  "fakeQuery", subPage: undefined});
      manager.addEntry({page: "resultsPage", data:  "fakeQuery", subPage: undefined});
      manager.addEntry({page: "abstractPage", data:  "fakeBibcode", subPage: "subPage2"});
      manager.addEntry({page: "abstractPage", data:  "fakeBibcode", subPage: "subPage1"});

      expect(manager.getPriorPage()).to.eql("resultsPage");


    })

    it("should return the prior page's data value using its getPriorPageVal function", function(){

      manager.addEntry({page: "landingPage", data:  "fakeQuery", subPage: undefined});
      manager.addEntry({page: "resultsPage", data:  "fakeQuery", subPage: undefined});
      manager.addEntry({page: "abstractPage", data:  "fakeBibcode", subPage: "subPage2"});
      manager.addEntry({page: "abstractPage", data:  "fakeBibcode", subPage: "subPage1"});

      expect(manager.getPriorPageVal()).to.eql("fakeQuery");




    })



  })



})