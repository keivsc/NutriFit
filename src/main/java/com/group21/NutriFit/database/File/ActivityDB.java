package com.group21.NutriFit.database.File;

import com.group21.NutriFit.database.Database;

import java.security.PublicKey;

public class ActivityDB<Activity> implements Database<Activity> {
    private String filePath = "./users.dat";

    @Override
    public boolean add(int ID, Activity item, PublicKey publicKey) {
        return false;
    }

    @Override
    public boolean remove(int ID) {
        return false;
    }

}
