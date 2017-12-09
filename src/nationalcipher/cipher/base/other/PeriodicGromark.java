package nationalcipher.cipher.base.other;

import javalibrary.lib.Timer;
import javalibrary.math.MathUtil;
import javalibrary.string.StringTransformer;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class PeriodicGromark implements IRandEncrypter {

	public static void main(String[] args) {
		Timer timer = new Timer();
		String text = "SEXREACHEDSUPPOSEOURWHETHEROHREALLYBYANMANNERSISTERSOONESPORTSMANTOLERABLYHIMEXTENSIVEPUTSHEIMMEDIATEHEABROADOFCANNOTLOOKEDINCONTINUINGINTERESTEDTENSTIMULATEDPROSPEROUSFREQUENTLYALLBOISTEROUSNAYOFOHREALLYHEEXTENTHORSESWICKETDETRACTYETDELIGHTWRITTENFARTHERHISGENERALIFINSOBREDATDAREROSELOSEGOODFEELANDMAKETWOREALMISSUSEEASYCELEBRATEDDELIGHTFULANESPECIALLYINCREASINGINSTRUMENTAMINDULGENCECONTRASTEDSUFFICIENTTOUNPLEASANTINININSENSIBLEFAVOURABLELATTERREMARKHUNTEDENOUGHVULGARSAYMANSITTINGHEARTEDONITWITHOUTMEMARIANNEORHUSBANDSIFATSTRONGERYECONSIDEREDISASMIDDLETONSUNCOMMONLYPROMOTIONPERFECTLYYECONSISTEDSOHISCHATTYDININGFOREFFECTLADIESACTIVEEQUALLYJOURNEYWISHINGNOTSEVERALBEHAVEDCHAPTERSHETWOSIRDEFICIENTPROCURINGFAVOURITEEXTENSIVEYOUTWOYETDIMINUTIONSHEIMPOSSIBLEUNDERSTOODAGE";
		String key = "MAGIC";
		
		String encoded = encode(text, key);
		
		System.out.println(encoded);
		String decoded = new String(decode(encoded.toCharArray(), new byte[encoded.length()], key));
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
			int keyIndex = (int)(Math.floor(i / inOrd.length) % inOrd.length);// + numericKey[i]
			cipherText += transposedKey.charAt((transposedKey.indexOf(key.charAt(keyIndex)) + (plainText.charAt(i) - 'A')) % 26);
		}
		
		return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String key) {
		int[] inOrd = new int[key.length()];
		int[] noOrd = new int[key.length()];
		
		char[] keyFull = new char[26];
		int index = 0;
		for(; index < key.length(); index++)
			keyFull[index] = key.charAt(index);
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			int keyindex = key.indexOf(ch);
			if(keyindex != -1) {
				inOrd[p++] = keyindex;
				noOrd[keyindex] = p;
			}
			else
				keyFull[index++] = ch;
		}
		
		int[] transposedKeyIndexOf = new int[26];
		int[] numericKey = new int[cipherText.length];
		int rows = (int)Math.ceil(26D / key.length());
		index = 0;
		
		for(int i = 0; i < inOrd.length; i++) {
			for(int r = 0; r < rows; r++) {
				if(r * inOrd.length + inOrd[i] >= 26) break;
				transposedKeyIndexOf[keyFull[r * inOrd.length + inOrd[i]] - 'A'] = index++;	
			}
			
			numericKey[i] = noOrd[i];
		}

		for(int i = 0; i < numericKey.length - noOrd.length; i++)
			numericKey[i + noOrd.length] = (numericKey[i] + numericKey[i + 1]) % 10;

		for(int i = 0; i < cipherText.length; i++) {
			int keyIndex = (int)(Math.floor(i / inOrd.length) % inOrd.length);
			plainText[i] = (byte)(MathUtil.mod(transposedKeyIndexOf[cipherText[i] - 'A'] - transposedKeyIndexOf[key.charAt(keyIndex) - 'A'] - numericKey[i], 26) + 'A');
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(RandomUtil.pickRandomInt(2, 8)));
	}
}
