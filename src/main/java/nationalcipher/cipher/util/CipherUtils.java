package nationalcipher.cipher.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import nationalcipher.cipher.decrypt.methods.DecryptionTracker;

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
}
