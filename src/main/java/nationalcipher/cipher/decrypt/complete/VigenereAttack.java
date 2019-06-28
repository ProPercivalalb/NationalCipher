package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.fitness.ChiSquared;
import javalibrary.language.ILanguage;
import javalibrary.math.MathUtil;
import javalibrary.string.StringTransformer;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.VigenereFamily;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.KeySearch;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class VigenereAttack extends CipherAttack {

	public JSpinner[] rangeSpinner;
	
	public VigenereAttack() {
		super("Vigenere");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED, DecryptionMethod.KEY_MANIPULATION);
		this.rangeSpinner = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		VigenereTask task = new VigenereTask(text, app);
		
		//Settings grab
		int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
		    if(app.getSettings().useParallel()) {
		        app.out().println("Generating Keys");
		        //List<String> keys = Collections.synchronizedList(Collections.emptyList());
		        //Arrays.stream(ArrayUtil.createRange(periodRange[0], periodRange[1])).parallel().forEach(length -> KeyIterator.iterateShort26Key(keys::add, length, true));
		        List<String> keys = new ArrayList<>(1000000);
		        for(int length = periodRange[0]; length <= periodRange[1]; ++length)
                    KeyIterator.iterateShort26Key(keys::add, length, true);
		        app.out().println("Generated Keys");

	            keys.stream().parallel().forEach(task::onIterationParallel);
		    } else {
		        for(int length = periodRange[0]; length <= periodRange[1]; ++length)
                    app.getProgress().addMaxValue(MathUtil.pow(26, length));
                
                for(int length = periodRange[0]; length <= periodRange[1]; ++length)
                    KeyIterator.iterateShort26Key(task::onIteration, length, true);
		    }
		} else if(method == DecryptionMethod.CALCULATED) {
		    List<String> keys = new ArrayList<String>(10000);
		    for(int length = periodRange[0]; length <= periodRange[1]; ++length)
		        KeyIterator.iterateShort26Key(keys::add, length, true);
		    
		    keys.stream().parallel().forEach(task::onIterationParallel);
		} else if(method == DecryptionMethod.CALCULATED) {
			int keyLength = StatCalculator.calculateBestKappaIC(text, periodRange[0], periodRange[1], app.getLanguage());
			
			app.getProgress().addMaxValue(keyLength * 26);
			
			String keyword = "";
	        for(int i = 0; i < keyLength; ++i) {
	        	String temp = StringTransformer.getEveryNthChar(text, i, keyLength);
	            int shift = this.findBestCaesarShift(temp.toCharArray(), app.getLanguage(), app.getProgress());
	            keyword += (char)('A' + shift);
	        }
			task.onIteration(keyword);
		} else if(method == DecryptionMethod.KEY_MANIPULATION) {
			app.getProgress().setIndeterminate(true);
			task.run(periodRange[0], periodRange[1]);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public int findBestCaesarShift(char[] text, ILanguage language, ProgressValue progressBar) {
		int best = 0;
	    double smallestSum = Double.MAX_VALUE;
	    for(int shift = 0; shift < 26; ++shift) {
	    	byte[] encodedText = Caesar.decode(text, shift);
	        double currentSum = ChiSquared.calculate(encodedText, language);
	    
	        if(currentSum < smallestSum) {
	        	best = shift;
	            smallestSum = currentSum;
	        }
	            
	        progressBar.increase();
	    }
	    return best;
	}
	
	public class VigenereTask extends KeySearch {

		public VigenereTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		public void onIterationParallel(String key) {
			Solution lastSolution = new Solution(VigenereFamily.decode(this.cipherText, new byte[this.cipherText.length], key, VigenereType.VIGENERE), this.getLanguage());
			
			synchronized(this) {
    			if(lastSolution.score >= this.bestSolution.score) {
    				this.bestSolution = lastSolution;
    				this.bestSolution.setKeyString(key);
    				//this.bestSolution.bakeSolution();
    				this.out().println(this.bestSolution.toString());
    				this.getKeyPanel().updateSolution(this.bestSolution);
    			}
			}
			
			//this.getKeyPanel().updateIteration(this.iteration++);
			//this.getProgress().increase();
		}
		
		public void onIteration(String key) {
            this.lastSolution = new Solution(VigenereFamily.decode(this.cipherText, this.plainText, key, VigenereType.VIGENERE), this.getLanguage());
            
            if(this.lastSolution.score >= this.bestSolution.score) {
                this.bestSolution = this.lastSolution;
                this.bestSolution.setKeyString(key);
                this.bestSolution.bakeSolution();
                this.out().println("%s", this.bestSolution);
                this.getKeyPanel().updateSolution(this.bestSolution);
            }
            
            //this.getKeyPanel().updateIteration(this.iteration++);
            //this.getProgress().increase();
        }
		
		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(VigenereFamily.decode(this.cipherText, this.plainText, key, VigenereType.VIGENERE), this.getLanguage()).setKeyString(key).bakeSolution();
		}
	}
	
	@Override
	public void writeTo(Map<String, Object> map) {
		map.put("vigenere_period_range_min", this.rangeSpinner[0].getValue());
		map.put("vigenere_period_range_max", this.rangeSpinner[1].getValue());
	}

	@Override
	public void readFrom(Map<String, Object> map) {
		if(map.containsKey("vigenere_period_range_max"))
			this.rangeSpinner[1].setValue(map.get("vigenere_period_range_max"));
		if(map.containsKey("vigenere_period_range_min"))
			this.rangeSpinner[0].setValue(map.get("vigenere_period_range_min"));
	}
}
