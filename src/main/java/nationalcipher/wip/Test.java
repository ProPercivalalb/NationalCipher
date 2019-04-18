package nationalcipher.wip;

import java.util.Arrays;
import java.util.stream.Stream;

import nationalcipher.cipher.decrypt.methods.KeyIterator;

public class Test {

	public static void main(String[] args) {
		KeyIterator.iterateObject(t -> System.out.println(Arrays.toString(t)), Integer.class, 2, new Integer[] {2,3,1,7});

	}

}
