package nationalcipher.cipher.decrypt.complete;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.substitution.QuagmireI;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class QuagmireIAttack extends CipherAttack {

	public JSpinner spinner;
	
	public QuagmireIAttack() {
		super("Quagmire I");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
		this.spinner = JSpinnerUtil.createSpinner(2, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period:", this.spinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		QuagmireITask task = new QuagmireITask(text, app);
		
		//Settings grab
		task.period = SettingParse.getInteger(this.spinner);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class QuagmireITask extends SimulatedAnnealing {

		public int period;
		public String bestKey, bestMaximaKey, lastKey;
		
		public QuagmireITask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeyGeneration.createLongKey26();
			return decode(this.bestMaximaKey);
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return decode(this.lastKey);
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString(this.bestKey);
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
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
		
		public Solution decode(String key) {
			//String indicatorKey = "";
			int[] keyIndex = new int[26];
			for(int i = 0; i < 26; i++)
				keyIndex[key.charAt(i) - 'A'] = i;
			
			
			String parent = StringTransformer.repeat("A", this.period);

			Solution currentBestSolution = new Solution();
			Solution lastSolution = new Solution();
			
			while(true) {
				String startParent = parent;
				for(int i = 0; i < this.period; i++) {
					for(char j = 0; j < 26; j++) {
						String child = parent.substring(0, i) + (char)(j + 'A') + parent.substring(i + 1, this.period);
						
						lastSolution = new Solution(QuagmireI.decode(this.cipherText, key, child, 'A'), this.getLanguage());
	
						
						if(lastSolution.score >= currentBestSolution.score) {
							parent = child;
							currentBestSolution = lastSolution;
						}
					}
				}
				
				if(startParent.equals(parent)) 
					break;
			}
			
			/**
	        for(int i = 0; i < this.period; ++i) {
	        	
	        	
	        	String temp = StringTransformer.getEveryNthChar(this.cipherText, i, this.period);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), keyIndex, this.getLanguage());
	            indicatorKey += key.charAt(shift);
	        }
	
	        return Quagmire.decode(this.cipherText, key, indicatorKey, 'A');**/
			return currentBestSolution;
		}
		
		public int findBestCaesarShift(char[] text, int[] keyIndex, ILanguage language) {
			int best = 0;
		    double smallestSum = Double.MAX_VALUE;
		    for(int shift = 0; shift < 26; ++shift) {
		    	char[] encodedText = decode(text, keyIndex, shift);
		        double currentSum = ChiSquared.calculate(encodedText, language);
		    
		        if(currentSum < smallestSum) {
		        	best = shift;
		            smallestSum = currentSum;
		        }
		  
		    }
		    return best;
		}
		
		public char[] decode(char[] cipherText, int[] keyIndex, int shift) {
			char[] plainText = new char[cipherText.length];
			
			for(int i = 0; i < cipherText.length; i++)
				plainText[i] = (char)((26 + keyIndex[cipherText[i] - 'A'] - shift) % 26 + 'A');
			
			return plainText;
		}
	}
}
