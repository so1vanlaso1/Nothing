package com.YeuTech.Application.Services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.YeuTech.Application.Utils.FacebookHelper;
import com.YeuTech.Domain.Entities.SocialMediaConnection;
import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Enums.SocialPlatform;
import com.YeuTech.Domain.Repository.ISocialMediaConnectionRepository;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.FacebookTokenUpdateRequest;
import com.YeuTech.Dtos.TokenStatusDto;

@Service
public class FacebookConfigService implements IFacebookConfigService {

        private final IUserService _userService;
        private final ISocialMediaConnectionRepository _socialMediaConnectionRepository;
        private final ITokenRefreshService _tokenRefreshService;
        private final RestTemplate restTemplate;

        public FacebookConfigService(
                        IUserService userService,
                        ISocialMediaConnectionRepository socialMediaConnectionRepository,
                        ITokenRefreshService tokenRefreshService,
                        RestTemplate restTemplate) {
                this._userService = userService;
                this._socialMediaConnectionRepository = socialMediaConnectionRepository;
                this._tokenRefreshService = tokenRefreshService;
                this.restTemplate = restTemplate;
        }

        // checks if stored token is valid or expired
        @Override
        public ApiResponseFormat<TokenStatusDto> getTokenStatus(String email) {
                User user = _userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found: " + email));

                // no connection in db
                var connectionOpt = _socialMediaConnectionRepository
                                .findByUserIdAndPlatform(user.getId(), SocialPlatform.FACEBOOK);

                if (connectionOpt.isEmpty()) {
                        return new ApiResponseFormat<>(200, "No Facebook connection found",
                                        TokenStatusDto.notConnected());
                }

                SocialMediaConnection connection = connectionOpt.get();
                boolean canAutoRefresh = connection.getLongLivedUserToken() != null
                                && !connection.getLongLivedUserToken().isBlank();

                // check if token is still valid by probing the Graph API
                boolean tokenValid = FacebookHelper.probeToken(connection, restTemplate);

                if (tokenValid) {
                        return new ApiResponseFormat<>(200, "Token is valid",
                                        TokenStatusDto.valid(
                                                        connection.getPageId(),
                                                        connection.getTokenExpiryDate(),
                                                        canAutoRefresh));
                } else {
                        return new ApiResponseFormat<>(200, "Token is expired",
                                        TokenStatusDto.expired(connection.getPageId(), canAutoRefresh));
                }
        }

        @Override
        public ApiResponseFormat<Void> updateAccessToken(String email, FacebookTokenUpdateRequest dto) {
                User user = _userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found: " + email));

                SocialMediaConnection connection = _socialMediaConnectionRepository
                                .findByUserIdAndPlatform(user.getId(), SocialPlatform.FACEBOOK)
                                .orElse(null);

                if (connection == null) {
                        connection = new SocialMediaConnection();
                        connection.setId(java.util.UUID.randomUUID().toString());
                        connection.setUserId(user.getId());
                        connection.setPlatform(SocialPlatform.FACEBOOK);
                        connection.setCreatedDate(LocalDateTime.now());
                        connection.setUpdatedDate(LocalDateTime.now());
                }

                connection.setPageId(dto.pageId());

                String tokenToStore = dto.accessToken();

                if (dto.exchangeForLongLived()) {
                        boolean userTokenValid = FacebookHelper.validateUserAccessToken(dto.accessToken(), dto.pageId(),
                                        restTemplate);
                        if (!userTokenValid) {
                                return new ApiResponseFormat<>(400,
                                                "Your Facebook access token has expired. Please get a new token from Facebook Graph API Explorer and try again.",
                                                null);
                        }

                        String longLivedToken = _tokenRefreshService.exchangeForLongLivedToken(dto.accessToken());
                        connection.setLongLivedUserToken(FacebookHelper.encrypt(longLivedToken));

                        tokenToStore = _tokenRefreshService.refreshPageAccessToken(connection);
                }

                // Validate before saving
                boolean isValid = FacebookHelper.validateUserAccessToken(tokenToStore, connection.getPageId(),
                                restTemplate);
                if (!isValid) {
                        throw new RuntimeException(
                                        "Token validation failed — the provided token is invalid or the page ID is wrong.");
                }

                connection.setAccessToken(FacebookHelper.encrypt(tokenToStore));
                connection.setTokenExpiryDate(null); // page tokens not expire
                _socialMediaConnectionRepository.save(connection);

                return new ApiResponseFormat<>(200, "Facebook access token updated successfully", null);
        }

        @Override
        public ApiResponseFormat<Void> refreshToken(String email) {
                User user = _userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found: " + email));

                SocialMediaConnection connection = _socialMediaConnectionRepository
                                .findByUserIdAndPlatform(user.getId(), SocialPlatform.FACEBOOK)
                                .orElseThrow(() -> new RuntimeException("No Facebook connection found."));

                if (connection.getLongLivedUserToken() == null
                                || connection.getLongLivedUserToken().isBlank()) {
                        return new ApiResponseFormat<>(400,
                                        "Cannot auto-refresh: no long-lived user token stored. " +
                                                        "Please reconnect your Facebook account.",
                                        null);
                }

                _tokenRefreshService.refreshPageAccessToken(connection);

                return new ApiResponseFormat<>(200, "Facebook token refreshed successfully", null);
        }
}
