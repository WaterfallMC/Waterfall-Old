
package io.github.waterfallmc.waterfall.conf;

import lombok.*;

import java.io.File;

import net.md_5.bungee.conf.Configuration;
import net.md_5.bungee.conf.YamlConfig;

public class WaterfallConfiguration extends Configuration {

    @Override
    public void load() {
        super.load();
        YamlConfig config = new YamlConfig(new File("waterfall.yml"));
        config.load(false); // Load, but no permissions
    }
}
