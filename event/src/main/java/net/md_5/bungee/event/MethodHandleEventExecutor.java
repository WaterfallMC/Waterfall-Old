package net.md_5.bungee.event;

import lombok.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class MethodHandleEventExecutor implements EventExecutor {
    private final MethodHandle handle;

    public MethodHandleEventExecutor(Method m) {
        try {
            m.setAccessible(true);
            this.handle = MethodHandles.lookup().unreflect(m);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void invoke(Object listener, Object event) throws Throwable {
        try {
            handle.invoke(listener, event);
        } catch (ClassCastException | WrongMethodTypeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
