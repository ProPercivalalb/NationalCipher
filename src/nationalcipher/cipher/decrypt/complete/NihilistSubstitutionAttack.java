package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;

public class NihilistSubstitutionAttack extends CipherAttack {

	public JSpinner spinner;
	
	public NihilistSubstitutionAttack() {
		super("Nihilist Substitution");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
		this.spinner = JSpinnerUtil.createSpinner(3, 2, 100, 1);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period:", this.spinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		NihilistSubstitutionTask task = new NihilistSubstitutionTask(text, app);
		
		//Settings grab
		task.period = SettingParse.getInteger(this.spinner);
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			
			List<Integer> list = new ArrayList<Integer>();
			for(int i = 0; i < task.cipherText.length / 2; i++)
				list.add(Integer.valueOf(new String(task.cipherText, i * 2, 2)));
			
			char[] tempText = new char[task.cipherText.length / 2];
			
			boolean error = false;
			
			for(int i = 0; i < task.period; i++) {
				List<Integer> old = getEveryNthChar(list, i, task.period);
				List<Integer> key = getBestKey(old, i);
				if(key.size() == 2) {
					app.out().println("PeriodColumn %d -- %d%d", i, key.get(0), key.get(1));
					
					
					int count = 0;
					for(int no : old) {
						if(no <= 10) no += 100;
						int newInt = no -= (key.get(0) * 10 + key.get(1));
						
						int alphaInt = ((int)(newInt / 10) - 1) * 5 + ((newInt % 10) - 1);
						tempText[count * task.period + i] = "ABCDEFGHIKLMNOPQRSTUVWXYZ".charAt(alphaInt);
						count += 1;
					}
				}
				else {
					if(key.get(0) == key.get(1))
						app.out().println("PeriodColumn %d -- Row: %d Column: %d-%d", i, key.get(0), key.get(2), key.get(3));
					else if(key.get(2) == key.get(3))
						app.out().println("PeriodColumn %d -- Row: %d-%d Column: %d", i, key.get(0), key.get(1), key.get(2));
					else 
						app.out().println("PeriodColumn %d -- Row: %d-%d Column: %d-%d", i, key.get(0), key.get(1), key.get(2), key.get(3));
					
					error = true;
				}
				

			}
			if(!error)
				new SimpleSubstitutionAttack().attemptAttack(new String(tempText), DecryptionMethod.SIMULATED_ANNEALING, app);
			else {
				app.out().println("Cannot complete decryption");
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static List<Integer> getEveryNthChar(List<Integer> old, int start, int n) {
		List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < old.size(); ++i) {
            if((i % n) == start) {
            	list.add(old.get(i));
            }
        }
        return list;
    }
	
	public List<Integer> getBestKey(List<Integer> nos, int periodColumn) {
		int rowMin = 1;
		int rowMax = 5;
		int colMin = 1;
		int colMax = 5;
		
		for(int no : nos) {
			if(no <= 10)
				no += 100;
			
			int col = no % 10;
			if(col == 0) {
				colMin = 5;
				colMax = 5;
				no -= 10;
			}
			else if(col - 1 <= 5) {
				colMin = Math.max(1, colMin);
				colMax = Math.min(col - 1, Math.max(colMin, colMax));
			}
			else {
				colMin = Math.max(col - 5, colMin);
				colMax = Math.min(5, Math.max(colMin, colMax));
			}
	
			
			int row = (int)(no / 10) % 10;
			if(row == 0) {
				rowMin = 5;
				rowMax = 5;
			}
			else if(row - 1 <= 5) {
				rowMin = Math.max(1, rowMin);
				rowMax = Math.min(row - 1, Math.max(rowMin, rowMax));
			}
			else {
				rowMin = Math.max(row - 5, rowMin);
				rowMax = Math.min(5, Math.max(rowMin, rowMax));
			}

		}

		if(rowMin == rowMax && colMin == colMax)
			return Arrays.asList(rowMin, colMin);
		
		else
			return Arrays.asList(rowMin,rowMax, colMin, colMax);
	}
	
	public static class NihilistSubstitutionTask extends InternalDecryption {

		public int period;
		
		public NihilistSubstitutionTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
	}
}
