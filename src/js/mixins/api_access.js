define([
    'underscore',
    'backbone',
    'js/components/api_query',
    'js/components/api_request'
  ],
  function(
    _,
    Backbone,
    ApiQuery,
    ApiRequest
    ){

    /*
    * this simple mixin contacts the api (getApiAccess), and if the {reconnect: true} option
    * is passed to getApiAccess, will save the relevant data.
    * */

    return {

      /**
       * After bootstrap receives all data, this routine should decide what to do with
       * them
       */
      onBootstrap: function (data) {
        // set the API key and other data from bootstrap
        if (data.access_token) {
          this.getBeeHive().getService('Api').setVals({
            access_token : data.token_type + ':' + data.access_token,
            refresh_token : data.refresh_token,
            expire_in : data.expire_in
        });

         console.warn('Redefining access_token: ' + data.access_token);

          var userObject = this.getBeeHive().getObject("User");
          var userName = data.anonymous ? undefined : data.username;
          userObject.setUser(userName);
        }

        else {
          console.warn("bootstrap didn't provide access_token!");
        }
      },

      getApiAccess: function (options) {
        options = options || {};
        var api = this.getBeeHive().getService('Api');
        var redirect_uri = location.origin + location.pathname;
        var self = this;
        var defer = $.Deferred();

        // if token expired, make a _request
        var request = options.tokenRefresh ? '_request' : 'request';

        api[request](new ApiRequest({
            query: new ApiQuery({redirect_uri: redirect_uri}),
            target: this.bootstrapUrls ? this.bootstrapUrls[0] : '/accounts/bootstrap'}),
          {
            done: function (data) {
              if (options.reconnect) {
                self.onBootstrap(data);
              }
              defer.resolve(data);
            },
            fail: function () {
              defer.reject(arguments);
            },
            type: 'GET'
          });
        return defer;
      }

    }




});
