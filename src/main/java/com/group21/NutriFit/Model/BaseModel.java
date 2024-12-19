package com.group21.NutriFit.Model;

import java.io.Serializable;

public abstract class BaseModel<T> implements Serializable {
    protected int userID;


    public BaseModel(int id){
        this.userID = id;
    }

    public int getUserID(){
        return this.userID;
    }

    public abstract String toString();

    public static Object fromString(String string) {
        return null;
    }
}
