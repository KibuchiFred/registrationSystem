package com.grocery.demo.ExceptionHandling;


public class UserNameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7676650724247116842L;

    //getting the error message
    public UserNameNotFoundException(String message) {
        super(message);
    }
}
