package nationalcipher.cipher;

import java.util.Arrays;
import java.util.List;

import javalibrary.string.MorseCode;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class FractionatedMorse implements IRandEncrypter {

	public static void main(String[] args) {
		System.out.println(encode("HARRY, THE PUZZLE OF THE STAMPED POSTCARD HAD ME FOOLED FOR A WHILE, BUT I THINK I FIGURED IT OUT. WAS THE MESSAGE ON THE BACK OF THE STAMP? I AM GUESSING YOU STEAMED IT OFF AND FOUND IT THERE. IT WAS A PRETTY INGENIOUS PLOY. MY MASTERS BACK IN WASHINGTON ARE INCREASINGLY WORRIED ABOUT OUR RELATIONSHIP WITH THE REST OF THE FOUR POWERS. FOLLOWING THE BREAKDOWN IN TRUST WITH THE SOVIETS THEY ARE COUNTING ON THE UK AND FRANCE AS ALLIES. IF THEY ARE GOING BEHIND OUR BACKS WITH THIS REICHSDOKTOR, THAT DOES NOT BODE WELL FOR FUTURE DIPLOMACY. DO YOU HAVE CONTACTS THERE YOU CAN EXPLOIT TO FIND OUT WHAT THEY ARE INTENDING? WE REALLY CANNOT AFFORD TO FALL OUT RIGHT NOW. THE ATTACHED MESSAGE IS ANOTHER INTERCEPT, THIS TIME FROM THE BRITISH EMBASSY WIRELESS. WHILE THINGS ARE DICEY I DON’T FEEL I CAN ASK THEM ABOUT IT, MAYBE YOU COULD CRACK IT FOR US. DOES IT MENTION THE RATLINES? BEST, CHARLIE", "ROUNDTABLECFGHIJKMPQSVWXYZ"));
		System.out.println(new String(decode("ESOAVVLJRSSTRX".toCharArray(), "ROUNDTABLECFGHIJKMPQSVWXYZ")));
	}
	
	public static String encode(String plainText, String key) {
		
		String cipherText = "";
		String morseText = "";
		
		morseText = MorseCode.getMorseEquivalent(plainText);
		while(morseText.length() % 3 != 0)
			morseText += "X";
		
		List<Character> list = Arrays.asList('.', '-', 'X'); 
		for(int i = 0; i < morseText.length(); i += 3) {
			int a = list.indexOf(morseText.charAt(i));
			int b = list.indexOf(morseText.charAt(i + 1));
			int c = list.indexOf(morseText.charAt(i + 2));
			cipherText += key.charAt(a * 9 + b * 3 + c);
		}
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String key) {
		String plainText = "";
		String morseText = "";
		char[] list = new char[] {'.', '-', 'X'}; 
		
		
		for(int i = 0; i < cipherText.length; i++) {
			char a = cipherText[i];
			
			int index = key.indexOf(a);
			int first = index / 9;
			int second = (int)(index / 3) % 3;
			int third = index % 3;
			morseText += list[first] + "" + list[second] + "" + list[third];
		}
		
		String[] split = morseText.split("X");
		for(String code : split) {
			try {
			
				plainText += MorseCode.getCharFromMorse(code);
			}
			catch(NullPointerException e) {
				plainText += code;
			}
		}
		
		return plainText.replaceAll(" ", "").toCharArray();
		
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createFullKey());
	}
}
