package com.example.cybersafe.Objects;

public class SchoolManager {

    private String schoolManager_id;
    private String school_id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String City;


    public SchoolManager() {
    }

    public SchoolManager(String schoolManager_id, String school_id,String City, String firstName, String lastName, String password, String email) {
        this.schoolManager_id = schoolManager_id;
        this.school_id = school_id;
        this.City = City;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;


    }


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
