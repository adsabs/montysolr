Orcid module
============

This module has been developed by blocshop.cz, with support of AAS. The module itself has
*been substantially remodelled* by ADS (to actually work well; here my rant should end...eh?)

Documentation
=============

Initially, there was none...

ORCID is using 3-legged OAuth: which means the user has to be redirected to ORCID,
authenticate with ORCID and authorize 'ADS application' to gain access to user's
records.

 - Originally, this mechanism is implemented through a separate window (orcidLoginContainer.html)
 - When authenticated, ORCID redirects to /oauthRedirect.html
 - which in turn passes code to /oauth/exchangeOauthCode; to exchange code for access token
   (this method is implemented in server.js)

Here a link to page which describes authentication example with ORCID:

   http://support.orcid.org/knowledgebase/articles/179969-methods-to-generate-an-access-token-for-testing

Basically, for ADS it goes like this:

   - curl -i -c cookies.txt -d 'userId=roman.chyla@gmail.com' -d 'password=Orcid123' 'https://sandbox.orcid.org/signin/auth.json'
   - curl -i -c cookies.txt -b cookies.txt 'https://sandbox.orcid.org/oauth/authorize?client_id=APP-P5ANJTQRRTMA6GXZ&response_type=code&scope=/orcid-profile/read-limited%20/orcid-works/create%20/orcid-works/update&redirect_uri=http://localhost:8000/oauthRedirect.html'
   - curl -X POST -c cookies.txt -b cookies.txt -i -H "Content-Type: application/json;charset=UTF-8" --data "@authorize.json" https://sandbox.orcid.org/oauth/custom/authorize.json

   the contents of the authorize.json file:

   {
    "errors": [],
    "userName": {
        "errors": [],
        "value": "",
        "required": true,
        "getRequiredMessage": null
    },
    "password": {
        "errors": [],
        "value": "",
        "required": true,
        "getRequiredMessage": null
    },
    "clientId": {
        "errors": [],
        "value": "APP-P5ANJTQRRTMA6GXZ",
        "required": true,
        "getRequiredMessage": null
    },
    "redirectUri": {
        "errors": [],
        "value": "http://localhost:8000/oauthRedirect.html",
        "required": true,
        "getRequiredMessage": null
    },
    "scope": {
        "errors": [],
        "value": "/orcid-profile/read-limited /orcid-works/create /orcid-works/update",
        "required": true,
        "getRequiredMessage": null
    },
    "responseType": {
        "errors": [],
        "value": "code",
        "required": true,
        "getRequiredMessage": null
    },
    "approved": true,
    "persistentTokenEnabled": true
  }


  response should be st like:

  {"errors":[],"userName":{"errors":[],"value":"","required":true,"getRequiredMessage":null},"password":{"errors":[],"value":"","required":true,"getRequiredMessage":null},"clientId":{"errors":[],"value":"APP-P5ANJTQRRTMA6GXZ","required":true,"getRequiredMessage":null},"redirectUri":{"errors":[],"value":"http://localhost:8000/oauthRedirect.html?code=bG0GoE","required":true,"getRequiredMessage":null},"scope":{"errors":[],"value":"/orcid-profile/read-limited /orcid-works/create /orcid-works/update","required":true,"getRequiredMessage":null},"responseType":{"errors":[],"value":"code","required":true,"getRequiredMessage":null},"approved":true,"persistentTokenEnabled":true}


  - curl -i -L -H 'Accept: application/json' --data 'client_id=APP-P5ANJTQRRTMA6GXZ&client_secret=989e54c8-7093-4128-935f-30c19ed9158c&grant_type=authorization_code&code=bG0GoE&redirect_uri=http://localhost:8000/oauthRedirect.html' 'https://api.sandbox.orcid.org/oauth/token'

  response is:

  {"access_token":"ea12a66a-2a07-49fc-b0cb-9d0967c484c8","token_type":"bearer","expires_in":3599,"scope":"/orcid-works/create /orcid-profile/read-limited /orcid-works/update","orcid":"0000-0001-8178-9506","name":"Roman Chyla"}


  then with the access_token, we can start doing stuff:

  - search: curl -H 'Content-Type: application/json' -H 'Authorization: Bearer ea12a66a-2a07-49b-9d0967c484c8' -H 'Accept: application/json'  'https://api.sandbox.orcid.org/v1.1/search/orcid-bio/?q=ads'


Alternative, obtain permanent access token (this can be good only for testing, it must be the ADS testing account):
==========================================

curl -i -L -H 'Accept: application/json' --data 'client_id=APP-P5ANJTQRRTMA6GXZ&client_secret=989e54c8-7093-4128-935f-30c19ed9158c&grant_type=client_credentials&scope=/orcid-profile/read-limited%20/orcid-works/create%20/orcid-works/update' 'https://api.sandbox.orcid.org/oauth/token'

{"access_token":"8f314d8d-928f-4df1-b276-5be866eac3b2","token_type":"bearer","expires_in":3599,"scope":"/activities/read-limited /activities/update /affiliations/create /affiliations/read-limited /affiliations/update /authenticate /funding/create /funding/read-limited /funding/update /orcid-bio/external-identifiers/create /orcid-bio/read-limited /orcid-bio/update /orcid-profile/read-limited /orcid-works/create /orcid-works/read-limited /orcid-works/update /person/read-limited /person/update /read-public","orcid":null}
