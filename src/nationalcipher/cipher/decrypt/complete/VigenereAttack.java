package nationalcipher.cipher.decrypt.complete;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.string.StringTransformer;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.cipher.base.polygraphic.Vigenere;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class VigenereAttack extends CipherAttack {

	private JTextField rangeBox;
	
	public VigenereAttack() {
		super("Vigenere");
		this.setAttackMethods(DecryptionMethod.CALCULATED);
		this.rangeBox = new JTextField("2-15");
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
			
		panel.add(new SubOptionPanel("Period Range:", this.rangeBox));
	        
		dialog.add(panel);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		VigenereTask task = new VigenereTask(text, app);
		
		//Settings grab
		int[] range = SettingParse.getIntegerRange(this.rangeBox);
		
		if(method == DecryptionMethod.CALCULATED) {
			int keyLength = StatCalculator.calculateBestKappaIC(text, range[0], range[1], app.getLanguage());
			
			app.getProgress().addMaxValue(keyLength * 26);
			
			String keyword = "";
	        for(int i = 0; i < keyLength; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(text, i, keyLength);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), app.getLanguage(), app.getProgress());
	            keyword += (char)('A' + shift);
	        }
			task.onIteration(keyword);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public int findBestCaesarShift(char[] text, ILanguage language, ProgressValue progressBar) {
		int best = 0;
	    double smallestSum = Double.MAX_VALUE;
	    for(int shift = 0; shift < 26; ++shift) {
	    	char[] encodedText = Caesar.decode(text, shift);
	        double currentSum = ChiSquared.calculate(encodedText, language);
	    
	        if(currentSum < smallestSum) {
	        	best = shift;
	            smallestSum = currentSum;
	        }
	            
	        progressBar.increase();
	    }
	    return best;
	}
	
	public static class VigenereTask extends InternalDecryption {

		public VigenereTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIteration(String key) {
			this.lastSolution = new Solution(Vigenere.decode(this.cipherText, key), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s", key);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}
