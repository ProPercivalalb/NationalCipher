package nationalcipher.cipher.base.anew;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

//TODO
public class RunningKeyCipher extends UniKeyCipher<String> {

    public RunningKeyCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS));
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        return null;
    }

}
