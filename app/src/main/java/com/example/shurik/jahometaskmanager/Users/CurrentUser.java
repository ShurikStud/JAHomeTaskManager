package com.example.shurik.jahometaskmanager.Users;

public class CurrentUser {

    private static CurrentUser instance;

    String email;
    String password;
    String description;


    private CurrentUser(){};

    public static synchronized CurrentUser getInstance() {
        if (instance == null){
            instance = new CurrentUser();
        }
        return instance;
    }

    public void init(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
