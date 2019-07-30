package nationalcipher.cipher.decrypt.methods;

import javalibrary.fitness.NGramData;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.list.ResultNegative;
import javalibrary.util.ArrayUtil;

public class Solution extends ResultNegative {

    public static final Solution WORST_SOLUTION = new Solution(new char[0], Double.NEGATIVE_INFINITY);
    private static final String UNKNOWN_KEY = "UNKNOWN";

    private char[] text;
    /**
     * Indicates whether {@link Solution#text} is a copy and so should be immutable
     */
    private boolean beenBaked;
    public String keyString;

    public Solution(char[] text, double score) {
        super(score);
        this.text = text;
        this.beenBaked = false;
        this.keyString = Solution.UNKNOWN_KEY;
    }

    public Solution(char[] text, ILanguage language) {
        this(text, TextFitness.scoreFitnessQuadgrams(text, language));
    }

    public Solution(char[] text, NGramData nGramData) {
        this(text, TextFitness.scoreFitness(text, nGramData));
    }

    public Solution setKeyString(String keyString) {
        this.keyString = keyString;
        return this;
    }

    public Solution setKeyString(String keyString, Object... args) {
        return this.setKeyString(String.format(keyString, args));
    }

    /**
     * Makes a copy of the text array as quite often this array reference is reused
     * so it can change
     */
    public Solution bake() {
        if (!this.beenBaked) {
            this.beenBaked = true;
            this.text = ArrayUtil.copy(this.text);
        }
        return this;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.score);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Solution other = (Solution) obj;
        if (this.score != other.score)
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