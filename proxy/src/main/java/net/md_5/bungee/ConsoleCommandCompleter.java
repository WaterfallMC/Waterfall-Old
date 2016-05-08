package net.md_5.bungee;

import com.google.common.collect.Iterables;
import jline.console.completer.Completer;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;

public class ConsoleCommandCompleter implements Completer
{

    private final ProxyServer proxy;

    public ConsoleCommandCompleter( ProxyServer proxy )
    {
        this.proxy = proxy;
    }

    @Override
    public int complete( String buffer, int cursor, List<CharSequence> candidates )
    {
        List<String> offers = new ArrayList<>();
        if ( !proxy.getPluginManager().dispatchCommand( proxy.getConsole(), buffer, offers ) )
        {
            return cursor;
        }

        Iterables.addAll( candidates, offers );

        final int lastSpace = buffer.lastIndexOf( ' ' );
        if ( lastSpace == -1 )
        {
            return cursor - buffer.length();
        } else
        {
            return cursor - ( buffer.length() - lastSpace - 1 );
        }
    }
}
