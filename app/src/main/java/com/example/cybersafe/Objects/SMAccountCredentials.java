package com.example.cybersafe.Objects;

public class SMAccountCredentials {
    private String id;
    private String child_id;
    private String socialMediaPlatform;
    private String password;
    private String account;


    public SMAccountCredentials() {
    }

    public SMAccountCredentials(String id, String child_id, String socialMediaPlatform, String password, String account) {
        this.id = id;
        this.child_id = child_id;
        this.socialMediaPlatform = socialMediaPlatform;
        this.password = password;
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getSocialMediaPlatform() {
        return socialMediaPlatform;
    }

    public void setSocialMediaPlatform(String socialMediaPlatform) {
        this.socialMediaPlatform = socialMediaPlatform;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }














}
