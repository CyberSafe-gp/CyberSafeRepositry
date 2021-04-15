package com.example.cybersafe.Objects;

public class SMAccountCredentials {
    private String id;
    private String child_id;
    private String socialMediaPlatform;
    private String account;
    private String access_token;
    private String Author_id;


    public SMAccountCredentials() {
    }


    public SMAccountCredentials(String id, String child_id, String socialMediaPlatform, String account, String access_token, String author_id) {
        this.id = id;
        this.child_id = child_id;
        this.socialMediaPlatform = socialMediaPlatform;
        this.account = account;
        this.access_token = access_token;
        Author_id = author_id;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }



    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAuthor_id() {
        return Author_id;
    }

    public void setAuthor_id(String author_id) {
        Author_id = author_id;
    }


}
