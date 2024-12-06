package com.group21.NutriFit.utils;
import java.io.*;
import java.util.Base64;

public class Utils {

    private static File openFile(String filePath){
        return new File(filePath);
    }

    public static void appendFile(String filePath, String data){
        try(FileWriter fw = new FileWriter(openFile(filePath), true);){
            for (int i = 0; i < data.length(); i++)
                fw.write(data.charAt(i));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void overrideFile(String filePath, String data) throws IOException {
        try (FileWriter fw = new FileWriter(filePath, false)) { // Directly use filePath
            fw.write(data); // Write the data in one go
        }
    }

    public static String readFile(String filePath) throws FileNotFoundException, IOException{
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            StringBuilder data= new StringBuilder();
            while((line = reader.readLine()) != null){
                data.append(line);
            }
            return data.toString();
        }
    }

    public static String base64Encode(String text){
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public static String base64Decode(String text){
        return new String(Base64.getDecoder().decode(text));
    }

}
