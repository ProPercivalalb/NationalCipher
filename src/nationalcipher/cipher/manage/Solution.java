package nationalcipher.cipher.manage;

import java.util.Arrays;

import javalibrary.cipher.stats.WordSplit;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.util.ArrayUtil;

public class Solution implements Comparable<Solution> {
	
	private char[] text;
	public final double score;
	public String keyString;
	public static final String UNKNOWN_KEY = "UNKNOWN";
	
	public Solution(char[] text, double score) {
		this.text = text;
		this.score = score;
		this.keyString = UNKNOWN_KEY;
	}
	
	public Solution() {
		this(new char[0], Double.NEGATIVE_INFINITY);
	}
	
	public Solution(char[] text, ILanguage language) {
		this(text, TextFitness.scoreFitnessQuadgrams(text, language));
	}
	
	public Solution(char[] text, ILanguage language, double currentLowest) {
		this(text, TextFitness.scoreFitnessQuadgrams(text, language, currentLowest));
	}
	
	public Solution setKeyString(String keyString) {
		this.keyString = keyString;
		return this;
	}
	
	public Solution setKeyString(String keyString, Object... args) {
		return this.setKeyString(String.format(keyString, args));
	}
	
	public Solution copyTextInstance() {
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

	public char[] getText() {
		return this.text;
	}
}