package com.example.cybersafe.Objects;

public class Parent {
    private String firstName;
    private String lastName;
    //private String password;
    private String email;
    private String parent_id;
    private String token;

    public Parent() {
    }

    public Parent(String firstName, String lastName, String email, String parent_id, String token) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.parent_id = parent_id;
        this.token = token;
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

    //public String getPassword() {
      //  return password;
   // }

   // public void setPassword(String password) {
    //    this.password = password;
    //}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
