package net.md_5.bungee;

import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jline.console.completer.Completer;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

public class ConsoleCommandCompleter implements Completer
{

    private final ExecutorService executor = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder().setNameFormat( "Console Command Completer Thread - %1$d" ).build() );

    private final ProxyServer proxy;

    public ConsoleCommandCompleter( ProxyServer proxy )
    {
        this.proxy = proxy;
    }

    @Override
    public int complete( String buffer, int cursor, List<CharSequence> candidates )
    {
        Future<Iterable<String>> future = executor.submit( new Callable<Iterable<String>>()
        {
            @Override
            public Iterable<String> call() throws Exception
            {
                List<String> results = new ArrayList<>();
                proxy.getPluginManager().dispatchCommand( proxy.getConsole(), buffer, results );
                return results;
            }
        } );

        try
        {
            Iterable<String> offers = future.get();
            if ( offers == null )
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
        } catch ( ExecutionException ex )
        {
            proxy.getLogger().log( Level.WARNING, "Unhandled exception when tab completing", ex );
        } catch ( InterruptedException ex )
        {
            Thread.currentThread().interrupt();
        }

        return cursor;
    }
}
