package net.md_5.bungee.api;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChatColorTest {

    @Test
    public void testStripColor() throws Exception {
        assertEquals("Test without colors", ChatColor.stripColor("Test without colors"));
        assertEquals("Test with red", ChatColor.stripColor(ChatColor.RED + "Test with red"));
        assertEquals("Test with credential", ChatColor.stripColor("Test with " + ChatColor.RED + "credential"));
        assertEquals("Test with special formatting", ChatColor.stripColor("Test with " + ChatColor.ITALIC + "special" +
                ChatColor.RESET + " formatting"));
    }

    @Test
    public void testTranslateAlternateColorCodes() throws Exception {
        // "normal" case
        assertEquals(ChatColor.RED + "test", ChatColor.translateAlternateColorCodes('&', "&ctest"));
        // odder but still valid cases
        assertEquals(ChatColor.AQUA + "test", ChatColor.translateAlternateColorCodes('^', "^btest"));
        assertEquals(ChatColor.AQUA + "test", ChatColor.translateAlternateColorCodes('^', "^Btest"));

        // invalid case
        assertEquals("^ztest", ChatColor.translateAlternateColorCodes('^', "^ztest"));

        // should be left alone
        assertEquals("test", ChatColor.translateAlternateColorCodes('&', "test"));
    }

}