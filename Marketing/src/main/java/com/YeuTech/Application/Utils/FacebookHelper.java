package com.YeuTech.Application.Utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.YeuTech.Domain.Entities.SocialMediaConnection;

public class FacebookHelper {
    private static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/v25.0";

    public static boolean validateUserAccessToken(String userAccessToken, String pageId, RestTemplate restTemplate) {
        String url = String.format("%s/%s?fields=id&access_token=%s",
                FACEBOOK_GRAPH_API_URL, pageId, userAccessToken);
        try {
            restTemplate.getForEntity(url, Object.class);
            return true;
        } catch (HttpClientErrorException e) {
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean probeToken(SocialMediaConnection connection, RestTemplate restTemplate) {
        String url = String.format("%s/%s?fields=id&access_token=%s",
                FACEBOOK_GRAPH_API_URL,
                connection.getPageId(),
                decrypt(connection.getAccessToken()));
        try {
            restTemplate.getForEntity(url, Object.class);
            return true;
        } catch (HttpClientErrorException e) {
            // 190 = invalid/expired token
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static String encrypt(String raw) {
        return raw;
    }

    public static String decrypt(String encrypted) {
        return encrypted;
    }

    public static void publishToFacebook(String accessToken, String pageId, String message, RestTemplate restTemplate) {
        String url = FACEBOOK_GRAPH_API_URL + "/" + pageId + "/feed";

        System.out.println("Attempting to publish to Facebook Page: " + pageId);
        System.out.println("Using URL: " + url);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("message", message);
        body.add("access_token", FacebookHelper.decrypt(accessToken));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Facebook Publish Success: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Facebook Publish Failed: " + e.getMessage());
            throw new RuntimeException("Facebook publish failed: " + e.getMessage());
        }
    }

}
