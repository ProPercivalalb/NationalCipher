package nationalcipher.cipher.base.substitution;

import javalibrary.fitness.TextFitness;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Enigma implements IRandEncrypter {

	public static void main(String[] args) {
		Languages.english.loadNGramData();
		System.out.println(encode("A", "DWR", "AAP", new int[] {1, 4, 3}));
		//System.out.println(encode("OLPFNEZBZLXTC", "AAA", "AAA", new int[] {0, 1, 2}, "CI"));
		Timer timer = new Timer();
		//iterateMyszkowski(3, 0, "");
		iterateRingSetting(3, 0, "");
		timer.displayTime();
		//OLPFDEZJJLXTI
	}
	
	private static void iterateRingSetting(int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i++) {
			String backup = key;
			backup += i;
			
			if(time + 1 >= no) {
				String s = encode("NPNKANVHWKPXORCDDTRJRXSJFLCIUAIIBUNQIUQFTHLOZOIMENDNGPCB", "DXR", backup, stringToOrder("143"));
				double score = TextFitness.scoreFitnessQuadgrams(s, Languages.english);
				if(score > bestScore) {
					bestScore = score;
					System.out.println(score + " " + s + " " + backup + " ");

				}
				//if(backup.startsWith("AA"))
				//	System.out.println(score + " " + s + " " + backup + " ");
				continue;
			}
			
			iterateRingSetting(no, time + 1, backup);
		}
	}
	
	private static void iterateMyszkowski(int no, int time, String key) {
		for(char i = 'A'; i <= 'Z'; i++) {
			String backup = key;
			backup += i;
			
			if(time + 1 >= no) {
				//System.out.println(backup);
				iterateCadenus(new char[] {'0', '1', '2', '3', '4', '5', '6', '7'}, 3, 0, "", backup);
				continue;
			}
			
			iterateMyszkowski(no, time + 1, backup);
		}
	}
	
	public static double bestScore = Double.NEGATIVE_INFINITY;
	
	private static void iterateCadenus(char[] characters, int no, int time, String key, String indicator) {
		for(char character : characters) {
			String backup = key;
			if(key.contains("" + character))
				continue;
			
			backup += character;
			
			if(time + 1 >= no) {
				String s = encode("NPNKANVHWKPXORCDDTRJRXSJFLCIUAIIBUNQIUQFTHLOZOIMENDNGPCB", indicator, "AAA", stringToOrder(backup));
				double score = TextFitness.scoreFitnessQuadgrams(s, Languages.english);
				if(score > bestScore) {
					bestScore = score;
					System.out.println(score + " " + s + " " + backup + " " +indicator );

				}
				//System.out.println(s);
				//System.out.println(backup);
				continue;
			}
			
			iterateCadenus(characters, no, time + 1, backup, indicator);
		}
	}
	
	public static int[] stringToOrder(String rotors) {
		int[] order = new int[3];
		for(int i = 0; i < order.length; i++)
			order[i] = rotors.charAt(i) - '0';
		return order;
	}
	
	public static int[][] NOTCHES = new int[][] {{16},{4},{21},{9},{25},{25,12},{25,12},{25,12}};
	public static char[][] KEYS = new char[][] {"EKMFLGDQVZNTOWYHXUSPAIBRCJ".toCharArray(),"AJDKSIRUXBLHWTMCQGZNPYFVOE".toCharArray(),"BDFHJLCPRTXVZNYEIWGAKMUSQO".toCharArray(),"ESOVPZJAYQUIRHXLNFTGKDCMWB".toCharArray(),"VZBRGITYUPSDNHLXAWMJQOFECK".toCharArray(),"JPGVOUMFYQBENHZRDKASXLICTW".toCharArray(),"NZJHGRCXMYSWBOUFAIVLPEKQDT".toCharArray(),"FKQHTLXOCBJSPDZRAMEWNIUYGV".toCharArray()};
	public static char[][] KEYS_INVERSE = new char[][] {"UWYGADFPVZBECKMTHXSLRINQOJ".toCharArray(),"AJPCZWRLFBDKOTYUQGENHXMIVS".toCharArray(),"TAGBPCSDQEUFVNZHYIXJWLRKOM".toCharArray(),"HZWVARTNLGUPXQCEJMBSKDYOIF".toCharArray(),"QCYLXWENFTZOSMVJUDKGIARPHB".toCharArray(),"SKXQLHCNWARVGMEBJPTYFDZUIO".toCharArray(),"QMGYVPEDRCWTIANUXFKZOSLHJB".toCharArray(),"QJINSAYDVKBFRUHMCPLEWZTGXO".toCharArray()};
	
	public static char[] REFLECTOR = "YRUHQSLDPXNGOKMIEBFZCWVJAT".toCharArray();
	
	public static String encode(String plainText, String notchSetting, String ringSetting, int[] rotors, String... plugBoardSettings) {
		int[] notchKey = new int[3];
		for(int i = 0; i < notchKey.length; i++)
			notchKey[i] = notchSetting.charAt(i) - 'A';
		
		int[] ring = new int[3];
		for(int i = 0; i < ring.length; i++)
			ring[i] = ringSetting.charAt(i) - 'A';
		
		return new String(decode(plainText.toCharArray(), new byte[plainText.length()], notchKey, ring, rotors));//TODO, plugBoardSettings));
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, int[] indicator, int[] ring, int[] rotors, char[]... plugBoardSettings) {
		
		char[] plugBoardArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		for(char[] swap : plugBoardSettings) {
			if(swap[0] == 0 || swap[1] == 0) continue;
			int ichar = ArrayUtil.indexOf(plugBoardArray, swap[0]);
			int jchar = ArrayUtil.indexOf(plugBoardArray, swap[1]);
			char temp = plugBoardArray[jchar]; 
			plugBoardArray[jchar] = plugBoardArray[ichar];
			plugBoardArray[ichar] = temp;
		}
		 
		for(int i = 0; i < cipherText.length; i++) {
			//Next settings
			int[] middleNotches = NOTCHES[rotors[1]];
			int[] endNotches = NOTCHES[rotors[2]];
			
			if(ArrayUtil.contains(middleNotches, indicator[1])) {
				indicator[0] = (indicator[0] + 1) % 26;
			    indicator[1] = (indicator[1] + 1) % 26;
			}
	
			if(ArrayUtil.contains(endNotches, indicator[2]))
				indicator[1] = (indicator[1] + 1) % 26;
			
		    indicator[2] = (indicator[2] + 1) % 26;
			
		    char ch = cipherText[i];
		    
		    if(plugBoardSettings.length > 0)
		    	ch = nextCharacter(ch, plugBoardArray);
		    
		    for(int r = 2; r >= 0; r--)
		    	ch = nextCharacter(ch, KEYS[rotors[r]], indicator[r] - ring[r]);
		    
		    ch = nextCharacter(ch, REFLECTOR);
	
		    for(int r = 0; r < 3; r++)
		    	ch = nextCharacter(ch, KEYS_INVERSE[rotors[r]], indicator[r] - ring[r]);
		    
		    if(plugBoardSettings.length > 0)
		    	ch = nextCharacter(ch, plugBoardArray);
		    
		    plainText[i] = (byte)ch;
		}
		
		return plainText;
	}
	
	//Used for a plugboard
	public static byte[] decode(char[] cipherText, byte[] plainText, int[] indicator, int[] ring, int[] rotors, char[] plugBoard) {
		 
		for(int i = 0; i < cipherText.length; i++) {
			//Next settings
			int[] middleNotches = NOTCHES[rotors[1]];
			int[] endNotches = NOTCHES[rotors[2]];
			
			if(ArrayUtil.contains(middleNotches, indicator[1])) {
				indicator[0] = (indicator[0] + 1) % 26;
			    indicator[1] = (indicator[1] + 1) % 26;
			}
	
			if(ArrayUtil.contains(endNotches, indicator[2]))
				indicator[1] = (indicator[1] + 1) % 26;
			
		    indicator[2] = (indicator[2] + 1) % 26;
			
		    char ch = cipherText[i];
		    
		    ch = nextCharacter(ch, plugBoard);
		    
		    for(int r = 2; r >= 0; r--)
		    	ch = nextCharacter(ch, KEYS[rotors[r]], indicator[r] - ring[r]);
		    
		    ch = nextCharacter(ch, REFLECTOR);
	
		    for(int r = 0; r < 3; r++)
		    	ch = nextCharacter(ch, KEYS_INVERSE[rotors[r]], indicator[r] - ring[r]);
		    
		    ch = nextCharacter(ch, plugBoard);
		    
		    plainText[i] = (byte)ch;
		}
		
		return plainText;
	}
	
	public static char nextCharacter(char ch, char[] key) {
		return key[ch - 'A'];
	}
	
	public static char nextCharacter(char ch, char[] key, int offset) {
		return (char)(((key[((ch - 'A') + 26 + offset) % 26] - 'A') + 26 - offset) % 26 + 'A');
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(3), KeyGeneration.createShortKey26(3), KeyGeneration.createOrder(3));
	}
}
