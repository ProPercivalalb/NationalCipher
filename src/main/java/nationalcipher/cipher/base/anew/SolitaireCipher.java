package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;
import nationalcipher.cipher.decrypt.solitaire.Solitaire;

public class SolitaireCipher extends UniKeyCipher<Integer[]> {

    public SolitaireCipher() {
        super(OrderedIntegerKeyType.builder().setRange(54, 54)); // TODO add joker checks
    }

    @Override
    public CharSequence encode(CharSequence plainText, Integer[] key, IFormat format) {
        String cipherText = "";
        int index = 0;

        while (index < plainText.length()) {
            key = Solitaire.nextCardOrder(key);

            int topCard = key[0];
            int keyStreamNumber;

            if (!Solitaire.isJoker(topCard))
                keyStreamNumber = key[topCard + 1];
            else
                keyStreamNumber = key[key.length - 1];

            if (Solitaire.isJoker(keyStreamNumber))
                continue;

            cipherText += (char) (((plainText.charAt(index) - 'A') + (keyStreamNumber + 1)) % 26 + 'A');
            index += 1;
        }

        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, Integer[] key) {
        int index = 0;

        for (int i = 0; i < index; i++)
            plainText[i] = cipherText.charAt(i);

        while (index < cipherText.length()) {

            key = Solitaire.nextCardOrder(key);

            int topCard = key[0];
            int keyStreamNumber;
            // System.out.println("Top card" + topCard);
            if (!Solitaire.isJoker(topCard))
                keyStreamNumber = key[topCard + 1];
            else
                keyStreamNumber = key[key.length - 1];

            // System.out.println(keyStreamNumber);
            if (Solitaire.isJoker(keyStreamNumber))
                continue;

            plainText[index] = (char) ((52 + (cipherText.charAt(index) - 'A') - (keyStreamNumber + 1)) % 26 + 'A');
            index += 1;
        }

        return plainText;
    }
}
