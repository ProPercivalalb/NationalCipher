package nationalcipher.util;

import java.util.function.Predicate;

/**
 * Provides similar methods to that off {@link java.util.stream.Stream} but 
 * without the overhead of creating a stream.
 */
public class CollectionUtil {

    public static <T> boolean allMatch(Iterable<T> collection, Predicate<T> pred) {
        for (T e : collection) {
            if (!pred.test(e)) {
                return false;
            }
        }
        return true;
    }
    
    public static <T> boolean noneMatch(Iterable<T> collection, Predicate<T> pred) {
        for (T e : collection) {
            if (pred.test(e)) {
                return false;
            }
        }
        return true;
    }
    
    public static <T> boolean anyMatch(Iterable<T> collection, Predicate<T> pred) {
        for (T e : collection) {
            if (pred.test(e)) {
                return true;
            }
        }
        return false;
    }
    
    // Exactly one match
    public static <T> boolean oneMatch(Iterable<T> collection, Predicate<T> pred) {
        boolean match = false;
        for (T e : collection) {
            if (match && pred.test(e)) {
                return false;
            } else {
                match = pred.test(e);
            }
        }
        return match;
    }
    
}
