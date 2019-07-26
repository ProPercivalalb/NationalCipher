package nationalcipher.cipher.decrypt.methods;

import java.lang.reflect.Array;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import javalibrary.math.matrics.Matrix;
import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.tools.KeyGeneration;

public class KeyIterator {

    public static void permuteIntegerOrderedKey(Consumer<Integer[]> consumer, int length) {
        permuteObject(consumer, ArrayUtil.createRangeInteger(length));
    }

    public static void permuteString(Consumer<String> consumer, String str) {
        permuteString(consumer, PrimTypeUtil.toCharacterArray(str));
    }

    public static void permuteString(Consumer<String> consumer, Character[] str) {
        permuteObject(d -> consumer.accept(PrimTypeUtil.toString(d)), str);
    }

    public static <T> void permuteObject(Consumer<T[]> consumer, T[] items) {
        permuteObject(consumer, items, 0);
    }

    private static <T> void permuteObject(Consumer<T[]> consumer, T[] arr, int pos) {
        if (arr.length - pos == 1)
            consumer.accept(arr);
        else {
            for (int i = pos; i < arr.length; i++) {
                T h = arr[pos];
                T j = arr[i];
                arr[pos] = j;
                arr[i] = h;

                permuteObject(consumer, arr, pos + 1);
                arr[pos] = h;
                arr[i] = j;
            }
        }
    }

    public static void iterateIntegerArray(Consumer<Integer[]> consumer, int length, int range, boolean repeats) {
        iterateObject(consumer, new Integer[length], ArrayUtil.createRangeInteger(range), repeats ? null : (a, b) -> a == b);
    }

    public static void iterateShort26Key(Consumer<String> consumer, int length, boolean repeats) {
        iterateShortKey(consumer, KeyGeneration.ALL_26_CHARS, length, repeats);
    }

    public static void iterateShortCustomKey(Consumer<String> consumer, String keyAlphabet, int length, boolean repeats) {
        iterateShortKey(consumer, PrimTypeUtil.toCharacterArray(keyAlphabet), length, repeats);
    }

    public static void iterateShortKey(Consumer<String> consumer, Character[] characters, int length, boolean repeats) {
        iterateObject(o -> consumer.accept(PrimTypeUtil.toString(o)), new Character[length], characters, repeats ? null : (a, b) -> a == b);
    }

    public static void iterateMatrix(Consumer<Matrix> consumer, int size) {
        iterateMatrix(consumer, size, size, 26);
    }

    public static void iterateMatrix(Consumer<Matrix> consumer, int noRows, int noColumns, int base) {
        iterateObject(o -> consumer.accept(new Matrix(o, noRows, noColumns)), new Integer[noRows * noColumns], ArrayUtil.createRangeInteger(base));
    }

    @SuppressWarnings("unchecked")
    public static <T> void iterateObject(Consumer<T[]> consumer, int length, T[] items) {
        iterateObject(consumer, (T[]) Array.newInstance(items.getClass().getComponentType(), length), items);
    }

    public static <T> void iterateObject(Consumer<T[]> consumer, T[] holder, T[] items) {
        iterateObject(consumer, holder, items, null, 0);
    }

    public static <T> void iterateObject(Consumer<T[]> consumer, T[] holder, T[] items, BiPredicate<T, T> equalsPred) {
        iterateObject(consumer, holder, items, equalsPred, 0);
    }

    private static <T> void iterateObject(Consumer<T[]> consumer, T[] holder, T[] items, BiPredicate<T, T> equalsPred, int pos) {
        if (holder.length - pos == 0)
            consumer.accept(holder);
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
                iterateObject(consumer, holder, items, equalsPred, pos + 1);
            }
        }
    }

    public static void iterateGrille(Consumer<Integer[]> consumer, int size) {
        double halfSize = size / 2D;
        int rows = (int) Math.ceil(halfSize);
        int cols = (int) Math.floor(halfSize);
        int keySize = rows * cols;

        int[] key = new int[keySize];
        int count = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                key[count++] = r * size + c;

        KeyIterator.iterateIntegerArray(o -> {
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
            consumer.accept(next);
        }, keySize, 4, true);
    }
}
