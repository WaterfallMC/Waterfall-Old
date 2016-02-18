package io.github.waterfallmc.waterfall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
public class QueryResult {
    /**
     * The message of the day to return.
     */
    @NonNull
    private String motd;
    /**
     * The game type to return, usually SMP.
     */
    @NonNull
    private String gameType;
    /**
     * The world name to return.
     */
    @NonNull
    private String worldName;
    /**
     * The number of players currently online.
     */
    @NonNull
    private int onlinePlayers;
    /**
     * The maximum number of players that can be online.
     */
    @NonNull
    private int maxPlayers;
    /**
     * The port for this server.
     */
    @NonNull
    private int port;
    /**
     * The hostname for this server.
     */
    @NonNull
    private String address;
    /**
     * The game ID for this server, usually MINECRAFT.
     */
    @NonNull
    private String gameId;
    /**
     * The players currently online.
     */
    @NonNull
    private final List<String> players;
    /**
     * The version to return for this server.
     */
    @NonNull
    private String version;
}
