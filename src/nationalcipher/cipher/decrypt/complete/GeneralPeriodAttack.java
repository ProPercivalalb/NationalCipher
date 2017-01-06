package nationalcipher.cipher.decrypt.complete;

import java.util.Arrays;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class GeneralPeriodAttack extends CipherAttack {

	public JSpinner spinner;
	
	public GeneralPeriodAttack() {
		super("General Period Subsitution");
		this.setAttackMethods(DecryptionMethod.CALCULATED, DecryptionMethod.KEY_MANIPULATION);
		this.spinner = JSpinnerUtil.createSpinner(5, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period:", this.spinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		char[] cipherText = text.toCharArray();
		GeneralPeriodTask task = new GeneralPeriodTask(text, app);
		
		//Settings grab
		task.period = SettingParse.getInteger(this.spinner);
		
		if(method == DecryptionMethod.CALCULATED) {
			int rowsMin = task.cipherText.length / task.period;
			int colLeft = task.cipherText.length % task.period;
			
			for(int p = 0; p < task.period; p++) {
				char[] colText = new char[rowsMin + (colLeft > p ? 1 : 0)];
				for(int i = 0; i < colText.length; i++)
					colText[i] = cipherText[i * task.period + p];
				Set<Character> uniqueChars = StringTransformer.getUniqueCharSet(new String(colText));
				app.out().println("Col:%d, UC:%d, %s", p, uniqueChars.size(), new String(colText));
				
				//task.setCipherText(colText);
				//task.run();
			}
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			task.run(task.period, task.period);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class GeneralPeriodTask extends InternalDecryption { ///KeySearch
		
		public int period;
		public String bestKey, bestMaximaKey, lastKey;
		
		public GeneralPeriodTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		public void run(int minLength, int maxLength) {
			for(int length = minLength; length <= maxLength; length++) {
				int rowsMin = this.cipherText.length / length;
				int colLeft = this.cipherText.length % length;
				int[] height = new int[length]; //Number of rows in the 'i'th column
				char[][] keysIndex = new char[length][26];
				
				byte[] editText = ArrayUtil.convertCharType(Arrays.copyOf(this.cipherText, this.cipherText.length));
				
				for(int p = 0; p < length; p++) {
					height[p] = rowsMin + (colLeft > p ? 1 : 0);
					
					for(int i = 0; i < 26; i++) {
						keysIndex[p][i] = (char)(i + 'A');
					}
				}
				
				Solution currentBestSolution = Solution.WORST_SOLUTION;
				
				while(true) {
					for(double TEMP = this.getSettings().getSATempStart(); TEMP >= 0; TEMP -= this.getSettings().getSATempStep()) {
						for(int count = 0; count < this.getSettings().getSACount(); count++) { 
							
							for(int p = 0; p < length; p++) {
								byte ch1 = (byte)RandomUtil.pickRandomInt('A', 'Z');
								byte ch2 = (byte)RandomUtil.pickRandomInt('A', 'Z');
	
								for(int i = 0; i < height[p]; i++) {
									int pos = i * length + p;
									byte ch = editText[pos];
									if(ch == ch1) editText[pos] = ch2;
									else if(ch == ch2) editText[pos] = ch1;
								}
								
								this.lastSolution = new Solution(editText, this.getLanguage()).bakeSolution();

								double lastDF = this.lastSolution.score - currentBestSolution.score;
								
							    if(lastDF >= 0) {
							    	currentBestSolution = this.lastSolution;
									
							    	char temp = keysIndex[p][ch1 - 'A'];
									keysIndex[p][ch1 - 'A'] = keysIndex[p][ch2 - 'A'];
									keysIndex[p][ch2 - 'A'] = temp;
							    }
							    else if(TEMP > 0) { 
							    	double prob = Math.exp(lastDF / TEMP);
							        if(prob > RandomUtil.pickDouble()) {
							        	currentBestSolution = this.lastSolution;
										
										char temp = keysIndex[p][ch1 - 'A'];
										keysIndex[p][ch1 - 'A'] = keysIndex[p][ch2 - 'A'];
										keysIndex[p][ch2 - 'A'] = temp;
						
							        }
							        else {
							        	for(int i = 0; i < height[p]; i++) {
											int pos = i * length + p;
											byte ch = editText[pos];
											if(ch == ch1) editText[pos] = ch2;
											else if(ch == ch2) editText[pos] = ch1;
										}
							        }
								}
							    
								if(currentBestSolution.score > this.bestSolution.score) {
									this.bestSolution = currentBestSolution;
									String str = "";
									for(int j = 0; j < length; j++) {
										str += new String(keysIndex[j]) + ", ";
									}
									this.bestSolution.setKeyString(str);
									this.solutionFound();
								}
							}
							
							
							this.onIteration();
						}
					}
				}
			}
		}

		public void solutionFound() {
			this.out().println("%s", this.bestSolution);
			this.getKeyPanel().updateSolution(this.bestSolution);
		}

		public void onIteration() {
			this.getKeyPanel().updateIteration(this.iteration++);
		}
	}
}
