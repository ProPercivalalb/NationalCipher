package nationalcipher.cipher.decrypt.methods;

import java.util.Arrays;

import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.util.ArrayUtil;

public class Solution implements Comparable<Solution> {
	
	private byte[] text;
	//Could change to float to reduce memory from 64 bits to 32 bits
	public final double score;
	public String keyString;
	public static final String UNKNOWN_KEY = "UNKNOWN";
	
	
	public Solution(byte[] text, double score) {
		this.text = text;
		this.score = score;
		this.keyString = UNKNOWN_KEY;
	}
	
	public Solution() {
		this(new byte[0], Double.NEGATIVE_INFINITY);
	}
	
	public Solution(char[] text, ILanguage language) {
		this(ArrayUtil.convertCharType(text), language);
	}
	
	public Solution(byte[] text, ILanguage language) {
		this(text, TextFitness.scoreFitnessQuadgrams(text, language));
	}
	
	//public Solution(char[] text, ILanguage language, Solution bestSolution) {
	//	this(text, language, bestSolution.score);
	//}
	
	//public Solution(char[] text, ILanguage language, double bestScore) {
	//	this(text, TextFitness.scoreFitnessQuadgrams(text, language, bestScore));
	//}
	
	public Solution setKeyString(String keyString) {
		this.keyString = keyString;
		return this;
	}
	
	public Solution setKeyString(String keyString, Object... args) {
		return this.setKeyString(String.format(keyString, args));
	}
	
	public Solution bakeSolution() {
		this.text = ArrayUtil.copyOfRange(this.text, 0, this.text.length);
		return this;
	}

	@Override
	public int compareTo(Solution o) {
		double dF = o.score - this.score;
		return dF == 0 ? 0 : dF > 0 ? 1 : -1;
	}
	
	@Override
	public int hashCode() {
	    return Arrays.hashCode(this.text);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
	    if(this.getClass() != obj.getClass())
	    	return false;
	    Solution other = (Solution)obj;
	    if(!Arrays.equals(this.getText(), other.getText()))
	    	return false;
	    return true;
		
	}
	
	@Override
	public String toString() {
		return String.format("Fitness: %f, Key: %s, Plaintext: %s", this.score, this.keyString, new String(this.text));
	}

	public byte[] getText() {
		return this.text;
	}
}