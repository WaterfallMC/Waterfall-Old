
package io.github.waterfallmc.waterfall.conf;

import lombok.*;

import java.io.File;

import net.md_5.bungee.conf.Configuration;
import net.md_5.bungee.conf.YamlConfig;

public class WaterfallConfiguration extends Configuration {

    /**
     * If metrics is enabled
     * <p>
     * Default is true (enabled)
     */
    private boolean metrics = true;

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
        config.load(false); // Load, but no permissions
        metrics = config.getBoolean("metrics", metrics);
        // Throttling options
        tabThrottle = config.getInt("throttling.tab_complete", tabThrottle);
    }

    @Override
    public boolean isMetrics() {
        return metrics;
    }

    @Override
    public int getTabThrottle() {
        return tabThrottle;
    }
}
