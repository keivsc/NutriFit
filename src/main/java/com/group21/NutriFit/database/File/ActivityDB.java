package com.group21.NutriFit.database.File;

import com.group21.NutriFit.Model.Activity;
import com.group21.NutriFit.database.Database;

public class ActivityDB<Activity> implements Database<Activity> {
    private String filePath = "./users.dat";


    @Override
    public void load() {

    }

    @Override
    public boolean add(Activity item) {
        return false;
    }

    @Override
    public boolean remove(Activity item) {
        return false;
    }

}
