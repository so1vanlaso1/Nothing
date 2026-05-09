package com.YeuTech.Application.Services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.YeuTech.Domain.Entities.SocialMediaConnection;
import com.YeuTech.Domain.Repository.ISocialMediaConnectionRepository;
import com.YeuTech.Domain.Exceptions.UserTokenExpiredException;

@Service
public class TokenRefreshService implements ITokenRefreshService {
    private final RestTemplate restTemplate;
    private final ISocialMediaConnectionRepository _socialMediaConnectionRepository;

    @Value("${facebook.app.id}")
    private String appId;

    @Value("${facebook.app.secret}")
    private String appSecret;

    @Value("${facebook.token.encryption-key}")
    private String encryptionKey;

    private static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/v25.0";

    public TokenRefreshService(
            RestTemplate restTemplate,
            ISocialMediaConnectionRepository socialMediaConnectionRepository) {
        this.restTemplate = restTemplate;
        this._socialMediaConnectionRepository = socialMediaConnectionRepository;
    }

    @Override
    public String exchangeForLongLivedToken(String shortLivedToken) {
        if (shortLivedToken == null || shortLivedToken.isBlank()) {
            throw new IllegalArgumentException("shortLivedToken must not be blank");
        }

        String url = String.format(
                "%s/oauth/access_token?grant_type=fb_exchange_token" +
                        "&client_id=%s&client_secret=%s&fb_exchange_token=%s",
                FACEBOOK_GRAPH_API_URL,
                appId,
                appSecret,
                shortLivedToken);

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Token exchange failed: empty or non-2xx response");
            }

            String longLivedToken = (String) response.getBody().get("access_token");
            if (longLivedToken == null || longLivedToken.isBlank()) {
                throw new RuntimeException("Token exchange failed: access_token missing in response");
            }

            return longLivedToken;

        } catch (HttpClientErrorException e) {
            throw new RuntimeException(
                    "Token exchange failed with Facebook error: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public String refreshPageAccessToken(SocialMediaConnection connection) {
        validateConnection(connection);

        String decryptedUserToken = decrypt(connection.getLongLivedUserToken());

        String url = String.format(
                "%s/%s?fields=access_token&access_token=%s",
                FACEBOOK_GRAPH_API_URL,
                connection.getPageId(),
                decryptedUserToken);

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Could not refresh page access token: empty or non-2xx response");
            }

            String pageToken = (String) response.getBody().get("access_token");
            if (pageToken == null || pageToken.isBlank()) {
                throw new RuntimeException("Could not refresh page access token: access_token missing in response");
            }

            persistPageToken(connection, pageToken);

            return pageToken;

        } catch (HttpClientErrorException e) {
            if (e.getResponseBodyAsString().contains("\"code\": 190")) {
                throw new UserTokenExpiredException("Facebook user access token has expired or is invalid.", e);
            }
            throw new RuntimeException(
                    "Page token refresh failed with Facebook error: " + e.getResponseBodyAsString(), e);
        }
    }

    private void validateConnection(SocialMediaConnection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("SocialMediaConnection must not be null");
        }
        if (connection.getPageId() == null || connection.getPageId().isBlank()) {
            throw new IllegalStateException("Connection has no pageId — cannot refresh token");
        }
        if (connection.getLongLivedUserToken() == null ||
                connection.getLongLivedUserToken().isBlank()) {
            throw new IllegalStateException(
                    "No long-lived user token stored for this connection. " +
                            "The user must complete the OAuth flow again.");
        }
    }

    private void persistPageToken(SocialMediaConnection connection, String rawPageToken) {
        connection.setAccessToken(encrypt(rawPageToken));
        connection.setTokenExpiryDate(null);
        _socialMediaConnectionRepository.save(connection);
    }

    private String encrypt(String raw) {
        return raw;
    }

    private String decrypt(String encrypted) {
        return encrypted;
    }
}
