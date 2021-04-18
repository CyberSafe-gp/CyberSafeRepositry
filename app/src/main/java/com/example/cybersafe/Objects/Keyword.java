package com.example.cybersafe.Objects;

public class Keyword {
    private  String Keyword_id;
    private String keyword;
    private String language;
    private String parent_id;




    public Keyword() {
    }

    public Keyword(String Keyword_id,String keyword, String language,String parent_id) {
        this.Keyword_id=Keyword_id;
        this.keyword = keyword;
        this.language = language;
        this.parent_id = parent_id;
    }
    public String getKeyword_id() {
        return Keyword_id;
    }

    public void setKeyword_id(String Keyword_id) {
        this.Keyword_id= Keyword_id;
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

    public String getParent_id() {
        return parent_id;
    }

   public void setParent_id(String parent_id) {
     this.parent_id = parent_id;

 }


}
