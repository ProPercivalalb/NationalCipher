package nationalcipher.wip;

import java.util.Arrays;

import nationalcipher.cipher.decrypt.methods.KeyIterator;

public class Test {

	public static void main(String[] args) {
		//KeyIterator.iterateHutton((k1, k2) -> System.out.println(k1 + " " + k2), 2, 2);
		KeyIterator.permuteDoubleIntegerOrderedKey((k1, k2) -> System.out.println(Arrays.toString(k1) + " " + Arrays.toString(k2)), 4, 3);
	}

}
