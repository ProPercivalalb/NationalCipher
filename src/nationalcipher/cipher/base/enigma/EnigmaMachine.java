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
	public boolean canUhr;
	public boolean hasThinRotor;
	public boolean stepping;
	
	public EnigmaMachine(String name) {
		this.name = name;
		this.canPlugboard = false;
		this.canUhr = false;
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
	
	public final boolean canUhr() {
		return this.canUhr;
	}
	
	public final boolean hasThinRotor() {
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
		EnigmaMachine.copy(this, copy);
		
		char[] plugBoardArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		for(char[] swap : input) {
			if(swap[0] == 0 || swap[1] == 0) continue;
			
			plugBoardArray[swap[0] - 'A'] = swap[1];
			plugBoardArray[swap[1] - 'A'] = swap[0];
		}
		
		copy.etw = plugBoardArray;
		copy.etwInverse = plugBoardArray;
		
		return copy;
	}
	
	public EnigmaMachine createWithUhr(int setting, char[][] input) {
		EnigmaMachine copy = new EnigmaUhr(this.name, input);
		EnigmaMachine.copy(this, copy);

		char[] plugBoardArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		for(char[] swap : input) {
			if(swap[0] == 0 || swap[1] == 0) continue;
			int uhrIndex = swap[2] - '0';
			
			int aWire = (uhrIndex * 4 + setting) % 40;
			int bWirePosition = (EnigmaLib.AB_WIRING[aWire] + (40 - setting)) % 40;
			plugBoardArray[swap[0] - 'A'] = input[EnigmaLib.B_PLUG_ORDER[bWirePosition / 4]][1];

			int bWire = (EnigmaLib.B_PLUG_ORDER_INDEXED[uhrIndex] * 4 + setting) % 40;
			int aWirePosition = (EnigmaLib.AB_WIRING_INDEXED[bWire] + (40 - setting)) % 40;
			plugBoardArray[swap[1] - 'A'] = input[aWirePosition / 4][0];
		}
		
		char[] inverse = new char[26];
		for(int i = 0; i < 26; i++)
			inverse[plugBoardArray[i] - 'A'] = (char)(i + 'A');
		
		copy.etwInverse = inverse;
		copy.etw = plugBoardArray;
		
		return copy;
	}
	
	public EnigmaMachine createWithUhr(int setting, String... input) {
		if(input.length == 10) {
			char[][] raw = new char[10][3];
			for(int i = 0; i < input.length; i++)
				raw[i] = input[i].toCharArray();
			return createWithUhr(setting, raw);
		}
		return null;
	}
	
	public static void copy(EnigmaMachine orignal, EnigmaMachine copy) {
		copy.rotors = orignal.rotors;
		copy.rotorsInverse = orignal.rotorsInverse;
		copy.rotorCount = orignal.rotorCount;
		copy.notches = orignal.notches;
		copy.reflector = orignal.reflector;
		copy.reflectorNames = orignal.reflectorNames;
		copy.reflectorCount = orignal.reflectorCount;
		copy.etw = orignal.etw;
		copy.etwInverse = orignal.etwInverse;
		copy.thinRotor = orignal.thinRotor;
		copy.thinRotorInverse = orignal.thinRotorInverse;
		copy.thinRotorCount = orignal.thinRotorCount;
		copy.canPlugboard = orignal.canPlugboard;
		copy.canUhr = orignal.canUhr;
		copy.hasThinRotor = orignal.hasThinRotor;
		copy.stepping = orignal.stepping;
	}
	
	public static class EnigmaPlugboard extends EnigmaMachine {

		public int plugCount;
		
		public EnigmaPlugboard(String name) {
			super(name);
		}
		
		@Override
		public String toString() {
			//char[] plugs = new char[Math.max(this.plugCount * 3 - 1, 0)];
			//Arrays.fill(plugs, ' ');
			//for(int p = 0; p < this.plugCount; p++) {
			//	plugs[p * 3] = plugboard[p][0];
			//	plugs[p * 3 + 1] = plugboard[p][1];
			//}
			
			return String.format("%s, Plugboard:%s", this.name, new String(this.etw));
		} 
	}
	
	public static class EnigmaUhr extends EnigmaMachine {

		public char[][] input;
		
		public EnigmaUhr(String name, char[][] input) {
			super(name);
			this.input = input;
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

	public char nextUhrCharacter(char ch) {
		return ch;
	}
}
