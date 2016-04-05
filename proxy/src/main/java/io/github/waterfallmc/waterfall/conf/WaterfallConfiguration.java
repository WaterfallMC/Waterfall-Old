package io.github.waterfallmc.waterfall.conf;

import lombok.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Joiner;

import net.md_5.bungee.conf.Configuration;
import net.md_5.bungee.conf.YamlConfig;
import net.md_5.bungee.protocol.ProtocolConstants;

public class WaterfallConfiguration extends Configuration {

    /*
     * Throttling options
     * Helps prevent players from overloading the servers behind us
     */

    /**
     * How often players are allowed to send tab throttle.
     * Value in milliseconds.
     * <p>
     * Default is one packet per second.
     */
    private int tabThrottle = (int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);

    /**
     * Join throttle
     * Value in milliseconds.
     * <p>
     * Default is one join per-ip every 4 seconds
     */
    private int joinThrottle = (int) TimeUnit.MILLISECONDS.convert(4, TimeUnit.SECONDS);

    /**
     * If metrics is enabled
     * <p>
     * Default is true (enabled)
     */
    private boolean metrics = true;

    /**
     * Whether we log server list pings
     * <p>
     * Default is false (don't log)
     */
    private boolean logServerListPing = false;

    /**
     * The supported versions displayed to the client
     * <p>Default is a comma seperated list of supported versions. For example 1.7.x, 1.8.x, 1.9.x</p>
     */
    @Getter
    private String gameVersion;

    @Override
    public void load() {
        super.load();
        YamlConfig config = new YamlConfig(new File("waterfall.yml"));
        config.load(false); // Load, but no permissions
        // Throttling options
        tabThrottle = config.getInt("throttling.tab_complete", tabThrottle);
        joinThrottle = config.getInt("throttling.join", joinThrottle);
        metrics = config.getBoolean("metrics", metrics);
        logServerListPing = config.getBoolean("log_server_list_ping", logServerListPing);
        gameVersion = config.getString("game_version", Joiner.on(", ").join(ProtocolConstants.SUPPORTED_VERSIONS));
    }

    @Override
    public long getTabThrottle() {
        return tabThrottle;
    }

    @Override
    public long getJoinThrottle() {
        return joinThrottle;
    }

    @Override
    public boolean isMetrics() {
        return metrics;
    }

    @Override
    public boolean isLogServerListPing() {
        return logServerListPing;
    }
}
