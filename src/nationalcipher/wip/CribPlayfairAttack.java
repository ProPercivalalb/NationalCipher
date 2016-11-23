package nationalcipher.wip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javalibrary.util.ArrayUtil;

public class CribPlayfairAttack {

	public static String[] CRIB = new String[] {"PRETTY", "TEMP"};
	
	public static String CIPHER_TEXT = "SIDUBPMECLFKUNUBSYYDKQNVQFOCOYIKPHZBFCZABQABNAFCSHDPGATKOAHOFBTXTUBPSIDQDNQPEBDSQENVOVMUAZCPUTPNDPIQKCDQQDEHTUKCPKDOTUNIBQZATUSIBUKHISHOUDNVOFKBSHABSYSIBUKHISYOPBCLAIQFHKQIMTBDYOPOCAILUBOYHTYTPMDQDKSIHOGZADCLUBFQOVKCMLSIABKBSHKDXHPTOACARXBLUKBYOYIGFQUCFQBUUTTNPTQGFOUDTGMHFQIMTGSINGCQYTCAUBFQOVRNBQRDQUSICPSIDQHTXGDQZBBQBKNYTIDQTGHISYSIDUCUYIDQELCTMULYOIAEHTBLEVHOFMDIDUDFPAEBOCLBPKTHROSEKCCQKDSUNTCZDUKCLESYUDSMTHITOSHPMYINYRKCXDPTOAFCZAIMUTPNSEIKLHMTLBYOPOBCPTUODPLMABUBSIDZTUMTHOKBZATWSIHOKMDPLMAENYPOOFPCHOAZBUSIPTHIRYKCOHUCYTQUPOELICHOLBSIBPDQHDFQNKXTHDTUFCYOSDPTOAPABEYTTNPBSIHOMBOYNXSEIKNWPBIODQDKNLBDYOTUFONVMPZADNRNPMMIWDQUSIBUDKQCUBFQERVYVENZLMDOMHXGDQZBPABQPCITQUKCUBDQIDLCOGVCOCHDCLRDAPKDZAINVMSEIZFDORNIFADUIKTNNZLPSUSPOYWOCZDUHOMBOYNXPNDPPCGQPNDHQDEANVPASIHTFLUDSMNYPOGYFOOQAPOVPTNPUBQFUDQMNYPIHOHGMTVBTBYTUCDKMHDTQEFBHOTNPOLBSUHO";
	
	public static void main(String[] args) {//CIPHER_TEXT.length()
		char[] first = new char[CIPHER_TEXT.length()];
		Arrays.fill(first, '-');
		List<char[]> ch = apply(CIPHER_TEXT, first, CRIB[0], false);
		for(char[] previous : ch) {
			apply(CIPHER_TEXT, previous, CRIB[1], true);
		}
	}
	
	public static List<char[]> apply(String cipherText, char[] plainText, String crib, boolean print) {
		List<char[]> list = new ArrayList<char[]>();
		round:
		for(int i = 0; i < Math.min(15, CIPHER_TEXT.length() - crib.length()); i++) {
			int evenStart = (int)(Math.ceil(i / 2D) * 2);
			
			boolean done = false;
			while(!done) {
				for(int k = i % 2; k < crib.length(); k+=2) {
					if(k + 1 >= crib.length()) break;
					if(crib.charAt(k) == crib.charAt(k + 1)) {
						crib = crib.substring(0, k + 1) + 'X' + crib.substring(k + 1, crib.length());
						break;
					}
					done = true;
				}
			}
			
			
			Map<String, String> mapping = new HashMap<String, String>();
			for(int e = evenStart; e < i + crib.length(); e+=2) {
				if(e - i + 2 > crib.length()) break;
				if((plainText[e] != crib.charAt(e - i) || plainText[e] != crib.charAt(e - i + 1)) && plainText[e] != '-') continue round;
				mapping.put(CIPHER_TEXT.substring(e, e+2), crib.substring(e - i, e - i + 2));
			}
			
			char[] plainText2 = ArrayUtil.copyOfRange(plainText, 0, plainText.length);
			for(String k : mapping.keySet()) {
				insert(CIPHER_TEXT, plainText2, k, mapping.get(k));
			}
			
	
			//for(int j = evenStart; j < i + crib.length(); j += 2) {
			//	if(j - i + 2 > crib.length()) break;
			//	insert(CIPHER_TEXT, plainText2, CIPHER_TEXT.substring(j, j + 2), crib.substring(j - i, j - i + 2));
				
				
			//}
			list.add(plainText2);
			if(print)
				System.out.println(new String(plainText2));
		}
		return list;
	}
	
	public static void insert(String cipherText, char[] plainText, String cipherGroup, String plainGroup) {
		for(int i = 0; i < cipherText.length(); i+=2) {
			if(cipherText.charAt(i) == cipherGroup.charAt(0) && cipherText.charAt(i + 1) == cipherGroup.charAt(1)) {
				plainText[i] = plainGroup.charAt(0);
				plainText[i + 1] = plainGroup.charAt(1);
			}
				
		}
	}

}
