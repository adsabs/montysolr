

define([
    'underscore',
    'jquery',
    'js/components/generic_module',
    'js/components/api_request',
    'js/mixins/dependon',
    'js/components/api_response',
    'js/components/api_query',
    'js/components/api_feedback',
    'js/mixins/hardened',
    'js/components/json_response'
  ],
  function(
    _,
    $,
    GenericModule,
    ApiRequest,
    Mixin,
    ApiResponse,
    ApiQuery,
    ApiFeedback,
    Hardened,
    JsonResponse
    ) {

    var Api = GenericModule.extend({
      url: '/api/1/', // usually overriden during app bootstrap
      clientVersion: 20140329,
      outstandingRequests: 0,

      access_token: null,
      refresh_token: null,
      expires_in: null,
      defaultTimeoutInMs: 60000,

      activate: function(beehive) {
        this.setBeeHive(beehive);
      },

      done: function( data, textStatus, jqXHR ) {
        // TODO: check the status responses
        var response = new ApiResponse(data);
        response.setApiQuery(this.request.get('query'));
        this.api.trigger('api-response', response);
      },
      fail: function( jqXHR, textStatus, errorThrown ) {

        console.error('API call failed:', JSON.stringify(this.request.url()), jqXHR.status, errorThrown);

        var pubsub = this.api.hasBeeHive() ? this.api.getPubSub() : null;
        if (pubsub) {
          var feedback = new ApiFeedback({
            code:ApiFeedback.CODES.API_REQUEST_ERROR,
            msg:textStatus,
            request: this.request,
            error: jqXHR,
            psk: this.key || this.api.getPubSub().getCurrentPubSubKey(),
            errorThrown: errorThrown,
            text: textStatus,
            beVerbose: true
          });
          pubsub.publish(pubsub.FEEDBACK, feedback);
        }
        else {
          if (this.api)
            this.api.trigger('api-error', this, jqXHR, textStatus, errorThrown);
        }
      },
      initialize: function() {
        this.always = _.bind(function() {this.outstandingRequests--;}, this);
      },
      getNumOutstandingRequests: function() {
        return this.outstandingRequests;
      },
      hardenedInterface : {
        request : "make a request to the API"
      }
    });

    Api.prototype.request = function(request, options) {

      options = _.extend({}, options, request.get('options'));
      
      var data,
          self = this,
          query = request.get('query');

      if (query && !(query instanceof ApiQuery)) {
        throw Error("Api.query must be instance of ApiQuery");
      }

      if (query) {
        data = options.contentType === "application/json" ? JSON.stringify(query.toJSON()) : query.url();
      }

      var target = request.get('target') || '';

      var u;
      if (target.indexOf('http') > -1) {
        u = target;
      }
      else {
        u = this.url + ((target.length > 0 && target.indexOf('/') == 0) ? target : (target ? '/' + target : target));
      }

      u = u.substring(0, this.url.length-2) + u.substring(this.url.length-2, u.length).replace('//', '/');

      if (!u) {
        throw Error("Sorry, you can't use api without url");
      }

      var opts = {
        type: 'GET',
        url: u,
        dataType: 'json',
        data: data,
        contentType: 'application/x-www-form-urlencoded',
        headers: {"X-BB-Api-Client-Version": this.clientVersion},
        context: {request: request, api: self },
        timeout: this.defaultTimeoutInMs,
        cache: true, // do not generate _ parameters (let browser cache responses),
        //need this so that cross domain cookies will work!
        xhrFields: {
          withCredentials: true // TODO: remove this (must be used only by certain widgets!!!)
        }
      };

      if (this.access_token) {
        opts.headers['Authorization'] = this.access_token;
      }

      //extend, rather than replace, the headers with user-supplied headers if any
      _.extend(opts.headers, options.headers);

      // one potential problem is that 'options' will override
      // whatever is set above (other than headers) (so if sb wants to shoot himself/herself,
      // we gave them the weapon... ;-))
      _.extend(opts, _.omit(options, "headers"));

      this.outstandingRequests++;

      var jqXhr = $.ajax(opts)
        .always(opts.always ? [this.always, opts.always] : this.always)
        .done(opts.done || this.done)
        .fail(opts.fail || this.fail);

      jqXhr = jqXhr.promise(jqXhr);

      return jqXhr;
    };

    _.extend(Api.prototype, Mixin.BeeHive);
    _.extend(Api.prototype, Hardened);


    return Api
  });

