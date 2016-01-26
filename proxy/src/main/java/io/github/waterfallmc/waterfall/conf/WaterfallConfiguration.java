package io.github.waterfallmc.waterfall.conf;

import java.io.File;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.conf.BungeeConfiguration;
import net.md_5.bungee.conf.YamlConfig;

public class WaterfallConfiguration extends BungeeConfiguration {

    /*
     * Throttling options
     * Helps prevent players from overloading the servers behind us
     */

    /**
     * How often players are allowed to send tab throttle.
     * Value in milliseconds.
     * <p/>
     * Default is one packet per second.
     */
    private int tabThrottle = (int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);

    /**
     * Join throttle
     * Value in milliseconds.
     * <p/>
     * Default is one join per-ip every 4 seconds
     */
    private int joinThrottle = (int) TimeUnit.MILLISECONDS.convert(4, TimeUnit.SECONDS);

    @Override
    public void load() {
        super.load();
        YamlConfig config = new YamlConfig(new File("waterfall.yml"));
        config.load();
        // Throttling options
        tabThrottle = config.getInt("throttling.tab_complete", tabThrottle);
        joinThrottle = config.getInt("throttling.join", joinThrottle);
    }

    @Override
    public long getTabThrottle() {
        return tabThrottle;
    }

    @Override
    public long getJoinThrottle() {
        return joinThrottle;
    }
}
