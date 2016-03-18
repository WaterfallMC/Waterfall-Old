package net.md_5.bungee.util;

import gnu.trove.strategy.HashingStrategy;

/*
 * The reason we can't use generics here, is because the values we compare may not be string.
 * Trove passes in non-string values, and we crash on them otherwize
 */
class CaseInsensitiveHashingStrategy implements HashingStrategy<Object>
{

    static final CaseInsensitiveHashingStrategy INSTANCE = new CaseInsensitiveHashingStrategy();

    @Override
    public int computeHashCode(Object t)
    {
        return ((String) t).toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object o1, Object o2)
    {
        if (o1 == null) return false;
        else if (o1 == o2) return true;
        else if (o1 instanceof String && o2 instanceof String) {
            return ((String) o1).equalsIgnoreCase((String) o2);
        } else {
            return o1.equals(o2);
        }
    }
}
