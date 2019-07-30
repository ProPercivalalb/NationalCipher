package nationalcipher.cipher.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javalibrary.math.matrics.Matrix;
import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.util.Pair;
import nationalcipher.util.Triple;

public class KeyGeneration {

    public final static Character[] ALL_POLLUX_CHARS = PrimTypeUtil.toCharacterArray("X.-");

    public final static Character[] ALL_36_CHARS = PrimTypeUtil.toCharacterArray("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"); // 0-9 added
    public final static Character[] ALL_27_CHARS = PrimTypeUtil.toCharacterArray("ABCDEFGHIJKLMNOPQRSTUVWXYZ#"); // # added
    public final static Character[] ALL_26_CHARS = PrimTypeUtil.toCharacterArray("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public final static Character[] ALL_25_CHARS = PrimTypeUtil.toCharacterArray("ABCDEFGHIKLMNOPQRSTUVWXYZ"); // J removed
    public final static Character[] ALL_24_CHARS = PrimTypeUtil.toCharacterArray("ABCDEFGHIKLMNOPQRSTUVWYZ"); // J & X removed

    public static String createRepeatingShortKey26(int length) {
        return createRepeatingShortKeyUniversal(ALL_26_CHARS, length);
    }

    public static String createShortKey26(int length) {
        return createShortKeyUniversal(ALL_26_CHARS, length);
    }

    public static String createRepeatingShortKeyUniversal(Character[] charList, int length) {
        Character[] key = new Character[length];

        for (int i = 0; i < length; i++)
            key[i] = RandomUtil.pickRandomElement(charList);

        return PrimTypeUtil.toString(key);
    }

    public static String createShortKeyUniversal(Character[] charList, int length) {
        List<Character> characters = ListUtil.toList(charList);

        char[] key = new char[length];

        for (int i = 0; i < length; i++) {
            key[i] = RandomUtil.pickRandomElement(characters);
            characters.remove((Character) key[i]);
        }

        return new String(key);
    }

    public static String createLongKey25() {
        return createLongKeyUniversal(ALL_25_CHARS);
    }

    public static String createLongKey26() {
        return createLongKeyUniversal(ALL_26_CHARS);
    }

    public static String createLongKey27() {
        return createLongKeyUniversal(ALL_27_CHARS);
    }

    public static String createLongKey36() {
        return createLongKeyUniversal(ALL_36_CHARS);
    }

    public static String createLongKeyUniversal(Character[] charList) {
        List<Character> characters = ListUtil.toList(charList);

        char[] key = new char[characters.size()];
        for (int i = 0; i < key.length; i++) {
            char rC = RandomUtil.pickRandomElement(characters);
            key[i] = rC;
            characters.remove((Character) rC);
        }

        return new String(key);
    }
    
    
    public static Integer[] createRepeatingShortOrderKey(int entryRange, int length) {
        Integer[] key = new Integer[length];

        for (int i = 0; i < length; i++)
            key[i] = RandomUtil.pickRandomInt(0, entryRange - 1);

        return key;
    }

    public static Integer[] createShortOrderKey(int entryRange, int length) {
        List<Integer> integers = ListUtil.range(0, entryRange - 1);

        Integer[] key = new Integer[length];

        for (int i = 0; i < length; i++) {
            key[i] = RandomUtil.pickRandomElement(integers);
            integers.remove((Integer) key[i]);
        }

        return key;
    }

    public static Integer[] createOrder(int length) {
        return ArrayUtil.shuffle(ArrayUtil.createRangeInteger(length));
    }

    public static Matrix createMatrix(int size, int range) {
        return createMatrix(size, size, range);
    }

    public static Matrix createMatrix(int rows, int columns, int range) {
        Matrix matrix = new Matrix(rows, columns);
        for (int i = 0; i < matrix.data.length; i++)
            matrix.data[i] = RandomUtil.pickRandomInt(range);

        return matrix;
    }

    public static List<Integer> createOrderList(int length) {
        return ListUtil.toList(createOrder(length));
    }

    // Specific key generators

    public static Character[] createPolluxKey() {
        List<Character> characters = ListUtil.toList(ALL_POLLUX_CHARS);
        int numChars = characters.size();

        Character[] key = new Character[10];
        int i = 0;

        for (; i < numChars; i++) {
            char rC = RandomUtil.pickRandomElement(characters);
            key[i] = rC;
            characters.remove((Character) rC);
        }

        for (; i < key.length; i++)
            key[i] = RandomUtil.pickRandomElement(ALL_POLLUX_CHARS);

        return ArrayUtil.shuffle(key);
    }

    public static int[] createSwagmanKey2(int size) {
        int[] key = new int[size * size];
        Arrays.fill(key, -1);

        for (int row = 0; row < size; row++) {
            List<Pair<Integer, List<Integer>>> rowOpts = new ArrayList<>(size);

            for (int col = 0; col < size; col++) {
                List<Integer> options = getOptionsForCell(key, size, row, col);
                // System.out.println(row + " " + col + " " + options);
                if (!options.isEmpty()) {
                    rowOpts.add(new Pair<>(col, options));
                }
            }

            while (rowOpts.size() > 0) {
                rowOpts.sort(Comparator.comparingInt(p -> p.getRight().size()));
                Pair<Integer, List<Integer>> pair = rowOpts.get(0);
                rowOpts.remove(0);

                Integer random = null;

                while (true) {
                    random = RandomUtil.pickRandomElement(pair.getRight());
                    final Integer test = random;
                    // System.out.println("Try " + test);
                    // Check choice is ok
                    final int possiblities = (size - row);
                    final int slotsLeft = rowOpts.size();
                    boolean notAllowed = rowOpts.stream().map(Pair::getRight).filter(p -> p != pair && rowOpts.stream().map(Pair::getRight).anyMatch(p2 -> p2 != p && p.equals(p2))).anyMatch(p -> p.size() - 1 < slotsLeft - 1);

                    if (notAllowed) {
                        // if(slotsLeft - 1 < possiblities &&
                        // (rowOpts.stream().map(Pair::getRight).filter(p -> p != pair &&
                        // p.contains(test)).anyMatch(p -> p.size() - 1 < slotsLeft - 1) ||
                        // rowOpts.stream().map(Pair::getRight).anyMatch(p -> p != pair &&
                        // p.isEmpty()))) {
                        pair.getRight().remove(test);

                    } else {
                        break;
                    }
                }

                key[row * size + pair.getLeft()] = random;
                // System.out.println("set " + (row * size + pair.getLeft()) + " to " + random);
                // List<Pair<Integer, List<Integer>>> remove = new ArrayList<>();

                for (int i = 0; i < rowOpts.size(); i++) {
                    Pair<Integer, List<Integer>> other = rowOpts.get(i);
                    other.getRight().remove(random);
                    if (other.getRight().isEmpty()) {
                        // remove.add(other);
                    }
                }
                // rowOpts.removeAll(remove);
            }
        }

        return key;
    }

    public static int[] createSwagmanKey(int size) {
        int[] key = new int[size * size];
        Arrays.fill(key, -1);

        List<Triple<Integer, Boolean, List<Integer>>> options = new ArrayList<>(size);

        int ite = 0;
        reset: while (ite < size) {
            // System.out.println("Runnings");
            options.clear();

            for (int col = 0; col < ite; col++) {
                options.add(new Triple<>(ite * size + col, true, getOptionsForCell(key, size, ite, col)));
            }

            for (int row = 0; row < ite; row++) {
                options.add(new Triple<>(row * size + ite, false, getOptionsForCell(key, size, row, ite)));
            }

            List<Integer> corner = getOptionsForCell(key, size, ite, ite);

            while (options.size() > 0) {

                // options.sort(Comparator.comparingInt(p -> p.getRight().size()));
                Triple<Integer, Boolean, List<Integer>> pair = options.remove(0);
                Integer random = null;

                do {
                    if (pair.getRight().isEmpty()) {
                        // System.out.println("Reset something went wrong");
                        // Something went wrong reset current row

                        boolean resetAll = true;

                        if (!resetAll) {
                            for (int r = 0; r < size / 2; r++) {
                                if (ite > 0) {
                                    for (int col = 0; col < ite; col++) {
                                        key[ite * size + col] = -1;
                                    }

                                    for (int row = 0; row < ite; row++) {
                                        key[row * size + ite] = -1;
                                    }

                                    key[ite * size + ite] = -1;
                                    ite--;
                                }
                            }
                        } else {

                            ite = 0;
                            Arrays.fill(key, -1);
                        }

                        // System.out.println("Reset something went wrong : " + ite);
                        continue reset;
                    }
                    // System.out.println("Stuck" + pair.getRight());

                    random = RandomUtil.pickRandomElement(pair.getRight());
                    final Integer test = random;

                    if (options.stream().filter(t -> t.getMiddle() == pair.getMiddle()).filter(t -> t.getRight().contains(test)).anyMatch(t -> t.getRight().size() <= 1)) {
                        pair.getRight().remove(test);
                        continue;
                    }

                    options.stream().filter(t -> t.getMiddle() == pair.getMiddle()).forEach(t -> t.getRight().remove(test));

                    if (!corner.contains(test) || corner.size() > 1) {
                        corner.remove(test);
                        break;
                    } else {
                        pair.getRight().remove(test);
                    }
                } while (true);

                final Integer sucess = random;
                key[pair.getLeft()] = random;
            }

            key[ite * size + ite] = RandomUtil.pickRandomElement(corner);
            ite++;
        }

        return key;
    }

    // Very inefficient requires much more work to be more efficient
    public static int[] createSwagmanKey3(int size) {
        while (true) {
            try {
                int[] key = new int[size * size];
                Arrays.fill(key, -1);

                while (!isFilled(key, size)) {
                    creation: for (int row = 0; row < size; row++) {
                        for (int col = 0; col < size; col++) {
                            boolean change = autoFill(key, size);
                            if (change)
                                break creation;
                            if (key[row * size + col] != -1)
                                continue;

                            List<Integer> options = getOptionsForCell(key, size, row, col);

                            Integer option = RandomUtil.pickRandomElement(options);

                            key[row * size + col] = option;
                            options.remove(option);
                        }
                    }
                }
                return key;
            } catch (Exception e) {
            } // If the key is invalid will attempt again till it gets it gets a valid key
        }
    }

    private static boolean isFilled(int[] key, int size) {
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                if (key[row * size + col] == -1)
                    return false;

        return true;
    }

    private static boolean autoFill(int[] key, int size) {
        boolean finished;
        boolean change = false;

        do {
            finished = true;
            search: for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if (key[row * size + col] != -1)
                        continue;

                    List<Integer> options = getOptionsForCell(key, size, row, col);

                    if (options.size() == 1) {
                        key[row * size + col] = options.get(0);
                        finished = false;
                        change = true;
                        break search;
                    }
                }
            }
        } while (!finished);

        return change;
    }

    private static List<Integer> getOptionsForCell(int[] key, int size, int row, int column) {
        List<Integer> validOptions = ListUtil.range(0, size - 1);

        for (int nC = 0; nC < size; nC++)
            validOptions.remove((Integer) key[row * size + nC]);

        for (int nR = 0; nR < size; nR++)
            validOptions.remove((Integer) key[nR * size + column]);

        return validOptions;
    }

    private static Integer[] createGenericGrilleKey(int size) {
        double half = size / 2D;
        int rows = (int) Math.ceil(half);
        int cols = (int) Math.floor(half);
        int keyLength = rows * cols;

        Integer[] key = new Integer[keyLength];
        int count = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                key[count++] = r * size + c;

        return key;
    }

    public static Integer[] createGrilleKey(int size) {
        Integer[] key = createGenericGrilleKey(size);

        for (int i = 0; i < key.length; i++) {
            int quadrant = RandomUtil.pickRandomInt(4);
            int value = key[i];
            for (int rot = 0; rot < quadrant; rot++) {
                int row = value / size;
                int col = value % size;
                value = col * size + (size - row - 1);
            }
            key[i] = value;
        }

        return key;
    }

    // Random key length generator

    public static String createShortKey26(int minLength, int maxLength) {
        int length = RandomUtil.pickRandomInt(minLength, maxLength);
        return createShortKey26(length);
    }

    public static String createRepeatingShortKey26(int minLength, int maxLength) {
        int length = RandomUtil.pickRandomInt(minLength, maxLength);
        return createRepeatingShortKey26(length);
    }

    public static Integer[] createOrder(int minLength, int maxLength) {
        int length = RandomUtil.pickRandomInt(minLength, maxLength);
        return createOrder(length);
    }

    public static Integer[] createGrilleKey(int minLength, int maxLength) {
        int length = RandomUtil.pickRandomInt(minLength, maxLength);
        return createGrilleKey(length);
    }

}
