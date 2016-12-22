package nationalcipher.cipher.decrypt.methods;

import java.util.Arrays;

import javalibrary.fitness.NGramData;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.list.ResultNegative;
import javalibrary.util.ArrayUtil;

public class Solution extends ResultNegative {
	
	private byte[] text;
	private boolean beenBaked;
	public String keyString;
	public static final String UNKNOWN_KEY = "UNKNOWN";
	
	public Solution(byte[] text, double score) {
		super(score);
		this.text = text;
		this.beenBaked = false;
		this.keyString = UNKNOWN_KEY;
	}
	
	public Solution() {
		this(new byte[0], Double.NEGATIVE_INFINITY);
	}
	
	//public Solution(char[] text, ILanguage language) {
	//	this(ArrayUtil.convertCharType(text), language);
	//}
	
	public Solution(byte[] text, ILanguage language) {
		this(text, TextFitness.scoreFitnessQuadgrams(text, language));
	}
	
	public Solution(byte[] text, NGramData nGramData) {
		this(text, TextFitness.scoreFitness(text, nGramData));
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
		if(!this.beenBaked) {
			this.beenBaked = true;
			this.text = ArrayUtil.copy(this.text);
		}
		return this;
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