package net.md_5.bungee.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.*;

@RequiredArgsConstructor
public class EventHandlerMethod
{

    @Getter
    private final Object listener;
    @Getter
    private final EventExecutor executor;

    public EventHandlerMethod(Object listener, Method m) {
        this(listener, EventExecutor.create(m));
    }

    public void invoke(Object event) throws Throwable
    {
        executor.invoke( listener, event );
    }
}
