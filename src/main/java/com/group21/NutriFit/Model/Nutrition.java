package com.group21.NutriFit.Model;

import java.io.Serializable;

public class Nutrition extends BaseModel<Nutrition>{
    private int nutritionID;
    private int target;
    private double calorieIntake;
    private double protein;  // Grams of protein
    private double fiber;    // Grams of fiber
    private double fat;      // Grams of fat
    private double carbs;    // Grams of carbs
    private int dateUnix;

    // Constructor
    public Nutrition(int nutritionID, int userID, double calorieIntake, double protein, double fiber, double fat, double carbs, int dateUnix) {
        super(userID);  // Call to BaseModel constructor
        this.nutritionID = nutritionID;
        this.calorieIntake = calorieIntake;
        this.protein = protein;
        this.fiber = fiber;
        this.fat = fat;
        this.carbs = carbs;
        this.dateUnix = dateUnix;
    }

    // Getters and setters
    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getNutritionID() {
        return nutritionID;
    }

    public void setNutritionID(int nutritionID) {
        this.nutritionID = nutritionID;
    }

    public double getCalorieIntake() {
        return calorieIntake;
    }

    public void setCalorieIntake(double calorieIntake) {
        this.calorieIntake = calorieIntake;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public int getDateUnix() {
        return dateUnix;
    }

    public void setDateUnix(int dateUnix) {
        this.dateUnix = dateUnix;
    }

    // toString method to represent the Nutrition object as a string
    @Override
    public String toString() {
        return "Nutrition{" +
                nutritionID + ", " +
                userID + ", " +
                calorieIntake + ", " +
                protein + ", " +
                fiber + ", " +
                fat + ", " +
                carbs + ", " +
                dateUnix +
                '}';
    }

    // fromString method to parse a string into a Nutrition object
    public static Nutrition fromString(String string) {
        try {
            // Clean the string by removing the "Nutrition{" and "}" part
            String cleanedString = string.replace("Nutrition{", "").replace("}", "").trim();

            // Split the string into individual fields
            String[] data = cleanedString.split(", ");

            // Parse each field
            int nutritionID = Integer.parseInt(data[0].trim());
            int userID = Integer.parseInt(data[1].trim());
            double calorieIntake = Double.parseDouble(data[2].trim());
            double protein = Double.parseDouble(data[3].trim());
            double fiber = Double.parseDouble(data[4].trim());
            double fat = Double.parseDouble(data[5].trim());
            double carbs = Double.parseDouble(data[6].trim());
            int dateUnix = Integer.parseInt(data[7].trim());

            // Return a new Nutrition object
            return new Nutrition(nutritionID, userID, calorieIntake, protein, fiber, fat, carbs, dateUnix);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in input string: " + string, e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing fields in input string: " + string, e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Nutrition from string: " + string, e);
        }
    }
}