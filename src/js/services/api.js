

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
    'js/mixins/api_access',
    'moment'
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
    ApiAccess,
    Moment
    ) {

    var Api = GenericModule.extend({
      url: '/api/1/', // usually overriden during app bootstrap
      clientVersion: null,
      outstandingRequests: 0,

      access_token: null,
      refresh_token: null,
      expire_in: null,
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

      //used by api_access.js
      setVals : function(obj){
        _.each(obj, function(v,k){
          this[k] = v;
        }, this);
      },

      /**
       * Before executing an ajax request, this will be passed
       * the options and can modify them. Typically, clients
       * make want to provide their own implementation.
       *
       * @param opts
       */
      modifyRequestOptions: function(opts) {
        // do nothing
      },

      hardenedInterface : {
        request : "make a request to the API",
        setVals : "set a value on API (such as new access token)"
      }
    });

    Api.prototype._request = function(request, options){

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
        context: {request: request, api: self },
        timeout: this.defaultTimeoutInMs,
        headers: {},
        cache: true // do not generate _ parameters (let browser cache responses),
      };

      if (options.timeout) {
        opts.timeout = options.timeout;
      }

      if (this.clientVersion) {
        opts.headers['X-BB-Api-Client-Version'] = this.clientVersion;
      }

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

      this.modifyRequestOptions(opts, request);

      var jqXhr = $.ajax(opts)
        .always(opts.always ? [this.always, opts.always] : this.always)
        .done(opts.done || this.done)
        .fail(opts.fail || this.fail);

      jqXhr = jqXhr.promise(jqXhr);

      return jqXhr;

    };

    //stubbable for testing
    Api.prototype.getCurrentUTCMoment = function(){
      return Moment().utc();
    };

    Api.prototype.request = function(request, options) {

      var that = this;

      if (!this.expire_in) return that._request(request, options);

      //expire_in is in UTC, not local time
      var expiration = Moment.utc(this.expire_in);
      var now = this.getCurrentUTCMoment();

      var difference = now.diff(expiration, 'minutes');
      //fewer than 2 minutes before token expires
      if (difference > -2 ){
        var d = $.Deferred();
        this.getApiAccess({ tokenRefresh: true }).done(function(){
          d.resolve(that._request(request, options));
        });
        return d.promise();
      }
      else {
        return that._request(request, options);
      }

    };

    _.extend(Api.prototype, Mixin.BeeHive);
    _.extend(Api.prototype, Hardened);
    _.extend(Api.prototype, ApiAccess);



    return Api
  });
