package nationalcipher.old;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.string.StringTransformer;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.util.RandomUtil;
import nationalcipher.Settings;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class GeneralPeriodDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "General Period";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		SubstitutionTask task = new SubstitutionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			Substitution2Task task2 = new Substitution2Task(new char[0], settings, keyPanel, output, progress);
			
			task.bestKey = new String[task.period];
			task.bestMaximaKey = new String[task.period];
			task.lastKey = new String[task.period];
			
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			for(int i = 0; i < task2.period; ++i) {
		    	String temp = StringTransformer.getEveryNthChar(text, i, task2.period);
		    	task2.cipherText = temp.toCharArray();
				task2.run();
				task.bestMaximaKey[i] = task2.bestKey;
				task.lastKey[i] = task.bestMaximaKey[i];
			}

			
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			task.resetSolution();
			task.run();
			
			
			
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	private JTextField rangeBox = new JTextField("5");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		
		panel.add(new SubOptionPanel("Period:", this.rangeBox));
        
		dialog.add(panel);
	}

	public class SubstitutionTask extends SimulatedAnnealing {

		public int period;
		public String[] bestKey, bestMaximaKey, lastKey;
		
		public SubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.period = SettingParse.getInteger(rangeBox);
		}

		public Solution generateKey() {
			
			
			return new Solution(GeneralPeriod.decode(this.cipherText, this.bestMaximaKey), this.settings.getLanguage());
		}

		public Solution modifyKey(double temp, int count, double lastDF) {
			int index = count % this.period;
			this.lastKey[index] = KeySquareManipulation.exchange2letters(this.bestMaximaKey[index]);
			return new Solution(GeneralPeriod.decode(this.cipherText, this.lastKey), this.settings.getLanguage());
		}

		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			//this.keyPanel.key.setText(this.bestKey);
		}
		
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.updateIteration(this.iteration++);
		}

		public boolean endIteration() {
			this.output.println("Best Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, this.bestKey, new String(this.bestSolution.getText()));
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.progress.setValue(0);
			return true;
		}
	}
	
	public class Substitution2Task extends InternalDecryption {

		public int period;
		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public Substitution2Task(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.period = SettingParse.getInteger(rangeBox);
		}

		public Solution maxSolution;
		
		public void run() {
			while(true) {
				this.bestSolution = this.generateKey();
				this.maxSolution = this.bestSolution;

				for(double TEMP = this.settings.getSATempStart(); TEMP >= 0; TEMP -= this.settings.getSATempStep()) {
					for(int count = 0; count < this.settings.getSACount(); count++) { 
						
						this.lastSolution = this.modifyKey(count);
						//this.addSolution(this.lastSolution);
						
						double score = this.lastSolution.score;
						double dF = score - this.maxSolution.score;
						
					    if(dF >= 0) {
					    	this.maxSolution = this.lastSolution;
					        this.storeKey();
					    }
					    else if(TEMP > 0) { 
					    	double prob = Math.exp(dF / TEMP);
					        if(prob > RandomUtil.pickDouble()) {
					        	this.maxSolution = this.lastSolution;
					        	this.storeKey();
					        }
						}
					    
						if(this.maxSolution.score > this.bestSolution.score) {
							this.bestSolution = this.maxSolution;
							this.solutionFound();
						}
						
						this.onIteration();
					}
				}

				if(this.endIteration()) break;
			}	
		}

		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKey();
			return new HalfSolution(Keyword.decode(this.cipherText, this.bestMaximaKey), this.settings.getLanguage());
		}

		public Solution modifyKey(double temp, int count, double lastDF) {
			this.lastKey = KeySquareManipulation.exchange2letters(this.bestMaximaKey);
			return new HalfSolution(Keyword.decode(this.cipherText, this.lastKey), this.settings.getLanguage());
		}

		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey);
		}
		
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.updateIteration(this.iteration++);
		}

		public boolean endIteration() {
			this.output.println("Best Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, this.bestKey, new String(this.bestSolution.getText()));
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.progress.setValue(0);
			return true;
		}
	}
	
	public static class GeneralPeriod {
		
		public static char[] decode(char[] cipherText, String[] keys) {
			char[] plainText = new char[cipherText.length];
			
			for(int i = 0; i < cipherText.length; i++) {
				String key = keys[i % keys.length];
				plainText[i] = (char)(key.indexOf(cipherText[i]) + 'A');
				
			}
			
			return plainText;
		}
	}
	
	public class HalfSolution extends Solution {
		
		public HalfSolution(char[] text, ILanguage language) {
			super(text, -ChiSquared.calculate(text, language));
		}
		
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
