package com.inbaytech.oauth2.client.demo.idq;

/*
  idQ Client
  Copyright(c) 2017 inBay Technologies Inc.
  MIT Licensed
 */

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.OAuth;

/**
 * IdqClient implements a basic client for the idQ TaaS Backend API
 * for explicit authentication flow.
 */
public class IdqClient {

    // OAuth 2.0 Client Configuration
    private String clientId;
    private String clientSecret;
    private String redirectUrl;


    // idQ TaaS Backend Configuration
    private String oauthBaseUrl;
    private String oauthAuthzUrl;
    private String oauthTokenUrl;
    private String oauthUserUrl;


    // Initialize OAuth 2.0 Client Configuration
    public void initOauthClient(String clientId, String clientSecret, String redirectUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
    }

    // Initialize idQ TaaS Backend Configuration
    public void initOauthServer(String oauthBaseUrl) {
        this.oauthBaseUrl = oauthBaseUrl;
        String AUTH_PATH = "/api/v1/auth";
        this.oauthAuthzUrl = this.oauthBaseUrl + AUTH_PATH;
        String TOKEN_PATH = "/api/v1/token";
        this.oauthTokenUrl = this.oauthBaseUrl + TOKEN_PATH;
        String RESOURCE_PATH = "/api/v1/user";
        this.oauthUserUrl = this.oauthBaseUrl + RESOURCE_PATH;
    }

    /**
     * Build a link to an idQ Authentication URL
     * using the configured OAuth 2.0 client credentials
     * and a given state.
     * @param oauthState
     * @param oauthScope
     * @return authzReqUrl
     * @throws OAuthSystemException
     */
    public String getAuthzUrl(String oauthState, String oauthScope) throws OAuthSystemException {
        OAuthClientRequest oauthRequest = OAuthClientRequest
                .authorizationLocation(this.oauthAuthzUrl)
                .setClientId(this.clientId)
                .setRedirectURI(this.redirectUrl)
                .setState(oauthState)
                .setScope(oauthScope)
                .setResponseType(ResponseType.CODE.toString())
                .buildQueryMessage();
        return oauthRequest.getLocationUri();
    }

    /**
     * Exchange an authorization code for an access token
     * using the configured OAuth 2.0 client credentials
     * and a given authorization_code.
     * @param authzCode
     * @return accessToken
     * @throws OAuthSystemException
     */
    public String getToken(String authzCode) throws OAuthSystemException, OAuthProblemException {
        OAuthClientRequest oauth2request = OAuthClientRequest
                .tokenLocation(oauthTokenUrl)
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectURI(redirectUrl)
                .setCode(authzCode)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .buildBodyMessage();

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(oauth2request,
                OAuthJSONAccessTokenResponse.class);
        return oAuthResponse.getAccessToken();
    }

    /**
     * Exchange an access token for an idQ user info
     * using the configured OAuth 2.0 client credentials
     * and a given access token.
     * @param accessToken
     * @return userInfo
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    public String getUserInfo(String accessToken) throws OAuthSystemException, OAuthProblemException {
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(oauthUserUrl)
                .setAccessToken(accessToken)
                .buildQueryMessage();

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest,
                OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        return resourceResponse.getBody();
    }

}