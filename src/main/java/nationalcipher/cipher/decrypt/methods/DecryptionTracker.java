package nationalcipher.cipher.decrypt.methods;

import java.util.function.Function;

import javax.annotation.Nullable;

import javalibrary.Output;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.api.IDecryptionTracker;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.NationalCipherUI;

public class DecryptionTracker implements IDecryptionTracker {

    private final CharSequence cipherText;
    private char[] plainText;
    private final IApplication app;
    private double UPPER_ESTIMATE;

    /**
     * Just a place to store the most recent solution
     */
    public Solution bestSolution;
    /**
     * Just a place to store the most recent solution
     */
    @Nullable
    public Solution lastSolution;
    public long iteration;
    private Function<Integer, Integer> outputLength = length -> length;

    public DecryptionTracker(CharSequence cipherText, IApplication app) {
        this.app = app;
        this.iteration = 1;
        this.bestSolution = Solution.WORST_SOLUTION;

        this.cipherText = cipherText;
    }

    public int getOutputTextLength(int inputLength) {
        return this.outputLength.apply(inputLength);
    }
    
    public DecryptionTracker setOutputLength(Function<Integer, Integer> outputLength) {
        this.outputLength = outputLength;
        return this;
    }

    public CharSequence getCipherText() {
        return this.cipherText;
    }
    
    public int getLength() {
        return this.cipherText.length();
    }

    public char[] getPlainTextHolder(boolean parallel) {
        if (this.getSettings().useParallel() && parallel) {
            return this.getNewHolder();
        } else {
            if (this.plainText == null) {
                this.plainText = new char[this.getOutputTextLength(this.cipherText.length())];
                this.UPPER_ESTIMATE = TextFitness.getEstimatedFitness(this.plainText.length, this.getLanguage().getQuadgramData()) * 1.1;
            }
            
            return this.plainText;
        }
    }
    
    public char[] getPlainTextHolder() {
        return this.getPlainTextHolder(true);
    }
    
    public char[] getNewHolder() {
        return new char[this.getOutputTextLength(this.getLength())];
    }

    public void addSolution(Solution solution) {
        // Some quick easy checks
        if (!this.getSettings().collectSolutions() || solution.score <= this.UPPER_ESTIMATE)
            return;

        if (NationalCipherUI.topSolutions.addSolution(solution)) { // Added successfully
            solution.bakeSolution();
        }
    }

    public void resetSolution() {
        this.bestSolution = Solution.WORST_SOLUTION;
        NationalCipherUI.topSolutions.reset();
    }
    
    public void increaseIteration() {
        if (this.getSettings().updateProgress()) {
            if(this.getProgress().increase()) {
                this.getKeyPanel().setIteration(this.getProgress().getValue());
            }
        }
    }

    public void resetIteration() {
        this.iteration = 1;
    }

    public String getBestText() {
        return new String(this.bestSolution.getText());
    }
    
    public Solution getBestSolution() {
        return this.bestSolution;
    }

    // IApplication methods
    public IApplication getApp() {
        return this.app;
    }
    
    public ILanguage getLanguage() {
        return this.app.getLanguage();
    }

    public Settings getSettings() {
        return this.app.getSettings();
    }

    public ProgressValue getProgress() {
        return this.app.getProgress();
    }

    public Output out() {
        return this.app.out();
    }

    public KeyPanel getKeyPanel() {
        return this.app.getKeyPanel();
    }
    
    public boolean shouldStop() {
        return this.app.shouldStop();
    }

    public void finish() {
        this.getProgress().finish();
        this.getKeyPanel().setIteration(this.getProgress().getValue());
    }
}
