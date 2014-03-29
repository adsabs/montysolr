

define(['underscore', 'jquery', 'js/components/generic_module', 'js/components/api_request', 'js/mixins/dependon'],
  function(_, $, GenericModule, ApiRequest, Mixin) {

  var Api = GenericModule.extend({
    url: '/api/1/',
    clientVersion: 20140329,
    outstandingRequests: 0,
    done: function( data, textStatus, jqXHR ) {
      // TODO: check the status responses
      var response = new ApiResponse($.parseJSON(data));
      this.api.trigger('api-response', this.request, response);
    },
    fail: function( jqXHR, textStatus, errorThrown ) {
      console.err('API call failed:', JSON.stringify(this.request.toJSON()));
      this.api.trigger('api-error', this.request, jqXHR, textStatus, errorThrown);
    },
    always: function() {
      this.api.outstandingRequests--;
    },
    getNumOutstandingRequests: function() {
      return this.outstandingRequests;
    }

  });


  Api.prototype.ERROR = {
    INVALID_PASSWORD: 498,
    ACCOUNT_NOT_FOUND: 495, // Account not found during signin
    ALREADY_LOGGED_IN: 493, // Already signed during signup
    REQUIRES_LOGIN: 491,
    TOO_MANY_CHARACTERS: 486,
    NOT_FOUND: 404,
    SERVER_ERROR: 503
  };



  Api.prototype.request = function(request, options) {

    if (!(request instanceof ApiRequest)) {
      throw Error("Api.request accepts only instances of ApiRequest");
    }


    var self = this;
    var query = request.get('query');

    var opts = {
      type: 'POST',
      url: this.url + (request.get('target') || ''),
      data: query ? query.toJSON() : {},
      dataType: 'json',
      cache: false,
      headers: {"X-BB-Api-Client-Version": this.clientVersion},
      context: {request: request, opts: this, api: self }
    };


    if (this.hasBeeHive()) {
      var beehive = this.getBeeHive();
      if (beehive.hasUser() && beehive.getUser().get('apiKey')) {
        _.extend(opts.headers, {'X-BB-ApiKey': beehive.getUser().get('apiKey')});
      }

      var clientId = beehive.getSettings('apiClientId');
      if (clientId) {
        opts.headers = _.extend(opts.headers || {}, {
          'X-BB-Client-Id': clientId
        });
      }
    }

    // one potential problem is that 'options' will override
    // whatever is set above (so if sb wants to shoot himself/herself,
    // we gave them the weapon... ;-))
    _.extend(opts, options);


    var jqXhr = $.ajax(opts);
    jqXHr = jqXhr.promise(jqXhr);


    this.outstandingRequests++;
    jqXhr.always(this.always);
    jqXhr.done(this.done);
    jqXhr.fail(this.fail);

    return jqXhr;
  };

  _.extend(Api.prototype, Mixin.BeeHive);

  return Api
});
