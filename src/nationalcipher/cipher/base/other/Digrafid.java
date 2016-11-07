package nationalcipher.cipher.base.other;

import java.util.Arrays;

public class Digrafid {

	public static void main(String[] args) {
		//System.out.println(new String(decode("HJMXWSWJADWGFCSPYI".toCharArray(), "KEYWORDABCFGHIJLMNPQSTUVXZ#", "VDPEFQRGSTHUIJWCKXAMYLNZBO#", 3)));
		//System.out.println(encode("HARRYTHEPUZZLEOFTHESTAMPEDPOSTCARDHADMEFOOLEDFORAWHILEBUTITHINKIFIGUREDITOUTWASTHEMESSAGEONTHEBACKOFTHESTAMPIAMGUESSINGYOUSTEAMEDITOFFANDFOUNDITTHEREITWASAPRETTYINGENIOUSPLOYMYMASTERSBACKINWASHINGTONAREINCREASINGLYWORRIEDABOUTOURRELATIONSHIPWITHTHERESTOFTHEFOURPOWERSFOLLOWINGTHEBREAKDOWNINTRUSTWITHTHESOVIETSTHEYARECOUNTINGONTHEUKANDFRANCEASALLIESIFTHEYAREGOINGBEHINDOURBACKSWITHTHISREICHSDOKTORTHATDOESNOTBODEWELLFORFUTUREDIPLOMACYDOYOUHAVECONTACTSTHEREYOUCANEXPLOITTOFINDOUTWHATTHEYAREINTENDINGWEREALLYCANNOTAFFORDTOFALLOUTRIGHTNOWTHEATTACHEDMESSAGEISANOTHERINTERCEPTTHISTIMEFROMTHEBRITISHEMBASSYWIRELESSWHILETHINGSAREDICEYIDONTFEELICANASKTHEMABOUTITMAYBEYOUCOULDCRACKITFORUSDOESITMENTIONTHERATLINESBESTCHARLIEX", "KEYWORDABCFGHIJLMNPQSTUVXZ#", "VDPEFQRGSTHUIJWCKXAMYLNZBO#", 3));
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String keysquare1, String keysquare2, int fractional) {
		if(fractional == 0) fractional = cipherText.length / 2; //I believe this will work
		
		int[] numberText = new int[cipherText.length * 3 / 2];
		for(int i = 0; i < cipherText.length / 2; i++) {
			
			char a = cipherText[i * 2];
			char b = cipherText[i * 2 + 1];
			
			int index1 = keysquare1.indexOf(a);
			int index2 = keysquare2.indexOf(b);
						
			int column1 = index1 % 9 + 1;
			int row1 = index1 / 9;
			
			int column2 = index2 % 3;
			int row2 = index2 / 3 + 1;
			
			int middleNo = row1 * 3 + column2 + 1;
		
			int blockBase = (int)(i / fractional) * (fractional * 3) + i % fractional;
			int min = Math.min(fractional, cipherText.length / 2 - (int)(i / fractional) * fractional);
			
			numberText[blockBase] = column1;
			numberText[blockBase + min] = middleNo;
			numberText[blockBase + min * 2] = row2;
		}

		int index = 0;
		
		for(int i = 0; i < numberText.length; i += 3) {
			int a = numberText[i] - 1;
			int b = numberText[i + 1] - 1;
			int c = numberText[i + 2] - 1;
			plainText[index++] = (byte)keysquare1.charAt(a + (int)Math.floor(b / 3) * 9);
			plainText[index++] = (byte)keysquare2.charAt(c * 3 + b % 3);
			
		}
		
		return plainText;
	}
	
}
