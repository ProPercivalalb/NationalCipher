package nationalcipher.cipher.base.enigma;

import java.util.Arrays;

import javalibrary.util.ArrayUtil;

public class EnigmaMachine {

	//TODO CHANGE ALL THE PUBLIC FIELDS TO PRIVATE
	public String name;
	
	public char[][] rotors;
	public char[][] rotorsInverse;
	public int[][] notches;
	public int rotorCount;
	
	public char[][] reflector;
	public String[] reflectorNames;
	public int reflectorCount;
	
	public char[] etw;
	public char[] etwInverse;
	
	public char[][] thinRotor;
	public char[][] thinRotorInverse;
	public int thinRotorCount;
	
	public boolean canPlugboard;
	public boolean hasThinRotor;
	public boolean stepping;
	
	public EnigmaMachine(String name) {
		this.name = name;
		this.canPlugboard = false;
		this.stepping = true;
		this.hasThinRotor = false;
	}
	
	public final void setRotors(String... input) {
		char[][] normal = new char[input.length][26];
		char[][] inverse = new char[normal.length][26];
		
		for(int r = 0; r < input.length; r++) {
			for(int i = 0; i < 26; i++) {
				normal[r][i] = input[r].charAt(i);
				inverse[r][normal[r][i] - 'A'] = (char)(i + 'A');
			}
		}
		
		this.rotors = normal;
		this.rotorsInverse = inverse;
		this.rotorCount = input.length;
	}
	
	public final void setNotches(int[][] input) {
		this.notches = input;
	}
	
	public final void setNotches(String... input) {
		int[][] normal = new int[input.length][];
		
		for(int r = 0; r < input.length; r++)  {
			normal[r] = new int[input[r].length()];
			for(int i = 0; i < normal[r].length; i++) 
				normal[r][i] = input[r].charAt(i) - 'A';
		}

		this.notches = normal;
	}
	
	public final void setReflectors(String... input) {
		char[][] normal = new char[input.length][];
		
		for(int r = 0; r < input.length; r++)
			normal[r] = input[r].toCharArray();
			
		this.reflector = normal;
		this.reflectorCount = input.length;
	}
	
	public final void setReflectorNames(String... input) {
		this.reflectorNames = input;
		this.reflectorCount = input.length;
	}

	public void setETW(String input) {
		this.etw = input.toCharArray();
		
		char[] inverse = new char[26];
		for(int i = 0; i < 26; i++)
			inverse[this.etw[i] - 'A'] = (char)(i + 'A');
			
		this.etwInverse = inverse;
	}
	
	public final void setThinRotors(String... input) {
		char[][] normal = new char[input.length][26];
		char[][] inverse = new char[normal.length][26];
		
		for(int r = 0; r < input.length; r++) {
			for(int i = 0; i < 26; i++) {
				normal[r][i] = input[r].charAt(i);
				inverse[r][normal[r][i] - 'A'] = (char)(i + 'A');
			}
		}
		
		this.thinRotor = normal;
		this.thinRotorInverse = inverse;
		this.thinRotorCount = input.length;
	}
	
	public final int getNumberOfRotors() {
		return this.rotorCount;
	}
	
	public final int getNumberOfReflectors() {
		return this.reflectorCount;
	}
	
	public final int getNumberOfThinRotors() {
		return this.thinRotorCount;
	}
	
	public final boolean canPlugboard() {
		return this.canPlugboard;
	}
	
	public boolean hasThinRotor() {
		return this.hasThinRotor;
	}
	
	/**
	 * True is the classic Ratchets setting and False is cogs
	 * @return
	 */
	public final boolean getStepping() {
		return this.stepping;
	}
	
	public boolean isSolvable() {
		return true;
	}
	
	public EnigmaMachine createWithPlugboard(char[]... input) {
		EnigmaMachine copy = new EnigmaPlugboard(this.name);
		copy.rotors = this.rotors;
		copy.rotorsInverse = this.rotorsInverse;
		copy.rotorCount = this.rotorCount;
		copy.notches = this.notches;
		copy.reflector = this.reflector;
		copy.reflectorNames = this.reflectorNames;
		copy.reflectorCount = this.reflectorCount;
		
		copy.canPlugboard = this.canPlugboard;
		copy.stepping = copy.stepping;
		
		char[] plugBoardArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		for(char[] swap : input) {
			if(swap[0] == 0 || swap[1] == 0) continue;
			int ichar = ArrayUtil.indexOf(plugBoardArray, swap[0]);
			int jchar = ArrayUtil.indexOf(plugBoardArray, swap[1]);
			char temp = plugBoardArray[jchar];
			plugBoardArray[jchar] = plugBoardArray[ichar];
			plugBoardArray[ichar] = temp;
		}
		
		copy.etw = plugBoardArray;
		copy.etwInverse = plugBoardArray;
		
		return copy;
	}
	
	public static class EnigmaPlugboard extends EnigmaMachine {

		public EnigmaPlugboard(String name) {
			super(name);
		}
		
		@Override
		public String toString() {
			return String.format("%s, Plugboard:%s", this.name, new String(this.etw));
		} 
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
