package nationalcipher.cipher.decrypt.complete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javalibrary.string.StringAnalyzer;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.ui.IApplication;

public class StraddleCheckerboardAttack extends CipherAttack {

    public StraddleCheckerboardAttack() {
        super("Straddle Checkerboard");
        this.setAttackMethods(DecryptionMethod.CALCULATED);
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        StraddleCheckerboardTask task = new StraddleCheckerboardTask(text, app);

        if (method == DecryptionMethod.CALCULATED) {
            List<Character> counts = StringAnalyzer.getOrderedCharacterCount(task.cipherText);
            app.out().println("Spare positions: %c %c", counts.get(0), counts.get(1));
            ;
            char first = counts.get(0);
            char second = counts.get(1);

            List<String> split = new ArrayList<String>();

            // int
            for (int i = 0; i < task.cipherText.length; i++) {
                if (task.cipherText[i] == first || task.cipherText[i] == second) {
                    split.add(task.cipherText[i] + "" + task.cipherText[i + 1]);
                    i++;
                } else
                    split.add("" + task.cipherText[i]);
            }

            app.out().println("" + split);

            char[] tempText = new char[split.size()];
            HashSet<String> nonDupList = new HashSet<String>(split);
            int t = 0;

            for (String nonDup : nonDupList) {
                for (int i = 0; i < split.size(); i++)
                    if (split.get(i).equals(nonDup))
                        tempText[i] = (char) ('A' + t);

                t++;
            }

            new SimpleSubstitutionAttack().attemptAttack(new String(tempText), DecryptionMethod.SIMULATED_ANNEALING, app);
        }

        app.out().println(task.getBestSolution());
    }

    public class StraddleCheckerboardTask extends DecryptionTracker {

        public StraddleCheckerboardTask(String text, IApplication app) {
            super(text.toCharArray(), app);
        }
    }
}
