package io.github.waterfallmc.waterfall.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;

import static org.junit.Assert.*;

public class LowMemorySetTest {

    private static final ImmutableList<String> ELEMENTS = ImmutableList.of("test", "bob", "road", "food", "sleep", "sore-thought", "pain");

    @Test
    public void testContains() {
        LowMemorySet<String> set = LowMemorySet.copyOf(ELEMENTS);
        assertTrue(set.contains("test"));
        assertTrue(set.contains("bob"));
        assertFalse(set.contains("stupid"));
        assertFalse(set.contains("head"));
    }

    @Test
    public void testRemove() {
        LowMemorySet<String> set = LowMemorySet.copyOf(ELEMENTS);
        assertTrue(set.contains("test"));
        set.remove("test");
        assertFalse(set.contains("test"));
        assertTrue(set.contains("bob"));
        set.remove("bob");
        assertFalse(set.contains("bob"));
        assertTrue(ELEMENTS.size() - set.size() == 2);
        assertTrue(set.contains("road"));
        assertTrue(set.contains("food"));
        assertTrue(set.contains("pain"));
        set.removeAll(ImmutableList.of("road", "food", "pain"));
        assertFalse(set.contains("road"));
        assertFalse(set.contains("food"));
        assertFalse(set.contains("pain"));
        assertTrue(ELEMENTS.size() - set.size() == 5);
    }

    @Test
    public void testAdd() {
        LowMemorySet<String> set = LowMemorySet.copyOf(ELEMENTS);
        assertFalse(set.contains("Techcable"));
        set.add("Techcable");
        assertTrue(set.contains("Techcable"));
        set.addAll(ImmutableList.of("Techcable", "PhanaticD", "Dragonslayer293", "Aikar"));
        assertTrue(set.contains("Techcable"));
        assertTrue(set.contains("PhanaticD"));
        assertTrue(set.contains("Aikar"));
        assertFalse(set.contains("md_5"));
    }

}
