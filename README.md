# Java / Spring Demo of idQ Authentication

This repository demonstrates how to integrate idQ authentication into your Java / Spring web application.

## Prerequisites
1. You need to have an idQ Developer account (https://beta.idquanta.com)
2. Login to your Account Portal and issue OAuth2 credentials for the demo app. See <https://docs.idquanta.com> for instructions on issuing OAuth2 credentials.
3. Specify http://localhost:8080/redirect as the Callback URL when creating your OAuth2 credentials.


## Usage
1. Clone this repository
2. Configure your OAuth2 Credentials in `src/main/resources/oauth2.properties`

```
clientID = "YOUR_CLIENT_ID"
clientSecret = "YOUR_CLIENT_SECRET"
redirectUrl = "YOUR_REDIRECT_URI"
baseUrl = "idQ Trust as a Service OAuth 2.0 endpoint, eg. https://beta.idquanta.com/idqoauth"
```

3. Execute `mvn jetty:run`
4. Open <http://localhost:8080>
