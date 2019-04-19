package nationalcipher.cipher.base.other;

import java.util.Arrays;
import java.util.List;

import javalibrary.string.MorseCode;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Morbit implements IRandEncrypter {

	public static void main(String[] args) {
		System.out.println(encode("THE FRENCH MAY BE YOUR ALLIES, BUT THEY ARE NOT YOUR FRIENDS. THEY PLAN TO INFILTRATE THE RATILINES AND TO TRY TO TURN THE HIGH VALUE TARGETS FOR THEMSELVES. THEY HAVE A PARTICULAR INTEREST IN NAZI SCIENTISTS FROM THE DIE ALCHEMISTEN PROJECT. IF YOU WANT TO BREAK THE REICHSDOKTOR NETWORK BEFORE THEY CAN DO SO, TAKE CARE NOT TO SHARE ANY INTELLIGENCE WITH THEM. YOU HAVE BEEN WARNED. I THINK IT IS TIME TO BEGIN NEGOTIATIONS. I HAVE A NUMBER IN MIND, AND I THINK ONCE YOU KNOW WHAT I AM OFFERING, YOU WILL FIND IT VERY REASONABLE. AS A SIGN OF GOOD FAITH I OFFER YOU THE FOLLOWING INFORMATION. ONE OF THE LOCAL RATILINE COORDINATORS WILL BE LEAVING THE US SECTOR TOMORROW NIGHT IN A BLACK LIMOUSINE. UNDER THE BACK SEAT OF HIS CAR YOU WILL FIND HIDDEN A JUNIOR SS OFFICER WHO IS TRYING TO ESCAPE, AND IN THE TRUNK YOU WILL FIND A NUMBER OF PAPERS RELATING TO STOLEN ARTWORKS THAT HE HOPES TO TRADE TO THE FRENCH FOR HIS FREEDOM. YOU MIGHT WANT TO CONSIDER CAREFULLY WHETHER YOU CAN TRUST YOUR FRIEND CHARLIE WITH THIS INFORMATION, AFTER ALL, HER HUSBAND FRANCOIS IS FRENCH.", new Integer[] {8, 4, 7, 3, 1, 6, 0, 2, 5}));
		System.out.println(new String(decode("99175858423198854153482914882794319889875375413912827987441758489917482115827275913915827585121989148849153187991843518941828975884893321531217987991378989853413238799139853584727943198898753754185372897898989382842321519311721391872794158538489432153848933198858893121798512858877421843413893215191285932357235721798785898491744351841217989918434332385141184358588519184341484212321414188485398279772797585885917793238989918541218799319885889872794319889432151412827578589894321485932121843431988491411747482757418431415193121879931988982795188798799135751413582797431843432387991153517439158585384411798514859188989158799135751413432184343988991843932151439853584798579319879918985889358991939323533214319853393211938277493239112114859158275894153151413918541482721744357482742321519851978443238753587987991141357279744174139754148279333215893848279775799177799135751411843138489382857858851485935744174382775158943214393321585148593215314182899338275779911777535899139378989853411898538277991141843114358854331989193182115858272319884914187919198823985191935757914114889898579918591319893828985141391844135793932387941114893585385193531988217493215318721742398842321535827975939358585358479315827589798482789893378431827431988585314893889849119335899175859188".toCharArray(), new Integer[] {8, 4, 7, 3, 1, 6, 0, 2, 5})));
	}
	
	public static String encode(String plainText, Integer[] order) {
		
		String cipherText = "";
		String morseText = "";
		
		morseText = MorseCode.getMorseEquivalent(plainText);
		if(morseText.length() % 2 != 0)
			morseText += "X";
		
		List<Character> list = Arrays.asList('.', '-', 'X'); 
		for(int i = 0; i < morseText.length(); i += 2) {
			int a = list.indexOf(morseText.charAt(i));
			int b = list.indexOf(morseText.charAt(i + 1));
			cipherText += order[a * 3 + b] + 1;
		}
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, Integer[] order) {
		int[] reversedOrder = new int[order.length];
		for(int i = 0; i < order.length; i++)
			reversedOrder[order[i]] = i;
		
		String plainText = "";
		char[] morseText = new char[cipherText.length * 2];
		char[] list = new char[] {'.', '-', 'X'}; 
		
		
		for(int i = 0; i < cipherText.length; i++) {
			int a = cipherText[i] - '0' - 1;
			
			int index = reversedOrder[a];
			int first = index / 3;
			int second = index % 3;
			morseText[i * 2] = list[first];
			morseText[i * 2 + 1] = list[second];
		}
		
		int last = 0;
		for(int i = 0; i < morseText.length; i++) {
			char a = morseText[i];
			boolean end = i == morseText.length - 1;
			if(a == 'X' || end) {
				String code = new String(morseText, last, i - last + (end ? 1 : 0));
				
				last = i + 1;
				try {
					plainText += MorseCode.getCharFromMorse(code);
				}
				catch(NullPointerException e) {
					plainText += code;
				}
			}
			
		}
		
		return plainText.replaceAll(" ", "").toCharArray();
		
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createOrder(9));
	}
}
