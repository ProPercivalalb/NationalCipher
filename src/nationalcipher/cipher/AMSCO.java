package nationalcipher.cipher;

import java.util.Arrays;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class AMSCO implements IRandEncrypter {

	public static void main(String[] args) {
		System.out.println(encode("MARKITOLDMYGRANDMAALLABOUTTHETEMPESTCONSPIRACYDIDYOUKNOWSHEUSEDTOWORKINTHESECURITYSERVICESSHESAIDSHEHADNEVERHEARDOFITBUTSHEHASAFRIENDSHECALLEDHARRYPROBABLYNOTHISREALNAMEWHOISSTILLONTHEEDGESOFTHATCOMMUNITYANDSHERECKONEDHEMIGHTBEABLETOHELPSHEALSOASKEDSOMEPEOPLEATHEROLDPLACETOKEEPANEYEOUTFORUSANDWATCHFORANYTHINGUNUSUALTHEYTOOKMYCOMPUTERINFORANALYSISTOSEEIFTHEYCOULDFINDANYMOREINFOABOUTTHEHACKERINTHEMEANTIMEIHAVEBEENWORKINGMOREONTHETURINGPAPERSANDREADINGSOMEOLDNEWSPAPERSTRYINGTOPICKUPANYTRAILLEFTBYTEMPESTIFIGUREDISHOULDLOOKFORBIGSHIFTSINSTOCKPRICESMOVEDBYHISTORICEVENTSBUTTHEREAREQUITEAFEWOFTHOSEASYOUMIGHTEXPECTBUTTHENITHOUGHTSOMEMOREABOUTTURINGSREMARKSCONCERNINGTHEIRUSEOFINTELLIGENCEANDBEGANTOWONDERWHATHEMEANTWHENHESAIDTHEYINVENTEDTHEIROWNDISTRIBUTIONNETWORKSMUGGLINGSECRETSACROSSTHEWORLDINMUSICMANUSCRIPTSUSINGSTEGANOGRAPHYSTEGANOGRAPHYISTHEARTOFHIDINGAMESSAGESOIFIGURETHEENCRYPTIONWASDISGUISEDBUTITWASTHEWORDSMUGGLEDTHATGOTMEWHATWOULDBETHEBESTWAYTOHIDETHEMUSICMANUSCRIPTSEEMSRISKYTOMEGRANDMACHARLIEREMEMBEREDACASEINNEWYORKINTHEFIFTIESWHENAGAMBLINGSYNDICATETRIEDTODISGUISEAPORTACIPHERINMUSICALNOTATIONBUTGOTCAUGHTWHENACOPWHOPLAYEDPIANONOTICEDHOWODDTHEMUSICLOOKEDIFIGUREDTHATTEMPESTARETOOCLEVERTORISKTHATBUTGRANDMAREMINDEDMEABOUTTHESTORYTHEPURLOINEDLETTERREMEMBERWHATDUPINSAIDPERHAPSTHEMYSTERYISALITTLETOOPLAINITHINKIMAYKNOWHOWTHEYDIDITMORELATERCHARLIE", true, new int[] {3, 1, 0, 4, 2}));
		System.out.println(new String(decode("HETEAMTTOWIMONNSEJNDTOSEBRERRHOOISSMIURNORISHIROR".toCharArray(), true, new int[] {6,3,4,5,2,1,0})));
	}
	
	public static String encode(String plainText, boolean first, String key) {
		int[] order = new int[key.length()];
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return encode(plainText, first, order);
	}
	
	public static String encode(String plainText, boolean first, int[] order) {
		System.out.println(Arrays.toString(order));
		String[] columns = new String[order.length];
		Arrays.fill(columns, "");
		int index = 0;
		int row = 0;
		while(index < plainText.length()) {
			for(int i = 0; i < order.length; i++) {
				if(index >= plainText.length()) break;
				if((i + row) % 2 == (first ? 0 : 1)) {
					columns[order[i]] += plainText.substring(index, Math.min(index + 2, plainText.length()));
					index += 2;
				}
				else {
					columns[order[i]] += plainText.substring(index, Math.min(index + 1, plainText.length()));
					index += 1;
				}
			}
			row++;
		}
		String read = "";
		for(int i = 0; i < order.length; i++)
			read += columns[i];
		
		return read;
	}
	
	public static String decode(String cipherText, boolean reversed, String keyword) {
		keyword = keyword.toUpperCase();
		int[] order = new int[keyword.length()];
		
		int i = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			String str = String.valueOf(ch);
			if(keyword.contains(str)) {
				order[keyword.indexOf(str)] = i;
				i++;
			}
		}
		
		return new String(decode(cipherText.toCharArray(), reversed, order));
	}
	
	public static char[] decode(char[] cipherText, boolean first, int[] order) {
		char[] plainText = new char[cipherText.length];
		int period = order.length;
		
		
		int[] reversedOrder = new int[order.length];
		for(int i = 0; i < order.length; i++)
			reversedOrder[order[i]] = i;

		int noChar1st = (int)((period + 1) / 2) * 2 + (int)(period / 2);
		int noChar2nd = (int)((period + 1) / 2) + (int)(period / 2) * 2;
		
		int rows = 0;
		int charactersLastRow = 0;
		
		boolean choose = first;
		int chars = 0;
		
		do {
			charactersLastRow = chars;
			chars += choose ? noChar1st : noChar2nd;
			rows += 1;
			choose = !choose;
		}
		while(chars < cipherText.length);
		
		charactersLastRow = plainText.length - charactersLastRow;
		
		int noCharColumn1st = (int)((rows - 1) / 2) + (int)(rows / 2) * 2;
		int noCharColumn2nd = (int)((rows - 1) / 2) * 2 + (int)(rows / 2);

		int index = 0;
		
		String[] grid = new String[period];
		
		for(int column = 0; column < period; column++) {
			int realColumn = reversedOrder[column];
			boolean isDoubleLetter = ((realColumn + rows) % 2 == (first ? 0 : 1)); //Double letter first

			int length = isDoubleLetter ? noCharColumn1st : noCharColumn2nd;
			int noCharN1st = (int)((realColumn + 1) / 2) * 2 + (int)(realColumn / 2);
			int noCharN2nd = (int)((realColumn + 1) / 2) + (int)(realColumn / 2) * 2;

			int left = charactersLastRow - (isDoubleLetter ? noCharN1st : noCharN2nd);
			if(left > 0)
				length += isDoubleLetter ? 1 : Math.min(2, left);

			grid[realColumn] = new String(cipherText, index, length);
			index += length;
		}
		
	
		int[] indexTracker = new int[period];
		
		int textIndex = 0;
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < period; column++) {
				int number = (column + row) % 2 == (first ? 0 : 1) ? 2 : 1;
				
				for(int i = 0; i < number; i++) {
					if(indexTracker[column] + i >= grid[column].length()) break;
					plainText[textIndex] = grid[column].charAt(indexTracker[column] + i);
					textIndex++;
				}
				
				indexTracker[column] += number;
			}
		}
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, RandomUtil.pickBoolean(), KeyGeneration.createOrder(RandomUtil.pickRandomInt(3, 7)));
	}
}
