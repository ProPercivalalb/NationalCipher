package nationalcipher.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JProgressBar;

import javalibrary.math.Statistics;
import javalibrary.streams.FileReader;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.stats.CipherStatistics;
import nationalcipher.cipher.stats.DataHolder;
import nationalcipher.cipher.stats.IdentifyOutput;
import nationalcipher.cipher.stats.TextStatistic;
import nationalcipher.cipher.stats.types.StatisticBifid0;
import nationalcipher.cipher.stats.types.StatisticDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticDoubleLetter;
import nationalcipher.cipher.stats.types.StatisticDoubleLetter2to40;
import nationalcipher.cipher.stats.types.StatisticEvenDiagrahpicICx10000;
import nationalcipher.cipher.stats.types.StatisticICx1000;
import nationalcipher.cipher.stats.types.StatisticKappaICx1000;
import nationalcipher.cipher.stats.types.StatisticLogDigraph;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAffine;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyBeaufort;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyPorta;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyVariant;
import nationalcipher.cipher.stats.types.StatisticLogDigraphAutokeyVigenere;
import nationalcipher.cipher.stats.types.StatisticLogDigraphBeaufort;
import nationalcipher.cipher.stats.types.StatisticLogDigraphCaesar;
import nationalcipher.cipher.stats.types.StatisticLogDigraphPorta;
import nationalcipher.cipher.stats.types.StatisticLogDigraphPortax;
import nationalcipher.cipher.stats.types.StatisticLogDigraphReversed;
import nationalcipher.cipher.stats.types.StatisticLogDigraphSlidefairBeaufort;
import nationalcipher.cipher.stats.types.StatisticLogDigraphSlidefairVariant;
import nationalcipher.cipher.stats.types.StatisticLogDigraphSlidefairVigenere;
import nationalcipher.cipher.stats.types.StatisticLogDigraphVariant;
import nationalcipher.cipher.stats.types.StatisticLogDigraphVigenere;
import nationalcipher.cipher.stats.types.StatisticLongRepeat;
import nationalcipher.cipher.stats.types.StatisticMaxBifid3to15;
import nationalcipher.cipher.stats.types.StatisticMaxICx1000;
import nationalcipher.cipher.stats.types.StatisticMaxNicodemus3to15;
import nationalcipher.cipher.stats.types.StatisticMaxTrifid3to15;
import nationalcipher.cipher.stats.types.StatisticMaxUniqueCharacters;
import nationalcipher.cipher.stats.types.StatisticNormalOrder;
import nationalcipher.cipher.stats.types.StatisticPercentageOddRepeats;
import nationalcipher.cipher.stats.types.StatisticTextLengthMultiple;
import nationalcipher.cipher.stats.types.StatisticTrigraphNoOverlapICx100000;
import nationalcipher.lib.StatisticsLib;
import nationalcipher.util.ReflectionUtil;

public class StatisticRegistry {

    public static final IRegistry<String, Class<? extends TextStatistic<?>>> TEXT_STATISTIC_MAP = Registry.<Class<? extends TextStatistic<?>>>builder().setRegistryName("TextStatistic").addValidation(value -> ReflectionUtil.getConstructor(value, String.class).isPresent()).build();

    public static final IRegistry<String, String> DISPLAY_NAME_MAP = Registry.builder(String.class).setRegistryName("StatisticDisplayName").build();

    public static boolean registerStatistic(String id, Class<? extends TextStatistic<?>> textStatistic) {
        return registerStatistic(id, textStatistic, id);
    }

    public static boolean registerStatistic(String id, Class<? extends TextStatistic<?>> textStatistic, String shortName) {
        if (TEXT_STATISTIC_MAP.register(id, textStatistic)) {
            if (DISPLAY_NAME_MAP.register(id, shortName)) {
                return true;
            } else {
                TEXT_STATISTIC_MAP.remove(id);
            }
        }

        return false;
    }

    public static List<IdentifyOutput> orderCipherProbibitly(String text) {
        return orderCipherProbibitly(createTextStatistics(text));
    }

    public static List<IdentifyOutput> orderCipherProbibitly(HashMap<String, TextStatistic<?>> stats) {
        return orderCipherProbibitly(stats, new ArrayList<String>(TEXT_STATISTIC_MAP.getKeys()));
    }

    public static List<IdentifyOutput> orderCipherProbibitly(HashMap<String, TextStatistic<?>> stats, List<String> doOnly) {
        List<IdentifyOutput> computedResult = new ArrayList<IdentifyOutput>();

        TreeMap<String, Object> statistic = CipherStatistics.getOtherCipherStatistics();
        traverseDataTree(stats, doOnly, computedResult, new ArrayList<String>(), statistic);

        return computedResult;
    }

    @SuppressWarnings("unchecked")
    public static void traverseDataTree(HashMap<String, TextStatistic<?>> stats, List<String> doOnly, List<IdentifyOutput> computedResult, List<String> keyBefore, Map<String, Object> toCheck) {
        for (String key : toCheck.keySet()) {
            Map<String, ?> nextBranch = (Map<String, ?>) toCheck.get(key);
            List<String> copy = new ArrayList<String>(keyBefore);
            copy.add(key);

            // Determines which tree is next (true = end of tree, false = recursive
            // function)
            boolean type = false;

            // Checks the first value to see if is DataHolder instance, if it is we have
            // reached the end of the true
            for (Object value : nextBranch.values()) {
                type = value instanceof DataHolder;
                break;
            }

            if (type) {
                double value = scoreCipher(stats, (Map<String, DataHolder>) nextBranch, doOnly);

                IdentifyOutput identifyOutput;
                List<IdentifyOutput> last = computedResult;
                for (int i = 0; i < copy.size(); i++) {
                    int index = indexOf(copy.get(i), last);
                    if (index != -1) {
                        IdentifyOutput old = last.get(index);
                        identifyOutput = new IdentifyOutput(old.id, Math.min(old.score, value));
                        identifyOutput.subOutput.addAll(old.subOutput);
                        old.subOutput.clear(); // Not sure it this is necessary, it may free up some memory but since
                                               // all the object referenced in the list are still in use I assume this
                                               // list just points to memory locations that are still in use
                        last.set(index, identifyOutput);
                    } else {
                        identifyOutput = new IdentifyOutput(copy.get(i), value);
                        last.add(identifyOutput);
                    }

                    last = identifyOutput.subOutput;
                }
            } else
                traverseDataTree(stats, doOnly, computedResult, copy, (Map<String, Object>) nextBranch);
        }
    }

    public static int indexOf(String key, List<IdentifyOutput> computedResult) {
        for (int i = 0; i < computedResult.size(); i++)
            if (computedResult.get(i).id.equals(key))
                return i;
        return -1;
    }

    /**
     * Return a number larger than 0, the closer to zero the better
     * 
     * @param stats  Pre-calculated statistics about a particular piece of text
     * @param data   Map of statistics IDs and their expected values for a specific
     *               cipher
     * @param doOnly A list of statistics IDs to test @see
     *               nationalcipher.lib.StatisticsLib
     * @return A quantised idea of how likely this text is to this cipher attributes
     */
    public static double scoreCipher(Map<String, TextStatistic<?>> stats, Map<String, DataHolder> data, List<String> doOnly) {
        double value = 0.0D;

        for (String id : data.keySet())
            if (doOnly.contains(id))
                value += stats.get(id).quantify(data.get(id));

        return value;
    }

    public static HashMap<String, TextStatistic<?>> createTextStatistics(String text) {
        HashMap<String, TextStatistic<?>> stats = new HashMap<String, TextStatistic<?>>();
        try {
            for (String id : TEXT_STATISTIC_MAP.getKeys()) {
                TextStatistic<?> stat = TEXT_STATISTIC_MAP.get(id).getConstructor(String.class).newInstance(text);
                stats.put(id, stat.calculateStatistic());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

    public static HashMap<String, TextStatistic<?>> createTextStatistics(String text, JProgressBar value) {
        HashMap<String, TextStatistic<?>> stats = new HashMap<String, TextStatistic<?>>();
        try {
            value.setMaximum(TEXT_STATISTIC_MAP.size());
            for (String id : TEXT_STATISTIC_MAP.getKeys()) {
                value.setString(DISPLAY_NAME_MAP.get(id));
                TextStatistic<?> stat = TEXT_STATISTIC_MAP.get(id).getConstructor(String.class).newInstance(text);
                stats.put(id, stat.calculateStatistic());
                value.setValue(value.getValue() + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

    public static void calculateStatPrint(IRandEncrypter randEncrypt, Class<? extends TextStatistic<?>> textStatistic, int times) {
        List<String> list = FileReader.compileTextFromResource("/plainText.txt", true);

        List<Double> values = new ArrayList<Double>();
        TextStatistic<?> test = null;
        try {
            test = textStatistic.getConstructor(String.class).newInstance("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String line : list) {
            String plainText = line;

            for (int i = 0; i < times; i++) {
                test.text = randEncrypt.randomlyEncrypt(plainText);
                // System.out.println(test.text);
                test.calculateStatistic();
                try {
                    if (test.value instanceof Number)
                        values.add(((Number) test.value).doubleValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Statistics stats = new Statistics(values);

        String name = randEncrypt.getClass().getSimpleName();
        String variableName = "";
        if (!Character.isJavaIdentifierStart(name.charAt(0)))
            variableName += "_";
        for (char c : name.toCharArray())
            if (!Character.isJavaIdentifierPart(c))
                variableName += "_";
            else
                variableName += c;

        System.out.println(variableName + ".put(" + textStatistic.getSimpleName() + ", new DataHolder(" + String.format("%.2f", stats.getMean()) + ", " + String.format("%.2f", stats.getStandardDeviation()) + ")); //Min: " + String.format("%.2f", stats.getMin()) + " Max: " + String.format("%.2f", stats.getMax()));
    }

    public static void registerStatistics() {
        // Default all ciphers
        registerStatistic(StatisticsLib.IC_x1000, StatisticICx1000.class, "IC");
        registerStatistic(StatisticsLib.IC_MAX_1to15_x1000, StatisticMaxICx1000.class, "MIC");
        registerStatistic(StatisticsLib.IC_2_TRUE_x10000, StatisticDiagrahpicICx10000.class, "DIC");
        registerStatistic(StatisticsLib.IC_2_FALSE_x10000, StatisticEvenDiagrahpicICx10000.class, "DIC_E");
        registerStatistic(StatisticsLib.IC_3_FALSE_x100000, StatisticTrigraphNoOverlapICx100000.class, "TIC_E");
        registerStatistic(StatisticsLib.IC_KAPPA_x1000, StatisticKappaICx1000.class, "MKA");
        registerStatistic(StatisticsLib.LOG_DIGRAPH, StatisticLogDigraph.class, "LDI");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_REVERSED, StatisticLogDigraphReversed.class, "LDI_R");
        registerStatistic(StatisticsLib.LONG_REPEAT, StatisticLongRepeat.class, "LR");
        registerStatistic(StatisticsLib.LONG_REPEAT_ODD_PERCENTAGE, StatisticPercentageOddRepeats.class, "LR_OP");
        registerStatistic(StatisticsLib.NORMAL_ORDER, StatisticNormalOrder.class, "NOR");

        registerStatistic(StatisticsLib.BIFID_MAX_3to15, StatisticMaxBifid3to15.class, "BIC");
        registerStatistic(StatisticsLib.BIFID_0, StatisticBifid0.class, "BIC_Z");
        registerStatistic(StatisticsLib.NICODEMUS_MAX_3to15, StatisticMaxNicodemus3to15.class, "NIC");
        registerStatistic(StatisticsLib.TRIFID_MAX_3to15, StatisticMaxTrifid3to15.class, "TIC");

        registerStatistic(StatisticsLib.LOG_DIGRAPH_CAESAR, StatisticLogDigraphCaesar.class, "LDI_CAE");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_AFFINE, StatisticLogDigraphAffine.class, "LDI_AFF");

        // Vigenere Family
        registerStatistic(StatisticsLib.LOG_DIGRAPH_BEAUFORT, StatisticLogDigraphBeaufort.class, "LDI_BEU");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_PORTA, StatisticLogDigraphPorta.class, "LDI_POR");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_PORTAX, StatisticLogDigraphPortax.class, "LDI_PTX");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_VARIANT, StatisticLogDigraphVariant.class, "LDI_VAR");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_VIGENERE, StatisticLogDigraphVigenere.class, "LDI_VIG");

        registerStatistic(StatisticsLib.LOG_DIGRAPH_AUTOKEY_BEAUFORT, StatisticLogDigraphAutokeyBeaufort.class, "LDI_A_BEU");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_AUTOKEY_PORTA, StatisticLogDigraphAutokeyPorta.class, "LDI_A_POR");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_AUTOKEY_VARIANT, StatisticLogDigraphAutokeyVariant.class, "LDI_A_VAR");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_AUTOKEY_VIGENERE, StatisticLogDigraphAutokeyVigenere.class, "LDI_A_VIG");

        registerStatistic(StatisticsLib.LOG_DIGRAPH_SLIDEFAIR_BEAUFORT, StatisticLogDigraphSlidefairBeaufort.class, "LDI_SLI_BEU");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_SLIDEFAIR_VARIANT, StatisticLogDigraphSlidefairVariant.class, "LDI_SLI_VAR");
        registerStatistic(StatisticsLib.LOG_DIGRAPH_SLIDEFAIR_VIGENERE, StatisticLogDigraphSlidefairVigenere.class, "LDI_SLI_VIG");

        // Boolean statistics
        registerStatistic(StatisticsLib.DOUBLE_LETTER_EVEN, StatisticDoubleLetter.class, "DBL_PLAY");
        registerStatistic(StatisticsLib.DOUBLE_LETTER_EVEN_2to40, StatisticDoubleLetter2to40.class, "DBL_SERP");
        registerStatistic(StatisticsLib.MAX_UNIQUE_CHARACTERS, StatisticMaxUniqueCharacters.class, "MAX_UNIQUE");
        registerStatistic(StatisticsLib.TEXT_LENGTH_MULTIPLE, StatisticTextLengthMultiple.class, "DIV_N");
    }
}
