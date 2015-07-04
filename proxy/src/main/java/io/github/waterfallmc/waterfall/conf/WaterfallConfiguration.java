package io.github.waterfallmc.waterfall.conf;

import java.io.File;

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
    private int tabThrottle = 1000;

    @Override
    public void load() {
        super.load();
        YamlConfig config = new YamlConfig(new File("waterfall.yml"));
        config.load();
        // Throttling options
        tabThrottle = config.getInt("throttling.tab_complete", tabThrottle);
    }

    @Override
    public int getTabThrottle() {
        return tabThrottle;
    }
}
