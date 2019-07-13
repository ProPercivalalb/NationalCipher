package nationalcipher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ClassDiscoverer {

    private static List<String> classCache;
    
    private static Function<String, Optional<String>> GET_CLASS = name -> {
        String clazz = null;
        
        if(name.endsWith(".class")) {
            String classPath = name.substring(0, name.length() - 6);
            clazz = classPath.replaceAll("[\\|/]", ".");
        }
        
        return Optional.ofNullable(clazz);
    };
    
    public static final List<String> getClassesInPackage(@Nonnull String basePackage) {
        if(classCache == null) {
            String path = basePackage.replace('.', File.separatorChar);
            
            List<String> classes = new ArrayList<>();
            String[] classPathEntries = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
            
            for(String classpathEntry : classPathEntries) {
                if(classpathEntry.endsWith(".jar")) {
                    File jar = new File(classpathEntry);
                    JarInputStream is = null;
                    try {
                        is = new JarInputStream(new FileInputStream(jar));
                        JarEntry entry;
                        while((entry = is.getNextJarEntry()) != null) {
                            String name = entry.getName();
                            if(name.startsWith(path)) {
                                ClassDiscoverer.GET_CLASS.apply(name).ifPresent(classes::add);
                            }
                        }
                    } catch (Exception ex) {} finally {
                        try {
                            if(is != null) is.close();
                        } catch (IOException e) {}
                    }
                } else {
                    try {
                        File base = new File(classpathEntry + File.separatorChar + path);
                        traverseDirectory(base, basePackage, (file, packageName) -> {
                            ClassDiscoverer.GET_CLASS.apply(packageName + '.' + file.getName())
                                .ifPresent(classes::add);
                        });
                    } catch (Exception ex) {}
                }
            }
            classCache = classes;
        }
        
        return classCache;
    }
    
    private static void traverseDirectory(File dir, String packageName, BiConsumer<File, String> consumer) {
        for(File file : dir.listFiles()) {
            if(file.isDirectory()) {
                traverseDirectory(file, (!packageName.isEmpty() ? packageName + '.' : "") + file.getName(), consumer);
            } else if(file.isFile()) {
                consumer.accept(file, packageName);
            }
        }
    }
    
    public static <T> List<T> getInstances(String basePackage, @Nullable Class<? extends Annotation> annotationClass, Class<T> instanceClass) {
        List<T> instances = new ArrayList<>();
        List<String> classes = getClassesInPackage(basePackage);
        
        for(String className : classes) {
            try {
                Class<?> asmClass = Class.forName(className);
                if(annotationClass == null || asmClass.isAnnotationPresent(annotationClass)) {
                    if(instanceClass.isAssignableFrom(asmClass) && instanceClass != asmClass) {
                        Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
                        T instance = asmInstanceClass.newInstance();
                        instances.add(instance);
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | LinkageError e) {
                System.err.println("Failed to load: " + className + " " +  e);
            }
        }
        
        return instances;
    }
}
