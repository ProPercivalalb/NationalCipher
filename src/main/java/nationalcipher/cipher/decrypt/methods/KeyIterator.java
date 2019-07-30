package nationalcipher.cipher.decrypt.methods;

import java.lang.reflect.Array;
import java.util.function.BiPredicate;

import javalibrary.math.matrics.Matrix;
import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.KeyFunction;
import nationalcipher.cipher.tools.KeyGeneration;

public class KeyIterator {

    public static boolean permuteIntegerOrderedKey(KeyFunction<Integer[]> consumer, int length) {
        return permuteObject(consumer, ArrayUtil.createRangeInteger(length));
    }

    public static boolean permuteString(KeyFunction<String> consumer, String str) {
        return permuteString(consumer, PrimTypeUtil.toCharacterArray(str));
    }

    public static boolean permuteString(KeyFunction<String> consumer, Character[] str) {
        return permuteObject(d -> consumer.apply(PrimTypeUtil.toString(d)), str);
    }

    public static <T> boolean permuteObject(KeyFunction<T[]> consumer, T[] items) {
        return permuteObject(consumer, items, 0);
    }

    private static <T> boolean permuteObject(KeyFunction<T[]> consumer, T[] arr, int pos) {
        if (arr.length - pos == 1)
            return consumer.apply(arr);
        else {
            for (int i = pos; i < arr.length; i++) {
                T h = arr[pos];
                T j = arr[i];
                arr[pos] = j;
                arr[i] = h;

                if (!permuteObject(consumer, arr, pos + 1)) {
                    return false;
                }
                arr[pos] = h;
                arr[i] = j;
            }
            
            return true;
        }
    }

    public static boolean iterateIntegerArray(KeyFunction<Integer[]> consumer, int length, int range, boolean repeats) {
        return iterateObject(consumer, new Integer[length], ArrayUtil.createRangeInteger(range), repeats ? null : (a, b) -> a == b);
    }

    public static boolean iterateShort26Key(KeyFunction<String> consumer, int length, boolean repeats) {
        return iterateShortKey(consumer, KeyGeneration.ALL_26_CHARS, length, repeats);
    }

    public static boolean iterateShortCustomKey(KeyFunction<String> consumer, String keyAlphabet, int length, boolean repeats) {
        return iterateShortKey(consumer, PrimTypeUtil.toCharacterArray(keyAlphabet), length, repeats);
    }

    public static boolean iterateShortKey(KeyFunction<String> consumer, Character[] characters, int length, boolean repeats) {
        return iterateObject(o -> consumer.apply(PrimTypeUtil.toString(o)), new Character[length], characters, repeats ? null : (a, b) -> a == b);
    }

    public static boolean iterateMatrix(KeyFunction<Matrix> consumer, int size) {
        return iterateMatrix(consumer, size, size, 26);
    }

    public static boolean iterateMatrix(KeyFunction<Matrix> consumer, int noRows, int noColumns, int base) {
        return iterateObject(o -> consumer.apply(new Matrix(o, noRows, noColumns)), new Integer[noRows * noColumns], ArrayUtil.createRangeInteger(base));
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean iterateObject(KeyFunction<T[]> consumer, int length, T[] items) {
        return iterateObject(consumer, (T[]) Array.newInstance(items.getClass().getComponentType(), length), items);
    }

    public static <T> boolean iterateObject(KeyFunction<T[]> consumer, T[] holder, T[] items) {
        return iterateObject(consumer, holder, items, null, 0);
    }

    public static <T> boolean iterateObject(KeyFunction<T[]> consumer, T[] holder, T[] items, BiPredicate<T, T> equalsPred) {
        return iterateObject(consumer, holder, items, equalsPred, 0);
    }

    private static <T> boolean iterateObject(KeyFunction<T[]> consumer, T[] holder, T[] items, BiPredicate<T, T> equalsPred, int pos) {
        if (holder.length - pos == 0)
            return consumer.apply(holder);
        else {
            skipPath:
            for (T i : items) {

                // If can't have duplicates
                if (equalsPred != null) {
                    for (int j = 0; j < pos; j++) {
                        if (equalsPred.test(holder[j], i)) {
                            continue skipPath;
                        }
                    }
                }

                holder[pos] = i;
                if (!iterateObject(consumer, holder, items, equalsPred, pos + 1)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean iterateGrille(KeyFunction<Integer[]> consumer, int size) {
        double halfSize = size / 2D;
        int rows = (int) Math.ceil(halfSize);
        int cols = (int) Math.floor(halfSize);
        int keySize = rows * cols;

        int[] key = new int[keySize];
        int count = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                key[count++] = r * size + c;

        return KeyIterator.iterateIntegerArray(o -> {
            int[] starting = (int[]) key;
            Integer[] next = new Integer[starting.length];

            for (int i = 0; i < key.length; i++) {
                int quadrant = o[i];
                int value = starting[i];
                for (int rot = 0; rot < quadrant; rot++) {
                    int row = value / size;
                    int col = value % size;
                    value = col * size + (size - row - 1);
                }
                next[i] = value;
            }
            return consumer.apply(next);
        }, keySize, 4, true);
    }
}
