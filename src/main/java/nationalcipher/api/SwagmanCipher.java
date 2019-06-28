package nationalcipher.api;

import javax.annotation.Nullable;

import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.SwagmanKeyType;

public class SwagmanCipher extends UniKeyCipher<int[]> {

    public SwagmanCipher() {
        super(SwagmanKeyType.builder().setRange(2, 5).create());
    }

    @Override
    public Character[] padPlainText(Character[] plainText, int[] key) {
        int size = (int)Math.sqrt(key.length);
        int numOver = plainText.length % size;
        if(numOver == 0) {
            return plainText;
        } else {
            int numToPad = size - numOver;
            Character[] paddedText = new Character[plainText.length + numToPad];
            System.arraycopy(plainText, 0, paddedText, 0, plainText.length);
            for(int i = 0; i < numOver; i++) {
                paddedText[plainText.length + i] = 'X';
            }
            
            return paddedText;
        }
    }
    
    @Override
    public String padPlainText(String plainText, int[] key) {
        int size = (int)Math.sqrt(key.length);
        StringBuilder builder =  new StringBuilder(plainText.length() + (size - (plainText.length() % size)) % size);
        builder.append(plainText);
        while(builder.length() % size != 0) {
            builder.append('X');
        }
        
        return builder.toString();
    }
    
    @Override
    public Character[] encode(Character[] plainText, int[] key, IFormat format) {
        int size = (int)Math.sqrt(key.length);
        
        char[] tempText = new char[plainText.length];
        Character[] cipherText = new Character[plainText.length];
        
        int squareMag = key.length;
        int rowLength = plainText.length / size;
        int noSquares = (int)Math.ceil(plainText.length / (double)squareMag);
        
        int[] colInSquare = new int[noSquares];
        for(int i = 0; i < noSquares; i++) colInSquare[i] = size;
        if(plainText.length % squareMag != 0) colInSquare[noSquares - 1] = (plainText.length % squareMag) / size;

        for(int s = 0; s < noSquares; s++)
            for(int r = 0; r < size; r++)
                for(int c = 0; c < colInSquare[s]; c++)
                    tempText[s * squareMag + c + colInSquare[s] * key[r * size + c % size]] = plainText[s * size + r * rowLength + c];
    

        for(int s = 0; s < noSquares; s++)
            for(int r = 0; r < size; r++)
                for(int c = 0; c < colInSquare[s]; c++)
                    cipherText[s * squareMag + c * size + r] = tempText[s * squareMag + c + colInSquare[s] * r];
        
        return cipherText;
    }
    
    @Override
    public byte[] decodeEfficently(byte[] cipherText, @Nullable byte[] plainText, int[] key) {
        int size = (int)Math.sqrt(key.length);
        
        int[] inKey = new int[key.length];
        for(int c = 0; c < size; c++) for(int r = 0; r < size; r++) inKey[key[r * size + c] * size + c] = r;
        
        byte[] tempText = new byte[cipherText.length];
        
        int squareMag = (int)Math.pow(size, 2);
        int noSquares = (int)Math.ceil(cipherText.length / (double)squareMag);
        
        int[] colInSquare = new int[noSquares];
        for(int i = 0; i < noSquares; i++) colInSquare[i] = size;
        if(cipherText.length % squareMag != 0) colInSquare[noSquares - 1] = (cipherText.length % squareMag) / size;
        
        
        for(int s = 0; s < noSquares; s++)
            for(int r = 0; r < size; r++)
                for(int c = 0; c < colInSquare[s]; c++)
                    tempText[s * squareMag + c + colInSquare[s] * inKey[r * size + c % size]] = (byte)cipherText[s * squareMag + c * size + r];
        
        int i = 0;

        for(int r = 0; r < size; r++)
            for(int s = 0; s < noSquares; s++)
                for(int c = 0; c < colInSquare[s]; c++)
                    plainText[i++] = tempText[s * squareMag + c + r * colInSquare[s]];
        
        
        return plainText;
    }

}
