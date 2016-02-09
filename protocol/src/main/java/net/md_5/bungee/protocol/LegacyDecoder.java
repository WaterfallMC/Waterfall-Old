package net.md_5.bungee.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

import com.google.common.base.Charsets;

import net.md_5.bungee.protocol.packet.LegacyHandshake;
import net.md_5.bungee.protocol.packet.LegacyPing;

public class LegacyDecoder extends ByteToMessageDecoder
{
    private boolean triedForMoreData;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if ( !in.isReadable() )
        {
            return;
        }

        in.markReaderIndex();
        short packetID = in.readUnsignedByte();

        boolean handled = false;
        try {
            if (packetID == 0xFE) {
                final LegacyPingType pingType;
                switch (in.readableBytes()) {
                    case 0:
                        pingType = LegacyPingType.ANCIENT;
                        break;
                    case 1:
                        if (in.readUnsignedByte() != 0x01) return; // Not a ping
                        pingType = LegacyPingType.v14_v15;
                        break;
                    default:
                        // Validate the 1.6 ping
                        if (in.readUnsignedByte() != 0x01) return; // Not a ping
                        if (in.readUnsignedByte() != 0xFA) return; // Invalid 1.6 ping (possibly a handshake)
                        if (!readLegacyString(in).equals("MC|PingHost")) return;
                        int lengthOfRestOfData = in.readShort();
                        int protocolVersion = in.readUnsignedByte();
                        if (protocolVersion < 73) return; // Invalid protocol version
                        String hostname = readLegacyString(in);
                        if (hostname.length() * 2 + 7 != lengthOfRestOfData) return; // Invalid length
                        int port = in.readInt();
                        if (port < 0 || port > 0xFFFF) return; // Invalid port
                        if (in.readableBytes() > 0) return; // We still have more (and shouldn't)
                        pingType = LegacyPingType.v16;
                        break;
                }
                out.add(new PacketWrapper(new LegacyPing(pingType != LegacyPingType.ANCIENT), Unpooled.EMPTY_BUFFER));
                handled = true;
            } else if (packetID == 0x02 && in.isReadable()) {
                in.skipBytes(in.readableBytes());
                out.add(new PacketWrapper(new LegacyHandshake(), Unpooled.EMPTY_BUFFER));
                handled = true;;
            }
        } catch (RuntimeException ignored) {
            handled = false;
        } finally {
            if (!handled) {
                in.resetReaderIndex();
                ctx.pipeline().remove(this);
            }
        }
    }

    public static String readLegacyString(ByteBuf buf) {
        int stringLength = buf.readShort();
        byte[] stringData = new byte[stringLength * 2];
        buf.readBytes(stringData);
        return new String(stringData, Charsets.UTF_16BE);
    }

    enum LegacyPingType {
        ANCIENT,
        v14_v15,
        v16;
    }
}
