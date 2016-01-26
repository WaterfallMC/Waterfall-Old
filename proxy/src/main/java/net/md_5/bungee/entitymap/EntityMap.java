package net.md_5.bungee.entitymap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;

import lombok.*;

import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

/**
 * Class to rewrite integers within packets.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class EntityMap
{

    private final boolean[] clientboundInts = new boolean[ 256 ];
    private final boolean[] clientboundVarInts = new boolean[ 256 ];

    private final boolean[] serverboundInts = new boolean[ 256 ];
    private final boolean[] serverboundVarInts = new boolean[ 256 ];

    // Returns the correct entity map for the protocol version
    public static EntityMap getEntityMap(int version)
    {
        switch ( version )
        {
            case ProtocolConstants.MINECRAFT_1_7_2:
                return EntityMap_1_7_2.INSTANCE;
            case ProtocolConstants.MINECRAFT_1_7_6:
                return EntityMap_1_7_6.INSTANCE;
            case ProtocolConstants.MINECRAFT_1_8:
                return EntityMap_1_8.INSTANCE;
        }
        throw new RuntimeException( "Version " + version + " has no entity map" );
    }

    protected void addRewrite(int id, ProtocolConstants.Direction direction, boolean varint)
    {
        if ( direction == ProtocolConstants.Direction.TO_CLIENT )
        {
            if ( varint )
            {
                clientboundVarInts[ id ] = true;
            } else
            {
                clientboundInts[ id ] = true;
            }
        } else
        {
            if ( varint )
            {
                serverboundVarInts[ id ] = true;
            } else
            {
                serverboundInts[ id ] = true;
            }
        }
    }

    public final void rewriteServerbound(ByteBuf packet, int oldId, int newId)
    {
        rewrite( packet, oldId, newId, ProtocolConstants.Direction.TO_SERVER );
    }

    public final void rewriteClientbound(ByteBuf packet, int oldId, int newId)
    {
        rewrite( packet, oldId, newId, ProtocolConstants.Direction.TO_CLIENT );
    }

    protected static void rewriteInt(ByteBuf packet, int oldId, int newId, int offset)
    {
        int readId = packet.getInt( offset );
        if ( readId == oldId )
        {
            packet.setInt( offset, newId );
        } else if ( readId == newId )
        {
            packet.setInt( offset, oldId );
        }
    }

    @SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
    protected static void rewriteVarInt(ByteBuf packet, int oldId, int newId, int offset)
    {
        // Need to rewrite the packet because VarInts are variable length
        int readId = DefinedPacket.readVarInt( packet );
        int readIdLength = packet.readerIndex() - offset;
        if ( readId == oldId || readId == newId )
        {
            ByteBuf data = packet.copy();
            packet.readerIndex( offset );
            packet.writerIndex( offset );
            DefinedPacket.writeVarInt( readId == oldId ? newId : oldId, packet );
            packet.writeBytes( data );
            data.release();
        }
    }

    private final void rewrite(ByteBuf packet, int oldId, int newId, ProtocolConstants.Direction direction) {
        int readerIndex = packet.readerIndex();
        int packetId = DefinedPacket.readVarInt(packet);
        int packetIdLength = packet.readerIndex() - readerIndex;
        RewriteType rewriteType = getRewriteType(packetId, direction);
        rewriteInternal(packet, oldId, newId, direction, readerIndex, packetId, packetIdLength, rewriteType);
        packet.readerIndex(readerIndex); // Reset the reader index
    }

    protected void rewriteInternal(ByteBuf packet, int oldId, int newId, ProtocolConstants.Direction direction, int readerIndex, int packetId, int packetIdLength, RewriteType rewriteType) {
        switch (rewriteType) {
            case INT:
            rewriteInt( packet, oldId, newId, readerIndex + packetIdLength );
            break;
            case VARINT:
            rewriteVarInt( packet, oldId, newId, readerIndex + packetIdLength );
            break;
        }
    }

    protected final RewriteType getRewriteType(int packetId, ProtocolConstants.Direction direction) {
        if (packetId < 0) return RewriteType.IGNORE;
        switch (direction) {
            case TO_CLIENT:
                if (packetId < clientboundInts.length && clientboundInts[packetId]) return RewriteType.INT;
                else if (packetId < clientboundVarInts.length && clientboundVarInts[packetId]) return RewriteType.VARINT;
                else return RewriteType.IGNORE;
            case TO_SERVER:
                if (packetId < serverboundInts.length && serverboundInts[packetId]) return RewriteType.INT;
                else if (packetId < serverboundVarInts.length && serverboundVarInts[packetId]) return RewriteType.VARINT;
                else return RewriteType.IGNORE;
            default:
                throw new AssertionError("Unknown protocol direction: " + direction.name());
        }
    }

    public enum RewriteType {
        INT,
        VARINT,
        IGNORE;
    }
}
