package net.md_5.bungee.event.asm;


import java.util.concurrent.atomic.AtomicInteger;

import net.md_5.bungee.event.UnsafeUtils;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

public interface ClassDefiner {

    /**
     * Returns if the defined classes can bypass access checks
     *
     * @return if classes bypass access checks
     */
    public default boolean isBypassAccessChecks() {
        return false;
    }

    /**
     * Define a class
     *
     * @param parentLoader the parent classloader
     * @param name         the name of the class
     * @param data         the class data to load
     * @return the defined class
     * @throws ClassFormatError     if the class data is invalid
     * @throws NullPointerException if any of the arguments are null
     */
    public Class<?> defineClass(ClassLoader parentLoader, Type name, byte[] data);

    public static ClassDefiner getInstance() {
        if (UnsafeUtils.isUnsafeSupported()) {
            return UnsafeClassDefiner.INSTANCE;
        } else {
            return SafeClassDefiner.INSTANCE;
        }
    }


}
