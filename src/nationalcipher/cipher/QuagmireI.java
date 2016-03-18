package nationalcipher.cipher;

import java.util.Arrays;

import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class QuagmireI implements IRandEncrypter {

	public static void main(String[] args) {
		//THESLIDEFAIRCANBEUSEDWITHVIGENEREVARIANTORBEAUFORT
		System.out.println(new String(encode("THEQUAGONEISAPERIODICCIPHERWITHAKEYEDPLAINALPHABETRUNAGAINSTASTRAIGHTCIPHERALPHABET", "SPRINGFEVABCDHJKLMOQTUWXYZ", "FLOWER")));
		System.out.println(new String(decode("TNDJSIXSUHOUJCBHEDJFDVTOVCVTCFDVDUSOPIEHONDNXVDUZKXVWWFQEVFMKBVNFNLVOKVHWWXVDWWFUKVPLCAWMXQPWVIAIYLHKUACGHHJLLOPQOUNIWIKTVQXFLHWWXVDSBPIUMIZIDIFCKXIPNHBKCIKBQZCKBPFXIXWBQIVUIZIRQHINBWWFICYCEEUZFZIATIHABDCAIYFBPMSQIFFHNCKDVSLHNIXMRMNFIOLVSHIIANZXCALCLHUMYFLNDFFOETPJNZQFXKZTCUAUZEKVCABHOUMMRMAIPLAIYEKVCSNFOKKCHNYQWXPCQPEVBKCIHEYWVHUXCSKSVIHHRPLIBEDNHRIXCIOPQIZAIAHLOYQEHIXXCLNNNUHAFUBHKNJZFBEWSERSXVFCATJFDVDBSENDCUNILHHQNLCSOPQGXQFQPLKNFHSSABCBLUAIZIOTUUTCZELKUIZKVCSRIROJOVIVXEXVBKCUNFUUKSBKHSTRZZFDVTOMNHMBISHYFKVCMHLCVTEXVXIELIIFCLCAWHSNYWUHOXKBOFCJCKBSQIESABCLCVIFEYIFBCHETEVCVDUMHRIPMMAUCIALTULIBKUHQYQHHUXFFKMLVELCVGFKMZIPNHIVKONYXEHNIXSXXPVPMOPQKIIASCVHENJPNGQULAFUBHKNJZFOUUHXVBPPFDVDUBALVPLICWBYRBFWKLKPHCEANZFVRTBPNDZHIPXJFKZNQFHIVAELHLPFQENDCLCVPIZIYSUSSHXBISNWBIUBQZCSVWBIIKPFMNXALUCASUUNIDBSIUMLHTIPSXELQSXFSNUAIBPIFFSWWFYUTZMHRWWFOVTFXBSQIZAIYELCAWWKMZQPZIYQEEACTHVNIXSZIIIRKVNGFXOVVZVNHSSXOPPPFIATZXKBQPXVDMBIOPQRIRIFWEEAJBEOUDIZIDTHAISPLLKNXCLCVSIEVVSHIIXKHIMADJLCVSBCPNQFHPUDINAQQLHEUPIVIATULAXVPEOPQFVNHWBZRFIZXNBQLHNIXSFNYZEMHLDVOOBKCRPUDBLLVVZXKVWWKOBKUHSVVPHOPVIVIKVPKFLVOHOVNZVCVDIZIHVFFXUAUSQGIPMVVSEXKBPVFEUZPMIQQFRYPQFFCVTCFDVDUSOPQUIKBZGXPKTLFFBC".toCharArray(), "JSRPATDGUMKYNXCWZIBHVFLQOE", "NAVHK")));
	}
	
	public static String encode(String plainText, String keyTop, String indicatorKey) {
		System.out.println(keyTop + " " + indicatorKey);
		String cipherText = "";
		String[] keyAlpha = new String[indicatorKey.length()];
		Arrays.fill(keyAlpha, "");
		
		for(int i = 0; i < indicatorKey.length(); i++)
			for(int k = 0; k < 26; k++) 
				keyAlpha[i] += (char)((26 - keyTop.indexOf('A') + (indicatorKey.charAt(i) - 'A') + k) % 26 + 'A');
		
		for(int i = 0; i < plainText.length(); i++) {
			cipherText += keyAlpha[i % indicatorKey.length()].charAt(keyTop.indexOf(plainText.charAt(i)));
		}
			
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String keyTop, String indicatorKey) {
		char[] plainText = new char[cipherText.length];
		
		String[] keyAlpha = new String[indicatorKey.length()];
		Arrays.fill(keyAlpha, "");
		
		for(int i = 0; i < indicatorKey.length(); i++)
			for(int k = 0; k < 26; k++) 
				keyAlpha[i] += (char)((26 - keyTop.indexOf('A') + (indicatorKey.charAt(i) - 'A') + k) % 26 + 'A');
		
		for(int i = 0; i < cipherText.length; i++) {
			plainText[i] = keyTop.charAt(keyAlpha[i % indicatorKey.length()].indexOf(cipherText[i]));
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey26(), KeyGeneration.createShortKey26(2, 15));
	}
}
