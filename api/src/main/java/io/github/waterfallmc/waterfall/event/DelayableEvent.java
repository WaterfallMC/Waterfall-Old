package io.github.waterfallmc.waterfall.event;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.plugin.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * {@code DelayableEvent} is a modern successor to {@link net.md_5.bungee.api.event.AsyncEvent}.
 * <p>
 * {@code DelayableEvent} allows listeners to delay the actual completion of an event by performing certain operations
 * asynchronously instead. Each task added will be invoked in order of addition after the event has been fired. If any
 * task added throws an exception, the execution of subsequent tasks will not occur.
 */
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DelayableEvent<T> extends Event {
    /**
     * A static executor service for running delayable events.
     */
    private static final ExecutorService DELAYABLE_EXECUTOR = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Delayables Executor - #%d").build());
    /**
     * A list of all tasks to be run.
     */
    private final List<Consumer<T>> tasksToRun = new ArrayList<>();
    /**
     * Determines whether or not the event's synchronous listeners have been run.
     */
    private final AtomicBoolean firedSync = new AtomicBoolean(false);
    /**
     * The callback for this event.
     */
    private final Callback<T> callback;

    /**
     * Adds a task to run for this event.
     * @param consumer the consumer for this event
     */
    public void addTask(Consumer<T> consumer) {
        Preconditions.checkNotNull(consumer, "consumer");
        Preconditions.checkState(!firedSync.get(), "event already fired");

        tasksToRun.add(consumer);
    }

    @Override
    public void postCall() {
        // The event has been dispatched.
        if (!firedSync.compareAndSet(false, true)) {
            return;
        }

        // Do we have tasks to run?
        if (!tasksToRun.isEmpty()) {
            // We do. We'll run them in a separate thread.
            DELAYABLE_EXECUTOR.execute(() -> {
                for (Consumer<T> consumer : tasksToRun) {
                    try {
                        consumer.accept((T) this);
                    } catch (Exception e) {
                        callback.done(null, e);
                        return;
                    }
                }

                callback.done((T) this, null);
            });
        } else {
            callback.done((T) this, null);
        }
    }
}
