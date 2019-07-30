package nationalcipher.cipher.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.KeyGeneration;

public class CipherUtils {

    private static final NumberFormat numFormatter = NumberFormat.getNumberInstance(Locale.UK);
    
    // TODO Use AlphabetMap
    public static Map<Character, Integer> createCharacterIndexMapping(CharSequence key) {
        Map<Character, Integer> keyIndex = new HashMap<Character, Integer>();

        for (int i = 0; i < key.length(); i++) {
            keyIndex.put(key.charAt(i), i);
        }

        return keyIndex;
    }

    public static byte getAlphaIndex(char alphaChar) {
        if ('A' <= alphaChar && alphaChar <= 'Z') {
            return (byte) (alphaChar - 'A');
        } else if ('a' <= alphaChar && alphaChar <= 'z') {
            return (byte) (alphaChar - 'a');
        } else {
            return -1;
        }
    }

    public static byte[] charSeqToByteArray(CharSequence input) {
        byte[] output = new byte[input.length()];
        for (int i = 0; i < input.length(); i++)
            output[i] = (byte) input.charAt(i);
        return output;
    }

    public static String byteArrayToCharSeq(byte[] input) {
        return new String(input, Charset.forName("UTF-8"));
    }
    
    public static <T> Stream<T> createStream(Collection<T> stream, DecryptionTracker tracker) {
        return changeStreamType(stream.stream(), tracker);
    }
    
    public static <T> Stream<T> changeStreamType(Stream<T> stream, DecryptionTracker tracker) {
        return optionalParallel(b -> b, stream::parallel, stream::sequential, tracker);
    }
    
    public static <R> R optionalParallel(Supplier<R> parallelOption, Supplier<R> sequentialOption, DecryptionTracker tracker) {
        return optionalParallel(b -> b, parallelOption, sequentialOption, tracker);
    }
    
    public static <R> R optionalParallel(Predicate<Boolean> shouldBeParallel, Supplier<R> parallelOption, Supplier<R> sequentialOption, DecryptionTracker tracker) {
        return (shouldBeParallel.test(tracker.getSettings().useParallel()) ? parallelOption.get() : sequentialOption.get());
    }
    
    public static String formatBigInteger(BigInteger value) {
        return numFormatter.format(value);
    }
    
    public static String displayAsLetters(Integer[] array) {
        StringBuilder builder = new StringBuilder(array.length);
        for (int i = 0; i < array.length; i++) {
            builder.append((char)(array[i] + 'A'));
        }
        return builder.toString();
    }
    
    //TODO
    public static <K> void recussivelyIterate(Object key, Consumer<List<Object>> accept, BiConsumer<Object, Consumer>... lists) {
        if (lists.length == 0) return;
        List<Object> list =  new ArrayList<>();
        Consumer<Consumer<K>> consumer = (func) -> {};
        BiConsumer<Object, Consumer> consumer3 = (o, c) -> {list.add(c); accept.accept(list);list.clear();};
        for (int i = lists.length - 2; i >=0; i--) {
            final BiConsumer<Object, Consumer> consumer2 = consumer3;
            int j = i;
            consumer3 = (o, c) -> lists[j].accept(key, n -> {list.add(n); consumer2.accept(key, null);});
        }
        consumer3.accept(null, null);
    }

    public static String genKeySquare(String keyword) {
        Character[] alphabet = KeyGeneration.ALL_25_CHARS;
        char[] builder = new char[alphabet.length];
        int index = 0;
        for (int i = 0; i < keyword.length(); i++) {
            if (ArrayUtil.contains(alphabet, keyword.charAt(i)) && !ArrayUtil.contains(builder, 0, index, keyword.charAt(i))) {
                builder[index++] = keyword.charAt(i);
            }
        }
        
        for (char ch : alphabet) {
            int keyindex = keyword.indexOf(ch);
            if (keyindex == -1) {
                builder[index++] = ch;
            }
        }
        return new String(builder);
    }
}
