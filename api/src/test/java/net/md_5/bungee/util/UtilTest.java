package net.md_5.bungee.util;

import net.md_5.bungee.Util;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class UtilTest {

    private static final String FULL_UUID = "982709b9-6fd8-4627-929d-e7d3b91166ea";
    private static final String MOJANG_UUID = "982709b96fd84627929de7d3b91166ea";

    @Test
    public void testGetUUID() throws Exception {
        Assert.assertEquals(FULL_UUID, Util.getUUID(MOJANG_UUID).toString());
    }

    @Test
    public void testGetMojangUUID() throws Exception {
        assertEquals(MOJANG_UUID, Util.getMojangUUID(UUID.fromString(FULL_UUID)));
    }
}