package com.YeuTech.Domain.Entities;

public class UserProfile {
    private String userId;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String config;

    public UserProfile() {
    }

    public UserProfile(String userId, String firstName, String lastName, String config) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.config = config;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
