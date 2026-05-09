package com.YeuTech.Application.Services;

import com.YeuTech.Domain.Entities.SocialMediaConnection;

public interface ITokenRefreshService {
    /**
     * Exchanges a short-lived Facebook user token (~1-2h) for a long-lived one (~60
     * days).
     * Should be called once during the OAuth callback and the result persisted.
     *
     * @param shortLivedToken the token received from the OAuth redirect
     * @return a long-lived user access token string
     */
    String exchangeForLongLivedToken(String shortLivedToken);

    /**
     * Derives a non-expiring Page Access Token from the stored long-lived user
     * token.
     * Safe to call repeatedly — always overwrites the stored page token on success.
     *
     * @param connection the persisted SocialMediaConnection containing the
     *                   long-lived user token
     * @return a fresh Page Access Token string
     */
    String refreshPageAccessToken(SocialMediaConnection connection);
}
