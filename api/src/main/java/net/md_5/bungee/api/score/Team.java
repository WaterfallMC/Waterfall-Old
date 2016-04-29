package net.md_5.bungee.api.score;

import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import io.github.waterfallmc.waterfall.utils.LowMemorySet;

@Data
public class Team
{

    @NonNull
    private final String name;
    private String displayName;
    private String prefix;
    private String suffix;
    private byte friendlyFire;
    private String nameTagVisibility;
    private String collisionRule;
    private byte color;
    private Set<String> players = LowMemorySet.create(); // TODO: Consider creating a dummy set instead,
                                                         // since we don't actually use this

    public Collection<String> getPlayers()
    {
        return Collections.unmodifiableSet( players );
    }

    public void addPlayer(String name)
    {
        players.add(name.intern());
    }

    public void removePlayer(String name)
    {
        players.remove( name );
    }
}
