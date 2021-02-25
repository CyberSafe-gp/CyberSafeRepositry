package com.example.cybersafe.Objects;

public class School {

    private String school_id;
    private String schoolName;
    private String city;



    public School() {
    }

    public School(String school_id, String schoolName, String city) {
        this.school_id = school_id;
        this.schoolName = schoolName;
        this.city = city;

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }


}
