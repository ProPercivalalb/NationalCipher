package nationalcipher.cipher;

import javalibrary.lib.Timer;
import javalibrary.math.MathHelper;
import javalibrary.string.StringTransformer;

public class PeriodicGromark {

	public static void main(String... args) {
		Timer timer = new Timer();
		String text = "PHASEFIVESEAHORSEISREADYFORTRIALSANDTHENAUTILUSSYSTEMISFULLYFUNCTIONALWEENGAGEDTHEMECHANISMANDLOWEREDTHEDECKTOTHREEFEETABOVESEALEVELAPPROACHINGTHESHOREBYTHERADARSTATIONATALLTIMESSIGNALSFROMTHEIRCOMMUNICATIONSWEREMONITOREDANDNOSIGNWASGIVENTHATOURAPPROACHHADBEENMONITOREDOREVENNOTICEDWEBACKEDOFFTHEDECKWASRAISEDBYTWOFEETANDTHEAPPROACHATTEMPTEDAGAINONCEMOREOURINCURSIONWASUNNOTICEDOVERNIGHTWECONDUCTEDARANGEOFTESTSANDMAPPEDTHERADARCOVERAGEONTHREESEPARATEOCCASIONSTHERESEEMSTOHAVEBEENAFLURRYOFACTIVITYANDOURMODELINGSUGGESTSTHATTHESHIPSMASTSMAYHAVETRIGGEREDBRIEFALARMSONALLOCCASIONSTHEAUTOMATICDIVESYSTEMSCUTINCORRECTLYLOWERINGTHEDECKSTOSEALEVELANDTHEALARMSWERECANCELLEDTHESEAHORSEDEPLOYMENTSYSTEMWILLBEFULLYMOUNTEDTONIGHTANDWEWILLCONDUCTABATTERYOFTESTSONTHEDEPLOYMENTANDEMERGENCYRECOVERYSYSTEMSOVERTHENEXTTWONIGHTSASSUMINGTHATSEAANDAIRTRAFFICREMAINSLOW";
		String key = "WELCOM";
		
		String encoded = encode(text, key);
		
		System.out.println(encoded);
		String decoded = new String(decode(encoded.toCharArray(), key));
		System.out.println(decoded);
		timer.displayTime();
	}
	
	public static String encode(String plainText, String key) {
		int[] inOrd = new int[key.length()];
		int[] noOrd = new int[key.length()];
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			int keyindex = key.indexOf(ch);
			if(keyindex != -1) {
				inOrd[p++] = keyindex;
				noOrd[keyindex] = p;
			}
			else
				key += ch;
		}
		
		
		String transposedKey = "";
		int[] numericKey = new int[plainText.length()];
		
		for(int i = 0; i < inOrd.length; i++) {
			transposedKey += StringTransformer.getEveryNthChar(key, inOrd[i], inOrd.length);
			numericKey[i] = noOrd[i];
		}

		for(int i = 0; i < numericKey.length - noOrd.length; i++)
			numericKey[i + noOrd.length] = (numericKey[i] + numericKey[i + 1]) % 10;
			
		String cipherText = "";
		
		for(int i = 0; i < plainText.length(); i++) {
			int keyIndex = (int)(Math.floor(i / inOrd.length) % inOrd.length);
			cipherText += transposedKey.charAt((transposedKey.indexOf(key.charAt(keyIndex)) + (plainText.charAt(i) - 'A') + numericKey[i]) % 26);
		}
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String key) {
		int[] inOrd = new int[key.length()];
		int[] noOrd = new int[key.length()];
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			int keyindex = key.indexOf(ch);
			if(keyindex != -1) {
				inOrd[p++] = keyindex;
				noOrd[keyindex] = p;
			}
			else
				key += ch;
		}
		
		
		String transposedKey = "";
		int[] numericKey = new int[cipherText.length];
		
		for(int i = 0; i < inOrd.length; i++) {
			transposedKey += StringTransformer.getEveryNthChar(key, inOrd[i], inOrd.length);
			numericKey[i] = noOrd[i];
		}

		for(int i = 0; i < numericKey.length - noOrd.length; i++)
			numericKey[i + noOrd.length] = (numericKey[i] + numericKey[i + 1]) % 10;
		
		
		char[] plainText = new char[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++) {
			int keyIndex = (int)(Math.floor(i / inOrd.length) % inOrd.length);
			plainText[i] = (char)(MathHelper.mod(transposedKey.indexOf(cipherText[i]) - transposedKey.indexOf(key.charAt(keyIndex)) - numericKey[i], 26) + 'A');
		}
		
		return plainText;
	}
}
