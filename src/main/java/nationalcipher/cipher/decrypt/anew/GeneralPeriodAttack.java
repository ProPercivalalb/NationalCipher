package nationalcipher.cipher.decrypt.anew;

import java.math.BigInteger;
import java.util.List;
import java.util.StringJoiner;

import javalibrary.lib.Timer;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.ui.IApplication;
import nationalcipher.util.CharArrayWrapper;

public class GeneralPeriodAttack extends CipherAttack {

    private int period;
    
    public GeneralPeriodAttack() {
        super(null, "General Period Subsitution");
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
        this.addSetting(SettingTypes.createIntSpinner("period", 5, 1, 100, 1, (value, cipher) -> GeneralPeriodAttack.this.period = value));
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        switch(method) {
        default:
             return super.attemptAttack(text, method, app);
        }
    }

    @Override
    public DecryptionTracker trySimulatedAnnealing(DecryptionTracker tracker, int iterations) {
        int rowsMin = tracker.getLength() / this.period;
        int colLeft = tracker.getLength() % this.period;
        int[] height = new int[this.period]; // Number of rows in the 'i'th column
        char[][] keysIndex = new char[this.period][26];

        List<Character> letter = tracker.getLanguage().getLetterLargestFirst();
        // TODO Generate an approximate key based off the letter frequence
        for (int p = 0; p < this.period; p++) {
            height[p] = (rowsMin + (colLeft > p ? 1 : 0)) * this.period;
            keysIndex[p] = KeyGeneration.createLongKey26().toCharArray();
//            String row = StringTransformer.getEveryNthChar(tracker.getCipherText(), p, this.period);
//            ArrayList<LetterCount> d = StringAnalyzer.countLettersInSizeOrder(row);
//
//            for (int i = 0; i < d.size(); i++) {
//                keysIndex[p][i] = d.get(i).ch;
//            }
        }
        
        char[] editText = tracker.getNewHolder();

        for (int p = 0; p < this.period; p++) {
            for (int i = p; i < height[p]; i += this.period) {
                char ch = tracker.getCipherText().charAt(i);
                editText[i] = (char) (ArrayUtil.indexOf(keysIndex[p], ch) + 'A');
            }
        }
        tracker.getProgress().addMaxValue(BigInteger.valueOf((long) Math.floor((double) tracker.getSettings().getSATempStart() / tracker.getSettings().getSATempStep()) + 1).multiply(BigInteger.valueOf(tracker.getSettings().getSACount())));
        System.out.println(new String(editText));
        for (int p = 0; p < this.period; p++) {
            System.out.println(new String(keysIndex[p]));
        }
        Solution bestMaximaSolution = new Solution(editText, tracker.getLanguage());
        tracker.addSolution(bestMaximaSolution);
        StringJoiner str2 = new StringJoiner(", ");
        for (int j = 0; j < this.period; j++) {
            str2.add(new CharArrayWrapper(keysIndex[j]));
        }
        bestMaximaSolution.setKeyString(str2.toString());
        this.updateIfBetterThanBest(tracker, bestMaximaSolution);
        
        int ite = 0;
        Timer timer = new Timer();
        stop:
        while (true) {
            timer.restart();
            this.startIteration(tracker);

            double TEMP = tracker.getSettings().getSATempStart();
            do {
                TEMP = Math.max(0.0D, TEMP - tracker.getSettings().getSATempStep());

                for (int count = 0; count < tracker.getSettings().getSACount(); count++) {
                    if (tracker.shouldStop()) {
                        break stop;
                    }

                    for (int p = 0; p < this.period; p++) {
                        char ch1 = RandomUtil.pickRandomChar("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                        char ch2 = RandomUtil.pickRandomChar("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                       // System.out.println(ch1 + " " + ch2);
                        for (int i = p; i < height[p]; i += this.period) {
                            if      (editText[i] == ch1) editText[i] = ch2;
                            else if (editText[i] == ch2) editText[i] = ch1;
                        }

                        tracker.lastSolution = new Solution(editText, tracker.getLanguage());
                        tracker.addSolution(tracker.lastSolution);
                        //System.out.println(tracker.lastSolution + " " + bestMaximaSolution);
                        if (this.shouldAcceptSolution(TEMP, tracker.lastSolution, bestMaximaSolution)) {
                            bestMaximaSolution = tracker.lastSolution;
                            
                            char temp = keysIndex[p][ch1 - 'A'];
                            keysIndex[p][ch1 - 'A'] = keysIndex[p][ch2 - 'A'];
                            keysIndex[p][ch2 - 'A'] = temp;

                            StringJoiner str = new StringJoiner(", ");
                            for (int j = 0; j < this.period; j++) {
                                str.add(new CharArrayWrapper(keysIndex[j]));
                            }
                            bestMaximaSolution.setKeyString(str.toString());
                            this.updateIfBetterThanBest(tracker, bestMaximaSolution);
                          
                        } else {
                            for (int i = p; i < height[p]; i += this.period) {
                                if      (editText[i] == ch1) editText[i] = ch2;
                                else if (editText[i] == ch2) editText[i] = ch1;
                            }
                        }
                    }
                  
                    this.onPostIteration(tracker);
                }
            } while (TEMP > 0);
            
            tracker.getProgress().finish();
            // TODO if(this.iterationTimer)
            // this.output(tracker, "Iteration Time: %f",
            // timer.getTimeRunning(Units.Time.MILLISECOND));
            if (this.endIteration(tracker, tracker.bestSolution)) {
                break;
            }
            
            this.output(tracker, "============================ Temperture Reset");
        }
        
        return tracker;
    }
}
