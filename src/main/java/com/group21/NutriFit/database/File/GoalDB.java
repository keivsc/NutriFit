package com.group21.NutriFit.database.File;

import com.group21.NutriFit.Model.Goal;
import com.group21.NutriFit.database.Database;

public class GoalDB<Goal> implements Database<Goal> {
    private String filePath = "./users.dat";


    @Override
    public boolean add(Goal item) {
        return false;
    }

    @Override
    public boolean remove(Goal item) {
        return false;
    }
}
