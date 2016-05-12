package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MinecraftOutput
{

    private final ByteBuf buf;

    public MinecraftOutput()
    {
        buf = Unpooled.buffer();
    }

    public byte[] toArray()
    {
        return ByteBufUtil.getBytes(buf, 0, buf.writerIndex());
    }

    public MinecraftOutput writeByte(byte b)
    {
        buf.writeByte( b );
        return this;
    }

    public void writeInt(int i)
    {
        buf.writeInt( i );
    }

    public void writeString(String s)
    {
        char[] cc = s.toCharArray();
        buf.writeShort( cc.length );
        for ( char c : cc )
        {
            buf.writeChar( c );
        }
    }

    public void writeStringUTF8WithoutLengthHeaderBecauseDinnerboneStuffedUpTheMCBrandPacket(String s)
    {
        buf.writeBytes( s.getBytes( Charset.forName( "UTF-8" ) ) );
    }
}
