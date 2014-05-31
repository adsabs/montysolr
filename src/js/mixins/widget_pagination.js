/**
 * This mixins adds predefined pagination interaction to the 'View'
 * 
 *  
 */
define(['underscore'], function (_) {
  
  var PaginatorInteraction = {

    /**
     * This method will automatically paginate through results, provided
     * your view contains the right methods and the paginator instance
     * is available
     *
     *
     * @param displayNum
     *    - int, how many items to load during this step
     * @param maxDisplayNum
     *    - int, maximum number of items that are allowed to be displayed;
     *      we'll ignore all further requests once this number is reached
     * @param numOfLoadedButHiddenItems
     *    - int, number of items that were loaded and rendered by the
     *      view, but are not yet shown to the user; the view should
     *      provide this information because we don't know how to 
     *      extract it
     * @param paginator
     *    - properly initialized instance of 'Paginator'
     * @param view
     *    - must contain methods: 
     *        disableShowMore([msg])
     *        displayMore(num)
     *        
     * @param collection
     *
     *
     * @return either null or an object with following semantics
     *      before: a function that you should execute before dispatching
     *              the query
     */
    handlePagination: function(displayNum, maxDisplayNum, numOfLoadedButHiddenItems, paginator, view, collection) {

      var _adjustMaxDisplay = function(currentLen, toDisplay) {
        var allowedMax = maxDisplayNum-currentLen;
        if (allowedMax < toDisplay) {
          return allowedMax;
        }
        return toDisplay;
      };

      // basic sanity validation
      if (!(_.isNumber(displayNum) && displayNum > 0 && _.isNumber(maxDisplayNum) && maxDisplayNum > 0 &&
      _.isNumber(numOfLoadedButHiddenItems) && numOfLoadedButHiddenItems >= 0)) {
        throw new Error("Wrong arguments");
      }

      if (!(paginator && paginator.hasMore && view && view.displayMore && view.disableShowMore)) {
        throw new Error("Your paginator (hasMore) and/or view are missing important methods (displayMore/disableShowMore)");
      }

      if (!(collection && collection.models)) {
        throw new Error("You collection is weird");
      }

      
      if (paginator.hasMore()) {

        // sanity check - there is a maximum that we'll allow to display
        // even if we may load slightly more
        var realDisplayLength = collection.models.length - numOfLoadedButHiddenItems;

        if (realDisplayLength >= maxDisplayNum) {
          view.disableShowMore("Reached max " + this.maxDisplayNum);
          return;
        }


        if (numOfLoadedButHiddenItems > 0) { // we have some docs (hidden)
          var toDisplay = displayNum;
          if (numOfLoadedButHiddenItems > displayNum) { // if it is more then necessary, we just display them
            view.displayMore(_adjustMaxDisplay(realDisplayLength, toDisplay));
            return;
          }
          else {
            var cachedDisplay = _adjustMaxDisplay(realDisplayLength, numOfLoadedButHiddenItems);
            view.displayMore(cachedDisplay); // display one part from the hidden items
            realDisplayLength += cachedDisplay;
            toDisplay = _adjustMaxDisplay(realDisplayLength, toDisplay-cachedDisplay);
          }
        }

        var output = {runQuery: false};
        //console.log('toDisplay', toDisplay);

        if (toDisplay > 0) {
          output['before'] = function() {
            // we'll wait 50 mills after the first item was added to the collection
            // and then show the remaining items
            view.once('after:item:added', function () {
                var self = this;
                setTimeout(function () {
                  //console.log('DisplayMore', toDisplay);
                  self.displayMore(toDisplay)
                }, 50);
              },
              view);
          }
        }


        if (toDisplay + realDisplayLength >= maxDisplayNum) {
          view.disableShowMore("Reached max " + maxDisplayNum);
        }
        else {
          output.runQuery = true;
        }

        return output;
      }
      else {
        view.displayMore(numOfLoadedButHiddenItems);
        view.disableShowMore();
      }

    }
  };
  
  return PaginatorInteraction;

});