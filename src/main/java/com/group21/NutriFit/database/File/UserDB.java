package com.group21.NutriFit.database.File;

import com.group21.NutriFit.Model.User;
import com.group21.NutriFit.database.Database;

public class UserDB<User> implements Database<User> {
    private String filePath = "./users.dat";

    @Override
    public void load() {

    }

    @Override
    public boolean add(User item) {
        return false;
    }

    @Override
    public boolean remove(User item) {
        return false;
    }
}
