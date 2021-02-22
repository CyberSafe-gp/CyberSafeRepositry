package com.example.cybersafe.Objects;

public class Child {

    private String child_id;
    private String parent_id;
    private String school_id;
    private String firstName;
    private String lastName;
    private String date_of_birth;
    private String city;
    private String gender;
    private String grade;


    public Child(String child_id, String parent_id, String school_id, String firstName, String lastName, String date_of_birth, String city, String gender, String grade) {
        this.child_id = child_id;
        this.parent_id = parent_id;
        this.school_id = school_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date_of_birth = date_of_birth;
        this.city = city;
        this.gender = gender;
        this.grade = grade;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
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

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
