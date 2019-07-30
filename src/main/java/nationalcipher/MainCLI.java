package nationalcipher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import nationalcipher.api.ICipher;
import nationalcipher.registry.EncrypterRegistry;

public class MainCLI {

    public static void main(String[] args) {
        EncrypterRegistry.registerAll();
        System.out.print("Realm (e.g. /): ");
        BufferedReader inputBuffer = (new BufferedReader(new InputStreamReader(System.in)));
        String line = null;
        try {
            while ((line = inputBuffer.readLine()) != null) {
                String[] parts = line.split(" ");
                
                if (parts[0].equals("encode")) {
                    ICipher cipher = EncrypterRegistry.getFromName(parts[1].toLowerCase());
                    if (cipher == null) {
                        System.out.println(String.format("%s not recongised", parts[1]));
                    } else {
                        Object key = null;
                        try {
                            key = cipher.parseKey(line.substring(parts[0].length() + parts[1].length() + 2));
                            
                            if (!cipher.isValid(key)) {
                                System.out.println("Key not valid");
                                continue;
                            }
                        } catch (ParseException e) {
                            System.out.println("Unable to parse cipher key: " + e.getMessage());
                            continue;
                        }
                        String cipherText = cipher.encode("TESTSTRINGMAN", key);
                        System.out.println(cipherText);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
