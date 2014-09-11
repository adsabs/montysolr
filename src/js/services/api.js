

define(['underscore', 'jquery', 'js/components/generic_module', 'js/components/api_request', 'js/mixins/dependon',
  'js/components/api_response', 'js/components/api_query'],
  function(_, $, GenericModule, ApiRequest, Mixin, ApiResponse, ApiQuery) {

  var Api = GenericModule.extend({
    url: '/api/1/',
    clientVersion: 20140329,
    outstandingRequests: 0,

    access_token: null,
    refresh_token: null,
    expires_in: null,

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
    u = u.substring(0, this.url.length-2) + u.substring(this.url.length-2, u.length).replace('//', '/');

    var opts = {
      type: 'GET',
      url: u,
      dataType: 'json',
      data: query ? query.url() : "{}",
      contentType: 'application/x-www-form-urlencoded',
      cache: false,
      headers: {"X-BB-Api-Client-Version": this.clientVersion},
      context: {request: request, api: self }
    };

    if (this.access_token) {
      opts.headers['Authorization'] = this.access_token;
    }

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
