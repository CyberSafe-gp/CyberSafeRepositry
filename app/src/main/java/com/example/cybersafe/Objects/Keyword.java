package com.example.cybersafe.Objects;

public class Keyword {
    private String arabicKeyword;
    private String englishKeyword;



    public Keyword() {
    }

    public Keyword(String arabicKeyword, String englishKeyword){
        this.arabicKeyword = arabicKeyword;
        this.englishKeyword = englishKeyword;

    }

    public String getArabicKeyword() {
        return arabicKeyword;
    }

    public void setArabicKeyword(String arabicKeyword) {
        this.arabicKeyword = arabicKeyword;
    }



    public String getEnglishKeyword() {
        return englishKeyword;
    }

    public void setEnglishKeyword(String englishKeyword) {
        this.englishKeyword = englishKeyword;
    }



}
