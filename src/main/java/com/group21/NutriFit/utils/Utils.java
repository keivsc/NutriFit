package com.group21.NutriFit.utils;
import java.io.*;
import java.util.Base64;

public class Utils {

    public static String base64Encode(String text){
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public static String base64Decode(String text){
        return new String(Base64.getDecoder().decode(text));
    }

}
