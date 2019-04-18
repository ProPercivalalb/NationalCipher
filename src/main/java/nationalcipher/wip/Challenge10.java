package nationalcipher.wip;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Challenge10 {

	public static void main(String[] args) throws IOException, URISyntaxException {
		List<String> read = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("nationalcipher/challenge10.txt").toURI()));
		String cipherText = read.get(0);
		String cipherText2 = read.get(1);
		for(int  i = 0 ; i < cipherText.length(); i++) {
			if(cipherText.charAt(i) != cipherText2.charAt(i)) {
				System.out.println(i + " "+ cipherText.substring(i - 10, i + 10));
				System.out.println(i + " "+ cipherText2.substring(i - 10, i + 10));
				System.out.println("");
			}
		}
		
		System.out.println(cipherText);
		cipherText = cipherText.replaceAll("[\\s+]+", "");
		System.out.println(cipherText);
		
		//ArrayList<String> string = new 
		int blockSize = 5;
		for(int i = 0; i < cipherText.length() / blockSize; i++) {
			String row = cipherText.substring(i * blockSize, i  * blockSize + blockSize);
			//System.out.println(row);
		}
	}

}
 