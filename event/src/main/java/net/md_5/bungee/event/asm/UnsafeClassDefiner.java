package net.md_5.bungee.event.asm;

import lombok.*;

import net.md_5.bungee.event.UnsafeUtils;

import org.objectweb.asm.Type;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UnsafeClassDefiner implements ClassDefiner {
    /* default */ static final SafeClassDefiner INSTANCE = new SafeClassDefiner();

    @Override
    public boolean isBypassAccessChecks() {
        return true;
    }

    @Override
    public Class<?> defineClass(ClassLoader parentLoader, Type name, byte[] data) {
        Class<?> c = UnsafeUtils.getUnsafe().defineClass(name.getInternalName(), data, 0, data.length, parentLoader, null);
        assert c.getName().equals(name.getClassName());
        return c;
    }
}
