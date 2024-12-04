package com.group21.NutriFit.utils;
import java.io.*;

public class Utils {

    private static File openFile(String filePath){
        return new File(filePath);
    }

    private static void appendFile(String filePath, String data){
        try(FileWriter fw = new FileWriter(openFile(filePath), true);){
            for (int i = 0; i < data.length(); i++)
                fw.write(data.charAt(i));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void overrideFile(String filePath, String data){
        try(FileWriter fw = new FileWriter(openFile(filePath), false);){
            for (int i = 0; i < data.length(); i++)
                fw.write(data.charAt(i));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
