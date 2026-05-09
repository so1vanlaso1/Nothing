package com.YeuTech.Domain.Exceptions;

/**
 * Thrown when a Facebook long-lived user access token has expired or been
 * revoked.
 * The user must complete the OAuth flow again to obtain a new token.
 */
public class UserTokenExpiredException extends RuntimeException {

    public UserTokenExpiredException(String message) {
        super(message);
    }

    public UserTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
