package com.group21.NutriFit.ViewController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.reflect.Type;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.group21.NutriFit.Model.Nutrition;
import com.group21.NutriFit.utils.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class ScannerController extends DefaultController {
    @FXML
    private ImageView foodImage;
    @FXML
    private Button uploadImage;
    @FXML
    private Button choice1;
    @FXML
    private Button choice2;
    @FXML
    private Button choice3;

    private List<String> getFoodName(byte[] imageData) {
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

            if(response.statusCode()==404){
                showPopup("Unable to connect to Server!\nCheck your internet Connection!");
                return null;
            }
            String responseBody = response.body();
            

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
                    
                }
            } else {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Nutrition getNutritionalInfo(String itemName) {
        try {
            if(itemName == null){
                return null;
            }
            String encodedItemName = URLEncoder.encode(itemName, StandardCharsets.UTF_8);
            String url = "https://api.calorieninjas.com/v1/nutrition?query=" + encodedItemName;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Api-Key", "oDIUmoEDiktpC3gavRwpVQ==1SotOsLN1t5raqir") // Replace with your actual API key
                    .header("accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode()==404){
                showPopup("Unable to connect to Server!\nCheck your internet Connection!");
                return null;
            }

            String responseBody = response.body();

            System.out.println(responseBody);
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
                        int userID = getSharedData().getCurrentUser().getUserID();
                        int dateUnix = (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC); // Current Unix time as a placeholder
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

    @FXML
    protected void onUploadImageClick() {
        // Initialize FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Open file dialog
        File selectedFile = fileChooser.showOpenDialog(uploadImage.getScene().getWindow());

        if (selectedFile != null) {
            uploadImage.setVisible(false);
            try {
                // Load the selected image
                byte[] imageData = Files.readAllBytes(selectedFile.toPath());
                foodImage.setImage(new Image(selectedFile.toURI().toString()));
                foodImage.setFitHeight(535);
                foodImage.setFitWidth(535);

                // Call the API to get food names
                List<String> foodNames = getFoodName(imageData);

                // Check and display results
                if (foodNames != null && !foodNames.isEmpty()) {
                    System.out.println("Recognised items: " + foodNames);

                    choice1.setText(foodNames.size() > 0 ? foodNames.get(0) : "");
                    choice1.setVisible(foodNames.size() > 0);

                    choice2.setText(foodNames.size() > 1 ? foodNames.get(1) : "");
                    choice2.setVisible(foodNames.size() > 1);

                    choice3.setText(foodNames.size() > 2 ? foodNames.get(2) : "");
                    choice3.setVisible(foodNames.size() > 2);

                    choice1.setOnMouseClicked(event -> onChoiceClick(event, foodNames.get(0)));
                    choice2.setOnMouseClicked(event -> onChoiceClick(event, foodNames.get(1)));
                    choice3.setOnMouseClicked(event -> onChoiceClick(event, foodNames.get(2)));
                } else {
                    showPopup("No Items Recognised!");
                    switchScene("Scanner");
                }
            } catch (IOException e) {
                // Handle file reading errors
                showPopup("Unable to load File");
                switchScene("Scanner");
                e.printStackTrace();
            } catch (Exception e) {
                // Handle API or other unexpected errors
                showPopup("An error occurred. Please try again.");
                switchScene("Scanner");
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    protected void onChoiceClick(MouseEvent event, String foodName) {
        Database<Nutrition> nutritionDB = new Database<>("nutrition.dat");
        Nutrition nutritionInfo = getNutritionalInfo(foodName);

        if (nutritionInfo != null) {
            nutritionDB.add(getSharedData().getCurrentUser().getUserID(), nutritionInfo);
            nutritionDB.pushDataEncrypted((PublicKey) getSharedData().getEncryptionkeys().get("PublicKey"));

            // Construct a detailed message with nutritional information
            StringBuilder popupMessage = new StringBuilder();
            popupMessage.append(foodName).append(" added to the list\n")
                    .append("Calories: ").append(nutritionInfo.getCalorieIntake()).append("\n")
                    .append("Protein: ").append(nutritionInfo.getProtein()).append(" g\n")
                    .append("Carbohydrates: ").append(nutritionInfo.getCarbs()).append(" g\n")
                    .append("Fat: ").append(nutritionInfo.getFat()).append(" g\n")
                    .append("Fiber: ").append(nutritionInfo.getFiber()).append(" g\n");

            showPopup(popupMessage.toString());
            switchScene("Scanner");
        } else {
            showPopup("Unable to retrieve nutritional information for " + foodName);
        }
    }


}