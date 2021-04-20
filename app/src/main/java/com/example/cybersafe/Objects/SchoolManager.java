package com.example.cybersafe.Objects;

public class SchoolManager {

    private String schoolManager_id;
    private String school_id;
    private String firstName;
    private String lastName;
    private String email;
    private String City;
    private String admin;
    private String token;



    public SchoolManager() {
    }

    public SchoolManager(String schoolManager_id, String school_id, String firstName, String lastName,String email, String city, String admin, String token) {
        this.schoolManager_id = schoolManager_id;
        this.school_id = school_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        City = city;
        this.admin = admin;
        this.token = token;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String schoolManager_id) {
        this.admin = admin; }

    public String getSchoolManager_id() {
        return schoolManager_id;
    }

    public void setSchoolManager_id(String schoolManager_id) {
        this.schoolManager_id = schoolManager_id; }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
