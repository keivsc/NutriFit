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

    public static PublicKey generateKeys(String email, String password) throws Exception {
        // Generate the key pair (RSA or another algorithm)
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Initialize the file processor to read/write keys
        FileProcessor fileProcessor = new FileProcessor();
        List<String> data = new ArrayList<>(fileProcessor.readFile("keys.dat"));

        // Append the email to the second element in the data list
        data.set(1, data.get(1) + ", " + email);

        // Convert the keys to Base64 encoded strings before encrypting
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        // Encrypt the keys (the Base64 encoded key data is what is encrypted here)
        String encryptedPublicKey = encryptKey(email, password, publicKeyBase64);
        String encryptedPrivateKey = encryptKey(email, password, privateKeyBase64);

        // Append the encrypted keys to the data list
        data.add(encryptedPublicKey + "[br]" + encryptedPrivateKey);

        // Write the data to the file asynchronously
        fileProcessor.writeFileAsync("keys.dat", base64Encode(String.join("\n", data)));

        return publicKey; // Returning the public key
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

    public static PrivateKey stringToPrivate(String privateKeyString) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString); // Decode the Base64 string
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Get RSA key factory
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes); // Create the key specification
        return keyFactory.generatePrivate(privateKeySpec); // Generate and return the PrivateKey
    }



    public static String encryptKey(String email, String password, String strToEncrypt) {
        try {
            // Generate a deterministic IV from the email
            IvParameterSpec ivspec = new IvParameterSpec(generateIV(email));

            // Generate the secret key using PBKDF2
            SecretKeySpec secretKey = new SecretKeySpec(
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                            .generateSecret(new PBEKeySpec(password.toCharArray(), email.getBytes(StandardCharsets.UTF_8), 65536, 256))
                            .getEncoded(), "AES");

            // Initialize AES encryption cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            // Encrypt and return Base64-encoded ciphertext
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptKey(String email, String password, String strToDecrypt) {
        try {
            // Generate a deterministic IV from the email
            IvParameterSpec ivspec = new IvParameterSpec(generateIV(email));

            // Generate the secret key using PBKDF2
            SecretKeySpec secretKey = new SecretKeySpec(
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                            .generateSecret(new PBEKeySpec(password.toCharArray(), email.getBytes(StandardCharsets.UTF_8), 65536, 256))
                            .getEncoded(), "AES");

            // Initialize AES decryption cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

            // Decrypt and return plaintext
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] generateIV(String email) throws Exception {
        // Derive a 16-byte IV deterministically from the email using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(email.getBytes(StandardCharsets.UTF_8));
        byte[] iv = new byte[16];
        System.arraycopy(hash, 0, iv, 0, 16); // Use the first 16 bytes of the hash as the IV
        return iv;
    }


    public static Map<String, Object> parseKeys(String key, String email, String password){
        String[] keys = key.split("\\[br]");
        Map<String, Object> encryptionMap = new HashMap<>();
        try {
            encryptionMap.put("PublicKey", stringToPublic(Objects.requireNonNull(decryptKey(email, password, keys[0]))));
            encryptionMap.put("PrivateKey", stringToPrivate(Objects.requireNonNull(decryptKey(email, password, keys[1]))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptionMap;
    }


}
