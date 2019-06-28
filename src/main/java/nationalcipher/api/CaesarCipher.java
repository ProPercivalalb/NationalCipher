package nationalcipher.api;

import javax.annotation.Nullable;

import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.util.CipherUtils;

public class CaesarCipher extends UniKeyCipher<Integer> {

	public CaesarCipher() {
        super(IntegerKeyType.builder().setRange(0, 25).create());
    }

    @Override
	public Character[] encode(Character[] plainText, Integer key, IFormat format) {
		Character[] cipherText = new Character[plainText.length];
		int i;
		for(i = 0; i < plainText.length;) {
		    byte ch = CipherUtils.getAlphaIndex((char)plainText[i]);
			if(ch == -1) {
				//if(format)
				//	cipherText[i++] = ch;
			} else {
				char newLetter = (char)(((ch + key) % 26) + 'A');
				cipherText[i++] = newLetter;
			}
		}
		
		return ArrayUtil.copyRange(cipherText, 0, i);
	}

	@Override
	public byte[] decodeEfficently(byte[] cipherText, @Nullable byte[] plainText, Integer key) {
		for(int i = 0; i < cipherText.length; i++) {
			byte ch = CipherUtils.getAlphaIndex((char)cipherText[i]);
			if(ch == -1)
				plainText[i] = ch;
			else {
				byte newLetter = (byte)(((ch + 26 - key) % 26) + 'A');
				plainText[i] = newLetter;
			}
		}
		
		return plainText;
	}

}
