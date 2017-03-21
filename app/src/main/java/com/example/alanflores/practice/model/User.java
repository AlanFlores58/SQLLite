package com.example.alanflores.practice.model;

/**
 * Created by alan.flores on 1/12/17.
 */

public class User {

    String username;

    String password;

    String id;

    public User(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
