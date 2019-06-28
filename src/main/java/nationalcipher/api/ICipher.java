package nationalcipher.api;

import javax.annotation.Nullable;

import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;

public interface ICipher<K> extends IKeyType<K> {

    /**
     * Alters the plaintext so that the cipher can be applied to it
     * E.g.
     *  Padding the text so it is a multiple of N (Hill Cipher)
     *  Making sure there is no double letters (Playfair Cipher)
     */
    default Character[] padPlainText(Character[] plainText, K key) {
        return plainText;
    }
    
    default String padPlainText(String plainText, K key) {
        return plainText;
    }
    
	Character[] encode(Character[] plainText, K key, IFormat format);
	
	default Character[] decode(Character[] cipherText, K key) {
	    return ArrayUtil.convertCharObj(decodeEfficently(ArrayUtil.convertCharType(cipherText), key));
	}
	
	/**
	 * Used in cipher solvers for the most memory and speed efficiency code
	 */
	default byte[] decodeEfficently(byte[] cipherText, @Nullable byte[] plainText, K key) {
	    return new byte[0];
	}
	
	default byte[] decodeEfficently(byte[] cipherText, K key) {
        return decodeEfficently(cipherText, new byte[cipherText.length], key);
    }
	
	default Character[] encode(Character[] plainText, K key) {
		return encode(plainText, key, null);
	}
	
	
	
	
	default String encode(String plainText, K key) {
		return PrimTypeUtil.toString(encode(padPlainText(PrimTypeUtil.toCharacterArray(plainText), key), key, null));
	}
	
	default String decode(String cipherText, K key) {
		return PrimTypeUtil.toString(decode(PrimTypeUtil.toCharacterArray(cipherText), key));
	}
	
	default String randomEncode(String plainText) {
	    return this.encode(plainText, this.randomise());
	}
	
	default Difficulty getDifficulty() {
	    return Difficulty.EASY;
	}
	
	enum Difficulty {
	    EASY,
	    NORMAL,
	    HARD;
	}
}
