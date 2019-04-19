package nationalcipher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainCLI {

	public static void main(String[] args) {
		System.out.print("Realm (e.g. /): ");
		BufferedReader inputBuffer = (new BufferedReader(new InputStreamReader(System.in)));
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = true;
		if(b1 && b2 || b3) {
			System.out.println("DAWWDA");
		}
		String line = null;
		try {
			while((line = inputBuffer.readLine()) != null) {
				System.out.println("READ in " + line);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
