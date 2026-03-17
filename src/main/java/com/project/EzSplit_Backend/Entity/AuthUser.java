package com.project.EzSplit_Backend.Entity;

public class AuthUser {

    private String email;
    private String providerId;

    public AuthUser(String email, String providerId){
        this.email = email;
        this.providerId = providerId;
    }

    public String getEmail(){ return email; }
    public String getProviderId(){ return providerId; }
}