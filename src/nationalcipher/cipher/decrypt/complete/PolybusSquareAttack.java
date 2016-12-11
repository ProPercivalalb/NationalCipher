package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.SubstitutionHack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.ui.IApplication;

public class PolybusSquareAttack extends CipherAttack {
	
	public PolybusSquareAttack() {
		super("Polybus Square");
		this.setAttackMethods(DecryptionMethod.CALCULATED);
	}

	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		PolybusSquareTask task = new PolybusSquareTask(text, app);
		
		if(method == DecryptionMethod.CALCULATED) {

			List<String> split = new ArrayList<String>();
			
			//int 
			for(int i = 0; i < task.cipherText.length; i+=2) {
				split.add(task.cipherText[i] + "" + task.cipherText[i + 1]);
			}
			
			app.out().println("" + split);
			
			char[] tempText = new char[split.size()];
			HashSet<String> nonDupList = new HashSet<String>(split);
			int t = 0;
			char[] alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray();
			
			for(String nonDup : nonDupList) {
				for(int i = 0; i < split.size(); i++) 
					if(split.get(i).equals(nonDup))
						tempText[i] = alphabet[t];

				t++;
			}
				
			SubstitutionHack substitutionHack = new SubstitutionHack(tempText, app) {
				@Override
				public char[] getAlphabet() {
					return alphabet;
				}
			};
			substitutionHack.run();
				
			if(substitutionHack.bestSolution.score >= task.bestSolution.score) {
				task.bestSolution = substitutionHack.bestSolution;
				task.bestSolution.bakeSolution();
				task.out().println("%s", task.bestSolution);	
				task.getKeyPanel().updateSolution(task.bestSolution);
			}
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class PolybusSquareTask extends InternalDecryption {
		
		public PolybusSquareTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
	}
}
