package nationalcipher.cipher.stats;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javalibrary.language.ILanguage;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.util.ArrayUtil;

/**
 * @author Alex Barter (10AS)
 */
public class StatCalculator {

    /* Index of coincidence calculations */

    public static double calculateMaxIC(String text, int minPeriod, int maxPeriod) {
        double maxIC = 0.0D;

        for (int period = minPeriod; period <= maxPeriod; ++period) {
            double totalIOC = 0.0D;

            for (int i = 0; i < period; ++i)
                totalIOC += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(text, i, period), 1, true);

            maxIC = Math.max(maxIC, totalIOC / period);
        }
        return maxIC;
    }

    public static double calculateIC(CharSequence text, int length, boolean overlap) {
        Map<String, Integer> letters = StringAnalyzer.getEmbeddedStrings(text, length, length, overlap);

        double sum = 0.0D;
        for (int value : letters.values())
            sum += value * (value - 1);

        int n = overlap ? text.length() - (length - 1) : text.length() / length;
        return sum / (n * (n - 1));
    }

    public static double calculateMonoIC(char[] text) {
        Map<Character, Integer> letters = StringAnalyzer.getCharacterCount(text);

        double sum = 0.0D;
        for (int value : letters.values())
            sum += value * (value - 1);

        int n = text.length;
        return sum / (n * (n - 1));
    }

    /* Friedman's Kappa test */

    /**
     * Calculates how many times a letter appears in the same place in two sets of
     * text. In English for a length of 1000 characters you would expect 66 or 67
     * coincidences between them.
     */
    public static double calculateKappaIC(String text, int period) {
        return calculateKappaIC(text, StringTransformer.rotateRight(text, period));
    }

    private static double calculateKappaIC(CharSequence text1, String text2) {
        double coincidence = 0;
        for (int i = 0; i < text1.length(); ++i)
            if (text1.charAt(i) == text2.charAt(i))
                coincidence += 1;

        return coincidence / text1.length();
    }

    public static double calculateMaxKappaIC(String text, int minPeriod, int maxPeriod) {
        if (text.length() == 0)
            return 0.0D;

        double maxKappa = Double.MIN_VALUE;

        for (int period = minPeriod; period <= maxPeriod; ++period)
            maxKappa = Math.max(maxKappa, calculateKappaIC(text, period));

        return maxKappa;
    }

    public static double calculateMinKappaIC(String text, int minPeriod, int maxPeriod) {
        double minKappa = Double.MAX_VALUE;

        for (int period = minPeriod; period <= maxPeriod; ++period)
            minKappa = Math.min(minKappa, calculateKappaIC(text, period));

        return minKappa;
    }

    public static int calculateBestKappaIC(String text, int minPeriod, int maxPeriod, ILanguage language) {
        int bestPeriod = -1;
        double bestKappa = Double.MAX_VALUE;

        for (int period = minPeriod; period <= maxPeriod; ++period) {
            double sqDiff = Math.pow(calculateKappaIC(text, period) - language.getNormalCoincidence(), 2);

            if (sqDiff < bestKappa)
                bestPeriod = period;

            bestKappa = Math.min(bestKappa, sqDiff);
        }

        return bestPeriod;
    }

    public static double calculateDiagraphicKappaIC(String text, int period) {
        return calculateDiagraphicKappaIC(text, StringTransformer.rotateRight(text, period));
    }

    private static double calculateDiagraphicKappaIC(String text1, String text2) {
        double coincidence = 0;
        for (int j = 0; j < text1.length(); j += 2)
            if (text1.substring(j, j + 2).equals(text2.substring(j, j + 2)))
                coincidence += 1;

        return coincidence / (text1.length() / 2);
    }

    public static double calculateMaxDiagraphicKappaIC(String text, int minPeriod, int maxPeriod) {
        double maxKappa = Double.MIN_VALUE;

        for (int period = minPeriod; period <= maxPeriod; ++period)
            maxKappa = Math.max(maxKappa, calculateDiagraphicKappaIC(text, period));

        return maxKappa * 1000.0D;
    }

    public static double calculateMinDiagraphicKappaIC(String text, int minPeriod, int maxPeriod) {
        double minKappa = Double.MAX_VALUE;

        for (int period = minPeriod; period <= maxPeriod; ++period)
            minKappa = Math.min(minKappa, calculateDiagraphicKappaIC(text, period));

        return minKappa * 1000.0D;
    }

    public static int calculateBestDiagraphicKappaIC(String text, int minPeriod, int maxPeriod, ILanguage language) {
        int bestPeriod = -1;
        double bestKappa = Double.POSITIVE_INFINITY;

        for (int period = minPeriod; period <= maxPeriod; ++period) {
            double sqDiff = Math.pow(calculateDiagraphicKappaIC(text, period) - language.getNormalCoincidence(), 2);

            if (sqDiff < bestKappa)
                bestPeriod = period;

            bestKappa = Math.min(bestKappa, sqDiff);
        }

        return bestPeriod;
    }

    public static double calculateBifidDiagraphicIC(String text, int period) {
        if (period == 0)
            period = text.length();

        Map<String, Integer> theatricalDiagram = new HashMap<String, Integer>();
        int count = 0;
        for (int i = 0; i < text.length(); i += period) {
            int columns = Math.min(period / 2, (text.length() - i) / 2);
            int limit = Math.min(i + period, text.length());

            for (int j = i; j < limit - columns; j++) {
                String theatrical = text.charAt(j) + "" + text.charAt(j + columns);
                theatricalDiagram.put(theatrical, 1 + (theatricalDiagram.containsKey(theatrical) ? theatricalDiagram.get(theatrical) : 0));
            }
            count += limit - columns - i;
        }

        double sum = 0.0;
        for (String diagram : theatricalDiagram.keySet())
            sum += theatricalDiagram.get(diagram) * (theatricalDiagram.get(diagram) - 1);

        return 62500 * sum / (count * (count - 1));
    }

    public static double calculateTrifidDiagraphicIC(String text, int period) {
        if (period == 0)
            period = text.length();

        Map<String, Integer> theatricalDiagram = new HashMap<String, Integer>();
        int count = 0;
        for (int i = 0; i < text.length(); i += period) {
            int columns = Math.min(period / 3, (text.length() - i) / 3);
            int limit = Math.min(i + period, text.length());

            for (int j = i; j < limit - columns * 2; j++) {
                String theatrical = text.charAt(j) + "" + text.charAt(j + columns) + "" + text.charAt(j + columns * 2);
                theatricalDiagram.put(theatrical, 1 + (theatricalDiagram.containsKey(theatrical) ? theatricalDiagram.get(theatrical) : 0));
            }
            count += limit - columns * 2 - i;
        }

        double sum = 0.0;
        for (String diagram : theatricalDiagram.keySet())
            sum += theatricalDiagram.get(diagram) * (theatricalDiagram.get(diagram) - 1);

        return 27 * 27 * 2700 * sum / (count * (count - 1));
    }

    public static double calculateStrangeIC(String text, int period, int size, int uniqueChars) {

        if (period == 0)
            period = text.length();

        Map<String, Integer> theatricalDiagram = new HashMap<String, Integer>();
        int count = 0;
        for (int i = 0; i < text.length(); i += period) {
            int columns = Math.min(period / size, (text.length() - i) / size);
            int limit = Math.min(i + period, text.length());

            for (int j = i; j < limit - columns * (size - 1); j++) {
                String theatrical = "";
                for (int s = 0; s < size; s++)
                    theatrical += text.charAt(j + columns * s);
                theatricalDiagram.put(theatrical, 1 + (theatricalDiagram.containsKey(theatrical) ? theatricalDiagram.get(theatrical) : 0));
            }
            count += Math.max(limit - columns * (size - 1) - i, 0);
        }

        double sum = 0.0;
        for (String diagram : theatricalDiagram.keySet())
            sum += theatricalDiagram.get(diagram) * (theatricalDiagram.get(diagram) - 1);

        return Math.pow(uniqueChars, size) * 100 * sum / (count * (count - 1));
    }

    public static double calculateLongIC(String text, int period, int uniqueChars) {

        Map<String, Integer> theatricalDiagram = new HashMap<String, Integer>();
        int count = 0;
        for (int i = 0; i < text.length(); i += period) {
            String theatrical = text.substring(i, Math.min(i + period, text.length()));
            theatricalDiagram.put(theatrical, 1 + (theatricalDiagram.containsKey(theatrical) ? theatricalDiagram.get(theatrical) : 0));
            count++;
        }

        double sum = 0.0;
        for (String diagram : theatricalDiagram.keySet())
            sum += theatricalDiagram.get(diagram) * (theatricalDiagram.get(diagram) - 1);

        return Math.pow(uniqueChars, period) * 100 * sum / (count * (count - 1));
    }

    public static double calculateMaxTrifidDiagraphicIC(String text, int minPeriod, int maxPeriod) {
        if (containsDigit(text))
            return 0.0D;

        double bestIC = 0;
        for (int period = minPeriod; period <= maxPeriod; period++) {
            if (period == 1)
                continue;

            bestIC = Math.max(bestIC, calculateTrifidDiagraphicIC(text, period));
        }

        return bestIC;
    }

    public static double calculateMaxBifidDiagraphicIC(String text, int minPeriod, int maxPeriod) {
        if (containsDigit(text) || containsHash(text))
            return 0.0D;

        double bestIC = 0;
        for (int period = minPeriod; period <= maxPeriod; period++) {
            if (period == 1)
                continue;

            bestIC = Math.max(bestIC, calculateBifidDiagraphicIC(text, period));
        }

        return bestIC;
    }

    public static int calculateBestBifidDiagraphicIC(String text, int minPeriod, int maxPeriod) {
        if (containsDigit(text) || containsHash(text))
            return -1;

        int bestPeriod = -1;
        double bestIC = Double.MIN_VALUE;
        for (int period = minPeriod; period <= maxPeriod; period++) {
            if (period == 1)
                continue;

            double score = calculateBifidDiagraphicIC(text, period);

            if (bestIC < score)
                bestPeriod = period;

            bestIC = Math.max(bestIC, score);
        }

        return bestPeriod;
    }

    public static double calculateNicodemusIC(String text, int blockHeight, int period) {
        int[][] counts = new int[period][26];

        int blocksFull = text.length() / (blockHeight * period);
        if (blocksFull == 0)
            return 0.0D;

        int limit = blocksFull * blockHeight * period;
        int index = 0;
        for (int i = 0; i < limit; i++) {
            counts[index][text.charAt(i) - 'A'] += 1;
            if ((i + 1) % blockHeight == 0)
                index = (index + 1) % period;
        }

        double sumIC = 0.0D;
        for (int i = 0; i < period; i++) {
            double total = 0D;
            int n = 0;
            for (int j = 0; j < 26; j++) {
                double count = counts[i][j];
                total += count * (count - 1);
                n += count;
            }

            if (n > 1)
                sumIC += total / (n * (n - 1));
        }

        return sumIC / period;
    }

    public static double calculateMaxNicodemusIC(String text, int minPeriod, int maxPeriod) {
        if (containsDigit(text) || containsHash(text))
            return 0.0D;

        double maxIC = Double.NEGATIVE_INFINITY;

        for (int period = minPeriod; period <= maxPeriod; period++)
            maxIC = Math.max(maxIC, calculateNicodemusIC(text, 5, period));

        return 1000.0D * maxIC;
    }

    public static int calculateBestNicodemusIC(String text, int minPeriod, int maxPeriod, ILanguage language) {
        if (containsDigit(text) || containsHash(text))
            return -1;

        int bestPeriod = -1;
        double bestIC = Double.POSITIVE_INFINITY;

        for (int period = minPeriod; period <= maxPeriod; ++period) {
            double sqDiff = Math.pow(calculateNicodemusIC(text, 5, period) - language.getNormalCoincidence(), 2);

            if (sqDiff < bestIC)
                bestPeriod = period;

            bestIC = Math.min(bestIC, sqDiff);
        }

        return bestPeriod;
    }

    // public static double calculatePortaIC(String text, int period) {
    //
    // }

    /* Normor test */

    public static double calculateNormalOrder(String text, ILanguage language) {
        if (containsDigit(text) || containsHash(text))
            return 0.0D;

        char[] frequencyLargestLanguage = language.getFrequencyLargest();

        List<Character> frequencyLargestText = StringAnalyzer.getOrderedCharacterCount(text.toCharArray());

        double total = 0.0D;
        for (int i = 0; i < frequencyLargestLanguage.length; i++) {
            char target = frequencyLargestLanguage[i];

            if (frequencyLargestText.contains(target))
                total += Math.abs(i - frequencyLargestText.indexOf(target));
            else
                total += i;
        }

        return total;
    }

    public static double calculateLR(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            for (int j = i + 1; j < text.length(); j++) {
                int n = 0;
                while (j + n < text.length() && text.charAt(i + n) == text.charAt(j + n)) {
                    n++;
                    if (n > 3)
                        break;
                }

                if (n == 3)
                    count += 1;
            }
        }

        return Math.sqrt(count) / text.length() * 1000.0D;
    }

    public static double calculateROD(String text) {
        int sumAll = 0;
        int sumOdd = 0;
        for (int i = 0; i < text.length(); i++) {
            for (int j = i + 1; j < text.length(); j++) {
                int n = 0;
                while (j + n < text.length() && text.charAt(i + n) == text.charAt(j + n))
                    n++;

                if (n > 1) {
                    sumAll++;
                    if (((j - i) & 1) == 1)
                        sumOdd++;
                }
            }
        }

        if (sumAll == 0)
            return 0.0D;

        return 100 * sumOdd / sumAll;
    }

    public static double calculateSDD(String text) {
        double score = 0;

        for (int i = 0; i < text.length() - 1; i++)
            if (Character.isLetter(text.charAt(i)) && Character.isLetter(text.charAt(i + 1)))
                score += sdd[text.charAt(i) - 'A'][text.charAt(i + 1) - 'A'];

        return score * 100 / (text.length() - 1);
    }

    public static boolean isLengthDivisible2(String text) {
        return text.length() % 2 == 0;
    }

    public static boolean isLengthDivisible3(String text) {
        return text.length() % 3 == 0;
    }

    public static boolean isLengthDivisible5(String text) {
        return text.length() % 5 == 0;
    }

    public static boolean isLengthDivisible25(String text) {
        return text.length() % 25 == 0;
    }

    public static boolean isLengthDivisible4_15(String text) {
        for (int n = 4; n <= 15; n++)
            if (text.length() % n == 0)
                return true;

        return false;
    }

    public static boolean isLengthDivisible4_30(String text) {
        for (int n = 4; n <= 30; n++)
            if (text.length() % n == 0)
                return true;

        return false;
    }

    public static boolean isLengthPerfectSquare(String text) {
        int n = (int) Math.floor(Math.sqrt(text.length()));
        return Math.pow(n, 2) == text.length();
    }

    public static boolean containsLetter(String text) {
        for (int i = 0; i < text.length(); i++)
            if ('A' <= text.charAt(i) && text.charAt(i) <= 'Z')
                return true;
        return false;
    }

    public static boolean containsDigit(String text) {
        for (int i = 0; i < text.length(); i++)
            if ('0' <= text.charAt(i) && text.charAt(i) <= '9')
                return true;
        return false;
    }

    public static boolean containsJ(String text) {
        return text.contains("J");
    }

    public static boolean containsHash(String text) {
        return text.contains("#");
    }

    public static boolean calculateDBL(String text) {
        if (text.length() % 2 == 0)
            for (int i = 0; i < text.length(); i += 2)
                if (text.charAt(i) == text.charAt(i + 1))
                    return true;
        return false;
    }

    public static double calculatePHIC(String text) {
        int col_len = 5;
        List<Integer> combine_alpha = Arrays.asList(0, 1, 2, 3, 0, 4, 5, 1);

        if (containsDigit(text) || containsHash(text))
            return 0.0D;

        int period = 8;
        int[][] ct = new int[period - 2][26];

        for (int i = 0; i < period - 2; i++) { // combine 2 of the alphbets so 6 altogether
            Arrays.fill(ct[i], 0);
        }
        int block_len = (int) Math.floor(text.length() / (col_len * period));
        if (block_len == 0)
            return 0.0D;

        int limit = block_len * col_len * period; // round off to nearest multiple of period*5
        int index = 0;
        for (int i = 0; i < limit; i++) {
            ct[combine_alpha.get(index)][text.charAt(i) - 'A'] += 1;
            if ((i + 1) % col_len == 0)
                index = (index + 1) % period;
        }
        double z = 0.0D;
        for (int i = 0; i < period - 2; i++) {
            double x = 0.0D, y = 0.0D;
            for (int j = 0; j < 26; j++) {
                x += ct[i][j] * (ct[i][j] - 1);
                y += ct[i][j];
            }
            if (y > 1)
                z += x / (y * (y - 1));
        }
        z = z / (period - 2);
        return Math.floor(1000.0D * z);
    }

    public static boolean calculateHAS0(String text) {
        if (containsLetter(text) || containsHash(text))
            return false;

        return text.contains("0");
    }

    public static double calculateCDD(String text) {
        if (containsDigit(text) || containsHash(text))
            return 0.0D;

        double best_score = 0.0D;
        for (int key_len = 4; key_len <= 15; key_len++) {
            int numb_long_cols = text.length() % key_len;
            int numb_short_cols = key_len - numb_long_cols;

            int numb_rows = (int) Math.floor(text.length() / key_len);

            int[] min_start = new int[key_len];
            int[] max_start = new int[key_len];
            int[] max_diff = new int[key_len];
            int[] col_pos = new int[key_len];
            int[] col_array = new int[key_len];
            boolean[] cols_in_use = new boolean[key_len];
            Arrays.fill(cols_in_use, false);
            int[] diff_array = new int[key_len];
            min_start[0] = 0;
            int n = 0;
            for (int j = 1; j < key_len; j++) {
                if (n < numb_short_cols) {
                    min_start[j] = min_start[j - 1] + numb_rows;
                    n++;
                } else {
                    min_start[j] = min_start[j - 1] + numb_rows + 1;
                }
            }
            max_start[0] = max_diff[0] = 0;
            n = 0;
            for (int j = 1; j < key_len; j++) {
                if (n < numb_long_cols) {
                    max_start[j] = max_start[j - 1] + numb_rows + 1;
                    n++;
                } else {
                    max_start[j] = max_start[j - 1] + numb_rows;
                }
                max_diff[j] = max_start[j] - min_start[j];
            }

            for (int j = 0; j < key_len; j++)
                col_pos[j] = min_start[j];

            for (int t0 = 0; t0 < key_len; t0++) {
                col_array[0] = t0;
                cols_in_use[t0] = true;
                int long_corr = 0;

                if (0 < numb_long_cols && t0 >= numb_long_cols)
                    long_corr = 1;

                for (int current_dif = 0; current_dif <= max_diff[t0] - long_corr; current_dif++) {
                    diff_array[0] = current_dif;
                    int index = 1;
                    for (int j = 0; j < key_len; j++)
                        if (!cols_in_use[j])
                            col_array[index++] = j;
                    double score = 0;
                    for (int j = 1; j < key_len; j++) {
                        try {
                            int[] tn = getBestDI(text, j, key_len, numb_long_cols, numb_short_cols, numb_rows, col_array, max_diff, col_pos, diff_array);
                            score += tn[0];
                            int swap = col_array[tn[1]];
                            col_array[tn[1]] = col_array[j];
                            col_array[j] = swap;
                            diff_array[j] = tn[2];
                        } catch (Exception e) {
                            return 0D;
                        }
                    }
                    score = 100 * score / (numb_rows * (key_len - 1));
                    best_score = Math.max(best_score, score);
                }
                cols_in_use[t0] = false;
            }
        }
        return Math.floor(best_score);
    }

    public static int[] getBestDI(String text, int col, int key_len, int numb_long_cols, int numb_short_cols, int numb_rows, int[] col_array, int[] max_diff, int[] col_pos, int[] diff_array) {
        int max = 0;
        int next_col = 0;
        int next_dif = 0;

        for (int j = col; j < key_len; j++) {
            int long_corr = 0, short_corr = 0;
            if (col >= numb_long_cols && col_array[j] >= numb_short_cols)
                short_corr = 1;
            else if (col < numb_long_cols && col_array[j] >= numb_long_cols)
                long_corr = 1;
            for (int dif = short_corr; dif <= max_diff[col_array[j]] - long_corr; dif++) {
                int sum = 0;
                for (int k = 0; k < numb_rows; k++)
                    sum += sdd[text.charAt(col_pos[col_array[col - 1]] + k + diff_array[col - 1]) - 'A'][text.charAt(col_pos[col_array[j]] + k + dif) - 'A'];

                if (sum > max) {
                    max = sum;
                    next_col = j;
                    next_dif = dif;
                }
            }
        }
        return new int[] { max, next_col, next_dif };
    }

    public static double calculateSSTD(String text) {
        if (containsDigit(text) || containsHash(text))
            return 0.0D;

        int best_score = 0;
        for (int period = 4; period <= 8; period++) {
            if (text.length() % period != 0)
                continue;

            if (3 * period * period > text.length())
                break;

            int result = swagTest(text, period);
            System.out.println(period + " " + result);
            if (result > best_score)
                best_score = result;
        }

        return best_score;
    }

    public static int swagTest(String text, int period) {

        int numb_columns = text.length() / period;

        int[] row_order = ArrayUtil.createRange(period);
        int[][] swag_array = new int[period][numb_columns];
        int index = 0, i = 0;

        for (int j = 0; j < text.length(); j++) {
            int c = text.charAt(j) - 'A';
            swag_array[i][index] = c;
            i += 1;
            if (i == period) {
                index += 1;
                i = 0;
            }
        }
        int[] row = construct_row(row_order, swag_array, period, numb_columns);
        double score = score_row(row);
        double best_score = score;
        int[] best_row = row;

        while (true) {
            Object[] per = next_per(row_order, row_order.length);
            row_order = (int[]) per[1];

            if ((int) per[0] == 0)
                break;

            row = construct_row(row_order, swag_array, period, numb_columns);
            score = score_row(row);
            if (score > best_score) {
                best_score = score;
                best_row = row;
            }
        }
        return (int) Math.floor(100.0D * best_score / (numb_columns - 2));
    }

    public static int[] construct_row(int[] row_order, int[][] swag_array, int period, int numb_columns) {
        int[] row = new int[numb_columns];
        int index = 0;
        for (int i = 0; i < numb_columns; i++) {
            int c = swag_array[row_order[index]][i];
            row[i] = c;
            index += 1;
            if (index == period)
                index = 0;
        }
        return row;
    }

    public static int score_row(int[] row) {
        int score = 0;
        for (int i = 0; i < row.length - 2; i++)
            score += bstd[row[i] + 26 * row[i + 1] + 26 * 26 * row[i + 2]];
        return score;
    }

    public static Object[] next_per(int[] str, int le) {
        if (le < 2)
            return new Object[] { 0, str };

        int last = le - 2;
        while (str[last] >= str[last + 1]) {
            if (last == 0)
                return new Object[] { 0, str };
            last -= 1;
        }
        int fst = le - 1;
        while (str[fst] <= str[last])
            fst -= 1;

        int c = str[last];
        str[last] = str[fst];
        str[fst] = c;

        if (str[last + 1] != str[le - 1]) {
            int i = 1;
            while (last + i < le - i) {
                c = str[last + i];
                str[last + i] = str[le - i];
                str[le - i] = c;
                i += 1;
            }
        }
        return new Object[] { 1, str };
    }

    public static double calculateMPIC(String text) {
        if (containsDigit(text) || containsHash(text))
            return 0.0D;

        double mx = 0.0D;

        int max_period = 15;
        int[][] ct = new int[max_period + 1][26];
        for (int i = 0; i <= max_period; i++)
            Arrays.fill(ct[i], 0);

        for (int period = 1; period <= max_period; period++) {
            for (int prog_index = 1; prog_index < 26; prog_index++) {

                for (int i = 0; i < period; i++)
                    Arrays.fill(ct[i], 0);

                int index = 0;
                int prog_incr = 0;
                for (int i = 0; i < text.length(); i++) {
                    int c = (26 + text.charAt(i) - 'A' - prog_incr) % 26;
                    ct[index][c] += 1;
                    if (++index == period) {
                        index = 0;
                        prog_incr = (prog_incr + prog_index) % 26;
                    }
                }

                double z = 0.0D;
                for (int i = 0; i < period; i++) {
                    double x = 0.0D, y = 0.0D;
                    for (int j = 0; j < 26; j++) {
                        x += ct[i][j] * (ct[i][j] - 1);
                        y += ct[i][j];
                    }
                    if (y > 1)
                        z += x / (y * (y - 1));
                }
                z = z / period;
                if (z > mx)
                    mx = z;
            }
        }
        return (Math.floor(1000.0 * mx));
    }

    public static boolean calculateSeriatedPlayfair(String text, int period) {
        for (int i = 0; i < text.length(); i += period * 2) {
            int min = Math.min(period, (text.length() - i) / 2);

            for (int j = 0; j < min; j++) {
                char a = text.charAt(i + j);
                char b = text.charAt(i + j + min);
                if (a == b)
                    return false;
            }
        }

        return true;
    }

    // Written by me
    public static boolean calculateSeriatedPlayfair(String text, int minPeriod, int maxPeriod) {
        if (text.length() % 2 == 1)
            return false;

        for (int period = 2; period <= 40; period++)
            if (calculateSeriatedPlayfair(text, period))
                return true;

        return false;
    }

    static int[][] sdd = new int[][] { { 0, 3, 4, 2, 0, 0, 1, 0, 0, 0, 4, 5, 2, 6, 0, 2, 0, 4, 4, 3, 0, 6, 0, 0, 3, 5 }, { 0, 0, 0, 0, 6, 0, 0, 0, 0, 9, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 7, 0 }, { 3, 0, 0, 0, 2, 0, 0, 6, 0, 0, 8, 0, 0, 0, 6, 0, 5, 0, 0, 0, 3, 0, 0, 0, 0, 0 }, { 1, 6, 0, 0, 1, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 4, 0, 1, 0 }, { 0, 0, 4, 5, 0, 0, 0, 0, 0, 3, 0, 0, 3, 2, 0, 3, 6, 5, 4, 0, 0, 4, 3, 8, 0, 0 }, { 3, 0, 0, 0, 0, 5, 0, 0, 2, 1, 0, 0, 0, 0, 5, 0, 0, 2, 0, 4, 1, 0, 0, 0, 0, 0 }, { 2, 0, 0, 0, 1, 0, 0, 6, 1, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 0 }, { 5, 0, 0, 0, 7, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 5, 0, 0, 0, 4, 0, 0, 0, 1, 1, 3, 7, 0, 0, 0, 0, 5, 3, 0, 5, 0, 0, 0, 8 }, { 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 6, 0, 0, 0, 5, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, { 2, 0, 0, 4, 2, 0, 0, 0, 3, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0 }, { 5, 5, 0, 0, 5, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 6, 0, 0, 0, 0, 2, 0, 0, 0, 6, 0 }, { 0, 0, 4, 7, 0, 0, 8, 0, 0, 2, 2, 0, 0, 0, 0, 0, 3, 0, 0, 4, 0, 0, 0, 0, 0, 0 }, { 0, 2, 0, 0, 0, 8, 0, 0, 0, 0, 4, 0, 5, 5, 0, 2, 0, 4, 0, 0, 7, 4, 5, 0, 0, 0 }, { 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, 7, 0, 6, 0, 0, 3, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 4, 0, 0, 0, 2, 0, 4, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0 }, { 1, 1, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 1, 4, 4, 0, 1, 4, 2, 0, 4, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 8, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0 }, { 0, 4, 3, 0, 0, 0, 5, 0, 0, 0, 0, 6, 2, 3, 0, 6, 0, 6, 5, 3, 0, 0, 0, 0, 0, 6 }, { 0, 0, 0, 0, 8, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 6, 0, 0, 0, 2, 0, 0, 6, 6, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 3, 0, 7, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 5, 0, 0, 0, 6, 0, 0 }, { 1, 6, 2, 0, 0, 2, 0, 0, 0, 6, 0, 0, 2, 0, 6, 2, 1, 0, 2, 1, 0, 0, 6, 0, 0, 0 }, { 2, 0, 0, 0, 8, 0, 0, 0, 0, 6, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9 } };

    public static int[] bstd = new int[17580];
}