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
        System.out.println(System.lineSeparator()+System.lineSeparator()+System.lineSeparator()+System.lineSeparator());
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
                            if (parts.length <= 2 || !cipher.isValid(key = cipher.parseKey(line.substring(parts[0].length() + parts[1].length() + 2)))) {
                                String help = cipher.getHelp();
                                
                                if (help == null) {
                                    System.out.println("Key not valid");
                                } else {
                                    System.out.println("key format is: " + help);
                                }
                                continue;
                            }
                        } catch (ParseException e) {
                            System.out.println("Unable to parse cipher key: " + e.getMessage());
                            continue;
                        }
                        
                        System.out.print("Enter Text: ");
                        String cipherText = cipher.encode(inputBuffer.readLine(), key);
                        System.out.println(cipherText);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
