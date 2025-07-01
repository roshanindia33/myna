package com.myna.myna;

public class User {
    public String name;
    public String email;

    // Firebase  default constructor
    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Optional: Getter & Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
