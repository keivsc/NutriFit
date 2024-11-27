package com.group21.NutriFit.Model;

import java.io.Serializable;

public class Goal implements Serializable {
    private int goalID;
    private int typeID;
    private double target;
    private int datelineUnix;

    public int getGoalID() {
        return goalID;
    }

    public void setGoalID(int goalID) {
        this.goalID = goalID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public int getDatelineUnix() {
        return datelineUnix;
    }

    public void setDatelineUnix(int datelineUnix) {
        this.datelineUnix = datelineUnix;
    }
}
