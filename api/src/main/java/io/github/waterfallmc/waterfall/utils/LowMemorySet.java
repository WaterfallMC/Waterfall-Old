package io.github.waterfallmc.waterfall.utils;

import lombok.*;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A set that uses a <a href=>binary search</a> to find objects in a <a href=https://en.wikipedia.org/wiki/Sorted_array>sorted array</a>.
 * Avoids the memory cost of {@link java.util.HashSet}, while maintaining reasonable {@link Set#contains}
 * <b>Insertions ma O(N)!</b>
 */
public class LowMemorySet<T extends Comparable<T>> extends AbstractSet<T> implements Set<T> {
    private final List<T> backing;
    @Setter
    private boolean trimAggressively;

    protected LowMemorySet(List<T> list) {
        this.backing = checkNotNull(list, "Null list");
        this.sort(); // We have to sort any initial elements
        this.trim(true);
    }

    public static <T extends Comparable<T>> LowMemorySet<T> create() {
        return new LowMemorySet<>(new ArrayList<T>());
    }

    public static <T extends Comparable<T>> LowMemorySet<T> copyOf(Collection<T> c) {
        return new LowMemorySet<>(new ArrayList<>(c));
    }

    @SuppressWarnings("unchecked") // nope
    private int indexOf(Object o) {
        return Collections.binarySearch((List) backing, o);
    }

    private void sort() {
        backing.sort(null);
        this.trim();
    }

    private void trim() {
        trim(false);
    }

    private void trim(boolean force) {
        if (backing instanceof ArrayList && force || trimAggressively) ((ArrayList) backing).trimToSize();
    }

    @Override
    public int size() {
        return backing.size();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<T> backing = this.backing.iterator();
        return new Iterator<T>() {
            private T last;

            @Override
            public boolean hasNext() {
                return backing.hasNext();
            }

            @Override
            public T next() {
                return (last = backing.next());
            }

            @Override
            public void remove() {
                LowMemorySet.this.remove(last);
            }

            @Override
            public void forEachRemaining(Consumer<? super T> action) {
                backing.forEachRemaining(action);
            }
        };
    }

    @Override
    public Object[] toArray() {
        return backing.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return backing.toArray(a);
    }

    @Override
    public boolean add(T t) {
        if (contains(t)) return false;
        backing.add(t);
        this.sort();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        T old = backing.remove(indexOf(o));
        this.trim();
        assert old == o;
        return old != null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int oldSize = this.size();
        boolean result = backing.removeIf(c::contains);
        this.trim(oldSize - this.size() > 10);
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int oldSize = this.size();
        boolean result = backing.removeIf((o) -> !c.contains(o));
        this.trim(oldSize - this.size() > 10);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection<? extends T> c) {
        if (containsAll(c)) return false;
        backing.addAll(c);
        this.sort();
        return true;
    }

    @Override
    public void clear() {
        backing.clear();
        this.trim(true);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        backing.forEach(action);
    }

    @Override
    public Stream<T> stream() {
        return backing.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return backing.parallelStream();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        int oldSize = this.size();
        boolean worked = backing.removeIf(filter);
        this.trim(this.size() - oldSize > 10);
        return worked;
    }
}
