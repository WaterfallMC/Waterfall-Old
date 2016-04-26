package io.github.waterfallmc.waterfall.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A more scalable alternative to {@link String#intern()}, whose internal hashmap doesn't to resizing.
 */
public class FastIntern {
    private static final ConcurrentMap<String, String> pool = new ConcurrentHashMap<>();

    public static String intern(String s) {
        checkNotNull(s, "Null string");
        String old = pool.putIfAbsent(s, s);
        return old != null ? old : s;
    }

    public boolean isInterned(String s) {
        return pool.containsKey(s);
    }
}
