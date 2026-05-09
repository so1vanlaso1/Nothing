package com.YeuTech.Application.Services;

// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;

// import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.YeuTech.Application.Utils.FacebookHelper;
import com.YeuTech.Domain.Entities.SocialMediaConnection;
import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Enums.SocialPlatform;
import com.YeuTech.Domain.Repository.ISocialMediaConnectionRepository;
import com.YeuTech.Dtos.ApiResponseFormat;
// import java.util.UUID;
// import java.time.LocalDateTime;

@Service
public class FacebookService implements IFacebookService {
    private final RestTemplate restTemplate;
    private final ISocialMediaConnectionRepository _socialMediaConnectionRepository;
    private final IUserService _userService;
    private static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/v25.0";

    public FacebookService(RestTemplate restTemplate,
            ISocialMediaConnectionRepository socialMediaConnectionRepository,
            IUserService userService) {
        this.restTemplate = restTemplate;
        this._socialMediaConnectionRepository = socialMediaConnectionRepository;
        this._userService = userService;
    }

    @Override
    public String getUserPages(String accessToken) {
        String url = FACEBOOK_GRAPH_API_URL + "/me?access_token=" + FacebookHelper.decrypt(accessToken);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Facebook get pages failed: " + response.getBody());
        }
        return response.getBody();
    }

    @Override
    public void publishToFacebookByUserId(String userId, String message) {
        var connection = _socialMediaConnectionRepository.findByUserIdAndPlatform(userId, SocialPlatform.FACEBOOK)
                .orElseThrow(() -> new RuntimeException("Facebook connection not found for user: " + userId));

        if (connection.getAccessToken() == null || connection.getPageId() == null) {
            throw new RuntimeException("Facebook connection incomplete for user: " + userId);
        }

        FacebookHelper.publishToFacebook(connection.getAccessToken(), connection.getPageId(), message, restTemplate);
    }

    @Override
    public ApiResponseFormat<Object> getPageInfo(String email) {
        User user = _userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        var connectionOpt = _socialMediaConnectionRepository.findByUserIdAndPlatform(user.getId(),
                SocialPlatform.FACEBOOK);
        if (connectionOpt.isEmpty()) {
            return new ApiResponseFormat<>(404, "No Facebook connection found. Please complete the OAuth flow first.",
                    null);
        }

        SocialMediaConnection connection = connectionOpt.get();
        String url = String.format("%s/%s?fields=id,name,picture,link,about&access_token=%s",
                FACEBOOK_GRAPH_API_URL, connection.getPageId(), FacebookHelper.decrypt(connection.getAccessToken()));

        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            return new ApiResponseFormat<>(200, "Page info retrieved successfully", response.getBody());
        } catch (Exception e) {
            return new ApiResponseFormat<>(400, "Failed to get Facebook page info: " + e.getMessage(), null);
        }
    }

    private ApiResponseFormat<Object> fetchPageInfo(SocialMediaConnection socialMediaConnection) {
        String url = String.format(
                "%s/%s?fields=id,name,picture,link,about&access_token=%s",
                FACEBOOK_GRAPH_API_URL,
                socialMediaConnection.getPageId(),
                FacebookHelper.decrypt(socialMediaConnection.getAccessToken()));

        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
        return new ApiResponseFormat<>(200, "Page info retrieved successfully", response.getBody());
    }
}
