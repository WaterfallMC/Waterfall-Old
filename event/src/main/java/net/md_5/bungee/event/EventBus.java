package net.md_5.bungee.event;

import java.util.logging.Logger;

public class EventBus
{

    private final net.techcable.event4j.EventBus<Object, Object> event4JBus = net.techcable.event4j.EventBus.builder()
            .eventMarker((m) -> m.isAnnotationPresent(EventHandler.class) ? m.getAnnotation(EventHandler.class)::priority : null)
            .build();
    private final Logger logger;

    public EventBus()
    {
        this( null );
    }

    public EventBus(Logger logger)
    {
        this.logger = ( logger == null ) ? Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ) : logger;
    }

    public void post(Object event)
    {
        event4JBus.fire(event);
    }

    public void register(Object listener)
    {
        event4JBus.register(listener);
    }

    public void unregister(Object listener)
    {
        event4JBus.unregister(listener);
    }
}
