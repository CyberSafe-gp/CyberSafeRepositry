package com.example.cybersafe.Objects;

public class Keyword {
    private String keyword;
    private String language;



    public Keyword() {
    }

    public Keyword(String keyword, String language) {
        this.keyword = keyword;
        this.language = language;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
