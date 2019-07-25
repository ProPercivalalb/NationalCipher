package nationalcipher.cipher.decrypt.anew;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javalibrary.string.StringAnalyzer;
import nationalcipher.cipher.base.anew.KeywordCipher;
import nationalcipher.cipher.base.anew.StraddleCheckerboardCipher;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.ui.IApplication;
import nationalcipher.util.CharArrayWrapper;

public class StraddleCheckerboardAttack extends CipherAttack<String, StraddleCheckerboardCipher> {

    public StraddleCheckerboardAttack() {
        super(null, "Straddle Checkerboard");
        this.setAttackMethods(DecryptionMethod.CALCULATED);
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        switch (method) {
        case CALCULATED:
            DecryptionTracker tracker = new DecryptionTracker(text, app);
            List<Character> counts = StringAnalyzer.getOrderedCharacterCount(tracker.getCipherText());
            app.out().println("Spare positions: %c %c", counts.get(0), counts.get(1));
            ;
            char first = counts.get(0);
            char second = counts.get(1);

            List<String> split = new ArrayList<String>();

            // int
            for (int i = 0; i < tracker.getLength(); i++) {
                if (tracker.getCipherText().charAt(i) == first || tracker.getCipherText().charAt(i) == second) {
                    split.add(tracker.getCipherText().charAt(i) + "" + tracker.getCipherText().charAt(i + 1));
                    i++;
                } else
                    split.add("" + tracker.getCipherText().charAt(i));
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

            CipherAttack<String, KeywordCipher> substitutionHack = new CipherAttack<>(new KeywordCipher(KeyGeneration.ALL_26_CHARS), "Straddle Checkerboard Sub").setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING).setIterations(10);
            DecryptionTracker keywordTracker = substitutionHack.attemptAttack(new CharArrayWrapper(tempText), DecryptionMethod.SIMULATED_ANNEALING, app);

            this.updateIfBetterThanBest(tracker, keywordTracker.getBestSolution());
            keywordTracker.getProgress().reset();
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }
}
