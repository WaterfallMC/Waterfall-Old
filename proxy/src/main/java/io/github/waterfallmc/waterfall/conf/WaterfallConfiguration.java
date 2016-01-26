package io.github.waterfallmc.waterfall.conf;

import java.io.File;

import net.md_5.bungee.conf.BungeeConfiguration;
import net.md_5.bungee.conf.YamlConfig;

public class WaterfallConfiguration extends BungeeConfiguration {
    @Override
    public void load() {
        super.load();
        YamlConfig config = new YamlConfig(new File("waterfall.yml"));
        config.load();
    }
}
