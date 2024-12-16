package com.group21.NutriFit.database.File;

import com.group21.NutriFit.Model.Goal;
import com.group21.NutriFit.database.Database;

import java.security.PublicKey;

public class GoalDB implements Database<Goal> {
    private String filePath = "./users.dat";


    @Override
    public boolean add(int ID, Goal item, PublicKey publicKey) {
        return false;
    }
}
