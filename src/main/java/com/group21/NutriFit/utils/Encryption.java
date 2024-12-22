package com.group21.NutriFit.utils;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import static com.group21.NutriFit.utils.Utils.base64Encode;

public class Encryption {

    private static String generateSalt() {
        FileProcessor fileProcessor = new FileProcessor();
        fileProcessor.createFile("salt.dat", false);  // Ensure file is created if it doesn't exist
        List<String> data = new ArrayList<>(fileProcessor.readFile("salt.dat"));

        if (!data.isEmpty() && data.size() > 3 && !data.get(3).isEmpty()) {
            // Return the existing salt if it exists in line 4 (index 3)
            return data.get(3);
        } else {
            // Generate a new salt if the file is empty or doesn't contain enough data
            byte[] saltBytes = new byte[16]; // 16 bytes = 128 bits
            SecureRandom random = new SecureRandom();
            random.nextBytes(saltBytes);
            String newSalt = Base64.getEncoder().encodeToString(saltBytes);

            data.add(newSalt);

            // Save the new salt back into the file
            fileProcessor.writeFile("salt.dat", base64Encode(String.join("\n", data)));
            return newSalt;
        }
    }

    public static void generateKeys(String email, String password) throws Exception {
        generateSalt();
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        FileProcessor fileProcessor = new FileProcessor();
        List<String> data = new ArrayList<>(fileProcessor.readFile("keys.dat"));
        data.set(1, data.get(1)+", "+email);
        data.add(encryptKey(email, password, publicKey.toString())+"[br]"+encryptKey(email, password, privateKey.toString()));
        fileProcessor.writeFileAsync("keys.dat", base64Encode(String.join("\n", data)));
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static String encrypt(PublicKey publicKey, String data) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(PrivateKey privateKey, String data) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)), StandardCharsets.UTF_8);
    }

    public static PublicKey stringToPublic(String publicKeyString) throws Exception{
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }

    private static PrivateKey stringToPrivate(String privateKeyString) throws Exception{
        byte[] privateKeyBytes = convertStringToByteArray(privateKeyString);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(privateKeySpec);
    }

    private static String encryptKey(String email, String password, String strToEncrypt) {
        try {
            IvParameterSpec ivspec = new IvParameterSpec(new byte[16]);  // Default IV filled with 0s
            SecretKeySpec secretKey = new SecretKeySpec(
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                            .generateSecret(new PBEKeySpec(password.toCharArray(), email.getBytes(), 65536, 256))
                            .getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            // Encrypt the string and return Base64 encoded result
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Encryption error: " + e);
        }
        return null;
    }

    private static String decryptKey(String email, String password, String strToDecrypt) {
        try {
            // Initialize AES decryption cipher with CBC mode and PKCS5 padding
            IvParameterSpec ivspec = new IvParameterSpec(new byte[16]);  // Default IV filled with 0s
            SecretKeySpec secretKey = new SecretKeySpec(
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                            .generateSecret(new PBEKeySpec(password.toCharArray(), email.getBytes(), 65536, 256))
                            .getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

            // Decrypt and return the result as a string
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Decryption error: " + e);
        }
        return null;
    }

    public static String hashText(String text) {

        KeySpec spec = new PBEKeySpec(text.toCharArray(), generateSalt().getBytes(), 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            String hashed =Arrays.toString(factory.generateSecret(spec).getEncoded());
            return base64Encode(hashed);
        }catch(NoSuchAlgorithmException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }

    public static byte[] convertStringToByteArray(String byteArrayString) {
        // Validate input
        if (byteArrayString == null || byteArrayString.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty.");
        }

        // Remove surrounding characters, if they are brackets
        if (byteArrayString.startsWith("[") && byteArrayString.endsWith("]")) {
            byteArrayString = byteArrayString.substring(1, byteArrayString.length() - 1);
        }

        // Split the string by comma and space
        String[] byteStrings = byteArrayString.split(",\\s*");

        // Create a byte array of the appropriate length
        byte[] byteArray = new byte[byteStrings.length];

        try {
            // Convert each string into a byte value
            for (int i = 0; i < byteStrings.length; i++) {
                int intValue = Integer.parseInt(byteStrings[i]);
                if (intValue < -128 || intValue > 255) {
                    throw new NumberFormatException("Value out of byte range: " + intValue);
                }
                byteArray[i] = (byte) intValue;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse byte array string: " + byteArrayString, e);
        }

        return byteArray;
    }

    public static Map<String, Object> parseKeys(String key){
        String[] keys = key.split("[br]");
        Map<String, Object> encryptionMap = new HashMap<>();
        try {
            encryptionMap.put("PublicKey", stringToPublic(keys[0]));
            encryptionMap.put("PrivateKey", stringToPublic(keys[1]));
        } catch (Exception e) {
            return null;
        }
        return encryptionMap;
    }

}
