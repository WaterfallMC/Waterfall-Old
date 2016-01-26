package net.md_5.bungee.entitymap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

class EntityMap_1_7_6 extends EntityMap_1_7_2
{

    static final EntityMap_1_7_6 INSTANCE = new EntityMap_1_7_6();

    @Override
    protected void rewriteInternal(ByteBuf packet, int oldId, int newId, ProtocolConstants.Direction direction, int readerIndex, int packetId, int packetIdLength, RewriteType rewriteType) {
        super.rewriteInternal(packet, oldId, newId, direction, readerIndex, packetId, packetIdLength, rewriteType);
        if (direction == ProtocolConstants.Direction.TO_CLIENT) {
            rewriteClientbound(packet, oldId, newId, readerIndex, packetId, packetIdLength);
        }
    }

    @SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
    private void rewriteClientbound(ByteBuf packet, int oldId, int newId, int readerIndex, int packetId, int packetIdLength)
    {
        /*
        super.rewriteClientbound( packet, oldId, newId );

        int readerIndex = packet.readerIndex();
        int packetId = DefinedPacket.readVarInt( packet );
        int packetIdLength = packet.readerIndex() - readerIndex;
        */
        if ( packetId == 0x0C /* Spawn Player */ )
        {
            DefinedPacket.readVarInt( packet );
            int idLength = packet.readerIndex() - readerIndex - packetIdLength;
            String uuid = DefinedPacket.readString( packet );
            String username = DefinedPacket.readString( packet );
            int props = DefinedPacket.readVarInt( packet );
            if ( props == 0 )
            {
                UserConnection player = (UserConnection) BungeeCord.getInstance().getPlayer( username );
                if ( player != null )
                {
                    LoginResult profile = player.getPendingConnection().getLoginProfile();
                    if ( profile != null && profile.getProperties() != null
                            && profile.getProperties().length >= 1 )
                    {
                        ByteBuf rest = packet.copy();
                        packet.readerIndex( readerIndex );
                        packet.writerIndex( readerIndex + packetIdLength + idLength );
                        DefinedPacket.writeString( player.getUniqueId().toString(), packet );
                        DefinedPacket.writeString( username, packet );
                        DefinedPacket.writeVarInt( profile.getProperties().length, packet );
                        for ( LoginResult.Property property : profile.getProperties() )
                        {
                            DefinedPacket.writeString( property.getName(), packet );
                            DefinedPacket.writeString( property.getValue(), packet );
                            DefinedPacket.writeString( property.getSignature(), packet );
                        }
                        packet.writeBytes( rest );
                        rest.release();
                    }
                }
            }
        }
        packet.readerIndex( readerIndex );
    }
}
