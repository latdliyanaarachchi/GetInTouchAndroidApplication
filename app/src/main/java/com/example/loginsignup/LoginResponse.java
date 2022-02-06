package com.example.loginsignup;

public class LoginResponse {

    Long id;
    Boolean userAvailable;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getUserAvailable() {
        return userAvailable;
    }

    public void setUserAvailable(Boolean userAvailable) {
        this.userAvailable = userAvailable;
    }
}
