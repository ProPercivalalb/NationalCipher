package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.util.ArrayUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.GrilleKeyType;
import nationalcipher.util.CharArrayWrapper;

public class GrilleCipher extends UniKeyCipher<Integer[], GrilleKeyType.Builder>{

    public GrilleCipher() {
        super(GrilleKeyType.builder().setRange(2, 8));
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, Integer[] key) {
        int blockSize = key.length * 4;

        if (plainText.length() % blockSize != 0) {
            StringBuilder builder = new StringBuilder(plainText.length() + blockSize - (plainText.length() % blockSize));
            builder.append(plainText);
            while (builder.length() % blockSize != 0) {
                builder.append('X');
            }

            return builder;
        } else {
            return plainText;
        }
    }

    @Override
    public CharSequence encode(CharSequence plainText, Integer[] key, IFormat format) {
        int blockSize = key.length * 4;
        int size = (int) Math.sqrt(blockSize + (key.length + 1) % 2);
        int[] fullKey = createFullKey(key, size);
        
        if(plainText.length() != blockSize) {
            StringBuilder builder = new StringBuilder(plainText.length());
            for(int i = 0; i < plainText.length(); i += blockSize) {
                builder.append(encodeSection(plainText, i, blockSize, size, fullKey));
            }

            return builder;
        }
        else {
            return encodeSection(plainText, 0, blockSize, size, fullKey);
        }
    }
    
    public static CharSequence encodeSection(CharSequence plainText, int start, int blockSize, int size, int[] fullKey) {
        int middleIndex = (size * size - 1) / 2;
        char[] cipherText = new char[blockSize];
        for(int i = 0; i < blockSize; i++) {
            int index = fullKey[i];
            if(index > middleIndex && size % 2 == 1)
                index--;
            cipherText[index] = plainText.charAt(i + start);
        }
            
        return new CharArrayWrapper(cipherText);
    }
    
    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, Integer[] key) {
        int blockSize = key.length * 4;
        int size = (int) Math.sqrt(blockSize + (key.length + 1) % 2);
        int[] fullKey = createFullKey(key, size);
        
        if(cipherText.length() != blockSize) {
            for(int i = 0; i < cipherText.length(); i += blockSize) {
                plainText = decodeSection(cipherText, plainText, i, blockSize, size, fullKey);
            }
        }
        else {
            plainText = decodeSection(cipherText, plainText, 0, blockSize, size, fullKey);
        }
        
        return plainText;
    }
    
    public static char[] decodeSection(CharSequence cipherText, char[] plainText, int start, int blockSize, int size, int[] fullKey) {
        int middleIndex = (size * size - 1) / 2;
        for(int i = 0; i < size * size; i++) {
            if(i == middleIndex && size % 2 == 1) continue;
            int index = i;
            if(index > middleIndex && size % 2 == 1)
                index--;
            plainText[start + ArrayUtil.indexOf(fullKey, i)] = cipherText.charAt(start + index);
        }
            
        return plainText;
    }
    
    public static int[] createFullKey(Integer[] key, int size) {

        int[] normal = new int[key.length * 4];
        for(int i = 0; i < key.length; i++)
            normal[i] = key[i];

        for(int rot = 1; rot < 4; rot++) {
            for(int i = 0; i < key.length; i++) {
                int value = normal[(rot - 1) * key.length + i];
                int row = value / size;
                int col = value % size;
                normal[rot * key.length + i] = col * size + (size-1 - row);
            }
        }
        
        int[] ordered = new int[normal.length];
        //Orders each quadrant
        for(int rot = 0; rot < 4; rot++) {
            int count = 0;
            for(int i = 0; i < size * size; i++) {
                if(ArrayUtil.contains(normal, rot * key.length, (rot + 1) * key.length, i)) {
                    ordered[rot * key.length + count++] = i;
                }
            }
        }
        
        return ordered;
    }
}
