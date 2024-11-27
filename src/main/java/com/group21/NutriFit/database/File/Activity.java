package com.group21.NutriFit.database.File;

import com.group21.NutriFit.database.Database;

public class Activity extends Database {
    private String filePath = "./users.txt";

    @Override
    public void load() {

    }

    @Override
    public boolean add() {
        return false;
    }

    @Override
    public boolean remove() {
        return false;
    }
}
