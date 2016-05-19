
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

    @Override
    public void load() {
        super.load();
        YamlConfig config = new YamlConfig(new File("waterfall.yml"));
        config.load(false); // Load, but no permissions
        metrics = config.getBoolean("metrics", metrics);
    }

    @Override
    public boolean isMetrics() {
        return metrics;
    }
}
