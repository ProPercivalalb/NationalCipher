package nationalcipher.cipher.transposition;

import java.util.ArrayList;
import java.util.List;

import javalibrary.math.MathHelper;
import javalibrary.string.StringTransformer;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.transposition.Routes.RouteCipherType;

public class RouteTransposition implements IRandEncrypter {

	public static void main(String[] args) {
		System.out.println(new String(decode("JEEISRIGTATSCSHSMESEA".toCharArray(), 7, 3, RouteWritten.DOWN, Routes.OTHER_LETTER)));
	}
	
	public static String encode(String plainText, int columns, int rows, RouteCipherType writtenOn, RouteCipherType readOff) {

		//Create pattern
		List<Integer> grid = new ArrayList<Integer>();
		grid = readOff.createPattern(columns, rows, plainText.length());
		
		List<Integer> grid2 = new ArrayList<Integer>();
		grid2 = writtenOn.createPattern(columns, rows, plainText.length());
				
		//Reads across the grid
		char[] gridString = new char[plainText.length()];
		for(int i = 0; i < plainText.length(); i++)
			gridString[grid2.get(i)] = plainText.charAt(i);
		
		char[] gridString2 = new char[plainText.length()];
		for(int i = 0; i < plainText.length(); i++)
			gridString2[i] = gridString[grid.get(i)];
		
		return new String(gridString2);
		//System.out.println(finalStr + " --- " + type.getDescription());
	}
	
	public static char[] decode(char[] cipherText, int columns, int rows, RouteCipherType writtenOn, RouteCipherType readOff) {

		//Create pattern
		List<Integer> grid = new ArrayList<Integer>();
		grid = readOff.createPattern(columns, rows, cipherText.length);
		
		List<Integer> grid2 = new ArrayList<Integer>();
		grid2 = writtenOn.createPattern(columns, rows, cipherText.length);

				char[] gridString = new char[cipherText.length];
				for(int i = 0; i < cipherText.length; i++)
					gridString[i] = cipherText[grid.indexOf(i)];
				
				char[] gridString2 = new char[cipherText.length];
				for(int i = 0; i < cipherText.length; i++)
					gridString2[grid2.indexOf(i)] = gridString[i];
		
		return gridString2;
		//System.out.println(finalStr + " --- " + type.getDescription());
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		List<Integer> factors = MathHelper.getFactors(plainText.length());
		factors.remove((Integer)1);
		factors.remove((Integer)plainText.length());
		int factor = RandomUtil.pickRandomElement(factors);
		return encode(plainText, factor, plainText.length() / factor, RandomUtil.pickRandomElement(Routes.getRoutes()), RandomUtil.pickRandomElement(Routes.getRoutes()));
	}
	
	
}
