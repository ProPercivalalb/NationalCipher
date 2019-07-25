package nationalcipher.cipher.base.anew;

import nationalcipher.api.ICipher;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.base.keys.VariableStringKeyType.Builder;

public class StraddleCheckerboardCipher extends UniKeyCipher<String, VariableStringKeyType.Builder> {

    public StraddleCheckerboardCipher(Builder firstKey) {
        super(firstKey);
        // TODO Auto-generated constructor stub
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        // TODO Auto-generated method stub
        return null;
    }

}
