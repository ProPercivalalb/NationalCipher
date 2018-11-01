package nationalcipher.cipher.base.substitution;

import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.Alphabet;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalcipher.cipher.base.Quagmire;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class QuagmireI implements IRandEncrypter {

	public static void main(String[] args) {
		System.out.println(new String(Quagmire.encode("HARRYTHISISSTRICTLYEYESONLYFIVEMINUTESWITHTHEOLLADYWASENOUGHTOGIVEOURTECHGUYSAHEADACHENOTHINGMUCHSHOWEDUNDERULTRAVIOLETBUTTHEYMANAGEDTOCATCHASCRAPINGOFVARNISHFROMTHEBACKOFTHEPAINTINGANDFOUNDHIGHCONCENTRATIONSOFLEADTHEYDIDNOTEXPECTTHISANDWEARETRYINGTOGETPERMISSIONTOXRAYHERBUTTHECURATORISNOTKEENFORHERTOBEMOVEDAGAINAFTERTHEYEARSOFTRAVELDURINGTHEWAROURAGENTSARETRYINGTOTRACKHERMOVEMENTSDURINGTHATTIMETOSEEWHENSHEMIGHTHAVEBEENBROUGHTTOMONTMARTRESHELEFTPARISONTHETWENTYEIGHTHOFAUGUSTJUSTBEFORETHEOUTBREAKOFWARANDTRAVELLEDINACONVOYOFTHIRTYSEVENTRUCKSTOAPLACECALLEDCHAMBORDIWILLLETYOUKNOWIFIHEARANYMOREABOUTHERTRAVELSASFORSARAHWEFOUNDOUTALITTLEMOREABOUTHERFAMILYONEOFTHEBUCHENWALDNEIGHBOURSMENTIONEDACONNECTIONWITHITALYWHICHISSUGGESTIVEGIVENTHEPERUGGIASTORYTHOUGHEXACTLYWHATTHECONNECTIONMIGHTBEIAMNOTSUREMOREWHENIHAVETIMEPHIL", Alphabet.getUpperCase(), "SPRINGFEVABCDHJKLMOQTUWXYZ", "FLOWER", 'A')));
		System.out.println(new String(decode("QPMGQRBUJUYIFDMPYAIFQYYJJJHJYCJLUUTPIDVWYMFSGAESDWHIZRBLIRVCFCZPELBPZYYJJJHWLJJLPUP".toCharArray(), "SPRINGFEVABCDHJKLMOQTUWXYZ", "FLOWER", 'A')));
		Languages.ENGLISH.loadNGramData();
		String en = "AIBISTFRAQLNTPRKJSSEWVAKOOYCRUSPTNZKJICTTFKRSNOLYXMEHNEJNLYAYODRUSNXRAVKZZXYQHRSHIALAJTNYHMOQNJBHQACEVIUJXJBJOTPHUQNOEAQLJKCEWPITHEEEKCVHYCFHAVIWPMOQKYPAPOGIALRUPEZVUALTCXKCEKHGTKTNDHWWYZUJXRQZCCUOKSOYRYKGKONOCSJGXYHIFNQXGOAVOOVBTAAGIHGDSVIBVYRWRWYKZGIKXSIAIQLGKOYOVIIDAMROJEJAMCZIIJNKIQOCJTMEJYCBAMRANFSPZVIXIYHTNYYESIYHIFJGINOCKZGEMLEJZQOETFVTGIZUPHQSOYSYIJJISIJZEKKKALTRSIAORVDSOYSEJZQOETFHEJRAEANASVQHIOAZVAIDAEZHPEOVJTQKOZZRJKZMUOENHKTPVAZVOECKXGITSUOEZVYWIOEDVTGFKRKYWUDJAJUXSAQJXNKEAAJKJYBPVIUNLWYIITXYRYEJRSMDMOIVNGVUFCXKCIPKMIVPEJKZHWFSANIOSWCIWIRSMDLAINQZRERTQSOLIKMKJFNUCGXRCEYIITFAOPVIPNXTFVZJIWVISAGLLOPLIBHCWIYCHOIOZKIRRYTBVDKIMAONLJAMRCHDQSSOJVCXKCEOJKZVGWYSNTVTGFQCHINMIOEQNGEEHKKOGELKGKOQIAAGJHOYSAGVATSQJQYVNTMEJYRPEJKRSMMRZZQQHNTUIMJAZUDAJCHBTBFTZHYTFVKKOGELKGKOAIDAEPVTATOCJLXRIPCBVQHIOGZHPEARDSMCIB";
		System.out.println(new String(decode(en, "AUTOMBILECDFGHJKNPQRSVWXYZ")));
	}
	
	public static byte[] decode(String text, String key) {
		String indicatorKey = "";
		int[] keyIndex = new int[26];
		for(int i = 0; i < 26; i++)
			keyIndex[key.charAt(i) - 'A'] = i;
		
        for(int i = 0; i < 7; ++i) {
        	String temp = StringTransformer.getEveryNthChar(text, i, 7);
            int shift = findBestCaesarShift(temp.toCharArray(), key, keyIndex, Languages.ENGLISH, null);
            indicatorKey += key.charAt((shift) % 26);
        }
        System.out.println("Keyword: " + indicatorKey);
		
        byte[] plainText = Quagmire.decode(text.toCharArray(), key, key, indicatorKey, 'A');
        return plainText;
	}
	
	public  static int findBestCaesarShift(char[] text, String keyTop, int[] keyIndex, ILanguage language, ProgressValue progressBar) {
		int best = 0;
	    double smallestSum = Double.MAX_VALUE;
	    System.out.println("----Start");
	    for(int shift = 0; shift < 26; ++shift) {
	    	//System.out.println(shift + " " + StringTransformer.rotateRight(key, shift));
	    	char[] encodedText = decode(text, keyTop, keyIndex, shift);
	        double currentSum = ChiSquared.calculate(encodedText, language);
	       	System.out.println(shift + " " + new String(encodedText));
	        if(currentSum < smallestSum) {
	        	best = shift;
	            smallestSum = currentSum;
	        }
	  
	    }
	    System.out.println("" + best);
	    return best;
	}
	
	public static char[] decode(char[] cipherText, String keyTop, int[] keyIndex, int shift) {
		char[] plainText = new char[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = keyTop.charAt((26 + keyIndex[cipherText[i] - 'A'] - shift) % 26);
		
		return plainText;
	}
	
	public static String encode(String plainText, String keyTop, String indicatorKey, char indicatorBelow) {
		return Quagmire.encode(plainText, keyTop, Alphabet.getUpperCase(), indicatorKey, indicatorBelow);
	}
	
	public static byte[] decode(char[] cipherText, String key, String indicatorKey, char indicatorBelow) {
		return Quagmire.decode(cipherText, key, Alphabet.getUpperCase(), indicatorKey, indicatorBelow);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey26(), KeyGeneration.createShortKey26(2, 15), 'A');
	}
}
