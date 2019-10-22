package com.grocery.demo.Model;

import javax.persistence.EnumType;

public enum UserType {

    CLIENT ("Client"),
    SUPPLIER ("Supplier"),
    ADMIN ("Admin");

    private final String type;

    UserType (String userType){

        this.type = userType;

    }

    public String getType() {

        return this.type;
    }
    public String setType() {

        return this.type;
    }




}

