package net.md_5.bungee;

import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConnectionThrottle
{

    private final long throttleTime;
    private final Map<InetAddress, Long> throttle = new LinkedHashMap<InetAddress, Long>(100, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<InetAddress, Long> eldest) {
            return System.currentTimeMillis() - throttleTime >= eldest.getValue();
        }
    };

    public void unthrottle(InetAddress address)
    {
        throttle.remove( address );
    }

    public boolean throttle(InetAddress address)
    {
        long currentTime = System.currentTimeMillis();
        Long value = throttle.put( address, currentTime );
        return value != null && currentTime - value < throttleTime;
    }
}
