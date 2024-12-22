package com.group21.NutriFit.Model;

public class Activity extends BaseModel<Activity> {
    private int activityID;
    private int userID;  // Added userID
    private int typeID;
    private int duration;
    private double caloriesBurned;
    private int dateUnix;

    // Constructor
    public Activity(int activityID, int userID, int typeID, int duration, double caloriesBurned, int dateUnix) {
        super(userID);  // Call to BaseModel constructor
        this.activityID = activityID;
        this.typeID = typeID;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.dateUnix = dateUnix;
    }

    // Getters and setters
    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public int getDateUnix() {
        return dateUnix;
    }

    public void setDateUnix(int dateUnix) {
        this.dateUnix = dateUnix;
    }

    // toString method to represent the Activity object as a string
    @Override
    public String toString() {
        return "Activity{" +
                activityID + ", " +
                userID + ", " +
                typeID + ", " +
                duration + ", " +
                caloriesBurned + ", " +
                dateUnix +
                '}';
    }

    // fromString method to parse a string into an Activity object

    public static Activity fromString(String string) {
        try {
            // Clean the string by removing the "Activity{" and "}" part
            String cleanedString = string.replace("Activity{", "").replace("}", "").trim();

            // Split the string into individual fields
            String[] data = cleanedString.split(", ");

            // Parse each field
            int activityID = Integer.parseInt(data[0].trim());
            int userID = Integer.parseInt(data[1].trim());
            int typeID = Integer.parseInt(data[2].trim());
            int duration = Integer.parseInt(data[3].trim());
            double caloriesBurned = Double.parseDouble(data[4].trim());
            int dateUnix = Integer.parseInt(data[5].trim());

            // Return a new Activity object
            Activity newActivity = new Activity(activityID, userID, typeID, duration, caloriesBurned, dateUnix);
            System.out.println(newActivity);
            return newActivity;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in input string: " + string, e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing fields in input string: " + string, e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Activity from string: " + string, e);
        }
    }
}