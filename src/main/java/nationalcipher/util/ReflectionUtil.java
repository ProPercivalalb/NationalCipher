package nationalcipher.util;

import java.lang.reflect.Constructor;
import java.util.Optional;

public class ReflectionUtil {

    public static <T> Optional<Constructor<T>> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        Constructor<T> constructor = null;
        
        try {
            constructor = clazz.getConstructor(parameterTypes);
        } 
        catch (NoSuchMethodException e) {} 
        catch (SecurityException e) {}
        
        return Optional.ofNullable(constructor);
    }
}
