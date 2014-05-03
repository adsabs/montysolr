

define(['underscore', 'jquery', 'js/components/generic_module', 'js/components/api_request', 'js/mixins/dependon',
  'js/components/api_response', 'js/components/api_query'],
  function(_, $, GenericModule, ApiRequest, Mixin, ApiResponse, ApiQuery) {

  var Api = GenericModule.extend({
    url: '/api/1/',
    clientVersion: 20140329,
    outstandingRequests: 0,

    done: function( data, textStatus, jqXHR ) {
      // TODO: check the status responses
      var response = new ApiResponse(data);
      response.setApiQuery(this.request.get('query'));
      this.api.trigger('api-response', response);
    },
    fail: function( jqXHR, textStatus, errorThrown ) {
      console.warn('API call failed:', JSON.stringify(this.request.url()), jqXHR);
      if (this.api)
        this.api.trigger('api-error', this, jqXHR, textStatus, errorThrown);
    },
    initialize: function() {
      this.always = _.bind(function() {this.outstandingRequests--;}, this);
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
    if (query && !(query instanceof ApiQuery)) {
      throw Error("Api.query must be instance of ApiQuery");
    }

    var u = this.url + (request.get('target') || '');
    if (!u) {
      throw Error("Sorry, dude, you can't use api without url");
    }
    u = u.replace(/\/\/+/, '/');

    var opts = {
      type: 'POST',
      url: u,
      dataType: 'json',
      data: query ? query.url() : "{}",
      contentType: 'application/x-www-form-urlencoded',
      cache: false,
      headers: {"X-BB-Api-Client-Version": this.clientVersion},
      context: {request: request, api: self }
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



    this.outstandingRequests++;

    var jqXhr = $.ajax(opts)
        .always(opts.always ? [this.always, opts.always] : this.always)
        .done(opts.done || this.done)
        .fail(opts.fail || this.fail);

    jqXhr = jqXhr.promise(jqXhr);

    return jqXhr;
  };

  _.extend(Api.prototype, Mixin.BeeHive);

  return Api
});
