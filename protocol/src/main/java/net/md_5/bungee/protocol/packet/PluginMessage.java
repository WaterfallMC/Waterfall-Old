package net.md_5.bungee.protocol.packet;

import lombok.*;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.MinecraftInput;
import net.md_5.bungee.protocol.ProtocolConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PluginMessage extends DefinedPacket
{

    public PluginMessage(String tag, ByteBuf data, boolean allowExtendedPacket) {
        this(tag, ByteBufUtil.getBytes(data), allowExtendedPacket);
    }

    private String tag;
    private byte[] data;

    public void setData(byte[] data) {
        this.data = Preconditions.checkNotNull(data, "Null data");
    }

    public void setData(ByteBuf buf) {
        Preconditions.checkNotNull(buf, "Null buffer");
        setData(ByteBufUtil.getBytes(buf));
    }

    /**
     * Allow this packet to be sent as an "extended" packet.
     */
    private boolean allowExtendedPacket = false;

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        tag = readString( buf );
        if ( protocolVersion < ProtocolConstants.MINECRAFT_1_8 )
        {
            data = readArrayLegacy( buf );
        } else
        {
        int maxSize = direction == ProtocolConstants.Direction.TO_SERVER ? Short.MAX_VALUE : 0x100000;
        Preconditions.checkArgument( buf.readableBytes() < maxSize );
        data = new byte[ buf.readableBytes() ];
        buf.readBytes( data );
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        writeString( tag, buf );
        if ( protocolVersion < ProtocolConstants.MINECRAFT_1_8 )
        {
            writeArrayLegacy( data, buf, allowExtendedPacket );
        } else
        {
            buf.writeBytes( data );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }

    public DataInput getStream()
    {
        return new DataInputStream( new ByteArrayInputStream( data ) );
    }

    public MinecraftInput getMCStream()
    {
        return new MinecraftInput( Unpooled.wrappedBuffer( data ) );
    }
}
