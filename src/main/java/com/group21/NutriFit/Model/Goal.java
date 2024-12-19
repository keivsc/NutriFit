package com.group21.NutriFit.Model;

public class Goal extends BaseModel<Goal> {
    private int goalID;
    private int typeID;
    private double target;
    private int deadlineUnix;

    // Constructor to initialize all fields
    public Goal(int UserID, int goalID, int typeID, double target, int deadlineUnix) {
        super(UserID);
        this.goalID = goalID;
        this.typeID = typeID;
        this.target = target;
        this.deadlineUnix = deadlineUnix;
    }

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

    public int getDeadlineUnix() {
        return deadlineUnix;
    }

    public void setDeadlineUnix(int deadlineUnix) {
        this.deadlineUnix = deadlineUnix;
    }

    @Override
    public String toString() {
        return "Goal{" +
                userID + ", " +
                goalID + ", " +
                typeID + ", " +
                target + ", " +
                deadlineUnix +
                '}';
    }

    public static Goal fromString(String string) {
        try {
            // Remove "Goal{" and "}" from the input string
            String cleanedString = string.replace("Goal{", "").replace("}", "").trim();

            // Split the cleaned string into fields
            String[] data = cleanedString.split(", ");

            // Parse and construct the Goal object
            int UserID = Integer.parseInt(data[0].trim());
            int goalID = Integer.parseInt(data[1].trim());
            int typeID = Integer.parseInt(data[2].trim());
            double target = Double.parseDouble(data[3].trim());
            int deadlineUnix = Integer.parseInt(data[4].trim());

            // Use the correct constructor to create and return the Goal object
            return new Goal(UserID, goalID, typeID, target, deadlineUnix);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in input string: " + string, e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing fields in input string: " + string, e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Goal from string: " + string, e);
        }
    }
}