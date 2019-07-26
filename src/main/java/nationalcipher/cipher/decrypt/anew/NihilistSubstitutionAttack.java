package nationalcipher.cipher.decrypt.anew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nationalcipher.cipher.base.anew.KeywordCipher;
import nationalcipher.cipher.base.anew.NihilistSubstitutionCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.util.CharArrayWrapper;

public class NihilistSubstitutionAttack extends CipherAttack<BiKey<String, String>, NihilistSubstitutionCipher> {

    private int period = 5;
    
    public NihilistSubstitutionAttack() {
        super(new NihilistSubstitutionCipher(), "Nihilist Substitution");
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
        this.addSetting(SettingTypes.createIntSpinner("period", 5, 1, 100, 1, (value, cipher2) -> this.period = value));
    }
    
    @Override
    public DecryptionTracker trySimulatedAnnealing(DecryptionTracker tracker, int iterations) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < tracker.getLength() / 2; i++) {
            list.add(Integer.valueOf(tracker.getCipherText().subSequence(i * 2, (i + 1) * 2).toString()));
        }

        char[] tempText = new char[tracker.getLength() / 2];

        boolean error = false;

        for (int i = 0; i < this.period; i++) {
            List<Integer> old = getEveryNthChar(list, i, this.period);
            List<Integer> key = getBestKey(old, i);
            if (key.size() == 2) {
                this.output(tracker, "PeriodColumn %d -- %d%d", i, key.get(0), key.get(1));

                int count = 0;
                for (int no : old) {
                    if (no <= 10)
                        no += 100;
                    int newInt = no -= (key.get(0) * 10 + key.get(1));

                    int alphaInt = ((int) (newInt / 10) - 1) * 5 + ((newInt % 10) - 1);
                    tempText[count * this.period + i] = "ABCDEFGHIKLMNOPQRSTUVWXYZ".charAt(alphaInt);
                    count += 1;
                }
            } else {
                if (key.get(0) == key.get(1))
                    this.output(tracker, "PeriodColumn %d -- Row: %d Column: %d-%d", i, key.get(0), key.get(2), key.get(3));
                else if (key.get(2) == key.get(3))
                    this.output(tracker, "PeriodColumn %d -- Row: %d-%d Column: %d", i, key.get(0), key.get(1), key.get(2));
                else
                    this.output(tracker, "PeriodColumn %d -- Row: %d-%d Column: %d-%d", i, key.get(0), key.get(1), key.get(2), key.get(3));

                error = true;
            }

        }
        if (!error) {
            CipherAttack<String, KeywordCipher> substitutionHack = new CipherAttack<>(new KeywordCipher(KeyGeneration.ALL_25_CHARS), "Nihilist Sub").setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING).setIterations(5).mute();
            DecryptionTracker keywordTracker = substitutionHack.attemptAttack(new CharArrayWrapper(tempText), DecryptionMethod.SIMULATED_ANNEALING, tracker.getApp());
            
            this.updateIfBetterThanBest(tracker, keywordTracker.getBestSolution());
        } else {
            this.output(tracker, "Cannot complete decryption");
        }
        
        return tracker;
    }

    public static List<Integer> getEveryNthChar(List<Integer> old, int start, int n) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < old.size(); ++i) {
            if ((i % n) == start) {
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

        for (int no : nos) {
            if (no <= 10)
                no += 100;

            int col = no % 10;
            if (col == 0) {
                colMin = 5;
                colMax = 5;
                no -= 10;
            } else if (col - 1 <= 5) {
                colMin = Math.max(1, colMin);
                colMax = Math.min(col - 1, Math.max(colMin, colMax));
            } else {
                colMin = Math.max(col - 5, colMin);
                colMax = Math.min(5, Math.max(colMin, colMax));
            }

            int row = (int) (no / 10) % 10;
            if (row == 0) {
                rowMin = 5;
                rowMax = 5;
            } else if (row - 1 <= 5) {
                rowMin = Math.max(1, rowMin);
                rowMax = Math.min(row - 1, Math.max(rowMin, rowMax));
            } else {
                rowMin = Math.max(row - 5, rowMin);
                rowMax = Math.min(5, Math.max(rowMin, rowMax));
            }

        }

        if (rowMin == rowMax && colMin == colMax)
            return Arrays.asList(rowMin, colMin);

        else
            return Arrays.asList(rowMin, rowMax, colMin, colMax);
    }
}
