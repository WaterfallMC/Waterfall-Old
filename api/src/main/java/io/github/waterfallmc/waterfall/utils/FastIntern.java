package io.github.waterfallmc.waterfall.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A more scalable alternative to {@link String#intern()}, whose internal hashmap doesn't to resizing.
 */
public class FastIntern {
    private static final ReferenceQueue<String> refQueue = new ReferenceQueue<>();
    private static final ConcurrentMap<InternEntry, InternEntry> pool = new ConcurrentHashMap<>();

    public static String intern(String s) {
        checkNotNull(s, "Null string");
        cleanup();
        InternEntry entry = new InternEntry(s, refQueue);
        String result;
        InternEntry existing;
        do {
            existing = pool.putIfAbsent(entry, entry);
            result = existing.get();
        } while (result == null);
        return result;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public static boolean isInterned(String s) {
        return pool.containsKey(s);
    }

    private static void cleanup() {
        Object o;
        while ((o = refQueue.poll()) != null) {
            @SuppressWarnings("unchecked")
            InternEntry entry = (InternEntry) o;
            pool.remove(entry);
        }
    }

    private static class InternEntry extends WeakReference<String> {

        public InternEntry(String referent, ReferenceQueue<? super String> q) {
            super(referent, q);
        }

        public int hashCode() {
            String value = get();
            return value == null ? 0 : value.hashCode();
        }

        @Override
        @SuppressWarnings("SimplifiableIfStatement")
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null) return false;
            if (obj instanceof InternEntry) {
                return Objects.equals(((InternEntry) obj).get(), this.get());
            } else if (obj instanceof String) {
                return obj.equals(this.get());
            } else {
                return false;
            }
        }
    }
}