package net.md_5.bungee.event.asm;

import lombok.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Preconditions;

import org.objectweb.asm.Type;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SafeClassDefiner implements ClassDefiner {
    /* default */ static final SafeClassDefiner INSTANCE = new SafeClassDefiner();

    private final ConcurrentMap<ClassLoader, GeneratedClassLoader> loaders = new ConcurrentHashMap<>();

    @Override
    public Class<?> defineClass(ClassLoader parentLoader, Type type, byte[] data) {
        GeneratedClassLoader loader = loaders.computeIfAbsent(parentLoader, GeneratedClassLoader::new);
        String name = type.getClassName();
        synchronized (loader.getClassLoadingLock(name)) {
            Preconditions.checkState(!loader.hasClass(name), "%s already defined", name);
            Class<?> c = loader.define(name, data);
            assert c.getName().equals(name);
            return c;
        }
    }

    private static class GeneratedClassLoader extends ClassLoader {
        static {
            ClassLoader.registerAsParallelCapable();
        }

        protected GeneratedClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String name, byte[] data) {
            synchronized (getClassLoadingLock(name)) {
                assert !hasClass(name);
                Class<?> c = defineClass(name, data, 0, data.length);
                resolveClass(c);
                return c;
            }
        }

        @Override
        public Object getClassLoadingLock(String name) {
            return super.getClassLoadingLock(name);
        }

        public boolean hasClass(String name) {
            synchronized (getClassLoadingLock(name)) {
                try {
                    Class.forName(name);
                    return true;
                } catch (ClassNotFoundException e) {
                    return false;
                }
            }
        }
    }
}
