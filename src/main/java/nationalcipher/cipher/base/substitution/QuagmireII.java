package nationalcipher.cipher.base.substitution;

import javalibrary.lib.Alphabet;
import nationalcipher.cipher.base.Quagmire;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class QuagmireII implements IRandEncrypter {

	public static void main(String[] args) {
		System.out.println(new String(encode("HARRYTHISISSTRICTLYEYESONLYFIVEMINUTESWITHTHEOLLADYWASENOUGHTOGIVEOURTECHGUYSAHEADACHENOTHINGMUCHSHOWEDUNDERULTRAVIOLETBUTTHEYMANAGEDTOCATCHASCRAPINGOFVARNISHFROMTHEBACKOFTHEPAINTINGANDFOUNDHIGHCONCENTRATIONSOFLEADTHEYDIDNOTEXPECTTHISANDWEARETRYINGTOGETPERMISSIONTOXRAYHERBUTTHECURATORISNOTKEENFORHERTOBEMOVEDAGAINAFTERTHEYEARSOFTRAVELDURINGTHEWAROURAGENTSARETRYINGTOTRACKHERMOVEMENTSDURINGTHATTIMETOSEEWHENSHEMIGHTHAVEBEENBROUGHTTOMONTMARTRESHELEFTPARISONTHETWENTYEIGHTHOFAUGUSTJUSTBEFORETHEOUTBREAKOFWARANDTRAVELLEDINACONVOYOFTHIRTYSEVENTRUCKSTOAPLACECALLEDCHAMBORDIWILLLETYOUKNOWIFIHEARANYMOREABOUTHERTRAVELSASFORSARAHWEFOUNDOUTALITTLEMOREABOUTHERFAMILYONEOFTHEBUCHENWALDNEIGHBOURSMENTIONEDACONNECTIONWITHITALYWHICHISSUGGESTIVEGIVENTHEPERUGGIASTORYTHOUGHEXACTLYWHATTHECONNECTIONMIGHTBEIAMNOTSUREMOREWHENIHAVETIMEPHIL", "SPRINGFEVABCDHJKLMOQTUWXYZ", "FLOWER", 'A')));
		System.out.println(new String(decode("JICICOSLYKILFVCHEBDXCCORJIOEWAFMWKKTXBGWHRJIBKEDBJWZABUXWHEHUXOXCU".toCharArray(), "SPRINGFEVABCDHJKLMOQTUWXYZ", "FLOWER", 'A')));
	}
	
	public static String encode(String plainText, String key, String indicatorKey, char indicatorBelow) {
		return Quagmire.encode(plainText, Alphabet.getUpperCase(), key, indicatorKey, indicatorBelow);
	}
	
	public static byte[] decode(char[] cipherText, String key, String indicatorKey, char indicatorBelow) {
		return Quagmire.decode(cipherText, Alphabet.getUpperCase(), key, indicatorKey, indicatorBelow);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey26(), KeyGeneration.createShortKey26(2, 15), 'A');
	}
	
	@Override
	public int getDifficulty() {
		return 6;
	}
}
