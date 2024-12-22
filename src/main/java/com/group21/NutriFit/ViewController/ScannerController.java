package com.group21.NutriFit.ViewController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group21.NutriFit.Model.Nutrition;

public class ScannerController extends DefaultController {

    private static List<String> getFoodName(byte[] imageData) {
        try {
            String url = "https://api.logmeal.com/v2/image/segmentation/complete/v1.0?language=eng";

            String boundary = "Boundary-" + System.currentTimeMillis();
            String contentType = "multipart/form-data; boundary=" + boundary;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            String bodyHeader = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"image\"; filename=\"image.jpeg\"\r\n"
                    + "Content-Type: image/jpeg\r\n\r\n";

            outputStream.write(bodyHeader.getBytes(StandardCharsets.UTF_8));
            outputStream.write(imageData);
            String bodyFooter = "\r\n--" + boundary + "--\r\n";
            outputStream.write(bodyFooter.getBytes(StandardCharsets.UTF_8));

            byte[] body = outputStream.toByteArray();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", contentType)
                    .header("Authorization", "Bearer 8afc7d3d5023b2036e01dd56995bce7e60523c87")
                    .header("accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println("Response Body: " + responseBody);

            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> map = gson.fromJson(responseBody, mapType);

            // Access segmentation_results
            List<String> recognisedItems = new ArrayList<>();

            List<Map<String, Object>> segmentationResults = (List<Map<String, Object>>) map.get("segmentation_results");
            if (segmentationResults != null && !segmentationResults.isEmpty()) {
                Map<String, Object> firstSegmentationResult = segmentationResults.get(0);

                // Access recognition_results
                List<Map<String, Object>> recognitionResults =
                        (List<Map<String, Object>>) firstSegmentationResult.get("recognition_results");

                if (recognitionResults != null) {
                    recognitionResults.forEach(data -> {
                        String name = (String) data.get("name");
                        recognisedItems.add(name);
                    });
                    return recognisedItems;
                } else {
                    System.out.println("No recognition results found.");
                }
            } else {
                System.out.println("No segmentation results found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Nutrition getNutritionalInfo(String itemName) {
        try {
            String url = "https://api.calorieninjas.com/v1/nutrition?query=" + itemName;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Api-Key", "YOUR_API_KEY_HERE") // Replace with your actual API key
                    .header("accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println("Nutrition Response Body: " + responseBody);

            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> nutritionMap = gson.fromJson(responseBody, mapType);

            if (nutritionMap != null && nutritionMap.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) nutritionMap.get("items");
                for (Map<String, Object> item : items) {
                    String name = (String) item.get("name");
                    if (name.equalsIgnoreCase(itemName)) {
                        double calories = ((Number) item.get("calories")).doubleValue();
                        double carbohydrates = ((Number) item.get("carbohydrates_total_g")).doubleValue();
                        double protein = ((Number) item.get("protein_g")).doubleValue();
                        double fat = ((Number) item.get("fat_total_g")).doubleValue();
                        double fatSaturated = ((Number) item.get("fat_saturated_g")).doubleValue();
                        double sugar = ((Number) item.get("sugar_g")).doubleValue();
                        double fiber = ((Number) item.get("fiber_g")).doubleValue();
                        double sodium = ((Number) item.get("sodium_mg")).doubleValue();
                        double potassium = ((Number) item.get("potassium_mg")).doubleValue();
                        double cholesterol = ((Number) item.get("cholesterol_mg")).doubleValue();

                        // Assuming you are passing userID and dateUnix as required by the Nutrition constructor
                        int nutritionID = 0; // Set your actual nutritionID
                        int userID = 0; // Set your actual userID
                        int dateUnix = (int) (System.currentTimeMillis() / 1000); // Current Unix time as a placeholder

                        return new Nutrition(nutritionID, userID, calories, protein, fiber, fat, carbohydrates, dateUnix);
                    }
                }
            }
            return null;  // Return null if no match is found
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getImageData(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file: " + filePath, e);
        }
    }
}