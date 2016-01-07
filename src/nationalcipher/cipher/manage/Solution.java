package nationalcipher.cipher.manage;

import java.util.Arrays;

import javalibrary.cipher.stats.WordSplit;
import javalibrary.fitness.ChiSquared;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;

public class Solution implements Comparable<Solution> {
	
	public final char[] text;
	public final double score;
	public String keyString;
	
	public Solution(char[] text, double score) {
		this.text = text;
		this.score = score;
		this.keyString = "UNKNOWN";
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
	
	public Solution(char[] text) {
		WordSplit.splitText(text);
		this.text = text;
		this.score = WordSplit.lastScore;
		this.keyString = "UNKNOWN";
	}
	
	public Solution setKeyString(String keyString) {
		this.keyString = keyString;
		return this;
	}
	
	public Solution setKeyString(String keyString, Object... args) {
		this.keyString = String.format(keyString, args);
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
	    if(!Arrays.equals(this.text, other.text))
	    	return false;
	    return true;
		
	}
	
	@Override
	public String toString() {
		return String.format("Fitness: %f, Key: %s, Plaintext: %s", this.score, this.keyString, new String(this.text));
	}
}