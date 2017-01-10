package com.inovatis.inovatis.models;

/**
 * Created by Lud' on 19/09/2016.
 *
 */



public class ServerRequest {

    private String operation;
    private User user;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }
}