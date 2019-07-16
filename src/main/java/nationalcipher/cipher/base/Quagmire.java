package nationalcipher.cipher.base;

import java.util.Arrays;

public class Quagmire {

    public static CharSequence encode(CharSequence plainText, String topKey, String gridKey, String indicatorKey, char indicatorBelow) {
        StringBuilder cipherText = new StringBuilder(plainText.length());

        String[] keyAlpha = new String[indicatorKey.length()];
        Arrays.fill(keyAlpha, "");

        for (int i = 0; i < indicatorKey.length(); i++) {
            for (int k = 0; k < 26; k++) {
                keyAlpha[i] += gridKey.charAt((26 - topKey.indexOf(indicatorBelow) + gridKey.indexOf(indicatorKey.charAt(i)) + k) % 26);
            }
        }
        
        for (int i = 0; i < plainText.length(); i++) {
            cipherText.append(keyAlpha[i % indicatorKey.length()].charAt(topKey.indexOf(plainText.charAt(i))));
        }

        return cipherText;
    }

    // Assuming the topKey is the ABCDEFGHIJKLMNPQRSTUVWXYZ
    public static char[] decode(CharSequence cipherText, char[] plainText, String gridKey, String indicatorKey, char indicatorBelow) {
        int indicatorIndex = indicatorBelow - 'A';

        int[][] keyAlpha = new int[indicatorKey.length()][26];

        for (int i = 0; i < indicatorKey.length(); i++)
            for (int k = 0; k < 26; k++)
                keyAlpha[i][gridKey.charAt((26 - indicatorIndex + gridKey.indexOf(indicatorKey.charAt(i)) + k) % 26) - 'A'] = k;

        for (int i = 0; i < cipherText.length(); i++) {
            plainText[i] = (char) (keyAlpha[i % indicatorKey.length()][cipherText.charAt(i) - 'A'] + 'A');
        }

        return plainText;
    }

    public static char[] decode(CharSequence cipherText, char[] plainText, String topKey, String gridKey, String indicatorKey, char indicatorBelow) {
        int indicatorIndex = topKey.indexOf(indicatorBelow);

        int[][] keyAlpha = new int[indicatorKey.length()][26];

        for (int i = 0; i < indicatorKey.length(); i++)
            for (int k = 0; k < 26; k++)
                keyAlpha[i][gridKey.charAt((26 - indicatorIndex + gridKey.indexOf(indicatorKey.charAt(i)) + k) % 26) - 'A'] += k;

        for (int i = 0; i < cipherText.length(); i++) {
            plainText[i] = (char) topKey.charAt(keyAlpha[i % indicatorKey.length()][cipherText.charAt(i) - 'A']);
        }

        return plainText;
    }
}
