package io.github.projectwaterfall.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.plugin.Event;

import java.util.List;

/**
 * This event will be posted whenever a Query request is received.
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ProxyQueryEvent extends Event {
    /**
     * The message of the day to return.
     */
    private String motd;
    /**
     * The game type to return, usually SMP.
     */
    private String gameType;
    /**
     * The world name to return.
     */
    private String worldName;
    /**
     * The number of players currently online.
     */
    private int onlinePlayers;
    /**
     * The maximum number of players that can be online.
     */
    private int maxPlayers;
    /**
     * The port for this server.
     */
    private int port;
    /**
     * The hostname for this server.
     */
    private String address;
    /**
     * The game ID for this server, usually MINECRAFT.
     */
    private String gameId;
    /**
     * The players currently online.
     */
    private final List<String> players;
    /**
     * The version to return for this server.
     */
    private String version;
}
