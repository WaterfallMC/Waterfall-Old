package net.md_5.bungee.config;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class Configuration
{

    private static final char SEPARATOR = '.';
    final Map<String, Object> self;
    private final Configuration defaults;

    public Configuration()
    {
        this( null );
    }

    public Configuration(Configuration defaults)
    {
        this( new LinkedHashMap<String, Object>(), defaults );
    }

    private Configuration getSectionFor(String path)
    {
        int index = path.indexOf( SEPARATOR );
        if ( index == -1 )
        {
            return this;
        }

        String root = path.substring( 0, index );
        Object section = self.get( root );
        if ( section == null )
        {
            section = new LinkedHashMap<>();
            self.put( root, section );
        }
        if ( section instanceof Configuration )
        {
            return (Configuration) section;
        }

        return new Configuration( (Map) section, ( defaults == null ) ? null : defaults.getSectionFor( path ) );
    }

    private String getChild(String path)
    {
        int index = path.indexOf( SEPARATOR );
        return ( index == -1 ) ? path : path.substring( index + 1 );
    }

    /*------------------------------------------------------------------------*/
    @SuppressWarnings("unchecked")
    public <T> T get(String path, T def)
    {
        Configuration section = getSectionFor( path );
        Object val;
        if ( section == this )
        {
            val = self.get( path );
        } else
        {
            val = section.get( getChild( path ), def );
        }

        return ( val != null ) ? (T) val : def;
    }

    public <T> T get(String path)
    {
        T def = getDefault( path );
        return get( path, def );
    }

    public <T> T getDefault(String path)
    {
        return ( defaults == null ) ? null : defaults.<T>get( path );
    }

    public void set(String path, Object value)
    {
        Configuration section = getSectionFor( path );
        if ( section == this )
        {
            if ( value == null )
            {
                self.remove( path );
            } else
            {
                self.put( path, value );
            }
        } else
        {
            section.set( getChild( path ), value );
        }
    }

    /*------------------------------------------------------------------------*/
    public Configuration getSection(String path)
    {
        Map<String, Object> def = getDefault( path );
        return new Configuration( ( get( path, def != null ? def : Collections.<String, Object>emptyMap() ) ), ( defaults == null ) ? null : defaults.getSection( path ) );
    }

    /**
     * Gets keys, not deep by default.
     *
     * @return top level keys for this section
     */
    public Collection<String> getKeys()
    {
        return Sets.newLinkedHashSet( self.keySet() );
    }

    /*------------------------------------------------------------------------*/
    public byte getByte(String path)
    {
        Number def = getDefault( path );
        return getByte( path, def != null ? def.byteValue() : 0 );
    }

    public byte getByte(String path, byte def)
    {
        return get( path, def );
    }

    public List<Byte> getByteList(String path)
    {
        List<Number> list = getList( path );
        List<Byte> result = new ArrayList<>( list.size() );

        for ( Number number : list )
        {
            result.add( number.byteValue() );
        }

        return result;
    }

    public short getShort(String path)
    {
        Number def = getDefault( path );
        return getShort( path, def != null ? def.shortValue() : 0 );
    }

    public short getShort(String path, short def)
    {
        Number val = get( path, def );
        return val != null ? val.shortValue() : def;
    }

    public List<Short> getShortList(String path)
    {
        List<Number> list = getList( path );
        List<Short> result = new ArrayList<>();

        for ( Number number : list )
        {
            result.add( number.shortValue() );
        }

        return result;
    }

    public int getInt(String path)
    {
        Number def = getDefault( path );
        return getInt( path, def != null ? def.intValue() : 0 );
    }

    public int getInt(String path, int def)
    {
        Number val = get( path, def );
        return val != null ? val.intValue() : def;
    }

    public List<Integer> getIntList(String path)
    {
        List<Number> list = getList( path );
        List<Integer> result = new ArrayList<>( list.size() );

        for ( Number number : list )
        {
            result.add( number.intValue() );
        }

        return result;
    }

    public long getLong(String path)
    {
        Number def = getDefault( path );
        return getLong( path, def != null ? def.longValue() : 0 );
    }

    public long getLong(String path, long def)
    {
        Number val = get( path, def );
        return val != null ? val.longValue() : def;
    }

    public List<Long> getLongList(String path)
    {
        List<Number> list = getList( path );
        List<Long> result = new ArrayList<>();

        for ( Number number : list )
        {
            result.add( number.longValue() );
        }

        return result;
    }

    public float getFloat(String path)
    {
        Object def = getDefault( path );
        return getFloat( path, ( def instanceof Number ) ? ( (Number) def ).floatValue() : 0 );
    }

    public float getFloat(String path, float def)
    {
        Number val = get( path, def );
        return val != null ? val.floatValue() : def;
    }

    public List<Float> getFloatList(String path)
    {
        List<Number> list = getList( path );
        List<Float> result = new ArrayList<>();

        for ( Number number : list )
        {
            result.add( number.floatValue() );
        }

        return result;
    }

    public double getDouble(String path)
    {
        Number def = getDefault( path );
        return getDouble( path, def != null ? def.doubleValue() : 0 );
    }

    public double getDouble(String path, double def)
    {
        Number val = get( path, def );
        return val != null ? val.doubleValue() : def;
    }

    public List<Double> getDoubleList(String path)
    {
        List<Number> list = getList( path );
        List<Double> result = new ArrayList<>( list.size() );

        for ( Number number : list )
        {
            result.add( number.doubleValue() );
        }

        return result;
    }

    public boolean getBoolean(String path)
    {
        Boolean def = getDefault( path );
        return getBoolean( path, def != null ? def : false );
    }

    public boolean getBoolean(String path, boolean def)
    {
        Boolean val = get( path, def );
        return ( val != null ) ? val : def;
    }

    public List<Boolean> getBooleanList(String path)
    {
        List<Boolean> list = getList( path );
        List<Boolean> result = new ArrayList<>();

        for ( Boolean bool : list )
        {
            result.add( bool );
        }

        return result;
    }

    public char getChar(String path)
    {
        Character def = getDefault( path );
        return getChar( path, def != null ? def : '\u0000' );
    }

    public char getChar(String path, char def)
    {
        Character val = get( path, def );
        return ( val != null ) ? val : def;
    }

    public List<Character> getCharList(String path)
    {
        List<Character> list = getList( path );
        List<Character> result = new ArrayList<>();

        for ( Character character : list )
        {
            result.add( character );
        }

        return result;
    }

    public String getString(String path)
    {
        Object def = getDefault( path );
        return getString( path, ( def instanceof String ) ? (String) def : "" );
    }

    public String getString(String path, String def)
    {
        String val = get( path, def );
        return val != null ? val : def;
    }

    public List<String> getStringList(String path)
    {
        List<String> list = getList( path );
        List<String> result = new ArrayList<>();

        for ( String string : list )
        {
            result.add( string );
        }

        return result;
    }

    /*------------------------------------------------------------------------*/
    public <T> List<T> getList(String path)
    {
        List<T> def = getDefault( path );
        return getList( path, def != null ? def : Collections.<T>emptyList() );
    }

    public <T> List<T> getList(String path, List<T> def)
    {
        return MoreObjects.firstNonNull( get( path, def ), def );
    }
}
