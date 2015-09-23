package net.md_5.bungee.util;

import gnu.trove.strategy.HashingStrategy;

class CaseInsensitiveHashingStrategy<T> implements HashingStrategy<T>
{

    static final CaseInsensitiveHashingStrategy<String> INSTANCE = new CaseInsensitiveHashingStrategy<>();

    @Override
    public int computeHashCode(T t)
    {
        return ( t instanceof String ? (String) t : t.toString() ).toLowerCase().hashCode();
    }

    @Override
    public boolean equals(T o1, T o2)
    {
        return o1.equals( o2 ) || ( o1 instanceof String && o2 instanceof String && ( (String) o1 ).toLowerCase().equals( ( (String) o2 ).toLowerCase() ) );
    }
}
