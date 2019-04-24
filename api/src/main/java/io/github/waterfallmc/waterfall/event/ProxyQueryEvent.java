package io.github.waterfallmc.waterfall.event;

import io.github.waterfallmc.waterfall.QueryResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.plugin.Event;

/**
 * This event will be posted whenever a Query request is received.
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ProxyQueryEvent extends Event {
    /**
     * The listener associated with this query.
     */
    @NonNull
    private final ListenerInfo listener;
    /**
     * The query to return.
     */
    @NonNull
    private QueryResult result;
}
