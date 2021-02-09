package com.example.cybersafe.Objects;

public class SMAccountCredentials {
    private String child_id;
    private String socialMediaPlatform;
    private String password;
    private String email;


    public SMAccountCredentials() {
    }

    public SMAccountCredentials(String child_id, String socialMediaPlatform, String password, String email) {
        this.child_id = child_id;
        this.socialMediaPlatform = socialMediaPlatform;
        this.password = password;
        this.email = email;

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



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
