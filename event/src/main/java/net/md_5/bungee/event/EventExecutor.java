package net.md_5.bungee.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.google.common.base.Preconditions;

import net.md_5.bungee.event.asm.ASMEventExecutorGenerator;
import net.md_5.bungee.event.asm.ClassDefiner;

import org.objectweb.asm.Type;

public interface EventExecutor {

    /**
     * Invoke the method on the listener with the given event as an argument
     *
     * @param listener the listener to invoke
     * @param event    the event
     * @throws ClassCastException if the listener or event is not of an appropriate type
     * @throws Throwable          any throwable thrown by the underlying method
     */
    public void invoke(Object listener, Object event) throws Throwable;

    public static EventExecutor create(Method m) {
        Preconditions.checkNotNull(m, "Null method");
        Preconditions.checkArgument(m.getParameterCount() != 0, "Incorrect number of arguments %s", m.getParameterCount());
        ClassDefiner definer = ClassDefiner.getInstance();
        if (Modifier.isStatic(m.getModifiers())) {
            return new StaticMethodHandleEventExecutor(m);
        } else if (definer.isBypassAccessChecks() || Modifier.isPublic(m.getDeclaringClass().getModifiers()) && Modifier.isPublic(m.getModifiers())) {
            String name = ASMEventExecutorGenerator.generateName();
            byte[] classData = ASMEventExecutorGenerator.generateEventExecutor(m, name);
            Class<? extends EventExecutor> c = definer.defineClass(m.getDeclaringClass().getClassLoader(), Type.getObjectType(name), classData).asSubclass(EventExecutor.class);
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new AssertionError("Unable to initialize generated event executor", e);
            }
        } else {
            return new MethodHandleEventExecutor(m);
        }
    }
}
