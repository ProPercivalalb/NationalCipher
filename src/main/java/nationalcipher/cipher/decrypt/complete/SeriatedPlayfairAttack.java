package nationalcipher.cipher.decrypt.complete;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.SeriatedPlayfair;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack;
import nationalcipher.cipher.decrypt.methods.DictionaryAttack.DictionaryKey;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.transposition.RouteCipherType;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.NationalCipherUI;

public class SeriatedPlayfairAttack extends CipherAttack {

	public JSpinner spinner;
	
	public SeriatedPlayfairAttack() {
		super("Seriated Playfair");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.DICTIONARY);
		this.spinner = JSpinnerUtil.createSpinner(ArrayUtil.concat(new Integer[] {0}, ArrayUtil.createRangeInteger(2, 101)));
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period:", this.spinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		SeriatedPlayfairTask task = new SeriatedPlayfairTask(text, app);
		
		//Settings grab
		task.period = SettingParse.getInteger(this.spinner);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			
			DictionaryAttack.tryKeysWithOptions(task, Dictionary.WORDS_CHAR, KeyGeneration.ALL_25_CHARS, 5, 5, app.getSettings().checkShift(), app.getSettings().checkReverse(), app.getSettings().checkRoutes());
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class SeriatedPlayfairTask extends SimulatedAnnealing implements DictionaryKey {

		public int period;
		public String bestKey, bestMaximaKey, lastKey;
		
		public SeriatedPlayfairTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onKeyCreation(char[] complete, char[] word, int shift, boolean reversed, RouteCipherType route) {
			this.lastSolution = new Solution(SeriatedPlayfair.decode(this.cipherText, this.plainText, complete, this.period), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s, p:%d", new String(complete), this.period);
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeyGeneration.createLongKey25();
			return new Solution(SeriatedPlayfair.decode(this.cipherText, this.plainText, this.bestMaximaKey, this.period), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeyManipulation.modifyKey(this.bestMaximaKey, 5, 5);
			return new Solution(SeriatedPlayfair.decode(this.cipherText, this.plainText, this.lastKey, this.period), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString("%s, p:%d", this.bestKey, this.period);
			this.bestSolution.bakeSolution();
			this.getKeyPanel().updateSolution(this.bestSolution);
		}
		
		@Override
		public void onIteration() {
			this.getProgress().increase();
			this.getKeyPanel().updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.out().println("%s", this.bestSolution);
			NationalCipherUI.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
}
