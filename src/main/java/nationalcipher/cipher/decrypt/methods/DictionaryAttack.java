package nationalcipher.cipher.decrypt.methods;

import java.util.List;

import javalibrary.lib.BooleanLib;
import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.transposition.RouteCipherType;
import nationalcipher.cipher.transposition.Routes;

public class DictionaryAttack {

    public static interface DictionaryKey {
        public void onKeyCreation(Character[] complete, Character[] word, int shift, boolean reversed, RouteCipherType route);
    }

    public static Character[] createLongKey(Character[] word, Character[] fullAlphabet, int shift, boolean reversed, RouteCipherType route, int width, int height) {
        int[] routeData = route.getPattern(width, height, width * height);
        Character[] untransformed = createLongKey(word, fullAlphabet, shift, reversed);
        Character[] transformed = new Character[untransformed.length];
        for (int index = 0; index < untransformed.length; index++)
            transformed[routeData[index]] = untransformed[index];

        return transformed;
    }

    /**
     * Total of fullAlphabet.length * 2 modes from the method
     * 
     * @param word
     * @param fullAlphabet
     * @param shift        Shifts the full alphabet to the left e.g "ABCDEFG" with
     *                     shift of 2 is equivalent of "FGABCDE" with shift of 0
     * @param reversed
     * @return
     */
    public static Character[] createLongKey(Character[] word, Character[] fullAlphabet, int shift, boolean reversed) {
        int length = fullAlphabet.length;

        Character[] complete = new Character[length];
        int index = 0;

        for (char cInW : word)
            if (!ArrayUtil.contains(complete, 0, index, cInW) && ArrayUtil.contains(fullAlphabet, cInW))
                complete[index++] = cInW;

        for (int i = 0; i < length; i++) {
            char cInA = fullAlphabet[(length * 2 + (reversed ? -1 : 1) * i - shift + (reversed ? -1 : 0)) % length];
            if (!ArrayUtil.contains(complete, 0, index, cInA))
                complete[index++] = cInA;

        }
        return complete;
    }

    public static void tryKeysWithOptions(DictionaryKey task, List<Character[]> words, Character[] fullAlphabet, boolean checkShift, boolean checkReverse) {
        tryKeysWithOptions(task, words, fullAlphabet, 0, 0, checkShift, checkReverse, false);
    }

    public static void tryKeysWithOptions(DictionaryKey task, List<Character[]> words, Character[] fullAlphabet, int width, int height, boolean checkShift, boolean checkReverse, boolean checkRoute) {
        if (checkShift && checkReverse && checkRoute) { // Check shift, reverse and route
            for (Character[] word : words)
                for (RouteCipherType route : Routes.getRoutes())
                    for (int s = 0; s < fullAlphabet.length; s++)
                        for (boolean r : BooleanLib.PRIMITIVE)
                            task.onKeyCreation(createLongKey(word, fullAlphabet, s, r, route, width, height), word, s, r, route);
        } else if (checkShift && checkReverse) { // Check shift and reverse
            for (Character[] word : words)
                for (int s = 0; s < fullAlphabet.length; s++)
                    for (boolean r : BooleanLib.PRIMITIVE)
                        task.onKeyCreation(createLongKey(word, fullAlphabet, s, r), word, s, r, null);
        } else if (checkShift && checkRoute) { // Check shift and route
            for (Character[] word : words)
                for (RouteCipherType route : Routes.getRoutes())
                    for (int s = 0; s < fullAlphabet.length; s++)
                        task.onKeyCreation(createLongKey(word, fullAlphabet, s, false, route, width, height), word, s, false, route);
        } else if (checkReverse && checkRoute) { // Check reverse and route
            for (Character[] word : words)
                for (RouteCipherType route : Routes.getRoutes())
                    for (boolean r : BooleanLib.PRIMITIVE)
                        task.onKeyCreation(createLongKey(word, fullAlphabet, 0, r, route, width, height), word, 0, r, route);
        } else if (checkShift) { // Only check shift
            for (Character[] word : words)
                for (int s = 0; s < fullAlphabet.length; s++)
                    task.onKeyCreation(createLongKey(word, fullAlphabet, s, false), word, s, false, null);
        } else if (checkReverse) { // Only check reverse
            for (Character[] word : words)
                for (boolean r : BooleanLib.PRIMITIVE)
                    task.onKeyCreation(createLongKey(word, fullAlphabet, 0, r), word, 0, r, null);
        } else if (checkRoute) { // Only check route
            for (Character[] word : words)
                for (RouteCipherType route : Routes.getRoutes())
                    task.onKeyCreation(createLongKey(word, fullAlphabet, 0, false, route, width, height), word, 0, false, route);
        } else // Check default only
            for (Character[] word : words)
                task.onKeyCreation(createLongKey(word, fullAlphabet, 0, false), word, 0, false, null);
    }

    /**
     * //Same method above but with no routes public static void
     * tryKeysWithOptions(DictionaryKey task, List<Character[]> words, Character[]
     * fullAlphabet, boolean checkShift, boolean checkReverse) { if(checkShift &&
     * checkReverse) { //Check both shift and reverse for(Character[] word : words)
     * for(int s = 0; s < fullAlphabet.length; s++) for(boolean r :
     * BooleanLib.PRIMITIVE) task.onKeyCreation(createLongKey(word, fullAlphabet, s,
     * r), word, s, r, null); } else if(checkShift) { //Only check shift
     * for(Character[] word : words) for(int s = 0; s < fullAlphabet.length; s++)
     * task.onKeyCreation(createLongKey(word, fullAlphabet, s, false), word, s,
     * false, null); } else if(checkReverse) { //Only check reverse for(Character[]
     * word : words) for(boolean r : BooleanLib.PRIMITIVE)
     * task.onKeyCreation(createLongKey(word, fullAlphabet, 0, r), word, 0, r,
     * null); } else //Check default only for(Character[] word : words)
     * task.onKeyCreation(createLongKey(word, fullAlphabet, 0, false), word, 0,
     * false, null); }
     **/

    public static String expressParameters(Character[] complete, Character[] word, int shift, boolean reversed, RouteCipherType route) {
        if (route == null)
            return String.format("%s [Word: %s, S:%d, B:%b]", PrimTypeUtil.toString(complete), PrimTypeUtil.toString(word), shift, reversed);

        return String.format("%s [Word: %s, S:%d, B:%b, R:%s]", PrimTypeUtil.toString(complete), PrimTypeUtil.toString(word), shift, reversed, route.getDescription());
    }
}
