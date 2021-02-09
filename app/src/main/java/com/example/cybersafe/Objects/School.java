package com.example.cybersafe.Objects;

public class School {

    private String school_id;
    private String child_id;
    private String schoolName;
    private String email;


    public School() {
    }

    public School(String school_id, String child_id, String schoolName, String email){
        this.school_id = school_id;
        this.child_id = child_id;
        this.schoolName = schoolName;
        this.email = email;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }



    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
