package nationalciphernew.cipher;

import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;

public class Solution {
	
	public final char[] text;
	public final double score;
	
	public Solution(char[] text, double score) {
		this.text = text;
		this.score = score;
	}
	
	public Solution(char[] text, ILanguage language) {
		this.text = text;
		this.score = TextFitness.scoreFitnessQuadgrams(text, language);
	}
}