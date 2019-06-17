package nationalcipher.cipher.transposition;

import java.util.List;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;

public class RouteTransposition implements IRandEncrypter {

	public static void main(String[] args) {
		//System.out.println(new String(decode("JEEISRIGTATSCSHSMESEA".toCharArray(), 7, 3, RouteWritten.DOWN, Routes.OTHER_LETTER)));
	}
	
	public static String encode(String plainText, int columns, int rows, RouteCipherType writtenOn, RouteCipherType readOff) {

		//Create pattern
		int[] gridWrite = writtenOn.getPattern(columns, rows, plainText.length());
		int[] gridRead = readOff.getPattern(columns, rows, plainText.length());
				
		//Reads across the grid
		char[] gridString = new char[plainText.length()];
		for(int i = 0; i < plainText.length(); i++)
			gridString[gridWrite[i]] = plainText.charAt(i);
		
		char[] gridString2 = new char[plainText.length()];
		for(int i = 0; i < plainText.length(); i++)
			gridString2[i] = gridString[gridRead[i]];
		
		return new String(gridString2);
	}
	
	public static byte[] decode(char[] cipherText, int columns, int rows, RouteCipherType writtenOn, RouteCipherType readOff) {

		int length = cipherText.length;
		
		//Create pattern
		int[] gridWrite = writtenOn.getPattern(columns, rows, length);
		int[] gridRead = readOff.getPattern(columns, rows, length);

		byte[] gridString = new byte[length];
		for(int i = 0; i < length; i++)
			gridString[gridRead[i]] = (byte)cipherText[i];
				
		byte[] gridString2 = new byte[length];
		for(int i = 0; i < length; i++)
			gridString2[i] = gridString[gridWrite[i]];
		
		return gridString2;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		List<Integer> factors = MathUtil.getFactors(plainText.length());
		factors.remove((Integer)1);
		factors.remove((Integer)plainText.length());
		int factor = RandomUtil.pickRandomElement(factors);
		return encode(plainText, factor, plainText.length() / factor, RandomUtil.pickRandomElement(Routes.getRoutes()), RandomUtil.pickRandomElement(Routes.getRoutes()));
	}
	
	@Override
	public int getDifficulty() {
		return 4;
	}
}
