package nationalcipher.cipher.base.enigma;

import java.lang.reflect.InvocationTargetException;

import javalibrary.util.ArrayUtil;

public class EnigmaMachine {

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
	
	public EnigmaMachine(String name) {
		this.name = name;
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
	
	public final int getNumberOfRotors() {
		return this.rotorCount;
	}
	
	public final int getNumberOfReflectors() {
		return this.reflectorCount;
	}
	
	public boolean canPlugboard() {
		return false;
	}
	
	public EnigmaMachine createWithPlugboard(char[]... input) {
		EnigmaMachine copy = this.copy();
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
	
	public EnigmaMachine copy() {
		try {
			return this.getClass().getConstructor(String.class).newInstance(this.name);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
